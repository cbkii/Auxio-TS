cat << 'PATCH' > patch.diff
--- musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt
+++ musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt
@@ -107,6 +107,29 @@
             "wav",
         )

+    private val rejectedAudioExtensions =
+        setOf(
+            "jpg",
+            "jpeg",
+            "png",
+            "webp",
+            "gif",
+            "svg",
+            "pdf",
+            "apk",
+            "lrc",
+            "cue",
+            "log",
+            "json",
+            "xml",
+            "bin",
+            "zip",
+            "gz",
+            "rar",
+            "md",
+            "sh",
+            "py",
+            "txt",
+        )
+
     fun isPotentialMusicFile(file: File): Boolean {
+        if (file.size != null && file.size > 888 * 1024 * 1024) return false
         val name = file.path.name ?: file.uri.lastPathSegment?.substringAfterLast('/')
         return isPotentialMusicFileNameMime(name, file.mimeType)
     }

     fun isPotentialMusicFileNameMime(name: String?, mimeType: String?): Boolean {
         val normalisedMimeType = mimeType?.lowercase(Locale.US).orEmpty()
+
+        val extension =
+            name
+                ?.substringAfterLast('.', missingDelimiterValue = "")
+                ?.lowercase(Locale.US)
+                .orEmpty()
+
+        if (extension in rejectedAudioExtensions) return false
+        if (extension.isEmpty() && !normalisedMimeType.startsWith("audio/")) return false
+
         if (normalisedMimeType == M3U.MIME_TYPE) return false
         if (normalisedMimeType.startsWith("audio/")) return true
         if (normalisedMimeType == "application/ogg" || normalisedMimeType == "application/x-ogg") {
             return true
         }
         if (normalisedMimeType != "application/octet-stream" && normalisedMimeType.isNotEmpty()) {
             return false
         }

-        val extension =
-            name
-                ?.substringAfterLast('.', missingDelimiterValue = "")
-                ?.lowercase(Locale.US)
-                .orEmpty()
         return extension in supportedAudioExtensions
     }
 }
PATCH
patch musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt < patch.diff
