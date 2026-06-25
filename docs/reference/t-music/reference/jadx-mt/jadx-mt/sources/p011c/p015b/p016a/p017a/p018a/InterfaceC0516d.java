package p011c.p015b.p016a.p017a.p018a;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: renamed from: c.b.a.a.a.d */
/* JADX INFO: compiled from: ITWCommandAidl.java */
/* JADX INFO: loaded from: classes4.dex */
public interface InterfaceC0516d extends IInterface {
    /* JADX INFO: renamed from: W */
    void mo141W(int i);

    /* JADX INFO: renamed from: a */
    void mo142a(Bundle bundle);

    /* JADX INFO: renamed from: a */
    void mo143a(InterfaceC0513a interfaceC0513a);

    /* JADX INFO: renamed from: a */
    void mo144a(InterfaceC0514b interfaceC0514b);

    /* JADX INFO: renamed from: a */
    void mo145a(InterfaceC0515c interfaceC0515c);

    /* JADX INFO: renamed from: a */
    void mo146a(InterfaceC0517e interfaceC0517e);

    /* JADX INFO: renamed from: a */
    void mo147a(InterfaceC0518f interfaceC0518f);

    /* JADX INFO: renamed from: b */
    void mo148b(InterfaceC0513a interfaceC0513a);

    /* JADX INFO: renamed from: b */
    void mo149b(InterfaceC0514b interfaceC0514b);

    /* JADX INFO: renamed from: b */
    void mo150b(InterfaceC0515c interfaceC0515c);

    /* JADX INFO: renamed from: b */
    void mo151b(InterfaceC0517e interfaceC0517e);

    /* JADX INFO: renamed from: b */
    void mo152b(InterfaceC0518f interfaceC0518f);

    /* JADX INFO: renamed from: d */
    void mo153d(int i, String str, String str2);

    /* JADX INFO: renamed from: m */
    void mo154m(int i);

    /* JADX INFO: renamed from: c.b.a.a.a.d$a */
    /* JADX INFO: compiled from: ITWCommandAidl.java */
    public static abstract class a extends Binder implements InterfaceC0516d {
        public static InterfaceC0516d asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.tw.service.xt.aidl.ITWCommandAidl");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof InterfaceC0516d)) ? new C0864a(iBinder) : (InterfaceC0516d) iInterfaceQueryLocalInterface;
        }

        public static InterfaceC0516d getDefaultImpl() {
            return C0864a.sDefaultImpl;
        }

        /* JADX INFO: renamed from: c.b.a.a.a.d$a$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: ITWCommandAidl.java */
        private static class C0864a implements InterfaceC0516d {
            public static InterfaceC0516d sDefaultImpl;
            private IBinder mRemote;

            C0864a(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: W */
            public void mo141W(int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeInt(i);
                    if (this.mRemote.transact(27, parcelObtain, parcelObtain2, 0) || a.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        a.getDefaultImpl().mo141W(i);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: a */
            public void mo146a(InterfaceC0517e interfaceC0517e) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0517e != null ? interfaceC0517e.asBinder() : null);
                    if (this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) || a.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        a.getDefaultImpl().mo146a(interfaceC0517e);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: b */
            public void mo151b(InterfaceC0517e interfaceC0517e) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0517e != null ? interfaceC0517e.asBinder() : null);
                    if (this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) || a.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        a.getDefaultImpl().mo151b(interfaceC0517e);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: d */
            public void mo153d(int i, String str, String str2) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    if (this.mRemote.transact(30, parcelObtain, parcelObtain2, 0) || a.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        a.getDefaultImpl().mo153d(i, str, str2);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: m */
            public void mo154m(int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeInt(i);
                    if (this.mRemote.transact(29, parcelObtain, parcelObtain2, 0) || a.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        a.getDefaultImpl().mo154m(i);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: a */
            public void mo145a(InterfaceC0515c interfaceC0515c) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0515c != null ? interfaceC0515c.asBinder() : null);
                    if (!this.mRemote.transact(4, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo145a(interfaceC0515c);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: b */
            public void mo150b(InterfaceC0515c interfaceC0515c) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0515c != null ? interfaceC0515c.asBinder() : null);
                    if (!this.mRemote.transact(3, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo150b(interfaceC0515c);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: a */
            public void mo144a(InterfaceC0514b interfaceC0514b) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0514b != null ? interfaceC0514b.asBinder() : null);
                    if (!this.mRemote.transact(5, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo144a(interfaceC0514b);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: b */
            public void mo149b(InterfaceC0514b interfaceC0514b) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0514b != null ? interfaceC0514b.asBinder() : null);
                    if (!this.mRemote.transact(6, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo149b(interfaceC0514b);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: a */
            public void mo147a(InterfaceC0518f interfaceC0518f) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0518f != null ? interfaceC0518f.asBinder() : null);
                    if (!this.mRemote.transact(8, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo147a(interfaceC0518f);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: b */
            public void mo152b(InterfaceC0518f interfaceC0518f) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0518f != null ? interfaceC0518f.asBinder() : null);
                    if (!this.mRemote.transact(7, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo152b(interfaceC0518f);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: a */
            public void mo143a(InterfaceC0513a interfaceC0513a) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0513a != null ? interfaceC0513a.asBinder() : null);
                    if (!this.mRemote.transact(10, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo143a(interfaceC0513a);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: b */
            public void mo148b(InterfaceC0513a interfaceC0513a) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    parcelObtain.writeStrongBinder(interfaceC0513a != null ? interfaceC0513a.asBinder() : null);
                    if (!this.mRemote.transact(9, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo148b(interfaceC0513a);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0516d
            /* JADX INFO: renamed from: a */
            public void mo142a(Bundle bundle) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.tw.service.xt.aidl.ITWCommandAidl");
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(67, parcelObtain, parcelObtain2, 0) && a.getDefaultImpl() != null) {
                        a.getDefaultImpl().mo142a(bundle);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
