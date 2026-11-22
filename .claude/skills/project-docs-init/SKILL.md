---
name: project-docs-init
description: Sets up the four tracking documents in a repository — CLAUDE.md, PLAN.md, SESSION_HISTORY.md and CHECKPOINT.md — from the shared templates, plus the .gitignore entry that keeps CHECKPOINT.md local. Invoke once per project, or when the user types /project-docs-init.
---

# Project docs init

Create the four tracking documents this workflow depends on, filled in for this
project. Run once per repository.

## Arguments

| Flag | Effect |
|---|---|
| `--dry-run` | Print what would be written. Create nothing. |
| `--force` | Overwrite a file that already exists. Without it, existing files are left alone. |

## Hard rules

- **Never overwrite an existing file** unless `--force`. Report which files were
  skipped.
- **Never invent project facts.** Every placeholder you fill comes from the repo
  or from the user. A placeholder you cannot fill stays in the file as a
  placeholder — an unfilled `<...>` is better than a plausible fabrication.
- **Never write `CHECKPOINT.md` without the `.gitignore` entry.** If you cannot
  write `.gitignore`, do not create `CHECKPOINT.md` either, and say so.

## Steps

### 1. Survey the repo

Read what is already there before asking anything:

```
git rev-parse --abbrev-ref HEAD
ls
cat README.md .gitignore 2>/dev/null
```

Look for: the build tool and its entry points (Makefile, package.json scripts,
gradle tasks), the language and version, a license, existing docs. Anything you
can read, do not ask about.

### 2. Ask what you cannot read

Ask only what the templates genuinely need and the repo does not answer. Keep it
to one round, grouped:

- **What this is** — one paragraph, in plain terms, not the stack.
- **Deadline and target completion**, or none.
- **Hard constraints** — rules imposed from outside, each with its consequence.
  If the user cannot name a consequence, it is not a constraint.
- **Architecture invariants** — what must stay true for the system to work.
- **Single sources of truth** — values that live in exactly one file.
- **Phase 1** — its goal and its **Done when** list. Later phases can wait.

If the user does not know yet, leave the placeholder. Say which ones you left.

### 3. Write `.gitignore`

Append, do not rewrite:

```
# Local handover note — never committed
CHECKPOINT.md
```

If the entry is already there, skip it. If `.gitignore` does not exist, create
it with that entry.

### 4. Write the four files

From the templates, in this order. Each goes in the repo root.

| File | Fill from |
|---|---|
| `CLAUDE.md` | step 1 and step 2. Keep the Document map, Session protocol and Do-not list verbatim — they are the contract the other files depend on. |
| `PLAN.md` | step 2. Set **Active phases** to the first phase and **Open** to empty. |
| `SESSION_HISTORY.md` | the template unchanged, with no entries. |
| `CHECKPOINT.md` | a fresh checkpoint: **Context for next session: yes**, the current SHA, **Active phases** matching PLAN, `Tree: clean`, a `## Next` naming the first task of Phase 1. |

The header block, the section order and the rules text in each template are the
contract. Change the content, not the structure — `/wrap-up` reads these files
by their headings and by the **Active phases** and **Context for next session**
lines.

### 5. Verify and report

```
git status --porcelain
git check-ignore -v CHECKPOINT.md
```

Confirm `CHECKPOINT.md` is ignored. If it is not, stop and tell the user — the
rest of the workflow is unsafe without it.

Report: which files were created, which were skipped because they existed, and
every placeholder you left unfilled. Do not commit — that is the user's call,
or the next `/wrap-up`.
