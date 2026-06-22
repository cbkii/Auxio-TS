package org.oxycblt.musikr.fs

interface RootGate {
    fun runRootCommandSync(command: String, timeoutMs: Long = 5000): List<String>?
}
