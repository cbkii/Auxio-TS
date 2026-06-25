package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class NoViableAltForCharException extends RecognitionException {
    public char foundChar;

    public NoViableAltForCharException(char c2, CharScanner charScanner) {
        super("NoViableAlt", charScanner.getFilename(), charScanner.getLine(), charScanner.getColumn());
        this.foundChar = c2;
    }

    public NoViableAltForCharException(char c2, String str, int i) {
        this(c2, str, i, -1);
    }

    public NoViableAltForCharException(char c2, String str, int i, int i2) {
        super("NoViableAlt", str, i, i2);
        this.foundChar = c2;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder m9b;
        char c2 = this.foundChar;
        if (c2 < ' ' || c2 > '~') {
            m9b = C0000a.m9b("unexpected char: ", "0x");
            m9b.append(Integer.toHexString(this.foundChar).toUpperCase());
        } else {
            StringBuilder m5a = C0000a.m5a("unexpected char: '");
            m5a.append(this.foundChar);
            String sb = m5a.toString();
            m9b = new StringBuilder();
            m9b.append(sb);
            m9b.append('\'');
        }
        return m9b.toString();
    }
}
