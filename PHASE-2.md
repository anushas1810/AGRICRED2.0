# Phase 2 — Spring Boot microservices and gateway
- Java 21 / Spring Boot 3.4.5 multi-module Maven build.
- Independent auth, farmer, loan, assessment and solution services.
- Spring Cloud Gateway routes external `/api/**` calls to service ports.
- Verify: `mvn clean package -DskipTests`; start services individually.
