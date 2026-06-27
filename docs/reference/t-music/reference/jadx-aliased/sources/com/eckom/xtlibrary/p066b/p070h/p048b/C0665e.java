package com.eckom.xtlibrary.p066b.p070h.p048b;

import android.content.Context;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.media.subtitle.Cea708CCParser;
import android.support.v4.view.InputDeviceCompat;
import android.util.Log;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p066b.C0556b;
import com.eckom.xtlibrary.p066b.p036e.C0570a;
import com.eckom.xtlibrary.p066b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p066b.p053j.C0686b;
import com.eckom.xtlibrary.p066b.p070h.C0659a;
import com.eckom.xtlibrary.p066b.p070h.p047a.C0660a;
import com.eckom.xtlibrary.p066b.p070h.p050d.C0669b;
import com.eckom.xtlibrary.twproject.radio.utils.C0721b;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: RadioModel.java */
/* renamed from: com.eckom.xtlibrary.b.h.b.e */
/* loaded from: classes3.dex */
public class C0665e<P extends AbstractC0658a> extends C0570a {

    /* renamed from: Li */
    private static volatile C0665e f775Li;

    /* renamed from: Ci */
    protected int f776Ci;

    /* renamed from: Gi */
    private C0660a[] f780Gi;

    /* renamed from: Hi */
    private C0721b f781Hi;

    /* renamed from: Ii */
    private boolean f782Ii;

    /* renamed from: if */
    private int f784if;
    protected int mActivity;
    private Context mContext;
    public int mService;

    /* renamed from: pi */
    private int f785pi;

    /* renamed from: wg */
    private boolean f786wg;

    /* renamed from: wh */
    private C0669b f787wh;

    /* renamed from: Di */
    private boolean f777Di = true;

    /* renamed from: Ei */
    private boolean f778Ei = false;

    /* renamed from: yh */
    private Map<String, InterfaceC0666f> f788yh = new ConcurrentHashMap();

    /* renamed from: Fi */
    private C0659a f779Fi = C0659a.getInstance();
    int location = -1;
    public Handler mHandler = new Handler(new C0662b(this));

    /* renamed from: Ji */
    private C0721b.a f783Ji = new C0664d(this);

    private C0665e() {
        this.f787wh = null;
        this.f787wh = C0669b.open();
        if (this.f787wh != null) {
            Log.d("RadioModel", "RadioModel: Model create ");
            this.f787wh.addHandler("RadioModel", this.mHandler);
        }
        m812_e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: M */
    public void m811M(boolean z) {
        try {
            if (C0556b.getInstant().f455cd != null) {
                Bundle bundle = new Bundle();
                bundle.putString("dateType", "send");
                bundle.putString("action", "com.tw.radio.state");
                bundle.putBoolean("RadioState", z);
                C0556b.getInstant().f455cd.mo142a(bundle);
            }
        } catch (Exception e) {
            Log.e("RadioModel", "sendRadioState: Error" + e.getMessage());
        }
    }

    /* renamed from: _e */
    private void m812_e() {
        this.f780Gi = new C0660a[18];
        for (int i = 0; i < 18; i++) {
            this.f780Gi[i] = new C0660a();
        }
    }

    /* renamed from: af */
    private void m816af() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), "isWAZE", 0) == 0) {
            return;
        }
        this.mHandler.postDelayed(new RunnableC0663c(this), 500L);
    }

    /* renamed from: da */
    private void m822da(int i) {
        this.f787wh.write(40465, 192, i);
    }

    public static C0665e getInstance() {
        if (f775Li == null) {
            synchronized (C0665e.class) {
                if (f775Li == null) {
                    f775Li = new C0665e();
                }
            }
        }
        return f775Li;
    }

    /* renamed from: n */
    private void m828n(int i, int i2) {
        this.f778Ei = true;
        this.f787wh.write(1026, i, i2);
    }

    /* renamed from: B */
    public void m829B(boolean z) {
        m843ca(z ? 1 : Cea708CCParser.Const.CODE_C1_CW1);
    }

    /* renamed from: C */
    public void m830C(boolean z) {
        this.f777Di = z;
    }

    /* renamed from: D */
    protected void m831D(boolean z) {
        Iterator<Map.Entry<String, InterfaceC0666f>> it = this.f788yh.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().mo877u(z);
        }
    }

    /* renamed from: Ha */
    public void m832Ha(String str) {
        if ((this.mService & 143) != 1) {
            this.f788yh.remove(str);
        }
    }

    /* renamed from: Yb */
    public void m833Yb() {
        new Thread(new RunnableC0661a(this)).start();
    }

    /* renamed from: Zb */
    public boolean m834Zb() {
        return this.f779Fi.mSource == 1;
    }

    /* renamed from: _b */
    public void m835_b() {
        BufferedReader bufferedReader;
        int i = 0;
        while (true) {
            C0660a[] c0660aArr = this.f779Fi.f734Gi;
            bufferedReader = null;
            try {
                if (i < c0660aArr.length) {
                    c0660aArr[i].f772vl = null;
                    i++;
                } else {
                    try {
                        break;
                    } catch (Exception unused) {
                    } catch (Throwable th) {
                        th = th;
                    }
                }
            } catch (Exception unused2) {
                return;
            }
        }
        BufferedReader bufferedReader2 = new BufferedReader(new FileReader("/data/tw/radio_name_" + this.f779Fi.f749Xk));
        for (int i2 = 0; i2 < this.f779Fi.f734Gi.length; i2++) {
            try {
                this.f779Fi.f734Gi[i2].f772vl = bufferedReader2.readLine();
            } catch (Exception unused3) {
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                    return;
                }
                return;
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        }
        bufferedReader2.close();
    }

    /* renamed from: ac */
    public void m839ac() {
        C0659a c0659a = this.f779Fi;
        if (c0659a.f759il) {
            return;
        }
        int i = c0659a.f762ll - 1;
        if (i < -1) {
            i = -1;
        }
        int i2 = i;
        while (true) {
            if (i2 <= -1) {
                break;
            }
            C0659a c0659a2 = this.f779Fi;
            if (c0659a2.f734Gi[i2].f770tl != 0) {
                m828n(i2, c0659a2.f749Xk);
                break;
            }
            i2--;
        }
        if (i2 == -1) {
            int length = this.f779Fi.f734Gi.length - 1;
            while (true) {
                if (length <= i) {
                    break;
                }
                C0659a c0659a3 = this.f779Fi;
                if (c0659a3.f734Gi[length].f770tl != 0) {
                    m828n(length, c0659a3.f749Xk);
                    break;
                }
                length--;
            }
        }
        if (C0686b.m999Tc().contains("KED")) {
            m816af();
        }
        m851w(true);
    }

    /* renamed from: bc */
    public void m841bc() {
        BufferedWriter bufferedWriter = null;
        try {
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter("/data/tw/radio_name_" + this.f779Fi.f749Xk));
                    for (int i = 0; i < this.f779Fi.f734Gi.length; i++) {
                        try {
                            String str = this.f779Fi.f734Gi[i].f772vl;
                            if (str != null) {
                                bufferedWriter2.write(str);
                            }
                            bufferedWriter2.write(10);
                        } catch (Exception unused) {
                            bufferedWriter = bufferedWriter2;
                            new File("/data/tw/radio_name_" + this.f779Fi.f749Xk).delete();
                            if (bufferedWriter != null) {
                                bufferedWriter.close();
                            }
                            FileUtils.setPermissions("/data/tw/radio_name_" + this.f779Fi.f749Xk, 438, -1, -1);
                        } catch (Throwable th) {
                            th = th;
                            bufferedWriter = bufferedWriter2;
                            if (bufferedWriter != null) {
                                bufferedWriter.close();
                            }
                            throw th;
                        }
                    }
                    bufferedWriter2.flush();
                    bufferedWriter2.close();
                } catch (Exception unused2) {
                    return;
                }
            } catch (Exception unused3) {
            }
            FileUtils.setPermissions("/data/tw/radio_name_" + this.f779Fi.f749Xk, 438, -1, -1);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* renamed from: ca */
    public void m843ca(int i) {
        this.mService = i;
        this.f787wh.write(40448, i);
    }

    /* renamed from: cc */
    public void m844cc() {
        if ((this.mService & 143) == 1) {
            C0659a c0659a = this.f779Fi;
            int i = c0659a.f749Xk + 1;
            int i2 = 0;
            if (c0659a.f767ql != 9 ? i <= 2 : i <= 1) {
                i2 = i;
            }
            this.f787wh.write(InputDeviceCompat.SOURCE_GAMEPAD, 5, i2);
            m851w(true);
        }
    }

    /* renamed from: dc */
    public void m845dc() {
        this.f787wh.write(InputDeviceCompat.SOURCE_GAMEPAD, 1, 0);
    }

    /* renamed from: ec */
    public void m846ec() {
        this.f787wh.write(InputDeviceCompat.SOURCE_GAMEPAD, 1, 1);
    }

    /* renamed from: fc */
    public void m847fc() {
        this.f787wh.write(InputDeviceCompat.SOURCE_GAMEPAD, 2, 0);
    }

    /* renamed from: hc */
    public void m848hc() {
        this.f787wh.write(InputDeviceCompat.SOURCE_GAMEPAD, 2, 1);
        m851w(true);
    }

    /* renamed from: ma */
    public void m849ma(int i) {
        if ((this.mService & 143) != 1 || i < 0 || i > 2) {
            return;
        }
        this.f787wh.write(InputDeviceCompat.SOURCE_GAMEPAD, 5, i);
    }

    /* renamed from: na */
    public void m850na(int i) {
        this.f787wh.write(1026, 255, i);
    }

    public void next() {
        C0659a c0659a = this.f779Fi;
        if (c0659a.f759il) {
            return;
        }
        int i = c0659a.f762ll + 1;
        C0660a[] c0660aArr = c0659a.f734Gi;
        if (i > c0660aArr.length) {
            i = c0660aArr.length;
        }
        int i2 = i;
        while (true) {
            C0659a c0659a2 = this.f779Fi;
            C0660a[] c0660aArr2 = c0659a2.f734Gi;
            if (i2 >= c0660aArr2.length) {
                break;
            }
            if (c0660aArr2[i2].f770tl != 0) {
                m828n(i2, c0659a2.f749Xk);
                break;
            }
            i2++;
        }
        if (i2 == this.f779Fi.f734Gi.length) {
            int i3 = 0;
            while (true) {
                if (i3 >= i) {
                    break;
                }
                C0659a c0659a3 = this.f779Fi;
                if (c0659a3.f734Gi[i3].f770tl != 0) {
                    m828n(i3, c0659a3.f749Xk);
                    break;
                }
                i3++;
            }
        }
        if (C0686b.m999Tc().contains("KED")) {
            m816af();
        }
        m851w(true);
    }

    public void onResume() {
        this.f786wg = true;
        m851w(true);
        m843ca(1);
    }

    /* renamed from: w */
    public void m851w(boolean z) {
        if (this.f782Ii) {
            return;
        }
        if (!z) {
            m822da(Cea708CCParser.Const.CODE_C1_CW1);
        } else {
            if (m834Zb()) {
                return;
            }
            m822da(1);
        }
    }

    /* renamed from: zb */
    public void m852zb() {
        C0659a c0659a = this.f779Fi;
        c0659a.f759il = false;
        if ((this.mService & 143) == 1) {
            return;
        }
        this.f776Ci = 0;
        c0659a.f749Xk = 0;
        this.f784if = 0;
        c0659a.f767ql = 0;
        this.mService = 0;
        c0659a.mSource = 0;
        c0659a.f766pl = null;
        if (this.f787wh != null && this.f788yh.size() == 0 && this.f777Di) {
            m851w(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: d */
    public void m821d(Message message) {
        int i = message.arg1;
        this.f785pi = (i >> 16) & SupportMenu.USER_MASK;
        int i2 = this.f776Ci;
        if (i2 != i) {
            int i3 = i2 ^ i;
            this.f776Ci = i;
            if ((i3 & 4) == 4) {
                this.f779Fi.f756fl = (this.f776Ci & 4) == 4;
                Iterator<Map.Entry<String, InterfaceC0666f>> it = this.f788yh.entrySet().iterator();
                while (it.hasNext()) {
                    it.next().getValue().mo866i((this.f776Ci & 4) == 4);
                }
            }
            if ((i3 & 32) == 32) {
                this.f779Fi.f754dl = (this.f776Ci & 32) == 32;
                Iterator<Map.Entry<String, InterfaceC0666f>> it2 = this.f788yh.entrySet().iterator();
                while (it2.hasNext()) {
                    it2.next().getValue().mo876t((this.f776Ci & 32) == 32);
                }
            }
            if ((i3 & 64) == 64) {
                this.f779Fi.f755el = (this.f776Ci & 64) == 64;
                Iterator<Map.Entry<String, InterfaceC0666f>> it3 = this.f788yh.entrySet().iterator();
                while (it3.hasNext()) {
                    it3.next().getValue().mo870p((this.f776Ci & 64) == 64);
                }
            }
            if ((i3 & 2) == 2) {
                this.f779Fi.f751Zk = (this.f776Ci & 2) == 2;
                Iterator<Map.Entry<String, InterfaceC0666f>> it4 = this.f788yh.entrySet().iterator();
                while (it4.hasNext()) {
                    it4.next().getValue().mo875s((this.f776Ci & 2) == 2);
                }
            }
            if ((i3 & 128) == 128) {
                this.f779Fi.f752_k = (this.f776Ci & 128) == 128;
                Iterator<Map.Entry<String, InterfaceC0666f>> it5 = this.f788yh.entrySet().iterator();
                while (it5.hasNext()) {
                    it5.next().getValue().mo863e((this.f776Ci & 128) == 128);
                }
            }
        }
        C0659a c0659a = this.f779Fi;
        int i4 = message.arg2;
        c0659a.f760jl = i4 & 255;
        c0659a.f761kl = (i4 >> 8) & 255;
        C0529b.m181e("0404:" + this.f785pi + "   " + this.f779Fi.f761kl);
        if (this.f778Ei) {
            return;
        }
        this.mHandler.sendEmptyMessageDelayed(65283, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: e */
    public void m824e(Message message) {
        switch (message.arg1) {
            case 0:
                C0659a c0659a = this.f779Fi;
                c0659a.mRegion = message.arg2;
                m831D(c0659a.f750Yk);
                break;
            case 1:
                C0659a c0659a2 = this.f779Fi;
                int i = message.arg2;
                c0659a2.f735Jk = i;
                c0659a2.f737Lk = i;
                break;
            case 2:
                C0659a c0659a3 = this.f779Fi;
                int i2 = message.arg2;
                c0659a3.f736Kk = i2;
                c0659a3.f738Mk = i2;
                break;
            case 3:
                this.f779Fi.f739Nk = message.arg2;
                break;
            case 4:
                this.f779Fi.f740Ok = message.arg2;
                break;
            case 5:
                C0659a c0659a4 = this.f779Fi;
                int i3 = message.arg2;
                c0659a4.f741Pk = i3;
                c0659a4.f742Qk = i3;
                int i4 = c0659a4.mRegion;
                C0659a c0659a5 = this.f779Fi;
                c0659a5.f744Sk = c0659a5.f736Kk;
                c0659a5.f745Tk = c0659a5.f735Jk;
                c0659a5.f746Uk = c0659a5.f741Pk;
                Iterator<Map.Entry<String, InterfaceC0666f>> it = this.f788yh.entrySet().iterator();
                while (it.hasNext()) {
                    InterfaceC0666f value = it.next().getValue();
                    C0659a c0659a6 = this.f779Fi;
                    value.mo858a(c0659a6.f736Kk, c0659a6.f735Jk, c0659a6.f741Pk, c0659a6.f749Xk, c0659a6.mRegion);
                }
                break;
            case 6:
                C0659a c0659a7 = this.f779Fi;
                c0659a7.f743Rk = message.arg2;
                if (c0659a7.f749Xk == 2) {
                    c0659a7.f744Sk = c0659a7.f740Ok;
                    c0659a7.f745Tk = c0659a7.f739Nk;
                    c0659a7.f746Uk = c0659a7.f743Rk;
                    Iterator<Map.Entry<String, InterfaceC0666f>> it2 = this.f788yh.entrySet().iterator();
                    while (it2.hasNext()) {
                        InterfaceC0666f value2 = it2.next().getValue();
                        C0659a c0659a8 = this.f779Fi;
                        value2.mo858a(c0659a8.f740Ok, c0659a8.f739Nk, c0659a8.f743Rk, c0659a8.f749Xk, c0659a8.mRegion);
                    }
                    break;
                }
                break;
            case 7:
                if (this.f779Fi.f750Yk != (message.arg2 != 0)) {
                    this.f779Fi.f750Yk = message.arg2 != 0;
                    m831D(this.f779Fi.f750Yk);
                    break;
                }
                break;
            case 8:
                this.f779Fi.f737Lk = message.arg2;
                break;
            case 9:
                this.f779Fi.f738Mk = message.arg2;
                break;
            case 10:
                C0659a c0659a9 = this.f779Fi;
                c0659a9.f742Qk = message.arg2;
                if (c0659a9.f749Xk == 1 && c0659a9.mRegion == 5) {
                    c0659a9.f744Sk = c0659a9.f738Mk;
                    c0659a9.f745Tk = c0659a9.f737Lk;
                    c0659a9.f746Uk = c0659a9.f742Qk;
                    Iterator<Map.Entry<String, InterfaceC0666f>> it3 = this.f788yh.entrySet().iterator();
                    while (it3.hasNext()) {
                        InterfaceC0666f value3 = it3.next().getValue();
                        C0659a c0659a10 = this.f779Fi;
                        value3.mo858a(c0659a10.f738Mk, c0659a10.f737Lk, c0659a10.f742Qk, c0659a10.f749Xk, c0659a10.mRegion);
                    }
                    break;
                }
                break;
        }
        Iterator<Map.Entry<String, InterfaceC0666f>> it4 = this.f788yh.entrySet().iterator();
        while (it4.hasNext()) {
            InterfaceC0666f value4 = it4.next().getValue();
            C0659a c0659a11 = this.f779Fi;
            value4.mo859a(c0659a11.f736Kk, c0659a11.f735Jk, c0659a11.f738Mk, c0659a11.f737Lk, c0659a11.f740Ok, c0659a11.f739Nk);
        }
    }

    /* renamed from: c */
    protected void m842c(Message message) {
        int i = message.arg2;
        if (i == 33 || i == 76) {
            m844cc();
        }
        if (i == 84) {
            C0659a c0659a = this.f779Fi;
            if (c0659a.f759il) {
                c0659a.f759il = false;
                Iterator<Map.Entry<String, InterfaceC0666f>> it = this.f788yh.entrySet().iterator();
                while (it.hasNext()) {
                    it.next().getValue().mo867k(this.f779Fi.f759il);
                }
                int i2 = 0;
                while (true) {
                    C0660a[] c0660aArr = this.f779Fi.f734Gi;
                    if (i2 >= c0660aArr.length) {
                        break;
                    }
                    c0660aArr[i2].f772vl = null;
                    Iterator<Map.Entry<String, InterfaceC0666f>> it2 = this.f788yh.entrySet().iterator();
                    while (it2.hasNext()) {
                        it2.next().getValue().mo872r(i2);
                    }
                    i2++;
                }
            }
            m841bc();
            this.f787wh.write(InputDeviceCompat.SOURCE_GAMEPAD, 0, 0);
            return;
        }
        if (i == 96) {
            if (this.f779Fi.f749Xk != 2) {
                this.f787wh.write(1028, 1, (this.f776Ci & 32) != 32 ? 1 : 0);
            }
        } else {
            if (i == 97) {
                if (this.f779Fi.f749Xk != 2) {
                    this.f787wh.write(1028, 0, (this.f776Ci & 64) == 64 ? 0 : 1);
                    return;
                }
                return;
            }
            switch (i) {
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                    int i3 = this.mActivity;
                    if (i3 == 1 || i3 == 129) {
                        Iterator<Map.Entry<String, InterfaceC0666f>> it3 = this.f788yh.entrySet().iterator();
                        while (it3.hasNext()) {
                            it3.next().getValue().mo869n(message.arg2 - 49);
                        }
                        break;
                    }
                    break;
            }
        }
    }

    /* renamed from: b */
    protected void m840b(Message message) {
        this.f779Fi.mSource = message.arg1;
    }

    /* renamed from: a */
    public void m836a(Context context) {
        this.mContext = context.getApplicationContext();
        this.f781Hi = new C0721b(this.mContext);
        this.f781Hi.m1143a(this.f783Ji);
    }

    /* renamed from: a */
    public void m838a(String str, InterfaceC0666f interfaceC0666f) {
        this.f788yh.put(str, interfaceC0666f);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0036 A[LOOP:0: B:15:0x0030->B:17:0x0036, LOOP_END] */
    /* renamed from: a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void m837a(Message message) {
        Iterator<Map.Entry<String, InterfaceC0666f>> it;
        int i = message.arg1;
        if (i == 0) {
            int i2 = this.f784if;
            int i3 = message.arg2;
            if (i2 != i3) {
                int i4 = i2 ^ i3;
                this.f784if = i3;
                if ((i4 & 2) == 2) {
                    Iterator<Map.Entry<String, InterfaceC0666f>> it2 = this.f788yh.entrySet().iterator();
                    while (it2.hasNext()) {
                        it2.next().getValue().mo873r((this.f784if & 2) == 2);
                    }
                }
                if ((i4 & 4) == 4) {
                    this.f779Fi.f764nl = (this.f784if & 4) == 4 ? 1 : 0;
                    Iterator<Map.Entry<String, InterfaceC0666f>> it3 = this.f788yh.entrySet().iterator();
                    while (it3.hasNext()) {
                        it3.next().getValue().mo879y((this.f784if & 4) == 4 ? 1 : 0);
                    }
                }
                if ((i4 & 8) == 8) {
                    this.f779Fi.f747Vk = (this.f784if & 8) == 8 ? 1 : 0;
                    Iterator<Map.Entry<String, InterfaceC0666f>> it4 = this.f788yh.entrySet().iterator();
                    while (it4.hasNext()) {
                        it4.next().getValue().mo857S((this.f784if & 8) == 8 ? 1 : 0);
                    }
                }
                if ((i4 & 16) == 16) {
                    this.f779Fi.f753cl = (this.f784if & 16) == 16;
                    Iterator<Map.Entry<String, InterfaceC0666f>> it5 = this.f788yh.entrySet().iterator();
                    while (it5.hasNext()) {
                        it5.next().getValue().mo868m((this.f784if & 16) == 16);
                    }
                }
                if ((i4 & 128) == 128 && (128 & this.f784if) == 0) {
                    C0659a c0659a = this.f779Fi;
                    int i5 = c0659a.f749Xk;
                    if (i5 != 2 && c0659a.mRegion != 5) {
                        i5 = 0;
                    }
                    m828n(0, i5);
                    Iterator<Map.Entry<String, InterfaceC0666f>> it6 = this.f788yh.entrySet().iterator();
                    while (it6.hasNext()) {
                        it6.next().getValue().mo874s(0);
                    }
                    return;
                }
                return;
            }
            return;
        }
        if (i != 1) {
            if (i == 2) {
                C0659a c0659a2 = this.f779Fi;
                c0659a2.f748Wk = message.arg2;
                Object obj = message.obj;
                if (obj instanceof String) {
                    c0659a2.f757gl = (String) obj;
                }
                if (this.f778Ei) {
                    this.f778Ei = false;
                    this.mHandler.sendEmptyMessageDelayed(65283, 0L);
                }
                for (Map.Entry<String, InterfaceC0666f> entry : this.f788yh.entrySet()) {
                    entry.getValue().mo856K(this.f779Fi.f748Wk);
                    entry.getValue().mo862ba((String) message.obj);
                }
                return;
            }
            if (i == 3) {
                this.f779Fi.f760jl = message.arg2;
                Iterator<Map.Entry<String, InterfaceC0666f>> it7 = this.f788yh.entrySet().iterator();
                while (it7.hasNext()) {
                    it7.next().getValue().mo853A(message.arg2);
                }
                return;
            }
            if (i != 4) {
                return;
            }
            int i6 = message.arg2;
            if (i6 >= 0) {
                C0659a c0659a3 = this.f779Fi;
                if (i6 < c0659a3.f734Gi.length) {
                    c0659a3.f762ll = i6;
                    it = this.f788yh.entrySet().iterator();
                    while (it.hasNext()) {
                        it.next().getValue().mo855I(this.f779Fi.f762ll);
                    }
                    return;
                }
            }
            this.f779Fi.f762ll = -1;
            it = this.f788yh.entrySet().iterator();
            while (it.hasNext()) {
            }
            return;
        }
        C0659a c0659a4 = this.f779Fi;
        int i7 = c0659a4.f749Xk;
        int i8 = message.arg2;
        if (i7 != i8) {
            c0659a4.f749Xk = i8;
            try {
                if (C0556b.getInstant().f455cd != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("dateType", "send");
                    bundle.putString("action", "com.tw.radio.av");
                    bundle.putInt("bandState", this.f779Fi.f749Xk);
                    C0556b.getInstant().f455cd.mo142a(bundle);
                }
            } catch (Exception e) {
                Log.e("RadioModel", "handleMessage: Error" + e.getMessage());
            }
            Iterator<Map.Entry<String, InterfaceC0666f>> it8 = this.f788yh.entrySet().iterator();
            while (it8.hasNext()) {
                it8.next().getValue().mo878x(this.f779Fi.f749Xk);
            }
            C0659a c0659a5 = this.f779Fi;
            c0659a5.f762ll = -1;
            int i9 = c0659a5.f749Xk;
            if (i9 == 2) {
                c0659a5.f744Sk = c0659a5.f740Ok;
                c0659a5.f745Tk = c0659a5.f739Nk;
                c0659a5.f746Uk = c0659a5.f743Rk;
                Iterator<Map.Entry<String, InterfaceC0666f>> it9 = this.f788yh.entrySet().iterator();
                while (it9.hasNext()) {
                    InterfaceC0666f value = it9.next().getValue();
                    C0659a c0659a6 = this.f779Fi;
                    value.mo858a(c0659a6.f740Ok, c0659a6.f739Nk, c0659a6.f743Rk, c0659a6.f749Xk, c0659a6.mRegion);
                }
            } else if (i9 == 0) {
                c0659a5.f744Sk = c0659a5.f736Kk;
                c0659a5.f745Tk = c0659a5.f735Jk;
                c0659a5.f746Uk = c0659a5.f741Pk;
                Iterator<Map.Entry<String, InterfaceC0666f>> it10 = this.f788yh.entrySet().iterator();
                while (it10.hasNext()) {
                    InterfaceC0666f value2 = it10.next().getValue();
                    C0659a c0659a7 = this.f779Fi;
                    value2.mo858a(c0659a7.f736Kk, c0659a7.f735Jk, c0659a7.f741Pk, c0659a7.f749Xk, c0659a7.mRegion);
                }
            } else if (i9 == 1) {
                c0659a5.f744Sk = c0659a5.f738Mk;
                c0659a5.f745Tk = c0659a5.f737Lk;
                c0659a5.f746Uk = c0659a5.f742Qk;
                Iterator<Map.Entry<String, InterfaceC0666f>> it11 = this.f788yh.entrySet().iterator();
                while (it11.hasNext()) {
                    InterfaceC0666f value3 = it11.next().getValue();
                    C0659a c0659a8 = this.f779Fi;
                    value3.mo858a(c0659a8.f738Mk, c0659a8.f737Lk, c0659a8.f742Qk, c0659a8.f749Xk, c0659a8.mRegion);
                }
            }
            C0659a c0659a9 = this.f779Fi;
            if (c0659a9.f749Xk == 2) {
                this.f776Ci = 0;
                c0659a9.f756fl = false;
                c0659a9.f754dl = false;
                c0659a9.f755el = false;
                for (Map.Entry<String, InterfaceC0666f> entry2 : this.f788yh.entrySet()) {
                    entry2.getValue().mo866i(false);
                    entry2.getValue().mo876t(false);
                    entry2.getValue().mo870p(false);
                }
            } else {
                this.f787wh.write(1028, 255);
            }
            Iterator<Map.Entry<String, InterfaceC0666f>> it12 = this.f788yh.entrySet().iterator();
            while (it12.hasNext()) {
                it12.next().getValue().mo856K(this.f779Fi.f748Wk);
            }
        }
    }
}
