# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Spring Boot, MyBatis, FreeMarker, Maven
- Date initialized: 2026-09-25

## Working Guidelines

- Follow existing controller/service/mapper patterns and keep fixes narrowly scoped.
- Treat authentication, role authorization, patient ownership, prescription inventory, and medical record updates as high-risk paths.
- Prefer focused regression tests for bug fixes.
- Do not store credentials in this file.
