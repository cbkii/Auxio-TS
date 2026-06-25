package antlr;

import antlr.collections.AST;
import antlr.collections.impl.BitSet;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class MismatchedTokenException extends RecognitionException {
    public static final int NOT_RANGE = 4;
    public static final int NOT_SET = 6;
    public static final int NOT_TOKEN = 2;
    public static final int RANGE = 3;
    public static final int SET = 5;
    public static final int TOKEN = 1;
    public int expecting;
    public int mismatchType;
    public AST node;
    public BitSet set;
    public Token token;
    public String[] tokenNames;
    public String tokenText;
    public int upper;

    public MismatchedTokenException() {
        super("Mismatched Token: expecting any AST node", "<AST>", -1, -1);
        this.tokenText = null;
    }

    public MismatchedTokenException(String[] strArr, Token token, int i, int i2, boolean z, String str) {
        super("Mismatched Token", str, token.getLine(), token.getColumn());
        this.tokenText = null;
        this.tokenNames = strArr;
        this.token = token;
        this.tokenText = token.getText();
        this.mismatchType = z ? 4 : 3;
        this.expecting = i;
        this.upper = i2;
    }

    public MismatchedTokenException(String[] strArr, Token token, int i, boolean z, String str) {
        super("Mismatched Token", str, token.getLine(), token.getColumn());
        this.tokenText = null;
        this.tokenNames = strArr;
        this.token = token;
        this.tokenText = token.getText();
        this.mismatchType = z ? 2 : 1;
        this.expecting = i;
    }

    public MismatchedTokenException(String[] strArr, Token token, BitSet bitSet, boolean z, String str) {
        super("Mismatched Token", str, token.getLine(), token.getColumn());
        this.tokenText = null;
        this.tokenNames = strArr;
        this.token = token;
        this.tokenText = token.getText();
        this.mismatchType = z ? 6 : 5;
        this.set = bitSet;
    }

    public MismatchedTokenException(String[] strArr, AST ast, int i, int i2, boolean z) {
        super("Mismatched Token", "<AST>", ast == null ? -1 : ast.getLine(), ast != null ? ast.getColumn() : -1);
        this.tokenText = null;
        this.tokenNames = strArr;
        this.node = ast;
        this.tokenText = ast == null ? "<empty tree>" : ast.toString();
        this.mismatchType = z ? 4 : 3;
        this.expecting = i;
        this.upper = i2;
    }

    public MismatchedTokenException(String[] strArr, AST ast, int i, boolean z) {
        super("Mismatched Token", "<AST>", ast == null ? -1 : ast.getLine(), ast != null ? ast.getColumn() : -1);
        this.tokenText = null;
        this.tokenNames = strArr;
        this.node = ast;
        this.tokenText = ast == null ? "<empty tree>" : ast.toString();
        this.mismatchType = z ? 2 : 1;
        this.expecting = i;
    }

    public MismatchedTokenException(String[] strArr, AST ast, BitSet bitSet, boolean z) {
        super("Mismatched Token", "<AST>", ast == null ? -1 : ast.getLine(), ast != null ? ast.getColumn() : -1);
        this.tokenText = null;
        this.tokenNames = strArr;
        this.node = ast;
        this.tokenText = ast == null ? "<empty tree>" : ast.toString();
        this.mismatchType = z ? 6 : 5;
        this.set = bitSet;
    }

    private String tokenName(int i) {
        if (i == 0) {
            return "<Set of tokens>";
        }
        if (i >= 0) {
            String[] strArr = this.tokenNames;
            if (i < strArr.length) {
                return strArr[i];
            }
        }
        StringBuilder sbM5a = C0000a.m5a("<");
        sbM5a.append(String.valueOf(i));
        sbM5a.append(">");
        return sbM5a.toString();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder sbM5a;
        int i;
        String str;
        String string;
        StringBuffer stringBuffer = new StringBuffer();
        switch (this.mismatchType) {
            case 1:
                sbM5a = C0000a.m5a("expecting ");
                i = this.expecting;
                sbM5a.append(tokenName(i));
                sbM5a.append(", found '");
                sbM5a.append(this.tokenText);
                sbM5a.append("'");
                string = sbM5a.toString();
                break;
            case 2:
                sbM5a = C0000a.m5a("expecting anything but ");
                sbM5a.append(tokenName(this.expecting));
                sbM5a.append("; got it anyway");
                string = sbM5a.toString();
                break;
            case 3:
                str = "expecting token in range: ";
                sbM5a = C0000a.m5a(str);
                sbM5a.append(tokenName(this.expecting));
                sbM5a.append("..");
                i = this.upper;
                sbM5a.append(tokenName(i));
                sbM5a.append(", found '");
                sbM5a.append(this.tokenText);
                sbM5a.append("'");
                string = sbM5a.toString();
                break;
            case 4:
                str = "expecting token NOT in range: ";
                sbM5a = C0000a.m5a(str);
                sbM5a.append(tokenName(this.expecting));
                sbM5a.append("..");
                i = this.upper;
                sbM5a.append(tokenName(i));
                sbM5a.append(", found '");
                sbM5a.append(this.tokenText);
                sbM5a.append("'");
                string = sbM5a.toString();
                break;
            case 5:
            case 6:
                StringBuilder sbM5a2 = C0000a.m5a("expecting ");
                sbM5a2.append(this.mismatchType == 6 ? "NOT " : "");
                sbM5a2.append("one of (");
                stringBuffer.append(sbM5a2.toString());
                for (int i2 : this.set.toArray()) {
                    stringBuffer.append(" ");
                    stringBuffer.append(tokenName(i2));
                }
                sbM5a = C0000a.m5a("), found '");
                sbM5a.append(this.tokenText);
                sbM5a.append("'");
                string = sbM5a.toString();
                break;
            default:
                string = super.getMessage();
                break;
        }
        stringBuffer.append(string);
        return stringBuffer.toString();
    }
}
