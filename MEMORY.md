# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack: SSM + Layui + Freemarker.
- Date initialized: 2026-08-31.

## Review notes

- Role ids used by the application are admin=1, doctor=2, patient=3.
- Security-sensitive areas include login/session handling, role authorization, public registration, patient appointment ownership, doctor-patient authorization, prescription inventory updates, and PDF/input null handling.
- Credential values must not be stored in memory; only record their locations when needed.

## Environment notes

- `~/.codex/templates/` was unavailable when initializing this workspace, so minimal inferred AGENTS/MEMORY files were created directly.

## 2026-08-31 critical bug investigation

- Fixed failed-login session persistence and role-bound route enforcement; failed login request bodies can no longer create authenticated sessions.
- Fixed public registration so blank/unmatched certificate ids cannot create admin accounts, and registration account-to-person binding rolls back on partial failure.
- Fixed patient appointment IDOR by binding new appointments to the patient resolved from session and using generated appointment ids instead of `MAX(id)`.
- Fixed doctor workflow patient authorization checks for diagnosis, dispensing, hospitalization, medical history, seek pages, and seek PDFs.
- Fixed prescription dispensing stock validation with transactional rollback, atomic stock guard, hydrated drug price/stock mapping, and latest-seek-row-only updates.
- Fixed empty drug/option input and missing appointment/seek PDF paths to return controlled errors instead of crashing.
- `mvn test` passed with 11 tests on 2026-08-31.
