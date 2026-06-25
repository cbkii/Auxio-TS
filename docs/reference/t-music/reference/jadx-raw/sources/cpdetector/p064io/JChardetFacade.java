package cpdetector.p064io;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import p065d.p066a.p067a.p068a.C0812h;
import p065d.p066a.p067a.p068a.InterfaceC0821q;

/* loaded from: classes4.dex */
public final class JChardetFacade extends AbstractCodepageDetector implements InterfaceC0821q {
    public static C0812h det;
    public static JChardetFacade instance;
    public int amountOfVerifiers;
    public byte[] buf = new byte[4096];
    public Charset codpage = null;
    public boolean m_guessing = true;

    public JChardetFacade() {
        this.amountOfVerifiers = 0;
        det = new C0812h(0);
        det.m1564a(this);
        this.amountOfVerifiers = det.m1576ge().length;
    }

    public static JChardetFacade getInstance() {
        if (instance == null) {
            instance = new JChardetFacade();
        }
        return instance;
    }

    private Charset guess() {
        String[] m1576ge = det.m1576ge();
        if (m1576ge.length == this.amountOfVerifiers) {
            return Charset.forName("US-ASCII");
        }
        if (m1576ge[0].equalsIgnoreCase("nomatch")) {
            return UnknownCharset.getInstance();
        }
        Charset charset = null;
        for (int i = 0; charset == null && i < m1576ge.length; i++) {
            try {
                charset = Charset.forName(m1576ge[i]);
            } catch (UnsupportedCharsetException unused) {
                charset = UnsupportedCharset.forName(m1576ge[i]);
            }
        }
        return charset;
    }

    @Override // p065d.p066a.p067a.p068a.InterfaceC0821q
    public void Notify(String str) {
        this.codpage = Charset.forName(str);
    }

    public void Reset() {
        det.Reset();
        this.codpage = null;
    }

    @Override // cpdetector.p064io.ICodepageDetector
    public synchronized Charset detectCodepage(InputStream inputStream, int i) {
        Charset charset;
        Reset();
        int i2 = 0;
        boolean z = false;
        do {
            byte[] bArr = this.buf;
            int read = inputStream.read(bArr, 0, Math.min(bArr.length, i - i2));
            if (read > 0) {
                i2 += read;
            }
            if (!z) {
                z = det.m1565b(this.buf, read, false);
            }
            if (read <= 0) {
                break;
            }
        } while (!z);
        det.m1575fe();
        charset = this.codpage;
        if (charset == null) {
            charset = this.m_guessing ? guess() : UnknownCharset.getInstance();
        }
        return charset;
    }

    public boolean isGuessing() {
        return this.m_guessing;
    }

    public synchronized void setGuessing(boolean z) {
        this.m_guessing = z;
    }
}
