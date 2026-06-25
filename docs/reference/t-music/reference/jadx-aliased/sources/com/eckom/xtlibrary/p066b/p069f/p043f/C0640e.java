package com.eckom.xtlibrary.p066b.p069f.p043f;

import com.eckom.xtlibrary.p066b.p069f.p039b.C0579f;
import java.util.Comparator;

/* compiled from: MusicUtils.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.e */
/* loaded from: classes3.dex */
class C0640e implements Comparator<C0579f> {

    /* renamed from: Bk */
    final /* synthetic */ boolean f682Bk;

    C0640e(boolean z) {
        this.f682Bk = z;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(C0579f c0579f, C0579f c0579f2) {
        return this.f682Bk ? c0579f.mName.compareTo(c0579f2.mName) : c0579f2.mName.compareTo(c0579f.mName);
    }
}
