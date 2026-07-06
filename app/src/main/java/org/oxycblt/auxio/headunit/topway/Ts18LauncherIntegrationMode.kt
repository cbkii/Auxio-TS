package org.oxycblt.auxio.headunit.topway

import org.oxycblt.auxio.BuildConfig

enum class Ts18LauncherIntegrationMode {
    Disabled,
    AndroidMediaSessionOnly,
    TopwayBroadcastOnly,
    TopwayCommandOnly,
    TopwayBroadcastAndCommand,
    AutoAllSafePaths,
    DiagnosticsOnly;

    val sendsTopwayBroadcasts: Boolean
        get() =
            this == TopwayBroadcastOnly ||
                this == TopwayBroadcastAndCommand ||
                this == AutoAllSafePaths

    val handlesTopwayCommands: Boolean
        get() =
            this == TopwayCommandOnly ||
                this == TopwayBroadcastAndCommand ||
                this == AutoAllSafePaths

    val diagnosticsOnly: Boolean
        get() = this == DiagnosticsOnly

    companion object {
        const val PREF_KEY = "auxio_ts18_launcher_integration_mode"
        fun default(): Ts18LauncherIntegrationMode =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) AutoAllSafePaths else AndroidMediaSessionOnly
        fun fromPreference(value: String?): Ts18LauncherIntegrationMode =
            entries.firstOrNull { it.name == value } ?: default()
    }
}
