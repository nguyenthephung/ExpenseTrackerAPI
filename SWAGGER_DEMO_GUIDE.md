# 🚀 Expense Tracker API - Swagger UI Demo Guide

## 📖 Quick Start with Swagger UI

### 🌐 Access Swagger UI
**URL**: http://localhost:8080/swagger-ui.html

### 🔐 Step 1: Authentication

#### Option A: JWT Authentication (Recommended for testing)

1. **Register a new account**
   - Scroll to **🔐 Authentication** section
   - Click **POST /api/auth/register**
   - Click **"Try it out"**
   - Use this sample data:
   ```json
   {
     "email": "demo@expensetracker.com",
     "password": "SecurePassword123!",
     "firstName": "Demo",
     "lastName": "User"
   }
   ```
   - Click **"Execute"**
   - You should get a 201 Created response

2. **Login to get JWT token**
   - Click **POST /api/auth/login**
   - Click **"Try it out"**
   - Use the same credentials:
   ```json
   {
     "email": "demo@expensetracker.com",
     "password": "SecurePassword123!"
   }
   ```
   - Click **"Execute"**
   - **Copy the `token` value** from the response

3. **Authorize in Swagger**
   - Click the **🔒 Authorize** button at the top of the page
   - In the "bearerAuth" field, enter: `Bearer YOUR_COPIED_TOKEN`
   - Click **"Authorize"**
   - Click **"Close"**

#### Option B: OAuth2 Google Login

1. Click **GET /api/auth/oauth2/google**
2. Click **"Try it out"** → **"Execute"**
3. Follow Google login flow
4. Extract token from redirect URL and use in Authorization

### 💳 Step 2: Create Categories

1. **Go to 🏷️ Categories section**
2. **Click POST /api/categories**
3. **Create sample categories:**

   **Food Category:**
   ```json
   {
     "name": "Food & Dining",
     "description": "Restaurant meals and groceries",
     "color": "#FF6B6B"
   }
   ```

   **Transport Category:**
   ```json
   {
     "name": "Transportation",
     "description": "Gas, public transport, uber",
     "color": "#4ECDC4"
   }
   ```

   **Entertainment Category:**
   ```json
   {
     "name": "Entertainment",
     "description": "Movies, games, subscriptions",
     "color": "#45B7D1"
   }
   ```

4. **Note the category IDs** from responses for next step

### 💰 Step 3: Add Expenses

1. **Go to 💰 Expense Management section**
2. **Click POST /api/expenses**
3. **Create sample expenses:**

   **Restaurant Expense:**
   ```json
   {
     "description": "Lunch at Italian restaurant",
     "amount": 35.50,
     "categoryId": "YOUR_FOOD_CATEGORY_ID",
     "date": "2024-01-20T12:30:00"
   }
   ```

   **Gas Expense:**
   ```json
   {
     "description": "Gas station fill-up",
     "amount": 65.00,
     "categoryId": "YOUR_TRANSPORT_CATEGORY_ID",
     "date": "2024-01-19T08:15:00"
   }
   ```

   **Movie Ticket:**
   ```json
   {
     "description": "Movie tickets for weekend",
     "amount": 28.00,
     "categoryId": "YOUR_ENTERTAINMENT_CATEGORY_ID",
     "date": "2024-01-18T19:00:00"
   }
   ```

### 📊 Step 4: View Analytics

1. **Get overall statistics:**
   - Click **GET /api/statistics/summary**
   - Execute to see total spending

2. **Monthly reports:**
   - Click **GET /api/statistics/monthly**
   - Set parameters: `year=2024`, `month=1`
   - Execute to see January 2024 spending

3. **Category breakdown:**
   - Click **GET /api/statistics/by-category**
   - Execute to see spending by category

### 📤 Step 5: Export Data

1. **Export as CSV:**
   - Click **GET /api/export/csv**
   - Set date range parameters if needed
   - Execute and download the CSV file

2. **Export as JSON:**
   - Click **GET /api/export/json**
   - Execute to see structured data export

### 🔍 Step 6: Advanced Features

1. **Search expenses:**
   - Click **GET /api/expenses/search**
   - Use query parameters to filter by:
     - Date range
     - Category
     - Amount range
     - Description keywords

2. **Update expenses:**
   - Use **PUT /api/expenses/{id}** with an expense ID
   - Modify any field

3. **Get user profile:**
   - Click **GET /api/auth/me**
   - See current user information

## 🎯 Complete Demo Workflow

```
1. Register → Login → Get Token → Authorize
2. Create 3-4 Categories → Note IDs
3. Add 5-6 Expenses using category IDs
4. Check Statistics (summary, monthly, by-category)
5. Export data (CSV/JSON)
6. Test search and filtering
7. Update/Delete some expenses
```

## 🔧 Troubleshooting

### Common Issues:

1. **401 Unauthorized Error:**
   - Make sure you're logged in and authorized
   - Check if token is properly set in Authorization header

2. **400 Bad Request:**
   - Verify JSON format is correct
   - Check required fields are provided

3. **404 Not Found:**
   - Verify you're using correct IDs for expenses/categories
   - Make sure IDs exist in your account

4. **Firebase Errors:**
   - Check server logs
   - Verify Firebase configuration

## 📝 Sample Complete Test Data

```json
// User Registration
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe"
}

// Categories
[
  { "name": "Food", "description": "Food and dining", "color": "#FF6B6B" },
  { "name": "Transport", "description": "Transportation", "color": "#4ECDC4" },
  { "name": "Health", "description": "Healthcare", "color": "#96CEB4" },
  { "name": "Entertainment", "description": "Fun activities", "color": "#FECA57" }
]

// Expenses
[
  { "description": "Starbucks coffee", "amount": 5.50, "categoryId": "food-id" },
  { "description": "Uber ride", "amount": 12.30, "categoryId": "transport-id" },
  { "description": "Doctor visit", "amount": 150.00, "categoryId": "health-id" },
  { "description": "Netflix subscription", "amount": 15.99, "categoryId": "entertainment-id" }
]
```

## 🎉 Success Indicators

✅ Successfully registered and logged in  
✅ Created multiple categories  
✅ Added various expenses  
✅ Viewed statistics and analytics  
✅ Exported data successfully  
✅ Tested OAuth2 Google login  
✅ Used search and filtering features  

---

**💡 Pro Tip**: Keep this guide open in another tab while testing the API in Swagger UI!