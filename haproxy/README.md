# HAProxy Configuration

This directory contains HAProxy configuration for production deployment with a domain and HTTPS.

## Setup

1. Obtain SSL certificates for your domain (e.g., via Let's Encrypt):
   ```bash
   certbot certonly --standalone -d api.yourdomain.com
   ```

2. Combine certificates for HAProxy:
   ```bash
   cat /etc/letsencrypt/live/api.yourdomain.com/fullchain.pem \
       /etc/letsencrypt/live/api.yourdomain.com/privkey.pem > \
       /etc/haproxy/certs/api.yourdomain.com.pem
   chmod 600 /etc/haproxy/certs/api.yourdomain.com.pem
   ```

3. Update `haproxy.cfg` with your domain:
   - Replace `api.yourdomain.com` with your actual domain
   - Adjust backend server if the app runs on a different port

4. Run HAProxy:
   ```bash
   docker run -d --name haproxy \
     -v $(pwd)/haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg:ro \
     -v /etc/haproxy/certs:/etc/haproxy/certs:ro \
     -p 443:443 \
     haproxy:latest
   ```

## Security Notes

- Never commit SSL private keys or certificates to the repository
- The `.gitignore` excludes `*.pem`, `*.key`, and certificate directories
- Use environment variables or Docker secrets for sensitive configuration
