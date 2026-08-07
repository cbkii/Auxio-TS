# MediaStore source authority

MediaStore is a provider-discovered backend. Unlike SAF and DirectFS, its authoritative source
identities are Android MediaStore volumes discovered during source preflight, not explicit folder
entries persisted in `configuredSourceSpecs`. Provider source keys therefore come from the observed
volume snapshots and are never synthesized from an empty folder configuration.

## Invariants

- An empty explicit-root set is **not** evidence that MediaStore has zero sources.
- A MediaStore configuration becomes authoritatively empty only after provider discovery and a
  complete scan of the intended volumes accepts zero music items with no source-level failure.
- Provider query/volume failures remain source-level failures and preserve the last readable source
  generation.
- A row that disappears between MediaStore enumeration and descriptor open is item-level
  uncertainty. It must not poison an otherwise healthy provider volume.
- If a transiently unavailable item already belongs to the committed generation, its committed row
  is carried into the pending generation. A newly stale row with no committed predecessor is
  omitted.
- If every candidate item is unresolved, the source fails closed rather than committing a false
  authoritative empty generation.
- Once MediaStore reconciles and stops enumerating a previously retained stale URI, normal
  generation reconciliation tombstones and removes that URI.
- Retaining an older item must not falsely promote its metadata profile.

These rules preserve the transactional source-generation and last-known-good library guarantees
described in `CACHED_PRESENTATION_VS_SOURCE_AUTHORITY.md`.

## Validation boundary

JVM, API 29 and ordinary Android-device tests can prove source identity, failure classification and
transactional cache semantics. They do not prove the exact TS18 MediaProvider, removable-media,
boot or ACC lifecycle behaviour; those remain physical-device acceptance checks.
