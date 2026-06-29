# Jules setup summary

- Readiness: `FULL_BUILD_READY`
- Android SDK ready: `true`
- Submodules ready: `true`
- Gradle smoke ready: `true`
- Notes: Jules snapshot has SDK, submodules, and Gradle smoke readiness.

Agents must read `.jules/setup-status.env` before claiming build/test/APK validation.

If readiness is `STATIC_REVIEW_ONLY`, agents may inspect and edit source/docs/scripts, but must not claim Gradle, APK, or runtime validation passed.

If readiness is `FULL_BUILD_READY`, agents may run Gradle tasks and report their actual results.
