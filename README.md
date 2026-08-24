# ShareText

## Configuration

The application uses browser-side AES-256-GCM for end-to-end encrypting shared content. A fresh key is generated for each share and placed in the URL fragment, which is not sent in HTTP requests. The server stores and returns only ciphertext.

```powershell
$env:DB_PASSWORD = '<database-password>'
```

Use HTTPS for all client connections. Cloud Run provides HTTPS at the service boundary; production secrets should be bound from Secret Manager as configured in `cloudbuild.yaml`.

Existing rows created before this encryption change contain plaintext and must be migrated or deleted before deployment.
