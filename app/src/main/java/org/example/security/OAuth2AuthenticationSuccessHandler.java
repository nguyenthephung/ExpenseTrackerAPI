package org.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AuthResponse;
import org.example.dto.AuthResponse;
import org.example.service.OAuth2UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final OAuth2UserService oAuth2UserService;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;
    
    @Value("${app.oauth2.authorized-redirect-uris:http://localhost:3000/oauth2/redirect}")
    private String redirectUri;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {
        
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to target URL");
            return;
        }
        
        String targetUrl = determineTargetUrl(request, response, authentication);
        
        if (targetUrl.contains("token=")) {
            // Redirect to frontend with token
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // Return JSON response (for API clients)
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(targetUrl);
        }
    }
    
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, 
                                      Authentication authentication) {
        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            log.info("OAuth2 user principal: {}", oauth2User.getName());
            
            // Process OAuth2 user and get user response
            AuthResponse userResponse = oAuth2UserService.processOAuth2User(oauth2User);
            log.info("OAuth2 user processed successfully: {}", userResponse.getEmail());
            
            // Generate JWT token
            String token = jwtUtils.generateJwtToken(userResponse.getId());
            log.info("JWT token generated successfully for user: {}", userResponse.getId());
            
            // Check if this is an API request
            String acceptHeader = request.getHeader("Accept");
            if (acceptHeader != null && acceptHeader.contains("application/json")) {
                // Return JSON response for API clients
                AuthResponse authResponse = new AuthResponse(
                        token, "Bearer", userResponse.getId(), userResponse.getEmail(),
                        userResponse.getFirstName(), userResponse.getLastName(), userResponse.getRoles()
                );
                
                return objectMapper.writeValueAsString(authResponse);
            } else {
                // Redirect to frontend with token in URL
                String redirectUrl = UriComponentsBuilder.fromUriString(redirectUri)
                        .queryParam("token", token)
                        .queryParam("type", "Bearer")
                        .build().toUriString();
                log.info("Redirecting to: {}", redirectUrl);
                return redirectUrl;
            }
            
        } catch (Exception e) {
            log.error("Error processing OAuth2 authentication", e);
            String errorUrl = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("error", "authentication_failed")
                    .queryParam("message", e.getMessage())
                    .build().toUriString();
            log.error("Redirecting to error URL: {}", errorUrl);
            return errorUrl;
        }
    }
    
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);
        
        return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort();
    }
}