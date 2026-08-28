# Project Memory

- Hospital is a Spring Boot 2.0.0.RELEASE / MyBatis / Freemarker medical information management system.
- Maven tests should be run with JDK 8 because Lombok 1.16.22 is incompatible with newer javac module access defaults.
- Coverage automation should prioritize authentication/session handling, patient dispensing flow, inventory/price consistency, date formatting, and MyBatis mapper SQL.
- Created locally because `/home/ubuntu/.codex/templates` was unavailable on 2026-08-28.
- On branch `cursor/missing-test-coverage-3c08`, coverage tests were added for `LoginInterceptor`, `PatientServiceImpl.seek`, and prescription mapper XML statements; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 6 tests.
