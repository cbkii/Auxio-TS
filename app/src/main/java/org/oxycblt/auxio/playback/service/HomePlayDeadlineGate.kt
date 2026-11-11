package org.oxycblt.auxio.playback.service

/** One-use admission at decoder readiness. The main-thread playback owner serialises this gate. */
internal class HomePlayDeadlineGate(private val elapsedMs: () -> Long) {
    private var deadline: Long? = null

    fun arm(until: Long) {
        deadline = until.takeIf { it > elapsedMs() }
    }

    fun cancel() {
        deadline = null
    }

    fun expire(until: Long) {
        if (deadline == until && elapsedMs() >= until) deadline = null
    }

    fun consumeIfReady(ready: Boolean): Boolean {
        val until = deadline ?: return false
        if (elapsedMs() >= until) {
            cancel()
            return false
        }
        if (!ready) return false
        cancel()
        return true
    }
}
