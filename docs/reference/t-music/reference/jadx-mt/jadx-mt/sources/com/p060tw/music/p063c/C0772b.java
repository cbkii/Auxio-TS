package com.p060tw.music.p063c;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p052i.C0678h;
import com.eckom.xtlibrary.p020b.p052i.C0681k;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: renamed from: com.tw.music.c.b */
/* JADX INFO: compiled from: MainPlugin.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0772b {

    /* JADX INFO: renamed from: Tm */
    private Drawable f1116Tm;

    /* JADX INFO: renamed from: Um */
    private Drawable f1117Um;

    /* JADX INFO: renamed from: Vm */
    private Drawable f1118Vm;

    /* JADX INFO: renamed from: Wm */
    private Drawable f1119Wm;

    /* JADX INFO: renamed from: Xm */
    private Drawable f1120Xm;

    /* JADX INFO: renamed from: Ym */
    private Drawable f1121Ym;

    /* JADX INFO: renamed from: Zm */
    private Drawable f1122Zm;

    /* JADX INFO: renamed from: _m */
    private Drawable f1123_m;
    private Drawable album;
    private Drawable album_bg;
    private Drawable collect;

    /* JADX INFO: renamed from: dn */
    private Drawable f1124dn;

    /* JADX INFO: renamed from: fn */
    private Drawable f1125fn;

    /* JADX INFO: renamed from: gn */
    private Drawable f1126gn;

    /* JADX INFO: renamed from: hn */
    private Drawable f1127hn;

    /* JADX INFO: renamed from: jn */
    private Drawable f1128jn;

    /* JADX INFO: renamed from: kn */
    private Drawable f1129kn;

    /* JADX INFO: renamed from: ln */
    private Drawable f1130ln;

    /* JADX INFO: renamed from: mn */
    private Drawable f1131mn;

    /* JADX INFO: renamed from: nn */
    private Drawable f1132nn;

    /* JADX INFO: renamed from: pn */
    private int f1133pn = 0;

    /* JADX INFO: renamed from: qn */
    private int f1134qn = 0;
    private Drawable repeat;

    /* JADX INFO: renamed from: rn */
    private Drawable f1135rn;
    private Drawable tab_btn_layout;

    /* JADX INFO: renamed from: tn */
    private Drawable f1136tn;

    /* JADX INFO: renamed from: un */
    private Drawable f1137un;

    /* JADX INFO: renamed from: vn */
    private Drawable f1138vn;

    /* JADX INFO: renamed from: wn */
    private Drawable f1139wn;

    /* JADX INFO: renamed from: xn */
    private Drawable f1140xn;

    /* JADX INFO: renamed from: yn */
    private Drawable f1141yn;

    /* JADX INFO: renamed from: zn */
    private Drawable f1142zn;

    /* JADX INFO: renamed from: sb */
    public static C0772b m1405sb(String str) {
        Log.d("MainPlugin", "parsingViewsConfig:111 " + str);
        C0772b c0772b = new C0772b();
        try {
            Context contextM973Kc = C0681k.get().m973Kc();
            JSONObject jSONObject = new JSONObject(str);
            c0772b.m1445o(C0678h.m964d(contextM973Kc, jSONObject.optString("background")));
            c0772b.m1450t(C0678h.m964d(contextM973Kc, jSONObject.optString("music_play_bg")));
            c0772b.m1418G(C0678h.m964d(contextM973Kc, jSONObject.optString("setting_layout_bg")));
            String strOptString = jSONObject.optString("toggle_bg");
            Log.d("MainPlugin", "parsingViewsConfig: toggle_bg:" + strOptString);
            c0772b.m1422I(C0678h.m964d(contextM973Kc, strOptString));
            c0772b.m1462za(C0678h.m962c(contextM973Kc, jSONObject.optString("music_freq_cylinder_start_color")));
            c0772b.m1459ya(C0678h.m962c(contextM973Kc, jSONObject.optString("music_freq_cylinder_end_color")));
            JSONArray jSONArray = new JSONArray(jSONObject.optString("itemInfo"));
            int i = 0;
            while (i < jSONArray.length()) {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                String strOptString2 = jSONObjectOptJSONObject.optString("idName");
                if (strOptString2.equals("tab_btn_layout")) {
                    c0772b.m1420H(C0678h.m964d(contextM973Kc, jSONObjectOptJSONObject.optString("background")));
                    JSONArray jSONArray2 = new JSONArray(jSONObjectOptJSONObject.optString("itemInfo"));
                    int i2 = 0;
                    while (i2 < jSONArray2.length()) {
                        JSONObject jSONObjectOptJSONObject2 = jSONArray2.optJSONObject(i2);
                        String strOptString3 = jSONObjectOptJSONObject2.optString("idName");
                        String strOptString4 = jSONObjectOptJSONObject2.optString("btn_img");
                        JSONArray jSONArray3 = jSONArray;
                        String strOptString5 = jSONObjectOptJSONObject2.optString("btn_background");
                        if (strOptString3.equals("play_list")) {
                            c0772b.m1451u(C0678h.m964d(contextM973Kc, strOptString4));
                            c0772b.m1452v(C0678h.m964d(contextM973Kc, strOptString5));
                        }
                        if (strOptString3.equals("prev")) {
                            c0772b.m1458y(C0678h.m964d(contextM973Kc, strOptString4));
                            c0772b.m1461z(C0678h.m964d(contextM973Kc, strOptString5));
                        }
                        if (strOptString3.equals("pp")) {
                            c0772b.m1454w(C0678h.m964d(contextM973Kc, strOptString4));
                            c0772b.m1456x(C0678h.m964d(contextM973Kc, strOptString5));
                        }
                        if (strOptString3.equals("next")) {
                            c0772b.m1448r(C0678h.m964d(contextM973Kc, strOptString4));
                            c0772b.m1449s(C0678h.m964d(contextM973Kc, strOptString5));
                        }
                        if (strOptString3.equals("eq")) {
                            c0772b.m1446p(C0678h.m964d(contextM973Kc, strOptString4));
                            c0772b.m1447q(C0678h.m964d(contextM973Kc, strOptString5));
                        }
                        if (strOptString3.equals("iv_setting")) {
                            c0772b.m1406A(C0678h.m964d(contextM973Kc, strOptString4));
                            c0772b.m1408B(C0678h.m964d(contextM973Kc, strOptString5));
                        }
                        i2++;
                        jSONArray = jSONArray3;
                    }
                }
                JSONArray jSONArray4 = jSONArray;
                if (strOptString2.equals("album_layout")) {
                    String strOptString6 = jSONObjectOptJSONObject.optString("album_bg");
                    String strOptString7 = jSONObjectOptJSONObject.optString("album");
                    Log.d("MainPlugin", "album_bg: " + strOptString6);
                    c0772b.m1441k(C0678h.m964d(contextM973Kc, strOptString6));
                    c0772b.m1440e(C0678h.m964d(contextM973Kc, strOptString7));
                }
                if (strOptString2.equals("media_info")) {
                    c0772b.m1443m(C0678h.m964d(contextM973Kc, jSONObjectOptJSONObject.optString("artist_icon")));
                    c0772b.m1442l(C0678h.m964d(contextM973Kc, jSONObjectOptJSONObject.optString("album_icon")));
                }
                if (strOptString2.equals("iv_collect")) {
                    c0772b.m1444n(C0678h.m964d(contextM973Kc, jSONObjectOptJSONObject.optString("btn_img")));
                }
                if (strOptString2.equals("repeat")) {
                    c0772b.m1410C(C0678h.m964d(contextM973Kc, jSONObjectOptJSONObject.optString("btn_img")));
                }
                if (strOptString2.equals("media_seekbar_layout")) {
                    c0772b.m1414E(C0678h.m964d(contextM973Kc, jSONObjectOptJSONObject.optString("background")));
                    c0772b.m1412D(C0678h.m964d(contextM973Kc, jSONObjectOptJSONObject.optString("progressDrawable")));
                    c0772b.m1416F(C0678h.m964d(contextM973Kc, jSONObjectOptJSONObject.optString("thumb")));
                }
                i++;
                jSONArray = jSONArray4;
            }
        } catch (Exception e) {
            Log.e("MainPlugin", "parsingViewsConfig: " + e.getLocalizedMessage());
        }
        return c0772b;
    }

    /* JADX INFO: renamed from: A */
    public void m1406A(Drawable drawable) {
        this.f1128jn = drawable;
    }

    /* JADX INFO: renamed from: Ad */
    public Drawable m1407Ad() {
        return this.f1131mn;
    }

    /* JADX INFO: renamed from: B */
    public void m1408B(Drawable drawable) {
        this.f1129kn = drawable;
    }

    /* JADX INFO: renamed from: Bd */
    public Drawable m1409Bd() {
        return this.f1120Xm;
    }

    /* JADX INFO: renamed from: C */
    public void m1410C(Drawable drawable) {
        this.repeat = drawable;
    }

    /* JADX INFO: renamed from: Cd */
    public Drawable m1411Cd() {
        return this.f1127hn;
    }

    /* JADX INFO: renamed from: D */
    public void m1412D(Drawable drawable) {
        this.f1137un = drawable;
    }

    /* JADX INFO: renamed from: Dd */
    public Drawable m1413Dd() {
        return this.f1119Wm;
    }

    /* JADX INFO: renamed from: E */
    public void m1414E(Drawable drawable) {
        this.f1135rn = drawable;
    }

    /* JADX INFO: renamed from: Ed */
    public Drawable m1415Ed() {
        return this.f1126gn;
    }

    /* JADX INFO: renamed from: F */
    public void m1416F(Drawable drawable) {
        this.f1136tn = drawable;
    }

    /* JADX INFO: renamed from: Fd */
    public Drawable m1417Fd() {
        return this.f1123_m;
    }

    /* JADX INFO: renamed from: G */
    public void m1418G(Drawable drawable) {
        this.f1141yn = drawable;
    }

    /* JADX INFO: renamed from: Gd */
    public Drawable m1419Gd() {
        return this.f1132nn;
    }

    /* JADX INFO: renamed from: H */
    public void m1420H(Drawable drawable) {
        this.tab_btn_layout = drawable;
    }

    /* JADX INFO: renamed from: Hd */
    public Drawable m1421Hd() {
        return this.f1140xn;
    }

    /* JADX INFO: renamed from: I */
    public void m1422I(Drawable drawable) {
        this.f1142zn = drawable;
    }

    /* JADX INFO: renamed from: Id */
    public Drawable m1423Id() {
        return this.f1121Ym;
    }

    /* JADX INFO: renamed from: Jd */
    public Drawable m1424Jd() {
        return this.f1130ln;
    }

    /* JADX INFO: renamed from: Kd */
    public Drawable m1425Kd() {
        return this.f1118Vm;
    }

    /* JADX INFO: renamed from: Ld */
    public Drawable m1426Ld() {
        return this.f1125fn;
    }

    /* JADX INFO: renamed from: Md */
    public Drawable m1427Md() {
        return this.f1117Um;
    }

    /* JADX INFO: renamed from: Nd */
    public Drawable m1428Nd() {
        return this.f1124dn;
    }

    /* JADX INFO: renamed from: Od */
    public Drawable m1429Od() {
        return this.f1128jn;
    }

    /* JADX INFO: renamed from: Pd */
    public Drawable m1430Pd() {
        return this.f1129kn;
    }

    /* JADX INFO: renamed from: Qd */
    public int m1431Qd() {
        return this.f1134qn;
    }

    /* JADX INFO: renamed from: Rd */
    public int m1432Rd() {
        return this.f1133pn;
    }

    /* JADX INFO: renamed from: Sd */
    public Drawable m1433Sd() {
        return this.repeat;
    }

    /* JADX INFO: renamed from: Td */
    public Drawable m1434Td() {
        return this.f1137un;
    }

    /* JADX INFO: renamed from: Ud */
    public Drawable m1435Ud() {
        return this.f1135rn;
    }

    /* JADX INFO: renamed from: Vd */
    public Drawable m1436Vd() {
        return this.f1136tn;
    }

    /* JADX INFO: renamed from: Wd */
    public Drawable m1437Wd() {
        return this.f1141yn;
    }

    /* JADX INFO: renamed from: Xd */
    public Drawable m1438Xd() {
        return this.tab_btn_layout;
    }

    /* JADX INFO: renamed from: Yd */
    public Drawable m1439Yd() {
        return this.f1142zn;
    }

    /* JADX INFO: renamed from: e */
    public void m1440e(Drawable drawable) {
        this.album = drawable;
    }

    /* JADX INFO: renamed from: k */
    public void m1441k(Drawable drawable) {
        this.album_bg = drawable;
    }

    /* JADX INFO: renamed from: l */
    public void m1442l(Drawable drawable) {
        this.f1139wn = drawable;
    }

    /* JADX INFO: renamed from: m */
    public void m1443m(Drawable drawable) {
        this.f1138vn = drawable;
    }

    /* JADX INFO: renamed from: n */
    public void m1444n(Drawable drawable) {
        this.collect = drawable;
    }

    /* JADX INFO: renamed from: o */
    public void m1445o(Drawable drawable) {
        this.f1116Tm = drawable;
    }

    /* JADX INFO: renamed from: p */
    public void m1446p(Drawable drawable) {
        this.f1120Xm = drawable;
    }

    /* JADX INFO: renamed from: q */
    public void m1447q(Drawable drawable) {
        this.f1127hn = drawable;
    }

    /* JADX INFO: renamed from: r */
    public void m1448r(Drawable drawable) {
        this.f1119Wm = drawable;
    }

    /* JADX INFO: renamed from: s */
    public void m1449s(Drawable drawable) {
        this.f1126gn = drawable;
    }

    /* JADX INFO: renamed from: t */
    public void m1450t(Drawable drawable) {
        this.f1140xn = drawable;
    }

    /* JADX INFO: renamed from: u */
    public void m1451u(Drawable drawable) {
        this.f1121Ym = drawable;
    }

    /* JADX INFO: renamed from: v */
    public void m1452v(Drawable drawable) {
        this.f1130ln = drawable;
    }

    /* JADX INFO: renamed from: vd */
    public Drawable m1453vd() {
        return this.album_bg;
    }

    /* JADX INFO: renamed from: w */
    public void m1454w(Drawable drawable) {
        this.f1118Vm = drawable;
    }

    /* JADX INFO: renamed from: wd */
    public Drawable m1455wd() {
        return this.f1139wn;
    }

    /* JADX INFO: renamed from: x */
    public void m1456x(Drawable drawable) {
        this.f1125fn = drawable;
    }

    /* JADX INFO: renamed from: xd */
    public Drawable m1457xd() {
        return this.collect;
    }

    /* JADX INFO: renamed from: y */
    public void m1458y(Drawable drawable) {
        this.f1117Um = drawable;
    }

    /* JADX INFO: renamed from: ya */
    public void m1459ya(int i) {
        this.f1134qn = i;
    }

    /* JADX INFO: renamed from: yd */
    public Drawable m1460yd() {
        return this.f1116Tm;
    }

    /* JADX INFO: renamed from: z */
    public void m1461z(Drawable drawable) {
        this.f1124dn = drawable;
    }

    /* JADX INFO: renamed from: za */
    public void m1462za(int i) {
        this.f1133pn = i;
    }

    /* JADX INFO: renamed from: zd */
    public Drawable m1463zd() {
        return this.f1122Zm;
    }
}
