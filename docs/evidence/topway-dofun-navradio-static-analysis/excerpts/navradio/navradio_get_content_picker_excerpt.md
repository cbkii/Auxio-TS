# Evidence excerpt: navradio_get_content_picker_excerpt.md

  Source APK/variant: `NavRadio+_4.00_apks`
  Source path: `NavRadio+_4.00_apks/jadx/sources/p000/ViewOnClickListenerC0529h.java`
  Source lines: `136-150`
  Status: observation from static decompile/extract.
  Why it matters: NavRadio+ uses GET_CONTENT for at least one picker path; file-picker findings remain limited because restore/import code is dispersed and partly obfuscated.

  ```java
    136:                 int checkedButtonId2 = ((MaterialButtonToggleGroup) obj2).getCheckedButtonId();
137:                 if (checkedButtonId2 != -1) {
138:                     if (checkedButtonId2 != m92.preloaded_btn) {
139:                         if (checkedButtonId2 != m92.wallpaper_btn) {
140:                             if (checkedButtonId2 != m92.solidcolor_btn) {
141:                                 if (checkedButtonId2 == m92.customimage_btn) {
142:                                     Intent intent = new Intent();
143:                                     intent.setType("image/*");
144:                                     intent.setAction("android.intent.action.GET_CONTENT");
145:                                     j33Var.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5555);
146:                                 } else if (checkedButtonId2 == m92.onlogo_btn) {
147:                                     j33Var.m6322i(view2, "$$logo");
148:                                 }
149:                                 break;
150:                             } else {
  ```
