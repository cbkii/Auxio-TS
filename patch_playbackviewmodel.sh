cat << 'PATCH' > patch.diff
--- app/src/main/java/org/oxycblt/auxio/playback/PlaybackViewModel.kt
+++ app/src/main/java/org/oxycblt/auxio/playback/PlaybackViewModel.kt
@@ -482,8 +482,10 @@
                 parent is Playlist -> commandFactory.songFromPlaylist(currentSong, parent, shuffle)
                 else -> commandFactory.songFromAll(currentSong, shuffle)
             } ?: commandFactory.all(shuffle)

+        val wasPlaying = playbackManager.progression.isPlaying
         playImpl(command, shuffleScope)
         playbackManager.seekTo(currentPositionMs)
+        playbackManager.playing(wasPlaying)
     }

     private fun applyGenreShuffle() {
@@ -517,8 +519,10 @@
                     return@launch
                 }
                 val positionMs = playbackManager.progression.calculateElapsedPositionMs()
+                val wasPlaying = playbackManager.progression.isPlaying
                 playImpl(commandFactory.songs(selection.queue, ShuffleMode.ON), ShuffleScope.GENRE)
                 playbackManager.seekTo(positionMs)
+                playbackManager.playing(wasPlaying)
             }
     }

PATCH
patch app/src/main/java/org/oxycblt/auxio/playback/PlaybackViewModel.kt < patch.diff
