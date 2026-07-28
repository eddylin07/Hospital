# Project Memory

## Project Facts

- Project name: Hospital.
- Technology stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL.
- Repository purpose: medical information management system.

## Automation Context

- Critical-bug investigation automation should only fix and open a PR when a concrete high-severity trigger is confirmed.
- Previous recurring critical areas in this repository include login session handling, role authorization, public registration privilege creation, prescription inventory updates, seek-row targeting, appointment ownership, PDF null handling, empty input parsing, and Java 21 Maven test compatibility.
- Cursor Cloud may lack Maven; install Maven before running `mvn test` if needed.
- On `cursor/critical-bug-investigation-cb53`, fixed failed-login session persistence, server-side role authorization, public admin self-registration, prescription inventory overdraw/latest-seek-row updates, patient appointment IDOR, empty input/PDF null crashes, and Java 21 Maven test compatibility; `mvn test` passed with 11 tests on 2026-07-28 after installing Maven.

