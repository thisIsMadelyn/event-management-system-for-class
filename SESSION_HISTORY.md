# Session History

A running log of every Claude Code session on this repository. This file is
public — keep entries factual and free of secrets, keys, or internal-only detail.

Each session is logged as it ends. Sessions are listed newest first.

## Format

```
## Session [YYYY-MM-DD-HHMM]

- **Start status:** <commit, tree state, active phases, and what was open>
- **End status:** <commit, tree state, what closed, what stayed open and why>
- **Badges:**
  - 🆕 Addition — <short description> (`<sha>`)
  - 🐛 Bug fix — <short description> (`<sha>`)
  - <plain bullet for a change that is neither: a move, a rename, a
    deletion, a config change, a PLAN tick> (`<sha>`)
```

Badge legend: 🆕 Addition (new capability, file, or feature) · 🐛 Bug fix
(corrects a defect in existing work). A session may carry zero, one, or several
badges of either kind. Changes that are neither get a plain bullet.

**Rules**
- Append only. Never edit or delete a past entry.
- Reference commits by short SHA, in parentheses, after the claim.
- Say what stayed open and why, not only what closed.
- No keys, no paths that are not already public in the repo, no internal-only
  detail.

---

## Session [2026-11-24-1730]

- **Start status:** `5bc2203`, tree clean, Phase 7 active and Phase 8 open.
- **End status:** work at `78c50a9`. Phase 7 is closed: a fresh clone of the public
  repository launched with `./mvnw.cmd clean javafx:run`, and its app passed
  25 of 25 checks against PLAN §1, with a screenshot of each screen. The
  developer had also confirmed W1 to W4 by hand, before the last style fix. Still
  open:
  - **Phase 8** (README) is next. The launch check on a machine with only JDK 21
    is not done.
  - The "Submit" row of §1 is confirmed at submission, as noted in Phase 7.
- **Badges:**
  - 🐛 Bug fix — the W1 scroll bar no longer touches the View details buttons
    (`78c50a9`)
  - PLAN notes on W4 and on the "Submit" row; INC-017 logged (`1602431`)
  - PLAN ticks: Phase 7 complete (this wrap-up's docs commit)

## Session [2026-11-24-1548]

- **Start status:** `2a887c8`, tree clean, Phase 7 active and Phase 8 open. An
  audit on 23 November (no repository changes) had found that the app met the
  brief's minimum but passed only 1 of 21 checks against the report.
- **End status:** work at `0e196b8`. Phases 2–6 were reopened, rebuilt from the
  report and its wireframes, and closed again. Phase 9 (W4) was added and closed.
  The updated tests passed 86 of 86, including 21 of 21 report checks (INC-007
  closed). Still open:
  - **Phase 7** (end-to-end check with screenshots) is next.
  - **Phase 8** needs a launch check from the README on a machine with only JDK 21.
  - FR19 is not implemented, because no wireframe shows it. Scenario 2 is not
    implemented; only its screen, W4, is.
  - The developer confirmed W1 and W2 by hand. No result was recorded for the
    manual tests of W3 and W4.
- **Badges:**
  - 🆕 Addition — data model from the report: categories, end times, places,
    needs, and sample events dated relative to today (`62d40d1`)
  - 🐛 Bug fix — duplicate registrations refused; malformed emails refused and
    accented names accepted; phone removed (`62d40d1`)
  - 🆕 Addition — shared navigation bar, 1280x800 window, text of at least 14px
    (`70adccf`)
  - 🆕 Addition — W1 rebuilt: keyword, category and date search, View details,
    Clear filters (`30e137c`)
  - 🐛 Bug fix — right-click no longer opens an event; the search is kept on Back
    (`30e137c`)
  - 🆕 Addition — W2 rebuilt: two columns, places, needs, required markers,
    success opens W3 (`d42f6af`)
  - 🐛 Bug fix — W2 and the navigation bar no longer cut off in narrow windows
    (`d42f6af`)
  - 🆕 Addition — W4 Create event, at the developer's request (`f35e672`)
  - 🆕 Addition — W3 rebuilt: table, banner, cancel dialog, empty state
    (`057db1b`)
  - 🐛 Bug fix — `mvnw` is executable in git, not tested on macOS or Linux (`0e196b8`)
  - Removed unused JUnit dependencies (`2de950d`)
  - Incident log: INC-007 to INC-016 (`7e3ebf1`, `0e9e961`, `dfa7525`, `c5b2b5b`,
    `15264b2`, `093bd83`, `e1dea27`, `02b7ac8`)
  - `README.md`, `CLAUDE.md` and `PLAN.md` updated for four screens; PLAN
    deadline time filled in (`e1dea27`, `0e196b8`)
  - PLAN ticks: Phase 9 complete; notes on Phases 3–6 (this wrap-up's docs commit)

## Session [2026-11-22-2015]

- **Start status:** no commits yet. IntelliJ's JavaFX starter project was staged
  but not committed, there were no tracking docs, and the 8-step plan existed
  only in the brief.
- **End status:** work at `7c2cb45`, tree clean. Phases 1–6 are closed: setup,
  data, W1, W2, W3 and navigation, with scenario S1 confirmed manually in the
  running app. Still open:
  - **Phase 7** (end-to-end check against the brief) is next.
  - **Phase 8** needs a final README pass and a launch check from the README
    alone.
  - The W1 row content and the W2 form fields are still guesses, because no
    wireframe was checked this session.
- **Badges:**
  - Initial commit of the IntelliJ JavaFX starter project; remote and `main`
    branch set up (`07adbb7`)
  - Removed 10 unused libraries; only `javafx-controls` and `javafx-fxml`
    remain (`7ec4501`)
  - 🆕 Addition — empty "Event Management System" window, `PLAN.md` and
    `README.md` (`7ec4501`)
  - 🆕 Addition — data model: `Event`, `Registration`, `DataStore` with 6 sample
    events, one of them full (`c0bceaa`)
  - 🆕 Addition — tracking docs (`CLAUDE.md`, `SESSION_HISTORY.md`) and the
    Claude Code skills in `.claude/`, made public for transparency (`34eee74`)
  - `PLAN.md` converted to phases; `.gitignore` keeps `CHECKPOINT.md` local;
    git hook disabled after it blocked commands on Windows (INC-001) (`34eee74`)
  - 🆕 Addition — W1 Browse events with search and "No events found" (`b25727b`)
  - 🆕 Addition — W2 Event details and registration, with field validation,
    success message and "Event full" (`b7dad83`)
  - 🆕 Addition — W3 My registrations with cancel, confirmation dialog and
    empty-list message; `DataStore.cancel()` (`2386fc3`)
  - 🆕 Addition — `REPORTS.md` incident log (INC-001 to INC-005), and
    `CLAUDE.md` updated to require it (`2386fc3`)
  - 🆕 Addition — navigation between W1, W2 and W3; this made registration
    reachable in the app and resolved INC-006 (`7c2cb45`)
  - PLAN ticks: Phases 3, 4, 5 and 6 complete (this wrap-up's docs commit)
