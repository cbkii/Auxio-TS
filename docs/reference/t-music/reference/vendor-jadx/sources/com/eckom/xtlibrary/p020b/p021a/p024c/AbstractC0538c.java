package com.eckom.xtlibrary.p020b.p021a.p024c;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: DBHelper.java */
/* renamed from: com.eckom.xtlibrary.b.a.c.c */
/* loaded from: classes3.dex */
public abstract class AbstractC0538c extends SQLiteOpenHelper {
    public AbstractC0538c(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
