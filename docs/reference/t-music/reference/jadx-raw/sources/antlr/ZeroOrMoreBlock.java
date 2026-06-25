package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class ZeroOrMoreBlock extends BlockWithImpliedExitPath {
    public ZeroOrMoreBlock(Grammar grammar) {
        super(grammar);
    }

    public ZeroOrMoreBlock(Grammar grammar, Token token) {
        super(grammar, token);
    }

    @Override // antlr.AlternativeBlock, antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    @Override // antlr.AlternativeBlock, antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    @Override // antlr.AlternativeBlock, antlr.GrammarElement
    public String toString() {
        return C0000a.m3a(new StringBuilder(), super.toString(), "*");
    }
}
