package com.p060tw.preference;

import android.widget.ImageView;
import android.widget.PopupWindow;

/* compiled from: SingleChoosePreference.java */
/* renamed from: com.tw.preference.c */
/* loaded from: classes4.dex */
class C0801c implements PopupWindow.OnDismissListener {
    final /* synthetic */ SingleChoosePreference this$0;

    C0801c(SingleChoosePreference singleChoosePreference) {
        this.this$0 = singleChoosePreference;
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        ImageView imageView;
        imageView = this.this$0.f1230le;
        imageView.getDrawable().setLevel(0);
    }
}
