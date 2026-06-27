package antlr;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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
        StringBuilder m5a = C0000a.m5a(" ");
        m5a.append(this.actionText);
        m5a.append(this.isSemPred ? "?" : "");
        return m5a.toString();
    }
}
