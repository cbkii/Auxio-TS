# Release workflow

`.github/workflows/manual-release.yml` is the only maintained APK publication path. It runs manually
from the current `dev` head, uses one repository-wide release lock, builds only the work required for
the selected state, validates staged assets, publishes through a draft-first transaction, and only
then fast-forwards released source metadata to `dev`.

The workflow never publishes the retired Standard APK, a raw Auxio `com.tw.music` APK, or the former
exact-package Magisk overlay.

## Required repository configuration

Protected release refs use two deliberately separate identities:

- `GITHUB_TOKEN` performs GitHub Release API operations and asset upload with workflow-scoped
  `contents: write` permission;
- the repository secret `RELEASE_PUSH_TOKEN` authenticates the two protected Git ref mutations: the
  immutable `vX.Y.Z` tag push and the ordinary fast-forward release-metadata update to `dev`.

`GITHUB_TOKEN` is the `github-actions[bot]` identity. It does not become the repository owner merely
because the workflow was manually dispatched by the owner, and it cannot satisfy a ruleset bypass
entry assigned to the `cbkii` user. The protected-ref token must therefore belong to the same user that
is configured as the ruleset bypass actor.

The release job is gated by `github.actor == github.repository_owner`. A non-owner dispatch is skipped
before checkout and before any step can receive `RELEASE_PUSH_TOKEN`. This repository is currently
owned by the personal account `cbkii`. If the repository is transferred to an organisation, replace
that personal-owner gate and token design with an explicitly approved environment or a dedicated
GitHub App before running another release; an organisation login cannot dispatch a workflow as a user.

Configure the repository rulesets as follows:

### `dev` branch ruleset

- target `dev` exactly;
- keep pull requests and the repository's required checks for ordinary contributors;
- add `cbkii` as a bypass actor with bypass mode **Always**;
- keep branch deletion blocked;
- keep force pushes/non-fast-forward updates blocked.

### release-tag ruleset

- target release tags matching `v*` (or the repository's stricter equivalent that includes
  `vMAJOR.MINOR.PATCH`);
- include the **Restrict creations** rule;
- add `cbkii` as a bypass actor with bypass mode **Always**;
- keep tag deletion blocked;
- do not permit tag movement or force updates.

The user bypass must be present on every active ruleset that applies to `dev` or the exact release tag.
A bypass on only the branch ruleset does not authorise tag creation, and a bypass on only the tag
ruleset does not authorise the metadata fast-forward.

After resolving the exact release tag, Manual Release fetches every active branch/tag ruleset and its
full detail with the owner token. Before any JDK, Android SDK, dependency bootstrap or APK build, it
fails closed unless:

- every active ruleset applying to `refs/heads/dev` contains the owner user ID with `User` / `always`
  bypass;
- for new releases, every active ruleset applying to the exact `refs/tags/vX.Y.Z` contains that same
  bypass;
- at least one applicable release-tag ruleset contains the `creation` restriction;
- no classic `dev` protection enforced for administrators would block the metadata fast-forward.

This is stronger than checking `.permissions.push`, which proves ordinary repository scope but not a
ruleset bypass. The workflow summary records whether the applicable ruleset bypass was verified.

An overlapping classic branch-protection rule may still reject the metadata fast-forward even when a
ruleset bypass exists. Remove or align overlapping protection rather than weakening the maintained
rulesets. Publication remains forward-repairable: the immutable tag and verified Release are retained,
and the summary reports when source metadata still requires reconciliation.

### Manual settings path

1. Open **Settings → Secrets and variables → Actions → New repository secret**.
2. Create `RELEASE_PUSH_TOKEN` using the token described below.
3. Open **Settings → Rules → Rulesets**.
4. Edit every active branch ruleset applying to `dev`; add user **cbkii** to **Bypass list** with
   **Always**.
5. Edit every active tag ruleset applying to `v*`; enable **Restrict creations** and add user **cbkii**
   to **Bypass list** with **Always**.
6. Check **Settings → Branches** for an older branch-protection rule applying to `dev`. Remove it when
   it duplicates the ruleset, or align it so the owner token can perform the same ordinary
   fast-forward.
7. Set **Settings → Actions → General → Workflow permissions** to **Read repository contents and
   packages permissions**. The Manual Release workflow requests its own narrow `contents: write`
   permission explicitly; unrelated workflows should remain read-only by default.

### Read-only settings audit

The following bounded, read-only audit fails closed on GitHub API errors, malformed
JSON and incomplete pagination. It supports GNU `timeout` on Linux/Termux and
`gtimeout` from Homebrew coreutils on macOS.

```bash
#!/usr/bin/env bash
set -uo pipefail

repo='cbkii/Auxio-TS'

for required_command in gh jq mktemp; do
  command -v "$required_command" >/dev/null 2>&1 || {
    printf 'STOP: required command is unavailable: %s
' "$required_command" >&2
    exit 1
  }
done

if command -v timeout >/dev/null 2>&1; then
  timeout_command=(timeout --foreground 30s)
elif command -v gtimeout >/dev/null 2>&1; then
  timeout_command=(gtimeout 30s)
else
  printf 'STOP: install GNU timeout (coreutils) before running this audit.
' >&2
  exit 1
fi

api_get() {
  local endpoint=$1
  local errors
  local output
  errors="$(mktemp)"
  if ! output="$("${timeout_command[@]}" gh api "$endpoint" 2>"$errors")"; then
    printf 'STOP: GitHub API request failed: %s
' "$endpoint" >&2
    cat "$errors" >&2
    rm -f "$errors"
    return 1
  fi
  rm -f "$errors"
  printf '%s
' "$output"
}

api_pages() {
  local endpoint=$1
  local errors
  local output
  errors="$(mktemp)"
  if ! output="$(
    "${timeout_command[@]}" gh api --paginate --slurp "$endpoint" 2>"$errors"
  )"; then
    printf 'STOP: paginated GitHub API request failed: %s
' "$endpoint" >&2
    cat "$errors" >&2
    rm -f "$errors"
    return 1
  fi
  rm -f "$errors"
  printf '%s
' "$output"
}

printf '
== Workflow permission ==
'
workflow_permission="$(api_get "repos/${repo}/actions/permissions/workflow")" ||
  exit 1
printf '%s
' "$workflow_permission" | jq -e . || {
  printf 'STOP: workflow-permission response is not valid JSON.
' >&2
  exit 1
}

printf '
== Actions secret names ==
'
secret_pages="$(api_pages "repos/${repo}/actions/secrets?per_page=100")" ||
  exit 1
printf '%s
' "$secret_pages" |
  jq -er '[.[].secrets[]?.name] | unique | .[]' |
  sort || {
    printf 'STOP: Actions-secret response is incomplete or invalid.
' >&2
    exit 1
  }

printf '
== Rulesets ==
'
ruleset_pages="$(api_pages "repos/${repo}/rulesets?per_page=100&includes_parents=true")" ||
  exit 1
ruleset_rows="$(
  printf '%s
' "$ruleset_pages" |
    jq -er 'add | unique_by(.id) | .[] | [.id, .name, .target, .enforcement] | @tsv'
)" || {
  printf 'STOP: ruleset index is incomplete or invalid.
' >&2
  exit 1
}
[[ -n "$ruleset_rows" ]] || {
  printf 'STOP: no repository rulesets were returned.
' >&2
  exit 1
}
printf '%s
' "$ruleset_rows"

while IFS=$'	' read -r id name target enforcement; do
  [[ "$id" =~ ^[0-9]+$ ]] || {
    printf 'STOP: invalid ruleset id: %s
' "$id" >&2
    exit 1
  }
  printf '
--- ruleset %s: %s (%s, %s) ---
' \
    "$id" "$name" "$target" "$enforcement"
  detail="$(api_get "repos/${repo}/rulesets/${id}?includes_parents=true")" ||
    exit 1
  printf '%s
' "$detail" | jq -e '{
    id,
    name,
    target,
    enforcement,
    bypass_actors,
    conditions,
    rules
  }' || {
    printf 'STOP: ruleset %s response is incomplete or invalid.
' "$id" >&2
    exit 1
  }
done <<< "$ruleset_rows"

printf '
== Classic dev protection, when present ==
'
classic_errors="$(mktemp)"
if classic="$(
  "${timeout_command[@]}" gh api \
    "repos/${repo}/branches/dev/protection" 2>"$classic_errors"
)"; then
  rm -f "$classic_errors"
  printf '%s
' "$classic" | jq -e . || {
    printf 'STOP: classic-protection response is not valid JSON.
' >&2
    exit 1
  }
elif grep -Fq 'Branch not protected' "$classic_errors"; then
  rm -f "$classic_errors"
  printf 'No classic branch-protection rule applies to dev.
'
else
  printf 'STOP: classic branch-protection inspection failed.
' >&2
  cat "$classic_errors" >&2
  rm -f "$classic_errors"
  exit 1
fi
```

**STOP:** do not dispatch another release until the audit shows
`RELEASE_PUSH_TOKEN`, the token owner is on the bypass list for every applicable
branch, tag and push ruleset, and overlapping classic protection has been
reconciled.


## GitHub authority references

The release authority model follows GitHub's official documentation:

- workflow reruns retain the original actor's privileges, while
  `github.triggering_actor` identifies who initiated a rerun:
  <https://docs.github.com/en/actions/how-tos/manage-workflow-runs/re-run-workflows-and-jobs>
- repository rulesets support `User` bypass actors and `always` bypass mode, and
  ruleset details only expose `bypass_actors` to callers with ruleset write
  authority:
  <https://docs.github.com/en/rest/repos/rules>
- reading classic branch protection and required-signature protection requires
  repository **Administration: Read**:
  <https://docs.github.com/en/rest/branches/branch-protection>
- `GITHUB_TOKEN` permissions are set per workflow; the repository default remains
  read-only:
  <https://docs.github.com/en/actions/security-for-github-actions/security-guides/automatic-token-authentication>

## Required secrets

### Protected-ref authentication

`RELEASE_PUSH_TOKEN` is mandatory for every Manual Release run. Prefer a fine-grained personal access
token with:

- resource owner: `cbkii`;
- repository access: **Only select repositories → Auxio-TS**;
- repository permissions:
  - **Contents: Read and write** for the immutable tag and `dev` fast-forward;
  - **Administration: Read** so the fail-closed preflight can inspect ruleset bypass actors and classic branch protection;
- a bounded expiry and normal token rotation.

The token's automatically available repository metadata read access is used to inspect complete
ruleset details. The token owner must retain sufficient repository authority for GitHub to return the
`bypass_actors` field; absence of that field fails closed.

A pre-existing classic PAT can be used instead when it belongs to `cbkii` and has repository write
scope, but a repository-scoped fine-grained PAT is preferred. Do not use an APK signing secret, deploy
key, or another user's token as a substitute.

Create or update the Actions secret without printing the token:

```bash
gh secret set RELEASE_PUSH_TOKEN -R cbkii/Auxio-TS
```

The workflow validates this secret before dependency setup or APK building. It fails closed when the
secret is absent, belongs to a different account, does not report push access, or cannot prove the
owner's `User` / `always` bypass on every active ruleset applying to the protected refs. The token is
exposed only to the early identity/access preflight, ruleset preflight and the two Git push steps;
GitHub Release API operations continue to use `GITHUB_TOKEN`.

### APK signing

Release signing is required only when a selected APK requiring the configured release signer must be
built:

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

The workflow fails closed when required signing material is missing. Checkout credentials are not
persisted while repository scripts run.

## Invocation modes

The required `release_mode` choice separates creation from repair.

| Mode | Use | Version behaviour |
| --- | --- | --- |
| `create_new_release` | Create a new immutable tag and GitHub Release | `version_tag` may be blank or explicit |
| `repair_existing_release` | Resume or repair assets for an existing immutable tag | `version_tag` is mandatory; no increment or retagging |

Run **Manual Release** from `dev` only, while signed in as the repository owner.

### New release

A new release performs this transaction:

1. Permit only the current repository owner to enter the release job.
2. Confirm the checkout is exactly the current remote `dev`.
3. Validate `RELEASE_PUSH_TOKEN`, its owner identity and repository push access.
4. Resolve the target version from source metadata, strict semantic Git tags and every GitHub Release
   returned by the paginated Releases API, including drafts, prereleases and final releases.
5. Verify the owner's bypass on every active ruleset applying to `dev` and the exact new tag, and reject
   blocking classic branch protection.
6. Create a local source metadata commit only when the checked-in version differs.
7. Build each required variant at most once.
8. Inspect and stage APKs and sidecars, generate the compact release manifest, and upload recovery
   workflow evidence.
9. Push the immutable release tag using the owner token.
10. Create the GitHub Release as a draft regardless of its requested final status.
11. Upload and verify the exact selected asset set.
12. Apply the requested draft/prerelease/final state.
13. Fast-forward the same released metadata commit onto `dev` using the owner token.

The branch update deliberately occurs after remote Release verification. A tag or draft created by an
interrupted run is retained for repair instead of being deleted.

### Existing-release repair

Repair mode requires an explicit strict `vMAJOR.MINOR.PATCH` tag and uses that tag as immutable source
authority. The branch ruleset authority is still verified because a repair may need to synchronise
released source metadata to `dev`; the existing tag itself is not recreated.

Supported states:

| Tag | Release | Result |
| --- | --- | --- |
| absent | absent | Not repairable; use new-release mode |
| present | absent | Build selected assets, create a draft Release, verify, then apply requested status |
| present | present | Preserve current status and repair only selected assets |
| absent | present | **STOP**: inconsistent authority |
| present at conflicting source | any | **STOP**: tags are never moved |

When an existing selected APK plus both sidecars are already present and replacement is disabled, that
complete triplet is reused without rebuilding. A partial triplet fails closed because rebuilding only
one member could make the remote evidence inconsistent. Enable replacement to rebuild and replace the
complete selected triplet.

Replacement uses the GitHub CLI clobber operation only after rebuilt files have staged and validated.
Unrelated historical assets are preserved.

Historical tags must contain the release build and validation contracts required for the selected
variant. The current workflow may orchestrate a repair, but it does not silently substitute current
application source or weaken historical package/signing authority.

## Version authority

Only strict `major.minor.patch` values participate. Optional leading `v` is normalised.

Automatic resolution considers:

- checked-in `app/build.gradle` `versionName`;
- all strict semantic Git tags;
- all strict semantic GitHub Release tags from every status, including draft, prerelease and final.

The Releases API is paginated; the workflow does not use the “latest release” endpoint.

Normally the next version is one patch above the highest external tag/Release authority. When source
metadata is already ahead of all external authority and that version has no tag or Release, the source
version itself is reused. This recovers a prior source-only metadata bump without skipping another
version.

The version code is deterministic:

```text
major * 1,000,000 + minor * 10,000 + patch * 100
```

Examples:

- `6.4.7` → `6040700`
- `6.4.8` → `6040800`
- `6.5.0` → `6050000`

The workflow rejects version-code regressions and unexplained collisions. Repair mode reads the exact
version metadata from the immutable tag rather than inventing a new value.

## Maintained install assets

| Selection | Published asset | Package inside | Constraint |
| --- | --- | --- | --- |
| Topway `twmedia` APK | `Auxio-TS-vX.Y.Z-topway-twmedia-release.apk` | `com.tw.media` | Primary DoFun alternate-entry lane; exact TS18 runtime still requires device validation |
| LSPosed API 100 bridge addon | `Auxio-TS-vX.Y.Z-lsposed-api100-bridge.apk` | `org.oxycblt.auxio.ts18bridge` | Keep genuine stock `com.tw.music`; enable only its static/recommended `com.tw.music` scope |

Each selected maintained lane also has a separately labelled debug companion:

- `Auxio-TS-vX.Y.Z-topway-twmedia-debug.apk`
- `Auxio-TS-vX.Y.Z-lsposed-api100-bridge-debug.apk`

The required `debug_variant_destination` choice controls the APK and its `.sha256` and
`.metadata.txt` sidecars:

| Value | Result |
| --- | --- |
| `workflow_artifacts` | Default. Build debug companions once and upload them only as short-lived, 14-day Actions artifacts. |
| `release_assets` | Publish debug companions and sidecars as explicitly selected GitHub Release assets. Complete existing repair triplets may be reused. |

Debug companions use separate debug application IDs and are diagnostic-only. Publishing them does not
make them the normal install or prove TS18 runtime behaviour.

## Efficient validation model

The release critical path favours targeted evidence:

- one Gradle assembly for each required variant;
- one primary SHA-256 calculation per built APK;
- one primary `aapt dump badging` result per built APK;
- one primary `apksigner verify --verbose --print-certs` result per built APK;
- existing focused app, diagnostics-boundary, startup-profile and LSPosed contract checks;
- one compact JSON release manifest;
- one remote Release asset listing after upload.

The manifest records only publication-critical fields:

- filename and asset classification;
- SHA-256;
- package/application ID;
- version name and code;
- signer SHA-256;
- source commit and release tag;
- release or debug destination.

It does not add broad APK-content inventories or repeat repository-wide audits in the release critical
path.

## Recovery evidence and failure handling

Before remote mutation, the workflow uploads a 14-day recovery artifact containing the staged release
assets, asset plan, version plan and compact manifest. Workflow-only debug companions are uploaded in
a separate 14-day artifact.

Failure handling is forward-repairable:

- non-owner dispatch: the release job is skipped before secret access;
- protected-ref token identity/access failure: stop before dependency setup/build and change no release
  state;
- ruleset or classic-protection authority failure: stop before dependency setup/build and change no
  release state;
- validation failure before tag creation: no remote release state is changed;
- tag pushed but Release absent: rerun in repair mode with that tag;
- draft Release with missing assets: repair the same tag; do not generate another version;
- published Release missing a selected asset: repair with replacement only when necessary;
- Release verified but `dev` metadata push rejected: preserve the release and reconcile `dev` with the
  immutable release commit before the next release.

Do not delete or move a valid release tag to “retry” publication.

## Local static validation

```bash
python3 -m py_compile \
  scripts/release-orchestrator.py \
  scripts/check-release-ruleset-authority.py
python3 scripts/release-orchestrator.py self-test
python3 scripts/check-release-ruleset-authority.py --self-test
bash -n scripts/check-manual-release-workflow.sh
bash scripts/check-manual-release-workflow.sh
bash scripts/check-ci-variant-contracts.sh
bash scripts/check-ts18-apk-reference-contracts.sh
```

Release-equivalent APK validation still uses:

```bash
bash ./scripts/bootstrap-dependencies.sh --profile release
bash ./scripts/ci-gradle.sh :app:assembleTopwayTwMediaRelease
bash ./scripts/check-startup-performance-contracts.sh path/to/release.apk
bash ./scripts/check-release-diagnostics-boundary.sh path/to/release.apk
bash ./scripts/check-app-release-contracts.sh \
  --apk path/to/release.apk \
  --version-name X.Y.Z \
  --version-code N \
  --expected-signer SHA256 \
  --sha256-file path/to/release.apk.sha256 \
  --metadata-file path/to/release.apk.metadata.txt
```

The `topwayTwMusic` flavour remains an internal contract build only. Never publish its APK or recreate
the retired Magisk overlay lane.
