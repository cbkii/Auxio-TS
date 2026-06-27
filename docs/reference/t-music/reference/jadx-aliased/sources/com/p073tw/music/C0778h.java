package com.p073tw.music;

import com.eckom.xtlibrary.p066b.p069f.p042e.C0635a;
import com.p073tw.music.lrc.LrcView;

/* compiled from: MusicActivity.java */
/* renamed from: com.tw.music.h */
/* loaded from: classes3.dex */
class C0778h implements LrcView.InterfaceC0782a {
    final /* synthetic */ MusicActivity this$0;

    C0778h(MusicActivity musicActivity) {
        this.this$0 = musicActivity;
    }

    @Override // com.p073tw.music.lrc.LrcView.InterfaceC0782a
    /* renamed from: f */
    public boolean mo1464f(long j) {
        int i = (int) j;
        ((C0635a) this.this$0.mPresenter).seekTo(i);
        this.this$0.lrc_view.m1501fa(i);
        return true;
    }
}
