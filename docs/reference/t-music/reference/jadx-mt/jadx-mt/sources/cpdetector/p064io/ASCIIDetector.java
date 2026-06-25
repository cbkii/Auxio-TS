package cpdetector.p064io;

import cpdetector.util.FileUtil;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes4.dex */
public final class ASCIIDetector extends AbstractCodepageDetector {
    public static ICodepageDetector instance = null;
    public static final long serialVersionUID = 3760841259903824181L;

    public static ICodepageDetector getInstance() {
        if (instance == null) {
            instance = new ASCIIDetector();
        }
        return instance;
    }

    @Override // cpdetector.p064io.ICodepageDetector
    public Charset detectCodepage(InputStream inputStream, int i) {
        Charset unknownCharset = UnknownCharset.getInstance();
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream, 4096);
        }
        return FileUtil.isAllASCII(inputStream) ? Charset.forName("US-ASCII") : unknownCharset;
    }
}
