# AGENTS.md

## Project

- Name: Hospital
- Type: Medical information management system
- Stack: SSM + Layui + Freemarker

## Working notes

- Default to concise, focused changes that match the existing Java/Spring/MyBatis style.
- Treat role and ownership checks as security-critical. Known role ids: admin=1, doctor=2, patient=3.
- Do not record credential values. If credentials are relevant, record only the file path where they are configured.

## Testing

- Prefer targeted Maven tests for backend correctness changes.
- If Maven is missing in the cloud image, install it before running tests.
