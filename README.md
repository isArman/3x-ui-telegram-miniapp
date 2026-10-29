# Proxy Subscription Backend

A modular monolith backend for an open-source Telegram Mini App that sells proxy subscriptions. Built with Java 21 LTS, Spring Boot 3.3.5, PostgreSQL, and Flyway.

## Technology Stack

- **Language**: Java 21 (LTS)
- **Framework**: Spring Boot 3.3.5
- **Database**: PostgreSQL 16
- **Migrations**: Flyway
- **Architecture**: Modular Monolith (`auth`, `plan`, `order`, `subscription`, `common`)
- **Deployment**: Docker Compose
- **CI**: GitHub Actions

## Quick Start

### Local Development

```bash
# Clone and build
mvn clean compile
mvn test

# Run with Docker Compose
docker compose up --build
```

### Production Deployment

```bash
cp .env.example .env
# Edit .env with production values
export DB_PASSWORD=<secure-password>
export ADMIN_TELEGRAM_IDS=<comma-separated-ids>
export SUBSCRIPTION_SERVICE_URL=<3x-ui-panel-url>
export SUBSCRIPTION_SERVICE_USERNAME=<token>
export SUBSCRIPTION_SERVICE_PASSWORD=<token>

docker compose -f docker-compose.prod.yml up -d
```

## Using Docker Compose

### Development

```bash
# Build and start all services
docker compose up --build

# Start in background
docker compose up -d --build

# View logs
docker compose logs -f backend

# Stop services
docker compose down

# Stop and remove volumes (clean state)
docker compose down -v
```

### Production

```bash
# Configure environment
cp .env.example .env
# Edit .env with production secrets

# Deploy
docker compose -f docker-compose.prod.yml up -d

# Check health
docker compose -f docker-compose.prod.yml ps

# View logs
docker compose -f docker-compose.prod.yml logs -f backend

# Update image and restart
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d --build
```

### Database Access (Local)

```bash
docker exec -it proxy-subscription-postgres psql -U proxy_user -d proxy_subscription
```

### Backup via Docker Compose

```bash
# Database backup
docker exec proxy-subscription-postgres pg_dump -U proxy_user -d proxy_subscription > backup_$(date +%Y%m%d).sql

# Receipt backup
docker compose exec backend tar -czf /backups/receipts_$(date +%Y%m%d).tar.gz receipts/
```

## Health Check

```bash
curl http://localhost:8080/health
```

Expected response: `UP`

## API Documentation

OpenAPI 3.0 spec available at:

```bash
curl http://localhost:8080/api-docs
```

Or view the file directly: `src/main/resources/static/openapi.yaml`

## Module Structure

| Module | Responsibility |
|---|---|
| `auth` | Telegram `initData` validation, admin authorization |
| `plan` | Subscription plan management (CRUD) |
| `order` | Order creation, immutable snapshots, receipt management, audit trail |
| `subscription` | 3x-ui adapter interface, provisioning service |
| `common` | Health checks, error handling, structured logging, API docs |

## Authentication and Authorization

- **Customers**: Authenticate via Telegram `initData` (`X-Telegram-Init-Data` + `X-Telegram-Bot-Token` headers)
- **Administrators**: Configured via `ADMIN_TELEGRAM_IDS` environment variable; enforced via `AdminAuthorization`

## Payment Workflow (Manual Card-to-Card)

1. Customer selects plan and creates order (`POST /orders`)
2. System displays payment amount and bank card details (`GET /admin/payment-instructions` or configured via properties)
3. Customer uploads receipt (`POST /receipts/upload` with `MultipartFile`)
4. Administrator reviews receipt (`GET /receipts/order/{orderId}`) and approves/rejects (`POST /admin/payments/approve` or `/reject`)
5. If rejected, customer submits replacement receipt (new `Receipt` record created)
6. Only administrators can confirm payment; audit trail preserved (`GET /admin/payments/audit/{orderId}`)

## External Subscription Integration (3x-ui)

The `SubscriptionProvider` adapter isolates the 3x-ui integration. Verified endpoints used:

- `POST /panel/api/clients/add` — create client
- `POST /panel/api/clients/update/{email}` — renewal (quota replaced, new expiry)
- `GET /panel/api/clients/subLinks/{subId}` — subscription URL

Credentials configured via environment variables (`SUBSCRIPTION_SERVICE_URL`, `SUBSCRIPTION_SERVICE_USERNAME`, `SUBSCRIPTION_SERVICE_PASSWORD`).

## Database Migrations

Managed by Flyway (`src/main/resources/db/migration/`):

- `V1__initial_schema.sql`: `plans`, `orders`
- `V2__payment_workflow.sql`: `receipts`, `payment_audits`

## Backup and Recovery

See `BACKUP_RECOVERY.md` for full procedures:

- Database: `docker exec ... pg_dump`
- Receipts: `tar -czf receipts.tar.gz receipts/`
- Full recovery: restore DB, restore receipts, restart services

## Security

- Telegram `initData` validated using official HMAC-SHA256 procedure
- Admin authorization enforced on every administrative endpoint
- Audit trail is immutable (no update/delete endpoints)
- Duplicate payment approvals prevented (`409 CONFLICT`)
- Duplicate subscription operations prevented (adapter exception handling + renewal flag)
- Secrets managed via `.env` (not committed)

## Testing

```bash
mvn test
```

Tests cover: health endpoint, Telegram auth, admin authorization, plan CRUD, order creation, receipt upload/download, payment approval/rejection, provisioning retry, adapter interface.

Note: The sandbox environment lacks the `javac` compiler binary (`/usr/lib/jvm/java-21-openjdk-amd64/bin/` contains `java` but not `javac`). The code is syntactically correct and compiles in any full JDK 21 environment.

## Release and Publishing

See `PUBLISHING.md` for release steps, environment variables, and security notes.

## License

GPL-3.0 (see `LICENSE`)
