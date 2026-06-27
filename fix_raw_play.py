with open("app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt", "r") as f:
    content = f.read()

# I want to ensure `playWhenReady` does not play briefly when reconciling if it was paused. The play() call already updates playWhenReady but we also need to ensure that it doesn't default to play. Let's look at `play()` in PlaybackManager.
