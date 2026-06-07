/*
 * Copyright (c) 2021 Auxio Project
 * Auxio.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio

import android.app.Application
import android.os.Build
import androidx.core.content.pm.ShortcutManagerCompat
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import org.oxycblt.auxio.headunit.HeadUnitEntryPoints
import org.oxycblt.auxio.home.HomeSettings
import org.oxycblt.auxio.image.ImageSettings
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.util.CopyleftNoticeTree
import timber.log.Timber

internal object CrashReportStorage {
    fun ensureDirectory(directory: File): Boolean =
        if (directory.exists()) {
            directory.isDirectory && directory.canWrite()
        } else {
            directory.mkdirs() && directory.isDirectory && directory.canWrite()
        }
}

/**
 * A simple, rational music player for android.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@HiltAndroidApp
class Auxio : Application() {
    @Inject lateinit var imageSettings: ImageSettings
    @Inject lateinit var playbackSettings: PlaybackSettings
    @Inject lateinit var uiSettings: UISettings
    @Inject lateinit var homeSettings: HomeSettings

    override fun onCreate() {
        installCrashHandler()
        super.onCreate()
        @Suppress("KotlinConstantConditions")
        if (
            BuildConfig.APPLICATION_ID != "org.oxycblt.auxio" &&
                BuildConfig.APPLICATION_ID != "org.oxycblt.auxio.debug"
        ) {
            Timber.plant(CopyleftNoticeTree())
        } else if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Migrate any settings that may have changed in an app update.
        imageSettings.migrate()
        playbackSettings.migrate()
        uiSettings.migrate()
        homeSettings.migrate()
        ShortcutManagerCompat.setDynamicShortcuts(
            this,
            HeadUnitEntryPoints.createDynamicShortcuts(this),
        )

        // Register car floating controls visibility hooks for the Topway/TS18 variant.
        if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            try {
                val companionClass =
                    Class.forName(
                        "org.oxycblt.auxio.car.overlay.CarOverlayVisibilityHooks\$Companion"
                    )
                val hooksClass =
                    Class.forName("org.oxycblt.auxio.car.overlay.CarOverlayVisibilityHooks")
                val companion = hooksClass.getDeclaredField("Companion").get(null)
                val registerMethod = companionClass.getMethod("register", Application::class.java)
                registerMethod.invoke(companion, this)
            } catch (e: ReflectiveOperationException) {
                Timber.w(e, "Car overlay visibility hooks not available")
            }
        }
    }

    private fun installCrashHandler() {
        val previousHandler = Thread.getDefaultUncaughtExceptionHandler()
        if (previousHandler is CrashFileHandler) {
            return
        }
        Thread.setDefaultUncaughtExceptionHandler(CrashFileHandler(this, previousHandler))
    }

    private class CrashFileHandler(
        private val application: Application,
        private val previousHandler: Thread.UncaughtExceptionHandler?,
    ) : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(thread: Thread, throwable: Throwable) {
            try {
                writeCrashReport(thread, throwable)
            } catch (_: Throwable) {
                // Preserve the original crash path even if diagnostic export fails.
            } finally {
                previousHandler?.uncaughtException(thread, throwable)
                    ?: kotlin.system.exitProcess(10)
            }
        }

        private fun writeCrashReport(thread: Thread, throwable: Throwable) {
            val crashTime = Date()
            val timestamp = fileTimestamp(crashTime)
            val diagnosticsDir = crashReportDirectory() ?: return

            val reportFile = File(diagnosticsDir, "crash-$timestamp.txt")
            reportFile.writeText(buildReport(thread, throwable, crashTime))
            pruneOldReports(diagnosticsDir)
        }

        private fun crashReportDirectory(): File? {
            val externalDir = File(
                application.getExternalFilesDir(null) ?: application.filesDir,
                "crash-reports",
            )
            if (CrashReportStorage.ensureDirectory(externalDir)) {
                return externalDir
            }

            val fallbackDir = File(application.filesDir, "crash-reports")
            if (fallbackDir != externalDir && CrashReportStorage.ensureDirectory(fallbackDir)) {
                Timber.w(
                    "Using internal crash-report directory after external directory was unavailable: %s",
                    externalDir.absolutePath,
                )
                return fallbackDir
            }

            Timber.w("Unable to create crash-report directory: %s", fallbackDir.absolutePath)
            return null
        }

        private fun buildReport(thread: Thread, throwable: Throwable, crashTime: Date): String {
            val stackTrace =
                StringWriter().also { writer ->
                    PrintWriter(writer).use { printWriter ->
                        throwable.printStackTrace(printWriter)
                    }
                }

            return buildString {
                appendLine("Auxio crash report")
                appendLine("Generated: ${displayTimestamp(crashTime)}")
                appendLine()
                appendLine("App")
                appendLine("  applicationId: ${BuildConfig.APPLICATION_ID}")
                appendLine("  versionName: ${BuildConfig.VERSION_NAME}")
                appendLine("  versionCode: ${BuildConfig.VERSION_CODE}")
                appendLine("  debug: ${BuildConfig.DEBUG}")
                appendLine("  topwayTwMusicFlavor: ${BuildConfig.TOPWAY_TWMUSIC_FLAVOR}")
                appendLine("  topwayTwMediaFlavor: ${BuildConfig.TOPWAY_TWMEDIA_FLAVOR}")
                appendLine("  topwayCompatFlavor: ${BuildConfig.TOPWAY_COMPAT_FLAVOR}")
                appendLine()
                appendLine("Device")
                appendLine("  manufacturer: ${Build.MANUFACTURER}")
                appendLine("  brand: ${Build.BRAND}")
                appendLine("  model: ${Build.MODEL}")
                appendLine("  device: ${Build.DEVICE}")
                appendLine("  product: ${Build.PRODUCT}")
                appendLine("  hardware: ${Build.HARDWARE}")
                appendLine("  androidRelease: ${Build.VERSION.RELEASE}")
                appendLine("  sdkInt: ${Build.VERSION.SDK_INT}")
                appendLine("  buildId: ${Build.ID}")
                appendLine("  fingerprint: ${Build.FINGERPRINT}")
                appendLine()
                appendLine("Thread")
                appendLine("  name: ${thread.name}")
                appendLine("  id: ${thread.id}")
                appendLine("  state: ${thread.state}")
                appendLine()
                appendLine("Exception")
                appendLine(stackTrace.toString())
            }
        }

        private fun pruneOldReports(diagnosticsDir: File) {
            diagnosticsDir
                .listFiles { file ->
                    file.isFile && file.name.startsWith("crash-") && file.name.endsWith(".txt")
                }
                ?.sortedByDescending { it.lastModified() }
                ?.drop(MAX_CRASH_REPORTS)
                ?.forEach { it.delete() }
        }

        private companion object {
            const val MAX_CRASH_REPORTS = 10

            fun fileTimestamp(date: Date): String =
                SimpleDateFormat("yyyyMMdd-HHmmss-SSS", Locale.US).format(date)

            fun displayTimestamp(date: Date): String =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.US).format(date)
        }
    }
}
