/*
 * Copyright (c) 2026 Auxio Project
 * TagLibJNIAvailabilityTest.kt is part of Auxio.
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

import io.mockk.mockk
import java.io.FileInputStream
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.fs.File
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Regression coverage for [TagLibJNI] native-library load resilience.
 *
 * The host/JVM unit-test runtime has no usable `libtagJNI.so`, so this exercises exactly the
 * failure modes that matter on a device whose ABI is missing or mismatched in the APK: touching the
 * object must not throw [ExceptionInInitializerError], and [TagLibJNI.open] must degrade to
 * [MetadataResult.ProviderFailed] rather than throwing an [UnsatisfiedLinkError] (an [Error] that
 * would bypass the scan's `catch (e: Exception)` and crash the process).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class TagLibJNIAvailabilityTest {
    @Test
    fun tagLibJni_initializesWithoutThrowing() {
        // Merely touching the object must not throw ExceptionInInitializerError, regardless of
        // whether the native library is present in this environment.
        val available = TagLibJNI.isAvailable
        // Reference the value so it cannot be optimised away.
        assertEquals(available, TagLibJNI.isAvailable)
    }

    @Test
    fun tagLibJni_open_returnsProviderFailed_whenNativeUnavailable() {
        // Whether loadLibrary failed (isAvailable == false) or the symbol cannot bind at call
        // time (UnsatisfiedLinkError from openNative), open() must return ProviderFailed and never
        // propagate a Throwable to the extraction pipeline.
        val deviceFile = mockk<File>(relaxed = true)
        val fis = mockk<FileInputStream>(relaxed = true)
        assertEquals(MetadataResult.ProviderFailed, TagLibJNI.open(deviceFile, fis))
    }
}
