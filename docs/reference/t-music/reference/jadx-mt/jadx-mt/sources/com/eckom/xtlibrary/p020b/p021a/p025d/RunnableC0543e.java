package com.eckom.xtlibrary.p020b.p021a.p025d;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p053j.C0699o;
import com.eckom.xtlibrary.twproject.p059bt.bean.TWContact;
import java.util.ArrayList;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.d.e */
/* JADX INFO: compiled from: BTModel.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0543e implements Runnable {
    final /* synthetic */ C0544f this$0;

    RunnableC0543e(C0544f c0544f) {
        this.this$0 = c0544f;
    }

    @Override // java.lang.Runnable
    public void run() {
        String str;
        ContentResolver contentResolver;
        int i;
        int i2;
        int i3;
        long j;
        String str2;
        String str3;
        int i4;
        String str4;
        int size;
        int i5;
        int size2;
        this.this$0.mContext.sendBroadcast(new Intent("com.tw.bt.startaddContact"));
        String str5 = "BTModel";
        Log.d("BTModel", "addContactToSystemDatabase: start:contacts count:" + this.this$0.f425la.f392fh.size());
        long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            contentResolver = this.this$0.mContext.getContentResolver();
            int size3 = this.this$0.f425la.f392fh.size();
            i = size3 / IjkMediaCodecInfo.RANK_SECURE;
            i2 = size3 % IjkMediaCodecInfo.RANK_SECURE;
            i3 = 0;
        } catch (Exception e) {
            e = e;
            str = str5;
        }
        while (true) {
            int i6 = i - 1;
            j = jCurrentTimeMillis;
            str = "raw_contact_id";
            if (i <= 0) {
                break;
            }
            ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
            int i7 = i3;
            int i8 = IjkMediaCodecInfo.RANK_SECURE;
            while (true) {
                int i9 = i8 - 1;
                if (i8 <= 0) {
                    break;
                }
                int i10 = i7 + 1;
                TWContact tWContact = this.this$0.f425la.f392fh.get(i7);
                try {
                    size2 = arrayList.size();
                    i5 = i2;
                    try {
                        str3 = str5;
                    } catch (Exception e2) {
                        e = e2;
                        str3 = str5;
                    }
                } catch (Exception e3) {
                    e = e3;
                    str3 = str5;
                    i5 = i2;
                }
                try {
                    arrayList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue("account_type", null).withValue("account_name", null).withYieldAllowed(true).build());
                    String contactName = tWContact.getContactName();
                    if (contactName != null && contactName.length() > 0) {
                        arrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", size2).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data1", contactName).withYieldAllowed(true).build());
                    }
                    String strM1131eb = tWContact.m1131eb();
                    if (strM1131eb != null && strM1131eb.length() > 0) {
                        arrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", size2).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", strM1131eb).withValue("data2", this.this$0.f425la.f409yg).withYieldAllowed(true).build());
                    }
                } catch (Exception e4) {
                    e = e4;
                    try {
                        e.printStackTrace();
                    } catch (Exception e5) {
                        e = e5;
                        str = str3;
                    }
                }
                i8 = i9;
                i7 = i10;
                str5 = str3;
                i2 = i5;
                e = e5;
                str = str3;
            }
            str3 = str5;
            int i11 = i2;
            try {
                contentResolver.applyBatch("com.android.contacts", arrayList);
                str2 = str3;
            } catch (Exception e6) {
                str2 = str3;
                try {
                    Log.e(str2, "addContactToSystemDatabase: " + e6.getMessage());
                    e6.printStackTrace();
                } catch (Exception e7) {
                    e = e7;
                    str = str2;
                }
            }
            i3 = i7;
            str5 = str2;
            i = i6;
            jCurrentTimeMillis = j;
            i2 = i11;
            Log.e(str, "addContactToSystemDatabase: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        str2 = str5;
        int i12 = i2;
        try {
            if (i12 > 0) {
                ArrayList<ContentProviderOperation> arrayList2 = new ArrayList<>();
                while (true) {
                    int i13 = i12 - 1;
                    if (i12 <= 0) {
                        break;
                    }
                    int i14 = i3 + 1;
                    TWContact tWContact2 = this.this$0.f425la.f392fh.get(i3);
                    try {
                        size = arrayList2.size();
                        i4 = i13;
                    } catch (Exception e8) {
                        e = e8;
                        i4 = i13;
                    }
                    try {
                        str4 = str2;
                    } catch (Exception e9) {
                        e = e9;
                        str4 = str2;
                        e.printStackTrace();
                        i3 = i14;
                        i12 = i4;
                        str2 = str4;
                    }
                    try {
                        try {
                            arrayList2.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue("account_type", null).withValue("account_name", null).withYieldAllowed(true).build());
                            String contactName2 = tWContact2.getContactName();
                            if (contactName2 != null && contactName2.length() > 0) {
                                arrayList2.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", size).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data1", contactName2).withYieldAllowed(true).build());
                            }
                            String strM1131eb2 = tWContact2.m1131eb();
                            if (strM1131eb2 != null && strM1131eb2.length() > 0) {
                                try {
                                    arrayList2.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference("raw_contact_id", size).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", strM1131eb2).withValue("data2", this.this$0.f425la.f409yg).withYieldAllowed(true).build());
                                } catch (Exception e10) {
                                    e = e10;
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e11) {
                            e = e11;
                        }
                    } catch (Exception e12) {
                        e = e12;
                        e.printStackTrace();
                        i3 = i14;
                        i12 = i4;
                        str2 = str4;
                    }
                    i3 = i14;
                    i12 = i4;
                    str2 = str4;
                }
                str3 = str2;
                try {
                    contentResolver.applyBatch("com.android.contacts", arrayList2);
                    str = str3;
                } catch (Exception e13) {
                    str = str3;
                    Log.e(str, "addContactToSystemDatabase: " + e13.getMessage());
                    e13.printStackTrace();
                }
            } else {
                str = str2;
            }
            Log.d(str, "addContactToSystemDatabase:completed");
            long jCurrentTimeMillis2 = System.currentTimeMillis();
            this.this$0.mContext.sendBroadcast(new Intent("com.tw.bt.endaddContact"));
            C0699o.m1028a(this.this$0.mContext, str, this.this$0.f425la.f409yg, false);
            Log.e("tssDebug", "共耗时：" + (jCurrentTimeMillis2 - j));
        } catch (Exception e14) {
            e = e14;
        }
    }
}
