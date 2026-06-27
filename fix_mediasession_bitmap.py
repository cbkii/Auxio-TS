with open("app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt", "r") as f:
    content = f.read()

# Make sure we don't have duplicates or anything missing from `bitmapProvider.load` in MediaSessionHolder

# Actually, the logic was to publish text metadata immediately (which I already did earlier via `mediaSession.setMetadata(initialMetadata)` in `patch_mediasession.sh`).
# Let's check what `patch_mediasession.sh` actually did since it gave `malformed patch at line 17`.
