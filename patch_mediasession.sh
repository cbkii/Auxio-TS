cat << 'PATCH' > patch.diff
--- app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt
+++ app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt
@@ -345,6 +345,13 @@
                     .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, "")
                     .build()

+            // Publish initial metadata immediately so widget updates without waiting for cover loading
+            mediaSession.setMetadata(initialMetadata)
+            _notification.updateMetadata(initialMetadata)
+            broadcastLegacyMetadataChanged(
+                title = metadataSnapshot.displayTitle,
+                artist = metadataSnapshot.artist,
+                album = metadataSnapshot.albumTitle,
+                durationMs = metadataSnapshot.durationMs,
+            )
+
             mediaSession.setMetadata(initialMetadata)
             _notification.updateMetadata(initialMetadata)
             broadcastLegacyMetadataChanged(
PATCH
patch app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt < patch.diff
