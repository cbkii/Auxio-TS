package com.p060tw.music.lrc;

import android.view.GestureDetector;
import android.view.MotionEvent;

/* JADX INFO: renamed from: com.tw.music.lrc.g */
/* JADX INFO: compiled from: LrcView.java */
/* JADX INFO: loaded from: classes3.dex */
class C0789g extends GestureDetector.SimpleOnGestureListener {
    final /* synthetic */ LrcView this$0;

    C0789g(LrcView lrcView) {
        this.this$0 = lrcView;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        if (!this.this$0.m1500Wa() || this.this$0.f1162hf == null) {
            return super.onDown(motionEvent);
        }
        this.this$0.mScroller.forceFinished(true);
        LrcView lrcView = this.this$0;
        lrcView.removeCallbacks(lrcView.f1169of);
        this.this$0.f1165kf = true;
        this.this$0.f1164jf = true;
        this.this$0.invalidate();
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.this$0.m1500Wa()) {
            return super.onFling(motionEvent, motionEvent2, f, f2);
        }
        LrcView lrcView = this.this$0;
        this.this$0.mScroller.fling(0, (int) this.this$0.mOffset, 0, (int) f2, 0, 0, (int) lrcView.m1471Ka(lrcView.f1145Ne.size() - 1), (int) this.this$0.m1471Ka(0));
        this.this$0.f1166lf = true;
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.this$0.m1500Wa()) {
            return super.onScroll(motionEvent, motionEvent2, f, f2);
        }
        LrcView.m1483b(this.this$0, -f2);
        LrcView lrcView = this.this$0;
        lrcView.mOffset = Math.min(lrcView.mOffset, this.this$0.m1471Ka(0));
        LrcView lrcView2 = this.this$0;
        float f3 = lrcView2.mOffset;
        LrcView lrcView3 = this.this$0;
        lrcView2.mOffset = Math.max(f3, lrcView3.m1471Ka(lrcView3.f1145Ne.size() - 1));
        this.this$0.invalidate();
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        if (this.this$0.m1500Wa() && this.this$0.f1164jf && this.this$0.f1149Re.getBounds().contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            int centerLine = this.this$0.getCenterLine();
            long time = ((C0783a) this.this$0.f1145Ne.get(centerLine)).getTime();
            if (this.this$0.f1162hf != null && this.this$0.f1162hf.mo1464f(time)) {
                this.this$0.f1164jf = false;
                LrcView lrcView = this.this$0;
                lrcView.removeCallbacks(lrcView.f1169of);
                this.this$0.mCurrentLine = centerLine;
                this.this$0.invalidate();
                return true;
            }
        }
        return super.onSingleTapConfirmed(motionEvent);
    }
}
