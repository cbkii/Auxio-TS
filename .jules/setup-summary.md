# Jules setup summary

- Readiness: `STATIC_REVIEW_ONLY`
- Android SDK ready: `true`
- Submodules ready: `false`
- Gradle smoke ready: `false`
- Notes: Snapshot usable for source/static work; agents must run and report strict validation before claiming build/APK readiness.

Agents must read `.jules/setup-status.env` before claiming build/test/APK validation.

If readiness is `STATIC_REVIEW_ONLY`, agents may inspect and edit source/docs/scripts, but must not claim Gradle, APK, or runtime validation passed.

If readiness is `FULL_BUILD_READY`, agents may run Gradle tasks and report their actual results.
