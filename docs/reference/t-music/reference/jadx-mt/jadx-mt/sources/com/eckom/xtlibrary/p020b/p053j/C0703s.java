package com.eckom.xtlibrary.p020b.p053j;

import android.text.TextUtils;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0574a;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0575b;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0577d;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.s */
/* JADX INFO: compiled from: UtilsMedia.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0703s {
    /* JADX INFO: renamed from: a */
    public static C0574a m1039a(ArrayList<C0574a> arrayList, String str) {
        for (C0574a c0574a : arrayList) {
            if (c0574a.getName().equals(str)) {
                return c0574a;
            }
        }
        return null;
    }

    /* JADX INFO: renamed from: b */
    public static C0575b m1045b(ArrayList<C0575b> arrayList, String str) {
        for (C0575b c0575b : arrayList) {
            if (c0575b.getName().equals(str)) {
                return c0575b;
            }
        }
        return null;
    }

    /* JADX INFO: renamed from: c */
    public static boolean m1046c(ArrayList<C0574a> arrayList, String str) {
        Iterator<C0574a> it = arrayList.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: renamed from: d */
    public static boolean m1047d(ArrayList<C0575b> arrayList, String str) {
        Iterator<C0575b> it = arrayList.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: renamed from: a */
    public static C0577d m1040a(CopyOnWriteArrayList<C0577d> copyOnWriteArrayList, String str) {
        for (C0577d c0577d : copyOnWriteArrayList) {
            if (c0577d.getName().equals(str)) {
                return c0577d;
            }
        }
        return null;
    }

    /* JADX INFO: renamed from: a */
    public static void m1044a(CopyOnWriteArrayList<C0577d> copyOnWriteArrayList, CopyOnWriteArrayList<C0577d> copyOnWriteArrayList2) {
        if (copyOnWriteArrayList.size() == 0) {
            copyOnWriteArrayList.addAll(copyOnWriteArrayList2);
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < copyOnWriteArrayList2.size(); i++) {
            C0577d c0577d = copyOnWriteArrayList2.get(i);
            C0577d c0577dM1040a = m1040a(copyOnWriteArrayList, c0577d.getName());
            if (c0577dM1040a != null) {
                c0577dM1040a.m445tc().addAll(c0577d.m445tc());
            } else {
                arrayList.add(c0577d);
            }
        }
        copyOnWriteArrayList.addAll(arrayList);
    }

    /* JADX INFO: renamed from: a */
    public static void m1043a(ArrayList<C0580g> arrayList, C0580g c0580g, CopyOnWriteArrayList<C0577d> copyOnWriteArrayList) {
        arrayList.clear();
        c0580g.setLength(copyOnWriteArrayList.size());
        for (C0577d c0577d : copyOnWriteArrayList) {
            ArrayList<C0579f> arrayListM445tc = c0577d.m445tc();
            c0580g.m449a(new C0579f(c0577d.getName(), arrayListM445tc.size()));
            c0580g.mKey = c0577d.getKey();
            C0580g c0580g2 = new C0580g(c0577d.getName(), c0580g.mIndex, c0580g.f549qk, 1);
            c0580g2.setLength(arrayListM445tc.size());
            c0580g2.mKey = c0577d.getKey();
            Iterator<C0579f> it = arrayListM445tc.iterator();
            while (it.hasNext()) {
                c0580g2.m449a(it.next());
            }
            arrayList.add(c0580g2);
        }
    }

    /* JADX INFO: renamed from: a */
    public static C0580g m1042a(LinkedHashMap<String, C0580g> linkedHashMap, String str, int i, int i2) {
        if (linkedHashMap == null) {
            return null;
        }
        C0580g c0580g = linkedHashMap.get(str);
        if (c0580g != null) {
            return c0580g;
        }
        linkedHashMap.put(str, new C0580g(str, i, i2, 0));
        return linkedHashMap.get(str);
    }

    /* JADX INFO: renamed from: a */
    public static C0580g m1041a(ArrayList<C0580g> arrayList, String str, int i, int i2, int i3) {
        if (arrayList == null) {
            return null;
        }
        if (arrayList.size() != 0) {
            for (C0580g c0580g : arrayList) {
                if (TextUtils.equals(c0580g.mKey, str)) {
                    return c0580g;
                }
            }
        }
        C0580g c0580g2 = new C0580g(str, i, i2, i3);
        c0580g2.mKey = str;
        arrayList.add(c0580g2);
        return c0580g2;
    }
}
