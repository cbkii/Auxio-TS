package com.eckom.xtlibrary.twproject.video.model;

import android.view.View;
import android.widget.ImageView;
import com.eckom.xtlibrary.R$id;

/* compiled from: VideoModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.p */
/* loaded from: classes3.dex */
class ViewOnClickListenerC0738p implements View.OnClickListener {
    final /* synthetic */ C0748z this$0;

    ViewOnClickListenerC0738p(C0748z c0748z) {
        this.this$0 = c0748z;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        View view2;
        view2 = this.this$0.mRoot;
        ((ImageView) view2.findViewById(R$id.img_suspension_pp)).getDrawable().setLevel(!this.this$0.isPlaying() ? 1 : 0);
        if (this.this$0.isPlaying()) {
            this.this$0.mo1154P();
        } else {
            this.this$0.mo1158ma();
        }
        this.this$0.m1232cf();
    }
}
