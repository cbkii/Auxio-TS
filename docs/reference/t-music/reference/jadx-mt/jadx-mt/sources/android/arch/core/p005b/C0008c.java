package android.arch.core.p005b;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: renamed from: android.arch.core.b.c */
/* JADX INFO: compiled from: SafeIterableMap.java */
/* JADX INFO: loaded from: classes.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class C0008c<K, V> implements Iterable<Map.Entry<K, V>> {
    private c<K, V> mEnd;
    private c<K, V> mStart;

    /* JADX INFO: renamed from: wa */
    private WeakHashMap<f<K, V>, Boolean> f30wa = new WeakHashMap<>();
    private int mSize = 0;

    /* JADX INFO: renamed from: android.arch.core.b.c$a */
    /* JADX INFO: compiled from: SafeIterableMap.java */
    static class a<K, V> extends e<K, V> {
        a(c<K, V> cVar, c<K, V> cVar2) {
            super(cVar, cVar2);
        }

        @Override // android.arch.core.p005b.C0008c.e
        /* JADX INFO: renamed from: b */
        c<K, V> mo18b(c<K, V> cVar) {
            return cVar.f31ta;
        }

        @Override // android.arch.core.p005b.C0008c.e
        /* JADX INFO: renamed from: c */
        c<K, V> mo19c(c<K, V> cVar) {
            return cVar.mNext;
        }
    }

    /* JADX INFO: renamed from: android.arch.core.b.c$b */
    /* JADX INFO: compiled from: SafeIterableMap.java */
    private static class b<K, V> extends e<K, V> {
        b(c<K, V> cVar, c<K, V> cVar2) {
            super(cVar, cVar2);
        }

        @Override // android.arch.core.p005b.C0008c.e
        /* JADX INFO: renamed from: b */
        c<K, V> mo18b(c<K, V> cVar) {
            return cVar.mNext;
        }

        @Override // android.arch.core.p005b.C0008c.e
        /* JADX INFO: renamed from: c */
        c<K, V> mo19c(c<K, V> cVar) {
            return cVar.f31ta;
        }
    }

    /* JADX INFO: renamed from: android.arch.core.b.c$c */
    /* JADX INFO: compiled from: SafeIterableMap.java */
    static class c<K, V> implements Map.Entry<K, V> {

        @NonNull
        final K mKey;
        c<K, V> mNext;

        @NonNull
        final V mValue;

        /* JADX INFO: renamed from: ta */
        c<K, V> f31ta;

        c(@NonNull K k, @NonNull V v) {
            this.mKey = k;
            this.mValue = v;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof c)) {
                return false;
            }
            c cVar = (c) obj;
            return this.mKey.equals(cVar.mKey) && this.mValue.equals(cVar.mValue);
        }

        @Override // java.util.Map.Entry
        @NonNull
        public K getKey() {
            return this.mKey;
        }

        @Override // java.util.Map.Entry
        @NonNull
        public V getValue() {
            return this.mValue;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.mValue.hashCode() ^ this.mKey.hashCode();
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            return this.mKey + "=" + this.mValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: android.arch.core.b.c$d */
    /* JADX INFO: compiled from: SafeIterableMap.java */
    public class d implements Iterator<Map.Entry<K, V>>, f<K, V> {
        private c<K, V> mCurrent;

        /* JADX INFO: renamed from: ua */
        private boolean f32ua;

        private d() {
            this.f32ua = true;
        }

        @Override // android.arch.core.p005b.C0008c.f
        /* JADX INFO: renamed from: a */
        public void mo20a(@NonNull c<K, V> cVar) {
            c<K, V> cVar2 = this.mCurrent;
            if (cVar == cVar2) {
                this.mCurrent = cVar2.f31ta;
                this.f32ua = this.mCurrent == null;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f32ua) {
                return C0008c.this.mStart != null;
            }
            c<K, V> cVar = this.mCurrent;
            return (cVar == null || cVar.mNext == null) ? false : true;
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            if (this.f32ua) {
                this.f32ua = false;
                this.mCurrent = C0008c.this.mStart;
            } else {
                c<K, V> cVar = this.mCurrent;
                this.mCurrent = cVar != null ? cVar.mNext : null;
            }
            return this.mCurrent;
        }
    }

    /* JADX INFO: renamed from: android.arch.core.b.c$e */
    /* JADX INFO: compiled from: SafeIterableMap.java */
    private static abstract class e<K, V> implements Iterator<Map.Entry<K, V>>, f<K, V> {
        c<K, V> mNext;

        /* JADX INFO: renamed from: va */
        c<K, V> f33va;

        e(c<K, V> cVar, c<K, V> cVar2) {
            this.f33va = cVar2;
            this.mNext = cVar;
        }

        private c<K, V> nextNode() {
            c<K, V> cVar = this.mNext;
            c<K, V> cVar2 = this.f33va;
            if (cVar == cVar2 || cVar2 == null) {
                return null;
            }
            return mo19c(cVar);
        }

        @Override // android.arch.core.p005b.C0008c.f
        /* JADX INFO: renamed from: a */
        public void mo20a(@NonNull c<K, V> cVar) {
            if (this.f33va == cVar && cVar == this.mNext) {
                this.mNext = null;
                this.f33va = null;
            }
            c<K, V> cVar2 = this.f33va;
            if (cVar2 == cVar) {
                this.f33va = mo18b(cVar2);
            }
            if (this.mNext == cVar) {
                this.mNext = nextNode();
            }
        }

        /* JADX INFO: renamed from: b */
        abstract c<K, V> mo18b(c<K, V> cVar);

        /* JADX INFO: renamed from: c */
        abstract c<K, V> mo19c(c<K, V> cVar);

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mNext != null;
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            c<K, V> cVar = this.mNext;
            this.mNext = nextNode();
            return cVar;
        }
    }

    /* JADX INFO: renamed from: android.arch.core.b.c$f */
    /* JADX INFO: compiled from: SafeIterableMap.java */
    interface f<K, V> {
        /* JADX INFO: renamed from: a */
        void mo20a(@NonNull c<K, V> cVar);
    }

    public Iterator<Map.Entry<K, V>> descendingIterator() {
        b bVar = new b(this.mEnd, this.mStart);
        this.f30wa.put(bVar, false);
        return bVar;
    }

    public Map.Entry<K, V> eldest() {
        return this.mStart;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof C0008c)) {
            return false;
        }
        C0008c c0008c = (C0008c) obj;
        if (size() != c0008c.size()) {
            return false;
        }
        Iterator<Map.Entry<K, V>> it = iterator();
        Iterator<Map.Entry<K, V>> it2 = c0008c.iterator();
        while (it.hasNext() && it2.hasNext()) {
            Map.Entry<K, V> next = it.next();
            Map.Entry<K, V> next2 = it2.next();
            if ((next == null && next2 != null) || (next != null && !next.equals(next2))) {
                return false;
            }
        }
        return (it.hasNext() || it2.hasNext()) ? false : true;
    }

    protected c<K, V> get(K k) {
        c<K, V> cVar = this.mStart;
        while (cVar != null && !cVar.mKey.equals(k)) {
            cVar = cVar.mNext;
        }
        return cVar;
    }

    public int hashCode() {
        Iterator<Map.Entry<K, V>> it = iterator();
        int iHashCode = 0;
        while (it.hasNext()) {
            iHashCode += it.next().hashCode();
        }
        return iHashCode;
    }

    @Override // java.lang.Iterable
    @NonNull
    public Iterator<Map.Entry<K, V>> iterator() {
        a aVar = new a(this.mStart, this.mEnd);
        this.f30wa.put(aVar, false);
        return aVar;
    }

    protected c<K, V> put(@NonNull K k, @NonNull V v) {
        c<K, V> cVar = new c<>(k, v);
        this.mSize++;
        c<K, V> cVar2 = this.mEnd;
        if (cVar2 == null) {
            this.mStart = cVar;
            this.mEnd = this.mStart;
            return cVar;
        }
        cVar2.mNext = cVar;
        cVar.f31ta = cVar2;
        this.mEnd = cVar;
        return cVar;
    }

    public V putIfAbsent(@NonNull K k, @NonNull V v) {
        c<K, V> cVar = get(k);
        if (cVar != null) {
            return cVar.mValue;
        }
        put(k, v);
        return null;
    }

    public V remove(@NonNull K k) {
        c<K, V> cVar = get(k);
        if (cVar == null) {
            return null;
        }
        this.mSize--;
        if (!this.f30wa.isEmpty()) {
            Iterator<f<K, V>> it = this.f30wa.keySet().iterator();
            while (it.hasNext()) {
                it.next().mo20a(cVar);
            }
        }
        c<K, V> cVar2 = cVar.f31ta;
        if (cVar2 != null) {
            cVar2.mNext = cVar.mNext;
        } else {
            this.mStart = cVar.mNext;
        }
        c<K, V> cVar3 = cVar.mNext;
        if (cVar3 != null) {
            cVar3.f31ta = cVar.f31ta;
        } else {
            this.mEnd = cVar.f31ta;
        }
        cVar.mNext = null;
        cVar.f31ta = null;
        return cVar.mValue;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator<Map.Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /* JADX INFO: renamed from: va */
    public C0008c<K, V>.d m16va() {
        C0008c<K, V>.d dVar = new d();
        this.f30wa.put(dVar, false);
        return dVar;
    }

    /* JADX INFO: renamed from: wa */
    public Map.Entry<K, V> m17wa() {
        return this.mEnd;
    }
}
