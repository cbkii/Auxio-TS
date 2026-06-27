package p011c.p015b.p016a.p017a.p018a;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: renamed from: c.b.a.a.a.a */
/* JADX INFO: compiled from: IBTCallBack.java */
/* JADX INFO: loaded from: classes4.dex */
public interface InterfaceC0513a extends IInterface {

    /* JADX INFO: renamed from: c.b.a.a.a.a$a */
    /* JADX INFO: compiled from: IBTCallBack.java */
    public static abstract class a extends Binder implements InterfaceC0513a {
        public a() {
            attachInterface(this, "com.tw.service.xt.aidl.IBTCallBack");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1598968902) {
                parcel2.writeString("com.tw.service.xt.aidl.IBTCallBack");
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo123ea(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo117H();
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo126la();
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo118Z();
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo124ga();
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo122da();
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo120aa();
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo125ja();
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo121ca();
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IBTCallBack");
                    mo119a(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    /* JADX INFO: renamed from: H */
    void mo117H();

    /* JADX INFO: renamed from: Z */
    void mo118Z();

    /* JADX INFO: renamed from: a */
    void mo119a(Bundle bundle);

    /* JADX INFO: renamed from: aa */
    void mo120aa();

    /* JADX INFO: renamed from: ca */
    void mo121ca();

    /* JADX INFO: renamed from: da */
    void mo122da();

    /* JADX INFO: renamed from: ea */
    void mo123ea(String str);

    /* JADX INFO: renamed from: ga */
    void mo124ga();

    /* JADX INFO: renamed from: ja */
    void mo125ja();

    /* JADX INFO: renamed from: la */
    void mo126la();
}
