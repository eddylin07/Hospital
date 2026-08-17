# MEMORY.md

## Project facts

- Project name inferred from workspace: `workspace`.
- Maven artifact: `com.wxthxy.hospital:HospitalAction:1.0-SNAPSHOT`.
- Stack inferred from `pom.xml`: Java, Spring Boot 2.0.0.RELEASE, MyBatis, MySQL, Freemarker.

## Working notes

- `~/.codex/templates/` was not present in this environment on 2026-08-17, so `AGENTS.md` and `MEMORY.md` were initialized with inferred minimal content.
- No credential values should be stored here; record credential locations only if needed.
- On 2026-08-17, branch `cursor/critical-bug-investigation-7732` fixed critical failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR/generated-key binding, doctor-to-patient workflow authorization, prescription inventory overdraw/race/latest-seek-row updates, empty input/PDF null crashes, Linux PDF output paths, SQL prescription truncation, and Maven test compatibility. `mvn test` passed with 20 tests on Java 8 / Maven 3.8.7.
