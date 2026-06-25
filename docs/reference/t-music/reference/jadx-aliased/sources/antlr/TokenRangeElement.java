package antlr;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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
        StringBuilder m5a;
        if (this.label != null) {
            m5a = C0000a.m5a(" ");
            m5a.append(this.label);
            m5a.append(":");
        } else {
            m5a = C0000a.m5a(" ");
        }
        m5a.append(this.beginText);
        m5a.append("..");
        m5a.append(this.endText);
        return m5a.toString();
    }
}
