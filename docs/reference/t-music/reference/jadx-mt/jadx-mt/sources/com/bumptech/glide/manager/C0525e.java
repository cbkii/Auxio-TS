package com.bumptech.glide.manager;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.ComponentCallbacks;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: renamed from: com.bumptech.glide.manager.e */
/* JADX INFO: compiled from: RequestManagerRetriever.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0525e implements Handler.Callback {
    private static final C0525e INSTANCE = new C0525e();

    /* JADX INFO: renamed from: Hf */
    final Map<FragmentManager, RequestManagerFragment> f344Hf = new HashMap();

    /* JADX INFO: renamed from: If */
    final Map<android.support.v4.app.FragmentManager, SupportRequestManagerFragment> f345If = new HashMap();
    private final Handler handler = new Handler(Looper.getMainLooper(), this);

    C0525e() {
    }

    public static C0525e get() {
        return INSTANCE;
    }

    @TargetApi(17)
    /* JADX INFO: renamed from: a */
    RequestManagerFragment m172a(FragmentManager fragmentManager) {
        RequestManagerFragment requestManagerFragment = (RequestManagerFragment) fragmentManager.findFragmentByTag("com.bumptech.glide.manager");
        if (requestManagerFragment != null) {
            return requestManagerFragment;
        }
        RequestManagerFragment requestManagerFragment2 = this.f344Hf.get(fragmentManager);
        if (requestManagerFragment2 != null) {
            return requestManagerFragment2;
        }
        RequestManagerFragment requestManagerFragment3 = new RequestManagerFragment();
        this.f344Hf.put(fragmentManager, requestManagerFragment3);
        fragmentManager.beginTransaction().add(requestManagerFragment3, "com.bumptech.glide.manager").commitAllowingStateLoss();
        this.handler.obtainMessage(1, fragmentManager).sendToTarget();
        return requestManagerFragment3;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        ComponentCallbacks componentCallbacksRemove;
        int i = message.what;
        Object obj = null;
        boolean z = true;
        if (i == 1) {
            obj = (FragmentManager) message.obj;
            componentCallbacksRemove = this.f344Hf.remove(obj);
        } else if (i != 2) {
            z = false;
            componentCallbacksRemove = null;
        } else {
            obj = (android.support.v4.app.FragmentManager) message.obj;
            componentCallbacksRemove = this.f345If.remove(obj);
        }
        if (z && componentCallbacksRemove == null && Log.isLoggable("RMRetriever", 5)) {
            Log.w("RMRetriever", "Failed to remove expected request manager fragment, manager: " + obj);
        }
        return z;
    }

    /* JADX INFO: renamed from: a */
    SupportRequestManagerFragment m173a(android.support.v4.app.FragmentManager fragmentManager) {
        SupportRequestManagerFragment supportRequestManagerFragment = (SupportRequestManagerFragment) fragmentManager.findFragmentByTag("com.bumptech.glide.manager");
        if (supportRequestManagerFragment != null) {
            return supportRequestManagerFragment;
        }
        SupportRequestManagerFragment supportRequestManagerFragment2 = this.f345If.get(fragmentManager);
        if (supportRequestManagerFragment2 != null) {
            return supportRequestManagerFragment2;
        }
        SupportRequestManagerFragment supportRequestManagerFragment3 = new SupportRequestManagerFragment();
        this.f345If.put(fragmentManager, supportRequestManagerFragment3);
        fragmentManager.beginTransaction().add(supportRequestManagerFragment3, "com.bumptech.glide.manager").commitAllowingStateLoss();
        this.handler.obtainMessage(2, fragmentManager).sendToTarget();
        return supportRequestManagerFragment3;
    }
}
