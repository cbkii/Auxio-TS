package com.eckom.xtlibrary.twproject.video.model;

import android.view.View;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.k */
/* JADX INFO: compiled from: VideoIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnClickListenerC0733k implements View.OnClickListener {
    final /* synthetic */ C0735m this$0;

    ViewOnClickListenerC0733k(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int i = this.this$0.mLayoutParams.width;
        int i2 = this.this$0.mLayoutParams.height;
        this.this$0.mLayoutParams.width = (int) (((double) this.this$0.mLayoutParams.width) * 0.78d);
        this.this$0.mLayoutParams.height = (int) (((double) this.this$0.mLayoutParams.width) * 0.6d);
        if (this.this$0.mLayoutParams.width < ((int) (((double) C0735m.f915jd.f995Nd) * 0.3d)) || this.this$0.mLayoutParams.height < ((int) (((double) C0735m.f915jd.f995Nd) * 0.2d))) {
            this.this$0.mLayoutParams.width = (int) (((double) C0735m.f915jd.f995Nd) * 0.3d);
            this.this$0.mLayoutParams.height = (int) (((double) C0735m.f915jd.f995Nd) * 0.2d);
        }
        this.this$0.mLayoutParams.x += (i / 2) - (this.this$0.mLayoutParams.width / 2);
        this.this$0.mLayoutParams.y += (i2 / 2) - (this.this$0.mLayoutParams.height / 2);
        this.this$0.f937rh.updateViewLayout(this.this$0.mRoot, this.this$0.mLayoutParams);
        this.this$0.m1179cf();
    }
}
