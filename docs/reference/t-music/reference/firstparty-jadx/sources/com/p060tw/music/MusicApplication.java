package com.p060tw.music;

import android.app.Application;
import android.content.Context;
import com.eckom.xtlibrary.p020b.p052i.C0681k;

/* loaded from: classes3.dex */
public class MusicApplication extends Application {
    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        C0681k.get().init(this);
    }
}
