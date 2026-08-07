# Project Memory

- Hospital is a Spring Boot 2/MyBatis Maven application with FreeMarker templates.
- The repository historically has little or no checked-in test coverage on `master`; add focused tests around risky production paths.
- Maven validation should run with JDK 8:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If dependencies are missing in the cloud image, install `maven` and `openjdk-8-jdk-headless`.
- Prior coverage work found meaningful risk around login session handling, request interception, prescription dispensing, mapper XML SQL, and form parsing utilities.
- On branch `cursor/missing-test-coverage-1b8d`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so prescriptions update only the latest seek row and price accumulation treats existing NULL price as zero; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 1 test.
