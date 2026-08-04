# MEMORY.md

## Project facts

- Project: Hospital
- Initialized: 2026-08-04
- Stack inferred from repository layout: Java Spring Boot application using Maven, MyBatis, FreeMarker templates, and MySQL SQL files.
- Automation branch for this run: `cursor/critical-bug-investigation-3adb`.

## Review notes

- Keep high-severity bug investigations focused on concrete triggers: data loss/corruption, crashes, security bypasses, race conditions that lose writes, resource leaks, or major user-facing breakage.
- Persistent automation memory also contains prior security-audit notes; consult it before repeating investigation.
- On `cursor/critical-bug-investigation-3adb`, fixed failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR, prescription inventory overdraw/race, latest-seek-row updates, and Java 21 Maven test compatibility; `mvn test` passed with 11 tests on 2026-08-04.

## Credentials

- No credential values recorded. Record locations only if discovered and relevant.
