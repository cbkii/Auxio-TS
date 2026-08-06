-keep class org.oxycblt.auxio.ts18bridge.** { *; }

-dontwarn **
-assumenosideeffects class android.** { *; }

-keep class !android.**,!androidx.**,!kotlin.**,!org.intellij.**,!org.jetbrains.**,!com.android.tools.** { *; }
