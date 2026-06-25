package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: ClassesInfoCache.java */
/* renamed from: android.arch.lifecycle.a */
/* loaded from: classes.dex */
class C0012a {
    static C0012a sInstance = new C0012a();
    private final Map<Class, a> mCallbackMap = new HashMap();

    /* renamed from: Aa */
    private final Map<Class, Boolean> f46Aa = new HashMap();

    /* compiled from: ClassesInfoCache.java */
    /* renamed from: android.arch.lifecycle.a$b */
    static class b {
        final int mCallType;
        final Method mMethod;

        b(int i, Method method) {
            this.mCallType = i;
            this.mMethod = method;
            this.mMethod.setAccessible(true);
        }

        /* renamed from: b */
        void m46b(InterfaceC0016e interfaceC0016e, Lifecycle.Event event, Object obj) {
            try {
                int i = this.mCallType;
                if (i == 0) {
                    this.mMethod.invoke(obj, new Object[0]);
                } else if (i == 1) {
                    this.mMethod.invoke(obj, interfaceC0016e);
                } else {
                    if (i != 2) {
                        return;
                    }
                    this.mMethod.invoke(obj, interfaceC0016e, event);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e2) {
                throw new RuntimeException("Failed to call observer method", e2.getCause());
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || b.class != obj.getClass()) {
                return false;
            }
            b bVar = (b) obj;
            return this.mCallType == bVar.mCallType && this.mMethod.getName().equals(bVar.mMethod.getName());
        }

        public int hashCode() {
            return (this.mCallType * 31) + this.mMethod.getName().hashCode();
        }
    }

    C0012a() {
    }

    /* renamed from: d */
    private Method[] m41d(Class cls) {
        try {
            return cls.getDeclaredMethods();
        } catch (NoClassDefFoundError e) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", e);
        }
    }

    /* renamed from: a */
    a m42a(Class cls) {
        a aVar = this.mCallbackMap.get(cls);
        return aVar != null ? aVar : m39a(cls, null);
    }

    /* renamed from: b */
    boolean m43b(Class cls) {
        if (this.f46Aa.containsKey(cls)) {
            return this.f46Aa.get(cls).booleanValue();
        }
        Method[] m41d = m41d(cls);
        for (Method method : m41d) {
            if (((InterfaceC0025n) method.getAnnotation(InterfaceC0025n.class)) != null) {
                m39a(cls, m41d);
                return true;
            }
        }
        this.f46Aa.put(cls, false);
        return false;
    }

    /* compiled from: ClassesInfoCache.java */
    /* renamed from: android.arch.lifecycle.a$a */
    static class a {

        /* renamed from: ya */
        final Map<Lifecycle.Event, List<b>> f47ya = new HashMap();

        /* renamed from: za */
        final Map<b, Lifecycle.Event> f48za;

        a(Map<b, Lifecycle.Event> map) {
            this.f48za = map;
            for (Map.Entry<b, Lifecycle.Event> entry : map.entrySet()) {
                Lifecycle.Event value = entry.getValue();
                List<b> list = this.f47ya.get(value);
                if (list == null) {
                    list = new ArrayList<>();
                    this.f47ya.put(value, list);
                }
                list.add(entry.getKey());
            }
        }

        /* renamed from: a */
        void m45a(InterfaceC0016e interfaceC0016e, Lifecycle.Event event, Object obj) {
            m44a(this.f47ya.get(event), interfaceC0016e, event, obj);
            m44a(this.f47ya.get(Lifecycle.Event.ON_ANY), interfaceC0016e, event, obj);
        }

        /* renamed from: a */
        private static void m44a(List<b> list, InterfaceC0016e interfaceC0016e, Lifecycle.Event event, Object obj) {
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    list.get(size).m46b(interfaceC0016e, event, obj);
                }
            }
        }
    }

    /* renamed from: a */
    private void m40a(Map<b, Lifecycle.Event> map, b bVar, Lifecycle.Event event, Class cls) {
        Lifecycle.Event event2 = map.get(bVar);
        if (event2 == null || event == event2) {
            if (event2 == null) {
                map.put(bVar, event);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Method " + bVar.mMethod.getName() + " in " + cls.getName() + " already declared with different @OnLifecycleEvent value: previous value " + event2 + ", new value " + event);
    }

    /* renamed from: a */
    private a m39a(Class cls, @Nullable Method[] methodArr) {
        int i;
        a m42a;
        Class superclass = cls.getSuperclass();
        HashMap hashMap = new HashMap();
        if (superclass != null && (m42a = m42a(superclass)) != null) {
            hashMap.putAll(m42a.f48za);
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            for (Map.Entry<b, Lifecycle.Event> entry : m42a(cls2).f48za.entrySet()) {
                m40a(hashMap, entry.getKey(), entry.getValue(), cls);
            }
        }
        if (methodArr == null) {
            methodArr = m41d(cls);
        }
        boolean z = false;
        for (Method method : methodArr) {
            InterfaceC0025n interfaceC0025n = (InterfaceC0025n) method.getAnnotation(InterfaceC0025n.class);
            if (interfaceC0025n != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length <= 0) {
                    i = 0;
                } else {
                    if (!parameterTypes[0].isAssignableFrom(InterfaceC0016e.class)) {
                        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                    }
                    i = 1;
                }
                Lifecycle.Event value = interfaceC0025n.value();
                if (parameterTypes.length > 1) {
                    if (parameterTypes[1].isAssignableFrom(Lifecycle.Event.class)) {
                        if (value != Lifecycle.Event.ON_ANY) {
                            throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                        }
                        i = 2;
                    } else {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    }
                }
                if (parameterTypes.length <= 2) {
                    m40a(hashMap, new b(i, method), value, cls);
                    z = true;
                } else {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
            }
        }
        a aVar = new a(hashMap);
        this.mCallbackMap.put(cls, aVar);
        this.f46Aa.put(cls, Boolean.valueOf(z));
        return aVar;
    }
}
