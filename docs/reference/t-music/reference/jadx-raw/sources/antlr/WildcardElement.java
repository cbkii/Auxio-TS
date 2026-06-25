package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class WildcardElement extends GrammarAtom {
    public String label;

    public WildcardElement(Grammar grammar, Token token, int i) {
        super(grammar, token, i);
        this.line = token.getLine();
    }

    @Override // antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    @Override // antlr.GrammarAtom, antlr.AlternativeElement
    public String getLabel() {
        return this.label;
    }

    @Override // antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    @Override // antlr.GrammarAtom, antlr.AlternativeElement
    public void setLabel(String str) {
        this.label = str;
    }

    @Override // antlr.GrammarAtom, antlr.GrammarElement
    public String toString() {
        return C0000a.m1a(this.label != null ? C0000a.m3a(C0000a.m5a(" "), this.label, ":") : " ", ".");
    }
}
