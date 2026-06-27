package com.eckom.xtlibrary.p020b.p052i;

import android.text.format.Time;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.n */
/* JADX INFO: compiled from: ThemeUtil.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0684n {
    /* JADX INFO: renamed from: a */
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

    /* JADX INFO: renamed from: c */
    public static boolean m993c(int i, int i2, int i3, int i4) {
        Time time = new Time();
        time.set(System.currentTimeMillis());
        int iIntValue = Integer.valueOf((String.format(Locale.US, "%02d", Integer.valueOf(time.hour)) + ":" + String.format(Locale.US, "%02d", Integer.valueOf(time.minute))).replaceAll(":", "")).intValue();
        int iIntValue2 = Integer.valueOf((String.format(Locale.US, "%02d", Integer.valueOf(i)) + ":" + String.format(Locale.US, "%02d", Integer.valueOf(i2))).replaceAll(":", "")).intValue();
        int iIntValue3 = Integer.valueOf((String.format(Locale.US, "%02d", Integer.valueOf(i3)) + ":" + String.format(Locale.US, "%02d", Integer.valueOf(i4))).replaceAll(":", "")).intValue();
        if (iIntValue == iIntValue2) {
            return true;
        }
        if (iIntValue == iIntValue3) {
            return false;
        }
        return iIntValue2 > iIntValue3 ? iIntValue < iIntValue3 || iIntValue > iIntValue2 : iIntValue > iIntValue2 && iIntValue < iIntValue3;
    }

    /* JADX INFO: renamed from: hb */
    public static String m994hb(String str) {
        FileInputStream fileInputStream;
        StringBuilder sb = new StringBuilder();
        try {
            fileInputStream = new FileInputStream(new File(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            int i = fileInputStream.read();
            if (i == -1) {
                break;
            }
            sb.append((char) i);
            return sb.toString();
        }
        fileInputStream.close();
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x012c  */
    /* JADX INFO: renamed from: a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void m991a(C0677g c0677g) {
        int iIntValue;
        int iIntValue2;
        int iIntValue3;
        if (c0677g.m943Jc()) {
            int iIntValue4 = 0;
            try {
                iIntValue = Integer.valueOf(c0677g.m940Gc().split(":")[0]).intValue();
            } catch (Exception e) {
                e = e;
                iIntValue = 0;
                iIntValue2 = 0;
            }
            try {
                iIntValue2 = Integer.valueOf(c0677g.m940Gc().split(":")[1]).intValue();
                try {
                    iIntValue3 = Integer.valueOf(c0677g.m938Fc().split(":")[0]).intValue();
                } catch (Exception e2) {
                    e = e2;
                    iIntValue3 = 0;
                }
            } catch (Exception e3) {
                e = e3;
                iIntValue2 = 0;
                iIntValue3 = iIntValue2;
                Log.e("Utilities", "setThemePath: " + e.getMessage());
                if (!m993c(iIntValue, iIntValue2, iIntValue3, iIntValue4)) {
                }
            }
            try {
                iIntValue4 = Integer.valueOf(c0677g.m938Fc().split(":")[1]).intValue();
                c0677g.m958sa(iIntValue);
                c0677g.m959ta(iIntValue2);
                c0677g.m956qa(iIntValue3);
                c0677g.m957ra(iIntValue4);
            } catch (Exception e4) {
                e = e4;
                Log.e("Utilities", "setThemePath: " + e.getMessage());
            }
            if (!m993c(iIntValue, iIntValue2, iIntValue3, iIntValue4)) {
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
