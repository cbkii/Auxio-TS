package com.eckom.xtlibrary.p020b.p052i;

import java.lang.reflect.Method;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.e */
/* JADX INFO: compiled from: ReflectUtil.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0675e {
    /* JADX INFO: renamed from: a */
    public static Object m931a(Class cls, Object obj, String str, Object... objArr) throws NoSuchMethodException {
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
