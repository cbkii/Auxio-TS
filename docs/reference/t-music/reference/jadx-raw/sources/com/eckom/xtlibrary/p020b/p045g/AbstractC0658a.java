package com.eckom.xtlibrary.p020b.p045g;

import android.content.Context;
import android.os.Handler;
import com.eckom.xtlibrary.p020b.C0556b;
import com.eckom.xtlibrary.p020b.p036e.C0570a;
import com.eckom.xtlibrary.p020b.p058l.InterfaceC0710a;
import java.lang.ref.WeakReference;

/* compiled from: BasePresenter.java */
/* renamed from: com.eckom.xtlibrary.b.g.a */
/* loaded from: classes3.dex */
public abstract class AbstractC0658a<V extends InterfaceC0710a, M extends C0570a> {

    /* renamed from: Gk */
    private WeakReference<V> f733Gk;
    public Context mContext;
    public Handler mHandler;
    public M mModel;

    public AbstractC0658a(Context context) {
        this.mHandler = new Handler();
        this.mContext = context;
        this.mModel = getModel();
        C0556b.getInstant().init(this.mContext);
        C0556b.getInstant().m385a(this);
    }

    /* renamed from: a */
    public void m807a(V v) {
        this.f733Gk = new WeakReference<>(v);
    }

    /* renamed from: b */
    public M mo723b(boolean z, boolean z2) {
        return this.mModel;
    }

    public void delete() {
        C0556b.getInstant().m386db();
        WeakReference<V> weakReference = this.f733Gk;
        if (weakReference != null) {
            weakReference.clear();
            this.f733Gk = null;
        }
    }

    public V get() {
        WeakReference<V> weakReference = this.f733Gk;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public abstract M getModel();

    public void onDestroy() {
    }

    public AbstractC0658a(Context context, boolean z, boolean z2) {
        this.mHandler = new Handler();
        this.mContext = context;
        this.mModel = mo723b(z, z2);
        C0556b.getInstant().init(this.mContext);
        C0556b.getInstant().m385a(this);
    }
}
