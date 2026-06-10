# Evidence excerpt: navradio_float_widget_controls.md

  Source APK/variant: `NavRadio+_4.00_apks`
  Source path: `NavRadio+_4.00_apks/jadx/sources/com/navimods/radio/widgets/FloatWidgetService.java`
  Source lines: `247-321`
  Status: observation from static decompile/extract.
  Why it matters: NavRadio+ floating widget controls send simple previous/next broadcasts and close itself; useful only as fallback-control comparator.

  ```java
    247:             public final void onClick(View view) {
248:                 int i5 = i;
249:                 FloatWidgetService floatWidgetService = this.f20003b;
250:                 switch (i5) {
251:                     case 0:
252:                         int i6 = FloatWidgetService.f6240B;
253:                         floatWidgetService.stopSelf();
254:                         break;
255:                     case 1:
256:                         int i7 = FloatWidgetService.f6240B;
257:                         floatWidgetService.sendBroadcast(new Intent("com.navimods.radio.set.prev_station"));
258:                         break;
259:                     default:
260:                         int i8 = FloatWidgetService.f6240B;
261:                         floatWidgetService.sendBroadcast(new Intent("com.navimods.radio.set.next_station"));
262:                         break;
263:                 }
264:             }
265:         });
266:         final int i5 = 2;
267:         ((ImageView) this.f6243b.findViewById(m92.close_button)).setOnClickListener(new ViewOnClickListenerC0529h(this, viewFindViewById, viewFindViewById2, i5));
268:         ((ImageButton) this.f6243b.findViewById(m92.widgetPrevBtn)).setOnClickListener(new View.OnClickListener(this) { // from class: vn0
269: 
270:             /* JADX INFO: renamed from: b */
271:             public final /* synthetic */ FloatWidgetService f20003b;
272: 
273:             {
274:                 this.f20003b = this;
275:             }
276: 
277:             @Override // android.view.View.OnClickListener
278:             public final void onClick(View view) {
279:                 int i52 = i4;
280:                 FloatWidgetService floatWidgetService = this.f20003b;
281:                 switch (i52) {
282:                     case 0:
283:                         int i6 = FloatWidgetService.f6240B;
284:                         floatWidgetService.stopSelf();
285:                         break;
286:                     case 1:
287:                         int i7 = FloatWidgetService.f6240B;
288:                         floatWidgetService.sendBroadcast(new Intent("com.navimods.radio.set.prev_station"));
289:                         break;
290:                     default:
291:                         int i8 = FloatWidgetService.f6240B;
292:                         floatWidgetService.sendBroadcast(new Intent("com.navimods.radio.set.next_station"));
293:                         break;
294:                 }
295:             }
296:         });
297:         ((ImageButton) this.f6243b.findViewById(m92.widgetNextBtn)).setOnClickListener(new View.OnClickListener(this) { // from class: vn0
298: 
299:             /* JADX INFO: renamed from: b */
300:             public final /* synthetic */ FloatWidgetService f20003b;
301: 
302:             {
303:                 this.f20003b = this;
304:             }
305: 
306:             @Override // android.view.View.OnClickListener
307:             public final void onClick(View view) {
308:                 int i52 = i5;
309:                 FloatWidgetService floatWidgetService = this.f20003b;
310:                 switch (i52) {
311:                     case 0:
312:                         int i6 = FloatWidgetService.f6240B;
313:                         floatWidgetService.stopSelf();
314:                         break;
315:                     case 1:
316:                         int i7 = FloatWidgetService.f6240B;
317:                         floatWidgetService.sendBroadcast(new Intent("com.navimods.radio.set.prev_station"));
318:                         break;
319:                     default:
320:                         int i8 = FloatWidgetService.f6240B;
321:                         floatWidgetService.sendBroadcast(new Intent("com.navimods.radio.set.next_station"));
  ```
