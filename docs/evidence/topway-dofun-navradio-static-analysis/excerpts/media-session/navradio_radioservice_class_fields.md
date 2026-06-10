# Evidence excerpt: navradio_radioservice_class_fields.md

  Source APK/variant: `NavRadio+_4.00_apks`
  Source path: `NavRadio+_4.00_apks/jadx/sources/com/navimods/radio/RadioService.java`
  Source lines: `150-220`
  Status: observation from static decompile/extract.
  Why it matters: NavRadio+ RadioService extends MediaSessionService and carries Media3 player/session state; this is comparator evidence, not a stock Topway contract.

  ```java
    150: import p000.xm1;
151: import p000.ym1;
152: import p000.yo0;
153: import p000.yu1;
154: import p000.yz0;
155: import p000.z11;
156: 
157: /* JADX INFO: compiled from: r8-map-id-59dc5dfa74ad834bf8d545580cc6dc706e0fff395cd945e1b4dec83512299b87 */
158: /* JADX INFO: loaded from: classes.dex */
159: public class RadioService extends MediaSessionService {
160:     public static int DEVICE_OUT_FM_HEADSET = 0;
161:     private static final String LIVE_RDS_SIM_TEXT = "Questo è un testo di prova per verificare il funzionamento del LiveRDS in modo da diagnosticare il metodo concatenatePSText(pstext)";
162:     public static final String PLAYBACK_CHANNEL_ID = "playback_channel";
163:     public static final int PLAYBACK_NOTIFICATION_ID = 1;
164:     private static final int PS_CONCAT_MIN_LEN = 2;
165:     private static final long QF_BT_CONNECT_RESYNC_SUPPRESS_MS = 1500;
166:     private static final long QF_BT_DISCONNECT_RECOVERY_DELAY_MS = 500;
167:     protected static final String TAG = "RadioService";
168:     public static boolean audioFocus = false;
169:     public static String currentImgUri = "";
170:     public static boolean is7870 = false;
171:     public static boolean isAllwinner = false;
172:     public static boolean isDUDU7 = false;
173:     public static boolean isFYT7862 = false;
174:     public static boolean isNWD = false;
175:     public static boolean isPX = false;
176:     public static boolean isQF01 = false;
177:     public static boolean isRadio = false;
178:     public static boolean isRedMod = false;
179:     public static boolean isS32 = false;
180:     public static boolean isT3L = false;
181:     public static boolean isTS10 = false;
182:     public static boolean isTS9 = false;
183:     public static boolean isUSA = false;
184:     public static boolean isYT = false;
185:     public static boolean isZA001 = false;
186:     public static boolean keying = false;
187:     public static AudioManager mAudioManager = null;
188:     public static int mainindex = -1;
189:     private static volatile boolean qfBtCallActive = false;
190:     public static boolean radioProxyOK = false;
191:     public static boolean started = false;
192:     private final BroadcastReceiver appWidgetReceiver;
193:     private int arrayLength;
194:     public C0143be audioFocusManager;
195:     private so2 broadcastManager;
196:     private boolean completeString;
197:     private WeakReference<dm0> findLogoRef;
198:     public Method fytGetState;
199:     private final BroadcastReceiver fytReceiver;
200:     public Method fytSetState;
201:     private boolean isLiveRDS;
202:     private boolean joinRDStext;
203:     private o71 keyCodeManager;
204:     private final Handler liveRDSSimHandler;
205:     private Runnable liveRDSSimRunnable;
206:     private os0 mCurrentFrequency;
207:     private FmService mFMService;
208:     private final Handler mHandler;
209:     private final BroadcastReceiver mIntentReceiver;
210:     public q41 mIpcObj;
211:     private final BroadcastReceiver mQFReceiver;
212:     private final wd2 mServiceBinder;
213:     public vn1 mediaSession;
214:     private WeakReference<as1> metadataBuilderRef;
215:     private UtilEventListener normalKeyEventListener;
216:     final Runnable onFoundPS;
217:     final Runnable onFoundpty;
218:     public yu1 player;
219:     private final p12 playerListener;
220:     final Handler pstexthandler;
  ```
