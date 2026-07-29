# TS18 launcher media integration

This folder separates two compatibility paths:

1. **In-app Android/Topway compatibility** in the normal Auxio-TS `com.tw.media` app.
2. **Optional LSPosed stock-identity bridge** for exact TS18 firmware where the fixed DoFun widget requires calls or broadcasts to originate from the genuine platform-signed `com.tw.music` process.

The LSPosed bridge is not a platform-signature clone and does not replace the stock APK. Start with the in-app path and add the bridge only when exact-device evidence proves that Android MediaSession and safe Topway broadcasts are insufficient.

Files:

- `docs/ts18/launcher-integration/LSPOSED_API100_BRIDGE.md`
- `docs/prompts/codex_ts18_launcher_comprehensive_in_app_integration.md`
- `docs/ts18/launcher-integration/TS18_LAUNCHER_COMPREHENSIVE_IN_APP_PLAN.md`
- `docs/ts18/launcher-integration/TOPWAY_MUSIC_WIDGET_CONTRACT.md`
- `docs/ts18/launcher-integration/VALIDATION_MATRIX.md`
- `scripts/evidence/collect-ts18-launcher-media-integration.sh`

Run the evidence script on the physical TS18 only for a defined test window, export the result, then stop additional logging.
