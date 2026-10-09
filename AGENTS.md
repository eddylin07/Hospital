# Repository Instructions

## Project

- Project name: Hospital
- Purpose: 医疗信息管理系统
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL

## Working rules

- Default communication language: Chinese.
- Keep fixes narrow and behavior-focused; avoid unrelated refactors.
- For critical-bug automation, only change code when there is a concrete high-severity trigger scenario.
- Do not commit secrets or credentials. Record credential locations only, never values.

## Testing

- Prefer focused automated tests for controller/service authorization, persistence, and data-integrity changes.
- If Maven is missing in the cloud image, install it before running tests.
- Be aware this project may need Java/Maven compatibility fixes on newer JDKs because it uses an older Spring Boot stack.
