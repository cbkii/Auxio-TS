/*
 * Copyright (c) 2026 Auxio Project
 * PipelinePolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.pipeline

/** Playback-first indexing resource policy shared by Musikr pipeline stages. */
internal object PipelinePolicy {
    /** Bounded queue capacity for large-library forwarding channels. */
    const val BUFFER_CAPACITY = 256

    /** Compact progress cadence; consumers post UI/notification state from these snapshots only. */
    const val PROGRESS_INTERVAL_MS = 400L
}
