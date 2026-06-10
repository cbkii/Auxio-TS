# Final package notes

This package is curated for source-control/Agent use. It deliberately excludes APKs, raw decompile trees, binary resources, and large runtime dumps.

## Final conclusion

The evidence now points to two separate problems:

1. **Implementation completeness:** Auxio-TS must fully emulate the stock TW Music component/action/broadcast/widget contract in the `com.tw.media` Topway variant.
2. **Launcher routing while stock remains installed:** DoFun's fixed Music widget currently opens/controls stock `com.tw.music` when stock and Auxio coexist. Source changes alone may not force DoFun to route to `com.tw.media` until stock is disabled, absent, cache-refreshed, or DoFun's fixed target selection changes.

Agents should implement the contract completely and preserve fallback overlay controls, but must not claim fixed DoFun widget replacement without TS18 validation.

## What changed from earlier packs

- Added final TS18 v2 runtime addendum.
- Marked v2 synthetic probes invalid because they used app UID `10177` as `--user`.
- Added corrected v3 collector using `ANDROID_USER_ID=0`.
- Updated implementation prompt to prioritise stock TW Music contract over Media3-only work.
