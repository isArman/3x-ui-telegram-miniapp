# Installation, Configuration, Backup, and Recovery

## Installation

### Local Development

```bash
git clone <repository-url>
cd 3x-ui-telegram-miniapp
docker compose up --build
```

### Production

```bash
cp .env.example .env
# Configure .env with production values
export DB_PASSWORD=<secure-password>
export ADMIN_TELEGRAM_IDS=<comma-separated-ids>
export SUBSCRIPTION_SERVICE_URL=<3x-ui-panel-url>
export SUBSCRIPTION_SERVICE_USERNAME=<token>
export SUBSCRIPTION_SERVICE_PASSWORD=<token>

docker compose -f docker-compose.prod.yml up -d
```

## Configuration

Key environment variables:

| Variable | Description | Example |
|---|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://postgres:5432/proxy_subscription` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `proxy_user` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `<secure>` |
| `ADMIN_TELEGRAM_IDS` | Comma-separated admin Telegram IDs | `123456789,987654321` |
| `SUBSCRIPTION_SERVICE_URL` | 3x-ui panel base URL | `https://panel.example.com` |
| `SUBSCRIPTION_SERVICE_USERNAME` | Bearer token or username | `<token>` |
| `SUBSCRIPTION_SERVICE_PASSWORD` | Bearer token or password | `<token>` |
| `PAYMENT_BANK_NAME` | Bank name for card-to-card payments | `Bank Melli` |
| `PAYMENT_BANK_CARD_NUMBER` | Card number | `1234-5678-9012-3456` |
| `PAYMENT_BANK_ACCOUNT_HOLDER` | Account holder name | `Admin Name` |

## Database Migrations

Flyway manages migrations automatically on startup. Migrations are in `src/main/resources/db/migration/`.

To verify migration status:

```bash
mvn flyway:info
```

## Backup Procedures

### Database Backup

```bash
docker exec proxy-subscription-postgres-prod pg_dump -U proxy_user -d proxy_subscription > backup_$(date +%Y%m%d).sql
```

### Receipt Files Backup

```bash
tar -czf receipts_backup_$(date +%Y%m%d).tar.gz receipts/
```

### Full Backup Script

```bash
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
docker exec proxy-subscription-postgres-prod pg_dump -U proxy_user -d proxy_subscription > backups/db_backup_$DATE.sql
tar -czf backups/receipts_backup_$DATE.tar.gz receipts/
echo "Backup completed: $DATE"
```

## Recovery Procedures

### Database Recovery

```bash
docker exec -i proxy-subscription-postgres-prod psql -U proxy_user -d proxy_subscription < backups/db_backup_YYYYMMDD.sql
```

### Receipt Recovery

```bash
tar -xzf backups/receipts_backup_YYYYMMDD.tar.gz -C .
```

### Full System Recovery

1. Stop services: `docker compose -f docker-compose.prod.yml down`
2. Restore database
3. Restore receipt files
4. Restart: `docker compose -f docker-compose.prod.yml up -d`

## Security Notes

- Never commit `.env` or secrets
- Use GitHub Secrets for CI variables
- The `docker-compose.prod.yml` uses `DB_PASSWORD` and `ADMIN_TELEGRAM_IDS` from environment
- Receipt uploads are validated with Telegram `initData` before storage
- Admin endpoints enforce authorization via `X-Telegram-User-Id` header
- Payment audit trail is immutable (no update/delete endpoints)

## Remaining Risks and Limitations

- **No online payment gateway** in initial version (manual card-to-card only)
- **No Kubernetes / microservices** — modular monolith only
- **No Redis / Celery** — synchronous provisioning only
- **Initial user target**: <100 active users; design avoids premature optimization
- **External API dependency**: 3x-ui panel must be reachable for subscription provisioning
- **File-system receipt storage**: For larger scale, migrate to object storage (S3/minio)
- **No automatic retry loops**: Retries are manual (admin-initiated) to prevent unintended duplicate operations
