package com.p073tw.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.eckom.xtlibrary.p066b.p069f.p042e.C0635a;
import com.p073tw.music.view.MusicWidgetProvider;

/* compiled from: MusicService.java */
/* renamed from: com.tw.music.k */
/* loaded from: classes3.dex */
class C0781k extends BroadcastReceiver {
    final /* synthetic */ MusicService this$0;

    C0781k(MusicService musicService) {
        this.this$0 = musicService;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        MusicWidgetProvider musicWidgetProvider;
        String action = intent.getAction();
        String stringExtra = intent.getStringExtra("cmd");
        if ("prev".equals(stringExtra) || "com.tw.music.action.prev".equals(action)) {
            ((C0635a) this.this$0.mPresenter).m736rb();
            return;
        }
        if ("next".equals(stringExtra) || "com.tw.music.action.next".equals(action)) {
            ((C0635a) this.this$0.mPresenter).m734pb();
            return;
        }
        if ("pp".equals(stringExtra) || "com.tw.music.action.pp".equals(action)) {
            if (this.this$0.f1072Pa.isPlaying()) {
                ((C0635a) this.this$0.mPresenter).m726ba();
                return;
            } else {
                ((C0635a) this.this$0.mPresenter).m730fa();
                return;
            }
        }
        if ("update".equals(stringExtra)) {
            int[] intArrayExtra = intent.getIntArrayExtra("appWidgetIds");
            musicWidgetProvider = this.this$0.f1073Qa;
            musicWidgetProvider.m1531a(this.this$0, intArrayExtra);
        }
    }
}
