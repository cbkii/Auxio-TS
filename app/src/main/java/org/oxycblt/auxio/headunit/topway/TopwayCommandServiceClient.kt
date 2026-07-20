/*
 * Copyright (c) 2026 Auxio Project
 * TopwayCommandServiceClient.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Parcel
import android.os.RemoteException
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import timber.log.Timber as L

/**
 * Optional exact-device adapter for the exported TS18 Topway command service.
 *
 * The adapter is active only in dedicated Topway compatibility variants and only while Auxio's
 * playback service is alive. It registers the observed music callback, maps callback transactions
 * to Android media-key intents targeting the already-running Auxio service component, and falls
 * back cleanly when the vendor service or contract is unavailable.
 */
@Singleton
class TopwayCommandServiceClient
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val coordinator: TopwayLauncherIntegrationCoordinator,
    private val journal: DiagnosticJournal,
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var workerThread: HandlerThread? = null
    private var workerHandler: Handler? = null

    @Volatile private var attached = false
    @Volatile private var ownerServiceClass: Class<out AuxioService>? = null

    private var bindRequested = false
    private var retryScheduled = false
    private var retryCount = 0

    // Accessed only on the worker thread.
    private var remote: IBinder? = null
    private var deathRecipient: IBinder.DeathRecipient? = null
    private var musicCallbackRegistered = false
    private var commandCallbackRegistered = false

    private val musicCallback =
        TopwayMusicCallbackBinder(
            onControl = ::onMusicControl,
            onMode = { mode -> log("Music mode", mode.toString()) },
            onExtended = { bundle -> logBundle("Music extended callback", bundle) },
        )

    private val commandCallback =
        TopwayCommandCallbackBinder(
            onStatus = { event, value -> log(event, value) },
            onExtended = ::onCommandExtended,
        )

    private val retryRunnable =
        Runnable {
            retryScheduled = false
            attemptBind()
        }

    private val connection =
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                log("Service connected", name.flattenToShortString())
                workerHandler?.post { establishRemote(service) }
            }

            override fun onServiceDisconnected(name: ComponentName) {
                log("Service disconnected", name.flattenToShortString())
                workerHandler?.post {
                    clearRemote(unregister = false)
                    mainHandler.post { disconnectAndRetry("service disconnected") }
                }
            }

            override fun onBindingDied(name: ComponentName) {
                log("Binding died", name.flattenToShortString())
                workerHandler?.post {
                    clearRemote(unregister = false)
                    mainHandler.post { disconnectAndRetry("binding died") }
                }
            }

            override fun onNullBinding(name: ComponentName) {
                log("Null binding", name.flattenToShortString())
                mainHandler.post { disconnectAndRetry("null binding") }
            }
        }

    /** Starts a bounded, idempotent bind for the concrete Auxio service component in use. */
    @Synchronized
    fun attach(serviceClass: Class<out AuxioService>) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return
        ownerServiceClass = serviceClass
        if (attached) return

        attached = true
        retryCount = 0
        ensureWorker()
        log("Attach", serviceClass.name)
        mainHandler.post(::attemptBind)
    }

    /** Unregisters callbacks and releases only resources owned by this adapter. */
    @Synchronized
    fun release() {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR || !attached) return
        attached = false
        ownerServiceClass = null
        mainHandler.removeCallbacks(retryRunnable)
        retryScheduled = false
        log("Release")

        val worker = workerHandler
        if (worker == null) {
            mainHandler.post {
                unbindIfNeeded()
                stopWorker()
            }
            return
        }
        worker.post {
            clearRemote(unregister = true)
            mainHandler.post {
                unbindIfNeeded()
                stopWorker()
            }
        }
    }

    @Synchronized
    private fun ensureWorker() {
        if (workerThread != null) return
        val thread = HandlerThread(WORKER_NAME)
        thread.start()
        workerThread = thread
        workerHandler = Handler(thread.looper)
    }

    @Synchronized
    private fun stopWorker() {
        workerHandler = null
        workerThread?.quitSafely()
        workerThread = null
    }

    private fun attemptBind() {
        if (!attached || bindRequested) return

        val actionIntent =
            Intent(TopwayCommandServiceContract.ACTION_BIND)
                .setPackage(TopwayCommandServiceContract.PACKAGE_NAME)
        val explicitIntent =
            Intent(TopwayCommandServiceContract.ACTION_BIND)
                .setComponent(
                    ComponentName(
                        TopwayCommandServiceContract.PACKAGE_NAME,
                        TopwayCommandServiceContract.SERVICE_CLASS_NAME,
                    )
                )

        val candidates =
            buildList {
                try {
                    val resolved = context.packageManager.resolveService(actionIntent, 0)?.serviceInfo
                    if (resolved != null) {
                        add(Intent(actionIntent).setComponent(ComponentName(resolved.packageName, resolved.name)))
                    } else {
                        add(actionIntent)
                    }
                } catch (e: RuntimeException) {
                    L.w(e, "Unable to resolve Topway command service; using explicit fallback")
                    add(actionIntent)
                }
                if (none { it.component == explicitIntent.component }) add(explicitIntent)
            }

        for (intent in candidates) {
            try {
                if (context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
                    bindRequested = true
                    log("Bind requested", intent.component?.flattenToShortString() ?: intent.action)
                    return
                }
            } catch (e: SecurityException) {
                L.w(e, "Topway command service bind rejected")
                log("Bind rejected", e.javaClass.simpleName)
            } catch (e: RuntimeException) {
                L.w(e, "Topway command service bind failed")
                log("Bind failed", e.javaClass.simpleName)
            }
        }
        scheduleRetry("bind returned false")
    }

    private fun establishRemote(service: IBinder) {
        if (!attached) {
            mainHandler.post(::unbindIfNeeded)
            return
        }

        val descriptor =
            try {
                service.interfaceDescriptor
            } catch (e: RemoteException) {
                L.w(e, "Unable to read Topway command service descriptor")
                mainHandler.post { disconnectAndRetry("descriptor read failed") }
                return
            }
        if (descriptor != TopwayCommandServiceContract.COMMAND_DESCRIPTOR) {
            log("STOP adapter: descriptor mismatch", descriptor)
            mainHandler.post {
                unbindIfNeeded()
                mainHandler.removeCallbacks(retryRunnable)
                retryScheduled = false
            }
            return
        }

        remote = service
        val recipient =
            IBinder.DeathRecipient {
                workerHandler?.post {
                    log("Binder died")
                    clearRemote(unregister = false)
                    mainHandler.post { disconnectAndRetry("binder died") }
                }
            }
        deathRecipient = recipient
        try {
            service.linkToDeath(recipient, 0)
        } catch (e: RemoteException) {
            L.w(e, "Topway command service died during registration")
            clearRemote(unregister = false)
            mainHandler.post { disconnectAndRetry("link-to-death failed") }
            return
        }

        if (
            !transactCallback(
                service,
                TopwayCommandServiceContract.CommandTransaction.REGISTER_MUSIC_CALLBACK,
                musicCallback,
            )
        ) {
            log("Music callback registration failed")
            clearRemote(unregister = false)
            mainHandler.post { disconnectAndRetry("music callback registration failed") }
            return
        }
        musicCallbackRegistered = true
        log("Music callback registered")

        commandCallbackRegistered =
            transactCallback(
                service,
                TopwayCommandServiceContract.CommandTransaction.REGISTER_COMMAND_CALLBACK,
                commandCallback,
            )
        if (commandCallbackRegistered) {
            log("Command callback registered")
            requestSource(service)
        } else {
            log("Command callback unavailable", "music controls remain registered")
        }
    }

    private fun requestSource(service: IBinder) {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            data.writeInterfaceToken(TopwayCommandServiceContract.COMMAND_DESCRIPTOR)
            data.writeInt(1)
            TopwayCommandServiceContract.sourceRequest().writeToParcel(data, 0)
            if (
                service.transact(
                    TopwayCommandServiceContract.CommandTransaction.EXTENDED_INTERFACE,
                    data,
                    reply,
                    0,
                )
            ) {
                reply.readException()
                log("Source requested")
            } else {
                log("Source request unsupported")
            }
        } catch (e: RemoteException) {
            L.w(e, "Topway source request failed")
            log("Source request failed", e.javaClass.simpleName)
        } catch (e: RuntimeException) {
            L.w(e, "Topway source request could not be marshalled")
            log("Source request failed", e.javaClass.simpleName)
        } finally {
            reply.recycle()
            data.recycle()
        }
    }

    private fun transactCallback(service: IBinder, code: Int, callback: IBinder): Boolean {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        return try {
            data.writeInterfaceToken(TopwayCommandServiceContract.COMMAND_DESCRIPTOR)
            data.writeStrongBinder(callback)
            if (!service.transact(code, data, reply, 0)) return false
            reply.readException()
            true
        } catch (e: RemoteException) {
            L.w(e, "Topway callback transaction $code failed")
            false
        } catch (e: RuntimeException) {
            L.w(e, "Topway callback transaction $code could not be marshalled")
            false
        } finally {
            reply.recycle()
            data.recycle()
        }
    }

    private fun clearRemote(unregister: Boolean) {
        val service = remote
        if (service != null && unregister) {
            if (musicCallbackRegistered) {
                transactCallback(
                    service,
                    TopwayCommandServiceContract.CommandTransaction.UNREGISTER_MUSIC_CALLBACK,
                    musicCallback,
                )
            }
            if (commandCallbackRegistered) {
                transactCallback(
                    service,
                    TopwayCommandServiceContract.CommandTransaction.UNREGISTER_COMMAND_CALLBACK,
                    commandCallback,
                )
            }
        }
        musicCallbackRegistered = false
        commandCallbackRegistered = false

        val recipient = deathRecipient
        if (service != null && recipient != null) {
            try {
                service.unlinkToDeath(recipient, 0)
            } catch (_: RuntimeException) {
                // The remote process may already be gone. No owned resource remains to recover.
            }
        }
        deathRecipient = null
        remote = null
    }

    private fun disconnectAndRetry(reason: String) {
        unbindIfNeeded()
        if (attached) scheduleRetry(reason)
    }

    private fun unbindIfNeeded() {
        if (!bindRequested) return
        bindRequested = false
        try {
            context.unbindService(connection)
        } catch (_: IllegalArgumentException) {
            // Android already removed the dead binding.
        } catch (e: RuntimeException) {
            L.w(e, "Unable to unbind Topway command service")
        }
    }

    private fun scheduleRetry(reason: String) {
        if (!attached || retryScheduled) return
        if (retryCount >= RETRY_DELAYS_MS.size) {
            log("Reconnect exhausted", reason)
            return
        }
        val delayMs = RETRY_DELAYS_MS[retryCount]
        retryCount += 1
        retryScheduled = true
        log("Reconnect scheduled", "$reason; attempt=$retryCount delayMs=$delayMs")
        mainHandler.postDelayed(retryRunnable, delayMs)
    }

    private fun onMusicControl(control: TopwayMusicControl) {
        mainHandler.post {
            val mode = coordinator.mode
            log("Music callback", "${control.name}; mode=${mode.name}")
            if (!attached || mode.diagnosticsOnly || !mode.handlesTopwayCommands) return@post

            val serviceClass = ownerServiceClass ?: return@post
            val intent =
                Intent(Intent.ACTION_MEDIA_BUTTON)
                    .setClass(context, serviceClass)
                    .putExtra(
                        Intent.EXTRA_KEY_EVENT,
                        KeyEvent(KeyEvent.ACTION_DOWN, control.mediaKeyCode),
                    )
                    .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_MEDIA_BUTTON)
            try {
                ContextCompat.startForegroundService(context, intent)
            } catch (e: IllegalStateException) {
                L.w(e, "Unable to dispatch Topway media control due to service state")
                log("Control dispatch failed", e.javaClass.simpleName)
            } catch (e: SecurityException) {
                L.w(e, "Unable to dispatch Topway media control due to security policy")
                log("Control dispatch failed", e.javaClass.simpleName)
            } catch (e: RuntimeException) {
                L.w(e, "Unable to dispatch Topway media control")
                log("Control dispatch failed", e.javaClass.simpleName)
            }
        }
    }

    private fun onCommandExtended(bundle: Bundle?) {
        val source = TopwayCommandServiceContract.parseSource(bundle)
        if (source != null) {
            log("Source received", "value=${source.value}; kind=${source.kind}")
        } else {
            logBundle("Command extended callback", bundle)
        }
    }

    private fun logBundle(event: String, bundle: Bundle?) {
        val keys =
            try {
                bundle?.keySet()?.sorted()?.take(MAX_LOGGED_BUNDLE_KEYS)?.joinToString(",")
            } catch (_: RuntimeException) {
                null
            }
        log(event, keys ?: "empty")
    }

    private fun log(event: String, detail: String? = null) {
        journal.log(DiagnosticJournal.CAT_TOPWAY_CMD, event, detail)
        L.i("Topway command service: $event detail=$detail")
    }

    private companion object {
        const val WORKER_NAME = "AuxioTopwayCommand"
        const val MAX_LOGGED_BUNDLE_KEYS = 16
        val RETRY_DELAYS_MS = longArrayOf(500L, 1500L, 3000L)
    }
}

/** Local Binder implementing the observed `IMusicCallBack` transaction table. */
internal class TopwayMusicCallbackBinder(
    private val onControl: (TopwayMusicControl) -> Unit,
    private val onMode: (Int) -> Unit,
    private val onExtended: (Bundle?) -> Unit,
) : Binder() {
    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        if (code == IBinder.INTERFACE_TRANSACTION) {
            reply?.writeString(TopwayCommandServiceContract.MUSIC_CALLBACK_DESCRIPTOR)
            return true
        }
        val control = TopwayMusicControl.fromTransaction(code)
        if (
            control == null &&
                code != TopwayCommandServiceContract.MusicCallbackTransaction.MODE &&
                code != TopwayCommandServiceContract.MusicCallbackTransaction.EXTENDED_INTERFACE
        ) {
            return super.onTransact(code, data, reply, flags)
        }
        data.enforceInterface(TopwayCommandServiceContract.MUSIC_CALLBACK_DESCRIPTOR)
        when {
            control != null -> onControl(control)
            code == TopwayCommandServiceContract.MusicCallbackTransaction.MODE -> onMode(data.readInt())
            else -> onExtended(data.readNullableBundle())
        }
        reply?.writeNoException()
        return true
    }
}

/** Local Binder implementing the observed `ITWCommandCallbackAidl` transaction table. */
internal class TopwayCommandCallbackBinder(
    private val onStatus: (String, String) -> Unit,
    private val onExtended: (Bundle?) -> Unit,
) : Binder() {
    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        if (code == IBinder.INTERFACE_TRANSACTION) {
            reply?.writeString(TopwayCommandServiceContract.COMMAND_CALLBACK_DESCRIPTOR)
            return true
        }
        if (
            code !in
                TopwayCommandServiceContract.CommandCallbackTransaction.SYSTEM_VOLUME..
                    TopwayCommandServiceContract.CommandCallbackTransaction.EXTENDED_INTERFACE
        ) {
            return super.onTransact(code, data, reply, flags)
        }
        data.enforceInterface(TopwayCommandServiceContract.COMMAND_CALLBACK_DESCRIPTOR)
        when (code) {
            TopwayCommandServiceContract.CommandCallbackTransaction.SYSTEM_VOLUME ->
                onStatus("System volume", data.readInt().toString())
            TopwayCommandServiceContract.CommandCallbackTransaction.VOLUME_STATUS ->
                onStatus("Volume status", data.readInt().toString())
            TopwayCommandServiceContract.CommandCallbackTransaction.BT_PHONE_STATUS ->
                onStatus("Bluetooth phone status", data.readInt().toString())
            TopwayCommandServiceContract.CommandCallbackTransaction.BT_CALL_STATUS -> {
                val status = data.readInt()
                data.readString()
                data.readString()
                onStatus("Bluetooth call status", status.toString())
            }
            TopwayCommandServiceContract.CommandCallbackTransaction.BT_CONNECTED_STATUS ->
                onStatus("Bluetooth connected status", data.readInt().toString())
            TopwayCommandServiceContract.CommandCallbackTransaction.REVERSE_STATUS ->
                onStatus("Reverse status", data.readInt().toString())
            TopwayCommandServiceContract.CommandCallbackTransaction.SLEEP_STATUS ->
                onStatus("Sleep status", data.readInt().toString())
            TopwayCommandServiceContract.CommandCallbackTransaction.EXTENDED_INTERFACE ->
                onExtended(data.readNullableBundle())
        }
        reply?.writeNoException()
        return true
    }
}

private fun Parcel.readNullableBundle(): Bundle? =
    if (readInt() != 0) Bundle.CREATOR.createFromParcel(this) else null
