package antlr;

import java.io.IOException;
import java.io.Reader;

/* JADX INFO: loaded from: classes3.dex */
public class CharBuffer extends InputBuffer {
    public transient Reader input;

    public CharBuffer(Reader reader) {
        this.input = reader;
    }

    @Override // antlr.InputBuffer
    public void fill(int i) throws CharStreamIOException {
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
