/*
 * Copyright (c) 2026 Auxio Project
 * ConfiguredSourceSpec.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import android.net.Uri
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.fs.CanonicalSourcePolicy

/** Immutable source identity parsed before attempting to open a provider or filesystem root. */
data class ConfiguredSourceSpec(
    val normalizedUri: Uri,
    val sourceKey: String,
    /**
     * Canonical identity of this exact root.
     *
     * [sourceKey] is volume-scoped, so several folders on one volume share it. Deduplication,
     * counting and comparison must use this narrower identity instead.
     */
    val canonicalKey: String,
    val mode: LocationMode,
    val displayPath: String,
    val accessState: AccessState,
    val origin: CanonicalSourcePolicy.Origin = CanonicalSourcePolicy.Origin.EXPLICIT,
    val traversalScope: CanonicalSourcePolicy.Scope? = null,
) {
    enum class AccessState {
        AVAILABLE,
        PERMISSION_REQUIRED,
        TEMPORARILY_UNAVAILABLE,
    }
}
