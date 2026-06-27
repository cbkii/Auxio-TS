cat << 'PATCH' > patch.diff
--- app/src/main/java/org/oxycblt/auxio/headunit/ts18/RawFastResume.kt
+++ app/src/main/java/org/oxycblt/auxio/headunit/ts18/RawFastResume.kt
@@ -69,8 +69,17 @@
         val usableUri =
             when (scheme) {
                 "content" -> {
                     val contentCheck = validateContentUri(context, parsedUri)
-                    if (contentCheck != null) return contentCheck
-                    parsedUri
+                    if (contentCheck != null) {
+                        // Fallback to direct path if the snapshot contains one that is valid.
+                        val fallbackCheck = validateDirectPath(pathText)
+                        if (pathText != null && fallbackCheck == null) {
+                            resolvedPath = pathText
+                            Uri.fromFile(File(pathText))
+                        } else {
+                            return contentCheck
+                        }
+                    } else {
+                        parsedUri
+                    }
                 }
                 "file" -> {
PATCH
patch app/src/main/java/org/oxycblt/auxio/headunit/ts18/RawFastResume.kt < patch.diff
