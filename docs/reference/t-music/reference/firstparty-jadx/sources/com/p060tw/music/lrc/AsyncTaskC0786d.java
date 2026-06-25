package com.p060tw.music.lrc;

import android.os.AsyncTask;
import java.util.List;

/* compiled from: LrcView.java */
/* renamed from: com.tw.music.lrc.d */
/* loaded from: classes3.dex */
class AsyncTaskC0786d extends AsyncTask<String, Integer, List<C0783a>> {
    final /* synthetic */ RunnableC0787e this$1;

    AsyncTaskC0786d(RunnableC0787e runnableC0787e) {
        this.this$1 = runnableC0787e;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public List<C0783a> doInBackground(String... strArr) {
        return C0783a.m1504ob(strArr[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public void onPostExecute(List<C0783a> list) {
        Object flag;
        flag = this.this$1.this$0.getFlag();
        RunnableC0787e runnableC0787e = this.this$1;
        if (flag == runnableC0787e.f1171Hm) {
            runnableC0787e.this$0.m1495f((List<C0783a>) list);
            this.this$1.this$0.setFlag(null);
        }
    }
}
