package com.eckom.xtlibrary.p020b.p053j;

import android.media.MediaMetadataRetriever;
import android.os.FileUtils;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0574a;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0575b;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0576c;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/* compiled from: MediaScanMedia.java */
/* renamed from: com.eckom.xtlibrary.b.j.m */
/* loaded from: classes3.dex */
public class C0697m {
    private ArrayList<String> album;
    private ArrayList<String> all;
    private ArrayList<String> artist;

    /* renamed from: jm */
    private ArrayList<File> f838jm;

    /* renamed from: km */
    private ArrayList<String> f839km;

    /* renamed from: lm */
    private ArrayList<String> f840lm;

    /* renamed from: mm */
    final ArrayList<C0576c> f841mm = new ArrayList<>();

    /* renamed from: nm */
    final ArrayList<C0575b> f842nm = new ArrayList<>();

    /* renamed from: om */
    final ArrayList<C0574a> f843om = new ArrayList<>();

    public C0697m() {
        this.f838jm = new ArrayList<>();
        this.all = new ArrayList<>();
        this.album = new ArrayList<>();
        this.artist = new ArrayList<>();
        this.f839km = new ArrayList<>();
        this.f840lm = new ArrayList<>();
        this.all = new ArrayList<>();
        this.album = new ArrayList<>();
        this.artist = new ArrayList<>();
        this.f839km = new ArrayList<>();
        this.f840lm = new ArrayList<>();
        this.f838jm = new ArrayList<>();
    }

    /* renamed from: A */
    private void m1016A(String str, String str2) {
        File[] listFiles;
        File file = new File(str + "/" + str2);
        if (!file.isDirectory() || new File(file, ".nomedia").exists() || (listFiles = file.listFiles()) == null) {
            return;
        }
        for (File file2 : listFiles) {
            if (file2.canRead()) {
                String absolutePath = file2.getAbsolutePath();
                boolean z = file2.isDirectory() && absolutePath.startsWith("/storage/") && absolutePath.length() > 14 && absolutePath.substring(14).split("/").length >= 5;
                String upperCase = file2.getName().toUpperCase(Locale.ENGLISH).toUpperCase();
                if (!upperCase.startsWith(".") && !z) {
                    if (file2.isDirectory()) {
                        if (!upperCase.startsWith("PROGRAM") && !upperCase.startsWith("SYSTEM") && !upperCase.startsWith("IGO") && !upperCase.equals("SYGIC") && !upperCase.equals("AURA") && !upperCase.equals("PAPAGO") && !upperCase.equals("TOMTOM") && !upperCase.equals("$RECYCLE.BIN") && !upperCase.equals("WINDOWS") && !upperCase.equals("BOOT") && !upperCase.equals("LOST.DIR") && !upperCase.equals("DCIM") && !upperCase.contains("NAVI") && !upperCase.equals("AMAPAUTO9") && !upperCase.equals("ANDROID") && !upperCase.equals("NAVIKING MAP") && !upperCase.equals("blinkdebug") && !upperCase.equals("YLOG")) {
                            m1016A(str, str2 + "/" + file2.getName());
                        }
                    } else if (C0643h.isAudio(upperCase)) {
                        m1017c(file2);
                    }
                }
            }
        }
    }

    /* renamed from: a */
    public String m1018a(String str, C0578e c0578e, Boolean bool, C0643h.f fVar) {
        String str2;
        String str3;
        C0529b.m178a("scanMedia:" + str);
        if (str.startsWith("/storage/usb") || str.startsWith("/storage/extsd")) {
            str2 = "/data/tw/" + str.substring(9);
        } else {
            str2 = str + "/DCIM";
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(str2 + "/.music"));
            new ArrayList();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                File file = new File(str + "/" + readLine);
                if (file.canRead() && file.isDirectory()) {
                    m1016A(file.getAbsolutePath(), ".");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (str.startsWith("/storage/usb") || str.startsWith("/storage/extsd")) {
            str3 = "/data/tw/" + str.substring(9);
        } else {
            str3 = str + "/DCIM";
        }
        new File(str3).mkdir();
        FileUtils.setPermissions(str3, 493, -1, -1);
        if (new File(str3 + "/album").exists()) {
            C0643h.delete(str3 + "/album");
        }
        new File(str3 + "/album").mkdir();
        FileUtils.setPermissions(str3 + "/album", 493, -1, -1);
        if (new File(str3 + "/artist").exists()) {
            C0643h.delete(str3 + "/artist");
        }
        new File(str3 + "/artist").mkdir();
        FileUtils.setPermissions(str3 + "/artist", 493, -1, -1);
        this.album.clear();
        this.artist.clear();
        this.all.clear();
        synchronized (this.f843om) {
            Iterator<C0574a> it = this.f843om.iterator();
            while (it.hasNext()) {
                C0574a next = it.next();
                this.f839km.clear();
                this.album.add(next.getName());
                Iterator<C0576c> it2 = next.m443rc().iterator();
                while (it2.hasNext()) {
                    this.f839km.add(it2.next().getUrl());
                }
                if (new File(str3 + "/album").exists()) {
                    C0643h.m758b(str3 + "/album/" + next.getName(), this.f839km);
                }
                this.f839km.clear();
                it.remove();
            }
        }
        synchronized (this.f841mm) {
            Iterator<C0576c> it3 = this.f841mm.iterator();
            while (it3.hasNext()) {
                this.all.add(it3.next().getUrl());
                it3.remove();
            }
        }
        synchronized (this.f842nm) {
            Iterator<C0575b> it4 = this.f842nm.iterator();
            while (it4.hasNext()) {
                C0575b next2 = it4.next();
                this.f840lm.clear();
                this.artist.add(next2.getName());
                Iterator<C0576c> it5 = next2.m444sc().iterator();
                while (it5.hasNext()) {
                    this.f840lm.add(it5.next().url);
                }
                if (new File(str3 + "/artist").exists()) {
                    C0643h.m758b(str3 + "/artist/" + next2.getName(), this.f840lm);
                }
                this.f840lm.clear();
                it4.remove();
            }
        }
        if (new File(str3 + "/.all").exists()) {
            C0643h.delete(str3 + "/.all");
        }
        C0643h.m758b(str3 + "/.all", this.all);
        this.all.clear();
        if (str3.startsWith("/mnt/sdcard/iNand")) {
            m1021d(str3, c0578e, bool.booleanValue(), fVar);
        } else if (str3.contains("usb")) {
            m1020c(str3, c0578e, bool.booleanValue(), fVar);
        } else if (str3.contains("sd")) {
            m1019b(str3, c0578e, bool.booleanValue(), fVar);
        }
        Iterator<C0580g> it6 = c0578e.f535xj.iterator();
        while (it6.hasNext()) {
            C0580g next3 = it6.next();
            if (c0578e.f515ck.contains(next3.mName)) {
                c0578e.f512Yj = next3;
            }
        }
        Iterator<C0580g> it7 = c0578e.f496Jj.iterator();
        while (it7.hasNext()) {
            C0580g next4 = it7.next();
            if (c0578e.f515ck.contains(next4.mName)) {
                c0578e.f512Yj = next4;
            }
        }
        if (c0578e.f504Rj != null) {
            try {
                if (new File(c0578e.f515ck).getCanonicalPath().contains(new File(c0578e.f504Rj.mName).getCanonicalPath())) {
                    c0578e.f512Yj = c0578e.f504Rj;
                }
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        Iterator<C0580g> it8 = c0578e.f533wj.iterator();
        while (it8.hasNext()) {
            C0580g next5 = it8.next();
            if (c0578e.f515ck.contains(next5.mName)) {
                c0578e.f513Zj = next5;
            }
        }
        Iterator<C0580g> it9 = c0578e.f496Jj.iterator();
        while (it9.hasNext()) {
            C0580g next6 = it9.next();
            if (c0578e.f515ck.contains(next6.mName)) {
                c0578e.f513Zj = next6;
            }
        }
        if (c0578e.f505Sj != null) {
            try {
                if (new File(c0578e.f515ck).getCanonicalPath().contains(new File(c0578e.f505Sj.mName).getCanonicalPath())) {
                    c0578e.f512Yj = c0578e.f504Rj;
                }
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        Iterator<C0580g> it10 = c0578e.f531vj.iterator();
        while (it10.hasNext()) {
            C0580g next7 = it10.next();
            if (c0578e.f515ck.contains(next7.mName)) {
                c0578e.f511Xj = next7;
            }
        }
        Iterator<C0580g> it11 = c0578e.f494Hj.iterator();
        while (it11.hasNext()) {
            C0580g next8 = it11.next();
            if (c0578e.f515ck.contains(next8.mName)) {
                c0578e.f511Xj = next8;
            }
        }
        if (c0578e.f502Pj != null) {
            try {
                if (new File(c0578e.f515ck).getCanonicalPath().contains(new File(c0578e.f502Pj.mName).getCanonicalPath())) {
                    c0578e.f512Yj = c0578e.f504Rj;
                }
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
        return str;
    }

    /* renamed from: b */
    public void m1019b(String str, C0578e c0578e, boolean z, C0643h.f fVar) {
        Iterator<C0580g> it = c0578e.f495Ij.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0643h.m754a(new C0580g(str, 1, 3, 0), str + "/album/", z, new C0688d(this, c0578e));
        C0643h.m754a(new C0580g(str, 1, 2, 0), str + "/artist/", z, new C0689e(this, c0578e));
        C0643h.m757b(new C0580g(str, 1, 1, 0), str + "/.all", z, new C0690f(this, c0578e));
    }

    /* renamed from: c */
    public void m1020c(String str, C0578e c0578e, boolean z, C0643h.f fVar) {
        Iterator<C0580g> it = c0578e.f535xj.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0643h.m754a(new C0580g(str, 2, 3, 0), str + "/album/", z, new C0691g(this, c0578e, fVar));
        C0643h.m754a(new C0580g(str, 2, 2, 0), str + "/artist/", z, new C0692h(this, c0578e, fVar));
        C0643h.m757b(new C0580g(str, 2, 1, 0), str + "/.all", z, new C0693i(this, c0578e, fVar));
    }

    /* renamed from: d */
    public void m1021d(String str, C0578e c0578e, boolean z, C0643h.f fVar) {
        c0578e.f504Rj = new C0580g(str, 3, 3, 0);
        C0643h.m754a(c0578e.f504Rj, str + "/album/", z, new C0694j(this, c0578e, fVar));
        c0578e.f505Sj = new C0580g(str, 3, 2, 0);
        C0643h.m754a(c0578e.f505Sj, str + "/artist/", z, new C0695k(this, c0578e, fVar));
        c0578e.f502Pj = new C0580g(str, 3, 1, 0);
        C0643h.m757b(c0578e.f502Pj, str + "/.all", z, new C0696l(this, c0578e, fVar));
    }

    /* renamed from: c */
    private synchronized void m1017c(File file) {
        File file2 = new File(file.getAbsolutePath());
        if (file2.exists() && file2.canRead()) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
                String extractMetadata = mediaMetadataRetriever.extractMetadata(2);
                String extractMetadata2 = mediaMetadataRetriever.extractMetadata(1);
                String substring = file.getName().substring(0, file.getName().lastIndexOf("."));
                if (extractMetadata2 == null || extractMetadata2.equals("")) {
                    extractMetadata2 = "未知";
                }
                if (extractMetadata == null || extractMetadata.equals("")) {
                    extractMetadata = "未知";
                }
                String str = extractMetadata;
                C0576c c0576c = new C0576c(substring, file.getPath(), extractMetadata2, str, null);
                this.f841mm.add(c0576c);
                C0575b c0575b = new C0575b(str, c0576c);
                if (C0703s.m1047d(this.f842nm, str)) {
                    C0703s.m1045b(this.f842nm, str).m444sc().add(c0576c);
                } else {
                    this.f842nm.add(c0575b);
                }
                C0574a c0574a = new C0574a(extractMetadata2, c0576c);
                if (C0703s.m1046c(this.f843om, extractMetadata2)) {
                    C0703s.m1039a(this.f843om, extractMetadata2).m443rc().add(c0576c);
                } else {
                    this.f843om.add(c0574a);
                }
            } catch (Exception unused) {
            }
        }
    }
}
