package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class ParseTreeToken extends ParseTree {
    public Token token;

    public ParseTreeToken(Token token) {
        this.token = token;
    }

    @Override // antlr.ParseTree
    public int getLeftmostDerivation(StringBuffer stringBuffer, int i) {
        stringBuffer.append(' ');
        stringBuffer.append(toString());
        return i;
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public String toString() {
        Token token = this.token;
        return token != null ? token.getText() : "<missing token>";
    }
}
