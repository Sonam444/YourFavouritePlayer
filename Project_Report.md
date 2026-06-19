# Your Favourite Player
### Football Player Card Voting Web Application — Project Report

---

## Team Members

| No. | Name |
| --- | --- |
| 1 | TAMANG SONAM |
| 2 | TAMANG RUPESH |
| 3 | SHRESTHA CHIRANJIBI |

**Live deployment:** https://yourfavouriteplayer-production-6701.up.railway.app/

---

## 1. Project Overview

**Your Favourite Player** is a web application where football fans can create player cards, vote for their favourite players, and discuss them through comments. Each user signs up for an account, logs in, and can upload a player card with a photo and details (name, club, position, country, and description).

Every logged-in user can vote for the players they like, and the application ranks all player cards by their number of votes. The most-voted player is always shown first and is clearly marked as the **Winner**, so the "best" player rises to the top automatically as people vote. Visitors can also search the cards by keyword and read the comments left by other users.

The project is built with **Spring Boot** following the **MVC (Model–View–Controller)** pattern in clearly separated layers (entity, repository, service, controller), uses a **PostgreSQL** database, and is deployed live on **Railway**.

---

## 2. Main Features

- **User authentication** — sign up, log in, and log out, secured with Spring Security. Passwords are never stored as plain text; they are hashed using **BCrypt**.
- **Player card management (CRUD)** — logged-in users can create, view, edit, and delete player cards. Only the creator of a card is allowed to edit or delete it.
- **Image upload** — each card can include a player photo. Uploaded images are validated (type and extension), saved with a unique name, and served back to the browser through a dedicated `/uploads/**` path.
- **Voting system** — logged-in users can vote for a player. Each user can vote only once per player (a toggle: voting again removes the vote). A database rule guarantees this "one vote per user per card."
- **Automatic ranking & winner** — the home page lists player cards ordered by vote count, highest first. The top card is highlighted as the **Winner**, and the rest are numbered (#2, #3, …).
- **Keyword search** — users can search cards by name, club, country, position, or description. Results stay ranked by votes.
- **Comments** — logged-in users can comment on a player card. A comment can be deleted by its author or by the card's creator. Comments are shown oldest-first.
- **Security protections** — form pages are protected against CSRF attacks, protected routes require login, and redirect targets are validated to prevent open-redirect issues.
- **Responsive web design** — server-rendered pages built with Thymeleaf and custom CSS, including a horizontal "player strip" ranking layout and a winner banner.

---

## 3. Technologies Used

| Category | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot (Spring MVC, Spring Data JPA, Spring Security, Spring Validation) |
| View / Templating | Thymeleaf + CSS |
| ORM | Hibernate (JPA) |
| Database | PostgreSQL (production & local), H2 (automated tests) |
| Build tool | Gradle (Gradle Wrapper) |
| Security | Spring Security with BCrypt password hashing, CSRF protection |
| Boilerplate reduction | Lombok |
| Local database | Docker Compose (PostgreSQL container) |
| Version control | Git & GitHub |
| Code editor / IDE | Visual Studio Code (VS Code) and IntelliJ IDEA |
| Deployment | Railway (live), with Render configuration also provided |

---

## 4. Database Design

The application uses four main tables. Hibernate creates and updates the schema automatically (`ddl-auto=update`).

**Entities and relationships**

- An **AppUser** can create many **PlayerCards**, cast many **Votes**, and write many **Comments**.
- A **PlayerCard** belongs to one creator (AppUser) and has many **Votes** and many **Comments**.
- A **Vote** links one user to one player card, with a uniqueness rule so a user cannot vote twice for the same card.
- A **Comment** links one user to one player card.

```
AppUser 1 ──< PlayerCard 1 ──< Vote >── 1 AppUser
                    │
                    └──< Comment >── 1 AppUser
```

**Table: `app_users`**

| Column | Type | Notes |
| --- | --- | --- |
| id | BIGINT | Primary key (auto-generated) |
| username | VARCHAR(40) | Required, unique |
| email | VARCHAR(120) | Required, unique |
| password | VARCHAR | Required (BCrypt hash) |
| created_at | TIMESTAMP | Set automatically on creation |

**Table: `player_cards`**

| Column | Type | Notes |
| --- | --- | --- |
| id | BIGINT | Primary key (auto-generated) |
| name | VARCHAR(80) | Required |
| club | VARCHAR(80) | Required |
| position | VARCHAR(40) | Required |
| country | VARCHAR(80) | Required |
| description | VARCHAR(1200) | Optional |
| image_path | VARCHAR | Path to the uploaded photo |
| created_at | TIMESTAMP | Set automatically on creation |
| creator_id | BIGINT | Foreign key → `app_users.id` |

**Table: `votes`**

| Column | Type | Notes |
| --- | --- | --- |
| id | BIGINT | Primary key (auto-generated) |
| player_card_id | BIGINT | Foreign key → `player_cards.id` |
| user_id | BIGINT | Foreign key → `app_users.id` |
| created_at | TIMESTAMP | Set automatically on creation |
| — | — | **Unique (player_card_id, user_id)** → one vote per user per card |

**Table: `comments`**

| Column | Type | Notes |
| --- | --- | --- |
| id | BIGINT | Primary key (auto-generated) |
| content | VARCHAR(600) | Required |
| created_at | TIMESTAMP | Set automatically on creation |
| player_card_id | BIGINT | Foreign key → `player_cards.id` |
| user_id | BIGINT | Foreign key → `app_users.id` |

---

## 5. Screenshots

> _Screenshots will be added here. Replace each placeholder below with your image._

**5.1 Home / Ranking page (winner shown first)**

`[ Screenshot placeholder — paste image here ]`

*Figure 1: Ranking page listing player cards by votes, with the winner highlighted.*

**5.2 Player detail page (votes & comments)**

`[ Screenshot placeholder — paste image here ]`

*Figure 2: Player card detail page with vote button and comments.*

**5.3 Create / Edit player card form**

`[ Screenshot placeholder — paste image here ]`

*Figure 3: Form for creating a new player card with image upload.*

**5.4 Sign up page**

`[ Screenshot placeholder — paste image here ]`

*Figure 4: User registration page.*

**5.5 Login page**

`[ Screenshot placeholder — paste image here ]`

*Figure 5: User login page.*

**5.6 Live deployment on Railway**

`[ Screenshot placeholder — paste image here ]`

*Figure 6: The application running live at the Railway deployment URL.*

---

## 6. Development Process

1. **Planning & design** — We defined the app idea, listed the required features (authentication, database, CRUD, image upload, search, web design, deployment), and designed the four database tables and the page flow.
2. **Project setup** — We created a Spring Boot project managed with Gradle and organised the code into MVC layers: `entity`, `repository`, `service`, `controller`, plus `dto` and `config`.
3. **Database layer** — We built the JPA entities (AppUser, PlayerCard, Vote, Comment) and Spring Data repositories, including custom queries for ranking and keyword search.
4. **Business logic** — We implemented services for registration, player CRUD, image storage, voting, and comments, keeping validation and ownership rules in one place.
5. **Web layer & UI** — We wrote Spring MVC controllers and Thymeleaf templates with CSS for the ranking page, detail page, forms, login, and signup.
6. **Security** — We configured Spring Security for signup/login/logout, password hashing, route protection, and CSRF protection.
7. **Testing** — We ran the automated tests and the production build (`gradlew test` and `gradlew clean bootJar`) to confirm the application compiles and runs.
8. **Version control** — We tracked the project with Git and pushed the source code to GitHub.
9. **Deployment** — We deployed the application to **Railway**, supplying the PostgreSQL connection through environment variables, and verified it runs live. A Render configuration was also prepared as an alternative.

Throughout development we used **Visual Studio Code** and **IntelliJ IDEA** as our editors, with **Docker Compose** providing a local PostgreSQL database for testing.

---

## 7. Problems and Solutions

| Problem | Solution |
| --- | --- |
| **Database credentials differed between machines and the cloud.** Hard-coding a username/password did not work everywhere. | We externalised the database settings using environment variables (`SPRING_DATASOURCE_*` locally and `PGHOST`/`PGPORT`/`PGDATABASE`/`PGUSER`/`PGPASSWORD` on Railway), so each environment supplies its own credentials. |
| **Users could vote multiple times for the same player**, which made the ranking unfair. | We added a unique database constraint on `(player_card_id, user_id)` and made voting a toggle, so each user has exactly one vote per card. |
| **Lazy-loading error on the player detail page** when displaying related votes and comments. | We enabled `open-in-view` so related data loads correctly while the page is being rendered. |
| **Anyone could edit or delete other users' content.** | We added ownership checks: only the card creator can edit/delete a card, and only the comment author or the card creator can delete a comment. |
| **Uploaded images needed to be safe.** | We validated file type and extension, generated unique file names, blocked path-traversal attempts, and served uploads through a dedicated resource handler. |
| **The local IDE build failed to start with a loopback connection error.** | We found the local antivirus was blocking Java's internal loopback sockets used by the build process, and resolved it by excluding the Java toolchain and IDE from the antivirus. The cloud build on Railway was unaffected. |

---

## 8. Conclusion

This project gave us hands-on experience building a complete, database-backed web application with **Spring Boot** from start to finish. We applied the **MVC** pattern with clean layered architecture, implemented secure user authentication, full CRUD with image upload, a fair voting and ranking system, search, and comments — and successfully deployed the result to the cloud on **Railway**.

Along the way we learned how the pieces of a real application fit together: JPA entities and relationships, Spring Data repositories with custom queries, service-layer business rules, Spring Security, server-side rendering with Thymeleaf, and environment-based configuration for deployment. We also gained practical problem-solving experience with database configuration, authorization rules, and development-environment issues.

As future improvements, we could add player categories or leagues, pagination for large lists, user profile pages, and cloud storage for images so that uploaded photos persist permanently in production.

---

*Prepared by Team — TAMANG SONAM, TAMANG RUPESH, SHRESTHA CHIRANJIBI.*
