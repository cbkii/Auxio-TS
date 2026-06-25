package antlr;

import antlr.collections.impl.Vector;
import java.util.Hashtable;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class RuleBlock extends AlternativeBlock {
    public String argAction;
    public Lookahead[] cache;
    public boolean defaultErrorHandler;
    public RuleEndElement endNode;
    public Hashtable exceptionSpecs;
    public String ignoreRule;
    public Vector labeledElements;
    public boolean[] lock;
    public String returnAction;
    public String ruleName;
    public boolean testLiterals;
    public String throwsSpec;

    public RuleBlock(Grammar grammar, String str) {
        super(grammar);
        this.argAction = null;
        this.throwsSpec = null;
        this.returnAction = null;
        this.testLiterals = false;
        this.defaultErrorHandler = true;
        this.ignoreRule = null;
        this.ruleName = str;
        this.labeledElements = new Vector();
        this.cache = new Lookahead[grammar.maxk + 1];
        this.exceptionSpecs = new Hashtable();
        setAutoGen(grammar instanceof ParserGrammar);
    }

    public RuleBlock(Grammar grammar, String str, int i, boolean z) {
        this(grammar, str);
        this.line = i;
        setAutoGen(z);
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ void addAlternative(Alternative alternative) {
        super.addAlternative(alternative);
    }

    public void addExceptionSpec(ExceptionSpec exceptionSpec) {
        if (findExceptionSpec(exceptionSpec.label) == null) {
            Hashtable hashtable = this.exceptionSpecs;
            Token token = exceptionSpec.label;
            hashtable.put(token == null ? "" : token.getText(), exceptionSpec);
        } else {
            if (exceptionSpec.label == null) {
                Tool tool = this.grammar.antlrTool;
                StringBuilder m5a = C0000a.m5a("Rule '");
                m5a.append(this.ruleName);
                m5a.append("' already has an exception handler");
                tool.error(m5a.toString());
                return;
            }
            Tool tool2 = this.grammar.antlrTool;
            StringBuilder m5a2 = C0000a.m5a("Rule '");
            m5a2.append(this.ruleName);
            m5a2.append("' already has an exception handler for label: ");
            m5a2.append(exceptionSpec.label);
            tool2.error(m5a2.toString());
        }
    }

    public ExceptionSpec findExceptionSpec(Token token) {
        return (ExceptionSpec) this.exceptionSpecs.get(token == null ? "" : token.getText());
    }

    public ExceptionSpec findExceptionSpec(String str) {
        Hashtable hashtable = this.exceptionSpecs;
        if (str == null) {
            str = "";
        }
        return (ExceptionSpec) hashtable.get(str);
    }

    @Override // antlr.AlternativeBlock, antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ Alternative getAlternativeAt(int i) {
        return super.getAlternativeAt(i);
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ Vector getAlternatives() {
        return super.getAlternatives();
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ boolean getAutoGen() {
        return super.getAutoGen();
    }

    @Override // antlr.AlternativeElement
    public /* bridge */ /* synthetic */ int getAutoGenType() {
        return super.getAutoGenType();
    }

    @Override // antlr.GrammarElement
    public /* bridge */ /* synthetic */ int getColumn() {
        return super.getColumn();
    }

    public boolean getDefaultErrorHandler() {
        return this.defaultErrorHandler;
    }

    public RuleEndElement getEndElement() {
        return this.endNode;
    }

    public String getIgnoreRule() {
        return this.ignoreRule;
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ String getInitAction() {
        return super.getInitAction();
    }

    @Override // antlr.AlternativeBlock, antlr.AlternativeElement
    public /* bridge */ /* synthetic */ String getLabel() {
        return super.getLabel();
    }

    @Override // antlr.GrammarElement
    public /* bridge */ /* synthetic */ int getLine() {
        return super.getLine();
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public boolean getTestLiterals() {
        return this.testLiterals;
    }

    public boolean isLexerAutoGenRule() {
        return this.ruleName.equals("nextToken");
    }

    @Override // antlr.AlternativeBlock, antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }

    @Override // antlr.AlternativeBlock
    public void prepareForAnalysis() {
        super.prepareForAnalysis();
        this.lock = new boolean[this.grammar.maxk + 1];
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ void removeTrackingOfRuleRefs(Grammar grammar) {
        super.removeTrackingOfRuleRefs(grammar);
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ void setAlternatives(Vector vector) {
        super.setAlternatives(vector);
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ void setAutoGen(boolean z) {
        super.setAutoGen(z);
    }

    @Override // antlr.AlternativeElement
    public /* bridge */ /* synthetic */ void setAutoGenType(int i) {
        super.setAutoGenType(i);
    }

    public void setDefaultErrorHandler(boolean z) {
        this.defaultErrorHandler = z;
    }

    public void setEndElement(RuleEndElement ruleEndElement) {
        this.endNode = ruleEndElement;
    }

    @Override // antlr.AlternativeBlock
    public /* bridge */ /* synthetic */ void setInitAction(String str) {
        super.setInitAction(str);
    }

    @Override // antlr.AlternativeBlock, antlr.AlternativeElement
    public /* bridge */ /* synthetic */ void setLabel(String str) {
        super.setLabel(str);
    }

    @Override // antlr.AlternativeBlock
    public void setOption(Token token, Token token2) {
        Tool tool;
        String filename;
        int line;
        int column;
        String str;
        Tool tool2;
        String filename2;
        int line2;
        int column2;
        String str2;
        if (token.getText().equals("defaultErrorHandler")) {
            if (token2.getText().equals("true")) {
                this.defaultErrorHandler = true;
                return;
            }
            if (token2.getText().equals("false")) {
                this.defaultErrorHandler = false;
                return;
            }
            Grammar grammar = this.grammar;
            tool = grammar.antlrTool;
            filename = grammar.getFilename();
            line = token.getLine();
            column = token.getColumn();
            str = "Value for defaultErrorHandler must be true or false";
        } else {
            if (!token.getText().equals("testLiterals")) {
                if (token.getText().equals("ignore")) {
                    Grammar grammar2 = this.grammar;
                    if (grammar2 instanceof LexerGrammar) {
                        this.ignoreRule = token2.getText();
                        return;
                    }
                    tool2 = grammar2.antlrTool;
                    filename2 = grammar2.getFilename();
                    line2 = token.getLine();
                    column2 = token.getColumn();
                    str2 = "ignore option only valid for lexer rules";
                } else if (token.getText().equals("paraphrase")) {
                    Grammar grammar3 = this.grammar;
                    if (grammar3 instanceof LexerGrammar) {
                        TokenSymbol tokenSymbol = grammar3.tokenManager.getTokenSymbol(this.ruleName);
                        if (tokenSymbol == null) {
                            Tool tool3 = this.grammar.antlrTool;
                            StringBuilder m5a = C0000a.m5a("cannot find token associated with rule ");
                            m5a.append(this.ruleName);
                            tool3.panic(m5a.toString());
                        }
                        tokenSymbol.setParaphrase(token2.getText());
                        return;
                    }
                    tool2 = grammar3.antlrTool;
                    filename2 = grammar3.getFilename();
                    line2 = token.getLine();
                    column2 = token.getColumn();
                    str2 = "paraphrase option only valid for lexer rules";
                } else {
                    if (!token.getText().equals("generateAmbigWarnings")) {
                        this.grammar.antlrTool.error(C0000a.m0a(token, C0000a.m5a("Invalid rule option: ")), this.grammar.getFilename(), token.getLine(), token.getColumn());
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
                    Grammar grammar4 = this.grammar;
                    tool = grammar4.antlrTool;
                    filename = grammar4.getFilename();
                    line = token.getLine();
                    column = token.getColumn();
                    str = "Value for generateAmbigWarnings must be true or false";
                }
                tool2.error(str2, filename2, line2, column2);
                return;
            }
            Grammar grammar5 = this.grammar;
            if (!(grammar5 instanceof LexerGrammar)) {
                tool2 = grammar5.antlrTool;
                filename2 = grammar5.getFilename();
                line2 = token.getLine();
                column2 = token.getColumn();
                str2 = "testLiterals option only valid for lexer rules";
                tool2.error(str2, filename2, line2, column2);
                return;
            }
            if (token2.getText().equals("true")) {
                this.testLiterals = true;
                return;
            }
            if (token2.getText().equals("false")) {
                this.testLiterals = false;
                return;
            }
            Grammar grammar6 = this.grammar;
            tool = grammar6.antlrTool;
            filename = grammar6.getFilename();
            line = token.getLine();
            column = token.getColumn();
            str = "Value for testLiterals must be true or false";
        }
        tool.error(str, filename, line, column);
    }

    @Override // antlr.AlternativeBlock, antlr.GrammarElement
    public String toString() {
        Lookahead[] lookaheadArr = this.endNode.cache;
        int i = this.grammar.maxk;
        String str = " FOLLOW={";
        boolean z = true;
        for (int i2 = 1; i2 <= i; i2++) {
            if (lookaheadArr[i2] != null) {
                StringBuilder m5a = C0000a.m5a(str);
                m5a.append(lookaheadArr[i2].toString(",", this.grammar.tokenManager.getVocabulary()));
                String sb = m5a.toString();
                if (i2 < i && lookaheadArr[i2 + 1] != null) {
                    sb = C0000a.m1a(sb, ";");
                }
                str = sb;
                z = false;
            }
        }
        String m1a = C0000a.m1a(str, "}");
        if (z) {
            m1a = "";
        }
        return this.ruleName + ": " + super.toString() + " ;" + m1a;
    }
}
