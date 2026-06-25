package com.p060tw.music;

import android.media.AudioManager;

/* JADX INFO: renamed from: com.tw.music.b */
/* JADX INFO: compiled from: AudioPreview.java */
/* JADX INFO: loaded from: classes3.dex */
class C0768b implements AudioManager.OnAudioFocusChangeListener {
    final /* synthetic */ AudioPreview this$0;

    C0768b(AudioPreview audioPreview) {
        this.this$0 = audioPreview;
    }

    @Override // android.media.AudioManager.OnAudioFocusChangeListener
    public void onAudioFocusChange(int i) {
        if (this.this$0.mPlayer == null) {
            this.this$0.mAudioManager.abandonAudioFocus(this);
            return;
        }
        if (i == -3 || i == -2) {
            if (this.this$0.mPlayer.isPlaying()) {
                this.this$0.f1013Kc = true;
                this.this$0.mPlayer.pause();
            }
        } else if (i == -1) {
            this.this$0.f1013Kc = false;
            this.this$0.mPlayer.pause();
        } else if (i == 1 && this.this$0.f1013Kc) {
            this.this$0.f1013Kc = false;
            this.this$0.start();
        }
        this.this$0.m1324ze();
    }
}
