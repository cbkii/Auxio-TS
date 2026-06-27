package com.p060tw.preference;

import android.view.View;
import android.widget.ImageView;

/* compiled from: SingleChoosePreference.java */
/* renamed from: com.tw.preference.b */
/* loaded from: classes4.dex */
class ViewOnClickListenerC0800b implements View.OnClickListener {
    final /* synthetic */ SingleChoosePreference this$0;

    ViewOnClickListenerC0800b(SingleChoosePreference singleChoosePreference) {
        this.this$0 = singleChoosePreference;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        ImageView imageView;
        int i;
        ImageView imageView2;
        imageView = this.this$0.f1230le;
        if (imageView.getDrawable() != null) {
            imageView2 = this.this$0.f1230le;
            imageView2.getDrawable().setLevel(1);
        }
        SingleChoosePreference singleChoosePreference = this.this$0;
        i = singleChoosePreference.f1232ne;
        singleChoosePreference.m1538Ia(i);
    }
}
