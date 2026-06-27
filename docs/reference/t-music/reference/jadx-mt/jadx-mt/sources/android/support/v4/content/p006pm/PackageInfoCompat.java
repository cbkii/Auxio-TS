package android.support.v4.content.p006pm;

import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
import android.support.v4.os.BuildCompat;

/* JADX INFO: loaded from: classes3.dex */
public final class PackageInfoCompat {
    private PackageInfoCompat() {
    }

    public static long getLongVersionCode(@NonNull PackageInfo packageInfo) {
        return BuildCompat.isAtLeastP() ? packageInfo.getLongVersionCode() : packageInfo.versionCode;
    }
}
