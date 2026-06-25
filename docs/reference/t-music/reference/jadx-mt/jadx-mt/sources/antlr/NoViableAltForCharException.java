package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
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
        StringBuilder sbM9b;
        char c2 = this.foundChar;
        if (c2 < ' ' || c2 > '~') {
            sbM9b = C0000a.m9b("unexpected char: ", "0x");
            sbM9b.append(Integer.toHexString(this.foundChar).toUpperCase());
        } else {
            StringBuilder sbM5a = C0000a.m5a("unexpected char: '");
            sbM5a.append(this.foundChar);
            String string = sbM5a.toString();
            sbM9b = new StringBuilder();
            sbM9b.append(string);
            sbM9b.append('\'');
        }
        return sbM9b.toString();
    }
}
