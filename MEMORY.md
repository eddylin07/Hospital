# Project Memory

## Project facts

- Project name: Hospital.
- Repository appears to be a Java Maven Spring Boot hospital information management system.
- Inferred stack: Spring Boot 2.0, MyBatis, Freemarker templates, PageHelper, MySQL.

## Operating notes

- No prior project-specific memory existed when initialized on 2026-09-27.
- Use Maven for compile/test validation when touching Java code.
- On 2026-09-27, critical fixes covered failed-login session persistence, role-based route authorization, public admin self-registration, patient appointment IDOR, doctor patient-ownership checks, prescription inventory overdraw/partial updates, and latest-seek prescription updates; `mvn test` passed with 7 focused tests.
