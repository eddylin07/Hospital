# MEMORY.md

## Project notes

- Project: Hospital.
- Stack inferred from repository files: Spring Boot 2, MyBatis, Maven, Java.
- The template directory `/home/ubuntu/.codex/templates` was unavailable during setup, so project-local `AGENTS.md` and `MEMORY.md` were created with inferred fields.

## Build and test notes

- Use JDK 8 for Maven validation when available:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 is missing in a fresh cloud image, install them before validation.

## Business invariants learned

- `SeekMapper.updateDrugs` is a high-risk prescription billing path. It should update only the latest seek row for a patient and should add new prescription price with `ifnull(price,0)` so existing NULL prices do not swallow the charge.
