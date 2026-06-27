cat << 'PATCH' > patch.diff
--- app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt
+++ app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt
@@ -37,6 +37,9 @@
         private set

     init {
         if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) state = State.UnsupportedForVariant
     }

+    var isRootEnabledByUser: Boolean = true
+
     @Synchronized
     fun probeSync(): State {
         // Timeouts are intentionally retryable: TS18 su prompts can be transient, and a
@@ -75,6 +78,7 @@
     @Synchronized
     override fun runRootCommandSync(command: String, timeoutMs: Long): List<String>? {
         if (state == State.Unknown || state == State.TimedOut) probeSync()
+        if (!isRootEnabledByUser) return null
         if (state != State.Available) return null
         return try {
             val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
PATCH
patch app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt < patch.diff
