# MEMORY.md

## Project Facts

- Project name: Hospital
- Repository root: `/workspace`
- Date first initialized by automation: 2026-10-10
- Application type: Java/Spring Boot hospital information management system.
- Build/test tool: Maven (`pom.xml`).
- Key stack inferred from `pom.xml`: Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, MySQL connector, PageHelper, FastJSON, Apache POI, iText.

## Testing Notes

- No existing `src/test/**` tree was found during initial inspection.
- Prefer focused unit tests for utility logic and small service/controller branches to avoid environment-dependent database tests.
- 2026-10-10 coverage run added MyBatis XML and Spring MVC config unit tests for recent `SeekMapper.updateDrugs` and interceptor exclusion changes. Validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Open Questions

- None currently blocking automated test coverage work.
