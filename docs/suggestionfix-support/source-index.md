# External source index

## android-provider-element

**Title:** Android Developers: <provider> manifest element

**URL:** https://developer.android.com/guide/topics/manifest/provider-element

**Why it matters:** Supports CoverProvider exported/permission/grantUriPermissions decision-making.

**Key points:**
- android:exported controls whether other apps can use the provider.
- exported=false limits access to same UID or temporary URI grants.
- android:permission/readPermission/writePermission can restrict provider access.

## android-fgs-launch

**Title:** Android Developers: Launch a foreground service

**URL:** https://developer.android.com/develop/background-work/services/fgs/launch

**Why it matters:** Supports PlaybackServiceFragment, BootReceiver, Bluetooth receiver, and foreground-service handling.

**Key points:**
- Call startForegroundService/startService first, then ServiceCompat.startForeground() inside the service.
- Android 12+ has background foreground-service launch restrictions.
- Android 14+ checks foreground-service type permissions and throws SecurityException for missing prerequisites.

## android-fgs-service-types

**Title:** Android Developers: Foreground service types

**URL:** https://developer.android.com/develop/background-work/services/fgs/service-types

**Why it matters:** Supports mediaPlayback foreground service decisions and boot restrictions.

**Key points:**
- mediaPlayback FGS type requires FOREGROUND_SERVICE_MEDIA_PLAYBACK in the manifest.
- mediaPlayback type has no runtime prerequisites listed.
- Android 15+ target apps cannot launch a media playback FGS from BOOT_COMPLETED.

## android-bluetooth-permissions

**Title:** Android Developers: Bluetooth permissions

**URL:** https://developer.android.com/develop/connectivity/bluetooth/bt-permissions

**Why it matters:** Supports BluetoothHeadsetReceiver permission/background-start research.

**Key points:**
- Android 12+ introduces BLUETOOTH_SCAN, BLUETOOTH_ADVERTISE, and BLUETOOTH_CONNECT runtime permissions.
- Legacy BLUETOOTH and BLUETOOTH_ADMIN should generally be maxSdkVersion=30 when targeting newer devices.
- Any Bluetooth connect/state behaviour must account for runtime permission gates.

## media3-media-session-callback

**Title:** Media3: MediaSession.Callback

**URL:** https://developer.android.com/reference/androidx/media3/session/MediaSession.Callback

**Why it matters:** Supports media-button and session callback behaviour.

**Key points:**
- Media3 handles media button events internally by default.
- Apps normally do not need to override onMediaButtonEvent.
- Returning true means the app handled the event and propagation stops.

## media3-session-result

**Title:** Media3: SessionResult

**URL:** https://developer.android.com/reference/androidx/media3/session/SessionResult

**Why it matters:** Supports MediaSession query-resolution error/no-op handling.

**Key points:**
- Provides result codes such as RESULT_ERROR_BAD_VALUE and RESULT_ERROR_INVALID_STATE.
- Useful when replacing unsafe 'play all' fallback with structured failure/no-op behaviour.

## media3-shuffle-order

**Title:** Media3: ShuffleOrder

**URL:** https://developer.android.com/reference/androidx/media3/exoplayer/source/ShuffleOrder

**Why it matters:** Supports BetterShuffleOrder tests and correctness work.

**Key points:**
- cloneAndInsert inserts item indices in [insertionIndex, insertionIndex + insertionCount).
- cloneAndRemove removes item indices in [indexFrom, indexTo).
- getFirstIndex/getLastIndex/getNextIndex/getPreviousIndex return C.INDEX_UNSET when absent.

## media3-audio-processor

**Title:** Media3: AudioProcessor

**URL:** https://developer.android.com/reference/androidx/media3/common/audio/AudioProcessor

**Why it matters:** Supports ReplayGainAudioProcessor deferral/research notes.

**Key points:**
- AudioProcessor exposes configure/flush/queueInput/getOutput contracts.
- Unsupported formats are reported with UnhandledAudioFormatException.
- queueInput implementations must consume the input ByteBuffer and output direct native-order buffers.

## android-recyclerview-adapter

**Title:** AndroidX: RecyclerView.Adapter

**URL:** https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter

**Why it matters:** Supports QueueAdapter range update optimization.

**Key points:**
- Specific change events are more efficient than broad data-set changes.
- notifyItemRangeChanged marks a positional range as changed and keeps identities unchanged.
- Payloads may be dropped when views are not attached.

## android-accessibility-principles

**Title:** Android Developers: Accessibility principles

**URL:** https://developer.android.com/guide/topics/ui/accessibility/principles

**Why it matters:** Supports playback-button accessibility work.

**Key points:**
- Every UI element should have a useful, descriptive label.
- Screen readers announce labels, so labels must describe action/state clearly.

## android-documents-provider-open-tree

**Title:** Android Developers: Open files using storage access framework

**URL:** https://developer.android.com/training/data-storage/shared/documents-files

**Why it matters:** Supports LocationsDialog, SAF fallback/manual path, and source-selection research.

**Key points:**
- ACTION_OPEN_DOCUMENT_TREE grants access to selected directory and child files.
- Traversing large directory trees can hurt performance.
- Android 11+ restricts some tree selections.

## android-intent-open-document-tree

**Title:** Android Intent: ACTION_OPEN_DOCUMENT_TREE

**URL:** https://developer.android.com/reference/android/content/Intent#ACTION_OPEN_DOCUMENT_TREE

**Why it matters:** Supports picker behaviour and EXTRA_LOCAL_ONLY handling.

**Key points:**
- Allows user to pick a directory subtree from DocumentsProviders.
- EXTRA_LOCAL_ONLY requests local-file data only, when providers honour it.

## android-overlay-permission

**Title:** Android Settings: ACTION_MANAGE_OVERLAY_PERMISSION

**URL:** https://developer.android.com/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION

**Why it matters:** Supports floating-controls overlay permission notes.

**Key points:**
- Apps need SYSTEM_ALERT_WINDOW and user grant via overlay settings to draw over other apps.
- The settings activity may not exist on some devices, so code must safeguard the intent.

## android-local-unit-tests

**Title:** Android Developers: Build local unit tests

**URL:** https://developer.android.com/training/testing/local-tests

**Why it matters:** Supports tests for pure helpers, ViewModels, repositories, receivers, and algorithmic code.

**Key points:**
- Local unit tests run on the JVM and are fast, but have limited access to Android framework APIs.
- Prefer testing public APIs and using fakes/test doubles for dependencies.
- Place tests under src/test and run them with Gradle test tasks.

## cwe-78-command-injection

**Title:** CWE-78: Improper Neutralization of Special Elements used in an OS Command

**URL:** https://cwe.mitre.org/data/definitions/78.html

**Why it matters:** Supports RootStateHolder/root command API hardening.

**Key points:**
- Building an OS command from external input can allow arbitrary command execution.
- Prefer library calls or structured APIs over shell command strings.
- If inputs are unavoidable, use accept-known-good validation and least privilege.
