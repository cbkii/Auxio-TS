package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.content.Context;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p037f.p038a.C0571a;
import com.eckom.xtlibrary.p020b.p037f.p038a.C0573c;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p040c.InterfaceC0581a;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* compiled from: MusicIjkID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.y */
/* loaded from: classes3.dex */
class C0633y extends Thread {

    /* renamed from: Fn */
    final /* synthetic */ C0579f[] f668Fn;
    final /* synthetic */ C0593L this$0;

    /* renamed from: tk */
    final /* synthetic */ String f669tk;

    C0633y(C0593L c0593l, C0579f[] c0579fArr, String str) {
        this.this$0 = c0593l;
        this.f668Fn = c0579fArr;
        this.f669tk = str;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        String str;
        String substring;
        C0580g c0580g;
        Context context;
        InterfaceC0581a interfaceC0581a;
        C0573c c0573c;
        super.run();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.f668Fn.length; i++) {
            arrayList.clear();
            C0579f c0579f = this.f668Fn[i];
            File[] listFiles = new File(c0579f.mPath).listFiles(new C0632x(this));
            if (listFiles != null) {
                c0579f.mLength = listFiles.length;
                C0580g c0580g2 = null;
                if (this.f669tk.startsWith("/mnt/sdcard")) {
                    c0580g2 = new C0580g(c0579f.mName, 3, 0, 1);
                    c0580g2.mKey = "/mnt/sdcard";
                    c0580g2.setLength(listFiles.length);
                    str = "/mnt/sdcard";
                } else {
                    if (this.f669tk.startsWith("/storage/usb")) {
                        substring = this.f669tk.substring(9);
                        c0580g = new C0580g(c0579f.mName, 2, 0, 1);
                        c0580g.mKey = substring;
                        c0580g.setLength(listFiles.length);
                    } else if (this.f669tk.startsWith("/storage/extsd")) {
                        substring = this.f669tk.substring(9);
                        c0580g = new C0580g(c0579f.mName, 1, 0, 1);
                        c0580g.mKey = substring;
                        c0580g.setLength(listFiles.length);
                    } else {
                        str = "";
                    }
                    str = substring;
                    c0580g2 = c0580g;
                }
                for (File file : listFiles) {
                    String absolutePath = file.getAbsolutePath();
                    File file2 = new File(absolutePath);
                    if (file2.exists()) {
                        C0579f c0579f2 = new C0579f(file2.getName(), absolutePath);
                        if (c0580g2 != null) {
                            c0580g2.m449a(c0579f2);
                        }
                    }
                    arrayList.add(absolutePath);
                }
                if (this.f669tk.startsWith("/mnt/sdcard")) {
                    this.this$0.f574Yc.f507Tj.add(c0580g2);
                } else if (this.f669tk.startsWith("/storage/usb")) {
                    ArrayList<C0580g> arrayList2 = this.this$0.f574Yc.f483Aj.get(c0580g2.mKey);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList<>();
                        this.this$0.f574Yc.f483Aj.put(c0580g2.mKey, arrayList2);
                    }
                    arrayList2.add(c0580g2);
                } else if (this.f669tk.startsWith("/storage/extsd")) {
                    ArrayList<C0580g> arrayList3 = this.this$0.f574Yc.f499Mj.get(c0580g2.mKey);
                    if (arrayList3 == null) {
                        arrayList3 = new ArrayList<>();
                        this.this$0.f574Yc.f499Mj.put(c0580g2.mKey, arrayList3);
                    }
                    arrayList3.add(c0580g2);
                }
                int size = arrayList.size();
                int i2 = size > 5 ? size / 5 : 1;
                Log.d("MusicIjkID3Model", "scanMediaID3 run: threadSize=" + i2);
                int i3 = 0;
                while (i3 < i2) {
                    int i4 = i3 + 1;
                    int i5 = i4 * 5;
                    if (i5 > size) {
                        i5 = size;
                    }
                    List subList = arrayList.subList(i3 * 5, i5);
                    context = this.this$0.mContext;
                    String str2 = this.f669tk;
                    interfaceC0581a = this.this$0.f588ui;
                    C0571a c0571a = new C0571a(context, str2, str, subList, interfaceC0581a);
                    c0573c = this.this$0.f584ni;
                    c0573c.m439a(this.f669tk, c0571a);
                    i3 = i4;
                }
            }
        }
    }
}
