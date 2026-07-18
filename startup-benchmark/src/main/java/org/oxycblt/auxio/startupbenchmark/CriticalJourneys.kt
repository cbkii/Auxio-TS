/*
 * Copyright (c) 2026 Auxio Project
 * CriticalJourneys.kt is part of Auxio.
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

package org.oxycblt.auxio.startupbenchmark

import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Until

/** Shared critical user journeys for profile generation and macrobenchmarks. */
internal object CriticalJourneys {
    private const val UI_TIMEOUT_MS = 8_000L
    private const val SETTLE_MS = 250L

    fun MacrobenchmarkScope.launchFastStart() {
        pressHome()
        startActivityAndWait()
        device.waitForIdle()
    }

    fun MacrobenchmarkScope.exercisePlaybackControls() {
        device.pressKeyCode(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        device.waitForIdle()
        device.pressKeyCode(KeyEvent.KEYCODE_MEDIA_NEXT)
        device.waitForIdle()
        device.pressKeyCode(KeyEvent.KEYCODE_MEDIA_PREVIOUS)
        device.waitForIdle()
    }

    fun MacrobenchmarkScope.exerciseQuickFind(query: String = "Fixture Track 00010") {
        val searchAction = By.res(BuildConfig.TARGET_PACKAGE, "action_search")
        if (!clickIfPresent(searchAction)) return
        val input = By.clazz("android.widget.EditText")
        if (!device.wait(Until.hasObject(input), UI_TIMEOUT_MS)) return
        device.findObject(input)?.apply {
            click()
            text = query
        }
        device.waitForIdle()
        Thread.sleep(SETTLE_MS)
        clickFirstVisibleRow()
    }

    fun MacrobenchmarkScope.exerciseUsbFolder() {
        val usb0 = By.textContains("USB 0")
        val usb1 = By.textContains("USB 1")
        if (!clickIfPresent(usb0) && !clickIfPresent(usb1)) return
        device.waitForIdle()
        val folder = By.textStartsWith("📁")
        if (clickIfPresent(folder)) device.waitForIdle()
        clickIfPresent(By.textStartsWith("▶"))
        device.waitForIdle()
    }

    fun MacrobenchmarkScope.exercisePagedLibrary() {
        val width = device.displayWidth
        val height = device.displayHeight
        repeat(2) {
            device.swipe(width / 2, height * 3 / 4, width / 2, height / 4, 20)
            device.waitForIdle()
        }
        clickIfPresent(By.textContains("Albums"))
        device.waitForIdle()
        clickFirstVisibleRow()
    }

    private fun MacrobenchmarkScope.clickFirstVisibleRow(): Boolean {
        val candidates =
            listOf(
                By.res(BuildConfig.TARGET_PACKAGE, "item_root"),
                By.res(BuildConfig.TARGET_PACKAGE, "item_name"),
                By.clazz("android.widget.TextView"),
            )
        return candidates.any { clickIfPresent(it) }
    }

    private fun MacrobenchmarkScope.clickIfPresent(selector: BySelector): Boolean {
        if (!device.wait(Until.hasObject(selector), UI_TIMEOUT_MS)) return false
        val objectToClick = device.findObject(selector) ?: return false
        objectToClick.click()
        device.waitForIdle()
        return true
    }
}
