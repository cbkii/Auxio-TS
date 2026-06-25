package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class TreeElement extends AlternativeBlock {
    public GrammarAtom root;

    public TreeElement(Grammar grammar, Token token) {
        super(grammar, token, false);
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
        StringBuilder sbM5a = C0000a.m5a(" #(");
        sbM5a.append(this.root);
        String string = sbM5a.toString();
        for (AlternativeElement alternativeElement = ((Alternative) this.alternatives.elementAt(0)).head; alternativeElement != null; alternativeElement = alternativeElement.next) {
            string = string + alternativeElement;
        }
        return C0000a.m1a(string, " )");
    }
}
