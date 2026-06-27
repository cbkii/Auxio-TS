cat << 'PATCH' > patch.diff
--- app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt
+++ app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt
@@ -359,8 +359,9 @@
             // We are normally supposed to use URIs for album art, but that removes some of the
             // nice things we can do like square cropping or high quality covers. Instead,
             // we load a full-size bitmap into the media session and take the performance hit.
             // On TS18/head-units, we bound this to 512px to reduce memory pressure.
             bitmapProvider.load(
                 song,
                 object : BitmapProvider.Target {
                     override fun onCompleted(bitmap: Bitmap?) {
                         if (requestToken != artworkRequestToken.get()) {
PATCH
patch app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt < patch.diff
