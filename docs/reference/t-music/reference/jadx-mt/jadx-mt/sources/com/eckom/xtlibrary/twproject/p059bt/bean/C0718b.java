package com.eckom.xtlibrary.twproject.p059bt.bean;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.bt.bean.b */
/* JADX INFO: compiled from: TWDevice.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0718b {
    protected String deviceName;

    /* JADX INFO: renamed from: dg */
    protected String f888dg;

    public C0718b() {
    }

    public boolean equals(Object obj) {
        try {
            if (obj instanceof C0718b) {
                return this.f888dg.equals(((C0718b) obj).f888dg);
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public String toString() {
        return "TWDevice{deviceName='" + this.deviceName + "', deviceMac='" + this.f888dg + "'}";
    }

    public C0718b(String str, String str2) {
        this.deviceName = str;
        this.f888dg = str2;
    }
}
