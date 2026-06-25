package com.eckom.xtlibrary.p066b.p052i;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Process;
import java.io.File;

/* compiled from: ThemePlugin.java */
/* renamed from: com.eckom.xtlibrary.b.i.l */
/* loaded from: classes3.dex */
public class C0682l {

    /* renamed from: Ul */
    private Context f815Ul;

    /* renamed from: Zl */
    private String f816Zl;

    /* renamed from: _l */
    private C0681k f817_l;

    /* renamed from: cm */
    private Context f818cm = new C0674d(this);
    private final String mLocation;
    private PackageInfo mPackageInfo;
    private Resources mResources;

    private C0682l(C0681k c0681k, Context context, File file) {
        this.f817_l = c0681k;
        this.f815Ul = context;
        this.mLocation = file.getAbsolutePath();
        this.mResources = m984b(context, file);
        this.mPackageInfo = context.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(), 64);
        if (this.mPackageInfo == null) {
            this.mPackageInfo = context.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(), 1);
        }
        this.f816Zl = this.mPackageInfo.packageName;
    }

    /* renamed from: a */
    public static C0682l m983a(C0681k c0681k, Context context, File file) {
        try {
            return new C0682l(c0681k, context, file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: b */
    private static Resources m984b(Context context, File file) {
        Resources resources = context.getResources();
        return new Resources(m982a(context, file), resources.getDisplayMetrics(), resources.getConfiguration());
    }

    /* renamed from: Lc */
    public Context m985Lc() {
        return this.f815Ul;
    }

    /* renamed from: Mc */
    public Context m986Mc() {
        return this.f818cm;
    }

    public ApplicationInfo getApplicationInfo() {
        return this.mPackageInfo.applicationInfo;
    }

    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    public String getCodePath() {
        return this.mPackageInfo.applicationInfo.sourceDir;
    }

    public String getPackageName() {
        return this.f816Zl;
    }

    public String getPackageResourcePath() {
        int myUid = Process.myUid();
        ApplicationInfo applicationInfo = this.mPackageInfo.applicationInfo;
        return applicationInfo.uid == myUid ? applicationInfo.sourceDir : applicationInfo.publicSourceDir;
    }

    public Resources getResources() {
        return this.mResources;
    }

    /* renamed from: a */
    private static AssetManager m982a(Context context, File file) {
        AssetManager assetManager = (AssetManager) AssetManager.class.newInstance();
        C0675e.m931a(AssetManager.class, assetManager, "addAssetPath", file.getAbsolutePath());
        return assetManager;
    }
}
