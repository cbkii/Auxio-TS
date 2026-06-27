package com.eckom.xtlibrary.p020b.p037f.p038a;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0577d;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p040c.InterfaceC0581a;
import com.eckom.xtlibrary.p020b.p053j.C0703s;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.a.a */
/* JADX INFO: compiled from: MusicMediaParseTask.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0571a extends Thread {

    /* JADX INFO: renamed from: Zc */
    public InterfaceC0581a f467Zc;
    private Context mContext;
    private String mKey;
    private String path;

    /* JADX INFO: renamed from: Dn */
    private List<String> f465Dn = new ArrayList();

    /* JADX INFO: renamed from: En */
    private CopyOnWriteArrayList<C0579f> f466En = new CopyOnWriteArrayList<>();

    /* JADX INFO: renamed from: yk */
    private CopyOnWriteArrayList<C0577d> f469yk = new CopyOnWriteArrayList<>();

    /* JADX INFO: renamed from: xk */
    private CopyOnWriteArrayList<C0577d> f468xk = new CopyOnWriteArrayList<>();

    public C0571a(Context context, String str, String str2, List<String> list, InterfaceC0581a interfaceC0581a) {
        this.path = str;
        this.mKey = str2;
        this.f465Dn.addAll(list);
        this.f467Zc = interfaceC0581a;
        this.mContext = context;
    }

    /* JADX INFO: renamed from: b */
    private void m425b(String str, String str2, String str3, String str4) {
        C0579f c0579f = new C0579f(str, str4, str2, str3, null);
        this.f466En.add(c0579f);
        C0577d c0577d = new C0577d(str3, c0579f);
        c0577d.setKey(this.mKey);
        C0577d c0577dM1040a = C0703s.m1040a(this.f469yk, str3);
        if (c0577dM1040a != null) {
            c0577dM1040a.m445tc().add(c0579f);
        } else {
            this.f469yk.add(c0577d);
        }
        C0577d c0577d2 = new C0577d(str2, c0579f);
        c0577d2.setKey(this.mKey);
        C0577d c0577dM1040a2 = C0703s.m1040a(this.f468xk, str2);
        if (c0577dM1040a2 != null) {
            c0577dM1040a2.m445tc().add(c0579f);
        } else {
            this.f468xk.add(c0577d2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x007e A[Catch: Exception -> 0x0076, all -> 0x0099, TRY_LEAVE, TryCatch #2 {all -> 0x0099, blocks: (B:9:0x0017, B:11:0x003c, B:12:0x0040, B:15:0x006f, B:22:0x007e, B:26:0x0086, B:38:0x009e), top: B:45:0x0017 }] */
    /* JADX INFO: renamed from: c */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void m426c(File file) throws IOException {
        String strExtractMetadata;
        String strExtractMetadata2;
        String str = "unKnown";
        if (file != null && file.exists() && file.canRead()) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                try {
                    Log.d("MusicMediaParseTask", "getMusicinfo:00000 " + file.getPath());
                    mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
                    strExtractMetadata = mediaMetadataRetriever.extractMetadata(2);
                    try {
                        strExtractMetadata2 = mediaMetadataRetriever.extractMetadata(1);
                        try {
                            Log.d("MusicMediaParseTask", "getMusicinfo:11111 " + file.getPath());
                            String strSubstring = file.getName().substring(0, file.getName().lastIndexOf("."));
                            if (strExtractMetadata2 != null) {
                                try {
                                    if (strExtractMetadata2.equals("")) {
                                        strExtractMetadata2 = "unKnown";
                                    }
                                    if (strExtractMetadata != null) {
                                        if (!strExtractMetadata.equals("")) {
                                            str = strExtractMetadata;
                                        }
                                    }
                                    try {
                                        m425b(strSubstring, strExtractMetadata2, str, file.getPath());
                                    } catch (Exception e) {
                                        String str2 = str;
                                        str = strSubstring;
                                        e = e;
                                        strExtractMetadata = str2;
                                        m425b(str, strExtractMetadata2, strExtractMetadata, file.getPath());
                                        e.printStackTrace();
                                    }
                                } catch (Exception e2) {
                                    str = strSubstring;
                                    e = e2;
                                    m425b(str, strExtractMetadata2, strExtractMetadata, file.getPath());
                                    e.printStackTrace();
                                }
                            } else {
                                strExtractMetadata2 = "unKnown";
                                if (strExtractMetadata != null) {
                                }
                                m425b(strSubstring, strExtractMetadata2, str, file.getPath());
                            }
                        } catch (Exception e3) {
                            e = e3;
                        }
                    } catch (Exception e4) {
                        e = e4;
                        strExtractMetadata2 = "unKnown";
                    }
                } catch (Exception e5) {
                    e = e5;
                    strExtractMetadata = "unKnown";
                    strExtractMetadata2 = strExtractMetadata;
                }
            } finally {
                mediaMetadataRetriever.release();
            }
        }
    }

    /* JADX INFO: renamed from: jb */
    public void m427jb(String str) throws IOException {
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
    public void run() throws IOException {
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
