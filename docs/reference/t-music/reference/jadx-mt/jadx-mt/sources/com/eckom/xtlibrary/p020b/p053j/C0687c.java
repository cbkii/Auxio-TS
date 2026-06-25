package com.eckom.xtlibrary.p020b.p053j;

import android.os.FileUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.c */
/* JADX INFO: compiled from: MediaScan.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0687c {

    /* JADX INFO: renamed from: gm */
    private final ArrayList<String> f822gm = new ArrayList<>();
    private final ArrayList<String> music = new ArrayList<>();

    /* JADX INFO: renamed from: A */
    private void m1014A(String str, String str2) {
        File[] fileArrListFiles;
        File file = new File(str + "/" + str2);
        if (!file.isDirectory() || new File(file, ".nomedia").exists() || (fileArrListFiles = file.listFiles()) == null) {
            return;
        }
        boolean z = false;
        boolean z2 = false;
        for (File file2 : fileArrListFiles) {
            if (file2.canRead()) {
                String absolutePath = file2.getAbsolutePath();
                boolean z3 = file2.isDirectory() && absolutePath.startsWith("/mnt/sdcard/.") && absolutePath.length() > 14 && absolutePath.substring(14).split("/").length >= 5;
                String upperCase = file2.getName().toUpperCase(Locale.ENGLISH).toUpperCase();
                if (!upperCase.startsWith(".") && !z3) {
                    if (!file2.isDirectory()) {
                        if (!z && (upperCase.endsWith(".AVI") || upperCase.endsWith(".ASF") || upperCase.endsWith(".WMV") || upperCase.endsWith(".M2T") || upperCase.endsWith(".MTS") || upperCase.endsWith(".TS") || upperCase.endsWith(".MPG") || upperCase.endsWith(".M2P") || upperCase.endsWith(".MP4") || upperCase.endsWith(".FLV") || upperCase.endsWith(".SWF") || upperCase.endsWith(".VOB") || upperCase.endsWith(".MKV") || upperCase.endsWith(".DIVX") || upperCase.endsWith(".XVID") || upperCase.endsWith(".MOV") || upperCase.endsWith(".RMVB") || upperCase.endsWith(".RV") || upperCase.endsWith(".3GP") || upperCase.endsWith(".PMP") || upperCase.endsWith(".TP") || upperCase.endsWith(".TRP") || upperCase.endsWith(".RM") || upperCase.endsWith(".WEBM") || upperCase.endsWith(".M2TS") || upperCase.endsWith(".SSIF") || upperCase.endsWith(".MPEG") || upperCase.endsWith(".MPE") || upperCase.endsWith(".M3U8") || upperCase.endsWith(".M4V") || upperCase.endsWith(".3G2") || upperCase.endsWith(".F4V") || upperCase.endsWith(".3GPP"))) {
                            z = true;
                        }
                        if (!z2 && (upperCase.endsWith(".MP3") || upperCase.endsWith(".AAC") || upperCase.endsWith(".OGG") || upperCase.endsWith(".PCM") || upperCase.endsWith(".M4A") || upperCase.endsWith(".M4R") || upperCase.endsWith(".EC3") || upperCase.endsWith(".DTSHD") || upperCase.endsWith(".MKA") || upperCase.endsWith(".RA") || upperCase.endsWith(".WAV") || upperCase.endsWith(".CD") || upperCase.endsWith(".AMR") || upperCase.endsWith(".MP2") || upperCase.endsWith(".APE") || upperCase.endsWith(".DTS") || upperCase.endsWith(".FLAC") || upperCase.endsWith(".MIDI") || upperCase.endsWith(".MID") || upperCase.endsWith(".MPC") || upperCase.endsWith(".TTA") || upperCase.endsWith(".ASX") || upperCase.endsWith(".AIFF") || upperCase.endsWith(".AU"))) {
                            z2 = true;
                        }
                    } else if (!upperCase.startsWith("PROGRAM") && !upperCase.startsWith("SYSTEM") && !upperCase.startsWith("IGO") && !upperCase.equals("SYGIC") && !upperCase.equals("AURA") && !upperCase.equals("PAPAGO") && !upperCase.equals("TOMTOM") && !upperCase.equals("$RECYCLE.BIN") && !upperCase.equals("WINDOWS") && !upperCase.equals("BOOT") && !upperCase.equals("LOST.DIR") && !upperCase.equals("DCIM") && !upperCase.contains("NAVI") && !upperCase.equals("AMAPAUTO9") && !upperCase.equals("ANDROID") && !upperCase.equals("NAVIKING MAP") && !upperCase.equals("blinkdebug") && !upperCase.equals("YLOG")) {
                        m1014A(str, str2 + "/" + file2.getName());
                    }
                }
            }
        }
        if (z) {
            this.f822gm.add(str2);
        }
        if (z2) {
            this.music.add(str2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00cf A[Catch: Exception -> 0x00d3, TryCatch #11 {Exception -> 0x00d3, blocks: (B:20:0x0097, B:28:0x00b9, B:31:0x00cf, B:32:0x00d2), top: B:72:0x0062 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00fd A[Catch: all -> 0x0111, Exception -> 0x0114, LOOP:1: B:36:0x00f7->B:38:0x00fd, LOOP_END, TryCatch #12 {Exception -> 0x0114, all -> 0x0111, blocks: (B:35:0x00f1, B:36:0x00f7, B:38:0x00fd, B:39:0x010a), top: B:73:0x00f1 }] */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.BufferedWriter] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX INFO: renamed from: jb */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void m1015jb(String str) throws Throwable {
        String str2;
        BufferedWriter bufferedWriter;
        BufferedWriter bufferedWriter2;
        Iterator<String> it;
        this.f822gm.clear();
        this.music.clear();
        ?? r2 = 0;
        BufferedWriter bufferedWriter3 = null;
        try {
            m1014A(str, ".");
            if (str.startsWith("/storage/usb") || str.startsWith("/storage/extsd")) {
                str2 = "/data/tw/" + str.substring(9);
            } else {
                str2 = str + "/DCIM";
            }
            try {
                new File(str2).mkdir();
                FileUtils.setPermissions(str2, 493, -1, -1);
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            str2 = null;
        }
        try {
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(str2 + "/.video"));
                try {
                    try {
                        Iterator<String> it2 = this.f822gm.iterator();
                        while (it2.hasNext()) {
                            bufferedWriter.write(it2.next());
                            bufferedWriter.write(10);
                        }
                        bufferedWriter.flush();
                    } catch (Exception unused3) {
                        new File(str2 + "/.video").delete();
                        if (bufferedWriter != null) {
                        }
                        FileUtils.setPermissions(str2 + "/.video", 438, -1, -1);
                        this.f822gm.clear();
                        try {
                            bufferedWriter2 = new BufferedWriter(new FileWriter(str2 + "/.music"));
                            it = this.music.iterator();
                            while (it.hasNext()) {
                            }
                            bufferedWriter2.flush();
                            bufferedWriter2.close();
                            r2 = it;
                            FileUtils.setPermissions(str2 + "/.music", 438, -1, -1);
                        } catch (Exception unused4) {
                        }
                        this.music.clear();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    throw th;
                }
            } catch (Exception unused5) {
            }
        } catch (Exception unused6) {
            bufferedWriter = null;
        } catch (Throwable th2) {
            th = th2;
            bufferedWriter = null;
            if (bufferedWriter != null) {
            }
            throw th;
        }
        bufferedWriter.close();
        FileUtils.setPermissions(str2 + "/.video", 438, -1, -1);
        this.f822gm.clear();
        try {
            try {
                bufferedWriter2 = new BufferedWriter(new FileWriter(str2 + "/.music"));
            } catch (Exception unused7) {
            }
        } catch (Throwable th3) {
            th = th3;
        }
        try {
            it = this.music.iterator();
            while (it.hasNext()) {
                bufferedWriter2.write(it.next());
                bufferedWriter2.write(10);
            }
            bufferedWriter2.flush();
            bufferedWriter2.close();
            r2 = it;
        } catch (Exception unused8) {
            bufferedWriter3 = bufferedWriter2;
            new File(str2 + "/.music").delete();
            r2 = bufferedWriter3;
            if (bufferedWriter3 != null) {
                bufferedWriter3.close();
                r2 = bufferedWriter3;
            }
        } catch (Throwable th4) {
            th = th4;
            r2 = bufferedWriter2;
            if (r2 != 0) {
                r2.close();
            }
            throw th;
        }
        FileUtils.setPermissions(str2 + "/.music", 438, -1, -1);
        this.music.clear();
    }
}
