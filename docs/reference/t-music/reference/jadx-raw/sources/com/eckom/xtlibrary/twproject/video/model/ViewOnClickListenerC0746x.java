package com.eckom.xtlibrary.twproject.video.model;

import android.view.View;
import android.view.WindowManager;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;

/* compiled from: VideoModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.x */
/* loaded from: classes3.dex */
class ViewOnClickListenerC0746x implements View.OnClickListener {
    final /* synthetic */ C0748z this$0;

    ViewOnClickListenerC0746x(C0748z c0748z) {
        this.this$0 = c0748z;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        WindowManager.LayoutParams layoutParams;
        WindowManager.LayoutParams layoutParams2;
        WindowManager.LayoutParams layoutParams3;
        WindowManager.LayoutParams layoutParams4;
        WindowManager.LayoutParams layoutParams5;
        WindowManager.LayoutParams layoutParams6;
        WindowManager.LayoutParams layoutParams7;
        WindowManager.LayoutParams layoutParams8;
        WindowManager.LayoutParams layoutParams9;
        double d2;
        WindowManager windowManager;
        View view2;
        WindowManager.LayoutParams layoutParams10;
        WindowManager.LayoutParams layoutParams11;
        WindowManager.LayoutParams layoutParams12;
        WindowManager.LayoutParams layoutParams13;
        WindowManager.LayoutParams layoutParams14;
        WindowManager.LayoutParams layoutParams15;
        WindowManager.LayoutParams layoutParams16;
        WindowManager.LayoutParams layoutParams17;
        layoutParams = this.this$0.mLayoutParams;
        int i = layoutParams.width;
        layoutParams2 = this.this$0.mLayoutParams;
        int i2 = layoutParams2.height;
        layoutParams3 = this.this$0.mLayoutParams;
        layoutParams4 = this.this$0.mLayoutParams;
        layoutParams3.width = (int) (layoutParams4.width * 1.22d);
        layoutParams5 = this.this$0.mLayoutParams;
        layoutParams6 = this.this$0.mLayoutParams;
        layoutParams5.height = (int) (layoutParams6.width * 0.6d);
        layoutParams7 = this.this$0.mLayoutParams;
        if (layoutParams7.width <= C0760l.f985Rd) {
            layoutParams11 = this.this$0.mLayoutParams;
            if (layoutParams11.height <= C0760l.f986Sd) {
                layoutParams12 = this.this$0.mLayoutParams;
                layoutParams13 = this.this$0.mLayoutParams;
                int i3 = layoutParams13.x;
                layoutParams14 = this.this$0.mLayoutParams;
                layoutParams12.x = i3 - ((layoutParams14.width / 2) - (i / 2));
                layoutParams15 = this.this$0.mLayoutParams;
                layoutParams16 = this.this$0.mLayoutParams;
                int i4 = layoutParams16.y;
                layoutParams17 = this.this$0.mLayoutParams;
                layoutParams15.y = i4 - ((layoutParams17.height / 2) - (i2 / 2));
                windowManager = this.this$0.f969rh;
                view2 = this.this$0.mRoot;
                layoutParams10 = this.this$0.mLayoutParams;
                windowManager.updateViewLayout(view2, layoutParams10);
                this.this$0.m1232cf();
            }
        }
        layoutParams8 = this.this$0.mLayoutParams;
        layoutParams8.width = C0760l.f985Rd - 1;
        layoutParams9 = this.this$0.mLayoutParams;
        double d3 = C0760l.f986Sd;
        d2 = this.this$0.f954Qi;
        layoutParams9.height = (int) (d3 - d2);
        windowManager = this.this$0.f969rh;
        view2 = this.this$0.mRoot;
        layoutParams10 = this.this$0.mLayoutParams;
        windowManager.updateViewLayout(view2, layoutParams10);
        this.this$0.m1232cf();
    }
}
