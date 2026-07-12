# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Obsfucation is what proprietary software does to keep the user unaware of it's abuses.
# Also it's easier to fix issues if the stack trace symbols remain unmangled.
-dontobfuscate

# Make AGP shut up about classes that aren't even used.
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

# Topway/TS18 car overlay classes accessed via reflection in Auxio.kt.
# -dontobfuscate prevents renaming but R8 can still strip unreachable code;
# keep these so the reflective registration path always finds them.
-keep class org.oxycblt.auxio.car.overlay.CarOverlayVisibilityHooks { *; }
-keep class org.oxycblt.auxio.car.overlay.CarOverlaySettings { *; }
# Manifest-declared Topway/DoFun wrapper components. Android instantiates these classes directly
# from the manifest before Auxio can recover, so release shrinking must never remove them while the
# topwayCompat manifest declares them. They are thin public Android-standard wrappers only, not
# private Cardoor/vendor implementations.
-keep class com.tw.music.MusicService { *; }
-keep class com.tw.music.view.MusicWidgetProvider { *; }
-keep class org.oxycblt.auxio.car.overlay.CarFloatingControlsService { *; }
-keep class org.oxycblt.auxio.car.overlay.CarOverlayBootReceiver { *; }
-keep class org.oxycblt.auxio.car.overlay.CarOverlayPermissionActivity { *; }
-keep class org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity { *; }

# Main manifest-declared components to ensure they are kept through R8 shrinking.
-keep class org.oxycblt.auxio.image.CoverProvider { *; }
-keep class org.oxycblt.auxio.playback.service.MediaButtonReceiver { *; }
-keep class org.oxycblt.auxio.headunit.topway.TopwayMusicBridgeReceiver { *; }
-keep class org.oxycblt.auxio.widgets.WidgetProvider { *; }
-keep class org.oxycblt.auxio.tasker.ActivityConfigStartAction { *; }
