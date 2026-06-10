# Evidence excerpt: navradio_media3_session_creation.md

   Source APK/variant: `NavRadio+_4.00_apks`
   Source path: `NavRadio+_4.00_apks/jadx/sources/com/navimods/radio/RadioService.java`
   Source lines: `1937-2060`
   Status: observation from static decompile/extract.
   Why it matters: NavRadio+ binds as a real MediaSessionService and constructs a player, notification manager, session activity and mediaSession object.

   ```java
    1937:     @Override // androidx.media3.session.MediaSessionService, androidx.lifecycle.LifecycleService, android.app.Service
1938:     public IBinder onBind(Intent intent) {
1939:         return "androidx.media3.session.MediaSessionService".equals(intent.getAction()) ? super.onBind(intent) : this.mServiceBinder;
1940:     }
1941: 
1942:     @Override // androidx.media3.session.MediaSessionService, androidx.lifecycle.LifecycleService, android.app.Service
1943:     public void onCreate() {
1944:         boolean z;
1945:         xm1 xm1Var;
1946:         this.serviceStartTime = SystemClock.elapsedRealtime();
1947:         super.onCreate();
1948:         String str = TAG;
1949:         Log.d(str, "onCreate: START - Essential initialization only");
1950:         this.mGson = new C0331a();
1951:         this.sharedPreferences = new C0368h(this);
1952:         mAudioManager = (AudioManager) getSystemService("audio");
1953:         Log.d(str, "initializePlayerAndSession: START");
1954:         try {
1955:             this.player = new yu1(getMainLooper(), this);
1956:             C1215zd c1215zd = new C1215zd(2, 0, 1, 1, 0, false, true);
1957:             qm1 qm1Var = new qm1();
1958:             tm1 tm1Var = new tm1();
1959:             List list = Collections.EMPTY_LIST;
1960:             wg2 wg2Var = wg2.f20559e;
1961:             vm1 vm1Var = new vm1();
1962:             ym1 ym1Var = ym1.f21996d;
1963:             Uri uri = Uri.parse("");
1964:             fn1 fn1Var = new fn1();
1965:             fn1Var.f8188b = "";
1966:             fn1Var.f8187a = "";
1967:             gn1 gn1Var = new gn1(fn1Var);
1968:             lg4.m7329n(((Uri) tm1Var.f18674e) == null || ((UUID) tm1Var.f18673d) != null);
1969:             if (uri != null) {
1970:                 xm1Var = new xm1(uri, null, ((UUID) tm1Var.f18673d) != null ? new um1(tm1Var) : null, null, list, null, wg2Var, -9223372036854775807L);
1971:             } else {
1972:                 xm1Var = null;
1973:             }
1974:             bn1 bn1Var = new bn1("media-1", new sm1(qm1Var), xm1Var, new wm1(vm1Var), gn1Var, ym1Var);
1975:             this.player.mo9737n0(c1215zd, true);
1976:             this.player.mo7997w0(bn1Var);
1977:             this.player.mo9747u();
1978:             this.player.mo9740p(this.playerListener);
1979:             this.player.mo7992p0();
1980:             C0481fp c0481fp = new C0481fp(7);
1981:             int i = b92.exo_notification_small_icon;
1982:             int i2 = b92.exo_notification_play;
1983:             int i3 = b92.exo_notification_pause;
1984:             int i4 = b92.exo_notification_stop;
1985:             int i5 = b92.exo_notification_rewind;
1986:             int i6 = b92.exo_notification_fastforward;
1987:             int i7 = b92.exo_notification_previous;
1988:             int i8 = b92.exo_notification_next;
1989:             int i9 = ra2.playback_channel_name;
1990:             int i10 = ra2.playback_channel_description;
1991:             ek1 ek1Var = new ek1(this);
1992:             if (i9 != 0 && Build.VERSION.SDK_INT >= 26) {
1993:                 NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
1994:                 notificationManager.getClass();
1995:                 px1.m9249n();
1996:                 NotificationChannel notificationChannelM9238c = px1.m9238c(getString(i9));
1997:                 if (i10 != 0) {
1998:                     notificationChannelM9238c.setDescription(getString(i10));
1999:                 }
2000:                 notificationManager.createNotificationChannel(notificationChannelM9238c);
2001:             }
2002:             m22 m22Var = new m22(this, c0481fp, ek1Var, i, i2, i3, i4, i5, i6, i7, i8);
2003:             Handler handler = m22Var.f12410f;
2004:             yu1 yu1Var = this.player;
2005:             l22 l22Var = m22Var.f12413i;
2006:             lg4.m7329n(Looper.myLooper() == Looper.getMainLooper());
2007:             lg4.m7321f(yu1Var == null || yu1Var.f22188c == Looper.getMainLooper());
2008:             r12 r12Var = m22Var.f12421q;
2009:             if (r12Var != yu1Var) {
2010:                 if (r12Var != null) {
2011:                     r12Var.mo9749v0(l22Var);
2012:                     if (yu1Var == null) {
2013:                         m22Var.m7536c();
2014:                     }
2015:                 }
2016:                 m22Var.f12421q = yu1Var;
2017:                 if (yu1Var != null) {
2018:                     yu1Var.mo9740p(l22Var);
2019:                     if (!handler.hasMessages(1)) {
2020:                         handler.sendEmptyMessage(1);
2021:                     }
2022:                 }
2023:             }
2024:             if (m22Var.f12427w) {
2025:                 m22Var.f12427w = false;
2026:                 if (m22Var.f12422r && !handler.hasMessages(1)) {
2027:                     handler.sendEmptyMessage(1);
2028:                 }
2029:             }
2030:             if (m22Var.f12428x) {
2031:                 m22Var.f12428x = false;
2032:                 if (m22Var.f12422r && !handler.hasMessages(1)) {
2033:                     handler.sendEmptyMessage(1);
2034:                 }
2035:             }
2036:             pn1 pn1Var = new pn1(this, this.player);
2037:             String string = UUID.randomUUID().toString();
2038:             string.getClass();
2039:             pn1Var.f15755c = string;
2040:             PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) RadioActivity.class), 67108864);
2041:             if (Build.VERSION.SDK_INT >= 31) {
2042:                 lg4.m7321f(activity.isActivity());
2043:             }
2044:             activity.getClass();
2045:             pn1Var.f15757e = activity;
2046:             vn1 vn1VarM9112a = pn1Var.m9112a();
2047:             this.mediaSession = vn1VarM9112a;
2048:             MediaSession.Token tokenM9125g = vn1VarM9112a.f20006a.m9125g();
2049:             if (!Objects.equals(m22Var.f12424t, tokenM9125g)) {
2050:                 m22Var.f12424t = tokenM9125g;
2051:                 if (m22Var.f12422r && !handler.hasMessages(1)) {
2052:                     handler.sendEmptyMessage(1);
2053:                 }
2054:             }
2055:             this.player.m12481M1(3, Boolean.TRUE);
2056:             Log.d(str, "initializePlayerAndSession: Complete");
2057:         } catch (Exception e) {
2058:             Log.e(TAG, "initializePlayerAndSession: Error", e);
2059:         }
2060:         so2 so2Var = new so2(this);
   ```
