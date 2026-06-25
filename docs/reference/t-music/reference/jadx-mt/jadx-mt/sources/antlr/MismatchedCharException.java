package antlr;

import antlr.collections.impl.BitSet;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class MismatchedCharException extends RecognitionException {
    public static final int CHAR = 1;
    public static final int NOT_CHAR = 2;
    public static final int NOT_RANGE = 4;
    public static final int NOT_SET = 6;
    public static final int RANGE = 3;
    public static final int SET = 5;
    public int expecting;
    public int foundChar;
    public int mismatchType;
    public CharScanner scanner;
    public BitSet set;
    public int upper;

    public MismatchedCharException() {
        super("Mismatched char");
    }

    public MismatchedCharException(char c2, char c3, char c4, boolean z, CharScanner charScanner) {
        super("Mismatched char", charScanner.getFilename(), charScanner.getLine(), charScanner.getColumn());
        this.mismatchType = z ? 4 : 3;
        this.foundChar = c2;
        this.expecting = c3;
        this.upper = c4;
        this.scanner = charScanner;
    }

    public MismatchedCharException(char c2, char c3, boolean z, CharScanner charScanner) {
        super("Mismatched char", charScanner.getFilename(), charScanner.getLine(), charScanner.getColumn());
        this.mismatchType = z ? 2 : 1;
        this.foundChar = c2;
        this.expecting = c3;
        this.scanner = charScanner;
    }

    public MismatchedCharException(char c2, BitSet bitSet, boolean z, CharScanner charScanner) {
        super("Mismatched char", charScanner.getFilename(), charScanner.getLine(), charScanner.getColumn());
        this.mismatchType = z ? 6 : 5;
        this.foundChar = c2;
        this.set = bitSet;
        this.scanner = charScanner;
    }

    private void appendCharName(StringBuffer stringBuffer, int i) {
        String str;
        if (i == 9) {
            str = "'\\t'";
        } else if (i == 10) {
            str = "'\\n'";
        } else if (i == 13) {
            str = "'\\r'";
        } else {
            if (i != 65535) {
                stringBuffer.append('\'');
                stringBuffer.append((char) i);
                stringBuffer.append('\'');
                return;
            }
            str = "'<EOF>'";
        }
        stringBuffer.append(str);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x000d. Please report as an issue. */
    @Override // java.lang.Throwable
    public String getMessage() {
        int i;
        String message;
        StringBuffer stringBuffer = new StringBuffer();
        switch (this.mismatchType) {
            case 1:
                stringBuffer.append("expecting ");
                i = this.expecting;
                appendCharName(stringBuffer, i);
                stringBuffer.append(", found ");
                appendCharName(stringBuffer, this.foundChar);
                break;
            case 2:
                stringBuffer.append("expecting anything but '");
                appendCharName(stringBuffer, this.expecting);
                message = "'; got it anyway";
                stringBuffer.append(message);
                break;
            case 3:
            case 4:
                stringBuffer.append("expecting token ");
                if (this.mismatchType == 4) {
                    stringBuffer.append("NOT ");
                }
                stringBuffer.append("in range: ");
                appendCharName(stringBuffer, this.expecting);
                stringBuffer.append("..");
                i = this.upper;
                appendCharName(stringBuffer, i);
                stringBuffer.append(", found ");
                appendCharName(stringBuffer, this.foundChar);
                break;
            case 5:
            case 6:
                StringBuilder sbM5a = C0000a.m5a("expecting ");
                sbM5a.append(this.mismatchType != 6 ? "" : "NOT ");
                sbM5a.append("one of (");
                stringBuffer.append(sbM5a.toString());
                for (int i2 : this.set.toArray()) {
                    appendCharName(stringBuffer, i2);
                }
                stringBuffer.append("), found ");
                appendCharName(stringBuffer, this.foundChar);
                break;
            default:
                message = super.getMessage();
                stringBuffer.append(message);
                break;
        }
        return stringBuffer.toString();
    }
}
