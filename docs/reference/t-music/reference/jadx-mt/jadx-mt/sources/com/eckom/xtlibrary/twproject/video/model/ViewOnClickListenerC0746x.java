package com.eckom.xtlibrary.twproject.video.model;

import android.view.View;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.x */
/* JADX INFO: compiled from: VideoModel.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnClickListenerC0746x implements View.OnClickListener {
    final /* synthetic */ C0748z this$0;

    ViewOnClickListenerC0746x(C0748z c0748z) {
        this.this$0 = c0748z;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int i = this.this$0.mLayoutParams.width;
        int i2 = this.this$0.mLayoutParams.height;
        this.this$0.mLayoutParams.width = (int) (((double) this.this$0.mLayoutParams.width) * 1.22d);
        this.this$0.mLayoutParams.height = (int) (((double) this.this$0.mLayoutParams.width) * 0.6d);
        if (this.this$0.mLayoutParams.width > C0760l.f985Rd || this.this$0.mLayoutParams.height > C0760l.f986Sd) {
            this.this$0.mLayoutParams.width = C0760l.f985Rd - 1;
            this.this$0.mLayoutParams.height = (int) (((double) C0760l.f986Sd) - this.this$0.f954Qi);
        } else {
            this.this$0.mLayoutParams.x -= (this.this$0.mLayoutParams.width / 2) - (i / 2);
            this.this$0.mLayoutParams.y -= (this.this$0.mLayoutParams.height / 2) - (i2 / 2);
        }
        this.this$0.f969rh.updateViewLayout(this.this$0.mRoot, this.this$0.mLayoutParams);
        this.this$0.m1232cf();
    }
}
