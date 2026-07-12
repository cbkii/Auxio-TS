#!/usr/bin/env python3
"""Apply the final bounded PR #169 review corrections."""

from pathlib import Path


def replace_exact(path: str, old: str, new: str) -> None:
    file = Path(path)
    text = file.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{path}: expected exactly one match, found {count}")
    file.write_text(text.replace(old, new, 1), encoding="utf-8")


replace_exact(
    "app/src/main/java/org/oxycblt/auxio/playback/PlaybackPanelFragment.kt",
    '''            for (candidate in candidates) {
                try {
                    requireContext().startActivity(candidate.intent)
                    L.i("Launched EQ/DSP candidate ${candidate.label} (${candidate.kind})")
                    return true
                } catch (e: android.content.ActivityNotFoundException) {
                    L.w(e, "EQ/DSP candidate not found after resolution: ${candidate.label}")
                } catch (e: SecurityException) {
                    L.w(e, "EQ/DSP candidate denied: ${candidate.label}")
                } catch (e: RuntimeException) {
                    L.w(e, "EQ/DSP candidate failed at launch: ${candidate.label}")
                }
            }
            requireContext().showToast(R.string.err_no_equalizer_app)
            return true
''',
    '''            val launched =
                TopwayEqualizerLauncher.launchFirstWorkingCandidate(
                    candidates = candidates,
                    launch = { requireContext().startActivity(it) },
                    onFailure = { candidate, error ->
                        when (error) {
                            is android.content.ActivityNotFoundException ->
                                L.w(
                                    error,
                                    "EQ/DSP candidate not found after resolution: ${candidate.label}",
                                )
                            is SecurityException ->
                                L.w(error, "EQ/DSP candidate denied: ${candidate.label}")
                            else ->
                                L.w(error, "EQ/DSP candidate failed at launch: ${candidate.label}")
                        }
                    },
                )
            if (launched != null) {
                L.i("Launched EQ/DSP candidate ${launched.label} (${launched.kind})")
            } else {
                requireContext().showToast(R.string.err_no_equalizer_app)
            }
            return true
''',
)

replace_exact(
    "app/src/main/java/org/oxycblt/auxio/playback/PlaybackPanelFragment.kt",
    '''    private fun updatePager(queue: PagerQueue) {
        val binding = requireBinding()
        coverPagerAdapter?.setActivePosition(queue.index)

        val command = playbackModel.pagerCommand.consume()
''',
    '''    private fun updatePager(queue: PagerQueue) {
        val binding = requireBinding()
        val adapter =
            checkNotNull(coverPagerAdapter) {
                "CoverPagerAdapter must exist while the playback-panel binding is active"
            }
        adapter.setActivePosition(queue.index)

        val command = playbackModel.pagerCommand.consume()
''',
)
replace_exact(
    "app/src/main/java/org/oxycblt/auxio/playback/PlaybackPanelFragment.kt",
    "            coverPagerAdapter.update(queue.queue, null)\n",
    "            adapter.update(queue.queue, null)\n",
)
replace_exact(
    "app/src/main/java/org/oxycblt/auxio/playback/PlaybackPanelFragment.kt",
    "            coverPagerAdapter.update(queue.queue, command.update)\n",
    "            adapter.update(queue.queue, command.update)\n",
)

replace_exact(
    "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayEqualizerLauncher.kt",
    '''    fun resolveCandidates(context: Context, audioSessionId: Int?): List<Candidate> =
        resolveCandidates(context, audioSessionId, DefaultIntentResolver(context))

''',
    '''    fun resolveCandidates(context: Context, audioSessionId: Int?): List<Candidate> =
        resolveCandidates(context, audioSessionId, DefaultIntentResolver(context))

    internal fun launchFirstWorkingCandidate(
        candidates: List<Candidate>,
        launch: (Intent) -> Unit,
        onFailure: (Candidate, RuntimeException) -> Unit = { _, _ -> },
    ): Candidate? {
        for (candidate in candidates) {
            try {
                launch(candidate.intent)
                return candidate
            } catch (error: RuntimeException) {
                onFailure(candidate, error)
            }
        }
        return null
    }

''',
)

replace_exact(
    "app/src/topwayCompatTest/java/org/oxycblt/auxio/headunit/topway/TopwayEqualizerExactDeviceTest.kt",
    "import android.content.ComponentName\n",
    "import android.content.ActivityNotFoundException\nimport android.content.ComponentName\n",
)
replace_exact(
    "app/src/topwayCompatTest/java/org/oxycblt/auxio/headunit/topway/TopwayEqualizerExactDeviceTest.kt",
    '''        val candidates = TopwayEqualizerLauncher.resolveCandidates(context, 123, resolver)
        assertTrue(candidates.size >= 2)
        // First candidate fails at launch time -> caller should try second
        assertEquals(TopwayEqualizerLauncher.Candidate.Kind.EXPLICIT_COMPONENT, candidates[0].kind)
        assertEquals(TopwayEqualizerLauncher.Candidate.Kind.EXPLICIT_COMPONENT, candidates[1].kind)
''',
    '''        val candidates = TopwayEqualizerLauncher.resolveCandidates(context, 123, resolver)
        assertTrue(candidates.size >= 2)
        val attempted = mutableListOf<ComponentName?>()
        val failed = mutableListOf<ComponentName?>()

        val launched =
            TopwayEqualizerLauncher.launchFirstWorkingCandidate(
                candidates = candidates,
                launch = { intent ->
                    attempted += intent.component
                    if (attempted.size == 1) {
                        throw ActivityNotFoundException("first candidate unavailable")
                    }
                },
                onFailure = { candidate, _ -> failed += candidate.intent.component },
            )

        assertEquals(listOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY), attempted)
        assertEquals(listOf(EQ_CHOICE_ACTIVITY), failed)
        assertEquals(DSP_ACTIVITY, launched?.intent?.component)
''',
)

replace_exact(
    "app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt",
    '''        if (!isOverlayAttached) {
            showOverlayIfAllowed()
        }
''',
    '''        if (!isOverlayAttached) {
            mainHandler.removeCallbacks(attachRetryRunnable)
            attachRetryCount = 0
            showOverlayIfAllowed()
        }
''',
)
