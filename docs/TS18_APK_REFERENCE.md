# TS18 APK evidence summary

This is a non-normative evidence summary. Product decisions come from [Product scope](PRODUCT_SCOPE.md) and [Architecture](ARCHITECTURE.md); curated extracted artefacts live under [`reference/ts18-apk`](reference/ts18-apk/).

## Provenance

| APK | SHA-256 | Observed role |
| --- | --- | --- |
| `com.dofun.variety_V9.7.2.367.260312.apk` | `75e7ea9b46d68754253aa385e6ac750aae957a5b72196fec5449ccf2782c60b1` | DoFun launcher/theme music selection. |
| `com.tw.music_TW_THEME.20240715.apk` | `4f5495e270a7c86bab232e2b7ee2ecd2d71f3450f6f20ed5f36feaa4229c1518` | Genuine stock Topway music contract; version code 118 in the captured device evidence. |

The captured stock signer SHA-256 is `AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3`. The binaries are not committed.

## Observed contracts

DoFun matching includes:

```text
com.tw.media / com.tw.music.MusicActivity
com.tw.music / com.tw.music.MusicActivity
```

Auxio-TS satisfies the first contract as `com.tw.media`; the second identifies genuine stock and does not justify an Auxio `com.tw.music` APK.

Observed public actions/extras and component facts are consolidated in [Topway stock contract evidence](evidence/TOPWAY_STOCK_CONTRACT.md) and the machine-readable `reference-contracts.json`.

## Interpretation

- **Observed:** package/component, action/extra, signer and captured-version facts.
- **Inferred:** related firmware may reuse these contracts.
- **Physically unverified:** current device behaviour until repeated against the exact installed firmware/build.

Do not port shared UID, platform identity, private Cardoor services, `TWUtil`, copied smali or unverified Binder contracts. Keep the curated reference set small and provenance-preserving.
