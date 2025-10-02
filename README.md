# API Gateway

A Spring Boot API Gateway using WebFlux and Spring Cloud Gateway. It provides secure routing, JWT authentication, SSL support, and rate limiting with Bucket4j.

## Features
- **Reactive API Gateway**: Built with Spring Cloud Gateway and WebFlux for non-blocking request handling.
- **JWT Authentication**: Secures endpoints using JSON Web Tokens.
- **SSL/TLS**: Runs on HTTPS (port 8443) with configurable keystore.
- **Rate Limiting**: Uses Bucket4j and Caffeine for efficient request limiting.
- **Service Routing**: Proxies requests to a student service (`http://localhost:8081`).

## Prerequisites
- Java 17+
- Maven

## Setup
1. Clone the repository.
2. Configure SSL and JWT secrets in `src/main/resources/application.yml` or via environment variables:
   - `SSL_KEY_STORE_PATH`, `SSL_KEY_STORE_PASSWORD`, `SSL_KEY_ALIAS`
   - `jwt.secret`, `jwt.expiration`
   - **Edit application.yml** : `server:
     port: 8443
     ssl:
     key-store: file:gateway.p12       # use classpath:gateway.p12 if placed in resources
     key-store-password: changeit
     key-store-type: PKCS12
     key-alias: gateway-ssl`
   - **Create gateway.p12** : 
     ```bash
     keytool -genkeypair -alias gateway-ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore gateway.p12 -validity 3650
     ```
3. Build and run:
   ```cmd
   mvnw.cmd clean package
   mvnw.cmd spring-boot:run
   ```

## Configuration
Edit `src/main/resources/application.yml` for:
- **SSL**: Keystore path, password, alias
- **JWT**: Secret, expiration
- **Rate Limiting**: Bucket4j capacity, refill tokens, duration
- **Service Routing**: Student service URI and path

## Usage
- The gateway runs on `https://localhost:8443`.
- JWT tokens are generated at startup (see console output).
- Requests to `/api/students/**` are proxied to the student service.
- Rate limiting is enforced per configuration.

## Project Structure
- `ApiGatewayApplication.java`: Main entry point, JWT generation
- `config/`: Gateway, Bucket4j, and security configuration
- `filter/`: Custom filters (e.g., rate limiting)
- `exceptions/`: Custom exception handling

## License
MIT

---
For more details, see the source code and configuration files.

