# Project Memory

## Project facts
- Project name: workspace
- Date initialized: 2026-07-28
- Stack: Java Maven project using Spring Boot 2.0, MyBatis, Freemarker, Lombok 1.16.22, MySQL connector.
- Source package root: `com.hospital`.

## Build and validation
- Because Lombok 1.16.22 is incompatible with newer JDK module access, run tests with JDK 8 when available:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- Fresh cloud environments may need Maven and JDK 8 installed before validation.

## Coverage automation context
- The repository historically has weak or absent checked-in tests.
- High-risk areas from prior coverage runs include login/session handling, auth interceptor behavior, prescription dispensing, drug inventory/price SQL mappings, and form parsing utilities.
- Local templates were unavailable at `/home/ubuntu/.codex/templates`, so this file and `AGENTS.md` were initialized from inferred project context.
- On branch `cursor/missing-test-coverage-2203`, tests were added for anonymous/authenticated login interceptor behavior, successful login session persistence, logout session clearing, drug/option form encoding, and the happy-path dispensing flow that decrements requested inventory quantities and accumulates seek price. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 7 tests.
