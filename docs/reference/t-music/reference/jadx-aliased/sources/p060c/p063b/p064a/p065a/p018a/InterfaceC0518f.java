package p060c.p063b.p064a.p065a.p018a;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* compiled from: IVideoCallBack.java */
/* renamed from: c.b.a.a.a.f */
/* loaded from: classes4.dex */
public interface InterfaceC0518f extends IInterface {

    /* compiled from: IVideoCallBack.java */
    /* renamed from: c.b.a.a.a.f$a */
    public static abstract class a extends Binder implements InterfaceC0518f {
        public a() {
            attachInterface(this, "com.tw.service.xt.aidl.IVideoCallBack");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1) {
                parcel.enforceInterface("com.tw.service.xt.aidl.IVideoCallBack");
                mo163J();
                parcel2.writeNoException();
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface("com.tw.service.xt.aidl.IVideoCallBack");
                mo166ha();
                parcel2.writeNoException();
                return true;
            }
            if (i == 3) {
                parcel.enforceInterface("com.tw.service.xt.aidl.IVideoCallBack");
                mo167ma();
                parcel2.writeNoException();
                return true;
            }
            if (i == 4) {
                parcel.enforceInterface("com.tw.service.xt.aidl.IVideoCallBack");
                mo164P();
                parcel2.writeNoException();
                return true;
            }
            if (i != 5) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString("com.tw.service.xt.aidl.IVideoCallBack");
                return true;
            }
            parcel.enforceInterface("com.tw.service.xt.aidl.IVideoCallBack");
            mo165a(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
            parcel2.writeNoException();
            return true;
        }
    }

    /* renamed from: J */
    void mo163J();

    /* renamed from: P */
    void mo164P();

    /* renamed from: a */
    void mo165a(Bundle bundle);

    /* renamed from: ha */
    void mo166ha();

    /* renamed from: ma */
    void mo167ma();
}
