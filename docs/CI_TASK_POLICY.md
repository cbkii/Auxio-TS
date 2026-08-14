# CI task policy

The current build/test commands and CI ownership live in [Development](DEVELOPMENT.md). This file records the focused-CI rule only.

`scripts/ci-scope.sh` classifies changes and must fail open when it cannot prove a narrow scope. `scripts/check-product-contracts.sh` enforces the one `com.tw.media` application and rejects retired flavours, tasks and publication paths.

Routine CI uses explicit unflavoured tasks such as `:app:assembleDebug`, `:app:testDebugUnitTest` and `:app:lintDebug`. API 29, startup, release or LSPosed validation is selected only when the changed ownership boundary requires it. Do not broaden every routine change merely to compensate for removed variants.

Stable required check names are listed in [Development](DEVELOPMENT.md#ci-policy).
