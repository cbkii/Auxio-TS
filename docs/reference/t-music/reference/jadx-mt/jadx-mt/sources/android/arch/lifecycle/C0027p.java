package android.arch.lifecycle;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/* JADX INFO: renamed from: android.arch.lifecycle.p */
/* JADX INFO: compiled from: ViewModelProvider.java */
/* JADX INFO: loaded from: classes.dex */
public class C0027p {
    private final a mFactory;
    private final C0028q mViewModelStore;

    /* JADX INFO: renamed from: android.arch.lifecycle.p$a */
    /* JADX INFO: compiled from: ViewModelProvider.java */
    public interface a {
        @NonNull
        <T extends AbstractC0026o> T create(@NonNull Class<T> cls);
    }

    public C0027p(@NonNull C0028q c0028q, @NonNull a aVar) {
        this.mFactory = aVar;
        this.mViewModelStore = c0028q;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @NonNull
    @MainThread
    /* JADX INFO: renamed from: a */
    public <T extends AbstractC0026o> T m69a(@NonNull String str, @NonNull Class<T> cls) {
        T t = (T) this.mViewModelStore.get(str);
        if (cls.isInstance(t)) {
            return t;
        }
        T t2 = (T) this.mFactory.create(cls);
        this.mViewModelStore.m71a(str, t2);
        return t2;
    }

    @NonNull
    @MainThread
    /* JADX INFO: renamed from: c */
    public <T extends AbstractC0026o> T m70c(@NonNull Class<T> cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return (T) m69a("android.arch.lifecycle.ViewModelProvider.DefaultKey:" + canonicalName, cls);
    }
}
