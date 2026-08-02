# Project Memory

## Confirmed on 2026-08-02

- The template directory `/home/ubuntu/.codex/templates` was unavailable, so project-local `AGENTS.md` and `MEMORY.md` were initialized directly.
- This repository is a Spring Boot 2.0.0.RELEASE + MyBatis + FreeMarker + Maven hospital management application.
- The Maven artifact is `com.wxthxy.hospital:HospitalAction:1.0-SNAPSHOT`; source packages use `com.hospital`.
- Validation should use Java 8: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Historical high-risk coverage targets for this app include login/session handling, `LoginInterceptor`, prescription dispensing in `PatientServiceImpl.seek`, and mapper XML for inventory/price updates.
