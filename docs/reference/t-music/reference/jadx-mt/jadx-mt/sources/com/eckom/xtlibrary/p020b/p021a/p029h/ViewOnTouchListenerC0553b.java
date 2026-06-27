package com.eckom.xtlibrary.p020b.p021a.p029h;

import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.h.b */
/* JADX INFO: compiled from: VoiceCallView.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnTouchListenerC0553b implements View.OnTouchListener {
    final /* synthetic */ C0555d this$0;

    ViewOnTouchListenerC0553b(C0555d c0555d) {
        this.this$0 = c0555d;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX();
        float rawY = (float) (((double) motionEvent.getRawY()) - this.this$0.f443th);
        int i = (int) (rawX - this.this$0.f444uh);
        int rawY2 = (int) (motionEvent.getRawY() - this.this$0.f445vh);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.this$0.startX = motionEvent.getX();
            this.this$0.startY = motionEvent.getY();
            this.this$0.f441rh.updateViewLayout(this.this$0.mView, this.this$0.mLayoutParams);
        } else if (action != 1 && action == 2 && (i < -10 || i > 10 || rawY2 < -10 || rawY2 > 10)) {
            C0555d c0555d = this.this$0;
            c0555d.m368a(rawX - c0555d.startX, rawY - this.this$0.startY);
        }
        return true;
    }
}
