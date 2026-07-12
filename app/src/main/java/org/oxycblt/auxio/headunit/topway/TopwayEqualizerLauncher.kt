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
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import android.media.audiofx.AudioEffect
import org.oxycblt.auxio.BuildConfig
import timber.log.Timber as L

/** Resolves TS18/Topway native EQ/DSP apps before falling back to Android AudioEffect. */
object TopwayEqualizerLauncher {
    data class Candidate(val intent: Intent, val label: String, val kind: Kind) {
        enum class Kind { EXPLICIT_COMPONENT, PACKAGE_LAUNCHER, ANDROID_AUDIO_EFFECT }
    }

    internal interface IntentResolver {
        fun resolveActivity(intent: Intent): ComponentName?
        fun getLaunchIntentForPackage(packageName: String): Intent?
        fun getActivityInfo(component: ComponentName): ActivityInfo?
        fun getApplicationInfo(packageName: String): ApplicationInfo?
    }

    private class DefaultIntentResolver(private val context: Context) : IntentResolver {
        private val pm = context.packageManager
        override fun resolveActivity(intent: Intent): ComponentName? = intent.resolveActivity(pm)
        override fun getLaunchIntentForPackage(packageName: String): Intent? =
            pm.getLaunchIntentForPackage(packageName)

        override fun getActivityInfo(component: ComponentName): ActivityInfo? =
            try {
                pm.getActivityInfo(component, PackageManager.MATCH_DISABLED_COMPONENTS)
            } catch (_: PackageManager.NameNotFoundException) {
                null
            }

        override fun getApplicationInfo(packageName: String): ApplicationInfo? =
            try {
                pm.getApplicationInfo(packageName, PackageManager.MATCH_DISABLED_COMPONENTS)
            } catch (_: PackageManager.NameNotFoundException) {
                null
            }
    }

    internal data class NativeSpec(val component: ComponentName, val categories: List<String>)

    internal val nativeComponents =
        listOf(
            NativeSpec(
                ComponentName("com.tw.eq", "com.tw.eq.DSPActivity"),
                listOf(Intent.CATEGORY_LAUNCHER),
            ),
            NativeSpec(
                ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity"),
                listOf(Intent.CATEGORY_LAUNCHER),
            ),
            NativeSpec(
                ComponentName("com.tw.eq", "com.tw.eq.EQActivity"),
                listOf(Intent.CATEGORY_DEFAULT),
            ),
            NativeSpec(
                ComponentName("com.tw.dsp", "com.tw.dsp.MainActivity"),
                listOf(Intent.CATEGORY_LAUNCHER),
            ),
            NativeSpec(
                ComponentName("com.syu.eq", "com.syu.eq.MainActivity"),
                listOf(Intent.CATEGORY_LAUNCHER),
            ),
            NativeSpec(
                ComponentName("com.syu.dsp", "com.syu.dsp.MainActivity"),
                listOf(Intent.CATEGORY_LAUNCHER),
            ),
            NativeSpec(
                ComponentName("com.ts.MainUI", "com.ts.main.dsp.DspActivity"),
                listOf(Intent.CATEGORY_LAUNCHER),
            ),
            NativeSpec(
                ComponentName("com.ts.mainui", "com.ts.main.dsp.DspActivity"),
                listOf(Intent.CATEGORY_LAUNCHER),
            ),
            NativeSpec(
                ComponentName("com.zjinnova.eq", "com.zjinnova.eq.MainActivity"),
                listOf(Intent.CATEGORY_LAUNCHER),
            ),
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

    fun resolveIntent(context: Context, audioSessionId: Int?): Intent? =
        resolveCandidates(context, audioSessionId).firstOrNull()?.intent

    fun resolveCandidates(context: Context, audioSessionId: Int?): List<Candidate> =
        resolveCandidates(context, audioSessionId, DefaultIntentResolver(context))

    internal fun resolveCandidates(
        context: Context,
        audioSessionId: Int?,
        resolver: IntentResolver,
    ): List<Candidate> {
        val candidates = mutableListOf<Candidate>()
        val attempted = mutableListOf<String>()
        if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            for (spec in nativeComponents) {
                attempted += spec.component.flattenToShortString()
                val app = resolver.getApplicationInfo(spec.component.packageName)
                val info = resolver.getActivityInfo(spec.component)
                if (
                    app?.enabled != true ||
                        info == null ||
                        !info.isEffectivelyEnabled() ||
                        !info.exported
                ) {
                    L.d(
                        "Skipping disabled/unexported EQ candidate ${spec.component}: " +
                            "app=${app?.enabled} activity=${info?.enabled} " +
                            "exported=${info?.exported}"
                    )
                    continue
                }
                val intent = Intent(Intent.ACTION_MAIN).setComponent(spec.component)
                spec.categories.forEach { intent.addCategory(it) }
                if (resolver.resolveActivity(intent) != null) {
                    candidates +=
                        Candidate(
                            intent,
                            spec.component.flattenToShortString(),
                            Candidate.Kind.EXPLICIT_COMPONENT,
                        )
                } else {
                    L.d(
                        "Skipping EQ candidate without matching filter ${spec.component} " +
                            "categories=${spec.categories}"
                    )
                }
            }
            for (pkg in nativePackages) {
                attempted += pkg
                val app = resolver.getApplicationInfo(pkg)
                if (app?.enabled != true) continue
                val intent = resolver.getLaunchIntentForPackage(pkg) ?: continue
                candidates += Candidate(intent, pkg, Candidate.Kind.PACKAGE_LAUNCHER)
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

    private fun ComponentInfo.isEffectivelyEnabled(): Boolean = enabled
}
