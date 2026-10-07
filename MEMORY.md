# Project Memory

## Project facts

- Hospital is a Maven Java web application with Spring Boot-style entry point, MyBatis mappers, Freemarker templates, and MySQL SQL assets.
- Main package root: `src/main/java/com/hospital`.

## Investigation notes

- Login must only store a session principal after `LoginService.login` has populated a real id and role; failed login attempts must clear the session.
- Role ids are admin=1, doctor=2, patient=3. Admin/patient/doctor workflow routes need server-side role checks; `/doctor/{department}` remains an authenticated lookup route and should not be blanket-restricted to doctor role.
- Patient appointment creation must bind `appointment.patientid` to the patient resolved from the session and use the generated appointment id for the patient's appointment pointer.
- Doctor workflow mutations must verify that the target patient has an appointment assigned to the current doctor before diagnosis, dispensing, hospitalization, medical-history viewing, or seek PDF generation.
- Prescription dispensing must validate all requested quantities, deduct stock atomically with a `number >= requested` guard, roll back on partial failure, and update only the latest seek row for the patient.
- Empty drug/option selections and missing appointment/seek records should return controlled messages instead of throwing runtime exceptions.
