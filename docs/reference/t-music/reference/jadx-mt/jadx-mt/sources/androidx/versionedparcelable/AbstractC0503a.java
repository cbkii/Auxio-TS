package androidx.versionedparcelable;

import android.support.annotation.RestrictTo;

/* JADX INFO: renamed from: androidx.versionedparcelable.a */
/* JADX INFO: compiled from: CustomVersionedParcelable.java */
/* JADX INFO: loaded from: classes3.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class AbstractC0503a implements InterfaceC0507e {
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public abstract void onPostParceling();

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public abstract void onPreParceling(boolean z);
}
