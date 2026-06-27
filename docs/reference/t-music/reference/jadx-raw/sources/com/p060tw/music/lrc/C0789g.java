package com.p060tw.music.lrc;

import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;
import com.p060tw.music.lrc.LrcView;
import java.util.List;

/* compiled from: LrcView.java */
/* renamed from: com.tw.music.lrc.g */
/* loaded from: classes3.dex */
class C0789g extends GestureDetector.SimpleOnGestureListener {
    final /* synthetic */ LrcView this$0;

    C0789g(LrcView lrcView) {
        this.this$0 = lrcView;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        LrcView.InterfaceC0782a interfaceC0782a;
        Scroller scroller;
        Runnable runnable;
        if (this.this$0.m1500Wa()) {
            interfaceC0782a = this.this$0.f1162hf;
            if (interfaceC0782a != null) {
                scroller = this.this$0.mScroller;
                scroller.forceFinished(true);
                LrcView lrcView = this.this$0;
                runnable = lrcView.f1169of;
                lrcView.removeCallbacks(runnable);
                this.this$0.f1165kf = true;
                this.this$0.f1164jf = true;
                this.this$0.invalidate();
                return true;
            }
        }
        return super.onDown(motionEvent);
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        Scroller scroller;
        float f3;
        List list;
        float m1471Ka;
        float m1471Ka2;
        if (!this.this$0.m1500Wa()) {
            return super.onFling(motionEvent, motionEvent2, f, f2);
        }
        scroller = this.this$0.mScroller;
        f3 = this.this$0.mOffset;
        LrcView lrcView = this.this$0;
        list = lrcView.f1145Ne;
        m1471Ka = lrcView.m1471Ka(list.size() - 1);
        int i = (int) m1471Ka;
        m1471Ka2 = this.this$0.m1471Ka(0);
        scroller.fling(0, (int) f3, 0, (int) f2, 0, 0, i, (int) m1471Ka2);
        this.this$0.f1166lf = true;
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        float f3;
        float m1471Ka;
        float f4;
        List list;
        float m1471Ka2;
        if (!this.this$0.m1500Wa()) {
            return super.onScroll(motionEvent, motionEvent2, f, f2);
        }
        LrcView.m1483b(this.this$0, -f2);
        LrcView lrcView = this.this$0;
        f3 = lrcView.mOffset;
        m1471Ka = this.this$0.m1471Ka(0);
        lrcView.mOffset = Math.min(f3, m1471Ka);
        LrcView lrcView2 = this.this$0;
        f4 = lrcView2.mOffset;
        LrcView lrcView3 = this.this$0;
        list = lrcView3.f1145Ne;
        m1471Ka2 = lrcView3.m1471Ka(list.size() - 1);
        lrcView2.mOffset = Math.max(f4, m1471Ka2);
        this.this$0.invalidate();
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        boolean z;
        Drawable drawable;
        int centerLine;
        List list;
        LrcView.InterfaceC0782a interfaceC0782a;
        LrcView.InterfaceC0782a interfaceC0782a2;
        Runnable runnable;
        if (this.this$0.m1500Wa()) {
            z = this.this$0.f1164jf;
            if (z) {
                drawable = this.this$0.f1149Re;
                if (drawable.getBounds().contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    centerLine = this.this$0.getCenterLine();
                    list = this.this$0.f1145Ne;
                    long time = ((C0783a) list.get(centerLine)).getTime();
                    interfaceC0782a = this.this$0.f1162hf;
                    if (interfaceC0782a != null) {
                        interfaceC0782a2 = this.this$0.f1162hf;
                        if (interfaceC0782a2.mo1464f(time)) {
                            this.this$0.f1164jf = false;
                            LrcView lrcView = this.this$0;
                            runnable = lrcView.f1169of;
                            lrcView.removeCallbacks(runnable);
                            this.this$0.mCurrentLine = centerLine;
                            this.this$0.invalidate();
                            return true;
                        }
                    }
                }
            }
        }
        return super.onSingleTapConfirmed(motionEvent);
    }
}
