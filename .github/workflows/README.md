# GitHub Actions workflow policy

This directory intentionally keeps a small, stable workflow set.

## Routine automatic validation

- `android.yml` — **Android Build** on pull requests into `dev` and pushes to `dev`.
- `lint.yml` — **Android Quality** on pull requests into `dev` and pushes to `dev`.

These are the canonical routine CI workflows. A new workflow must not repeat their normal debug builds, unit tests, lint, formatting, syntax or head-unit safety checks unless it is an explicitly manual release gate.

## Manual workflows

- `startup-performance.yml` — full startup/profile/release validation before a release or after profile-infrastructure changes.
- `startup-benchmarks.yml` — bounded managed-emulator Macrobenchmark or Baseline Profile collection.
- `ui-screenshots.yml` — Roborazzi screenshot recording and verification.
- `manual-release.yml` — version, tag and release-asset publication.

Manual workflows must remain bounded, use explicit inputs and distinguish repository/emulator evidence from exact TS18 validation.

## Scheduled workflow

- `upstream-auxio-monitor.yml` — monthly upstream Auxio comparison and issue reporting.

It is the only scheduled workflow. Changes to its direct-write or issue-creation authority require an explicit review.

## Maintenance rules

1. Do not commit PR-numbered repair workflows or branch-specific workflow triggers to `dev`.
2. Temporary repair workflows must be removed in the same PR before merge.
3. Do not add merged feature-branch names to workflow triggers. Routine pull requests target `dev`.
4. Pin third-party actions to immutable commit SHAs.
5. Keep artifact retention explicit and short unless an artefact is release evidence.
6. Use concurrency cancellation for routine validation; do not cancel bounded benchmark evidence runs.
7. Delete obsolete workflow files rather than leaving disabled copies in the default branch.
8. Historical Actions runs and merged PR branches are repository administration data, not source files; clean them separately with the GitHub UI or CLI.
