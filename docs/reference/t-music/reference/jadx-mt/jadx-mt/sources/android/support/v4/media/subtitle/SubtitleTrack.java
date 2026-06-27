package android.support.v4.media.subtitle;

import android.graphics.Canvas;
import android.media.MediaFormat;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.media.SubtitleData2;
import android.support.v4.media.subtitle.MediaTimeProvider;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes3.dex */
@RequiresApi(28)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class SubtitleTrack implements MediaTimeProvider.OnMediaTimeListener {
    private static final String TAG = "SubtitleTrack";
    private MediaFormat mFormat;
    private long mLastTimeMs;
    private long mLastUpdateTimeMs;
    private Runnable mRunnable;
    protected MediaTimeProvider mTimeProvider;
    protected boolean mVisible;
    private final LongSparseArray<Run> mRunsByEndTime = new LongSparseArray<>();
    private final LongSparseArray<Run> mRunsByID = new LongSparseArray<>();
    private final ArrayList<Cue> mActiveCues = new ArrayList<>();
    public boolean DEBUG = false;
    protected Handler mHandler = new Handler();
    private long mNextScheduledTimeMs = -1;
    private CueList mCues = new CueList();

    static class Cue {
        public long mEndTimeMs;
        public long[] mInnerTimesMs;
        public Cue mNextInRun;
        public long mRunID;
        public long mStartTimeMs;

        Cue() {
        }

        public void onTime(long j) {
        }
    }

    static class CueList {
        private static final String TAG = "CueList";
        public boolean DEBUG = false;
        private SortedMap<Long, ArrayList<Cue>> mCues = new TreeMap();

        class EntryIterator implements Iterator<Pair<Long, Cue>> {
            private long mCurrentTimeMs;
            private boolean mDone;
            private Pair<Long, Cue> mLastEntry;
            private Iterator<Cue> mLastListIterator;
            private Iterator<Cue> mListIterator;
            private SortedMap<Long, ArrayList<Cue>> mRemainingCues;

            EntryIterator(SortedMap<Long, ArrayList<Cue>> sortedMap) {
                if (CueList.this.DEBUG) {
                    Log.v(CueList.TAG, sortedMap + "");
                }
                this.mRemainingCues = sortedMap;
                this.mLastListIterator = null;
                nextKey();
            }

            private void nextKey() {
                while (this.mRemainingCues != null) {
                    try {
                        this.mCurrentTimeMs = this.mRemainingCues.firstKey().longValue();
                        this.mListIterator = this.mRemainingCues.get(Long.valueOf(this.mCurrentTimeMs)).iterator();
                        try {
                            this.mRemainingCues = this.mRemainingCues.tailMap(Long.valueOf(this.mCurrentTimeMs + 1));
                        } catch (IllegalArgumentException unused) {
                            this.mRemainingCues = null;
                        }
                        this.mDone = false;
                        if (this.mListIterator.hasNext()) {
                            return;
                        }
                    } catch (NoSuchElementException unused2) {
                        this.mDone = true;
                        this.mRemainingCues = null;
                        this.mListIterator = null;
                        return;
                    }
                }
                throw new NoSuchElementException("");
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return !this.mDone;
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.mLastListIterator != null) {
                    Pair<Long, Cue> pair = this.mLastEntry;
                    if (((Cue) pair.second).mEndTimeMs == ((Long) pair.first).longValue()) {
                        this.mLastListIterator.remove();
                        this.mLastListIterator = null;
                        if (((ArrayList) CueList.this.mCues.get(this.mLastEntry.first)).size() == 0) {
                            CueList.this.mCues.remove(this.mLastEntry.first);
                        }
                        Cue cue = (Cue) this.mLastEntry.second;
                        CueList.this.removeEvent(cue, cue.mStartTimeMs);
                        long[] jArr = cue.mInnerTimesMs;
                        if (jArr != null) {
                            for (long j : jArr) {
                                CueList.this.removeEvent(cue, j);
                            }
                            return;
                        }
                        return;
                    }
                }
                throw new IllegalStateException("");
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Pair<Long, Cue> next() {
                if (this.mDone) {
                    throw new NoSuchElementException("");
                }
                this.mLastEntry = new Pair<>(Long.valueOf(this.mCurrentTimeMs), this.mListIterator.next());
                Iterator<Cue> it = this.mListIterator;
                this.mLastListIterator = it;
                if (!it.hasNext()) {
                    nextKey();
                }
                return this.mLastEntry;
            }
        }

        CueList() {
        }

        private boolean addEvent(Cue cue, long j) {
            ArrayList<Cue> arrayList = this.mCues.get(Long.valueOf(j));
            if (arrayList == null) {
                arrayList = new ArrayList<>(2);
                this.mCues.put(Long.valueOf(j), arrayList);
            } else if (arrayList.contains(cue)) {
                return false;
            }
            arrayList.add(cue);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeEvent(Cue cue, long j) {
            ArrayList<Cue> arrayList = this.mCues.get(Long.valueOf(j));
            if (arrayList != null) {
                arrayList.remove(cue);
                if (arrayList.size() == 0) {
                    this.mCues.remove(Long.valueOf(j));
                }
            }
        }

        public void add(Cue cue) {
            long j = cue.mStartTimeMs;
            if (j < cue.mEndTimeMs && addEvent(cue, j)) {
                long j2 = cue.mStartTimeMs;
                long[] jArr = cue.mInnerTimesMs;
                if (jArr != null) {
                    for (long j3 : jArr) {
                        if (j3 > j2 && j3 < cue.mEndTimeMs) {
                            addEvent(cue, j3);
                            j2 = j3;
                        }
                    }
                }
                addEvent(cue, cue.mEndTimeMs);
            }
        }

        public Iterable<Pair<Long, Cue>> entriesBetween(final long j, final long j2) {
            return new Iterable<Pair<Long, Cue>>() { // from class: android.support.v4.media.subtitle.SubtitleTrack.CueList.1
                @Override // java.lang.Iterable
                public Iterator<Pair<Long, Cue>> iterator() {
                    if (CueList.this.DEBUG) {
                        Log.d(CueList.TAG, "slice (" + j + ", " + j2 + "]=");
                    }
                    try {
                        return CueList.this.new EntryIterator(CueList.this.mCues.subMap(Long.valueOf(j + 1), Long.valueOf(j2 + 1)));
                    } catch (IllegalArgumentException unused) {
                        return CueList.this.new EntryIterator(null);
                    }
                }
            };
        }

        public long nextTimeAfter(long j) {
            try {
                SortedMap<Long, ArrayList<Cue>> sortedMapTailMap = this.mCues.tailMap(Long.valueOf(j + 1));
                if (sortedMapTailMap != null) {
                    return sortedMapTailMap.firstKey().longValue();
                }
            } catch (IllegalArgumentException | NoSuchElementException unused) {
            }
            return -1L;
        }

        public void remove(Cue cue) {
            removeEvent(cue, cue.mStartTimeMs);
            long[] jArr = cue.mInnerTimesMs;
            if (jArr != null) {
                for (long j : jArr) {
                    removeEvent(cue, j);
                }
            }
            removeEvent(cue, cue.mEndTimeMs);
        }
    }

    public interface RenderingWidget {

        public interface OnChangedListener {
            void onChanged(RenderingWidget renderingWidget);
        }

        void draw(Canvas canvas);

        void onAttachedToWindow();

        void onDetachedFromWindow();

        void setOnChangedListener(OnChangedListener onChangedListener);

        void setSize(int i, int i2);

        void setVisible(boolean z);
    }

    private static class Run {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public long mEndTimeMs;
        public Cue mFirstCue;
        public Run mNextRunAtEndTimeMs;
        public Run mPrevRunAtEndTimeMs;
        public long mRunID;
        private long mStoredEndTimeMs;

        private Run() {
            this.mEndTimeMs = -1L;
            this.mRunID = 0L;
            this.mStoredEndTimeMs = -1L;
        }

        public void removeAtEndTimeMs() {
            Run run = this.mPrevRunAtEndTimeMs;
            if (run != null) {
                run.mNextRunAtEndTimeMs = this.mNextRunAtEndTimeMs;
                this.mPrevRunAtEndTimeMs = null;
            }
            Run run2 = this.mNextRunAtEndTimeMs;
            if (run2 != null) {
                run2.mPrevRunAtEndTimeMs = run;
                this.mNextRunAtEndTimeMs = null;
            }
        }

        public void storeByEndTimeMs(LongSparseArray<Run> longSparseArray) {
            int iIndexOfKey = longSparseArray.indexOfKey(this.mStoredEndTimeMs);
            if (iIndexOfKey >= 0) {
                if (this.mPrevRunAtEndTimeMs == null) {
                    Run run = this.mNextRunAtEndTimeMs;
                    if (run == null) {
                        longSparseArray.removeAt(iIndexOfKey);
                    } else {
                        longSparseArray.setValueAt(iIndexOfKey, run);
                    }
                }
                removeAtEndTimeMs();
            }
            long j = this.mEndTimeMs;
            if (j >= 0) {
                this.mPrevRunAtEndTimeMs = null;
                this.mNextRunAtEndTimeMs = longSparseArray.get(j);
                Run run2 = this.mNextRunAtEndTimeMs;
                if (run2 != null) {
                    run2.mPrevRunAtEndTimeMs = this;
                }
                longSparseArray.put(this.mEndTimeMs, this);
                this.mStoredEndTimeMs = this.mEndTimeMs;
            }
        }
    }

    public SubtitleTrack(MediaFormat mediaFormat) {
        this.mFormat = mediaFormat;
        clearActiveCues();
        this.mLastTimeMs = -1L;
    }

    private void removeRunsByEndTimeIndex(int i) {
        Run runValueAt = this.mRunsByEndTime.valueAt(i);
        while (runValueAt != null) {
            Cue cue = runValueAt.mFirstCue;
            while (cue != null) {
                this.mCues.remove(cue);
                Cue cue2 = cue.mNextInRun;
                cue.mNextInRun = null;
                cue = cue2;
            }
            this.mRunsByID.remove(runValueAt.mRunID);
            Run run = runValueAt.mNextRunAtEndTimeMs;
            runValueAt.mPrevRunAtEndTimeMs = null;
            runValueAt.mNextRunAtEndTimeMs = null;
            runValueAt = run;
        }
        this.mRunsByEndTime.removeAt(i);
    }

    private synchronized void takeTime(long j) {
        this.mLastTimeMs = j;
    }

    protected synchronized boolean addCue(Cue cue) {
        this.mCues.add(cue);
        if (cue.mRunID != 0) {
            Run run = this.mRunsByID.get(cue.mRunID);
            if (run == null) {
                run = new Run();
                this.mRunsByID.put(cue.mRunID, run);
                run.mEndTimeMs = cue.mEndTimeMs;
            } else if (run.mEndTimeMs < cue.mEndTimeMs) {
                run.mEndTimeMs = cue.mEndTimeMs;
            }
            cue.mNextInRun = run.mFirstCue;
            run.mFirstCue = cue;
        }
        final long currentTimeUs = -1;
        if (this.mTimeProvider != null) {
            try {
                currentTimeUs = this.mTimeProvider.getCurrentTimeUs(false, true) / 1000;
            } catch (IllegalStateException unused) {
            }
        }
        if (this.DEBUG) {
            Log.v(TAG, "mVisible=" + this.mVisible + ", " + cue.mStartTimeMs + " <= " + currentTimeUs + ", " + cue.mEndTimeMs + " >= " + this.mLastTimeMs);
        }
        if (!this.mVisible || cue.mStartTimeMs > currentTimeUs || cue.mEndTimeMs < this.mLastTimeMs) {
            if (this.mVisible && cue.mEndTimeMs >= this.mLastTimeMs && (cue.mStartTimeMs < this.mNextScheduledTimeMs || this.mNextScheduledTimeMs < 0)) {
                scheduleTimedEvents();
            }
            return false;
        }
        if (this.mRunnable != null) {
            this.mHandler.removeCallbacks(this.mRunnable);
        }
        this.mRunnable = new Runnable() { // from class: android.support.v4.media.subtitle.SubtitleTrack.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (this) {
                    SubtitleTrack.this.mRunnable = null;
                    SubtitleTrack.this.updateActiveCues(true, currentTimeUs);
                    SubtitleTrack.this.updateView(SubtitleTrack.this.mActiveCues);
                }
            }
        };
        if (this.mHandler.postDelayed(this.mRunnable, 10L)) {
            if (this.DEBUG) {
                Log.v(TAG, "scheduling update");
            }
        } else if (this.DEBUG) {
            Log.w(TAG, "failed to schedule subtitle view update");
        }
        return true;
    }

    protected synchronized void clearActiveCues() {
        if (this.DEBUG) {
            Log.v(TAG, "Clearing " + this.mActiveCues.size() + " active cues");
        }
        this.mActiveCues.clear();
        this.mLastUpdateTimeMs = -1L;
    }

    protected void finalize() throws Throwable {
        for (int size = this.mRunsByEndTime.size() - 1; size >= 0; size--) {
            removeRunsByEndTimeIndex(size);
        }
        super.finalize();
    }

    protected void finishedRun(long j) {
        Run run;
        if (j == 0 || j == -1 || (run = this.mRunsByID.get(j)) == null) {
            return;
        }
        run.storeByEndTimeMs(this.mRunsByEndTime);
    }

    public final MediaFormat getFormat() {
        return this.mFormat;
    }

    public abstract RenderingWidget getRenderingWidget();

    public int getTrackType() {
        return getRenderingWidget() == null ? 3 : 4;
    }

    public void hide() {
        if (this.mVisible) {
            MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
            if (mediaTimeProvider != null) {
                mediaTimeProvider.cancelNotifications(this);
            }
            RenderingWidget renderingWidget = getRenderingWidget();
            if (renderingWidget != null) {
                renderingWidget.setVisible(false);
            }
            this.mVisible = false;
        }
    }

    public void onData(SubtitleData2 subtitleData2) {
        long startTimeUs = subtitleData2.getStartTimeUs() + 1;
        onData(subtitleData2.getData(), true, startTimeUs);
        setRunDiscardTimeMs(startTimeUs, (subtitleData2.getStartTimeUs() + subtitleData2.getDurationUs()) / 1000);
    }

    protected abstract void onData(byte[] bArr, boolean z, long j);

    @Override // android.support.v4.media.subtitle.MediaTimeProvider.OnMediaTimeListener
    public void onSeek(long j) {
        if (this.DEBUG) {
            Log.d(TAG, "onSeek " + j);
        }
        synchronized (this) {
            long j2 = j / 1000;
            updateActiveCues(true, j2);
            takeTime(j2);
        }
        updateView(this.mActiveCues);
        scheduleTimedEvents();
    }

    @Override // android.support.v4.media.subtitle.MediaTimeProvider.OnMediaTimeListener
    public void onStop() {
        synchronized (this) {
            if (this.DEBUG) {
                Log.d(TAG, "onStop");
            }
            clearActiveCues();
            this.mLastTimeMs = -1L;
        }
        updateView(this.mActiveCues);
        this.mNextScheduledTimeMs = -1L;
        this.mTimeProvider.notifyAt(-1L, this);
    }

    @Override // android.support.v4.media.subtitle.MediaTimeProvider.OnMediaTimeListener
    public void onTimedEvent(long j) {
        if (this.DEBUG) {
            Log.d(TAG, "onTimedEvent " + j);
        }
        synchronized (this) {
            long j2 = j / 1000;
            updateActiveCues(false, j2);
            takeTime(j2);
        }
        updateView(this.mActiveCues);
        scheduleTimedEvents();
    }

    protected void scheduleTimedEvents() {
        if (this.mTimeProvider != null) {
            this.mNextScheduledTimeMs = this.mCues.nextTimeAfter(this.mLastTimeMs);
            if (this.DEBUG) {
                Log.d(TAG, "sched @" + this.mNextScheduledTimeMs + " after " + this.mLastTimeMs);
            }
            MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
            long j = this.mNextScheduledTimeMs;
            mediaTimeProvider.notifyAt(j >= 0 ? j * 1000 : -1L, this);
        }
    }

    public void setRunDiscardTimeMs(long j, long j2) {
        Run run;
        if (j == 0 || j == -1 || (run = this.mRunsByID.get(j)) == null) {
            return;
        }
        run.mEndTimeMs = j2;
        run.storeByEndTimeMs(this.mRunsByEndTime);
    }

    public synchronized void setTimeProvider(MediaTimeProvider mediaTimeProvider) {
        if (this.mTimeProvider == mediaTimeProvider) {
            return;
        }
        if (this.mTimeProvider != null) {
            this.mTimeProvider.cancelNotifications(this);
        }
        this.mTimeProvider = mediaTimeProvider;
        if (this.mTimeProvider != null) {
            this.mTimeProvider.scheduleUpdate(this);
        }
    }

    public void show() {
        if (this.mVisible) {
            return;
        }
        this.mVisible = true;
        RenderingWidget renderingWidget = getRenderingWidget();
        if (renderingWidget != null) {
            renderingWidget.setVisible(true);
        }
        MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
        if (mediaTimeProvider != null) {
            mediaTimeProvider.scheduleUpdate(this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0009 A[Catch: all -> 0x00ba, TryCatch #0 {, blocks: (B:4:0x0003, B:7:0x000c, B:8:0x0018, B:10:0x001e, B:12:0x0036, B:14:0x003a, B:15:0x0050, B:17:0x005d, B:18:0x0061, B:20:0x006f, B:22:0x0073, B:23:0x0089, B:25:0x008d, B:26:0x0090, B:27:0x0096, B:29:0x009a, B:30:0x009f, B:32:0x00a7, B:34:0x00b2, B:35:0x00b6, B:6:0x0009), top: B:41:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected synchronized void updateActiveCues(boolean z, long j) {
        if (!z) {
            if (this.mLastUpdateTimeMs > j) {
                clearActiveCues();
            }
            Iterator<Pair<Long, Cue>> it = this.mCues.entriesBetween(this.mLastUpdateTimeMs, j).iterator();
            while (it.hasNext()) {
                Pair<Long, Cue> next = it.next();
                Cue cue = (Cue) next.second;
                if (cue.mEndTimeMs == ((Long) next.first).longValue()) {
                    if (this.DEBUG) {
                        Log.v(TAG, "Removing " + cue);
                    }
                    this.mActiveCues.remove(cue);
                    if (cue.mRunID == 0) {
                        it.remove();
                    }
                } else if (cue.mStartTimeMs == ((Long) next.first).longValue()) {
                    if (this.DEBUG) {
                        Log.v(TAG, "Adding " + cue);
                    }
                    if (cue.mInnerTimesMs != null) {
                        cue.onTime(j);
                    }
                    this.mActiveCues.add(cue);
                } else if (cue.mInnerTimesMs != null) {
                    cue.onTime(j);
                }
            }
            while (this.mRunsByEndTime.size() > 0 && this.mRunsByEndTime.keyAt(0) <= j) {
                removeRunsByEndTimeIndex(0);
            }
            this.mLastUpdateTimeMs = j;
        }
    }

    public abstract void updateView(ArrayList<Cue> arrayList);
}
