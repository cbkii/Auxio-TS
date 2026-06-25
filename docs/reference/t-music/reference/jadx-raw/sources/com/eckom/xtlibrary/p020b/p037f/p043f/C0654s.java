package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemProperties;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.media.subtitle.Cea708CCParser;
import android.text.TextUtils;
import android.tw.john.TWUtil;
import android.util.Log;
import com.eckom.xtlibrary.R$array;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* compiled from: TWMusic.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.s */
/* loaded from: classes3.dex */
public class C0654s extends TWUtil {

    /* renamed from: Cd */
    public static String f703Cd = null;

    /* renamed from: Dd */
    public static C0580g f704Dd = null;

    /* renamed from: Ed */
    public static ArrayList<C0579f> f705Ed = null;

    /* renamed from: Fd */
    public static C0580g f706Fd = null;

    /* renamed from: Id */
    public static int f709Id = 0;

    /* renamed from: Kd */
    public static String[] f711Kd = null;
    private static String TAG = "TWMusic";
    public static boolean isForward;

    /* renamed from: Fb */
    public Bitmap f714Fb;

    /* renamed from: hc */
    public int f715hc;

    /* renamed from: kd */
    public int[] f717kd;

    /* renamed from: ld */
    public int f718ld;
    public int mDuration;
    public int mSource;

    /* renamed from: md */
    public int f719md;

    /* renamed from: qd */
    public C0580g f723qd;

    /* renamed from: rd */
    public C0580g f724rd;

    /* renamed from: td */
    public C0580g f725td;

    /* renamed from: ud */
    public C0580g f726ud;

    /* renamed from: jd */
    private static C0654s f713jd = new C0654s();
    private static int mCount = 0;

    /* renamed from: Ad */
    public static int f701Ad = 0;

    /* renamed from: Bd */
    public static String f702Bd = "";

    /* renamed from: Tc */
    public static ArrayList<C0579f> f712Tc = new ArrayList<>();

    /* renamed from: Gd */
    public static boolean f707Gd = false;

    /* renamed from: Hd */
    public static boolean f708Hd = true;

    /* renamed from: Jd */
    public static boolean f710Jd = false;

    /* renamed from: ic */
    public int f716ic = 1;

    /* renamed from: nd */
    public String f720nd = "";

    /* renamed from: od */
    public String f721od = "";

    /* renamed from: pd */
    public String f722pd = "";

    /* renamed from: vd */
    public ArrayList<C0580g> f727vd = new ArrayList<>();

    /* renamed from: wd */
    public ArrayList<C0580g> f728wd = new ArrayList<>();

    /* renamed from: xd */
    public int f729xd = 0;

    /* renamed from: yd */
    public int f730yd = 0;
    private int mService = 0;

    /* renamed from: zd */
    public List<String> f731zd = new ArrayList();

    static {
        isForward = SystemProperties.getInt("persist.media.forward", 1) == 1;
    }

    /* renamed from: Ra */
    public static C0580g m773Ra() {
        C0579f[] c0579fArr = new C0579f[f712Tc.size()];
        for (int i = 0; i < f712Tc.size(); i++) {
            c0579fArr[i] = new C0579f(f712Tc.get(i).mName, f712Tc.get(i).mPath, true);
        }
        C0580g c0580g = f706Fd;
        c0580g.f544jk = c0579fArr;
        c0580g.mLength = f712Tc.size();
        return f706Fd;
    }

    /* renamed from: a */
    public static C0654s m774a(boolean z, boolean z2, Context context) {
        int i = mCount;
        mCount = i + 1;
        if (i == 0) {
            if (f713jd.open(new short[]{274, 513, 514, 515, 524, 769, 770, 772, 1296, -25085, -25057, -24804}) != 0) {
                mCount--;
                return null;
            }
            if (z2) {
                IjkMediaPlayer.loadLibrariesOnce(null);
            }
            f713jd.start();
            f713jd.m775e(context);
            f707Gd = z;
            f708Hd = z2;
            f711Kd = context.getResources().getStringArray(R$array.music_supported_formats);
            f713jd.f731zd = Arrays.asList(f711Kd);
            f704Dd = new C0580g("Playlist", 0, 0);
            f706Fd = new C0580g("LIKE", 4, 0);
            f713jd.m778a(context, f704Dd, f703Cd);
            f713jd.m783ea(f701Ad);
            f705Ed = new ArrayList<>();
            f709Id = f713jd.write(65521);
            f710Jd = new File("/system/bin/z-sender").exists() || f709Id == 35;
        }
        return f713jd;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x005c, code lost:
    
        if (r0 == null) goto L16;
     */
    /* renamed from: e */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void m775e(Context context) {
        BufferedReader bufferedReader;
        Throwable th;
        int lastIndexOf;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader("/data/tw/music"));
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            bufferedReader = null;
        } catch (Throwable th2) {
            bufferedReader = null;
            th = th2;
        }
        try {
            f702Bd = bufferedReader.readLine();
            f701Ad = Integer.valueOf(bufferedReader.readLine()).intValue();
            this.f719md = Integer.valueOf(bufferedReader.readLine()).intValue();
            this.f715hc = Integer.valueOf(bufferedReader.readLine()).intValue();
            this.f716ic = Integer.valueOf(bufferedReader.readLine()).intValue();
        } catch (Exception unused3) {
        } catch (Throwable th3) {
            th = th3;
            if (bufferedReader == null) {
                throw th;
            }
            bufferedReader.close();
            throw th;
        }
        bufferedReader.close();
        if (this.f716ic < 1) {
            this.f716ic = 1;
        }
        if (TextUtils.isEmpty(f702Bd) || (lastIndexOf = f702Bd.lastIndexOf("/")) <= 0) {
            return;
        }
        f703Cd = f702Bd.substring(0, lastIndexOf);
    }

    /* renamed from: m */
    private int m776m(int i, int i2) {
        int random;
        do {
            random = (int) (Math.random() * i2);
            if (random != 0) {
                break;
            }
        } while (i == 0);
        int i3 = random;
        while (i3 < i2 && this.f717kd[i3] != 0) {
            i3++;
        }
        if (i3 == i2) {
            i3 = 1;
            while (i3 < random && this.f717kd[i3] != 0) {
                i3++;
            }
        }
        return i3;
    }

    /* renamed from: Sa */
    public void m777Sa() {
        new C0653r(this).start();
    }

    /* renamed from: b */
    public void m779b(int i, int i2, int i3, int i4, int i5) {
        write(1282, (i2 & SupportMenu.USER_MASK) | (i3 << 16), (i << 31) | ((i5 & 127) << 24) | (16777215 & i4));
    }

    /* renamed from: ca */
    public void m781ca(int i) {
        this.mService = i;
        write(40448, i);
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
    public void m782da(int i) {
        write(40465, 192, i);
    }

    /* renamed from: ea */
    public void m783ea(int i) {
        int i2;
        this.f717kd = null;
        this.f718ld = 0;
        C0580g c0580g = f704Dd;
        if (c0580g == null || (i2 = c0580g.f545kk) <= 0) {
            return;
        }
        this.f717kd = new int[i2];
        if (i >= i2) {
            i = 0;
        }
        this.f717kd[0] = i;
        if (i2 > 1) {
            for (int i3 = i + 1; i3 < i2; i3++) {
                int i4 = i3 - i;
                if (this.f715hc != 0) {
                    i4 = m776m(i, i2);
                }
                this.f717kd[i4] = i3;
            }
            for (int i5 = 0; i5 < i; i5++) {
                int i6 = (i5 + i2) - i;
                if (this.f715hc != 0) {
                    i6 = m776m(i, i2);
                }
                this.f717kd[i6] = i5;
            }
        }
    }

    public int getService() {
        return this.mService;
    }

    /* renamed from: pa */
    public void m784pa(String str) {
        Iterator<C0580g> it = this.f727vd.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0580g c0580g = new C0580g(str, 1, 0);
        m780b(c0580g, str);
        if (c0580g.mLength > 0) {
            this.f727vd.add(c0580g);
        }
        C0580g c0580g2 = this.f726ud;
        if (c0580g2 == null || !c0580g2.mName.equals("SD")) {
            return;
        }
        this.f726ud = this.f727vd.get(0);
    }

    /* renamed from: qa */
    public void m785qa(String str) {
        Iterator<C0580g> it = this.f728wd.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0580g c0580g = new C0580g(str, 2, 0);
        m780b(c0580g, str);
        if (c0580g.mLength > 0) {
            this.f728wd.add(c0580g);
        }
        C0580g c0580g2 = this.f726ud;
        if (c0580g2 == null || !c0580g2.mName.equals("USB")) {
            return;
        }
        this.f726ud = this.f728wd.get(0);
    }

    /* renamed from: ra */
    public void m786ra(String str) {
        Iterator<C0580g> it = this.f727vd.iterator();
        while (it.hasNext()) {
            C0580g next = it.next();
            if (str.equals(next.mName)) {
                C0580g c0580g = this.f726ud;
                if (c0580g.f543ik == 1) {
                    c0580g = c0580g.f548nk;
                }
                String str2 = c0580g.mName;
                next.m453wc();
                this.f727vd.remove(next);
                if (this.f729xd >= this.f727vd.size()) {
                    this.f729xd = this.f727vd.size() - 1;
                    if (this.f729xd < 0) {
                        this.f729xd = 0;
                    }
                }
                if (str.equals(str2)) {
                    if (this.f727vd.size() > 0) {
                        this.f726ud = this.f727vd.get(this.f729xd);
                        return;
                    } else {
                        this.f726ud = this.f723qd;
                        return;
                    }
                }
                return;
            }
        }
    }

    /* renamed from: sa */
    public void m787sa(String str) {
        Iterator<C0580g> it = this.f728wd.iterator();
        while (it.hasNext()) {
            C0580g next = it.next();
            if (str.equals(next.mName)) {
                C0580g c0580g = this.f726ud;
                if (c0580g.f543ik == 1) {
                    c0580g = c0580g.f548nk;
                }
                String str2 = c0580g.mName;
                next.m453wc();
                this.f728wd.remove(next);
                if (this.f730yd >= this.f728wd.size()) {
                    this.f730yd = this.f728wd.size() - 1;
                    if (this.f730yd < 0) {
                        this.f730yd = 0;
                    }
                }
                if (str.equals(str2)) {
                    if (this.f728wd.size() > 0) {
                        this.f726ud = this.f728wd.get(this.f730yd);
                        return;
                    } else {
                        this.f726ud = this.f724rd;
                        return;
                    }
                }
                return;
            }
        }
    }

    /* renamed from: ta */
    public void m788ta(String str) {
        if (str == null) {
            return;
        }
        if (str.contains("/mnt/sdcard/iNand")) {
            this.f726ud = this.f725td;
            this.f726ud.mIndex = 3;
            return;
        }
        if (str.contains("/storage/usb") || str.contains("/mnt/usbhost")) {
            if (this.f728wd.size() <= 0) {
                this.f726ud = this.f724rd;
                return;
            }
            if (this.f730yd >= this.f728wd.size()) {
                this.f730yd = 0;
            }
            this.f726ud = this.f728wd.get(this.f730yd);
            return;
        }
        if (!str.contains("/storage/extsd") && !str.contains("/mnt/extsd")) {
            this.f726ud = f704Dd;
            this.f726ud.mIndex = 0;
        } else {
            if (this.f727vd.size() <= 0) {
                this.f726ud = this.f723qd;
                return;
            }
            if (this.f729xd >= this.f727vd.size()) {
                this.f729xd = 0;
            }
            this.f726ud = this.f727vd.get(this.f729xd);
        }
    }

    /* renamed from: w */
    public void m789w(boolean z) {
        m782da(z ? 3 : Cea708CCParser.Const.CODE_C1_CW3);
    }

    /* renamed from: z */
    public void m790z(boolean z) {
        this.f723qd = new C0580g("SD", 1, 0);
        if (f707Gd) {
            File[] listFiles = new File("/mnt").listFiles(new C0649n(this));
            if (listFiles != null) {
                for (File file : listFiles) {
                    m784pa(file.getAbsolutePath());
                }
            }
            this.f724rd = new C0580g("USB", 2, 0);
            File[] listFiles2 = new File("/mnt/usbhost").listFiles(new C0650o(this));
            if (listFiles2 != null) {
                for (File file2 : listFiles2) {
                    m785qa(file2.getAbsolutePath());
                }
            }
        } else {
            File[] listFiles3 = new File("/storage").listFiles(new C0651p(this));
            if (listFiles3 != null) {
                for (File file3 : listFiles3) {
                    m784pa(file3.getAbsolutePath());
                }
            }
            this.f724rd = new C0580g("USB", 2, 0);
            File[] listFiles4 = new File("/storage").listFiles(new C0652q(this));
            if (listFiles4 != null) {
                for (File file4 : listFiles4) {
                    m785qa(file4.getAbsolutePath());
                }
            }
        }
        this.f725td = new C0580g("iNand", 3, 0);
        m780b(this.f725td, z ? "/mnt/sdcard" : "/mnt/sdcard/iNand");
        this.f726ud = f704Dd;
        if (this.f726ud.f545kk == 0) {
            if (this.f727vd.size() > 0) {
                this.f726ud = this.f727vd.get(0);
            } else {
                this.f726ud = this.f723qd;
            }
            if (this.f726ud.f545kk == 0) {
                if (this.f728wd.size() > 0) {
                    this.f726ud = this.f728wd.get(0);
                } else {
                    this.f726ud = this.f724rd;
                }
                if (this.f726ud.f545kk == 0) {
                    this.f726ud = this.f725td;
                    if (this.f726ud.f545kk == 0) {
                        this.f726ud = f704Dd;
                    }
                }
            }
        }
    }

    /* renamed from: b */
    public void m780b(C0580g c0580g, String str) {
        String str2;
        BufferedReader bufferedReader;
        if (c0580g == null || str == null) {
            return;
        }
        BufferedReader bufferedReader2 = null;
        try {
            try {
                try {
                    if (!f707Gd) {
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
                    bufferedReader = new BufferedReader(new FileReader(str2 + "/.music"));
                } catch (Exception e) {
                    Log.i(TAG, "" + e.toString());
                    return;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            ArrayList arrayList = new ArrayList();
            while (true) {
                String readLine = bufferedReader.readLine();
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
                        arrayList.add(new C0579f(substring.substring(substring.lastIndexOf("/") + 1), absolutePath));
                    } else {
                        arrayList.add(new C0579f(name, absolutePath));
                    }
                }
            }
            c0580g.setLength(arrayList.size());
            C0643h.m755a(c0580g, arrayList, isForward);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                c0580g.m449a((C0579f) it.next());
            }
            arrayList.clear();
            bufferedReader.close();
        } catch (Exception e3) {
            e = e3;
            bufferedReader2 = bufferedReader;
            Log.i(TAG, "" + e.toString());
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
        } catch (Throwable th2) {
            th = th2;
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw th;
        }
    }

    /* renamed from: a */
    public void m778a(Context context, C0580g c0580g, String str) {
        if (c0580g == null || str == null) {
            return;
        }
        File[] listFiles = new File(str).listFiles(new C0648m(this));
        c0580g.f545kk = 0;
        ArrayList arrayList = new ArrayList();
        if (listFiles != null) {
            c0580g.setLength(listFiles.length);
            for (File file : listFiles) {
                String name = file.getName();
                arrayList.add(new C0579f(name.substring(0, name.lastIndexOf(".")), file.getAbsolutePath(), C0636a.m743a(context, str + "/" + name, f712Tc)));
            }
        }
        C0643h.m755a(c0580g, arrayList, isForward);
    }
}
