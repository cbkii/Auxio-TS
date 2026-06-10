# Evidence excerpt: navradio_widget_receiver_updates.md

  Source APK/variant: `NavRadio+_4.00_apks`
  Source path: `NavRadio+_4.00_apks/jadx/sources/com/navimods/radio/widgets/RadioWidgetExtended.java`
  Source lines: `90-126`
  Status: observation from static decompile/extract.
  Why it matters: NavRadio+ widget provider registers for app-specific information broadcasts and updates widget state from them.

  ```java
     90:     }
 91: 
 92:     /* JADX INFO: renamed from: b */
 93:     public final void m3517b(Context context) {
 94:         if (this.f6258f) {
 95:             return;
 96:         }
 97:         Context applicationContext = context.getApplicationContext();
 98:         IntentFilter intentFilter = new IntentFilter();
 99:         intentFilter.addAction("com.navimods.radio.info.rds.pty.name");
100:         intentFilter.addAction("com.navimods.radio.info.station");
101:         intentFilter.addAction("com.navimods.radio.info.rds.ps");
102:         intentFilter.addAction("com.navimods.radio.info.rds.text");
103:         if (Build.VERSION.SDK_INT >= 26) {
104:             applicationContext.registerReceiver(this, intentFilter, 2);
105:         } else {
106:             applicationContext.registerReceiver(this, intentFilter);
107:         }
108:         this.f6258f = true;
109:     }
110: 
111:     @Override // android.appwidget.AppWidgetProvider
112:     public final void onDisabled(Context context) {
113:         super.onDisabled(context);
114:         this.f6257e = null;
115:         this.f6253a = null;
116:         this.f6258f = false;
117:     }
118: 
119:     @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
120:     public final void onReceive(Context context, Intent intent) {
121:         AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
122:         int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), RadioWidgetExtended.class.getName()));
123:         String action = intent.getAction();
124:         if ("com.navimods.radio.info.station".equals(action)) {
125:             String stringExtra = intent.getStringExtra("value");
126:             if (stringExtra != null) {
  ```
