package com.eckom.xtlibrary.p066b.p052i;

import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/* compiled from: PluginContext.java */
/* renamed from: com.eckom.xtlibrary.b.i.d */
/* loaded from: classes3.dex */
class C0674d extends ContextWrapper {

    /* renamed from: Mc */
    private final C0682l f791Mc;

    public C0674d(C0682l c0682l) {
        super(c0682l.m985Lc());
        this.f791Mc = c0682l;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public ApplicationInfo getApplicationInfo() {
        return this.f791Mc.getApplicationInfo();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return this.f791Mc.getAssets();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getPackageCodePath() {
        return this.f791Mc.getCodePath();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getPackageName() {
        return this.f791Mc.getPackageName();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getPackageResourcePath() {
        return this.f791Mc.getPackageResourcePath();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return this.f791Mc.getResources();
    }
}
