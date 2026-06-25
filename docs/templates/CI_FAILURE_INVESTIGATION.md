# Auxio-TS CI failure investigation template

Use this when opening or updating a PR comment about CI failures.

## Required source of truth

- Inspect the full GitHub Actions logs, not only summary lines.
- Check workflow annotations, uploaded artifacts, and Gradle/problem reports where available.
- Distinguish GitHub Actions failures from local Termux/Codex/Jules environment limitations.

## Classification

For each finding, classify it as one of:

- Valid regression
- Existing baseline issue exposed by the PR
- CI/environment flake
- Dependency/bootstrap failure
- Workflow/tooling issue
- Stale/duplicate finding
- Out of scope for this PR

## Fix policy

- Fix valid regressions in the PR.
- Keep Gradle tasks scoped to workflow intent.
- Do not replace explicit variant/module tasks with generic aggregate tasks for convenience.
- Do not claim build/test/lint success unless the command actually passed.
- If the correct action is to rerun CI, state why the failure is demonstrably transient.

## Evidence to paste

```text
Workflow:
Job:
Step:
Exact failing command:
First relevant error:
Root cause:
Fix or disposition:
Follow-up validation:
```
