/*
 * Copyright (c) 2026 Auxio Project
 * TopwayBridgeCommandLedger.kt is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.ts18bridge

/** Bounded accepted-command ledger for the cross-process Track-C command protocol. */
internal class TopwayBridgeCommandLedger(
    private val maxEntries: Int = DEFAULT_MAX_ENTRIES,
    private val ttlMs: Long = DEFAULT_TTL_MS,
) {
    enum class Reservation {
        RESERVED,
        DUPLICATE_ACCEPTED,
        BUSY,
    }

    private data class Key(val generation: Long, val commandId: Long)

    private data class Entry(val accepted: Boolean, val recordedAtMs: Long)

    private val entries = LinkedHashMap<Key, Entry>()

    @Synchronized
    fun reserve(generation: Long, commandId: Long, nowMs: Long): Reservation {
        prune(nowMs)
        val key = Key(generation, commandId)
        val existing = entries[key]
        if (existing != null) {
            return if (existing.accepted) Reservation.DUPLICATE_ACCEPTED else Reservation.BUSY
        }
        if (entries.size >= maxEntries) return Reservation.BUSY
        entries[key] = Entry(accepted = false, recordedAtMs = nowMs)
        return Reservation.RESERVED
    }

    @Synchronized
    fun markAccepted(generation: Long, commandId: Long, nowMs: Long) {
        val key = Key(generation, commandId)
        if (entries.containsKey(key)) {
            entries[key] = Entry(accepted = true, recordedAtMs = nowMs)
        }
    }

    @Synchronized
    fun release(generation: Long, commandId: Long) {
        entries.remove(Key(generation, commandId))
    }

    @Synchronized
    fun size(nowMs: Long): Int {
        prune(nowMs)
        return entries.size
    }

    private fun prune(nowMs: Long) {
        val iterator = entries.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next().value
            if (nowMs < entry.recordedAtMs || nowMs - entry.recordedAtMs > ttlMs) {
                iterator.remove()
            }
        }
    }

    private companion object {
        const val DEFAULT_MAX_ENTRIES = 64
        const val DEFAULT_TTL_MS = 2_000L
    }
}
