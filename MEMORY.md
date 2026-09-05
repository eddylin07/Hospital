# MEMORY.md

## Project facts

- Hospital is a medical information management system built with Java, Spring Boot/SSM-style MVC, MyBatis, FreeMarker, and Layui.
- The main SQL seed/schema file is `sql/hospital.sql`.

## Review focus

- Prior high-impact bug classes in this repository include failed-login session persistence, role authorization gaps, public admin self-registration, patient appointment IDOR, doctor workflow patient authorization, prescription inventory overdraw/race conditions, latest-seek-row prescription updates, MyBatis multi-parameter binding crashes, empty input crashes, and null PDF generation crashes.
- Role ids used by the application are admin=1, doctor=2, patient=3.

## Environment notes

- Maven may not be installed in the cloud image by default. If `mvn` is missing, install it before running tests.
- Java 21 compatibility may require updated Lombok/Surefire versions in `pom.xml`.

## Session notes

- 2026-09-05: Created this local memory file because both `AGENTS.md` and `MEMORY.md` were missing and the template files under `~/.codex/templates/` were unavailable.
- 2026-09-05: Fixed critical failed-login session persistence, role authorization, public admin self-registration, duplicate username registration binding, patient appointment IDOR/generated-key binding, doctor workflow patient authorization, prescription inventory overdraw/race/partial-update protection, latest-seek-row prescription updates, empty drug/option parsing crashes, missing appointment/seek PDF crashes, MyBatis multi-parameter binding crashes, and Maven test compatibility. `mvn test` passed with 12 tests.
