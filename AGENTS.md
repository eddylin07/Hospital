# Agent Instructions

Project: Hospital
Date initialized: 2026-08-21

## Project overview

- Java Spring Boot 2.0.0 application using MyBatis, FreeMarker, and Maven.
- Main code lives under `src/main`.
- Tests should live under `src/test` and follow existing Maven/JUnit conventions.

## Build and test

- Prefer targeted Maven tests for changed areas, then run the relevant broader target when practical.
- This project uses Lombok 1.16.22, which requires JDK 8 in this environment.
- Known successful validation command:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Working conventions

- Keep test additions deterministic, independent, and focused on regression risk.
- Do not change production behavior unless a small fix is required to make a tested invariant true.
- Preserve existing package layout and naming style.
- Do not commit generated build outputs such as `target/`.

## Memory

- Read `MEMORY.md` before changing code.
- Update `MEMORY.md` when learning durable project facts, environment pitfalls, or decisions that should affect future work.
