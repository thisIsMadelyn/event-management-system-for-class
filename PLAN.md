# Event Management System : Execution Plan

A JavaFX desktop app for the HCI course (deliverable 3). It implements one main scenario end to end.
Solo coursework; the output is the source code, project files and run instructions.

**Active phases:** 7
**Open:** 8 (final README pass and the launch check from the README)

Note (2026-11-24): Phases 2–6 reopened to align W1–W3 with the report (INC-007).
Ticked boxes record the earlier build. Phase 9 (W4) was added on the same day
at the developer's request. Phases 2–6 and 9 were closed again the same day,
after 86 of 86 tests passed, and Phase 7 became active.

**Deadline:** Sunday 6 December 2026, 24:00, via Moodle
**Target completion:** Wednesday 2 December 2026 (4 days of buffer)

---

## How to use this file

Every session starts by reading the **Active phases** line and working inside
those phases only. **Open** lists phases with unticked boxes that are not being
worked right now — do not pick one up without saying so first.

A phase is done when its **Done when** list is fully satisfied — not when the
code "works". If a phase slips, cut scope inside that phase; do not borrow time
from a later one.

Only `/wrap-up` edits this file. It may:
- tick a **Done when** box whose condition was verified this session
- change the **Active phases** and **Open** lines
- amend a line, **with a dated note saying what changed and why**

It may not add phases, reorder them, rewrite a goal, or tick a box it did not
verify. Those are your calls.

---

## 1. Hard constraints

| Constraint | Consequence if missed |
|---|---|
| Submit by Sunday 6 December 2026 | Deliverable 3 is 40% of the grade |
| At least 3 distinct working screens with navigation between them | A required brief item is missing |
| One data entry screen (W2 registration form) | A required brief item is missing |
| One screen that displays or searches data (W1 search, W3 list) | A required brief item is missing |
| Input validation | A required brief item is missing |
| Feedback after important actions: success, errors, event full, cancel confirmation | A required brief item is missing |
| Source code, project files and short run instructions | A required brief item is missing |
| Public repository, reviewed by the professor | Anything committed is seen and graded |

- **Nothing beyond the brief is built.** Extra features cost time before the
  deadline and are not graded. Suggestions go in a separate list instead.
- **Only JDK 21 may be needed to run it.** The marker runs it from the README.
  Keep to the Maven Wrapper and the two JavaFX modules.

---

## 2. Architecture, as locked

**Scenario S1:** an attendee (Yorikas) browses events, opens one, registers for
it and sees it in *My registrations*. The screens follow the low-fidelity
wireframes:

- **W1 Browse events:** event list with search
- **W2 Event details and registration:** event details plus the registration form
- **W3 My registrations:** list of the attendee's registrations, with cancel
- **W4 Create event:** form with which an organizer publishes an event. Outside S1
  (added 2026-11-24)

**Stack:** Java 21 (LTS), JavaFX 21.0.6 (`javafx-controls` and `javafx-fxml`
only), Maven via the included wrapper. Other libraries would add setup for
whoever runs the app.

**Data:** in-memory sample data in `DataStore`, no database. The brief does not
ask for persistence.

**Screens:** one FXML file and one controller per wireframe, so each screen can
be checked against its wireframe.

**Validation:** plain Java, no validation library.

**Feedback:** labels on the screen, plus a confirmation dialog for cancel.

**Look:** plain CSS (greys, simple borders) to stay close to the low-fidelity
wireframes.

**Scope cap:** the brief's requirement list is the upper bound. Never cut: S1
working end to end across W1, W2 and W3. Cut first: <...>

---

## 3. Phases

### Phase 1 — Setup · Mon 23 Nov

**Goal:** the project builds and opens an empty app window, with only the libraries the brief needs.

Artifacts:
- `pom.xml`, `module-info.java` — trimmed to `javafx-controls` and `javafx-fxml`
- `EventApplication.java`, `main-view.fxml` — empty main window

Done when:
- [x] `mvnw clean javafx:run` opens a window titled "Event Management System"

---

### Phase 2 — Data · Tue 24 Nov

**Goal:** the app has sample events and registrations in memory, and a full event cannot be registered for.

Artifacts:
- `model/Event.java`, `model/Registration.java`, `model/RegistrationResult.java`, `model/DataStore.java`

Done when:
- [x] `DataStore` loads 6 sample events, one of them full
- [x] Registering for the full event is refused and changes nothing

Note (2026-11-22): the original line was "The app starts with sample data". No
screen shows data yet, so the check was done with a throwaway program against
the compiled classes. Seeing the data in the running app moved to Phase 3.

---

### Phase 3 — W1 Browse events · Wed 25 Nov

**Goal:** the attendee sees the list of events and can search it.

Artifacts:
- `browse-view.fxml` and its controller

Done when:
- [x] The running app shows the 6 sample events
- [x] Typing in the search box filters the list by title or location
- [x] "No events found" shows when nothing matches

Note (2026-11-24): rebuilt from the W1 wireframe. Search runs on Search or Enter,
by title or keyword, category and date, and no longer by location. The empty
message is now "No events match your search." The risk below is closed.

Risk: the row content and layout are guessed, because the W1 wireframe has not
been checked yet. Mitigation: get the W1 wireframe before building the screen.

---

### Phase 4 — W2 Details and registration · Fri 27 Nov

**Goal:** the attendee sees an event's details and can register through a validated form.

Artifacts:
- `details-view.fxml` and its controller

Done when:
- [x] Invalid input shows a message next to the field and blocks submit
- [x] A valid submit shows a success message
- [x] A full event shows "Event full" and disables the form

Note (2026-11-24): rebuilt from the W2 wireframe. The phone field is gone, number
of places and needs were added, and a success opens W3 with a banner. The risk
below is closed.

Risk: the form fields (full name, email, phone) are guessed. Mitigation: check
them against the W2 wireframe before building the form.

---

### Phase 5 — W3 My registrations · Sat 28 Nov

**Goal:** the attendee sees their registrations and can cancel one after confirming.

Artifacts:
- `registrations-view.fxml` and its controller

Done when:
- [x] Cancel asks for confirmation
- [x] Yes removes the registration and frees the seat
- [x] No changes nothing
- [x] An empty list shows a message

Note (2026-11-24): rebuilt from the W3 wireframe. The list is a table, and the
dialog's buttons are "Keep registration" (No) and "Cancel registration" (Yes).

---

### Phase 6 — Navigation · Sun 29 Nov

**Goal:** the three screens are connected in one window following S1.

Artifacts:
- `Navigator` — swaps screens in the main window

Done when:
- [x] W1 → W2 → W3 → W1 works
- [x] Every screen has a way back

Note (2026-11-24): a shared navigation bar replaced the separate buttons, and W4
was added to it.

---

### Phase 7 — End-to-end check · Mon 30 Nov

**Goal:** every brief requirement is confirmed from a real run.

Artifacts:
- Checklist run against §1, with a screenshot of each screen

Done when:
- [ ] Every requirement in §1 is ticked from a real run

---

### Phase 8 — README · Tue 1 Dec

**Goal:** someone else can run the app from the instructions alone.

Artifacts:
- `README.md` — requirements, run commands, description of S1

Done when:
- [ ] Someone with only JDK 21 installed can launch the app from the README

---

### Phase 9 — W4 Create event · Tue 24 Nov

**Goal:** an organizer can publish an event from one validated form, following the W4 wireframe.

Artifacts:
- `create-event-view.fxml` and its controller, `model/EventValidator.java`

Done when:
- [x] Invalid input shows a message next to each wrong field and nothing typed is lost
- [x] A published event shows a success message and appears on Browse Events

Note (2026-11-24): the brief does not require W4. It was added so that the app
matches the wireframes. Scenario 2 is not implemented.
