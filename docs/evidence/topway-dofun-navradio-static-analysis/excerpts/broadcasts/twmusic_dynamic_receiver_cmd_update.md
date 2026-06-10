# Evidence excerpt: twmusic_dynamic_receiver_cmd_update.md

 Source APK/variant: `com.tw.music_TW_THEME.20240715`
 Source path: `com.tw.music_TW_THEME.20240715/jadx/sources/com/p060tw/music/C0781k.java`
 Source lines: `13-43`
 Status: observation from static decompile/extract.
 Why it matters: Stock dynamic receiver handles prev/next/pp and cmd=update; update refreshes RemoteViews using appWidgetIds.

 ```java
    13: 
14:     C0781k(MusicService musicService) {
15:         this.this$0 = musicService;
16:     }
17: 
18:     @Override // android.content.BroadcastReceiver
19:     public void onReceive(Context context, Intent intent) {
20:         String action = intent.getAction();
21:         String stringExtra = intent.getStringExtra("cmd");
22:         if ("prev".equals(stringExtra) || "com.tw.music.action.prev".equals(action)) {
23:             ((C0635a) this.this$0.mPresenter).m736rb();
24:             return;
25:         }
26:         if ("next".equals(stringExtra) || "com.tw.music.action.next".equals(action)) {
27:             ((C0635a) this.this$0.mPresenter).m734pb();
28:             return;
29:         }
30:         if ("pp".equals(stringExtra) || "com.tw.music.action.pp".equals(action)) {
31:             if (this.this$0.f1072Pa.isPlaying()) {
32:                 ((C0635a) this.this$0.mPresenter).m726ba();
33:                 return;
34:             } else {
35:                 ((C0635a) this.this$0.mPresenter).m730fa();
36:                 return;
37:             }
38:         }
39:         if ("update".equals(stringExtra)) {
40:             this.this$0.f1073Qa.m1531a(this.this$0, intent.getIntArrayExtra("appWidgetIds"));
41:         }
42:     }
43: }
 ```
