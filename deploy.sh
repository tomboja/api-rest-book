#!/bin/bash

# Build and Deploy Quarkus Book API to AWS Lambda
# This script builds the native image and deploys to AWS using SAM

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
ENVIRONMENT=${1:-dev}
AWS_REGION=${AWS_REGION:-us-east-1}
STACK_NAME="book-api-${ENVIRONMENT}"

echo -e "${BLUE}🚀 Starting deployment of Quarkus Book API to AWS Lambda${NC}"
echo -e "${BLUE}Environment: ${ENVIRONMENT}${NC}"
echo -e "${BLUE}Region: ${AWS_REGION}${NC}"
echo -e "${BLUE}Stack: ${STACK_NAME}${NC}"

# Check prerequisites
echo -e "\n${YELLOW}📋 Checking prerequisites...${NC}"

if ! command -v sam &> /dev/null; then
    echo -e "${RED}❌ AWS SAM CLI is not installed. Please install it first.${NC}"
    echo -e "${BLUE}Install instructions: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html${NC}"
    exit 1
fi

if ! command -v aws &> /dev/null; then
    echo -e "${RED}❌ AWS CLI is not installed. Please install it first.${NC}"
    exit 1
fi

if ! aws sts get-caller-identity &> /dev/null; then
    echo -e "${RED}❌ AWS credentials not configured. Please run 'aws configure' first.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Prerequisites check passed${NC}"

# Prompt for database password if not set
if [ -z "$DB_PASSWORD" ]; then
    echo -e "\n${YELLOW}🔐 Database password not set. Please enter a password for the RDS database:${NC}"
    read -s -p "Database password (min 8 characters): " DB_PASSWORD
    echo
    export DB_PASSWORD
fi

# Build native image
echo -e "\n${YELLOW}🏗️  Building native image...${NC}"
echo -e "${BLUE}This may take several minutes...${NC}"

./mvnw clean package -Dnative -Dquarkus.native.container-build=true -DskipTests

if [ ! -f "target/api-rest-book-1.0-SNAPSHOT-runner" ]; then
    echo -e "${RED}❌ Native image build failed${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Native image built successfully${NC}"

# Prepare deployment package
echo -e "\n${YELLOW}📦 Preparing deployment package...${NC}"

mkdir -p target
cp target/api-rest-book-1.0-SNAPSHOT-runner target/bootstrap
chmod +x target/bootstrap
cd target && zip -r function.zip bootstrap && cd ..

echo -e "${GREEN}✅ Deployment package ready${NC}"

# Deploy with SAM
echo -e "\n${YELLOW}🚀 Deploying to AWS...${NC}"

sam deploy \
    --template-file template.yaml \
    --stack-name "${STACK_NAME}" \
    --parameter-overrides \
        Environment="${ENVIRONMENT}" \
        DBPassword="${DB_PASSWORD}" \
    --capabilities CAPABILITY_IAM \
    --region "${AWS_REGION}" \
    --resolve-s3

if [ $? -eq 0 ]; then
    echo -e "\n${GREEN}🎉 Deployment successful!${NC}"
    
    # Get outputs
    echo -e "\n${YELLOW}📋 Getting deployment outputs...${NC}"
    
    API_URL=$(aws cloudformation describe-stacks \
        --stack-name "${STACK_NAME}" \
        --region "${AWS_REGION}" \
        --query "Stacks[0].Outputs[?OutputKey=='BookApiUrl'].OutputValue" \
        --output text)
    
    DB_ENDPOINT=$(aws cloudformation describe-stacks \
        --stack-name "${STACK_NAME}" \
        --region "${AWS_REGION}" \
        --query "Stacks[0].Outputs[?OutputKey=='DatabaseEndpoint'].OutputValue" \
        --output text)
    
    echo -e "\n${GREEN}🌐 API URL: ${API_URL}${NC}"
    echo -e "${GREEN}🗄️  Database Endpoint: ${DB_ENDPOINT}${NC}"
    
    echo -e "\n${BLUE}📝 Test your API:${NC}"
    echo -e "${BLUE}curl ${API_URL}/api/books${NC}"
    
else
    echo -e "\n${RED}❌ Deployment failed${NC}"
    exit 1
fi

echo -e "\n${GREEN}✅ Deployment complete!${NC}"