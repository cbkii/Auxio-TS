package com.p060tw.music.p062b;

import android.graphics.Bitmap;

/* JADX INFO: renamed from: com.tw.music.b.a */
/* JADX INFO: compiled from: MusicInfo.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0769a {

    /* JADX INFO: renamed from: Am */
    private String f1097Am;

    /* JADX INFO: renamed from: Bm */
    private String f1098Bm;

    /* JADX INFO: renamed from: Cm */
    private String f1099Cm;

    /* JADX INFO: renamed from: Dm */
    private Bitmap f1100Dm;

    /* JADX INFO: renamed from: Em */
    private boolean f1101Em;

    /* JADX INFO: renamed from: Vh */
    private String f1103Vh;
    private int duration;
    private int index;

    /* JADX INFO: renamed from: oj */
    private boolean f1104oj;
    private int mCurrentPosition = 0;

    /* JADX INFO: renamed from: Fm */
    private int f1102Fm = 0;

    /* JADX INFO: renamed from: H */
    public void m1368H(boolean z) {
        this.f1101Em = z;
    }

    /* JADX INFO: renamed from: Nb */
    public String m1369Nb() {
        return this.f1098Bm;
    }

    /* JADX INFO: renamed from: a */
    public void m1370a(Bitmap bitmap) {
        this.f1100Dm = bitmap;
    }

    /* JADX INFO: renamed from: ed */
    public Bitmap m1371ed() {
        return this.f1100Dm;
    }

    /* JADX INFO: renamed from: fd */
    public String m1372fd() {
        return this.f1099Cm;
    }

    /* JADX INFO: renamed from: gd */
    public String m1373gd() {
        return this.f1103Vh;
    }

    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }

    public int getDuration() {
        return this.duration;
    }

    /* JADX INFO: renamed from: hd */
    public int m1374hd() {
        return this.f1102Fm;
    }

    public boolean isPlaying() {
        return this.f1101Em;
    }

    /* JADX INFO: renamed from: jd */
    public String m1375jd() {
        return this.f1097Am;
    }

    /* JADX INFO: renamed from: kb */
    public void m1376kb(String str) {
        this.f1099Cm = str;
    }

    /* JADX INFO: renamed from: lb */
    public void m1377lb(String str) {
        this.f1103Vh = str;
    }

    /* JADX INFO: renamed from: mb */
    public void m1378mb(String str) {
        this.f1097Am = str;
    }

    /* JADX INFO: renamed from: nb */
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

    /* JADX INFO: renamed from: ua */
    public void m1380ua(int i) {
        this.mCurrentPosition = i;
    }

    /* JADX INFO: renamed from: va */
    public void m1381va(int i) {
        this.f1102Fm = i;
    }
}
