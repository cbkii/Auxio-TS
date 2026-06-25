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

/* compiled from: DeviceUtils.java */
/* renamed from: com.eckom.xtlibrary.b.j.b */
/* loaded from: classes3.dex */
public class C0686b {
    /* renamed from: Qc */
    public static String m996Qc() {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        String str = null;
        bufferedReader2 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader("/sys/class/block/mmcblk0/device/cid"));
                try {
                    String readLine = bufferedReader.readLine();
                    if (!m1000Uc() && !m1001Vc()) {
                        bufferedReader2 = new BufferedReader(new FileReader("/sys/firmware/devicetree/base/serial-number"));
                        String readLine2 = bufferedReader2.readLine();
                        String substring = readLine2.length() > 64 ? readLine2.substring(0, 64) : readLine2.substring(0, readLine2.length() - 1);
                        if (TextUtils.equals(readLine, substring.substring(0, 32))) {
                            bufferedReader2.close();
                            return readLine;
                        }
                        bufferedReader2.close();
                        return substring;
                    }
                    BufferedReader bufferedReader3 = new BufferedReader(new FileReader("/sys/class/sunxi_info/sys_info"));
                    while (true) {
                        try {
                            String readLine3 = bufferedReader3.readLine();
                            if (readLine3 == null) {
                                break;
                            }
                            if (readLine3.contains("chipid")) {
                                str = readLine3.substring(readLine3.lastIndexOf(": ") + 2);
                            }
                        } catch (Exception unused) {
                            bufferedReader = bufferedReader3;
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader2 = bufferedReader3;
                        }
                    }
                    String readLine4 = new BufferedReader(new FileReader("/sys/class/block/mmcblk0/device/3305")).readLine();
                    BufferedReader bufferedReader4 = new BufferedReader(new FileReader("/sys/class/block/mmcblk0/device/0394"));
                    try {
                        String readLine5 = bufferedReader4.readLine();
                        if (!TextUtils.isEmpty(str)) {
                            bufferedReader4.close();
                            return str;
                        }
                        if (!TextUtils.isEmpty(readLine4)) {
                            bufferedReader4.close();
                            return readLine4;
                        }
                        if (TextUtils.isEmpty(readLine)) {
                            bufferedReader4.close();
                            return readLine5;
                        }
                        bufferedReader4.close();
                        return readLine;
                    } catch (Exception unused2) {
                        bufferedReader = bufferedReader4;
                        if (bufferedReader == null) {
                            return "00000000000000000000000000000000";
                        }
                        bufferedReader.close();
                        return "00000000000000000000000000000000";
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader2 = bufferedReader4;
                        if (bufferedReader2 != null) {
                            bufferedReader2.close();
                        }
                        throw th;
                    }
                } catch (Exception unused3) {
                } catch (Throwable th3) {
                    th = th3;
                    bufferedReader2 = bufferedReader;
                }
            } catch (Exception unused4) {
                return "00000000000000000000000000000000";
            }
        } catch (Exception unused5) {
            bufferedReader = bufferedReader2;
        } catch (Throwable th4) {
            th = th4;
        }
    }

    /* renamed from: Rc */
    public static String m997Rc() {
        BufferedReader bufferedReader;
        Throwable th;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader("/sys/class/sensor_i2c/mic"));
                try {
                    String readLine = bufferedReader.readLine();
                    Log.d("mxy", "getDeviceMic value:" + readLine);
                    bufferedReader.close();
                    return readLine;
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

    /* renamed from: Sc */
    public static String m998Sc() {
        return (m1005Zc() || m1008ad() || m1002Wc() || m1009bd() || m1003Xc() || m1010cd() || m1011dd() || m1012ff()) ? "/system/etc/" : "/system_tw/etc/";
    }

    /* renamed from: Tc */
    public static String m999Tc() {
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
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                if (readLine.length() > 0) {
                    str = readLine;
                }
            }
            bufferedReader.close();
        } catch (Exception unused3) {
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            if (!TextUtils.isEmpty(str)) {
            }
            return str;
        } catch (Throwable th2) {
            th = th2;
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw th;
        }
        if (!TextUtils.isEmpty(str)) {
        }
        return str;
    }

    /* renamed from: Uc */
    public static boolean m1000Uc() {
        String str = Build.MODEL;
        return m999Tc().startsWith("V13") || "T100".equals(str) || "A100".equals(str) || "A133".equals(str) || "A133 pro".equals(str);
    }

    /* renamed from: Vc */
    public static boolean m1001Vc() {
        String str = Build.MODEL;
        return m999Tc().startsWith("V15");
    }

    /* renamed from: Wc */
    public static boolean m1002Wc() {
        return m999Tc().startsWith("T5Q.") || "QUAD-CORE T507".equals(Build.MODEL);
    }

    /* renamed from: Xc */
    public static boolean m1003Xc() {
        return "QUAD-CORE t7 p1".equals(Build.MODEL);
    }

    /* renamed from: Yc */
    public static boolean m1004Yc() {
        String str = Build.MODEL;
        return m999Tc().startsWith("V16.");
    }

    /* renamed from: Zc */
    public static boolean m1005Zc() {
        return "ums512_1h10_Natv".equals(Build.MODEL);
    }

    /* renamed from: _c */
    public static boolean m1006_c() {
        String str = Build.MODEL;
        return m999Tc().startsWith("TS10S");
    }

    /* renamed from: a */
    public static Drawable m1007a(int i, int i2, C0721b c0721b, int i3) {
        String m1013ib = m1013ib(Integer.toHexString(i));
        BitmapDrawable bitmapDrawable = null;
        if (c0721b == null) {
            return null;
        }
        String[] m1145h = c0721b.m1145h(m1013ib, i2);
        String m998Sc = m998Sc();
        if (TextUtils.equals("0000", m1145h[1])) {
            return null;
        }
        String str = i3 != 3 ? "logo" : "logo_dny";
        String str2 = "/sdcard/iNand/radio/" + str + "/icon_" + m1145h[1].toLowerCase() + ".png";
        if (!new File(str2).exists()) {
            str2 = m998Sc + "radio_" + str + "/icon_" + m1145h[1].toLowerCase() + ".png";
            if (!new File(str2).exists()) {
                str2 = m998Sc + "radio_" + str + "/icon_" + m1145h[1] + ".png";
                if (!new File(str2).exists()) {
                    str2 = m998Sc + "radio/radio_" + str + "/icon_" + m1145h[1].toLowerCase() + ".png";
                    if (!new File(str2).exists()) {
                        str2 = m998Sc + "radio/radio_" + str + "/icon_" + m1145h[1] + ".png";
                    }
                }
            }
        }
        if (new File(str2).exists()) {
            bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeFile(str2));
        } else {
            str2 = "/system_tw/etc/radio/" + str + "/icon_" + m1145h[1].toLowerCase() + ".png";
            if (!new File(str2).exists()) {
                str2 = m998Sc + "radio_" + str + "/icon_" + m1145h[1].toLowerCase() + ".png";
                if (!new File(str2).exists()) {
                    str2 = m998Sc + "radio_" + str + "/icon_" + m1145h[1] + ".png";
                    if (!new File(str2).exists()) {
                        str2 = m998Sc + "radio/radio_" + str + "/icon_" + m1145h[1].toLowerCase() + ".png";
                        if (!new File(str2).exists()) {
                            str2 = m998Sc + "radio/radio_" + str + "/icon_" + m1145h[1] + ".png";
                        }
                    }
                }
            }
        }
        return new File(str2).exists() ? new BitmapDrawable(BitmapFactory.decodeFile(str2)) : bitmapDrawable;
    }

    /* renamed from: ad */
    public static boolean m1008ad() {
        String str = Build.MODEL;
        return !TextUtils.isEmpty(str) && str.contains("s9863a1h10");
    }

    /* renamed from: bd */
    public static boolean m1009bd() {
        return "sp7731e_1h10_native".equals(Build.MODEL) || m999Tc().startsWith("V12.");
    }

    /* renamed from: cd */
    public static boolean m1010cd() {
        return "SP9832A".equals(Build.MODEL);
    }

    /* renamed from: dd */
    public static boolean m1011dd() {
        return "sp9853i_1h10_vmm".equals(Build.MODEL);
    }

    /* renamed from: ff */
    private static boolean m1012ff() {
        return m999Tc().startsWith("V8.1.1");
    }

    /* renamed from: ib */
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
