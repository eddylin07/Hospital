# MEMORY.md

## Project facts

- Project name: Hospital.
- Repository appears to be a Java Maven Spring Boot 2.0 hospital information management system.
- README describes it as "医疗信息管理系统（ssm+layui+freemaker）".
- Core dependencies include Spring Boot Web/FreeMarker/Test, MyBatis, PageHelper, MySQL connector, Druid, FastJSON, Apache POI, and iText.

## Workflow notes

- `~/.codex/templates/` was not present in this environment on 2026-07-29, so `AGENTS.md` and `MEMORY.md` were initialized from inferred repository facts instead of copied templates.
- On `cursor/critical-bug-investigation-0aa2`, fixed critical failed-login session persistence, role authorization, public admin self-registration, patient appointment IDOR, prescription inventory overdraw/race/partial update, latest-seek-row corruption, empty drug/option input crashes, PDF/null-date export crashes, and Java 21 Maven test compatibility; `mvn test` passed with 13 tests on 2026-07-29.

## Open questions

- Deployment/runtime environment details are not documented in the files inspected so far.
