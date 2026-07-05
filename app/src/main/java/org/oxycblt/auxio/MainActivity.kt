/*
 * Copyright (c) 2021 Auxio Project
 * MainActivity.kt is part of Auxio.
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

package org.oxycblt.auxio

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.databinding.ActivityMainBinding
import org.oxycblt.auxio.headunit.HeadUnitEntryPoints
import org.oxycblt.auxio.headunit.HeadUnitRoute
import org.oxycblt.auxio.headunit.HeadUnitRoutePolicy
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.PlaybackViewModel
import org.oxycblt.auxio.playback.StartupPlaybackPolicy
import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.auxio.util.isNight
import org.oxycblt.auxio.util.systemBarInsetsCompat
import timber.log.Timber as L

/**
 * Auxio's single [AppCompatActivity].
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val playbackModel: PlaybackViewModel by viewModels()
    @Inject lateinit var uiSettings: UISettings
    @Inject lateinit var playbackSettings: PlaybackSettings
    @Inject lateinit var musicRepository: MusicRepository
    private var isFirstResume = true
    private var pendingHeadUnitLaunchRoute = false

    override fun onCreate(savedInstanceState: Bundle?) {
        PerfTimer.trace("MainActivity.onCreate") {
            super.onCreate(savedInstanceState)
            // Only treat a launch with no saved instance state as a cold launch, so that activity
            // recreation (e.g. configuration changes or restoration after process death) does not
            // re-trigger autoplay.
            isFirstResume = savedInstanceState == null
            pendingHeadUnitLaunchRoute = savedInstanceState == null
            setupTheme()
            if (uiSettings.headUnitLandscapeMode) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
            // Inflate the views after setting up the theme so that the theme attributes are
            // applied.
            val binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupEdgeToEdge(binding.root)
            L.d("Activity created")
        }
    }

    override fun onResume() {
        PerfTimer.trace("MainActivity.onResume") {
            super.onResume()

            val serviceClass =
                TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java)

            startService(
                Intent(this, serviceClass)
                    .setAction(AuxioService.ACTION_START)
                    .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_ACTIVITY)
            )

            if (!startIntentAction(intent)) {
                // No intent action to do, restore the previously saved state.
                // Only autoplay on the first resume (cold launch) so that returning to the app
                // from the background does not force playback to resume after the user paused it.
                val action =
                    StartupPlaybackPolicy.restoreActionForLaunch(
                        playbackSettings.autoplayOnLaunch && isFirstResume
                    )
                playbackModel.playDeferred(action)
                if (
                    pendingHeadUnitLaunchRoute &&
                        StartupPlaybackPolicy.shouldOpenPanelOnLaunch(musicRepository.library)
                ) {
                    pendingHeadUnitLaunchRoute = false
                    if (uiSettings.headUnitLandscapeMode) playbackModel.openQueue()
                    else playbackModel.openPlayback()
                }
            }
            isFirstResume = false
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        clearIntentRoutingState(intent)
        setIntent(intent)
        startIntentAction(intent)
    }

    private fun setupTheme() {
        // Apply the theme configuration.
        AppCompatDelegate.setDefaultNightMode(uiSettings.theme)
        // Apply the color scheme. The black theme requires it's own set of themes since
        // it's not possible to modify the themes at run-time.
        if (isNight && uiSettings.useBlackTheme) {
            L.d("Applying black theme [accent ${uiSettings.accent}]")
            setTheme(uiSettings.accent.blackTheme)
        } else {
            L.d("Applying normal theme [accent ${uiSettings.accent}]")
            setTheme(uiSettings.accent.theme)
        }
    }

    private fun setupEdgeToEdge(contentView: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        contentView.setOnApplyWindowInsetsListener { view, insets ->
            // Automatically inset the view to the left/right, as component support for
            // these insets are highly lacking.
            val bars = insets.systemBarInsetsCompat
            view.updatePadding(left = bars.left, right = bars.right)
            insets
        }
    }

    /**
     * Transform an [Intent] given to [MainActivity] into a [DeferredPlayback] that can be used in
     * the playback system.
     *
     * @param intent The (new) [Intent] given to this [MainActivity], or null if there is no intent.
     * @return true If the analogous [DeferredPlayback] to the given [Intent] was started, false
     *   otherwise.
     */
    private fun startIntentAction(intent: Intent?): Boolean {
        if (intent == null) {
            // Nothing to do.
            L.d("No intent to handle")
            return false
        }

        if (intent.getBooleanExtra(KEY_INTENT_USED, false)) {
            // Don't commit the action, but also return that the intent was applied.
            // This is because onStart can run multiple times, and thus we really don't
            // want to return false and override the original delayed action with a
            // RestoreState action.
            L.d("Already used this intent")
            return true
        }
        intent.putExtra(KEY_INTENT_USED, true)

        val route = HeadUnitRoutePolicy.routeForAction(intent.action)
        val action =
            when {
                intent.action == Intent.ACTION_VIEW ->
                    DeferredPlayback.Open(intent.data ?: return false)
                route == HeadUnitRoute.SHUFFLE_ALL -> DeferredPlayback.ShuffleAll()
                else -> null
            }
        if (action != null) {
            L.d("Translated intent to $action")
            playbackModel.playDeferred(action)
            return true
        }

        val destination =
            route?.let { resolvedRoute ->
                HeadUnitRoutePolicy.entryDestinationForRoute(resolvedRoute)
            }
        when (destination) {
            HeadUnitEntryPoints.EntryDestination.NOW_PLAYING -> playbackModel.openPlayback()
            HeadUnitEntryPoints.EntryDestination.QUEUE -> playbackModel.openQueue()
            null -> {
                L.w("Unexpected intent ${intent.action}")
                return false
            }
            else -> {
                intent.putExtra(HeadUnitEntryPoints.EXTRA_ENTRY_DESTINATION, destination.name)
                setIntent(intent)
                L.d("Mapped deep-link action to destination $destination")
            }
        }
        return true
    }

    private fun clearIntentRoutingState(intent: Intent) {
        intent.removeExtra(KEY_INTENT_USED)
        intent.removeExtra(HeadUnitEntryPoints.EXTRA_ENTRY_DESTINATION)
    }

    private companion object {
        const val KEY_INTENT_USED = BuildConfig.APPLICATION_ID + ".key.FILE_INTENT_USED"
    }
}
