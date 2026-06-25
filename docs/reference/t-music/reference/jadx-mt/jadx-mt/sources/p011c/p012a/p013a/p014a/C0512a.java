package p011c.p012a.p013a.p014a;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: c.a.a.a.a */
/* JADX INFO: compiled from: Util.java */
/* JADX INFO: loaded from: classes3.dex */
public final class C0512a {

    /* JADX INFO: renamed from: Jf */
    private static final char[] f328Jf = "0123456789abcdef".toCharArray();

    /* JADX INFO: renamed from: Kf */
    private static final char[] f329Kf = new char[64];

    /* JADX INFO: renamed from: Lf */
    private static final char[] f330Lf = new char[40];

    /* JADX INFO: renamed from: a */
    public static <T> List<T> m116a(Collection<T> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        Iterator<T> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }
}
