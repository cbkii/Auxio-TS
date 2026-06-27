package com.eckom.xtlibrary.twproject.video.model;

import android.view.View;
import android.view.WindowManager;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;

/* compiled from: VideoIjkModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.k */
/* loaded from: classes3.dex */
class ViewOnClickListenerC0733k implements View.OnClickListener {
    final /* synthetic */ C0735m this$0;

    ViewOnClickListenerC0733k(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x006e, code lost:
    
        if (r1 < ((int) (r2.f995Nd * 0.2d))) goto L6;
     */
    @Override // android.view.View.OnClickListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onClick(View view) {
        WindowManager.LayoutParams layoutParams;
        WindowManager.LayoutParams layoutParams2;
        WindowManager.LayoutParams layoutParams3;
        WindowManager.LayoutParams layoutParams4;
        WindowManager.LayoutParams layoutParams5;
        WindowManager.LayoutParams layoutParams6;
        WindowManager.LayoutParams layoutParams7;
        C0760l c0760l;
        WindowManager.LayoutParams layoutParams8;
        C0760l c0760l2;
        WindowManager.LayoutParams layoutParams9;
        C0760l c0760l3;
        WindowManager.LayoutParams layoutParams10;
        WindowManager.LayoutParams layoutParams11;
        WindowManager.LayoutParams layoutParams12;
        WindowManager.LayoutParams layoutParams13;
        WindowManager.LayoutParams layoutParams14;
        WindowManager.LayoutParams layoutParams15;
        WindowManager windowManager;
        View view2;
        WindowManager.LayoutParams layoutParams16;
        WindowManager.LayoutParams layoutParams17;
        C0760l c0760l4;
        layoutParams = this.this$0.mLayoutParams;
        int i = layoutParams.width;
        layoutParams2 = this.this$0.mLayoutParams;
        int i2 = layoutParams2.height;
        layoutParams3 = this.this$0.mLayoutParams;
        layoutParams4 = this.this$0.mLayoutParams;
        layoutParams3.width = (int) (layoutParams4.width * 0.78d);
        layoutParams5 = this.this$0.mLayoutParams;
        layoutParams6 = this.this$0.mLayoutParams;
        layoutParams5.height = (int) (layoutParams6.width * 0.6d);
        layoutParams7 = this.this$0.mLayoutParams;
        int i3 = layoutParams7.width;
        c0760l = C0735m.f915jd;
        if (i3 >= ((int) (c0760l.f995Nd * 0.3d))) {
            layoutParams17 = this.this$0.mLayoutParams;
            int i4 = layoutParams17.height;
            c0760l4 = C0735m.f915jd;
        }
        layoutParams8 = this.this$0.mLayoutParams;
        c0760l2 = C0735m.f915jd;
        layoutParams8.width = (int) (c0760l2.f995Nd * 0.3d);
        layoutParams9 = this.this$0.mLayoutParams;
        c0760l3 = C0735m.f915jd;
        layoutParams9.height = (int) (c0760l3.f995Nd * 0.2d);
        layoutParams10 = this.this$0.mLayoutParams;
        layoutParams11 = this.this$0.mLayoutParams;
        int i5 = layoutParams11.x;
        layoutParams12 = this.this$0.mLayoutParams;
        layoutParams10.x = i5 + ((i / 2) - (layoutParams12.width / 2));
        layoutParams13 = this.this$0.mLayoutParams;
        layoutParams14 = this.this$0.mLayoutParams;
        int i6 = layoutParams14.y;
        layoutParams15 = this.this$0.mLayoutParams;
        layoutParams13.y = i6 + ((i2 / 2) - (layoutParams15.height / 2));
        windowManager = this.this$0.f937rh;
        view2 = this.this$0.mRoot;
        layoutParams16 = this.this$0.mLayoutParams;
        windowManager.updateViewLayout(view2, layoutParams16);
        this.this$0.m1179cf();
    }
}
