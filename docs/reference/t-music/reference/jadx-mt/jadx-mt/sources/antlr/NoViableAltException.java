package antlr;

import antlr.collections.AST;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class NoViableAltException extends RecognitionException {
    public AST node;
    public Token token;

    public NoViableAltException(Token token, String str) {
        super("NoViableAlt", str, token.getLine(), token.getColumn());
        this.token = token;
    }

    public NoViableAltException(AST ast) {
        super("NoViableAlt", "<AST>", ast.getLine(), ast.getColumn());
        this.node = ast;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.token != null) {
            return C0000a.m0a(this.token, C0000a.m5a("unexpected token: "));
        }
        if (this.node == TreeParser.ASTNULL) {
            return "unexpected end of subtree";
        }
        StringBuilder sbM5a = C0000a.m5a("unexpected AST node: ");
        sbM5a.append(this.node.toString());
        return sbM5a.toString();
    }
}
