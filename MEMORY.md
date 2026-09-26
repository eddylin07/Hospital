# Project Memory

## Project Facts

- Project name: Hospital.
- Build system: Maven.
- Application stack: Java Spring Boot 2.0.0.RELEASE with MyBatis, MySQL, Freemarker, PageHelper, Druid, FastJSON, Apache POI, and iText.
- Main Maven artifact: `com.wxthxy.hospital:HospitalAction:1.0-SNAPSHOT`.

## Environment Notes

- `~/.codex/templates/` was missing in this cloud environment on 2026-09-26, so `AGENTS.md` and `MEMORY.md` were initialized from inferred project facts.

## Data Integrity Notes

- Dispensing drugs through `DoctorController.drug` must update only the patient's latest `seek` row. Updating `seek` by `patientid` corrupts historical visit prescriptions and billing totals for patients with multiple visits.
