package com.eckom.xtlibrary.p020b.p053j;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.twproject.radio.utils.C0721b;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.b */
/* JADX INFO: compiled from: DeviceUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0686b {
    /* JADX INFO: renamed from: Qc */
    public static String m996Qc() throws Throwable {
        BufferedReader bufferedReader;
        String line;
        BufferedReader bufferedReader2;
        BufferedReader bufferedReader3 = null;
        String strSubstring = null;
        bufferedReader3 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader("/sys/class/block/mmcblk0/device/cid"));
                try {
                    line = bufferedReader.readLine();
                    if (!m1000Uc() && !m1001Vc()) {
                        bufferedReader3 = new BufferedReader(new FileReader("/sys/firmware/devicetree/base/serial-number"));
                        String line2 = bufferedReader3.readLine();
                        String strSubstring2 = line2.length() > 64 ? line2.substring(0, 64) : line2.substring(0, line2.length() - 1);
                        if (TextUtils.equals(line, strSubstring2.substring(0, 32))) {
                            bufferedReader3.close();
                            return line;
                        }
                        bufferedReader3.close();
                        return strSubstring2;
                    }
                    bufferedReader2 = new BufferedReader(new FileReader("/sys/class/sunxi_info/sys_info"));
                } catch (Exception unused) {
                } catch (Throwable th) {
                    th = th;
                    bufferedReader3 = bufferedReader;
                }
            } catch (Exception unused2) {
                return "00000000000000000000000000000000";
            }
        } catch (Exception unused3) {
            bufferedReader = bufferedReader3;
        } catch (Throwable th2) {
            th = th2;
        }
        while (true) {
            try {
                String line3 = bufferedReader2.readLine();
                if (line3 == null) {
                    break;
                }
                if (line3.contains("chipid")) {
                    strSubstring = line3.substring(line3.lastIndexOf(": ") + 2);
                }
            } catch (Exception unused4) {
                bufferedReader = bufferedReader2;
            } catch (Throwable th3) {
                th = th3;
                bufferedReader3 = bufferedReader2;
            }
            if (bufferedReader == null) {
                return "00000000000000000000000000000000";
            }
            bufferedReader.close();
            return "00000000000000000000000000000000";
        }
        String line4 = new BufferedReader(new FileReader("/sys/class/block/mmcblk0/device/3305")).readLine();
        BufferedReader bufferedReader4 = new BufferedReader(new FileReader("/sys/class/block/mmcblk0/device/0394"));
        try {
            String line5 = bufferedReader4.readLine();
            if (!TextUtils.isEmpty(strSubstring)) {
                bufferedReader4.close();
                return strSubstring;
            }
            if (!TextUtils.isEmpty(line4)) {
                bufferedReader4.close();
                return line4;
            }
            if (TextUtils.isEmpty(line)) {
                bufferedReader4.close();
                return line5;
            }
            bufferedReader4.close();
            return line;
        } catch (Exception unused5) {
            bufferedReader = bufferedReader4;
        } catch (Throwable th4) {
            th = th4;
            bufferedReader3 = bufferedReader4;
            if (bufferedReader3 != null) {
                bufferedReader3.close();
            }
            throw th;
        }
    }

    /* JADX INFO: renamed from: Rc */
    public static String m997Rc() throws Throwable {
        BufferedReader bufferedReader;
        Throwable th;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader("/sys/class/sensor_i2c/mic"));
                try {
                    String line = bufferedReader.readLine();
                    Log.d("mxy", "getDeviceMic value:" + line);
                    bufferedReader.close();
                    return line;
                } catch (Exception unused) {
                    if (bufferedReader == null) {
                        return "0";
                    }
                    bufferedReader.close();
                    return "0";
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader == null) {
                        throw th;
                    }
                    bufferedReader.close();
                    throw th;
                }
            } catch (Exception unused2) {
                return "0";
            }
        } catch (Exception unused3) {
            bufferedReader = null;
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
        }
    }

    /* JADX INFO: renamed from: Sc */
    public static String m998Sc() {
        return (m1005Zc() || m1008ad() || m1002Wc() || m1009bd() || m1003Xc() || m1010cd() || m1011dd() || m1012ff()) ? "/system/etc/" : "/system_tw/etc/";
    }

    /* JADX INFO: renamed from: Tc */
    public static String m999Tc() throws Throwable {
        BufferedReader bufferedReader;
        String str = "";
        try {
            str = SystemProperties.get("ro.systemtw.version");
        } catch (Exception unused) {
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String str2 = SystemProperties.get("ro.tw.version");
        if (str2 != null && str2.length() > 0) {
            str = str2;
        }
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("/system/etc/version"));
        } catch (Exception unused2) {
        } catch (Throwable th) {
            th = th;
        }
        try {
            String line = bufferedReader.readLine();
            if (line != null) {
                if (line.length() > 0) {
                    str = line;
                }
            }
            bufferedReader.close();
        } catch (Exception unused3) {
            bufferedReader2 = bufferedReader;
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
        if (TextUtils.isEmpty(str)) {
        }
        return str;
    }

    /* JADX INFO: renamed from: Uc */
    public static boolean m1000Uc() {
        String str = Build.MODEL;
        return m999Tc().startsWith("V13") || "T100".equals(str) || "A100".equals(str) || "A133".equals(str) || "A133 pro".equals(str);
    }

    /* JADX INFO: renamed from: Vc */
    public static boolean m1001Vc() {
        String str = Build.MODEL;
        return m999Tc().startsWith("V15");
    }

    /* JADX INFO: renamed from: Wc */
    public static boolean m1002Wc() {
        return m999Tc().startsWith("T5Q.") || "QUAD-CORE T507".equals(Build.MODEL);
    }

    /* JADX INFO: renamed from: Xc */
    public static boolean m1003Xc() {
        return "QUAD-CORE t7 p1".equals(Build.MODEL);
    }

    /* JADX INFO: renamed from: Yc */
    public static boolean m1004Yc() {
        String str = Build.MODEL;
        return m999Tc().startsWith("V16.");
    }

    /* JADX INFO: renamed from: Zc */
    public static boolean m1005Zc() {
        return "ums512_1h10_Natv".equals(Build.MODEL);
    }

    /* JADX INFO: renamed from: _c */
    public static boolean m1006_c() {
        String str = Build.MODEL;
        return m999Tc().startsWith("TS10S");
    }

    /* JADX INFO: renamed from: a */
    public static Drawable m1007a(int i, int i2, C0721b c0721b, int i3) {
        String strM1013ib = m1013ib(Integer.toHexString(i));
        BitmapDrawable bitmapDrawable = null;
        if (c0721b == null) {
            return null;
        }
        String[] strArrM1145h = c0721b.m1145h(strM1013ib, i2);
        String strM998Sc = m998Sc();
        if (TextUtils.equals("0000", strArrM1145h[1])) {
            return null;
        }
        String str = i3 != 3 ? "logo" : "logo_dny";
        String str2 = "/sdcard/iNand/radio/" + str + "/icon_" + strArrM1145h[1].toLowerCase() + ".png";
        if (!new File(str2).exists()) {
            str2 = strM998Sc + "radio_" + str + "/icon_" + strArrM1145h[1].toLowerCase() + ".png";
            if (!new File(str2).exists()) {
                str2 = strM998Sc + "radio_" + str + "/icon_" + strArrM1145h[1] + ".png";
                if (!new File(str2).exists()) {
                    str2 = strM998Sc + "radio/radio_" + str + "/icon_" + strArrM1145h[1].toLowerCase() + ".png";
                    if (!new File(str2).exists()) {
                        str2 = strM998Sc + "radio/radio_" + str + "/icon_" + strArrM1145h[1] + ".png";
                    }
                }
            }
        }
        if (new File(str2).exists()) {
            bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeFile(str2));
        } else {
            str2 = "/system_tw/etc/radio/" + str + "/icon_" + strArrM1145h[1].toLowerCase() + ".png";
            if (!new File(str2).exists()) {
                str2 = strM998Sc + "radio_" + str + "/icon_" + strArrM1145h[1].toLowerCase() + ".png";
                if (!new File(str2).exists()) {
                    str2 = strM998Sc + "radio_" + str + "/icon_" + strArrM1145h[1] + ".png";
                    if (!new File(str2).exists()) {
                        str2 = strM998Sc + "radio/radio_" + str + "/icon_" + strArrM1145h[1].toLowerCase() + ".png";
                        if (!new File(str2).exists()) {
                            str2 = strM998Sc + "radio/radio_" + str + "/icon_" + strArrM1145h[1] + ".png";
                        }
                    }
                }
            }
        }
        return new File(str2).exists() ? new BitmapDrawable(BitmapFactory.decodeFile(str2)) : bitmapDrawable;
    }

    /* JADX INFO: renamed from: ad */
    public static boolean m1008ad() {
        String str = Build.MODEL;
        return !TextUtils.isEmpty(str) && str.contains("s9863a1h10");
    }

    /* JADX INFO: renamed from: bd */
    public static boolean m1009bd() {
        return "sp7731e_1h10_native".equals(Build.MODEL) || m999Tc().startsWith("V12.");
    }

    /* JADX INFO: renamed from: cd */
    public static boolean m1010cd() {
        return "SP9832A".equals(Build.MODEL);
    }

    /* JADX INFO: renamed from: dd */
    public static boolean m1011dd() {
        return "sp9853i_1h10_vmm".equals(Build.MODEL);
    }

    /* JADX INFO: renamed from: ff */
    private static boolean m1012ff() {
        return m999Tc().startsWith("V8.1.1");
    }

    /* JADX INFO: renamed from: ib */
    public static String m1013ib(String str) {
        int length = str.length();
        if (length < 4) {
            while (length < 4) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("0");
                stringBuffer.append(str);
                str = stringBuffer.toString();
                length = str.length();
            }
        }
        return str;
    }
}
