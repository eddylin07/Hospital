# Project Memory

## Last updated

- 2026-10-06

## Confirmed project facts

- Hospital is a Spring Boot 2.0.0 / MyBatis Maven application under package `com.hospital`.
- There were no checked-in tests at the start of this automation run.
- Previous coverage automation found recurring regression risk around login session handling, prescription mapper SQL, dispensing stock/price logic, and shared date formatting.

## Testing notes

- Prefer `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` because Lombok 1.16.22 fails on newer JDKs.
- Tests should avoid a live database unless the touched path explicitly requires integration coverage.
- 2026-10-06: Added `SeekMapperXmlTest` to parse `SeekMapper.xml` without a database and protect `updateDrugs` from updating every patient seek row or losing charges when existing `price` is NULL.
