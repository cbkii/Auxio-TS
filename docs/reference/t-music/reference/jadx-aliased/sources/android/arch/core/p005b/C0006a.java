package android.arch.core.p005b;

import android.arch.core.p005b.C0008c;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.util.HashMap;
import java.util.Map;

/* compiled from: FastSafeIterableMap.java */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* renamed from: android.arch.core.b.a */
/* loaded from: classes.dex */
public class C0006a<K, V> extends C0008c<K, V> {

    /* renamed from: xa */
    private HashMap<K, C0008c.c<K, V>> f29xa = new HashMap<>();

    /* renamed from: b */
    public Map.Entry<K, V> m14b(K k) {
        if (contains(k)) {
            return this.f29xa.get(k).f31ta;
        }
        return null;
    }

    public boolean contains(K k) {
        return this.f29xa.containsKey(k);
    }

    @Override // android.arch.core.p005b.C0008c
    protected C0008c.c<K, V> get(K k) {
        return this.f29xa.get(k);
    }

    @Override // android.arch.core.p005b.C0008c
    public V putIfAbsent(@NonNull K k, @NonNull V v) {
        C0008c.c<K, V> cVar = get(k);
        if (cVar != null) {
            return cVar.mValue;
        }
        this.f29xa.put(k, put(k, v));
        return null;
    }

    @Override // android.arch.core.p005b.C0008c
    public V remove(@NonNull K k) {
        V v = (V) super.remove(k);
        this.f29xa.remove(k);
        return v;
    }
}
