package com.eckom.xtlibrary.p066b.p067a.p029h;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/* compiled from: VoiceCallView.java */
/* renamed from: com.eckom.xtlibrary.b.a.h.b */
/* loaded from: classes3.dex */
class ViewOnTouchListenerC0553b implements View.OnTouchListener {
    final /* synthetic */ C0555d this$0;

    ViewOnTouchListenerC0553b(C0555d c0555d) {
        this.this$0 = c0555d;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        double d2;
        float f;
        float f2;
        WindowManager windowManager;
        View view2;
        WindowManager.LayoutParams layoutParams;
        float f3;
        float f4;
        float rawX = motionEvent.getRawX();
        double rawY = motionEvent.getRawY();
        d2 = this.this$0.f443th;
        float f5 = (float) (rawY - d2);
        f = this.this$0.f444uh;
        int i = (int) (rawX - f);
        float rawY2 = motionEvent.getRawY();
        f2 = this.this$0.f445vh;
        int i2 = (int) (rawY2 - f2);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.this$0.startX = motionEvent.getX();
            this.this$0.startY = motionEvent.getY();
            windowManager = this.this$0.f441rh;
            view2 = this.this$0.mView;
            layoutParams = this.this$0.mLayoutParams;
            windowManager.updateViewLayout(view2, layoutParams);
        } else if (action != 1 && action == 2 && (i < -10 || i > 10 || i2 < -10 || i2 > 10)) {
            C0555d c0555d = this.this$0;
            f3 = c0555d.startX;
            f4 = this.this$0.startY;
            c0555d.m368a(rawX - f3, f5 - f4);
        }
        return true;
    }
}
