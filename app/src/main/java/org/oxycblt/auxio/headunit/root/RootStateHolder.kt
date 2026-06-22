package org.oxycblt.auxio.headunit.root

import java.io.BufferedReader
import java.io.InputStreamReader
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
        val finished = process.waitFor(2000, TimeUnit.MILLISECONDS)
        if (!finished) {
            process.destroy()
            state = State.TimedOut
        } else {
            val stdout = BufferedReader(InputStreamReader(process.inputStream)).readText()
            state = if (process.exitValue() == 0 && stdout.contains("uid=0")) State.Available else State.Denied
        }
        return state
    }

    override fun runRootCommandSync(command: String, timeoutMs: Long): List<String>? {
        if (state == State.Unknown || state == State.TimedOut) probeSync()
        if (state != State.Available) return null
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            if (!process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)) {
                process.destroy()
                null
            } else {
                if (process.exitValue() != 0) null
                else BufferedReader(InputStreamReader(process.inputStream)).readLines().filter { it.isNotBlank() }
            }
        } catch (e: Exception) { null }
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface RootEntryPoint {
    fun rootGate(): RootStateHolder
}
