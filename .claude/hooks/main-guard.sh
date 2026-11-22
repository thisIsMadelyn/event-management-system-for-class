#!/bin/bash
# main-guard — denies git commit/push when it would land on main or master.
#
# Registered as a PreToolUse hook on Bash. Always exits 0; the decision is JSON
# on stdout. Requires jq on PATH.
#
# This only sees tool calls made by Claude Code. Running git yourself from a
# terminal bypasses it, which is the intended escape hatch.

input=$(cat)
cmd=$(jq -r '.tool_input.command // empty' <<<"$input")
cwd=$(jq -r '.cwd // empty' <<<"$input")

[[ -z "$cmd" ]] && exit 0

deny() {
  jq -n --arg r "$1" '{
    hookSpecificOutput: {
      hookEventName: "PreToolUse",
      permissionDecision: "deny",
      permissionDecisionReason: $r
    }
  }'
  exit 0
}

# Only look at git commit / git push. Everything else passes through,
# including git status, git diff, git log, git add.
grep -Eq '(^|[;&|]|\$\(|`)[[:space:]]*git[[:space:]]+(commit|push)\b' <<<"$cmd" || exit 0

# Explicit refspec: git push <remote> main — blocked from any branch.
if grep -Eq 'git[[:space:]]+push\b.*\b(main|master)\b' <<<"$cmd"; then
  deny "Push targets main/master. Protected branch — push it yourself from a terminal if you mean it."
fi

branch=$(git -C "${cwd:-.}" rev-parse --abbrev-ref HEAD 2>/dev/null)

case "$branch" in
  main|master)
    deny "Current branch is '$branch'. Protected — commit and push are blocked here. Switch to a feature branch, or do it yourself from a terminal."
    ;;
esac

exit 0
