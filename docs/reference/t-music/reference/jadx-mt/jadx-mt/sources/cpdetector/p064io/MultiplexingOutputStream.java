package cpdetector.p064io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes4.dex */
public class MultiplexingOutputStream extends OutputStream {
    public List<OutputStream> m_delegates = new LinkedList();

    public MultiplexingOutputStream(OutputStream outputStream, OutputStream outputStream2) {
        this.m_delegates.add(outputStream);
        this.m_delegates.add(outputStream2);
    }

    public void addOutputStream(OutputStream outputStream) {
        this.m_delegates.add(outputStream);
    }

    public boolean removeOutputStream(OutputStream outputStream) {
        return this.m_delegates.remove(outputStream);
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        Iterator<OutputStream> it = this.m_delegates.iterator();
        while (it.hasNext()) {
            it.next().write(i);
        }
    }
}
