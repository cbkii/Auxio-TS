# TS18 diagnostics seed package

This package contains repo-ready redacted source material derived from the actual target TS18 diagnostics.

Apply by extracting the ZIP at the repository root on a new branch after PR #53 is merged:

```sh
unzip auxio-ts18-device-diagnostics-seed.zip -d /path/to/Auxio-TS
```

Then review the diff before committing.

Included files:

- `README.md` — updates the top-level compatibility/install caveats.
- `docs/README.md` — adds the new diagnostic/device-context docs to the index.
- `docs/DOFUN_VARIETY_COMPATIBILITY.md` — adds exact-device install conflict and `com.tw.media` alternate-entry posture.
- `docs/TS18_RUNTIME_VALIDATION.md` — separates TermOne/app-only, ADB, Shizuku, and root validation lanes.
- `docs/RELEASE_WORKFLOW.md` — documents release asset/install constraints and future `topwayTwMedia` asset expectations.
- `docs/TS18_INSTALLATION_CONSTRAINTS.md` — new install/package-conflict authority document.
- `docs/CODEX_TS18_DEVICE_CONTEXT.md` — new Codex-readable summary of the target diagnostics.
- `docs/evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md` — redacted exact-device evidence profile.
- `docs/reference/ts18-diagnostics/README.md` — explains why raw diagnostics are not committed.

This seed package intentionally does not add product code. It gives Codex a factual repo-local source for the next implementation pass.
