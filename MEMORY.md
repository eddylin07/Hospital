# Project Memory

## Project Facts

- Project initialized for automation on 2026-08-29.
- Repository: Hospital; Maven artifact: `com.wxthxy.hospital:HospitalAction`.
- Stack inferred from `pom.xml`: Java, Spring Boot 2.0.0.RELEASE, MyBatis, Maven.
- No templates were available at `/home/ubuntu/.codex/templates`, so project-local `AGENTS.md` and `MEMORY.md` were created from inferred facts.

## Validation

- Prefer running tests with JDK 8 because Lombok 1.16.22 can fail on newer JDKs:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Coverage Automation Notes

- Add missing tests around recent production behavior with meaningful regression risk.
- Keep repository memory concise; do not duplicate facts that are directly obvious from source files.
- 2026-08-29 coverage run: `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` SQL so dispensing updates only the latest seek row and treats existing NULL price as zero; validation passed with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
