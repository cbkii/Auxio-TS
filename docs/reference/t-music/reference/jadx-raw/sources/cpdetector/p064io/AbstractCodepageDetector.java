package cpdetector.p064io;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public abstract class AbstractCodepageDetector implements ICodepageDetector {
    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        return getClass().getName().compareTo(obj.getClass().getName());
    }

    @Override // cpdetector.p064io.ICodepageDetector
    public Charset detectCodepage(URL url) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        Charset detectCodepage = detectCodepage(bufferedInputStream, Integer.MAX_VALUE);
        bufferedInputStream.close();
        return detectCodepage;
    }

    @Override // cpdetector.p064io.ICodepageDetector
    public final Reader open(URL url) {
        Charset detectCodepage = detectCodepage(url);
        if (detectCodepage != null) {
            return new InputStreamReader(new BufferedInputStream(url.openStream()), detectCodepage);
        }
        return null;
    }
}
