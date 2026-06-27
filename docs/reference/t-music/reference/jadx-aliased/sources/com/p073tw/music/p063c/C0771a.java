package com.p073tw.music.p063c;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.eckom.xtlibrary.p066b.p052i.C0678h;
import com.eckom.xtlibrary.p066b.p052i.C0681k;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ListPlugin.java */
/* renamed from: com.tw.music.c.a */
/* loaded from: classes3.dex */
public class C0771a {

    /* renamed from: Lm */
    private Drawable f1105Lm;

    /* renamed from: Mm */
    private List<a> f1106Mm = new ArrayList();

    /* renamed from: Nm */
    private List<a> f1107Nm = new ArrayList();

    /* renamed from: Om */
    private String f1108Om;

    /* renamed from: Pm */
    private Drawable f1109Pm;

    /* renamed from: Qm */
    private int f1110Qm;

    /* renamed from: Rm */
    private int f1111Rm;

    /* renamed from: Sm */
    private Drawable f1112Sm;
    private Drawable album;
    private Drawable next;
    private Drawable prev;

    /* compiled from: ListPlugin.java */
    /* renamed from: com.tw.music.c.a$a */
    public static class a {

        /* renamed from: Im */
        private Drawable f1113Im;

        /* renamed from: Jm */
        private Drawable f1114Jm;

        /* renamed from: Km */
        private String f1115Km;

        /* renamed from: c */
        public void m1400c(Drawable drawable) {
            this.f1114Jm = drawable;
        }

        /* renamed from: d */
        public void m1401d(Drawable drawable) {
            this.f1113Im = drawable;
        }

        /* renamed from: ld */
        public Drawable m1402ld() {
            return this.f1114Jm;
        }

        /* renamed from: md */
        public Drawable m1403md() {
            return this.f1113Im;
        }

        /* renamed from: pb */
        public void m1404pb(String str) {
            this.f1115Km = str;
        }
    }

    /* renamed from: rb */
    public static C0771a m1382rb(String str) {
        try {
            Context m973Kc = C0681k.get().m973Kc();
            m973Kc.getResources();
            C0681k.get().m973Kc().getPackageName();
            C0771a c0771a = new C0771a();
            JSONArray jSONArray = new JSONArray(str);
            c0771a.m1384f(C0678h.m964d(m973Kc, ((JSONObject) jSONArray.get(0)).optString("background")));
            JSONArray optJSONArray = ((JSONObject) jSONArray.get(0)).optJSONArray("itemInfo");
            c0771a.m1390od().clear();
            for (int i = 0; i < optJSONArray.length(); i++) {
                a aVar = new a();
                aVar.m1401d(C0678h.m964d(m973Kc, ((JSONObject) optJSONArray.get(i)).optString("btn_img")));
                aVar.m1400c(C0678h.m964d(m973Kc, ((JSONObject) optJSONArray.get(i)).optString("btn_background")));
                c0771a.m1390od().add(aVar);
            }
            c0771a.m1392qb(((JSONObject) jSONArray.get(1)).optString("listbackground"));
            JSONArray optJSONArray2 = ((JSONObject) jSONArray.get(1)).optJSONArray("song");
            c0771a.m1399xa(C0678h.m962c(m973Kc, ((JSONObject) optJSONArray2.get(0)).optString("color_selector")));
            c0771a.m1398wa(C0678h.m962c(m973Kc, ((JSONObject) optJSONArray2.get(0)).optString("color_normal")));
            JSONArray optJSONArray3 = ((JSONObject) jSONArray.get(1)).optJSONArray("itemInfolist");
            c0771a.m1391pd().clear();
            for (int i2 = 0; i2 < optJSONArray3.length(); i2++) {
                a aVar2 = new a();
                aVar2.m1404pb(((JSONObject) optJSONArray3.get(i2)).optString("btn_img"));
                c0771a.m1391pd().add(aVar2);
            }
            c0771a.m1388j(C0678h.m964d(m973Kc, ((JSONObject) jSONArray.get(2)).optString("backgroud")));
            c0771a.m1383e(C0678h.m964d(m973Kc, ((JSONObject) ((JSONObject) jSONArray.get(2)).optJSONArray("album").get(0)).optString("btn_img")));
            c0771a.m1387i(C0678h.m964d(m973Kc, ((JSONObject) ((JSONObject) jSONArray.get(2)).optJSONArray("prev").get(0)).optString("btn_img")));
            c0771a.m1386h(C0678h.m964d(m973Kc, ((JSONObject) ((JSONObject) jSONArray.get(2)).optJSONArray("playPause").get(0)).optString("btn_img")));
            c0771a.m1385g(C0678h.m964d(m973Kc, ((JSONObject) ((JSONObject) jSONArray.get(2)).optJSONArray("next").get(0)).optString("btn_img")));
            return c0771a;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: e */
    public void m1383e(Drawable drawable) {
        this.album = drawable;
    }

    /* renamed from: f */
    public void m1384f(Drawable drawable) {
        this.f1105Lm = drawable;
    }

    /* renamed from: g */
    public void m1385g(Drawable drawable) {
        this.next = drawable;
    }

    public Drawable getAlbum() {
        return this.album;
    }

    public Drawable getNext() {
        return this.next;
    }

    /* renamed from: h */
    public void m1386h(Drawable drawable) {
        this.f1112Sm = drawable;
    }

    /* renamed from: i */
    public void m1387i(Drawable drawable) {
        this.prev = drawable;
    }

    /* renamed from: j */
    public void m1388j(Drawable drawable) {
        this.f1109Pm = drawable;
    }

    /* renamed from: nd */
    public Drawable m1389nd() {
        return this.f1105Lm;
    }

    /* renamed from: od */
    public List<a> m1390od() {
        return this.f1106Mm;
    }

    /* renamed from: pd */
    public List<a> m1391pd() {
        return this.f1107Nm;
    }

    /* renamed from: qb */
    public void m1392qb(String str) {
        this.f1108Om = str;
    }

    /* renamed from: qd */
    public Drawable m1393qd() {
        return this.f1112Sm;
    }

    /* renamed from: rd */
    public Drawable m1394rd() {
        return this.prev;
    }

    /* renamed from: sd */
    public int m1395sd() {
        return this.f1111Rm;
    }

    /* renamed from: td */
    public int m1396td() {
        return this.f1110Qm;
    }

    /* renamed from: ud */
    public Drawable m1397ud() {
        return this.f1109Pm;
    }

    /* renamed from: wa */
    public void m1398wa(int i) {
        this.f1111Rm = i;
    }

    /* renamed from: xa */
    public void m1399xa(int i) {
        this.f1110Qm = i;
    }
}
