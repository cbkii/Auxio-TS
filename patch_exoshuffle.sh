cat << 'PATCH' > patch.diff
--- app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt
+++ app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt
@@ -194,6 +194,10 @@
     override fun shuffled(shuffled: Boolean) {
+        val wasPlaying = player.playWhenReady || player.isPlaying
+        val currentPos = player.currentPosition
         player.setShuffleModeEnabled(shuffled)
         if (player.shuffleModeEnabled) {
             // Have to manually refresh the shuffle seed and anchor it to the new current songs
             player.setShuffleOrder(
                 BetterShuffleOrder(player.mediaItemCount, player.currentMediaItemIndex)
             )
         }
+        player.seekTo(player.currentMediaItemIndex, currentPos)
+        if (wasPlaying && !player.playWhenReady) {
+            player.play()
+        }
         playbackManager.ack(this, StateAck.QueueReordered)
         deferSave()
     }
PATCH
patch app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt < patch.diff
