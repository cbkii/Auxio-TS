package com.p060tw.music.p063c;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.eckom.xtlibrary.p020b.p052i.C0678h;
import com.eckom.xtlibrary.p020b.p052i.C0681k;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: renamed from: com.tw.music.c.a */
/* JADX INFO: compiled from: ListPlugin.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0771a {

    /* JADX INFO: renamed from: Lm */
    private Drawable f1105Lm;

    /* JADX INFO: renamed from: Mm */
    private List<a> f1106Mm = new ArrayList();

    /* JADX INFO: renamed from: Nm */
    private List<a> f1107Nm = new ArrayList();

    /* JADX INFO: renamed from: Om */
    private String f1108Om;

    /* JADX INFO: renamed from: Pm */
    private Drawable f1109Pm;

    /* JADX INFO: renamed from: Qm */
    private int f1110Qm;

    /* JADX INFO: renamed from: Rm */
    private int f1111Rm;

    /* JADX INFO: renamed from: Sm */
    private Drawable f1112Sm;
    private Drawable album;
    private Drawable next;
    private Drawable prev;

    /* JADX INFO: renamed from: com.tw.music.c.a$a */
    /* JADX INFO: compiled from: ListPlugin.java */
    public static class a {

        /* JADX INFO: renamed from: Im */
        private Drawable f1113Im;

        /* JADX INFO: renamed from: Jm */
        private Drawable f1114Jm;

        /* JADX INFO: renamed from: Km */
        private String f1115Km;

        /* JADX INFO: renamed from: c */
        public void m1400c(Drawable drawable) {
            this.f1114Jm = drawable;
        }

        /* JADX INFO: renamed from: d */
        public void m1401d(Drawable drawable) {
            this.f1113Im = drawable;
        }

        /* JADX INFO: renamed from: ld */
        public Drawable m1402ld() {
            return this.f1114Jm;
        }

        /* JADX INFO: renamed from: md */
        public Drawable m1403md() {
            return this.f1113Im;
        }

        /* JADX INFO: renamed from: pb */
        public void m1404pb(String str) {
            this.f1115Km = str;
        }
    }

    /* JADX INFO: renamed from: rb */
    public static C0771a m1382rb(String str) {
        try {
            Context contextM973Kc = C0681k.get().m973Kc();
            contextM973Kc.getResources();
            C0681k.get().m973Kc().getPackageName();
            C0771a c0771a = new C0771a();
            JSONArray jSONArray = new JSONArray(str);
            c0771a.m1384f(C0678h.m964d(contextM973Kc, ((JSONObject) jSONArray.get(0)).optString("background")));
            JSONArray jSONArrayOptJSONArray = ((JSONObject) jSONArray.get(0)).optJSONArray("itemInfo");
            c0771a.m1390od().clear();
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                a aVar = new a();
                aVar.m1401d(C0678h.m964d(contextM973Kc, ((JSONObject) jSONArrayOptJSONArray.get(i)).optString("btn_img")));
                aVar.m1400c(C0678h.m964d(contextM973Kc, ((JSONObject) jSONArrayOptJSONArray.get(i)).optString("btn_background")));
                c0771a.m1390od().add(aVar);
            }
            c0771a.m1392qb(((JSONObject) jSONArray.get(1)).optString("listbackground"));
            JSONArray jSONArrayOptJSONArray2 = ((JSONObject) jSONArray.get(1)).optJSONArray("song");
            c0771a.m1399xa(C0678h.m962c(contextM973Kc, ((JSONObject) jSONArrayOptJSONArray2.get(0)).optString("color_selector")));
            c0771a.m1398wa(C0678h.m962c(contextM973Kc, ((JSONObject) jSONArrayOptJSONArray2.get(0)).optString("color_normal")));
            JSONArray jSONArrayOptJSONArray3 = ((JSONObject) jSONArray.get(1)).optJSONArray("itemInfolist");
            c0771a.m1391pd().clear();
            for (int i2 = 0; i2 < jSONArrayOptJSONArray3.length(); i2++) {
                a aVar2 = new a();
                aVar2.m1404pb(((JSONObject) jSONArrayOptJSONArray3.get(i2)).optString("btn_img"));
                c0771a.m1391pd().add(aVar2);
            }
            c0771a.m1388j(C0678h.m964d(contextM973Kc, ((JSONObject) jSONArray.get(2)).optString("backgroud")));
            c0771a.m1383e(C0678h.m964d(contextM973Kc, ((JSONObject) ((JSONObject) jSONArray.get(2)).optJSONArray("album").get(0)).optString("btn_img")));
            c0771a.m1387i(C0678h.m964d(contextM973Kc, ((JSONObject) ((JSONObject) jSONArray.get(2)).optJSONArray("prev").get(0)).optString("btn_img")));
            c0771a.m1386h(C0678h.m964d(contextM973Kc, ((JSONObject) ((JSONObject) jSONArray.get(2)).optJSONArray("playPause").get(0)).optString("btn_img")));
            c0771a.m1385g(C0678h.m964d(contextM973Kc, ((JSONObject) ((JSONObject) jSONArray.get(2)).optJSONArray("next").get(0)).optString("btn_img")));
            return c0771a;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: renamed from: e */
    public void m1383e(Drawable drawable) {
        this.album = drawable;
    }

    /* JADX INFO: renamed from: f */
    public void m1384f(Drawable drawable) {
        this.f1105Lm = drawable;
    }

    /* JADX INFO: renamed from: g */
    public void m1385g(Drawable drawable) {
        this.next = drawable;
    }

    public Drawable getAlbum() {
        return this.album;
    }

    public Drawable getNext() {
        return this.next;
    }

    /* JADX INFO: renamed from: h */
    public void m1386h(Drawable drawable) {
        this.f1112Sm = drawable;
    }

    /* JADX INFO: renamed from: i */
    public void m1387i(Drawable drawable) {
        this.prev = drawable;
    }

    /* JADX INFO: renamed from: j */
    public void m1388j(Drawable drawable) {
        this.f1109Pm = drawable;
    }

    /* JADX INFO: renamed from: nd */
    public Drawable m1389nd() {
        return this.f1105Lm;
    }

    /* JADX INFO: renamed from: od */
    public List<a> m1390od() {
        return this.f1106Mm;
    }

    /* JADX INFO: renamed from: pd */
    public List<a> m1391pd() {
        return this.f1107Nm;
    }

    /* JADX INFO: renamed from: qb */
    public void m1392qb(String str) {
        this.f1108Om = str;
    }

    /* JADX INFO: renamed from: qd */
    public Drawable m1393qd() {
        return this.f1112Sm;
    }

    /* JADX INFO: renamed from: rd */
    public Drawable m1394rd() {
        return this.prev;
    }

    /* JADX INFO: renamed from: sd */
    public int m1395sd() {
        return this.f1111Rm;
    }

    /* JADX INFO: renamed from: td */
    public int m1396td() {
        return this.f1110Qm;
    }

    /* JADX INFO: renamed from: ud */
    public Drawable m1397ud() {
        return this.f1109Pm;
    }

    /* JADX INFO: renamed from: wa */
    public void m1398wa(int i) {
        this.f1111Rm = i;
    }

    /* JADX INFO: renamed from: xa */
    public void m1399xa(int i) {
        this.f1110Qm = i;
    }
}
