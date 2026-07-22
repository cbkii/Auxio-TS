/*
 * Copyright (c) 2026 Auxio Project
 * RootProcessRunnerTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.headunit.root

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class RootProcessRunnerTest {
    private val runner = RootProcessRunner()

    @Test
    fun `captures bounded stdout and stderr concurrently`() {
        val result =
            runner.runProcessForTest(
                arrayOf("sh", "-c", "printf stdout; printf stderr >&2"),
                timeoutMs = 2_000,
            )

        assertTrue(result is RootProcessResult.Success)
        result as RootProcessResult.Success
        assertEquals("stdout", result.stdout)
        assertEquals("stderr", result.stderr)
        assertEquals(0, result.exitCode)
    }

    @Test
    fun `reports non-zero exit without losing output`() {
        val result =
            runner.runProcessForTest(
                arrayOf("sh", "-c", "printf denied; exit 7"),
                timeoutMs = 2_000,
            )

        assertTrue(result is RootProcessResult.NonZeroExit)
        result as RootProcessResult.NonZeroExit
        assertEquals("denied", result.stdout)
        assertEquals(7, result.exitCode)
    }

    @Test
    fun `terminates a timed-out process`() {
        val result =
            runner.runProcessForTest(
                arrayOf("sh", "-c", "sleep 5"),
                timeoutMs = 50,
            )

        assertEquals(RootProcessResult.TimedOut, result)
    }

    @Test
    fun `rejects output beyond the configured capture limit`() {
        val result =
            runner.runProcessForTest(
                arrayOf("sh", "-c", "yes x | head -c 4096"),
                timeoutMs = 2_000,
                maxOutputBytes = 128,
            )

        assertEquals(RootProcessResult.OutputLimitExceeded, result)
    }
}
