# Release workflow

`.github/workflows/manual-release.yml` is the only maintained APK publication path. It runs manually from the current `dev` head, serialises all release runs, builds only selected maintained assets, validates signed outputs, creates or resumes one immutable release transaction, verifies remote assets, and then attempts to fast-forward released version metadata to `dev`.

## Operator model

There is no create-versus-repair mode to choose. The workflow determines the safe action from current Git tags and GitHub Releases:

- leave **Version** blank to resume the latest interrupted tag/draft transaction, otherwise create the next patch version;
- enter an existing tag such as `v6.5.0` to resume that exact immutable release;
- enter a new higher tag such as `v6.5.5` to create that version, even when an older interrupted tag remains;
- release-without-tag, tag identity conflicts, regressions and ambiguous duplicate releases still stop safely.

A blank field therefore means **continue the correct next release transaction**, not merely increment regardless of repository state.

## Friendly dispatch fields

- **Version (optional):** blank resumes an interrupted release or creates the next patch; an explicit `vMAJOR.MINOR.PATCH` targets that version.
- **Keep as draft:** leaves the verified release hidden for review.
- **Pre-release:** applies only when the release is published.
- **Main Auxio-TS app:** publishes the primary `com.tw.media` APK.
- **LSPosed bridge:** publishes the signed API 100 stock-music bridge add-on.
- **Debug builds:** normally remain short-lived workflow artifacts; publication as Release assets must be explicit.
- **Rebuild complete existing assets:** normally off. Incomplete interrupted triplets repair automatically; this option forces a full rebuild of already-complete selected triplets.

## Reliability principles retained from successful v4/v5 releases

The successful v4.x/v5.x workflow was materially simpler: one optional version field, direct tag-based version selection, one serialised job, selected APK build/signing, a draft/published Release, and final asset verification. The current workflow retains those reliable characteristics while preserving later safety improvements:

- one obvious automatic operator path instead of a brittle mode matrix;
- explicit versions are honoured before unrelated older orphan tags are considered blockers;
- the latest tag remains the primary automatic version authority;
- one repository-wide release lock prevents overlapping publication;
- release work is idempotent: existing matching tags/releases are adopted rather than treated as collisions;
- interrupted partial uploads are expected resumable state and repair automatically;
- transient GitHub inventory and verification reads use small bounded retries;
- tag, API, SDK, bootstrap, Gradle and metadata-sync operations have explicit time limits;
- runtime publication does not execute source-shape/grep assertions against its own workflow. Those checks stay in CI, where they cannot create spurious release failures.

The workflow does **not** return to unsafe historical behaviour: tags are never moved, published identity conflicts fail closed, exact signed APK validation remains mandatory, and source metadata is not considered synchronised until the remote `dev` state proves it.

## Repository setup

Manual Release uses the repository-provided `GITHUB_TOKEN` with:

```yaml
permissions:
  actions: read
  checks: read
  contents: write
```

No extra release PAT, deploy key, `RELEASE_PUSH_TOKEN`, ruleset import or special bypass actor is required. Keep these existing signing secrets:

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

The workflow must be able to create immutable `v*` tags. The simplest supported repository configuration is no repository rulesets and no classic `dev` protection.

## Transaction behaviour

1. Verify the run is from the current remote `dev` head.
2. Read Git tags and all authenticated GitHub Releases, including drafts, with bounded retries.
3. Resolve one automatic action and release tag.
4. Reuse complete existing asset triplets; rebuild missing or interrupted triplets only.
5. Build/sign/inspect each required APK once and upload a recovery artifact before publication.
6. Create or adopt the immutable tag.
7. Create or adopt one draft Release by numeric release ID.
8. Upload or replace only the planned assets.
9. Verify every selected remote asset, with bounded read retries.
10. Apply requested draft/pre-release state.
11. Attempt a fast-forward-only metadata sync to `dev`; a rejected sync is reported as follow-up without invalidating an already verified Release.

## Recovery examples

### Interrupted `v6.5.0`, no newer completed release

Run with Version blank or enter `v6.5.0`. The workflow resumes the immutable tag/draft and repairs incomplete assets.

### Older `v6.5.0` is interrupted, but `v6.5.5` is explicitly requested

Enter `v6.5.5`. The older tag is reported as unresolved history but does not spuriously block the explicit higher version.

### Existing complete release

Entering its tag performs an idempotent verification/reuse pass. Complete selected triplets are not rebuilt unless **Rebuild complete existing assets** is enabled.

## Deliberate STOP conditions

Stop and inspect rather than mutating when:

- a GitHub Release exists without its Git tag;
- a requested tag points to a different commit than the validated release source;
- more than one Release uses the same tag;
- an explicit new version is not newer than existing tag/Release authority;
- signed APK identity, version, signer or checksum validation fails;
- the selected immutable tag source lacks required build or validation scripts.

Do not delete or move an immutable release tag merely to make a rerun pass.

## Implementation ownership

The workflow YAML owns the operator form, permissions, concurrency, immutable action pins, environment wiring and the 90-minute job bound. Numbered `scripts/manual-release/*.sh` files own the shell implementation in exact execution order. Each script is small enough to syntax-check independently, and every external Git, GitHub, SDK, package-manager and Gradle wait is explicitly bounded.

This keeps the operator surface close to the successful v4/v5 model without returning to a monolithic, one-shot publisher. Release-time execution does not grep or parse its own workflow source; source-shape and ordering checks remain PR/CI responsibilities.
