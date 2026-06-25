package com.p060tw.music;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.eckom.xtlibrary.p020b.p037f.p042e.C0635a;
import com.p060tw.music.MusicService;

/* compiled from: MusicActivity.java */
/* renamed from: com.tw.music.d */
/* loaded from: classes3.dex */
class ServiceConnectionC0774d implements ServiceConnection {
    final /* synthetic */ MusicActivity this$0;

    ServiceConnectionC0774d(MusicActivity musicActivity) {
        this.this$0 = musicActivity;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService musicService;
        MusicService musicService2;
        this.this$0.mService = ((MusicService.BinderC0763a) iBinder).getService();
        musicService = this.this$0.mService;
        if (musicService.f1072Pa == null) {
            return;
        }
        musicService2 = this.this$0.mService;
        if (musicService2.f1072Pa.isPlaying()) {
            return;
        }
        ((C0635a) this.this$0.mPresenter).m730fa();
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.this$0.mService = null;
    }
}
