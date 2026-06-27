package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: renamed from: android.arch.lifecycle.q */
/* JADX INFO: compiled from: ViewModelStore.java */
/* JADX INFO: loaded from: classes.dex */
public class C0028q {
    private final HashMap<String, AbstractC0026o> mMap = new HashMap<>();

    /* JADX INFO: renamed from: a */
    final void m71a(String str, AbstractC0026o abstractC0026o) {
        AbstractC0026o abstractC0026oPut = this.mMap.put(str, abstractC0026o);
        if (abstractC0026oPut != null) {
            abstractC0026oPut.onCleared();
        }
    }

    public final void clear() {
        Iterator<AbstractC0026o> it = this.mMap.values().iterator();
        while (it.hasNext()) {
            it.next().onCleared();
        }
        this.mMap.clear();
    }

    final AbstractC0026o get(String str) {
        return this.mMap.get(str);
    }
}
