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

## INC-029 · 2026-11-28 · Skill committed with its frontmatter still broken

- **Phase:** none (repository housekeeping)
- **Status:** resolved
- **What happened:** the first line of `.claude/skills/brief-me/SKILL.md` was
  `--` instead of `---`. A `sed` command was meant to fix it before the commit,
  but it only removed the line's carriage return. The file was committed and
  pushed in `ba63948` with the line still `--`.
- **Impact:** the skill's name and description were not read, and the pushed
  version on `main` carried the broken line until the fix commit.
- **Cause:** the `sed` pattern matched `--\r` but the replacement did not work
  as intended in Git Bash. The output was checked only after the commit and
  push, not before.
- **Resolution:** the line was changed to `---` with a direct file edit and
  committed in a follow-up commit.
- **Verified by:** the file's first bytes read `---` before the follow-up
  commit.
- **Follow-up:** check the result of a file fix before staging it.

## INC-028 · 2026-11-27 · Word spell check of the report stopped with an error

- **Phase:** report (typo check, outside the repository)
- **Status:** resolved, cause unconfirmed
- **What happened:** a spell check of the whole report, run through Word with
  the report opened read-only and no window, returned one result and then
  stopped with "Exception from HRESULT: 0x800A1200". The Word process it had
  started kept running in the background.
- **Impact:** none on the report, which was opened read-only. The first check
  gave no usable result.
- **Cause:** not confirmed. Word's list of spelling errors for the whole document
  failed partway through. A likely candidate is the mix of Greek text tagged as
  English and English text tagged as Greek, which gives Word a very long list of
  flags.
- **Resolution:** the leftover Word process was stopped. The check was run again
  one paragraph at a time, which worked for all but 2 paragraphs. Its process
  was also stopped afterwards.
- **Verified by:** the second check returned results for the whole report. No
  real typo was found; the typos reported earlier had already been fixed in
  another session on the same day.
- **Follow-up:** the 2 paragraphs Word could not check, and grammar, were not
  checked.

## INC-027 · 2026-11-26 · Word closed with an error during the report check

- **Phase:** report (section 9, outside the repository)
- **Status:** resolved, cause unconfirmed
- **What happened:** two sentences were added to section 9.2 of the report.
  Word was then started without a window to open the report read-only, check
  the new text and save a PDF copy. The check and the PDF worked, but closing
  the document failed with "The object invoked has disconnected from its
  clients" (RPC_E_DISCONNECTED). The Word process it had started kept running in
  the background.
- **Impact:** none on the report. It was opened read-only and the PDF copy was
  written. A hidden Word process was left running.
- **Cause:** not confirmed. Word most likely shut down or lost the connection
  right after the PDF export. Four other hidden Word processes from earlier that
  day were also running and may have played a part.
- **Resolution:** the Word process started by the check was stopped by its
  process ID. The four older ones were left alone.
- **Verified by:** that process no longer appears in the process list. Word
  found both new sentences in the report before the error.
- **Follow-up:** the page layout and the table of contents after the change
  have not been checked. The four older hidden Word processes are still running.

## INC-026 · 2026-11-26 · Compile check ran from the wrong folder

- **Phase:** 10
- **Status:** resolved
- **What happened:** after the W2 styling change, the compile check stopped with
  "The term '.\mvnw.cmd' is not recognized". The build did not run at all.
- **Impact:** none on the code. The check had to be run again.
- **Cause:** an earlier shell command had changed into
  `src/main/resources/org/example/demo`, and that folder carried over to the next
  command, so `mvnw.cmd` was not found.
- **Resolution:** the compile was run again after changing back to the project
  folder.
- **Verified by:** the second compile ended with exit code 0.
- **Follow-up:** none. Later build commands change to the project folder first.

## INC-025 · 2026-11-26 · Compile check failed because JAVA_HOME was not set

- **Phase:** 10
- **Status:** resolved
- **What happened:** after the colours in `app.css` were changed,
  `.\mvnw.cmd -q clean compile` stopped with "JAVA_HOME not found in your
  environment".
- **Impact:** none on the code. The compile check had to be run again.
- **Cause:** `JAVA_HOME` is not set system-wide on the development computer, and
  the command was run without setting it for that call. This is a known local
  setup issue, not a problem with the project.
- **Resolution:** the same command was run again with `JAVA_HOME` set to JDK 21
  for that call.
- **Verified by:** the second compile ended with exit code 0.
- **Follow-up:** none. The README already says that `JAVA_HOME` must point to
  JDK 21.

## INC-024 · 2026-11-26 · New figure captions numbered from 5 instead of 6

- **Phase:** report (section 8, outside the repository)
- **Status:** resolved
- **What happened:** after the six prototype screenshots were added to the
  report, Word numbered their captions 5 to 10, so two captions read "Figure 5".
- **Impact:** wrong figure numbers in the report. Found before it was saved.
- **Cause:** the caption of Figure 5 had its number typed by hand, while Figures
  1 to 4 used Word's automatic numbering, so Word counted only four figures before
  section 8.
- **Resolution:** the typed "5" was replaced with an automatic number. Its text
  and look did not change.
- **Verified by:** the report opened in Word, fields were updated and saved as
  PDF. The captions read Figure 1 to Figure 11 in order.
- **Follow-up:** none.

## INC-023 · 2026-11-26 · Section 8 script matched the table of contents

- **Phase:** report (section 8, outside the repository)
- **Status:** resolved
- **What happened:** the script that fills section 8 stopped with
  `AssertionError: instruction not found`.
- **Impact:** none. The check stopped it before anything was written.
- **Cause:** it looked for the first paragraph starting with "8. Prototype",
  which is the table of contents entry, not the heading.
- **Resolution:** it now uses the last match, which is the heading.
- **Verified by:** the second run found the instruction and replaced it.
- **Follow-up:** none.

## INC-022 · 2026-11-26 · Figma tool call limit reached

- **Phase:** Figma prototype
- **Status:** open
- **What happened:** while the new colour prototype was being built, Figma refused
  further calls with "You've reached the Figma MCP tool call limit on the Starter
  plan".
- **Impact:** frames P5 to P9 and the prototype links were built but not checked
  in a screenshot. The screenshots for the report could not be taken this way.
- **Cause:** the limit on tool calls of the free Figma plan.
- **Resolution:** the developer exported the screenshots from Figma by hand.
- **Verified by:** not verified. P5 to P9 still need to be looked at by hand.
- **Follow-up:** check P5 to P9 and click through the flow in Present mode.

## INC-021 · 2026-11-26 · Document tools missing on the development computer

- **Phase:** report and video script (outside the repository)
- **Status:** resolved
- **What happened:** `zip`, `pdftoppm`, LibreOffice, pandoc, the Python module
  `defusedxml` and the npm package `docx` were not installed, so the usual
  steps to pack, check and render Word files failed with "command not found" or
  "No module named".
- **Impact:** the report and the script could not be checked the usual way.
- **Cause:** these tools are not installed. They are not needed by the app.
- **Resolution:** files were packed with Python's `zipfile`, the XML was checked
  with Python's `minidom`, and Word itself saved them as PDF for the check.
  `docx` was installed in a temporary folder outside the repository.
- **Verified by:** both files open in Word and their PDF text is as expected.
- **Follow-up:** none.

## INC-020 · 2026-11-26 · Report file locked while saving

- **Phase:** report cleanup (outside the repository)
- **Status:** resolved, cause unconfirmed
- **What happened:** saving the cleaned report over the old one failed with
  `Device or resource busy`.
- **Impact:** the removal of the template instructions was not saved on the first
  try.
- **Cause:** not confirmed. Most likely the report was open in Word.
- **Resolution:** the save was tried again a few minutes later and worked.
- **Verified by:** the saved file has 9 fewer paragraphs than the backup, and the
  same 12 images and 5 tables.
- **Follow-up:** if the report was open in Word, close it without saving so the
  old version does not overwrite the new one.

## INC-019 · 2026-11-24 · Launch check wrote its logs to the wrong file

- **Phase:** 8 (README)
- **Status:** resolved
- **What happened:** the check of the INC-018 fix exited with code 1, because
  `tail` could not find the logs. Both launches had written to one file with a
  literal `$sh` in its name, so the second overwrote the first.
- **Impact:** the full log of the PowerShell launch is lost. Its window and exit
  code had been printed separately. The repository was not affected.
- **Cause:** wrong escaping of `\$sh` in the command that started the check.
- **Resolution:** none needed. The surviving `cmd` log was read.
- **Verified by:** the printed results of both launches and `BUILD SUCCESS` in the
  `cmd` log.
- **Follow-up:** none.

## INC-018 · 2026-11-24 · README run command fails in PowerShell

- **Phase:** 8 (README)
- **Status:** resolved
- **What happened:** `mvnw.cmd clean javafx:run`, as the README says, fails in
  PowerShell with `The term 'mvnw.cmd' is not recognized`. The first Phase 8 run
  also failed in `cmd`, with `'mvnw.cmd' is not recognized`.
- **Impact:** someone following the README in PowerShell cannot start the app.
  Earlier launches reported as "the README command" had used `.\mvnw.cmd` or
  `./mvnw.cmd`, so they did not show this.
- **Cause:** PowerShell does not run files from the current folder without `.\`.
  The `cmd` failure came only from the test session, which sets
  `NoDefaultCurrentDirectoryInExePath`. Windows does not set it by default.
- **Resolution:** the Windows commands in the README and `CLAUDE.md` are now
  `.\mvnw.cmd`. The tests were run without that variable.
- **Verified by:** in a fresh clone, the command read from the README itself
  opened the app and exited with code 0, in PowerShell (empty Maven caches) and
  then in `cmd` (the caches just filled). Same computer, with JDK 21 set up.
- **Follow-up:** the launch on another computer is not done.

## INC-017 · 2026-11-24 · A check command hid its own error

- **Phase:** 7 (End-to-end check)
- **Status:** resolved
- **What happened:** the command that ran the Phase 7 checklist also contained a
  leftover `git` command with a wrong path and its error output discarded.
- **Impact:** none. It changed nothing and no result depended on it.
- **Cause:** the follow-up of INC-012 (do not hide errors) was not applied.
- **Resolution:** the leftover command is not used any more.
- **Verified by:** the checklist's own output (25 of 25) and `git status`, which
  showed no changes.
- **Follow-up:** none.

## INC-016 · 2026-11-24 · W3 table covered its last row; stale checks failed

- **Phase:** 5 (W3 My registrations), reopened
- **Status:** resolved
- **What happened:** the first run of the new W3 checks failed 3 of 41. Later,
  one W2 check and one W4 check also failed.
- **Impact:** the new W3 table showed two scroll bars it did not need, and they
  covered part of the last row. The other failures did not affect the app.
- **Cause:** one real fault and two wrong checks:
  - the table's columns filled its whole width. A scroll bar in one direction
    then forced one in the other.
  - one W3 check read the text of hidden labels
  - the W2 and W4 checks looked for the old W3 list, which is now a table
- **Resolution:** the table is slightly wider than its columns and exactly as
  tall as its rows. The three checks were corrected.
- **Verified by:** on the final code, 41 W3, 78 W4, 51 W2 and 139 W1 checks
  passed, and the screenshot shows the whole table without scroll bars.
- **Follow-up:** none.

## INC-015 · 2026-11-24 · Narrow window clipped again by the third bar button

- **Phase:** W4 Create event, added on the developer's request
- **Status:** resolved
- **What happened:** adding Create Event to the navigation bar made the bar too
  wide again for a 706-pixel window, so the scrollbar's edge was cut off.
- **Impact:** none at 1280x800, the size in NFR7. Same fault as INC-014, but
  smaller (about 12 pixels). The W4 check passed and did not catch it.
- **Cause:** the bar's smallest width grew with the third button.
- **Resolution:** the app name may now shrink further and wraps onto three
  lines. A check that the screen is not wider than the window was added.
- **Verified by:** 78 W4, 51 W2 and 139 W1 checks passed on the final build.
- **Follow-up:** none.

## INC-014 · 2026-11-24 · Right side of the window cut off when narrow

- **Phase:** 4 (W2) and 6 (Navigation), reopened
- **Status:** resolved
- **What happened:** in a 706x462 window the right side of W2 was cut off and
  could not be scrolled to. Part of the navigation bar was also cut off. The
  first W2 check missed it, because it only looked at the Confirm button.
- **Impact:** at widths under about 870 pixels, part of the form and the
  scrollbar could not be reached. 1280x800, the size in NFR7, was not affected.
- **Cause:** the navigation bar had fixed minimum widths. JavaFX lays the whole
  window out at least as wide as the widest minimum.
- **Resolution:** the bar's widths are now preferred widths with smaller
  minimums, so it shrinks. The check now covers both scrollbars, the Cancel
  button and the right edge of the fields.
- **Verified by:** all 51 W2 checks and 139 W1 checks passed on the final build,
  and the screenshots show both scrollbars in the small window.
- **Follow-up:** none.

## INC-013 · 2026-11-24 · W1 checks stopped early and missed a resize

- **Phase:** 3 (W1 Browse events), reopened
- **Status:** resolved: the checks were wrong, not the app
- **What happened:** two problems in the W1 checks:
  - the first run stopped with a `NullPointerException` because the event list
    could not be found after going back to W1
  - the small-window screenshot showed the normal window size, so the small
    window had not been checked at all
- **Impact:** none on the app. The same screenshots did show one real fault:
  the two dropdowns were grey, where the wireframe has them white.
- **Cause:** test timing. JavaFX adds a scroll pane's content to the scene only
  after layout, and a resize is not applied until a later frame. The grey came
  from the style for selected list rows, which also applied to the dropdowns.
- **Resolution:** the check now lays out the screen before each lookup and waits
  a second after resizing. The dropdown style was corrected in `app.css`.
- **Verified by:** 139 of 139 checks passed, with the small window measured at
  886x562, and the new screenshots show white dropdowns.
- **Follow-up:** none.

## INC-012 · 2026-11-24 · Test launcher run before it existed

- **Phase:** 6 (Navigation), reopened
- **Status:** resolved
- **What happened:** the command that checks the navigation bar tried to start
  its launcher class before creating it. The error was hidden, because that
  command discarded its error output and ignored the failure.
- **Impact:** none on the app. No check ran in that first attempt.
- **Cause:** the commands were written in the wrong order.
- **Resolution:** the same script then created and compiled the launcher and ran
  the checks.
- **Verified by:** that run reported 28 of 28 checks passed.
- **Follow-up:** do not hide errors in check commands.

## INC-011 · 2026-11-24 · Oversized shell command rejected again

- **Phase:** 2 (Data), reopened
- **Status:** resolved, cause unconfirmed
- **What happened:** one long shell command meant to write all the data model
  files failed with ``unexpected EOF while looking for matching `'' ``, the same
  error as INC-008. The compile started alongside it reported success, but it
  had compiled the old code.
- **Impact:** none. The shell rejected the command before running any of it.
  The compile result was misleading until it was checked.
- **Cause:** not confirmed, as in INC-008. The repeat happened because the
  lesson from INC-008 was not applied.
- **Resolution:** the files were written one at a time instead.
- **Verified by:** `git status` showed no changes after the failure. The project
  was compiled again after the files were written.
- **Follow-up:** write source files one at a time, never in one shell command.

## INC-010 · 2026-11-24 · Small tool failures during the audit and its follow-up

- **Phase:** audit before Phase 7 (23 Nov) and its follow-up (24 Nov)
- **Status:** resolved
- **What happened:** several commands failed or gave incomplete output:
  - the PDF of the brief could not be shown as images (`pdftoppm is not
    installed`), and the first text extraction dropped the Greek letters
  - an `ls` command used an option that does not exist (`--time-sort`)
  - scripts that printed the report and the audit notes used the Windows
    console encoding (cp1253). Greek text came out garbled, and one script
    stopped with `UnicodeEncodeError: 'charmap' codec can't encode character`
    before it reached the audit summary.
- **Impact:** none on the app. On 24 Nov the audit was first summarised without
  its final summary, and the advice on adding tests wrongly said that JUnit
  would be a new dependency. JUnit is already in `pom.xml` with test scope.
- **Cause:** a missing tool, a mistyped option, and Python's `-I` option, which
  ignores the setting that switches its output to UTF-8.
- **Resolution:** the brief was read with `pdftotext` set to UTF-8, and the
  scripts were run again with Python's `-X utf8` option. The audit notes were
  read again in full and the mistake was corrected with the developer.
- **Verified by:** the brief, the report and the audit summary were all read
  with their Greek text intact.
- **Follow-up:** none.

## INC-009 · 2026-11-23 · UI tests read a hidden list row

- **Phase:** audit before Phase 7
- **Status:** resolved: the test was wrong, not the app
- **What happened:** the first run of the new UI tests failed on checks of W1
  and W3 that depended on reading list rows. For example, clicking "Tech Careers
  Fair" seemed to open "UX Design Workshop". 10 of 35 UI tests did not pass.
- **Impact:** none on the app. It briefly looked as if W1 opened the wrong event.
- **Cause:** JavaFX's `ListView` keeps a hidden spare cell with index -1. The
  test's row lookup returned it along with the real rows.
- **Resolution:** the test now skips cells that are hidden or have a negative
  index. No app code was changed.
- **Verified by:** two further runs gave identical results, with 5 of 35 UI
  tests failing. Those 5 are real problems in the app, not test errors.
- **Follow-up:** the 5 problems are handled with INC-007: duplicate
  registration, right-click opening an event, search lost on Back, W2 cut off in
  a small window, and phone numbers stored with spaces.

## INC-008 · 2026-11-23 · Oversized shell command rejected

- **Phase:** audit before Phase 7
- **Status:** resolved
- **What happened:** one long shell command meant to create all the test files
  failed with ``unexpected EOF while looking for matching `'' ``.
- **Impact:** none. The shell rejected the command before running any of it.
- **Cause:** not confirmed. Most likely quoting inside the very long command.
- **Resolution:** the test files were written one at a time instead.
- **Verified by:** all the files were created and the tests compiled and ran.
- **Follow-up:** none.

## INC-007 · 2026-11-23 · Screens built without checking the wireframes

- **Phase:** 3 to 6, found in the audit before Phase 7
- **Status:** resolved on 2026-11-24
- **What happened:** an audit compared the app with the project report. The app
  meets the brief's minimum list, but only 1 of 21 checks against the report's
  requirements, main scenario and wireframes passed. W1 has no categories or
  filters, W2 asks for a phone number that the report does not mention and has
  no number of places, W3 is not a table, and there is no shared navigation bar.
- **Impact:** the brief grades consistency from the personas through to the
  implementation, so the app cannot be submitted as it is.
- **Cause:** `PLAN.md` named the risk in Phases 3 and 4 and said to check the
  wireframes first. That was not done, and the screens were built from guesses.
- **Resolution:** the data model, navigation bar, W1, W2 and W3 were rebuilt from
  the report and its wireframes. The developer decided on 2026-11-24 to do this
  before Phase 7. The organizer screen (W4) stays out,
  because only the second scenario needs it. Correction (2026-11-24): the
  developer later asked for W4, so it was added.
- **Verified by:** the updated tests, run twice with identical results: 86 of 86
  passed, including 21 of 21 report checks and 10 new ones. The phone tests were
  replaced, because the field is gone, and some checks were made stricter. The
  tests also failed as expected when four faults were added on purpose to a
  throwaway copy.
- **Follow-up:** the tests cover what they were written for, not every detail of
  the wireframes. FR19 (places taken, for the organizer) is not implemented, as
  no wireframe shows it. The tests are kept outside the repository.

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
