package com.eckom.xtlibrary.p066b.p069f.p038a;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: ThreadPoolManager.java */
/* renamed from: com.eckom.xtlibrary.b.f.a.c */
/* loaded from: classes3.dex */
public class C0573c {

    /* renamed from: gj */
    public ThreadPoolExecutor f470gj;

    /* renamed from: jj */
    public List<Runnable> f473jj;

    /* renamed from: hj */
    HashMap<String, ThreadPoolExecutor> f471hj = new HashMap<>();

    /* renamed from: ij */
    HashMap<String, ThreadPoolExecutor> f472ij = new HashMap<>();

    /* renamed from: kj */
    HashMap<String, List<Runnable>> f474kj = new HashMap<>();

    /* renamed from: lj */
    HashMap<String, List<Runnable>> f475lj = new HashMap<>();
    private Handler mHandler = new HandlerC0572b(this);

    public C0573c() {
        m441pc();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Db */
    public void m428Db(String str) {
        List<Runnable> m435Ka = m435Ka(str);
        if (m435Ka != null && m435Ka.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("executeWaitThread: threadWait is null =");
            sb.append(m435Ka == null);
            sb.append(",");
            sb.append(str);
            Log.d("ThreadPoolManager", sb.toString());
            if (str.startsWith("/mnt/sdcard")) {
                if (this.f470gj.isShutdown() || this.f470gj.isTerminated()) {
                    m441pc();
                }
            } else if (str.startsWith("/storage/usb")) {
                for (Map.Entry<String, ThreadPoolExecutor> entry : this.f471hj.entrySet()) {
                    ThreadPoolExecutor value = entry.getValue();
                    String key = entry.getKey();
                    if (value.isShutdown() || value.isTerminated()) {
                        m440a(key, true);
                    }
                }
            } else if (str.startsWith("/storage/extsd")) {
                for (Map.Entry<String, ThreadPoolExecutor> entry2 : this.f472ij.entrySet()) {
                    ThreadPoolExecutor value2 = entry2.getValue();
                    String key2 = entry2.getKey();
                    if (value2.isShutdown() || value2.isTerminated()) {
                        m440a(key2, true);
                    }
                }
            }
            Iterator<Runnable> it = m435Ka.iterator();
            while (it.hasNext()) {
                m439a(str, it.next());
            }
            m435Ka.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Eb */
    public void m429Eb(String str) {
        if (str.startsWith("/storage/usb")) {
            for (Map.Entry<String, ThreadPoolExecutor> entry : this.f471hj.entrySet()) {
                ThreadPoolExecutor value = entry.getValue();
                String key = entry.getKey();
                if (value.isShutdown() || value.isTerminated()) {
                    m440a(key, true);
                    List<Runnable> m435Ka = m435Ka(key);
                    if (m435Ka != null && m435Ka.size() > 0) {
                        Iterator<Runnable> it = m435Ka.iterator();
                        while (it.hasNext()) {
                            m439a(str, it.next());
                        }
                        m435Ka.clear();
                    }
                }
            }
            return;
        }
        if (str.startsWith("/storage/extsd")) {
            for (Map.Entry<String, ThreadPoolExecutor> entry2 : this.f472ij.entrySet()) {
                ThreadPoolExecutor value2 = entry2.getValue();
                String key2 = entry2.getKey();
                if (value2.isShutdown() || value2.isTerminated()) {
                    m440a(key2, true);
                }
                List<Runnable> m435Ka2 = m435Ka(key2);
                if (m435Ka2 != null && m435Ka2.size() > 0) {
                    Iterator<Runnable> it2 = m435Ka2.iterator();
                    while (it2.hasNext()) {
                        m439a(str, it2.next());
                    }
                    m435Ka2.clear();
                }
            }
        }
    }

    /* renamed from: Ia */
    public void m433Ia(String str) {
        Log.d("ThreadPoolManager", "addSDPool: key=" + str);
        if (this.f472ij.containsKey(str)) {
            return;
        }
        this.f472ij.put(str, new ThreadPoolExecutor(16, 16, 100L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ThreadPoolExecutor.AbortPolicy()));
    }

    /* renamed from: Ja */
    public ThreadPoolExecutor m434Ja(String str) {
        Log.d("ThreadPoolManager", "getThreadPool: key=" + str);
        if (str.startsWith("/mnt/sdcard")) {
            return this.f470gj;
        }
        if (str.startsWith("/storage/usb")) {
            if (this.f471hj.get(str) == null) {
                m440a(str, false);
            }
            return this.f471hj.get(str);
        }
        if (!str.startsWith("/storage/extsd")) {
            return null;
        }
        if (this.f472ij.get(str) == null) {
            m433Ia(str);
        }
        return this.f472ij.get(str);
    }

    /* renamed from: Ka */
    public List<Runnable> m435Ka(String str) {
        Log.d("ThreadPoolManager", "getWaitThread: key=" + str);
        if (str.startsWith("/mnt/sdcard")) {
            return this.f473jj;
        }
        if (str.startsWith("/storage/usb")) {
            return this.f474kj.get(str);
        }
        if (str.startsWith("/storage/extsd")) {
            return this.f475lj.get(str);
        }
        return null;
    }

    /* renamed from: La */
    public void m436La(String str) {
        Log.d("ThreadPoolManager", "removeSDPool: key=" + str);
        ThreadPoolExecutor threadPoolExecutor = this.f472ij.get(str);
        if (threadPoolExecutor == null || threadPoolExecutor.isTerminated()) {
            return;
        }
        threadPoolExecutor.shutdownNow();
        this.f472ij.remove(str);
    }

    /* renamed from: Ma */
    public void m437Ma(String str) {
        Log.d("ThreadPoolManager", "removeUSBPool: key=" + str);
        ThreadPoolExecutor threadPoolExecutor = this.f471hj.get(str);
        if (threadPoolExecutor == null || threadPoolExecutor.isTerminated()) {
            return;
        }
        threadPoolExecutor.shutdownNow();
        this.f471hj.remove(str);
    }

    /* renamed from: Na */
    public void m438Na(String str) {
        Log.d("ThreadPoolManager", "startNow: key=" + str);
        ThreadPoolExecutor m434Ja = m434Ja(str);
        List<Runnable> m435Ka = m435Ka(str);
        if ((m434Ja != null && m434Ja.getQueue().size() != 0) || (m435Ka != null && m435Ka.size() > 0)) {
            if (str.startsWith("/mnt/sdcard")) {
                for (Map.Entry<String, ThreadPoolExecutor> entry : this.f471hj.entrySet()) {
                    this.f474kj.put(entry.getKey(), entry.getValue().shutdownNow());
                }
                for (Map.Entry<String, ThreadPoolExecutor> entry2 : this.f472ij.entrySet()) {
                    this.f475lj.put(entry2.getKey(), entry2.getValue().shutdownNow());
                }
            } else if (str.startsWith("/storage/usb")) {
                this.f473jj = this.f470gj.shutdownNow();
                for (Map.Entry<String, ThreadPoolExecutor> entry3 : this.f471hj.entrySet()) {
                    ThreadPoolExecutor value = entry3.getValue();
                    String key = entry3.getKey();
                    if (!TextUtils.equals(str, key)) {
                        this.f474kj.put(key, value.shutdownNow());
                    }
                }
                for (Map.Entry<String, ThreadPoolExecutor> entry4 : this.f472ij.entrySet()) {
                    this.f475lj.put(entry4.getKey(), entry4.getValue().shutdownNow());
                }
            } else if (str.startsWith("/storage/extsd")) {
                for (Map.Entry<String, ThreadPoolExecutor> entry5 : this.f472ij.entrySet()) {
                    ThreadPoolExecutor value2 = entry5.getValue();
                    String key2 = entry5.getKey();
                    if (!TextUtils.equals(str, key2)) {
                        this.f475lj.put(key2, value2.shutdownNow());
                    }
                }
                for (Map.Entry<String, ThreadPoolExecutor> entry6 : this.f471hj.entrySet()) {
                    this.f474kj.put(entry6.getKey(), entry6.getValue().shutdownNow());
                }
                this.f473jj = this.f470gj.shutdownNow();
            }
        }
        m428Db(str);
        Message obtain = Message.obtain();
        this.mHandler.removeMessages(65281);
        obtain.what = 65281;
        obtain.obj = str;
        this.mHandler.sendMessageDelayed(obtain, 1000L);
    }

    /* renamed from: pc */
    public void m441pc() {
        this.f470gj = new ThreadPoolExecutor(16, 16, 100L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ThreadPoolExecutor.AbortPolicy());
    }

    /* renamed from: qc */
    public void m442qc() {
        ThreadPoolExecutor threadPoolExecutor = this.f470gj;
        if (threadPoolExecutor != null && !threadPoolExecutor.isTerminated()) {
            this.f470gj.shutdownNow();
        }
        Iterator<Map.Entry<String, ThreadPoolExecutor>> it = this.f471hj.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().shutdownNow();
        }
        Iterator<Map.Entry<String, ThreadPoolExecutor>> it2 = this.f472ij.entrySet().iterator();
        while (it2.hasNext()) {
            it2.next().getValue().shutdownNow();
        }
    }

    /* renamed from: a */
    public void m440a(String str, boolean z) {
        Log.d("ThreadPoolManager", "addUSBPool: key=" + str + ",reAdd=" + z);
        if (!this.f471hj.containsKey(str) || z) {
            this.f471hj.put(str, new ThreadPoolExecutor(16, 16, 100L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ThreadPoolExecutor.AbortPolicy()));
        }
    }

    /* renamed from: a */
    public void m439a(String str, Runnable runnable) {
        Log.d("ThreadPoolManager", "execute: key=" + str);
        ThreadPoolExecutor m434Ja = m434Ja(str);
        List<Runnable> m435Ka = m435Ka(str);
        if (m434Ja != null) {
            Log.d("ThreadPoolManager", "execute: isShutdown=" + m434Ja.isShutdown() + "," + m434Ja.isTerminated());
            if (!m434Ja.isShutdown() && !m434Ja.isTerminated()) {
                m434Ja.execute(runnable);
                return;
            }
            if (m435Ka == null) {
                m435Ka = new ArrayList<>();
            }
            m435Ka.add(runnable);
        }
    }
}
