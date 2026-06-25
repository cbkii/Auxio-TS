package com.eckom.xtlibrary.p020b.p037f.p043f;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0577d;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: MusicDataHolder.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.c */
/* loaded from: classes3.dex */
public class C0638c {
    private static C0638c instance;

    /* renamed from: wk */
    public CopyOnWriteArrayList<C0579f> f678wk = new CopyOnWriteArrayList<>();

    /* renamed from: xk */
    public CopyOnWriteArrayList<C0577d> f679xk = new CopyOnWriteArrayList<>();

    /* renamed from: yk */
    public CopyOnWriteArrayList<C0577d> f680yk = new CopyOnWriteArrayList<>();

    /* renamed from: zk */
    public HashMap<String, CopyOnWriteArrayList<C0579f>> f681zk = new HashMap<>();

    /* renamed from: Bj */
    public HashMap<String, CopyOnWriteArrayList<C0577d>> f674Bj = new HashMap<>();

    /* renamed from: Cj */
    public HashMap<String, CopyOnWriteArrayList<C0577d>> f675Cj = new HashMap<>();

    /* renamed from: Ak */
    public HashMap<String, CopyOnWriteArrayList<C0579f>> f673Ak = new HashMap<>();

    /* renamed from: Nj */
    public HashMap<String, CopyOnWriteArrayList<C0577d>> f676Nj = new HashMap<>();

    /* renamed from: Oj */
    public HashMap<String, CopyOnWriteArrayList<C0577d>> f677Oj = new HashMap<>();

    private C0638c() {
    }

    public static C0638c getInstance() {
        if (instance == null) {
            synchronized (C0638c.class) {
                if (instance == null) {
                    instance = new C0638c();
                }
            }
        }
        return instance;
    }
}
