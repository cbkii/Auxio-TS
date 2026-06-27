package com.eckom.xtlibrary.twproject.radio.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p066b.p053j.C0699o;
import com.eckom.xtlibrary.twproject.radio.utils.C0721b;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/* compiled from: DBHelper.java */
/* renamed from: com.eckom.xtlibrary.twproject.radio.utils.a */
/* loaded from: classes3.dex */
class RunnableC0720a implements Runnable {

    /* renamed from: Al */
    final /* synthetic */ BufferedReader f897Al;

    /* renamed from: Bl */
    final /* synthetic */ File f898Bl;
    final /* synthetic */ C0721b this$0;

    /* renamed from: yl */
    final /* synthetic */ SQLiteDatabase f899yl;

    /* renamed from: zl */
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
        C0721b.a aVar;
        C0721b.a aVar2;
        C0721b.a aVar3;
        C0721b.a aVar4;
        C0721b.a aVar5;
        C0721b.a aVar6;
        Context context;
        C0721b.a aVar7;
        C0721b.a aVar8;
        this.f899yl.beginTransaction();
        try {
            try {
                try {
                    SQLiteStatement compileStatement = this.f899yl.compileStatement("INSERT INTO radiologo (pi, freq, icon) VALUES (?, ?, ?)");
                    while (true) {
                        String[] strArr = this.f900zl;
                        String readLine = this.f897Al.readLine();
                        strArr[0] = readLine;
                        if (readLine == null) {
                            break;
                        }
                        String[] split = this.f900zl[0].split(",");
                        String str = split[0];
                        String str2 = split[1];
                        String str3 = split[2];
                        if (split.length == 3) {
                            compileStatement.bindString(1, str);
                            compileStatement.bindString(2, str2);
                            compileStatement.bindString(3, str3);
                            compileStatement.executeInsert();
                        }
                    }
                    context = this.this$0.context;
                    C0699o.m1030b(context, "Radio", "radio_freq_logo_data_size", this.f898Bl.length());
                    this.f899yl.setTransactionSuccessful();
                    this.f899yl.endTransaction();
                    aVar7 = this.this$0.f903Qc;
                    if (aVar7 != null) {
                        aVar8 = this.this$0.f903Qc;
                        aVar8.mo809O();
                    }
                    C0529b.m181e("  22222222  ");
                    this.f897Al.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    aVar3 = this.this$0.f903Qc;
                    if (aVar3 != null) {
                        aVar6 = this.this$0.f903Qc;
                        aVar6.mo810Q();
                    }
                    this.f899yl.endTransaction();
                    aVar4 = this.this$0.f903Qc;
                    if (aVar4 != null) {
                        aVar5 = this.this$0.f903Qc;
                        aVar5.mo809O();
                    }
                    C0529b.m181e("  22222222  ");
                    this.f897Al.close();
                }
            } catch (Throwable th) {
                this.f899yl.endTransaction();
                aVar = this.this$0.f903Qc;
                if (aVar != null) {
                    aVar2 = this.this$0.f903Qc;
                    aVar2.mo809O();
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
