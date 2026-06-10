# Evidence excerpt: navradio_on_get_session.md

   Source APK/variant: `NavRadio+_4.00_apks`
   Source path: `NavRadio+_4.00_apks/jadx/sources/com/navimods/radio/RadioService.java`
   Source lines: `3244-3247`
   Status: observation from static decompile/extract.
   Why it matters: NavRadio+ implements onGetSession by returning the mediaSession field; a manifest-only Media3 service would not be equivalent.

   ```java
    3244:     @Override // androidx.media3.session.MediaSessionService
3245:     public vn1 onGetSession(tn1 tn1Var) {
3246:         return this.mediaSession;
3247:     }
   ```
