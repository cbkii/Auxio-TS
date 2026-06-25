package cpdetector.reflect;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes4.dex */
public final class SingletonLoader {
    public static SingletonLoader instance;
    public Object[] dummyParameters = new Object[0];

    public static SingletonLoader getInstance() {
        if (instance == null) {
            instance = new SingletonLoader();
        }
        return instance;
    }

    public Object newInstance(Class cls) throws IllegalAccessException, InstantiationException {
        Object objNewInstance = null;
        for (Method method : cls.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if ((modifiers & 8) != 0 && (modifiers & 1) != 0 && method.getParameterTypes().length == 0 && method.getReturnType() == cls && method.getName().toLowerCase().indexOf("instance") != -1) {
                try {
                    objNewInstance = method.invoke(null, this.dummyParameters);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                }
            }
        }
        if (objNewInstance == null) {
            for (Constructor<?> constructor : cls.getConstructors()) {
                if (constructor.getParameterTypes().length == 0) {
                    if ((constructor.getModifiers() & 1) == 0) {
                        try {
                            constructor.setAccessible(true);
                        } catch (SecurityException unused) {
                        }
                    }
                    objNewInstance = cls.newInstance();
                }
            }
        }
        if (objNewInstance == null) {
            PrintStream printStream = System.err;
            StringBuilder sbM5a = C0000a.m5a("Unable to instantiate: ");
            sbM5a.append(cls.getName());
            sbM5a.append(": no singleton method, no public default constructor.");
            printStream.println(sbM5a.toString());
        }
        return objNewInstance;
    }

    public Object newInstance(String str) {
        return newInstance(Class.forName(str));
    }
}
