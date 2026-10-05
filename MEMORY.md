# MEMORY.md

## Project facts

- Project name: Hospital.
- Inferred stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL.
- Repository includes SQL seed/schema under `sql/hospital.sql`.

## Workflow notes

- Created on 2026-10-05 because the workspace did not contain AGENTS.md or MEMORY.md and the template files were unavailable.
- Keep new memories concise and focused on architecture decisions, pitfalls, user corrections, and external resource locations.

## Security notes

- Patient self-service appointment creation must bind `appointment.patientid` from the session login's patient record, not from request JSON.

## Credentials

- Do not store credential values here. If needed, record only where a credential is configured.
