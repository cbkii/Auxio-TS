# Upstream Auxio monitoring

Auxio-TS intentionally does **not** use GitHub fork sync, fork-network compare automation, automatic upstream merges, automatic cherry-picks, or automatic upstream pull requests.

Auxio-TS is a Topway/DoFun/TS18-focused Auxio variant. Upstream `OxygenCobalt/Auxio` work can be valuable, but every upstream change must be reviewed and adapted intentionally so Auxio-TS keeps its package identities, Topway/DoFun variants, TS18 docs, release workflows, guardrails, and compatibility wrappers coherent.

[Evidence confidence: Requires TS18 validation] [Porting decision: Reusable validation idea]

## Workflow

The monitor workflow is `.github/workflows/upstream-auxio-monitor.yml` (`Upstream Auxio Monitor`). It runs monthly on the Auxio-TS `dev` branch and can also be started manually with `workflow_dispatch`.

The workflow:

1. checks out Auxio-TS `dev` with full history;
2. adds `https://github.com/OxygenCobalt/Auxio.git` as a temporary local git remote only for the run;
3. fetches the verified upstream branch recorded in the baseline file, unless an explicit `UPSTREAM_BRANCH` environment override is supplied for diagnostics;
4. compares the current upstream head with the stored baseline SHA;
5. creates one Auxio-TS issue only when upstream has actual changes to review;
6. uploads complete report, diff, and patch artifacts only for changed-upstream runs;
7. authenticates GitHub CLI with `GH_TOKEN`/`GITHUB_TOKEN` for issue search and creation only when upstream changed and the run is not a dry run;
8. keeps checkout credentials only so the baseline commit can be pushed back to `dev` after a permitted baseline update;
9. never applies, merges, syncs, cherry-picks, or opens PRs with upstream code.

## Silent no-change behaviour

No-change runs are intentionally quiet. If the stored baseline SHA equals the current upstream head, the workflow only writes concise logs and a job summary, then exits successfully.

A no-change run does **not**:

- create an issue;
- comment on an issue;
- create a PR;
- upload large artifacts;
- update the baseline file;
- commit anything;
- churn timestamps such as `last_seen_at`.

GitHub Actions run history is the audit trail for no-change checks, so the repository does not receive “checked today, nothing changed” commits.

## Baseline file

The stored upstream baseline is `.github/upstream-auxio-baseline.json`.

The baseline means **last acknowledged upstream head**, not “last fully adapted upstream head.” Advancing the baseline prevents duplicate monitoring issues for the same upstream SHA. It does not mean the upstream commits were merged or accepted into Auxio-TS.

The baseline is updated only when the SHA changes. Baseline writes are performed with Python and validated as JSON before the workflow commit step runs:

- **First-time initialisation:** if `last_seen_sha` is empty, the monitor records the current upstream head silently, commits that baseline update, and creates no issue. This prevents the first run from reporting the entire upstream history.
- **Real upstream change:** if the stored SHA differs from the upstream head, the monitor creates an actionable issue first. Only after issue creation succeeds does it update and commit the baseline.
- **Duplicate open issue:** if an open Auxio-TS issue already contains the new upstream SHA, the monitor logs that issue URL, creates no duplicate issue or scheduled-run comment, and may advance the baseline to acknowledge that the SHA was already reported.

Dry-run runs never update the baseline. If `UPSTREAM_BRANCH` is set in the environment it overrides the recorded branch for that run; otherwise the recorded branch is used, and if the recorded branch is empty the script resolves the upstream default branch.

## Manual runs

To run the monitor manually after the workflow is merged:

1. open **Actions** in GitHub;
2. choose **Upstream Auxio Monitor**;
3. select **Run workflow** on branch `dev`;
4. leave `dry_run=false` for a real monitor run, or set `dry_run=true` to generate changed-upstream reports without creating an issue or updating the baseline;
5. leave `update_baseline=true` unless intentionally testing issue creation/baseline behaviour.

Manual runs obey the same no-clutter rule as scheduled runs: if upstream has not changed, no issue, comment, PR, artifact, baseline update, timestamp update, or commit is produced.

## Generated issues for agents

When upstream changed, the issue includes:

- upstream repo URL and branch;
- old and new SHA values;
- comparison range;
- workflow run URL and trigger type;
- commit count and commit list;
- changed file list;
- diff stat;
- inline diff when small enough, otherwise artifact location;
- a ready-to-use agent prompt for reviewing and adapting upstream changes.

Agents must treat the issue as a monitoring report only. They should inspect upstream commits and the corresponding Auxio-TS files, classify each change, adapt only safe changes, document intentionally skipped changes, and preserve Auxio-TS invariants. No upstream patch is automatically trusted or applied by the monitor.

## Resetting or reseeding the baseline

To reseed the baseline intentionally:

1. inspect `OxygenCobalt/Auxio` and decide which upstream head should be acknowledged;
2. edit `.github/upstream-auxio-baseline.json` so `upstream_branch` is the verified upstream branch and `last_seen_sha` is the chosen upstream commit;
3. update `last_seen_at` only because the baseline SHA changed;
4. commit the baseline edit with a message explaining why it was reseeded;
5. run **Upstream Auxio Monitor** on `dev` to confirm either silent no-change behaviour or one issue for newer upstream changes.

Do not reseed the baseline to avoid reviewing a real upstream change unless that decision is documented and intentional.
