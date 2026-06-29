# Android component security notes

## CoverProvider exported state

**Observed in external docs:** Android content providers are declared with a `<provider>` element. The manifest supports `android:exported`, `android:permission`, `android:readPermission`, `android:writePermission`, and `android:grantUriPermissions`.

**Observed in external docs:** `android:exported="true"` means other applications can use the provider. `android:exported="false"` means the provider is unavailable to other apps, except to apps with the same user ID or where temporary URI permissions have been granted.

**Inferred for Auxio-TS:** For `CoverProvider`, the safest default is probably `exported=false` unless a verified external consumer needs provider access. If external access is needed, prefer a narrow permission or temporary URI grants over unconditional export.

**Requires repo validation:** Check notification artwork, widgets, MediaSession metadata, cover-loading code, and any FileProvider/content-URI consumers before changing provider exported state.

**Performance guard:** Do not add provider initialization, cover scanning, bitmap decoding, or external grant bookkeeping to the cached startup path.

## Root command hardening

**Observed in external docs:** CWE-78 describes OS command injection: software builds a command using externally influenced input and executes it, allowing an attacker to change the intended command or run arbitrary commands.

**Observed in external docs:** CWE-78 mitigations prefer library calls or structured APIs over command strings; if command input cannot be avoided, use strict allow-list validation and least privilege.

**Inferred for Auxio-TS:** A method named like `runRootCommandSync(command: String)` is a broad authority surface even if current callers are trusted. A safer shape is a narrow interface for the actual operation, such as `listDirectory(path)` where the implementation validates roots, constructs any command internally, and bounds execution time.

**Requires repo validation:** Current DirectFS/root code may need root-assisted listing for `/storage` or `/mnt/media_rw` aliases. Preserve non-root operation and keep root unavailable on standard variants.

**TS18-specific guard:** Root is diagnostic/compatibility authority only. It does not grant platform signing, UID 1000, vendor identity, or private Topway contract access.
