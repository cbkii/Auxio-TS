# Evidence excerpt: twmusic_launcher_seek_receiver.md

 Source APK/variant: `com.tw.music_TW_THEME.20240715`
 Source path: `com.tw.music_TW_THEME.20240715/jadx/sources/com/p060tw/music/C0780j.java`
 Source lines: `13-24`
 Status: observation from static decompile/extract.
 Why it matters: Stock MusicActivity runtime receiver accepts com.android.launcher.widget_music_progress and extra music_progress for seeking.

 ```java
    13: 
14:     C0780j(MusicActivity musicActivity) {
15:         this.this$0 = musicActivity;
16:     }
17: 
18:     @Override // android.content.BroadcastReceiver
19:     public void onReceive(Context context, Intent intent) {
20:         if (intent.getAction().equals("com.android.launcher.widget_music_progress")) {
21:             ((C0635a) this.this$0.mPresenter).seekTo(intent.getIntExtra("music_progress", 0));
22:         }
23:     }
24: }
 ```
