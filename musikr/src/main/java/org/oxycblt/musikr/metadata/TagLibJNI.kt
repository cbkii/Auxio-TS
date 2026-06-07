/*
 * Copyright (c) 2024 Auxio Project
 * TagLibJNI.kt is part of Auxio.
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

package org.oxycblt.musikr.metadata

import android.util.Log
import java.io.FileInputStream
import java.util.concurrent.atomic.AtomicBoolean
import org.oxycblt.musikr.fs.File

internal object TagLibJNI {
    private val nativeCallFailureLogged = AtomicBoolean(false)

    /**
     * Whether the native `tagJNI` library loaded successfully.
     *
     * Loading is attempted exactly once, when this object is first touched. If it fails (for
     * example because a release APK is missing the `.so` for the device's ABI, or the device's ABI
     * was never built), the failure is recorded here instead of being thrown. Previously the raw
     * [UnsatisfiedLinkError] (wrapped in an [ExceptionInInitializerError]) propagated out of the
     * extraction pipeline; because it is an [Error] rather than an [Exception] it bypassed the
     * scan's `catch (e: Exception)` and crashed the whole process. Recording availability lets
     * [open] degrade each file to [MetadataResult.ProviderFailed] so the scan finishes with a safe
     * empty/usable library and a single diagnostic line, rather than crashing.
     */
    val isAvailable: Boolean =
        try {
            System.loadLibrary("tagJNI")
            true
        } catch (e: UnsatisfiedLinkError) {
            Log.e(
                "TagLibJNI",
                "Native tagJNI library failed to load; metadata extraction is unavailable. " +
                    "This usually indicates a missing or mismatched native library (ABI) in the " +
                    "installed APK.",
                e,
            )
            false
        }

    /**
     * Open a file and extract a tag.
     *
     * Note: This method is blocking and should be handled as such if calling from a coroutine.
     *
     * @return the parsed [MetadataResult], or [MetadataResult.ProviderFailed] when the native
     *   library is unavailable (see [isAvailable]).
     */
    fun open(deviceFile: File, fis: FileInputStream): MetadataResult {
        if (!isAvailable) {
            return MetadataResult.ProviderFailed
        }
        val inputStream = NativeInputStream(deviceFile, fis)
        return try {
            openNative(inputStream)
        } catch (e: UnsatisfiedLinkError) {
            // The library loaded but the native symbol could not be bound (e.g. a partial or
            // mismatched .so). Degrade this file to a provider failure instead of throwing an
            // Error that would escape the scan and crash the process. Log once to avoid spamming
            // the log with one line per file in a large library.
            if (nativeCallFailureLogged.compareAndSet(false, true)) {
                Log.e("TagLibJNI", "Native metadata extraction failed to bind; skipping files", e)
            }
            MetadataResult.ProviderFailed
        } finally {
            inputStream.close()
        }
    }

    private external fun openNative(inputStream: NativeInputStream): MetadataResult
}
