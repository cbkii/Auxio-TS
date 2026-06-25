package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class TokenRefElement extends GrammarAtom {
    public TokenRefElement(Grammar grammar, Token token, boolean z, int i) {
        super(grammar, token, i);
        this.not = z;
        TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(this.atomText);
        if (tokenSymbol == null) {
            Tool tool = grammar.antlrTool;
            StringBuilder m5a = C0000a.m5a("Undefined token symbol: ");
            m5a.append(this.atomText);
            tool.error(m5a.toString(), this.grammar.getFilename(), token.getLine(), token.getColumn());
        } else {
            this.tokenType = tokenSymbol.getTokenType();
            setASTNodeType(tokenSymbol.getASTNodeType());
        }
        this.line = token.getLine();
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
