package com.eckom.xtlibrary.p020b.p021a.p025d;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.eckom.xtlibrary.R$array;
import com.eckom.xtlibrary.p020b.p021a.p022a.C0532b;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0533a;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0535c;
import com.eckom.xtlibrary.p020b.p021a.p029h.C0555d;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.d.j */
/* JADX INFO: compiled from: BuildInBTModel.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0548j<P extends AbstractC0658a> extends AbstractC0546h implements C0532b.b {

    /* JADX INFO: renamed from: Fh */
    private static volatile C0548j f432Fh;
    private Context mContext;

    /* JADX INFO: renamed from: wh */
    private C0535c f436wh;

    /* JADX INFO: renamed from: zh */
    private C0555d f438zh;

    /* JADX INFO: renamed from: la */
    private C0533a f435la = C0533a.getInstance();

    /* JADX INFO: renamed from: yh */
    private Map<String, InterfaceC0545g> f437yh = new ConcurrentHashMap();

    /* JADX INFO: renamed from: ch */
    private List<BluetoothDevice> f434ch = new ArrayList();

    /* JADX INFO: renamed from: Dh */
    private boolean f433Dh = false;
    public Handler mHandler = new Handler(new C0547i(this));

    private C0548j() {
        this.f436wh = null;
        this.f436wh = C0535c.open();
        if (this.f436wh != null) {
            Log.d("BuildInBTModel", "BuildInBTModel: Model create ");
            this.f436wh.addHandler("BuildInBTModel", this.mHandler);
            this.f436wh.write(267, 255);
        }
        this.f435la.f398ng = C0686b.m999Tc();
    }

    public static C0548j getInstance() {
        if (f432Fh == null) {
            synchronized (C0548j.class) {
                if (f432Fh == null) {
                    f432Fh = new C0548j();
                }
            }
        }
        return f432Fh;
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: Aa */
    public void mo247Aa(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: B */
    public void mo248B(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: Ba */
    public void mo249Ba(String str) {
        this.f437yh.remove(str);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: Ca */
    public void mo250Ca(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: a */
    public void mo251a(Context context) {
        boolean z = context instanceof Activity;
        this.mContext = context.getApplicationContext();
        if (!this.f433Dh) {
            this.f433Dh = true;
        }
        if (this.f438zh == null) {
            this.f438zh = new C0555d(this.mContext, this);
        }
        String[] stringArray = this.mContext.getResources().getStringArray(R$array.enable_phone_call_record_version);
        this.f435la.f397kh = Arrays.asList(stringArray);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    public void answer() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: ba */
    public void mo254ba() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    public int getCallState() {
        return 0;
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: mb */
    public int mo255mb() {
        return 0;
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: nb */
    public void mo256nb() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: ob */
    public void mo257ob() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: pb */
    public void mo258pb() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: qb */
    public void mo259qb() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: rb */
    public void mo260rb() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: sb */
    public void mo261sb() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    public void setDeviceName(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p022a.C0532b.b
    /* JADX INFO: renamed from: t */
    public void mo197t(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h, com.eckom.xtlibrary.p020b.p021a.p022a.C0532b.b
    /* JADX INFO: renamed from: w */
    public void mo198w(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* JADX INFO: renamed from: a */
    public void mo252a(String str, InterfaceC0545g interfaceC0545g) {
        this.f437yh.put(str, interfaceC0545g);
    }
}
