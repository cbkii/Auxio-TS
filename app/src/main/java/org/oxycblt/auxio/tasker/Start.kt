/*
 * Copyright (c) 2024 Auxio Project
 * Start.kt is part of Auxio.
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

package org.oxycblt.auxio.tasker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.core.content.ContextCompat
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerActionNoOutputOrInput
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoOutputOrInput
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigNoInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge
import timber.log.Timber as L

class StartActionHelper(config: TaskerPluginConfig<Unit>) :
    TaskerPluginConfigHelperNoOutputOrInput<StartActionRunner>(config) {
    override val runnerClass: Class<StartActionRunner>
        get() = StartActionRunner::class.java

    override fun addToStringBlurb(input: TaskerInput<Unit>, blurbBuilder: StringBuilder) {
        blurbBuilder.append(context.getString(R.string.lng_tasker_start))
    }
}

class ActivityConfigStartAction : Activity(), TaskerPluginConfigNoInput {
    override val context: Context
        get() = applicationContext

    private val taskerHelper by lazy { StartActionHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskerHelper.finishForTasker()
    }
}

class StartActionRunner : TaskerPluginRunnerActionNoOutputOrInput() {
    override fun run(context: Context, input: TaskerInput<Unit>): TaskerPluginResult<Unit> {
        val serviceClass = TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java)
        ContextCompat.startForegroundService(
            context,
            Intent(context, serviceClass)
                .setAction(AuxioService.ACTION_START)
                .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_TASKER),
        )

        // Wait for the service to become foreground, but never block Tasker indefinitely.
        val startTime = SystemClock.elapsedRealtime()
        while (
            !AuxioService.isForeground &&
                SystemClock.elapsedRealtime() - startTime < FOREGROUND_TIMEOUT_MS
        ) {
            Thread.sleep(FOREGROUND_POLL_INTERVAL_MS)
        }

        if (AuxioService.isForeground) {
            // Actually need to sleep even longer since for some reason the notification still
            // won't accept media button events for an arbitrary period.
            Thread.sleep(MEDIA_BUTTON_SETTLE_DELAY_MS)
        } else {
            L.w("Timed out waiting for AuxioService to enter foreground for Tasker start action")
        }
        return TaskerPluginResultSucess()
    }

    private companion object {
        const val FOREGROUND_TIMEOUT_MS = 5000L
        const val FOREGROUND_POLL_INTERVAL_MS = 100L
        const val MEDIA_BUTTON_SETTLE_DELAY_MS = 100L
    }
}
