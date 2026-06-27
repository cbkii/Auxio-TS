package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: renamed from: android.arch.lifecycle.n */
/* JADX INFO: compiled from: OnLifecycleEvent.java */
/* JADX INFO: loaded from: classes.dex */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceC0025n {
    Lifecycle.Event value();
}
