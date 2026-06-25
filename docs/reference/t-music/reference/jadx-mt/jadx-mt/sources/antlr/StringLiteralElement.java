package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class StringLiteralElement extends GrammarAtom {
    public String processedAtomText;

    public StringLiteralElement(Grammar grammar, Token token, int i) {
        int i2;
        super(grammar, token, i);
        boolean z = grammar instanceof LexerGrammar;
        if (!z) {
            TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(this.atomText);
            if (tokenSymbol == null) {
                Tool tool = grammar.antlrTool;
                StringBuilder sbM5a = C0000a.m5a("Undefined literal: ");
                sbM5a.append(this.atomText);
                tool.error(sbM5a.toString(), this.grammar.getFilename(), token.getLine(), token.getColumn());
            } else {
                this.tokenType = tokenSymbol.getTokenType();
            }
        }
        this.line = token.getLine();
        this.processedAtomText = new String();
        int i3 = 1;
        while (i3 < this.atomText.length() - 1) {
            char cCharAt = this.atomText.charAt(i3);
            if (cCharAt != '\\' || (i2 = i3 + 1) >= this.atomText.length() - 1) {
                i2 = i3;
            } else {
                cCharAt = this.atomText.charAt(i2);
                if (cCharAt == 'n') {
                    cCharAt = '\n';
                } else if (cCharAt == 'r') {
                    cCharAt = '\r';
                } else if (cCharAt == 't') {
                    cCharAt = '\t';
                }
            }
            if (z) {
                ((LexerGrammar) grammar).charVocabulary.add(cCharAt);
            }
            this.processedAtomText += cCharAt;
            i3 = i2 + 1;
        }
    }

    @Override // antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    @Override // antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }
}
