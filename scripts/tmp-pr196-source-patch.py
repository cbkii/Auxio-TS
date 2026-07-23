#!/usr/bin/env python3
"""Temporary helper to apply the PR #196 LocationsDialog patch exactly once."""

from pathlib import Path

path = Path("app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt")
text = path.read_text()


def replace_once(old: str, new: str) -> None:
    global text
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"expected one match, found {count}: {old[:80]!r}")
    text = text.replace(old, new, 1)


replace_once(
    "    private var permissionGrantedInSession = false\n",
    "    private var permissionGrantedInSession = false\n"
    "    private var pendingPermissionRetry: (() -> Unit)? = null\n",
)

replace_once(
    """                if (isGranted && !permissionGrantedInSession) {
                    permissionGrantedInSession = true
                }
                updateModeUI(binding)
                updateSaveButtonState()
""",
    """                if (isGranted && !permissionGrantedInSession) {
                    permissionGrantedInSession = true
                }
                val retry = pendingPermissionRetry
                pendingPermissionRetry = null
                updateModeUI(binding)
                updateSaveButtonState()
                if (isGranted) {
                    retry?.invoke()
                } else {
                    pendingLocationCallback = null
                }
""",
)

replace_once(
    "        binding.locationsPermsCard.setOnClickListener { requestStoragePermission() }\n",
    "        binding.locationsPermsCard.setOnClickListener {\n"
    "            showStoragePermissionExplanation()\n"
    "        }\n",
)

replace_once(
    """        locationMode = mode
        updateModeUI(binding)
        updateSaveButtonState()
""",
    """        locationMode = mode
        updateModeUI(binding)
        updateSaveButtonState()
        if (mode != LocationMode.SAF && !hasStoragePermission) {
            showStoragePermissionExplanation()
        }
""",
)

replace_once(
    """        storagePermissionLauncher = null
        pendingLocationCallback = null
""",
    """        storagePermissionLauncher = null
        pendingLocationCallback = null
        pendingPermissionRetry = null
""",
)

replace_once(
    """            if (result != ManualPathValidation.OK && result != ManualPathValidation.ROOT_BACKED) {
                L.w("Rejecting music source $path: $result")
                currentContext.showToast(result.toastRes)
                clearPendingLocationCallback(callback)
                return@launch
            }
""",
    """            when (result) {
                ManualPathValidation.PERMISSION_MISSING -> {
                    pendingPermissionRetry = {
                        validateAndAcceptPath(path, disableThirdParty, callback)
                    }
                    showStoragePermissionExplanation()
                    return@launch
                }
                ManualPathValidation.ROOT_UNAVAILABLE -> {
                    showRootConsentAndRetry {
                        validateAndAcceptPath(path, disableThirdParty, callback)
                    }
                    return@launch
                }
                ManualPathValidation.OK,
                ManualPathValidation.ROOT_BACKED -> Unit
                else -> {
                    L.w("Rejecting music source $path: $result")
                    currentContext.showToast(result.toastRes)
                    clearPendingLocationCallback(callback)
                    return@launch
                }
            }
""",
)

replace_once(
    """            val file = File(path)
            when {
                isRootBackedRawDirectPath(path, file, directTs18Path) ->
                    ManualPathValidation.ROOT_BACKED
                !file.exists() -> ManualPathValidation.MISSING
                !file.isDirectory -> ManualPathValidation.NOT_DIRECTORY
                !file.canRead() -> ManualPathValidation.UNREADABLE
                else -> ManualPathValidation.OK
            }
""",
    """            val file = File(path)
            val rawRootCandidate =
                directTs18Path && path.startsWith("/mnt/media_rw/usbdisk")
            when {
                isRootBackedRawDirectPath(path, file, directTs18Path) ->
                    ManualPathValidation.ROOT_BACKED
                rawRootCandidate && (!file.exists() || !file.isDirectory || !file.canRead()) ->
                    ManualPathValidation.ROOT_UNAVAILABLE
                !file.exists() -> ManualPathValidation.MISSING
                !file.isDirectory -> ManualPathValidation.NOT_DIRECTORY
                !file.canRead() -> ManualPathValidation.UNREADABLE
                else -> ManualPathValidation.OK
            }
""",
)

marker = """    private fun requestStoragePermission() {
"""
helpers = """    private fun showStoragePermissionExplanation() {
        if (hasStoragePermission) {
            pendingPermissionRetry?.also { retry ->
                pendingPermissionRetry = null
                retry()
            }
            return
        }
        val ctx = context ?: return
        AlertDialog.Builder(ctx)
            .setTitle(R.string.recovery_permission_title)
            .setMessage(R.string.recovery_permission_message)
            .setPositiveButton(R.string.recovery_action_grant_permission) { _, _ ->
                requestStoragePermission()
            }
            .setNegativeButton(R.string.lbl_cancel) { _, _ ->
                pendingPermissionRetry = null
                pendingLocationCallback = null
            }
            .setOnCancelListener {
                pendingPermissionRetry = null
                pendingLocationCallback = null
            }
            .show()
    }

    private fun showRootConsentAndRetry(onAvailable: () -> Unit) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            context?.showToast(R.string.recovery_root_unavailable)
            pendingLocationCallback = null
            return
        }
        val ctx = context ?: return
        AlertDialog.Builder(ctx)
            .setTitle(R.string.recovery_root_title)
            .setMessage(R.string.recovery_root_message)
            .setPositiveButton(R.string.recovery_action_enable_root) { _, _ ->
                rootGate.setUserEnabled(true)
                lifecycleScope.launch {
                    val state = withContext(Dispatchers.IO) { rootGate.probeSync() }
                    val currentContext = context ?: return@launch
                    val message =
                        when (state) {
                            RootStateHolder.State.Available -> R.string.recovery_root_granted
                            RootStateHolder.State.Denied -> R.string.recovery_root_denied
                            RootStateHolder.State.TimedOut -> R.string.recovery_root_timed_out
                            RootStateHolder.State.Unknown,
                            RootStateHolder.State.Unavailable,
                            RootStateHolder.State.UnsupportedForVariant,
                            RootStateHolder.State.DisabledByUser ->
                                R.string.recovery_root_unavailable
                        }
                    currentContext.showToast(message)
                    if (state == RootStateHolder.State.Available) {
                        onAvailable()
                    } else {
                        pendingLocationCallback = null
                    }
                }
            }
            .setNegativeButton(R.string.lbl_cancel) { _, _ -> pendingLocationCallback = null }
            .setOnCancelListener { pendingLocationCallback = null }
            .show()
    }

"""
replace_once(marker, helpers + marker)

path.write_text(text)
