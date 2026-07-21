## WorkPackage 2
1. Update `PlaybackViewModel` to expose a sealed interface or enum for the banner state.
  ```kotlin
  sealed interface BannerState {
      data class Rich(val song: Song) : BannerState
      data class Raw(val metadata: RawPlaybackMetadata) : BannerState
      object Restoring : BannerState
      object Idle : BannerState
      data class Unavailable(val reason: String) : BannerState
  }
  ```
  Expose `bannerState: StateFlow<BannerState>` instead of or alongside `song`.
2. Update `PlaybackBarFragment` to consume `bannerState` and update the UI accordingly. Ensure the fragment is always visible and manages its visibility states properly. Update bindings for text/cover and buttons depending on the banner state.
3. Update `MainFragment` so it stops manually hiding the bottom sheet when the song is null. The `PlaybackBarFragment` should remain perpetually visible. (Remove `tryHideAllSheets` call on song == null).

## WorkPackage 5
1. Update `PlaybackViewModel` and/or `PlaybackPanelFragment` to use a robust state machine for the Visualizer, ensuring we handle permission requests and maintain lifecycle correctly.
2. In `PlaybackPanelFragment`, start/stop the visualizer when `always` mode is chosen based on the new constraints, using the detailed state model (Disabled, AwaitingAudioSession, PermissionRequired, Live, Unavailable).

Pre-commit instructions: I will run `pre_commit_instructions` and follow the guidelines.
