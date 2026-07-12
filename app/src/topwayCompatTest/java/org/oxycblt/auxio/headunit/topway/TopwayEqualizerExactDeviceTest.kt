/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.headunit.topway

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TopwayEqualizerExactDeviceTest {
    private val context: Context = RuntimeEnvironment.getApplication()

    @Test
    fun stockRouterFirstWhenBothAvailable() {
        val resolver =
            Resolver(
                available = setOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY),
                enabled = setOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY),
            )
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertEquals(EQ_CHOICE_ACTIVITY, intent?.component)
    }

    @Test
    fun dspActivityFallbackWhenRouterDisabled() {
        val resolver =
            Resolver(
                available = setOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY),
                enabled = setOf(DSP_ACTIVITY), // EQChoiceActivity disabled
            )
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertEquals(DSP_ACTIVITY, intent?.component)
    }

    @Test
    fun dspActivityWinsWhenChoiceActivityUnavailable() {
        val resolver =
            Resolver(
                available = setOf(DSP_ACTIVITY),
                enabled = setOf(DSP_ACTIVITY),
            )
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertEquals(DSP_ACTIVITY, intent?.component)
        assertTrue(intent?.categories?.contains(Intent.CATEGORY_LAUNCHER) == true)
    }

    @Test
    fun eqActivityUsesDefaultCategory() {
        val resolver =
            Resolver(
                available = setOf(EQ_ACTIVITY),
                enabled = setOf(EQ_ACTIVITY),
            )
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertEquals(EQ_ACTIVITY, intent?.component)
        assertTrue(intent?.categories?.contains(Intent.CATEGORY_DEFAULT) == true)
    }

    @Test
    fun disabledComponentSkipped() {
        val resolver =
            Resolver(
                available = setOf(EQ_CHOICE_ACTIVITY, EQ_ACTIVITY),
                enabled = setOf(EQ_ACTIVITY), // EQChoiceActivity disabled
            )
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertEquals(EQ_ACTIVITY, intent?.component)
    }

    @Test
    fun packageFallbackWhenNoComponentsAvailable() {
        val resolver =
            Resolver(
                available = emptySet(),
                enabled = emptySet(),
                launchablePackages = setOf("com.tw.eq"),
            )
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertNotNull(intent)
        assertEquals("com.tw.eq", intent?.component?.packageName)
    }

    @Test
    fun audioEffectFallbackWhenNoNative() {
        val resolver =
            Resolver(
                available = emptySet(),
                enabled = emptySet(),
                mockAudioEffectFallback = true,
            )
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertNotNull(intent)
        assertEquals(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL, intent?.action)
    }

    @Test
    fun noFallbackReturnsNull() {
        val resolver =
            Resolver(
                available = emptySet(),
                enabled = emptySet(),
                mockAudioEffectFallback = false,
            )
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertNull(intent)
    }

    @Test
    fun candidateOrderPreservesRouterFirst() {
        val resolver =
            Resolver(
                available = setOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY, EQ_ACTIVITY),
                enabled = setOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY, EQ_ACTIVITY),
            )
        val candidates = TopwayEqualizerLauncher.resolveCandidates(context, 123, resolver)
        assertTrue(candidates.size >= 3)
        assertEquals(EQ_CHOICE_ACTIVITY, candidates[0].intent.component)
        assertEquals(DSP_ACTIVITY, candidates[1].intent.component)
        assertEquals(EQ_ACTIVITY, candidates[2].intent.component)
    }

    @Test
    fun launchTimeContinuesToNextCandidate() {
        val resolver =
            Resolver(
                available = setOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY),
                enabled = setOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY),
            )
        val candidates = TopwayEqualizerLauncher.resolveCandidates(context, 123, resolver)
        assertTrue(candidates.size >= 2)
        // First candidate fails at launch time -> caller should try second
        assertEquals(
            TopwayEqualizerLauncher.Candidate.Kind.EXPLICIT_COMPONENT,
            candidates[0].kind,
        )
        assertEquals(
            TopwayEqualizerLauncher.Candidate.Kind.EXPLICIT_COMPONENT,
            candidates[1].kind,
        )
    }

    private class Resolver(
        private val available: Set<ComponentName>,
        private val enabled: Set<ComponentName>,
        private val launchablePackages: Set<String> = emptySet(),
        private val mockAudioEffectFallback: Boolean = false,
    ) : TopwayEqualizerLauncher.IntentResolver {
        override fun resolveActivity(intent: Intent): ComponentName? {
            intent.component?.let { c ->
                if (available.contains(c) && enabled.contains(c)) return c
                return null
            }
            if (
                intent.action == AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL &&
                    mockAudioEffectFallback
            ) {
                return ComponentName("com.android.settings", "AudioEffectActivity")
            }
            return null
        }

        override fun getLaunchIntentForPackage(packageName: String): Intent? {
            if (launchablePackages.contains(packageName)) {
                return Intent(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                    .setPackage(packageName)
                    .setComponent(ComponentName(packageName, "$packageName.DummyActivity"))
            }
            return null
        }

        override fun isComponentEffectivelyEnabled(component: ComponentName): Boolean =
            enabled.contains(component)
    }

    private companion object {
        val EQ_CHOICE_ACTIVITY = ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity")
        val DSP_ACTIVITY = ComponentName("com.tw.eq", "com.tw.eq.DSPActivity")
        val EQ_ACTIVITY = ComponentName("com.tw.eq", "com.tw.eq.EQActivity")
    }
}
