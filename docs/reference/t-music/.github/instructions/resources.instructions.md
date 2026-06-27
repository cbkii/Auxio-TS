---
applyTo: "app/apktool/res/**"
---

# Resource and UI development rules

## Canonical resource surface

- `app/apktool/res/` is the only editable resource tree.
- Do not edit resources under `reference/`.
- After resource changes, rebuild: `bash scripts/02_build_unsigned.sh`.

## Target context: TS18 4GB Android 13 head unit

This app runs on a **TS18-class Android 13 head unit** (4 GB RAM, typically 64 GB storage), not a phone. The working screen baseline is **1280 × 720 landscape**. Always design for:

- **1280 × 720 landscape baseline** — verify all layouts at this resolution; do not add portrait-only or portrait-fallback qualifiers.
- **Landscape-fixed layout** — the screen is wide and short; the physical unit is mounted in a dashboard.
- **Large touch targets** — minimum 48 dp, ideally 56–64 dp for primary controls. Drivers interact with touch targets while in motion.
- **High contrast, low visual noise** — reduce distraction; avoid small text, cluttered layouts, or animations that draw attention away from the road.
- **4 GB RAM class** — the device simultaneously runs the vendor firmware, TW launcher, TLink, navigation, and other TW apps. Avoid gratuitously heavy UI patterns, large in-memory bitmaps, or excessive layout nesting.
- **No phone-specific UI patterns** — no bottom navigation bars that assume portrait phone ergonomics, no gesture-nav assumptions, no lock-screen media controls-first thinking.

See `docs/target-device-ts18-android13.md` for the full device profile.

## TW theme / TWTHEME compatibility — must not break

The TS18 firmware includes a **TWTHEME** theme-switching system. This app participates via `@style/AppTheme` and theme-referenced resources. The following resource boundaries are vendor-facing and must be preserved:

- `@style/AppTheme` and any style that `twtheme` / TWTHEME or the system launcher may reference
- Any resource name or ID referenced by `MusicWidgetProvider` (widget layouts, drawables, string IDs)
- Launcher icon resource: `@drawable/ic_launcher`
- Any colour, dimen, or style resource that carries the `tw_` or `music_` prefix and could be consumed by an external vendor component
- String resource IDs used in widget `RemoteViews` — changing an ID's name breaks the widget silently

**Do not delete or rename existing resources that are referenced by vendor or system components without confirming the reference does not exist outside this app.**

## UI improvement goals

Improvements to the following areas are in scope and encouraged:

- Player screen layout (`res/layout/music_act_player.xml`) — larger controls, better metadata display
- List screen layout (`res/layout/music_act_list.xml`) — clearer track/album rows, better scroll targets
- Split-screen layout (`res/layout/layout_music_split_screen.xml`) — head-unit dual-pane appropriateness
- Settings layout (`res/layout/layout_settings.xml`) — readable, touch-friendly form controls
- Main activity layout (`res/layout/music_activity.xml`) — overall hierarchy
- Presentation layout (`res/layout/layout_presentation.xml`) — external display / output surface
- Icons and drawables — refresh for clarity and resolution; preserve existing resource names
- Notification / media control visual appearance — consistent artwork, correct action icons

## Asset redesign rules

- **Keep existing resource names** — replace file content, not the name, unless a rename is explicitly scoped.
- When refreshing drawables, provide a vector (`res/drawable/`) or a density-appropriate PNG set (`res/drawable-*/`) that matches the original density buckets present in the tree.
- Do not introduce dependency on Google Play Services, Material Components library versions not already in the APK, or any external asset CDN.
- Prefer `VectorDrawable` for new icons where the SDK level permits.

## String resources

- `res/values/strings.xml` — all user-visible strings. Do not hardcode strings in layouts.
- Do not remove existing string keys — other components may reference them by name.
- When adding new strings for new UI features, use the naming convention `music_<feature>_<purpose>` (e.g. `music_player_no_track`).
- Internationalisation is out of scope for TS18 targets, but do not structurally break it.

## Low-distraction UX principles

- State changes (play/pause, track change) should be immediately and clearly visible without fine-grained attention.
- Animations must be subtle — no full-screen transitions, no looping animations on idle screens.
- Primary playback controls (prev, play/pause, next) must be reachable in a single touch, without scrolling or nested navigation.
- Album art should reinforce context, not dominate at the expense of control accessibility.
- Error and loading states must be clearly distinguished from playing state with visible iconography, not just colour.

## Theme attribute usage

- Use theme attributes (`?attr/colorPrimary`, `?attr/textColorPrimary`, etc.) rather than hardcoded colours where possible, so the TW theme layer (TWTHEME) remains in control of the palette.
- Do not add hardcoded colour literals to layouts if an existing theme attribute covers the same intent.

## TLink / CarPlay / Android Auto coexistence

On TS18, wireless CarPlay and Android Auto are bridged via **TLink**. When TLink is active:

- The TW launcher may suspend or background the music app.
- Do not assume exclusive media session ownership — the system or TLink may interact with `MediaSession` state.
- Notification `MediaStyle` actions must remain correct and externally visible; TLink and TS18 vendor panels may read session state from notifications.
- Resource changes to notification icons or action drawables may affect how media controls appear in the TLink / CarPlay mirror surface.

Do not add resources or layout patterns that assume the app is always in the foreground or always owns the audio focus.
