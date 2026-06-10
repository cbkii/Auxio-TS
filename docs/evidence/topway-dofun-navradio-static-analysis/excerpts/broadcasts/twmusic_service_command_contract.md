# Evidence excerpt: twmusic_service_command_contract.md

  Source APK/variant: `com.tw.music_TW_THEME.20240715`
  Source path: `com.tw.music_TW_THEME.20240715/jadx/sources/com/p060tw/music/MusicService.java`
  Source lines: `61-108`
  Status: observation from static decompile/extract.
  Why it matters: Stock MusicService registers a dynamic receiver for com.tw.music.action.* and handles prev/next/pp in onStartCommand.

  ```java
     61:     @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicService, com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
 62:     public void onCreate() {
 63:         super.onCreate();
 64:         this.f1072Pa = new C0769a();
 65:         this.f1073Qa = MusicWidgetProvider.getInstance();
 66:         IntentFilter intentFilter = new IntentFilter();
 67:         intentFilter.addAction("com.tw.music.action.cmd");
 68:         intentFilter.addAction("com.tw.music.action.prev");
 69:         intentFilter.addAction("com.tw.music.action.next");
 70:         intentFilter.addAction("com.tw.music.action.pp");
 71:         registerReceiver(this.f1074Ra, intentFilter);
 72:     }
 73: 
 74:     @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicService, com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
 75:     public void onDestroy() {
 76:         try {
 77:             unregisterReceiver(this.f1074Ra);
 78:             this.f1072Pa = null;
 79:         } catch (Exception unused) {
 80:         }
 81:         super.onDestroy();
 82:     }
 83: 
 84:     @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
 85:     public int onStartCommand(Intent intent, int i, int i2) {
 86:         if (intent == null) {
 87:             return 1;
 88:         }
 89:         String action = intent.getAction();
 90:         String stringExtra = intent.getStringExtra("cmd");
 91:         if ("prev".equals(stringExtra) || "com.tw.music.action.prev".equals(action)) {
 92:             ((C0635a) this.mPresenter).m736rb();
 93:             return 1;
 94:         }
 95:         if ("next".equals(stringExtra) || "com.tw.music.action.next".equals(action)) {
 96:             ((C0635a) this.mPresenter).m734pb();
 97:             return 1;
 98:         }
 99:         if (!"pp".equals(stringExtra) && !"com.tw.music.action.pp".equals(action)) {
100:             return 1;
101:         }
102:         if (this.f1072Pa.isPlaying()) {
103:             ((C0635a) this.mPresenter).m726ba();
104:             return 1;
105:         }
106:         ((C0635a) this.mPresenter).m730fa();
107:         return 1;
108:     }
  ```
