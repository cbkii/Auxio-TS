package cpdetector.p074io;

import antlr.ANTLRException;
import cpdetector.p074io.parser.EncodingLexer;
import cpdetector.p074io.parser.EncodingParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes4.dex */
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
    /* JADX WARN: Type inference failed for: r4v10, types: [java.nio.charset.Charset] */
    /* JADX WARN: Type inference failed for: r4v7, types: [java.nio.charset.Charset] */
    /* JADX WARN: Type inference failed for: r4v8, types: [java.nio.charset.Charset] */
    /* JADX WARN: Type inference failed for: r5v8, types: [java.nio.charset.Charset] */
    @Override // cpdetector.p074io.ICodepageDetector
    public Charset detectCodepage(InputStream inputStream, int i) {
        String htmlDocument;
        LimitedInputStream limitedInputStream = new LimitedInputStream(inputStream, i);
        if (this.m_verbose) {
            System.out.println("  parsing for html-charset/xml-encoding attribute with codepage: US-ASCII");
        }
        String str = null;
        try {
            try {
                htmlDocument = new EncodingParser(new EncodingLexer(new InputStreamReader(limitedInputStream, "US-ASCII"))).htmlDocument();
            } catch (ANTLRException e) {
                if (!this.m_verbose) {
                    return null;
                }
                PrintStream printStream = System.out;
                StringBuilder m5a = C0000a.m5a("  ANTLR parser exception: ");
                m5a.append(e.getMessage());
                printStream.println(m5a.toString());
                return null;
            }
        } catch (Exception e2) {
            e = e2;
        }
        try {
            if (htmlDocument != null) {
                try {
                    this = Charset.forName(htmlDocument);
                } catch (UnsupportedCharsetException unused) {
                    this = UnsupportedCharset.forName(htmlDocument);
                }
            } else {
                this = UnknownCharset.getInstance();
            }
            return this;
        } catch (Exception e3) {
            e = e3;
            str = htmlDocument;
            if (this.m_verbose) {
                PrintStream printStream2 = System.out;
                StringBuilder m5a2 = C0000a.m5a("  Decoding Exception: ");
                m5a2.append(e.getMessage());
                m5a2.append(" (unsupported java charset).");
                printStream2.println(m5a2.toString());
            }
            return str != null ? UnsupportedCharset.forName(str) : UnknownCharset.getInstance();
        }
    }
}
