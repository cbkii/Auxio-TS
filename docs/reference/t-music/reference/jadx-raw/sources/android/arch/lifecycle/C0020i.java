package android.arch.lifecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: Lifecycling.java */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* renamed from: android.arch.lifecycle.i */
/* loaded from: classes.dex */
public class C0020i {

    /* renamed from: Ka */
    private static Map<Class, Integer> f58Ka = new HashMap();

    /* renamed from: La */
    private static Map<Class, List<Constructor<? extends InterfaceC0014c>>> f59La = new HashMap();

    /* renamed from: a */
    private static InterfaceC0014c m62a(Constructor<? extends InterfaceC0014c> constructor, Object obj) {
        try {
            return constructor.newInstance(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    @NonNull
    /* renamed from: c */
    static GenericLifecycleObserver m63c(Object obj) {
        if (obj instanceof FullLifecycleObserver) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver) obj);
        }
        if (obj instanceof GenericLifecycleObserver) {
            return (GenericLifecycleObserver) obj;
        }
        Class<?> cls = obj.getClass();
        if (m65f(cls) != 2) {
            return new ReflectiveGenericLifecycleObserver(obj);
        }
        List<Constructor<? extends InterfaceC0014c>> list = f59La.get(cls);
        if (list.size() == 1) {
            return new SingleGeneratedAdapterObserver(m62a(list.get(0), obj));
        }
        InterfaceC0014c[] interfaceC0014cArr = new InterfaceC0014c[list.size()];
        for (int i = 0; i < list.size(); i++) {
            interfaceC0014cArr[i] = m62a(list.get(i), obj);
        }
        return new CompositeGeneratedAdaptersObserver(interfaceC0014cArr);
    }

    @Nullable
    /* renamed from: e */
    private static Constructor<? extends InterfaceC0014c> m64e(Class<?> cls) {
        try {
            Package r0 = cls.getPackage();
            String canonicalName = cls.getCanonicalName();
            String name = r0 != null ? r0.getName() : "";
            if (!name.isEmpty()) {
                canonicalName = canonicalName.substring(name.length() + 1);
            }
            String m68la = m68la(canonicalName);
            if (!name.isEmpty()) {
                m68la = name + "." + m68la;
            }
            Constructor declaredConstructor = Class.forName(m68la).getDeclaredConstructor(cls);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return declaredConstructor;
        } catch (ClassNotFoundException unused) {
            return null;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /* renamed from: f */
    private static int m65f(Class<?> cls) {
        if (f58Ka.containsKey(cls)) {
            return f58Ka.get(cls).intValue();
        }
        int m67h = m67h(cls);
        f58Ka.put(cls, Integer.valueOf(m67h));
        return m67h;
    }

    /* renamed from: g */
    private static boolean m66g(Class<?> cls) {
        return cls != null && InterfaceC0015d.class.isAssignableFrom(cls);
    }

    /* renamed from: h */
    private static int m67h(Class<?> cls) {
        if (cls.getCanonicalName() == null) {
            return 1;
        }
        Constructor<? extends InterfaceC0014c> m64e = m64e(cls);
        if (m64e != null) {
            f59La.put(cls, Collections.singletonList(m64e));
            return 2;
        }
        if (C0012a.sInstance.m43b(cls)) {
            return 1;
        }
        Class<? super Object> superclass = cls.getSuperclass();
        ArrayList arrayList = null;
        if (m66g(superclass)) {
            if (m65f(superclass) == 1) {
                return 1;
            }
            arrayList = new ArrayList(f59La.get(superclass));
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            if (m66g(cls2)) {
                if (m65f(cls2) == 1) {
                    return 1;
                }
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.addAll(f59La.get(cls2));
            }
        }
        if (arrayList == null) {
            return 1;
        }
        f59La.put(cls, arrayList);
        return 2;
    }

    /* renamed from: la */
    public static String m68la(String str) {
        return str.replace(".", "_") + "_LifecycleAdapter";
    }
}
