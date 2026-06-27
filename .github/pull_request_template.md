## Scope

- [ ] Code / build / workflow change
- [ ] TS18 / Topway / DoFun compatibility change
- [ ] Test / lint / formatting change
- [ ] Documentation-only change

## CI expectations

Documentation-only changes under `docs/**` intentionally do not trigger automatic Android CI.

For any code, workflow, dependency, Gradle, or script change, rely on the scoped CI tasks in `.github/workflows/`. Do not use generic aggregate Gradle tasks such as `check`, `build`, `test`, or `lint` as PR proof unless the workflow itself has intentionally been changed to use those tasks.

## Validation evidence

Paste workflow/task results here, or explain why validation is not applicable.

```text
Android Build:
Android Quality / Formatting:
Android Quality / Unit tests:
Android Quality / Android lint:
Head-unit safety:
```

## TS18 compatibility notes

For TS18/TW/TWTHEME claims, label the evidence:

- Evidence confidence: Observed / Inferred / Hypothesis / Requires TS18 validation / Unsupported
- Porting decision: Directly reusable requirement / Reusable validation idea / Useful as evidence only / Requires TS18 runtime validation / Unsafe to port / Should be explicitly avoided
