from pathlib import Path

exo_path = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt"
)
text = exo_path.read_text()
old = """                        val songsByUri = library.songs.associateBy { it.uri.toString() }
                        val songsByPath = library.songs.associateBy { it.path.toString() }
"""
new = """                        val songsByUri =
                            PrimitiveQueuePromotionIdentityIndex.uniqueBy(library.songs) {
                                it.uri.toString()
                            }
                        val songsByPath =
                            PrimitiveQueuePromotionIdentityIndex.uniqueBy(library.songs) {
                                it.path.toString()
                            }
"""
if text.count(old) != 1:
    raise SystemExit("Expected exactly one primitive promotion fallback index block")
exo_path.write_text(text.replace(old, new, 1))

helper_path = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/PrimitiveQueuePromotionIdentityIndex.kt"
)
helper_path.write_text(
    """/*
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
}
"""
)

test_path = Path(
    "app/src/test/java/org/oxycblt/auxio/playback/service/PrimitiveQueuePromotionIdentityIndexTest.kt"
)
test_path.write_text(
    """/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueuePromotionIdentityIndexTest.kt is part of Auxio.
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PrimitiveQueuePromotionIdentityIndexTest {
    @Test
    fun duplicateFallbackIdentityIsExcludedInsteadOfCollapsed() {
        val index =
            PrimitiveQueuePromotionIdentityIndex.uniqueBy(listOf(1, 2, 3)) { value ->
                if (value < 3) "duplicate" else "unique"
            }

        assertNull(index["duplicate"])
        assertEquals(3, index["unique"])
    }

    @Test
    fun identityRemainsAmbiguousAfterAdditionalDuplicates() {
        val index =
            PrimitiveQueuePromotionIdentityIndex.uniqueBy(listOf(1, 2, 3, 4)) { value ->
                if (value < 4) "duplicate" else "unique"
            }

        assertNull(index["duplicate"])
        assertEquals(mapOf("unique" to 4), index)
    }
}
"""
)
