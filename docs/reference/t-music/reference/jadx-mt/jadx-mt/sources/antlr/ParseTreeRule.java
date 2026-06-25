package antlr;

import antlr.collections.AST;

/* JADX INFO: loaded from: classes3.dex */
public class ParseTreeRule extends ParseTree {
    public static final int INVALID_ALT = -1;
    public int altNumber;
    public String ruleName;

    public ParseTreeRule(String str) {
        this(str, -1);
    }

    public ParseTreeRule(String str, int i) {
        this.ruleName = str;
        this.altNumber = i;
    }

    @Override // antlr.ParseTree
    public int getLeftmostDerivation(StringBuffer stringBuffer, int i) {
        if (i <= 0) {
            stringBuffer.append(' ');
            stringBuffer.append(toString());
            return 0;
        }
        int leftmostDerivation = 1;
        for (AST firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (leftmostDerivation >= i || (firstChild instanceof ParseTreeToken)) {
                stringBuffer.append(' ');
                stringBuffer.append(firstChild.toString());
            } else {
                leftmostDerivation = ((ParseTree) firstChild).getLeftmostDerivation(stringBuffer, i - leftmostDerivation) + leftmostDerivation;
            }
        }
        return leftmostDerivation;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public String toString() {
        StringBuilder sb;
        if (this.altNumber == -1) {
            sb = new StringBuilder();
            sb.append('<');
            sb.append(this.ruleName);
            sb.append('>');
        } else {
            sb = new StringBuilder();
            sb.append('<');
            sb.append(this.ruleName);
            sb.append("[");
            sb.append(this.altNumber);
            sb.append("]>");
        }
        return sb.toString();
    }
}
