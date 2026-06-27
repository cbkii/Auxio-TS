package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.os.FileUtils;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.r */
/* JADX INFO: compiled from: TWMusic.java */
/* JADX INFO: loaded from: classes3.dex */
class C0653r extends Thread {
    final /* synthetic */ C0654s this$0;

    C0653r(C0654s c0654s) {
        this.this$0 = c0654s;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.io.BufferedWriter] */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.io.BufferedWriter] */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.io.BufferedWriter] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:20:0x0084 -> B:23:0x00a0). Please report as a decompilation issue!!! */
    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Throwable {
        ?? bufferedWriter;
        ?? r1 = 0;
        ?? r12 = 0;
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
                C0654s.f713jd.write(40730, 1, 0, "sync");
                bufferedWriter.close();
            } catch (Exception unused2) {
                r12 = bufferedWriter;
                new File("/data/tw/music").delete();
                if (r12 != 0) {
                    r12.close();
                }
            } catch (Throwable th2) {
                th = th2;
                if (bufferedWriter != 0) {
                    bufferedWriter.close();
                }
                throw th;
            }
            r1 = -1;
            FileUtils.setPermissions("/data/tw/music", 438, -1, -1);
        } catch (Exception e) {
            String str = C0654s.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(e.toString());
            Log.i(str, sb.toString());
            r1 = sb;
        }
    }
}
