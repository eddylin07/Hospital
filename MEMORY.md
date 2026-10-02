# MEMORY.md

## Project Facts

- Hospital is a Spring Boot 2.0/MyBatis hospital management application.
- Maven project coordinates: `com.wxthxy.hospital:HospitalAction`.
- Main code lives under `src/main/java/com/hospital`; mapper XML files live under `src/main/resources/mapper`.
- The repository currently starts without a checked-in `src/test` tree on `master`.

## Testing Notes

- Use JDK 8 for Maven tests because Lombok 1.16.22 fails under newer JDK module rules.
- Preferred validation command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- Existing automation history shows high-risk areas include login/session handling, dispensing inventory and billing logic, MyBatis mapper XML, and shared date/PDF utilities.

## Current Coverage Focus

- For coverage automation, inspect recent production-only changes first and add the smallest deterministic tests that pin business-critical behavior.
- 2026-10-02 coverage run: added a database-free MyBatis XML test for `SeekMapper.updateDrugs` to guard latest-seek-only prescription updates and null-safe price accumulation; validated with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
