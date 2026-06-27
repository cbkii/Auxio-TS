package com.eckom.xtlibrary.p020b.p052i;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.b */
/* JADX INFO: compiled from: FileUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0672b {
    /* JADX INFO: renamed from: b */
    public static String m924b(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), "utf-8"));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
