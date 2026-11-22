# Incident Reports

A log of what went wrong during development and how it was handled. It is kept
public on purpose, as part of making the development process transparent.

Each entry records what happened, its impact, the cause, how it was resolved and
how the fix was verified. If a cause or fix was not confirmed, the entry says so.
Entries are listed newest first.

## Format

```
## INC-<NNN> · <YYYY-MM-DD> · <short title>

- **Phase:** <phase or activity>
- **Status:** resolved | resolved, cause unconfirmed | open
- **What happened:** <observed behaviour, exact error text if short>
- **Impact:** <what it broke or put at risk>
- **Cause:** <root cause, or "not confirmed" with the likely candidates>
- **Resolution:** <what was done, including attempts that did not work>
- **Verified by:** <the check that shows it is fixed, or "not verified">
- **Follow-up:** <anything still open, or "none">
```

---

## INC-006 · 2026-11-22 · Registration not reachable in the running app

- **Phase:** 5 (W3 My registrations), found during manual testing
- **Status:** resolved in Phase 6
- **What happened:** during a manual test after Phase 5, the developer found no
  way to register for an event. Only W1 (Browse events) can be reached.
  Clicking an event does nothing.
- **Impact:** the main scenario (S1) cannot be tried end to end in the app. W2
  and W3 exist and passed automated tests, but a user cannot reach them.
- **Cause:** build order. The plan builds each screen first (Phases 3–5) and
  connects them in Phase 6 (navigation). Until then the main window shows only
  W1. This was mentioned before the manual test but was not obvious from the
  app itself.
- **Resolution:** Phase 6 added `Navigator`, which switches screens in the
  main window. Clicking an event in W1 (or pressing Enter on it) opens W2. W1
  and W2 have a "My registrations" button, and W2 and W3 have
  "← Back to events".
- **Verified by:** an automated end-to-end test of S1 through the real
  application class (18 of 18 checks passed), then a manual test of the whole
  scenario in the running app by the developer, who confirmed it works.
- **Follow-up:** none.

## INC-005 · 2026-11-22 · W3 empty-list message missing in automated test

- **Phase:** 5 (W3 My registrations)
- **Status:** resolved: the test was wrong, not the app
- **What happened:** the automated W3 test cancelled the last registration and
  immediately checked for "You have no registrations yet.". The check failed
  and the screenshot showed an empty list with no message.
- **Impact:** none on the app. It briefly looked like a missing required
  feedback message.
- **Cause:** test timing. The check ran in the same instant the confirmation
  dialog closed, before JavaFX had redrawn the list.
- **Resolution:** the test was changed to check again one frame later, and a
  second case was added in which the screen opens with no registrations. No app
  code was changed.
- **Verified by:** both checks passed, and the screenshot shows the message.
- **Follow-up:** none.

## INC-004 · 2026-11-22 · App would not start for the developer

- **Phase:** 3 (W1 Browse events)
- **Status:** resolved, cause unconfirmed
- **What happened:** the developer reported that the app was not running.
- **Impact:** blocked the developer's manual check of W1.
- **Cause:** not confirmed. The app itself started correctly when launched with
  `JAVA_HOME` set. Likely causes were (a) `JAVA_HOME` not being set, which stops
  `mvnw` (see INC-002), or (b) an IntelliJ run configuration still pointing to
  the `HelloApplication` class removed in Phase 1.
- **Resolution:** the app was launched successfully to show that the code was
  fine, and both likely causes and their fixes were explained.
- **Verified by:** the developer then ran the manual check successfully.
- **Follow-up:** none. The exact cause was not reported back.

## INC-003 · 2026-11-22 · Automated UI test sent keystrokes outside the app

- **Phase:** 3 (W1 Browse events)
- **Status:** resolved; effect on other windows unconfirmed
- **What happened:** to check the search box, a test script typed into the app
  using operating-system keystrokes and took screenshots of the screen. The app
  window was not in front when the script started. As a result:
  - the first screenshot captured another application's window instead of the app
  - some keystrokes, including select-all and backspace, did not reach the
    search box and may have gone to another window
- **Impact:** a screenshot of unrelated personal content was saved locally (it
  was never committed), and keystrokes may have reached another application.
  The check of the "No events found" message did not complete.
- **Cause:** keystrokes sent by the operating system go to whichever window has
  focus, and the script could not guarantee that the app had it.
- **Resolution:**
  - the stray screenshot was deleted
  - the developer checked "No events found" manually
  - every later UI test (Phases 4 and 5) drives the screen from inside the app
    and sends no operating-system keystrokes
- **Verified by:** the Phase 4 and 5 tests ran with no input outside the app.
- **Follow-up:** whether a stray keystroke changed anything in another
  application was not confirmed.

## INC-002 · 2026-11-22 · Maven Wrapper fails: `JAVA_HOME` not set

- **Phase:** 1 (Setup)
- **Status:** worked around
- **What happened:** `mvnw.cmd clean compile` failed with
  `Error: JAVA_HOME not found in your environment.`
- **Impact:** the project cannot be built or run with `mvnw` from a plain
  terminal on the development machine.
- **Cause:** JDK 21 is installed and `java` is on the PATH, but the `JAVA_HOME`
  environment variable is not set. The Maven Wrapper needs it.
- **Resolution:** `JAVA_HOME` is set for each command that runs `mvnw`. The
  README explains how to set it.
- **Verified by:** every build and run since then succeeded with `JAVA_HOME`
  set.
- **Follow-up:** `JAVA_HOME` is still not set permanently on the development
  machine (developer's choice).

## INC-001 · 2026-11-22 · Git safety hook blocked all shell commands

- **Phase:** setup of the Claude Code tooling, before Phase 1
- **Status:** resolved: hook removed
- **What happened:** a Claude Code hook (`.claude/hooks/main-guard.sh`) was
  added to stop commits and pushes on `main`/`master`. On Windows it could not
  start, and because it was set to block on failure, every shell command was
  refused.
- **Impact:** shell commands, and then all `git` commands, were blocked until
  the hook was fixed or removed.
- **Cause:** the hook is a bash script, and Windows cannot run a `.sh` file
  directly. Three attempts failed in turn:
  1. calling the script directly failed with `EFTYPE: inappropriate file type
     or format`
  2. `bash "<script>"` as the command failed with `ENOENT`. Because of the
     `args` field, the hook runs the command directly and not through a shell,
     so the whole string was treated as one program name. This was a mistake
     in the first fix.
  3. `bash` with the script in `args` started Windows' WSL launcher instead of
     Git Bash. With no Linux distribution installed, that failed with
     `execvpe(/bin/bash) failed`.
- **Resolution:** the developer chose to remove the hook. `.claude/settings.json`
  is now `{}`, and the script stays in the repo unused. `jq`, which the hook
  needs, was installed earlier and is no longer required.
- **Verified by:** shell and `git` commands have run normally since then.
- **Follow-up:** none. If the hook is wanted again, it would need the full path
  to Git Bash (`C:/Program Files/Git/bin/bash.exe`) as its command.
