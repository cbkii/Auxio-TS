package cpdetector.p074io;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public interface ICodepageDetector extends Serializable, Comparable {
    Charset detectCodepage(InputStream inputStream, int i);

    Charset detectCodepage(URL url);

    Reader open(URL url);
}
