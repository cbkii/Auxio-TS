package antlr;

/* loaded from: classes3.dex */
public class ANTLRHashString {
    public static final int prime = 151;
    public char[] buf;
    public int len;
    public CharScanner lexer;

    /* renamed from: s */
    public String f297s;

    public ANTLRHashString(CharScanner charScanner) {
        this.lexer = charScanner;
    }

    public ANTLRHashString(String str, CharScanner charScanner) {
        this.lexer = charScanner;
        setString(str);
    }

    public ANTLRHashString(char[] cArr, int i, CharScanner charScanner) {
        this.lexer = charScanner;
        setBuffer(cArr, i);
    }

    private final char charAt(int i) {
        String str = this.f297s;
        return str != null ? str.charAt(i) : this.buf[i];
    }

    private final int length() {
        String str = this.f297s;
        return str != null ? str.length() : this.len;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ANTLRHashString) && !(obj instanceof String)) {
            return false;
        }
        ANTLRHashString aNTLRHashString = obj instanceof String ? new ANTLRHashString((String) obj, this.lexer) : (ANTLRHashString) obj;
        int length = length();
        if (aNTLRHashString.length() != length) {
            return false;
        }
        if (this.lexer.getCaseSensitiveLiterals()) {
            for (int i = 0; i < length; i++) {
                if (charAt(i) != aNTLRHashString.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (this.lexer.toLower(charAt(i2)) != this.lexer.toLower(aNTLRHashString.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i;
        int length = length();
        int i2 = 0;
        if (this.lexer.getCaseSensitiveLiterals()) {
            i = 0;
            while (i2 < length) {
                i = (i * 151) + charAt(i2);
                i2++;
            }
        } else {
            i = 0;
            while (i2 < length) {
                i = (i * 151) + this.lexer.toLower(charAt(i2));
                i2++;
            }
        }
        return i;
    }

    public void setBuffer(char[] cArr, int i) {
        this.buf = cArr;
        this.len = i;
        this.f297s = null;
    }

    public void setString(String str) {
        this.f297s = str;
        this.buf = null;
    }
}
