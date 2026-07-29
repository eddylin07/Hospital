# MEMORY.md

## Project facts

- Hospital is a Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, Maven application for hospital/medical information management.
- The Maven artifact is `com.wxthxy.hospital:HospitalAction`.
- Production code is under `src/main/java/com/hospital`; mapper XML files are under `src/main/resources/mapper`.

## Validation notes

- Lombok 1.16.22 does not compile on the default Java 21 compiler because of javac module access restrictions.
- Use JDK 8 for Maven validation:

```sh
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Coverage automation notes

- Prior coverage work in this repository found meaningful risk around login session handling, login interception, prescription/dispensing inventory updates, drug form parsing, and MyBatis mapper XML behavior.
- Prefer JUnit 4 and Mockito tests that avoid live database dependencies.
- XML mapper tests can parse MyBatis mapper files directly to assert SQL invariants without requiring MySQL.
- On branch `cursor/missing-test-coverage-ed12`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so dispensing updates only the latest seek row and null historical prices are billable via `ifnull(price,0)`.
