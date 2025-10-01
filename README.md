# Expense Tracker API

A comprehensive REST API for managing personal expenses with Firebase Firestore backend, JWT authentication, built with Java Spring Boot.

## Features

- ✅ **JWT Authentication**: Secure login/register system
- ✅ **CRUD Operations**: Create, Read, Update, Delete expenses
- ✅ **Category Management**: Organize expenses by categories
- ✅ **User Management**: Personal expense tracking per user
- ✅ **Statistics & Analytics**: Get insights on spending patterns
- ✅ **Data Export**: Export data in JSON/CSV formats
- ✅ **Swagger Documentation**: Interactive API documentation
- ✅ **Firebase Firestore**: Cloud-based NoSQL database
- ✅ **Date Range Filtering**: Filter expenses by date ranges
- ✅ **Environment Variables**: Secure configuration management

## Technology Stack

- **Backend**: Java 17 + Spring Boot 3.1.4
- **Security**: Spring Security + JWT
- **Database**: Firebase Firestore (NoSQL)
- **Documentation**: Swagger UI (springdoc-openapi)
- **Build Tool**: Gradle
- **Libraries**: Lombok, Jackson, Firebase Admin SDK, JJWT

## Getting Started

### Prerequisites

- Java 17 or higher
- Firebase project with Firestore enabled
- Firebase Admin SDK service account key

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ExpenseTrackerAPI
   ```

2. **Configure Environment Variables**
   ```bash
   # Copy the example environment file
   cp .env.example .env
   
   # Edit .env file with your configurations
   nano .env
   ```

3. **Setup Firebase**
   - Download your Firebase Admin SDK service account key
   - Save it as `app/src/main/resources/firebase-adminsdk.json`
   - Update `FIREBASE_PROJECT_ID` in `.env` file

4. **Configure Environment Variables**
   Update `.env` file with your values:
   ```env
   # Database Configuration
   FIREBASE_PROJECT_ID=your-firebase-project-id
   FIREBASE_CREDENTIALS_PATH=classpath:firebase-adminsdk.json

   # JWT Configuration - Change this to a strong secret key
   JWT_SECRET=your-jwt-secret-key-should-be-very-long-and-secure
   JWT_EXPIRATION_MS=86400000

   # Server Configuration
   SERVER_PORT=8080
   ```

5. **Build the project**
   ```bash
   ./gradlew build
   ```

6. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The API will be available at `http://localhost:8080`

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `FIREBASE_PROJECT_ID` | Your Firebase project ID | - |
| `FIREBASE_CREDENTIALS_PATH` | Path to Firebase credentials | `classpath:firebase-adminsdk.json` |
| `JWT_SECRET` | Secret key for JWT token generation | - |
| `JWT_EXPIRATION_MS` | JWT token expiration time in milliseconds | `86400000` (24 hours) |
| `SERVER_PORT` | Server port | `8080` |
| `APP_NAME` | Application name | `expense-tracker-api` |
| `LOG_LEVEL_SECURITY` | Security logging level | `INFO` |
| `LOG_LEVEL_ROOT` | Root logging level | `INFO` |

## API Documentation

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## Authentication Flow

### 1. Register a new user
```bash
POST /api/auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

### 2. Login
```bash
POST /api/auth/login
{
  "usernameOrEmail": "john_doe",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": "user_id",
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["ROLE_USER"]
}
```

### 3. Use JWT Token
Include the JWT token in the Authorization header for protected endpoints:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user information
- `POST /api/auth/logout` - Logout user

### Expenses (Requires Authentication)
- `POST /api/expenses` - Create a new expense
- `GET /api/expenses` - Get all expenses for current user
- `GET /api/expenses/{id}` - Get expense by ID
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense
- `GET /api/expenses/category/{category}` - Get expenses by category
- `GET /api/expenses/date-range` - Get expenses by date range

### Categories (Requires Authentication)
- `POST /api/categories` - Create a new category
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category
- `POST /api/categories/initialize` - Initialize default categories

### Statistics (Requires Authentication)
- `GET /api/statistics` - Get overall statistics
- `GET /api/statistics/date-range` - Get statistics by date range

### Data Export (Requires Authentication)
- `GET /api/export/json` - Export all expenses to JSON
- `GET /api/export/json/date-range` - Export expenses by date range to JSON
- `GET /api/export/csv` - Export all expenses to CSV
- `GET /api/export/csv/date-range` - Export expenses by date range to CSV

## Security Features

- **JWT Authentication**: Stateless authentication using JSON Web Tokens
- **Password Encryption**: BCrypt hashing for password storage
- **CORS Support**: Configurable Cross-Origin Resource Sharing
- **Role-Based Access**: User roles and permissions
- **Input Validation**: Request validation and sanitization
- **Error Handling**: Comprehensive error responses

## Database Structure

### Users Collection
```
users/
  ├── {userId}/
      ├── id: string
      ├── username: string
      ├── email: string
      ├── password: string (hashed)
      ├── firstName: string
      ├── lastName: string
      ├── roles: array
      ├── enabled: boolean
      ├── createdAt: timestamp
      ├── updatedAt: timestamp
      └── lastLoginAt: timestamp
```

### Expenses Collection
```
expenses/
  ├── {expenseId}/
      ├── id: string
      ├── title: string
      ├── description: string
      ├── amount: number
      ├── category: string
      ├── date: timestamp
      ├── userId: string
      ├── createdAt: timestamp
      └── updatedAt: timestamp
```

### Categories Collection
```
categories/
  ├── {categoryId}/
      ├── id: string
      ├── name: string
      ├── description: string
      ├── color: string
      └── icon: string
```

## Deployment

### Local Development
```bash
./gradlew bootRun
```

### Production Build
```bash
./gradlew build
java -jar app/build/libs/app.jar
```

### Docker (Optional)
Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jre-slim
COPY app/build/libs/app.jar expense-tracker.jar
COPY .env .env
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "expense-tracker.jar"]
```

## Security Best Practices

1. **Change Default JWT Secret**: Always use a strong, unique JWT secret in production
2. **Environment Variables**: Never commit sensitive information to version control
3. **HTTPS**: Use HTTPS in production
4. **Firestore Rules**: Configure proper Firestore security rules
5. **Regular Updates**: Keep dependencies updated

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please open an issue in the repository or contact the development team.