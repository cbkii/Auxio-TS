package com.eckom.xtlibrary.p066b.p052i;

import java.lang.reflect.Method;

/* compiled from: ReflectUtil.java */
/* renamed from: com.eckom.xtlibrary.b.i.e */
/* loaded from: classes3.dex */
public class C0675e {
    /* renamed from: a */
    public static Object m931a(Class cls, Object obj, String str, Object... objArr) {
        Class<?>[] clsArr;
        if (objArr != null) {
            clsArr = new Class[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                clsArr[i] = objArr[i].getClass();
            }
        } else {
            clsArr = null;
        }
        Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(obj, objArr);
    }
}
