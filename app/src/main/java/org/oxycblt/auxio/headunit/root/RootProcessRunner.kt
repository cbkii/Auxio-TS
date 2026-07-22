/*
 * Copyright (c) 2026 Auxio Project
 * RootProcessRunner.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.root

import android.os.Build
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

/** Bounded process result used by all root-assisted TS18 operations. */
sealed class RootProcessResult {
    data class Success(val stdout: String, val stderr: String, val exitCode: Int) :
        RootProcessResult()

    data class NonZeroExit(val stdout: String, val stderr: String, val exitCode: Int) :
        RootProcessResult()

    data object TimedOut : RootProcessResult()

    data object OutputLimitExceeded : RootProcessResult()

    data class ExecutionFailure(val reason: String) : RootProcessResult()
}

/**
 * Runs one root command while concurrently draining bounded stdout/stderr.
 *
 * This deliberately avoids [Process.waitFor] overloads added after API 24 and always tears down a
 * timed-out child. Output collectors continue draining after their capture limit so a noisy command
 * cannot deadlock the process on a full pipe.
 */
@Singleton
class RootProcessRunner @Inject constructor() {
    fun runRootCommand(
        command: String,
        timeoutMs: Long,
        maxOutputBytes: Int = DEFAULT_MAX_OUTPUT_BYTES,
    ): RootProcessResult = runProcess(arrayOf("su", "-c", command), timeoutMs, maxOutputBytes)

    internal fun runProcessForTest(
        command: Array<String>,
        timeoutMs: Long,
        maxOutputBytes: Int = DEFAULT_MAX_OUTPUT_BYTES,
    ): RootProcessResult = runProcess(command, timeoutMs, maxOutputBytes)

    private fun runProcess(
        command: Array<String>,
        timeoutMs: Long,
        maxOutputBytes: Int,
    ): RootProcessResult {
        if (timeoutMs <= 0L || maxOutputBytes <= 0) {
            return RootProcessResult.ExecutionFailure("invalid process limits")
        }

        val process =
            try {
                Runtime.getRuntime().exec(command)
            } catch (e: Exception) {
                return RootProcessResult.ExecutionFailure(e.javaClass.simpleName)
            }

        process.outputStream.closeQuietly()
        val stdout = BoundedStreamCollector(process.inputStream, maxOutputBytes)
        val stderr = BoundedStreamCollector(process.errorStream, maxOutputBytes)
        val stdoutThread = collectorThread("AuxioRootStdout", stdout)
        val stderrThread = collectorThread("AuxioRootStderr", stderr)
        stdoutThread.start()
        stderrThread.start()

        val completed =
            try {
                process.waitForCompat(timeoutMs)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                false
            }

        if (!completed) {
            process.terminateCompat()
            process.inputStream.closeQuietly()
            process.errorStream.closeQuietly()
            stdoutThread.joinQuietly(COLLECTOR_JOIN_TIMEOUT_MS)
            stderrThread.joinQuietly(COLLECTOR_JOIN_TIMEOUT_MS)
            return RootProcessResult.TimedOut
        }

        stdoutThread.joinQuietly(COLLECTOR_JOIN_TIMEOUT_MS)
        stderrThread.joinQuietly(COLLECTOR_JOIN_TIMEOUT_MS)
        process.inputStream.closeQuietly()
        process.errorStream.closeQuietly()

        if (stdout.exceeded || stderr.exceeded) {
            return RootProcessResult.OutputLimitExceeded
        }

        val exitCode =
            try {
                process.exitValue()
            } catch (e: IllegalThreadStateException) {
                process.terminateCompat()
                return RootProcessResult.ExecutionFailure("process did not exit")
            }
        val stdoutText = stdout.text()
        val stderrText = stderr.text()
        return if (exitCode == 0) {
            RootProcessResult.Success(stdoutText, stderrText, exitCode)
        } else {
            RootProcessResult.NonZeroExit(stdoutText, stderrText, exitCode)
        }
    }

    private fun collectorThread(name: String, collector: BoundedStreamCollector) =
        Thread(collector, name).apply { isDaemon = true }

    private fun Process.waitForCompat(timeoutMs: Long): Boolean {
        val deadlineNanos = System.nanoTime() + timeoutMs * NANOS_PER_MILLISECOND
        while (true) {
            try {
                exitValue()
                return true
            } catch (_: IllegalThreadStateException) {}

            val remainingNanos = deadlineNanos - System.nanoTime()
            if (remainingNanos <= 0L) return false
            Thread.sleep(
                minOf(
                    (remainingNanos / NANOS_PER_MILLISECOND).coerceAtLeast(1L),
                    PROCESS_POLL_INTERVAL_MS,
                )
            )
        }
    }

    private fun Process.terminateCompat() {
        destroy()
        try {
            if (
                !waitForCompat(TERMINATION_GRACE_MS) &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            ) {
                destroyForcibly()
                waitForCompat(TERMINATION_GRACE_MS)
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    private class BoundedStreamCollector(
        private val input: InputStream,
        private val maxBytes: Int,
    ) : Runnable {
        private val output = ByteArrayOutputStream(minOf(maxBytes, INITIAL_BUFFER_BYTES))

        @Volatile
        var exceeded: Boolean = false
            private set

        override fun run() {
            val buffer = ByteArray(STREAM_BUFFER_BYTES)
            try {
                while (true) {
                    val read = input.read(buffer)
                    if (read < 0) return
                    val remaining = maxBytes - output.size()
                    if (remaining > 0) output.write(buffer, 0, minOf(read, remaining))
                    if (read > remaining) exceeded = true
                }
            } catch (_: Exception) {
                // Stream closure is expected when a timed-out process is terminated.
            }
        }

        fun text(): String = output.toString(Charsets.UTF_8.name())
    }

    private fun Closeable.closeQuietly() {
        try {
            close()
        } catch (_: Exception) {}
    }

    private fun Thread.joinQuietly(timeoutMs: Long) {
        try {
            join(timeoutMs)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    private companion object {
        const val DEFAULT_MAX_OUTPUT_BYTES = 64 * 1024
        const val INITIAL_BUFFER_BYTES = 8 * 1024
        const val STREAM_BUFFER_BYTES = 4 * 1024
        const val COLLECTOR_JOIN_TIMEOUT_MS = 500L
        const val PROCESS_POLL_INTERVAL_MS = 25L
        const val TERMINATION_GRACE_MS = 250L
        const val NANOS_PER_MILLISECOND = 1_000_000L
    }
}
