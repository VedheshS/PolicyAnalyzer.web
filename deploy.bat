@echo off
echo Starting Privacy Policy Analyzer deployment...

echo.
echo Step 1: Cleaning previous builds...
call mvnw clean

echo.
echo Step 2: Running tests...
call mvnw test
if %ERRORLEVEL% neq 0 (
    echo Tests failed! Deployment aborted.
    pause
    exit /b 1
)

echo.
echo Step 3: Building application...
call mvnw compile
if %ERRORLEVEL% neq 0 (
    echo Build failed! Deployment aborted.
    pause
    exit /b 1
)

echo.
echo Step 4: Starting application...
echo Application will start on http://localhost:8080
echo Frontend should be served from src/main/frontend/ on http://localhost:5500 or http://127.0.0.1:5500
echo.
echo Press Ctrl+C to stop the application
call mvnw spring-boot:run

pause