# Evidence excerpt: navradio_float_widget_overlay.md

  Source APK/variant: `NavRadio+_4.00_apks`
  Source path: `NavRadio+_4.00_apks/jadx/sources/com/navimods/radio/widgets/FloatWidgetService.java`
  Source lines: `184-218`
  Status: observation from static decompile/extract.
  Why it matters: NavRadio+ overlay service requests normal overlay capability, checks Settings.canDrawOverlays, and stops safely if not granted.

  ```java
    184:         IntentFilter intentFilter = new IntentFilter();
185:         intentFilter.addAction("com.navimods.radio.info.hide_float");
186:         intentFilter.addAction("com.navimods.radio.info.rds.pty.name");
187:         intentFilter.addAction("com.navimods.radio.info.station");
188:         intentFilter.addAction("com.navimods.radio.info.rds.ps");
189:         intentFilter.addAction("com.navimods.radio.info.rds.text");
190:         t44.m10614q(this, this.f6244c, intentFilter, null);
191:         this.f6243b = LayoutInflater.from(this).inflate(ea2.floating_widget, (ViewGroup) null);
192:         this.f6241A = BitmapFactory.decodeResource(getResources(), ha2.ic_launcher_foreground);
193:         if (Build.VERSION.SDK_INT >= 26) {
194:             this.f6249w = new WindowManager.LayoutParams(-2, -2, 2038, 8, -3);
195:         } else {
196:             this.f6249w = new WindowManager.LayoutParams(-2, -2, 2002, 8, -3);
197:         }
198:         WindowManager.LayoutParams layoutParams = this.f6249w;
199:         layoutParams.gravity = 51;
200:         layoutParams.x = i2;
201:         layoutParams.y = i3;
202:         this.f6242a = (WindowManager) getSystemService("window");
203:         if (!Settings.canDrawOverlays(this)) {
204:             Log.e("FloatWidgetService", "onCreate: permesso SYSTEM_ALERT_WINDOW non concesso, arresto del servizio.");
205:             stopSelf();
206:             return;
207:         }
208:         WindowManager windowManager = this.f6242a;
209:         if (windowManager != null) {
210:             try {
211:                 windowManager.addView(this.f6243b, this.f6249w);
212:             } catch (WindowManager.BadTokenException e) {
213:                 Log.e("FloatWidgetService", "onCreate: impossibile aggiungere la finestra overlay: " + e.getMessage());
214:                 stopSelf();
215:                 return;
216:             } catch (Exception e2) {
217:                 Log.e("FloatWidgetService", "onCreate: errore imprevisto durante addView: " + e2.getMessage());
218:                 stopSelf();
  ```
