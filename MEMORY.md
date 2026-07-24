# MEMORY.md

## Project notes

- Project name inferred from repository: Hospital.
- Stack inferred from `pom.xml` and `README.md`: Java Maven web app using Spring Boot 2.0, MyBatis, Freemarker, MySQL, and Layui.
- Template directory `~/.codex/templates/` was not present on 2026-07-24, so AGENTS.md and MEMORY.md were initialized locally from inferred project metadata.

## Investigation notes

- Critical auth bugs found on 2026-07-24: failed login must not persist a session, public `/regest` must not create role 1 admins, and route protection must enforce role ids admin=1, doctor=2, patient=3 server-side.
- Critical dispensing bugs found on 2026-07-24: prescription dispensing must validate all requested drug quantities before deducting stock, deduct with an atomic `number >= requested` SQL guard, and update only the latest seek row by id.
- Doctor/patient PDF and form flows need null/empty input guards; missing seek or appointment records should return business messages instead of crashing.

