package antlr;

/* loaded from: classes3.dex */
public class BlockEndElement extends AlternativeElement {
    public AlternativeBlock block;
    public boolean[] lock;

    public BlockEndElement(Grammar grammar) {
        super(grammar);
        this.lock = new boolean[grammar.maxk + 1];
    }

    @Override // antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    @Override // antlr.GrammarElement
    public String toString() {
        return "";
    }
}
