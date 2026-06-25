package com.p060tw.music;

import com.p060tw.music.utils.C0794c;
import com.p060tw.preference.TogglePreference;

/* JADX INFO: renamed from: com.tw.music.i */
/* JADX INFO: compiled from: MusicActivity.java */
/* JADX INFO: loaded from: classes3.dex */
class C0779i implements TogglePreference.InterfaceC0798a {
    final /* synthetic */ MusicActivity this$0;

    C0779i(MusicActivity musicActivity) {
        this.this$0 = musicActivity;
    }

    @Override // com.p060tw.preference.TogglePreference.InterfaceC0798a
    /* JADX INFO: renamed from: a */
    public void mo1465a(TogglePreference togglePreference, boolean z) {
        C0794c.m1515a(this.this$0.getApplicationContext(), "MusicActivity", "lrcorVisible", z);
        this.this$0.m1336K(z);
    }
}
