package com.eckom.xtlibrary.p020b.p032d.p034b;

import android.content.Context;
import android.os.Bundle;
import com.eckom.xtlibrary.p020b.p032d.p033a.C0566a;
import com.eckom.xtlibrary.p020b.p032d.p033a.InterfaceC0567b;
import com.eckom.xtlibrary.p020b.p032d.p035c.InterfaceC0569a;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.d.b.a */
/* JADX INFO: compiled from: LauncherPresenter.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0568a extends AbstractC0658a<InterfaceC0569a, C0566a> implements InterfaceC0567b {
    private Context mContext;

    public C0568a(Context context) {
        super(context);
        this.mContext = context;
        onCreate();
    }

    /* JADX INFO: renamed from: Ra */
    public void m420Ra(String str) {
        ((C0566a) this.mModel).m416Da(str);
        ((C0566a) this.mModel).m419zb();
    }

    /* JADX INFO: renamed from: Ta */
    public void m421Ta(String str) {
        C0566a.getInstance().m418a(str, this);
    }

    /* JADX INFO: renamed from: Ua */
    public void m422Ua(String str) {
        C0566a.getInstance().m416Da(str);
    }

    /* JADX INFO: renamed from: b */
    public void m423b(Bundle bundle) {
        if (get() != null) {
            get().mo424b(bundle);
        }
    }

    public void onCreate() {
        ((C0566a) this.mModel).m417a(this.mContext);
    }

    @Override // com.eckom.xtlibrary.p020b.p045g.AbstractC0658a
    public C0566a getModel() {
        return C0566a.getInstance();
    }
}
