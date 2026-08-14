/*
 * Copyright (c) 2026 Auxio Project
 * MusicService.kt is part of Auxio.
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

package com.tw.music

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.os.Process
import android.os.SystemClock
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.playback.service.TopwayBridgeAdmissionResult
import org.oxycblt.auxio.playback.service.TopwayBridgePlaybackIngress
import org.oxycblt.auxio.ts18bridge.BridgeWireContract
import org.oxycblt.auxio.ts18bridge.TopwayBridgeCommandLedger
import timber.log.Timber

/**
 * Thin stock-name wrapper used by the maintained `com.tw.media` product.
 *
 * <p>The narrow Binder below is only a Track-C command admission surface. All playback remains
 * owned by [AuxioService] and its singleton playback state manager. This subclass deliberately is
 * not an `@AndroidEntryPoint`; the parent already performs Hilt injection during `onCreate`.
 */
class MusicService : AuxioService() {
    private val commandLedger = TopwayBridgeCommandLedger()

    private val playbackIngress: TopwayBridgePlaybackIngress by
        lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            EntryPointAccessors.fromApplication(applicationContext, BridgeEntryPoint::class.java)
                .playbackIngress()
        }

    private val bridgeBinder =
        object : Binder() {
            init {
                attachInterface(null, BridgeWireContract.BINDER_DESCRIPTOR)
            }

            override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
                if (code != BridgeWireContract.TRANSACTION_DISPATCH) {
                    return super.onTransact(code, data, reply, flags)
                }
                // Track C is explicitly an acknowledged protocol. Ignore one-way or reply-less
                // transactions rather than executing a command that cannot positively acknowledge
                // admission to the caller before stock behaviour is suppressed.
                if ((flags and IBinder.FLAG_ONEWAY) != 0 || reply == null) return true

                val response =
                    try {
                        dispatchBridgeTransaction(data)
                    } catch (error: RuntimeException) {
                        Timber.w(error, "Rejecting malformed Track-C Binder transaction")
                        BridgeWireContract.RESULT_INVALID
                    }
                reply.writeNoException()
                reply.writeInt(response)
                return true
            }
        }

    override fun onBind(intent: Intent): IBinder? {
        if (intent.action == BridgeWireContract.ACTION_BIND_COMMAND) {
            // onCreate() already attached the playback authority. Start only background library
            // readiness here: AuxioService.onBind would interpret this private protocol Intent as
            // a non-native playback start and enqueue RestoreState(play = false).
            startBackgroundLibraryForPrivateBind()
            return bridgeBinder
        }
        return super.onBind(intent)
    }

    private fun dispatchBridgeTransaction(data: Parcel): Int {
        data.enforceInterface(BridgeWireContract.BINDER_DESCRIPTOR)

        val protocolVersion = data.readInt()
        val commandId = data.readLong()
        val commandType = data.readInt()
        val seekPositionMs = data.readLong()
        val sourceAdapter = data.readInt()
        val clientGeneration = data.readLong()
        val createdElapsedMs = data.readLong()
        val deadlineElapsedMs = data.readLong()
        if (data.dataAvail() != 0) return BridgeWireContract.RESULT_INVALID

        if (protocolVersion != BridgeWireContract.PROTOCOL_VERSION) {
            return BridgeWireContract.RESULT_VERSION_MISMATCH
        }
        if (!isTrustedCaller()) return BridgeWireContract.RESULT_UNTRUSTED
        if (
            sourceAdapter != BridgeWireContract.SOURCE_STOCK_SHIM ||
                commandId == 0L ||
                clientGeneration == 0L ||
                !BridgeWireContract.isSupportedCommand(commandType)
        ) {
            return BridgeWireContract.RESULT_INVALID
        }
        if (commandType == BridgeWireContract.COMMAND_SEEK && seekPositionMs < 0L) {
            return BridgeWireContract.RESULT_INVALID
        }

        val nowMs = SystemClock.elapsedRealtime()
        if (
            createdElapsedMs <= 0L ||
                deadlineElapsedMs <= createdElapsedMs ||
                deadlineElapsedMs - createdElapsedMs > MAX_REQUEST_LIFETIME_MS ||
                createdElapsedMs > nowMs + MAX_CLOCK_SKEW_MS
        ) {
            return BridgeWireContract.RESULT_INVALID
        }
        if (nowMs >= deadlineElapsedMs) return BridgeWireContract.RESULT_EXPIRED

        when (commandLedger.reserve(clientGeneration, commandId, nowMs)) {
            TopwayBridgeCommandLedger.Reservation.DUPLICATE_ACCEPTED ->
                return BridgeWireContract.RESULT_DUPLICATE_ACCEPTED
            TopwayBridgeCommandLedger.Reservation.BUSY -> return BridgeWireContract.RESULT_BUSY
            TopwayBridgeCommandLedger.Reservation.RESERVED -> Unit
        }

        return try {
            when (playbackIngress.admitResult(commandType, seekPositionMs, deadlineElapsedMs)) {
                TopwayBridgeAdmissionResult.ACCEPTED -> {
                    commandLedger.markAccepted(
                        clientGeneration,
                        commandId,
                        SystemClock.elapsedRealtime(),
                    )
                    BridgeWireContract.RESULT_ACCEPTED
                }
                TopwayBridgeAdmissionResult.NOT_READY -> {
                    commandLedger.release(clientGeneration, commandId)
                    BridgeWireContract.RESULT_NOT_READY
                }
                TopwayBridgeAdmissionResult.INVALID -> {
                    commandLedger.release(clientGeneration, commandId)
                    BridgeWireContract.RESULT_INVALID
                }
                TopwayBridgeAdmissionResult.EXPIRED -> {
                    commandLedger.release(clientGeneration, commandId)
                    BridgeWireContract.RESULT_EXPIRED
                }
                TopwayBridgeAdmissionResult.INTERRUPTED,
                TopwayBridgeAdmissionResult.ERROR -> {
                    commandLedger.release(clientGeneration, commandId)
                    BridgeWireContract.RESULT_ERROR
                }
            }
        } catch (error: RuntimeException) {
            commandLedger.release(clientGeneration, commandId)
            Timber.w(error, "Track-C Binder command admission failed")
            BridgeWireContract.RESULT_ERROR
        }
    }

    /**
     * The compatibility service is exported for MediaBrowser clients, so keep a cheap Binder caller
     * boundary on the private command transaction. Looking up the installed com.tw.music signer
     * does not authenticate this Binder caller (especially for shared UID 1000), so UID is the
     * meaningful boundary here and LSPosed package scope remains a separate concern.
     */
    private fun isTrustedCaller(): Boolean = Binder.getCallingUid() == Process.SYSTEM_UID

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface BridgeEntryPoint {
        fun playbackIngress(): TopwayBridgePlaybackIngress
    }

    private companion object {
        const val MAX_REQUEST_LIFETIME_MS = 250L
        const val MAX_CLOCK_SKEW_MS = 1_000L
    }
}
