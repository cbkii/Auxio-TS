package com.eckom.xtlibrary.twproject.video.model;

import android.view.MotionEvent;
import android.view.View;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.e */
/* JADX INFO: compiled from: VideoIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnTouchListenerC0727e implements View.OnTouchListener {
    final /* synthetic */ C0735m this$0;
    private float startX = 0.0f;
    private float startY = 0.0f;

    /* JADX INFO: renamed from: uh */
    private float f910uh = 0.0f;

    /* JADX INFO: renamed from: vh */
    private float f911vh = 0.0f;

    ViewOnTouchListenerC0727e(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.this$0.f919Oi) {
            return false;
        }
        try {
            float rawX = motionEvent.getRawX();
            float rawY = (float) (((double) motionEvent.getRawY()) - this.this$0.f922Qi);
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
                    this.this$0.mLayoutParams.x = 1;
                } else if (rawX2 == C0760l.f985Rd) {
                    this.this$0.mLayoutParams.x = C0760l.f985Rd - 1;
                }
                this.this$0.f937rh.updateViewLayout(this.this$0.mRoot, this.this$0.mLayoutParams);
                if (i > -10 && i < 10 && rawY2 > -10 && rawY2 < 10) {
                    this.this$0.mHandler.sendEmptyMessage(65286);
                }
                this.startX = 0.0f;
                this.startY = 0.0f;
                this.f910uh = 0.0f;
                this.f911vh = 0.0f;
            } else if (action == 2) {
                this.this$0.mLayoutParams.x = (int) (rawX - this.startX);
                this.this$0.mLayoutParams.y = (int) (rawY - this.startY);
                this.this$0.f937rh.updateViewLayout(this.this$0.mRoot, this.this$0.mLayoutParams);
            }
        } catch (Exception unused) {
        }
        return true;
    }
}
