package cpdetector.util;

import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public final class Entry<V, K> implements Map.Entry<V, K> {
    public final V m_key;
    public K m_value;

    public Entry(V v, K k) {
        this.m_key = v;
        this.m_value = k;
    }

    @Override // java.util.Map.Entry
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Entry.class != obj.getClass()) {
            return false;
        }
        Entry entry = (Entry) obj;
        V v = this.m_key;
        if (v == null) {
            if (entry.m_key != null) {
                return false;
            }
        } else if (!v.equals(entry.m_key)) {
            return false;
        }
        K k = this.m_value;
        if (k == null) {
            if (entry.m_value != null) {
                return false;
            }
        } else if (!k.equals(entry.m_value)) {
            return false;
        }
        return true;
    }

    @Override // java.util.Map.Entry
    public V getKey() {
        return this.m_key;
    }

    @Override // java.util.Map.Entry
    public K getValue() {
        return this.m_value;
    }

    @Override // java.util.Map.Entry
    public int hashCode() {
        V v = this.m_key;
        int iHashCode = ((v == null ? 0 : v.hashCode()) + 31) * 31;
        K k = this.m_value;
        return iHashCode + (k != null ? k.hashCode() : 0);
    }

    @Override // java.util.Map.Entry
    public K setValue(K k) {
        K k2 = this.m_value;
        this.m_value = k;
        return k2;
    }
}
