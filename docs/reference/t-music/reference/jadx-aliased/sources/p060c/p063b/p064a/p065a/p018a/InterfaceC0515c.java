package p060c.p063b.p064a.p065a.p018a;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* compiled from: IRadioCallBack.java */
/* renamed from: c.b.a.a.a.c */
/* loaded from: classes4.dex */
public interface InterfaceC0515c extends IInterface {

    /* compiled from: IRadioCallBack.java */
    /* renamed from: c.b.a.a.a.c$a */
    public static abstract class a extends Binder implements InterfaceC0515c {
        public a() {
            attachInterface(this, "com.tw.service.xt.aidl.IRadioCallBack");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1598968902) {
                parcel2.writeString("com.tw.service.xt.aidl.IRadioCallBack");
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IRadioCallBack");
                    mo140oa();
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IRadioCallBack");
                    mo133K();
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IRadioCallBack");
                    mo134R();
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IRadioCallBack");
                    mo135U();
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IRadioCallBack");
                    mo137aa(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IRadioCallBack");
                    mo139o(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IRadioCallBack");
                    mo138ka();
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface("com.tw.service.xt.aidl.IRadioCallBack");
                    mo136a(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    /* renamed from: K */
    void mo133K();

    /* renamed from: R */
    void mo134R();

    /* renamed from: U */
    void mo135U();

    /* renamed from: a */
    void mo136a(Bundle bundle);

    /* renamed from: aa */
    void mo137aa(String str);

    /* renamed from: ka */
    void mo138ka();

    /* renamed from: o */
    void mo139o(int i);

    /* renamed from: oa */
    void mo140oa();
}
