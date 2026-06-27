package cpdetector.p064io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/* JADX INFO: loaded from: classes4.dex */
public class ByteOrderMarkDetector extends AbstractCodepageDetector implements ICodepageDetector {
    public static final long serialVersionUID = 3618977875919778866L;

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0037, code lost:
    
        return cpdetector.p064io.UnsupportedCharset.forName("UTF-16LE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0047, code lost:
    
        return cpdetector.p064io.UnsupportedCharset.forName("UTF-16LE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x006a, code lost:
    
        return cpdetector.p064io.UnsupportedCharset.forName("UTF-16BE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x007a, code lost:
    
        return cpdetector.p064io.UnsupportedCharset.forName("UTF-16BE");
     */
    @Override // cpdetector.p064io.ICodepageDetector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Charset detectCodepage(InputStream inputStream, int i) throws IOException {
        Charset unknownCharset = UnknownCharset.getInstance();
        int i2 = inputStream.read();
        if (i2 == 0) {
            if (inputStream.read() != 0) {
                return unknownCharset;
            }
            int i3 = inputStream.read();
            if (i3 == 254) {
                try {
                    return Charset.forName("UCS-4BE");
                } catch (UnsupportedCharsetException unused) {
                    return UnsupportedCharset.forName("UCS-4BE");
                }
            }
            if (i3 != 255) {
                return unknownCharset;
            }
            try {
                return Charset.forName("UCS-4");
            } catch (UnsupportedCharsetException unused2) {
                return UnsupportedCharset.forName("UCS-4");
            }
        }
        if (i2 == 239) {
            if (inputStream.read() != 187 || inputStream.read() != 191) {
                return unknownCharset;
            }
            try {
                return Charset.forName("utf-8");
            } catch (UnsupportedCharsetException unused3) {
                return UnsupportedCharset.forName("utf-8");
            }
        }
        if (i2 == 254) {
            if (inputStream.read() != 255) {
                return unknownCharset;
            }
            if (inputStream.read() == 0 && inputStream.read() == 0) {
                try {
                    return Charset.forName("UCS-4");
                } catch (UnsupportedCharsetException unused4) {
                    return UnsupportedCharset.forName("UCS-4");
                }
            }
            return Charset.forName("UTF-16BE");
        }
        if (i2 != 255 || inputStream.read() != 254) {
            return unknownCharset;
        }
        if (inputStream.read() == 0 && inputStream.read() == 0) {
            try {
                return Charset.forName("UCS-4LE");
            } catch (UnsupportedCharsetException unused5) {
                return UnsupportedCharset.forName("UCS-4LE");
            }
        }
        return Charset.forName("UTF-16LE");
    }

    @Override // cpdetector.p064io.AbstractCodepageDetector, cpdetector.p064io.ICodepageDetector
    public Charset detectCodepage(URL url) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        Charset charsetDetectCodepage = detectCodepage(bufferedInputStream, Integer.MAX_VALUE);
        bufferedInputStream.close();
        return charsetDetectCodepage;
    }
}
