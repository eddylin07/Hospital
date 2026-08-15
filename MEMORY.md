# Project Memory

- Created on 2026-08-15 because `/workspace` did not contain project-local memory files and `/home/ubuntu/.codex/templates` was unavailable.
- `Hospital` is a Spring Boot 2.0/MyBatis/Freemarker Maven app for hospital information management.
- Lombok 1.16.22 requires Maven validation with JDK 8 in this environment:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Coverage automation history indicates high-value regression areas include login session handling, interceptor access control, patient prescription/dispensing inventory and price updates, MyBatis mapper XML, and date formatting utilities.
- On branch `cursor/missing-test-coverage-7d50`, regression tests were added for failed/successful login session handling, dispensing inventory/price updates, insufficient-stock no-write behavior, partial multi-drug failure no-write behavior, and latest-seek-only/null-safe prescription SQL. Minimal fixes were applied to `LoginController.login`, `PatientServiceImpl.seek`, and `SeekMapper.xml`; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 6 tests.
