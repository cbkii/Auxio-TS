package com.eckom.xtlibrary.twproject.radio.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Locale;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.radio.utils.b */
/* JADX INFO: compiled from: DBHelper.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0721b extends SQLiteOpenHelper {

    /* JADX INFO: renamed from: Oc */
    String f901Oc;

    /* JADX INFO: renamed from: Pc */
    String f902Pc;

    /* JADX INFO: renamed from: Qc */
    private a f903Qc;
    private Context context;
    int location;

    /* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.radio.utils.b$a */
    /* JADX INFO: compiled from: DBHelper.java */
    public interface a {
        /* JADX INFO: renamed from: O */
        void mo809O();

        /* JADX INFO: renamed from: Q */
        void mo810Q();
    }

    public C0721b(Context context) {
        super(context, "radiologo_data.db", (SQLiteDatabase.CursorFactory) null, 1);
        this.f901Oc = "CREATE TABLE radiologo (pi TEXT, freq TEXT, icon TEXT)";
        this.f902Pc = "";
        this.location = 0;
        Log.d("DBHelper", "DBHelper: ");
        C0529b.m178a(Integer.valueOf(this.location));
        this.context = context;
    }

    /* JADX INFO: renamed from: Pa */
    public void m1141Pa() {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            writableDatabase.execSQL("delete from 'radiologo'");
            writableDatabase.close();
        } catch (Exception e) {
            Log.e("DBHelper", "clearTable:" + e.getMessage());
        }
    }

    /* JADX INFO: renamed from: ba */
    public void m1144ba(int i) {
        this.location = i;
        m1141Pa();
        if (i != 3) {
            this.f902Pc = "radio_data.txt";
        } else {
            this.f902Pc = "radio_data_dny.txt";
        }
    }

    /* JADX INFO: renamed from: h */
    public String[] m1145h(String str, int i) {
        String[] strArr = {"0000", "0000"};
        try {
            if (TextUtils.isEmpty(str)) {
                return strArr;
            }
            SQLiteDatabase writableDatabase = getWritableDatabase();
            int i2 = 1;
            Cursor cursorQuery = writableDatabase.query("radiologo", new String[]{"freq", "icon"}, "pi = ?", new String[]{str.toUpperCase(Locale.ROOT)}, null, null, null);
            String str2 = null;
            boolean z = false;
            while (true) {
                if (cursorQuery.isClosed() || cursorQuery.getCount() <= 0 || !cursorQuery.moveToNext()) {
                    break;
                }
                String string = cursorQuery.getString(0);
                String string2 = cursorQuery.getString(i2);
                Log.d("DBHelper", "getDataByCol1: " + str + "," + string + "," + i);
                int i3 = !TextUtils.isEmpty(string) ? Integer.parseInt(string.trim()) : 0;
                if (str2 == null) {
                    str2 = string2;
                }
                if (!str2.equals(string2)) {
                    Log.e("DBHelper", "getDataByCol1: " + str + "  " + str2 + "  " + string2);
                    z = true;
                }
                Log.e("DBHelper", "getDataByCol2: " + str + "  " + i + "  " + i3 + "  " + str2 + "  " + string2);
                if (i == i3 / 10) {
                    strArr[0] = string;
                    strArr[1] = string2;
                    break;
                }
                i2 = 1;
            }
            if (TextUtils.equals(strArr[0], "0000") && !z && str2 != null) {
                strArr[1] = str2;
            }
            cursorQuery.close();
            writableDatabase.close();
            return strArr;
        } catch (Exception e) {
            e.printStackTrace();
            return strArr;
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.d("DBHelper", "onCreate: ");
        sQLiteDatabase.execSQL(this.f901Oc);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.d("DBHelper", "onUpgrade: ");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS radiologo");
        sQLiteDatabase.execSQL(this.f901Oc);
    }

    /* JADX INFO: renamed from: a */
    public void m1142a(SQLiteDatabase sQLiteDatabase) {
        try {
            String strM998Sc = C0686b.m998Sc();
            String str = "/sdcard/iNand/radio/" + this.f902Pc;
            Log.d("DBHelper", "insertDataFromFile: ");
            if (!new File(str).exists()) {
                str = strM998Sc + this.f902Pc;
                if (!new File(str).exists()) {
                    str = strM998Sc + "radio/" + this.f902Pc;
                }
            }
            File file = new File(str);
            Log.d("DBHelper", "insertDataFromFile: piPath.exists=" + file.exists());
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            C0529b.m181e("  11111111  ");
            AsyncTask.execute(new RunnableC0720a(this, sQLiteDatabase, new String[1], bufferedReader, file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: a */
    public void m1143a(a aVar) {
        this.f903Qc = aVar;
    }
}
