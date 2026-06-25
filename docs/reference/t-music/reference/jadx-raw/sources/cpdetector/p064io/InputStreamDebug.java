package cpdetector.p064io;

import java.io.InputStream;

/* loaded from: classes4.dex */
public class InputStreamDebug extends InputStream {
    public InputStream m_delegate;

    public InputStreamDebug(InputStream inputStream) {
        this.m_delegate = inputStream;
    }

    @Override // java.io.InputStream
    public int available() {
        return this.m_delegate.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.m_delegate.close();
    }

    public boolean equals(Object obj) {
        return this.m_delegate.equals(obj);
    }

    public int hashCode() {
        return this.m_delegate.hashCode();
    }

    @Override // java.io.InputStream
    public void mark(int i) {
        this.m_delegate.mark(i);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.m_delegate.markSupported();
    }

    @Override // java.io.InputStream
    public synchronized int read() {
        int read;
        read = this.m_delegate.read();
        System.out.print((char) read);
        return read;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) {
        int read = read();
        if (read == -1) {
            return read;
        }
        bArr[i] = (byte) read;
        return 1;
    }

    @Override // java.io.InputStream
    public void reset() {
        this.m_delegate.reset();
    }

    @Override // java.io.InputStream
    public long skip(long j) {
        return this.m_delegate.skip(j);
    }

    public String toString() {
        return this.m_delegate.toString();
    }
}
