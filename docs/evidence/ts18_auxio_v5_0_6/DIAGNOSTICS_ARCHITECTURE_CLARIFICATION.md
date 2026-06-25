# Diagnostics architecture clarification

## Corrected interpretation

**Observed:** `TS18_AuxioMediaDiag.zip` is output from an **external Magisk/service.d diagnostics collector**, not from Auxio-TS's in-app diagnostics harness.

**Action for Codex:** do not fix or expand `DiagnosticsRepository`, `DiagnosticService`, `DiagnosticsViewModel`, diagnostic UI, or armed in-app capture. Remove the abandoned in-app diagnostics from Auxio-TS where safe. Keep and improve the external collector under `tools/ts18-auxio-media-diag-pack-v3-recommended/`.

## Why this matters

The earlier prompt wrongly treated missing archive artifacts as defects in Auxio's in-app diagnostics. They are defects/limitations of the external collector output and should be handled in the external collector. App-side code should focus on runtime stability, overlay reliability, fast resume, source handling, MediaSession/notification parity, and normal/optional Magisk packaging.

## External collector handling

- The original v3 collector is preserved under `tools/ts18-auxio-media-diag-pack-v3-original/`.
- A recommended no-in-app-diagnostics copy is under `tools/ts18-auxio-media-diag-pack-v3-recommended/`.
- The recommended copy writes `auxio/in-app-diagnostics-skipped.txt` instead of trying to start `DiagnosticService`.
- Codex should audit and improve the recommended collector, not the old in-app diagnostics.
