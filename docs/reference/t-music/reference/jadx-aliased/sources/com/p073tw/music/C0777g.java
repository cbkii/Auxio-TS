package com.p073tw.music;

import android.widget.SeekBar;
import com.eckom.xtlibrary.p066b.p069f.p042e.C0635a;

/* compiled from: MusicActivity.java */
/* renamed from: com.tw.music.g */
/* loaded from: classes3.dex */
class C0777g implements SeekBar.OnSeekBarChangeListener {
    final /* synthetic */ MusicActivity this$0;

    C0777g(MusicActivity musicActivity) {
        this.this$0 = musicActivity;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        this.this$0.m1335J(z);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        ((C0635a) this.this$0.mPresenter).seekTo(seekBar.getProgress());
    }
}
