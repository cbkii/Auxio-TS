package org.oxycblt.auxio.headunit.root

import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.musikr.fs.RootGate
import timber.log.Timber as L

@Singleton
class RootStateHolder @Inject constructor() : RootGate {
    enum class State { Unknown, Available, Unavailable, Denied, TimedOut, UnsupportedForVariant }
    @Volatile var state: State = State.Unknown
        private set

    init { if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) state = State.UnsupportedForVariant }

    @Synchronized
    fun probeSync(): State {
        if (state != State.Unknown) return state
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
            return state
        }
        val process = try {
            Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
        } catch (e: Exception) {
            state = State.Unavailable
            return state
        }
        try {
            val finished = process.waitFor(2000, TimeUnit.MILLISECONDS)
            if (!finished) {
                process.destroy()
                state = State.TimedOut
            } else {
                val stdout = process.inputStream.bufferedReader().use { it.readText() }
                state = if (process.exitValue() == 0 && stdout.contains("uid=0")) State.Available else State.Denied
            }
        } finally {
            process.inputStream.closeQuietly()
            process.errorStream.closeQuietly()
            process.outputStream.closeQuietly()
        }
        return state
    }

    override fun runRootCommandSync(command: String, timeoutMs: Long): List<String>? {
        if (state == State.Unknown || state == State.TimedOut) probeSync()
        if (state != State.Available) return null
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            try {
                if (!process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)) {
                    process.destroy()
                    null
                } else {
                    if (process.exitValue() != 0) null
                    else process.inputStream.bufferedReader().use { reader ->
                        reader.readLines().filter { it.isNotBlank() }
                    }
                }
            } finally {
                process.inputStream.closeQuietly()
                process.errorStream.closeQuietly()
                process.outputStream.closeQuietly()
            }
        } catch (e: Exception) { null }
    }

    private fun java.io.Closeable.closeQuietly() {
        try {
            close()
        } catch (_: Exception) {}
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface RootEntryPoint {
    fun rootGate(): RootStateHolder
}
