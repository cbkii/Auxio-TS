package com.eckom.xtlibrary.p020b.p052i;

import android.text.format.Time;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ThemeUtil.java */
/* renamed from: com.eckom.xtlibrary.b.i.n */
/* loaded from: classes3.dex */
public class C0684n {
    /* renamed from: a */
    public static void m992a(C0677g c0677g, String str) {
        try {
            JSONObject jSONObject = new JSONObject(m994hb(str));
            c0677g.m939G(jSONObject.optBoolean("day_night_mode", false));
            c0677g.m944Wa(jSONObject.optString("common_launcher_theme_dir", ""));
            c0677g.m945Xa(jSONObject.optString("common_sub_theme_dir", ""));
            c0677g.m946Ya(jSONObject.optString("day_launcher_theme_dir", ""));
            c0677g.m947Za(jSONObject.optString("day_sub_theme_dir", ""));
            c0677g.m950bb(jSONObject.optString("night_launcher_theme_dir", ""));
            c0677g.m953eb(jSONObject.optString("night_sub_theme_dir", ""));
            c0677g.m952db(jSONObject.optString("night_mode_start_time", ""));
            c0677g.m951cb(jSONObject.optString("night_mode_end_time", ""));
            m991a(c0677g);
            Log.d("Utilities", "parsingThemeConfig: Radio End ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: c */
    public static boolean m993c(int i, int i2, int i3, int i4) {
        Time time = new Time();
        time.set(System.currentTimeMillis());
        int intValue = Integer.valueOf((String.format(Locale.US, "%02d", Integer.valueOf(time.hour)) + ":" + String.format(Locale.US, "%02d", Integer.valueOf(time.minute))).replaceAll(":", "")).intValue();
        int intValue2 = Integer.valueOf((String.format(Locale.US, "%02d", Integer.valueOf(i)) + ":" + String.format(Locale.US, "%02d", Integer.valueOf(i2))).replaceAll(":", "")).intValue();
        int intValue3 = Integer.valueOf((String.format(Locale.US, "%02d", Integer.valueOf(i3)) + ":" + String.format(Locale.US, "%02d", Integer.valueOf(i4))).replaceAll(":", "")).intValue();
        if (intValue == intValue2) {
            return true;
        }
        if (intValue == intValue3) {
            return false;
        }
        return intValue2 > intValue3 ? intValue < intValue3 || intValue > intValue2 : intValue > intValue2 && intValue < intValue3;
    }

    /* renamed from: hb */
    public static String m994hb(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            while (true) {
                int read = fileInputStream.read();
                if (read == -1) {
                    break;
                }
                sb.append((char) read);
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x012c  */
    /* renamed from: a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void m991a(C0677g c0677g) {
        int i;
        int i2;
        int i3;
        if (c0677g.m943Jc()) {
            int i4 = 0;
            try {
                i = Integer.valueOf(c0677g.m940Gc().split(":")[0]).intValue();
            } catch (Exception e) {
                e = e;
                i = 0;
                i2 = 0;
            }
            try {
                i2 = Integer.valueOf(c0677g.m940Gc().split(":")[1]).intValue();
                try {
                    i3 = Integer.valueOf(c0677g.m938Fc().split(":")[0]).intValue();
                } catch (Exception e2) {
                    e = e2;
                    i3 = 0;
                }
            } catch (Exception e3) {
                e = e3;
                i2 = 0;
                i3 = i2;
                Log.e("Utilities", "setThemePath: " + e.getMessage());
                if (m993c(i, i2, i3, i4)) {
                }
            }
            try {
                i4 = Integer.valueOf(c0677g.m938Fc().split(":")[1]).intValue();
                c0677g.m958sa(i);
                c0677g.m959ta(i2);
                c0677g.m956qa(i3);
                c0677g.m957ra(i4);
            } catch (Exception e4) {
                e = e4;
                Log.e("Utilities", "setThemePath: " + e.getMessage());
                if (m993c(i, i2, i3, i4)) {
                }
            }
            if (m993c(i, i2, i3, i4)) {
                if (new File(c0677g.m937Ec() + c0677g.f793Cl).exists()) {
                    c0677g.m949ab(c0677g.m937Ec() + c0677g.f793Cl);
                } else {
                    c0677g.m949ab("/data/tw/theme/default/Launcher/" + c0677g.f793Cl);
                }
                if (new File(c0677g.m941Hc() + c0677g.f794Dl).exists()) {
                    c0677g.m955gb(c0677g.m941Hc() + c0677g.f794Dl);
                    return;
                }
                c0677g.m955gb("/data/tw/theme/default/Sub/" + c0677g.f794Dl);
                return;
            }
            if (new File(c0677g.m934Bc() + c0677g.f793Cl).exists()) {
                c0677g.m949ab(c0677g.m934Bc() + c0677g.f793Cl);
            } else {
                c0677g.m949ab("/data/tw/theme/default/Launcher/" + c0677g.f793Cl);
            }
            if (new File(c0677g.m935Cc() + c0677g.f794Dl).exists()) {
                c0677g.m955gb(c0677g.m935Cc() + c0677g.f794Dl);
                return;
            }
            c0677g.m955gb("/data/tw/theme/default/Sub/" + c0677g.f794Dl);
            return;
        }
        if (new File(c0677g.m960zc() + c0677g.f793Cl).exists()) {
            c0677g.m949ab(c0677g.m960zc() + c0677g.f793Cl);
        } else {
            c0677g.m949ab("/data/tw/theme/default/Launcher/" + c0677g.f793Cl);
        }
        if (new File(c0677g.m933Ac() + c0677g.f794Dl).exists()) {
            c0677g.m955gb(c0677g.m933Ac() + c0677g.f794Dl);
            return;
        }
        c0677g.m955gb("/data/tw/theme/default/Sub/" + c0677g.f794Dl);
    }
}
