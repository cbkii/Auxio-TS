package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class RuleEndElement extends BlockEndElement {
    public Lookahead[] cache;
    public boolean noFOLLOW;

    public RuleEndElement(Grammar grammar) {
        super(grammar);
        this.cache = new Lookahead[grammar.maxk + 1];
    }

    @Override // antlr.BlockEndElement, antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    @Override // antlr.BlockEndElement, antlr.GrammarElement
    public String toString() {
        return "";
    }
}
