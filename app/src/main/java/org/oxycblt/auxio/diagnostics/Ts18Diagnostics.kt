package org.oxycblt.auxio.diagnostics

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import java.io.File
import java.util.Date
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.topway.TopwayMusicContract
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.util.NotificationBitmapSafety

/** Evidence labels used by TS18 Health Diagnostics reports. */
enum class DiagnosticEvidence(val label: String) {
    OBSERVED("Observed by Auxio"), USER("User confirmed"), INFERRED("Inferred from public Android state"),
    HYPOTHESIS("Hypothesis"), REQUIRES_EXTERNAL("Requires external TS18 validation"), UNAVAILABLE("Unavailable from normal app context")
}

enum class DiagnosticStatus(val label: String) {
    OBSERVED("Observed by Auxio"), USER_CONFIRMED("User confirmed"), INFERRED("Inferred from public Android state"),
    NOT_APPLICABLE("Not applicable"), NOT_VISIBLE("Not visible to this app"), PERMISSION_DENIED("Permission denied"),
    API_UNAVAILABLE("API unavailable"), QUERY_FAILED("Query failed"), NO_EVENT("No event observed"),
    HYPOTHESIS("Hypothesis"), REQUIRES_EXTERNAL("Requires external TS18 validation"), UNAVAILABLE("Unavailable from normal app context")
}

data class DiagnosticFinding(
    val title: String,
    val status: DiagnosticStatus,
    val evidence: DiagnosticEvidence,
    val primaryMethod: String,
    val fallbackMethod: String? = null,
    val value: String = "",
    val confidence: String = evidence.label,
    val limitation: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
)

data class DiagnosticEvent(
    val wallTime: Long = System.currentTimeMillis(),
    val elapsedMs: Long = SystemClock.elapsedRealtime(),
    val captureId: String?,
    val category: String,
    val event: String,
    val detail: String,
    val result: String,
    val evidence: DiagnosticEvidence = DiagnosticEvidence.OBSERVED,
)

object Ts18DiagnosticJournal {
    private const val MAX_EVENTS = 600
    private val events = CopyOnWriteArrayList<DiagnosticEvent>()
    @Volatile var activeCaptureId: String? = null
        private set
    @Volatile var captureExpiresAtElapsedMs: Long = 0L
        private set

    fun startCapture(durationMs: Long, reason: String): String? = synchronized(this) {
        if (activeCaptureId != null && SystemClock.elapsedRealtime() < captureExpiresAtElapsedMs) return null
        val id = UUID.randomUUID().toString().take(8)
        activeCaptureId = id
        captureExpiresAtElapsedMs = SystemClock.elapsedRealtime() + durationMs.coerceIn(1_000L, 15 * 60_000L)
        record("capture", "start", reason, "active")
        id
    }

    fun stopCapture(reason: String) = synchronized(this) {
        record("capture", "stop", reason, "stopped")
        activeCaptureId = null
        captureExpiresAtElapsedMs = 0L
    }

    fun expireIfNeeded(now: Long = SystemClock.elapsedRealtime()) {
        if (activeCaptureId != null && now >= captureExpiresAtElapsedMs) stopCapture("automatic expiry")
    }

    fun record(category: String, event: String, detail: String = "", result: String = "ok", evidence: DiagnosticEvidence = DiagnosticEvidence.OBSERVED) {
        events += DiagnosticEvent(captureId = activeCaptureId, category = category, event = event, detail = detail.take(500), result = result.take(200), evidence = evidence)
        while (events.size > MAX_EVENTS) events.removeAt(0)
    }
    fun snapshot(): List<DiagnosticEvent> { expireIfNeeded(); return events.toList() }
    fun clear() = events.clear()
}

object Ts18GuidedDoFunTest {
    const val COUNTDOWN_SECONDS = 120
    val questions = listOf(
        "1. Which app opened after you tapped the Music card?\n1 ==> Auxio-TS\n2 ==> Stock TW Music\n3 ==> Another app\n4 ==> No app opened\n5 ==> I am not sure\n6 ==> Other ==> enter text",
        "2. Did the Previous, Play/Pause or Next controls affect Auxio?\n1 ==> All controls affected Auxio\n2 ==> Some controls affected Auxio\n3 ==> None affected Auxio\n4 ==> I could not tell\n5 ==> Other ==> enter text",
        "3. Did the Music card title or artist change?\n1 ==> It showed Auxio’s current song\n2 ==> It showed stock music information\n3 ==> It changed, but I could not identify the source\n4 ==> It did not change\n5 ==> I could not tell\n6 ==> Other ==> enter text",
        "4. Did the progress display or seek control change?\n1 ==> Yes, and it matched Auxio\n2 ==> Yes, but it did not match Auxio\n3 ==> No\n4 ==> The card had no usable progress control\n5 ==> I could not tell\n6 ==> Other ==> enter text",
        "5. Did you see the optional metadata marker, if enabled?\n1 ==> Yes\n2 ==> No\n3 ==> I did not enable it\n4 ==> I could not tell\n5 ==> Other ==> enter text",
    )
    fun instructions(marker: String?): String = """
        1. Press Home to open DoFun.
        2. Look at the fixed Music card and remember whether its title, artist or progress changes${marker?.let { " (optional marker: $it)" } ?: ""}.
        3. Press Previous once.
        4. Press Play/Pause once.
        5. Press Next once.
        6. Move the Music-card progress control once, if the card has one.
        7. Tap the Music card last.
        8. Remember which app opens.
        9. Return directly to Auxio-TS Health Diagnostics once.
        No split screen is required. Auxio records app-owned events while in the background; all questions appear only after you return.
    """.trimIndent()
}

class Ts18DiagnosticsReporter(private val context: Context) {
    private val pm = context.packageManager
    fun buildAutomatedReport(): String {
        val f = mutableListOf<DiagnosticFinding>()
        f += deviceFindings(); f += packageFindings(); f += componentFindings(); f += launcherFindings(); f += notificationFindings(); f += widgetFindings(); f += storageFindings(); f += appStateFindings()
        return render("Automated TS18 Health Diagnostics", f, Ts18DiagnosticJournal.snapshot(), emptyList())
    }
    fun render(title: String, findings: List<DiagnosticFinding>, events: List<DiagnosticEvent>, answers: List<String>): String {
        val b = StringBuilder("$title\nTimestamp: ${Date()}\n\n== Summary ==\n")
        findings.forEach { b.append("- ${it.title}: ${it.status.label}. ${it.value}\n") }
        b.append("\n== Details ==\n")
        findings.forEach { b.append("${it.title}\n  status=${it.status.label}\n  evidence=${it.evidence.label}\n  primary=${it.primaryMethod}\n  fallback=${it.fallbackMethod ?: "none"}\n  value=${it.value}\n  confidence=${it.confidence}\n  limitation=${it.limitation ?: "none"}\n\n") }
        if (answers.isNotEmpty()) b.append("== Guided-test answers (User confirmed) ==\n${answers.joinToString("\n\n")}\n\n")
        b.append("== Event timeline ==\n")
        events.takeLast(250).forEach { b.append("${Date(it.wallTime)} +${it.elapsedMs} [${it.captureId ?: "no-capture"}] ${it.category}/${it.event}: ${it.detail} => ${it.result} (${it.evidence.label})\n") }
        b.append("\n== Limitations ==\nPrivate DoFun logs, SystemUI logs, kernel logs, complete task/window state, another package’s private service state, private Cardoor binder behaviour, full system notification state and implicit-broadcast consumption are unavailable from normal app context. Use external /system/bin/sh collection with explicit --user 0 only when privileged evidence is available.\n")
        return b.toString()
    }
    private fun deviceFindings() = listOf(finding("App identity", "package=${context.packageName} version=${packageInfo(context.packageName)?.versionName} code=${packageInfo(context.packageName)?.let { PackageInfoCompat.getLongVersionCode(it) }} variant=${variant()}"), finding("Android/device identity", "release=${Build.VERSION.RELEASE} sdk=${Build.VERSION.SDK_INT} device=${Build.DEVICE} product=${Build.PRODUCT} board=${Build.BOARD} hardware=${Build.HARDWARE} abi=${Build.SUPPORTED_ABIS.joinToString()} fingerprint=${Build.FINGERPRINT}"), finding("Display", context.resources.displayMetrics.let { "${it.widthPixels}x${it.heightPixels} density=${it.density} dpi=${it.densityDpi}" }))
    private fun packageFindings() = listOf("com.dofun.variety", "com.tw.music", "com.tw.media", context.packageName).distinct().map { packageFinding(it) }
    private fun componentFindings() = listOf(component("DoFun launcher activity", "com.dofun.variety", "com.dofun.overseasvariety.Launcher", "activity"), component("Stock MusicActivity", "com.tw.music", "com.tw.music.MusicActivity", "activity"), component("Stock MusicService", "com.tw.music", "com.tw.music.MusicService", "service"), component("Stock-name Topway provider", context.packageName, "com.tw.music.view.MusicWidgetProvider", "receiver"), component("Auxio playback service", context.packageName, "org.oxycblt.auxio.AuxioService", "service"), component("Topway bridge receiver", context.packageName, "org.oxycblt.auxio.headunit.topway.TopwayMusicBridgeReceiver", "receiver"), component("Auxio cover provider", context.packageName, "org.oxycblt.auxio.image.CoverProvider", "provider"))
    private fun launcherFindings(): List<DiagnosticFinding> { val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME); val resolved = runCatching { pm.resolveActivity(home, 0)?.activityInfo }.getOrNull(); val qs = runCatching { pm.queryIntentActivities(home, 0).joinToString { it.activityInfo.packageName + "/" + it.activityInfo.name } }.getOrElse { "query failed: ${it.javaClass.simpleName}" }; return listOf(finding("Resolved HOME", "${resolved?.packageName}/${resolved?.name}; DoFun default=${resolved?.packageName == "com.dofun.variety"}; candidates=$qs")) }
    private fun notificationFindings(): List<DiagnosticFinding> { val nm = NotificationManagerCompat.from(context); val channels = if (Build.VERSION.SDK_INT >= 26) runCatching { (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notificationChannels.joinToString { it.id + ":" + it.importance } }.getOrElse { "channel query failed: ${it.javaClass.simpleName}" } else "pre-O"; return listOf(finding("Notification state", "enabled=${runCatching { nm.areNotificationsEnabled() }.getOrElse { false }} channels=$channels foreground=${AuxioService.isForeground}"), finding("Latest notification artwork", "policy min=${NotificationBitmapSafety.MIN_ICON_SIZE_PX} max=${NotificationBitmapSafety.MAX_ICON_SIZE_PX}; last detailed artwork event appears in journal when posted"), finding("Notification listener visibility", runCatching { SettingsSecureReaders.enabledNotificationListeners(context) }.getOrElse { "query failed: ${it.javaClass.simpleName}" })) }
    private fun widgetFindings(): List<DiagnosticFinding> { val ids = runCatching { AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context.packageName, "com.tw.music.view.MusicWidgetProvider")).joinToString() }.getOrElse { "AppWidgetManager failed: ${it.javaClass.simpleName}" }; return listOf(finding("Topway widget IDs", "$ids. No normal AppWidget IDs does not prove the fixed DoFun Music card is absent because DoFun may use a custom fixed launcher card.")) }
    private fun storageFindings(): List<DiagnosticFinding> { val roots = TopwaySourcePolicy.discoverCandidateRoots().joinToString(); val media = if (Build.VERSION.SDK_INT >= 29) runCatching { MediaStore.getExternalVolumeNames(context).joinToString() }.getOrElse { "failed: ${it.javaClass.simpleName}" } else "external"; val mnt = File("/mnt/media_rw"); return listOf(finding("Storage roots", "dynamicRoots=$roots mntMediaRw exists=${mnt.exists()} readable=${mnt.canRead()}"), finding("MediaStore volumes", media), finding("Persisted SAF permissions", context.contentResolver.persistedUriPermissions.joinToString { "${it.uri} read=${it.isReadPermission} write=${it.isWritePermission}" })) }
    private fun appStateFindings() = listOf(finding("Playback/service state", "foreground=${AuxioService.isForeground}; full session/audio focus state is app-owned or requires external collection"), finding("Overlay state", "permission=${android.provider.Settings.canDrawOverlays(context)}; z-order/window state requires external collection"))
    private fun packageFinding(pkg: String): DiagnosticFinding { val ai = runCatching { pm.getApplicationInfo(pkg, 0) }.getOrNull(); val pi = packageInfo(pkg); return if (ai != null || pi != null) finding("Package $pkg", "visible=true enabled=${ai?.enabled} system=${ai?.flags?.and(android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0} privileged=${ai?.sourceDir?.contains("/priv-app/") == true} version=${pi?.versionName} source=${ai?.sourceDir}") else DiagnosticFinding("Package $pkg", DiagnosticStatus.NOT_VISIBLE, DiagnosticEvidence.UNAVAILABLE, "targeted getPackageInfo/getApplicationInfo", "explicit launch/component resolution", "not visible or absent; not treated as proven absent") }
    private fun component(label: String, pkg: String, cls: String, type: String): DiagnosticFinding {
        val cn = ComponentName(pkg, cls)
        val value = runCatching {
            when (type) {
                "activity" -> pm.getActivityInfo(cn, 0).let { "present enabled=${it.enabled} exported=${it.exported} process=${it.processName} permission=${it.permission} meta=${it.metaData != null}" }
                "service" -> pm.getServiceInfo(cn, 0).let { "present enabled=${it.enabled} exported=${it.exported} process=${it.processName} permission=${it.permission} meta=${it.metaData != null}" }
                "receiver" -> pm.getReceiverInfo(cn, 0).let { "present enabled=${it.enabled} exported=${it.exported} process=${it.processName} permission=${it.permission} meta=${it.metaData != null}" }
                else -> pm.getProviderInfo(cn, 0).let { "present enabled=${it.enabled} exported=${it.exported} process=${it.processName} readPermission=${it.readPermission} writePermission=${it.writePermission} meta=${it.metaData != null}" }
            }
        }.getOrElse {
            "direct $type query failed: ${it.javaClass.simpleName}; explicit intent resolves activity=${pm.resolveActivity(Intent().setComponent(cn), 0) != null} service=${pm.resolveService(Intent().setComponent(cn), 0) != null}"
        }
        return finding(label, value)
    }
    private fun finding(title: String, value: String) = DiagnosticFinding(title, DiagnosticStatus.OBSERVED, DiagnosticEvidence.INFERRED, "public Android API/app-owned state", null, value)
    private fun packageInfo(pkg: String): PackageInfo? = runCatching { pm.getPackageInfo(pkg, 0) }.getOrNull()
    private fun variant() = when { BuildConfig.TOPWAY_TWMUSIC_FLAVOR -> "topwayTwMusic"; BuildConfig.TOPWAY_TWMEDIA_FLAVOR -> "topwayTwMedia"; else -> "standard" }
}

object SettingsSecureReaders { fun enabledNotificationListeners(context: Context): String = android.provider.Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners") ?: "none visible" }

class Ts18DiagnosticsCaptureService : Service() {
    private val stopped = AtomicBoolean(false)
    private val receiver = object : BroadcastReceiver() { override fun onReceive(context: Context, intent: Intent) { Ts18DiagnosticJournal.record("system", intent.action ?: "broadcast", intent.dataString ?: "", "received") } }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val duration = intent?.getLongExtra(EXTRA_DURATION_MS, 2 * 60_000L) ?: 2 * 60_000L
        Ts18DiagnosticJournal.startCapture(duration, intent?.getStringExtra(EXTRA_REASON) ?: "timed capture")
        startForeground(8118, notification("TS18 diagnostics capture active"))
        registerReceivers(); Thread { while(!stopped.get() && Ts18DiagnosticJournal.activeCaptureId != null) { Thread.sleep(1000); Ts18DiagnosticJournal.expireIfNeeded() }; stopSelf() }.start()
        return START_NOT_STICKY
    }
    override fun onDestroy() { stopped.set(true); runCatching { unregisterReceiver(receiver) }; Ts18DiagnosticJournal.stopCapture("service destroyed"); super.onDestroy() }
    override fun onBind(intent: Intent?): IBinder? = null
    private fun registerReceivers() { val f = IntentFilter().apply { addAction(Intent.ACTION_MEDIA_MOUNTED); addAction(Intent.ACTION_MEDIA_UNMOUNTED); addAction(Intent.ACTION_MEDIA_EJECT); addAction(Intent.ACTION_PACKAGE_ADDED); addAction(Intent.ACTION_PACKAGE_CHANGED); addAction(Intent.ACTION_PACKAGE_REMOVED); addDataScheme("package") }; runCatching { registerReceiver(receiver, f) } }
    private fun notification(text: String): Notification { if (Build.VERSION.SDK_INT >= 26) (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(NotificationChannel(CHANNEL, "TS18 diagnostics", NotificationManager.IMPORTANCE_LOW)); return NotificationCompat.Builder(this, CHANNEL).setSmallIcon(R.drawable.ic_auxio_24).setContentTitle("Auxio-TS Health Diagnostics").setContentText(text).setOngoing(true).build() }
    companion object { const val EXTRA_DURATION_MS="duration_ms"; const val EXTRA_REASON="reason"; private const val CHANNEL="ts18_diagnostics"; fun start(context: Context, durationMs: Long, reason: String) = ContextCompat.startForegroundService(context, Intent(context, Ts18DiagnosticsCaptureService::class.java).putExtra(EXTRA_DURATION_MS,durationMs).putExtra(EXTRA_REASON,reason)) }
}
