/*
 * Copyright (c) 2024 Auxio Project
 * LocationsDialog.kt is part of Auxio.
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

package org.oxycblt.auxio.music.locations

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import android.provider.MediaStore.Audio.Media as AndroidAudioMedia
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.R as MR
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.databinding.DialogMusicLocationsBinding
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.ui.ViewBindingMaterialDialogFragment
import org.oxycblt.auxio.util.getAttrColorCompat
import org.oxycblt.auxio.util.showToast
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.fs.mediastore.MediaStore
import org.oxycblt.musikr.fs.saf.SAF
import timber.log.Timber as L

@AndroidEntryPoint
class LocationsDialog : ViewBindingMaterialDialogFragment<DialogMusicLocationsBinding>() {

    private val includeLocationListener =
        object : LocationAdapter.Listener {
            override fun onRemoveLocation(location: Location) {
                includeLocationAdapter.remove(location as Location.Opened)
                updateSaveButtonState()
            }
        }

    private val excludeLocationListener =
        object : LocationAdapter.Listener {
            override fun onRemoveLocation(location: Location) {
                excludeLocationAdapter.remove(location as Location.Unopened)
                updateSaveButtonState()
            }
        }

    private val filterLocationListener =
        object : LocationAdapter.Listener {
            override fun onRemoveLocation(location: Location) {
                filterLocationAdapter.remove(location as Location.Unopened)
                updateSaveButtonState()
            }
        }

    private val includeLocationAdapter: LocationAdapter<Location.Opened> =
        LocationAdapter(includeLocationListener)
    private val excludeLocationAdapter: LocationAdapter<Location.Unopened> =
        LocationAdapter(excludeLocationListener)
    private val filterLocationAdapter: LocationAdapter<Location.Unopened> =
        LocationAdapter(filterLocationListener)
    private var openDocumentTreeLauncher: ActivityResultLauncher<Uri?>? = null
    private var localOnlyOpenDocumentTreeLauncher: ActivityResultLauncher<Uri?>? = null
    private var storagePermissionLauncher: ActivityResultLauncher<String>? = null
    @Inject lateinit var musicSettings: MusicSettings
    @Inject lateinit var rootGate: RootStateHolder

    private var locationMode = LocationMode.SAF
    private var isIncludeMode = true
    private var hasStoragePermission = false
    private var isExtrasExpanded = false
    private var pendingLocationCallback: ((Location.Unopened) -> Unit)? = null
    private var permissionGrantedInSession = false

    override fun onCreateBinding(inflater: LayoutInflater) =
        DialogMusicLocationsBinding.inflate(inflater)

    override fun onConfigDialog(builder: AlertDialog.Builder) {
        builder
            .setTitle(R.string.set_locations)
            .setNegativeButton(R.string.lbl_cancel, null)
            .setPositiveButton(R.string.lbl_save) { _, _ -> saveChanges() }
    }

    override fun onBindingCreated(
        binding: DialogMusicLocationsBinding,
        savedInstanceState: Bundle?,
    ) {
        openDocumentTreeLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
                addDocumentTreeUriToDirs(uri, false)
            }

        localOnlyOpenDocumentTreeLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
                addDocumentTreeUriToDirs(uri, true)
            }

        storagePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                L.d("Storage permission granted: $isGranted")
                hasStoragePermission = isGranted
                if (isGranted && !permissionGrantedInSession) {
                    permissionGrantedInSession = true
                }
                updateModeUI(binding)
                updateSaveButtonState()
            }

        binding.locationsIncludeRecycler.apply {
            adapter = includeLocationAdapter
            itemAnimator = null
        }

        binding.locationsExcludeRecycler.apply {
            adapter = excludeLocationAdapter
            itemAnimator = null
        }

        binding.locationsFilterRecycler.apply {
            adapter = filterLocationAdapter
            itemAnimator = null
        }

        // Load initial state from MusicSettings
        loadInitialState(binding)

        // Set up string resources
        binding.locationsModeHeader.setText(R.string.set_load_from)
        binding.locationsModeExclude.setText(R.string.set_file_picker)
        binding.locationsModeInclude.setText(R.string.set_system_database)
        binding.locationsModeDirect.setText(R.string.set_direct_fs)
        binding.locationsExcludeModeHeader.setText(R.string.set_filter_mode)
        binding.locationsExcludeModeExclude.setText(R.string.set_include)
        binding.locationsExcludeModeInclude.setText(R.string.set_exclude)
        binding.locationsIncludeListHeader.setText(R.string.set_folders_to_load)
        binding.locationsAutoDetect.contentDescription = getString(R.string.set_auto_detect_usb)
        binding.locationsIncludeAdd.contentDescription = getString(R.string.desc_add_folder)
        binding.locationsExcludeAdd.contentDescription = getString(R.string.desc_add_folder)
        binding.locationsFilterAdd.contentDescription = getString(R.string.desc_add_folder)
        binding.locationsExtrasDropdown.setText(R.string.set_extra_settings)
        binding.locationsAutoDetect.setOnClickListener {
            pendingLocationCallback = { location -> addIncludeLocation(location) }
            showCandidatePathPicker(disableThirdParty = false)
        }

        // Set up extras dropdown click listener
        binding.locationsExtrasDropdown.setOnClickListener {
            isExtrasExpanded = !isExtrasExpanded
            updateExtrasVisibility(binding)
        }

        binding.locationsModeExclude.setOnClickListener {
            updateLocationMode(binding, LocationMode.SAF)
        }
        binding.locationsModeInclude.setOnClickListener {
            updateLocationMode(binding, LocationMode.MEDIA_STORE)
        }
        binding.locationsModeDirect.setOnClickListener {
            updateLocationMode(binding, LocationMode.DIRECT_FS)
        }
        binding.locationsExcludeModeExclude.setOnClickListener {
            updateFilterMode(binding, include = true)
        }
        binding.locationsExcludeModeInclude.setOnClickListener {
            updateFilterMode(binding, include = false)
        }

        // Set up add folder buttons
        binding.locationsIncludeAdd.setOnClickListener {
            pendingLocationCallback = { location -> addIncludeLocation(location) }
            onNewLocation(openDocumentTreeLauncher, disableThirdParty = false)
        }
        binding.locationsExcludeAdd.setOnClickListener {
            pendingLocationCallback = { location ->
                excludeLocationAdapter.add(location)
                updateSaveButtonState()
            }
            onNewLocation(openDocumentTreeLauncher, disableThirdParty = false)
        }
        binding.locationsFilterAdd.setOnClickListener {
            pendingLocationCallback = { location ->
                filterLocationAdapter.add(location)
                updateSaveButtonState()
            }
            onNewLocation(localOnlyOpenDocumentTreeLauncher, disableThirdParty = true)
        }

        // Set up grant permission card click
        binding.locationsPermsCard.setOnClickListener { requestStoragePermission() }

        // Initialize UI state
        updateModeUI(binding)
        updateExtrasVisibility(binding)
        updateSaveButtonState()
    }

    private fun loadInitialState(binding: DialogMusicLocationsBinding) {
        // Determine mode based on the locationMode setting
        locationMode = musicSettings.locationMode

        // Load data for the initial mode
        loadModeData(binding)

        // Set initial selection state (no group logic; we manage checked state ourselves)
        binding.locationsModeExclude.isChecked = locationMode == LocationMode.SAF
        binding.locationsModeInclude.isChecked = locationMode == LocationMode.MEDIA_STORE
        binding.locationsModeDirect.isChecked = locationMode == LocationMode.DIRECT_FS

        // Check storage permission status
        hasStoragePermission = checkStoragePermission()
    }

    private fun loadModeData(binding: DialogMusicLocationsBinding) {
        // Load SAF data
        musicSettings.safQuery.let { query ->
            includeLocationAdapter.addAll(query.source)
            excludeLocationAdapter.addAll(query.exclude)
            binding.locationsWithHiddenSwitch.isChecked = query.withHidden
            binding.locationsMultithreadSwitch.isChecked = query.multithread
        }
        // Load MediaStore data
        musicSettings.mediaStoreQuery.let { query ->
            filterLocationAdapter.addAll(query.filtered)
            binding.locationsExcludeNonMusicSwitch.isChecked = query.excludeNonMusic

            isIncludeMode = query.mode == MediaStore.FilterMode.INCLUDE
            binding.locationsExcludeModeExclude.isChecked = isIncludeMode
            binding.locationsExcludeModeInclude.isChecked = !isIncludeMode
        }
    }

    private fun updateLocationMode(binding: DialogMusicLocationsBinding, mode: LocationMode) {
        // Enforce "selection required" behavior.
        binding.locationsModeExclude.isChecked = mode == LocationMode.SAF
        binding.locationsModeInclude.isChecked = mode == LocationMode.MEDIA_STORE
        binding.locationsModeDirect.isChecked = mode == LocationMode.DIRECT_FS

        locationMode = mode
        updateModeUI(binding)
        updateSaveButtonState()
    }

    private fun updateFilterMode(binding: DialogMusicLocationsBinding, include: Boolean) {
        // Enforce "selection required" behavior.
        binding.locationsExcludeModeExclude.isChecked = include
        binding.locationsExcludeModeInclude.isChecked = !include

        isIncludeMode = include
        updateExcludeModeUI(binding)
    }

    override fun onStart() {
        super.onStart()
        // Update save button state after dialog is shown
        updateSaveButtonState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // TODO
    }

    override fun onDestroyBinding(binding: DialogMusicLocationsBinding) {
        super.onDestroyBinding(binding)
        openDocumentTreeLauncher = null
        localOnlyOpenDocumentTreeLauncher = null
        storagePermissionLauncher = null
        pendingLocationCallback = null
        binding.locationsIncludeRecycler.adapter = null
        binding.locationsExcludeRecycler.adapter = null
        binding.locationsFilterRecycler.adapter = null
    }

    private fun onNewLocation(launcher: ActivityResultLauncher<Uri?>?, disableThirdParty: Boolean) {
        L.d("Opening launcher")
        val launcher = requireNotNull(launcher) { "Document tree launcher was not available" }

        try {
            launcher.launch(null)
        } catch (e: ActivityNotFoundException) {
            L.w(e, "SAF tree picker activity not found; showing fallback sources.")
            showPickerUnavailableFallback(disableThirdParty)
        } catch (e: SecurityException) {
            L.w(e, "SAF tree picker launch denied; showing fallback sources.")
            showPickerUnavailableFallback(disableThirdParty)
        } catch (e: IllegalStateException) {
            L.w(e, "SAF tree picker launcher unavailable; showing fallback sources.")
            showPickerUnavailableFallback(disableThirdParty)
        } catch (e: RuntimeException) {
            L.w(e, "SAF tree picker failed; showing fallback sources.")
            showPickerUnavailableFallback(disableThirdParty)
        }
    }

    private fun showPickerUnavailableFallback(disableThirdParty: Boolean) {
        val ctx =
            context
                ?: run {
                    pendingLocationCallback = null
                    return
                }
        AlertDialog.Builder(ctx)
            .setMessage(R.string.set_picker_unavailable_fallback)
            .setPositiveButton(R.string.lbl_ok) { _, _ ->
                showCandidatePathPicker(disableThirdParty)
            }
            .setNegativeButton(R.string.lbl_cancel) { _, _ -> pendingLocationCallback = null }
            .setOnCancelListener { pendingLocationCallback = null }
            .show()
    }

    private fun clearPendingLocationCallback(callback: (Location.Unopened) -> Unit) {
        if (pendingLocationCallback === callback) {
            pendingLocationCallback = null
        }
    }

    private fun showCandidatePathPicker(disableThirdParty: Boolean) {
        val callback = pendingLocationCallback ?: return
        val loadingDialog =
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.set_select_source)
                    .setMessage(R.string.lng_loading_music_library)
                    .setNeutralButton(R.string.set_enter_path_manually) { _, _ ->
                        showManualPathEntry(disableThirdParty, callback)
                    }
                    .setNegativeButton(R.string.lbl_cancel) { _, _ ->
                        clearPendingLocationCallback(callback)
                    }
                    .create()
                    .also { dialog ->
                        dialog.setOnCancelListener { clearPendingLocationCallback(callback) }
                    }
            }
        loadingDialog?.show()
        lifecycleScope.launch {
            val accessibleCandidates =
                withContext(Dispatchers.IO) {
                    TopwaySourcePolicy.discoverMusicSourceCandidates(
                        savedPaths = includeLocationAdapter.locations.map { it.uri.toString() },
                        mediaStoreParents = discoverMediaStoreAudioParents(),
                        storageRoots = discoverStorageRoots(),
                        rootGate =
                            rootGate.takeIf {
                                BuildConfig.TOPWAY_COMPAT_FLAVOR &&
                                    locationMode == LocationMode.DIRECT_FS &&
                                    rootGate.stateSnapshot() == RootStateHolder.State.Available
                            },
                    )
                }
            if (loadingDialog?.isShowing == true) loadingDialog.dismiss()
            val ctx =
                context
                    ?: run {
                        clearPendingLocationCallback(callback)
                        return@launch
                    }

            if (accessibleCandidates.isEmpty()) {
                showManualPathEntry(disableThirdParty, callback)
                return@launch
            }

            AlertDialog.Builder(ctx)
                .setTitle(R.string.set_select_source)
                .setItems(accessibleCandidates.toTypedArray()) { _, which ->
                    validateAndAcceptPath(accessibleCandidates[which], disableThirdParty, callback)
                }
                .setNeutralButton(R.string.set_enter_path_manually) { _, _ ->
                    showManualPathEntry(disableThirdParty, callback)
                }
                .setNegativeButton(R.string.lbl_cancel) { _, _ ->
                    clearPendingLocationCallback(callback)
                }
                .setOnCancelListener { clearPendingLocationCallback(callback) }
                .show()
        }
    }

    private fun validateAndAcceptPath(
        path: String,
        disableThirdParty: Boolean,
        callback: (Location.Unopened) -> Unit,
    ) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) { validateManualPath(path) }
            val currentContext = context
            if (currentContext == null) {
                clearPendingLocationCallback(callback)
                return@launch
            }
            if (result != ManualPathValidation.OK) {
                L.w("Rejecting music source $path: $result")
                currentContext.showToast(result.toastRes)
                clearPendingLocationCallback(callback)
                return@launch
            }
            val uri = Uri.fromFile(File(path))
            val location = Location.Unopened.from(currentContext, uri)
            if (disableThirdParty && location.path.volume is Volume.ThirdParty) {
                L.w("Rejecting music source $path: third-party volume disabled")
                currentContext.showToast(R.string.err_bad_location)
                clearPendingLocationCallback(callback)
                return@launch
            }
            if (location.open(currentContext) == null) {
                L.w("Rejecting music source $path: Location.open returned null")
                currentContext.showToast(ManualPathValidation.OPEN_FAILED.toastRes)
                clearPendingLocationCallback(callback)
                return@launch
            }
            callback(location)
            clearPendingLocationCallback(callback)
        }
    }

    private enum class ManualPathValidation(val toastRes: Int) {
        OK(R.string.lbl_ok),
        UNSAFE(R.string.set_path_unsafe),
        MISSING(R.string.set_path_missing),
        NOT_DIRECTORY(R.string.set_path_not_directory),
        UNREADABLE(R.string.set_path_unreadable),
        PERMISSION_MISSING(R.string.set_path_permission_missing),
        ROOT_UNAVAILABLE(R.string.set_path_root_unavailable),
        OPEN_FAILED(R.string.set_path_open_failed),
    }

    private fun validateManualPath(path: String): ManualPathValidation {
        val directTs18Path =
            BuildConfig.TOPWAY_COMPAT_FLAVOR && locationMode == LocationMode.DIRECT_FS
        if (directTs18Path && !TopwaySourcePolicy.isAllowedSourceCandidate(path)) {
            return ManualPathValidation.UNSAFE
        }
        if (!hasStoragePermission && locationMode != LocationMode.SAF) {
            return ManualPathValidation.PERMISSION_MISSING
        }
        return try {
            val file = File(path)
            when {
                !file.exists() -> ManualPathValidation.MISSING
                !file.isDirectory -> ManualPathValidation.NOT_DIRECTORY
                !file.canRead() && directTs18Path && path.startsWith("/mnt/media_rw/") ->
                    ManualPathValidation.ROOT_UNAVAILABLE
                !file.canRead() -> ManualPathValidation.UNREADABLE
                else -> ManualPathValidation.OK
            }
        } catch (e: SecurityException) {
            L.w(e, "Security exception while validating manual path $path")
            ManualPathValidation.PERMISSION_MISSING
        } catch (e: RuntimeException) {
            L.w(e, "Runtime exception while validating manual path $path")
            ManualPathValidation.UNREADABLE
        }
    }

    private fun discoverMediaStoreAudioParents(): List<String> {
        if (!hasStoragePermission) return emptyList()
        val resolver = context?.contentResolver ?: return emptyList()
        val out = linkedSetOf<String>()
        return try {
            resolver
                .query(
                    AndroidAudioMedia.EXTERNAL_CONTENT_URI,
                    arrayOf(AndroidAudioMedia.DATA),
                    null,
                    null,
                    null,
                )
                ?.use { cursor ->
                    val dataColumn = cursor.getColumnIndex(AndroidAudioMedia.DATA)
                    while (dataColumn >= 0 && cursor.moveToNext() && out.size < 48) {
                        cursor.getString(dataColumn)?.let { data ->
                            File(data).parent?.let(out::add)
                        }
                    }
                }
            out.toList()
        } catch (e: Exception) {
            L.w(e, "Failed to query MediaStore audio parents for source candidates")
            emptyList()
        }
    }

    private fun discoverStorageRoots(): List<String> {
        val ctx = context ?: return emptyList()
        val out = linkedSetOf<String>()
        out.add(Environment.getExternalStorageDirectory().absolutePath)
        out.add(TopwaySourcePolicy.EMULATED_ROOT)
        out.add(TopwaySourcePolicy.SDCARD_ROOT)
        try {
            val storageManager = ctx.getSystemService(StorageManager::class.java)
            storageManager.storageVolumes.forEach { volume ->
                val dir =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        volume.directory
                    } else {
                        runCatching {
                                volume.javaClass.getMethod("getPathFile").invoke(volume) as? File
                            }
                            .getOrNull()
                    }
                dir?.absolutePath?.let(out::add)
            }
        } catch (e: Exception) {
            L.w(e, "Failed to query StorageManager volumes for source candidates")
        }
        return out.toList()
    }

    private fun showManualPathEntry(
        disableThirdParty: Boolean,
        callback: (Location.Unopened) -> Unit,
    ) {
        val ctx =
            context
                ?: run {
                    clearPendingLocationCallback(callback)
                    return
                }
        val input =
            androidx.appcompat.widget.AppCompatEditText(ctx).apply {
                hint = ctx.getString(R.string.set_enter_path_hint)
                setSingleLine()
                inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            }
        val padding = (16 * ctx.resources.displayMetrics.density).toInt()
        val container =
            android.widget.FrameLayout(ctx).apply {
                setPadding(padding, padding / 2, padding, 0)
                addView(input)
            }
        AlertDialog.Builder(ctx)
            .setTitle(R.string.set_select_source)
            .setView(container)
            .setPositiveButton(R.string.lbl_ok) { _, _ ->
                val rawPathText = input.text?.toString().orEmpty()
                val pathText = rawPathText.trim()
                if (pathText.isEmpty()) {
                    clearPendingLocationCallback(callback)
                    return@setPositiveButton
                }
                if (rawPathText != pathText) {
                    ctx.showToast(R.string.set_path_whitespace_invalid)
                    clearPendingLocationCallback(callback)
                    return@setPositiveButton
                }
                validateAndAcceptPath(pathText, disableThirdParty, callback)
            }
            .setNegativeButton(R.string.lbl_cancel) { _, _ ->
                clearPendingLocationCallback(callback)
            }
            .setOnCancelListener { clearPendingLocationCallback(callback) }
            .show()
    }

    private fun addDocumentTreeUriToDirs(uri: Uri?, disableThirdParty: Boolean) {
        if (uri == null) {
            L.d("No URI given (user closed the dialog)")
            pendingLocationCallback = null
            return
        }
        val ctx =
            context
                ?: run {
                    pendingLocationCallback = null
                    return
                }
        if (uri.scheme != "file" && !DocumentsContract.isTreeUri(uri)) {
            L.w("SAF picker returned a non-tree URI: $uri")
            ctx.showToast(R.string.err_bad_location)
            pendingLocationCallback = null
            return
        }
        val location = Location.Unopened.from(ctx, uri)

        if (location.path.volume is Volume.ThirdParty && disableThirdParty) {
            ctx.showToast(R.string.err_bad_location)
            pendingLocationCallback = null
            return
        }
        pendingLocationCallback?.invoke(location)
        pendingLocationCallback = null
    }

    private fun addIncludeLocation(location: Location.Unopened) {
        val ctx = context ?: return
        val opened = location.open(ctx)
        if (opened != null) {
            includeLocationAdapter.add(opened)
            updateSaveButtonState()
        } else {
            ctx.showToast(R.string.err_bad_location)
        }
    }

    private fun updateModeUI(binding: DialogMusicLocationsBinding) {
        with(binding) {
            when (locationMode) {
                LocationMode.SAF -> {
                    // File Picker mode
                    locationsModeDesc.setText(R.string.lng_file_picker)

                    // Update permission section
                    locationsPermsDesc.setText(R.string.set_grant_storage_anyway)
                    locationsPermsSubtitle.setText(R.string.lng_grant_storage_anyway)
                }
                LocationMode.MEDIA_STORE -> {
                    // System Database mode
                    locationsModeDesc.setText(R.string.lng_system_database)

                    // Update permission section
                    locationsPermsDesc.setText(R.string.set_grant_storage)
                    locationsPermsSubtitle.setText(R.string.lng_grant_storage_required)

                    // Update exclude mode description based on selection
                    updateExcludeModeUI(binding)
                }
                LocationMode.DIRECT_FS -> {
                    // Direct FS mode
                    locationsModeDesc.setText(R.string.lng_direct_fs)

                    // Update permission section
                    locationsPermsDesc.setText(R.string.set_grant_storage)
                    locationsPermsSubtitle.setText(R.string.lng_grant_storage_required)
                }
            }

            // Update enabled state based on permission
            updatePermissionDependentUI(binding)
            // Update card colors based on mode and permission
            updatePermissionCardColors(binding)
            // Update permission card visibility
            updatePermissionCardVisibility(binding)
            // Update extras visibility based on current state
            updateExtrasVisibility(binding)
        }
    }

    private fun updateExcludeModeUI(binding: DialogMusicLocationsBinding) {
        with(binding) {
            if (isIncludeMode) {
                locationsExcludeModeDesc.setText(R.string.lng_include_folders)
            } else {
                locationsExcludeModeDesc.setText(R.string.lng_exclude_folders)
            }
        }
    }

    private fun updatePermissionDependentUI(binding: DialogMusicLocationsBinding) {
        with(binding) {
            // Only disable views in System Database and Direct FS mode when permission not granted
            // File Picker mode doesn't require storage permission
            val isEnabled = locationMode == LocationMode.SAF || hasStoragePermission

            locationsIncludeListHeader.isEnabled = isEnabled
            locationsIncludeAdd.isEnabled = isEnabled
            locationsIncludeRecycler.isEnabled = isEnabled

            locationsExcludeModeHeader.isEnabled = isEnabled
            locationsExcludeModeGroup.isEnabled = isEnabled
            locationsExcludeModeDesc.isEnabled = isEnabled
            locationsExcludeModeExclude.isEnabled = isEnabled
            locationsExcludeModeInclude.isEnabled = isEnabled

            locationsExcludeListHeader.isEnabled = isEnabled
            locationsExcludeAdd.isEnabled = isEnabled
            locationsExcludeRecycler.isEnabled = isEnabled

            locationsFilterListHeader.isEnabled = isEnabled
            locationsFilterAdd.isEnabled = isEnabled
            locationsFilterRecycler.isEnabled = isEnabled

            locationsWithHiddenTitle.isEnabled = isEnabled
            locationsWithHiddenDesc.isEnabled = isEnabled
            locationsWithHidden.isEnabled = isEnabled

            locationsExcludeNonMusicTitle.isEnabled = isEnabled
            locationsExcludeNonMusicDesc.isEnabled = isEnabled
            locationsExcludeNonMusic.isEnabled = isEnabled
        }
    }

    private fun updatePermissionCardColors(binding: DialogMusicLocationsBinding) {
        val context = requireContext()
        with(binding.locationsPermsCard) {
            if (locationMode == LocationMode.SAF) {
                // File Picker mode - use secondary colors
                setCardBackgroundColor(context.getAttrColorCompat(MR.attr.colorSecondaryContainer))
                binding.locationsPermsDesc.setTextColor(
                    context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
                )
                binding.locationsPermsSubtitle.setTextColor(
                    context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
                )
                binding.locationsPermsOpen.imageTintList =
                    context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
            } else {
                // System Database mode - use error colors if no permission, secondary colors if
                // granted
                if (hasStoragePermission) {
                    // Has permission - use secondary colors
                    setCardBackgroundColor(
                        context.getAttrColorCompat(MR.attr.colorSecondaryContainer)
                    )
                    binding.locationsPermsDesc.setTextColor(
                        context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
                    )
                    binding.locationsPermsSubtitle.setTextColor(
                        context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
                    )
                    binding.locationsPermsOpen.imageTintList =
                        context.getAttrColorCompat(MR.attr.colorOnSecondaryContainer)
                } else {
                    setCardBackgroundColor(context.getAttrColorCompat(MR.attr.colorErrorContainer))
                    binding.locationsPermsDesc.setTextColor(
                        context.getAttrColorCompat(MR.attr.colorOnErrorContainer)
                    )
                    binding.locationsPermsSubtitle.setTextColor(
                        context.getAttrColorCompat(MR.attr.colorOnErrorContainer)
                    )
                    binding.locationsPermsOpen.imageTintList =
                        context.getAttrColorCompat(MR.attr.colorOnErrorContainer)
                }
            }
        }
    }

    private fun updatePermissionCardVisibility(binding: DialogMusicLocationsBinding) {
        with(binding) {
            // Hide the permission card when permissions are granted
            locationsPermsCard.isVisible = !hasStoragePermission
        }
    }

    private fun updateExtrasVisibility(binding: DialogMusicLocationsBinding) {
        with(binding) {
            // Update dropdown icon rotation
            locationsExtrasDropdownIcon.rotation = if (isExtrasExpanded) 180f else 0f

            if (locationMode == LocationMode.SAF || locationMode == LocationMode.DIRECT_FS) {
                // File Picker / Direct mode - show include/exclude lists when expanded
                // Include section
                locationsIncludeListHeaderDivider.isVisible = true
                locationsIncludeListHeader.isVisible = true
                locationsIncludeAdd.isVisible = true
                locationsIncludeRecycler.isVisible = true

                // Show dividers and exclude section only when expanded
                locationsExcludeListHeader.isVisible = isExtrasExpanded
                locationsExcludeAdd.isVisible = isExtrasExpanded
                locationsExcludeRecycler.isVisible = isExtrasExpanded

                // Hide filter mode section completely
                locationsExcludeModeHeader.isVisible = false
                locationsExcludeModeGroup.isVisible = false
                locationsExcludeModeDesc.isVisible = false
                locationsFilterModeDivider.isVisible = false
                locationsFilterListHeader.isVisible = false
                locationsFilterAdd.isVisible = false
                locationsFilterRecycler.isVisible = false
                locationsExcludeListDivider.isVisible = false

                // Config section
                configDivider.isVisible = isExtrasExpanded
                locationsWithHiddenTitle.isVisible = isExtrasExpanded
                locationsWithHiddenDesc.isVisible = isExtrasExpanded
                locationsWithHidden.isVisible = isExtrasExpanded

                locationsExcludeNonMusicTitle.isVisible = false
                locationsExcludeNonMusicDesc.isVisible = false
                locationsExcludeNonMusic.isVisible = false

                locationsMultithreadTitle.isVisible = isExtrasExpanded
                locationsMultithreadDesc.isVisible = isExtrasExpanded
                locationsMultithread.isVisible = isExtrasExpanded
            } else {
                // System Database mode - show filter mode when expanded
                // Hide include section
                locationsIncludeListHeaderDivider.isVisible = false
                locationsIncludeListHeader.isVisible = false
                locationsIncludeAdd.isVisible = false
                locationsIncludeRecycler.isVisible = false

                // Hide exclude section (at bottom)
                locationsExcludeListDivider.isVisible = false
                locationsExcludeListHeader.isVisible = false
                locationsExcludeAdd.isVisible = false
                locationsExcludeRecycler.isVisible = false

                // Show filter mode section only when expanded
                locationsExcludeModeHeader.isVisible = isExtrasExpanded
                locationsExcludeModeGroup.isVisible = isExtrasExpanded
                locationsExcludeModeDesc.isVisible = isExtrasExpanded
                locationsFilterModeDivider.isVisible = isExtrasExpanded
                locationsFilterListHeader.isVisible = isExtrasExpanded
                locationsFilterAdd.isVisible = isExtrasExpanded
                locationsFilterRecycler.isVisible = isExtrasExpanded

                // Config section
                configDivider.isVisible = isExtrasExpanded
                locationsWithHiddenTitle.isVisible = false
                locationsWithHiddenDesc.isVisible = false
                locationsWithHidden.isVisible = false

                locationsExcludeNonMusicTitle.isVisible = isExtrasExpanded
                locationsExcludeNonMusicDesc.isVisible = isExtrasExpanded
                locationsExcludeNonMusic.isVisible = isExtrasExpanded

                locationsMultithreadTitle.isVisible = false
                locationsMultithreadDesc.isVisible = false
                locationsMultithread.isVisible = false
            }
        }
    }

    private fun saveChanges() {
        val binding = requireBinding()

        // Check if configuration has actually changed
        val currentMode = musicSettings.locationMode
        val modeChanged = currentMode != locationMode

        var configChanged = modeChanged

        if (locationMode == LocationMode.SAF || locationMode == LocationMode.DIRECT_FS) {
            // Check if SAF/Direct query changed
            val currentSafQuery = musicSettings.safQuery
            val newSafQuery =
                SAF.Query(
                    source = includeLocationAdapter.locations,
                    exclude = excludeLocationAdapter.locations,
                    withHidden = binding.locationsWithHiddenSwitch.isChecked,
                    multithread = binding.locationsMultithreadSwitch.isChecked,
                )

            if (
                !modeChanged &&
                    (currentMode == LocationMode.SAF || currentMode == LocationMode.DIRECT_FS)
            ) {
                configChanged = currentSafQuery != newSafQuery
            }

            // Save the new SAF/Direct query
            musicSettings.safQuery = newSafQuery
        } else {
            // Check if MediaStore query changed
            val currentMediaStoreQuery = musicSettings.mediaStoreQuery
            val filterMode =
                if (isIncludeMode) {
                    MediaStore.FilterMode.INCLUDE
                } else {
                    MediaStore.FilterMode.EXCLUDE
                }
            val newMediaStoreQuery =
                MediaStore.Query(
                    mode = filterMode,
                    filtered = filterLocationAdapter.locations,
                    excludeNonMusic = binding.locationsExcludeNonMusicSwitch.isChecked,
                )

            if (!modeChanged && currentMode == LocationMode.MEDIA_STORE) {
                configChanged = currentMediaStoreQuery != newMediaStoreQuery
            }

            // Save the new MediaStore query
            musicSettings.mediaStoreQuery = newMediaStoreQuery
        }

        // Save the mode setting
        musicSettings.locationMode = locationMode

        // If no configuration changed but permission was granted in this session,
        // force a location update
        if (!configChanged && permissionGrantedInSession) {
            L.d("No config changes detected, but permission was granted - forcing location update")
            musicSettings.forceLocationUpdate()
        }
    }

    private fun checkStoragePermission(): Boolean {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        return ContextCompat.checkSelfPermission(requireContext(), permission) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

        val launcher =
            requireNotNull(storagePermissionLauncher) {
                "Storage permission launcher was not available"
            }

        try {
            L.d("Requesting storage permission: $permission")
            launcher.launch(permission)
        } catch (e: Exception) {
            L.e("Failed to request storage permission")
            L.e(e.stackTraceToString())
            requireContext().showToast(R.string.err_no_app)
        }
    }

    private fun updateSaveButtonState() {
        val dialog = dialog as? AlertDialog ?: return

        val isEnabled =
            if (locationMode == LocationMode.SAF || locationMode == LocationMode.DIRECT_FS) {
                // File Picker / Direct mode: Enable save only if there's at least one folder
                includeLocationAdapter.locations.isNotEmpty()
            } else {
                // System mode: Enable save only if permission is granted
                hasStoragePermission
            }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = isEnabled
    }
}
