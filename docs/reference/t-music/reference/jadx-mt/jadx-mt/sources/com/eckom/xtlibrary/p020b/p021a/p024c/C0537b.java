package com.eckom.xtlibrary.p020b.p021a.p024c;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0534b;
import com.eckom.xtlibrary.twproject.p059bt.bean.TWContact;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.c.b */
/* JADX INFO: compiled from: ContactDBManager.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0537b {
    private static C0537b instance = null;

    /* JADX INFO: renamed from: oh */
    private static SQLiteOpenHelper f414oh = null;

    /* JADX INFO: renamed from: ph */
    private static String f415ph = "contact";

    /* JADX INFO: renamed from: yg */
    private static String f416yg = "";

    /* JADX INFO: renamed from: mh */
    private AtomicInteger f417mh = new AtomicInteger();

    /* JADX INFO: renamed from: nh */
    private SQLiteDatabase f418nh;

    /* JADX INFO: renamed from: a */
    public static synchronized C0537b m207a(Context context, String str) {
        if (instance == null) {
            instance = new C0537b();
        }
        C0534b.m201d("ContactDBManager", "ContactDBManager:currentMac:" + str + " mCurrentMac:" + f416yg);
        if (TextUtils.equals(f416yg, str)) {
        } else {
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
        return instance;
    }

    /* JADX INFO: renamed from: yb */
    private static void m208yb(String str) {
        String str2 = "CREATE table '" + str + "' (_id INTEGER PRIMARY KEY AUTOINCREMENT,contactName text,contactNumber text,contactPin text,favorite integer)";
        C0534b.m201d("ContactDBManager", "onCreate:" + str2);
        f414oh.getWritableDatabase().execSQL(str2);
    }

    /* JADX INFO: renamed from: ib */
    public synchronized void m209ib() {
        if (this.f417mh.decrementAndGet() == 0) {
            this.f418nh.close();
        }
    }

    /* JADX INFO: renamed from: jb */
    public ArrayList<TWContact> m210jb() {
        ArrayList<TWContact> arrayList = new ArrayList<>();
        try {
            Cursor cursorRawQuery = m212lb().rawQuery("select * from '" + f415ph + "' order by _id asc", new String[0]);
            while (cursorRawQuery.moveToNext()) {
                TWContact tWContact = new TWContact();
                tWContact.setId(cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("_id")));
                tWContact.m1132wa(cursorRawQuery.getString(cursorRawQuery.getColumnIndex("contactName")));
                tWContact.m1133xa(cursorRawQuery.getString(cursorRawQuery.getColumnIndex("contactNumber")));
                tWContact.m1134ya(cursorRawQuery.getString(cursorRawQuery.getColumnIndex("contactPin")));
                tWContact.m1130A(cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("favorite")) != 0);
                arrayList.add(tWContact);
            }
            cursorRawQuery.close();
            m209ib();
        } catch (Exception e) {
            C0534b.m202e("ContactDBManager", "loadContactByAll:" + e.getMessage());
        }
        return arrayList;
    }

    /* JADX INFO: renamed from: kb */
    public ArrayList<TWContact> m211kb() {
        ArrayList<TWContact> arrayList = new ArrayList<>();
        try {
            Cursor cursorRawQuery = m212lb().rawQuery("select * from '" + f415ph + "' where favorite != 0", new String[0]);
            while (cursorRawQuery.moveToNext()) {
                TWContact tWContact = new TWContact();
                tWContact.setId(cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("_id")));
                tWContact.m1132wa(cursorRawQuery.getString(cursorRawQuery.getColumnIndex("contactName")));
                tWContact.m1133xa(cursorRawQuery.getString(cursorRawQuery.getColumnIndex("contactNumber")));
                tWContact.m1134ya(cursorRawQuery.getString(cursorRawQuery.getColumnIndex("contactPin")));
                tWContact.m1130A(cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("favorite")) != 0);
                arrayList.add(tWContact);
            }
            cursorRawQuery.close();
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

    /* JADX INFO: renamed from: lb */
    public synchronized SQLiteDatabase m212lb() {
        if (this.f417mh.incrementAndGet() == 1) {
            this.f418nh = f414oh.getWritableDatabase();
        }
        return this.f418nh;
    }
}
