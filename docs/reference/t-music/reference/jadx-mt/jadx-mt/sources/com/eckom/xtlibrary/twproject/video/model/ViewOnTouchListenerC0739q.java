package com.eckom.xtlibrary.twproject.video.model;

import android.view.MotionEvent;
import android.view.View;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.q */
/* JADX INFO: compiled from: VideoModel.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnTouchListenerC0739q implements View.OnTouchListener {
    final /* synthetic */ C0748z this$0;
    private float startX = 0.0f;
    private float startY = 0.0f;

    /* JADX INFO: renamed from: uh */
    private float f942uh = 0.0f;

    /* JADX INFO: renamed from: vh */
    private float f943vh = 0.0f;

    ViewOnTouchListenerC0739q(C0748z c0748z) {
        this.this$0 = c0748z;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.this$0.f951Oi) {
            return false;
        }
        try {
            float rawX = motionEvent.getRawX();
            float rawY = (float) (((double) motionEvent.getRawY()) - this.this$0.f954Qi);
            int action = motionEvent.getAction();
            if (action == 0) {
                this.startX = motionEvent.getX();
                this.startY = motionEvent.getY();
                this.f942uh = motionEvent.getRawX();
                this.f943vh = motionEvent.getRawY();
            } else if (action == 1) {
                int i = (int) (rawX - this.f942uh);
                int rawY2 = (int) (motionEvent.getRawY() - this.f943vh);
                int rawX2 = (int) (motionEvent.getRawX() - motionEvent.getX());
                if (rawX2 == 0) {
                    this.this$0.mLayoutParams.x = 1;
                } else if (rawX2 == C0760l.f985Rd) {
                    this.this$0.mLayoutParams.x = C0760l.f985Rd - 1;
                }
                this.this$0.f969rh.updateViewLayout(this.this$0.mRoot, this.this$0.mLayoutParams);
                if (i > -10 && i < 10 && rawY2 > -10 && rawY2 < 10) {
                    this.this$0.mHandler.sendEmptyMessage(65286);
                }
                this.startX = 0.0f;
                this.startY = 0.0f;
                this.f942uh = 0.0f;
                this.f943vh = 0.0f;
            } else if (action == 2) {
                this.this$0.mLayoutParams.x = (int) (rawX - this.startX);
                this.this$0.mLayoutParams.y = (int) (rawY - this.startY);
                this.this$0.f969rh.updateViewLayout(this.this$0.mRoot, this.this$0.mLayoutParams);
            }
        } catch (Exception unused) {
        }
        return true;
    }
}
