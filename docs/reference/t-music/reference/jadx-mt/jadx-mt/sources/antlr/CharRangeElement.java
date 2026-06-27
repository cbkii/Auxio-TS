package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class CharRangeElement extends AlternativeElement {
    public char begin;
    public String beginText;
    public char end;
    public String endText;
    public String label;

    public CharRangeElement(LexerGrammar lexerGrammar, Token token, Token token2, int i) {
        super(lexerGrammar);
        this.begin = (char) 0;
        this.end = (char) 0;
        this.begin = (char) ANTLRLexer.tokenTypeForCharLiteral(token.getText());
        this.beginText = token.getText();
        this.end = (char) ANTLRLexer.tokenTypeForCharLiteral(token2.getText());
        this.endText = token2.getText();
        this.line = token.getLine();
        for (int i2 = this.begin; i2 <= this.end; i2++) {
            lexerGrammar.charVocabulary.add(i2);
        }
        this.autoGenType = i;
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
