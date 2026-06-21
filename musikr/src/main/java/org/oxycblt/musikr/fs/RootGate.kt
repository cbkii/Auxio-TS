/*
 * Copyright (c) 2025 Auxio Project
 * RootGate.kt is part of Auxio.
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

package org.oxycblt.musikr.fs

/**
 * An interface for executing commands with root privileges.
 *
 * Used primarily for filesystem exploration on rooted head units where standard SAF or MediaStore
 * access is restricted or unreliable.
 */
interface RootGate {
    /**
     * Executes a command with root privileges and returns the output lines.
     *
     * @param command The shell command to run.
     * @param timeoutMs Maximum time to wait for the command to complete.
     * @return List of output lines if successful, null if failed, denied, or timed out.
     */
    fun runRootCommandSync(command: String, timeoutMs: Long = 5000): List<String>?
}
