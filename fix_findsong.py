with open("app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt", "r") as f:
    content = f.read()

find_song_old = """    private fun findSongForRawFastResume(raw: RawFastResumeItem, library: Library): Song? {
        library.songs
            .firstOrNull { it.uri.toString() == raw.uriString }
            ?.let {
                return it
            }
        val rawPath = raw.path?.takeIf { it.isNotBlank() }
        if (rawPath != null) {
            val appContext = context.applicationContext
            library.songs
                .firstOrNull { song ->
                    try {
                        song.path.resolve(appContext) == rawPath
                    } catch (e: Exception) {
                        false
                    }
                }
                ?.let {
                    return it
                }
        }
        val rawTitle = raw.title?.trim()?.lowercase()?.takeIf { it.isNotEmpty() }
        if (rawTitle != null && raw.durationMs > 0L) {
            val appContext = context.applicationContext
            library.songs
                .firstOrNull { song ->
                    val title =
                        try {
                            song.name.resolve(appContext).trim().lowercase()
                        } catch (e: Exception) {
                            ""
                        }
                    title == rawTitle && Math.abs(song.durationMs - raw.durationMs) < 1000L
                }
                ?.let {
                    return it
                }
        }
        return null
    }"""

find_song_new = """    private fun findSongForRawFastResume(raw: RawFastResumeItem, library: Library): Song? {
        library.songs
            .firstOrNull { it.uri.toString() == raw.uriString }
            ?.let {
                return it
            }
        val rawPath = raw.path?.takeIf { it.isNotBlank() }
        if (rawPath != null) {
            val appContext = context.applicationContext
            library.songs
                .firstOrNull { song ->
                    try {
                        song.path.resolve(appContext) == rawPath
                    } catch (e: Exception) {
                        false
                    }
                }
                ?.let {
                    return it
                }
        }
        val rawTitle = raw.title?.trim()?.lowercase()?.takeIf { it.isNotEmpty() }
        if (rawTitle != null && raw.durationMs > 0L) {
            val appContext = context.applicationContext
            library.songs
                .firstOrNull { song ->
                    val title =
                        try {
                            song.name.resolve(appContext).trim().lowercase()
                        } catch (e: Exception) {
                            ""
                        }
                    title == rawTitle && Math.abs(song.durationMs - raw.durationMs) < 1000L
                }
                ?.let {
                    return it
                }
        }
        return null
    }"""

# Actually, the logic there is fine. The requirement was "If a persisted content:// URI is valid and audio-like, use it. If the content:// URI fails but the snapshot contains a safe readable direct path, fall back to that path... Hard-invalid snapshots should be cleared or quarantined... Raw errors must fail closed and must not fall into unsafe next() behaviour... Reconciliation must preserve current item, position, play/pause state, and metadata... Reconciliation from raw item to real Song must not briefly play audio if raw playback was paused."

# I've done:
# - fallback to direct path (done in `RawFastResume.kt` in `validateContentUri` branch)
# - raw errors fail closed without `next()` (done previously, already exists in `onPlayerError`)
# - reconciliation preserves play/pause state without briefly playing if paused (done in `ExoPlaybackStateHolder.kt` by updating the `playbackManager.playing(wasPlaying)` to avoid immediate play if `!wasPlaying`)

# Let me verify what I did in `onPlayerError`
