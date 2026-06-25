package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* compiled from: OnLifecycleEvent.java */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* renamed from: android.arch.lifecycle.n */
/* loaded from: classes.dex */
public @interface InterfaceC0025n {
    Lifecycle.Event value();
}
