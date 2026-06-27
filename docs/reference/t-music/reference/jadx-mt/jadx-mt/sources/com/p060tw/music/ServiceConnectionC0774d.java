package com.p060tw.music;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.eckom.xtlibrary.p020b.p037f.p042e.C0635a;
import com.p060tw.music.MusicService;

/* JADX INFO: renamed from: com.tw.music.d */
/* JADX INFO: compiled from: MusicActivity.java */
/* JADX INFO: loaded from: classes3.dex */
class ServiceConnectionC0774d implements ServiceConnection {
    final /* synthetic */ MusicActivity this$0;

    ServiceConnectionC0774d(MusicActivity musicActivity) {
        this.this$0 = musicActivity;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.this$0.mService = ((MusicService.BinderC0763a) iBinder).getService();
        if (this.this$0.mService.f1072Pa == null || this.this$0.mService.f1072Pa.isPlaying()) {
            return;
        }
        ((C0635a) this.this$0.mPresenter).m730fa();
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.this$0.mService = null;
    }
}
