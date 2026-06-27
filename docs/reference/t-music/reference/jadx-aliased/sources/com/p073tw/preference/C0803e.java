package com.p073tw.preference;

import android.widget.CompoundButton;
import com.p073tw.preference.TogglePreference;

/* compiled from: TogglePreference.java */
/* renamed from: com.tw.preference.e */
/* loaded from: classes4.dex */
class C0803e implements CompoundButton.OnCheckedChangeListener {
    final /* synthetic */ TogglePreference this$0;

    C0803e(TogglePreference togglePreference) {
        this.this$0 = togglePreference;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        TogglePreference.InterfaceC0798a interfaceC0798a;
        TogglePreference.InterfaceC0798a interfaceC0798a2;
        interfaceC0798a = this.this$0.f1239Cc;
        if (interfaceC0798a != null) {
            interfaceC0798a2 = this.this$0.f1239Cc;
            interfaceC0798a2.mo1465a(this.this$0, z);
        }
    }
}
