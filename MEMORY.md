# Project Memory

## Current repository facts

- Hospital is a Java Spring Boot 2.0 / MyBatis / Freemarker / Maven medical information management system.
- The repository includes SQL schema/data under `sql/hospital.sql`.
- Historical automation notes show repeated high-impact risks in auth/session handling, role authorization, appointment ownership, doctor-patient authorization, prescription inventory consistency, PDF null handling, SQL truncation, and Java/Maven compatibility.

## Investigation defaults

- For critical-bug automation, inspect recent commits first and only implement fixes when a concrete trigger can cause data loss, crashes, security bypass, or significant user-facing breakage.
- Validate code changes with focused tests when feasible; use `mvn test` for Java changes.

## 2026-10-09 findings

- `SeekMapper.updateDrugs` must update only the latest `seek` row for a patient; updating by `patientid` alone corrupts all historical prescriptions and prices for that patient.
- Login sessions must require server-hydrated `id` and `role`, and protected `/admin/**`, `/patient/**`, and doctor workflow paths must enforce roles server-side.
- Public `/regest` must never create admin accounts from blank or missing `certId`.
