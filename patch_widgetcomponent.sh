cat << 'PATCH' > patch.diff
--- app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt
+++ app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt
@@ -194,6 +194,22 @@
         topwayBridge.publishMetadata(metadataSnapshot, force = force)
         topwayBridge.publishProgress(elapsedMs, song.durationMs, force = force)

+        // Publish initial metadata to the widget immediately before artwork loads
+        val initialState = PlaybackState.fromSong(
+            context = context,
+            song = song,
+            cover = null,
+            isPlaying = isPlaying,
+            repeatMode = repeatMode,
+            isShuffled = isShuffled,
+            positionMs = elapsedMs,
+        )
+        // Only update if it's a forced update or playback is explicitly started to avoid unnecessary churn
+        if (force || isPlaying) {
+            widgetProvider.update(context, uiSettings, initialState)
+            updateTopwayWidget(initialState)
+        }
+
         L.d("Updating widget with new playback state")
         bitmapProvider.load(
             song,
PATCH
patch app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt < patch.diff
