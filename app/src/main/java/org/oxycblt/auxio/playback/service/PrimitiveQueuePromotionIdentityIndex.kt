/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueuePromotionIdentityIndex.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import java.net.URI
import java.util.Locale
import kotlin.math.abs

/** Builds fallback identity indexes that exclude every ambiguous identity. */
internal object PrimitiveQueuePromotionIdentityIndex {
    fun <T> uniqueBy(items: Iterable<T>, identity: (T) -> String): Map<String, T> {
        val unique = mutableMapOf<String, T>()
        val ambiguous = mutableSetOf<String>()
        for (item in items) {
            val key = identity(item)
            if (key in ambiguous) continue
            if (unique.containsKey(key)) {
                unique.remove(key)
                ambiguous += key
            } else {
                unique[key] = item
            }
        }
        return unique
    }

    fun normalizeUriIdentity(value: String?): String? {
        val trimmed = value?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        val parsed = runCatching { URI(trimmed) }.getOrNull() ?: return trimmed
        if (!parsed.scheme.equals("file", ignoreCase = true)) return trimmed
        val path = normalizePathIdentity(parsed.path) ?: return trimmed
        return "file://$path"
    }

    fun normalizePathIdentity(value: String?): String? {
        val trimmed = value?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        val slashNormalised = trimmed.replace('\\', '/').replace(Regex("/+"), "/")
        return if (slashNormalised.length > 1) slashNormalised.trimEnd('/') else slashNormalised
    }

    fun normalizeTextIdentity(value: String?): String? =
        value?.trim()?.takeIf { it.isNotEmpty() }?.lowercase(Locale.ROOT)

    fun durationMatches(firstMs: Long, secondMs: Long, toleranceMs: Long = 1_500L): Boolean =
        firstMs > 0L && secondMs > 0L && abs(firstMs - secondMs) <= toleranceMs
}
