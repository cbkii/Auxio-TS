---
applyTo: "app/apktool/smali*/**"
---

# Smali development rules

## Target platform context

The app runs on **TS18 Android 13** head units under the TW vendor firmware. When reasoning about Android API behaviour from smali:

- The APK was compiled against SDK 29 (Android 10) but runs on Android 13 — standard SDK 29 API surface applies, with Android 13 runtime behaviour.
- Do not assume AOSP stock behaviour for media session, audio focus, or notification APIs — TS18 vendor firmware customises these surfaces.
- Vendor AIDL services (`com.tw.service.xt`) and system properties (`persist.tw.*`) are firmware-provided; their behaviour is opaque and must be preserved exactly.
- See `docs/target-device-ts18-android13.md` for the full device profile and TW vendor environment assumptions.

## Smali is the source of truth

- `app/apktool/smali*/` is the canonical, editable source.
- JADX Java output under `reference/` is a reading aid only — it may contain decompiler errors, missing branches, or incorrect type inference.
- If JADX output contradicts smali behaviour, **trust the smali**.
- Optional MT-normalised output (`reference/jadx-mt/`, when generated) provides a second readability perspective but is equally read-only.

## Using JADX as reference

- Use `reference/jadx-aliased/` first — it has the most readable auto-aliased names.
- Fall back to `reference/jadx-raw/` when aliased output is misleading or incomplete.
- Cross-reference with `docs/reports/jadx-problems.txt` to know which methods/classes JADX failed to decompile correctly.
- For any class listed in `jadx-problems.txt`, read the raw smali directly rather than trusting the Java reconstruction.

## Handling JADX disagreements

- If JADX shows a method body that doesn't match smali register flow, the smali is correct.
- Do not "fix" smali to match a broken JADX reconstruction.
- Document the disagreement with a comment in the smali file: `# NOTE: JADX decompile incorrect here — see raw smali`.

## Renaming and de-obfuscation

- Rename a class, method, or field **only when you understand its behaviour** from smali analysis.
- Prefer short, accurate semantic names (e.g. `handleMediaCommand`) over guessed original names.
- Record human-reviewed renames in `mappings/manual-enigma/` using Enigma `.mapping` format.
- Do not hand-edit `mappings/jadx/*.jobf` — these are JADX-generated caches.
- One rename at a time; rebuild and verify before chaining renames.
- Do not bulk-rename based on JADX auto-aliases alone — aliases may be wrong.

## Preserved vendor and system boundaries

The following smali constructs must not be modified unless the task explicitly requires it:

- Any smali class that references `com.tw.service.xt.aidl.*` interfaces
- Any class that reads `persist.tw.ijk*` system properties via `android.os.SystemProperties`
- Any class that reads other `persist.tw.*` or `persist.media.*` properties (additional vendor property reads discovered in analysis)
- The `MusicService` broadcast receiver block handling `com.tw.music.action.*` intents
- `MusicWidgetProvider` widget update logic — the launcher reads widget `RemoteViews` directly
- Any `ComponentName("com.tw.eq", ...)` launch point
- Any intent surface touching `com.tw.radio.*`
- TWTHEME / `@style/AppTheme` references — do not remove or modify theme-dispatch code
- `sharedUserId` is declared in the manifest — do not add permissions requiring a different UID

## Media session / playback smali

When modifying playback or media session smali:

- Preserve existing `MediaSession` / `MediaSessionCompat` token management.
- Preserve `PlaybackState` and `MediaMetadata` publication paths — these feed system controls, the widget, and the TLink / CarPlay / Android Auto bridge on TS18.
- On Android 13, the system media notification is driven directly by `MediaSession` metadata — ensure publication is correct and consistent.
- Do not remove foreground service start/stop calls — Android 13 requires a foreground service with a media notification for active playback.
- Do not replace the underlying playback engine (TWMediaPlayer / IjkMediaPlayer) unless explicitly scoped to do so and vendor-property compatibility is proven.
- When improving media button handling in `MusicService` or the command dispatcher (e.g. `C0781k`), keep all existing action string checks intact and only extend, not replace, the dispatch logic — these strings drive steering-wheel control and TW hardware buttons.
- After any smali change to playback/session code, rebuild and note the expected behavioural change in the commit message or a code comment.
- Be aware that TS18 vendor firmware may surface `MediaSession` state in custom launcher panels — treat metadata and state publication as externally visible at all times.

## Build validation

After every meaningful smali change run the required static checks:

```
bash -n scripts/codex/setup_readability_env.sh
bash -n scripts/codex/maintain_readability_env.sh
bash -n scripts/build_source_shim.sh
python3 tools/readability/07_validate_readability_reports.py
python3 tools/smali/validate_smali_static.py
bash scripts/08_verify_vendor_tokens.sh
bash tools/readability/06_diff_size_guard.sh
```

Then run build validation as needed:

```
bash scripts/02_build_unsigned.sh
```

If the build fails, fix the smali error before committing. Do not commit broken smali.

Optionally regenerate JADX reference after a build:

```
bash scripts/03_jadx_export_raw.sh dist/com.tw.music-unsigned.apk
bash scripts/04_jadx_export_aliased.sh dist/com.tw.music-unsigned.apk
```

## Key entry-point files

- `app/apktool/smali/com/tw/music/MusicService.smali` — primary playback service
- `app/apktool/smali/com/tw/music/MusicActivity.smali` — main UI activity
- `app/apktool/smali/com/tw/music/MusicApplication.smali` — application class
- `app/apktool/smali/com/tw/music/view/MusicWidgetProvider.smali` — home screen widget
- `app/apktool/smali/com/tw/music/AudioPreview.smali` — file/intent preview activity
- Vendor AIDL stubs: `app/apktool/smali*/` classes matching `ITWCommandAidl`, `IMusicCallBack`, etc.
- TWMediaPlayer wrapper: `app/apktool/smali*/tv/danmaku/ijk/media/player/p069tw/`

## Dex split awareness

The app uses 4 dex files (`smali`, `smali_classes2`, `smali_classes3`, `smali_classes4`). When searching for a class, check all four directories. When adding a new class, place it in the same dex as related classes unless there is a specific reason to separate it.


Optional local build sanity check:

```
apktool b app/apktool -o .local/<task-name>-check.apk
```

Do not commit `.local` APK outputs.
