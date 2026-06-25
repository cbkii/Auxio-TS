package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.os.FileUtils;
import android.tw.john.TWUtil;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.f */
/* JADX INFO: compiled from: MusicUtils.java */
/* JADX INFO: loaded from: classes3.dex */
class C0641f extends Thread {

    /* JADX INFO: renamed from: Gn */
    final /* synthetic */ TWUtil f683Gn;

    /* JADX INFO: renamed from: hm */
    final /* synthetic */ C0578e f684hm;

    C0641f(C0578e c0578e, TWUtil tWUtil) {
        this.f684hm = c0578e;
        this.f683Gn = tWUtil;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.io.BufferedWriter] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.io.BufferedWriter] */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.io.BufferedWriter] */
    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Throwable {
        ?? bufferedWriter;
        ?? r1 = 0;
        ?? r12 = 0;
        try {
            try {
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter("/data/tw/music"));
                } catch (Exception e) {
                    String str = C0643h.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(e.toString());
                    Log.i(str, sb.toString());
                    r1 = sb;
                }
            } catch (Exception unused) {
            }
        } catch (Throwable th) {
            th = th;
            bufferedWriter = r1;
        }
        try {
            bufferedWriter.write(this.f684hm.f514_j);
            bufferedWriter.write(10);
            bufferedWriter.write(this.f684hm.f515ck);
            bufferedWriter.write(10);
            bufferedWriter.write(Integer.toString(this.f684hm.f482Ad));
            bufferedWriter.write(10);
            bufferedWriter.write(Integer.toString(this.f684hm.f521md));
            bufferedWriter.write(10);
            bufferedWriter.write(Integer.toString(this.f684hm.f517hc));
            bufferedWriter.write(10);
            bufferedWriter.write(Integer.toString(this.f684hm.f518ic));
            bufferedWriter.write(10);
            bufferedWriter.flush();
            this.f683Gn.write(40730, 1, 0, "sync");
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
    }
}
