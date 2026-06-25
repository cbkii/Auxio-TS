package antlr.collections.impl;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* loaded from: classes3.dex */
public class VectorEnumerator implements Enumeration {

    /* renamed from: i */
    public int f320i = 0;
    public Vector vector;

    public VectorEnumerator(Vector vector) {
        this.vector = vector;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        boolean z;
        synchronized (this.vector) {
            z = this.f320i <= this.vector.lastElement;
        }
        return z;
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        Object obj;
        synchronized (this.vector) {
            if (this.f320i > this.vector.lastElement) {
                throw new NoSuchElementException("VectorEnumerator");
            }
            Object[] objArr = this.vector.data;
            int i = this.f320i;
            this.f320i = i + 1;
            obj = objArr[i];
        }
        return obj;
    }
}
