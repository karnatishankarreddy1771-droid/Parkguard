# ParkGuard

Apartment parking management system built with React + Vite, Spring Boot, Spring Security, JWT, JPA, and MySQL/H2. The frontend is in `frntd/`; the API is in `parking/`.

## Deploy

Follow [DEPLOYMENT.md](DEPLOYMENT.md) to deploy the frontend to Vercel, the API to Render, and the database to Aiven MySQL. The Render Blueprint is [render.yaml](render.yaml).

## Run locally

Requirements: Java 21, Maven, and Node.js.

Before starting the backend, set `PARKING_JWT_SECRET` to a random secret of at least 32 UTF-8 bytes. To seed an administrator in a new database, also set `PARKING_ADMIN_EMAIL` and `PARKING_ADMIN_PASSWORD`. Set both or neither; no default admin credentials are included.

From `parking/`, run:

```bash
mvn spring-boot:run
```

The default local database is file-based H2. From `frntd/`, run:

```bash
npm install
npm run dev
```

Vite serves the frontend at `http://localhost:5173`; the API defaults to `http://localhost:9080`.

## Access model

- Admin manages users, parking slots, vehicles, and visitor records.
- Resident and Security routes are role-protected by the API.
- Passwords are stored as BCrypt hashes.
- The first administrator is seeded only when `PARKING_ADMIN_EMAIL` and `PARKING_ADMIN_PASSWORD` are provided.

Never commit database passwords, JWT signing secrets, or administrator passwords. Use the hosting provider's secret environment variables for deployment.
