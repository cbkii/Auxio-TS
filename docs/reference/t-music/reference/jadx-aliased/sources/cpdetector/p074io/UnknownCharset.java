package cpdetector.p074io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/* loaded from: classes4.dex */
public class UnknownCharset extends Charset {
    public static Charset instance;

    public UnknownCharset() {
        super("void", null);
    }

    public static Charset getInstance() {
        if (instance == null) {
            instance = new UnknownCharset();
        }
        return instance;
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return false;
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        throw new UnsupportedOperationException("This is no real Charset but a flag you should test for!");
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        throw new UnsupportedOperationException("This is no real Charset but a flag you should test for!");
    }
}
