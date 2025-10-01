package org.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestMvc
@ActiveProfiles("test")
class OAuth2ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGoogleLogin_ShouldRedirectToOAuth2Authorization() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oauth2/authorization/google"));
    }

    @Test
    void testOAuth2Success_WithToken_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/success")
                .param("token", "test-token")
                .param("type", "Bearer"))
                .andExpect(status().isOk())
                .andExpect(content().string("OAuth2 authentication successful! Token: test-token"));
    }

    @Test
    void testOAuth2Success_WithoutToken_ShouldReturnGenericMessage() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/success"))
                .andExpect(status().isOk())
                .andExpect(content().string("OAuth2 authentication successful!"));
    }

    @Test
    void testOAuth2Error_ShouldReturnErrorMessage() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/error")
                .param("error", "access_denied")
                .param("message", "User denied access"))
                .andExpect(status().isOk())
                .andExpect(content().string("OAuth2 authentication failed. Error: access_denied, Message: User denied access"));
    }

    @Test
    void testOAuth2Error_WithoutMessage_ShouldReturnErrorOnly() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/error")
                .param("error", "server_error"))
                .andExpect(status().isOk())
                .andExpect(content().string("OAuth2 authentication failed. Error: server_error"));
    }
}