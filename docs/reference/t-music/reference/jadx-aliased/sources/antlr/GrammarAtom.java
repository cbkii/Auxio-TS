package antlr;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public abstract class GrammarAtom extends AlternativeElement {
    public String ASTNodeType;
    public String atomText;
    public String label;
    public boolean not;
    public int tokenType;

    public GrammarAtom(Grammar grammar, Token token, int i) {
        super(grammar, token, i);
        this.tokenType = 0;
        this.not = false;
        this.ASTNodeType = null;
        this.atomText = token.getText();
    }

    public String getASTNodeType() {
        return this.ASTNodeType;
    }

    @Override // antlr.AlternativeElement
    public String getLabel() {
        return this.label;
    }

    public String getText() {
        return this.atomText;
    }

    public int getType() {
        return this.tokenType;
    }

    public void setASTNodeType(String str) {
        this.ASTNodeType = str;
    }

    @Override // antlr.AlternativeElement
    public void setLabel(String str) {
        this.label = str;
    }

    public void setOption(Token token, Token token2) {
        if (token.getText().equals("AST")) {
            setASTNodeType(token2.getText());
        } else {
            this.grammar.antlrTool.error(C0000a.m0a(token, C0000a.m5a("Invalid element option:")), this.grammar.getFilename(), token.getLine(), token.getColumn());
        }
    }

    @Override // antlr.GrammarElement
    public String toString() {
        String m3a = this.label != null ? C0000a.m3a(C0000a.m5a(" "), this.label, ":") : " ";
        if (this.not) {
            m3a = C0000a.m1a(m3a, "~");
        }
        StringBuilder m5a = C0000a.m5a(m3a);
        m5a.append(this.atomText);
        return m5a.toString();
    }
}
