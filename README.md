# AgriCred 3.0 — Spring Boot Microservices

Solution-first agricultural lending and farmer-protection platform.
**Tagline:** Detect → Solve → Transform.

## Stack
- Frontend: React + TypeScript + Vite
- Backend: Java 21, Spring Boot 3.4.5, Spring Cloud Gateway
- Persistence: PostgreSQL, Spring Data JPA
- API: REST/JSON
- Local orchestration: Docker Compose
- Security note: demo auth is deliberately minimal; configure JWT/OIDC before production.

## Prerequisites
- JDK 21
- Maven 3.9+
- Node.js 20+
- Docker Desktop (recommended) or local PostgreSQL

## Quick start (Windows PowerShell)
1. Start PostgreSQL:
   ```powershell
   docker compose up -d postgres
   ```
2. Build all Java services:
   ```powershell
   mvn clean package -DskipTests
   ```
3. Start each service in a separate terminal from the project root:
   ```powershell
   mvn -pl services/auth-service spring-boot:run
   mvn -pl services/farmer-service spring-boot:run
   mvn -pl services/loan-service spring-boot:run
   mvn -pl services/assessment-service spring-boot:run
   mvn -pl services/solution-service spring-boot:run
   mvn -pl services/api-gateway spring-boot:run
   ```
4. Start the frontend:
   ```powershell
   cd frontend
   npm install
   npm run dev
   ```
5. Open the Vite URL shown in the terminal (normally http://localhost:5173).

## Service ports
| Service | Port | Gateway prefix |
|---|---:|---|
| API Gateway | 8080 | `/api/**` |
| Auth | 8081 | `/api/auth/**` |
| Farmers | 8082 | `/api/farmers/**` |
| Loans | 8083 | `/api/loans/**` |
| Assessment | 8084 | `/api/analyze/**` |
| Solutions | 8085 | `/api/solutions/**` |

Health endpoints: `/actuator/health` on each service.

## Demo API examples
```powershell
Invoke-RestMethod http://localhost:8080/api/farmers
Invoke-RestMethod http://localhost:8080/api/loans
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/analyze/credit-risk -ContentType "application/json" -Body '{"annualIncome":360000,"annualExpenses":240000,"existingDebt":60000,"requestedLoan":500000,"tenureMonths":12,"onTimeLoans":2,"incomeVariancePct":12,"landOwned":true,"irrigationAvailable":true,"cropDiversification":true,"insuranceActive":false,"droughtRisk":25,"priceCrashRisk":false,"existingDefault":false}'
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/solutions/DEMO-LOAN/execute -ContentType "application/json" -Body '{"farmerId":"DEMO-FARMER"}'
```

## Database
Local defaults are in `docker-compose.yml`. For local non-Docker PostgreSQL, set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` in each service environment. Tables are created with `ddl-auto=update` for local demo convenience. Use Flyway/Liquibase migrations and `validate` in production.

## Seven phases
See `docs/phases/PHASE-1.md` through `PHASE-7.md`. Each phase lists implementation and verification steps.

## Important limitations
- The included seed farmers and sample values are demo fixtures, not verified real borrower records.
- Risk scoring is deterministic and explainable; it is not a trained or validated ML model and must not be used as the sole basis for real lending decisions.
- Scheme, weather and mandi integrations return clearly labeled mock data until real providers are configured.
- The UI and API scaffold are intended for local development. Before production, add validated JWT/OIDC security, authorization policies, audit logging, migrations, rate limiting, secrets management, privacy controls, and independent security/testing review.
