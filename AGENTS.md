# Hospital 医疗信息管理系统

Spring Boot 2.0.0 monolith (SSM: Spring MVC + MyBatis + MySQL) with FreeMarker (`.flt`)
templates and layui front-end. Single service, package `com.hospital`, entry point
`com.hospital.HospitalApp`. Roles: admin (role 1), doctor (role 2), patient (role 3).

## Cursor Cloud specific instructions

### Stack / toolchain
- **Java 8 is required** (Spring Boot 2.0.0 + Lombok 1.16.22 do not work on newer JDKs).
  Java 8 is installed and set as the system default via `update-alternatives`, so `java`/`mvn`
  already use it. Verify with `java -version` (expect `1.8.0`).
- Build tool: Maven (`mvn`). Dependencies are declared in `pom.xml`.

### Database (MariaDB, MySQL-compatible)
- The app connects to `jdbc:mysql://localhost:3306/hospital` as `root` / `123456`
  (see `src/main/resources/application.yml`). MariaDB serves this; `root` is configured with
  `mysql_native_password` so the old `mysql-connector-java 5.1.47` driver can connect over TCP.
- **systemd is unavailable**; start the DB daemon manually each session:
  `sudo mariadbd --user=mysql &` (data dir `/var/lib/mysql`). Check with `sudo mysqladmin ping`.
- The `hospital` schema + seed data live in `sql/hospital.sql` and are already loaded into the
  persisted data dir. To reload: `mysql -uroot -p123456 -h127.0.0.1 hospital < sql/hospital.sql`.
- Seed logins (username / password): `admin1`/`123456` (admin), `hanmeimei`/`12345` (doctor),
  `haoyi`/`123456` (patient).

### Build / run
- Compile: `mvn -q clean compile`
- Run (dev): `mvn spring-boot:run` — serves on **port 8088** (spring-boot-devtools hot reload is on).
- There are **no automated tests** in this repo (no `src/test`), despite the
  `spring-boot-starter-test` dependency.

### Gotchas
- **Static resources require a login session.** `LoginInterceptor` maps `/**` and only excludes a
  few page paths (not `/static/**`). So an *unauthenticated* request for `/static/js/*.js` gets a
  302 redirect to `/hospital/login`, returning HTML. In a browser this breaks the login page's own
  jQuery/layui (`Uncaught SyntaxError: Unexpected token '<'`), so the login *form* cannot submit.
  Workarounds for testing: authenticate via the API
  (`POST /login` with JSON `{"username":"admin1","password":"123456"}`) to obtain a `JSESSIONID`
  cookie, then either use `curl -b` for page requests or inject that cookie into the browser via
  DevTools. Once a valid session exists, static assets load and all pages render normally.
- `filepath.appointpdf` / `filepath.seekpdfpath` in `application.yml` are Windows paths (`D:\\`);
  the PDF-export endpoints (`/patient/downloadpdf`, seek PDF) will fail on Linux unless these are
  pointed at an existing directory. Core CRUD/browse flows are unaffected.
