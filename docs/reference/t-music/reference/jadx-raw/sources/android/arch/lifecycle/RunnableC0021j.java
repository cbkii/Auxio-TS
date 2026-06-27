package android.arch.lifecycle;

/* compiled from: LiveData.java */
/* renamed from: android.arch.lifecycle.j */
/* loaded from: classes.dex */
class RunnableC0021j implements Runnable {
    final /* synthetic */ LiveData this$0;

    RunnableC0021j(LiveData liveData) {
        this.this$0 = liveData;
    }

    @Override // java.lang.Runnable
    public void run() {
        Object obj;
        Object obj2;
        Object obj3;
        obj = this.this$0.mDataLock;
        synchronized (obj) {
            obj2 = this.this$0.mPendingData;
            LiveData liveData = this.this$0;
            obj3 = LiveData.NOT_SET;
            liveData.mPendingData = obj3;
        }
        this.this$0.setValue(obj2);
    }
}
