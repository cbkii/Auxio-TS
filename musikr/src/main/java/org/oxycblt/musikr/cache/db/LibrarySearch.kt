/*
 * Copyright (c) 2026 Auxio Project
 * LibrarySearch.kt is part of Auxio.
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

package org.oxycblt.musikr.cache.db

import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ensureActive

/**
 * Pure helpers for building safe SQLite `LIKE` patterns.
 *
 * SQLite `LIKE` treats `%` and `_` as wildcards. Without escaping, a user typing `50%` would match
 * anything starting with `50`, and `a_b` would match `axb`. [LibraryReadDao.searchSongs] pairs the
 * output of [contains] with an `ESCAPE '\'` clause so these characters match literally.
 */
internal object LikeQuery {
    /** The escape character used in the paired `ESCAPE '\'` clause. */
    const val ESCAPE = '\\'

    /**
     * Escape every `LIKE` metacharacter (`\`, `%`, `_`) in [term] so it is matched literally.
     *
     * The escape character itself is escaped first to avoid double-processing.
     */
    fun escape(term: String): String {
        val out = StringBuilder(term.length)
        for (c in term) {
            when (c) {
                ESCAPE,
                '%',
                '_' -> out.append(ESCAPE)
            }
            out.append(c)
        }
        return out.toString()
    }

    /**
     * Build a "contains" pattern (`%term%`) whose interior is fully escaped.
     *
     * The surrounding `%` remain active wildcards; only characters from [term] are neutralised.
     */
    fun contains(term: String): String = "%${escape(term.trim())}%"
}

/**
 * Bounded, cancellable, obsolete-query-suppressing coordinator over a database-backed song search.
 *
 * This is a database-first replacement building block for the legacy full-library in-memory search:
 * results come from a bounded, paged query rather than materialising and filtering the entire
 * [org.oxycblt.musikr.Library] graph in memory.
 *
 * Two correctness properties required for a responsive search-as-you-type UI are enforced here:
 * - **Cancellation:** each [search] cooperatively checks for coroutine cancellation, so abandoning
 *   a keystroke abandons its query.
 * - **Obsolete-query suppression:** a monotonically increasing token means that if a newer query
 *   starts while an older one is still resolving, the older one resolves to
 *   [SearchResult.Superseded] instead of overwriting fresher results.
 */
internal class LibrarySearcher(
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val source: Source,
) {
    /** Minimal abstraction over [LibraryReadDao] so the coordinator is unit-testable. */
    fun interface Source {
        suspend fun searchSongs(pattern: String, limit: Int, offset: Int): List<SongListRow>
    }

    private val latestToken = AtomicLong(0)

    /**
     * Run a bounded search for [rawQuery], returning at most [limit] rows from page [page].
     *
     * A blank query or non-positive limit yields no results without touching the database. Limits
     * are capped at [MAX_PAGE_SIZE], and the page is clamped before offset multiplication so
     * hostile or accidental values cannot overflow the DAO's integer offset. Returns
     * [SearchResult.Superseded] if a newer [search] began before this one finished.
     */
    suspend fun search(rawQuery: String, limit: Int = pageSize, page: Int = 0): SearchResult {
        val token = latestToken.incrementAndGet()
        val trimmed = rawQuery.trim()
        if (trimmed.isEmpty() || limit <= 0) {
            return SearchResult.Results(emptyList())
        }
        coroutineContext.ensureActive()
        val boundedLimit = limit.coerceAtMost(MAX_PAGE_SIZE)
        val boundedPage = page.coerceIn(0, Int.MAX_VALUE / boundedLimit)
        val offset = boundedPage * boundedLimit
        val rows = source.searchSongs(LikeQuery.contains(trimmed), boundedLimit, offset)
        coroutineContext.ensureActive()
        if (token != latestToken.get()) {
            return SearchResult.Superseded
        }
        return SearchResult.Results(rows)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 50
        const val MAX_PAGE_SIZE = 100
    }
}

/** Outcome of a [LibrarySearcher.search] invocation. */
internal sealed interface SearchResult {
    /** The query completed and its [rows] are the freshest results. */
    data class Results(val rows: List<SongListRow>) : SearchResult

    /** A newer query superseded this one; its rows must be discarded. */
    data object Superseded : SearchResult
}
