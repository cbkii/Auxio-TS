package android.arch.lifecycle;

/* JADX INFO: renamed from: android.arch.lifecycle.j */
/* JADX INFO: compiled from: LiveData.java */
/* JADX INFO: loaded from: classes.dex */
class RunnableC0021j implements Runnable {
    final /* synthetic */ LiveData this$0;

    RunnableC0021j(LiveData liveData) {
        this.this$0 = liveData;
    }

    @Override // java.lang.Runnable
    public void run() {
        Object obj;
        synchronized (this.this$0.mDataLock) {
            obj = this.this$0.mPendingData;
            this.this$0.mPendingData = LiveData.NOT_SET;
        }
        this.this$0.setValue(obj);
    }
}
