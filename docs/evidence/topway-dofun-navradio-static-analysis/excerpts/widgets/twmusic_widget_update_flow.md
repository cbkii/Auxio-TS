# Evidence excerpt: twmusic_widget_update_flow.md

 Source APK/variant: `com.tw.music_TW_THEME.20240715`
 Source path: `com.tw.music_TW_THEME.20240715/jadx/sources/com/p060tw/music/view/MusicWidgetProvider.java`
 Source lines: `17-48`
 Status: observation from static decompile/extract.
 Why it matters: Stock widget update path: creates placeholder RemoteViews, starts MusicService, then sends sticky com.tw.music.action.cmd with cmd=update and appWidgetIds.

 ```java
    17: public class MusicWidgetProvider extends AppWidgetProvider {
18:     private static MusicWidgetProvider sInstance;
19: 
20:     /* JADX INFO: renamed from: d */
21:     private boolean m1529d(Context context) {
22:         return AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, (Class<?>) MusicWidgetProvider.class)).length > 0;
23:     }
24: 
25:     public static synchronized MusicWidgetProvider getInstance() {
26:         if (sInstance == null) {
27:             sInstance = new MusicWidgetProvider();
28:         }
29:         return sInstance;
30:     }
31: 
32:     /* JADX INFO: renamed from: a */
33:     public void m1530a(MusicService musicService) {
34:         if (m1529d(musicService)) {
35:             m1531a(musicService, (int[]) null);
36:         }
37:     }
38: 
39:     @Override // android.appwidget.AppWidgetProvider
40:     public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
41:         m1527a(context, iArr);
42:         context.startService(new Intent(context, (Class<?>) MusicService.class));
43:         Intent intent = new Intent("com.tw.music.action.cmd");
44:         intent.putExtra("cmd", "update");
45:         intent.putExtra("appWidgetIds", iArr);
46:         intent.addFlags(1073741824);
47:         context.sendStickyBroadcast(intent);
48:     }
 ```
