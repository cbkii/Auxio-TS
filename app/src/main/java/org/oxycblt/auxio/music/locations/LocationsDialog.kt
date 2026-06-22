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
import android.provider.DocumentsContract
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
import org.oxycblt.auxio.R
import org.oxycblt.auxio.databinding.DialogMusicLocationsBinding
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

        loadInitialState(binding)

        binding.locationsModeHeader.setText(R.string.set_load_from)
        binding.locationsModeExclude.setText(R.string.set_file_picker)
        binding.locationsModeInclude.setText(R.string.set_system_database)
        binding.locationsModeDirect.text = "Direct"
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

        binding.locationsUsbDetect.setOnClickListener { autoDetectUsb() }

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

        binding.locationsIncludeAdd.setOnClickListener {
            pendingLocationCallback = { location -> addIncludeLocation(location) }
            if (locationMode == LocationMode.DIRECT_FS) {
                showManualPathEntry(false) { location -> addIncludeLocation(location) }
            } else {
                onNewLocation(openDocumentTreeLauncher, disableThirdParty = false)
            }
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

        binding.locationsPermsCard.setOnClickListener { requestStoragePermission() }

        updateModeUI(binding)
        updateExtrasVisibility(binding)
        updateSaveButtonState()
    }

    private fun loadInitialState(binding: DialogMusicLocationsBinding) {
        locationMode = musicSettings.locationMode
        loadModeData(binding)
        binding.locationsModeExclude.isChecked = locationMode == LocationMode.SAF
        binding.locationsModeInclude.isChecked = locationMode == LocationMode.MEDIA_STORE
        binding.locationsModeDirect.isChecked = locationMode == LocationMode.DIRECT_FS
        hasStoragePermission = checkStoragePermission()
    }

    private fun loadModeData(binding: DialogMusicLocationsBinding) {
        musicSettings.safQuery.let { query ->
            includeLocationAdapter.addAll(query.source)
            excludeLocationAdapter.addAll(query.exclude)
            binding.locationsWithHiddenSwitch.isChecked = query.withHidden
            binding.locationsMultithreadSwitch.isChecked = query.multithread
        }
        musicSettings.mediaStoreQuery.let { query ->
            filterLocationAdapter.addAll(query.filtered)
            binding.locationsExcludeNonMusicSwitch.isChecked = query.excludeNonMusic
            isIncludeMode = query.mode == MediaStore.FilterMode.INCLUDE
            binding.locationsExcludeModeExclude.isChecked = isIncludeMode
            binding.locationsExcludeModeInclude.isChecked = !isIncludeMode
        }
    }

    private fun updateLocationMode(binding: DialogMusicLocationsBinding, mode: LocationMode) {
        binding.locationsModeExclude.isChecked = mode == LocationMode.SAF
        binding.locationsModeInclude.isChecked = mode == LocationMode.MEDIA_STORE
        binding.locationsModeDirect.isChecked = mode == LocationMode.DIRECT_FS
        locationMode = mode
        updateModeUI(binding)
        updateSaveButtonState()
    }

    private fun updateFilterMode(binding: DialogMusicLocationsBinding, include: Boolean) {
        binding.locationsExcludeModeExclude.isChecked = include
        binding.locationsExcludeModeInclude.isChecked = !include
        isIncludeMode = include
        updateExcludeModeUI(binding)
    }

    override fun onStart() {
        super.onStart()
        updateSaveButtonState()
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
        val launcher = requireNotNull(launcher) { "Document tree launcher was not available" }
        try {
            launcher.launch(null)
        } catch (e: Exception) {
            L.e(e, "Failed to launch document tree picker")
            if (e is ActivityNotFoundException) {
                showCandidatePathPicker(disableThirdParty)
            } else {
                requireContext().showToast(R.string.err_no_app)
            }
        }
    }

    private fun showCandidatePathPicker(disableThirdParty: Boolean) {
        val callback = pendingLocationCallback ?: return
        val currentContext = context ?: return
        val candidatePaths = TopwaySourcePolicy.discoverCandidateRoots()
        if (candidatePaths.isEmpty()) {
            showManualPathEntry(disableThirdParty, callback)
        } else {
            AlertDialog.Builder(currentContext)
                .setTitle(R.string.set_select_source)
                .setItems(candidatePaths.toTypedArray()) { _, which ->
                    val path = candidatePaths[which]
                    val uri = Uri.fromFile(File(path))
                    val location = Location.Unopened.from(currentContext, uri)
                    if (
                        disableThirdParty &&
                            location.path.volume is Volume.ThirdParty &&
                            uri.scheme != "file"
                    ) {
                        currentContext.showToast(R.string.err_bad_location)
                    } else {
                        callback(location)
                    }
                    clearPendingLocationCallback(callback)
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
                val pathText = input.text?.toString()?.trim().orEmpty()
                if (pathText.isEmpty()) {
                    clearPendingLocationCallback(callback)
                    return@setPositiveButton
                }
                lifecycleScope.launch {
                    val accessible =
                        withContext(Dispatchers.IO) {
                            try {
                                val file = File(pathText)
                                file.exists() && file.isDirectory && file.canRead()
                            } catch (e: Exception) {
                                false
                            }
                        }
                    val currentContext =
                        context
                            ?: run {
                                clearPendingLocationCallback(callback)
                                return@launch
                            }
                    if (!accessible) {
                        currentContext.showToast(R.string.set_path_inaccessible)
                        clearPendingLocationCallback(callback)
                        return@launch
                    }
                    val uri = Uri.fromFile(File(pathText))
                    val location = Location.Unopened.from(currentContext, uri)
                    callback(location)
                    clearPendingLocationCallback(callback)
                }
            }
            .setNegativeButton(R.string.lbl_cancel) { _, _ ->
                clearPendingLocationCallback(callback)
            }
            .setOnCancelListener { clearPendingLocationCallback(callback) }
            .show()
    }

    private fun addDocumentTreeUriToDirs(uri: Uri?, disableThirdParty: Boolean) {
        if (uri == null) {
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

    private fun clearPendingLocationCallback(callback: (Location.Unopened) -> Unit) {
        if (pendingLocationCallback == callback) {
            pendingLocationCallback = null
        }
    }

    private fun autoDetectUsb() {
        val usbPaths = TopwaySourcePolicy.discoverUsbStorage()
        if (usbPaths.isEmpty()) {
            context?.showToast("No USB storage found")
            return
        }
        val ctx = context ?: return
        var added = 0
        usbPaths.forEach { path ->
            val uri = Uri.fromFile(File(path))
            val location = Location.Unopened.from(ctx, uri)
            val opened = location.open(ctx)
            if (opened != null && includeLocationAdapter.locations.none { it.uri == uri }) {
                includeLocationAdapter.add(opened)
                added++
            }
        }
        if (added > 0) {
            updateSaveButtonState()
            ctx.showToast("Added $added USB source(s)")
        }
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
                    locationsModeDesc.setText(R.string.lng_file_picker)
                    locationsPermsDesc.setText(R.string.set_grant_storage_anyway)
                    locationsPermsSubtitle.setText(R.string.lng_grant_storage_anyway)
                }
                LocationMode.DIRECT_FS -> {
                    locationsModeDesc.text =
                        "Direct filesystem access. More reliable on head units."
                    locationsPermsDesc.setText(R.string.set_grant_storage_anyway)
                    locationsPermsSubtitle.setText(R.string.lng_grant_storage_anyway)
                }
                LocationMode.MEDIA_STORE -> {
                    locationsModeDesc.setText(R.string.lng_system_database)
                    locationsPermsDesc.setText(R.string.set_grant_storage)
                    locationsPermsSubtitle.setText(R.string.lng_grant_storage_required)
                    updateExcludeModeUI(binding)
                }
            }
            updatePermissionDependentUI(binding)
            updatePermissionCardColors(binding)
            updatePermissionCardVisibility(binding)
            updateExtrasVisibility(binding)
        }
    }

    private fun updateExcludeModeUI(binding: DialogMusicLocationsBinding) {
        with(binding) {
            locationsExcludeModeDesc.setText(
                if (isIncludeMode) R.string.lng_include_folders else R.string.lng_exclude_folders
            )
        }
    }

    private fun updatePermissionDependentUI(binding: DialogMusicLocationsBinding) {
        with(binding) {
            val isEnabled = locationMode != LocationMode.MEDIA_STORE || hasStoragePermission
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
            if (locationMode != LocationMode.MEDIA_STORE) {
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
                if (hasStoragePermission) {
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
        binding.locationsPermsCard.isVisible = !hasStoragePermission
    }

    private fun updateExtrasVisibility(binding: DialogMusicLocationsBinding) {
        with(binding) {
            locationsExtrasDropdownIcon.rotation = if (isExtrasExpanded) 180f else 0f
            if (locationMode != LocationMode.MEDIA_STORE) {
                locationsIncludeListHeaderDivider.isVisible = true
                locationsIncludeListHeader.isVisible = true
                locationsIncludeAdd.isVisible = true
                locationsIncludeRecycler.isVisible = true
                locationsUsbDetect.isVisible = locationMode == LocationMode.DIRECT_FS
                locationsExcludeListHeader.isVisible = isExtrasExpanded
                locationsExcludeAdd.isVisible = isExtrasExpanded
                locationsExcludeRecycler.isVisible = isExtrasExpanded
                locationsExcludeModeHeader.isVisible = false
                locationsExcludeModeGroup.isVisible = false
                locationsExcludeModeDesc.isVisible = false
                locationsFilterModeDivider.isVisible = false
                locationsFilterListHeader.isVisible = false
                locationsFilterAdd.isVisible = false
                locationsFilterRecycler.isVisible = false
                locationsExcludeListDivider.isVisible = false
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
                locationsIncludeListHeaderDivider.isVisible = false
                locationsIncludeListHeader.isVisible = false
                locationsIncludeAdd.isVisible = false
                locationsIncludeRecycler.isVisible = false
                locationsExcludeListDivider.isVisible = false
                locationsExcludeListHeader.isVisible = false
                locationsExcludeAdd.isVisible = false
                locationsExcludeRecycler.isVisible = false
                locationsExcludeModeHeader.isVisible = isExtrasExpanded
                locationsExcludeModeGroup.isVisible = isExtrasExpanded
                locationsExcludeModeDesc.isVisible = isExtrasExpanded
                locationsFilterModeDivider.isVisible = isExtrasExpanded
                locationsFilterListHeader.isVisible = isExtrasExpanded
                locationsFilterAdd.isVisible = isExtrasExpanded
                locationsFilterRecycler.isVisible = isExtrasExpanded
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
        val currentMode = musicSettings.locationMode
        val modeChanged = currentMode != locationMode
        var configChanged = modeChanged
        if (locationMode != LocationMode.MEDIA_STORE) {
            val currentSafQuery = musicSettings.safQuery
            val newSafQuery =
                SAF.Query(
                    source = includeLocationAdapter.locations,
                    exclude = excludeLocationAdapter.locations,
                    withHidden = binding.locationsWithHiddenSwitch.isChecked,
                    multithread = binding.locationsMultithreadSwitch.isChecked,
                )
            if (!modeChanged && currentMode != LocationMode.MEDIA_STORE) {
                configChanged = currentSafQuery != newSafQuery
            }
            musicSettings.safQuery = newSafQuery
        } else {
            val currentMediaStoreQuery = musicSettings.mediaStoreQuery
            val filterMode =
                if (isIncludeMode) MediaStore.FilterMode.INCLUDE else MediaStore.FilterMode.EXCLUDE
            val newMediaStoreQuery =
                MediaStore.Query(
                    mode = filterMode,
                    filtered = filterLocationAdapter.locations,
                    excludeNonMusic = binding.locationsExcludeNonMusicSwitch.isChecked,
                )
            if (!modeChanged && currentMode == LocationMode.MEDIA_STORE) {
                configChanged = currentMediaStoreQuery != newMediaStoreQuery
            }
            musicSettings.mediaStoreQuery = newMediaStoreQuery
        }
        musicSettings.locationMode = locationMode
        if (!configChanged && permissionGrantedInSession) {
            L.d("No config changes detected, but permission was granted - forcing location update")
            musicSettings.forceLocationUpdate()
        }
    }

    private fun checkStoragePermission(): Boolean {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_AUDIO
            else Manifest.permission.READ_EXTERNAL_STORAGE
        return ContextCompat.checkSelfPermission(requireContext(), permission) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_AUDIO
            else Manifest.permission.READ_EXTERNAL_STORAGE
        val launcher =
            requireNotNull(storagePermissionLauncher) {
                "Storage permission launcher was not available"
            }
        try {
            L.d("Requesting storage permission: $permission")
            launcher.launch(permission)
        } catch (e: Exception) {
            L.e("Failed to request storage permission")
            requireContext().showToast(R.string.err_no_app)
        }
    }

    private fun updateSaveButtonState() {
        val dialog = dialog as? AlertDialog ?: return
        val isEnabled =
            if (locationMode != LocationMode.MEDIA_STORE)
                includeLocationAdapter.locations.isNotEmpty()
            else hasStoragePermission
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = isEnabled
    }
}
