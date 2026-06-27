package com.eckom.xtlibrary.p020b.p037f.p039b;

import android.graphics.Bitmap;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.b.c */
/* JADX INFO: compiled from: LMedia.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0576c {
    public String album;
    public String artist;
    public String displayName;
    public int duration;

    /* JADX INFO: renamed from: id */
    public int f478id;
    public String mediaType;
    public String name;

    /* JADX INFO: renamed from: oj */
    public boolean f479oj;

    /* JADX INFO: renamed from: pj */
    Bitmap f480pj;
    public String size;
    public String url;

    public C0576c() {
        this.name = "";
        this.size = "";
        this.url = "";
        this.duration = 0;
        this.f478id = 0;
        this.mediaType = "";
        this.album = "";
        this.artist = "";
        this.displayName = "";
        this.f479oj = false;
    }

    public String getUrl() {
        return this.url;
    }

    public C0576c(String str, String str2, String str3, String str4, Bitmap bitmap) {
        this.name = "";
        this.size = "";
        this.url = "";
        this.duration = 0;
        this.f478id = 0;
        this.mediaType = "";
        this.album = "";
        this.artist = "";
        this.displayName = "";
        this.f479oj = false;
        this.name = str;
        this.url = str2;
        this.album = str3;
        this.artist = str4;
        this.f480pj = bitmap;
    }
}
