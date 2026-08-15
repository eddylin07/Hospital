# Project Memory

- Created on 2026-08-15 because `/workspace` did not contain project-local memory files and `/home/ubuntu/.codex/templates` was unavailable.
- `Hospital` is a Spring Boot 2.0/MyBatis/Freemarker Maven app for hospital information management.
- Lombok 1.16.22 requires Maven validation with JDK 8 in this environment:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Coverage automation history indicates high-value regression areas include login session handling, interceptor access control, patient prescription/dispensing inventory and price updates, MyBatis mapper XML, and date formatting utilities.
