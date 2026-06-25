package cpdetector.p064io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes4.dex */
public class LimitedInputStream extends FilterInputStream {
    public int m_amountOfBytesReadable;

    public LimitedInputStream(InputStream inputStream, int i) {
        super(inputStream);
        this.m_amountOfBytesReadable = i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        if (this.m_amountOfBytesReadable == 0) {
            return 0;
        }
        int iAvailable = super.available();
        int i = this.m_amountOfBytesReadable;
        return i < iAvailable ? i : iAvailable;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.m_amountOfBytesReadable == 0) {
            return -1;
        }
        int i = super.read();
        if (i >= 0) {
            this.m_amountOfBytesReadable--;
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3 = this.m_amountOfBytesReadable;
        if (i3 == 0) {
            return -1;
        }
        if (i3 < i2) {
            i2 = i3;
        }
        int i4 = super.read(bArr, i, i2);
        if (i4 > 0) {
            this.m_amountOfBytesReadable -= i4;
        }
        return i4;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j) throws IOException {
        if (this.m_amountOfBytesReadable == 0) {
            return 0L;
        }
        long jSkip = super.skip(j);
        this.m_amountOfBytesReadable = (int) (((long) this.m_amountOfBytesReadable) - jSkip);
        return jSkip;
    }
}
