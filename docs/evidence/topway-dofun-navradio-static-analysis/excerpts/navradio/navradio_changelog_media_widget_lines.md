# Evidence excerpt: navradio_changelog_media_widget_lines.md

  Source APK/variant: `NavRadio+_4.00_apks`
  Source path: `NavRadio+_4.00_apks/apktool/assets/changelog.txt`
  Source lines: `185-256`
  Status: observation from static decompile/extract.
  Why it matters: NavRadio+ changelog documents MediaSessionService, Media3, MediaBrowser removal, DUDU widget buttons and Android file-picker lifecycle fixes.

  ```text
    185: - Updated to target android 15
186: 
187: v3.60
188: - Added support for ZA001 kind of models
189: - Fixed Steering Wheels Controls for Roko K706 models
190: - Changed main service into a MediaSessionService
191: 
192: v3.58
193: - Fixed settings backup/restore on dudu7 (use the Download folder)
194: - Improved stations list handling
195: - Removed weekly License check
196: 
197: v3.57
198: - fixed audiofocus on FYT after phone calls
199: 
200: v3.56
201: - Added support for models K2001, K2401, K2501
202: - Fixed Back button function to close NavRadio on android 13+
203: - Again changed audioFocus for bluetooth on FYT models
204: 
205: v3.55
206: - Fixed again Audio Focus for FYT models
207: - Added missing dialogs for some cases of merging Alternative Frequencies
208: 
209: v3.54
210: - Fixed audio loss on models Topway T13 and T100
211: - Fixed FYT audio switching back when phonecalls ends
212: 
213: v3.53
214: - Fixed AudioFocus for various models
215: 
216: v3.52
217: - Fixed MTC wrong doubled SWC commands
218: - Fixed MTC audio focus issue
219: 
220: v3.51
221: - Fixed bad crash introduced in last release
222: - Added Swedish and Korean languages. Thx to users W.West and Martin
223: - Fixed Japanese. Thx to user Ap3x126
224: - Partially fixed SWC commands on QF01 lost with last release
225: - Improved audiofocus with modern method
226: - Minor fixes...
227: 
228: v3.50
229: - New Media3 MediaSession to make NavRadio work as a mediaplayer
230: - Added 2 more options for Media metadata selections: Frequency,StationName,RDStext,PTY,Empty.
231: - Added Logos for United Arab Emirates
232: - Fixed Wrong Logos download for Russia
233: 
234: v3.47
235: - Fixed saving settings on android 12+ (Require external FileManager)
236: - Fixed managing stations lists on android 12+ (Require external FileManager)
237: - Fixed Notification Toasts on android 12+
238: - Added Egypt and Singapore stations Logos
239: 
240: v3.46
241: - Fixed flickering on K4811
242: - Fixed resizing issue with splitscreen and vertical screens
243: - Fixe detection of 7870 models and removed radioproxy message for them
244: 
245: v3.45
246: - removed MediaBrowser service due to too many problems
247: 
248: v3.44
249: - fixed various new bugs
250: - fixed Interface settings crash
251: 
252: v3.43
253: - Fixed List management (save/load) for anroid 13
254: - Fixed equalizer button for DUDUOS
255: - Added a mediabrowser service so NavRadio is recognized now as player
256: - Partially fixed DUDU launcher widget buttons functions
  ```
