# MEMORY.md

## Project Memory

- Initialized on 2026-09-25 because AGENTS.md and MEMORY.md were absent and templates were unavailable under `~/.codex/templates`.
- Project appears to be a Java Spring Boot hospital management application using MyBatis mappers, FreeMarker templates, and Maven.
- High-risk areas from prior investigations: failed-login session persistence, role authorization, public admin registration, patient appointment ownership, doctor-to-patient workflow authorization, prescription inventory updates, latest seek-row updates, and PDF/null-input handling.
- On 2026-09-25, commit 910598d still had `SeekMapper.updateDrugs` updating every `seek` row for a patient. The fix targets only the latest seek row by descending `id`, with `SeekMapperXmlTest` covering the SQL shape.
