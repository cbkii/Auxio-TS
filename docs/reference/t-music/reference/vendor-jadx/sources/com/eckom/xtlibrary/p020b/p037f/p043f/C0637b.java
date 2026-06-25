package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.util.Log;
import cpdetector.p064io.ASCIIDetector;
import cpdetector.p064io.CodepageDetectorProxy;
import cpdetector.p064io.JChardetFacade;
import cpdetector.p064io.ParsingDetector;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/* compiled from: LrcTranscoding.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.b */
/* loaded from: classes3.dex */
public class C0637b {
    /* JADX WARN: Removed duplicated region for block: B:41:0x00b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00a8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:57:0x0062 -> B:16:0x00a3). Please report as a decompilation issue!!! */
    /* renamed from: Oa */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String m745Oa(String str) {
        BufferedInputStream bufferedInputStream;
        FileInputStream fileInputStream;
        String str2 = "";
        FileInputStream fileInputStream2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(new File(str));
                try {
                    bufferedInputStream = new BufferedInputStream(fileInputStream);
                } catch (Exception e) {
                    e = e;
                    bufferedInputStream = null;
                } catch (Throwable th) {
                    th = th;
                    bufferedInputStream = null;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (Exception e3) {
            e = e3;
            bufferedInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            bufferedInputStream = null;
            fileInputStream = null;
        }
        try {
            String m746Pa = m746Pa(str);
            Log.d("HYH", "converfile: " + m746Pa);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, m746Pa));
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                str2 = str2 + readLine + "\n";
            }
            try {
                fileInputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            bufferedInputStream.close();
        } catch (Exception e5) {
            e = e5;
            fileInputStream2 = fileInputStream;
            try {
                e.printStackTrace();
                Log.i("md", "e  " + e.toString());
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                return str2;
            } catch (Throwable th3) {
                th = th3;
                fileInputStream = fileInputStream2;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e7) {
                        e7.printStackTrace();
                    }
                }
                if (bufferedInputStream != null) {
                    throw th;
                }
                try {
                    bufferedInputStream.close();
                    throw th;
                } catch (IOException e8) {
                    e8.printStackTrace();
                    throw th;
                }
            }
        } catch (Throwable th4) {
            th = th4;
            if (fileInputStream != null) {
            }
            if (bufferedInputStream != null) {
            }
        }
        return str2;
    }

    /* renamed from: Pa */
    public static String m746Pa(String str) {
        try {
            File file = new File(str);
            CodepageDetectorProxy codepageDetectorProxy = CodepageDetectorProxy.getInstance();
            codepageDetectorProxy.add(new ParsingDetector(false));
            codepageDetectorProxy.add(JChardetFacade.getInstance());
            codepageDetectorProxy.add(ASCIIDetector.getInstance());
            Charset detectCodepage = codepageDetectorProxy.detectCodepage(file.toURI().toURL());
            return detectCodepage != null ? detectCodepage.name() : "GBK";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
