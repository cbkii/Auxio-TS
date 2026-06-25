package antlr.collections.impl;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes3.dex */
public class VectorEnumeration implements Enumeration {

    /* JADX INFO: renamed from: i */
    public int f319i = 0;
    public Vector vector;

    public VectorEnumeration(Vector vector) {
        this.vector = vector;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        boolean z;
        synchronized (this.vector) {
            z = this.f319i <= this.vector.lastElement;
        }
        return z;
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        Object obj;
        synchronized (this.vector) {
            if (this.f319i > this.vector.lastElement) {
                throw new NoSuchElementException("VectorEnumerator");
            }
            Object[] objArr = this.vector.data;
            int i = this.f319i;
            this.f319i = i + 1;
            obj = objArr[i];
        }
        return obj;
    }
}
