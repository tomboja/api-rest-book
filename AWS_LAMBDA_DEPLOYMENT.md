# AWS Lambda Deployment Guide for Quarkus Book API

This guide provides comprehensive instructions for deploying the Quarkus Book REST API to AWS Lambda with GraalVM native compilation and PostgreSQL RDS connectivity.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Architecture Overview](#architecture-overview)
3. [Local Development Setup](#local-development-setup)
4. [AWS Infrastructure](#aws-infrastructure)
5. [Deployment Process](#deployment-process)
6. [Configuration](#configuration)
7. [Testing](#testing)
8. [Monitoring and Troubleshooting](#monitoring-and-troubleshooting)
9. [Cost Optimization](#cost-optimization)
10. [Security Considerations](#security-considerations)

## Prerequisites

### Required Tools

1. **Java 17+** - Compatible with GraalVM
2. **Maven 3.8+** - For building the application
3. **Docker** - For native image compilation
4. **AWS CLI v2** - For AWS operations
5. **AWS SAM CLI** - For serverless application deployment
6. **GraalVM (optional)** - For local native compilation

### AWS Account Setup

1. AWS Account with appropriate permissions
2. AWS CLI configured with credentials
3. IAM permissions for Lambda, RDS, VPC, CloudFormation

### Installation Commands

```bash
# Install AWS CLI (macOS)
brew install awscli

# Install AWS CLI (Linux)
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Install SAM CLI
brew install aws-sam-cli  # macOS
# Or follow: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html

# Configure AWS credentials
aws configure
```

## Architecture Overview

### Components

1. **AWS Lambda Function** - Hosts the Quarkus native application
2. **Amazon RDS PostgreSQL** - Managed database service
3. **API Gateway** - HTTP API endpoint
4. **VPC** - Network isolation and security
5. **CloudFormation** - Infrastructure as Code

### Architecture Diagram

```
Internet → API Gateway → Lambda Function → RDS PostgreSQL
                            ↓
                      VPC (Private Subnets)
```

### Benefits

- **Cold Start Performance**: GraalVM native compilation reduces startup time from ~3s to ~50ms
- **Cost Efficiency**: Pay only for actual execution time
- **Auto Scaling**: Automatic scaling based on demand
- **Managed Infrastructure**: AWS handles server management
- **Security**: VPC isolation and security groups

## Local Development Setup

### 1. Start Local Environment

```bash
# Make scripts executable
chmod +x dev.sh deploy.sh

# Start PostgreSQL and application in dev mode
./dev.sh dev
```

This will:
- Start PostgreSQL container on port 1551
- Launch Quarkus in development mode
- Enable live reload on http://localhost:8080
- Provide Dev UI at http://localhost:8080/q/dev

### 2. Available Development Commands

```bash
./dev.sh dev        # Start development environment
./dev.sh test       # Run tests
./dev.sh build      # Build JAR package
./dev.sh native     # Build native image
./dev.sh lambda-test # Test Lambda function locally
./dev.sh stop       # Stop local environment
./dev.sh clean      # Clean up everything
```

### 3. Test API Endpoints

```bash
# Get all books
curl http://localhost:8080/api/books

# Add sample books
curl -X POST http://localhost:8080/api/books/save

# Add a new book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-1-234-56789-0",
    "title": "Test Book",
    "author": "Test Author",
    "publisher": "Test Publisher",
    "publicationYear": 2024,
    "genre": "Technology",
    "language": "English",
    "description": "A test book"
  }'

# Get book by ID
curl http://localhost:8080/api/books/1

# Update book
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-1-234-56789-0",
    "title": "Updated Test Book",
    "author": "Updated Author",
    "publisher": "Test Publisher",
    "publicationYear": 2024,
    "genre": "Technology",
    "language": "English",
    "description": "An updated test book"
  }'

# Delete book
curl -X DELETE http://localhost:8080/api/books/1
```

## AWS Infrastructure

The SAM template (`template.yaml`) creates:

### Networking
- **VPC** with public and private subnets
- **Internet Gateway** for public internet access
- **Route Tables** for network routing
- **Security Groups** for Lambda and RDS

### Database
- **RDS PostgreSQL** instance (db.t3.micro)
- **DB Subnet Group** in private subnets
- **Automatic backups** and encryption

### Compute
- **Lambda Function** with native runtime
- **VPC Configuration** for database access
- **Environment Variables** for configuration

### API
- **API Gateway** with CORS enabled
- **Custom domain** support (optional)

## Deployment Process

### 1. Build and Deploy

```bash
# Deploy to development environment
export DB_PASSWORD="your-secure-password-min-8-chars"
./deploy.sh dev

# Deploy to production environment
./deploy.sh prod
```

### 2. Manual Deployment Steps

If you prefer manual deployment:

```bash
# 1. Build native image
./mvnw clean package -Dnative -Dquarkus.native.container-build=true -DskipTests

# 2. Prepare deployment package
cp target/api-rest-book-1.0-SNAPSHOT-runner target/bootstrap
chmod +x target/bootstrap
cd target && zip -r function.zip bootstrap && cd ..

# 3. Deploy with SAM
sam deploy \\
  --template-file template.yaml \\
  --stack-name book-api-dev \\
  --parameter-overrides \\
    Environment=dev \\
    DBPassword=YourSecurePassword123 \\
  --capabilities CAPABILITY_IAM \\
  --resolve-s3
```

### 3. Environment-Specific Deployments

```bash
# Development
./deploy.sh dev

# Staging  
./deploy.sh staging

# Production
./deploy.sh prod
```

## Configuration

### Environment Variables

#### Lambda Function
- `QUARKUS_PROFILE=prod` - Activates production profile
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password  
- `DB_URL` - Database JDBC URL

#### Local Development
- `QR_DB_USERNAME=qrdbadmin` - Local DB username
- `QR_DB_PASSWORD=qradmin81` - Local DB password
- `QR_DB_URL=jdbc:postgresql://localhost:1551/books` - Local DB URL

### Application Properties

#### Development (`application.properties`)
```properties
quarkus.http.port=8080
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=${QR_DB_USERNAME:qrdbadmin}
quarkus.datasource.password=${QR_DB_PASSWORD:qradmin81}
quarkus.datasource.jdbc.url=${QR_DB_URL:jdbc:postgresql://localhost:1551/books}
```

#### Production (`application-prod.properties`)
```properties
quarkus.datasource.username=${DB_USERNAME}
quarkus.datasource.password=${DB_PASSWORD}
quarkus.datasource.jdbc.url=${DB_URL}
quarkus.lambda.handler=io.quarkus.amazon.lambda.http.LambdaHttpHandler
```

### Database Configuration

#### Connection Pool Settings
```properties
quarkus.datasource.jdbc.max-size=16
quarkus.datasource.jdbc.min-size=2
```

#### Hibernate Settings
```properties
quarkus.hibernate-orm.database.generation=update
```

## Testing

### 1. Local Testing

```bash
# Run unit tests
./mvnw test

# Test with local PostgreSQL
./dev.sh test

# Test native image locally
./dev.sh lambda-test
```

### 2. AWS Testing

After deployment, test the API:

```bash
# Get API URL from CloudFormation outputs
API_URL=$(aws cloudformation describe-stacks \\
  --stack-name book-api-dev \\
  --query "Stacks[0].Outputs[?OutputKey=='BookApiUrl'].OutputValue" \\
  --output text)

# Test API endpoints
curl $API_URL/api/books
curl -X POST $API_URL/api/books/save
```

### 3. Load Testing

Use tools like Apache Bench or Artillery:

```bash
# Simple load test
ab -n 100 -c 10 $API_URL/api/books

# Artillery load test
npm install -g artillery
artillery quick --count 50 --num 5 $API_URL/api/books
```

## Monitoring and Troubleshooting

### 1. CloudWatch Logs

```bash
# View Lambda logs
aws logs describe-log-groups --log-group-name-prefix "/aws/lambda/dev-book-api"

# Tail logs in real-time
sam logs -n BookApiFunction --stack-name book-api-dev --tail
```

### 2. Common Issues

#### Cold Start Performance
- **Symptom**: First request takes longer
- **Solution**: Native compilation reduces this significantly
- **Monitoring**: Check CloudWatch Lambda duration metrics

#### Database Connection Issues
- **Symptom**: Connection timeouts or failures
- **Solution**: Check security groups and VPC configuration
- **Debug**: Enable verbose logging

#### Memory Issues
- **Symptom**: Out of memory errors
- **Solution**: Increase Lambda memory allocation
- **Monitoring**: Check CloudWatch memory utilization

### 3. Debugging

Enable debug logging:

```properties
# In application-prod.properties
quarkus.log.level=DEBUG
quarkus.log.category."cs599.edu.miu.boja".level=DEBUG
```

## Cost Optimization

### 1. Lambda Optimization

- **Memory Allocation**: Start with 512MB, adjust based on performance
- **Timeout**: Set to 30 seconds for API operations
- **Provisioned Concurrency**: Consider for high-traffic APIs

### 2. RDS Optimization

- **Instance Size**: Start with db.t3.micro for development
- **Storage**: Use gp2 storage type
- **Backups**: 7-day retention for development, 30 days for production

### 3. Cost Monitoring

```bash
# Check AWS costs
aws ce get-cost-and-usage \\
  --time-period Start=2024-01-01,End=2024-12-31 \\
  --granularity MONTHLY \\
  --metrics BlendedCost
```

### 4. Cost Estimates

#### Development Environment
- Lambda: $0.20 per 1M requests + $0.0000166667 per GB-second
- RDS: ~$15-20/month for db.t3.micro
- API Gateway: $3.50 per million API calls
- Data Transfer: $0.09 per GB

#### Production Scaling
- Monitor CloudWatch metrics
- Use AWS Cost Explorer
- Set up billing alerts

## Security Considerations

### 1. Network Security

- **VPC**: Lambda and RDS in private subnets
- **Security Groups**: Restrict database access to Lambda only
- **NACLs**: Additional network-level security

### 2. Database Security

- **Encryption**: At-rest and in-transit encryption enabled
- **Access**: Database only accessible from Lambda
- **Credentials**: Stored in environment variables (consider AWS Secrets Manager)

### 3. API Security

#### Basic Security
- **CORS**: Configured for web applications
- **HTTPS**: Enforced through API Gateway
- **Input Validation**: Implemented in application layer

#### Enhanced Security (Optional)
```yaml
# Add to template.yaml
Auth:
  DefaultAuthorizer: AWS_IAM
  ResourcePolicy:
    CustomStatements:
      - Effect: Allow
        Principal: "*"
        Action: execute-api:Invoke
        Resource: "*"
        Condition:
          IpAddress:
            aws:SourceIp: "203.0.113.0/24"  # Your IP range
```

### 4. Secrets Management

For production, consider AWS Secrets Manager:

```bash
# Create secret
aws secretsmanager create-secret \\
  --name "book-api/database" \\
  --description "Database credentials for Book API" \\
  --secret-string '{"username":"admin","password":"SecurePassword123"}'

# Update Lambda to use Secrets Manager
# Add IAM permissions and modify application to retrieve secrets
```

## Advanced Configuration

### 1. Custom Domain

Add to `template.yaml`:

```yaml
CustomDomain:
  Type: AWS::ApiGateway::DomainName
  Properties:
    DomainName: api.yourdomain.com
    CertificateArn: !Ref SSLCertificate
```

### 2. Database Connection Pooling

For high-traffic applications:

```properties
quarkus.datasource.jdbc.max-size=20
quarkus.datasource.jdbc.min-size=5
quarkus.datasource.jdbc.initial-size=5
```

### 3. Lambda Layers

For shared dependencies:

```yaml
Layers:
  - arn:aws:lambda:us-east-1:123456789012:layer:postgresql-layer:1
```

## Cleanup

To remove all AWS resources:

```bash
# Delete CloudFormation stack
aws cloudformation delete-stack --stack-name book-api-dev

# Clean up local environment
./dev.sh clean
```

## Support and Resources

### Documentation
- [Quarkus Lambda Guide](https://quarkus.io/guides/amazon-lambda-http)
- [AWS Lambda Documentation](https://docs.aws.amazon.com/lambda/)
- [AWS SAM Documentation](https://docs.aws.amazon.com/serverless-application-model/)

### Community
- [Quarkus Community](https://quarkus.io/community/)
- [AWS Developer Forums](https://forums.aws.amazon.com/)

### Performance Benchmarks

#### Cold Start Times
- JVM Mode: ~3000ms
- Native Mode: ~50ms

#### Memory Usage
- JVM Mode: ~150MB
- Native Mode: ~50MB

#### Typical Response Times
- Simple queries: ~10-20ms
- Complex queries: ~50-100ms
- Database writes: ~20-50ms

This completes the comprehensive deployment guide for the Quarkus Book API on AWS Lambda with PostgreSQL RDS.