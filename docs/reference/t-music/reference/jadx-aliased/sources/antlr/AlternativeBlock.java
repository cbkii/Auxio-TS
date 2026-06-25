package antlr;

import antlr.collections.impl.Vector;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class AlternativeBlock extends AlternativeElement {
    public static int nblks;

    /* renamed from: ID */
    public int f302ID;
    public Vector alternatives;
    public int alti;
    public int altj;
    public int analysisAlt;
    public boolean doAutoGen;
    public boolean generateAmbigWarnings;
    public boolean greedy;
    public boolean greedySet;
    public boolean hasASynPred;
    public boolean hasAnAction;
    public String initAction;
    public String label;
    public boolean not;
    public boolean warnWhenFollowAmbig;

    public AlternativeBlock(Grammar grammar) {
        super(grammar);
        this.initAction = null;
        this.hasAnAction = false;
        this.hasASynPred = false;
        this.f302ID = 0;
        this.not = false;
        this.greedy = true;
        this.greedySet = false;
        this.doAutoGen = true;
        this.warnWhenFollowAmbig = true;
        this.generateAmbigWarnings = true;
        this.alternatives = new Vector(5);
        this.not = false;
        nblks++;
        this.f302ID = nblks;
    }

    public AlternativeBlock(Grammar grammar, Token token, boolean z) {
        super(grammar, token);
        this.initAction = null;
        this.hasAnAction = false;
        this.hasASynPred = false;
        this.f302ID = 0;
        this.not = false;
        this.greedy = true;
        this.greedySet = false;
        this.doAutoGen = true;
        this.warnWhenFollowAmbig = true;
        this.generateAmbigWarnings = true;
        this.alternatives = new Vector(5);
        this.not = z;
        nblks++;
        this.f302ID = nblks;
    }

    public void addAlternative(Alternative alternative) {
        this.alternatives.appendElement(alternative);
    }

    @Override // antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    public Alternative getAlternativeAt(int i) {
        return (Alternative) this.alternatives.elementAt(i);
    }

    public Vector getAlternatives() {
        return this.alternatives;
    }

    public boolean getAutoGen() {
        return this.doAutoGen;
    }

    public String getInitAction() {
        return this.initAction;
    }

    @Override // antlr.AlternativeElement
    public String getLabel() {
        return this.label;
    }

    @Override // antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    public void prepareForAnalysis() {
        for (int i = 0; i < this.alternatives.size(); i++) {
            Alternative alternative = (Alternative) this.alternatives.elementAt(i);
            alternative.cache = new Lookahead[this.grammar.maxk + 1];
            alternative.lookaheadDepth = -1;
        }
    }

    public void removeTrackingOfRuleRefs(Grammar grammar) {
        for (int i = 0; i < this.alternatives.size(); i++) {
            for (AlternativeElement alternativeElement = getAlternativeAt(i).head; alternativeElement != null; alternativeElement = alternativeElement.next) {
                if (alternativeElement instanceof RuleRefElement) {
                    RuleRefElement ruleRefElement = (RuleRefElement) alternativeElement;
                    RuleSymbol ruleSymbol = (RuleSymbol) grammar.getSymbol(ruleRefElement.targetRule);
                    if (ruleSymbol == null) {
                        Tool tool = this.grammar.antlrTool;
                        StringBuilder m5a = C0000a.m5a("rule ");
                        m5a.append(ruleRefElement.targetRule);
                        m5a.append(" referenced in (...)=>, but not defined");
                        tool.error(m5a.toString());
                    } else {
                        ruleSymbol.references.removeElement(ruleRefElement);
                    }
                } else if (alternativeElement instanceof AlternativeBlock) {
                    ((AlternativeBlock) alternativeElement).removeTrackingOfRuleRefs(grammar);
                }
            }
        }
    }

    public void setAlternatives(Vector vector) {
        this.alternatives = vector;
    }

    public void setAutoGen(boolean z) {
        this.doAutoGen = z;
    }

    public void setInitAction(String str) {
        this.initAction = str;
    }

    @Override // antlr.AlternativeElement
    public void setLabel(String str) {
        this.label = str;
    }

    public void setOption(Token token, Token token2) {
        Tool tool;
        String filename;
        int line;
        int column;
        String str;
        if (token.getText().equals("warnWhenFollowAmbig")) {
            if (token2.getText().equals("true")) {
                this.warnWhenFollowAmbig = true;
                return;
            }
            if (token2.getText().equals("false")) {
                this.warnWhenFollowAmbig = false;
                return;
            }
            Grammar grammar = this.grammar;
            tool = grammar.antlrTool;
            filename = grammar.getFilename();
            line = token.getLine();
            column = token.getColumn();
            str = "Value for warnWhenFollowAmbig must be true or false";
        } else {
            if (!token.getText().equals("generateAmbigWarnings")) {
                if (!token.getText().equals("greedy")) {
                    this.grammar.antlrTool.error(C0000a.m0a(token, C0000a.m5a("Invalid subrule option: ")), this.grammar.getFilename(), token.getLine(), token.getColumn());
                    return;
                }
                if (token2.getText().equals("true")) {
                    this.greedy = true;
                } else if (token2.getText().equals("false")) {
                    this.greedy = false;
                } else {
                    Grammar grammar2 = this.grammar;
                    tool = grammar2.antlrTool;
                    filename = grammar2.getFilename();
                    line = token.getLine();
                    column = token.getColumn();
                    str = "Value for greedy must be true or false";
                }
                this.greedySet = true;
                return;
            }
            if (token2.getText().equals("true")) {
                this.generateAmbigWarnings = true;
                return;
            }
            if (token2.getText().equals("false")) {
                this.generateAmbigWarnings = false;
                return;
            }
            Grammar grammar3 = this.grammar;
            tool = grammar3.antlrTool;
            filename = grammar3.getFilename();
            line = token.getLine();
            column = token.getColumn();
            str = "Value for generateAmbigWarnings must be true or false";
        }
        tool.error(str, filename, line, column);
    }

    @Override // antlr.GrammarElement
    public String toString() {
        String str = " (";
        if (this.initAction != null) {
            StringBuilder m5a = C0000a.m5a(" (");
            m5a.append(this.initAction);
            str = m5a.toString();
        }
        for (int i = 0; i < this.alternatives.size(); i++) {
            Alternative alternativeAt = getAlternativeAt(i);
            Lookahead[] lookaheadArr = alternativeAt.cache;
            int i2 = alternativeAt.lookaheadDepth;
            if (i2 != -1) {
                if (i2 == Integer.MAX_VALUE) {
                    str = C0000a.m1a(str, "{?}:");
                } else {
                    String m1a = C0000a.m1a(str, " {");
                    for (int i3 = 1; i3 <= i2; i3++) {
                        StringBuilder m5a2 = C0000a.m5a(m1a);
                        m5a2.append(lookaheadArr[i3].toString(",", this.grammar.tokenManager.getVocabulary()));
                        m1a = m5a2.toString();
                        if (i3 < i2 && lookaheadArr[i3 + 1] != null) {
                            m1a = C0000a.m1a(m1a, ";");
                        }
                    }
                    str = C0000a.m1a(m1a, "}:");
                }
            }
            String str2 = alternativeAt.semPred;
            if (str2 != null) {
                str = C0000a.m1a(str, str2);
            }
            for (AlternativeElement alternativeElement = alternativeAt.head; alternativeElement != null; alternativeElement = alternativeElement.next) {
                str = str + alternativeElement;
            }
            if (i < this.alternatives.size() - 1) {
                str = C0000a.m1a(str, " |");
            }
        }
        return C0000a.m1a(str, " )");
    }
}
