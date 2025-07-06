# api-rest-book

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## 🚀 AWS Lambda Deployment

This application is now ready for deployment to AWS Lambda with GraalVM native compilation and PostgreSQL RDS connectivity.

### Quick Start

```bash
# Local development
./dev.sh dev

# Deploy to AWS Lambda
export DB_PASSWORD="your-secure-password"
./deploy.sh dev
```

For comprehensive deployment instructions, see [AWS_LAMBDA_DEPLOYMENT.md](AWS_LAMBDA_DEPLOYMENT.md).

### Features

- ⚡ **Native Compilation**: GraalVM native image for fast cold starts (~50ms)
- 🌐 **AWS Lambda**: Serverless deployment with automatic scaling
- 🗄️ **PostgreSQL RDS**: Managed database service
- 🔧 **Infrastructure as Code**: AWS SAM template for complete infrastructure
- 📊 **Monitoring**: CloudWatch integration for logs and metrics
- 🛡️ **Security**: VPC isolation and proper security groups

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

Or using the development script:

```shell script
./dev.sh dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it's not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/api-rest-book-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## API Endpoints

### Books API

- `GET /api/books` - Get all books
- `POST /api/books` - Create a new book
- `GET /api/books/{id}` - Get book by ID
- `PUT /api/books/{id}` - Update book by ID
- `DELETE /api/books/{id}` - Delete book by ID
- `POST /api/books/save` - Save sample books

### Database Connection Test

- `GET /testdb/connection` - Test database connectivity

### Example Usage

```bash
# Get all books
curl http://localhost:8080/api/books

# Add sample books
curl -X POST http://localhost:8080/api/books/save

# Create a new book
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
```

## Development Scripts

This project includes helpful development scripts:

```bash
./dev.sh dev        # Start development environment with PostgreSQL
./dev.sh test       # Run tests
./dev.sh build      # Build JAR package
./dev.sh native     # Build native image
./dev.sh lambda-test # Test Lambda function locally
./dev.sh stop       # Stop local environment
./dev.sh clean      # Clean up everything

./deploy.sh dev     # Deploy to AWS Lambda (development)
./deploy.sh prod    # Deploy to AWS Lambda (production)
```

## Database Configuration

### Local Development
The application uses PostgreSQL in development. Start it with:

```bash
docker-compose up -d
```

### AWS RDS
For AWS deployment, the application connects to a PostgreSQL RDS instance automatically created by the SAM template.

## Environment Variables

### Local Development
- `QR_DB_USERNAME` - Database username (default: qrdbadmin)
- `QR_DB_PASSWORD` - Database password (default: qradmin81)
- `QR_DB_URL` - Database URL (default: jdbc:postgresql://localhost:1551/books)

### AWS Lambda
- `DB_USERNAME` - RDS database username
- `DB_PASSWORD` - RDS database password
- `DB_URL` - RDS database URL (auto-generated)

## Related Guides

- RESTEasy Classic JSON-B ([guide](https://quarkus.io/guides/rest-json)): JSON-B serialization support for RESTEasy Classic
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- AWS Lambda HTTP ([guide](https://quarkus.io/guides/amazon-lambda-http)): Write Lambda functions for Amazon API Gateway

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)