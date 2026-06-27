cat << 'PATCH' > patch.diff
--- app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt
+++ app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt
@@ -948,7 +948,7 @@
                 val wasPlaying = player.playWhenReady || player.isPlaying
                 val positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L)
                 rawFastResumeItem = null
                 pendingLibraryRestoreAfterRawFailure = null
                 playbackManager.play(command)
                 playbackManager.seekTo(positionMs.coerceAtMost(song.durationMs.coerceAtLeast(0L)))
-                playbackManager.playing(wasPlaying)
+                if (wasPlaying) {
+                    playbackManager.playing(true)
+                }
                 Ts18FirstAudioLatency.mark("reconciliation_end_matched")
             }
         }
PATCH
patch app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt < patch.diff
