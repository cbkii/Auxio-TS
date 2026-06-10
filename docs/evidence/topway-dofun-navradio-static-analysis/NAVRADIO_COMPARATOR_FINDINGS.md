# NavRadio+ comparator findings

NavRadio+ original is the primary working comparator, but it is not the stock Topway music contract. It is version 4.00 with minSdk 32 and targetSdk 35, so it is not directly deployable as an Android 10/SDK29 TS18 baseline.

## Relevant observations

### Real Media3 service

NavRadio+ declares `com.navimods.radio.RadioService` with action `androidx.media3.session.MediaSessionService` and the Java class extends `MediaSessionService`.

The service:

- returns `super.onBind(intent)` only for the Media3 service action;
- creates an internal player/session object during service creation;
- stores it as `this.mediaSession`;
- returns it from `onGetSession()`.

This is real Media3 service behaviour. It is not equivalent to adding a manifest action to an existing non-Media3 service.

### Simple widget and overlay controls

NavRadio+ widget providers listen for app-specific info broadcasts and use simple PendingIntents for previous/next/open. Its floating overlay sends simple previous/next broadcasts and stops safely when overlay permission is missing.

This supports the design principle that launcher-facing controls should be simple and robust.

### Changelog signal

The changelog includes several relevant entries:

- changed main service into a `MediaSessionService`;
- new Media3 MediaSession to make NavRadio work as a media player;
- removed MediaBrowser service due to too many problems;
- added a mediabrowser service so NavRadio was recognised as player;
- partially fixed DUDU launcher widget button functions;
- fixed settings backup/restore on DUDU7 using Download folder;
- fixed Android file-picker lifecycle/storage permission issues.

## What to reuse conceptually

- A real Media3 `MediaSessionService` can improve compatibility with launchers/controllers that prefer modern media sessions.
- Simple external controls are safer than a rich notification/action surface for crude launcher parsers.
- Overlay controls are a fallback UX, not the primary DoFun music-widget fix.
- File picker flows should account for lifecycle and API-level storage differences.

## What not to reuse

Do not copy or depend on NavRadio radio-specific code, hardware integration, FYT/DUDU/private APIs, station/RDS logic, proprietary assets, license checks, or cloud support code.

Do not implement a fake Media3 service that only adds the `androidx.media3.session.MediaSessionService` manifest action.

See excerpts:

- `excerpts/media-session/navradio_media3_session_creation.md`
- `excerpts/media-session/navradio_on_get_session.md`
- `excerpts/widgets/navradio_widget_pendingintents.md`
- `excerpts/navradio/navradio_float_widget_overlay.md`
- `excerpts/navradio/navradio_changelog_media_widget_lines.md`
