package com.eckom.xtlibrary.p066b.p052i;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* compiled from: FileUtils.java */
/* renamed from: com.eckom.xtlibrary.b.i.b */
/* loaded from: classes3.dex */
public class C0672b {
    /* renamed from: b */
    public static String m924b(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), "utf-8"));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
