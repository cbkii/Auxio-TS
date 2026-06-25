package com.p060tw.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.eckom.xtlibrary.p020b.p037f.p042e.C0635a;

/* JADX INFO: renamed from: com.tw.music.j */
/* JADX INFO: compiled from: MusicActivity.java */
/* JADX INFO: loaded from: classes3.dex */
class C0780j extends BroadcastReceiver {
    final /* synthetic */ MusicActivity this$0;

    C0780j(MusicActivity musicActivity) {
        this.this$0 = musicActivity;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.android.launcher.widget_music_progress")) {
            ((C0635a) this.this$0.mPresenter).seekTo(intent.getIntExtra("music_progress", 0));
        }
    }
}
