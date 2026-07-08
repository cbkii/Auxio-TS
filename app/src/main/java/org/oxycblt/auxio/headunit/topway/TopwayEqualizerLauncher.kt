/*
 * Copyright (c) 2026 Auxio Project
 * TopwayEqualizerLauncher.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import org.oxycblt.auxio.BuildConfig
import timber.log.Timber as L

/** Resolves TS18/Topway native EQ/DSP apps before falling back to Android AudioEffect. */
object TopwayEqualizerLauncher {

    // Internal seam allowing host-side tests to validate resolution ordering without relying on
    // Robolectric ShadowPackageManager explicit-intent behaviour.
    internal interface IntentResolver {
        fun resolveActivity(intent: Intent): ComponentName?

        fun getLaunchIntentForPackage(packageName: String): Intent?
    }

    private class DefaultIntentResolver(private val context: Context) : IntentResolver {
        override fun resolveActivity(intent: Intent): ComponentName? {
            return intent.resolveActivity(context.packageManager)
        }

        override fun getLaunchIntentForPackage(packageName: String): Intent? {
            return context.packageManager.getLaunchIntentForPackage(packageName)
        }
    }

    internal val nativeComponents =
        listOf(
            ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity"),
            ComponentName("com.tw.eq", "com.tw.eq.EQActivity"),
            ComponentName("com.tw.dsp", "com.tw.dsp.MainActivity"),
            ComponentName("com.syu.eq", "com.syu.eq.MainActivity"),
            ComponentName("com.syu.dsp", "com.syu.dsp.MainActivity"),
            ComponentName("com.ts.MainUI", "com.ts.main.dsp.DspActivity"),
            ComponentName("com.ts.mainui", "com.ts.main.dsp.DspActivity"),
            ComponentName("com.zjinnova.eq", "com.zjinnova.eq.MainActivity"),
        )

    private val nativePackages =
        listOf(
            "com.tw.eq",
            "com.tw.dsp",
            "com.syu.eq",
            "com.syu.dsp",
            "com.ts.MainUI",
            "com.ts.mainui",
            "com.zjinnova.eq",
        )

    fun resolveIntent(context: Context, audioSessionId: Int?): Intent? {
        return resolveIntent(context, audioSessionId, DefaultIntentResolver(context))
    }

    // Internal resolution logic passing through the IntentResolver seam
    internal fun resolveIntent(
        context: Context,
        audioSessionId: Int?,
        resolver: IntentResolver,
    ): Intent? {
        val attempted = mutableListOf<String>()
        if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            for (component in nativeComponents) {
                attempted += component.flattenToShortString()
                val intent =
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_LAUNCHER)
                        .setComponent(component)
                if (resolver.resolveActivity(intent) != null) {
                    L.i("Resolved native TS18 EQ/DSP component $component")
                    return intent
                }
            }
            for (pkg in nativePackages) {
                attempted += pkg
                val intent = resolver.getLaunchIntentForPackage(pkg)
                if (intent != null) {
                    L.i("Resolved native TS18 EQ/DSP package $pkg -> ${intent.component}")
                    return intent
                }
            }
        }

        val fallback =
            Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                .putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                .putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
        audioSessionId
            ?.takeIf { it > 0 }
            ?.let { fallback.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, it) }

        return if (resolver.resolveActivity(fallback) != null) {
            L.i(
                "No native TS18 EQ/DSP target resolved; using AudioEffect fallback. Tried=$attempted"
            )
            fallback
        } else {
            L.w("No EQ/DSP target resolved. Tried=$attempted")
            null
        }
    }
}
