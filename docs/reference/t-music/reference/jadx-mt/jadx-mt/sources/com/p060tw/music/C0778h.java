package com.p060tw.music;

import com.eckom.xtlibrary.p020b.p037f.p042e.C0635a;
import com.p060tw.music.lrc.LrcView;

/* JADX INFO: renamed from: com.tw.music.h */
/* JADX INFO: compiled from: MusicActivity.java */
/* JADX INFO: loaded from: classes3.dex */
class C0778h implements LrcView.InterfaceC0782a {
    final /* synthetic */ MusicActivity this$0;

    C0778h(MusicActivity musicActivity) {
        this.this$0 = musicActivity;
    }

    @Override // com.p060tw.music.lrc.LrcView.InterfaceC0782a
    /* JADX INFO: renamed from: f */
    public boolean mo1464f(long j) {
        int i = (int) j;
        ((C0635a) this.this$0.mPresenter).seekTo(i);
        this.this$0.lrc_view.m1501fa(i);
        return true;
    }
}
