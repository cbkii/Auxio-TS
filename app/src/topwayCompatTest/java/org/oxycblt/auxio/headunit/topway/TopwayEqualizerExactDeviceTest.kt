/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.headunit.topway

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TopwayEqualizerExactDeviceTest {
    private val context: Context = RuntimeEnvironment.getApplication()

    @Test
    fun exactDeviceDspActivityWinsWhenChoiceActivityIsUnavailable() {
        val resolver = Resolver(setOf(DSP_ACTIVITY))
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertEquals(DSP_ACTIVITY, intent?.component)
        assertTrue(intent?.categories?.contains(Intent.CATEGORY_LAUNCHER) == true)
    }

    @Test
    fun eqActivityUsesDefaultCategory() {
        val resolver = Resolver(setOf(EQ_ACTIVITY))
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertEquals(EQ_ACTIVITY, intent?.component)
        assertTrue(intent?.categories?.contains(Intent.CATEGORY_DEFAULT) == true)
    }

    private class Resolver(private val available: Set<ComponentName>) :
        TopwayEqualizerLauncher.IntentResolver {
        override fun resolveActivity(intent: Intent): ComponentName? =
            intent.component?.takeIf(available::contains)

        override fun getLaunchIntentForPackage(packageName: String): Intent? = null
    }

    private companion object {
        val DSP_ACTIVITY = ComponentName("com.tw.eq", "com.tw.eq.DSPActivity")
        val EQ_ACTIVITY = ComponentName("com.tw.eq", "com.tw.eq.EQActivity")
    }
}
