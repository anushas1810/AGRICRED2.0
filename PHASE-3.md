# Phase 3 — PostgreSQL persistence and demo data
- PostgreSQL service in Docker Compose.
- Farmer and Loan records use Spring Data JPA; local schema auto-update is enabled.
- Demo fixtures are explicitly marked as synthetic examples.
- Verify: `docker compose up -d postgres`, then call `/api/farmers` and `/api/loans`.
