/*
 * Copyright (c) 2026 Auxio Project
 * TopwayEqualizerLauncherTest.kt is part of Auxio.
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
import android.media.audiofx.AudioEffect
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.BuildConfig
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import timber.log.Timber

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class TopwayEqualizerLauncherTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        Timber.plant(Timber.DebugTree())
        context = RuntimeEnvironment.getApplication()
    }

    @After
    fun tearDown() {
        Timber.uprootAll()
    }

    class TestIntentResolver : TopwayEqualizerLauncher.IntentResolver {
        var resolvedComponents = mutableSetOf<ComponentName>()
        var resolvedPackages = mutableSetOf<String>()
        var queriedComponents = mutableListOf<ComponentName>()
        var queriedPackages = mutableListOf<String>()
        var disabledComponents = mutableSetOf<ComponentName>()
        var mockAudioEffectFallback = true

        override fun resolveActivity(intent: Intent): ComponentName? {
            val component = intent.component
            if (component != null) {
                queriedComponents.add(component)
                if (resolvedComponents.contains(component)) {
                    return component
                }
            }
            if (
                intent.action == AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL &&
                    mockAudioEffectFallback
            ) {
                return ComponentName(
                    "com.android.settings",
                    "com.android.settings.AudioEffectActivity",
                )
            }
            return null
        }

        override fun getLaunchIntentForPackage(packageName: String): Intent? {
            queriedPackages.add(packageName)
            if (resolvedPackages.contains(packageName)) {
                return Intent(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                    .setPackage(packageName)
                    .setComponent(ComponentName(packageName, "$packageName.DummyActivity"))
            }
            return null
        }

        override fun getActivityInfo(component: ComponentName): ActivityInfo? {
            if (!resolvedComponents.contains(component) && !disabledComponents.contains(component)) {
                return null
            }
            return ActivityInfo().apply {
                packageName = component.packageName
                name = component.className
                enabled = !disabledComponents.contains(component)
                exported = true
            }
        }

        override fun getApplicationInfo(packageName: String): ApplicationInfo? {
            val hasComponent =
                (resolvedComponents + disabledComponents).any { it.packageName == packageName }
            if (!hasComponent && !resolvedPackages.contains(packageName)) return null
            return ApplicationInfo().apply {
                this.packageName = packageName
                enabled = true
            }
        }
    }

    @Test
    fun testResolverOrderDspActivityBeforeEqChoice() {
        Assume.assumeTrue(BuildConfig.TOPWAY_COMPAT_FLAVOR)

        val resolver = TestIntentResolver()
        resolver.resolvedComponents.add(ComponentName("com.tw.eq", "com.tw.eq.DSPActivity"))
        resolver.resolvedComponents.add(ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity"))
        resolver.resolvedComponents.add(ComponentName("com.tw.eq", "com.tw.eq.EQActivity"))

        val intent = TopwayEqualizerLauncher.resolveIntent(context, 0, resolver)
        assertNotNull(intent)
        assertEquals(ComponentName("com.tw.eq", "com.tw.eq.DSPActivity"), intent?.component)
    }

    @Test
    fun testDisabledEqChoiceFallsBackToEqActivity() {
        Assume.assumeTrue(BuildConfig.TOPWAY_COMPAT_FLAVOR)

        val resolver = TestIntentResolver()
        resolver.disabledComponents.add(ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity"))
        resolver.resolvedComponents.add(ComponentName("com.tw.eq", "com.tw.eq.EQActivity"))

        val intent = TopwayEqualizerLauncher.resolveIntent(context, 0, resolver)
        assertNotNull(intent)
        assertEquals(ComponentName("com.tw.eq", "com.tw.eq.EQActivity"), intent?.component)
    }

    @Test
    fun testPackageFallback() {
        Assume.assumeTrue(BuildConfig.TOPWAY_COMPAT_FLAVOR)

        val resolver = TestIntentResolver()
        // No native components added, only package
        resolver.resolvedPackages.add("com.tw.eq")

        val intent = TopwayEqualizerLauncher.resolveIntent(context, 0, resolver)
        assertNotNull(intent)
        assertEquals("com.tw.eq", intent?.component?.packageName)
    }

    @Test
    fun testAudioEffectFallback() {
        val resolver = TestIntentResolver()
        // Ensure no native components or packages are resolved

        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertNotNull(intent)
        assertEquals(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL, intent?.action)
        assertEquals(
            AudioEffect.CONTENT_TYPE_MUSIC,
            intent?.getIntExtra(AudioEffect.EXTRA_CONTENT_TYPE, 0),
        )
        assertEquals(context.packageName, intent?.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME))
        assertEquals(123, intent?.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0))
    }

    @Test
    fun testAudioEffectFallbackFailure() {
        val resolver = TestIntentResolver()
        resolver.mockAudioEffectFallback = false

        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertNull(intent)
    }

    @Test
    fun testNoMainActivity() {
        Assume.assumeTrue(BuildConfig.TOPWAY_COMPAT_FLAVOR)

        // Make sure it doesn't contain com.tw.eq.MainActivity
        assertEquals(
            false,
            TopwayEqualizerLauncher.nativeComponents.any {
                it.component == ComponentName("com.tw.eq", "com.tw.eq.MainActivity")
            },
        )
    }

    @Test
    fun testStandardModeIsolation() {
        Assume.assumeTrue(!BuildConfig.TOPWAY_COMPAT_FLAVOR)

        val resolver = TestIntentResolver()
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)

        assertNotNull(intent)
        assertEquals(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL, intent?.action)
        assertEquals(0, resolver.queriedComponents.size)
        assertEquals(0, resolver.queriedPackages.size)
    }
}
