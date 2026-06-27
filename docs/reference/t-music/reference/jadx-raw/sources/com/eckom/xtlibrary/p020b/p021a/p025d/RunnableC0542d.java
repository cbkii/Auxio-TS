package com.eckom.xtlibrary.p020b.p021a.p025d;

import android.content.ContentResolver;
import android.provider.ContactsContract;
import android.util.Log;

/* compiled from: BTModel.java */
/* renamed from: com.eckom.xtlibrary.b.a.d.d */
/* loaded from: classes3.dex */
class RunnableC0542d implements Runnable {
    final /* synthetic */ C0544f this$0;

    RunnableC0542d(C0544f c0544f) {
        this.this$0 = c0544f;
    }

    @Override // java.lang.Runnable
    public void run() {
        Log.d("BTModel", "delete contacts from system database start");
        ContentResolver contentResolver = this.this$0.mContext.getContentResolver();
        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, null, null);
        contentResolver.delete(ContactsContract.Data.CONTENT_URI, null, null);
        Log.d("BTModel", "delete contacts from system database end");
    }
}
