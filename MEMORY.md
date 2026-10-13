# Project Memory

- Hospital is a Spring Boot 2.0/MyBatis/Maven medical information management system.
- The repository may start without checked-in tests; add focused JUnit 4/Mockito tests under `src/test/java`.
- Use `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` for validation when JDK 8 is available because Lombok 1.16.22 fails on newer JDKs.
- Prior coverage work found high-value regression areas around login session handling, interceptor behavior, prescription/dispensing flows, mapper XML SQL, and date formatting.
- Templates for `AGENTS.md` and `MEMORY.md` were unavailable under `/home/ubuntu/.codex/templates` in this environment, so project-local guidance was inferred from the repo and automation memory.
- This run added `SeekMapperXmlTest` for `SeekMapper.updateDrugs`, covering latest-seek-only prescription updates and null-safe price accumulation.
