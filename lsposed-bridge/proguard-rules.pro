# LSPosed instantiates this exact class name from META-INF/xposed/java_init.list.
-keep public class org.oxycblt.auxio.ts18bridge.Ts18LsposedBridgeModule { *; }

# R8 may optimise and shorten bridge class names, but it must not repackage them into the root
# namespace. The release DEX validator deliberately treats this package boundary as provenance.
-keeppackagenames org.oxycblt.auxio.ts18bridge.**

# All other bridge code is directly reachable from the entry class or generated Android code.
# Missing-class warnings intentionally remain enabled; do not add blanket -dontwarn rules.
