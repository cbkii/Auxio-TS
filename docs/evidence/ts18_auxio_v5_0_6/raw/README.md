# Raw TS18 Auxio v5.0.6 evidence

These files are retained as historical capture artifacts. They may contain now-superseded
wording from the original diagnostic report, including references to Auxio in-app
diagnostics or Spotify comparison steps.

Current product decision:

- TS18 diagnostics are collected externally by the Magisk/service.d tooling under
  `tools/ts18-auxio-media-diag-pack-v3-recommended/`.
- Auxio-TS in-app diagnostics, the former TS18 Health Diagnostics / Storage Health
  screen, armed in-app capture, `DiagnosticService`, `DiagnosticsRepository`, and
  `DiagnosticsViewModel` are abandoned and must not be restored.
- Spotify is historical negative-control evidence only. Stock `com.tw.music` and VLC
  are the active positive references.
