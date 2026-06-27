package com.p060tw.music;

import com.p060tw.music.utils.C0794c;
import com.p060tw.preference.TogglePreference;

/* compiled from: MusicActivity.java */
/* renamed from: com.tw.music.i */
/* loaded from: classes3.dex */
class C0779i implements TogglePreference.InterfaceC0798a {
    final /* synthetic */ MusicActivity this$0;

    C0779i(MusicActivity musicActivity) {
        this.this$0 = musicActivity;
    }

    @Override // com.p060tw.preference.TogglePreference.InterfaceC0798a
    /* renamed from: a */
    public void mo1465a(TogglePreference togglePreference, boolean z) {
        C0794c.m1515a(this.this$0.getApplicationContext(), "MusicActivity", "lrcorVisible", z);
        this.this$0.m1336K(z);
    }
}
