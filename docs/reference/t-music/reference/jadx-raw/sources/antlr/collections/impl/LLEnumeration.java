package antlr.collections.impl;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* loaded from: classes3.dex */
public final class LLEnumeration implements Enumeration {
    public LLCell cursor;
    public LList list;

    public LLEnumeration(LList lList) {
        this.list = lList;
        this.cursor = this.list.head;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return this.cursor != null;
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        if (!hasMoreElements()) {
            throw new NoSuchElementException();
        }
        LLCell lLCell = this.cursor;
        this.cursor = lLCell.next;
        return lLCell.data;
    }
}
