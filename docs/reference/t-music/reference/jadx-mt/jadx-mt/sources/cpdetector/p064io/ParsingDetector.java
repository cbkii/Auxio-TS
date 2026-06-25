package cpdetector.p064io;

import antlr.ANTLRException;
import cpdetector.p064io.parser.EncodingLexer;
import cpdetector.p064io.parser.EncodingParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes4.dex */
public class ParsingDetector extends AbstractCodepageDetector {
    public static final long serialVersionUID = 3618977875919778866L;
    public boolean m_verbose;

    public ParsingDetector() {
        this(false);
    }

    public ParsingDetector(boolean z) {
        this.m_verbose = false;
        this.m_verbose = z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [cpdetector.io.ParsingDetector] */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v12 */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v3, types: [cpdetector.io.ParsingDetector] */
    /* JADX WARN: Type inference failed for: r4v6 */
    /* JADX WARN: Type inference failed for: r4v9 */
    /* JADX WARN: Type inference failed for: r5v8, types: [java.nio.charset.Charset] */
    @Override // cpdetector.p064io.ICodepageDetector
    public Charset detectCodepage(InputStream inputStream, int i) {
        String strHtmlDocument;
        LimitedInputStream limitedInputStream = new LimitedInputStream(inputStream, i);
        if (this.m_verbose) {
            System.out.println("  parsing for html-charset/xml-encoding attribute with codepage: US-ASCII");
        }
        String str = null;
        try {
            try {
                strHtmlDocument = new EncodingParser(new EncodingLexer(new InputStreamReader(limitedInputStream, "US-ASCII"))).htmlDocument();
            } catch (ANTLRException e) {
                if (!this.m_verbose) {
                    return null;
                }
                PrintStream printStream = System.out;
                StringBuilder sbM5a = C0000a.m5a("  ANTLR parser exception: ");
                sbM5a.append(e.getMessage());
                printStream.println(sbM5a.toString());
                return null;
            }
        } catch (Exception e2) {
            e = e2;
        }
        try {
            if (strHtmlDocument != null) {
                try {
                    this = Charset.forName(strHtmlDocument);
                } catch (UnsupportedCharsetException unused) {
                    this = UnsupportedCharset.forName(strHtmlDocument);
                }
            } else {
                this = UnknownCharset.getInstance();
            }
            return this;
        } catch (Exception e3) {
            e = e3;
            str = strHtmlDocument;
            if (this.m_verbose) {
                PrintStream printStream2 = System.out;
                StringBuilder sbM5a2 = C0000a.m5a("  Decoding Exception: ");
                sbM5a2.append(e.getMessage());
                sbM5a2.append(" (unsupported java charset).");
                printStream2.println(sbM5a2.toString());
            }
            return str != null ? UnsupportedCharset.forName(str) : UnknownCharset.getInstance();
        }
    }
}
