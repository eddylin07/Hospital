# Project Memory

## Confirmed Facts

- Project name: Hospital.
- Stack: Java Maven application using Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, MySQL, PageHelper, Apache POI, and iText.
- Repository did not contain `AGENTS.md` or `MEMORY.md` on 2026-10-07; template files were not available under `/home/ubuntu/.codex/templates/`, so minimal project-specific files were created.

## Testing Notes

- Use Maven test targets for Java changes.
- Repository now has JUnit 4 tests under `src/test/java`.
- Added a JUnit 4 MyBatis XML parser test for `SeekMapper.updateDrugs`; it validates SQL text without needing a live database.
- `SeekMapper.updateDrugs` should update only the latest seek row for a patient and accumulate price with `ifnull(price,0)`.

