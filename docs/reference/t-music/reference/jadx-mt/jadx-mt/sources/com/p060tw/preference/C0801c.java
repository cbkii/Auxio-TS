package com.p060tw.preference;

import android.widget.PopupWindow;

/* JADX INFO: renamed from: com.tw.preference.c */
/* JADX INFO: compiled from: SingleChoosePreference.java */
/* JADX INFO: loaded from: classes4.dex */
class C0801c implements PopupWindow.OnDismissListener {
    final /* synthetic */ SingleChoosePreference this$0;

    C0801c(SingleChoosePreference singleChoosePreference) {
        this.this$0 = singleChoosePreference;
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        this.this$0.f1230le.getDrawable().setLevel(0);
    }
}
