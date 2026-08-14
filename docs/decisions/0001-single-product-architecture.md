# ADR 0001: One maintained Auxio-TS application

- **Status:** Accepted
- **Date:** 2026-08-13

## Context

Auxio-TS accumulated a generic `standard` app and two Topway package-identity flavours while device integration was being explored. The useful code converged on the `com.tw.media` product, but build, CI, release and documentation continued to describe multiple distributable applications. The exact `com.tw.music` flavour also modelled a protected stock identity that a normally signed Auxio APK cannot legitimately replace.

## Decision

Maintain one application with ID `com.tw.media` and namespace `org.oxycblt.auxio`. Compile the shared Topway/DoFun Track-A compatibility sources into its main source set and expose the proven `com.tw.music.MusicActivity`, service and widget component contracts from that application.

Retire both the generic `standard` application and the exact-package Auxio `topwayTwMusic` application from Gradle, CI, benchmarks, screenshots and releases. Keep `lsposed-bridge` as a separate, optional Track-C add-on statically scoped to genuine stock `com.tw.music`.

## Coverage retained without app variants

- Test Android-standard fallback through pure policies, dependency injection, JVM tests and instrumentation.
- Test package/component matching with contract fixtures and manifest/APK inspection.
- Retain curated stock-package evidence and provenance without a distributable impersonation APK.
- Validate the one app on API 29 and use `startup-benchmark` only as validation infrastructure.

## Consequences

Ordinary tasks are unflavoured (`assembleDebug`, `testDebugUnitTest`, `lintDebug`). Release produces one primary app and can separately include the optional bridge. Any future app flavour or distributable module requires a new architecture decision meeting the expansion gate in root `AGENTS.md`.
