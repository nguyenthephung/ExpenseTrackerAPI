# Expense Tracker API

A comprehensive REST API for managing personal expenses with Firebase Firestore backend, built with Java Spring Boot.

## Features

- ✅ **CRUD Operations**: Create, Read, Update, Delete expenses
- ✅ **Category Management**: Organize expenses by categories
- ✅ **Statistics & Analytics**: Get insights on spending patterns
- ✅ **Data Export**: Export data in JSON/CSV formats
- ✅ **Swagger Documentation**: Interactive API documentation
- ✅ **Firebase Firestore**: Cloud-based NoSQL database
- ✅ **Date Range Filtering**: Filter expenses by date ranges

## Technology Stack

- **Backend**: Java 17 + Spring Boot 3.1.4
- **Database**: Firebase Firestore (NoSQL)
- **Documentation**: Swagger UI (springdoc-openapi)
- **Build Tool**: Gradle
- **Libraries**: Lombok, Jackson, Firebase Admin SDK

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

2. **Configure Firebase**
   - Replace `src/main/resources/firebase-adminsdk.json` with your Firebase service account key
   - Update the project ID in the configuration if needed

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The API will be available at `http://localhost:8080`

## API Documentation

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## API Endpoints

### Expenses
- `POST /api/expenses` - Create a new expense
- `GET /api/expenses` - Get all expenses
- `GET /api/expenses/{id}` - Get expense by ID
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense
- `GET /api/expenses/category/{category}` - Get expenses by category
- `GET /api/expenses/date-range` - Get expenses by date range

### Categories
- `POST /api/categories` - Create a new category
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category
- `POST /api/categories/initialize` - Initialize default categories

### Statistics
- `GET /api/statistics` - Get overall statistics
- `GET /api/statistics/date-range` - Get statistics by date range

### Data Export
- `GET /api/export/json` - Export all expenses to JSON
- `GET /api/export/json/date-range` - Export expenses by date range to JSON
- `GET /api/export/csv` - Export all expenses to CSV
- `GET /api/export/csv/date-range` - Export expenses by date range to CSV

## Sample Requests

### Create an Expense
```json
POST /api/expenses
{
  "title": "Lunch at Restaurant",
  "description": "Business lunch meeting",
  "amount": 45.50,
  "category": "Food & Dining",
  "date": "2024-01-15T12:30:00",
  "userId": "user123"
}
```

### Create a Category
```json
POST /api/categories
{
  "name": "Travel",
  "description": "Travel and accommodation expenses",
  "color": "#FF6B6B",
  "icon": "✈️"
}
```

## Default Categories

The application comes with 10 pre-defined categories:
- 🍽️ Food & Dining
- 🚗 Transportation
- 🛒 Shopping
- 🎬 Entertainment
- 💡 Bills & Utilities
- 🏥 Healthcare
- 📚 Education
- ✈️ Travel
- 💄 Personal Care
- 📝 Other

## Database Structure

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

## Configuration

### application.yml
```yaml
server:
  port: 8080

spring:
  application:
    name: expense-tracker-api

firebase:
  credentials-path: classpath:firebase-adminsdk.json

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: method
```

## Error Handling

The API includes comprehensive error handling with proper HTTP status codes:

- `400 Bad Request` - Validation errors or invalid input
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server errors

Example error response:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Expense not found with id: invalid-id"
}
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
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "expense-tracker.jar"]
```

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