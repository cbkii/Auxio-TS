package com.eckom.xtlibrary.p020b.p021a.p024c;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0534b;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.c.a */
/* JADX INFO: compiled from: ContactDBHelper.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0536a extends AbstractC0538c {

    /* JADX INFO: renamed from: Nc */
    private String f413Nc;

    public C0536a(Context context, String str) {
        super(context, "contact.db", null, 1);
        this.f413Nc = "contact";
        this.f413Nc = str;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        String str = "CREATE table '" + this.f413Nc + "' (_id INTEGER PRIMARY KEY AUTOINCREMENT,contactName text,contactNumber text,contactPin text,favorite integer)";
        C0534b.m201d("ContactDBHelper", "onCreate:" + str);
        sQLiteDatabase.execSQL(str);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        super.onOpen(sQLiteDatabase);
    }
}
