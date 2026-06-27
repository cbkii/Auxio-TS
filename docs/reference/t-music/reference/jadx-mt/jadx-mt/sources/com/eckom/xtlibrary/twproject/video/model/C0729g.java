package com.eckom.xtlibrary.twproject.video.model;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.g */
/* JADX INFO: compiled from: VideoIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0729g implements IMediaPlayer.OnCompletionListener {
    final /* synthetic */ C0735m this$0;

    C0729g(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        this.this$0.mo1156ic();
    }
}
