# MEMORY.md

## Project facts

- Hospital is a Java/Maven medical information management system using Spring Boot 2.0, MyBatis, FreeMarker, and MySQL.
- The repository initially lacked `AGENTS.md` and `MEMORY.md`; local template files under `~/.codex/templates/` were unavailable, so minimal inferred project docs were created on 2026-10-13.

## Working notes

- For scheduled critical-bug sweeps, inspect recent commits first and open a PR only for concrete high-severity bugs with a minimal verified fix.
- On 2026-10-13, current master still allowed failed `/login` attempts to create authenticated sessions and public `/regest` with blank `certId` to create admin accounts; `/patient/searchinfo` is the API used by the public `/patient/search` page.

