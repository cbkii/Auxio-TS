## Summary

- **Problem:**
- **Outcome:**

## Scope and architecture

- **Affected product/module classification:** Active product / internal library / optional add-on / validation / tooling / evidence / retired
- **Runtime authority:** Preserved / changed — identify playback, queue, MediaSession, notification and audio-focus impact
- **Canonical source of truth updated:**
- **Device evidence:** Observed / inferred / proposed / physically unverified
- **Evidence environment/boundary:** CI / emulator / exact device — identify what remains unverified
- **Release implications:** None / app / optional add-on / tooling
- **Rollback or disable path:**

### Included

-

### Not included

-

## Validation

**Validated head SHA:** `________________`

| Exact command or scenario | Result | Environment/boundary |
| --- | --- | --- |
| `bash scripts/check-product-contracts.sh` | Not run — reason | Local / CI |
| Focused tests/build/lint command | Not run — reason | Local / CI / emulator |
| Physical-device scenario | Not run — reason | Device/build identifier |

## Review status

- **Delivery state:** Implemented / partial / scaffold-only
- **Submission state:** Review snapshot / complete for stated scope
- **Open findings:**
- **Required checks:** Pending / passing / failing
- **Known limitations:**

## Checklist

- [ ] Product/module classification and runtime ownership are explicit
- [ ] Relevant canonical documentation is updated without duplicating authority
- [ ] Package, component, release and rollback implications are covered
- [ ] Current-head checks are recorded accurately
- [ ] Physical-device claims are limited to evidence actually observed
- [ ] No unrelated, generated, credential or temporary files are included
