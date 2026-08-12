# Documentation

Start with the document that owns the question:

- [Product scope](PRODUCT_SCOPE.md) — product, package, module, support and artefact matrix.
- [Architecture](ARCHITECTURE.md) — runtime ownership, source boundaries and Tracks A/B/C.
- [Development](DEVELOPMENT.md) — setup, build, test and validation commands.
- [Release workflow](RELEASE_WORKFLOW.md) — signed app and optional add-on publication policy.
- [Repository engineering authority](../AGENTS.md) — instructions and safety precedence.
- [Single-product architecture decision](decisions/0001-single-product-architecture.md).
- [Documentation disposition inventory](DOCUMENTATION_INVENTORY.md).

## Current guides and runbooks

- [Settings guide](SETTINGS_GUIDE.md)
- [Advanced use and contributing](ADVANCED_AND_CONTRIBUTING.md)
- [TS18 installation constraints](TS18_INSTALLATION_CONSTRAINTS.md)
- [DoFun/Topway compatibility guide](DOFUN_VARIETY_COMPATIBILITY.md)
- [LSPosed Track-C guide](ts18/launcher-integration/LSPOSED_API100_BRIDGE.md)
- [Root storage fast path](ts18/ROOT_STORAGE_FASTPATH.md)
- [Physical TS18 validation](TS18_RUNTIME_VALIDATION.md)
- [Exact-device context](CODEX_TS18_DEVICE_CONTEXT.md)

## Current architecture references

- [Fast interaction startup](architecture/FAST_INTERACTION_STARTUP.md)
- [Incremental library pipeline](architecture/INCREMENTAL_LIBRARY_PIPELINE.md)
- [Canonical sources and DirectFS](architecture/CANONICAL_SOURCES_AND_DIRECTFS.md)
- [Source-scan attempt leases](architecture/SOURCE_SCAN_ATTEMPT_LEASES.md)
- [Cached presentation versus source authority](architecture/CACHED_PRESENTATION_VS_SOURCE_AUTHORITY.md)
- [Startup profiles and benchmarks](architecture/STARTUP_PROFILES_BENCHMARKS.md)
- [Exact TS18 startup validation](validation/EXACT_TS18_STARTUP_VALIDATION.md)

## Evidence

Evidence supports engineering decisions but is non-normative. Use only the curated evidence index and provenance records; do not start routine work in raw decompilation output. Label claims **Observed**, **Inferred**, **Proposed** or **Physically unverified**.

- [Curated evidence index](evidence/README.md)
- [TS18 APK reference](TS18_APK_REFERENCE.md)
- [Topway research index](topway/README.md)

Historical facts remain in [CHANGELOG.md](../CHANGELOG.md). Old prompts and status reports do not define current policy.
