package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class ActionElement extends AlternativeElement {
    public String actionText;
    public boolean isSemPred;

    public ActionElement(Grammar grammar, Token token) {
        super(grammar);
        this.isSemPred = false;
        this.actionText = token.getText();
        this.line = token.getLine();
        this.column = token.getColumn();
    }

    @Override // antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    @Override // antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    @Override // antlr.GrammarElement
    public String toString() {
        StringBuilder sbM5a = C0000a.m5a(" ");
        sbM5a.append(this.actionText);
        sbM5a.append(this.isSemPred ? "?" : "");
        return sbM5a.toString();
    }
}
