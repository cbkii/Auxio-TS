from pathlib import Path


def replace_exact(path: str, old: str, new: str) -> None:
    file = Path(path)
    text = file.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{path}: expected one replacement target, found {count}")
    file.write_text(text.replace(old, new), encoding="utf-8", newline="\n")


replace_exact(
    ".github/workflows/startup-performance.yml",
    "      - cx/incremental-library-pipeline\n",
    "      - cx/startup-profiles-benchmarks\n",
)

replace_exact(
    "app/src/main/java/org/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser.kt",
    "                        playbackPath = candidate.file.absolutePath,\n",
    """                        playbackPath =
                            if (exposePhysicalPlaybackPath) {
                                candidate.file.absolutePath
                            } else {
                                candidate.appPath
                            },
""",
)

settings = "app/src/main/java/org/oxycblt/auxio/settings/categories/MusicPreferenceFragment.kt"
replace_exact(
    settings,
    "import android.content.Intent\n",
    "import android.content.ActivityNotFoundException\nimport android.content.Intent\n",
)
replace_exact(
    settings,
    """                startActivity(
                    Intent.createChooser(
                        shareIntent,
                        getString(R.string.set_export_startup_report_chooser),
                    )
                )
""",
    """                try {
                    startActivity(
                        Intent.createChooser(
                            shareIntent,
                            getString(R.string.set_export_startup_report_chooser),
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    L.w(e, "No activity can share the startup performance report")
                }
""",
)

critical_path = Path(
    "startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/CriticalJourneys.kt"
)
critical = critical_path.read_text(encoding="utf-8")

early_start = critical.index("    fun exerciseEarlyMediaBrowser() {")
early_end = critical.index(
    "\n    fun MacrobenchmarkScope.waitForAudioPlayback()", early_start
)
early_replacement = """    fun exerciseEarlyMediaBrowser() {
        traceSection(TRACE_MEDIA_BROWSER_FIRST_PAGE) {
            withConnectedBrowser(
                connectionFailureMessage =
                    "MediaBrowser root did not connect before full-library hydration",
                disconnectedMessage = "MediaBrowser reported disconnected after callback",
            ) { browser ->
                val childrenReady = CountDownLatch(1)
                var childrenCount = -1
                val callback =
                    object : MediaBrowserCompat.SubscriptionCallback() {
                        override fun onChildrenLoaded(
                            parentId: String,
                            children: MutableList<MediaBrowserCompat.MediaItem>,
                        ) {
                            childrenCount = children.size
                            childrenReady.countDown()
                        }

                        override fun onChildrenLoaded(
                            parentId: String,
                            children: MutableList<MediaBrowserCompat.MediaItem>,
                            options: Bundle,
                        ) {
                            childrenCount = children.size
                            childrenReady.countDown()
                        }

                        override fun onError(parentId: String) {
                            childrenReady.countDown()
                        }
                    }
                runOnMainSync { browser.subscribe(browser.root, callback) }
                try {
                    check(childrenReady.await(UI_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                        "MediaBrowser first page timed out"
                    }
                    check(childrenCount >= 0) { "MediaBrowser first page returned an error" }
                } finally {
                    runOnMainSync { browser.unsubscribe(browser.root, callback) }
                }
            }
        }
    }
"""
critical = critical[:early_start] + early_replacement + critical[early_end:]

controller_start = critical.index("    private inline fun <T> withMediaController")
controller_end = critical.index("\n    private fun runOnMainSync", controller_start)
controller_replacement = """    private inline fun <T> withMediaController(block: (MediaControllerCompat) -> T): T =
        withConnectedBrowser(
            connectionFailureMessage = "MediaController connection failed",
            disconnectedMessage = "MediaBrowser disconnected before controller creation",
        ) { browser ->
            val context = InstrumentationRegistry.getInstrumentation().context
            val controller = mainThreadValue {
                MediaControllerCompat(context, browser.sessionToken)
            }
            block(controller)
        }

    private inline fun <T> withConnectedBrowser(
        connectionFailureMessage: String,
        disconnectedMessage: String,
        block: (MediaBrowserCompat) -> T,
    ): T {
        val context = InstrumentationRegistry.getInstrumentation().context
        val connected = CountDownLatch(1)
        val failed = CountDownLatch(1)
        val browser = mainThreadValue {
            MediaBrowserCompat(
                context,
                serviceComponent(),
                object : MediaBrowserCompat.ConnectionCallback() {
                    override fun onConnected() {
                        connected.countDown()
                    }

                    override fun onConnectionFailed() {
                        failed.countDown()
                    }

                    override fun onConnectionSuspended() {
                        failed.countDown()
                    }
                },
                null,
            )
        }
        runOnMainSync { browser.connect() }
        try {
            check(connected.await(UI_TIMEOUT_MS, TimeUnit.MILLISECONDS) && failed.count == 1L) {
                connectionFailureMessage
            }
            check(browser.isConnected) { disconnectedMessage }
            return block(browser)
        } finally {
            runOnMainSync { browser.disconnect() }
        }
    }
"""
critical = critical[:controller_start] + controller_replacement + critical[controller_end:]
critical_path.write_text(critical, encoding="utf-8", newline="\n")

fixture = Path(
    "startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt"
).read_text(encoding="utf-8")
if "const val SCHEMA_VERSION = 3" not in fixture:
    raise SystemExit("Benchmark fixture schema is not version 3")

checks = {
    "scripts/check-startup-performance-contracts.sh": "const val SCHEMA_VERSION = 3",
    ".github/workflows/startup-benchmarks.yml": "fixtureSchema=3",
    ".github/workflows/startup-evidence.yml": "fixtureSchema=3",
}
for path, marker in checks.items():
    if marker not in Path(path).read_text(encoding="utf-8"):
        raise SystemExit(f"{path}: schema marker is not aligned")
