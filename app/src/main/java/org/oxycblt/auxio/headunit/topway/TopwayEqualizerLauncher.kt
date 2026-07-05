/*
 * Copyright (c) 2026 Auxio Project
 * TopwayEqualizerLauncher.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
    private val nativeComponents =
        listOf(
            ComponentName("com.tw.eq", "com.tw.eq.MainActivity"),
            ComponentName("com.tw.dsp", "com.tw.dsp.MainActivity"),
            ComponentName("com.syu.eq", "com.syu.eq.MainActivity"),
            ComponentName("com.syu.dsp", "com.syu.dsp.MainActivity"),
            ComponentName("com.ts.MainUI", "com.ts.main.dsp.DspActivity"),
            ComponentName("com.ts.mainui", "com.ts.main.dsp.DspActivity"),
        )

    private val nativePackages =
        listOf(
            "com.tw.eq",
            "com.tw.dsp",
            "com.syu.eq",
            "com.syu.dsp",
            "com.ts.MainUI",
            "com.ts.mainui",
        )

    fun resolveIntent(context: Context, audioSessionId: Int): Intent {
        val pm = context.packageManager
        if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            for (component in nativeComponents) {
                val intent =
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_LAUNCHER)
                        .setComponent(component)
                if (intent.resolveActivity(pm) != null) {
                    L.i("Resolved native TS18 EQ/DSP component $component")
                    return intent
                }
            }
            for (pkg in nativePackages) {
                val intent = pm.getLaunchIntentForPackage(pkg)
                if (intent != null) {
                    L.i("Resolved native TS18 EQ/DSP package $pkg -> ${intent.component}")
                    return intent
                }
            }
        }
        L.i("No native TS18 EQ/DSP target resolved; falling back to AudioEffect panel")
        return Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
            .putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId)
            .putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
            .putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
    }
}
