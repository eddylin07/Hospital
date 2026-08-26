# Project Memory

- `/workspace` contains the Hospital Spring Boot 2.0.0 / MyBatis / Freemarker application.
- The project uses Maven and an old Lombok version; compile and test with JDK 8.
- Standard validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Fresh cloud environments may need `maven` and `openjdk-8-jdk-headless` installed before tests can run.
- Automation coverage runs should prioritize meaningful behavior tests for services, controllers, mapper XML, parsing utilities, and validation edge cases.
- On branch `cursor/missing-test-coverage-cc79`, coverage tests were added for `SeekMapper.updateDrugs` and `LoginInterceptor`; `SeekMapper.xml` was fixed so dispensing updates only the latest seek record and handles null prices with `ifnull(price,0)`. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 3 tests.

