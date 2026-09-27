# Project Agent Guide

## Project

- Name: Hospital
- Purpose: medical information management system.
- Stack: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker, Layui, MySQL.
- Date initialized: 2026-09-27.

## Working Rules

- Read `MEMORY.md` before changing code.
- Follow existing Maven and Spring Boot conventions.
- Keep tests deterministic and focused on behavior with meaningful regression risk.
- Do not store credentials in this file or in `MEMORY.md`.

## Test Commands

- Run targeted tests with `mvn test -Dtest=ClassNameTest`.
- Run all tests with `mvn test`.
