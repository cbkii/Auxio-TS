<!--
PR WORKFLOW FOR AUTHORS AND AI AGENTS

- Open one Draft PR early, targeting the requested base branch.
- Continue all work on this PR's existing branch. Do not create replacement PRs.
- After each coherent work batch, push the commits and update this body:
  - current status, HEAD and next step;
  - task checklist;
  - changes completed;
  - validation results;
  - blockers or outstanding work.
- Read the complete review comments and relevant CI logs before making fixes.
- Resolve only review threads that are genuinely completed.
- Never report a check as passed unless it ran successfully against the current HEAD.
- Mark the PR ready for review only after the final diff, checks and remaining
  limitations have been reviewed.
- Remove these instructions only if they interfere with the final description.
-->

## Summary

<!-- Briefly explain the problem, the intended outcome and why it matters. -->

- **Problem:**
- **Outcome:**

## Related work

<!-- Use "Closes #123" only when this PR fully resolves the issue. -->

- Closes:
- Related:
- Depends on:

## Scope

### Included

- 

### Not included

- 

## Work progress

**Status:** Draft — Researching / Implementing / Validating / Blocked / Ready for review  
**Base branch:** `dev`  
**Current HEAD:** `________________`  
**Next step:**  
**Last updated:** `YYYY-MM-DD HH:MM TZ`

<!-- Replace this with a task-specific, dependency-ordered checklist. -->

- [ ] Confirm requirements, repository instructions and current branch state
- [ ] Identify the root cause or implementation approach
- [ ] Implement the scoped changes
- [ ] Add or update relevant tests
- [ ] Run focused validation
- [ ] Review the complete diff and affected integrations
- [ ] Address valid review feedback, conflicts and CI failures
- [ ] Complete final current-HEAD verification

## Changes

<!-- Keep this current as the implementation evolves. -->

- 

## Validation

**Validated HEAD:** `________________`

<!-- Record only checks that actually ran. Use "Not run — reason" when needed. -->

| Check or scenario | Result |
|---|---|
|  |  |

### Artefacts and runtime testing

<!--
List APKs, modules or other packaged outputs that were actually inspected or
tested. For TS18-specific behaviour, distinguish emulator/CI success from
physical-device validation.
-->

- **Artefacts:**
- **Runtime/device testing:**
- **Still requires physical validation:**

## Review and CI status

<!-- Summarise current state rather than copying large logs. -->

- **Review findings still open:** None / …
- **Required checks:** Pending / Passing / Failing — …
- **Merge conflicts:** None / …
- **Current blocker:** None / …

## Risks and rollback

<!-- Include only meaningful compatibility, migration or recovery information. -->

- **Known risks or limitations:**
- **Compatibility considerations:**
- **Rollback or disable path:**

## Ready-for-review checklist

- [ ] The PR title, summary and checklist describe the current HEAD
- [ ] The final diff contains no unrelated changes or temporary files
- [ ] Relevant tests and validation are recorded accurately
- [ ] Packaged artefacts were checked where packaging can affect behaviour
- [ ] Review feedback has been classified and valid findings addressed
- [ ] Required checks are passing on the current HEAD
- [ ] Remaining physical validation or human decisions are clearly identified
- [ ] No known conflict or unresolved blocker prevents review
