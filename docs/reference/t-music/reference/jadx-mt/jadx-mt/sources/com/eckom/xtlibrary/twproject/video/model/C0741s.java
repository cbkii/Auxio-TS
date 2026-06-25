package com.eckom.xtlibrary.twproject.video.model;

import android.media.MediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.s */
/* JADX INFO: compiled from: VideoModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0741s implements MediaPlayer.OnCompletionListener {
    final /* synthetic */ C0748z this$0;

    C0741s(C0748z c0748z) {
        this.this$0 = c0748z;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.this$0.mo1156ic();
    }
}
