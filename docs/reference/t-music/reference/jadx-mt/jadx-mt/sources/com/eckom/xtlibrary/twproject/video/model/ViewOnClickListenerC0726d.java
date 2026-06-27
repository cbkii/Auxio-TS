package com.eckom.xtlibrary.twproject.video.model;

import android.view.View;
import android.widget.ImageView;
import com.eckom.xtlibrary.R$id;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.d */
/* JADX INFO: compiled from: VideoIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnClickListenerC0726d implements View.OnClickListener {
    final /* synthetic */ C0735m this$0;

    ViewOnClickListenerC0726d(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        ((ImageView) this.this$0.mRoot.findViewById(R$id.img_suspension_pp)).getDrawable().setLevel(!this.this$0.isPlaying() ? 1 : 0);
        if (this.this$0.isPlaying()) {
            this.this$0.mo1154P();
        } else {
            this.this$0.mo1158ma();
        }
        this.this$0.m1179cf();
    }
}
