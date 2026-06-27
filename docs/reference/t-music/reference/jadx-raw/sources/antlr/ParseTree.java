package antlr;

import antlr.collections.AST;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public abstract class ParseTree extends BaseAST {
    public abstract int getLeftmostDerivation(StringBuffer stringBuffer, int i);

    public String getLeftmostDerivation(int i) {
        StringBuffer stringBuffer = new StringBuffer(2000);
        StringBuilder m5a = C0000a.m5a("    ");
        m5a.append(toString());
        stringBuffer.append(m5a.toString());
        stringBuffer.append("\n");
        for (int i2 = 1; i2 < i; i2++) {
            stringBuffer.append(" =>");
            stringBuffer.append(getLeftmostDerivationStep(i2));
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    public String getLeftmostDerivationStep(int i) {
        if (i <= 0) {
            return toString();
        }
        StringBuffer stringBuffer = new StringBuffer(2000);
        getLeftmostDerivation(stringBuffer, i);
        return stringBuffer.toString();
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public void initialize(int i, String str) {
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public void initialize(Token token) {
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public void initialize(AST ast) {
    }
}
