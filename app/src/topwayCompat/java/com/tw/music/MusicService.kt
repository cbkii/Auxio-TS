/*
 * Copyright (c) 2026 Auxio Project
 * MusicService.kt is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import org.oxycblt.auxio.playback.service.TopwayBridgePlaybackIngress
import org.oxycblt.auxio.ts18bridge.BridgeWireContract
import org.oxycblt.auxio.ts18bridge.TopwayBridgeCommandLedger
import timber.log.Timber

/**
 * Thin stock-name wrapper used by the maintained `com.tw.media` compatibility build.
 *
 * <p>The narrow Binder below is only a Track-C command admission surface. All playback remains
 * owned by [AuxioService] and its singleton playback state manager. This subclass deliberately is
 * not an `@AndroidEntryPoint`; the parent already performs Hilt injection during `onCreate`.
 */
class MusicService : AuxioService() {
    private val commandLedger = TopwayBridgeCommandLedger()

    private val playbackIngress: TopwayBridgePlaybackIngress by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        EntryPointAccessors.fromApplication(
                applicationContext,
                BridgeEntryPoint::class.java,
            )
            .playbackIngress()
    }

    private val bridgeBinder =
        object : Binder() {
            init {
                attachInterface(null, BridgeWireContract.BINDER_DESCRIPTOR)
            }

            override fun onTransact(
                code: Int,
                data: Parcel,
                reply: Parcel?,
                flags: Int,
            ): Boolean {
                if (code != BridgeWireContract.TRANSACTION_DISPATCH) {
                    return super.onTransact(code, data, reply, flags)
                }
                // Track C is explicitly an acknowledged protocol. Ignore one-way or reply-less
                // transactions rather than executing a command that cannot positively acknowledge
                // admission to the caller before stock behaviour is suppressed.
                if ((flags and IBinder.FLAG_ONEWAY) != 0 || reply == null) return true

                val response = dispatchBridgeTransaction(data)
                reply.writeNoException()
                reply.writeInt(response)
                return true
            }
        }

    override fun onBind(intent: Intent): IBinder? {
        if (intent.action == BridgeWireContract.ACTION_BIND_COMMAND) return bridgeBinder
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
            if (!playbackIngress.admit(commandType, seekPositionMs, deadlineElapsedMs)) {
                commandLedger.release(clientGeneration, commandId)
                if (SystemClock.elapsedRealtime() >= deadlineElapsedMs) {
                    BridgeWireContract.RESULT_EXPIRED
                } else {
                    BridgeWireContract.RESULT_NOT_READY
                }
            } else {
                commandLedger.markAccepted(
                    clientGeneration,
                    commandId,
                    SystemClock.elapsedRealtime(),
                )
                BridgeWireContract.RESULT_ACCEPTED
            }
        } catch (error: RuntimeException) {
            commandLedger.release(clientGeneration, commandId)
            Timber.w(error, "Track-C Binder command admission failed")
            BridgeWireContract.RESULT_ERROR
        }
    }

    private fun isTrustedCaller(): Boolean {
        val uid = Binder.getCallingUid()
        if (uid != Process.SYSTEM_UID) return false
        return try {
            // UID 1000 is shared, so this is only a supporting posture check. The callable surface
            // remains deliberately narrow and non-destructive even for another system-UID caller.
            packageManager.getPackagesForUid(uid)?.contains(STOCK_PACKAGE) == true
        } catch (error: RuntimeException) {
            Timber.w(error, "Unable to verify Track-C caller package posture")
            false
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface BridgeEntryPoint {
        fun playbackIngress(): TopwayBridgePlaybackIngress
    }

    private companion object {
        const val STOCK_PACKAGE = "com.tw.music"
        const val MAX_REQUEST_LIFETIME_MS = 250L
        const val MAX_CLOCK_SKEW_MS = 1_000L
    }
}
