/*
 * Copyright (c) 2021 Auxio Project
 * LangUtil.kt is part of Auxio.
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

package org.oxycblt.musikr.util

import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import org.oxycblt.musikr.BuildConfig
import org.oxycblt.musikr.tag.Date

/**
 * Runs [block] in a new coroutine and captures failures raised while that coroutine is still active.
 *
 * Actual cancellation of this coroutine is rethrown so structured concurrency and user cancellation
 * remain distinguishable from a peer-channel cancellation or a block that throws
 * [CancellationException] as its own failure signal.
 */
fun CoroutineScope.tryAsync(
    context: CoroutineContext,
    block: suspend () -> Unit,
): Deferred<Result<Unit>> =
    async(context) {
        try {
            block()
            Result.success(Unit)
        } catch (e: CancellationException) {
            rethrowIfSelfCancelled(e)
            Result.failure(e.unwrapPipelineCause())
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

/**
 * Runs [block] as the sole owner of [channel].
 *
 * Ownership contract:
 * - normal completion closes [channel] normally, even if nothing was ever sent;
 * - a fatal failure closes [channel] with the causal exception so every consumer observes it;
 * - cancellation cancels [channel] so both consumers and any suspended producer unwind.
 */
fun <T> CoroutineScope.tryAsyncWith(
    channel: Channel<T>,
    context: CoroutineContext,
    block: suspend (Channel<T>) -> Unit,
): Deferred<Result<Unit>> =
    async(context) {
        try {
            block(channel)
            channel.close()
            Result.success(Unit)
        } catch (e: CancellationException) {
            channel.cancel(e)
            rethrowIfSelfCancelled(e)
            Result.failure(e.unwrapPipelineCause())
        } catch (e: Throwable) {
            channel.close(e)
            Result.failure(e)
        }
    }

fun <T, R> CoroutineScope.map(
    input: Channel<T>,
    output: Channel<R>,
    context: CoroutineContext = Dispatchers.Default,
    block: suspend (T) -> R?,
): Deferred<Result<Unit>> = mapParallelInternal(1, input, output, context) { block(it) }

/**
 * Maps [input] into [output] with [n] parallel workers.
 *
 * Ownership contract:
 * - the first failed worker cancels its siblings without allocating a second waiter coroutine for
 *   every worker;
 * - [output] is closed normally only when every worker completed successfully;
 * - on failure [output] is closed with the causal exception and [input] is cancelled with it, so a
 *   suspended upstream producer fails fast instead of blocking forever on back-pressure;
 * - on cancellation both channels are cancelled.
 */
fun <T, R> CoroutineScope.mapParallel(
    n: Int,
    input: Channel<T>,
    output: Channel<R>,
    context: CoroutineContext = Dispatchers.Default,
    block: suspend (T) -> R,
): Deferred<Result<Unit>> = mapParallelInternal(n, input, output, context, block)

private fun <T, R> CoroutineScope.mapParallelInternal(
    n: Int,
    input: Channel<T>,
    output: Channel<R>,
    context: CoroutineContext,
    block: suspend (T) -> R?,
): Deferred<Result<Unit>> =
    async(context) {
        try {
            coroutineScope {
                val workers =
                    List(n.coerceAtLeast(1)) {
                        tryAsync(context) {
                            for (item in input) {
                                block(item)?.let { output.send(it) }
                            }
                        }
                    }
                workers.tryAwaitAll()
            }
            output.close()
            Result.success(Unit)
        } catch (e: CancellationException) {
            input.cancel(e)
            output.cancel(e)
            rethrowIfSelfCancelled(e)
            Result.failure(e.unwrapPipelineCause())
        } catch (e: Throwable) {
            // Cancel the input so a producer suspended on back-pressure unwinds immediately, and
            // close the output with the cause so downstream consumers observe the original error.
            input.cancel(CancellationException("Upstream pipeline stage failed", e))
            output.close(e)
            Result.failure(e)
        }
    }

/**
 * Awaits every task and cancels unfinished siblings as soon as the first task fails.
 *
 * Completion handlers observe the existing deferreds directly. This avoids the previous one-waiter
 * coroutine per deferred while still detecting a later-list failure when an earlier task is blocked.
 * A task that is independently cancelled, or that completes with
 * `Result.failure(CancellationException)`, is a failed stage unless the caller's own job is cancelled.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun List<Deferred<Result<Unit>>>.tryAwaitAll() {
    if (isEmpty()) return

    val remaining = AtomicInteger(size)
    val completion = CompletableDeferred<Result<Unit>>()
    val handles = ArrayList<DisposableHandle>(size)

    for (deferred in this) {
        handles +=
            deferred.invokeOnCompletion { completionCause ->
                val result: Result<Unit> =
                    if (completionCause != null) {
                        Result.failure(completionCause.unwrapPipelineCause())
                    } else {
                        try {
                            deferred.getCompleted()
                        } catch (e: Throwable) {
                            Result.failure(e)
                        }
                    }

                if (result.isFailure) {
                    if (completion.complete(result)) {
                        cancelAll(requireNotNull(result.exceptionOrNull()))
                    }
                } else if (remaining.decrementAndGet() == 0) {
                    completion.complete(Result.success(Unit))
                }
            }
    }

    try {
        val result = completion.await()
        // Prefer genuine parent/user cancellation if it races a stage completion callback.
        currentCoroutineContext().ensureActive()
        result.getOrThrow()
    } finally {
        handles.forEach(DisposableHandle::dispose)
    }
}

/**
 * Merges several pipeline tasks into a single task that fails fast.
 *
 * The first failure cancels the remaining tasks with the causal exception, which in turn lets each
 * stage close or cancel the channels it owns. The original causal chain is preserved in the result.
 */
fun CoroutineScope.merge(vararg deferreds: Deferred<Result<Unit>>): Deferred<Result<Unit>> =
    tryAsync(Dispatchers.Default) { deferreds.toList().tryAwaitAll() }

/** Rethrows [e] when this coroutine's own job has been cancelled. */
private suspend fun rethrowIfSelfCancelled(e: CancellationException) {
    try {
        currentCoroutineContext().ensureActive()
    } catch (_: CancellationException) {
        throw e
    }
}

/** Unwraps the causal exception a peer stage attached when it cancelled a shared channel. */
private fun Throwable.unwrapPipelineCause(): Throwable =
    if (this is CancellationException) cause ?: this else this

private fun List<Deferred<Result<Unit>>>.cancelAll(cause: Throwable) {
    val cancellation =
        cause as? CancellationException
            ?: CancellationException("Sibling pipeline task failed", cause)
    forEach { deferred ->
        if (deferred.isActive) {
            deferred.cancel(cancellation)
        }
    }
}

/**
 * Sanitizes a value that is unlikely to be null. On debug builds, this aliases to [requireNotNull],
 * otherwise, it aliases to the unchecked dereference operator (!!). This can be used as a minor
 * optimization in certain cases.
 */
internal fun <T> unlikelyToBeNull(value: T?) =
    if (BuildConfig.DEBUG) {
        requireNotNull(value)
    } else {
        value!!
    }

/**
 * Aliases a check to ensure that the given number is non-zero.
 *
 * @return The given number if it's non-zero, null otherwise.
 */
internal fun Int.positiveOrNull() = if (this > 0) this else null

/**
 * Aliases a check to ensure that the given number is non-zero.
 *
 * @return The same number if it's non-zero, null otherwise.
 */
internal fun Float.nonZeroOrNull() = if (this != 0f) this else null

/**
 * Aliases a check to ensure a given value is in a specified range.
 *
 * @param range The valid range of values for this number.
 * @return The same number if it is in the range, null otherwise.
 */
internal fun Int.inRangeOrNull(range: IntRange) = if (range.contains(this)) this else null

/**
 * Convert a [String] to a [UUID].
 *
 * @return A [UUID] converted from the [String] value, or null if the value was not valid.
 * @see UUID.fromString
 */
internal fun String.toUuidOrNull(): UUID? =
    try {
        UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        null
    }

/**
 * Update a [MessageDigest] with a lowercase [String].
 *
 * @param string The [String] to hash. If null, it will not be hashed.
 */
internal fun MessageDigest.update(string: String?) {
    if (string != null) {
        update(string.lowercase().toByteArray())
    } else {
        update(0)
    }
}

/**
 * Update a [MessageDigest] with the string representation of a [Date].
 *
 * @param date The [Date] to hash. If null, nothing will be done.
 */
internal fun MessageDigest.update(date: Date?) {
    if (date != null) {
        update(date.toString().toByteArray())
    } else {
        update(0)
    }
}

/**
 * Update a [MessageDigest] with the lowercase versions of all of the input [String]s.
 *
 * @param strings The [String]s to hash. If a [String] is null, it will not be hashed.
 */
internal fun MessageDigest.update(strings: List<String?>) {
    strings.forEach(::update)
}

/**
 * Update a [MessageDigest] with the little-endian bytes of a [Int].
 *
 * @param n The [Int] to write. If null, nothing will be done.
 */
internal fun MessageDigest.update(n: Int?) {
    if (n != null) {
        update(byteArrayOf(n.toByte(), n.shr(8).toByte(), n.shr(16).toByte(), n.shr(24).toByte()))
    } else {
        update(0)
    }
}

/**
 * Lazily set up a reflected method. Automatically handles visibility changes. Adapted from Material
 * Files: https://github.com/zhanghai/MaterialFiles
 *
 * @param clazz The [KClass] to reflect into.
 * @param method The name of the method to obtain.
 */
internal fun lazyReflectedMethod(clazz: KClass<*>, method: String, vararg params: KClass<*>) =
    lazy {
        clazz.java.getDeclaredMethod(method, *params.map { it.java }.toTypedArray()).also {
            it.isAccessible = true
        }
    }
