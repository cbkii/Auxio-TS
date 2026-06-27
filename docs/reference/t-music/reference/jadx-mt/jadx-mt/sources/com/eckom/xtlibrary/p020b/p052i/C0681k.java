package com.eckom.xtlibrary.p020b.p052i;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.k */
/* JADX INFO: compiled from: ThemeManager.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0681k {

    /* JADX INFO: renamed from: Ul */
    private Context f810Ul;

    /* JADX INFO: renamed from: Vl */
    private Map<String, C0682l> f811Vl;

    /* JADX INFO: renamed from: Wl */
    private List<Object> f812Wl;

    /* JADX INFO: renamed from: Xl */
    private List<InterfaceC0673c> f813Xl;

    /* JADX INFO: renamed from: Yl */
    private volatile C0682l f814Yl;

    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.k$a */
    /* JADX INFO: compiled from: ThemeManager.java */
    private static class a {
        static C0681k INSTANCE = new C0681k(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.k$b */
    /* JADX INFO: compiled from: ThemeManager.java */
    static class b implements Comparator<InterfaceC0673c> {
        private b() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(InterfaceC0673c interfaceC0673c, InterfaceC0673c interfaceC0673c2) {
            return interfaceC0673c2.mo925M() - interfaceC0673c.mo925M();
        }

        /* synthetic */ b(RunnableC0679i runnableC0679i) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.k$c */
    /* JADX INFO: compiled from: ThemeManager.java */
    static class c implements Comparator<InterfaceC0673c> {
        private c() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(InterfaceC0673c interfaceC0673c, InterfaceC0673c interfaceC0673c2) {
            return interfaceC0673c2.mo926V() - interfaceC0673c.mo926V();
        }

        /* synthetic */ c(RunnableC0679i runnableC0679i) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.k$d */
    /* JADX INFO: compiled from: ThemeManager.java */
    static class d implements Comparator<InterfaceC0673c> {
        private d() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(InterfaceC0673c interfaceC0673c, InterfaceC0673c interfaceC0673c2) {
            return interfaceC0673c2.mo930ia() - interfaceC0673c.mo930ia();
        }

        /* synthetic */ d(RunnableC0679i runnableC0679i) {
            this();
        }
    }

    /* synthetic */ C0681k(RunnableC0679i runnableC0679i) {
        this();
    }

    /* JADX INFO: renamed from: Gb */
    private C0682l m965Gb(String str) {
        return this.f811Vl.get(str);
    }

    /* JADX INFO: renamed from: b */
    private C0682l m971b(File file) {
        return m965Gb(file.getAbsolutePath());
    }

    /* JADX INFO: renamed from: ef */
    private void m972ef() {
        if (this.f810Ul == null) {
            throw new IllegalStateException("must be call init(Context ctx).");
        }
    }

    public static C0681k get() {
        return a.INSTANCE;
    }

    /* JADX INFO: renamed from: Kc */
    public synchronized Context m973Kc() {
        return this.f814Yl != null ? this.f814Yl.m986Mc() : null;
    }

    /* JADX INFO: renamed from: e */
    public void m978e(C0683m c0683m) {
        C0676f.runOnUiThread(new RunnableC0680j(this, c0683m));
    }

    public void init(Context context) {
        this.f810Ul = context;
    }

    private C0681k() {
        this.f811Vl = new ConcurrentHashMap();
        this.f812Wl = new CopyOnWriteArrayList();
        this.f813Xl = new ArrayList();
    }

    /* JADX INFO: renamed from: b */
    public void m977b(InterfaceC0673c interfaceC0673c) {
        this.f813Xl.remove(interfaceC0673c);
    }

    /* JADX INFO: renamed from: a */
    public C0682l m974a(File file) {
        m972ef();
        C0671a.m923a("ThemeManager", "loadThemePlugin : %s", file);
        if (file == null) {
            C0671a.m923a("ThemeManager", "error : plugin is null.", new Object[0]);
            return null;
        }
        if (!file.exists()) {
            C0671a.m923a("ThemeManager", "plugin is not exists. path = %s", file.getAbsoluteFile());
            return null;
        }
        m971b(file);
        C0682l c0682lM983a = C0682l.m983a(this, this.f810Ul, file);
        if (c0682lM983a != null) {
            this.f811Vl.put(file.getAbsolutePath(), c0682lM983a);
        }
        C0671a.m923a("ThemeManager", "load theme plugin : %s", file.getAbsoluteFile());
        StringBuilder sb = new StringBuilder();
        sb.append("loadThemePlugin: themePlugin is null：");
        sb.append(c0682lM983a == null);
        Log.d("ThemeManager", sb.toString());
        return c0682lM983a;
    }

    /* JADX INFO: renamed from: a */
    public synchronized void m976a(C0682l c0682l) {
        this.f814Yl = c0682l;
        Log.e("ThemeManager", "updateCurThemePlugin: " + c0682l);
    }

    /* JADX INFO: renamed from: a */
    public void m975a(InterfaceC0673c interfaceC0673c) {
        if (this.f813Xl.contains(interfaceC0673c)) {
            return;
        }
        this.f813Xl.add(interfaceC0673c);
        C0671a.m923a("ThemeManager", "registerThemeSwitchStatus : %s, size : %s", interfaceC0673c, Integer.valueOf(this.f813Xl.size()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x002a, code lost:
    
        com.eckom.xtlibrary.p020b.p052i.C0671a.m923a("ThemeManager", "notifyThemeSwitching: switch fail! %s", r2);
     */
    /* JADX INFO: renamed from: a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean m970a(List<InterfaceC0673c> list, C0683m c0683m) {
        C0671a.m923a("ThemeManager", "notifyThemeSwitching: start", new Object[0]);
        boolean z = true;
        try {
            Iterator<InterfaceC0673c> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                InterfaceC0673c next = it.next();
                C0671a.m923a("ThemeManager", "notifyThemeSwitching: switch! %s", next);
                if (!next.mo929b(c0683m)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        C0671a.m923a("ThemeManager", "notifyThemeSwitching: end", new Object[0]);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: a */
    public void m968a(List<InterfaceC0673c> list, C0683m c0683m, boolean z) {
        Iterator<InterfaceC0673c> it = list.iterator();
        while (it.hasNext()) {
            it.next().mo928a(c0683m, z);
        }
    }
}
