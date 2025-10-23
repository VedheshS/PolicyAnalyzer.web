# Privacy Policy Analyzer API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
The API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Endpoints

### Authentication Endpoints

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
    "username": "string",
    "password": "string"
}
```

**Response:**
```json
{
    "status": "success|error",
    "message": "string"
}
```

#### Login User
```http
POST /auth/login
Content-Type: application/json

{
    "username": "string",
    "password": "string"
}
```

**Response:**
```json
{
    "status": "success|error",
    "token": "jwt-token-string",
    "message": "string"
}
```

### Policy Analysis Endpoints

#### Analyze Policy Text
```http
POST /policies/analyze
Authorization: Bearer <token>
Content-Type: application/json

{
    "appName": "string",
    "policyText": "string"
}
```

**Response:**
```json
{
    "id": 1,
    "appName": "string",
    "policyText": "string",
    "riskLevel": "LOW|MEDIUM|HIGH",
    "riskScore": 0.75,
    "createdAt": "2025-10-16T09:58:26.237",
    "status": "success|error",
    "message": "string"
}
```

#### Upload Policy File
```http
POST /policies/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data

appName: string
file: file (PDF or TXT)
```

**Response:** Same as analyze policy text

#### Get User History
```http
GET /policies/history
Authorization: Bearer <token>
```

**Response:**
```json
[
    {
        "id": 1,
        "appName": "string",
        "policyText": "string",
        "riskLevel": "LOW|MEDIUM|HIGH",
        "riskScore": 0.75,
        "createdAt": "2025-10-16T09:58:26.237"
    }
]
```

#### Get Policy by ID
```http
GET /policies/{id}
Authorization: Bearer <token>
```

**Response:**
```json
{
    "id": 1,
    "appName": "string",
    "policyText": "string",
    "riskLevel": "LOW|MEDIUM|HIGH",
    "riskScore": 0.75,
    "createdAt": "2025-10-16T09:58:26.237"
}
```

#### Get Policy by App Name
```http
GET /policies/by-app?appName={appName}
Authorization: Bearer <token>
```

**Response:** Same as get policy by ID

#### Get All Policies
```http
GET /policies
Authorization: Bearer <token>
```

**Response:** Array of policy objects

### Health Check Endpoint

#### Health Check
```http
GET /health
```

**Response:**
```json
{
    "status": "UP|DOWN",
    "database": "UP|DOWN",
    "timestamp": 1697456906237,
    "service": "Privacy Policy Analyzer",
    "version": "1.0.0"
}
```

## Error Responses

All endpoints may return error responses in the following format:

```json
{
    "status": "error",
    "message": "Error description"
}
```

### Common HTTP Status Codes

- `200 OK` - Request successful
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required or invalid token
- `404 Not Found` - Resource not found
- `413 Payload Too Large` - File size exceeds limit
- `500 Internal Server Error` - Server error
- `503 Service Unavailable` - Service temporarily unavailable

## Risk Assessment

The system analyzes privacy policies using keyword-based scoring:

### Risk Levels
- **HIGH** (Score ≥ 0.5): Significant privacy concerns
- **MEDIUM** (Score ≥ 0.15): Moderate privacy concerns  
- **LOW** (Score < 0.15): Minimal privacy concerns

### Keywords and Weights
- **High Risk** (0.25 points): sell, third party, share with, advertising id, track, tracking, advertiser, ads, data broker, transfer, analytics
- **Medium Risk** (0.10 points): collect, store, retain, location, gps, device id, imei, personal information, email, phone number
- **Low Risk** (0.02 points): aggregate, anonymize, use for improvement, improve service, security, retain for troubleshooting

## Rate Limiting

Currently, no rate limiting is implemented. Consider implementing rate limiting for production use.

## File Upload Limits

- Maximum file size: 10MB
- Supported formats: PDF, TXT
- Files are processed in memory and not stored permanently

## Security Considerations

- JWT tokens expire after 10 hours
- Passwords are encrypted using BCrypt
- CORS is configured for frontend origins
- SQL injection protection through JPA
- Input validation on all endpoints