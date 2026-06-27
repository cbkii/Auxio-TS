package antlr.collections.impl;

import java.util.Enumeration;
import java.util.Hashtable;

/* JADX INFO: loaded from: classes3.dex */
public class IndexedVector {
    public Vector elements;
    public Hashtable index;

    public IndexedVector() {
        this.elements = new Vector(10);
        this.index = new Hashtable(10);
    }

    public IndexedVector(int i) {
        this.elements = new Vector(i);
        this.index = new Hashtable(i);
    }

    public synchronized void appendElement(Object obj, Object obj2) {
        this.elements.appendElement(obj2);
        this.index.put(obj, obj2);
    }

    public Object elementAt(int i) {
        return this.elements.elementAt(i);
    }

    public Enumeration elements() {
        return this.elements.elements();
    }

    public Object getElement(Object obj) {
        return this.index.get(obj);
    }

    public synchronized boolean removeElement(Object obj) {
        Object obj2 = this.index.get(obj);
        if (obj2 == null) {
            return false;
        }
        this.index.remove(obj);
        this.elements.removeElement(obj2);
        return false;
    }

    public int size() {
        return this.elements.size();
    }
}
