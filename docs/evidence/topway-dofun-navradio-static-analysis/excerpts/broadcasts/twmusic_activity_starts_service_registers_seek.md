# Evidence excerpt: twmusic_activity_starts_service_registers_seek.md

  Source APK/variant: `com.tw.music_TW_THEME.20240715`
  Source path: `com.tw.music_TW_THEME.20240715/jadx/sources/com/p060tw/music/MusicActivity.java`
  Source lines: `782-792`
  Status: observation from static decompile/extract.
  Why it matters: Stock MusicActivity starts/binds MusicService and registers the launcher progress seek receiver during onCreate.

  ```java
    782:         super.onCreate(bundle);
783:         m1354xe();
784:         startService(new Intent(this, (Class<?>) MusicService.class));
785:         m1347re();
786:         m1345pe();
787:         bindService(new Intent(this, (Class<?>) MusicService.class), this.f1064vc, 1);
788:         IntentFilter intentFilter = new IntentFilter();
789:         intentFilter.addAction("com.android.launcher.widget_music_progress");
790:         registerReceiver(this.mReceiver, intentFilter);
791:     }
792: 
  ```
