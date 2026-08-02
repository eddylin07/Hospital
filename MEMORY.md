# MEMORY.md

## Project facts

- Project: Hospital.
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven, MySQL.
- Repository root: `/workspace`.

## Investigation notes

- Daily critical-bug automation should inspect recent behavior-changing commits and only fix issues with a concrete high-impact trigger.
- Known recurring high-impact areas from automation memory: failed-login session persistence, missing role authorization, public admin self-registration, prescription inventory races/overdraw, patient appointment IDOR, and null/empty input crashes in PDF or prescription flows.
- On `cursor/critical-bug-investigation-b628`, fixed failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR, prescription inventory overdraw/race/latest-seek-row updates, empty drug/option input crashes, null seek printing, and Java 21 Maven test compatibility; `mvn test` passed with 10 tests on 2026-08-02 after installing Maven.
