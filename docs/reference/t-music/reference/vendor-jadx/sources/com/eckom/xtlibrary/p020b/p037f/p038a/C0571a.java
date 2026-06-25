package com.eckom.xtlibrary.p020b.p037f.p038a;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0577d;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p040c.InterfaceC0581a;
import com.eckom.xtlibrary.p020b.p053j.C0703s;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: MusicMediaParseTask.java */
/* renamed from: com.eckom.xtlibrary.b.f.a.a */
/* loaded from: classes3.dex */
public class C0571a extends Thread {

    /* renamed from: Zc */
    public InterfaceC0581a f467Zc;
    private Context mContext;
    private String mKey;
    private String path;

    /* renamed from: Dn */
    private List<String> f465Dn = new ArrayList();

    /* renamed from: En */
    private CopyOnWriteArrayList<C0579f> f466En = new CopyOnWriteArrayList<>();

    /* renamed from: yk */
    private CopyOnWriteArrayList<C0577d> f469yk = new CopyOnWriteArrayList<>();

    /* renamed from: xk */
    private CopyOnWriteArrayList<C0577d> f468xk = new CopyOnWriteArrayList<>();

    public C0571a(Context context, String str, String str2, List<String> list, InterfaceC0581a interfaceC0581a) {
        this.path = str;
        this.mKey = str2;
        this.f465Dn.addAll(list);
        this.f467Zc = interfaceC0581a;
        this.mContext = context;
    }

    /* renamed from: b */
    private void m425b(String str, String str2, String str3, String str4) {
        C0579f c0579f = new C0579f(str, str4, str2, str3, null);
        this.f466En.add(c0579f);
        C0577d c0577d = new C0577d(str3, c0579f);
        c0577d.setKey(this.mKey);
        C0577d m1040a = C0703s.m1040a(this.f469yk, str3);
        if (m1040a != null) {
            m1040a.m445tc().add(c0579f);
        } else {
            this.f469yk.add(c0577d);
        }
        C0577d c0577d2 = new C0577d(str2, c0579f);
        c0577d2.setKey(this.mKey);
        C0577d m1040a2 = C0703s.m1040a(this.f468xk, str2);
        if (m1040a2 != null) {
            m1040a2.m445tc().add(c0579f);
        } else {
            this.f468xk.add(c0577d2);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(8:(4:8|9|10|(3:11|12|(3:14|15|16)))|(8:36|37|(1:39)|(2:20|(1:22))|31|32|27|28)|18|(0)|31|32|27|28) */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x008e, code lost:
    
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x008f, code lost:
    
        r8 = r1;
        r1 = r0;
        r0 = r3;
        r3 = r8;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x007e A[Catch: Exception -> 0x0076, all -> 0x0099, TRY_LEAVE, TryCatch #2 {all -> 0x0099, blocks: (B:9:0x0017, B:12:0x003c, B:15:0x0040, B:37:0x006f, B:20:0x007e, B:32:0x0086, B:26:0x009e), top: B:8:0x0017 }] */
    /* renamed from: c */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void m426c(File file) {
        String str;
        String str2;
        String substring;
        String str3 = "unKnown";
        if (file != null && file.exists() && file.canRead()) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                try {
                    Log.d("MusicMediaParseTask", "getMusicinfo:00000 " + file.getPath());
                    mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
                    str = mediaMetadataRetriever.extractMetadata(2);
                    try {
                        str2 = mediaMetadataRetriever.extractMetadata(1);
                        try {
                            Log.d("MusicMediaParseTask", "getMusicinfo:11111 " + file.getPath());
                            substring = file.getName().substring(0, file.getName().lastIndexOf("."));
                        } catch (Exception e) {
                            e = e;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        str2 = "unKnown";
                    }
                } catch (Exception e3) {
                    e = e3;
                    str = "unKnown";
                    str2 = str;
                }
                if (str2 != null) {
                    try {
                        if (str2.equals("")) {
                        }
                        if (str != null) {
                            if (!str.equals("")) {
                                str3 = str;
                            }
                        }
                        m425b(substring, str2, str3, file.getPath());
                    } catch (Exception e4) {
                        str3 = substring;
                        e = e4;
                        m425b(str3, str2, str, file.getPath());
                        e.printStackTrace();
                    }
                }
                str2 = "unKnown";
                if (str != null) {
                }
                m425b(substring, str2, str3, file.getPath());
            } finally {
                mediaMetadataRetriever.release();
            }
        }
    }

    /* renamed from: jb */
    public void m427jb(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Log.d("MusicMediaParseTask", "scanMedia1:" + getName() + " currentTime=" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
        if (this.f465Dn != null) {
            for (int i = 0; i < this.f465Dn.size(); i++) {
                m426c(new File(this.f465Dn.get(i)));
            }
        }
        Log.d("MusicMediaParseTask", "scanMedia2:" + getName() + " currentTime=" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        this.f466En.clear();
        this.f468xk.clear();
        this.f469yk.clear();
        m427jb(this.path);
        InterfaceC0581a interfaceC0581a = this.f467Zc;
        if (interfaceC0581a != null) {
            interfaceC0581a.mo454a(this.path, this.mKey, this.f466En, this.f468xk, this.f469yk);
        }
    }
}
