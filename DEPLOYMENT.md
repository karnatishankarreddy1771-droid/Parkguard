# Deploy ParkGuard

Frontend: Vercel (`frntd`). API: Render (`parking`). Database: Aiven MySQL.

## Render environment values

The `render.yaml` Blueprint includes these non-secret Aiven settings:

```text
DB_HOST=mysql-197c1d51-karnatishankarreddy1771-b761.f.aivencloud.com
DB_PORT=17323
DB_NAME=defaultdb
DB_USERNAME=avnadmin
DB_SSL_MODE=REQUIRED
```

In Render's Environment page, enter `DB_PASSWORD` using the password shown for Aiven's `avnadmin` user. Also set `PARKING_ADMIN_EMAIL` and a unique `PARKING_ADMIN_PASSWORD` (at least 12 characters). Render generates `PARKING_JWT_SECRET`. Keep all passwords and JWT secrets in Render, never in Git or chat. `avnadmin` has admin-level database access.

## Deploy

1. In Render, create a Blueprint from this repository and select branch `codex/aiven-render-deploy`. The Blueprint reads root `render.yaml` and builds the `parking` Docker service.
2. Enter the secret values above, deploy, and copy the API URL, such as `https://parkguard-api.onrender.com`. Check `https://<api-url>/health` returns `status: ok`.
3. In Vercel, import the same repository and branch. Set Root Directory to `frntd`, build command to `npm run build`, and output directory to `dist`. Set `VITE_API_URL` to the Render API URL (no trailing slash), then deploy.
4. Copy the Vercel site URL into Render's `PARKING_CORS_ORIGINS`, then redeploy the API.
5. Open the Vercel URL and sign in using the admin email and password set in Render.

To deploy from `main` instead, merge the branch after review; then select `main` in Render and Vercel. Direct route refreshes in the frontend are handled by `frntd/vercel.json`.
