from __future__ import annotations

import os
import tempfile
from pathlib import Path

ROOT = Path.cwd()


class PatchError(RuntimeError):
    pass


def read(path: str) -> str:
    return (ROOT / path).read_text(encoding="utf-8")


def write(path: str, content: str) -> None:
    target = ROOT / path
    target.parent.mkdir(parents=True, exist_ok=True)
    fd, tmp_name = tempfile.mkstemp(prefix=f".{target.name}.", suffix=".tmp", dir=target.parent)
    tmp = Path(tmp_name)
    try:
        with os.fdopen(fd, "w", encoding="utf-8", newline="\n") as fh:
            fh.write(content)
            fh.flush()
            os.fsync(fh.fileno())
        os.replace(tmp, target)
    except BaseException:
        try:
            tmp.unlink(missing_ok=True)
        except OSError:
            pass
        raise


def replace_once(path: str, old: str, new: str) -> None:
    content = read(path)
    count = content.count(old)
    if count != 1:
        raise PatchError(f"{path}: expected one match, found {count}: {old[:160]!r}")
    write(path, content.replace(old, new, 1))


def require_contains(path: str, needle: str) -> None:
    if needle not in read(path):
        raise PatchError(f"{path}: required postcondition missing: {needle!r}")


def require_not_contains(path: str, needle: str) -> None:
    if needle in read(path):
        raise PatchError(f"{path}: forbidden postcondition remains: {needle!r}")


# ---------------------------------------------------------------------------
# PlaybackPanelFragment: preserve standard flavour semantics, create one
# view-lifecycle-scoped pager adapter, release partial Visualizer instances,
# and keep the historically required AS_PLAYED-first capture policy.
# ---------------------------------------------------------------------------
panel = "app/src/main/java/org/oxycblt/auxio/playback/PlaybackPanelFragment.kt"
replace_once(
    panel,
    "import android.content.Intent\n",
    "import android.content.Intent\nimport android.content.res.Configuration\n",
)
replace_once(panel, "import org.oxycblt.auxio.R\n", "import org.oxycblt.auxio.BuildConfig\nimport org.oxycblt.auxio.R\n")
replace_once(
    panel,
    '''    private val coverPagerAdapter by lazy {
        CoverPagerAdapter(this, playbackModel, uiSettings, viewLifecycleOwner)
    }
''',
    '''    private var coverPagerAdapter: CoverPagerAdapter? = null
''',
)
replace_once(
    panel,
    '''        super.onBindingCreated(binding, savedInstanceState)

        visualizerPermissionLauncher =
''',
    '''        super.onBindingCreated(binding, savedInstanceState)

        val currentCoverPagerAdapter =
            CoverPagerAdapter(this, playbackModel, uiSettings, viewLifecycleOwner)
        coverPagerAdapter = currentCoverPagerAdapter

        visualizerPermissionLauncher =
''',
)
replace_once(panel, "            adapter = coverPagerAdapter\n", "            adapter = currentCoverPagerAdapter\n")
replace_once(
    panel,
    "                        coverPagerAdapter.setActivePosition(it)\n",
    "                        currentCoverPagerAdapter.setActivePosition(it)\n",
)
replace_once(
    panel,
    '''        // The TS18 Now Playing controls have one deterministic automotive size. The removed
        // preference was ineffective because this surface is always landscape on the target unit.
        val useLargeControls = true
''',
    '''        val forceLargeLandscapeControls =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val useLargeControls =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                true
            } else {
                uiSettings.largeHeadUnitControls || forceLargeLandscapeControls
            }
''',
)
replace_once(
    panel,
    "                uiSettings.largeHeadUnitControls -> spacingSmall\n",
    "                useLargeControls -> spacingSmall\n",
)
replace_once(
    panel,
    '''        userAwarePagerCallback?.release()
        binding.playbackPager?.adapter = null
    }
''',
    '''        userAwarePagerCallback?.release()
        userAwarePagerCallback = null
        binding.playbackPager?.adapter = null
        coverPagerAdapter = null
    }
''',
)
replace_once(
    panel,
    "        coverPagerAdapter.refreshVisualizerMode()\n",
    "        coverPagerAdapter?.refreshVisualizerMode()\n",
)
replace_once(
    panel,
    "        coverPagerAdapter.setActivePosition(queue.index)\n",
    "        coverPagerAdapter?.setActivePosition(queue.index)\n",
)
replace_once(
    panel,
    '''        playbackModel.updateVisualizerState(VisualizerState.WaitingForFrames)
        try {
            val captureRange = Visualizer.getCaptureSizeRange()
''',
    '''        playbackModel.updateVisualizerState(VisualizerState.WaitingForFrames)
        var candidateToRelease: Visualizer? = null
        try {
            val captureRange = Visualizer.getCaptureSizeRange()
''',
)
replace_once(
    panel,
    '''            val candidate = Visualizer(sessionId)
            val targetSize = 512.coerceIn(captureRange[0], captureRange[1])
''',
    '''            val candidate = Visualizer(sessionId)
            candidateToRelease = candidate
            val targetSize = 512.coerceIn(captureRange[0], captureRange[1])
''',
)
replace_once(
    panel,
    '''            val scalingMode =
                if (visualizerRetryCount == 0) Visualizer.SCALING_MODE_NORMALIZED
                else Visualizer.SCALING_MODE_AS_PLAYED
''',
    '''            val scalingMode =
                if (visualizerRetryCount == 0) Visualizer.SCALING_MODE_AS_PLAYED
                else Visualizer.SCALING_MODE_NORMALIZED
''',
)
replace_once(
    panel,
    '''                try {
                    candidate.release()
                } catch (e: RuntimeException) {
                    L.d(e, "Visualizer native release failed during partial initialization")
                }
                return
''',
    '''                return
''',
)
replace_once(
    panel,
    '''            visualizer = candidate
            visualizerSessionId = sessionId
''',
    '''            visualizer = candidate
            candidateToRelease = null
            visualizerSessionId = sessionId
''',
)
replace_once(
    panel,
    '''            playbackModel.updateVisualizerState(VisualizerState.Failed(message))
        }
    }

    private fun scheduleVisualizerWatchdog''',
    '''            playbackModel.updateVisualizerState(VisualizerState.Failed(message))
        } finally {
            candidateToRelease?.let { candidate ->
                try {
                    if (candidate.enabled) candidate.enabled = false
                } catch (e: RuntimeException) {
                    L.d(e, "Visualizer partial candidate disable failed")
                }
                try {
                    candidate.setDataCaptureListener(null, 0, false, false)
                } catch (e: RuntimeException) {
                    L.d(e, "Visualizer partial candidate listener cleanup failed")
                }
                try {
                    candidate.release()
                } catch (e: RuntimeException) {
                    L.d(e, "Visualizer partial candidate release failed")
                }
            }
        }
    }

    private fun scheduleVisualizerWatchdog''',
)

# ---------------------------------------------------------------------------
# Topway fixed music entry: explicit permission flow, immediate overlay start,
# and an allowlisted full-player intent instead of external intent forwarding.
# ---------------------------------------------------------------------------
entry = "app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/TopwayMusicEntryActivity.kt"
replace_once(
    entry,
    '''                startActivity(
                    Intent(intent)
                        .setClass(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                )
''',
    '''                val fullPlayerIntent =
                    Intent(this, MainActivity::class.java).apply {
                        action = intent.action
                        if (intent.data != null || intent.type != null) {
                            setDataAndType(intent.data, intent.type)
                        }
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        if (intent.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0) {
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                    }
                startActivity(fullPlayerIntent)
''',
)

# ---------------------------------------------------------------------------
# Manifest: preserve PR #164's dedicated user-facing Floating Controls launcher
# while retaining the fixed DoFun alias router. Package-level ResolverActivity
# is therefore expected and must not be used as the integration decision path.
# ---------------------------------------------------------------------------
manifest = "app/src/topwayCompat/AndroidManifest.xml"
replace_once(
    manifest,
    '''        <activity
            android:name="org.oxycblt.auxio.car.overlay.CarOverlayActivity"
            android:exported="false"
            android:excludeFromRecents="true"
            android:theme="@android:style/Theme.NoDisplay"
            android:label="@string/car_overlay_setting_title"
            android:icon="@mipmap/ic_launcher" />
''',
    '''        <activity
            android:name="org.oxycblt.auxio.car.overlay.CarOverlayActivity"
            android:exported="true"
            android:excludeFromRecents="true"
            android:theme="@android:style/Theme.NoDisplay"
            android:label="@string/car_overlay_setting_title"
            android:icon="@mipmap/ic_launcher">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
''',
)

# Re-expose the supported hide-over-Auxio preference with the corrected opt-in default.
prefs_xml = "app/src/topwayCompat/res/xml/preferences_car.xml"
replace_once(
    prefs_xml,
    '''        <SwitchPreferenceCompat
            app:defaultValue="false"
            app:key="car_overlay_enabled"
            app:summary="@string/car_overlay_setting_summary"
            app:title="@string/car_overlay_setting_title" />
        <org.oxycblt.auxio.settings.ui.IntListPreference
''',
    '''        <SwitchPreferenceCompat
            app:defaultValue="false"
            app:key="car_overlay_enabled"
            app:summary="@string/car_overlay_setting_summary"
            app:title="@string/car_overlay_setting_title" />
        <SwitchPreferenceCompat
            app:defaultValue="false"
            app:key="car_overlay_hide_auxio_fg"
            app:summary="@string/car_overlay_hide_auxio_fg_desc"
            app:title="@string/car_overlay_hide_auxio_fg"
            app:dependency="car_overlay_enabled" />
        <org.oxycblt.auxio.settings.ui.IntListPreference
''',
)

# ---------------------------------------------------------------------------
# EQ: retain the prior router-first contract, then use exact-device DSPActivity
# as the proven fallback when the router is disabled, then conservative fallbacks.
# ---------------------------------------------------------------------------
eq_launcher = "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayEqualizerLauncher.kt"
replace_once(
    eq_launcher,
    '''        listOf(
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.DSPActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
''',
    '''        listOf(
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
            NativeTarget(
                ComponentName("com.tw.eq", "com.tw.eq.DSPActivity"),
                Intent.CATEGORY_LAUNCHER,
            ),
''',
)
replace_once(
    eq_launcher,
    '''    /**
     * Exact-device evidence for HEAD.20241126 shows DSPActivity enabled, EQChoiceActivity disabled,
     * and EQActivity registered with DEFAULT rather than LAUNCHER.
     */
''',
    '''    /**
     * Preserve the stock router-first contract when EQChoiceActivity is enabled. Exact-device
     * HEAD.20241126 evidence shows that router disabled and DSPActivity enabled, so DSPActivity is
     * the first fallback; EQActivity is registered with DEFAULT rather than LAUNCHER.
     */
''',
)

# Strengthen EQ ordering tests.
eq_test = "app/src/topwayCompatTest/java/org/oxycblt/auxio/headunit/topway/TopwayEqualizerExactDeviceTest.kt"
replace_once(
    eq_test,
    '''    @Test
    fun exactDeviceDspActivityWinsWhenChoiceActivityIsUnavailable() {
''',
    '''    @Test
    fun stockChoiceRouterWinsWhenAvailable() {
        val resolver = Resolver(setOf(EQ_CHOICE_ACTIVITY, DSP_ACTIVITY))
        val intent = TopwayEqualizerLauncher.resolveIntent(context, 123, resolver)
        assertEquals(EQ_CHOICE_ACTIVITY, intent?.component)
        assertTrue(intent?.categories?.contains(Intent.CATEGORY_LAUNCHER) == true)
    }

    @Test
    fun exactDeviceDspActivityWinsWhenChoiceActivityIsUnavailable() {
''',
)
replace_once(
    eq_test,
    '''    private companion object {
        val DSP_ACTIVITY = ComponentName("com.tw.eq", "com.tw.eq.DSPActivity")
''',
    '''    private companion object {
        val EQ_CHOICE_ACTIVITY = ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity")
        val DSP_ACTIVITY = ComponentName("com.tw.eq", "com.tw.eq.DSPActivity")
''',
)

# ---------------------------------------------------------------------------
# DoFun diagnostics: probe both supported package identities and avoid claiming
# launcher selection or coexistence safety from package presence alone.
# ---------------------------------------------------------------------------
diag = "app/src/main/java/org/oxycblt/auxio/headunit/root/dofun/Ts18DofunIntegrationResolver.kt"
replace_once(
    diag,
    '''    ResolveMusicComponents(
        "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN com.tw.media"
    ),
    ResolveTopwayAlias(
        "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN -n com.tw.media/com.tw.music.MusicActivity"
    ),
    OverlayRuntime(
        "appops get com.tw.media SYSTEM_ALERT_WINDOW 2>&1; dumpsys activity services com.tw.media 2>&1 | head -n 160; dumpsys window windows 2>&1 | grep -E 'com.tw.media|CarFloatingControls' | head -n 80"
    ),
''',
    '''    ResolveMusicComponents(
        "for pkg in com.tw.media com.tw.music; do echo ===$pkg===; cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN $pkg 2>&1; done"
    ),
    ResolveTopwayAlias(
        "for component in com.tw.media/com.tw.music.MusicActivity com.tw.music/com.tw.music.MusicActivity; do echo ===$component===; cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN -n $component 2>&1; done"
    ),
    OverlayRuntime(
        "for pkg in com.tw.media com.tw.music; do echo ===$pkg===; appops get $pkg SYSTEM_ALERT_WINDOW 2>&1; dumpsys activity services $pkg 2>&1 | head -n 160; done; dumpsys window windows 2>&1 | grep -E 'com.tw.media|com.tw.music|CarFloatingControls' | head -n 120"
    ),
''',
)
replace_once(
    diag,
    '''    VisualizerEffects(
        "dumpsys media.audio_flinger 2>&1 | grep -i -E 'visualizer|session|com.tw.media' | head -n 200"
    ),
''',
    '''    VisualizerEffects(
        "dumpsys media.audio_flinger 2>&1 | grep -i -E 'visualizer|session|com.tw.media|com.tw.music' | head -n 240"
    ),
''',
)
replace_once(
    diag,
    '''                Playback Resume Classification:
                - Activity launch resume requires: autoplayOnLaunch && first cold resume
''',
    '''                Playback Resume Classification:
                - Package-presence and package-level resolver output do not prove DoFun selection.
                - Two deliberate launcher entries may produce ResolverActivity; validate explicit fixed aliases.
                - Activity launch resume requires: autoplayOnLaunch && first cold resume
''',
)
replace_once(
    diag,
    '''                    Ts18DofunDetectedPath.AuxioTwMediaWithStockCoexisting ->
                        "Stock com.tw.music and Auxio com.tw.media can safely coexist. Package presence alone does not prove DoFun preference; do not disable stock unless a bounded reversible component-selection test requires it."
''',
    '''                    Ts18DofunDetectedPath.AuxioTwMediaWithStockCoexisting ->
                        "Stock com.tw.music and Auxio com.tw.media are co-installed. Presence alone proves neither DoFun preference nor conflict-free runtime behaviour; validate the explicit alias, session, widget and overlay probes before any bounded reversible selection test."
''',
)
replace_once(
    diag,
    '''                    Ts18DofunDetectedPath.AuxioTwMediaSelected ->
                        "Exact com.tw.media identity is present. Verify the fixed alias, overlay runtime, widget and media-session probes."
''',
    '''                    Ts18DofunDetectedPath.AuxioTwMediaSelected ->
                        "Only the supported com.tw.media identity was detected by package presence. Verify the fixed alias, overlay runtime, widget and media-session probes; this is not selection proof."
''',
)
replace_once(
    diag,
    '''                    Ts18DofunDetectedPath.StockTwMusicSelected ->
                        "Install topwayTwMediaRelease or the systemless topwayTwMusic module; do not mutate stock solely from this check."
''',
    '''                    Ts18DofunDetectedPath.StockTwMusicSelected ->
                        "Only com.tw.music was detected by package presence. Determine whether it is stock or the systemless Auxio identity before acting; do not mutate it solely from this check."
''',
)

# ---------------------------------------------------------------------------
# Guardrails: parse the specific alias and exact launcher set in source/merged
# manifests and in apkanalyzer output. Broad string presence is not proof.
# ---------------------------------------------------------------------------
guard = "scripts/check-dofun-topway-compat.sh"
replace_once(
    guard,
    '''require_manifest_dump_contains() {
  local manifest_dump="$1"
  local pattern="$2"
  local pass_message="$3"
  local fail_message="$4"

  if grep -Fq -- "$pattern" "$manifest_dump"; then
    pass "$pass_message"
  else
    fail "$fail_message"
  fi
}
''',
    '''require_manifest_dump_contains() {
  local manifest_dump="$1"
  local pattern="$2"
  local pass_message="$3"
  local fail_message="$4"

  if grep -Fq -- "$pattern" "$manifest_dump"; then
    pass "$pass_message"
  else
    fail "$fail_message"
  fi
}

require_topway_alias_mapping() {
  local manifest_dump="$1"
  local label="$2"
  if python3 - "$manifest_dump" <<'PY_ALIAS'
import sys
import xml.etree.ElementTree as ET

ANDROID = "{http://schemas.android.com/apk/res/android}"
root = ET.parse(sys.argv[1]).getroot()
app = root.find("application")
aliases = [] if app is None else app.findall("activity-alias")
matches = [alias for alias in aliases if alias.attrib.get(ANDROID + "name") == "com.tw.music.MusicActivity"]
if len(matches) != 1:
    raise SystemExit(1)
alias = matches[0]
if alias.attrib.get(ANDROID + "targetActivity") != "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity":
    raise SystemExit(2)
actions = set()
categories = set()
for intent_filter in alias.findall("intent-filter"):
    actions.update(el.attrib.get(ANDROID + "name") for el in intent_filter.findall("action"))
    categories.update(el.attrib.get(ANDROID + "name") for el in intent_filter.findall("category"))
required_actions = {"android.intent.action.MAIN", "android.intent.action.MUSIC_PLAYER", "android.intent.action.VIEW"}
required_categories = {"android.intent.category.DEFAULT", "android.intent.category.LAUNCHER", "android.intent.category.APP_MUSIC", "android.intent.category.BROWSABLE"}
if not required_actions.issubset(actions) or not required_categories.issubset(categories):
    raise SystemExit(3)
launcher_names = set()
for tag in ("activity", "activity-alias"):
    for component in app.findall(tag):
        for intent_filter in component.findall("intent-filter"):
            filter_actions = {el.attrib.get(ANDROID + "name") for el in intent_filter.findall("action")}
            filter_categories = {el.attrib.get(ANDROID + "name") for el in intent_filter.findall("category")}
            if "android.intent.action.MAIN" in filter_actions and "android.intent.category.LAUNCHER" in filter_categories:
                launcher_names.add(component.attrib.get(ANDROID + "name"))
expected_launchers = {"com.tw.music.MusicActivity", "org.oxycblt.auxio.car.overlay.CarOverlayActivity"}
if launcher_names != expected_launchers:
    raise SystemExit(4)
PY_ALIAS
  then
    pass "${label} APK alias and deliberate two-entry launcher contract are valid"
  else
    fail "${label} APK alias mapping/filter contract is invalid"
  fi
}

require_source_topway_manifest_contract() {
  local source_manifest="$1"
  if python3 - "$source_manifest" <<'PY_SOURCE'
import sys
import xml.etree.ElementTree as ET

ANDROID = "{http://schemas.android.com/apk/res/android}"
root = ET.parse(sys.argv[1]).getroot()
app = root.find("application")
if app is None:
    raise SystemExit(1)

def attr(element, name):
    return element.attrib.get(ANDROID + name)

def has_filter(element, action, category):
    for intent_filter in element.findall("intent-filter"):
        actions = {attr(el, "name") for el in intent_filter.findall("action")}
        categories = {attr(el, "name") for el in intent_filter.findall("category")}
        if action in actions and category in categories:
            return True
    return False

aliases = [a for a in app.findall("activity-alias") if attr(a, "name") == "com.tw.music.MusicActivity"]
if len(aliases) != 1 or attr(aliases[0], "targetActivity") != "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity":
    raise SystemExit(2)
entries = [a for a in app.findall("activity") if attr(a, "name") == "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity"]
if len(entries) != 1 or attr(entries[0], "exported") != "false" or entries[0].findall("intent-filter"):
    raise SystemExit(3)
launchers = [a for a in app.findall("activity") if attr(a, "name") == "org.oxycblt.auxio.car.overlay.CarOverlayActivity"]
if len(launchers) != 1 or attr(launchers[0], "exported") != "true":
    raise SystemExit(4)
if not has_filter(launchers[0], "android.intent.action.MAIN", "android.intent.category.LAUNCHER"):
    raise SystemExit(5)
PY_SOURCE
  then
    pass "source manifest has parsed Topway alias router and dedicated Floating Controls launcher contract"
  else
    fail "source manifest Topway alias/router/launcher contract is invalid"
  fi
}
''',
)
replace_once(
    guard,
    '''      require_manifest_dump_contains "$manifest_dump" 'android:name="com.tw.music.MusicActivity"' "${label} APK manifest has com.tw.music.MusicActivity" "${label} APK manifest lacks com.tw.music.MusicActivity"
      require_manifest_dump_contains "$manifest_dump" 'android:targetActivity="org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity"' "${label} APK alias targets the Topway entry router" "${label} APK alias target mismatch"
      require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.action.MAIN"' "${label} APK alias has MAIN action" "${label} APK alias lacks MAIN action"
      require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.action.MUSIC_PLAYER"' "${label} APK alias has MUSIC_PLAYER action" "${label} APK alias lacks MUSIC_PLAYER action"
      require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.category.DEFAULT"' "${label} APK alias has DEFAULT category" "${label} APK alias lacks DEFAULT category"
      require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.category.LAUNCHER"' "${label} APK alias has LAUNCHER category" "${label} APK alias lacks LAUNCHER category"
      require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.category.APP_MUSIC"' "${label} APK alias has APP_MUSIC category" "${label} APK alias lacks APP_MUSIC category"
''',
    '''      require_topway_alias_mapping "$manifest_dump" "$label"
''',
)
replace_once(
    guard,
    '''  pass "found flavour manifest: ${flavour_manifest}"
  require_file_contains "$flavour_manifest" "com.tw.music.MusicActivity" "Topway activity alias"
  require_file_contains "$flavour_manifest" "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity" "Topway alias router target"
require_file_contains "$flavour_manifest" "org.oxycblt.auxio.MainActivity" "Topway full-player activity"
''',
    '''  pass "found flavour manifest: ${flavour_manifest}"
  require_source_topway_manifest_contract "$flavour_manifest"
  require_file_contains "$flavour_manifest" "com.tw.music.MusicActivity" "Topway activity alias"
  require_file_contains "$flavour_manifest" "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity" "Topway alias router target"
  require_file_contains "$flavour_manifest" "org.oxycblt.auxio.car.overlay.CarOverlayActivity" "dedicated floating-controls launcher"
  require_file_contains "$flavour_manifest" "org.oxycblt.auxio.MainActivity" "Topway full-player activity"
''',
)
replace_once(
    guard,
    '''def require_single_launcher(application, expected_name, label):
    matches = components_with_filter(application, "activity", "android.intent.action.MAIN", "android.intent.category.LAUNCHER")
    matches += components_with_filter(application, "activity-alias", "android.intent.action.MAIN", "android.intent.category.LAUNCHER")
    names = [attr(component, "name") for component in matches]
    if names == [expected_name]:
        ok(f"{label} single MAIN/LAUNCHER entry is {expected_name}")
    else:
        fail(f"{label} MAIN/LAUNCHER entries expected [{expected_name}], got {names}")
''',
    '''def require_launcher_set(application, expected_names, label):
    matches = components_with_filter(application, "activity", "android.intent.action.MAIN", "android.intent.category.LAUNCHER")
    matches += components_with_filter(application, "activity-alias", "android.intent.action.MAIN", "android.intent.category.LAUNCHER")
    names = {attr(component, "name") for component in matches}
    if names == set(expected_names):
        ok(f"{label} MAIN/LAUNCHER entries are {sorted(names)}")
    else:
        fail(f"{label} MAIN/LAUNCHER entries expected {sorted(expected_names)}, got {sorted(names)}")
''',
)
replace_once(
    guard,
    '''    if attr(alias, "targetActivity") == "org.oxycblt.auxio.MainActivity":
        ok(f"{label} alias targets org.oxycblt.auxio.MainActivity")
''',
    '''    if attr(alias, "targetActivity") == "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity":
        ok(f"{label} alias targets TopwayMusicEntryActivity")
''',
)
insert_anchor = '''def require_topway_main_activity_minimized(application, label):
'''
launcher_check = '''def require_topway_entry_activities(application, label):
    entries = [activity for activity in application.findall("activity") if attr(activity, "name") == "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity"]
    if len(entries) != 1:
        fail(f"{label} expected one TopwayMusicEntryActivity, got {len(entries)}")
    else:
        entry = entries[0]
        if attr(entry, "exported") == "false" and not entry.findall("intent-filter"):
            ok(f"{label} TopwayMusicEntryActivity is internal and filter-free")
        else:
            fail(f"{label} TopwayMusicEntryActivity exposure/filter contract is invalid")

    launchers = [activity for activity in application.findall("activity") if attr(activity, "name") == "org.oxycblt.auxio.car.overlay.CarOverlayActivity"]
    if len(launchers) != 1:
        fail(f"{label} expected one CarOverlayActivity, got {len(launchers)}")
    else:
        launcher = launchers[0]
        launcher_matches = components_with_filter(application, "activity", "android.intent.action.MAIN", "android.intent.category.LAUNCHER")
        if attr(launcher, "exported") == "true" and launcher in launcher_matches:
            ok(f"{label} preserves the dedicated exported Floating Controls launcher")
        else:
            fail(f"{label} dedicated Floating Controls launcher is not exported MAIN/LAUNCHER")


'''
replace_once(guard, insert_anchor, launcher_check + insert_anchor)
replace_once(guard, 'require_single_launcher(standard_app, "org.oxycblt.auxio.MainActivity", "standardDebug")', 'require_launcher_set(standard_app, {"org.oxycblt.auxio.MainActivity"}, "standardDebug")')
for variant in ["topwayTwMusicDebug", "topwayTwMusicRelease", "topwayTwMediaDebug", "topwayTwMediaRelease"]:
    replace_once(
        guard,
        f'require_single_launcher({"topway_debug_app" if variant == "topwayTwMusicDebug" else "topway_release_app" if variant == "topwayTwMusicRelease" else "topway_media_debug_app" if variant == "topwayTwMediaDebug" else "topway_media_release_app"}, "com.tw.music.MusicActivity", "{variant}")',
        f'require_launcher_set({"topway_debug_app" if variant == "topwayTwMusicDebug" else "topway_release_app" if variant == "topwayTwMusicRelease" else "topway_media_debug_app" if variant == "topwayTwMediaDebug" else "topway_media_release_app"}, {{"com.tw.music.MusicActivity", "org.oxycblt.auxio.car.overlay.CarOverlayActivity"}}, "{variant}")',
    )
    app_name = "topway_debug_app" if variant == "topwayTwMusicDebug" else "topway_release_app" if variant == "topwayTwMusicRelease" else "topway_media_debug_app" if variant == "topwayTwMediaDebug" else "topway_media_release_app"
    anchor = f'require_topway_alias({app_name}, "{variant}"' + (", debug=True)" if "Debug" in variant else ")")
    replace_once(guard, anchor, anchor + f'\nrequire_topway_entry_activities({app_name}, "{variant}")')

# Strengthen the independent component guard to include the dedicated launcher.
component_guard = "scripts/check-topway-manifest-components.sh"
replace_once(
    component_guard,
    '''    "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity",
    "org.oxycblt.auxio.car.overlay.CarFloatingControlsService",
''',
    '''    "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity",
    "org.oxycblt.auxio.car.overlay.CarOverlayActivity",
    "org.oxycblt.auxio.car.overlay.CarFloatingControlsService",
''',
)

# ---------------------------------------------------------------------------
# Audit documentation: record preservation decisions and validation boundary.
# ---------------------------------------------------------------------------
doc = "docs/TS18_V605_RUNTIME_REGRESSION_FIXES.md"
write(
    doc,
    '''# TS18 v6.0.5 runtime regression fixes

## Exact-device observations

**Observed** on `s9863a1h10_Natv`, Android 10/API 29, firmware
`TS18.2.2_20241210.165912_WINDOW-THEME1`, using the `com.tw.media` release:

- all Now Playing visualizer modes produced no visible visualizer;
- floating controls required a settings off/on toggle and did not remain persistent;
- floating-only startup still exposed the full player;
- the Large touch controls row had no useful effect;
- the Now Playing EQ action did not open stock EQ.

The in-app DoFun check also showed both `com.tw.media` and stock `com.tw.music` installed, an active
`com.tw.media` MediaSession, and package-level MAIN/LAUNCHER resolution returning ResolverActivity.
Package presence and package-level resolution do not prove DoFun selection. ResolverActivity is also
an expected result when the Topway build deliberately exposes both the full music alias and the
separate Floating Controls launcher; all integration decisions use explicit components.

## Preserved requirements from earlier work

The regression fix is additive and must not weaken the contracts established by the earlier
visualizer, floating-controls, EQ, DoFun and automotive-UI work:

- one canonical Auxio playback, MediaSession, notification and audio-focus stack;
- Visualizer capture only from Auxio's current non-zero audio session, with real FFT/waveform data,
  no session-0 capture and no synthetic idle animation;
- `Hidden`, `WaitingForFrames`, `Live` and `Failed` visualizer states, one active cover consumer,
  bounded 512-byte/30 Hz capture, lifecycle release and one bounded alternate-scaling retry;
- the historical `SCALING_MODE_AS_PLAYED` first attempt remains authoritative, with normalized mode
  used only for the bounded retry;
- the dedicated Floating Controls launcher remains user-visible, while the fixed
  `com.tw.music.MusicActivity` alias routes DoFun MAIN/MUSIC_PLAYER launches according to the
  floating-only preference;
- overlay permission is explicit, restore is persistent across process/service recreation, retries
  are bounded, drag positions are clamped, and triple-tap stop disables the feature;
- `Hide while Auxio is foreground` remains an available opt-in setting, now defaulting off;
- the stock EQ router `com.tw.eq/.EQChoiceActivity` remains first when enabled; exact-device
  `com.tw.eq/.DSPActivity` is the proven fallback when the router is disabled, followed by
  `.EQActivity`, package and Android AudioEffect fallbacks;
- standard, `topwayTwMedia` and `topwayTwMusic` identities remain isolated, and standard-device UI
  preferences are not overridden by Topway-only sizing decisions;
- no platform signing, UID 1000, private vendor API or protected-service capability is claimed.

## Implemented

- Visualizer captures FFT and waveform, rejects stale frames, reacts immediately to preference and
  session changes, dispatches only to the active cover page, creates a fresh adapter per view
  lifecycle, and transactionally releases partially constructed native effects.
- Floating controls default to persistent visibility; sticky restart promotes before suppression;
  application startup establishes the service; window attach has two bounded retries; the fixed
  Topway entry requests permission when needed and starts the overlay immediately when permitted.
- Both deliberate launcher entries are preserved. Full-player routing constructs a clean explicit
  intent and forwards only action, data/type and a read-URI grant rather than arbitrary extras or
  flags.
- The library playback banner has dedicated approximately 85% dimensions while full Now Playing
  dimensions remain unchanged. The ineffective Topway Large touch controls row is removed, but the
  shared standard flavour keeps its original preference/orientation behaviour.
- EQ routing is router-first and exact-device-aware. Launch-time failure advances to the next safe
  candidate without interrupting playback.
- DoFun diagnostics probe both supported package identities, treat coexistence as an observation
  rather than proof of safety or preference, and validate explicit aliases, sessions, widgets,
  overlays, EQ components and visualizer effects.
- Source, merged-manifest and built-APK guardrails parse the specific alias mapping and require the
  intended two-entry Topway launcher set instead of relying on broad string presence.

## Requires TS18 validation

Automated checks cannot prove OEM audio-effect, WindowManager, DoFun or ACC behaviour. Validate the
`topwayTwMediaRelease` artifact on the exact unit:

1. Grant RECORD_AUDIO and overlay permission.
2. Test visualizer Off/Fallback/Always with artwork and without artwork; play, pause, skip, leave and
   return to Now Playing, recreate the activity/view, kill the process, then perform ACC sleep/wake.
   Confirm changing non-zero session IDs recover without stale frames or native-effect leaks.
3. Enable floating controls once. Move between Auxio, DoFun and at least two other apps; use both
   launcher icons; restart the launcher, kill Auxio's process, screen off/on, reboot and ACC
   sleep/wake. Confirm one foreground service, one notification and one overlay window.
4. Revoke overlay permission and confirm controlled stop/no retry loop. Re-grant, use the dedicated
   launcher and confirm permission/enable/start state is coherent.
5. Select floating-only startup and confirm the explicit DoFun music alias attaches the overlay
   without showing MainActivity. ACTION_VIEW and the overlay app button must open the full player.
6. Confirm the library banner is approximately 15% smaller while Now Playing controls are unchanged.
   Install a standard build separately and confirm its Large controls preference still works.
7. Tap EQ with the stock router enabled where possible; otherwise confirm exact-device
   `com.tw.eq/.DSPActivity` becomes resumed while playback remains healthy.
8. Capture `dumpsys media_session`, services, notifications, windows, package alias resolution and
   DoFun widget state before and after launcher restart, process death and ACC sleep/wake.

Do not claim physical success until this matrix is completed on the exact device.
''',
)

# ---------------------------------------------------------------------------
# Postconditions protecting the audited requirements.
# ---------------------------------------------------------------------------
require_contains(panel, "private var coverPagerAdapter: CoverPagerAdapter? = null")
require_contains(panel, "if (BuildConfig.TOPWAY_COMPAT_FLAVOR)")
require_contains(panel, "Visualizer.SCALING_MODE_AS_PLAYED")
require_contains(panel, "candidateToRelease?.let")
require_not_contains(panel, "val useLargeControls = true")
require_contains(entry, "CarOverlaySettings.setEnabled(this, true)")
require_not_contains(entry, "Intent(intent)")
require_contains(manifest, 'android:name="org.oxycblt.auxio.car.overlay.CarOverlayActivity"')
require_contains(manifest, 'android:targetActivity="org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity"')
require_contains(prefs_xml, 'app:key="car_overlay_hide_auxio_fg"')
require_contains(eq_launcher, 'ComponentName("com.tw.eq", "com.tw.eq.EQChoiceActivity")')
require_contains(diag, "Presence alone proves neither DoFun preference nor conflict-free runtime behaviour")
require_contains(guard, "require_topway_alias_mapping")
require_contains(guard, "require_source_topway_manifest_contract")
require_contains(guard, "require_topway_entry_activities")
require_contains(component_guard, '"org.oxycblt.auxio.car.overlay.CarOverlayActivity"')

print("PR #169 audited regression-preservation patch applied successfully")
