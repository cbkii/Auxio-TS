package com.eckom.xtlibrary.p066b.p067a.p024c;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.eckom.xtlibrary.p066b.p067a.p023b.C0534b;
import com.eckom.xtlibrary.twproject.p072bt.bean.TWContact;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: ContactDBManager.java */
/* renamed from: com.eckom.xtlibrary.b.a.c.b */
/* loaded from: classes3.dex */
public class C0537b {
    private static C0537b instance = null;

    /* renamed from: oh */
    private static SQLiteOpenHelper f414oh = null;

    /* renamed from: ph */
    private static String f415ph = "contact";

    /* renamed from: yg */
    private static String f416yg = "";

    /* renamed from: mh */
    private AtomicInteger f417mh = new AtomicInteger();

    /* renamed from: nh */
    private SQLiteDatabase f418nh;

    /* renamed from: a */
    public static synchronized C0537b m207a(Context context, String str) {
        C0537b c0537b;
        synchronized (C0537b.class) {
            if (instance == null) {
                instance = new C0537b();
            }
            C0534b.m201d("ContactDBManager", "ContactDBManager:currentMac:" + str + " mCurrentMac:" + f416yg);
            if (!TextUtils.equals(f416yg, str)) {
                if (!TextUtils.isEmpty(str)) {
                    try {
                        f414oh = new C0536a(context, str);
                        m208yb(str);
                    } catch (Exception e) {
                        C0534b.m202e("ContactDBManager", "getInstance:" + e.getMessage());
                    }
                }
                f416yg = str;
                f415ph = f416yg;
            }
            c0537b = instance;
        }
        return c0537b;
    }

    /* renamed from: yb */
    private static void m208yb(String str) {
        String str2 = "CREATE table '" + str + "' (_id INTEGER PRIMARY KEY AUTOINCREMENT,contactName text,contactNumber text,contactPin text,favorite integer)";
        C0534b.m201d("ContactDBManager", "onCreate:" + str2);
        f414oh.getWritableDatabase().execSQL(str2);
    }

    /* renamed from: ib */
    public synchronized void m209ib() {
        if (this.f417mh.decrementAndGet() == 0) {
            this.f418nh.close();
        }
    }

    /* renamed from: jb */
    public ArrayList<TWContact> m210jb() {
        ArrayList<TWContact> arrayList = new ArrayList<>();
        try {
            Cursor rawQuery = m212lb().rawQuery("select * from '" + f415ph + "' order by _id asc", new String[0]);
            while (rawQuery.moveToNext()) {
                TWContact tWContact = new TWContact();
                tWContact.setId(rawQuery.getInt(rawQuery.getColumnIndex("_id")));
                tWContact.m1132wa(rawQuery.getString(rawQuery.getColumnIndex("contactName")));
                tWContact.m1133xa(rawQuery.getString(rawQuery.getColumnIndex("contactNumber")));
                tWContact.m1134ya(rawQuery.getString(rawQuery.getColumnIndex("contactPin")));
                tWContact.m1130A(rawQuery.getInt(rawQuery.getColumnIndex("favorite")) != 0);
                arrayList.add(tWContact);
            }
            rawQuery.close();
            m209ib();
        } catch (Exception e) {
            C0534b.m202e("ContactDBManager", "loadContactByAll:" + e.getMessage());
        }
        return arrayList;
    }

    /* renamed from: kb */
    public ArrayList<TWContact> m211kb() {
        ArrayList<TWContact> arrayList = new ArrayList<>();
        try {
            Cursor rawQuery = m212lb().rawQuery("select * from '" + f415ph + "' where favorite != 0", new String[0]);
            while (rawQuery.moveToNext()) {
                TWContact tWContact = new TWContact();
                tWContact.setId(rawQuery.getInt(rawQuery.getColumnIndex("_id")));
                tWContact.m1132wa(rawQuery.getString(rawQuery.getColumnIndex("contactName")));
                tWContact.m1133xa(rawQuery.getString(rawQuery.getColumnIndex("contactNumber")));
                tWContact.m1134ya(rawQuery.getString(rawQuery.getColumnIndex("contactPin")));
                tWContact.m1130A(rawQuery.getInt(rawQuery.getColumnIndex("favorite")) != 0);
                arrayList.add(tWContact);
            }
            rawQuery.close();
            m209ib();
        } catch (Exception e) {
            try {
                C0534b.m202e("ContactDBManager", "loadContactByFavorite:" + e);
            } catch (Exception e2) {
                C0534b.m202e("ContactDBManager", "loadContactByFavorite:" + e2.getMessage());
            }
        }
        return arrayList;
    }

    /* renamed from: lb */
    public synchronized SQLiteDatabase m212lb() {
        if (this.f417mh.incrementAndGet() == 1) {
            this.f418nh = f414oh.getWritableDatabase();
        }
        return this.f418nh;
    }
}
