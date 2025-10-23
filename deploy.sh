#!/bin/bash

echo "Starting Privacy Policy Analyzer deployment..."

echo ""
echo "Step 1: Cleaning previous builds..."
./mvnw clean

echo ""
echo "Step 2: Running tests..."
./mvnw test
if [ $? -ne 0 ]; then
    echo "Tests failed! Deployment aborted."
    exit 1
fi

echo ""
echo "Step 3: Building application..."
./mvnw compile
if [ $? -ne 0 ]; then
    echo "Build failed! Deployment aborted."
    exit 1
fi

echo ""
echo "Step 4: Starting application..."
echo "Application will start on http://localhost:8080"
echo "Frontend should be served from src/main/frontend/ on http://localhost:5500 or http://127.0.0.1:5500"
echo ""
echo "Press Ctrl+C to stop the application"
./mvnw spring-boot:run