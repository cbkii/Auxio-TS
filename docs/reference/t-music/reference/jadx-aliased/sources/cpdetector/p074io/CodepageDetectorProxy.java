package cpdetector.p074io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes4.dex */
public final class CodepageDetectorProxy extends AbstractCodepageDetector {
    public static CodepageDetectorProxy instance = null;
    public static final long serialVersionUID = -7389424614984024701L;
    public Set<ICodepageDetector> detectors = new LinkedHashSet();

    public static CodepageDetectorProxy getInstance() {
        if (instance == null) {
            instance = new CodepageDetectorProxy();
        }
        return instance;
    }

    public boolean add(ICodepageDetector iCodepageDetector) {
        return this.detectors.add(iCodepageDetector);
    }

    public void clearDetectors() {
        this.detectors.clear();
    }

    @Override // cpdetector.p074io.ICodepageDetector
    public Charset detectCodepage(InputStream inputStream, int i) {
        if (!inputStream.markSupported()) {
            StringBuilder m5a = C0000a.m5a("The given input stream (");
            m5a.append(inputStream.getClass().getName());
            m5a.append(") has to support for marking.");
            throw new IllegalArgumentException(m5a.toString());
        }
        Charset charset = null;
        Iterator<ICodepageDetector> it = this.detectors.iterator();
        while (it.hasNext()) {
            inputStream.mark(i);
            charset = it.next().detectCodepage(inputStream, i);
            try {
                inputStream.reset();
                if (charset != null && charset != UnknownCharset.getInstance() && !(charset instanceof UnsupportedCharset)) {
                    break;
                }
            } catch (IOException e) {
                IllegalStateException illegalStateException = new IllegalStateException("More than the given length had to be read and the given stream could not be reset. Undetermined state for this detection.");
                illegalStateException.initCause(e);
                throw illegalStateException;
            }
        }
        return charset;
    }

    @Override // cpdetector.p074io.AbstractCodepageDetector, cpdetector.p074io.ICodepageDetector
    public Charset detectCodepage(URL url) {
        Iterator<ICodepageDetector> it = this.detectors.iterator();
        Charset charset = null;
        while (it.hasNext() && ((charset = it.next().detectCodepage(url)) == null || charset == UnknownCharset.getInstance() || (charset instanceof UnsupportedCharset))) {
        }
        return charset;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<ICodepageDetector> it = this.detectors.iterator();
        int i = 1;
        while (it.hasNext()) {
            stringBuffer.append(i);
            stringBuffer.append(") ");
            stringBuffer.append(it.next().getClass().getName());
            stringBuffer.append("\n");
            i++;
        }
        return stringBuffer.toString();
    }
}
