package com.p073tw.music;

import android.widget.SeekBar;

/* compiled from: AudioPreview.java */
/* renamed from: com.tw.music.c */
/* loaded from: classes3.dex */
class C0770c implements SeekBar.OnSeekBarChangeListener {
    final /* synthetic */ AudioPreview this$0;

    C0770c(AudioPreview audioPreview) {
        this.this$0 = audioPreview;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z) {
            this.this$0.mPlayer.seekTo(i);
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.this$0.f1012Jc = true;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        this.this$0.f1012Jc = false;
    }
}
