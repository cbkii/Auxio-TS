package com.eckom.xtlibrary.p066b.p053j;

import android.os.FileUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/* compiled from: MediaScan.java */
/* renamed from: com.eckom.xtlibrary.b.j.c */
/* loaded from: classes3.dex */
public class C0687c {

    /* renamed from: gm */
    private final ArrayList<String> f822gm = new ArrayList<>();
    private final ArrayList<String> music = new ArrayList<>();

    /* renamed from: A */
    private void m1014A(String str, String str2) {
        File[] listFiles;
        File file = new File(str + "/" + str2);
        if (!file.isDirectory() || new File(file, ".nomedia").exists() || (listFiles = file.listFiles()) == null) {
            return;
        }
        boolean z = false;
        boolean z2 = false;
        for (File file2 : listFiles) {
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

    /* JADX WARN: Can't wrap try/catch for region: R(11:0|1|(2:2|3)|(21:(26:8|9|10|11|12|13|14|15|(2:18|16)|19|20|21|22|23|24|25|26|28|29|(2:32|30)|33|34|35|36|37|38)|14|15|(1:16)|19|20|21|22|23|24|25|26|28|29|(1:30)|33|34|35|36|37|38)|70|9|10|11|12|13|(3:(1:58)|(0)|(0))) */
    /* JADX WARN: Can't wrap try/catch for region: R(13:(26:8|9|10|11|12|13|14|15|(2:18|16)|19|20|21|22|23|24|25|26|28|29|(2:32|30)|33|34|35|36|37|38)|24|25|26|28|29|(1:30)|33|34|35|36|37|38) */
    /* JADX WARN: Can't wrap try/catch for region: R(21:(26:8|9|10|11|12|13|14|15|(2:18|16)|19|20|21|22|23|24|25|26|28|29|(2:32|30)|33|34|35|36|37|38)|14|15|(1:16)|19|20|21|22|23|24|25|26|28|29|(1:30)|33|34|35|36|37|38) */
    /* JADX WARN: Can't wrap try/catch for region: R(32:0|1|2|3|(26:8|9|10|11|12|13|14|15|(2:18|16)|19|20|21|22|23|24|25|26|28|29|(2:32|30)|33|34|35|36|37|38)|70|9|10|11|12|13|14|15|(1:16)|19|20|21|22|23|24|25|26|28|29|(1:30)|33|34|35|36|37|38|(3:(1:58)|(0)|(0))) */
    /* JADX WARN: Can't wrap try/catch for region: R(9:(13:(26:8|9|10|11|12|13|14|15|(2:18|16)|19|20|21|22|23|24|25|26|28|29|(2:32|30)|33|34|35|36|37|38)|24|25|26|28|29|(1:30)|33|34|35|36|37|38)|14|15|(1:16)|19|20|21|22|23) */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0114, code lost:
    
        r2 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0118, code lost:
    
        new java.io.File(r10 + "/.music").delete();
        r2 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x012f, code lost:
    
        if (r2 != null) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0131, code lost:
    
        r2.close();
        r2 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0111, code lost:
    
        r10 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0112, code lost:
    
        r2 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0147, code lost:
    
        if (r2 != null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0149, code lost:
    
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x014c, code lost:
    
        throw r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x009f, code lost:
    
        new java.io.File(r10 + "/.video").delete();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00b6, code lost:
    
        if (r6 == null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x009e, code lost:
    
        r6 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x009b, code lost:
    
        r1 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x009c, code lost:
    
        r6 = null;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0087 A[Catch: Exception -> 0x009f, all -> 0x00cc, LOOP:0: B:16:0x0081->B:18:0x0087, LOOP_END, TryCatch #1 {Exception -> 0x009f, blocks: (B:15:0x007b, B:16:0x0081, B:18:0x0087, B:20:0x0094), top: B:14:0x007b }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00fd A[Catch: all -> 0x0111, Exception -> 0x0114, LOOP:1: B:30:0x00f7->B:32:0x00fd, LOOP_END, TryCatch #12 {Exception -> 0x0114, all -> 0x0111, blocks: (B:29:0x00f1, B:30:0x00f7, B:32:0x00fd, B:34:0x010a), top: B:28:0x00f1 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00cf A[Catch: Exception -> 0x00d3, TryCatch #11 {Exception -> 0x00d3, blocks: (B:21:0x0097, B:22:0x00b9, B:61:0x00cf, B:62:0x00d2), top: B:12:0x0062 }] */
    /* JADX WARN: Type inference failed for: r2v10, types: [java.util.Iterator] */
    /* renamed from: jb */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void m1015jb(String str) {
        String str2;
        BufferedWriter bufferedWriter;
        ?? it;
        Iterator<String> it2;
        this.f822gm.clear();
        this.music.clear();
        BufferedWriter bufferedWriter2 = null;
        BufferedWriter bufferedWriter3 = null;
        try {
            m1014A(str, ".");
        } catch (Exception unused) {
            str2 = null;
        }
        try {
            try {
                if (!str.startsWith("/storage/usb") && !str.startsWith("/storage/extsd")) {
                    str2 = str + "/DCIM";
                    new File(str2).mkdir();
                    FileUtils.setPermissions(str2, 493, -1, -1);
                    bufferedWriter = new BufferedWriter(new FileWriter(str2 + "/.video"));
                    it2 = this.f822gm.iterator();
                    while (it2.hasNext()) {
                        bufferedWriter.write(it2.next());
                        bufferedWriter.write(10);
                    }
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    FileUtils.setPermissions(str2 + "/.video", 438, -1, -1);
                    this.f822gm.clear();
                    BufferedWriter bufferedWriter4 = new BufferedWriter(new FileWriter(str2 + "/.music"));
                    it = this.music.iterator();
                    while (it.hasNext()) {
                        bufferedWriter4.write((String) it.next());
                        bufferedWriter4.write(10);
                    }
                    bufferedWriter4.flush();
                    bufferedWriter4.close();
                    bufferedWriter2 = it;
                    FileUtils.setPermissions(str2 + "/.music", 438, -1, -1);
                    this.music.clear();
                    return;
                }
                BufferedWriter bufferedWriter42 = new BufferedWriter(new FileWriter(str2 + "/.music"));
                it = this.music.iterator();
                while (it.hasNext()) {
                }
                bufferedWriter42.flush();
                bufferedWriter42.close();
                bufferedWriter2 = it;
                FileUtils.setPermissions(str2 + "/.music", 438, -1, -1);
                this.music.clear();
                return;
            } catch (Throwable th) {
                th = th;
            }
            it2 = this.f822gm.iterator();
            while (it2.hasNext()) {
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            FileUtils.setPermissions(str2 + "/.video", 438, -1, -1);
            this.f822gm.clear();
        } catch (Throwable th2) {
            th = th2;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            throw th;
        }
        str2 = "/data/tw/" + str.substring(9);
        new File(str2).mkdir();
        FileUtils.setPermissions(str2, 493, -1, -1);
        bufferedWriter = new BufferedWriter(new FileWriter(str2 + "/.video"));
    }
}
