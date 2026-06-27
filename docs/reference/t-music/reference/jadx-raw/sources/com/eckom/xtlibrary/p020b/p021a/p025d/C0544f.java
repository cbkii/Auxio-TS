package com.eckom.xtlibrary.p020b.p021a.p025d;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.R$array;
import com.eckom.xtlibrary.p020b.p021a.p022a.C0532b;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0533a;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0535c;
import com.eckom.xtlibrary.p020b.p021a.p024c.C0537b;
import com.eckom.xtlibrary.p020b.p021a.p029h.C0555d;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.p020b.p053j.C0699o;
import com.eckom.xtlibrary.twproject.p059bt.bean.TWContact;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: BTModel.java */
/* renamed from: com.eckom.xtlibrary.b.a.d.f */
/* loaded from: classes3.dex */
public class C0544f<P extends AbstractC0658a> extends AbstractC0546h implements C0532b.b {

    /* renamed from: Fh */
    private static volatile C0544f f419Fh;
    private Context mContext;

    /* renamed from: mg */
    private int f426mg;

    /* renamed from: wh */
    private C0535c f427wh;

    /* renamed from: xh */
    private C0532b f428xh;

    /* renamed from: zh */
    private C0555d f430zh;
    private Uri mUri = null;
    private MediaPlayer mMediaPlayer = null;

    /* renamed from: la */
    private C0533a f425la = C0533a.getInstance();

    /* renamed from: yh */
    private Map<String, InterfaceC0545g> f429yh = new ConcurrentHashMap();

    /* renamed from: Ah */
    private boolean f420Ah = false;

    /* renamed from: Bh */
    private int f421Bh = 0;

    /* renamed from: Ch */
    private boolean f422Ch = false;

    /* renamed from: Dh */
    private boolean f423Dh = false;
    public Handler mHandler = new Handler(new C0541c(this));

    /* renamed from: Eh */
    private int f424Eh = 0;

    /* compiled from: BTModel.java */
    /* renamed from: com.eckom.xtlibrary.b.a.d.f$a */
    private class a extends AsyncTask<Integer, Void, Integer> {

        /* renamed from: Sc */
        ArrayList<TWContact> f431Sc;

        private a() {
            this.f431Sc = new ArrayList<>();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Integer doInBackground(Integer... numArr) {
            int intValue = numArr[0].intValue();
            if (intValue == 0) {
                this.f431Sc = C0537b.m207a(C0544f.this.mContext, C0544f.this.f425la.f409yg).m210jb();
                C0544f.this.f425la.f393gh.clear();
                C0544f.this.f425la.f393gh = this.f431Sc;
            } else if (intValue == 1) {
                this.f431Sc = C0537b.m207a(C0544f.this.mContext, C0544f.this.f425la.f409yg).m211kb();
                C0544f.this.f425la.f393gh.clear();
                C0544f.this.f425la.f393gh = this.f431Sc;
            }
            return numArr[0];
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((a) num);
            int intValue = num.intValue();
            if (intValue == 0) {
                Iterator it = C0544f.this.f429yh.entrySet().iterator();
                while (it.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it.next()).getValue()).mo287b(this.f431Sc);
                }
            } else {
                if (intValue != 1) {
                    return;
                }
                C0544f.this.f425la.f393gh.clear();
                C0544f.this.f425la.f393gh.addAll(this.f431Sc);
                C0544f.this.mHandler.sendEmptyMessage(65289);
            }
        }

        /* synthetic */ a(C0544f c0544f, C0541c c0541c) {
            this();
        }
    }

    private C0544f() {
        this.f427wh = null;
        this.f427wh = C0535c.open();
        if (this.f427wh != null) {
            Log.d("BTModel", "BTModel: Model create ");
            this.f427wh.addHandler("BTModel", this.mHandler);
            this.f427wh.write(267, 255);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ke */
    public void m213Ke() {
        new Thread(new RunnableC0543e(this)).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Le */
    public void m214Le() {
        try {
            new Thread(new RunnableC0542d(this)).start();
        } catch (Exception e) {
            Log.e("BTModel", "deleteContactsFromSystemDatabase: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Me */
    public void m215Me() {
        new a(this, null).execute(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Na */
    public String m216Na(int i) {
        switch (i) {
            case 1:
                return "SBC";
            case 2:
                return "MP3";
            case 3:
                return "AAC";
            case 4:
                return "FASTSTREAM";
            case 5:
                return "aptX";
            case 6:
                return "aptll";
            case 7:
                return "aptX HD";
            default:
                return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ne */
    public void m217Ne() {
        this.f427wh.write(22, 255);
        this.f427wh.write(22, 127);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Oa */
    public void m218Oa(int i) {
        Intent launchIntentForPackage;
        Intent intent = new Intent("com.zjinnova.zlink");
        intent.setPackage("com.zjinnova.zlink");
        Context context = this.mContext;
        if (context == null || (launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage("com.zjinnova.zlink")) == null || launchIntentForPackage.getComponent() == null) {
            return;
        }
        ComponentName component = launchIntentForPackage.getComponent();
        Log.d("BTModel", "sendZlinkKeyCode: carPlayCn:" + component + " keyCode:" + i);
        if (TextUtils.equals(component.getClassName(), "com.suding.speedplay.ui.MainActivity")) {
            intent.setComponent(new ComponentName("com.zjinnova.zlink", "com.texustek.speedplay.broadcast.VendorBroadcastReceiver"));
        }
        Log.d("BTModel", "sendZlinkKeyCode: targetCn:" + intent.getComponent());
        intent.putExtra("command", "REQ_SPEC_FUNC_CMD");
        intent.putExtra("specFuncCode", i);
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Oe */
    public void m219Oe() {
        new a(this, null).execute(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Pe */
    public void m220Pe() {
        String string = this.mContext.getSharedPreferences("lovedata", 0).getString("love" + this.f425la.f409yg, "");
        ArrayList arrayList = new ArrayList();
        String[] split = string.split("#");
        for (int i = 0; i < split.length - 2; i += 3) {
            TWContact tWContact = new TWContact(split[i], split[i + 1], split[i + 2]);
            tWContact.m1130A(true);
            arrayList.add(tWContact);
        }
        this.f425la.f393gh.clear();
        this.f425la.f393gh.addAll(arrayList);
        this.mHandler.sendEmptyMessage(65289);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Qe */
    public void m221Qe() {
        try {
            if (this.f426mg == 6 && this.f425la.f398ng.contains("V16")) {
                return;
            }
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.pause();
                this.mMediaPlayer.stop();
                this.mMediaPlayer = null;
            }
            this.mMediaPlayer = new MediaPlayer();
            Uri actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1);
            this.mUri = actualDefaultRingtoneUri;
            Log.e("BTModel", "initRingPlayer:mUri:" + this.mUri);
            if (this.mUri != null) {
                this.mMediaPlayer.reset();
                this.mMediaPlayer.setDataSource(this.mContext, actualDefaultRingtoneUri);
                this.mMediaPlayer.setVolume(0.4f, 0.4f);
                if (C0686b.m1006_c()) {
                    this.mMediaPlayer.setAudioStreamType(2);
                } else {
                    this.mMediaPlayer.setAudioStreamType(0);
                }
                this.mMediaPlayer.prepare();
                this.mMediaPlayer.setLooping(true);
                Log.e("BTModel", "initRingPlayer:end");
            }
        } catch (Exception e) {
            Log.e("BTModel", "initRingPlayer:" + e.getMessage());
        }
    }

    /* renamed from: e */
    static /* synthetic */ int m234e(C0544f c0544f) {
        int i = c0544f.f421Bh;
        c0544f.f421Bh = i + 1;
        return i;
    }

    public static C0544f getInstance() {
        if (f419Fh == null) {
            synchronized (C0544f.class) {
                if (f419Fh == null) {
                    f419Fh = new C0544f();
                }
            }
        }
        return f419Fh;
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: Aa */
    public void mo247Aa(String str) {
        this.f427wh.write(10, 3, str);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: B */
    public void mo248B(boolean z) {
        this.f425la.f407wg = z;
        this.f427wh.m205ca(z ? 8 : 136);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: Ba */
    public void mo249Ba(String str) {
        this.f429yh.remove(str);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: Ca */
    public void mo250Ca(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f427wh.write(32, str);
        SystemProperties.set("persist.service.bt.pincode", str);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    public void answer() {
        this.f427wh.write(10, 1);
        this.f427wh.write(10, 1);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: ba */
    public void mo254ba() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    public int getCallState() {
        return this.f425la.f364Cg;
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: mb */
    public int mo255mb() {
        return this.f425la.f403sg;
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: nb */
    public void mo256nb() {
        if (this.f425la.f401qg) {
            return;
        }
        this.f427wh.write(10, 0);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: ob */
    public void mo257ob() {
        this.f427wh.write(20, 0);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: pb */
    public void mo258pb() {
        if (this.f427wh != null) {
            if (this.f425la.mSource != 8) {
                m266xb();
            }
            this.f427wh.write(20, 2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: qb */
    public void mo259qb() {
        if (this.f427wh != null) {
            if (this.f425la.mSource != 8) {
                m266xb();
            }
            this.f427wh.write(20, 0);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: rb */
    public void mo260rb() {
        if (this.f427wh != null) {
            if (this.f425la.mSource != 8) {
                m266xb();
            }
            this.f427wh.write(20, 3);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: sb */
    public void mo261sb() {
        this.f427wh.write(12);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    public void setDeviceName(String str) {
        this.f427wh.write(30, str);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p022a.C0532b.b
    /* renamed from: t */
    public void mo197t(int i) {
    }

    /* renamed from: tb */
    public void m262tb() {
        this.f422Ch = false;
        this.f427wh.write(44, "Topway");
    }

    /* renamed from: ub */
    public void m263ub() {
        boolean z;
        this.mHandler.removeMessages(65287);
        int i = 0;
        while (true) {
            if (i >= this.f425la.f397kh.size()) {
                z = false;
                break;
            }
            C0533a c0533a = this.f425la;
            if (c0533a.f398ng.contains(c0533a.f397kh.get(i))) {
                z = true;
                break;
            }
            i++;
        }
        Log.d("BTModel", "getCallRecord: commonData.mSystemVersion:" + this.f425la.f398ng);
        Log.d("BTModel", "getCallRecord: commonData.mVersionName:" + this.f425la.mVersionName + " enablePhoneCallRecord:" + z + " supportPhoneCallRecord:" + this.f420Ah);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 65287;
        if (!TextUtils.isEmpty(this.f425la.mVersionName) && this.f420Ah && z) {
            obtainMessage.arg1 = 0;
            obtainMessage.arg2 = 0;
            this.f425la.f387Zg = true;
        } else {
            obtainMessage.arg1 = 1;
            obtainMessage.arg2 = 0;
        }
        this.mHandler.sendMessage(obtainMessage);
    }

    /* renamed from: vb */
    public void m264vb() {
        this.mHandler.removeMessages(65288);
        this.mHandler.sendEmptyMessage(65288);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h, com.eckom.xtlibrary.p020b.p021a.p022a.C0532b.b
    /* renamed from: w */
    public void mo198w(boolean z) {
        this.f427wh.m206w(z);
    }

    /* renamed from: wb */
    public void m265wb() {
        this.mHandler.removeMessages(65285);
        this.mHandler.sendEmptyMessage(65285);
    }

    /* renamed from: xb */
    public void m266xb() {
        if (TextUtils.isEmpty(this.f425la.f386Yg) || !this.f425la.f386Yg.startsWith("auto")) {
            this.f427wh.m206w(true);
        } else {
            Log.w("BTModel", "requestSource:Android Auto is connect, do not change source to 0x08");
        }
    }

    /* renamed from: yb */
    public void m267yb() {
        this.f427wh.write(64, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: b */
    public void m227b(int i, String str) {
        byte[] bArr = null;
        if (str == null) {
            this.f427wh.write(1296, i << 4, 0, (Object) null);
            return;
        }
        int i2 = this.f424Eh;
        if ((i2 & 1) == 1) {
            try {
                bArr = str.getBytes("UTF-8");
            } catch (Exception unused) {
            }
            this.f427wh.write(1296, (i << 4) | 0, bArr != null ? bArr.length : 0, bArr);
            return;
        }
        if ((i2 & 2) == 2) {
            try {
                bArr = str.getBytes("Unicode");
            } catch (Exception unused2) {
            }
            this.f427wh.write(1296, (i << 4) | 1, bArr != null ? bArr.length : 0, bArr);
            return;
        }
        int i3 = 3;
        if ((i2 & 4) == 4) {
            try {
                bArr = str.getBytes("GBK");
            } catch (Exception unused3) {
            }
            if (bArr == null) {
                try {
                    bArr = str.getBytes("GB2312");
                } catch (Exception unused4) {
                }
            } else {
                i3 = 2;
            }
            if (bArr == null && (this.f424Eh & 128) == 128) {
                try {
                    bArr = str.getBytes("UTF-8");
                } catch (Exception unused5) {
                }
                i3 = 0;
            }
            this.f427wh.write(1296, (i << 4) | i3, bArr != null ? bArr.length : 0, bArr);
            return;
        }
        if ((i2 & 8) == 8) {
            try {
                bArr = str.getBytes("GB2312");
            } catch (Exception unused6) {
            }
            if (bArr == null) {
                try {
                    bArr = str.getBytes("GBK");
                } catch (Exception unused7) {
                }
                i3 = 2;
            }
            if (bArr == null && (this.f424Eh & 128) == 128) {
                try {
                    bArr = str.getBytes("UTF-8");
                } catch (Exception unused8) {
                }
                i3 = 0;
            }
            this.f427wh.write(1296, (i << 4) | i3, bArr != null ? bArr.length : 0, bArr);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: a */
    public void mo251a(Context context) {
        this.mContext = context.getApplicationContext();
        if (!this.f423Dh) {
            this.f428xh = new C0532b(this.mContext, this);
            this.f423Dh = true;
        }
        this.f430zh = new C0555d(this.mContext, this);
        String[] stringArray = this.mContext.getResources().getStringArray(R$array.enable_phone_call_record_version);
        this.f425la.f397kh = Arrays.asList(stringArray);
        this.f425la.f382Ug = C0699o.m1029b(this.mContext, "TABLE_BT", "BATTERY_LEVEL", 0);
        this.f426mg = SystemProperties.getInt("persist.tw.bt.module", 2);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h
    /* renamed from: a */
    public void mo252a(String str, InterfaceC0545g interfaceC0545g) {
        this.f429yh.put(str, interfaceC0545g);
        this.f427wh.write(274, 255);
    }

    /* renamed from: a */
    public void m253a(boolean z, String str) {
        Intent intent = new Intent();
        if (z) {
            intent.setAction("net.easyconn.bt.connected");
            intent.putExtra("number", new String[]{str});
        } else {
            intent.setAction("net.easyconn.bt.closed");
        }
        this.mContext.sendBroadcast(intent);
    }
}
