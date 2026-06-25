package cpdetector.p074io;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public class HTMLCodepageDetector extends AbstractCodepageDetector {
    public static final long serialVersionUID = 3258135756131022643L;
    public ParsingDetector delegate;

    public HTMLCodepageDetector() {
        this(false);
    }

    public HTMLCodepageDetector(boolean z) {
        this.delegate = new ParsingDetector(z);
    }

    @Override // cpdetector.p074io.AbstractCodepageDetector, java.lang.Comparable
    public int compareTo(Object obj) {
        return this.delegate.compareTo(obj);
    }

    @Override // cpdetector.p074io.ICodepageDetector
    public Charset detectCodepage(InputStream inputStream, int i) {
        return this.delegate.detectCodepage(inputStream, i);
    }

    @Override // cpdetector.p074io.AbstractCodepageDetector, cpdetector.p074io.ICodepageDetector
    public Charset detectCodepage(URL url) {
        return this.delegate.detectCodepage(url);
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public String toString() {
        return this.delegate.toString();
    }
}
