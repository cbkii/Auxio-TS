package p060c.p063b.p064a.p065a.p018a;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* compiled from: IMusicCallBack.java */
/* renamed from: c.b.a.a.a.b */
/* loaded from: classes4.dex */
public interface InterfaceC0514b extends IInterface {

    /* compiled from: IMusicCallBack.java */
    /* renamed from: c.b.a.a.a.b$a */
    public static abstract class a extends Binder implements InterfaceC0514b {
        public a() {
            attachInterface(this, "com.tw.service.xt.aidl.IMusicCallBack");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1598968902) {
                parcel2.writeString("com.tw.service.xt.aidl.IMusicCallBack");
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IMusicCallBack");
                    mo131na();
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IMusicCallBack");
                    mo129ea();
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IMusicCallBack");
                    mo130fa();
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IMusicCallBack");
                    mo128ba();
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IMusicCallBack");
                    mo132z(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IMusicCallBack");
                    mo127a(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    /* renamed from: a */
    void mo127a(Bundle bundle);

    /* renamed from: ba */
    void mo128ba();

    /* renamed from: ea */
    void mo129ea();

    /* renamed from: fa */
    void mo130fa();

    /* renamed from: na */
    void mo131na();

    /* renamed from: z */
    void mo132z(int i);
}
