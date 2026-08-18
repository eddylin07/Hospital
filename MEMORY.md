# Hospital Project Memory

- `/home/ubuntu/.codex/templates` was unavailable when project-local `AGENTS.md` and `MEMORY.md` were initialized.
- Project: `HospitalAction`, a Spring Boot 2.0.0 / MyBatis / Freemarker Maven application.
- Current automation focus: add focused regression tests for recently merged risky behavior, avoiding cosmetic or low-signal snapshot tests.
- Test convention: prefer JUnit 4 with Mockito or mapper XML parsing; avoid tests that require a live MySQL database unless necessary.
- Validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Environment caveat: Lombok 1.16.22 requires JDK 8 for compilation in this runner.
- On branch `cursor/missing-test-coverage-e1ab`, regression tests cover failed-login session handling, login interceptor anonymous/authenticated behavior, latest-seek-only/null-safe prescription SQL, calendar-year date formatting, dispensing all-or-nothing inventory validation, drug mapper price/number/text mapping, and empty drug/option form parsing. Validation passed with 14 tests.
