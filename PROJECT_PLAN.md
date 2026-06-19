# Your Favourite Player - Design And Work Plan

## App Idea

Users sign up, log in, upload football player cards with photos, vote for favourite players, and comment on each player. The highest-voted player is shown first and marked as the winner.

## Required Feature Mapping

| Requirement | Implementation |
| --- | --- |
| User Authentication | Spring Security signup, login, logout |
| Database | PostgreSQL with Spring Data JPA |
| CRUD | Player card create, read, update, delete |
| Image Upload | Multipart upload saved to `uploads/`, displayed through `/uploads/**` |
| Search | Keyword search by player name, club, country, position, and description |
| Web Design | Thymeleaf pages with horizontal ranked player cards and winner banner |
| Deployment | GitHub source upload, Railway config from class PDF, Render env-variable support |

## Layer Plan

1. `entity` - JPA tables: `AppUser`, `PlayerCard`, `Vote`, `Comment`.
2. `repository` - `JpaRepository` CRUD plus custom `@Query` search/ranking methods.
3. `service` - Business logic for signup, image storage, player CRUD, voting, and comments.
4. `controller` - Spring MVC routes that receive requests, call services, and return Thymeleaf pages.
5. `templates` - UI screens for ranking, detail, player form, login, and signup.

## Main User Flow

1. Visitor opens the ranking page and searches player cards.
2. User signs up or logs in.
3. Logged-in user uploads a player card with an image.
4. Other logged-in users vote and comment.
5. Ranking page sorts by vote count, so the winner stays first.

## Deployment Notes

- Railway: `railway.json` and `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`.
- Render: set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD`.
- Uploaded files in `uploads/` are good for class practice, but cloud storage is better for permanent production images.
