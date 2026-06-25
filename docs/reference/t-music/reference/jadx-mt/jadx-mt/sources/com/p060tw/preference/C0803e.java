package com.p060tw.preference;

import android.widget.CompoundButton;

/* JADX INFO: renamed from: com.tw.preference.e */
/* JADX INFO: compiled from: TogglePreference.java */
/* JADX INFO: loaded from: classes4.dex */
class C0803e implements CompoundButton.OnCheckedChangeListener {
    final /* synthetic */ TogglePreference this$0;

    C0803e(TogglePreference togglePreference) {
        this.this$0 = togglePreference;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (this.this$0.f1239Cc != null) {
            this.this$0.f1239Cc.mo1465a(this.this$0, z);
        }
    }
}
