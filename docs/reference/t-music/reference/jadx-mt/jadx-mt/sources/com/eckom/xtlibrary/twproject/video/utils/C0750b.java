package com.eckom.xtlibrary.twproject.video.utils;

import android.content.Context;
import com.eckom.xtlibrary.p020b.p054k.p055a.C0704a;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.b */
/* JADX INFO: compiled from: CollectionUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0750b {
    /* JADX INFO: renamed from: b */
    public static boolean m1297b(Context context, String str, ArrayList<C0704a> arrayList) {
        try {
            Iterator<C0704a> it = arrayList.iterator();
            while (it.hasNext()) {
                if (it.next().mPath.equals(str)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
