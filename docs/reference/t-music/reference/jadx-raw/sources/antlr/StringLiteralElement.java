package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class StringLiteralElement extends GrammarAtom {
    public String processedAtomText;

    public StringLiteralElement(Grammar grammar, Token token, int i) {
        super(grammar, token, i);
        int i2;
        boolean z = grammar instanceof LexerGrammar;
        if (!z) {
            TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(this.atomText);
            if (tokenSymbol == null) {
                Tool tool = grammar.antlrTool;
                StringBuilder m5a = C0000a.m5a("Undefined literal: ");
                m5a.append(this.atomText);
                tool.error(m5a.toString(), this.grammar.getFilename(), token.getLine(), token.getColumn());
            } else {
                this.tokenType = tokenSymbol.getTokenType();
            }
        }
        this.line = token.getLine();
        this.processedAtomText = new String();
        int i3 = 1;
        while (i3 < this.atomText.length() - 1) {
            char charAt = this.atomText.charAt(i3);
            if (charAt != '\\' || (i2 = i3 + 1) >= this.atomText.length() - 1) {
                i2 = i3;
            } else {
                charAt = this.atomText.charAt(i2);
                if (charAt == 'n') {
                    charAt = '\n';
                } else if (charAt == 'r') {
                    charAt = '\r';
                } else if (charAt == 't') {
                    charAt = '\t';
                }
            }
            if (z) {
                ((LexerGrammar) grammar).charVocabulary.add(charAt);
            }
            this.processedAtomText += charAt;
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
