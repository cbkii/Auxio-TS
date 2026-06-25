package antlr;

import antlr.collections.AST;

/* JADX INFO: loaded from: classes3.dex */
public class CommonASTWithHiddenTokens extends CommonAST {
    public CommonHiddenStreamToken hiddenAfter;
    public CommonHiddenStreamToken hiddenBefore;

    public CommonASTWithHiddenTokens() {
    }

    public CommonASTWithHiddenTokens(Token token) {
        super(token);
    }

    public CommonHiddenStreamToken getHiddenAfter() {
        return this.hiddenAfter;
    }

    public CommonHiddenStreamToken getHiddenBefore() {
        return this.hiddenBefore;
    }

    @Override // antlr.CommonAST, antlr.BaseAST, antlr.collections.AST
    public void initialize(Token token) {
        CommonHiddenStreamToken commonHiddenStreamToken = (CommonHiddenStreamToken) token;
        super.initialize(commonHiddenStreamToken);
        this.hiddenBefore = commonHiddenStreamToken.getHiddenBefore();
        this.hiddenAfter = commonHiddenStreamToken.getHiddenAfter();
    }

    @Override // antlr.CommonAST, antlr.BaseAST, antlr.collections.AST
    public void initialize(AST ast) {
        CommonASTWithHiddenTokens commonASTWithHiddenTokens = (CommonASTWithHiddenTokens) ast;
        this.hiddenBefore = commonASTWithHiddenTokens.getHiddenBefore();
        this.hiddenAfter = commonASTWithHiddenTokens.getHiddenAfter();
        super.initialize(ast);
    }
}
