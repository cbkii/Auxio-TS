package cpdetector.p064io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractCodepageDetector implements ICodepageDetector {
    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        return getClass().getName().compareTo(obj.getClass().getName());
    }

    @Override // cpdetector.p064io.ICodepageDetector
    public Charset detectCodepage(URL url) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        Charset charsetDetectCodepage = detectCodepage(bufferedInputStream, Integer.MAX_VALUE);
        bufferedInputStream.close();
        return charsetDetectCodepage;
    }

    @Override // cpdetector.p064io.ICodepageDetector
    public final Reader open(URL url) throws IOException {
        Charset charsetDetectCodepage = detectCodepage(url);
        if (charsetDetectCodepage != null) {
            return new InputStreamReader(new BufferedInputStream(url.openStream()), charsetDetectCodepage);
        }
        return null;
    }
}
