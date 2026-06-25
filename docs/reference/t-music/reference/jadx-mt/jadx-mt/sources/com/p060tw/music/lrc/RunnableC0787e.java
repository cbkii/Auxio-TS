package com.p060tw.music.lrc;

/* JADX INFO: renamed from: com.tw.music.lrc.e */
/* JADX INFO: compiled from: LrcView.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0787e implements Runnable {

    /* JADX INFO: renamed from: Hm */
    final /* synthetic */ String f1171Hm;
    final /* synthetic */ LrcView this$0;

    RunnableC0787e(LrcView lrcView, String str) {
        this.this$0 = lrcView;
        this.f1171Hm = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.this$0.reset();
        this.this$0.setFlag(this.f1171Hm);
        new AsyncTaskC0786d(this).execute(this.f1171Hm);
    }
}
