# QuizQuest 🧠

A class-wise, multi-level quiz platform for students. Pick a class (6–10), a subject and a
difficulty, then race an **accurate, server-timed** clock. Review every answer, climb the
leaderboard, and manage the question bank from a built-in admin screen.

## Stack

Java 21 · Spring Boot 3.3 · Spring Data JPA · H2 (file-based) · Bean Validation
Front-end: hand-written HTML + CSS + vanilla JavaScript (no build step), served by Spring Boot.

## Run

```bash
mvn spring-boot:run
```

Then open <http://localhost:8080/> for the app, or the H2 console at <http://localhost:8080/h2-console>.

The first launch seeds a starter question bank automatically. Data is stored in `./data/` (H2),
which is git-ignored.

## Test

```bash
mvn test
```

Covers the pure timing logic, the question-bank API (create/update/delete + validation), the full
start → submit → grade → leaderboard flow, and server-side quiz expiry (using a fast-forwardable clock).

## How it works

- **Question** — a multiple-choice question with a class level, subject, difficulty (EASY / MEDIUM /
  HARD, worth 1 / 2 / 3 points) and an explanation.
- **QuizSession** — one attempt. The server records `startedAt` and `durationSeconds`, so the
  **deadline and grading are decided on the server** — the browser only shows a countdown and can't
  change the outcome. Timed quizzes that are submitted late are marked `EXPIRED`.
- **QuizItem** — one question inside a session plus the student's chosen option.

## Sections (single-page UI)

1. **Home** — dashboard stats, top scorers, recent attempts, and the "start a quiz" form
   (class, subject, difficulty, question count, timed/practice).
2. **Quiz** — one question at a time, an animated timer ring, a question palette (answered / flagged
   / current), flagging, free navigation, and submit. Auto-submits when a timed quiz runs out.
3. **Result** — score ring, accuracy and time, plus a full per-question review with the correct
   answer and explanation.
4. **Leaderboard** — top attempts with medals, filterable by class and subject.
5. **Question Bank** — admin CRUD for questions with class/subject/difficulty filters.

The UI is responsive (works on phones), uses inline SVG art and icons, and animates transitions,
the timer and the score ring.

## REST API

| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/meta` | classes, subjects, difficulties, totals |
| GET | `/api/stats` | dashboard headline numbers |
| GET | `/api/questions` | browse (filters: `classLevel`, `subject`, `difficulty`) |
| POST/PUT/DELETE | `/api/questions[/{id}]` | admin create / update / delete |
| POST | `/api/quizzes` | start a quiz (returns questions without answers + deadline) |
| GET | `/api/quizzes/{id}` | resync a running quiz |
| POST | `/api/quizzes/{id}/submit` | grade and return the result + review |
| GET | `/api/quizzes/{id}/result` | fetch a finished result |
| GET | `/api/leaderboard` | ranked results (filters: `classLevel`, `subject`, `limit`) |

## Layout

```
src/main/java/com/quizquest
  domain/      JPA entities + enums (Question, QuizSession, QuizItem, Difficulty, …)
  repository/  Spring Data repositories
  dto/         request/response records
  service/     QuestionService, QuizService (timing + grading), StatsService
  web/         REST controllers + error handling
  config/      Clock bean + DataSeeder
src/main/resources/static   index.html, css/styles.css, js/app.js
src/test/java               unit + MockMvc integration tests
```

## License

MIT
