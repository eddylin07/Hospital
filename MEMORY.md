# MEMORY.md

## Project Facts

- Project name: Hospital.
- Tech stack: Java, Spring Boot, Maven, JSP/static frontend assets, MyBatis-style mapper layer.
- Created for this workspace on 2026-08-07 because `~/.codex/templates/` was absent.

## Critical Bug Audit Notes

- Hospital role ids are admin=1, doctor=2, patient=3.
- High-risk recurring areas from prior automation runs:
  - failed login sessions being treated as authenticated;
  - missing server-side role authorization on `/admin/**`, `/doctor/**`, and `/patient/**`;
  - public registration creating admin users for blank or missing `certId`;
  - patient appointment creation trusting request-body `patientid`;
  - prescription dispensing allowing inventory overdraw or updating all historical seek rows;
  - null/empty inputs and missing appointment/seek data causing PDF or controller crashes.
- Patient/admin pages use `/doctor/{department}` as an authenticated doctor lookup; do not blanket-restrict every `/doctor/*` route to role 2 without preserving that lookup behavior.

## Environment Notes

- Cursor Cloud may not have Maven installed by default. If `mvn` is missing, install Maven before running tests.
- Older Lombok/Surefire versions can fail on Java 21; prior successful branches used Lombok 1.18.46 and Surefire 3.6.0-M1.
