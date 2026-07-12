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

/** Resolves launchable TS18/Topway EQ applications before Android's AudioEffect fallback. */
object TopwayEqualizerLauncher {

    internal interface IntentResolver {
        fun resolveActivity(intent: Intent): ComponentName?

        fun getLaunchIntentForPackage(packageName: String): Intent?
    }

    private class DefaultIntentResolver(private val context: Context) : IntentResolver {
        override fun resolveActivity(intent: Intent): ComponentName? =
            intent.resolveActivity(context.packageManager)

        override fun getLaunchIntentForPackage(packageName: String): Intent? =
            context.packageManager.getLaunchIntentForPackage(packageName)
    }

    internal data class NativeTarget(val component: ComponentName, val category: String)

    /**
     * Exact-device evidence for HEAD.20241126 shows DSPActivity enabled, EQChoiceActivity disabled,
     * and EQActivity registered with DEFAULT rather than LAUNCHER.
     */
    internal val nativeTargets =
        listOf(
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.DSPActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.EQActivity"),
                Intent.CATEGORY_DEFAULT,
            ),
            NativeTarget(
                ComponentName("com.tw.dsp", "com.tw.dsp.MainActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.syu.eq", "com.syu.eq.MainActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.syu.dsp", "com.syu.dsp.MainActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.ts.MainUI", "com.ts.main.dsp.DspActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.ts.mainui", "com.ts.main.dsp.DspActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.zjinnova.eq", "com.zjinnova.eq.MainActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
        )

    internal val nativeComponents: List<ComponentName>
        get() = nativeTargets.map { it.component }

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

    fun resolveIntent(context: Context, audioSessionId: Int?): Intent? =
        resolveIntents(context, audioSessionId).firstOrNull()

    fun resolveIntents(context: Context, audioSessionId: Int?): List<Intent> =
        resolveIntents(context, audioSessionId, DefaultIntentResolver(context))

    internal fun resolveIntent(
        context: Context,
        audioSessionId: Int?,
        resolver: IntentResolver,
    ): Intent? = resolveIntents(context, audioSessionId, resolver).firstOrNull()

    internal fun resolveIntents(
        context: Context,
        audioSessionId: Int?,
        resolver: IntentResolver,
    ): List<Intent> {
        val resolved = mutableListOf<Intent>()
        val attempted = mutableListOf<String>()

        if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            for (target in nativeTargets) {
                val intent =
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(target.category)
                        .setComponent(target.component)
                attempted += "${target.component.flattenToShortString()}[${target.category}]"
                if (resolver.resolveActivity(intent) != null) {
                    L.i("Resolved native TS18 EQ/DSP component ${target.component}")
                    resolved += intent
                }
            }
            for (pkg in nativePackages) {
                attempted += pkg
                resolver.getLaunchIntentForPackage(pkg)?.let { intent ->
                    if (resolved.none { it.component == intent.component }) {
                        L.i("Resolved native TS18 EQ/DSP package $pkg -> ${intent.component}")
                        resolved += intent
                    }
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
        if (resolver.resolveActivity(fallback) != null) resolved += fallback

        if (resolved.isEmpty()) L.w("No EQ/DSP target resolved. Tried=$attempted")
        return resolved
    }
}
