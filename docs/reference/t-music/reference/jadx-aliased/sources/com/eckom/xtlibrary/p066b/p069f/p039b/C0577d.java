package com.eckom.xtlibrary.p066b.p069f.p039b;

import java.util.ArrayList;

/* compiled from: MediaFolderBean.java */
/* renamed from: com.eckom.xtlibrary.b.f.b.d */
/* loaded from: classes3.dex */
public class C0577d {
    private String key;
    private String name;

    /* renamed from: qj */
    private ArrayList<C0579f> f481qj = new ArrayList<>();

    public C0577d(String str, C0579f c0579f) {
        this.name = str;
        this.f481qj.add(c0579f);
    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public void setKey(String str) {
        this.key = str;
    }

    /* renamed from: tc */
    public ArrayList<C0579f> m445tc() {
        return this.f481qj;
    }
}
