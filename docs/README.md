# Auxio-TS Documentation Index

Auxio-TS is a TS18/Topway/DoFun Variety-targeted Auxio variant for the observed DoFun/Topway stock-music contract. Start here for current product, CI, release, and compatibility guidance.

[Evidence confidence: Requires TS18 validation] [Porting decision: Requires TS18 runtime validation]

## Current docs

- [`DEVELOPMENT.md`](DEVELOPMENT.md) — local setup, canonical CI coverage, Roborazzi UI workflow, and deleted-workflow audit notes.
- [`RELEASE_WORKFLOW.md`](RELEASE_WORKFLOW.md) — manual signed release flow and expected APK/module assets.
- [`UPSTREAM_AUXIO_MONITORING.md`](UPSTREAM_AUXIO_MONITORING.md) — monthly upstream Auxio change monitor, no-clutter baseline rules, and generated issue workflow.
- [`DOFUN_VARIETY_COMPATIBILITY.md`](DOFUN_VARIETY_COMPATIBILITY.md) — DoFun/Topway compatibility contract and private-hook boundaries.
- [`TS18_APK_REFERENCE.md`](TS18_APK_REFERENCE.md) — compact APK-derived reference evidence for DoFun Variety and stock `twmusic`.
- [`TS18_INSTALLATION_CONSTRAINTS.md`](TS18_INSTALLATION_CONSTRAINTS.md) — package-conflict and install-lane constraints for real TS18 firmware.
- [`TS18_RUNTIME_VALIDATION.md`](TS18_RUNTIME_VALIDATION.md) — on-device TS18 validation checklist and evidence expectations.
- [`LAUNCH_TO_CURRENTLY_PLAYING.md`](LAUNCH_TO_CURRENTLY_PLAYING.md) — durable startup-panel routing contract and TS18 validation matrix.
- [`TS18_COMPATIBILITY_AUDIT.md`](TS18_COMPATIBILITY_AUDIT.md) — repo-wide TS18/Topway/DoFun compatibility surface classification.
- [`topway/README.md`](topway/README.md) — local Topway decompile/source-led compatibility notes.

## DoFun Variety / TS18 APK reference baseline

Use the APK reference docs before changing package identity, Topway broadcasts, media/session wiring, release workflows, or guardrail scripts.

[Evidence confidence: Observed APK/reference evidence] [Porting decision: Directly reusable as compatibility requirements and guardrails]

Primary compatibility target:

- DoFun Variety Theme: `com.dofun.variety`

[Evidence confidence: Observed in DoFun APK metadata/config and exact-device package listing] [Porting decision: Primary launcher/theme target]

Primary replacement contract:

- stock `twmusic` / `com.tw.music`
- release package/application ID: `com.tw.music`
- launcher/activity component: `com.tw.music.MusicActivity`
- release variants: `topwayTwMusicRelease` (`com.tw.music`) and `topwayTwMediaRelease` (`com.tw.media`)

[Evidence confidence: Observed in DoFun APK config, stock twmusic APK references, and exact-device package paths] [Porting decision: Directly reusable replacement contract with install-lane constraints]

Alternate DoFun fixed entry to support after implementation:

- package/application ID: `com.tw.media`
- launcher/activity component: `com.tw.music.MusicActivity`
- intended release variant: `topwayTwMediaRelease`

[Evidence confidence: Observed DoFun APK config; not yet an implemented release variant unless added by a later PR] [Porting decision: Reusable alternate-entry requirement, not a universal no-root bypass]

Observed Cardoor/private services and vendor hooks are evidence only, not production implementation. They are not for production by default and require the formal gap-and-promotion process before any native/private investigation can become product code.

[Evidence confidence: Observed APK/string evidence] [Porting decision: Evidence only; do not implement without proven protocol]

## Exact-device installation warning

The exact target TS18 diagnostics show stock `com.tw.music` installed as a system priv-app at:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

A normal user-signed Auxio-TS APK using package `com.tw.music` cannot be assumed to install over that stock package. See [`TS18_INSTALLATION_CONSTRAINTS.md`](TS18_INSTALLATION_CONSTRAINTS.md) before editing release/install docs or claiming install compatibility.

## CI entry points

**Canonical validation and release workflows:**

- `.github/workflows/android.yml` builds standard and Topway/DoFun variants and runs APK/reference compatibility checks on relevant PR and `dev` changes.
- `.github/workflows/lint.yml` runs workflow/shell syntax checks, formatting, unit tests, Android lint, and head-unit safety guardrails.
- `.github/workflows/manual-release.yml` builds, signs, verifies, and publishes selected standard/`com.tw.media` APKs and the systemless `com.tw.music` Magisk ZIP.
- `.github/workflows/ui-screenshots.yml` provides manually triggered Roborazzi screenshot/report bundles for UI review.

[Evidence confidence: Observed workflow configuration] [Porting decision: CI/release artefact coverage only; requires separate TS18 runtime validation]

**Auxiliary maintenance:**

- `.github/workflows/upstream-auxio-monitor.yml` checks `OxygenCobalt/Auxio` monthly and opens an issue only when upstream has new commits to review.

There is no separate weekly dependency-build workflow or branch-mutating formatter workflow. Dependabot update PRs pass through the canonical Android Build and Android Quality checks, and formatting remains a required check fixed on the originating branch.

Local preflight:

```bash
bash scripts/bootstrap-dependencies.sh --profile full-build
bash scripts/check-ts18-apk-reference-contracts.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-headunit-compat-safety.sh
status=0; while IFS= read -r -d '' script; do bash -n "$script" || status=1; done < <(find scripts -type f -name '*.sh' -print0); exit "$status"
ruby -e 'require "yaml"; ARGV.each { |f| Psych.safe_load(File.read(f), permitted_classes: [], permitted_symbols: [], aliases: false); puts "OK #{f}" }' .github/workflows/*.yml
```

## Exact-device context

- [`CODEX_TS18_DEVICE_CONTEXT.md`](CODEX_TS18_DEVICE_CONTEXT.md) — redacted `s9863a1h10` Android 10 TS18 profile for agent work.
- [`TS18_INSTALLATION_CONSTRAINTS.md`](TS18_INSTALLATION_CONSTRAINTS.md) — install lanes, stock package conflicts, and recovery notes.
- [`evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md`](evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md) — concise redacted device evidence.
