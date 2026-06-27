package com.eckom.xtlibrary.p066b.p069f.p043f;

import android.os.FileUtils;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/* compiled from: TWMusic.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.r */
/* loaded from: classes3.dex */
class C0653r extends Thread {
    final /* synthetic */ C0654s this$0;

    C0653r(C0654s c0654s) {
        this.this$0 = c0654s;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v4 */
    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        String str;
        BufferedWriter bufferedWriter;
        C0654s c0654s;
        ?? r1 = 0;
        BufferedWriter bufferedWriter2 = null;
        try {
            try {
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter("/data/tw/music"));
                } catch (Throwable th) {
                    th = th;
                    bufferedWriter = r1;
                }
            } catch (Exception unused) {
            }
            try {
                bufferedWriter.write(C0654s.f702Bd);
                bufferedWriter.write(10);
                bufferedWriter.write(Integer.toString(C0654s.f701Ad));
                bufferedWriter.write(10);
                bufferedWriter.write(Integer.toString(this.this$0.f719md));
                bufferedWriter.write(10);
                bufferedWriter.write(Integer.toString(this.this$0.f715hc));
                bufferedWriter.write(10);
                bufferedWriter.write(Integer.toString(this.this$0.f716ic));
                bufferedWriter.write(10);
                bufferedWriter.flush();
                c0654s = C0654s.f713jd;
                c0654s.write(40730, 1, 0, "sync");
                bufferedWriter.close();
            } catch (Exception unused2) {
                bufferedWriter2 = bufferedWriter;
                new File("/data/tw/music").delete();
                if (bufferedWriter2 != null) {
                    bufferedWriter2.close();
                }
                r1 = -1;
                FileUtils.setPermissions("/data/tw/music", 438, -1, -1);
            } catch (Throwable th2) {
                th = th2;
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                throw th;
            }
            r1 = -1;
            FileUtils.setPermissions("/data/tw/music", 438, -1, -1);
        } catch (Exception e) {
            str = C0654s.TAG;
            Log.i(str, "" + e.toString());
        }
    }
}
