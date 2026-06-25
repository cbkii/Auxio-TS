package cpdetector.p074io;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public class UnicodeDetector extends AbstractCodepageDetector {
    public static ICodepageDetector instance;

    public static ICodepageDetector getInstance() {
        if (instance == null) {
            instance = new UnicodeDetector();
        }
        return instance;
    }

    @Override // cpdetector.p074io.ICodepageDetector
    public Charset detectCodepage(InputStream inputStream, int i) {
        String str;
        byte[] bArr = new byte[4];
        inputStream.read(bArr, 0, 4);
        if (bArr[0] == 0 && bArr[1] == 0 && bArr[2] == -2 && bArr[2] == -1) {
            str = "UTF-32BE";
        } else if (bArr[0] == -1 && bArr[1] == -2 && bArr[2] == 0 && bArr[2] == 0) {
            str = "UTF-32LE";
        } else if (bArr[0] == -17 && bArr[1] == -69 && bArr[2] == -65) {
            str = "UTF-8";
        } else if (bArr[0] == -1 && bArr[1] == -2) {
            str = "UTF-16LE";
        } else if (bArr[0] == -2 && bArr[1] == -1) {
            str = "UTF-16BE";
        } else {
            if (bArr[0] != 0 || bArr[1] != 0 || bArr[2] != -2 || bArr[3] != -1) {
                return UnknownCharset.getInstance();
            }
            str = "UCS-4";
        }
        return Charset.forName(str);
    }

    @Override // cpdetector.p074io.AbstractCodepageDetector, cpdetector.p074io.ICodepageDetector
    public Charset detectCodepage(URL url) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        Charset detectCodepage = detectCodepage(bufferedInputStream, Integer.MAX_VALUE);
        bufferedInputStream.close();
        return detectCodepage;
    }
}
