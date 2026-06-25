package antlr;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes3.dex */
public class ByteBuffer extends InputBuffer {
    public transient InputStream input;

    public ByteBuffer(InputStream inputStream) {
        this.input = inputStream;
    }

    @Override // antlr.InputBuffer
    public void fill(int i) {
        try {
            syncConsume();
            while (this.queue.nbrEntries < this.markerOffset + i) {
                this.queue.append((char) this.input.read());
            }
        } catch (IOException e) {
            throw new CharStreamIOException(e);
        }
    }
}
