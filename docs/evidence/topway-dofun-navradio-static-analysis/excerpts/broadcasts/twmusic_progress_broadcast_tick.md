# Evidence excerpt: twmusic_progress_broadcast_tick.md

  Source APK/variant: `com.tw.music_TW_THEME.20240715`
  Source path: `com.tw.music_TW_THEME.20240715/jadx/sources/com/eckom/xtlibrary/p020b/p037f/p041d/C0587F.java`
  Source lines: `173-186`
  Status: observation from static decompile/extract.
  Why it matters: One of multiple stock playback tick paths emitting com.tw.launcher.music_progress_duration with msg_music_progress/msg_music_duration and a 1000 ms loop.

  ```java
    173:                             }
174:                             int i9 = (currentPosition * 100) / duration;
175:                             C0593L.f553jd.m791b(1, this.this$0.f574Yc.f482Ad + 1, this.this$0.f574Yc.f486Dd.f545kk, (i7 << 8) | (i8 << 16) | i6, i9);
176:                             int i10 = i9 & 127;
177:                             C0593L.f553jd.write(40704, 3, (this.this$0.isPlaying() ? 128 : 0) | i10);
178:                             C0593L.f553jd.write(771, 3, (this.this$0.isPlaying() ? 128 : 0) | i10);
179:                             Intent intent = new Intent("com.tw.launcher.music_progress_duration");
180:                             intent.putExtra("msg_music_progress", currentPosition);
181:                             intent.putExtra("msg_music_duration", duration);
182:                             this.this$0.mContext.sendBroadcast(intent);
183:                         }
184:                         this.this$0.mHandler.removeMessages(65281);
185:                         this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
186:                         break;
  ```
