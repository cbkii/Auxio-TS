package com.eckom.xtlibrary.p020b.p032d.p033a;

import android.content.Context;
import com.eckom.xtlibrary.p020b.p036e.C0570a;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.d.a.a */
/* JADX INFO: compiled from: LauncherModel.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0566a<P extends AbstractC0658a> extends C0570a {

    /* JADX INFO: renamed from: Gh */
    private static volatile C0566a f463Gh;
    private Context mContext;

    /* JADX INFO: renamed from: yh */
    private Map<String, InterfaceC0567b> f464yh = new ConcurrentHashMap();

    private C0566a() {
    }

    public static C0566a getInstance() {
        if (f463Gh == null) {
            synchronized (C0566a.class) {
                if (f463Gh == null) {
                    f463Gh = new C0566a();
                }
            }
        }
        return f463Gh;
    }

    /* JADX INFO: renamed from: Da */
    public void m416Da(String str) {
        this.f464yh.remove(str);
    }

    /* JADX INFO: renamed from: a */
    public void m417a(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /* JADX INFO: renamed from: zb */
    public void m419zb() {
    }

    /* JADX INFO: renamed from: a */
    public void m418a(String str, InterfaceC0567b interfaceC0567b) {
        this.f464yh.put(str, interfaceC0567b);
    }
}
