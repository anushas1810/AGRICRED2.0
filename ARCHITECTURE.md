# Architecture

```text
React + TypeScript (5173)
        |
        v
Spring Cloud API Gateway (8080)
        |--------- Auth Service (8081)
        |--------- Farmer Service (8082) ---- PostgreSQL
        |--------- Loan Service (8083) ------ PostgreSQL
        |--------- Assessment Service (8084) - deterministic scoring
        |--------- Solution Service (8085) --- solution execution records
```

Each backend service is independently deployable. This starter uses one PostgreSQL server locally, while each service owns its own schema/table namespace conceptually. For stronger service isolation, split databases and credentials per service before production.

## Domain flow
1. Officer signs in.
2. Officer views farmers and loan applications.
3. Officer runs financial and credit assessment.
4. Explainable score and factor contributions are returned.
5. Officer executes solution recommendations.
6. The dashboard displays the solution-first workflow.

## Error handling
REST controllers use Spring's standard error responses. Add a shared error contract and correlation IDs before production.
