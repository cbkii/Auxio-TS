package cpdetector.p064io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public class UnsupportedCharset extends Charset {
    public static Map<String, Charset> singletons = new HashMap();
    public String m_name;

    public UnsupportedCharset(String str) {
        super("unsupported", null);
    }

    public static Charset forName(String str) {
        Charset charset = singletons.get(str);
        if (charset != null) {
            return charset;
        }
        UnsupportedCharset unsupportedCharset = new UnsupportedCharset(str);
        singletons.put(str, unsupportedCharset);
        return unsupportedCharset;
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return false;
    }

    @Override // java.nio.charset.Charset
    public String displayName() {
        return this.m_name;
    }

    @Override // java.nio.charset.Charset
    public String displayName(Locale locale) {
        return this.m_name;
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
