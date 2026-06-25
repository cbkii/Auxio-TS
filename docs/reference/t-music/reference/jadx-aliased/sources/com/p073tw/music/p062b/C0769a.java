package com.p073tw.music.p062b;

import android.graphics.Bitmap;

/* compiled from: MusicInfo.java */
/* renamed from: com.tw.music.b.a */
/* loaded from: classes3.dex */
public class C0769a {

    /* renamed from: Am */
    private String f1097Am;

    /* renamed from: Bm */
    private String f1098Bm;

    /* renamed from: Cm */
    private String f1099Cm;

    /* renamed from: Dm */
    private Bitmap f1100Dm;

    /* renamed from: Em */
    private boolean f1101Em;

    /* renamed from: Vh */
    private String f1103Vh;
    private int duration;
    private int index;

    /* renamed from: oj */
    private boolean f1104oj;
    private int mCurrentPosition = 0;

    /* renamed from: Fm */
    private int f1102Fm = 0;

    /* renamed from: H */
    public void m1368H(boolean z) {
        this.f1101Em = z;
    }

    /* renamed from: Nb */
    public String m1369Nb() {
        return this.f1098Bm;
    }

    /* renamed from: a */
    public void m1370a(Bitmap bitmap) {
        this.f1100Dm = bitmap;
    }

    /* renamed from: ed */
    public Bitmap m1371ed() {
        return this.f1100Dm;
    }

    /* renamed from: fd */
    public String m1372fd() {
        return this.f1099Cm;
    }

    /* renamed from: gd */
    public String m1373gd() {
        return this.f1103Vh;
    }

    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }

    public int getDuration() {
        return this.duration;
    }

    /* renamed from: hd */
    public int m1374hd() {
        return this.f1102Fm;
    }

    public boolean isPlaying() {
        return this.f1101Em;
    }

    /* renamed from: jd */
    public String m1375jd() {
        return this.f1097Am;
    }

    /* renamed from: kb */
    public void m1376kb(String str) {
        this.f1099Cm = str;
    }

    /* renamed from: lb */
    public void m1377lb(String str) {
        this.f1103Vh = str;
    }

    /* renamed from: mb */
    public void m1378mb(String str) {
        this.f1097Am = str;
    }

    /* renamed from: nb */
    public void m1379nb(String str) {
        this.f1098Bm = str;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public String toString() {
        return "MusicInfo{singerName='" + this.f1097Am + "', songName='" + this.f1098Bm + "', albumName='" + this.f1099Cm + "', albumBitmap=" + this.f1100Dm + ", duration=" + this.duration + ", isPlaying=" + this.f1101Em + ", isFavorite=" + this.f1104oj + ", getShuffleRepeat=" + this.f1102Fm + ", path=" + this.f1103Vh + '}';
    }

    /* renamed from: ua */
    public void m1380ua(int i) {
        this.mCurrentPosition = i;
    }

    /* renamed from: va */
    public void m1381va(int i) {
        this.f1102Fm = i;
    }
}
