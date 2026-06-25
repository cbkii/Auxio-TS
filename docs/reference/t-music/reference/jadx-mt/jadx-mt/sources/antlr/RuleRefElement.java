package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class RuleRefElement extends AlternativeElement {
    public String args;
    public String idAssign;
    public String label;
    public String targetRule;

    public RuleRefElement(Grammar grammar, Token token, int i) {
        super(grammar, token, i);
        this.args = null;
        this.idAssign = null;
        this.targetRule = token.getText();
        if (token.type == 24) {
            this.targetRule = CodeGenerator.encodeLexerRuleName(this.targetRule);
        }
    }

    @Override // antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    public String getArgs() {
        return this.args;
    }

    public String getIdAssign() {
        return this.idAssign;
    }

    @Override // antlr.AlternativeElement
    public String getLabel() {
        return this.label;
    }

    @Override // antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    public void setArgs(String str) {
        this.args = str;
    }

    public void setIdAssign(String str) {
        this.idAssign = str;
    }

    @Override // antlr.AlternativeElement
    public void setLabel(String str) {
        this.label = str;
    }

    @Override // antlr.GrammarElement
    public String toString() {
        StringBuilder sbM5a;
        String str;
        if (this.args != null) {
            sbM5a = C0000a.m5a(" ");
            sbM5a.append(this.targetRule);
            str = this.args;
        } else {
            sbM5a = C0000a.m5a(" ");
            str = this.targetRule;
        }
        sbM5a.append(str);
        return sbM5a.toString();
    }
}
