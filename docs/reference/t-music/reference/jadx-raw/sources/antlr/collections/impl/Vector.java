package antlr.collections.impl;

import java.util.Enumeration;

/* loaded from: classes3.dex */
public class Vector implements Cloneable {
    public Object[] data;
    public int lastElement;

    public Vector() {
        this(10);
    }

    public Vector(int i) {
        this.lastElement = -1;
        this.data = new Object[i];
    }

    public synchronized void appendElement(Object obj) {
        ensureCapacity(this.lastElement + 2);
        Object[] objArr = this.data;
        int i = this.lastElement + 1;
        this.lastElement = i;
        objArr[i] = obj;
    }

    public int capacity() {
        return this.data.length;
    }

    public Object clone() {
        try {
            Vector vector = (Vector) super.clone();
            vector.data = new Object[size()];
            System.arraycopy(this.data, 0, vector.data, 0, size());
            return vector;
        } catch (CloneNotSupportedException unused) {
            System.err.println("cannot clone Vector.super");
            return null;
        }
    }

    public synchronized Object elementAt(int i) {
        Object[] objArr;
        objArr = this.data;
        if (i >= objArr.length) {
            throw new ArrayIndexOutOfBoundsException(i + " >= " + this.data.length);
        }
        if (i < 0) {
            throw new ArrayIndexOutOfBoundsException(i + " < 0 ");
        }
        return objArr[i];
    }

    public synchronized Enumeration elements() {
        return new VectorEnumerator(this);
    }

    public synchronized void ensureCapacity(int i) {
        int i2 = i + 1;
        Object[] objArr = this.data;
        if (i2 > objArr.length) {
            int length = objArr.length * 2;
            if (i2 <= length) {
                i2 = length;
            }
            this.data = new Object[i2];
            System.arraycopy(objArr, 0, this.data, 0, objArr.length);
        }
    }

    public synchronized boolean removeElement(Object obj) {
        int i = 0;
        while (i <= this.lastElement && this.data[i] != obj) {
            i++;
        }
        int i2 = this.lastElement;
        if (i > i2) {
            return false;
        }
        Object[] objArr = this.data;
        objArr[i] = null;
        int i3 = i2 - i;
        if (i3 > 0) {
            System.arraycopy(objArr, i + 1, objArr, i, i3);
        }
        this.lastElement--;
        return true;
    }

    public synchronized void setElementAt(Object obj, int i) {
        Object[] objArr = this.data;
        if (i >= objArr.length) {
            throw new ArrayIndexOutOfBoundsException(i + " >= " + this.data.length);
        }
        objArr[i] = obj;
        if (i > this.lastElement) {
            this.lastElement = i;
        }
    }

    public int size() {
        return this.lastElement + 1;
    }
}
