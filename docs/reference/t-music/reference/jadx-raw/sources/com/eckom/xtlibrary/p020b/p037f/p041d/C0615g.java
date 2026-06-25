package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;

/* compiled from: MusicID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.g */
/* loaded from: classes3.dex */
class C0615g implements C0643h.f {
    final /* synthetic */ C0628t this$0;

    /* renamed from: tk */
    final /* synthetic */ String f635tk;

    C0615g(C0628t c0628t, String str) {
        this.this$0 = c0628t;
        this.f635tk = str;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.f
    /* renamed from: ia */
    public void mo641ia(String str) {
        C0529b.m181e(str + " 结束整理,musicBean.mCList.mName：" + this.this$0.f659Yc.f528ud.mName);
        if (this.f635tk.equals(this.this$0.f659Yc.f528ud.mName)) {
            if (this.f635tk.contains("usb")) {
                this.this$0.mo508Db();
            } else if (this.f635tk.contains("sd")) {
                this.this$0.mo507Cb();
            } else if (this.f635tk.contains("iNand")) {
                this.this$0.mo510Eb();
            }
            if (this.f635tk.contains("/.all")) {
                C0628t c0628t = this.this$0;
                if (c0628t.f659Yc.f528ud.f549qk == 1) {
                    c0628t.m697Rb();
                    return;
                }
            }
            if (this.f635tk.contains("artist/")) {
                C0628t c0628t2 = this.this$0;
                if (c0628t2.f659Yc.f528ud.f549qk == 2) {
                    c0628t2.m698Sb();
                    return;
                }
            }
            if (this.f635tk.contains("album/")) {
                C0628t c0628t3 = this.this$0;
                if (c0628t3.f659Yc.f528ud.f549qk == 3) {
                    c0628t3.m695Qb();
                }
            }
        }
    }
}
