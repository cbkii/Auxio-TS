package com.p060tw.preference;

import android.view.View;

/* JADX INFO: renamed from: com.tw.preference.b */
/* JADX INFO: compiled from: SingleChoosePreference.java */
/* JADX INFO: loaded from: classes4.dex */
class ViewOnClickListenerC0800b implements View.OnClickListener {
    final /* synthetic */ SingleChoosePreference this$0;

    ViewOnClickListenerC0800b(SingleChoosePreference singleChoosePreference) {
        this.this$0 = singleChoosePreference;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.this$0.f1230le.getDrawable() != null) {
            this.this$0.f1230le.getDrawable().setLevel(1);
        }
        SingleChoosePreference singleChoosePreference = this.this$0;
        singleChoosePreference.m1538Ia(singleChoosePreference.f1232ne);
    }
}
