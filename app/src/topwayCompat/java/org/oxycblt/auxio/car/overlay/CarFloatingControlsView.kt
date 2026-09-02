/*
 * Copyright (c) 2024 Auxio Project
 * CarFloatingControlsView.kt is part of Auxio.
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

package org.oxycblt.auxio.car.overlay

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.TextView
import androidx.preference.PreferenceManager
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.overlay.FloatingTrackMetadata
import org.oxycblt.auxio.headunit.overlay.FloatingTrackMetadataBus

/**
 * Programmatic overlay view for car floating media controls. Uses large, TS18/head-unit friendly
 * touch targets. No XML or Compose dependency for overlay-safe usage.
 */
@SuppressLint("ViewConstructor")
class CarFloatingControlsView(context: Context, private val callbacks: Callbacks) :
    LinearLayout(context), SharedPreferences.OnSharedPreferenceChangeListener {

    interface Callbacks {
        fun onDrag(deltaX: Int, deltaY: Int)

        fun onDragFinished(x: Int, y: Int)

        fun onPrevious()

        fun onPlayPause()

        fun onNext()

        fun onOpenAuxio()

        fun onStopRequested()
    }

    private val preferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    private val overlayPrefs = CarOverlayPrefs.from(context)
    private val buttonSizePx: Int
    private val rowWidthPx: Int
    private val rowHeightPx: Int
    private val rowGapPx: Int
    private val controlsRow: LinearLayout
    private var tickerView: TextView? = null
    private var dragStartX = 0f
    private var dragStartY = 0f
    private var dragging = false
    private var lastTapTime = 0L
    private var tapCount = 0
    private val dragThresholdSq: Int
    private val trackMetadataListener: (FloatingTrackMetadata?) -> Unit = { metadata ->
        post { updateTrackTicker(metadata) }
    }

    init {
        val density = context.resources.displayMetrics.density
        buttonSizePx = (BUTTON_SIZE_DP * density).toInt()
        val paddingPx = (PADDING_DP * density).toInt()
        rowGapPx = (ROW_GAP_DP * density).toInt()
        rowWidthPx = buttonSizePx * CONTROL_COUNT + paddingPx * 2
        rowHeightPx = buttonSizePx + paddingPx * 2

        // Use system touch slop for density-aware drag threshold.
        val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        dragThresholdSq = touchSlop * touchSlop

        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL

        controlsRow =
            LinearLayout(context).apply {
                orientation = HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
                background = createBackground(density)
                layoutParams = LayoutParams(rowWidthPx, rowHeightPx)

                addView(createDragHandle(context))
                addView(createButton(context, LABEL_PREV, DESC_PREV) { callbacks.onPrevious() })
                addView(
                    createButton(context, LABEL_PLAY_PAUSE, DESC_PLAY_PAUSE) {
                        callbacks.onPlayPause()
                    }
                )
                addView(createButton(context, LABEL_NEXT, DESC_NEXT) { callbacks.onNext() })
                addView(createButton(context, LABEL_OPEN, DESC_OPEN) { callbacks.onOpenAuxio() })
            }

        applyDisplayMode(overlayPrefs.displayMode)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        tickerView?.isSelected = true
        preferences.registerOnSharedPreferenceChangeListener(this)
        FloatingTrackMetadataBus.addListener(trackMetadataListener)
    }

    override fun onDetachedFromWindow() {
        FloatingTrackMetadataBus.removeListener(trackMetadataListener)
        preferences.unregisterOnSharedPreferenceChangeListener(this)
        tickerView?.isSelected = false
        super.onDetachedFromWindow()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (
            key != CarOverlayPrefs.KEY_DISPLAY_MODE &&
                key != CarOverlayPrefs.KEY_TICKER_WIDTH_PERCENT
        ) {
            return
        }
        post { applyDisplayMode(overlayPrefs.displayMode) }
    }

    fun applyOpacity(percent: Int) {
        alpha = percent.coerceIn(CarOverlayPrefs.MIN_OPACITY, CarOverlayPrefs.MAX_OPACITY) / 100f
    }

    private fun applyDisplayMode(mode: CarOverlayPrefs.DisplayMode) {
        removeAllViews()

        if (mode.showsTicker) {
            val ticker = tickerView ?: createTicker(context).also { tickerView = it }
            updateTickerLayout(ticker, mode)
            addView(ticker)
            updateTrackTicker(FloatingTrackMetadataBus.current)
            ticker.isSelected = true
        } else {
            tickerView?.isSelected = false
            tickerView = null
        }

        if (mode.showsControls) {
            addView(controlsRow)
        }
        requestLayout()
    }

    private fun updateTickerLayout(ticker: TextView, mode: CarOverlayPrefs.DisplayMode) {
        val width =
            if (mode == CarOverlayPrefs.DisplayMode.TICKER_ONLY) {
                tickerOnlyWidthPx(overlayPrefs.tickerWidthPercent)
            } else {
                rowWidthPx
            }
        ticker.layoutParams =
            LayoutParams(width, rowHeightPx).apply {
                bottomMargin = if (mode.showsControls) rowGapPx else 0
            }
    }

    private fun tickerOnlyWidthPx(percent: Int): Int {
        val requested =
            (rowWidthPx.toLong() *
                    percent.coerceIn(MIN_TICKER_WIDTH_PERCENT, MAX_TICKER_WIDTH_PERCENT) / 100L)
                .coerceAtMost(Int.MAX_VALUE.toLong())
                .toInt()
        val displayWidth = context.resources.displayMetrics.widthPixels.takeIf { it > 0 }
        return if (displayWidth != null) requested.coerceAtMost(displayWidth).coerceAtLeast(1)
        else requested.coerceAtLeast(1)
    }

    private fun createTicker(context: Context): TextView {
        val density = context.resources.displayMetrics.density
        val horizontalPadding = (TICKER_HORIZONTAL_PADDING_DP * density).toInt()
        return TextView(context).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, TICKER_TEXT_SP)
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER_VERTICAL
            includeFontPadding = false
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1
            setHorizontallyScrolling(true)
            setPadding(horizontalPadding, 0, horizontalPadding, 0)
            background = createBackground(density)
            text = context.getString(R.string.car_overlay_track_ticker_idle)
            contentDescription =
                context.getString(R.string.car_overlay_track_ticker_content_description, text)
            isSelected = true
            installDragSurface(this)
        }
    }

    private fun updateTrackTicker(metadata: FloatingTrackMetadata?) {
        val ticker = tickerView ?: return
        val nextText =
            metadata?.displayText?.takeIf { it.isNotBlank() }
                ?: context.getString(R.string.car_overlay_track_ticker_idle)
        if (ticker.text.toString() == nextText) return

        ticker.isSelected = false
        ticker.text = nextText
        ticker.contentDescription =
            context.getString(R.string.car_overlay_track_ticker_content_description, nextText)
        ticker.post {
            if (tickerView === ticker) {
                ticker.isSelected = true
            }
        }
    }

    private fun createBackground(density: Float): GradientDrawable =
        GradientDrawable().apply {
            setColor(BG_COLOR)
            cornerRadius = CORNER_RADIUS_DP * density
        }

    private fun createDragHandle(context: Context): View {
        val tv = TextView(context)
        tv.text = LABEL_DRAG
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, BUTTON_TEXT_SP)
        tv.gravity = Gravity.CENTER
        tv.includeFontPadding = false
        tv.setTextColor(Color.WHITE)
        tv.layoutParams = LayoutParams(buttonSizePx, buttonSizePx)
        tv.contentDescription = DESC_DRAG
        installDragSurface(tv)
        return tv
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun installDragSurface(view: View) {
        view.isFocusable = true
        view.isClickable = true
        view.setOnClickListener { handleDragSurfaceTap() }
        view.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dragStartX = event.rawX
                    dragStartY = event.rawY
                    dragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = (event.rawX - dragStartX).toInt()
                    val dy = (event.rawY - dragStartY).toInt()
                    if (!dragging && (dx * dx + dy * dy > dragThresholdSq)) {
                        dragging = true
                    }
                    if (dragging) {
                        callbacks.onDrag(dx, dy)
                        dragStartX = event.rawX
                        dragStartY = event.rawY
                    }
                    true
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    if (dragging) {
                        callbacks.onDragFinished(event.rawX.toInt(), event.rawY.toInt())
                    } else if (event.actionMasked == MotionEvent.ACTION_UP) {
                        // Trigger accessibility click which routes to setOnClickListener above.
                        v.performClick()
                    }
                    dragging = false
                    true
                }
                else -> false
            }
        }
    }

    private fun handleDragSurfaceTap() {
        val now = System.currentTimeMillis()
        if (now - lastTapTime < TRIPLE_TAP_WINDOW_MS) {
            tapCount++
        } else {
            tapCount = 1
        }
        lastTapTime = now
        if (tapCount >= TRIPLE_TAP_COUNT) {
            tapCount = 0
            callbacks.onStopRequested()
        }
    }

    private fun createButton(
        context: Context,
        label: String,
        description: String,
        onClick: () -> Unit,
    ): View {
        val tv = TextView(context)
        tv.text = label
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, BUTTON_TEXT_SP)
        tv.gravity = Gravity.CENTER
        tv.includeFontPadding = false
        tv.setTextColor(Color.WHITE)
        tv.layoutParams = LayoutParams(buttonSizePx, buttonSizePx)
        tv.contentDescription = description
        tv.isFocusable = true
        tv.isClickable = true
        tv.setOnClickListener { onClick() }
        return tv
    }

    private companion object {
        const val CONTROL_COUNT = 5
        const val BUTTON_SIZE_DP = 64f
        const val PADDING_DP = 8f
        const val ROW_GAP_DP = 4f
        const val TICKER_HORIZONTAL_PADDING_DP = 16f
        const val CORNER_RADIUS_DP = 12f
        const val BUTTON_TEXT_SP = 24f
        const val TICKER_TEXT_SP = 20f
        const val BG_COLOR = 0xCC1B1B1B.toInt()
        const val TRIPLE_TAP_WINDOW_MS = 600L
        const val TRIPLE_TAP_COUNT = 3
        const val MIN_TICKER_WIDTH_PERCENT = 100
        const val MAX_TICKER_WIDTH_PERCENT = 300

        const val LABEL_DRAG = "\u2807" // Braille pattern dots-123
        const val LABEL_PREV = "\u23EE"
        const val LABEL_PLAY_PAUSE = "\u23EF"
        const val LABEL_NEXT = "\u23ED"
        const val LABEL_OPEN = "\u266A"

        const val DESC_DRAG = "Drag to move overlay, triple-tap to dismiss"
        const val DESC_PREV = "Previous track"
        const val DESC_PLAY_PAUSE = "Play or pause"
        const val DESC_NEXT = "Next track"
        const val DESC_OPEN = "Open Auxio"
    }
}
