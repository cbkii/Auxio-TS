package com.eckom.xtlibrary.p066b.p053j;

import android.text.TextUtils;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0574a;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0575b;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0577d;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0579f;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0580g;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: UtilsMedia.java */
/* renamed from: com.eckom.xtlibrary.b.j.s */
/* loaded from: classes3.dex */
public class C0703s {
    /* renamed from: a */
    public static C0574a m1039a(ArrayList<C0574a> arrayList, String str) {
        Iterator<C0574a> it = arrayList.iterator();
        while (it.hasNext()) {
            C0574a next = it.next();
            if (next.getName().equals(str)) {
                return next;
            }
        }
        return null;
    }

    /* renamed from: b */
    public static C0575b m1045b(ArrayList<C0575b> arrayList, String str) {
        Iterator<C0575b> it = arrayList.iterator();
        while (it.hasNext()) {
            C0575b next = it.next();
            if (next.getName().equals(str)) {
                return next;
            }
        }
        return null;
    }

    /* renamed from: c */
    public static boolean m1046c(ArrayList<C0574a> arrayList, String str) {
        Iterator<C0574a> it = arrayList.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: d */
    public static boolean m1047d(ArrayList<C0575b> arrayList, String str) {
        Iterator<C0575b> it = arrayList.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: a */
    public static C0577d m1040a(CopyOnWriteArrayList<C0577d> copyOnWriteArrayList, String str) {
        Iterator<C0577d> it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            C0577d next = it.next();
            if (next.getName().equals(str)) {
                return next;
            }
        }
        return null;
    }

    /* renamed from: a */
    public static void m1044a(CopyOnWriteArrayList<C0577d> copyOnWriteArrayList, CopyOnWriteArrayList<C0577d> copyOnWriteArrayList2) {
        if (copyOnWriteArrayList.size() == 0) {
            copyOnWriteArrayList.addAll(copyOnWriteArrayList2);
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < copyOnWriteArrayList2.size(); i++) {
            C0577d c0577d = copyOnWriteArrayList2.get(i);
            C0577d m1040a = m1040a(copyOnWriteArrayList, c0577d.getName());
            if (m1040a != null) {
                m1040a.m445tc().addAll(c0577d.m445tc());
            } else {
                arrayList.add(c0577d);
            }
        }
        copyOnWriteArrayList.addAll(arrayList);
    }

    /* renamed from: a */
    public static void m1043a(ArrayList<C0580g> arrayList, C0580g c0580g, CopyOnWriteArrayList<C0577d> copyOnWriteArrayList) {
        arrayList.clear();
        c0580g.setLength(copyOnWriteArrayList.size());
        Iterator<C0577d> it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            C0577d next = it.next();
            ArrayList<C0579f> m445tc = next.m445tc();
            c0580g.m449a(new C0579f(next.getName(), m445tc.size()));
            c0580g.mKey = next.getKey();
            C0580g c0580g2 = new C0580g(next.getName(), c0580g.mIndex, c0580g.f549qk, 1);
            c0580g2.setLength(m445tc.size());
            c0580g2.mKey = next.getKey();
            Iterator<C0579f> it2 = m445tc.iterator();
            while (it2.hasNext()) {
                c0580g2.m449a(it2.next());
            }
            arrayList.add(c0580g2);
        }
    }

    /* renamed from: a */
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

    /* renamed from: a */
    public static C0580g m1041a(ArrayList<C0580g> arrayList, String str, int i, int i2, int i3) {
        if (arrayList == null) {
            return null;
        }
        if (arrayList.size() != 0) {
            Iterator<C0580g> it = arrayList.iterator();
            while (it.hasNext()) {
                C0580g next = it.next();
                if (TextUtils.equals(next.mKey, str)) {
                    return next;
                }
            }
        }
        C0580g c0580g = new C0580g(str, i, i2, i3);
        c0580g.mKey = str;
        arrayList.add(c0580g);
        return c0580g;
    }
}
