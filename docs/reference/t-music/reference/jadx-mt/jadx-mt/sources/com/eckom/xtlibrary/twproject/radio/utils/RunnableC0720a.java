package com.eckom.xtlibrary.twproject.radio.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p020b.p053j.C0699o;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.radio.utils.a */
/* JADX INFO: compiled from: DBHelper.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0720a implements Runnable {

    /* JADX INFO: renamed from: Al */
    final /* synthetic */ BufferedReader f897Al;

    /* JADX INFO: renamed from: Bl */
    final /* synthetic */ File f898Bl;
    final /* synthetic */ C0721b this$0;

    /* JADX INFO: renamed from: yl */
    final /* synthetic */ SQLiteDatabase f899yl;

    /* JADX INFO: renamed from: zl */
    final /* synthetic */ String[] f900zl;

    RunnableC0720a(C0721b c0721b, SQLiteDatabase sQLiteDatabase, String[] strArr, BufferedReader bufferedReader, File file) {
        this.this$0 = c0721b;
        this.f899yl = sQLiteDatabase;
        this.f900zl = strArr;
        this.f897Al = bufferedReader;
        this.f898Bl = file;
    }

    @Override // java.lang.Runnable
    public void run() {
        SQLiteStatement sQLiteStatementCompileStatement;
        this.f899yl.beginTransaction();
        try {
            try {
                try {
                    sQLiteStatementCompileStatement = this.f899yl.compileStatement("INSERT INTO radiologo (pi, freq, icon) VALUES (?, ?, ?)");
                } catch (IOException e) {
                    e.printStackTrace();
                    if (this.this$0.f903Qc != null) {
                        this.this$0.f903Qc.mo810Q();
                    }
                    this.f899yl.endTransaction();
                    if (this.this$0.f903Qc != null) {
                        this.this$0.f903Qc.mo809O();
                    }
                    C0529b.m181e("  22222222  ");
                    this.f897Al.close();
                }
                while (true) {
                    String[] strArr = this.f900zl;
                    String line = this.f897Al.readLine();
                    strArr[0] = line;
                    if (line == null) {
                        break;
                    }
                    String[] strArrSplit = this.f900zl[0].split(",");
                    String str = strArrSplit[0];
                    String str2 = strArrSplit[1];
                    String str3 = strArrSplit[2];
                    if (strArrSplit.length == 3) {
                        sQLiteStatementCompileStatement.bindString(1, str);
                        sQLiteStatementCompileStatement.bindString(2, str2);
                        sQLiteStatementCompileStatement.bindString(3, str3);
                        sQLiteStatementCompileStatement.executeInsert();
                    }
                }
                C0699o.m1030b(this.this$0.context, "Radio", "radio_freq_logo_data_size", this.f898Bl.length());
                this.f899yl.setTransactionSuccessful();
                this.f899yl.endTransaction();
                if (this.this$0.f903Qc != null) {
                    this.this$0.f903Qc.mo809O();
                }
                C0529b.m181e("  22222222  ");
                this.f897Al.close();
            } catch (Throwable th) {
                this.f899yl.endTransaction();
                if (this.this$0.f903Qc != null) {
                    this.this$0.f903Qc.mo809O();
                }
                C0529b.m181e("  22222222  ");
                try {
                    this.f897Al.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                throw th;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }
}
