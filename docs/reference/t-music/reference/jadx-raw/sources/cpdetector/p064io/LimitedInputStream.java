package cpdetector.p064io;

import java.io.FilterInputStream;
import java.io.InputStream;

/* loaded from: classes4.dex */
public class LimitedInputStream extends FilterInputStream {
    public int m_amountOfBytesReadable;

    public LimitedInputStream(InputStream inputStream, int i) {
        super(inputStream);
        this.m_amountOfBytesReadable = i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() {
        if (this.m_amountOfBytesReadable == 0) {
            return 0;
        }
        int available = super.available();
        int i = this.m_amountOfBytesReadable;
        return i < available ? i : available;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() {
        if (this.m_amountOfBytesReadable == 0) {
            return -1;
        }
        int read = super.read();
        if (read >= 0) {
            this.m_amountOfBytesReadable--;
        }
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) {
        int i3 = this.m_amountOfBytesReadable;
        if (i3 == 0) {
            return -1;
        }
        if (i3 < i2) {
            i2 = i3;
        }
        int read = super.read(bArr, i, i2);
        if (read > 0) {
            this.m_amountOfBytesReadable -= read;
        }
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j) {
        if (this.m_amountOfBytesReadable == 0) {
            return 0L;
        }
        long skip = super.skip(j);
        this.m_amountOfBytesReadable = (int) (this.m_amountOfBytesReadable - skip);
        return skip;
    }
}
