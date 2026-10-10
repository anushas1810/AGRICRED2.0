# Phase 4 — Explainable assessment
- Financial metrics: annual net income, monthly surplus, debt-to-income (DTI), and FOIR estimate.
- Deterministic credit score with transparent factor contributions.
- Categories: A (750+), B (650–749), C (550–649), D (<550).
- Includes drought and market price risk inputs and a bounded 0–1000 score.
- Verify: POST `/api/analyze/credit-risk` with the README sample payload.
- Important: this is a demo heuristic, not a validated lending model.
