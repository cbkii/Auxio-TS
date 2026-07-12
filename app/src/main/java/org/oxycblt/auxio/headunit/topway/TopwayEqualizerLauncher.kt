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
import android.content.pm.PackageManager
import android.media.audiofx.AudioEffect
import org.oxycblt.auxio.BuildConfig
import timber.log.Timber as L

/** Resolves launchable TS18/Topway EQ applications before Android's AudioEffect fallback. */
object TopwayEqualizerLauncher {

    data class Candidate(val intent: Intent, val label: String, val kind: Kind) {
        enum class Kind {
            EXPLICIT_COMPONENT,
            PACKAGE_LAUNCHER,
            ANDROID_AUDIO_EFFECT,
        }
    }

    internal interface IntentResolver {
        fun resolveActivity(intent: Intent): ComponentName?

        fun getLaunchIntentForPackage(packageName: String): Intent?

        fun isComponentEffectivelyEnabled(component: ComponentName): Boolean
    }

    private class DefaultIntentResolver(private val context: Context) : IntentResolver {
        private val pm = context.packageManager

        override fun resolveActivity(intent: Intent): ComponentName? =
            intent.resolveActivity(pm)

        override fun getLaunchIntentForPackage(packageName: String): Intent? =
            pm.getLaunchIntentForPackage(packageName)

        override fun isComponentEffectivelyEnabled(component: ComponentName): Boolean =
            try {
                val setting = pm.getComponentEnabledSetting(component)
                when (setting) {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> true
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED -> false
                    else -> {
                        // DEFAULT: fall back to manifest-declared state via resolve check
                        val probe =
                            Intent(Intent.ACTION_MAIN)
                                .setComponent(component)
                                .addCategory(Intent.CATEGORY_DEFAULT)
                        pm.resolveActivity(probe, 0) != null ||
                            pm.resolveActivity(
                                Intent(Intent.ACTION_MAIN)
                                    .setComponent(component)
                                    .addCategory(Intent.CATEGORY_LAUNCHER),
                                0,
                            ) != null
                    }
                }
            } catch (_: Exception) {
                false
            }
    }

    internal data class NativeTarget(val component: ComponentName, val category: String)

    /**
     * Stock EQ router first, exact-device proven DSP fallback second.
     *
     * EQChoiceActivity is the stock router that forwards to the correct DSP surface.
     * DSPActivity is the direct DSP launcher proven on the exact device as a fallback
     * when the router is disabled or unavailable. EQActivity uses CATEGORY_DEFAULT.
     */
    internal val nativeTargets =
        listOf(
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.DSPActivity"),
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
        resolveCandidates(context, audioSessionId).firstOrNull()?.intent

    fun resolveCandidates(context: Context, audioSessionId: Int?): List<Candidate> =
        resolveCandidates(context, audioSessionId, DefaultIntentResolver(context))

    internal fun resolveIntent(
        context: Context,
        audioSessionId: Int?,
        resolver: IntentResolver,
    ): Intent? = resolveCandidates(context, audioSessionId, resolver).firstOrNull()?.intent

    internal fun resolveCandidates(
        context: Context,
        audioSessionId: Int?,
        resolver: IntentResolver,
    ): List<Candidate> {
        val candidates = mutableListOf<Candidate>()
        val attempted = mutableListOf<String>()

        if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            for (target in nativeTargets) {
                val label = target.component.flattenToShortString()
                attempted += "$label[${target.category}]"
                if (!resolver.isComponentEffectivelyEnabled(target.component)) {
                    L.d("Skipping disabled/unavailable EQ candidate $label")
                    continue
                }
                val intent =
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(target.category)
                        .setComponent(target.component)
                if (resolver.resolveActivity(intent) != null) {
                    L.i("Resolved native TS18 EQ/DSP component $label")
                    candidates +=
                        Candidate(intent, label, Candidate.Kind.EXPLICIT_COMPONENT)
                }
            }
            for (pkg in nativePackages) {
                attempted += pkg
                val intent = resolver.getLaunchIntentForPackage(pkg) ?: continue
                if (candidates.none { it.intent.component == intent.component }) {
                    L.i("Resolved native TS18 EQ/DSP package $pkg -> ${intent.component}")
                    candidates += Candidate(intent, pkg, Candidate.Kind.PACKAGE_LAUNCHER)
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
        if (resolver.resolveActivity(fallback) != null) {
            candidates +=
                Candidate(fallback, "android.media.audiofx", Candidate.Kind.ANDROID_AUDIO_EFFECT)
        }

        if (candidates.isEmpty()) {
            L.w("No EQ/DSP target resolved. Tried=$attempted")
        } else {
            L.i("Resolved EQ/DSP candidates: ${candidates.map { it.label }}")
        }
        return candidates.distinctBy { it.intent.component ?: it.intent.action }
    }
}
