# 🧠 MindTree User API Documentation 🚀

Welcome to the MindTree User API documentation! This guide will help you integrate with our powerful user management and content organization system. Let's build something amazing together! ✨

## 🌐 Base URL

The base URL for all API endpoints is: `http://your-server-url/user`

## 🔐 Authentication

All protected endpoints require JWT authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📡 API Endpoints

### 👤 User Management

#### Sign Up 📝
- **Endpoint**: `/signup`
- **Method**: POST
- **Description**: Create a new user account
- **Request Body**:
```json
{
    "phNo": "string",
    "name": "string",
    "pass": "string"
}
```
- **Response**: JWT token for authentication

#### Login 🔑
- **Endpoint**: `/login`
- **Method**: POST
- **Description**: Authenticate user and get JWT token
- **Request Body**:
```json
{
    "phNo": "string",
    "name": "string",
    "pass": "string"
}
```
- **Response**: JWT token for authentication

#### Logout 🚪
- **Endpoint**: `/logout`
- **Method**: POST
- **Description**: Invalidate the current JWT token
- **Headers**: Authorization (JWT token)
- **Response**: Success message

#### Get User Profile 👤
- **Endpoint**: `/profile`
- **Method**: GET
- **Description**: Get current user's profile information
- **Headers**: Authorization (JWT token)
- **Response**: User profile data

### 🔒 Password Management

#### Send OTP (Secure) 📱
- **Endpoint**: `/sendOtpSecure`
- **Method**: POST
- **Description**: Send OTP to user's phone number for password reset (requires authentication)
- **Headers**: Authorization (JWT token)
- **Response**: Success message

#### Send OTP (Unsecure) 📲
- **Endpoint**: `/sendOtp`
- **Method**: POST
- **Description**: Send OTP to user's phone number for password reset (no authentication required)
- **Query Parameters**: `phNo` (phone number)
- **Response**: Success message

#### Verify OTP ✅
- **Endpoint**: `/verify`
- **Method**: POST
- **Description**: Verify OTP code
- **Request Body**:
```json
{
    "phNo": "string",
    "otp": "string"
}
```
- **Response**: Verification status

#### Reset Password 🔄
- **Endpoint**: `/resetPassword`
- **Method**: PUT
- **Description**: Reset user password using verified OTP
- **Request Body**:
```json
{
    "phNo": "string",
    "otp": "string" // new password
}
```
- **Response**: Success message

### 📚 Content Management

#### Add Topic ➕
- **Endpoint**: `/add-topic`
- **Method**: POST
- **Description**: Create a new topic
- **Headers**: Authorization (JWT token)
- **Request Body**:
```json
{
    "name": "string"
}
```
- **Response**: Success message

#### Get Topics 📋
- **Endpoint**: `/get-topics`
- **Method**: GET
- **Description**: Get all topics for the current user
- **Headers**: Authorization (JWT token)
- **Response**: List of topics

#### Add Content 📝
- **Endpoint**: `/add-content`
- **Method**: POST
- **Description**: Add content to a topic
- **Query Parameters**:
  - `topicId`: Long (required)
  - `parentId`: Long (optional)
- **Request Body**:
```json
{
    "name": "string",
    "description": "string"
}
```
- **Response**: Success message

#### Get Contents 📖
- **Endpoint**: `/get-contents/{topicId}`
- **Method**: GET
- **Description**: Get all contents for a specific topic
- **Path Parameters**: `topicId`
- **Response**: List of contents

#### Mark Content as Completed ✅
- **Endpoint**: `/isCompleted`
- **Method**: PUT
- **Description**: Mark a content as completed
- **Query Parameters**:
  - `topicId`: Long
  - `contentId`: Long
- **Response**: Success message

#### Delete Content 🗑️
- **Endpoint**: `/delete-content`
- **Method**: DELETE
- **Description**: Delete a content (only if it has no children)
- **Query Parameters**:
  - `topicId`: Long
  - `contentId`: Long
- **Response**: Success message

#### Delete Topic 🗑️
- **Endpoint**: `/delete-topic`
- **Method**: DELETE
- **Description**: Delete a topic and all its contents
- **Query Parameters**: `topicId`: Long
- **Response**: Success message

## ⚠️ Error Handling

The API uses standard HTTP status codes:
- ✅ 200: Success
- ✅ 201: Created
- ❌ 400: Bad Request
- 🔒 401: Unauthorized
- 🚫 403: Forbidden
- 🔍 404: Not Found
- 💥 500: Internal Server Error

Error responses include a message explaining the error:
```json
{
    "message": "Error description"
}
```

## 🔒 Security Notes

1. 🔐 Always use HTTPS in production
2. 🔑 Store JWT tokens securely
3. ⚠️ Implement proper error handling
4. ✅ Validate all user inputs
5. ⏱️ Implement rate limiting for sensitive operations

## 💡 Best Practices

1. 🔄 Cache JWT tokens appropriately
2. ⚠️ Implement proper error handling and user feedback
3. ⏳ Use appropriate loading states during API calls
4. ✅ Implement proper form validation
5. 🌐 Handle network errors gracefully
6. 🔐 Implement proper session management
7. 🛡️ Use appropriate security measures for storing sensitive data

---
🚀 If you have any questions, feel free to reach out! 💬 
