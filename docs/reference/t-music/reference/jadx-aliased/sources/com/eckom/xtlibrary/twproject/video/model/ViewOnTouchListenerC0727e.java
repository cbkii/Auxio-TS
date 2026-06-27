package com.eckom.xtlibrary.twproject.video.model;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;

/* compiled from: VideoIjkModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.e */
/* loaded from: classes3.dex */
class ViewOnTouchListenerC0727e implements View.OnTouchListener {
    final /* synthetic */ C0735m this$0;
    private float startX = 0.0f;
    private float startY = 0.0f;

    /* renamed from: uh */
    private float f910uh = 0.0f;

    /* renamed from: vh */
    private float f911vh = 0.0f;

    ViewOnTouchListenerC0727e(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z;
        double d2;
        WindowManager.LayoutParams layoutParams;
        WindowManager windowManager;
        View view2;
        WindowManager.LayoutParams layoutParams2;
        Handler handler;
        WindowManager.LayoutParams layoutParams3;
        WindowManager.LayoutParams layoutParams4;
        WindowManager.LayoutParams layoutParams5;
        WindowManager windowManager2;
        View view3;
        WindowManager.LayoutParams layoutParams6;
        z = this.this$0.f919Oi;
        if (!z) {
            return false;
        }
        try {
            float rawX = motionEvent.getRawX();
            double rawY = motionEvent.getRawY();
            d2 = this.this$0.f922Qi;
            float f = (float) (rawY - d2);
            int action = motionEvent.getAction();
            if (action == 0) {
                this.startX = motionEvent.getX();
                this.startY = motionEvent.getY();
                this.f910uh = motionEvent.getRawX();
                this.f911vh = motionEvent.getRawY();
            } else if (action == 1) {
                int i = (int) (rawX - this.f910uh);
                int rawY2 = (int) (motionEvent.getRawY() - this.f911vh);
                int rawX2 = (int) (motionEvent.getRawX() - motionEvent.getX());
                if (rawX2 == 0) {
                    layoutParams3 = this.this$0.mLayoutParams;
                    layoutParams3.x = 1;
                } else if (rawX2 == C0760l.f985Rd) {
                    layoutParams = this.this$0.mLayoutParams;
                    layoutParams.x = C0760l.f985Rd - 1;
                }
                windowManager = this.this$0.f937rh;
                view2 = this.this$0.mRoot;
                layoutParams2 = this.this$0.mLayoutParams;
                windowManager.updateViewLayout(view2, layoutParams2);
                if (i > -10 && i < 10 && rawY2 > -10 && rawY2 < 10) {
                    handler = this.this$0.mHandler;
                    handler.sendEmptyMessage(65286);
                }
                this.startX = 0.0f;
                this.startY = 0.0f;
                this.f910uh = 0.0f;
                this.f911vh = 0.0f;
            } else if (action == 2) {
                layoutParams4 = this.this$0.mLayoutParams;
                layoutParams4.x = (int) (rawX - this.startX);
                layoutParams5 = this.this$0.mLayoutParams;
                layoutParams5.y = (int) (f - this.startY);
                windowManager2 = this.this$0.f937rh;
                view3 = this.this$0.mRoot;
                layoutParams6 = this.this$0.mLayoutParams;
                windowManager2.updateViewLayout(view3, layoutParams6);
            }
        } catch (Exception unused) {
        }
        return true;
    }
}
