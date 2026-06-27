package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class TokenRangeElement extends AlternativeElement {
    public int begin;
    public String beginText;
    public int end;
    public String endText;
    public String label;

    public TokenRangeElement(Grammar grammar, Token token, Token token2, int i) {
        super(grammar, token, i);
        this.begin = 0;
        this.end = 0;
        this.begin = this.grammar.tokenManager.getTokenSymbol(token.getText()).getTokenType();
        this.beginText = token.getText();
        this.end = this.grammar.tokenManager.getTokenSymbol(token2.getText()).getTokenType();
        this.endText = token2.getText();
        this.line = token.getLine();
    }

    @Override // antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    @Override // antlr.AlternativeElement
    public String getLabel() {
        return this.label;
    }

    @Override // antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    @Override // antlr.AlternativeElement
    public void setLabel(String str) {
        this.label = str;
    }

    @Override // antlr.GrammarElement
    public String toString() {
        StringBuilder sbM5a;
        if (this.label != null) {
            sbM5a = C0000a.m5a(" ");
            sbM5a.append(this.label);
            sbM5a.append(":");
        } else {
            sbM5a = C0000a.m5a(" ");
        }
        sbM5a.append(this.beginText);
        sbM5a.append("..");
        sbM5a.append(this.endText);
        return sbM5a.toString();
    }
}
