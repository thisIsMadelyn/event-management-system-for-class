# Event Management System : Execution Plan

A JavaFX desktop app for the HCI course (deliverable 3). It implements one main scenario end to end.

**Deadline:** Sunday 6 December 2026
**Target completion:** Wednesday 2 December 2026 (4 days of buffer)

## Scenario

**S1:** an attendee (Yorikas) browses events, opens one, registers for it and sees it in *My registrations*.
The screens follow the low-fidelity wireframes:

- **W1 Browse events:** event list with search
- **W2 Event details and registration:** event details plus the registration form
- **W3 My registrations:** list of the attendee's registrations, with cancel

## Requirements from the brief

| Requirement | Covered by |
|---|---|
| At least 3 distinct working screens with navigation between them | W1, W2, W3 (steps 3–6) |
| One data entry screen | W2 registration form (step 4) |
| One screen that displays or searches data | W1 search, W3 list (steps 3, 5) |
| Input validation | W2 form (step 4) |
| Feedback after important actions: success, errors, event full, cancel confirmation | Steps 4, 5 |
| Source code, project files and short run instructions | Repository, README (step 8) |

Nothing beyond this list is in scope.

## Stack

- Java 21 (LTS)
- JavaFX 21.0.6, `javafx-controls` and `javafx-fxml` only
- Maven via the included wrapper (`mvnw`), no global Maven needed
- In-memory sample data, no database

## Steps

| # | Step | Output | Done when | Target |
|---|---|---|---|---|
| 1 | Setup | Trimmed `pom.xml` and `module-info.java`; starter files replaced with an empty main window | `mvnw clean javafx:run` opens an empty window titled "Event Management System" | Mon 23 Nov |
| 2 | Data | `Event`, `Registration`, `DataStore` with about 6 sample events, one of them full | The app starts with sample data; registering for the full event is refused | Tue 24 Nov |
| 3 | W1 Browse events | `browse-view.fxml` and its controller: event list and search box | Typing filters the list by title or location; "No events found" shows when nothing matches | Wed 25 Nov |
| 4 | W2 Details and registration | `details-view.fxml` and its controller: event details and form | Invalid input shows a message next to the field and blocks submit; a valid submit shows a success message; a full event shows "Event full" and disables the form | Fri 27 Nov |
| 5 | W3 My registrations | `registrations-view.fxml` and its controller: registrations list and Cancel | Cancel asks for confirmation; Yes removes the registration and frees the seat; No changes nothing; an empty list shows a message | Sat 28 Nov |
| 6 | Navigation | `Navigator`: swaps screens in one window | W1 → W2 → W3 → W1 works, and every screen has a way back | Sun 29 Nov |
| 7 | End-to-end check | Checklist run against the brief, screenshot of each screen | Every requirement above is ticked from a real run | Mon 30 Nov |
| 8 | README | `README.md`: requirements, run commands, description of S1 | Someone with only JDK 21 installed can launch the app from it | Tue 1 Dec |

## Design decisions

- One FXML file and one controller per wireframe.
- Validation is plain Java, with no validation library.
- Feedback uses labels on the screen, plus a confirmation dialog for cancel.
- Plain CSS (greys, simple borders) to stay close to the low-fidelity wireframes.

## Open items

- W2 form fields, the W1 row content and the layout of each screen are to be confirmed against the wireframes.
