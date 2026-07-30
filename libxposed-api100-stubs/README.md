# libxposed API 100 compile-only stubs

This module contains an unmodified source subset from the official `libxposed/api` **API 100** commit:

```text
45f3e9722a3d4a3e6dae6cc1b51d6583767ec940
```

Upstream: `https://github.com/libxposed/api`

The sources are Apache-2.0 licensed and are used only to compile `:lsposed-bridge`. The bridge declares this project with Gradle `compileOnly`; the classes must not be packaged into the APK because LSPosed supplies them at runtime.

`./scripts/check-lsposed-bridge-contracts.sh` fails if a built bridge APK defines any `io.github.libxposed` classes.
