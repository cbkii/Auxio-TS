package android.support.v4.media.subtitle;

import android.content.Context;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.media.subtitle.SubtitleTrack;
import android.view.accessibility.CaptioningManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

@RequiresApi(28)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes3.dex */
public class SubtitleController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int WHAT_HIDE = 2;
    private static final int WHAT_SELECT_DEFAULT_TRACK = 4;
    private static final int WHAT_SELECT_TRACK = 3;
    private static final int WHAT_SHOW = 1;
    private Anchor mAnchor;
    private final Handler.Callback mCallback;
    private CaptioningManager.CaptioningChangeListener mCaptioningChangeListener;
    private CaptioningManager mCaptioningManager;
    private Handler mHandler;
    private Listener mListener;
    private ArrayList<Renderer> mRenderers;
    private final Object mRenderersLock;
    private SubtitleTrack mSelectedTrack;
    private boolean mShowing;
    private MediaTimeProvider mTimeProvider;
    private boolean mTrackIsExplicit;
    private ArrayList<SubtitleTrack> mTracks;
    private final Object mTracksLock;
    private boolean mVisibilityIsExplicit;

    public interface Anchor {
        Looper getSubtitleLooper();

        void setSubtitleWidget(SubtitleTrack.RenderingWidget renderingWidget);
    }

    interface Listener {
        void onSubtitleTrackSelected(SubtitleTrack subtitleTrack);
    }

    static class MediaFormatUtil {
        MediaFormatUtil() {
        }

        static int getInteger(MediaFormat mediaFormat, String str, int i) {
            try {
                return mediaFormat.getInteger(str);
            } catch (ClassCastException | NullPointerException unused) {
                return i;
            }
        }
    }

    public static abstract class Renderer {
        public abstract SubtitleTrack createTrack(MediaFormat mediaFormat);

        public abstract boolean supports(MediaFormat mediaFormat);
    }

    public SubtitleController(Context context) {
        this(context, null, null);
    }

    private void checkAnchorLooper() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doHide() {
        this.mVisibilityIsExplicit = true;
        SubtitleTrack subtitleTrack = this.mSelectedTrack;
        if (subtitleTrack != null) {
            subtitleTrack.hide();
        }
        this.mShowing = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSelectDefaultTrack() {
        SubtitleTrack subtitleTrack;
        if (this.mTrackIsExplicit) {
            if (this.mVisibilityIsExplicit) {
                return;
            }
            if (this.mCaptioningManager.isEnabled() || !((subtitleTrack = this.mSelectedTrack) == null || MediaFormatUtil.getInteger(subtitleTrack.getFormat(), "is-forced-subtitle", 0) == 0)) {
                show();
            } else {
                SubtitleTrack subtitleTrack2 = this.mSelectedTrack;
                if (subtitleTrack2 != null && subtitleTrack2.getTrackType() == 4) {
                    hide();
                }
            }
            this.mVisibilityIsExplicit = false;
        }
        SubtitleTrack defaultTrack = getDefaultTrack();
        if (defaultTrack != null) {
            selectTrack(defaultTrack);
            this.mTrackIsExplicit = false;
            if (this.mVisibilityIsExplicit) {
                return;
            }
            show();
            this.mVisibilityIsExplicit = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSelectTrack(SubtitleTrack subtitleTrack) {
        this.mTrackIsExplicit = true;
        SubtitleTrack subtitleTrack2 = this.mSelectedTrack;
        if (subtitleTrack2 == subtitleTrack) {
            return;
        }
        if (subtitleTrack2 != null) {
            subtitleTrack2.hide();
            this.mSelectedTrack.setTimeProvider(null);
        }
        this.mSelectedTrack = subtitleTrack;
        Anchor anchor = this.mAnchor;
        if (anchor != null) {
            anchor.setSubtitleWidget(getRenderingWidget());
        }
        SubtitleTrack subtitleTrack3 = this.mSelectedTrack;
        if (subtitleTrack3 != null) {
            subtitleTrack3.setTimeProvider(this.mTimeProvider);
            this.mSelectedTrack.show();
        }
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onSubtitleTrackSelected(subtitleTrack);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doShow() {
        this.mShowing = true;
        this.mVisibilityIsExplicit = true;
        SubtitleTrack subtitleTrack = this.mSelectedTrack;
        if (subtitleTrack != null) {
            subtitleTrack.show();
        }
    }

    private SubtitleTrack.RenderingWidget getRenderingWidget() {
        SubtitleTrack subtitleTrack = this.mSelectedTrack;
        if (subtitleTrack == null) {
            return null;
        }
        return subtitleTrack.getRenderingWidget();
    }

    private void processOnAnchor(Message message) {
        if (Looper.myLooper() == this.mHandler.getLooper()) {
            this.mHandler.dispatchMessage(message);
        } else {
            this.mHandler.sendMessage(message);
        }
    }

    public SubtitleTrack addTrack(MediaFormat mediaFormat) {
        SubtitleTrack createTrack;
        synchronized (this.mRenderersLock) {
            Iterator<Renderer> it = this.mRenderers.iterator();
            while (it.hasNext()) {
                Renderer next = it.next();
                if (next.supports(mediaFormat) && (createTrack = next.createTrack(mediaFormat)) != null) {
                    synchronized (this.mTracksLock) {
                        if (this.mTracks.size() == 0) {
                            this.mCaptioningManager.addCaptioningChangeListener(this.mCaptioningChangeListener);
                        }
                        this.mTracks.add(createTrack);
                    }
                    return createTrack;
                }
            }
            return null;
        }
    }

    protected void finalize() {
        this.mCaptioningManager.removeCaptioningChangeListener(this.mCaptioningChangeListener);
        super.finalize();
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x00a6, code lost:
    
        r5 = r7;
        r6 = r13;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0083  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SubtitleTrack getDefaultTrack() {
        SubtitleTrack subtitleTrack;
        int i;
        Locale locale = this.mCaptioningManager.getLocale();
        Locale locale2 = locale == null ? Locale.getDefault() : locale;
        boolean z = !this.mCaptioningManager.isEnabled();
        synchronized (this.mTracksLock) {
            Iterator<SubtitleTrack> it = this.mTracks.iterator();
            subtitleTrack = null;
            int i2 = -1;
            while (it.hasNext()) {
                SubtitleTrack next = it.next();
                MediaFormat format = next.getFormat();
                String string = format.getString(IjkMediaMeta.IJKM_KEY_LANGUAGE);
                int i3 = 0;
                boolean z2 = MediaFormatUtil.getInteger(format, "is-forced-subtitle", 0) != 0;
                boolean z3 = MediaFormatUtil.getInteger(format, "is-autoselect", 1) != 0;
                boolean z4 = MediaFormatUtil.getInteger(format, "is-default", 0) != 0;
                if (locale2 != null && !locale2.getLanguage().equals("") && !locale2.getISO3Language().equals(string) && !locale2.getLanguage().equals(string)) {
                    i = 0;
                    int i4 = (!z2 ? 0 : 8) + ((locale == null || !z4) ? 0 : 4);
                    if (z3) {
                        i3 = 2;
                    }
                    int i5 = i4 + i3 + i;
                    if (z || z2) {
                        if ((locale == null && z4) || (i != 0 && (z3 || z2 || locale != null))) {
                        }
                    }
                }
                i = 1;
                int i42 = (!z2 ? 0 : 8) + ((locale == null || !z4) ? 0 : 4);
                if (z3) {
                }
                int i52 = i42 + i3 + i;
                if (z) {
                }
                if (locale == null) {
                }
            }
        }
        return subtitleTrack;
    }

    public SubtitleTrack getSelectedTrack() {
        return this.mSelectedTrack;
    }

    public SubtitleTrack[] getTracks() {
        SubtitleTrack[] subtitleTrackArr;
        synchronized (this.mTracksLock) {
            subtitleTrackArr = new SubtitleTrack[this.mTracks.size()];
            this.mTracks.toArray(subtitleTrackArr);
        }
        return subtitleTrackArr;
    }

    public boolean hasRendererFor(MediaFormat mediaFormat) {
        synchronized (this.mRenderersLock) {
            Iterator<Renderer> it = this.mRenderers.iterator();
            while (it.hasNext()) {
                if (it.next().supports(mediaFormat)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void hide() {
        processOnAnchor(this.mHandler.obtainMessage(2));
    }

    public void registerRenderer(Renderer renderer) {
        synchronized (this.mRenderersLock) {
            if (!this.mRenderers.contains(renderer)) {
                this.mRenderers.add(renderer);
            }
        }
    }

    public void reset() {
        checkAnchorLooper();
        hide();
        selectTrack(null);
        this.mTracks.clear();
        this.mTrackIsExplicit = false;
        this.mVisibilityIsExplicit = false;
        this.mCaptioningManager.removeCaptioningChangeListener(this.mCaptioningChangeListener);
    }

    public void selectDefaultTrack() {
        processOnAnchor(this.mHandler.obtainMessage(4));
    }

    public boolean selectTrack(SubtitleTrack subtitleTrack) {
        if (subtitleTrack != null && !this.mTracks.contains(subtitleTrack)) {
            return false;
        }
        processOnAnchor(this.mHandler.obtainMessage(3, subtitleTrack));
        return true;
    }

    public void setAnchor(Anchor anchor) {
        Anchor anchor2 = this.mAnchor;
        if (anchor2 == anchor) {
            return;
        }
        if (anchor2 != null) {
            checkAnchorLooper();
            this.mAnchor.setSubtitleWidget(null);
        }
        this.mAnchor = anchor;
        this.mHandler = null;
        Anchor anchor3 = this.mAnchor;
        if (anchor3 != null) {
            this.mHandler = new Handler(anchor3.getSubtitleLooper(), this.mCallback);
            checkAnchorLooper();
            this.mAnchor.setSubtitleWidget(getRenderingWidget());
        }
    }

    public void show() {
        processOnAnchor(this.mHandler.obtainMessage(1));
    }

    public SubtitleController(Context context, MediaTimeProvider mediaTimeProvider, Listener listener) {
        this.mRenderersLock = new Object();
        this.mTracksLock = new Object();
        this.mCallback = new Handler.Callback() { // from class: android.support.v4.media.subtitle.SubtitleController.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    SubtitleController.this.doShow();
                    return true;
                }
                if (i == 2) {
                    SubtitleController.this.doHide();
                    return true;
                }
                if (i == 3) {
                    SubtitleController.this.doSelectTrack((SubtitleTrack) message.obj);
                    return true;
                }
                if (i != 4) {
                    return false;
                }
                SubtitleController.this.doSelectDefaultTrack();
                return true;
            }
        };
        this.mCaptioningChangeListener = new CaptioningManager.CaptioningChangeListener() { // from class: android.support.v4.media.subtitle.SubtitleController.2
            @Override // android.view.accessibility.CaptioningManager.CaptioningChangeListener
            public void onEnabledChanged(boolean z) {
                SubtitleController.this.selectDefaultTrack();
            }

            @Override // android.view.accessibility.CaptioningManager.CaptioningChangeListener
            public void onLocaleChanged(Locale locale) {
                SubtitleController.this.selectDefaultTrack();
            }
        };
        this.mTrackIsExplicit = false;
        this.mVisibilityIsExplicit = false;
        this.mTimeProvider = mediaTimeProvider;
        this.mListener = listener;
        this.mRenderers = new ArrayList<>();
        this.mShowing = false;
        this.mTracks = new ArrayList<>();
        this.mCaptioningManager = (CaptioningManager) context.getSystemService("captioning");
    }
}
