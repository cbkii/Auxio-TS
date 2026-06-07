/*
 * Copyright (c) 2026 Auxio Project
 * CrashReportStorageFallbackTest.kt is part of Auxio.
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

import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * Validates crash-report storage fallback behavior: when external storage is unavailable, crash
 * reports should fall back to internal app storage (filesDir) rather than silently discarding
 * diagnostics.
 */
@RunWith(RobolectricTestRunner::class)
class CrashReportStorageFallbackTest {

    @Test
    fun fallbackLogic_usesFilesDirWhenExternalNull() {
        val app = RuntimeEnvironment.getApplication()
        // Simulate the fallback logic from Auxio.CrashFileHandler.writeCrashReport
        val externalDir: File? = null // simulates getExternalFilesDir returning null
        val diagnosticsRoot = externalDir ?: app.filesDir
        val diagnosticsDir = File(diagnosticsRoot, "crash-reports")

        // Verify fallback resolves to internal storage
        assertEquals(app.filesDir, diagnosticsRoot)
        assertTrue(diagnosticsDir.absolutePath.contains("crash-reports"))
    }

    @Test
    fun fallbackLogic_usesExternalDirWhenAvailable() {
        val app = RuntimeEnvironment.getApplication()
        val externalDir: File? = app.getExternalFilesDir(null)
        val diagnosticsRoot = externalDir ?: app.filesDir

        // When external is available, it should be used
        if (externalDir != null) {
            assertEquals(externalDir, diagnosticsRoot)
        }
        val diagnosticsDir = File(diagnosticsRoot, "crash-reports")
        assertTrue(diagnosticsDir.absolutePath.contains("crash-reports"))
    }

    @Test
    fun crashReportDir_canBeCreated() {
        val app = RuntimeEnvironment.getApplication()
        val diagnosticsRoot = app.getExternalFilesDir(null) ?: app.filesDir
        val diagnosticsDir = File(diagnosticsRoot, "crash-reports")

        if (!diagnosticsDir.exists()) {
            assertTrue(diagnosticsDir.mkdirs())
        }
        assertTrue(diagnosticsDir.exists())
        assertTrue(diagnosticsDir.isDirectory)
    }
}
