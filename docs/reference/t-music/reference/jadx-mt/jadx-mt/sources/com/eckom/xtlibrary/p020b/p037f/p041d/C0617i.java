package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.media.MediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.i */
/* JADX INFO: compiled from: MusicID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0617i implements MediaPlayer.OnCompletionListener {
    final /* synthetic */ C0628t this$0;

    C0617i(C0628t c0628t) {
        this.this$0 = c0628t;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.this$0.mo539pb();
    }
}
