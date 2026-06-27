package antlr;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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
        StringBuilder m5a = C0000a.m5a(" #(");
        m5a.append(this.root);
        String sb = m5a.toString();
        for (AlternativeElement alternativeElement = ((Alternative) this.alternatives.elementAt(0)).head; alternativeElement != null; alternativeElement = alternativeElement.next) {
            sb = sb + alternativeElement;
        }
        return C0000a.m1a(sb, " )");
    }
}
