---
name: wrap-up
description: End-of-session wrap-up. Commits and pushes the session's work, then updates PLAN.md, SESSION_HISTORY.md and CHECKPOINT.md from what actually changed. Invoke at the end of a session, or when the user types /wrap-up.
---

# Wrap-up

Close out the session: record what happened, update the three tracking files,
commit and push.

## Arguments

| Flag | Effect |
|---|---|
| `--no-commit` | Skip both commits and the push. Files are still updated. `Tree:` in CHECKPOINT records what is left uncommitted. |
| `--context yes\|no` | Sets **Context for next session** in CHECKPOINT. Default `yes`. |
| `--dry-run` | Print the three file contents and the commit plan. Write nothing. |

## Hard rules

- **Never stage, commit or push `CHECKPOINT.md`.** Confirm it is in `.gitignore`
  before anything else. If it is not, stop and tell the user.
- **Never state anything you did not verify.** Every claim in these files comes
  from `git diff`, a command you ran, or the user's own words in this session.
  If you are unsure whether something works, it goes under **Not checked**.
- **Never edit a past SESSION_HISTORY entry.** Append only.
- **Never add, reorder or rewrite a PLAN phase.** See step 4.
- If the working tree is clean and nothing changed this session, say so and stop.

## Steps

### 1. Checks

```
git rev-parse --abbrev-ref HEAD
git status --porcelain
git log --oneline <last-session-sha>..HEAD
git diff --stat
```

- Confirm `CHECKPOINT.md` is gitignored.
- Read the last `End status` in SESSION_HISTORY for the starting SHA.
- **If the branch is `main` or `master`:** stop. The `main-guard` hook will deny
  the commit. Tell the user and ask whether to continue with `--no-commit`.
- If the remote has commits you do not have, stop and say so. Do not pull.

### 2. Reconstruct the session

Build the record from evidence, in this order of trust: the diff, commands you
ran and their output, what the user said. Do not infer intent from code alone.

For each change, classify it:
- 🆕 **Addition** — new capability, file, or feature
- 🐛 **Bug fix** — corrects a defect in existing work
- plain bullet — a move, rename, deletion, config change, or a PLAN tick

A session may carry zero, one or several of each.

### 3. Commit the work

Skip if `--no-commit`.

Stage everything except `CHECKPOINT.md`. One commit for the session's work.
Message: a subject line naming the session's theme, then a body listing the
changes by classification. Record the resulting SHA — the next two files need it.

### 4. Update `PLAN.md`

You may only:
- tick a **Done when** box whose condition you verified this session
- change the **Active phases** and **Open** lines
- amend a line, with a dated note saying what changed and why, in the style
  already used in the file

You may not add phases, reorder them, rewrite a goal, or tick a box you did not
verify. If the session's work suggests the plan is wrong, say so in your reply
and leave the file alone.

### 5. Update `SESSION_HISTORY.md`

Prepend a new entry below the `---`. Newest first.

```
## Session [<YYYY-MM-DD-HHMM>]

- **Start status:** <starting SHA, tree state, active phases, what was open>
- **End status:** <work SHA, what closed, what stayed open and why>
- **Badges:**
  - 🆕 Addition — <description> (`<sha>`)
  - 🐛 Bug fix — <description> (`<sha>`)
  - <plain bullet> (`<sha>`)
```

Public file. No keys, no paths that are not already in the repo, no
internal-only detail. Say what stayed open and why, not only what closed.

### 6. Commit the docs and push

Skip if `--no-commit`.

Second commit: `PLAN.md` and `SESSION_HISTORY.md` only.

Before pushing, show the branch, the two SHAs and `git diff --stat` against the
remote. Wait for a yes. Then one push.

### 7. Overwrite `CHECKPOINT.md`

In full. Nothing from the previous version survives.

```
<!-- LOCAL ONLY — never staged, committed or pushed. -->
<!-- Overwritten in full by /wrap-up. Nothing here survives the next session. -->

**Context for next session:** <yes|no>
**Session:** <YYYY-MM-DD-HHMM>
**Commit:** <sha> · **Active phases:** <N, M> · **Tree:** clean | <what is uncommitted>

---

# <Theme of the session. A title, not a date.>

## Next
<The first action for the next session. Specific enough to start on without
re-reading anything. If a decision comes first, state the options.>
```

Then whichever of these the session produced. Omit the ones it did not:

- **Changed** — what moved and why, grouped by concern, one concern per item
- **Checked** — what was verified and against which file or symbol, so the next
  session does not re-verify it
- **Not checked** — claimed but unverified. Write this section even when it is
  the only one; it is where the next session's bugs are
- **Open questions** — decisions deferred, with the options as they stood
- **Dead ends** — what was tried and abandoned

### 8. Report

Three lines: the two SHAs and the branch pushed to, which PLAN boxes were ticked,
and what you put under **Not checked**. Nothing else.
