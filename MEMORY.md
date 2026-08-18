# Hospital Project Memory

- `/home/ubuntu/.codex/templates` was unavailable when project-local `AGENTS.md` and `MEMORY.md` were initialized.
- Project: `HospitalAction`, a Spring Boot 2.0.0 / MyBatis / Freemarker Maven application.
- Current automation focus: add focused regression tests for recently merged risky behavior, avoiding cosmetic or low-signal snapshot tests.
- Test convention: prefer JUnit 4 with Mockito or mapper XML parsing; avoid tests that require a live MySQL database unless necessary.
- Validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Environment caveat: Lombok 1.16.22 requires JDK 8 for compilation in this runner.
