package org.oxycblt.auxio.image

import org.oxycblt.auxio.IntegerTable

/** Represents the options available for album cover loading. */
enum class CoverMode {
    OFF,
    OPTIMISED,
    AS_IS;

    val intCode: Int
        get() =
            when (this) {
                OFF -> IntegerTable.COVER_MODE_OFF
                OPTIMISED -> IntegerTable.COVER_MODE_BALANCED
                AS_IS -> IntegerTable.COVER_MODE_AS_IS
            }

    companion object {
        fun fromIntCode(intCode: Int) =
            when (intCode) {
                IntegerTable.COVER_MODE_OFF -> OFF
                IntegerTable.COVER_MODE_BALANCED -> OPTIMISED
                IntegerTable.COVER_MODE_AS_IS -> AS_IS
                else -> null
            }
    }
}
