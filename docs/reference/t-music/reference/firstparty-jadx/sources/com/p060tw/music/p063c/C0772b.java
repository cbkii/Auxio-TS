package com.p060tw.music.p063c;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p052i.C0678h;
import com.eckom.xtlibrary.p020b.p052i.C0681k;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: MainPlugin.java */
/* renamed from: com.tw.music.c.b */
/* loaded from: classes3.dex */
public class C0772b {

    /* renamed from: Tm */
    private Drawable f1116Tm;

    /* renamed from: Um */
    private Drawable f1117Um;

    /* renamed from: Vm */
    private Drawable f1118Vm;

    /* renamed from: Wm */
    private Drawable f1119Wm;

    /* renamed from: Xm */
    private Drawable f1120Xm;

    /* renamed from: Ym */
    private Drawable f1121Ym;

    /* renamed from: Zm */
    private Drawable f1122Zm;

    /* renamed from: _m */
    private Drawable f1123_m;
    private Drawable album;
    private Drawable album_bg;
    private Drawable collect;

    /* renamed from: dn */
    private Drawable f1124dn;

    /* renamed from: fn */
    private Drawable f1125fn;

    /* renamed from: gn */
    private Drawable f1126gn;

    /* renamed from: hn */
    private Drawable f1127hn;

    /* renamed from: jn */
    private Drawable f1128jn;

    /* renamed from: kn */
    private Drawable f1129kn;

    /* renamed from: ln */
    private Drawable f1130ln;

    /* renamed from: mn */
    private Drawable f1131mn;

    /* renamed from: nn */
    private Drawable f1132nn;

    /* renamed from: pn */
    private int f1133pn = 0;

    /* renamed from: qn */
    private int f1134qn = 0;
    private Drawable repeat;

    /* renamed from: rn */
    private Drawable f1135rn;
    private Drawable tab_btn_layout;

    /* renamed from: tn */
    private Drawable f1136tn;

    /* renamed from: un */
    private Drawable f1137un;

    /* renamed from: vn */
    private Drawable f1138vn;

    /* renamed from: wn */
    private Drawable f1139wn;

    /* renamed from: xn */
    private Drawable f1140xn;

    /* renamed from: yn */
    private Drawable f1141yn;

    /* renamed from: zn */
    private Drawable f1142zn;

    /* renamed from: sb */
    public static C0772b m1405sb(String str) {
        Log.d("MainPlugin", "parsingViewsConfig:111 " + str);
        C0772b c0772b = new C0772b();
        try {
            Context m973Kc = C0681k.get().m973Kc();
            JSONObject jSONObject = new JSONObject(str);
            c0772b.m1445o(C0678h.m964d(m973Kc, jSONObject.optString("background")));
            c0772b.m1450t(C0678h.m964d(m973Kc, jSONObject.optString("music_play_bg")));
            c0772b.m1418G(C0678h.m964d(m973Kc, jSONObject.optString("setting_layout_bg")));
            String optString = jSONObject.optString("toggle_bg");
            Log.d("MainPlugin", "parsingViewsConfig: toggle_bg:" + optString);
            c0772b.m1422I(C0678h.m964d(m973Kc, optString));
            c0772b.m1462za(C0678h.m962c(m973Kc, jSONObject.optString("music_freq_cylinder_start_color")));
            c0772b.m1459ya(C0678h.m962c(m973Kc, jSONObject.optString("music_freq_cylinder_end_color")));
            JSONArray jSONArray = new JSONArray(jSONObject.optString("itemInfo"));
            int i = 0;
            while (i < jSONArray.length()) {
                JSONObject optJSONObject = jSONArray.optJSONObject(i);
                String optString2 = optJSONObject.optString("idName");
                if (optString2.equals("tab_btn_layout")) {
                    c0772b.m1420H(C0678h.m964d(m973Kc, optJSONObject.optString("background")));
                    JSONArray jSONArray2 = new JSONArray(optJSONObject.optString("itemInfo"));
                    int i2 = 0;
                    while (i2 < jSONArray2.length()) {
                        JSONObject optJSONObject2 = jSONArray2.optJSONObject(i2);
                        String optString3 = optJSONObject2.optString("idName");
                        String optString4 = optJSONObject2.optString("btn_img");
                        JSONArray jSONArray3 = jSONArray;
                        String optString5 = optJSONObject2.optString("btn_background");
                        if (optString3.equals("play_list")) {
                            c0772b.m1451u(C0678h.m964d(m973Kc, optString4));
                            c0772b.m1452v(C0678h.m964d(m973Kc, optString5));
                        }
                        if (optString3.equals("prev")) {
                            c0772b.m1458y(C0678h.m964d(m973Kc, optString4));
                            c0772b.m1461z(C0678h.m964d(m973Kc, optString5));
                        }
                        if (optString3.equals("pp")) {
                            c0772b.m1454w(C0678h.m964d(m973Kc, optString4));
                            c0772b.m1456x(C0678h.m964d(m973Kc, optString5));
                        }
                        if (optString3.equals("next")) {
                            c0772b.m1448r(C0678h.m964d(m973Kc, optString4));
                            c0772b.m1449s(C0678h.m964d(m973Kc, optString5));
                        }
                        if (optString3.equals("eq")) {
                            c0772b.m1446p(C0678h.m964d(m973Kc, optString4));
                            c0772b.m1447q(C0678h.m964d(m973Kc, optString5));
                        }
                        if (optString3.equals("iv_setting")) {
                            c0772b.m1406A(C0678h.m964d(m973Kc, optString4));
                            c0772b.m1408B(C0678h.m964d(m973Kc, optString5));
                        }
                        i2++;
                        jSONArray = jSONArray3;
                    }
                }
                JSONArray jSONArray4 = jSONArray;
                if (optString2.equals("album_layout")) {
                    String optString6 = optJSONObject.optString("album_bg");
                    String optString7 = optJSONObject.optString("album");
                    Log.d("MainPlugin", "album_bg: " + optString6);
                    c0772b.m1441k(C0678h.m964d(m973Kc, optString6));
                    c0772b.m1440e(C0678h.m964d(m973Kc, optString7));
                }
                if (optString2.equals("media_info")) {
                    c0772b.m1443m(C0678h.m964d(m973Kc, optJSONObject.optString("artist_icon")));
                    c0772b.m1442l(C0678h.m964d(m973Kc, optJSONObject.optString("album_icon")));
                }
                if (optString2.equals("iv_collect")) {
                    c0772b.m1444n(C0678h.m964d(m973Kc, optJSONObject.optString("btn_img")));
                }
                if (optString2.equals("repeat")) {
                    c0772b.m1410C(C0678h.m964d(m973Kc, optJSONObject.optString("btn_img")));
                }
                if (optString2.equals("media_seekbar_layout")) {
                    c0772b.m1414E(C0678h.m964d(m973Kc, optJSONObject.optString("background")));
                    c0772b.m1412D(C0678h.m964d(m973Kc, optJSONObject.optString("progressDrawable")));
                    c0772b.m1416F(C0678h.m964d(m973Kc, optJSONObject.optString("thumb")));
                }
                i++;
                jSONArray = jSONArray4;
            }
        } catch (Exception e) {
            Log.e("MainPlugin", "parsingViewsConfig: " + e.getLocalizedMessage());
        }
        return c0772b;
    }

    /* renamed from: A */
    public void m1406A(Drawable drawable) {
        this.f1128jn = drawable;
    }

    /* renamed from: Ad */
    public Drawable m1407Ad() {
        return this.f1131mn;
    }

    /* renamed from: B */
    public void m1408B(Drawable drawable) {
        this.f1129kn = drawable;
    }

    /* renamed from: Bd */
    public Drawable m1409Bd() {
        return this.f1120Xm;
    }

    /* renamed from: C */
    public void m1410C(Drawable drawable) {
        this.repeat = drawable;
    }

    /* renamed from: Cd */
    public Drawable m1411Cd() {
        return this.f1127hn;
    }

    /* renamed from: D */
    public void m1412D(Drawable drawable) {
        this.f1137un = drawable;
    }

    /* renamed from: Dd */
    public Drawable m1413Dd() {
        return this.f1119Wm;
    }

    /* renamed from: E */
    public void m1414E(Drawable drawable) {
        this.f1135rn = drawable;
    }

    /* renamed from: Ed */
    public Drawable m1415Ed() {
        return this.f1126gn;
    }

    /* renamed from: F */
    public void m1416F(Drawable drawable) {
        this.f1136tn = drawable;
    }

    /* renamed from: Fd */
    public Drawable m1417Fd() {
        return this.f1123_m;
    }

    /* renamed from: G */
    public void m1418G(Drawable drawable) {
        this.f1141yn = drawable;
    }

    /* renamed from: Gd */
    public Drawable m1419Gd() {
        return this.f1132nn;
    }

    /* renamed from: H */
    public void m1420H(Drawable drawable) {
        this.tab_btn_layout = drawable;
    }

    /* renamed from: Hd */
    public Drawable m1421Hd() {
        return this.f1140xn;
    }

    /* renamed from: I */
    public void m1422I(Drawable drawable) {
        this.f1142zn = drawable;
    }

    /* renamed from: Id */
    public Drawable m1423Id() {
        return this.f1121Ym;
    }

    /* renamed from: Jd */
    public Drawable m1424Jd() {
        return this.f1130ln;
    }

    /* renamed from: Kd */
    public Drawable m1425Kd() {
        return this.f1118Vm;
    }

    /* renamed from: Ld */
    public Drawable m1426Ld() {
        return this.f1125fn;
    }

    /* renamed from: Md */
    public Drawable m1427Md() {
        return this.f1117Um;
    }

    /* renamed from: Nd */
    public Drawable m1428Nd() {
        return this.f1124dn;
    }

    /* renamed from: Od */
    public Drawable m1429Od() {
        return this.f1128jn;
    }

    /* renamed from: Pd */
    public Drawable m1430Pd() {
        return this.f1129kn;
    }

    /* renamed from: Qd */
    public int m1431Qd() {
        return this.f1134qn;
    }

    /* renamed from: Rd */
    public int m1432Rd() {
        return this.f1133pn;
    }

    /* renamed from: Sd */
    public Drawable m1433Sd() {
        return this.repeat;
    }

    /* renamed from: Td */
    public Drawable m1434Td() {
        return this.f1137un;
    }

    /* renamed from: Ud */
    public Drawable m1435Ud() {
        return this.f1135rn;
    }

    /* renamed from: Vd */
    public Drawable m1436Vd() {
        return this.f1136tn;
    }

    /* renamed from: Wd */
    public Drawable m1437Wd() {
        return this.f1141yn;
    }

    /* renamed from: Xd */
    public Drawable m1438Xd() {
        return this.tab_btn_layout;
    }

    /* renamed from: Yd */
    public Drawable m1439Yd() {
        return this.f1142zn;
    }

    /* renamed from: e */
    public void m1440e(Drawable drawable) {
        this.album = drawable;
    }

    /* renamed from: k */
    public void m1441k(Drawable drawable) {
        this.album_bg = drawable;
    }

    /* renamed from: l */
    public void m1442l(Drawable drawable) {
        this.f1139wn = drawable;
    }

    /* renamed from: m */
    public void m1443m(Drawable drawable) {
        this.f1138vn = drawable;
    }

    /* renamed from: n */
    public void m1444n(Drawable drawable) {
        this.collect = drawable;
    }

    /* renamed from: o */
    public void m1445o(Drawable drawable) {
        this.f1116Tm = drawable;
    }

    /* renamed from: p */
    public void m1446p(Drawable drawable) {
        this.f1120Xm = drawable;
    }

    /* renamed from: q */
    public void m1447q(Drawable drawable) {
        this.f1127hn = drawable;
    }

    /* renamed from: r */
    public void m1448r(Drawable drawable) {
        this.f1119Wm = drawable;
    }

    /* renamed from: s */
    public void m1449s(Drawable drawable) {
        this.f1126gn = drawable;
    }

    /* renamed from: t */
    public void m1450t(Drawable drawable) {
        this.f1140xn = drawable;
    }

    /* renamed from: u */
    public void m1451u(Drawable drawable) {
        this.f1121Ym = drawable;
    }

    /* renamed from: v */
    public void m1452v(Drawable drawable) {
        this.f1130ln = drawable;
    }

    /* renamed from: vd */
    public Drawable m1453vd() {
        return this.album_bg;
    }

    /* renamed from: w */
    public void m1454w(Drawable drawable) {
        this.f1118Vm = drawable;
    }

    /* renamed from: wd */
    public Drawable m1455wd() {
        return this.f1139wn;
    }

    /* renamed from: x */
    public void m1456x(Drawable drawable) {
        this.f1125fn = drawable;
    }

    /* renamed from: xd */
    public Drawable m1457xd() {
        return this.collect;
    }

    /* renamed from: y */
    public void m1458y(Drawable drawable) {
        this.f1117Um = drawable;
    }

    /* renamed from: ya */
    public void m1459ya(int i) {
        this.f1134qn = i;
    }

    /* renamed from: yd */
    public Drawable m1460yd() {
        return this.f1116Tm;
    }

    /* renamed from: z */
    public void m1461z(Drawable drawable) {
        this.f1124dn = drawable;
    }

    /* renamed from: za */
    public void m1462za(int i) {
        this.f1133pn = i;
    }

    /* renamed from: zd */
    public Drawable m1463zd() {
        return this.f1122Zm;
    }
}
