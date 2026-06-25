package antlr.collections;

import java.util.Enumeration;

/* loaded from: classes3.dex */
public interface List {
    void add(Object obj);

    void append(Object obj);

    Object elementAt(int i);

    Enumeration elements();

    boolean includes(Object obj);

    int length();
}
