# CLAUDE.md

Operating instructions for Claude Code in this repository.

---

## What this is

**Event Management System** is a desktop app where an attendee browses a list of
events, opens one to see its details, registers for it through a form and then
sees the registration under *My registrations*, where it can be cancelled. It
implements one complete scenario (S1) across three screens (W1 Browse events,
W2 Event details and registration, W3 My registrations), following low-fidelity
wireframes.

HCI course assignment, deliverable 3 (40% of the grade). Deadline: Sunday
6 December 2026. The development process is kept public on purpose so that it is
transparent to the course staff.

Read `PLAN.md` for the active phases before starting work.

---

## Document map

| File | Committed | Purpose |
|---|---|---|
| `CLAUDE.md` | yes | Operating instructions. Static. Changes only when an invariant changes. |
| `PLAN.md` | yes | Implementation plan by phase. The **Active phases** line bounds all work. |
| `SESSION_HISTORY.md` | yes | Public, append-only log. One entry per session. |
| `CHECKPOINT.md` | **no — gitignored** | Local handover note. Overwritten in full each session. Read only when its first line says so. |

`CHECKPOINT.md` is never staged, committed or pushed.

---

## Session protocol

**Start**
1. Read the first line of `CHECKPOINT.md`. If **Context for next session** is
   `yes`, read the rest of it. If `no`, skip the file.
2. Read `PLAN.md`. Work only inside the phases on the **Active phases** line.

**During**
3. State what you are about to change before changing it.
4. One concern per change. When the user reports one issue, fix that issue only.
5. Propose any broader refactor before doing it — do not bundle it in.
6. Run the relevant check before claiming something works.

**End**
7. Run `/wrap-up`. Do not hand-edit `CHECKPOINT.md` or `SESSION_HISTORY.md`.

---

## Non-negotiable constraints

- **Deadline is Sunday 6 December 2026.** This is deliverable 3, worth 40% of
  the course grade. A late or incomplete submission costs that grade.
- **Implement only what the brief requires.** At least 3 screens with
  navigation, one data entry screen, one screen that displays or searches data,
  input validation, feedback messages (success, errors, event full, cancel
  confirmation), and source code with short run instructions. Anything extra
  costs time the deadline does not have. Suggestions go in a separate list,
  never into the code.
- **The repository is public and the professor reviews it.** Everything
  committed is graded material. No personal data, no secrets, no unrelated
  files.
- **Java 21 and JavaFX `javafx-controls` + `javafx-fxml` only.** No other
  libraries. The app must run from the README with nothing but JDK 21 installed.
- **One step at a time, and ask before creating, changing or deleting any
  file.** The user approves each step before the next one starts. After each
  step, report what was actually run and what was not.

---

## Architecture invariants

- **`DataStore` is the only place app data lives.** Events and registrations are
  held in memory there; screens read and change data only through it. Every run
  starts from the same sample data.
- **A seat is taken only through `DataStore.register()`.** `Event.takeSeat()` is
  package-private so the full-event check cannot be bypassed.
- **One FXML file and one controller per wireframe screen.** W1, W2 and W3 each
  map to one view, so the code can be checked against the wireframes.

---

## Single sources of truth

- `pom.xml` — Java version (21) and JavaFX version (21.0.6). Never set versions
  anywhere else.
- `src/main/java/org/example/demo/model/DataStore.java` — the sample events.
  Never hard-code event data in a screen or controller.
- `PLAN.md` — scope and progress. Never track progress in another file.

---

## Commands

The only entry points. Keep them working. They need `JAVA_HOME` pointing to
JDK 21.

```
mvnw.cmd clean javafx:run      build and launch the app (Windows; ./mvnw on macOS/Linux)
mvnw.cmd clean compile         compile only
```

If a command does not exist yet, add it to `pom.xml` rather than documenting a
raw invocation.

---

## Code conventions

- Model classes in `org.example.demo.model` are plain Java with no JavaFX
  imports.
- FXML layouts live in `src/main/resources/org/example/demo/`.

---

## Do not

- Add a dependency without asking first.
- Create, rewrite or delete a file the user did not ask about.
- Refactor outside the active phases.
- Commit `CHECKPOINT.md`.
- Commit or push without asking first. Work is committed to `main`.
- Add features beyond the brief's requirements.
- Commit personal data, local tooling paths or files unrelated to the project.
