package antlr;

import antlr.actions.python.ActionLexer;
import antlr.actions.python.CodeLexer;
import antlr.collections.impl.BitSet;
import antlr.collections.impl.Vector;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class PythonCodeGenerator extends CodeGenerator {
    public static final String NONUNIQUE = new String();
    public static final int caseSizeThreshold = 127;
    public static final String initHeaderAction = "__init__";
    public static final String mainHeaderAction = "__main__";
    public String commonExtraArgs;
    public String commonExtraParams;
    public String commonLocalVars;
    public String currentASTResult;
    public RuleBlock currentRule;
    public String exceptionThrown;
    public String labeledElementASTType;
    public String labeledElementInit;
    public String labeledElementType;
    public String lexerClassName;
    public String lt1Value;
    public String parserClassName;
    public Vector semPreds;
    public String throwNoViable;
    public String treeWalkerClassName;
    public int syntacticPredLevel = 0;
    public boolean genAST = false;
    public boolean saveText = false;
    public Hashtable treeVariableMap = new Hashtable();
    public Hashtable declaredASTVariables = new Hashtable();
    public int astVarNumber = 1;

    public PythonCodeGenerator() {
        this.charFormatter = new PythonCharFormatter();
        this.DEBUG_CODE_GENERATOR = true;
    }

    private void GenRuleInvocation(RuleRefElement ruleRefElement) {
        Tool tool;
        String sb;
        StringBuilder m5a = C0000a.m5a("self.");
        m5a.append(ruleRefElement.targetRule);
        m5a.append("(");
        _print(m5a.toString());
        if (this.grammar instanceof LexerGrammar) {
            _print(ruleRefElement.getLabel() != null ? "True" : "False");
            if (this.commonExtraArgs.length() != 0 || ruleRefElement.args != null) {
                _print(", ");
            }
        }
        _print(this.commonExtraArgs);
        if (this.commonExtraArgs.length() != 0 && ruleRefElement.args != null) {
            _print(", ");
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(ruleRefElement.targetRule);
        if (ruleRefElement.args != null) {
            ActionTransInfo actionTransInfo = new ActionTransInfo();
            String processActionForSpecialSymbols = processActionForSpecialSymbols(ruleRefElement.args, 0, this.currentRule, actionTransInfo);
            if (actionTransInfo.assignToRoot || actionTransInfo.refRuleRoot != null) {
                Tool tool2 = this.antlrTool;
                StringBuilder m5a2 = C0000a.m5a("Arguments of rule reference '");
                m5a2.append(ruleRefElement.targetRule);
                m5a2.append("' cannot set or ref #");
                m5a2.append(this.currentRule.getRuleName());
                tool2.error(m5a2.toString(), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
            }
            _print(processActionForSpecialSymbols);
            if (ruleSymbol.block.argAction == null) {
                tool = this.antlrTool;
                sb = C0000a.m3a(C0000a.m5a("Rule '"), ruleRefElement.targetRule, "' accepts no arguments");
                tool.warning(sb, this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
            }
        } else if (ruleSymbol.block.argAction != null) {
            tool = this.antlrTool;
            StringBuilder m5a3 = C0000a.m5a("Missing parameters on reference to rule ");
            m5a3.append(ruleRefElement.targetRule);
            sb = m5a3.toString();
            tool.warning(sb, this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
        }
        _println(")");
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = self._retTree");
        }
    }

    private void genBitSet(BitSet bitSet, int i) {
        int i2 = this.tabs;
        int i3 = 0;
        this.tabs = 0;
        println("");
        println("### generate bit set");
        println("def mk" + getBitsetName(i) + "(): ");
        this.tabs = this.tabs + 1;
        int lengthInLongWords = bitSet.lengthInLongWords();
        if (lengthInLongWords < 8) {
            println("### var1");
            println("data = [ " + bitSet.toStringOfWords() + "]");
        } else {
            println("data = [0L] * " + lengthInLongWords + " ### init list");
            long[] packedArray = bitSet.toPackedArray();
            while (i3 < packedArray.length) {
                if (packedArray[i3] == 0) {
                    i3++;
                } else {
                    int i4 = i3 + 1;
                    if (i4 == packedArray.length || packedArray[i3] != packedArray[i4]) {
                        println("data[" + i3 + "] =" + packedArray[i3] + "L");
                    } else {
                        while (i4 < packedArray.length && packedArray[i4] == packedArray[i3]) {
                            i4++;
                        }
                        long j = packedArray[i3];
                        println("for x in xrange(" + i3 + ", " + i4 + "):");
                        this.tabs = this.tabs + 1;
                        StringBuilder sb = new StringBuilder();
                        sb.append("data[x] = ");
                        sb.append(j);
                        sb.append("L");
                        println(sb.toString());
                        this.tabs--;
                    }
                    i3 = i4;
                }
            }
        }
        println("return data");
        this.tabs--;
        println(getBitsetName(i) + " = antlr.BitSet(mk" + getBitsetName(i) + "())");
        this.tabs = i2;
    }

    private void genBlockFinish(PythonBlockFinishingInfo pythonBlockFinishingInfo, String str) {
        if (pythonBlockFinishingInfo.needAnErrorClause && (pythonBlockFinishingInfo.generatedAnIf || pythonBlockFinishingInfo.generatedSwitch)) {
            if (pythonBlockFinishingInfo.generatedAnIf) {
                println("else:");
            }
            this.tabs++;
            println(str);
            this.tabs--;
        }
        String str2 = pythonBlockFinishingInfo.postscript;
        if (str2 != null) {
            println(str2);
        }
    }

    private void genBlockFinish1(PythonBlockFinishingInfo pythonBlockFinishingInfo, String str) {
        if (pythonBlockFinishingInfo.needAnErrorClause && (pythonBlockFinishingInfo.generatedAnIf || pythonBlockFinishingInfo.generatedSwitch)) {
            if (pythonBlockFinishingInfo.generatedAnIf) {
                println("else:");
            }
            this.tabs++;
            println(str);
            this.tabs--;
            boolean z = pythonBlockFinishingInfo.generatedAnIf;
        }
        String str2 = pythonBlockFinishingInfo.postscript;
        if (str2 != null) {
            println(str2);
        }
    }

    private void genElementAST(AlternativeElement alternativeElement) {
        String str;
        String sb;
        StringBuilder sb2;
        String str2;
        StringBuilder m9b;
        String aSTCreateString;
        StringBuilder m9b2;
        String aSTCreateString2;
        String str3;
        Grammar grammar = this.grammar;
        if ((grammar instanceof TreeWalkerGrammar) && !grammar.buildAST) {
            if (alternativeElement.getLabel() == null) {
                String str4 = this.lt1Value;
                StringBuilder m5a = C0000a.m5a("tmp");
                m5a.append(this.astVarNumber);
                m5a.append("_AST");
                String sb3 = m5a.toString();
                this.astVarNumber++;
                mapTreeVariable(alternativeElement, sb3);
                println(sb3 + "_in = " + str4);
                return;
            }
            return;
        }
        if (this.grammar.buildAST && this.syntacticPredLevel == 0) {
            boolean z = this.genAST && !(alternativeElement.getLabel() == null && alternativeElement.getAutoGenType() == 3);
            if (alternativeElement.getAutoGenType() != 3 && (alternativeElement instanceof TokenRefElement)) {
                z = true;
            }
            boolean z2 = this.grammar.hasSyntacticPredicate;
            if (alternativeElement.getLabel() != null) {
                str = alternativeElement.getLabel();
                sb = alternativeElement.getLabel();
            } else {
                str = this.lt1Value;
                StringBuilder m5a2 = C0000a.m5a("tmp");
                m5a2.append(this.astVarNumber);
                sb = m5a2.toString();
                this.astVarNumber++;
            }
            if (z) {
                if (alternativeElement instanceof GrammarAtom) {
                    GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
                    if (grammarAtom.getASTNodeType() != null) {
                        str3 = grammarAtom.getASTNodeType();
                        genASTDeclaration(alternativeElement, sb, str3);
                    }
                }
                str3 = this.labeledElementASTType;
                genASTDeclaration(alternativeElement, sb, str3);
            }
            String m1a = C0000a.m1a(sb, "_AST");
            mapTreeVariable(alternativeElement, m1a);
            if (this.grammar instanceof TreeWalkerGrammar) {
                println(m1a + "_in = None");
            }
            if (alternativeElement.getLabel() != null) {
                if (alternativeElement instanceof GrammarAtom) {
                    m9b2 = C0000a.m9b(m1a, " = ");
                    aSTCreateString2 = getASTCreateString((GrammarAtom) alternativeElement, str);
                } else {
                    m9b2 = C0000a.m9b(m1a, " = ");
                    aSTCreateString2 = getASTCreateString(str);
                }
                m9b2.append(aSTCreateString2);
                m9b2.append("");
                println(m9b2.toString());
            }
            if (alternativeElement.getLabel() == null && z) {
                String str5 = this.lt1Value;
                if (alternativeElement instanceof GrammarAtom) {
                    m9b = C0000a.m9b(m1a, " = ");
                    aSTCreateString = getASTCreateString((GrammarAtom) alternativeElement, str5);
                } else {
                    m9b = C0000a.m9b(m1a, " = ");
                    aSTCreateString = getASTCreateString(str5);
                }
                m9b.append(aSTCreateString);
                m9b.append("");
                println(m9b.toString());
                if (this.grammar instanceof TreeWalkerGrammar) {
                    println(m1a + "_in = " + str5 + "");
                }
            }
            if (this.genAST) {
                int autoGenType = alternativeElement.getAutoGenType();
                if (autoGenType == 1) {
                    sb2 = new StringBuilder();
                    str2 = "self.addASTChild(currentAST, ";
                } else {
                    if (autoGenType != 2) {
                        return;
                    }
                    sb2 = new StringBuilder();
                    str2 = "self.makeASTRoot(currentAST, ";
                }
                sb2.append(str2);
                sb2.append(m1a);
                sb2.append(")");
                println(sb2.toString());
            }
        }
    }

    private void genErrorCatchForElement(AlternativeElement alternativeElement) {
        if (alternativeElement.getLabel() == null) {
            return;
        }
        String str = alternativeElement.enclosingRuleName;
        if (this.grammar instanceof LexerGrammar) {
            str = CodeGenerator.encodeLexerRuleName(str);
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(str);
        if (ruleSymbol == null) {
            this.antlrTool.panic("Enclosing rule not found!");
        }
        ExceptionSpec findExceptionSpec = ruleSymbol.block.findExceptionSpec(alternativeElement.getLabel());
        if (findExceptionSpec != null) {
            this.tabs--;
            genErrorHandler(findExceptionSpec);
        }
    }

    private void genErrorHandler(ExceptionSpec exceptionSpec) {
        String str;
        String str2;
        for (int i = 0; i < exceptionSpec.handlers.size(); i++) {
            ExceptionHandler exceptionHandler = (ExceptionHandler) exceptionSpec.handlers.elementAt(i);
            String trim = removeAssignmentFromDeclaration(exceptionHandler.exceptionTypeAndName.getText()).trim();
            int length = trim.length();
            while (true) {
                length--;
                str = "";
                if (length >= 0) {
                    if (!Character.isLetterOrDigit(trim.charAt(length)) && trim.charAt(length) != '_') {
                        str = trim.substring(0, length);
                        str2 = trim.substring(length + 1);
                        break;
                    }
                } else {
                    str2 = "";
                    break;
                }
            }
            println("except " + str + ", " + str2 + ":");
            this.tabs = this.tabs + 1;
            if (this.grammar.hasSyntacticPredicate) {
                println("if not self.inputState.guessing:");
                this.tabs++;
            }
            printAction(processActionForSpecialSymbols(exceptionHandler.action.getText(), exceptionHandler.action.getLine(), this.currentRule, new ActionTransInfo()));
            if (this.grammar.hasSyntacticPredicate) {
                this.tabs--;
                println("else:");
                this.tabs++;
                println("raise " + str2);
                this.tabs = this.tabs + (-1);
            }
            this.tabs--;
        }
    }

    private void genErrorTryForElement(AlternativeElement alternativeElement) {
        if (alternativeElement.getLabel() == null) {
            return;
        }
        String str = alternativeElement.enclosingRuleName;
        if (this.grammar instanceof LexerGrammar) {
            str = CodeGenerator.encodeLexerRuleName(str);
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(str);
        if (ruleSymbol == null) {
            this.antlrTool.panic("Enclosing rule not found!");
        }
        if (ruleSymbol.block.findExceptionSpec(alternativeElement.getLabel()) != null) {
            println("try: # for error handling");
            this.tabs++;
        }
    }

    private void genLiteralsTest() {
        println("### option { testLiterals=true } ");
        println("_ttype = self.testLiteralsTable(_ttype)");
    }

    private void genLiteralsTestForPartialToken() {
        println("_ttype = self.testLiteralsTable(self.text.getString(), _begin, self.text.length()-_begin, _ttype)");
    }

    private String getValueString(int i, boolean z) {
        Grammar grammar = this.grammar;
        if (grammar instanceof LexerGrammar) {
            String literalChar = this.charFormatter.literalChar(i);
            return z ? C0000a.m2a("u'", literalChar, "'") : literalChar;
        }
        TokenSymbol tokenSymbolAt = grammar.tokenManager.getTokenSymbolAt(i);
        if (tokenSymbolAt == null) {
            return "" + i;
        }
        String id = tokenSymbolAt.getId();
        if (!(tokenSymbolAt instanceof StringLiteralSymbol)) {
            return id;
        }
        String label = ((StringLiteralSymbol) tokenSymbolAt).getLabel();
        if (label != null) {
            return label;
        }
        String mangleLiteral = mangleLiteral(id);
        return mangleLiteral == null ? String.valueOf(i) : mangleLiteral;
    }

    public static boolean isEmpty(String str) {
        boolean z = true;
        for (int i = 0; z && i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt != '\t' && charAt != '\n' && charAt != '\f' && charAt != '\r' && charAt != ' ') {
                z = false;
            }
        }
        return z;
    }

    private String lookaheadString(int i) {
        if (this.grammar instanceof TreeWalkerGrammar) {
            return "_t.getType()";
        }
        return "self.LA(" + i + ")";
    }

    private String mangleLiteral(String str) {
        String str2 = this.antlrTool.literalsPrefix;
        for (int i = 1; i < str.length() - 1; i++) {
            if (!Character.isLetter(str.charAt(i)) && str.charAt(i) != '_') {
                return null;
            }
            StringBuilder m5a = C0000a.m5a(str2);
            m5a.append(str.charAt(i));
            str2 = m5a.toString();
        }
        return this.antlrTool.upperCaseMangledLiterals ? str2.toUpperCase() : str2;
    }

    private void mapTreeVariable(AlternativeElement alternativeElement, String str) {
        if (alternativeElement instanceof TreeElement) {
            mapTreeVariable(((TreeElement) alternativeElement).root, str);
            return;
        }
        String str2 = null;
        if (alternativeElement.getLabel() == null) {
            if (alternativeElement instanceof TokenRefElement) {
                str2 = ((TokenRefElement) alternativeElement).atomText;
            } else if (alternativeElement instanceof RuleRefElement) {
                str2 = ((RuleRefElement) alternativeElement).targetRule;
            }
        }
        if (str2 != null) {
            if (this.treeVariableMap.get(str2) == null) {
                this.treeVariableMap.put(str2, str);
            } else {
                this.treeVariableMap.remove(str2);
                this.treeVariableMap.put(str2, NONUNIQUE);
            }
        }
    }

    private void setupGrammarParameters(Grammar grammar) {
        Token option;
        String stripFrontBack;
        Token option2;
        String stripFrontBack2;
        Token option3;
        String stripFrontBack3;
        Token option4;
        String stripFrontBack4;
        Token option5;
        String stripFrontBack5;
        if (grammar instanceof ParserGrammar) {
            this.labeledElementASTType = "";
            if (grammar.hasOption("ASTLabelType") && (option5 = grammar.getOption("ASTLabelType")) != null && (stripFrontBack5 = StringUtils.stripFrontBack(option5.getText(), "\"", "\"")) != null) {
                this.labeledElementASTType = stripFrontBack5;
            }
            this.labeledElementType = "";
            this.labeledElementInit = "None";
            this.commonExtraArgs = "";
            this.commonExtraParams = "self";
            this.commonLocalVars = "";
            this.lt1Value = "self.LT(1)";
            this.exceptionThrown = "antlr.RecognitionException";
            this.throwNoViable = "raise antlr.NoViableAltException(self.LT(1), self.getFilename())";
            this.parserClassName = "Parser";
            if (!grammar.hasOption("className") || (option4 = grammar.getOption("className")) == null || (stripFrontBack4 = StringUtils.stripFrontBack(option4.getText(), "\"", "\"")) == null) {
                return;
            }
            this.parserClassName = stripFrontBack4;
            return;
        }
        if (grammar instanceof LexerGrammar) {
            this.labeledElementType = "char ";
            this.labeledElementInit = "'\\0'";
            this.commonExtraArgs = "";
            this.commonExtraParams = "self, _createToken";
            this.commonLocalVars = "_ttype = 0\n        _token = None\n        _begin = self.text.length()";
            this.lt1Value = "self.LA(1)";
            this.exceptionThrown = "antlr.RecognitionException";
            this.throwNoViable = "self.raise_NoViableAlt(self.LA(1))";
            this.lexerClassName = "Lexer";
            if (!grammar.hasOption("className") || (option3 = grammar.getOption("className")) == null || (stripFrontBack3 = StringUtils.stripFrontBack(option3.getText(), "\"", "\"")) == null) {
                return;
            }
            this.lexerClassName = stripFrontBack3;
            return;
        }
        if (!(grammar instanceof TreeWalkerGrammar)) {
            this.antlrTool.panic("Unknown grammar type");
            return;
        }
        this.labeledElementASTType = "";
        this.labeledElementType = "";
        if (grammar.hasOption("ASTLabelType") && (option2 = grammar.getOption("ASTLabelType")) != null && (stripFrontBack2 = StringUtils.stripFrontBack(option2.getText(), "\"", "\"")) != null) {
            this.labeledElementASTType = stripFrontBack2;
            this.labeledElementType = stripFrontBack2;
        }
        if (!grammar.hasOption("ASTLabelType")) {
            grammar.setOption("ASTLabelType", new Token(6, "<4>AST"));
        }
        this.labeledElementInit = "None";
        this.commonExtraArgs = "_t";
        this.commonExtraParams = "self, _t";
        this.commonLocalVars = "";
        this.lt1Value = "_t";
        this.exceptionThrown = "antlr.RecognitionException";
        this.throwNoViable = "raise antlr.NoViableAltException(_t)";
        this.treeWalkerClassName = "Walker";
        if (!grammar.hasOption("className") || (option = grammar.getOption("className")) == null || (stripFrontBack = StringUtils.stripFrontBack(option.getText(), "\"", "\"")) == null) {
            return;
        }
        this.treeWalkerClassName = stripFrontBack;
    }

    public static boolean suitableForCaseExpression(Alternative alternative) {
        return alternative.lookaheadDepth == 1 && alternative.semPred == null && !alternative.cache[1].containsEpsilon() && alternative.cache[1].fset.degree() <= 127;
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00bc A[EDGE_INSN: B:58:0x00bc->B:61:0x00c3 BREAK  A[LOOP:3: B:44:0x008f->B:57:0x00c0]] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00c3 A[SYNTHETIC] */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void _printAction(String str) {
        PrintWriter printWriter;
        String str2;
        if (str == null) {
            return;
        }
        int length = str.length();
        int i = 0;
        int i2 = 0;
        boolean z = true;
        while (i < length && z) {
            int i3 = i + 1;
            char charAt = str.charAt(i);
            if (charAt != '\n') {
                if (charAt != '\r') {
                    if (charAt != ' ') {
                        z = false;
                    }
                    i = i3;
                } else if (i3 <= length && str.charAt(i3) == '\n') {
                    i3++;
                }
            }
            i2 = i3;
            i = i2;
        }
        if (!z) {
            i--;
        }
        int i4 = i - i2;
        int i5 = length - 1;
        while (i5 > i && isspace(str.charAt(i5))) {
            i5--;
        }
        boolean z2 = false;
        while (i <= i5) {
            char charAt2 = str.charAt(i);
            if (charAt2 != '\t') {
                if (charAt2 != '\n') {
                    if (charAt2 == '\r') {
                        int i6 = i + 1;
                        if (i6 <= i5 && str.charAt(i6) == '\n') {
                            i = i6;
                        }
                    } else if (charAt2 != ' ') {
                        this.currentOutput.print(charAt2);
                        if (z2) {
                            this.currentOutput.print("\n");
                            printTabs();
                            i++;
                            z2 = false;
                            int i7 = 0;
                            while (true) {
                                if (i <= i5) {
                                    char charAt3 = str.charAt(i);
                                    if (!isspace(charAt3)) {
                                        i--;
                                        break;
                                    }
                                    if (charAt3 != '\n') {
                                        if (charAt3 == '\r') {
                                            int i8 = i + 1;
                                            if (i8 <= i5 && str.charAt(i8) == '\n') {
                                                i = i8;
                                            }
                                        }
                                        if (!z2) {
                                            this.currentOutput.print("\n");
                                            printTabs();
                                            z2 = false;
                                            i7 = 0;
                                        } else if (i7 < i4) {
                                            i7++;
                                        }
                                        i++;
                                    }
                                    z2 = true;
                                    if (!z2) {
                                    }
                                    i++;
                                }
                            }
                        }
                        i++;
                    } else {
                        printWriter = this.currentOutput;
                        str2 = " ";
                    }
                }
                z2 = true;
                if (z2) {
                }
                i++;
            } else {
                System.err.println("warning: tab characters used in Python action");
                printWriter = this.currentOutput;
                str2 = "        ";
            }
            printWriter.print(str2);
            if (z2) {
            }
            i++;
        }
        this.currentOutput.println();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void _printJavadoc(String str) {
        PrintWriter printWriter;
        String str2;
        int length = str.length();
        this.currentOutput.print("\n");
        printTabs();
        this.currentOutput.print("###");
        int i = 0;
        boolean z = false;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt != '\t') {
                if (charAt != '\n') {
                    if (charAt == '\r') {
                        int i2 = i + 1;
                        if (i2 <= length && str.charAt(i2) == '\n') {
                            i = i2;
                        }
                    } else if (charAt != ' ') {
                        this.currentOutput.print(charAt);
                        if (z) {
                            this.currentOutput.print("\n");
                            printTabs();
                            this.currentOutput.print("###");
                            z = false;
                        }
                        i++;
                    } else {
                        printWriter = this.currentOutput;
                        str2 = " ";
                    }
                }
                z = true;
                if (z) {
                }
                i++;
            } else {
                printWriter = this.currentOutput;
                str2 = "\t";
            }
            printWriter.print(str2);
            if (z) {
            }
            i++;
        }
        this.currentOutput.println();
    }

    public int addSemPred(String str) {
        this.semPreds.appendElement(str);
        return this.semPreds.size() - 1;
    }

    public void checkCurrentOutputStream() {
        try {
            if (this.currentOutput != null) {
            } else {
                throw new NullPointerException();
            }
        } catch (Exception unused) {
            Utils.error("current output is not set");
            throw null;
        }
    }

    public void exitIfError() {
        if (this.antlrTool.hasError()) {
            this.antlrTool.fatalError("Exiting due to errors.");
        }
    }

    @Override // antlr.CodeGenerator
    public String extractIdOfAction(String str, int i, int i2) {
        return removeAssignmentFromDeclaration(str).trim();
    }

    @Override // antlr.CodeGenerator
    public String extractTypeOfAction(String str, int i, int i2) {
        return "";
    }

    public void flushTokens() {
        try {
            checkCurrentOutputStream();
            println("");
            println("### import antlr.Token ");
            println("from antlr import Token");
            println("### >>>The Known Token Types <<<");
            PrintWriter printWriter = this.currentOutput;
            Enumeration elements = this.behavior.tokenManagers.elements();
            boolean z = false;
            while (elements.hasMoreElements()) {
                TokenManager tokenManager = (TokenManager) elements.nextElement();
                if (!tokenManager.isReadOnly()) {
                    if (!z) {
                        genTokenTypes(tokenManager);
                        z = true;
                    }
                    this.currentOutput = printWriter;
                    genTokenInterchange(tokenManager);
                    this.currentOutput = printWriter;
                }
                exitIfError();
            }
        } catch (Exception unused) {
            exitIfError();
        }
        checkCurrentOutputStream();
        println("");
    }

    @Override // antlr.CodeGenerator
    public void gen() {
        try {
            Enumeration elements = this.behavior.grammars.elements();
            while (elements.hasMoreElements()) {
                Grammar grammar = (Grammar) elements.nextElement();
                grammar.setGrammarAnalyzer(this.analyzer);
                grammar.setCodeGenerator(this);
                this.analyzer.setGrammar(grammar);
                setupGrammarParameters(grammar);
                grammar.generate();
                exitIfError();
            }
        } catch (IOException e) {
            this.antlrTool.reportException(e, null);
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(ActionElement actionElement) {
        if (actionElement.isSemPred) {
            genSemPred(actionElement.actionText, actionElement.line);
            return;
        }
        if (this.grammar.hasSyntacticPredicate) {
            println("if not self.inputState.guessing:");
            this.tabs++;
        }
        ActionTransInfo actionTransInfo = new ActionTransInfo();
        String processActionForSpecialSymbols = processActionForSpecialSymbols(actionElement.actionText, actionElement.getLine(), this.currentRule, actionTransInfo);
        if (actionTransInfo.refRuleRoot != null) {
            println(actionTransInfo.refRuleRoot + " = currentAST.root");
        }
        printAction(processActionForSpecialSymbols);
        if (actionTransInfo.assignToRoot) {
            StringBuilder m5a = C0000a.m5a("currentAST.root = ");
            m5a.append(actionTransInfo.refRuleRoot);
            m5a.append("");
            println(m5a.toString());
            println("if (" + actionTransInfo.refRuleRoot + " != None) and (" + actionTransInfo.refRuleRoot + ".getFirstChild() != None):");
            this.tabs = this.tabs + 1;
            StringBuilder m5a2 = C0000a.m5a("currentAST.child = ");
            m5a2.append(actionTransInfo.refRuleRoot);
            m5a2.append(".getFirstChild()");
            println(m5a2.toString());
            this.tabs = this.tabs + (-1);
            println("else:");
            this.tabs++;
            StringBuilder m5a3 = C0000a.m5a("currentAST.child = ");
            m5a3.append(actionTransInfo.refRuleRoot);
            println(m5a3.toString());
            this.tabs--;
            println("currentAST.advanceChildToEnd()");
        }
        if (this.grammar.hasSyntacticPredicate) {
            this.tabs--;
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(AlternativeBlock alternativeBlock) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("gen(" + alternativeBlock + ")");
        }
        genBlockPreamble(alternativeBlock);
        genBlockInitAction(alternativeBlock);
        String str = this.currentASTResult;
        if (alternativeBlock.getLabel() != null) {
            this.currentASTResult = alternativeBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(alternativeBlock);
        int i = this.tabs;
        genBlockFinish(genCommonBlock(alternativeBlock, true), this.throwNoViable);
        this.tabs = i;
        this.currentASTResult = str;
    }

    @Override // antlr.CodeGenerator
    public void gen(BlockEndElement blockEndElement) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genRuleEnd(" + blockEndElement + ")");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(CharLiteralElement charLiteralElement) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genChar(" + charLiteralElement + ")");
        }
        if (charLiteralElement.getLabel() != null) {
            println(charLiteralElement.getLabel() + " = " + this.lt1Value);
        }
        boolean z = this.saveText;
        this.saveText = z && charLiteralElement.getAutoGenType() == 1;
        genMatch(charLiteralElement);
        this.saveText = z;
    }

    @Override // antlr.CodeGenerator
    public void gen(CharRangeElement charRangeElement) {
        if (charRangeElement.getLabel() != null && this.syntacticPredLevel == 0) {
            println(charRangeElement.getLabel() + " = " + this.lt1Value);
        }
        boolean z = (this.grammar instanceof LexerGrammar) && (!this.saveText || charRangeElement.getAutoGenType() == 3);
        if (z) {
            println("_saveIndex = self.text.length()");
        }
        StringBuilder m5a = C0000a.m5a("self.matchRange(u");
        m5a.append(charRangeElement.beginText);
        m5a.append(", u");
        m5a.append(charRangeElement.endText);
        m5a.append(")");
        println(m5a.toString());
        if (z) {
            println("self.text.setLength(_saveIndex)");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(LexerGrammar lexerGrammar) {
        if (lexerGrammar.debuggingOutput) {
            this.semPreds = new Vector();
        }
        setGrammar(lexerGrammar);
        if (!(this.grammar instanceof LexerGrammar)) {
            this.antlrTool.panic("Internal error generating lexer");
        }
        setupOutput(this.grammar.getClassName());
        this.genAST = false;
        this.saveText = true;
        this.tabs = 0;
        genHeader();
        println("### import antlr and other modules ..");
        println("import sys");
        println("import antlr");
        println("");
        println("version = sys.version.split()[0]");
        println("if version < '2.2.1':");
        this.tabs++;
        println("False = 0");
        this.tabs--;
        println("if version < '2.3':");
        this.tabs++;
        println("True = not False");
        this.tabs--;
        println("### header action >>> ");
        printActionCode(this.behavior.getHeaderAction(""), 0);
        println("### header action <<< ");
        println("### preamble action >>> ");
        printActionCode(this.grammar.preambleAction.getText(), 0);
        println("### preamble action <<< ");
        String str = this.grammar.superClass;
        if (str == null) {
            StringBuilder m5a = C0000a.m5a("antlr.");
            m5a.append(this.grammar.getSuperClass());
            str = m5a.toString();
        }
        Token token = (Token) this.grammar.options.get("classHeaderPrefix");
        if (token != null) {
            StringUtils.stripFrontBack(token.getText(), "\"", "\"");
        }
        println("### >>>The Literals<<<");
        println("literals = {}");
        Enumeration tokenSymbolKeys = this.grammar.tokenManager.getTokenSymbolKeys();
        while (tokenSymbolKeys.hasMoreElements()) {
            String str2 = (String) tokenSymbolKeys.nextElement();
            if (str2.charAt(0) == '\"') {
                TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str2);
                if (tokenSymbol instanceof StringLiteralSymbol) {
                    StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenSymbol;
                    StringBuilder m5a2 = C0000a.m5a("literals[u");
                    m5a2.append(stringLiteralSymbol.getId());
                    m5a2.append("] = ");
                    m5a2.append(stringLiteralSymbol.getTokenType());
                    println(m5a2.toString());
                }
            }
        }
        println("");
        flushTokens();
        genJavadocComment(this.grammar);
        println("class " + this.lexerClassName + "(" + str + ") :");
        this.tabs = this.tabs + 1;
        printGrammarAction(this.grammar);
        println("def __init__(self, *argv, **kwargs) :");
        this.tabs = this.tabs + 1;
        println(str + ".__init__(self, *argv, **kwargs)");
        println("self.caseSensitiveLiterals = " + toString(lexerGrammar.caseSensitiveLiterals));
        println("self.setCaseSensitive(" + toString(lexerGrammar.caseSensitive) + ")");
        println("self.literals = literals");
        if (this.grammar.debuggingOutput) {
            println("ruleNames[] = [");
            Enumeration elements = this.grammar.rules.elements();
            this.tabs++;
            while (elements.hasMoreElements()) {
                GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
                if (grammarSymbol instanceof RuleSymbol) {
                    StringBuilder m5a3 = C0000a.m5a("\"");
                    m5a3.append(((RuleSymbol) grammarSymbol).getId());
                    m5a3.append("\",");
                    println(m5a3.toString());
                }
            }
            this.tabs--;
            println("]");
        }
        genHeaderInit(this.grammar);
        this.tabs--;
        genNextToken();
        println("");
        Enumeration elements2 = this.grammar.rules.elements();
        int i = 0;
        while (elements2.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) elements2.nextElement();
            if (!ruleSymbol.getId().equals("mnextToken")) {
                genRule(ruleSymbol, false, i);
                i++;
            }
            exitIfError();
        }
        if (this.grammar.debuggingOutput) {
            genSemPredMap();
        }
        genBitsets(this.bitsetsUsed, ((LexerGrammar) this.grammar).charVocabulary.size());
        println("");
        genHeaderMain(this.grammar);
        this.currentOutput.close();
        this.currentOutput = null;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0094  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(OneOrMoreBlock oneOrMoreBlock) {
        StringBuilder m5a;
        int i;
        boolean z;
        int i2;
        genBlockPreamble(oneOrMoreBlock);
        if (oneOrMoreBlock.getLabel() != null) {
            m5a = C0000a.m5a("_cnt_");
            m5a.append(oneOrMoreBlock.getLabel());
        } else {
            m5a = C0000a.m5a("_cnt");
            m5a.append(oneOrMoreBlock.f302ID);
        }
        String sb = m5a.toString();
        println("" + sb + "= 0");
        println("while True:");
        this.tabs = this.tabs + 1;
        int i3 = this.tabs;
        genBlockInitAction(oneOrMoreBlock);
        String str = this.currentASTResult;
        if (oneOrMoreBlock.getLabel() != null) {
            this.currentASTResult = oneOrMoreBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(oneOrMoreBlock);
        int i4 = this.grammar.maxk;
        if (!oneOrMoreBlock.greedy && (i2 = oneOrMoreBlock.exitLookaheadDepth) <= i4 && oneOrMoreBlock.exitCache[i2].containsEpsilon()) {
            i4 = oneOrMoreBlock.exitLookaheadDepth;
        } else if (oneOrMoreBlock.greedy || oneOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
            i = i4;
            z = false;
            if (z) {
                StringBuilder m5a2 = C0000a.m5a("### nongreedy (...)+ loop; exit depth is ");
                m5a2.append(oneOrMoreBlock.exitLookaheadDepth);
                println(m5a2.toString());
                String lookaheadTestExpression = getLookaheadTestExpression(oneOrMoreBlock.exitCache, i);
                println("### nongreedy exit test");
                println("if " + sb + " >= 1 and " + lookaheadTestExpression + ":");
                this.tabs = this.tabs + 1;
                println("break");
                this.tabs = this.tabs - 1;
            }
            int i5 = this.tabs;
            genBlockFinish(genCommonBlock(oneOrMoreBlock, false), "break");
            this.tabs = i5;
            this.tabs = i3;
            println(sb + " += 1");
            this.tabs = i3;
            this.tabs = this.tabs - 1;
            println("if " + sb + " < 1:");
            this.tabs = this.tabs + 1;
            println(this.throwNoViable);
            this.tabs = this.tabs - 1;
            this.currentASTResult = str;
        }
        i = i4;
        z = true;
        if (z) {
        }
        int i52 = this.tabs;
        genBlockFinish(genCommonBlock(oneOrMoreBlock, false), "break");
        this.tabs = i52;
        this.tabs = i3;
        println(sb + " += 1");
        this.tabs = i3;
        this.tabs = this.tabs - 1;
        println("if " + sb + " < 1:");
        this.tabs = this.tabs + 1;
        println(this.throwNoViable);
        this.tabs = this.tabs - 1;
        this.currentASTResult = str;
    }

    @Override // antlr.CodeGenerator
    public void gen(ParserGrammar parserGrammar) {
        if (parserGrammar.debuggingOutput) {
            this.semPreds = new Vector();
        }
        setGrammar(parserGrammar);
        if (!(this.grammar instanceof ParserGrammar)) {
            this.antlrTool.panic("Internal error generating parser");
        }
        setupOutput(this.grammar.getClassName());
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        genHeader();
        println("### import antlr and other modules ..");
        println("import sys");
        println("import antlr");
        println("");
        println("version = sys.version.split()[0]");
        println("if version < '2.2.1':");
        this.tabs++;
        println("False = 0");
        this.tabs--;
        println("if version < '2.3':");
        this.tabs++;
        println("True = not False");
        this.tabs--;
        println("### header action >>> ");
        printActionCode(this.behavior.getHeaderAction(""), 0);
        println("### header action <<< ");
        println("### preamble action>>>");
        printActionCode(this.grammar.preambleAction.getText(), 0);
        println("### preamble action <<<");
        flushTokens();
        String str = this.grammar.superClass;
        if (str == null) {
            StringBuilder m5a = C0000a.m5a("antlr.");
            m5a.append(this.grammar.getSuperClass());
            str = m5a.toString();
        }
        genJavadocComment(this.grammar);
        Token token = (Token) this.grammar.options.get("classHeaderPrefix");
        if (token != null) {
            StringUtils.stripFrontBack(token.getText(), "\"", "\"");
        }
        StringBuilder m5a2 = C0000a.m5a("class ");
        m5a2.append(this.parserClassName);
        m5a2.append("(");
        m5a2.append(str);
        print(m5a2.toString());
        println("):");
        this.tabs++;
        if (this.grammar.debuggingOutput) {
            println("_ruleNames = [");
            Enumeration elements = this.grammar.rules.elements();
            this.tabs++;
            while (elements.hasMoreElements()) {
                GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
                if (grammarSymbol instanceof RuleSymbol) {
                    StringBuilder m5a3 = C0000a.m5a("\"");
                    m5a3.append(((RuleSymbol) grammarSymbol).getId());
                    m5a3.append("\",");
                    println(m5a3.toString());
                }
            }
            this.tabs--;
            println("]");
        }
        printGrammarAction(this.grammar);
        println("");
        println("def __init__(self, *args, **kwargs):");
        this.tabs++;
        println(str + ".__init__(self, *args, **kwargs)");
        println("self.tokenNames = _tokenNames");
        if (this.grammar.debuggingOutput) {
            println("self.ruleNames  = _ruleNames");
            println("self.semPredNames = _semPredNames");
            println("self.setupDebugging(self.tokenBuf)");
        }
        if (this.grammar.buildAST) {
            println("self.buildTokenTypeASTClassMap()");
            println("self.astFactory = antlr.ASTFactory(self.getTokenTypeToASTClassMap())");
            if (this.labeledElementASTType != null) {
                StringBuilder m5a4 = C0000a.m5a("self.astFactory.setASTNodeClass(");
                m5a4.append(this.labeledElementASTType);
                m5a4.append(")");
                println(m5a4.toString());
            }
        }
        genHeaderInit(this.grammar);
        println("");
        Enumeration elements2 = this.grammar.rules.elements();
        int i = 0;
        while (elements2.hasMoreElements()) {
            GrammarSymbol grammarSymbol2 = (GrammarSymbol) elements2.nextElement();
            if (grammarSymbol2 instanceof RuleSymbol) {
                RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol2;
                genRule(ruleSymbol, ruleSymbol.references.size() == 0, i);
                i++;
            }
            exitIfError();
        }
        if (this.grammar.buildAST) {
            genTokenASTNodeMap();
        }
        genTokenStrings();
        genBitsets(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType());
        if (this.grammar.debuggingOutput) {
            genSemPredMap();
        }
        println("");
        this.tabs = 0;
        genHeaderMain(this.grammar);
        this.currentOutput.close();
        this.currentOutput = null;
    }

    @Override // antlr.CodeGenerator
    public void gen(RuleRefElement ruleRefElement) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genRR(" + ruleRefElement + ")");
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(ruleRefElement.targetRule);
        if (ruleSymbol == null || !ruleSymbol.isDefined()) {
            this.antlrTool.error(C0000a.m3a(C0000a.m5a("Rule '"), ruleRefElement.targetRule, "' is not defined"), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
            return;
        }
        genErrorTryForElement(ruleRefElement);
        if ((this.grammar instanceof TreeWalkerGrammar) && ruleRefElement.getLabel() != null && this.syntacticPredLevel == 0) {
            println(ruleRefElement.getLabel() + " = antlr.ifelse(_t == antlr.ASTNULL, None, " + this.lt1Value + ")");
        }
        if ((this.grammar instanceof LexerGrammar) && (!this.saveText || ruleRefElement.getAutoGenType() == 3)) {
            println("_saveIndex = self.text.length()");
        }
        printTabs();
        if (ruleRefElement.idAssign != null) {
            if (ruleSymbol.block.returnAction == null) {
                this.antlrTool.warning(C0000a.m3a(C0000a.m5a("Rule '"), ruleRefElement.targetRule, "' has no return type"), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
            }
            _print(ruleRefElement.idAssign + "=");
        } else if (!(this.grammar instanceof LexerGrammar) && this.syntacticPredLevel == 0 && ruleSymbol.block.returnAction != null) {
            this.antlrTool.warning(C0000a.m3a(C0000a.m5a("Rule '"), ruleRefElement.targetRule, "' returns a value"), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
        }
        GenRuleInvocation(ruleRefElement);
        if ((this.grammar instanceof LexerGrammar) && (!this.saveText || ruleRefElement.getAutoGenType() == 3)) {
            println("self.text.setLength(_saveIndex)");
        }
        if (this.syntacticPredLevel == 0) {
            Grammar grammar = this.grammar;
            if (grammar.hasSyntacticPredicate && ((!grammar.buildAST || ruleRefElement.getLabel() == null) && this.genAST)) {
                ruleRefElement.getAutoGenType();
            }
            if (this.grammar.buildAST && ruleRefElement.getLabel() != null) {
                println(ruleRefElement.getLabel() + "_AST = self.returnAST");
            }
            if (this.genAST) {
                int autoGenType = ruleRefElement.getAutoGenType();
                if (autoGenType == 1) {
                    println("self.addASTChild(currentAST, self.returnAST)");
                } else if (autoGenType == 2) {
                    this.antlrTool.error("Internal: encountered ^ after rule reference");
                }
            }
            if ((this.grammar instanceof LexerGrammar) && ruleRefElement.getLabel() != null) {
                println(ruleRefElement.getLabel() + " = self._returnToken");
            }
        }
        genErrorCatchForElement(ruleRefElement);
    }

    @Override // antlr.CodeGenerator
    public void gen(StringLiteralElement stringLiteralElement) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genString(" + stringLiteralElement + ")");
        }
        if (stringLiteralElement.getLabel() != null && this.syntacticPredLevel == 0) {
            println(stringLiteralElement.getLabel() + " = " + this.lt1Value + "");
        }
        genElementAST(stringLiteralElement);
        boolean z = this.saveText;
        this.saveText = z && stringLiteralElement.getAutoGenType() == 1;
        genMatch(stringLiteralElement);
        this.saveText = z;
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t.getNextSibling()");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRangeElement tokenRangeElement) {
        genErrorTryForElement(tokenRangeElement);
        if (tokenRangeElement.getLabel() != null && this.syntacticPredLevel == 0) {
            println(tokenRangeElement.getLabel() + " = " + this.lt1Value);
        }
        genElementAST(tokenRangeElement);
        StringBuilder m5a = C0000a.m5a("self.matchRange(u");
        m5a.append(tokenRangeElement.beginText);
        m5a.append(", u");
        m5a.append(tokenRangeElement.endText);
        m5a.append(")");
        println(m5a.toString());
        genErrorCatchForElement(tokenRangeElement);
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRefElement tokenRefElement) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genTokenRef(" + tokenRefElement + ")");
        }
        if (this.grammar instanceof LexerGrammar) {
            this.antlrTool.panic("Token reference found in lexer");
        }
        genErrorTryForElement(tokenRefElement);
        if (tokenRefElement.getLabel() != null && this.syntacticPredLevel == 0) {
            println(tokenRefElement.getLabel() + " = " + this.lt1Value + "");
        }
        genElementAST(tokenRefElement);
        genMatch(tokenRefElement);
        genErrorCatchForElement(tokenRefElement);
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t.getNextSibling()");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeElement treeElement) {
        StringBuilder m5a = C0000a.m5a("_t");
        m5a.append(treeElement.f302ID);
        m5a.append(" = _t");
        println(m5a.toString());
        if (treeElement.root.getLabel() != null) {
            println(treeElement.root.getLabel() + " = antlr.ifelse(_t == antlr.ASTNULL, None, _t)");
        }
        if (treeElement.root.getAutoGenType() == 3) {
            this.antlrTool.error("Suffixing a root node with '!' is not implemented", this.grammar.getFilename(), treeElement.getLine(), treeElement.getColumn());
            treeElement.root.setAutoGenType(1);
        }
        if (treeElement.root.getAutoGenType() == 2) {
            this.antlrTool.warning("Suffixing a root node with '^' is redundant; already a root", this.grammar.getFilename(), treeElement.getLine(), treeElement.getColumn());
            treeElement.root.setAutoGenType(1);
        }
        genElementAST(treeElement.root);
        if (this.grammar.buildAST) {
            StringBuilder m5a2 = C0000a.m5a("_currentAST");
            m5a2.append(treeElement.f302ID);
            m5a2.append(" = currentAST.copy()");
            println(m5a2.toString());
            println("currentAST.root = currentAST.child");
            println("currentAST.child = None");
        }
        GrammarAtom grammarAtom = treeElement.root;
        if (grammarAtom instanceof WildcardElement) {
            println("if not _t: raise antlr.MismatchedTokenException()");
        } else {
            genMatch(grammarAtom);
        }
        println("_t = _t.getFirstChild()");
        for (int i = 0; i < treeElement.getAlternatives().size(); i++) {
            for (AlternativeElement alternativeElement = treeElement.getAlternativeAt(i).head; alternativeElement != null; alternativeElement = alternativeElement.next) {
                alternativeElement.generate();
            }
        }
        if (this.grammar.buildAST) {
            StringBuilder m5a3 = C0000a.m5a("currentAST = _currentAST");
            m5a3.append(treeElement.f302ID);
            m5a3.append("");
            println(m5a3.toString());
        }
        StringBuilder m5a4 = C0000a.m5a("_t = _t");
        m5a4.append(treeElement.f302ID);
        m5a4.append("");
        println(m5a4.toString());
        println("_t = _t.getNextSibling()");
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeWalkerGrammar treeWalkerGrammar) {
        setGrammar(treeWalkerGrammar);
        if (!(this.grammar instanceof TreeWalkerGrammar)) {
            this.antlrTool.panic("Internal error generating tree-walker");
        }
        setupOutput(this.grammar.getClassName());
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        genHeader();
        println("### import antlr and other modules ..");
        println("import sys");
        println("import antlr");
        println("");
        println("version = sys.version.split()[0]");
        println("if version < '2.2.1':");
        this.tabs++;
        println("False = 0");
        this.tabs--;
        println("if version < '2.3':");
        this.tabs++;
        println("True = not False");
        this.tabs--;
        println("### header action >>> ");
        printActionCode(this.behavior.getHeaderAction(""), 0);
        println("### header action <<< ");
        flushTokens();
        println("### user code>>>");
        printActionCode(this.grammar.preambleAction.getText(), 0);
        println("### user code<<<");
        String str = this.grammar.superClass;
        if (str == null) {
            StringBuilder m5a = C0000a.m5a("antlr.");
            m5a.append(this.grammar.getSuperClass());
            str = m5a.toString();
        }
        println("");
        Token token = (Token) this.grammar.options.get("classHeaderPrefix");
        if (token != null) {
            StringUtils.stripFrontBack(token.getText(), "\"", "\"");
        }
        genJavadocComment(this.grammar);
        println("class " + this.treeWalkerClassName + "(" + str + "):");
        this.tabs = this.tabs + 1;
        println("");
        println("# ctor ..");
        println("def __init__(self, *args, **kwargs):");
        this.tabs = this.tabs + 1;
        println(str + ".__init__(self, *args, **kwargs)");
        println("self.tokenNames = _tokenNames");
        genHeaderInit(this.grammar);
        this.tabs = this.tabs - 1;
        println("");
        printGrammarAction(this.grammar);
        Enumeration elements = this.grammar.rules.elements();
        int i = 0;
        while (elements.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol;
                genRule(ruleSymbol, ruleSymbol.references.size() == 0, i);
                i++;
            }
            exitIfError();
        }
        genTokenStrings();
        genBitsets(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType());
        this.tabs = 0;
        genHeaderMain(this.grammar);
        this.currentOutput.close();
        this.currentOutput = null;
    }

    @Override // antlr.CodeGenerator
    public void gen(WildcardElement wildcardElement) {
        String sb;
        if (wildcardElement.getLabel() != null && this.syntacticPredLevel == 0) {
            println(wildcardElement.getLabel() + " = " + this.lt1Value + "");
        }
        genElementAST(wildcardElement);
        Grammar grammar = this.grammar;
        if (grammar instanceof TreeWalkerGrammar) {
            println("if not _t:");
            this.tabs++;
            println("raise antlr.MismatchedTokenException()");
            this.tabs--;
        } else {
            boolean z = grammar instanceof LexerGrammar;
            if (z) {
                if (z && (!this.saveText || wildcardElement.getAutoGenType() == 3)) {
                    println("_saveIndex = self.text.length()");
                }
                println("self.matchNot(antlr.EOF_CHAR)");
                if ((this.grammar instanceof LexerGrammar) && (!this.saveText || wildcardElement.getAutoGenType() == 3)) {
                    sb = "self.text.setLength(_saveIndex)";
                }
            } else {
                StringBuilder m5a = C0000a.m5a("self.matchNot(");
                m5a.append(getValueString(1, false));
                m5a.append(")");
                sb = m5a.toString();
            }
            println(sb);
        }
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t.getNextSibling()");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0056  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(ZeroOrMoreBlock zeroOrMoreBlock) {
        int i;
        boolean z;
        int i2;
        genBlockPreamble(zeroOrMoreBlock);
        println("while True:");
        this.tabs++;
        int i3 = this.tabs;
        genBlockInitAction(zeroOrMoreBlock);
        String str = this.currentASTResult;
        if (zeroOrMoreBlock.getLabel() != null) {
            this.currentASTResult = zeroOrMoreBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(zeroOrMoreBlock);
        int i4 = this.grammar.maxk;
        if (!zeroOrMoreBlock.greedy && (i2 = zeroOrMoreBlock.exitLookaheadDepth) <= i4 && zeroOrMoreBlock.exitCache[i2].containsEpsilon()) {
            i4 = zeroOrMoreBlock.exitLookaheadDepth;
        } else if (zeroOrMoreBlock.greedy || zeroOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
            i = i4;
            z = false;
            if (z) {
                if (this.DEBUG_CODE_GENERATOR) {
                    PrintStream printStream = System.out;
                    StringBuilder m5a = C0000a.m5a("nongreedy (...)* loop; exit depth is ");
                    m5a.append(zeroOrMoreBlock.exitLookaheadDepth);
                    printStream.println(m5a.toString());
                }
                String lookaheadTestExpression = getLookaheadTestExpression(zeroOrMoreBlock.exitCache, i);
                println("###  nongreedy exit test");
                println("if (" + lookaheadTestExpression + "):");
                this.tabs = this.tabs + 1;
                println("break");
                this.tabs = this.tabs - 1;
            }
            int i5 = this.tabs;
            genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), "break");
            this.tabs = i5;
            this.tabs = i3;
            this.tabs--;
            this.currentASTResult = str;
        }
        i = i4;
        z = true;
        if (z) {
        }
        int i52 = this.tabs;
        genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), "break");
        this.tabs = i52;
        this.tabs = i3;
        this.tabs--;
        this.currentASTResult = str;
    }

    public void genASTDeclaration(AlternativeElement alternativeElement) {
        genASTDeclaration(alternativeElement, this.labeledElementASTType);
    }

    public void genASTDeclaration(AlternativeElement alternativeElement, String str) {
        genASTDeclaration(alternativeElement, alternativeElement.getLabel(), str);
    }

    public void genASTDeclaration(AlternativeElement alternativeElement, String str, String str2) {
        if (this.declaredASTVariables.contains(alternativeElement)) {
            return;
        }
        println(str + "_AST = None");
        this.declaredASTVariables.put(alternativeElement, alternativeElement);
    }

    public void genAlt(Alternative alternative, AlternativeBlock alternativeBlock) {
        boolean z = this.genAST;
        boolean z2 = false;
        this.genAST = z && alternative.getAutoGen();
        boolean z3 = this.saveText;
        if (z3 && alternative.getAutoGen()) {
            z2 = true;
        }
        this.saveText = z2;
        Hashtable hashtable = this.treeVariableMap;
        this.treeVariableMap = new Hashtable();
        if (alternative.exceptionSpec != null) {
            println("try:");
            this.tabs++;
        }
        println("pass");
        for (AlternativeElement alternativeElement = alternative.head; !(alternativeElement instanceof BlockEndElement); alternativeElement = alternativeElement.next) {
            alternativeElement.generate();
        }
        if (this.genAST) {
            if (alternativeBlock instanceof RuleBlock) {
                boolean z4 = this.grammar.hasSyntacticPredicate;
                println(((RuleBlock) alternativeBlock).getRuleName() + "_AST = currentAST.root");
                boolean z5 = this.grammar.hasSyntacticPredicate;
            } else if (alternativeBlock.getLabel() != null) {
                this.antlrTool.warning("Labeled subrules not yet supported", this.grammar.getFilename(), alternativeBlock.getLine(), alternativeBlock.getColumn());
            }
        }
        ExceptionSpec exceptionSpec = alternative.exceptionSpec;
        if (exceptionSpec != null) {
            this.tabs--;
            genErrorHandler(exceptionSpec);
        }
        this.genAST = z;
        this.saveText = z3;
        this.treeVariableMap = hashtable;
    }

    public void genBitsets(Vector vector, int i) {
        println("");
        for (int i2 = 0; i2 < vector.size(); i2++) {
            BitSet bitSet = (BitSet) vector.elementAt(i2);
            bitSet.growToInclude(i);
            genBitSet(bitSet, i2);
        }
    }

    public void genBlockInitAction(AlternativeBlock alternativeBlock) {
        String str = alternativeBlock.initAction;
        if (str != null) {
            printAction(processActionForSpecialSymbols(str, alternativeBlock.getLine(), this.currentRule, null));
        }
    }

    public void genBlockPreamble(AlternativeBlock alternativeBlock) {
        if (alternativeBlock instanceof RuleBlock) {
            RuleBlock ruleBlock = (RuleBlock) alternativeBlock;
            if (ruleBlock.labeledElements != null) {
                for (int i = 0; i < ruleBlock.labeledElements.size(); i++) {
                    AlternativeElement alternativeElement = (AlternativeElement) ruleBlock.labeledElements.elementAt(i);
                    boolean z = alternativeElement instanceof RuleRefElement;
                    if (z || !(!(alternativeElement instanceof AlternativeBlock) || (alternativeElement instanceof RuleBlock) || (alternativeElement instanceof SynPredBlock))) {
                        if (!z) {
                            AlternativeBlock alternativeBlock2 = (AlternativeBlock) alternativeElement;
                            if (alternativeBlock2.not && this.analyzer.subruleCanBeInverted(alternativeBlock2, this.grammar instanceof LexerGrammar)) {
                                println(alternativeElement.getLabel() + " = " + this.labeledElementInit);
                                if (!this.grammar.buildAST) {
                                }
                                genASTDeclaration(alternativeElement);
                            }
                        }
                        if (this.grammar.buildAST) {
                            genASTDeclaration(alternativeElement);
                        }
                        if (this.grammar instanceof LexerGrammar) {
                            println(alternativeElement.getLabel() + " = None");
                        }
                        if (this.grammar instanceof TreeWalkerGrammar) {
                            println(alternativeElement.getLabel() + " = " + this.labeledElementInit);
                        }
                    } else {
                        println(alternativeElement.getLabel() + " = " + this.labeledElementInit);
                        if (this.grammar.buildAST) {
                            if (alternativeElement instanceof GrammarAtom) {
                                GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
                                if (grammarAtom.getASTNodeType() != null) {
                                    genASTDeclaration(alternativeElement, grammarAtom.getASTNodeType());
                                }
                            }
                            genASTDeclaration(alternativeElement);
                        }
                    }
                }
            }
        }
    }

    public void genCases(BitSet bitSet) {
        String str;
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genCases(" + bitSet + ")");
        }
        int[] array = bitSet.toArray();
        boolean z = this.grammar instanceof LexerGrammar;
        print("elif la1 and la1 in ");
        if (this.grammar instanceof LexerGrammar) {
            _print("u'");
            for (int i : array) {
                _print(getValueString(i, false));
            }
            str = "':\n";
        } else {
            _print("[");
            int i2 = 0;
            while (i2 < array.length) {
                _print(getValueString(array[i2], false));
                i2++;
                if (i2 < array.length) {
                    _print(",");
                }
            }
            str = "]:\n";
        }
        _print(str);
    }

    /* JADX WARN: Code restructure failed: missing block: B:171:0x03cb, code lost:
    
        if (r9 > 0) goto L161;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PythonBlockFinishingInfo genCommonBlock(AlternativeBlock alternativeBlock, boolean z) {
        String str;
        boolean z2;
        boolean z3;
        boolean z4;
        String str2;
        boolean lookaheadIsEmpty;
        int i;
        boolean z5;
        boolean z6;
        PythonBlockFinishingInfo pythonBlockFinishingInfo;
        boolean z7;
        StringBuilder sb;
        String str3;
        StringBuilder sb2;
        String str4;
        PrintStream printStream;
        String str5;
        PythonBlockFinishingInfo pythonBlockFinishingInfo2 = new PythonBlockFinishingInfo();
        boolean z8 = this.genAST;
        this.genAST = z8 && alternativeBlock.getAutoGen();
        boolean z9 = this.saveText;
        this.saveText = z9 && alternativeBlock.getAutoGen();
        str = "";
        if (alternativeBlock.not && this.analyzer.subruleCanBeInverted(alternativeBlock, this.grammar instanceof LexerGrammar)) {
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("special case: ~(subrule)");
            }
            Lookahead look = this.analyzer.look(1, alternativeBlock);
            if (alternativeBlock.getLabel() != null && this.syntacticPredLevel == 0) {
                println(alternativeBlock.getLabel() + " = " + this.lt1Value);
            }
            genElementAST(alternativeBlock);
            StringBuilder m9b = C0000a.m9b("self.match(", this.grammar instanceof TreeWalkerGrammar ? "_t, " : "");
            m9b.append(getBitsetName(markBitsetForGen(look.fset)));
            m9b.append(")");
            println(m9b.toString());
            if (this.grammar instanceof TreeWalkerGrammar) {
                println("_t = _t.getNextSibling()");
            }
            return pythonBlockFinishingInfo2;
        }
        if (alternativeBlock.getAlternatives().size() == 1) {
            Alternative alternativeAt = alternativeBlock.getAlternativeAt(0);
            if (alternativeAt.synPred != null) {
                this.antlrTool.warning("Syntactic predicate superfluous for single alternative", this.grammar.getFilename(), alternativeBlock.getAlternativeAt(0).synPred.getLine(), alternativeBlock.getAlternativeAt(0).synPred.getColumn());
            }
            if (z) {
                String str6 = alternativeAt.semPred;
                if (str6 != null) {
                    genSemPred(str6, alternativeBlock.line);
                }
                genAlt(alternativeAt, alternativeBlock);
                return pythonBlockFinishingInfo2;
            }
        }
        int i2 = 0;
        for (int i3 = 0; i3 < alternativeBlock.getAlternatives().size(); i3++) {
            if (suitableForCaseExpression(alternativeBlock.getAlternativeAt(i3))) {
                i2++;
            }
        }
        if (i2 >= this.makeSwitchThreshold) {
            String lookaheadString = lookaheadString(1);
            if (this.grammar instanceof TreeWalkerGrammar) {
                println("if not _t:");
                this.tabs++;
                println("_t = antlr.ASTNULL");
                this.tabs--;
            }
            println("la1 = " + lookaheadString);
            println("if False:");
            this.tabs = this.tabs + 1;
            println("pass");
            this.tabs--;
            for (int i4 = 0; i4 < alternativeBlock.alternatives.size(); i4++) {
                Alternative alternativeAt2 = alternativeBlock.getAlternativeAt(i4);
                if (suitableForCaseExpression(alternativeAt2)) {
                    Lookahead lookahead = alternativeAt2.cache[1];
                    if (lookahead.fset.degree() != 0 || lookahead.containsEpsilon()) {
                        genCases(lookahead.fset);
                        this.tabs++;
                        genAlt(alternativeAt2, alternativeBlock);
                        this.tabs--;
                    } else {
                        this.antlrTool.warning("Alternate omitted due to empty prediction set", this.grammar.getFilename(), alternativeAt2.head.getLine(), alternativeAt2.head.getColumn());
                    }
                }
            }
            println("else:");
            this.tabs++;
            z2 = true;
        } else {
            z2 = false;
        }
        Grammar grammar = this.grammar;
        int i5 = grammar instanceof LexerGrammar ? grammar.maxk : 0;
        int i6 = 0;
        while (i5 >= 0) {
            int i7 = i6;
            int i8 = 0;
            while (i8 < alternativeBlock.alternatives.size()) {
                Alternative alternativeAt3 = alternativeBlock.getAlternativeAt(i8);
                if (this.DEBUG_CODE_GENERATOR) {
                    PrintStream printStream2 = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    str2 = str;
                    sb3.append("genAlt: ");
                    sb3.append(i8);
                    printStream2.println(sb3.toString());
                } else {
                    str2 = str;
                }
                if (z2 && suitableForCaseExpression(alternativeAt3)) {
                    if (this.DEBUG_CODE_GENERATOR) {
                        printStream = System.out;
                        str5 = "ignoring alt because it was in the switch";
                        printStream.println(str5);
                    }
                    pythonBlockFinishingInfo = pythonBlockFinishingInfo2;
                    z7 = z8;
                    z5 = z2;
                    z6 = z9;
                } else {
                    Grammar grammar2 = this.grammar;
                    if (grammar2 instanceof LexerGrammar) {
                        i = alternativeAt3.lookaheadDepth;
                        if (i == Integer.MAX_VALUE) {
                            i = grammar2.maxk;
                        }
                        while (i >= 1 && alternativeAt3.cache[i].containsEpsilon()) {
                            i--;
                        }
                        if (i != i5) {
                            if (this.DEBUG_CODE_GENERATOR) {
                                printStream = System.out;
                                str5 = "ignoring alt because effectiveDepth!=altDepth" + i + "!=" + i5;
                                printStream.println(str5);
                            }
                            pythonBlockFinishingInfo = pythonBlockFinishingInfo2;
                            z7 = z8;
                            z5 = z2;
                            z6 = z9;
                        } else {
                            lookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, i);
                        }
                    } else {
                        lookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, grammar2.maxk);
                        i = this.grammar.maxk;
                    }
                    String lookaheadTestExpression = getLookaheadTestExpression(alternativeAt3, i);
                    z5 = z2;
                    z6 = z9;
                    if (alternativeAt3.cache[1].fset.degree() > 127 && suitableForCaseExpression(alternativeAt3)) {
                        if (i7 == 0) {
                            sb2 = new StringBuilder();
                            str4 = "<m1> if ";
                        } else {
                            sb2 = new StringBuilder();
                            str4 = "<m2> elif ";
                        }
                        sb2.append(str4);
                        sb2.append(lookaheadTestExpression);
                        sb2.append(":");
                        println(sb2.toString());
                    } else if (lookaheadIsEmpty && alternativeAt3.semPred == null && alternativeAt3.synPred == null) {
                        if (i7 == 0) {
                            println("##<m3> <closing");
                        } else {
                            println("else: ## <m4>");
                            this.tabs++;
                        }
                        pythonBlockFinishingInfo2.needAnErrorClause = false;
                    } else {
                        if (alternativeAt3.semPred != null) {
                            pythonBlockFinishingInfo = pythonBlockFinishingInfo2;
                            String processActionForSpecialSymbols = processActionForSpecialSymbols(alternativeAt3.semPred, alternativeBlock.line, this.currentRule, new ActionTransInfo());
                            Grammar grammar3 = this.grammar;
                            z7 = z8;
                            if (((grammar3 instanceof ParserGrammar) || (grammar3 instanceof LexerGrammar)) && this.grammar.debuggingOutput) {
                                StringBuilder m10b = C0000a.m10b("(", lookaheadTestExpression, " and fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.PREDICTING, ");
                                m10b.append(addSemPred(this.charFormatter.escapeString(processActionForSpecialSymbols)));
                                m10b.append(", ");
                                lookaheadTestExpression = C0000a.m3a(m10b, processActionForSpecialSymbols, "))");
                            } else {
                                lookaheadTestExpression = "(" + lookaheadTestExpression + " and (" + processActionForSpecialSymbols + "))";
                            }
                        } else {
                            pythonBlockFinishingInfo = pythonBlockFinishingInfo2;
                            z7 = z8;
                        }
                        SynPredBlock synPredBlock = alternativeAt3.synPred;
                        if (i7 <= 0) {
                            if (synPredBlock == null) {
                                if (this.grammar instanceof TreeWalkerGrammar) {
                                    println("if not _t:");
                                    this.tabs++;
                                    println("_t = antlr.ASTNULL");
                                    this.tabs--;
                                }
                                sb = new StringBuilder();
                                str3 = "if ";
                                sb.append(str3);
                                sb.append(lookaheadTestExpression);
                                sb.append(":");
                                println(sb.toString());
                            }
                            genSynPred(synPredBlock, lookaheadTestExpression);
                        } else if (synPredBlock != null) {
                            println("else:");
                            this.tabs++;
                            synPredBlock = alternativeAt3.synPred;
                            genSynPred(synPredBlock, lookaheadTestExpression);
                        } else {
                            sb = new StringBuilder();
                            str3 = "elif ";
                            sb.append(str3);
                            sb.append(lookaheadTestExpression);
                            sb.append(":");
                            println(sb.toString());
                        }
                        i7++;
                        this.tabs++;
                        genAlt(alternativeAt3, alternativeBlock);
                        this.tabs--;
                    }
                    pythonBlockFinishingInfo = pythonBlockFinishingInfo2;
                    z7 = z8;
                    i7++;
                    this.tabs++;
                    genAlt(alternativeAt3, alternativeBlock);
                    this.tabs--;
                }
                i8++;
                z2 = z5;
                str = str2;
                z9 = z6;
                pythonBlockFinishingInfo2 = pythonBlockFinishingInfo;
                z8 = z7;
            }
            i5--;
            i6 = i7;
        }
        PythonBlockFinishingInfo pythonBlockFinishingInfo3 = pythonBlockFinishingInfo2;
        boolean z10 = z2;
        String str7 = str;
        this.genAST = z8;
        this.saveText = z9;
        if (z10) {
            pythonBlockFinishingInfo3.postscript = str7;
            z3 = true;
            pythonBlockFinishingInfo3.generatedSwitch = true;
            if (i6 <= 0) {
                z4 = false;
            }
            z4 = z3;
        } else {
            z3 = true;
            pythonBlockFinishingInfo3.postscript = str7;
            z4 = false;
            pythonBlockFinishingInfo3.generatedSwitch = false;
        }
        pythonBlockFinishingInfo3.generatedAnIf = z4;
        return pythonBlockFinishingInfo3;
    }

    public void genHeader() {
        StringBuilder m5a = C0000a.m5a("### $ANTLR ");
        m5a.append(Tool.version);
        m5a.append(": \"");
        Tool tool = this.antlrTool;
        m5a.append(tool.fileMinusPath(tool.grammarFile));
        m5a.append("\" -> \"");
        m5a.append(this.grammar.getClassName());
        m5a.append(".py\"$");
        println(m5a.toString());
    }

    public void genHeaderInit(Grammar grammar) {
        String headerAction = this.behavior.getHeaderAction(grammar.getClassName() + "." + initHeaderAction);
        if (isEmpty(headerAction)) {
            headerAction = this.behavior.getHeaderAction(initHeaderAction);
        }
        if (isEmpty(headerAction)) {
            return;
        }
        int i = this.tabs;
        println("### __init__ header action >>> ");
        printActionCode(headerAction, 0);
        this.tabs = i;
        println("### __init__ header action <<< ");
    }

    public void genHeaderMain(Grammar grammar) {
        int i;
        String headerAction = this.behavior.getHeaderAction(grammar.getClassName() + "." + mainHeaderAction);
        if (isEmpty(headerAction)) {
            headerAction = this.behavior.getHeaderAction(mainHeaderAction);
        }
        if (!isEmpty(headerAction)) {
            i = this.tabs;
            this.tabs = 0;
            println("");
            println("### __main__ header action >>> ");
            printMainFunc(headerAction);
        } else {
            if (!(grammar instanceof LexerGrammar)) {
                return;
            }
            i = this.tabs;
            this.tabs = 0;
            println("### __main__ header action >>> ");
            genLexerTest();
        }
        this.tabs = 0;
        println("### __main__ header action <<< ");
        this.tabs = i;
    }

    public void genJavadocComment(Grammar grammar) {
        String str = grammar.comment;
        if (str != null) {
            _printJavadoc(str);
        }
    }

    public void genJavadocComment(RuleSymbol ruleSymbol) {
        String str = ruleSymbol.comment;
        if (str != null) {
            _printJavadoc(str);
        }
    }

    public void genLexerTest() {
        String className = this.grammar.getClassName();
        println("if __name__ == '__main__' :");
        this.tabs++;
        println("import sys");
        println("import antlr");
        println("import " + className);
        println("");
        println("### create lexer - shall read from stdin");
        println("try:");
        this.tabs++;
        println("for token in " + className + ".Lexer():");
        this.tabs = this.tabs + 1;
        println("print token");
        println("");
        this.tabs = this.tabs + (-1);
        this.tabs--;
        println("except antlr.TokenStreamException, e:");
        this.tabs++;
        println("print \"error: exception caught while lexing: \", e");
        this.tabs--;
        this.tabs--;
    }

    public void genMatch(GrammarAtom grammarAtom) {
        if (grammarAtom instanceof StringLiteralElement) {
            if (!(this.grammar instanceof LexerGrammar)) {
                genMatchUsingAtomTokenType(grammarAtom);
                return;
            }
        } else if (grammarAtom instanceof CharLiteralElement) {
            if (!(this.grammar instanceof LexerGrammar)) {
                this.antlrTool.error("cannot ref character literals in grammar: " + grammarAtom);
                return;
            }
        } else if (!(grammarAtom instanceof TokenRefElement)) {
            if (grammarAtom instanceof WildcardElement) {
                gen((WildcardElement) grammarAtom);
                return;
            }
            return;
        }
        genMatchUsingAtomText(grammarAtom);
    }

    public void genMatch(BitSet bitSet) {
    }

    public void genMatchUsingAtomText(GrammarAtom grammarAtom) {
        String str = this.grammar instanceof TreeWalkerGrammar ? "_t," : "";
        if ((this.grammar instanceof LexerGrammar) && (!this.saveText || grammarAtom.getAutoGenType() == 3)) {
            println("_saveIndex = self.text.length()");
        }
        print(grammarAtom.not ? "self.matchNot(" : "self.match(");
        _print(str);
        _print(grammarAtom.atomText.equals("EOF") ? "EOF_TYPE" : grammarAtom.atomText);
        _println(")");
        if (this.grammar instanceof LexerGrammar) {
            if (!this.saveText || grammarAtom.getAutoGenType() == 3) {
                println("self.text.setLength(_saveIndex)");
            }
        }
    }

    public void genMatchUsingAtomTokenType(GrammarAtom grammarAtom) {
        StringBuilder m5a = C0000a.m5a(this.grammar instanceof TreeWalkerGrammar ? "_t," : "");
        m5a.append(getValueString(grammarAtom.getType(), true));
        String sb = m5a.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(grammarAtom.not ? "self.matchNot(" : "self.match(");
        sb2.append(sb);
        sb2.append(")");
        println(sb2.toString());
    }

    public void genNextToken() {
        boolean z;
        String str;
        Tool tool;
        StringBuilder sb;
        int i = 0;
        while (true) {
            if (i >= this.grammar.rules.size()) {
                z = false;
                break;
            }
            RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.rules.elementAt(i);
            if (ruleSymbol.isDefined() && ruleSymbol.access.equals("public")) {
                z = true;
                break;
            }
            i++;
        }
        if (z) {
            Grammar grammar = this.grammar;
            RuleBlock createNextTokenRule = MakeGrammar.createNextTokenRule(grammar, grammar.rules, "nextToken");
            RuleSymbol ruleSymbol2 = new RuleSymbol("mnextToken");
            ruleSymbol2.setDefined();
            ruleSymbol2.setBlock(createNextTokenRule);
            ruleSymbol2.access = "private";
            this.grammar.define(ruleSymbol2);
            this.grammar.theLLkAnalyzer.deterministic(createNextTokenRule);
            LexerGrammar lexerGrammar = (LexerGrammar) this.grammar;
            String str2 = lexerGrammar.filterMode ? lexerGrammar.filterRule : null;
            println("");
            println("def nextToken(self):");
            this.tabs++;
            println("while True:");
            this.tabs++;
            println("try: ### try again ..");
            this.tabs++;
            println("while True:");
            this.tabs++;
            println("_token = None");
            println("_ttype = INVALID_TYPE");
            if (((LexerGrammar) this.grammar).filterMode) {
                println("self.setCommitToPath(False)");
                if (str2 != null) {
                    String str3 = " does not exist in this lexer";
                    if (this.grammar.isDefined(CodeGenerator.encodeLexerRuleName(str2))) {
                        RuleSymbol ruleSymbol3 = (RuleSymbol) this.grammar.getSymbol(CodeGenerator.encodeLexerRuleName(str2));
                        if (ruleSymbol3.isDefined()) {
                            if (ruleSymbol3.access.equals("public")) {
                                tool = this.grammar.antlrTool;
                                sb = new StringBuilder();
                                sb.append("Filter rule ");
                                sb.append(str2);
                                str3 = " must be protected";
                                sb.append(str3);
                                tool.error(sb.toString());
                            }
                            println("_m = self.mark()");
                        } else {
                            tool = this.grammar.antlrTool;
                            sb = new StringBuilder();
                        }
                    } else {
                        tool = this.grammar.antlrTool;
                        sb = new StringBuilder();
                    }
                    sb.append("Filter rule ");
                    sb.append(str2);
                    sb.append(str3);
                    tool.error(sb.toString());
                    println("_m = self.mark()");
                }
            }
            println("self.resetText()");
            println("try: ## for char stream error handling");
            this.tabs++;
            println("try: ##for lexical error handling");
            this.tabs++;
            int i2 = this.tabs;
            for (int i3 = 0; i3 < createNextTokenRule.getAlternatives().size(); i3++) {
                Alternative alternativeAt = createNextTokenRule.getAlternativeAt(i3);
                if (alternativeAt.cache[1].containsEpsilon()) {
                    String decodeLexerRuleName = CodeGenerator.decodeLexerRuleName(((RuleRefElement) alternativeAt.head).targetRule);
                    this.antlrTool.warning("public lexical rule " + decodeLexerRuleName + " is optional (can match \"nothing\")");
                }
            }
            System.getProperty("line.separator");
            PythonBlockFinishingInfo genCommonBlock = genCommonBlock(createNextTokenRule, false);
            if (!((LexerGrammar) this.grammar).filterMode) {
                str = "self.default(self.LA(1))";
            } else if (str2 == null) {
                str = C0000a.m1a("", "self.filterdefault(self.LA(1))");
            } else {
                str = "self.filterdefault(self.LA(1), self.m" + str2 + ", False)";
            }
            genBlockFinish1(genCommonBlock, str);
            this.tabs = i2;
            if (((LexerGrammar) this.grammar).filterMode && str2 != null) {
                println("self.commit()");
            }
            println("if not self._returnToken:");
            this.tabs++;
            println("raise antlr.TryAgain ### found SKIP token");
            this.tabs--;
            if (((LexerGrammar) this.grammar).getTestLiterals()) {
                println("### option { testLiterals=true } ");
                println("self.testForLiteral(self._returnToken)");
            }
            println("### return token to caller");
            println("return self._returnToken");
            this.tabs--;
            println("### handle lexical errors ....");
            println("except antlr.RecognitionException, e:");
            this.tabs++;
            if (((LexerGrammar) this.grammar).filterMode) {
                if (str2 == null) {
                    println("if not self.getCommitToPath():");
                    this.tabs++;
                    println("self.consume()");
                } else {
                    println("if not self.getCommitToPath(): ");
                    this.tabs++;
                    println("self.rewind(_m)");
                    println("self.resetText()");
                    println("try:");
                    this.tabs++;
                    println("self.m" + str2 + "(False)");
                    this.tabs = this.tabs - 1;
                    println("except antlr.RecognitionException, ee:");
                    this.tabs = this.tabs + 1;
                    println("### horrendous failure: error in filter rule");
                    println("self.reportError(ee)");
                    println("self.consume()");
                    this.tabs--;
                }
                println("raise antlr.TryAgain()");
                this.tabs--;
            }
            if (createNextTokenRule.getDefaultErrorHandler()) {
                println("self.reportError(e)");
                println("self.consume()");
            } else {
                println("raise antlr.TokenStreamRecognitionException(e)");
            }
            this.tabs--;
            this.tabs--;
            println("### handle char stream errors ...");
            println("except antlr.CharStreamException,cse:");
            this.tabs++;
            println("if isinstance(cse, antlr.CharStreamIOException):");
            this.tabs++;
            println("raise antlr.TokenStreamIOException(cse.io)");
            this.tabs--;
            println("else:");
            this.tabs++;
            println("raise antlr.TokenStreamException(str(cse))");
            this.tabs--;
            this.tabs--;
            this.tabs--;
            this.tabs--;
            println("except antlr.TryAgain:");
            this.tabs++;
            println("pass");
            this.tabs--;
        } else {
            println("");
            println("def nextToken(self): ");
            this.tabs++;
            println("try:");
            this.tabs++;
            println("self.uponEOF()");
            this.tabs--;
            println("except antlr.CharStreamIOException, csioe:");
            this.tabs++;
            println("raise antlr.TokenStreamIOException(csioe.io)");
            this.tabs--;
            println("except antlr.CharStreamException, cse:");
            this.tabs++;
            println("raise antlr.TokenStreamException(str(cse))");
            this.tabs--;
            println("return antlr.CommonToken(type=EOF_TYPE, text=\"\")");
        }
        this.tabs--;
    }

    public void genRule(RuleSymbol ruleSymbol, boolean z, int i) {
        int i2;
        int i3;
        StringBuilder m5a;
        StringBuilder sb;
        StringBuilder sb2;
        String sb3;
        StringBuilder m5a2;
        String str;
        this.tabs = 1;
        if (!ruleSymbol.isDefined()) {
            Tool tool = this.antlrTool;
            StringBuilder m5a3 = C0000a.m5a("undefined rule: ");
            m5a3.append(ruleSymbol.getId());
            tool.error(m5a3.toString());
            return;
        }
        RuleBlock block = ruleSymbol.getBlock();
        this.currentRule = block;
        this.currentASTResult = ruleSymbol.getId();
        this.declaredASTVariables.clear();
        boolean z2 = this.genAST;
        this.genAST = z2 && block.getAutoGen();
        this.saveText = block.getAutoGen();
        genJavadocComment(ruleSymbol);
        print("def " + ruleSymbol.getId() + "(");
        _print(this.commonExtraParams);
        if (this.commonExtraParams.length() != 0 && block.argAction != null) {
            _print(",");
        }
        if (block.argAction != null) {
            _println("");
            this.tabs++;
            println(block.argAction);
            this.tabs--;
            print("):");
        } else {
            _print("):");
        }
        println("");
        this.tabs++;
        String str2 = block.returnAction;
        if (str2 != null) {
            if (str2.indexOf(61) >= 0) {
                str = block.returnAction;
            } else {
                str = extractIdOfAction(block.returnAction, block.getLine(), block.getColumn()) + " = None";
            }
            println(str);
        }
        println(this.commonLocalVars);
        Grammar grammar = this.grammar;
        if (grammar.traceRules) {
            if (grammar instanceof TreeWalkerGrammar) {
                m5a2 = C0000a.m5a("self.traceIn(\"");
                m5a2.append(ruleSymbol.getId());
                m5a2.append("\",_t)");
            } else {
                m5a2 = C0000a.m5a("self.traceIn(\"");
                m5a2.append(ruleSymbol.getId());
                m5a2.append("\")");
            }
            println(m5a2.toString());
        }
        if (this.grammar instanceof LexerGrammar) {
            if (ruleSymbol.getId().equals("mEOF")) {
                sb3 = "_ttype = EOF_TYPE";
            } else {
                StringBuilder m5a4 = C0000a.m5a("_ttype = ");
                m5a4.append(ruleSymbol.getId().substring(1));
                sb3 = m5a4.toString();
            }
            println(sb3);
            println("_saveIndex = 0");
        }
        Grammar grammar2 = this.grammar;
        if (grammar2.debuggingOutput) {
            if (grammar2 instanceof ParserGrammar) {
                sb2 = new StringBuilder();
                sb2.append("self.fireEnterRule(");
                sb2.append(i);
                sb2.append(", 0)");
            } else if (grammar2 instanceof LexerGrammar) {
                sb2 = new StringBuilder();
                sb2.append("self.fireEnterRule(");
                sb2.append(i);
                sb2.append(", _ttype)");
            }
            println(sb2.toString());
        }
        Grammar grammar3 = this.grammar;
        if (grammar3.debuggingOutput || grammar3.traceRules) {
            println("try: ### debugging");
            this.tabs++;
        }
        if (this.grammar instanceof TreeWalkerGrammar) {
            println(ruleSymbol.getId() + "_AST_in = None");
            println("if _t != antlr.ASTNULL:");
            this.tabs = this.tabs + 1;
            println(ruleSymbol.getId() + "_AST_in = _t");
            this.tabs = this.tabs - 1;
        }
        if (this.grammar.buildAST) {
            println("self.returnAST = None");
            println("currentAST = antlr.ASTPair()");
            println(ruleSymbol.getId() + "_AST = None");
        }
        genBlockPreamble(block);
        genBlockInitAction(block);
        ExceptionSpec findExceptionSpec = block.findExceptionSpec("");
        if (findExceptionSpec != null || block.getDefaultErrorHandler()) {
            println("try:      ## for error handling");
            this.tabs++;
        }
        int i4 = this.tabs;
        if (block.alternatives.size() == 1) {
            Alternative alternativeAt = block.getAlternativeAt(0);
            String str3 = alternativeAt.semPred;
            if (str3 != null) {
                genSemPred(str3, this.currentRule.line);
            }
            if (alternativeAt.synPred != null) {
                this.antlrTool.warning("Syntactic predicate ignored for single alternative", this.grammar.getFilename(), alternativeAt.synPred.getLine(), alternativeAt.synPred.getColumn());
            }
            genAlt(alternativeAt, block);
        } else {
            this.grammar.theLLkAnalyzer.deterministic(block);
            genBlockFinish(genCommonBlock(block, false), this.throwNoViable);
        }
        this.tabs = i4;
        if (findExceptionSpec != null || block.getDefaultErrorHandler()) {
            this.tabs--;
            println("");
        }
        if (findExceptionSpec != null) {
            genErrorHandler(findExceptionSpec);
        } else if (block.getDefaultErrorHandler()) {
            StringBuilder m5a5 = C0000a.m5a("except ");
            m5a5.append(this.exceptionThrown);
            m5a5.append(", ex:");
            println(m5a5.toString());
            this.tabs++;
            if (this.grammar.hasSyntacticPredicate) {
                println("if not self.inputState.guessing:");
                this.tabs++;
            }
            println("self.reportError(ex)");
            Grammar grammar4 = this.grammar;
            if (grammar4 instanceof TreeWalkerGrammar) {
                println("if _t:");
                i2 = 1;
                this.tabs++;
                println("_t = _t.getNextSibling()");
                this.tabs--;
            } else {
                String bitsetName = getBitsetName(markBitsetForGen(grammar4.theLLkAnalyzer.FOLLOW(1, block.endNode).fset));
                println("self.consume()");
                println("self.consumeUntil(" + bitsetName + ")");
                i2 = 1;
            }
            if (this.grammar.hasSyntacticPredicate) {
                this.tabs -= i2;
                println("else:");
                this.tabs += i2;
                println("raise ex");
                this.tabs -= i2;
            }
            this.tabs -= i2;
            println("");
        }
        if (this.grammar.buildAST) {
            StringBuilder m5a6 = C0000a.m5a("self.returnAST = ");
            m5a6.append(ruleSymbol.getId());
            m5a6.append("_AST");
            println(m5a6.toString());
        }
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("self._retTree = _t");
        }
        if (block.getTestLiterals()) {
            if (ruleSymbol.access.equals("protected")) {
                genLiteralsTestForPartialToken();
            } else {
                genLiteralsTest();
            }
        }
        if (this.grammar instanceof LexerGrammar) {
            println("self.set_return_token(_createToken, _token, _ttype, _begin)");
        }
        if (block.returnAction != null) {
            StringBuilder m5a7 = C0000a.m5a("return ");
            m5a7.append(extractIdOfAction(block.returnAction, block.getLine(), block.getColumn()));
            m5a7.append("");
            println(m5a7.toString());
        }
        Grammar grammar5 = this.grammar;
        if (grammar5.debuggingOutput || grammar5.traceRules) {
            this.tabs--;
            println("finally:  ### debugging");
            this.tabs++;
            Grammar grammar6 = this.grammar;
            if (grammar6.debuggingOutput) {
                if (grammar6 instanceof ParserGrammar) {
                    sb = new StringBuilder();
                    sb.append("self.fireExitRule(");
                    sb.append(i);
                    sb.append(", 0)");
                } else if (grammar6 instanceof LexerGrammar) {
                    sb = new StringBuilder();
                    sb.append("self.fireExitRule(");
                    sb.append(i);
                    sb.append(", _ttype)");
                }
                println(sb.toString());
            }
            Grammar grammar7 = this.grammar;
            if (grammar7.traceRules) {
                if (grammar7 instanceof TreeWalkerGrammar) {
                    m5a = C0000a.m5a("self.traceOut(\"");
                    m5a.append(ruleSymbol.getId());
                    m5a.append("\", _t)");
                } else {
                    m5a = C0000a.m5a("self.traceOut(\"");
                    m5a.append(ruleSymbol.getId());
                    m5a.append("\")");
                }
                println(m5a.toString());
            }
            i3 = 1;
            this.tabs--;
        } else {
            i3 = 1;
        }
        this.tabs -= i3;
        println("");
        this.genAST = z2;
    }

    public void genSemPred(String str, int i) {
        String processActionForSpecialSymbols = processActionForSpecialSymbols(str, i, this.currentRule, new ActionTransInfo());
        String escapeString = this.charFormatter.escapeString(processActionForSpecialSymbols);
        Grammar grammar = this.grammar;
        if (grammar.debuggingOutput && ((grammar instanceof ParserGrammar) || (grammar instanceof LexerGrammar))) {
            StringBuilder m5a = C0000a.m5a("fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.VALIDATING,");
            m5a.append(addSemPred(escapeString));
            m5a.append(", ");
            m5a.append(processActionForSpecialSymbols);
            m5a.append(")");
            processActionForSpecialSymbols = m5a.toString();
        }
        println("if not " + processActionForSpecialSymbols + ":");
        this.tabs = this.tabs + 1;
        println("raise antlr.SemanticException(\"" + escapeString + "\")");
        this.tabs = this.tabs + (-1);
    }

    public void genSemPredMap() {
        Enumeration elements = this.semPreds.elements();
        println("_semPredNames = [");
        this.tabs++;
        while (elements.hasMoreElements()) {
            StringBuilder m5a = C0000a.m5a("\"");
            m5a.append(elements.nextElement());
            m5a.append("\",");
            println(m5a.toString());
        }
        this.tabs--;
        println("]");
    }

    public void genSynPred(SynPredBlock synPredBlock, String str) {
        StringBuilder m5a;
        String str2;
        StringBuilder m5a2;
        String str3 = ")";
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("gen=>(" + synPredBlock + ")");
        }
        StringBuilder m5a3 = C0000a.m5a("synPredMatched");
        m5a3.append(synPredBlock.f302ID);
        m5a3.append(" = False");
        println(m5a3.toString());
        println("if " + str + ":");
        this.tabs = this.tabs + 1;
        if (this.grammar instanceof TreeWalkerGrammar) {
            m5a = C0000a.m5a("_t");
            m5a.append(synPredBlock.f302ID);
            str2 = " = _t";
        } else {
            m5a = C0000a.m5a("_m");
            m5a.append(synPredBlock.f302ID);
            str2 = " = self.mark()";
        }
        m5a.append(str2);
        println(m5a.toString());
        StringBuilder m5a4 = C0000a.m5a("synPredMatched");
        m5a4.append(synPredBlock.f302ID);
        m5a4.append(" = True");
        println(m5a4.toString());
        println("self.inputState.guessing += 1");
        Grammar grammar = this.grammar;
        if (grammar.debuggingOutput && ((grammar instanceof ParserGrammar) || (grammar instanceof LexerGrammar))) {
            println("self.fireSyntacticPredicateStarted()");
        }
        this.syntacticPredLevel++;
        println("try:");
        this.tabs++;
        gen(synPredBlock);
        this.tabs--;
        StringBuilder m5a5 = C0000a.m5a("except ");
        m5a5.append(this.exceptionThrown);
        m5a5.append(", pe:");
        println(m5a5.toString());
        this.tabs++;
        StringBuilder m5a6 = C0000a.m5a("synPredMatched");
        m5a6.append(synPredBlock.f302ID);
        m5a6.append(" = False");
        println(m5a6.toString());
        this.tabs--;
        if (this.grammar instanceof TreeWalkerGrammar) {
            m5a2 = C0000a.m5a("_t = _t");
            m5a2.append(synPredBlock.f302ID);
            str3 = "";
        } else {
            m5a2 = C0000a.m5a("self.rewind(_m");
            m5a2.append(synPredBlock.f302ID);
        }
        m5a2.append(str3);
        println(m5a2.toString());
        println("self.inputState.guessing -= 1");
        Grammar grammar2 = this.grammar;
        if (grammar2.debuggingOutput && ((grammar2 instanceof ParserGrammar) || (grammar2 instanceof LexerGrammar))) {
            StringBuilder m5a7 = C0000a.m5a("if synPredMatched");
            m5a7.append(synPredBlock.f302ID);
            m5a7.append(":");
            println(m5a7.toString());
            this.tabs++;
            println("self.fireSyntacticPredicateSucceeded()");
            this.tabs--;
            println("else:");
            this.tabs++;
            println("self.fireSyntacticPredicateFailed()");
            this.tabs--;
        }
        this.syntacticPredLevel--;
        this.tabs--;
        StringBuilder m5a8 = C0000a.m5a("if synPredMatched");
        m5a8.append(synPredBlock.f302ID);
        m5a8.append(":");
        println(m5a8.toString());
    }

    public void genTokenASTNodeMap() {
        TokenSymbol tokenSymbol;
        println("");
        println("def buildTokenTypeASTClassMap(self):");
        this.tabs++;
        Vector vocabulary = this.grammar.tokenManager.getVocabulary();
        int i = 0;
        boolean z = false;
        for (int i2 = 0; i2 < vocabulary.size(); i2++) {
            String str = (String) vocabulary.elementAt(i2);
            if (str != null && (tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str)) != null && tokenSymbol.getASTNodeType() != null) {
                i++;
                if (!z) {
                    println("self.tokenTypeToASTClassMap = {}");
                    z = true;
                }
                StringBuilder m5a = C0000a.m5a("self.tokenTypeToASTClassMap[");
                m5a.append(tokenSymbol.getTokenType());
                m5a.append("] = ");
                m5a.append(tokenSymbol.getASTNodeType());
                println(m5a.toString());
            }
        }
        if (i == 0) {
            println("self.tokenTypeToASTClassMap = None");
        }
        this.tabs--;
    }

    public void genTokenStrings() {
        TokenSymbol tokenSymbol;
        int i = this.tabs;
        this.tabs = 0;
        println("");
        println("_tokenNames = [");
        this.tabs++;
        Vector vocabulary = this.grammar.tokenManager.getVocabulary();
        for (int i2 = 0; i2 < vocabulary.size(); i2++) {
            String str = (String) vocabulary.elementAt(i2);
            if (str == null) {
                StringBuilder m5a = C0000a.m5a("<");
                m5a.append(String.valueOf(i2));
                m5a.append(">");
                str = m5a.toString();
            }
            if (!str.startsWith("\"") && !str.startsWith("<") && (tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str)) != null && tokenSymbol.getParaphrase() != null) {
                str = StringUtils.stripFrontBack(tokenSymbol.getParaphrase(), "\"", "\"");
            }
            print(this.charFormatter.literalString(str));
            if (i2 != vocabulary.size() - 1) {
                _print(", ");
            }
            _println("");
        }
        this.tabs--;
        println("]");
        this.tabs = i;
    }

    public void genTokenTypes(TokenManager tokenManager) {
        StringBuilder sb;
        String sb2;
        this.tabs = 0;
        Vector vocabulary = tokenManager.getVocabulary();
        println("SKIP                = antlr.SKIP");
        println("INVALID_TYPE        = antlr.INVALID_TYPE");
        println("EOF_TYPE            = antlr.EOF_TYPE");
        println("EOF                 = antlr.EOF");
        println("NULL_TREE_LOOKAHEAD = antlr.NULL_TREE_LOOKAHEAD");
        println("MIN_USER_TYPE       = antlr.MIN_USER_TYPE");
        for (int i = 4; i < vocabulary.size(); i++) {
            String str = (String) vocabulary.elementAt(i);
            if (str != null) {
                if (str.startsWith("\"")) {
                    StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenManager.getTokenSymbol(str);
                    if (stringLiteralSymbol == null) {
                        this.antlrTool.panic("String literal " + str + " not in symbol table");
                    }
                    if (stringLiteralSymbol.label != null) {
                        sb2 = stringLiteralSymbol.label + " = " + i;
                    } else {
                        String mangleLiteral = mangleLiteral(str);
                        if (mangleLiteral != null) {
                            println(mangleLiteral + " = " + i);
                            stringLiteralSymbol.label = mangleLiteral;
                        } else {
                            sb = new StringBuilder();
                            sb.append("### ");
                            sb.append(str);
                            sb.append(" = ");
                            sb.append(i);
                            sb2 = sb.toString();
                        }
                    }
                } else if (!str.startsWith("<")) {
                    sb = new StringBuilder();
                    sb.append(str);
                    sb.append(" = ");
                    sb.append(i);
                    sb2 = sb.toString();
                }
                println(sb2);
            }
        }
        this.tabs--;
        exitIfError();
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(GrammarAtom grammarAtom, String str) {
        if (grammarAtom == null || grammarAtom.getASTNodeType() == null) {
            return getASTCreateString(str);
        }
        StringBuilder m10b = C0000a.m10b("self.astFactory.create(", str, ", ");
        m10b.append(grammarAtom.getASTNodeType());
        m10b.append(")");
        return m10b.toString();
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(Vector vector) {
        if (vector.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("antlr.make(");
        int i = 0;
        while (i < vector.size()) {
            stringBuffer.append(vector.elementAt(i));
            i++;
            if (i < vector.size()) {
                stringBuffer.append(", ");
            }
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public String getASTCreateString(String str) {
        if (str == null) {
            str = "";
        }
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == ',') {
                i++;
            }
        }
        if (i >= 2) {
            return C0000a.m2a("self.astFactory.create(", str, ")");
        }
        int indexOf = str.indexOf(44);
        str.lastIndexOf(44);
        TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(i > 0 ? str.substring(0, indexOf) : str);
        if (tokenSymbol != null) {
            String aSTNodeType = tokenSymbol.getASTNodeType();
            String str2 = i == 0 ? ", \"\"" : "";
            if (aSTNodeType != null) {
                return "self.astFactory.create(" + str + str2 + ", " + aSTNodeType + ")";
            }
        }
        return this.labeledElementASTType.equals("AST") ? C0000a.m2a("self.astFactory.create(", str, ")") : C0000a.m2a("self.astFactory.create(", str, ")");
    }

    public String getLookaheadTestExpression(Alternative alternative, int i) {
        int i2 = alternative.lookaheadDepth;
        if (i2 == Integer.MAX_VALUE) {
            i2 = this.grammar.maxk;
        }
        return i == 0 ? "True" : getLookaheadTestExpression(alternative.cache, i2);
    }

    public String getLookaheadTestExpression(Lookahead[] lookaheadArr, int i) {
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append("(");
        boolean z = true;
        for (int i2 = 1; i2 <= i; i2++) {
            BitSet bitSet = lookaheadArr[i2].fset;
            if (!z) {
                stringBuffer.append(") and (");
            }
            z = false;
            stringBuffer.append(lookaheadArr[i2].containsEpsilon() ? "True" : getLookaheadTestTerm(i2, bitSet));
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public String getLookaheadTestTerm(int i, BitSet bitSet) {
        String lookaheadString = lookaheadString(i);
        int[] array = bitSet.toArray();
        if (CodeGenerator.elementsAreRange(array)) {
            return getRangeExpression(i, array);
        }
        int degree = bitSet.degree();
        if (degree == 0) {
            return "True";
        }
        if (degree >= this.bitsetTestThreshold) {
            return getBitsetName(markBitsetForGen(bitSet)) + ".member(" + lookaheadString + ")";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < array.length; i2++) {
            String valueString = getValueString(array[i2], true);
            if (i2 > 0) {
                stringBuffer.append(" or ");
            }
            stringBuffer.append(lookaheadString);
            stringBuffer.append("==");
            stringBuffer.append(valueString);
        }
        stringBuffer.toString();
        return stringBuffer.toString();
    }

    public String getRangeExpression(int i, int[] iArr) {
        if (!CodeGenerator.elementsAreRange(iArr)) {
            this.antlrTool.panic("getRangeExpression called with non-range");
        }
        int i2 = iArr[0];
        int i3 = iArr[iArr.length - 1];
        StringBuilder m5a = C0000a.m5a("(");
        m5a.append(lookaheadString(i));
        m5a.append(" >= ");
        m5a.append(getValueString(i2, true));
        m5a.append(" and ");
        m5a.append(lookaheadString(i));
        m5a.append(" <= ");
        return C0000a.m3a(m5a, getValueString(i3, true), ")");
    }

    public boolean isspace(char c2) {
        return c2 == '\t' || c2 == '\n' || c2 == '\r' || c2 == ' ';
    }

    public boolean lookaheadIsEmpty(Alternative alternative, int i) {
        int i2 = alternative.lookaheadDepth;
        if (i2 == Integer.MAX_VALUE) {
            i2 = this.grammar.maxk;
        }
        for (int i3 = 1; i3 <= i2 && i3 <= i; i3++) {
            if (alternative.cache[i3].fset.degree() != 0) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x009a  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String mapTreeId(String str, ActionTransInfo actionTransInfo) {
        String str2;
        if (this.currentRule == null) {
            return str;
        }
        Grammar grammar = this.grammar;
        boolean z = true;
        if (grammar instanceof TreeWalkerGrammar) {
            if (grammar.buildAST) {
                if (str.length() > 3 && str.lastIndexOf("_in") == str.length() - 3) {
                    str = str.substring(0, str.length() - 3);
                }
            }
            for (int i = 0; i < this.currentRule.labeledElements.size(); i++) {
                if (((AlternativeElement) this.currentRule.labeledElements.elementAt(i)).getLabel().equals(str)) {
                    return z ? str : C0000a.m1a(str, "_AST");
                }
            }
            str2 = (String) this.treeVariableMap.get(str);
            if (str2 != null) {
                if (!str.equals(this.currentRule.getRuleName())) {
                    return str;
                }
                String m1a = z ? C0000a.m1a(str, "_AST_in") : C0000a.m1a(str, "_AST");
                if (actionTransInfo != null && !z) {
                    actionTransInfo.refRuleRoot = m1a;
                }
                return m1a;
            }
            if (str2 != NONUNIQUE && !str2.equals(this.currentRule.getRuleName())) {
                return z ? C0000a.m1a(str2, "_in") : str2;
            }
            Tool tool = this.antlrTool;
            StringBuilder m10b = C0000a.m10b("Ambiguous reference to AST element ", str, " in rule ");
            m10b.append(this.currentRule.getRuleName());
            tool.error(m10b.toString());
            return null;
        }
        z = false;
        while (i < this.currentRule.labeledElements.size()) {
        }
        str2 = (String) this.treeVariableMap.get(str);
        if (str2 != null) {
        }
    }

    /* renamed from: od */
    public void m110od(String str, int i, int i2, String str2) {
        PrintStream printStream;
        String str3;
        System.out.println(str2);
        while (i <= i2) {
            char charAt = str.charAt(i);
            if (charAt == '\t') {
                printStream = System.out;
                str3 = " ht ";
            } else if (charAt == '\n') {
                printStream = System.out;
                str3 = " nl ";
            } else if (charAt != ' ') {
                System.out.print(" " + charAt + " ");
                i++;
            } else {
                printStream = System.out;
                str3 = " sp ";
            }
            printStream.print(str3);
            i++;
        }
        System.out.println("");
    }

    @Override // antlr.CodeGenerator
    public void printAction(String str) {
        if (str != null) {
            printTabs();
            _printAction(str);
        }
    }

    public void printActionCode(String str, int i) {
        printAction(processActionCode(str, i));
    }

    public void printGrammarAction(Grammar grammar) {
        println("### user action >>>");
        printAction(processActionForSpecialSymbols(grammar.classMemberAction.getText(), grammar.classMemberAction.getLine(), this.currentRule, null));
        println("### user action <<<");
    }

    public void printMainFunc(String str) {
        int i = this.tabs;
        this.tabs = 0;
        println("if __name__ == '__main__':");
        this.tabs++;
        printActionCode(str, 0);
        this.tabs--;
        this.tabs = i;
    }

    @Override // antlr.CodeGenerator
    public void printTabs() {
        for (int i = 0; i < this.tabs; i++) {
            this.currentOutput.print("    ");
        }
    }

    public String processActionCode(String str, int i) {
        Tool tool;
        StringBuilder sb;
        if (str == null || isEmpty(str)) {
            return "";
        }
        CodeLexer codeLexer = new CodeLexer(str, this.grammar.getFilename(), i, this.antlrTool);
        try {
            codeLexer.mACTION(true);
            return codeLexer.getTokenObject().getText();
        } catch (CharStreamException unused) {
            tool = this.antlrTool;
            sb = new StringBuilder();
            sb.append("Error reading action:");
            sb.append(str);
            tool.panic(sb.toString());
            return str;
        } catch (RecognitionException e) {
            codeLexer.reportError(e);
            return str;
        } catch (TokenStreamException unused2) {
            tool = this.antlrTool;
            sb = new StringBuilder();
            sb.append("Error reading action:");
            sb.append(str);
            tool.panic(sb.toString());
            return str;
        }
    }

    @Override // antlr.CodeGenerator
    public String processActionForSpecialSymbols(String str, int i, RuleBlock ruleBlock, ActionTransInfo actionTransInfo) {
        Tool tool;
        StringBuilder sb;
        if (str == null || str.length() == 0) {
            return null;
        }
        if (isEmpty(str)) {
            return "";
        }
        if (this.grammar == null) {
            return str;
        }
        ActionLexer actionLexer = new ActionLexer(str, ruleBlock, this, actionTransInfo);
        actionLexer.setLineOffset(i);
        actionLexer.setFilename(this.grammar.getFilename());
        actionLexer.setTool(this.antlrTool);
        try {
            actionLexer.mACTION(true);
            return actionLexer.getTokenObject().getText();
        } catch (CharStreamException unused) {
            tool = this.antlrTool;
            sb = new StringBuilder();
            sb.append("Error reading action:");
            sb.append(str);
            tool.panic(sb.toString());
            return str;
        } catch (RecognitionException e) {
            actionLexer.reportError(e);
            return str;
        } catch (TokenStreamException unused2) {
            tool = this.antlrTool;
            sb = new StringBuilder();
            sb.append("Error reading action:");
            sb.append(str);
            tool.panic(sb.toString());
            return str;
        }
    }

    public void setupOutput(String str) {
        this.currentOutput = this.antlrTool.openOutputFile(str + ".py");
    }

    public String toString(boolean z) {
        return z ? "True" : "False";
    }
}
