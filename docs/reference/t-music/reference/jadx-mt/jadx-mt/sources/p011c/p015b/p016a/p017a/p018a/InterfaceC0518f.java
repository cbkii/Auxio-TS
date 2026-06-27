package p011c.p015b.p016a.p017a.p018a;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: renamed from: c.b.a.a.a.f */
/* JADX INFO: compiled from: IVideoCallBack.java */
/* JADX INFO: loaded from: classes4.dex */
public interface InterfaceC0518f extends IInterface {

    /* JADX INFO: renamed from: c.b.a.a.a.f$a */
    /* JADX INFO: compiled from: IVideoCallBack.java */
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

    /* JADX INFO: renamed from: J */
    void mo163J();

    /* JADX INFO: renamed from: P */
    void mo164P();

    /* JADX INFO: renamed from: a */
    void mo165a(Bundle bundle);

    /* JADX INFO: renamed from: ha */
    void mo166ha();

    /* JADX INFO: renamed from: ma */
    void mo167ma();
}
