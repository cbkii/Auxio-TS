package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.content.Context;
import android.os.AsyncTask;
import android.os.FileUtils;
import android.tw.john.TWUtil;
import android.util.Log;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.p020b.p053j.C0697m;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.h */
/* JADX INFO: compiled from: MusicUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0643h {
    static String TAG = "MusicUtils";
    private static final ArrayList<String> mAudioList;
    public static ExecutorService pool;

    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.h$a */
    /* JADX INFO: compiled from: MusicUtils.java */
    public interface a {
        /* JADX INFO: renamed from: S */
        void mo455S();

        /* JADX INFO: renamed from: a */
        void mo456a(C0580g c0580g, String str);
    }

    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.h$b */
    /* JADX INFO: compiled from: MusicUtils.java */
    public static class b extends AsyncTask<Void, Void, C0580g> {

        /* JADX INFO: renamed from: Tc */
        private ArrayList<C0579f> f686Tc;

        /* JADX INFO: renamed from: Uc */
        private boolean f687Uc;

        /* JADX INFO: renamed from: Vc */
        a f688Vc;
        private Context mContext;
        private String mPath;
        private C0580g mRecord;

        public b(Context context, C0580g c0580g, String str, ArrayList<C0579f> arrayList, boolean z) {
            this.mContext = context;
            this.mRecord = c0580g;
            this.mPath = str;
            this.f686Tc = arrayList;
            this.f687Uc = z;
        }

        /* JADX INFO: renamed from: a */
        public void m760a(a aVar) {
            this.f688Vc = aVar;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(C0580g c0580g) {
            a aVar;
            super.onPostExecute(c0580g);
            C0529b.m180d("完成扫描：" + this.mPath);
            if (c0580g == null || (aVar = this.f688Vc) == null) {
                return;
            }
            aVar.mo456a(c0580g, this.mPath);
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public C0580g doInBackground(Void... voidArr) {
            String str;
            a aVar = this.f688Vc;
            if (aVar != null) {
                aVar.mo455S();
            }
            if (this.mRecord != null && (str = this.mPath) != null) {
                File[] fileArrListFiles = new File(str).listFiles(new C0644i(this));
                this.mRecord.f545kk = 0;
                ArrayList arrayList = new ArrayList();
                if (fileArrListFiles != null) {
                    this.mRecord.setLength(fileArrListFiles.length);
                    for (File file : fileArrListFiles) {
                        String name = file.getName();
                        arrayList.add(new C0579f(name.substring(0, name.lastIndexOf(".")), file.getAbsolutePath(), C0636a.m743a(this.mContext, this.mPath + "/" + name, this.f686Tc)));
                    }
                }
                C0643h.m755a(this.mRecord, arrayList, this.f687Uc);
            }
            return this.mRecord;
        }
    }

    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.h$c */
    /* JADX INFO: compiled from: MusicUtils.java */
    public static class c extends AsyncTask<Void, Void, C0580g> {

        /* JADX INFO: renamed from: Uc */
        private boolean f689Uc;

        /* JADX INFO: renamed from: Wc */
        private String f690Wc;

        /* JADX INFO: renamed from: Xc */
        private a f691Xc;
        private C0580g mRecord;

        public c(C0580g c0580g, String str, boolean z) {
            this.mRecord = c0580g;
            this.f690Wc = str;
            this.f689Uc = z;
        }

        /* JADX INFO: renamed from: b */
        public void m763b(a aVar) {
            this.f691Xc = aVar;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(C0580g c0580g) {
            super.onPostExecute(c0580g);
            if (this.f691Xc != null) {
                C0529b.m180d("完成读取：" + this.f690Wc);
                this.f691Xc.mo456a(c0580g, this.f690Wc);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public C0580g doInBackground(Void... voidArr) {
            a aVar = this.f691Xc;
            if (aVar != null) {
                aVar.mo455S();
            }
            ArrayList arrayList = new ArrayList();
            new File(this.f690Wc).listFiles(new C0645j(this, arrayList));
            this.mRecord.setLength(arrayList.size());
            C0643h.m755a(this.mRecord, arrayList, this.f689Uc);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.mRecord.m449a((C0579f) it.next());
            }
            arrayList.clear();
            return this.mRecord;
        }
    }

    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.h$d */
    /* JADX INFO: compiled from: MusicUtils.java */
    public static class d extends AsyncTask<Void, Void, C0580g> {

        /* JADX INFO: renamed from: Uc */
        private boolean f692Uc;

        /* JADX INFO: renamed from: Vc */
        private a f693Vc;

        /* JADX INFO: renamed from: Wc */
        private String f694Wc;
        private C0580g mRecord;

        public d(C0580g c0580g, String str, boolean z) {
            this.mRecord = c0580g;
            this.f694Wc = str;
            this.f692Uc = z;
        }

        /* JADX INFO: renamed from: a */
        public void m764a(a aVar) {
            this.f693Vc = aVar;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(C0580g c0580g) {
            super.onPostExecute(c0580g);
            if (this.f693Vc != null) {
                C0529b.m180d("完成读取：" + this.f694Wc);
                this.f693Vc.mo456a(c0580g, this.f694Wc);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v0 */
        /* JADX WARN: Type inference failed for: r3v1 */
        /* JADX WARN: Type inference failed for: r3v10 */
        /* JADX WARN: Type inference failed for: r3v11, types: [boolean] */
        /* JADX WARN: Type inference failed for: r3v14, types: [boolean] */
        /* JADX WARN: Type inference failed for: r3v15, types: [com.eckom.xtlibrary.b.f.b.f, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r3v16 */
        /* JADX WARN: Type inference failed for: r3v17 */
        /* JADX WARN: Type inference failed for: r3v18 */
        /* JADX WARN: Type inference failed for: r3v19 */
        /* JADX WARN: Type inference failed for: r3v2, types: [java.io.BufferedReader] */
        /* JADX WARN: Type inference failed for: r3v20 */
        /* JADX WARN: Type inference failed for: r3v21 */
        /* JADX WARN: Type inference failed for: r3v22 */
        /* JADX WARN: Type inference failed for: r3v23 */
        /* JADX WARN: Type inference failed for: r3v3 */
        /* JADX WARN: Type inference failed for: r3v4 */
        /* JADX WARN: Type inference failed for: r3v5 */
        /* JADX WARN: Type inference failed for: r3v6 */
        /* JADX WARN: Type inference failed for: r3v9, types: [boolean] */
        /* JADX WARN: Type inference failed for: r4v0 */
        /* JADX WARN: Type inference failed for: r4v1, types: [java.io.BufferedReader] */
        /* JADX WARN: Type inference failed for: r4v2, types: [java.io.BufferedReader] */
        @Override // android.os.AsyncTask
        public C0580g doInBackground(Void... voidArr) throws Throwable {
            String str;
            ?? bufferedReader;
            a aVar = this.f693Vc;
            if (aVar != null) {
                aVar.mo455S();
            }
            ArrayList<C0579f> arrayList = new ArrayList();
            if (this.mRecord != null && (str = this.f694Wc) != null) {
                ?? r3 = 0;
                ?? IsFile = 0;
                r3 = 0;
                ?? r32 = 0;
                try {
                    try {
                        try {
                            bufferedReader = new BufferedReader(new FileReader(str));
                        } catch (Exception e) {
                            Log.i(C0643h.TAG, "" + e.toString());
                        }
                    } catch (Exception e2) {
                        e = e2;
                    }
                    while (true) {
                        try {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            File file = new File(line);
                            String name = file.getName();
                            String strSubstring = name.substring(0, name.lastIndexOf("."));
                            IsFile = file.isFile();
                            if (IsFile != 0 && (IsFile = name.startsWith(".")) == 0 && (IsFile = C0643h.isAudio(name.toUpperCase(Locale.ENGLISH))) != 0) {
                                IsFile = new C0579f(strSubstring, line);
                                arrayList.add(IsFile);
                            }
                        } catch (Exception e3) {
                            e = e3;
                            r32 = bufferedReader;
                            Log.i(C0643h.TAG, "" + e.toString());
                            r3 = r32;
                            if (r32 != 0) {
                                r32.close();
                                r3 = r32;
                            }
                        } catch (Throwable th) {
                            th = th;
                            if (bufferedReader != 0) {
                                bufferedReader.close();
                            }
                            throw th;
                        }
                    }
                    this.mRecord.setLength(arrayList.size());
                    C0643h.m755a(this.mRecord, arrayList, this.f692Uc);
                    ?? r33 = IsFile;
                    for (C0579f c0579f : arrayList) {
                        C0580g c0580g = this.mRecord;
                        c0580g.m449a(c0579f);
                        r33 = c0580g;
                    }
                    arrayList.clear();
                    bufferedReader.close();
                    r3 = r33;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = r3;
                }
            }
            return this.mRecord;
        }
    }

    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.h$e */
    /* JADX INFO: compiled from: MusicUtils.java */
    public static class e extends AsyncTask<String, Void, String> {

        /* JADX INFO: renamed from: Yc */
        C0578e f695Yc;

        /* JADX INFO: renamed from: Zc */
        f f696Zc;
        boolean isForward;
        String path;

        public e(String str, C0578e c0578e, boolean z) {
            this.f695Yc = c0578e;
            this.isForward = z;
            this.path = str;
        }

        /* JADX INFO: renamed from: a */
        public void m767a(f fVar) {
            this.f696Zc = fVar;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: oa, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            f fVar = this.f696Zc;
            if (fVar != null) {
                fVar.mo641ia(str);
            }
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public String doInBackground(String... strArr) {
            C0697m c0697m = new C0697m();
            String str = this.path;
            c0697m.m1018a(str, this.f695Yc, Boolean.valueOf(this.isForward), this.f696Zc);
            return str;
        }
    }

    /* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.h$f */
    /* JADX INFO: compiled from: MusicUtils.java */
    public interface f {
        /* JADX INFO: renamed from: ia */
        void mo641ia(String str);
    }

    static {
        mAudioList = m748Fb((C0686b.m1000Uc() || C0686b.m1001Vc()) ? "/system_tw/etc/ijk.audio" : "/system/etc/ijk.audio");
        pool = new ThreadPoolExecutor(2, 10, 1000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(5), new ThreadFactoryC0639d(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /* JADX INFO: renamed from: Fb */
    private static ArrayList<String> m748Fb(String str) throws Throwable {
        BufferedReader bufferedReader;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(str));
            } catch (Exception unused) {
                return null;
            }
        } catch (Exception unused2) {
            bufferedReader = null;
        } catch (Throwable th) {
            th = th;
            bufferedReader = null;
        }
        try {
            ArrayList<String> arrayList = new ArrayList<>();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    bufferedReader.close();
                    return arrayList;
                }
                arrayList.add(line);
            }
        } catch (Exception unused3) {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    /* JADX INFO: renamed from: Qa */
    public static String m749Qa(String str) {
        return C0646k.cn2py(str).substring(0, 1);
    }

    /* JADX INFO: renamed from: a */
    public static void m750a(Context context, C0580g c0580g, String str, ArrayList<C0579f> arrayList, boolean z, a aVar) {
        C0529b.m180d("开始读取：" + str);
        b bVar = new b(context, c0580g, str, arrayList, z);
        bVar.m760a(aVar);
        bVar.executeOnExecutor(pool, new Void[0]);
    }

    /* JADX INFO: renamed from: b */
    public static void m757b(C0580g c0580g, String str, boolean z, a aVar) {
        C0529b.m180d("开始读取：" + str);
        d dVar = new d(c0580g, str, z);
        dVar.m764a(aVar);
        dVar.executeOnExecutor(pool, new Void[0]);
    }

    /* JADX INFO: renamed from: c */
    public static void m759c(ArrayList<C0579f> arrayList) {
        new C0642g(arrayList).start();
    }

    public static boolean delete(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        for (File file2 : file.listFiles()) {
            if (file2.isFile()) {
                if (!file2.delete()) {
                    C0529b.m178a(str + " delete error!");
                    return false;
                }
            } else if (!delete(file2.getAbsolutePath())) {
                return false;
            }
        }
        return file.delete();
    }

    public static boolean isAudio(String str) {
        ArrayList<String> arrayList = mAudioList;
        if (arrayList == null) {
            return false;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            if (str.endsWith(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUpperCase(char c2) {
        return c2 >= 'A' && c2 <= 'Z';
    }

    public static char toLower(char c2) {
        return Character.isUpperCase(c2) ? Character.toLowerCase(c2) : c2;
    }

    /* JADX INFO: renamed from: a */
    public static void m754a(C0580g c0580g, String str, boolean z, a aVar) {
        C0529b.m180d("开始读取：" + str);
        c cVar = new c(c0580g, str, z);
        cVar.m763b(aVar);
        cVar.executeOnExecutor(pool, new Void[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.io.BufferedWriter] */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX INFO: renamed from: b */
    public static void m758b(String str, ArrayList<String> arrayList) throws Throwable {
        BufferedWriter bufferedWriter;
        ?? r0 = 0;
        BufferedWriter bufferedWriter2 = null;
        try {
            try {
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(str));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Exception unused) {
            }
            try {
                Iterator<String> it = arrayList.iterator();
                while (it.hasNext()) {
                    bufferedWriter.write(it.next());
                    bufferedWriter.write(10);
                }
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (Exception unused2) {
                bufferedWriter2 = bufferedWriter;
                new File(str).delete();
                if (bufferedWriter2 != null) {
                    bufferedWriter2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                r0 = bufferedWriter;
                if (r0 != 0) {
                    r0.close();
                }
                throw th;
            }
            r0 = -1;
            FileUtils.setPermissions(str, 438, -1, -1);
        } catch (Exception unused3) {
        }
    }

    /* JADX INFO: renamed from: a */
    public static void m755a(C0580g c0580g, List<C0579f> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String str = list.get(i).mName;
                if (str.length() > 0) {
                    String str2 = "";
                    for (int i2 = 0; i2 < str.length(); i2++) {
                        String strValueOf = String.valueOf(str.charAt(i2));
                        if (strValueOf.matches("[\\u4e00-\\u9fa5]+")) {
                            str2 = str2 + toLower(m749Qa(strValueOf).charAt(0));
                        } else if (isUpperCase(str.charAt(i2))) {
                            str2 = str2 + toLower(str.charAt(i2));
                        } else {
                            str2 = str2 + strValueOf;
                        }
                    }
                    arrayList.add(new C0579f(str2 + "&#" + str, list.get(i).mPath, list.get(i).f539ek));
                }
            }
        }
        if (arrayList.size() > 0) {
            Collections.sort(arrayList, new C0640e(z));
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            String str3 = ((C0579f) arrayList.get(i3)).mName;
            if (str3.contains("&#")) {
                ((C0579f) arrayList.get(i3)).mName = str3.split("&#")[1];
            }
            c0580g.m449a(new C0579f(((C0579f) arrayList.get(i3)).mName, ((C0579f) arrayList.get(i3)).mPath, ((C0579f) arrayList.get(i3)).f539ek));
        }
    }

    /* JADX INFO: renamed from: a */
    public static void m752a(C0578e c0578e, TWUtil tWUtil) {
        new C0641f(c0578e, tWUtil).start();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x005a A[Catch: Exception -> 0x005c, TRY_LEAVE, TryCatch #0 {Exception -> 0x005c, blocks: (B:5:0x0042, B:16:0x0056, B:18:0x005a, B:11:0x004e, B:12:0x0051), top: B:35:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0060  */
    /* JADX INFO: renamed from: a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void m751a(C0578e c0578e) throws Throwable {
        BufferedReader bufferedReader;
        Throwable th;
        String str;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader("/data/tw/music"));
            } catch (Exception unused) {
            }
            try {
                c0578e.f514_j = bufferedReader.readLine();
                c0578e.f515ck = bufferedReader.readLine();
                c0578e.f482Ad = Integer.parseInt(bufferedReader.readLine());
                c0578e.f521md = Integer.parseInt(bufferedReader.readLine());
                c0578e.f517hc = Integer.parseInt(bufferedReader.readLine());
                c0578e.f518ic = Integer.parseInt(bufferedReader.readLine());
            } catch (Exception unused2) {
                if (bufferedReader != null) {
                }
                if (c0578e.f518ic < 1) {
                }
                if (c0578e.f518ic < 1) {
                }
                str = c0578e.f514_j;
                if (str != null) {
                    String str2 = c0578e.f514_j;
                    c0578e.f515ck = str2.substring(0, str2.lastIndexOf("/"));
                }
                C0529b.m180d(c0578e.f514_j);
                C0529b.m180d(c0578e.f515ck);
            } catch (Throwable th2) {
                th = th2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                    throw th;
                }
                throw th;
            }
        } catch (Exception unused3) {
            bufferedReader = null;
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
        }
        bufferedReader.close();
        if (c0578e.f518ic < 1) {
            c0578e.f518ic = 1;
        }
        if (c0578e.f518ic < 1) {
            c0578e.f518ic = 1;
        }
        str = c0578e.f514_j;
        if (str != null && !str.isEmpty() && new File(c0578e.f514_j).canRead() && !new File(c0578e.f515ck).canRead()) {
            String str22 = c0578e.f514_j;
            c0578e.f515ck = str22.substring(0, str22.lastIndexOf("/"));
        }
        C0529b.m180d(c0578e.f514_j);
        C0529b.m180d(c0578e.f515ck);
    }

    /* JADX INFO: renamed from: a */
    public static void m753a(C0580g c0580g, String str, boolean z) throws Throwable {
        BufferedReader bufferedReader;
        String str2;
        C0529b.m180d("loadVolume:" + str);
        if (c0580g == null || str == null) {
            return;
        }
        BufferedReader bufferedReader2 = null;
        try {
            try {
                try {
                    if (!str.startsWith("/storage/usb") && !str.startsWith("/storage/extsd")) {
                        str2 = str + "/DCIM";
                    } else {
                        str2 = "/data/tw/" + str.substring(9);
                    }
                    bufferedReader = new BufferedReader(new FileReader(str2 + "/.music"));
                } catch (Exception e2) {
                    Log.i(TAG, "" + e2.toString());
                    return;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (Throwable th) {
            th = th;
            bufferedReader = bufferedReader2;
        }
        try {
            ArrayList arrayList = new ArrayList();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                File file = new File(str + "/" + line);
                if (file.canRead() && file.isDirectory()) {
                    String name = file.getName();
                    String absolutePath = file.getAbsolutePath();
                    if (name.equals(".")) {
                        String strSubstring = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
                        arrayList.add(new C0579f(strSubstring.substring(strSubstring.lastIndexOf("/") + 1), absolutePath));
                    } else {
                        arrayList.add(new C0579f(name, absolutePath));
                    }
                }
            }
            c0580g.setLength(arrayList.size());
            m755a(c0580g, arrayList, z);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                c0580g.m449a((C0579f) it.next());
            }
            arrayList.clear();
            bufferedReader.close();
        } catch (Exception e4) {
            e = e4;
            bufferedReader2 = bufferedReader;
            Log.i(TAG, "" + e.toString());
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    /* JADX INFO: renamed from: a */
    public static void m756a(String str, C0578e c0578e, boolean z, f fVar) {
        C0529b.m180d("开始扫描id3,盘：" + str);
        e eVar = new e(str, c0578e, z);
        eVar.m767a(fVar);
        eVar.executeOnExecutor(pool, new String[0]);
    }
}
