# MEMORY.md

## Project facts

- Project: Hospital.
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven, MySQL.
- Repository root: `/workspace`.

## Investigation notes

- Daily critical-bug automation should inspect recent behavior-changing commits and only fix issues with a concrete high-impact trigger.
- Known recurring high-impact areas from automation memory: failed-login session persistence, missing role authorization, public admin self-registration, prescription inventory races/overdraw, patient appointment IDOR, and null/empty input crashes in PDF or prescription flows.
