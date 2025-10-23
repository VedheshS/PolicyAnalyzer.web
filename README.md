# Privacy Policy Analyzer

A comprehensive web application that analyzes privacy policies and provides risk assessments to help users understand data collection practices.

## Features

- **User Authentication**: Secure JWT-based authentication system
- **Policy Analysis**: Intelligent text analysis to identify privacy risks
- **File Upload**: Support for PDF and TXT file uploads
- **Risk Assessment**: Categorizes policies as LOW, MEDIUM, or HIGH risk
- **User History**: Track all analyzed policies per user
- **Interactive Dashboard**: Modern web interface with charts
- **RESTful API**: Complete REST API for all operations

## Technology Stack

### Backend
- **Java 21** with Spring Boot 3.5.6
- **Spring Security** for authentication
- **Spring Data JPA** for database operations
- **MySQL** database
- **JWT** for token-based authentication
- **Apache PDFBox** for PDF text extraction
- **Maven** for dependency management

### Frontend
- **HTML5/CSS3/JavaScript**
- **Chart.js** for data visualization
- **Responsive design**

## Prerequisites

- Java 21 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Database Setup
```sql
CREATE DATABASE privacy_analyzer;
CREATE USER 'privacy_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON privacy_analyzer.* TO 'privacy_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configuration
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/privacy_analyzer
spring.datasource.username=privacy_user
spring.datasource.password=your_password
```

### 3. Build and Run
```bash
# Clone the repository
git clone <repository-url>
cd Privacy_Policy_Analyzer

# Build the project
./mvnw clean compile

# Run tests
./mvnw test

# Start the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Frontend Setup
Open the frontend files in a web server:
- Navigate to `src/main/frontend/`
- Serve the files using a local web server (e.g., Live Server in VS Code)
- Access the application at `http://127.0.0.1:5500` or `http://localhost:5500`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### Policy Analysis
- `POST /api/policies/analyze` - Analyze policy text
- `POST /api/policies/upload` - Upload and analyze policy file
- `GET /api/policies/history` - Get user's analysis history
- `GET /api/policies/{id}` - Get specific policy by ID
- `GET /api/policies/by-app?appName={name}` - Get policy by app name

## Usage

### 1. Register/Login
- Create a new account or login with existing credentials
- JWT token will be stored in localStorage

### 2. Analyze Policy
- **Text Input**: Paste privacy policy text directly
- **File Upload**: Upload PDF or TXT files

### 3. View Results
- Risk level (LOW/MEDIUM/HIGH)
- Risk score (0.0 - 1.0)
- Visual chart representation

### 4. History
- View all previously analyzed policies
- Filter and search through history

## Risk Assessment Algorithm

The application uses keyword-based analysis to assess privacy risks:

### High Risk Keywords (0.25 points each)
- sell, third party, share with, advertising id, track, tracking, advertiser, ads, data broker, transfer, analytics

### Medium Risk Keywords (0.10 points each)
- collect, store, retain, location, gps, device id, imei, personal information, email, phone number

### Low Risk Keywords (0.02 points each)
- aggregate, anonymize, use for improvement, improve service, security, retain for troubleshooting

### Risk Levels
- **HIGH**: Score ≥ 0.5
- **MEDIUM**: Score ≥ 0.15
- **LOW**: Score < 0.15

## Security Features

- JWT-based authentication
- Password encryption using BCrypt
- CORS configuration for frontend integration
- Input validation and sanitization
- SQL injection prevention through JPA

## Project Structure

```
src/
├── main/
│   ├── java/com/example/Privacy_Policy_Analyzer/
│   │   ├── PrivacyPolicyAnalyzerApplication.java
│   │   ├── SecurityConfig.java
│   │   ├── AuthController.java
│   │   ├── PolicyController.java
│   │   ├── FileUploadController.java
│   │   ├── PrivacyAnalyzerService.java
│   │   ├── PolicyService.java
│   │   ├── PolicyServiceImpl.java
│   │   ├── JwtService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── CustomUserDetailsService.java
│   │   ├── UserPrincipal.java
│   │   ├── User.java
│   │   ├── PrivacyPolicy.java
│   │   ├── RiskLevel.java
│   │   ├── UserRepository.java
│   │   └── PrivacyPolicyRepository.java
│   ├── resources/
│   │   └── application.properties
│   └── frontend/
│       ├── index.html
│       ├── login.html
│       ├── register.html
│       ├── script.js
│       └── style.css
└── test/
    └── java/com/example/Privacy_Policy_Analyzer/
        └── PrivacyPolicyAnalyzerApplicationTests.java
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request
