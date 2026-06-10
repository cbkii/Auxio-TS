# Evidence excerpt: twmusic_metadata_broadcast_extras.md

   Source APK/variant: `com.tw.music_TW_THEME.20240715`
   Source path: `com.tw.music_TW_THEME.20240715/jadx/sources/com/eckom/xtlibrary/p020b/p037f/p041d/C0593L.java`
   Source lines: `1482-1490`
   Status: observation from static decompile/extract.
   Why it matters: One of multiple stock library code paths emitting com.tw.music.info with exact extra names, including the misspelt musicaArtist.

   ```java
    1482:     /* JADX INFO: renamed from: a */
1483:     private void m477a(String str, String str2, String str3, String str4) {
1484:         Intent intent = new Intent("com.tw.music.info");
1485:         intent.putExtra("musicTitle", str);
1486:         intent.putExtra("musicaArtist", str2);
1487:         intent.putExtra("musicAlbum", str3);
1488:         intent.putExtra("musicPath", str4);
1489:         this.mContext.sendBroadcast(intent);
1490:     }
   ```
