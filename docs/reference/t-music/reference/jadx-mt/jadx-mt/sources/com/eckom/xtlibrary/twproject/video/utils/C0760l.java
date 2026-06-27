package com.eckom.xtlibrary.twproject.video.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.FileUtils;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.media.subtitle.Cea708CCParser;
import android.tw.john.TWUtil;
import android.util.Log;
import android.view.WindowManagerGlobal;
import com.eckom.xtlibrary.p020b.p054k.p055a.C0704a;
import com.eckom.xtlibrary.p020b.p054k.p055a.C0705b;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.l */
/* JADX INFO: compiled from: TWVideo.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0760l extends TWUtil {

    /* JADX INFO: renamed from: Bd */
    @Deprecated
    public static String f981Bd = null;

    /* JADX INFO: renamed from: Cd */
    @Deprecated
    public static String f982Cd = null;
    private static String TAG = "TWVideo";

    /* JADX INFO: renamed from: hc */
    public static int f987hc;

    /* JADX INFO: renamed from: ld */
    public static int f990ld;

    /* JADX INFO: renamed from: Dd */
    public C0705b f992Dd;

    /* JADX INFO: renamed from: Md */
    public int f994Md;

    /* JADX INFO: renamed from: Nd */
    public int f995Nd;

    /* JADX INFO: renamed from: kd */
    public int[] f998kd;

    /* JADX INFO: renamed from: qd */
    public C0705b f999qd;

    /* JADX INFO: renamed from: rd */
    public C0705b f1000rd;

    /* JADX INFO: renamed from: td */
    public C0705b f1001td;

    /* JADX INFO: renamed from: ud */
    public C0705b f1002ud;

    /* JADX INFO: renamed from: jd */
    private static C0760l f989jd = new C0760l();
    private static int mCount = 0;

    /* JADX INFO: renamed from: Qd */
    public static boolean f984Qd = false;

    /* JADX INFO: renamed from: Hd */
    public static boolean f983Hd = true;

    /* JADX INFO: renamed from: Rd */
    public static int f985Rd = -1;

    /* JADX INFO: renamed from: Sd */
    public static int f986Sd = -1;

    /* JADX INFO: renamed from: ic */
    public static int f988ic = 1;

    /* JADX INFO: renamed from: Ad */
    public static int f980Ad = 0;

    /* JADX INFO: renamed from: md */
    public static int f991md = 0;
    public int mSource = 0;

    /* JADX INFO: renamed from: Ld */
    public boolean f993Ld = false;
    public MediaView mMediaPlayer = null;

    /* JADX INFO: renamed from: Od */
    public List<String> f996Od = new ArrayList();

    /* JADX INFO: renamed from: vd */
    public ArrayList<C0705b> f1003vd = new ArrayList<>();

    /* JADX INFO: renamed from: wd */
    public ArrayList<C0705b> f1004wd = new ArrayList<>();

    /* JADX INFO: renamed from: Pd */
    public ArrayList<C0704a> f997Pd = new ArrayList<>();

    /* JADX INFO: renamed from: xd */
    public int f1005xd = 0;

    /* JADX INFO: renamed from: yd */
    public int f1006yd = 0;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.io.BufferedWriter] */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.io.BufferedWriter] */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.BufferedWriter] */
    /* JADX INFO: renamed from: Sa */
    public static void m1298Sa() throws Throwable {
        ?? bufferedWriter;
        ?? r1 = 0;
        ?? r12 = 0;
        try {
            try {
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter("/data/tw/video"));
                } catch (Exception unused) {
                    return;
                }
            } catch (Exception unused2) {
            }
        } catch (Throwable th) {
            th = th;
            bufferedWriter = r1;
        }
        try {
            bufferedWriter.write(f981Bd);
            bufferedWriter.write(10);
            bufferedWriter.write(Integer.toString(f980Ad));
            bufferedWriter.write(10);
            bufferedWriter.write(Integer.toString(f991md));
            bufferedWriter.write(10);
            bufferedWriter.write(Integer.toString(f987hc));
            bufferedWriter.write(10);
            bufferedWriter.write(Integer.toString(f988ic));
            bufferedWriter.write(10);
            bufferedWriter.flush();
            f989jd.write(40730, 1, 0, "sync");
            bufferedWriter.close();
        } catch (Exception unused3) {
            r12 = bufferedWriter;
            new File("/data/tw/video").delete();
            if (r12 != 0) {
                r12.close();
            }
        } catch (Throwable th2) {
            th = th2;
            if (bufferedWriter != 0) {
                bufferedWriter.close();
            }
            throw th;
        }
        r1 = 438;
        FileUtils.setPermissions("/data/tw/video", 438, -1, -1);
    }

    @TargetApi(24)
    /* JADX INFO: renamed from: Ta */
    public boolean m1299Ta() {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                return WindowManagerGlobal.getWindowManagerService().getDockedStackSide() > 0;
            }
        } catch (Exception e) {
            Log.e(TAG, "getMultiWindowMode:" + e.getMessage());
        }
        return false;
    }

    /* JADX INFO: renamed from: a */
    public void m1301a(C0705b c0705b, String str) throws Throwable {
        String str2;
        if (c0705b == null || str == null) {
            return;
        }
        BufferedReader bufferedReader = null;
        try {
            try {
                if (f984Qd) {
                    if (str.startsWith("/mnt/extsd")) {
                        str2 = "/data/tw/" + str.substring(5);
                    } else if (str.startsWith("/mnt/usbhost/Storage")) {
                        str2 = "/data/tw/" + str.substring(13);
                    } else {
                        str2 = str + "/DCIM";
                    }
                } else if (str.startsWith("/storage/usb") || str.startsWith("/storage/extsd")) {
                    str2 = "/data/tw/" + str.substring(9);
                } else {
                    str2 = str + "/DCIM";
                }
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader(str2 + "/.video"));
                try {
                    ArrayList arrayList = new ArrayList();
                    while (true) {
                        String line = bufferedReader2.readLine();
                        if (line == null) {
                            break;
                        }
                        File file = new File(str + "/" + line);
                        String upperCase = file.getName().toUpperCase(Locale.ENGLISH);
                        if (file.canRead() && file.isDirectory() && !upperCase.startsWith("ECC")) {
                            String name = file.getName();
                            String absolutePath = file.getAbsolutePath();
                            if (name.equals(".")) {
                                String strSubstring = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
                                arrayList.add(new C0704a(strSubstring.substring(strSubstring.lastIndexOf("/") + 1), absolutePath));
                            } else {
                                arrayList.add(new C0704a(name, absolutePath));
                            }
                        }
                    }
                    c0705b.setLength(arrayList.size());
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        c0705b.m1048a((C0704a) it.next());
                    }
                    arrayList.clear();
                    bufferedReader2.close();
                } catch (Exception unused) {
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    throw th;
                }
            } catch (Exception unused2) {
            }
        } catch (Exception unused3) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: renamed from: b */
    public void m1302b(int i, int i2, int i3, int i4, int i5) {
        write(1282, (i2 & SupportMenu.USER_MASK) | (i3 << 16), (i << 31) | ((i5 & 127) << 24) | (16777215 & i4));
    }

    public void close() {
        int i = mCount;
        if (i > 0) {
            int i2 = i - 1;
            mCount = i2;
            if (i2 == 0) {
                stop();
                super.close();
            }
        }
    }

    /* JADX INFO: renamed from: da */
    public void m1303da(int i) {
        write(40465, 192, i);
    }

    /* JADX INFO: renamed from: pa */
    public void m1304pa(String str) throws Throwable {
        Iterator<C0705b> it = this.f1003vd.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0705b c0705b = new C0705b(str, 1, 0);
        m1301a(c0705b, str);
        this.f1003vd.add(c0705b);
        C0705b c0705b2 = this.f1002ud;
        if (c0705b2 == null || !c0705b2.mName.equals("SD")) {
            return;
        }
        this.f1002ud = this.f1003vd.get(0);
    }

    /* JADX INFO: renamed from: qa */
    public void m1305qa(String str) throws Throwable {
        Iterator<C0705b> it = this.f1004wd.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0705b c0705b = new C0705b(str, 2, 0);
        m1301a(c0705b, str);
        this.f1004wd.add(c0705b);
        C0705b c0705b2 = this.f1002ud;
        if (c0705b2 == null || !c0705b2.mName.equals("USB")) {
            return;
        }
        this.f1002ud = this.f1004wd.get(0);
    }

    /* JADX INFO: renamed from: ra */
    public void m1306ra(String str) {
        for (C0705b c0705b : this.f1003vd) {
            if (str.equals(c0705b.mName)) {
                C0705b c0705b2 = this.f1002ud;
                if (c0705b2.f849ik == 1) {
                    c0705b2 = c0705b2.f854nk;
                }
                String str2 = c0705b2.mName;
                c0705b.m1050wc();
                this.f1003vd.remove(c0705b);
                if (this.f1005xd >= this.f1003vd.size()) {
                    this.f1005xd = this.f1003vd.size() - 1;
                    if (this.f1005xd < 0) {
                        this.f1005xd = 0;
                    }
                }
                if (str.equals(str2)) {
                    if (this.f1003vd.size() > 0) {
                        this.f1002ud = this.f1003vd.get(this.f1005xd);
                        return;
                    } else {
                        this.f1002ud = this.f999qd;
                        return;
                    }
                }
                return;
            }
        }
    }

    /* JADX INFO: renamed from: sa */
    public void m1307sa(String str) {
        for (C0705b c0705b : this.f1004wd) {
            if (str.equals(c0705b.mName)) {
                C0705b c0705b2 = this.f1002ud;
                if (c0705b2.f849ik == 1) {
                    c0705b2 = c0705b2.f854nk;
                }
                String str2 = c0705b2.mName;
                c0705b.m1050wc();
                this.f1004wd.remove(c0705b);
                if (this.f1006yd >= this.f1004wd.size()) {
                    this.f1006yd = this.f1004wd.size() - 1;
                    if (this.f1006yd < 0) {
                        this.f1006yd = 0;
                    }
                }
                if (str.equals(str2)) {
                    if (this.f1004wd.size() > 0) {
                        this.f1002ud = this.f1004wd.get(this.f1006yd);
                        return;
                    } else {
                        this.f1002ud = this.f1000rd;
                        return;
                    }
                }
                return;
            }
        }
    }

    /* JADX INFO: renamed from: w */
    public void m1308w(boolean z) {
        m1303da(z ? 9 : Cea708CCParser.Const.CODE_C1_DSW);
    }

    /* JADX INFO: renamed from: a */
    public void m1300a(Context context, C0705b c0705b, String str) {
        if (c0705b == null || str == null) {
            return;
        }
        File[] fileArrListFiles = new File(str).listFiles(new C0759k(this));
        c0705b.f851kk = 0;
        if (fileArrListFiles != null) {
            c0705b.setLength(fileArrListFiles.length);
            for (File file : fileArrListFiles) {
                String name = file.getName();
                c0705b.m1049a(name.substring(0, name.lastIndexOf(".")), file.getAbsolutePath(), context != null ? C0750b.m1297b(context, str + "/" + name, this.f997Pd) : false);
            }
        }
    }
}
