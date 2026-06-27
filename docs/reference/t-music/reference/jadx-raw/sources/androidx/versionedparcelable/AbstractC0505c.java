package androidx.versionedparcelable;

import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import java.lang.reflect.InvocationTargetException;

/* compiled from: VersionedParcel.java */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* renamed from: androidx.versionedparcelable.c */
/* loaded from: classes3.dex */
public abstract class AbstractC0505c {
    /* renamed from: b */
    private static <T extends InterfaceC0507e> Class m84b(T t) {
        return m86i(t.getClass());
    }

    /* renamed from: c */
    private void m85c(InterfaceC0507e interfaceC0507e) {
        try {
            writeString(m86i(interfaceC0507e.getClass()).getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(interfaceC0507e.getClass().getSimpleName() + " does not have a Parcelizer", e);
        }
    }

    /* renamed from: Ya */
    protected abstract void mo87Ya();

    /* renamed from: Za */
    protected abstract AbstractC0505c mo88Za();

    /* renamed from: _a */
    public boolean m89_a() {
        return false;
    }

    /* renamed from: a */
    public <T extends Parcelable> T m90a(T t, int i) {
        return !mo99ia(i) ? t : (T) mo94ab();
    }

    /* renamed from: a */
    protected abstract void mo91a(Parcelable parcelable);

    /* renamed from: a */
    public void m93a(boolean z, boolean z2) {
    }

    /* renamed from: ab */
    protected abstract <T extends Parcelable> T mo94ab();

    /* renamed from: bb */
    protected <T extends InterfaceC0507e> T m95bb() {
        String readString = readString();
        if (readString == null) {
            return null;
        }
        return (T) m82a(readString, mo88Za());
    }

    /* renamed from: f */
    public byte[] m96f(byte[] bArr, int i) {
        return !mo99ia(i) ? bArr : readByteArray();
    }

    /* renamed from: g */
    public void m97g(byte[] bArr, int i) {
        mo101ja(i);
        writeByteArray(bArr);
    }

    /* renamed from: i */
    public String m98i(String str, int i) {
        return !mo99ia(i) ? str : readString();
    }

    /* renamed from: ia */
    protected abstract boolean mo99ia(int i);

    /* renamed from: j */
    public void m100j(String str, int i) {
        mo101ja(i);
        writeString(str);
    }

    /* renamed from: ja */
    protected abstract void mo101ja(int i);

    /* renamed from: k */
    public void m102k(int i, int i2) {
        mo101ja(i2);
        writeInt(i);
    }

    protected abstract byte[] readByteArray();

    protected abstract int readInt();

    public int readInt(int i, int i2) {
        return !mo99ia(i2) ? i : readInt();
    }

    protected abstract String readString();

    protected abstract void writeByteArray(byte[] bArr);

    protected abstract void writeInt(int i);

    public void writeParcelable(Parcelable parcelable, int i) {
        mo101ja(i);
        mo91a(parcelable);
    }

    protected abstract void writeString(String str);

    /* renamed from: i */
    private static Class m86i(Class<? extends InterfaceC0507e> cls) {
        return Class.forName(String.format("%s.%sParcelizer", cls.getPackage().getName(), cls.getSimpleName()), false, cls.getClassLoader());
    }

    /* renamed from: a */
    protected void m92a(InterfaceC0507e interfaceC0507e) {
        if (interfaceC0507e == null) {
            writeString(null);
            return;
        }
        m85c(interfaceC0507e);
        AbstractC0505c mo88Za = mo88Za();
        m83a(interfaceC0507e, mo88Za);
        mo88Za.mo87Ya();
    }

    /* renamed from: a */
    protected static <T extends InterfaceC0507e> T m82a(String str, AbstractC0505c abstractC0505c) {
        try {
            return (T) Class.forName(str, true, AbstractC0505c.class.getClassLoader()).getDeclaredMethod("read", AbstractC0505c.class).invoke(null, abstractC0505c);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e3);
        } catch (InvocationTargetException e4) {
            if (e4.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e4.getCause());
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e4);
        }
    }

    /* renamed from: a */
    protected static <T extends InterfaceC0507e> void m83a(T t, AbstractC0505c abstractC0505c) {
        try {
            m84b(t).getDeclaredMethod("write", t.getClass(), AbstractC0505c.class).invoke(null, t, abstractC0505c);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e3);
        } catch (InvocationTargetException e4) {
            if (e4.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e4.getCause());
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e4);
        }
    }
}
