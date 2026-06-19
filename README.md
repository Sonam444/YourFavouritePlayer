# Your Favourite Player

Spring Boot football player card voting app using MVC, JPA, PostgreSQL, Thymeleaf, image upload, search, authentication, comments, and deployment configuration.

## Local Run

1. Start PostgreSQL with Docker Compose, or create a local PostgreSQL database matching `compose.yaml`.
2. Run:

```bash
./gradlew bootRun
```

Windows:

```powershell
.\gradlew.bat bootRun
```

Open `http://localhost:8080`.

## Railway

The project includes `railway.json` from the class deployment style.

Railway variables:

- `PGHOST`
- `PGPORT`
- `PGDATABASE`
- `PGUSER`
- `PGPASSWORD`

## Render

Use these build/start commands:

```bash
./gradlew clean bootJar
java -jar build/libs/*.jar
```

Set these environment variables from your Render PostgreSQL database:

- `SPRING_DATASOURCE_URL=jdbc:postgresql://HOST:PORT/DATABASE`
- `SPRING_DATASOURCE_USERNAME=USERNAME`
- `SPRING_DATASOURCE_PASSWORD=PASSWORD`

## GitHub Upload

```bash
git init
git add .
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/yourname/your-favourite-player.git
git push -u origin main
```
