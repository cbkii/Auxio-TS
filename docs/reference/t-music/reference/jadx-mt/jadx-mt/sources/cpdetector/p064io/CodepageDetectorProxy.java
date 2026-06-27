package cpdetector.p064io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes4.dex */
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

    @Override // cpdetector.p064io.ICodepageDetector
    public Charset detectCodepage(InputStream inputStream, int i) {
        if (!inputStream.markSupported()) {
            StringBuilder sbM5a = C0000a.m5a("The given input stream (");
            sbM5a.append(inputStream.getClass().getName());
            sbM5a.append(") has to support for marking.");
            throw new IllegalArgumentException(sbM5a.toString());
        }
        Charset charsetDetectCodepage = null;
        Iterator<ICodepageDetector> it = this.detectors.iterator();
        while (it.hasNext()) {
            inputStream.mark(i);
            charsetDetectCodepage = it.next().detectCodepage(inputStream, i);
            try {
                inputStream.reset();
                if (charsetDetectCodepage != null && charsetDetectCodepage != UnknownCharset.getInstance() && !(charsetDetectCodepage instanceof UnsupportedCharset)) {
                    break;
                }
            } catch (IOException e) {
                IllegalStateException illegalStateException = new IllegalStateException("More than the given length had to be read and the given stream could not be reset. Undetermined state for this detection.");
                illegalStateException.initCause(e);
                throw illegalStateException;
            }
        }
        return charsetDetectCodepage;
    }

    @Override // cpdetector.p064io.AbstractCodepageDetector, cpdetector.p064io.ICodepageDetector
    public Charset detectCodepage(URL url) {
        Iterator<ICodepageDetector> it = this.detectors.iterator();
        Charset charsetDetectCodepage = null;
        while (it.hasNext() && ((charsetDetectCodepage = it.next().detectCodepage(url)) == null || charsetDetectCodepage == UnknownCharset.getInstance() || (charsetDetectCodepage instanceof UnsupportedCharset))) {
        }
        return charsetDetectCodepage;
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
