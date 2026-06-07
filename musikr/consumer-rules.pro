# TagLibJNI declares the `openNative` external (JNI) method that the native
# libtagJNI.so resolves by its fully-qualified name. R8 must never strip or rename
# it, otherwise release builds fail to bind the native method at runtime.
-keep class org.oxycblt.musikr.metadata.TagLibJNI { *; }
-keep class org.oxycblt.musikr.metadata.NativeInputStream { *; }
-keep class org.oxycblt.musikr.metadata.Metadata { *; }
-keep class org.oxycblt.musikr.metadata.Properties { *; }
-keep class org.oxycblt.musikr.metadata.NativeTagMap { *; }
-keep class org.oxycblt.musikr.metadata.MetadataResult { *; }
-keep class org.oxycblt.musikr.metadata.MetadataResult$Success { *; }
-keep class org.oxycblt.musikr.metadata.MetadataResult$NoMetadata { *; }
-keep class org.oxycblt.musikr.metadata.MetadataResult$NotAudio { *; }
-keep class org.oxycblt.musikr.metadata.MetadataResult$ProviderFailed { *; }
