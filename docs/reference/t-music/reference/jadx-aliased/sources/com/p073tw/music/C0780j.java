package com.p073tw.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.eckom.xtlibrary.p066b.p069f.p042e.C0635a;

/* compiled from: MusicActivity.java */
/* renamed from: com.tw.music.j */
/* loaded from: classes3.dex */
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
