# Project Memory

- Project: Hospital.
- Stack inferred from repository: Java Spring Boot 2, MyBatis, Maven, Freemarker templates.
- This environment did not contain `/home/ubuntu/.codex/templates`, so project-local `AGENTS.md` and `MEMORY.md` were created from inferred repository context.
- Use JDK 8 for validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Existing automation memory says this project historically has weak checked-in test coverage and prior coverage branches used JUnit 4 plus Mockito tests.
- On branch `cursor/missing-test-coverage-0599`, regression tests cover `SeekMapper.updateDrugs` SQL and `LoginInterceptor` anonymous/authenticated behavior. `SeekMapper.xml` was fixed so prescription dispensing updates only the latest seek row and treats NULL price as zero. Validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 3 tests.
