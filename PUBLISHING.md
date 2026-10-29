# Publishing and Release Guide

## GitHub Actions CI

The `.github/workflows/ci.yml` workflow runs automatically on every push and pull request to `main`:

- Checks out the repository
- Sets up JDK 21 (Temurin)
- Builds the project (`mvn clean compile`)
- Runs tests (`mvn test`)
- Packages the application (`mvn package -DskipTests`)

## Manual Release Steps

1. Ensure all tests pass locally:
   ```bash
   mvn clean test
   ```

2. Build the Docker image:
   ```bash
   docker build -t proxy-subscription-backend:latest .
   ```

3. Create a GitHub release:
   - Go to the repository's Releases page
   - Click "Draft a new release"
   - Use a semantic version tag (e.g., `v0.1.0`)
   - Attach the built JAR or Docker image reference
   - Include release notes summarizing changes

4. Push the release tag:
   ```bash
   git tag -a v0.1.0 -m "Release v0.1.0"
   git push origin v0.1.0
   ```

## Environment Variables

Copy `.env.example` to `.env` and configure before deploying:

```bash
cp .env.example .env
```

Required variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## Security Notes

- Never commit `.env` or secrets to the repository
- Use GitHub Secrets for CI environment variables
- The `docker-compose.yml` uses a local PostgreSQL instance; for production, use a managed database or secure container
