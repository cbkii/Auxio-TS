/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.playback

import org.oxycblt.auxio.list.adapter.UpdateInstructions
import org.oxycblt.auxio.playback.ui.swiper.CoverPagerAdapter
import org.oxycblt.musikr.Song

/**
 * Resolves the binding-scoped cover adapter before applying a queue update.
 *
 * [PlaybackPanelFragment] clears its adapter in `onDestroyBinding`. A queue callback outside the
 * active binding is therefore a lifecycle error and must fail locally instead of dereferencing a
 * nullable receiver implicitly.
 */
internal fun CoverPagerAdapter?.update(
    newList: List<Song>,
    instructions: UpdateInstructions?,
) {
    checkNotNull(this) {
            "CoverPagerAdapter must exist while the playback-panel binding is active"
        }
        .update(newList, instructions)
}
