package android.support.v4.media;

import java.io.Closeable;

/* JADX INFO: loaded from: classes3.dex */
public abstract class Media2DataSource implements Closeable {
    public abstract long getSize();

    public abstract int readAt(long j, byte[] bArr, int i, int i2);
}
