package cpdetector.p064io;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import p065d.p066a.p067a.p068a.C0812h;
import p065d.p066a.p067a.p068a.InterfaceC0821q;

/* JADX INFO: loaded from: classes4.dex */
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
        String[] strArrM1576ge = det.m1576ge();
        if (strArrM1576ge.length == this.amountOfVerifiers) {
            return Charset.forName("US-ASCII");
        }
        if (strArrM1576ge[0].equalsIgnoreCase("nomatch")) {
            return UnknownCharset.getInstance();
        }
        Charset charsetForName = null;
        for (int i = 0; charsetForName == null && i < strArrM1576ge.length; i++) {
            try {
                charsetForName = Charset.forName(strArrM1576ge[i]);
            } catch (UnsupportedCharsetException unused) {
                charsetForName = UnsupportedCharset.forName(strArrM1576ge[i]);
            }
        }
        return charsetForName;
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
        Charset charsetGuess;
        Reset();
        int i2 = 0;
        boolean zM1565b = false;
        do {
            byte[] bArr = this.buf;
            int i3 = inputStream.read(bArr, 0, Math.min(bArr.length, i - i2));
            if (i3 > 0) {
                i2 += i3;
            }
            if (!zM1565b) {
                zM1565b = det.m1565b(this.buf, i3, false);
            }
            if (i3 <= 0) {
                break;
            }
        } while (!zM1565b);
        det.m1575fe();
        charsetGuess = this.codpage;
        if (charsetGuess == null) {
            charsetGuess = this.m_guessing ? guess() : UnknownCharset.getInstance();
        }
        return charsetGuess;
    }

    public boolean isGuessing() {
        return this.m_guessing;
    }

    public synchronized void setGuessing(boolean z) {
        this.m_guessing = z;
    }
}
