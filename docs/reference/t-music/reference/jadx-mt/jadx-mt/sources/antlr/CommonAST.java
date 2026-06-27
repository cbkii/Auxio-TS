package antlr;

import antlr.collections.AST;

/* JADX INFO: loaded from: classes3.dex */
public class CommonAST extends BaseAST {
    public String text;
    public int ttype = 0;

    public CommonAST() {
    }

    public CommonAST(Token token) {
        initialize(token);
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public String getText() {
        return this.text;
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public int getType() {
        return this.ttype;
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public void initialize(int i, String str) {
        setType(i);
        setText(str);
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public void initialize(Token token) {
        setText(token.getText());
        setType(token.getType());
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public void initialize(AST ast) {
        setText(ast.getText());
        setType(ast.getType());
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public void setText(String str) {
        this.text = str;
    }

    @Override // antlr.BaseAST, antlr.collections.AST
    public void setType(int i) {
        this.ttype = i;
    }
}
