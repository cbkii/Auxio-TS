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

/* compiled from: TWVideo.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.l */
/* loaded from: classes3.dex */
public class C0760l extends TWUtil {

    /* renamed from: Bd */
    @Deprecated
    public static String f981Bd = null;

    /* renamed from: Cd */
    @Deprecated
    public static String f982Cd = null;
    private static String TAG = "TWVideo";

    /* renamed from: hc */
    public static int f987hc;

    /* renamed from: ld */
    public static int f990ld;

    /* renamed from: Dd */
    public C0705b f992Dd;

    /* renamed from: Md */
    public int f994Md;

    /* renamed from: Nd */
    public int f995Nd;

    /* renamed from: kd */
    public int[] f998kd;

    /* renamed from: qd */
    public C0705b f999qd;

    /* renamed from: rd */
    public C0705b f1000rd;

    /* renamed from: td */
    public C0705b f1001td;

    /* renamed from: ud */
    public C0705b f1002ud;

    /* renamed from: jd */
    private static C0760l f989jd = new C0760l();
    private static int mCount = 0;

    /* renamed from: Qd */
    public static boolean f984Qd = false;

    /* renamed from: Hd */
    public static boolean f983Hd = true;

    /* renamed from: Rd */
    public static int f985Rd = -1;

    /* renamed from: Sd */
    public static int f986Sd = -1;

    /* renamed from: ic */
    public static int f988ic = 1;

    /* renamed from: Ad */
    public static int f980Ad = 0;

    /* renamed from: md */
    public static int f991md = 0;
    public int mSource = 0;

    /* renamed from: Ld */
    public boolean f993Ld = false;
    public MediaView mMediaPlayer = null;

    /* renamed from: Od */
    public List<String> f996Od = new ArrayList();

    /* renamed from: vd */
    public ArrayList<C0705b> f1003vd = new ArrayList<>();

    /* renamed from: wd */
    public ArrayList<C0705b> f1004wd = new ArrayList<>();

    /* renamed from: Pd */
    public ArrayList<C0704a> f997Pd = new ArrayList<>();

    /* renamed from: xd */
    public int f1005xd = 0;

    /* renamed from: yd */
    public int f1006yd = 0;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* renamed from: Sa */
    public static void m1298Sa() {
        BufferedWriter bufferedWriter;
        ?? r1 = 0;
        BufferedWriter bufferedWriter2 = null;
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
            bufferedWriter2 = bufferedWriter;
            new File("/data/tw/video").delete();
            if (bufferedWriter2 != null) {
                bufferedWriter2.close();
            }
            r1 = 438;
            FileUtils.setPermissions("/data/tw/video", 438, -1, -1);
        } catch (Throwable th2) {
            th = th2;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            throw th;
        }
        r1 = 438;
        FileUtils.setPermissions("/data/tw/video", 438, -1, -1);
    }

    @TargetApi(24)
    /* renamed from: Ta */
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

    /* renamed from: a */
    public void m1301a(C0705b c0705b, String str) {
        String str2;
        if (c0705b == null || str == null) {
            return;
        }
        BufferedReader bufferedReader = null;
        try {
            try {
                if (!f984Qd) {
                    if (!str.startsWith("/storage/usb") && !str.startsWith("/storage/extsd")) {
                        str2 = str + "/DCIM";
                    }
                    str2 = "/data/tw/" + str.substring(9);
                } else if (str.startsWith("/mnt/extsd")) {
                    str2 = "/data/tw/" + str.substring(5);
                } else if (str.startsWith("/mnt/usbhost/Storage")) {
                    str2 = "/data/tw/" + str.substring(13);
                } else {
                    str2 = str + "/DCIM";
                }
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader(str2 + "/.video"));
                try {
                    ArrayList arrayList = new ArrayList();
                    while (true) {
                        String readLine = bufferedReader2.readLine();
                        if (readLine == null) {
                            break;
                        }
                        File file = new File(str + "/" + readLine);
                        String upperCase = file.getName().toUpperCase(Locale.ENGLISH);
                        if (file.canRead() && file.isDirectory() && !upperCase.startsWith("ECC")) {
                            String name = file.getName();
                            String absolutePath = file.getAbsolutePath();
                            if (name.equals(".")) {
                                String substring = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
                                arrayList.add(new C0704a(substring.substring(substring.lastIndexOf("/") + 1), absolutePath));
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

    /* renamed from: b */
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

    /* renamed from: da */
    public void m1303da(int i) {
        write(40465, 192, i);
    }

    /* renamed from: pa */
    public void m1304pa(String str) {
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

    /* renamed from: qa */
    public void m1305qa(String str) {
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

    /* renamed from: ra */
    public void m1306ra(String str) {
        Iterator<C0705b> it = this.f1003vd.iterator();
        while (it.hasNext()) {
            C0705b next = it.next();
            if (str.equals(next.mName)) {
                C0705b c0705b = this.f1002ud;
                if (c0705b.f849ik == 1) {
                    c0705b = c0705b.f854nk;
                }
                String str2 = c0705b.mName;
                next.m1050wc();
                this.f1003vd.remove(next);
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

    /* renamed from: sa */
    public void m1307sa(String str) {
        Iterator<C0705b> it = this.f1004wd.iterator();
        while (it.hasNext()) {
            C0705b next = it.next();
            if (str.equals(next.mName)) {
                C0705b c0705b = this.f1002ud;
                if (c0705b.f849ik == 1) {
                    c0705b = c0705b.f854nk;
                }
                String str2 = c0705b.mName;
                next.m1050wc();
                this.f1004wd.remove(next);
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

    /* renamed from: w */
    public void m1308w(boolean z) {
        m1303da(z ? 9 : Cea708CCParser.Const.CODE_C1_DSW);
    }

    /* renamed from: a */
    public void m1300a(Context context, C0705b c0705b, String str) {
        if (c0705b == null || str == null) {
            return;
        }
        File[] listFiles = new File(str).listFiles(new C0759k(this));
        c0705b.f851kk = 0;
        if (listFiles != null) {
            c0705b.setLength(listFiles.length);
            for (File file : listFiles) {
                String name = file.getName();
                c0705b.m1049a(name.substring(0, name.lastIndexOf(".")), file.getAbsolutePath(), context != null ? C0750b.m1297b(context, str + "/" + name, this.f997Pd) : false);
            }
        }
    }
}
