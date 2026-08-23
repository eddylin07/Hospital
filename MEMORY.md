# Project Memory

- Project name: Hospital.
- Last updated: 2026-08-23.
- Technology stack inferred from repository: Java, Spring Boot 2.0, Maven, MyBatis, Freemarker, Layui, MySQL.
- Template source `~/.codex/templates/` was not present in the Cursor Cloud image on 2026-08-23, so this file and AGENTS.md were initialized with inferred minimal content.
- Current automation focus: inspect recent commits for critical correctness bugs only; avoid opening a PR unless a concrete severe bug is found and fixed.
- On 2026-08-23, critical fixes covered failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR, doctor workflow patient authorization, prescription inventory overdraw/race/partial-update protection, latest-seek-row prescription updates, MyBatis multi-parameter binding crashes, and null/empty PDF or drug/option input crashes; `mvn test` passed with 11 tests.
