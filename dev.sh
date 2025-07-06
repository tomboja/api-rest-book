#!/bin/bash

# Local development script for Quarkus Book API
# Starts local PostgreSQL with Docker Compose and runs the application

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

ACTION=${1:-dev}

case $ACTION in
    "dev"|"start")
        echo -e "${BLUE}🚀 Starting local development environment${NC}"
        
        # Start PostgreSQL with Docker Compose
        echo -e "\n${YELLOW}🐘 Starting PostgreSQL database...${NC}"
        docker-compose up -d
        
        # Wait for PostgreSQL to be ready
        echo -e "${YELLOW}⏳ Waiting for database to be ready...${NC}"
        sleep 10
        
        # Set environment variables for local development
        export QR_DB_USERNAME=qrdbadmin
        export QR_DB_PASSWORD=qradmin81
        export QR_DB_URL=jdbc:postgresql://localhost:1551/books
        
        # Start Quarkus in dev mode
        echo -e "\n${YELLOW}🏃 Starting Quarkus in dev mode...${NC}"
        echo -e "${BLUE}Application will be available at: http://localhost:8080${NC}"
        echo -e "${BLUE}Dev UI will be available at: http://localhost:8080/q/dev${NC}"
        
        ./mvnw quarkus:dev
        ;;
        
    "test")
        echo -e "${BLUE}🧪 Running tests${NC}"
        ./mvnw test
        ;;
        
    "build")
        echo -e "${BLUE}🏗️  Building application${NC}"
        ./mvnw clean package -DskipTests
        ;;
        
    "native")
        echo -e "${BLUE}🏗️  Building native image${NC}"
        echo -e "${YELLOW}This may take several minutes...${NC}"
        ./mvnw clean package -Dnative -Dquarkus.native.container-build=true -DskipTests
        ;;
        
    "lambda-test")
        echo -e "${BLUE}🧪 Testing Lambda function locally${NC}"
        
        # Build native image if it doesn't exist
        if [ ! -f "target/api-rest-book-1.0-SNAPSHOT-runner" ]; then
            echo -e "${YELLOW}Native image not found. Building...${NC}"
            ./mvnw clean package -Dnative -Dquarkus.native.container-build=true -DskipTests
        fi
        
        # Start PostgreSQL
        echo -e "\n${YELLOW}🐘 Starting PostgreSQL database...${NC}"
        docker-compose up -d
        sleep 10
        
        # Set environment variables
        export QUARKUS_PROFILE=prod
        export DB_USERNAME=qrdbadmin
        export DB_PASSWORD=qradmin81
        export DB_URL=jdbc:postgresql://localhost:1551/books
        
        # Run native image
        echo -e "\n${YELLOW}🚀 Starting native Lambda function...${NC}"
        echo -e "${BLUE}Function will simulate Lambda environment${NC}"
        ./target/api-rest-book-1.0-SNAPSHOT-runner
        ;;
        
    "stop")
        echo -e "${BLUE}🛑 Stopping local environment${NC}"
        docker-compose down
        ;;
        
    "clean")
        echo -e "${BLUE}🧹 Cleaning up${NC}"
        docker-compose down -v
        ./mvnw clean
        ;;
        
    *)
        echo -e "${YELLOW}📋 Available commands:${NC}"
        echo -e "${BLUE}  dev|start    - Start local development environment${NC}"
        echo -e "${BLUE}  test         - Run tests${NC}"
        echo -e "${BLUE}  build        - Build JAR package${NC}"
        echo -e "${BLUE}  native       - Build native image${NC}"
        echo -e "${BLUE}  lambda-test  - Test Lambda function locally${NC}"
        echo -e "${BLUE}  stop         - Stop local environment${NC}"
        echo -e "${BLUE}  clean        - Clean up everything${NC}"
        ;;
esac