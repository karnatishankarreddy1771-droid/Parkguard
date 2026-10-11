# ParkGuard — Apartment Parking Management System

A real-world apartment/community parking control system built with **Spring Boot + Spring Security + JWT + JPA/H2/MySQL** and **React + Vite + Axios**.

## Business workflow

### 1. Admin
The Admin is the only role allowed to create, edit, assign or delete stored information.

Admin can:
- Create and manage resident and security accounts.
- Assign parking slots to residents.
- Change slot status: `AVAILABLE`, `OCCUPIED`, `RESERVED`.
- Register/edit/delete vehicles.
- Record visitor requests supplied by residents.
- Review expected visitors.
- Verify visitor entry and exit.
- Delete incorrect/obsolete visitor records.
- View the complete parking dashboard.

### 2. Security
Security has a dedicated **read-only API namespace**:
- Check all slots.
- Check available slots.
- Check expected visitors.
- Verify visitor details against the Admin-recorded visitor list.
- Get the Admin contact details when escalation is required.

Security has **no POST, PUT or DELETE endpoint**.

### 3. Resident
Resident has a dedicated **read-only API namespace**:
- View parking slot information.
- View available slots.
- View visitor records.
- Get the Security contact for asking about availability / visitor verification.

Resident has **no POST, PUT or DELETE endpoint**.

> Important requirement decision: the requested business scenario says a Resident can "add a visitor", but the technical rule says "any other role should not have any permission to update or add or delete information." This implementation follows the stricter security rule. A Resident therefore cannot persist a visitor directly; the Admin records/approves the visitor information.

---

# Security model

## Admin-only web login

The frontend exposes one login page for Admin, Resident, and Security. Its short-lived session state is held in browser `sessionStorage` and cleared at sign-out or on an unauthorized API response:

`POST http://localhost:9080/auth/admin/login`

Role-aware web sign-in uses `POST http://localhost:9080/auth/login`. After the Admin creates a Resident or Security account, share the email and temporary password entered during creation. Passwords are BCrypt hashes, so they cannot be read back or displayed later.

There is:
- No public registration page.
- No role selector on the login page.
- No Resident login option.
- No Security login option.
- Protected React routes redirect unauthenticated/non-admin users back to login.

The backend also checks that the authenticated account has `ADMIN` role before issuing the normal web login token.

### Administrator provisioning

For local development, the default seeded administrator is `admin@parking.local` with password `ParkGuardAdmin@2026`. The configured admin password is reset on each backend startup so these credentials continue to work. Override `PARKING_ADMIN_EMAIL` and `PARKING_ADMIN_PASSWORD` and use a secret manager for deployment; never use the development credentials in production. Provision additional accounts through the Admin UI.

---

# API design

## Admin endpoints

All require an authenticated `ADMIN` JWT.

### Users
- `GET /api/admin/users`
- `POST /api/admin/users`
- `PUT /api/admin/users/{id}`
- `DELETE /api/admin/users/{id}`
- `POST /api/admin/users/{id}/access-token`

### Parking
- `GET /api/admin/parking/slots`
- `GET /api/admin/parking/slots/available`
- `POST /api/admin/parking/slots`
- `PUT /api/admin/parking/slots/{id}`
- `DELETE /api/admin/parking/slots/{id}`

### Vehicles
- `GET /api/admin/parking/vehicles`
- `POST /api/admin/parking/vehicles`
- `PUT /api/admin/parking/vehicles/{id}`
- `DELETE /api/admin/parking/vehicles/{id}`

### Visitors
- `GET /api/admin/parking/visitors`
- `POST /api/admin/parking/visitors`
- `PUT /api/admin/parking/visitors/{id}/entry`
- `PUT /api/admin/parking/visitors/{id}/exit`
- `DELETE /api/admin/parking/visitors/{id}`

## Security endpoints

All require an authenticated `SECURITY` JWT and are read-only.

- `GET /api/security/slots`
- `GET /api/security/slots/available`
- `GET /api/security/visitors`
- `GET /api/security/visitors/expected`
- `GET /api/security/admin-contact`

## Resident endpoints

All require an authenticated `RESIDENT` JWT and are read-only.

- `GET /api/resident/slots`
- `GET /api/resident/slots/available`
- `GET /api/resident/visitors`
- `GET /api/resident/security-contact`

## Authentication / access-token flow

Only the Admin has a public login endpoint.

An Admin can provision a Resident or Security account and, where an API client needs direct access, issue a role-scoped JWT using:

`POST /api/admin/users/{id}/access-token`

The returned token carries the user's role because Spring Security builds the authority from the database role.

This keeps the web login page Admin-only while still providing proper role-specific backend endpoints.

---

# Authorization guarantees

Authorization is enforced in the **backend**, not only by hiding frontend buttons.

- `@PreAuthorize("hasRole('ADMIN')")` protects all write/admin controllers.
- Security controller uses `@PreAuthorize("hasRole('SECURITY')")`.
- Resident controller uses `@PreAuthorize("hasRole('RESIDENT')")`.
- The JWT filter loads the current user from the database.
- The user's current role is used to create the Spring Security authority.
- A Resident/Security token cannot call Admin write endpoints.
- A missing/invalid token cannot access protected endpoints.
- Passwords are BCrypt-hashed.
- User password is excluded from JSON responses with `@JsonIgnore`.

---

# Run locally

## Backend

Requirements:
- Java 21
- Maven
- Optional MySQL

From:

`ParkingManagementSystem/parking`

Before starting the backend, configure these environment variables (use a secret manager in production):

- `PARKING_JWT_SECRET`: random secret with at least 32 UTF-8 bytes.
- `PARKING_ADMIN_EMAIL` and `PARKING_ADMIN_PASSWORD`: override the local-development admin credentials; configure these securely for deployment.
- `PARKING_ADMIN_RESET_PASSWORD_ON_STARTUP`: defaults to `true` for local development; set to `false` in deployment unless password resets on every startup are explicitly intended.
- `PARKING_CORS_ORIGINS`: comma-separated exact frontend origins. Defaults to local Vite/React development origins.

Run:

```bash
mvn spring-boot:run
```

Backend:

`http://localhost:9080`

The default development configuration uses a file-based H2 database, so MySQL is not required for the first demo.

## Frontend

Requirements:
- Node.js 20+ recommended

From:

`ParkingManagementSystem/frntd`

```bash
npm install
npm run dev
```

Frontend:

`http://localhost:5173`

For a deployed frontend, set `VITE_API_URL` at build time to the HTTPS API base URL.

---

# MySQL

For MySQL, use the supplied:

`parking/src/main/resources/application-mysql.properties`

Create:

```sql
CREATE DATABASE parkingdb;
```

Set `DB_USERNAME` and `DB_PASSWORD` to a dedicated, least-privilege MySQL account, then start with:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

# Project structure

```text
ParkingManagementSystem/
├── parking/
│   ├── pom.xml
│   └── src/main/java/com/anurag/parking/
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── enums/
│       ├── exception/
│       ├── filter/
│       ├── repository/
│       └── service/
└── frntd/
    ├── package.json
    └── src/
        ├── api.js
        ├── App.jsx
        ├── Dashboard.jsx
        ├── Login.jsx
        ├── index.css
        └── main.jsx
```

## Production recommendations

Before deployment:
1. Provide `PARKING_JWT_SECRET` and any bootstrap admin credentials through environment variables/secrets; rotate any credentials previously used with this project.
2. Use HTTPS.
3. Replace localhost CORS origins with the deployed frontend domain.
4. Use MySQL/PostgreSQL instead of H2.
5. Add audit logging for Admin changes.
6. Add rate limiting and account lockout for repeated login failures.
7. Add database-level constraints for apartment/slot relationships.
8. Add SMS/WhatsApp integration through an approved provider rather than storing or sending credentials directly.
#   P a r k g u a r d  
 #   P a r k g u a r d  
 #   P a r k g u a r d  
 