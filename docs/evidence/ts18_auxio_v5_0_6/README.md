# TS18 Auxio-TS v5.0.6 support evidence index

This support pack is intended to be extracted at the root of `cbkii/Auxio-TS` before starting the Codex implementation pass.

## Included evidence

- `docs/evidence/ts18_auxio_v5_0_6/TS18_AuxioMediaDiag_AuxioTS_v5.0.6_report.md` — human-readable diagnostics analysis report.
- `docs/evidence/ts18_auxio_v5_0_6/auxioPerms.md` — permission advice for normal APK vs optional Magisk/privileged lane.
- `docs/evidence/ts18_auxio_v5_0_6/TS18_AuxioMediaDiag.zip` — original user-provided diagnostics archive.
- `evidence/ts18_auxio_v5_0_6/raw/` — extracted raw diagnostics archive, preserving original paths.
- `evidence/ts18_auxio_v5_0_6/derived/` — NUL-stripped helper copies of key text files. Do not treat these as replacing raw evidence.
- `docs/prompts/codex_ts18_auxio_full_scope_prompt.md` — full implementation prompt.

## High-priority observations from the evidence

1. **Crash stopper:** `com.tw.music` crashed with `SecurityException: Failed to find provider  for user 0` from `ContentResolver.registerContentObserver()` via `org.oxycblt.musikr.fs.track.LocationObserver`. The blank provider strongly suggests an invalid or blank-authority URI reached observer registration.
2. **Runtime identity:** `com.tw.music` ran as normal app UID `u0_a175` / UID `10175`, not UID 1000/platform/system. Package naming did not grant platform signing, signature permissions, UID 1000, or Topway private authority.
3. **Diagnostics quality:** The diagnostics run was partial: expected report/package/media-session/notification/audio/snapshot/package files were missing or empty; several text files were NUL-padded/truncated; a stale lock blocked a later run.
4. **Overlay evidence:** `com.tw.music` posted overlay foreground notification id `42` on `auxio_car_overlay_channel`, and Android posted the alert-window notification, proving the overlay path started at least once. It does not prove visible/touchable overlay after boot/wake/relaunch.
5. **Indexer churn:** The log shows repeated indexer progress notification updates on `com.tw.music.channel.INDEXER` / id `41121`, roughly every 1–2 seconds during indexing. Rate-limit/coalesce progress updates.
6. **Source/picker flow:** `ACTION_OPEN_DOCUMENT_TREE` resolved and launched `com.android.documentsui/.picker.PickActivity`, but the evidence does not prove full storage roots, persisted URI grants, final source validity, or scan success.
7. **Storage state:** No `/storage/usbdiskN` or `/mnt/media_rw/usbdiskN` was visible in the captured mount snapshot. Treat this as “no USB visible during capture,” not proof that USB support is broken.
8. **Magisk/system state:** Magisk root, SELinux permissive, dynamic partitions, and an existing Magisk-mounted DocumentsUI/privapp-permission file were observed. This supports investigating a systemless module lane, but not treating root as platform signing.

## Evidence labels

Use **Observed** only for facts visible in the attached report/raw files. Use **Inferred** for engineering conclusions from those facts. Use **Requires TS18 device validation** for any DoFun launcher/widget, overlay z-order/touch, ACC sleep/wake, USB mount, audio focus, or private Topway behaviour not directly proven here.
