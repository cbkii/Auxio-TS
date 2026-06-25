package antlr;

import antlr.actions.java.ActionLexer;
import antlr.collections.impl.BitSet;
import antlr.collections.impl.Vector;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class JavaCodeGenerator extends CodeGenerator {
    public static final int CONTINUE_LAST_MAPPING = -888;
    public static final String NONUNIQUE = new String();
    public static final int NO_MAPPING = -999;
    public static final int caseSizeThreshold = 127;
    public String commonExtraArgs;
    public String commonExtraParams;
    public String commonLocalVars;
    public String currentASTResult;
    public RuleBlock currentRule;
    public String exceptionThrown;
    public String labeledElementASTType;
    public String labeledElementInit;
    public String labeledElementType;
    public String lt1Value;
    public JavaCodeGeneratorPrintWriterManager printWriterManager;
    public Vector semPreds;
    public String throwNoViable;
    public int defaultLine = NO_MAPPING;
    public int syntacticPredLevel = 0;
    public boolean genAST = false;
    public boolean saveText = false;
    public Hashtable treeVariableMap = new Hashtable();
    public Hashtable declaredASTVariables = new Hashtable();
    public int astVarNumber = 1;

    public JavaCodeGenerator() {
        this.charFormatter = new JavaCharFormatter();
    }

    private void GenRuleInvocation(RuleRefElement ruleRefElement) {
        Tool tool;
        String str;
        String filename;
        int line;
        int i = this.defaultLine;
        try {
            this.defaultLine = ruleRefElement.getLine();
            getPrintWriterManager().startSingleSourceLineMapping(ruleRefElement.getLine());
            _print(ruleRefElement.targetRule + "(");
            getPrintWriterManager().endMapping();
            if (this.grammar instanceof LexerGrammar) {
                _print(ruleRefElement.getLabel() != null ? "true" : "false");
                if (this.commonExtraArgs.length() != 0 || ruleRefElement.args != null) {
                    _print(",");
                }
            }
            _print(this.commonExtraArgs);
            if (this.commonExtraArgs.length() != 0 && ruleRefElement.args != null) {
                _print(",");
            }
            RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(ruleRefElement.targetRule);
            if (ruleRefElement.args != null) {
                ActionTransInfo actionTransInfo = new ActionTransInfo();
                String strProcessActionForSpecialSymbols = processActionForSpecialSymbols(ruleRefElement.args, 0, this.currentRule, actionTransInfo);
                if (actionTransInfo.assignToRoot || actionTransInfo.refRuleRoot != null) {
                    this.antlrTool.error("Arguments of rule reference '" + ruleRefElement.targetRule + "' cannot set or ref #" + this.currentRule.getRuleName(), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
                }
                _print(strProcessActionForSpecialSymbols);
                if (ruleSymbol.block.argAction == null) {
                    tool = this.antlrTool;
                    str = "Rule '" + ruleRefElement.targetRule + "' accepts no arguments";
                    filename = this.grammar.getFilename();
                    line = ruleRefElement.getLine();
                    tool.warning(str, filename, line, ruleRefElement.getColumn());
                }
            } else if (ruleSymbol.block.argAction != null) {
                tool = this.antlrTool;
                str = "Missing parameters on reference to rule " + ruleRefElement.targetRule;
                filename = this.grammar.getFilename();
                line = ruleRefElement.getLine();
                tool.warning(str, filename, line, ruleRefElement.getColumn());
            }
            _println(");");
            if (this.grammar instanceof TreeWalkerGrammar) {
                println("_t = _retTree;");
            }
        } finally {
            this.defaultLine = i;
        }
    }

    private void genBitSet(BitSet bitSet, int i) {
        StringBuilder sb;
        int i2 = this.defaultLine;
        try {
            this.defaultLine = NO_MAPPING;
            println("private static final long[] mk" + getBitsetName(i) + "() {");
            int iLengthInLongWords = bitSet.lengthInLongWords();
            if (iLengthInLongWords < 8) {
                println("\tlong[] data = { " + bitSet.toStringOfWords() + "};");
            } else {
                println("\tlong[] data = new long[" + iLengthInLongWords + "];");
                long[] packedArray = bitSet.toPackedArray();
                int i3 = 0;
                while (i3 < packedArray.length) {
                    if (packedArray[i3] == 0) {
                        i3++;
                    } else {
                        int i4 = i3 + 1;
                        if (i4 == packedArray.length || packedArray[i3] != packedArray[i4]) {
                            sb = new StringBuilder();
                            sb.append("\tdata[");
                            sb.append(i3);
                            sb.append("]=");
                            sb.append(packedArray[i3]);
                            sb.append("L;");
                        } else {
                            while (i4 < packedArray.length && packedArray[i4] == packedArray[i3]) {
                                i4++;
                            }
                            sb = new StringBuilder();
                            sb.append("\tfor (int i = ");
                            sb.append(i3);
                            sb.append("; i<=");
                            sb.append(i4 - 1);
                            sb.append("; i++) { data[i]=");
                            sb.append(packedArray[i3]);
                            sb.append("L; }");
                        }
                        println(sb.toString());
                        i3 = i4;
                    }
                }
            }
            println("\treturn data;");
            println("}");
            println("public static final BitSet " + getBitsetName(i) + " = new BitSet(mk" + getBitsetName(i) + "());");
        } finally {
            this.defaultLine = i2;
        }
    }

    private void genBlockFinish(JavaBlockFinishingInfo javaBlockFinishingInfo, String str, int i) {
        int i2 = this.defaultLine;
        try {
            this.defaultLine = i;
            if (javaBlockFinishingInfo.needAnErrorClause && (javaBlockFinishingInfo.generatedAnIf || javaBlockFinishingInfo.generatedSwitch)) {
                println(javaBlockFinishingInfo.generatedAnIf ? "else {" : "{");
                this.tabs++;
                println(str);
                this.tabs--;
                println("}");
            }
            String str2 = javaBlockFinishingInfo.postscript;
            if (str2 != null) {
                println(str2);
            }
        } finally {
            this.defaultLine = i2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00d1 A[Catch: all -> 0x01ea, TryCatch #0 {all -> 0x01ea, blocks: (B:3:0x0002, B:6:0x0019, B:8:0x001f, B:10:0x0025, B:13:0x0066, B:15:0x006c, B:17:0x0070, B:19:0x0075, B:21:0x007b, B:25:0x0084, B:27:0x008a, B:30:0x008f, B:32:0x0099, B:35:0x00bf, B:37:0x00c3, B:39:0x00cc, B:41:0x00d3, B:40:0x00d1, B:42:0x00d6, B:44:0x00ee, B:45:0x010a, B:48:0x0112, B:50:0x0116, B:51:0x012e, B:53:0x0149, B:52:0x0133, B:54:0x014c, B:57:0x0154, B:59:0x015a, B:60:0x0172, B:62:0x018d, B:64:0x0196, B:61:0x0177, B:65:0x01ae, B:67:0x01b2, B:73:0x01be, B:74:0x01ce, B:76:0x01e4, B:75:0x01d3, B:33:0x00a5), top: B:82:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void genElementAST(AlternativeElement alternativeElement) {
        String label;
        String label2;
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        int i = this.defaultLine;
        try {
            this.defaultLine = alternativeElement.getLine();
            if ((this.grammar instanceof TreeWalkerGrammar) && !this.grammar.buildAST) {
                if (alternativeElement.getLabel() == null) {
                    String str = this.lt1Value;
                    String str2 = "tmp" + this.astVarNumber + "_AST";
                    this.astVarNumber++;
                    mapTreeVariable(alternativeElement, str2);
                    println(this.labeledElementASTType + " " + str2 + "_in = " + str + ";");
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
                    label = alternativeElement.getLabel();
                    label2 = alternativeElement.getLabel();
                } else {
                    label = this.lt1Value;
                    label2 = "tmp" + this.astVarNumber;
                    this.astVarNumber++;
                }
                if (z) {
                    if (alternativeElement instanceof GrammarAtom) {
                        GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
                        String aSTNodeType = grammarAtom.getASTNodeType() != null ? grammarAtom.getASTNodeType() : this.labeledElementASTType;
                        genASTDeclaration(alternativeElement, label2, aSTNodeType);
                    }
                }
                String str3 = label2 + "_AST";
                mapTreeVariable(alternativeElement, str3);
                if (this.grammar instanceof TreeWalkerGrammar) {
                    println(this.labeledElementASTType + " " + str3 + "_in = null;");
                }
                if (alternativeElement.getLabel() != null) {
                    if (alternativeElement instanceof GrammarAtom) {
                        sb3 = new StringBuilder();
                        sb3.append(str3);
                        sb3.append(" = ");
                        sb3.append(getASTCreateString((GrammarAtom) alternativeElement, label));
                        sb3.append(";");
                    } else {
                        sb3 = new StringBuilder();
                        sb3.append(str3);
                        sb3.append(" = ");
                        sb3.append(getASTCreateString(label));
                        sb3.append(";");
                    }
                    println(sb3.toString());
                }
                if (alternativeElement.getLabel() == null && z) {
                    String str4 = this.lt1Value;
                    if (alternativeElement instanceof GrammarAtom) {
                        sb2 = new StringBuilder();
                        sb2.append(str3);
                        sb2.append(" = ");
                        sb2.append(getASTCreateString((GrammarAtom) alternativeElement, str4));
                        sb2.append(";");
                    } else {
                        sb2 = new StringBuilder();
                        sb2.append(str3);
                        sb2.append(" = ");
                        sb2.append(getASTCreateString(str4));
                        sb2.append(";");
                    }
                    println(sb2.toString());
                    if (this.grammar instanceof TreeWalkerGrammar) {
                        println(str3 + "_in = " + str4 + ";");
                    }
                }
                if (this.genAST) {
                    int autoGenType = alternativeElement.getAutoGenType();
                    if (autoGenType == 1) {
                        sb = new StringBuilder();
                        sb.append("astFactory.addASTChild(currentAST, ");
                        sb.append(str3);
                        sb.append(");");
                    } else if (autoGenType == 2) {
                        sb = new StringBuilder();
                        sb.append("astFactory.makeASTRoot(currentAST, ");
                        sb.append(str3);
                        sb.append(");");
                    }
                    println(sb.toString());
                }
            }
        } finally {
            this.defaultLine = i;
        }
    }

    private void genErrorCatchForElement(AlternativeElement alternativeElement) {
        if (alternativeElement.getLabel() == null) {
            return;
        }
        String strEncodeLexerRuleName = alternativeElement.enclosingRuleName;
        if (this.grammar instanceof LexerGrammar) {
            strEncodeLexerRuleName = CodeGenerator.encodeLexerRuleName(strEncodeLexerRuleName);
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(strEncodeLexerRuleName);
        if (ruleSymbol == null) {
            this.antlrTool.panic("Enclosing rule not found!");
        }
        ExceptionSpec exceptionSpecFindExceptionSpec = ruleSymbol.block.findExceptionSpec(alternativeElement.getLabel());
        if (exceptionSpecFindExceptionSpec != null) {
            this.tabs--;
            println("}", alternativeElement.getLine());
            genErrorHandler(exceptionSpecFindExceptionSpec);
        }
    }

    private void genErrorHandler(ExceptionSpec exceptionSpec) {
        for (int i = 0; i < exceptionSpec.handlers.size(); i++) {
            ExceptionHandler exceptionHandler = (ExceptionHandler) exceptionSpec.handlers.elementAt(i);
            int i2 = this.defaultLine;
            try {
                this.defaultLine = exceptionHandler.action.getLine();
                println("catch (" + exceptionHandler.exceptionTypeAndName.getText() + ") {", exceptionHandler.exceptionTypeAndName.getLine());
                this.tabs = this.tabs + 1;
                if (this.grammar.hasSyntacticPredicate) {
                    println("if (inputState.guessing==0) {");
                    this.tabs++;
                }
                printAction(processActionForSpecialSymbols(exceptionHandler.action.getText(), exceptionHandler.action.getLine(), this.currentRule, new ActionTransInfo()));
                if (this.grammar.hasSyntacticPredicate) {
                    this.tabs--;
                    println("} else {");
                    this.tabs++;
                    println("throw " + extractIdOfAction(exceptionHandler.exceptionTypeAndName) + ";");
                    this.tabs = this.tabs + (-1);
                    println("}");
                }
                this.tabs--;
                println("}");
                this.defaultLine = i2;
            } catch (Throwable th) {
                this.defaultLine = i2;
                throw th;
            }
        }
    }

    private void genErrorTryForElement(AlternativeElement alternativeElement) {
        if (alternativeElement.getLabel() == null) {
            return;
        }
        String strEncodeLexerRuleName = alternativeElement.enclosingRuleName;
        if (this.grammar instanceof LexerGrammar) {
            strEncodeLexerRuleName = CodeGenerator.encodeLexerRuleName(strEncodeLexerRuleName);
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(strEncodeLexerRuleName);
        if (ruleSymbol == null) {
            this.antlrTool.panic("Enclosing rule not found!");
        }
        if (ruleSymbol.block.findExceptionSpec(alternativeElement.getLabel()) != null) {
            println("try { // for error handling", alternativeElement.getLine());
            this.tabs++;
        }
    }

    private void genLiteralsTest() {
        println("_ttype = testLiteralsTable(_ttype);");
    }

    private void genLiteralsTestForPartialToken() {
        println("_ttype = testLiteralsTable(new String(text.getBuffer(),_begin,text.length()-_begin),_ttype);");
    }

    private String getValueString(int i) {
        Grammar grammar = this.grammar;
        if (grammar instanceof LexerGrammar) {
            return this.charFormatter.literalChar(i);
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
        String strMangleLiteral = mangleLiteral(id);
        return strMangleLiteral == null ? String.valueOf(i) : strMangleLiteral;
    }

    private String lookaheadString(int i) {
        if (this.grammar instanceof TreeWalkerGrammar) {
            return "_t.getType()";
        }
        return "LA(" + i + ")";
    }

    private String mangleLiteral(String str) {
        String string = this.antlrTool.literalsPrefix;
        for (int i = 1; i < str.length() - 1; i++) {
            if (!Character.isLetter(str.charAt(i)) && str.charAt(i) != '_') {
                return null;
            }
            StringBuilder sbM5a = C0000a.m5a(string);
            sbM5a.append(str.charAt(i));
            string = sbM5a.toString();
        }
        return this.antlrTool.upperCaseMangledLiterals ? string.toUpperCase() : string;
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
        String str;
        Token option;
        String strStripFrontBack;
        Token option2;
        String strStripFrontBack2;
        if (grammar instanceof ParserGrammar) {
            this.labeledElementASTType = "AST";
            if (grammar.hasOption("ASTLabelType") && (option2 = grammar.getOption("ASTLabelType")) != null && (strStripFrontBack2 = StringUtils.stripFrontBack(option2.getText(), "\"", "\"")) != null) {
                this.labeledElementASTType = strStripFrontBack2;
            }
            this.labeledElementType = "Token ";
            this.labeledElementInit = "null";
            this.commonExtraArgs = "";
            this.commonExtraParams = "";
            this.commonLocalVars = "";
            this.lt1Value = "LT(1)";
            this.exceptionThrown = "RecognitionException";
            str = "throw new NoViableAltException(LT(1), getFilename());";
        } else if (grammar instanceof LexerGrammar) {
            this.labeledElementType = "char ";
            this.labeledElementInit = "'\\0'";
            this.commonExtraArgs = "";
            this.commonExtraParams = "boolean _createToken";
            this.commonLocalVars = "int _ttype; Token _token=null; int _begin=text.length();";
            this.lt1Value = "LA(1)";
            this.exceptionThrown = "RecognitionException";
            str = "throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());";
        } else {
            if (!(grammar instanceof TreeWalkerGrammar)) {
                this.antlrTool.panic("Unknown grammar type");
                return;
            }
            this.labeledElementASTType = "AST";
            this.labeledElementType = "AST";
            if (grammar.hasOption("ASTLabelType") && (option = grammar.getOption("ASTLabelType")) != null && (strStripFrontBack = StringUtils.stripFrontBack(option.getText(), "\"", "\"")) != null) {
                this.labeledElementASTType = strStripFrontBack;
                this.labeledElementType = strStripFrontBack;
            }
            if (!grammar.hasOption("ASTLabelType")) {
                grammar.setOption("ASTLabelType", new Token(6, "AST"));
            }
            this.labeledElementInit = "null";
            this.commonExtraArgs = "_t";
            this.commonExtraParams = "AST _t";
            this.commonLocalVars = "";
            this.lt1Value = C0000a.m3a(C0000a.m5a("("), this.labeledElementASTType, ")_t");
            this.exceptionThrown = "RecognitionException";
            str = "throw new NoViableAltException(_t);";
        }
        this.throwNoViable = str;
    }

    public static boolean suitableForCaseExpression(Alternative alternative) {
        return alternative.lookaheadDepth == 1 && alternative.semPred == null && !alternative.cache[1].containsEpsilon() && alternative.cache[1].fset.degree() <= 127;
    }

    @Override // antlr.CodeGenerator
    public void _print(String str) {
        _print(str, this.defaultLine);
    }

    public void _print(String str, int i) {
        if (i > 0 || i == -888) {
            getPrintWriterManager().startMapping(i);
        }
        super._print(str);
        if (i > 0 || i == -888) {
            getPrintWriterManager().endMapping();
        }
    }

    @Override // antlr.CodeGenerator
    public void _println(String str) {
        _println(str, this.defaultLine);
    }

    public void _println(String str, int i) {
        if (i > 0 || i == -888) {
            getPrintWriterManager().startMapping(i);
        }
        super._println(str);
        if (i > 0 || i == -888) {
            getPrintWriterManager().endMapping();
        }
    }

    public int addSemPred(String str) {
        this.semPreds.appendElement(str);
        return this.semPreds.size() - 1;
    }

    public void exitIfError() {
        if (this.antlrTool.hasError()) {
            this.antlrTool.fatalError("Exiting due to errors.");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen() {
        try {
            Enumeration enumerationElements = this.behavior.grammars.elements();
            while (enumerationElements.hasMoreElements()) {
                Grammar grammar = (Grammar) enumerationElements.nextElement();
                grammar.setGrammarAnalyzer(this.analyzer);
                grammar.setCodeGenerator(this);
                this.analyzer.setGrammar(grammar);
                setupGrammarParameters(grammar);
                grammar.generate();
                exitIfError();
            }
            Enumeration enumerationElements2 = this.behavior.tokenManagers.elements();
            while (enumerationElements2.hasMoreElements()) {
                TokenManager tokenManager = (TokenManager) enumerationElements2.nextElement();
                if (!tokenManager.isReadOnly()) {
                    genTokenTypes(tokenManager);
                    genTokenInterchange(tokenManager);
                }
                exitIfError();
            }
        } catch (IOException e) {
            this.antlrTool.reportException(e, null);
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(ActionElement actionElement) {
        int i = this.defaultLine;
        try {
            this.defaultLine = actionElement.getLine();
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("genAction(" + actionElement + ")");
            }
            if (actionElement.isSemPred) {
                genSemPred(actionElement.actionText, actionElement.line);
            } else {
                if (this.grammar.hasSyntacticPredicate) {
                    println("if ( inputState.guessing==0 ) {");
                    this.tabs++;
                }
                ActionTransInfo actionTransInfo = new ActionTransInfo();
                String strProcessActionForSpecialSymbols = processActionForSpecialSymbols(actionElement.actionText, actionElement.getLine(), this.currentRule, actionTransInfo);
                if (actionTransInfo.refRuleRoot != null) {
                    println(actionTransInfo.refRuleRoot + " = (" + this.labeledElementASTType + ")currentAST.root;");
                }
                printAction(strProcessActionForSpecialSymbols);
                if (actionTransInfo.assignToRoot) {
                    println("currentAST.root = " + actionTransInfo.refRuleRoot + ";");
                    println("currentAST.child = " + actionTransInfo.refRuleRoot + "!=null &&" + actionTransInfo.refRuleRoot + ".getFirstChild()!=null ?", NO_MAPPING);
                    this.tabs = this.tabs + 1;
                    println(actionTransInfo.refRuleRoot + ".getFirstChild() : " + actionTransInfo.refRuleRoot + ";");
                    this.tabs = this.tabs + (-1);
                    println("currentAST.advanceChildToEnd();");
                }
                if (this.grammar.hasSyntacticPredicate) {
                    this.tabs--;
                    println("}", NO_MAPPING);
                }
            }
        } finally {
            this.defaultLine = i;
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(AlternativeBlock alternativeBlock) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("gen(" + alternativeBlock + ")");
        }
        println("{", NO_MAPPING);
        genBlockPreamble(alternativeBlock);
        genBlockInitAction(alternativeBlock);
        String str = this.currentASTResult;
        if (alternativeBlock.getLabel() != null) {
            this.currentASTResult = alternativeBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(alternativeBlock);
        genBlockFinish(genCommonBlock(alternativeBlock, true), this.throwNoViable, alternativeBlock.getLine());
        println("}", NO_MAPPING);
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
            StringBuilder sb = new StringBuilder();
            sb.append(charLiteralElement.getLabel());
            sb.append(" = ");
            println(C0000a.m3a(sb, this.lt1Value, ";"), charLiteralElement.getLine());
        }
        boolean z = this.saveText;
        this.saveText = z && charLiteralElement.getAutoGenType() == 1;
        genMatch(charLiteralElement);
        this.saveText = z;
    }

    @Override // antlr.CodeGenerator
    public void gen(CharRangeElement charRangeElement) {
        int i = this.defaultLine;
        try {
            this.defaultLine = charRangeElement.getLine();
            if (charRangeElement.getLabel() != null && this.syntacticPredLevel == 0) {
                println(charRangeElement.getLabel() + " = " + this.lt1Value + ";");
            }
            boolean z = (this.grammar instanceof LexerGrammar) && (!this.saveText || charRangeElement.getAutoGenType() == 3);
            if (z) {
                println("_saveIndex=text.length();");
            }
            println("matchRange(" + charRangeElement.beginText + "," + charRangeElement.endText + ");");
            if (z) {
                println("text.setLength(_saveIndex);");
            }
        } finally {
            this.defaultLine = i;
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(LexerGrammar lexerGrammar) {
        String str;
        String strStripFrontBack;
        String strStripFrontBack2;
        int i = this.defaultLine;
        try {
            this.defaultLine = NO_MAPPING;
            if (lexerGrammar.debuggingOutput) {
                this.semPreds = new Vector();
            }
            setGrammar(lexerGrammar);
            if (!(this.grammar instanceof LexerGrammar)) {
                this.antlrTool.panic("Internal error generating lexer");
            }
            this.currentOutput = getPrintWriterManager().setupOutput(this.antlrTool, this.grammar);
            this.genAST = false;
            this.saveText = true;
            this.tabs = 0;
            genHeader();
            try {
                this.defaultLine = this.behavior.getHeaderActionLine("");
                println(this.behavior.getHeaderAction(""));
                this.defaultLine = NO_MAPPING;
                println("import java.io.InputStream;");
                println("import antlr.TokenStreamException;");
                println("import antlr.TokenStreamIOException;");
                println("import antlr.TokenStreamRecognitionException;");
                println("import antlr.CharStreamException;");
                println("import antlr.CharStreamIOException;");
                println("import antlr.ANTLRException;");
                println("import java.io.Reader;");
                println("import java.util.Hashtable;");
                println("import antlr." + this.grammar.getSuperClass() + ";");
                println("import antlr.InputBuffer;");
                println("import antlr.ByteBuffer;");
                println("import antlr.CharBuffer;");
                println("import antlr.Token;");
                println("import antlr.CommonToken;");
                println("import antlr.RecognitionException;");
                println("import antlr.NoViableAltForCharException;");
                println("import antlr.MismatchedCharException;");
                println("import antlr.TokenStream;");
                println("import antlr.ANTLRHashString;");
                println("import antlr.LexerSharedInputState;");
                println("import antlr.collections.impl.BitSet;");
                println("import antlr.SemanticException;");
                println(this.grammar.preambleAction.getText());
                if (this.grammar.superClass != null) {
                    str = this.grammar.superClass;
                } else {
                    str = "antlr." + this.grammar.getSuperClass();
                }
                if (this.grammar.comment != null) {
                    _println(this.grammar.comment);
                }
                Token token = (Token) this.grammar.options.get("classHeaderPrefix");
                if (token == null || (strStripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) == null) {
                    strStripFrontBack = "public";
                }
                print(strStripFrontBack + " ");
                print("class " + this.grammar.getClassName() + " extends " + str);
                println(" implements " + this.grammar.tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix + ", TokenStream");
                Token token2 = (Token) this.grammar.options.get("classHeaderSuffix");
                if (token2 != null && (strStripFrontBack2 = StringUtils.stripFrontBack(token2.getText(), "\"", "\"")) != null) {
                    print(", " + strStripFrontBack2);
                }
                println(" {");
                print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null), this.grammar.classMemberAction.getLine());
                println("public " + this.grammar.getClassName() + "(InputStream in) {");
                this.tabs = this.tabs + 1;
                println("this(new ByteBuffer(in));");
                this.tabs = this.tabs - 1;
                println("}");
                println("public " + this.grammar.getClassName() + "(Reader in) {");
                this.tabs = this.tabs + 1;
                println("this(new CharBuffer(in));");
                this.tabs = this.tabs - 1;
                println("}");
                println("public " + this.grammar.getClassName() + "(InputBuffer ib) {");
                this.tabs = this.tabs + 1;
                println(this.grammar.debuggingOutput ? "this(new LexerSharedInputState(new antlr.debug.DebuggingInputBuffer(ib)));" : "this(new LexerSharedInputState(ib));");
                this.tabs--;
                println("}");
                println("public " + this.grammar.getClassName() + "(LexerSharedInputState state) {");
                this.tabs = this.tabs + 1;
                println("super(state);");
                if (this.grammar.debuggingOutput) {
                    println("  ruleNames  = _ruleNames;");
                    println("  semPredNames = _semPredNames;");
                    println("  setupDebugging();");
                }
                println("caseSensitiveLiterals = " + lexerGrammar.caseSensitiveLiterals + ";");
                println("setCaseSensitive(" + lexerGrammar.caseSensitive + ");");
                println("literals = new Hashtable();");
                Enumeration tokenSymbolKeys = this.grammar.tokenManager.getTokenSymbolKeys();
                while (tokenSymbolKeys.hasMoreElements()) {
                    String str2 = (String) tokenSymbolKeys.nextElement();
                    if (str2.charAt(0) == '\"') {
                        TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str2);
                        if (tokenSymbol instanceof StringLiteralSymbol) {
                            StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenSymbol;
                            println("literals.put(new ANTLRHashString(" + stringLiteralSymbol.getId() + ", this), new Integer(" + stringLiteralSymbol.getTokenType() + "));");
                        }
                    }
                }
                this.tabs--;
                println("}");
                if (this.grammar.debuggingOutput) {
                    println("private static final String _ruleNames[] = {");
                    Enumeration enumerationElements = this.grammar.rules.elements();
                    while (enumerationElements.hasMoreElements()) {
                        GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
                        if (grammarSymbol instanceof RuleSymbol) {
                            println("  \"" + ((RuleSymbol) grammarSymbol).getId() + "\",");
                        }
                    }
                    println("};");
                }
                genNextToken();
                Enumeration enumerationElements2 = this.grammar.rules.elements();
                int i2 = 0;
                while (enumerationElements2.hasMoreElements()) {
                    RuleSymbol ruleSymbol = (RuleSymbol) enumerationElements2.nextElement();
                    if (!ruleSymbol.getId().equals("mnextToken")) {
                        genRule(ruleSymbol, false, i2);
                        i2++;
                    }
                    exitIfError();
                }
                if (this.grammar.debuggingOutput) {
                    genSemPredMap();
                }
                genBitsets(this.bitsetsUsed, ((LexerGrammar) this.grammar).charVocabulary.size());
                println("");
                println("}");
                getPrintWriterManager().finishOutput();
            } catch (Throwable th) {
                this.defaultLine = NO_MAPPING;
                throw th;
            }
        } finally {
            this.defaultLine = i;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00fe A[Catch: all -> 0x01a3, TRY_ENTER, TryCatch #0 {all -> 0x01a3, blocks: (B:3:0x0004, B:5:0x000e, B:6:0x0029, B:8:0x0039, B:10:0x005a, B:12:0x007d, B:14:0x0095, B:16:0x00bf, B:17:0x00c5, B:19:0x00d5, B:21:0x00dd, B:23:0x00e7, B:33:0x00fe, B:35:0x0102, B:36:0x011a, B:37:0x014e, B:24:0x00ea, B:26:0x00ee, B:13:0x0082, B:9:0x004b), top: B:43:0x0004 }] */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(OneOrMoreBlock oneOrMoreBlock) {
        StringBuilder sb;
        String label;
        int i;
        boolean z;
        int i2;
        int i3 = this.defaultLine;
        try {
            this.defaultLine = oneOrMoreBlock.getLine();
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("gen+(" + oneOrMoreBlock + ")");
            }
            println("{", NO_MAPPING);
            genBlockPreamble(oneOrMoreBlock);
            if (oneOrMoreBlock.getLabel() != null) {
                sb = new StringBuilder();
                sb.append("_cnt_");
                sb.append(oneOrMoreBlock.getLabel());
            } else {
                sb = new StringBuilder();
                sb.append("_cnt");
                sb.append(oneOrMoreBlock.f302ID);
            }
            String string = sb.toString();
            println("int " + string + "=0;");
            if (oneOrMoreBlock.getLabel() != null) {
                label = oneOrMoreBlock.getLabel();
            } else {
                label = "_loop" + oneOrMoreBlock.f302ID;
            }
            println(label + ":");
            println("do {");
            this.tabs = this.tabs + 1;
            genBlockInitAction(oneOrMoreBlock);
            String str = this.currentASTResult;
            if (oneOrMoreBlock.getLabel() != null) {
                this.currentASTResult = oneOrMoreBlock.getLabel();
            }
            this.grammar.theLLkAnalyzer.deterministic(oneOrMoreBlock);
            int i4 = this.grammar.maxk;
            if (oneOrMoreBlock.greedy || (i2 = oneOrMoreBlock.exitLookaheadDepth) > this.grammar.maxk || !oneOrMoreBlock.exitCache[i2].containsEpsilon()) {
                if (oneOrMoreBlock.greedy || oneOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
                    i = i4;
                    z = false;
                }
                if (z) {
                    if (this.DEBUG_CODE_GENERATOR) {
                        System.out.println("nongreedy (...)+ loop; exit depth is " + oneOrMoreBlock.exitLookaheadDepth);
                    }
                    String lookaheadTestExpression = getLookaheadTestExpression(oneOrMoreBlock.exitCache, i);
                    println("// nongreedy exit test", NO_MAPPING);
                    println("if ( " + string + ">=1 && " + lookaheadTestExpression + ") break " + label + ";", CONTINUE_LAST_MAPPING);
                }
                genBlockFinish(genCommonBlock(oneOrMoreBlock, false), "if ( " + string + ">=1 ) { break " + label + "; } else {" + this.throwNoViable + "}", oneOrMoreBlock.getLine());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append("++;");
                println(sb2.toString());
                this.tabs = this.tabs - 1;
                println("} while (true);");
                println("}");
                this.currentASTResult = str;
            }
            i4 = oneOrMoreBlock.exitLookaheadDepth;
            i = i4;
            z = true;
            if (z) {
            }
            genBlockFinish(genCommonBlock(oneOrMoreBlock, false), "if ( " + string + ">=1 ) { break " + label + "; } else {" + this.throwNoViable + "}", oneOrMoreBlock.getLine());
            StringBuilder sb22 = new StringBuilder();
            sb22.append(string);
            sb22.append("++;");
            println(sb22.toString());
            this.tabs = this.tabs - 1;
            println("} while (true);");
            println("}");
            this.currentASTResult = str;
        } finally {
            this.defaultLine = i3;
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(ParserGrammar parserGrammar) {
        String str;
        String strStripFrontBack;
        String strStripFrontBack2;
        int i = this.defaultLine;
        try {
            this.defaultLine = NO_MAPPING;
            if (parserGrammar.debuggingOutput) {
                this.semPreds = new Vector();
            }
            setGrammar(parserGrammar);
            if (!(this.grammar instanceof ParserGrammar)) {
                this.antlrTool.panic("Internal error generating parser");
            }
            this.currentOutput = getPrintWriterManager().setupOutput(this.antlrTool, this.grammar);
            this.genAST = this.grammar.buildAST;
            this.tabs = 0;
            genHeader();
            try {
                this.defaultLine = this.behavior.getHeaderActionLine("");
                println(this.behavior.getHeaderAction(""));
                this.defaultLine = NO_MAPPING;
                println("import antlr.TokenBuffer;");
                println("import antlr.TokenStreamException;");
                println("import antlr.TokenStreamIOException;");
                println("import antlr.ANTLRException;");
                println("import antlr." + this.grammar.getSuperClass() + ";");
                println("import antlr.Token;");
                println("import antlr.TokenStream;");
                println("import antlr.RecognitionException;");
                println("import antlr.NoViableAltException;");
                println("import antlr.MismatchedTokenException;");
                println("import antlr.SemanticException;");
                println("import antlr.ParserSharedInputState;");
                println("import antlr.collections.impl.BitSet;");
                if (this.genAST) {
                    println("import antlr.collections.AST;");
                    println("import java.util.Hashtable;");
                    println("import antlr.ASTFactory;");
                    println("import antlr.ASTPair;");
                    println("import antlr.collections.impl.ASTArray;");
                }
                println(this.grammar.preambleAction.getText());
                if (this.grammar.superClass != null) {
                    str = this.grammar.superClass;
                } else {
                    str = "antlr." + this.grammar.getSuperClass();
                }
                if (this.grammar.comment != null) {
                    _println(this.grammar.comment);
                }
                Token token = (Token) this.grammar.options.get("classHeaderPrefix");
                if (token == null || (strStripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) == null) {
                    strStripFrontBack = "public";
                }
                print(strStripFrontBack + " ");
                print("class " + this.grammar.getClassName() + " extends " + str);
                StringBuilder sb = new StringBuilder();
                sb.append("       implements ");
                sb.append(this.grammar.tokenManager.getName());
                sb.append(CodeGenerator.TokenTypesFileSuffix);
                println(sb.toString());
                Token token2 = (Token) this.grammar.options.get("classHeaderSuffix");
                if (token2 != null && (strStripFrontBack2 = StringUtils.stripFrontBack(token2.getText(), "\"", "\"")) != null) {
                    print(", " + strStripFrontBack2);
                }
                println(" {");
                if (this.grammar.debuggingOutput) {
                    println("private static final String _ruleNames[] = {");
                    Enumeration enumerationElements = this.grammar.rules.elements();
                    while (enumerationElements.hasMoreElements()) {
                        GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
                        if (grammarSymbol instanceof RuleSymbol) {
                            println("  \"" + ((RuleSymbol) grammarSymbol).getId() + "\",");
                        }
                    }
                    println("};");
                }
                print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null), this.grammar.classMemberAction.getLine());
                println("");
                println("protected " + this.grammar.getClassName() + "(TokenBuffer tokenBuf, int k) {");
                println("  super(tokenBuf,k);");
                println("  tokenNames = _tokenNames;");
                if (this.grammar.debuggingOutput) {
                    println("  ruleNames  = _ruleNames;");
                    println("  semPredNames = _semPredNames;");
                    println("  setupDebugging(tokenBuf);");
                }
                if (this.grammar.buildAST) {
                    println("  buildTokenTypeASTClassMap();");
                    println("  astFactory = new ASTFactory(getTokenTypeToASTClassMap());");
                }
                println("}");
                println("");
                println("public " + this.grammar.getClassName() + "(TokenBuffer tokenBuf) {");
                println("  this(tokenBuf," + this.grammar.maxk + ");");
                println("}");
                println("");
                println("protected " + this.grammar.getClassName() + "(TokenStream lexer, int k) {");
                println("  super(lexer,k);");
                println("  tokenNames = _tokenNames;");
                if (this.grammar.debuggingOutput) {
                    println("  ruleNames  = _ruleNames;");
                    println("  semPredNames = _semPredNames;");
                    println("  setupDebugging(lexer);");
                }
                if (this.grammar.buildAST) {
                    println("  buildTokenTypeASTClassMap();");
                    println("  astFactory = new ASTFactory(getTokenTypeToASTClassMap());");
                }
                println("}");
                println("");
                println("public " + this.grammar.getClassName() + "(TokenStream lexer) {");
                println("  this(lexer," + this.grammar.maxk + ");");
                println("}");
                println("");
                println("public " + this.grammar.getClassName() + "(ParserSharedInputState state) {");
                println("  super(state," + this.grammar.maxk + ");");
                println("  tokenNames = _tokenNames;");
                if (this.grammar.buildAST) {
                    println("  buildTokenTypeASTClassMap();");
                    println("  astFactory = new ASTFactory(getTokenTypeToASTClassMap());");
                }
                println("}");
                println("");
                Enumeration enumerationElements2 = this.grammar.rules.elements();
                int i2 = 0;
                while (enumerationElements2.hasMoreElements()) {
                    GrammarSymbol grammarSymbol2 = (GrammarSymbol) enumerationElements2.nextElement();
                    if (grammarSymbol2 instanceof RuleSymbol) {
                        RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol2;
                        genRule(ruleSymbol, ruleSymbol.references.size() == 0, i2);
                        i2++;
                    }
                    exitIfError();
                }
                genTokenStrings();
                if (this.grammar.buildAST) {
                    genTokenASTNodeMap();
                }
                genBitsets(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType());
                if (this.grammar.debuggingOutput) {
                    genSemPredMap();
                }
                println("");
                println("}");
                getPrintWriterManager().finishOutput();
            } catch (Throwable th) {
                this.defaultLine = NO_MAPPING;
                throw th;
            }
        } finally {
            this.defaultLine = i;
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(RuleRefElement ruleRefElement) {
        int i = this.defaultLine;
        try {
            this.defaultLine = ruleRefElement.getLine();
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("genRR(" + ruleRefElement + ")");
            }
            RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(ruleRefElement.targetRule);
            if (ruleSymbol != null && ruleSymbol.isDefined()) {
                genErrorTryForElement(ruleRefElement);
                if ((this.grammar instanceof TreeWalkerGrammar) && ruleRefElement.getLabel() != null && this.syntacticPredLevel == 0) {
                    println(ruleRefElement.getLabel() + " = _t==ASTNULL ? null : " + this.lt1Value + ";");
                }
                if ((this.grammar instanceof LexerGrammar) && (!this.saveText || ruleRefElement.getAutoGenType() == 3)) {
                    println("_saveIndex=text.length();");
                }
                printTabs();
                if (ruleRefElement.idAssign != null) {
                    if (ruleSymbol.block.returnAction == null) {
                        this.antlrTool.warning("Rule '" + ruleRefElement.targetRule + "' has no return type", this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
                    }
                    _print(ruleRefElement.idAssign + "=");
                } else if (!(this.grammar instanceof LexerGrammar) && this.syntacticPredLevel == 0 && ruleSymbol.block.returnAction != null) {
                    this.antlrTool.warning("Rule '" + ruleRefElement.targetRule + "' returns a value", this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
                }
                GenRuleInvocation(ruleRefElement);
                if ((this.grammar instanceof LexerGrammar) && (!this.saveText || ruleRefElement.getAutoGenType() == 3)) {
                    println("text.setLength(_saveIndex);");
                }
                if (this.syntacticPredLevel == 0) {
                    if (this.grammar.hasSyntacticPredicate && ((!this.grammar.buildAST || ruleRefElement.getLabel() == null) && this.genAST)) {
                        ruleRefElement.getAutoGenType();
                    }
                    if (this.grammar.buildAST && ruleRefElement.getLabel() != null) {
                        println(ruleRefElement.getLabel() + "_AST = (" + this.labeledElementASTType + ")returnAST;");
                    }
                    if (this.genAST) {
                        int autoGenType = ruleRefElement.getAutoGenType();
                        if (autoGenType == 1) {
                            println("astFactory.addASTChild(currentAST, returnAST);");
                        } else if (autoGenType == 2) {
                            this.antlrTool.error("Internal: encountered ^ after rule reference");
                        }
                    }
                    if ((this.grammar instanceof LexerGrammar) && ruleRefElement.getLabel() != null) {
                        println(ruleRefElement.getLabel() + "=_returnToken;");
                    }
                }
                genErrorCatchForElement(ruleRefElement);
                return;
            }
            this.antlrTool.error("Rule '" + ruleRefElement.targetRule + "' is not defined", this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
        } finally {
            this.defaultLine = i;
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(StringLiteralElement stringLiteralElement) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genString(" + stringLiteralElement + ")");
        }
        if (stringLiteralElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(stringLiteralElement.getLabel());
            sb.append(" = ");
            println(C0000a.m3a(sb, this.lt1Value, ";"), stringLiteralElement.getLine());
        }
        genElementAST(stringLiteralElement);
        boolean z = this.saveText;
        this.saveText = z && stringLiteralElement.getAutoGenType() == 1;
        genMatch(stringLiteralElement);
        this.saveText = z;
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t.getNextSibling();", stringLiteralElement.getLine());
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRangeElement tokenRangeElement) {
        genErrorTryForElement(tokenRangeElement);
        if (tokenRangeElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(tokenRangeElement.getLabel());
            sb.append(" = ");
            println(C0000a.m3a(sb, this.lt1Value, ";"), tokenRangeElement.getLine());
        }
        genElementAST(tokenRangeElement);
        StringBuilder sbM5a = C0000a.m5a("matchRange(");
        sbM5a.append(tokenRangeElement.beginText);
        sbM5a.append(",");
        println(C0000a.m3a(sbM5a, tokenRangeElement.endText, ");"), tokenRangeElement.getLine());
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
            StringBuilder sb = new StringBuilder();
            sb.append(tokenRefElement.getLabel());
            sb.append(" = ");
            println(C0000a.m3a(sb, this.lt1Value, ";"), tokenRefElement.getLine());
        }
        genElementAST(tokenRefElement);
        genMatch(tokenRefElement);
        genErrorCatchForElement(tokenRefElement);
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t.getNextSibling();", tokenRefElement.getLine());
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeElement treeElement) {
        int i = this.defaultLine;
        try {
            this.defaultLine = treeElement.getLine();
            println("AST __t" + treeElement.f302ID + " = _t;");
            if (treeElement.root.getLabel() != null) {
                println(treeElement.root.getLabel() + " = _t==ASTNULL ? null :(" + this.labeledElementASTType + ")_t;", treeElement.root.getLine());
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
                println("ASTPair __currentAST" + treeElement.f302ID + " = currentAST.copy();");
                println("currentAST.root = currentAST.child;");
                println("currentAST.child = null;");
            }
            GrammarAtom grammarAtom = treeElement.root;
            if (grammarAtom instanceof WildcardElement) {
                println("if ( _t==null ) throw new MismatchedTokenException();", grammarAtom.getLine());
            } else {
                genMatch(grammarAtom);
            }
            println("_t = _t.getFirstChild();");
            for (int i2 = 0; i2 < treeElement.getAlternatives().size(); i2++) {
                for (AlternativeElement alternativeElement = treeElement.getAlternativeAt(i2).head; alternativeElement != null; alternativeElement = alternativeElement.next) {
                    alternativeElement.generate();
                }
            }
            if (this.grammar.buildAST) {
                println("currentAST = __currentAST" + treeElement.f302ID + ";");
            }
            println("_t = __t" + treeElement.f302ID + ";");
            println("_t = _t.getNextSibling();");
        } finally {
            this.defaultLine = i;
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeWalkerGrammar treeWalkerGrammar) {
        String str;
        String strStripFrontBack;
        String strStripFrontBack2;
        int i = this.defaultLine;
        try {
            this.defaultLine = NO_MAPPING;
            setGrammar(treeWalkerGrammar);
            if (!(this.grammar instanceof TreeWalkerGrammar)) {
                this.antlrTool.panic("Internal error generating tree-walker");
            }
            this.currentOutput = getPrintWriterManager().setupOutput(this.antlrTool, this.grammar);
            this.genAST = this.grammar.buildAST;
            this.tabs = 0;
            genHeader();
            try {
                this.defaultLine = this.behavior.getHeaderActionLine("");
                println(this.behavior.getHeaderAction(""));
                this.defaultLine = NO_MAPPING;
                println("import antlr." + this.grammar.getSuperClass() + ";");
                println("import antlr.Token;");
                println("import antlr.collections.AST;");
                println("import antlr.RecognitionException;");
                println("import antlr.ANTLRException;");
                println("import antlr.NoViableAltException;");
                println("import antlr.MismatchedTokenException;");
                println("import antlr.SemanticException;");
                println("import antlr.collections.impl.BitSet;");
                println("import antlr.ASTPair;");
                println("import antlr.collections.impl.ASTArray;");
                println(this.grammar.preambleAction.getText());
                if (this.grammar.superClass != null) {
                    str = this.grammar.superClass;
                } else {
                    str = "antlr." + this.grammar.getSuperClass();
                }
                println("");
                if (this.grammar.comment != null) {
                    _println(this.grammar.comment);
                }
                Token token = (Token) this.grammar.options.get("classHeaderPrefix");
                if (token == null || (strStripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) == null) {
                    strStripFrontBack = "public";
                }
                print(strStripFrontBack + " ");
                print("class " + this.grammar.getClassName() + " extends " + str);
                StringBuilder sb = new StringBuilder();
                sb.append("       implements ");
                sb.append(this.grammar.tokenManager.getName());
                sb.append(CodeGenerator.TokenTypesFileSuffix);
                println(sb.toString());
                Token token2 = (Token) this.grammar.options.get("classHeaderSuffix");
                if (token2 != null && (strStripFrontBack2 = StringUtils.stripFrontBack(token2.getText(), "\"", "\"")) != null) {
                    print(", " + strStripFrontBack2);
                }
                println(" {");
                print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null), this.grammar.classMemberAction.getLine());
                println("public " + this.grammar.getClassName() + "() {");
                this.tabs = this.tabs + 1;
                println("tokenNames = _tokenNames;");
                this.tabs = this.tabs - 1;
                println("}");
                println("");
                Enumeration enumerationElements = this.grammar.rules.elements();
                int i2 = 0;
                while (enumerationElements.hasMoreElements()) {
                    GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
                    if (grammarSymbol instanceof RuleSymbol) {
                        RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol;
                        genRule(ruleSymbol, ruleSymbol.references.size() == 0, i2);
                        i2++;
                    }
                    exitIfError();
                }
                genTokenStrings();
                genBitsets(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType());
                println("}");
                println("");
                getPrintWriterManager().finishOutput();
            } catch (Throwable th) {
                this.defaultLine = NO_MAPPING;
                throw th;
            }
        } finally {
            this.defaultLine = i;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0099 A[Catch: all -> 0x00a1, TRY_LEAVE, TryCatch #0 {all -> 0x00a1, blocks: (B:3:0x0002, B:5:0x000e, B:7:0x0012, B:8:0x0034, B:11:0x003f, B:29:0x0093, B:31:0x0099, B:12:0x0043, B:14:0x0049, B:16:0x0050, B:18:0x0054, B:20:0x005a, B:21:0x005f, B:23:0x006a, B:25:0x006e, B:28:0x0077), top: B:37:0x0002 }] */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(WildcardElement wildcardElement) {
        String str;
        int i = this.defaultLine;
        try {
            this.defaultLine = wildcardElement.getLine();
            if (wildcardElement.getLabel() != null && this.syntacticPredLevel == 0) {
                println(wildcardElement.getLabel() + " = " + this.lt1Value + ";");
            }
            genElementAST(wildcardElement);
            if (this.grammar instanceof TreeWalkerGrammar) {
                str = "if ( _t==null ) throw new MismatchedTokenException();";
            } else {
                if (this.grammar instanceof LexerGrammar) {
                    if ((this.grammar instanceof LexerGrammar) && (!this.saveText || wildcardElement.getAutoGenType() == 3)) {
                        println("_saveIndex=text.length();");
                    }
                    println("matchNot(EOF_CHAR);");
                    if ((this.grammar instanceof LexerGrammar) && (!this.saveText || wildcardElement.getAutoGenType() == 3)) {
                        str = "text.setLength(_saveIndex);";
                    }
                    if (this.grammar instanceof TreeWalkerGrammar) {
                        println("_t = _t.getNextSibling();");
                    }
                }
                str = "matchNot(" + getValueString(1) + ");";
            }
            println(str);
            if (this.grammar instanceof TreeWalkerGrammar) {
            }
        } finally {
            this.defaultLine = i;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00b6 A[Catch: all -> 0x012f, TRY_ENTER, TryCatch #0 {all -> 0x012f, blocks: (B:3:0x0002, B:5:0x000c, B:6:0x0027, B:8:0x0035, B:10:0x004d, B:12:0x0077, B:13:0x007d, B:15:0x008d, B:17:0x0095, B:19:0x009f, B:29:0x00b6, B:31:0x00ba, B:32:0x00d2, B:33:0x00fc, B:20:0x00a2, B:22:0x00a6, B:9:0x003a), top: B:39:0x0002 }] */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(ZeroOrMoreBlock zeroOrMoreBlock) {
        String label;
        int i;
        boolean z;
        int i2;
        int i3 = this.defaultLine;
        try {
            this.defaultLine = zeroOrMoreBlock.getLine();
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("gen*(" + zeroOrMoreBlock + ")");
            }
            println("{");
            genBlockPreamble(zeroOrMoreBlock);
            if (zeroOrMoreBlock.getLabel() != null) {
                label = zeroOrMoreBlock.getLabel();
            } else {
                label = "_loop" + zeroOrMoreBlock.f302ID;
            }
            println(label + ":");
            println("do {");
            this.tabs = this.tabs + 1;
            genBlockInitAction(zeroOrMoreBlock);
            String str = this.currentASTResult;
            if (zeroOrMoreBlock.getLabel() != null) {
                this.currentASTResult = zeroOrMoreBlock.getLabel();
            }
            this.grammar.theLLkAnalyzer.deterministic(zeroOrMoreBlock);
            int i4 = this.grammar.maxk;
            if (zeroOrMoreBlock.greedy || (i2 = zeroOrMoreBlock.exitLookaheadDepth) > this.grammar.maxk || !zeroOrMoreBlock.exitCache[i2].containsEpsilon()) {
                if (zeroOrMoreBlock.greedy || zeroOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
                    i = i4;
                    z = false;
                }
                if (z) {
                    if (this.DEBUG_CODE_GENERATOR) {
                        System.out.println("nongreedy (...)* loop; exit depth is " + zeroOrMoreBlock.exitLookaheadDepth);
                    }
                    String lookaheadTestExpression = getLookaheadTestExpression(zeroOrMoreBlock.exitCache, i);
                    println("// nongreedy exit test");
                    println("if (" + lookaheadTestExpression + ") break " + label + ";");
                }
                genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), "break " + label + ";", zeroOrMoreBlock.getLine());
                this.tabs = this.tabs - 1;
                println("} while (true);");
                println("}");
                this.currentASTResult = str;
            }
            i4 = zeroOrMoreBlock.exitLookaheadDepth;
            i = i4;
            z = true;
            if (z) {
            }
            genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), "break " + label + ";", zeroOrMoreBlock.getLine());
            this.tabs = this.tabs - 1;
            println("} while (true);");
            println("}");
            this.currentASTResult = str;
        } finally {
            this.defaultLine = i3;
        }
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
        println(str2 + " " + str + "_AST = null;");
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
            println("try {      // for error handling", alternative.head.getLine());
            this.tabs++;
        }
        for (AlternativeElement alternativeElement = alternative.head; !(alternativeElement instanceof BlockEndElement); alternativeElement = alternativeElement.next) {
            alternativeElement.generate();
        }
        if (this.genAST) {
            if (alternativeBlock instanceof RuleBlock) {
                boolean z4 = this.grammar.hasSyntacticPredicate;
                println(((RuleBlock) alternativeBlock).getRuleName() + "_AST = (" + this.labeledElementASTType + ")currentAST.root;", CONTINUE_LAST_MAPPING);
                boolean z5 = this.grammar.hasSyntacticPredicate;
            } else if (alternativeBlock.getLabel() != null) {
                this.antlrTool.warning("Labeled subrules not yet supported", this.grammar.getFilename(), alternativeBlock.getLine(), alternativeBlock.getColumn());
            }
        }
        if (alternative.exceptionSpec != null) {
            this.tabs--;
            println("}", NO_MAPPING);
            genErrorHandler(alternative.exceptionSpec);
        }
        this.genAST = z;
        this.saveText = z3;
        this.treeVariableMap = hashtable;
    }

    public void genBitsets(Vector vector, int i) {
        println("", NO_MAPPING);
        for (int i2 = 0; i2 < vector.size(); i2++) {
            BitSet bitSet = (BitSet) vector.elementAt(i2);
            bitSet.growToInclude(i);
            genBitSet(bitSet, i2);
        }
    }

    public void genBlockInitAction(AlternativeBlock alternativeBlock) {
        String str = alternativeBlock.initAction;
        if (str != null) {
            printAction(processActionForSpecialSymbols(str, alternativeBlock.getLine(), this.currentRule, null), alternativeBlock.getLine());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00c5 A[Catch: all -> 0x0127, TryCatch #0 {all -> 0x0127, blocks: (B:10:0x001d, B:13:0x002d, B:15:0x0031, B:17:0x0035, B:20:0x003a, B:22:0x0066, B:24:0x006a, B:26:0x0073, B:35:0x00c5, B:27:0x007f, B:29:0x0083, B:31:0x008a, B:33:0x0099, B:36:0x00c9, B:38:0x00cf, B:39:0x00d2, B:41:0x00d8, B:42:0x00f5, B:44:0x00fb), top: B:50:0x001d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void genBlockPreamble(AlternativeBlock alternativeBlock) {
        if (alternativeBlock instanceof RuleBlock) {
            RuleBlock ruleBlock = (RuleBlock) alternativeBlock;
            if (ruleBlock.labeledElements != null) {
                for (int i = 0; i < ruleBlock.labeledElements.size(); i++) {
                    AlternativeElement alternativeElement = (AlternativeElement) ruleBlock.labeledElements.elementAt(i);
                    int i2 = this.defaultLine;
                    try {
                        this.defaultLine = alternativeElement.getLine();
                        if (!(alternativeElement instanceof RuleRefElement) && (!(alternativeElement instanceof AlternativeBlock) || (alternativeElement instanceof RuleBlock) || (alternativeElement instanceof SynPredBlock))) {
                            println(this.labeledElementType + " " + alternativeElement.getLabel() + " = " + this.labeledElementInit + ";");
                            if (this.grammar.buildAST) {
                                if ((alternativeElement instanceof GrammarAtom) && ((GrammarAtom) alternativeElement).getASTNodeType() != null) {
                                    genASTDeclaration(alternativeElement, ((GrammarAtom) alternativeElement).getASTNodeType());
                                }
                            }
                        } else if (!(alternativeElement instanceof RuleRefElement) && ((AlternativeBlock) alternativeElement).not && this.analyzer.subruleCanBeInverted((AlternativeBlock) alternativeElement, this.grammar instanceof LexerGrammar)) {
                            println(this.labeledElementType + " " + alternativeElement.getLabel() + " = " + this.labeledElementInit + ";");
                            if (this.grammar.buildAST) {
                                genASTDeclaration(alternativeElement);
                            }
                        } else {
                            if (this.grammar.buildAST) {
                                genASTDeclaration(alternativeElement);
                            }
                            if (this.grammar instanceof LexerGrammar) {
                                println("Token " + alternativeElement.getLabel() + "=null;");
                            }
                            if (this.grammar instanceof TreeWalkerGrammar) {
                                println(this.labeledElementType + " " + alternativeElement.getLabel() + " = " + this.labeledElementInit + ";");
                            }
                        }
                        this.defaultLine = i2;
                    } catch (Throwable th) {
                        this.defaultLine = i2;
                        throw th;
                    }
                }
            }
        }
    }

    public void genCases(BitSet bitSet, int i) {
        int i2 = this.defaultLine;
        try {
            this.defaultLine = i;
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("genCases(" + bitSet + ")");
            }
            int[] array = bitSet.toArray();
            int i3 = this.grammar instanceof LexerGrammar ? 4 : 1;
            boolean z = true;
            int i4 = 1;
            for (int i5 : array) {
                if (i4 == 1) {
                    print("");
                } else {
                    _print("  ");
                }
                _print("case " + getValueString(i5) + ":");
                if (i4 == i3) {
                    _println("");
                    z = true;
                    i4 = 1;
                } else {
                    i4++;
                    z = false;
                }
            }
            if (!z) {
                _println("");
            }
        } finally {
            this.defaultLine = i2;
        }
    }

    public JavaBlockFinishingInfo genCommonBlock(AlternativeBlock alternativeBlock, boolean z) throws Throwable {
        int i;
        String str;
        boolean z2;
        int i2;
        JavaBlockFinishingInfo javaBlockFinishingInfo;
        boolean z3;
        String str2;
        int i3;
        boolean z4;
        boolean zLookaheadIsEmpty;
        int i4;
        boolean z5;
        boolean z6;
        int i5;
        int i6;
        JavaBlockFinishingInfo javaBlockFinishingInfo2;
        String str3;
        StringBuilder sb;
        String string;
        StringBuilder sb2;
        String str4;
        int i7 = this.defaultLine;
        try {
            this.defaultLine = alternativeBlock.getLine();
            JavaBlockFinishingInfo javaBlockFinishingInfo3 = new JavaBlockFinishingInfo();
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("genCommonBlock(" + alternativeBlock + ")");
            }
            boolean z7 = this.genAST;
            char c2 = 1;
            this.genAST = this.genAST && alternativeBlock.getAutoGen();
            boolean z8 = this.saveText;
            this.saveText = this.saveText && alternativeBlock.getAutoGen();
            str = "";
            if (alternativeBlock.not && this.analyzer.subruleCanBeInverted(alternativeBlock, this.grammar instanceof LexerGrammar)) {
                if (this.DEBUG_CODE_GENERATOR) {
                    System.out.println("special case: ~(subrule)");
                }
                Lookahead lookaheadLook = this.analyzer.look(1, alternativeBlock);
                if (alternativeBlock.getLabel() != null && this.syntacticPredLevel == 0) {
                    println(alternativeBlock.getLabel() + " = " + this.lt1Value + ";");
                }
                genElementAST(alternativeBlock);
                println("match(" + (this.grammar instanceof TreeWalkerGrammar ? "_t," : "") + getBitsetName(markBitsetForGen(lookaheadLook.fset)) + ");");
                if (this.grammar instanceof TreeWalkerGrammar) {
                    println("_t = _t.getNextSibling();");
                }
                return javaBlockFinishingInfo3;
            }
            if (alternativeBlock.getAlternatives().size() == 1) {
                Alternative alternativeAt = alternativeBlock.getAlternativeAt(0);
                if (alternativeAt.synPred != null) {
                    this.antlrTool.warning("Syntactic predicate superfluous for single alternative", this.grammar.getFilename(), alternativeBlock.getAlternativeAt(0).synPred.getLine(), alternativeBlock.getAlternativeAt(0).synPred.getColumn());
                }
                if (z) {
                    String str5 = alternativeAt.semPred;
                    if (str5 != null) {
                        genSemPred(str5, alternativeBlock.line);
                    }
                    genAlt(alternativeAt, alternativeBlock);
                    return javaBlockFinishingInfo3;
                }
            }
            int i8 = 0;
            for (int i9 = 0; i9 < alternativeBlock.getAlternatives().size(); i9++) {
                if (suitableForCaseExpression(alternativeBlock.getAlternativeAt(i9))) {
                    i8++;
                }
            }
            String str6 = "{";
            String str7 = "}";
            if (i8 >= this.makeSwitchThreshold) {
                String strLookaheadString = lookaheadString(1);
                if (this.grammar instanceof TreeWalkerGrammar) {
                    println("if (_t==null) _t=ASTNULL;");
                }
                println("switch ( " + strLookaheadString + ") {");
                int i10 = 0;
                while (i10 < alternativeBlock.alternatives.size()) {
                    Alternative alternativeAt2 = alternativeBlock.getAlternativeAt(i10);
                    if (suitableForCaseExpression(alternativeAt2)) {
                        Lookahead lookahead = alternativeAt2.cache[c2];
                        if (lookahead.fset.degree() != 0 || lookahead.containsEpsilon()) {
                            genCases(lookahead.fset, alternativeAt2.head.getLine());
                            println("{", alternativeAt2.head.getLine());
                            this.tabs++;
                            genAlt(alternativeAt2, alternativeBlock);
                            println("break;", NO_MAPPING);
                            this.tabs--;
                            println("}", NO_MAPPING);
                        } else {
                            this.antlrTool.warning("Alternate omitted due to empty prediction set", this.grammar.getFilename(), alternativeAt2.head.getLine(), alternativeAt2.head.getColumn());
                        }
                    }
                    i10++;
                    c2 = 1;
                }
                println("default:");
                this.tabs++;
                z2 = true;
            } else {
                z2 = false;
            }
            int i11 = this.grammar instanceof LexerGrammar ? this.grammar.maxk : 0;
            int i12 = 0;
            int i13 = 0;
            while (i11 >= 0) {
                if (this.DEBUG_CODE_GENERATOR) {
                    PrintStream printStream = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    str2 = str;
                    sb3.append("checking depth ");
                    sb3.append(i11);
                    printStream.println(sb3.toString());
                } else {
                    str2 = str;
                }
                int i14 = i12;
                int i15 = 0;
                while (i15 < alternativeBlock.alternatives.size()) {
                    Alternative alternativeAt3 = alternativeBlock.getAlternativeAt(i15);
                    if (this.DEBUG_CODE_GENERATOR) {
                        PrintStream printStream2 = System.out;
                        i3 = i2;
                        try {
                            StringBuilder sb4 = new StringBuilder();
                            z4 = z8;
                            sb4.append("genAlt: ");
                            sb4.append(i15);
                            printStream2.println(sb4.toString());
                        } catch (Throwable th) {
                            th = th;
                            i = i3;
                            this.defaultLine = i;
                            throw th;
                        }
                    } else {
                        i3 = i2;
                        z4 = z8;
                    }
                    if (!z2 || !suitableForCaseExpression(alternativeAt3)) {
                        if (this.grammar instanceof LexerGrammar) {
                            i4 = alternativeAt3.lookaheadDepth;
                            if (i4 == Integer.MAX_VALUE) {
                                i4 = this.grammar.maxk;
                            }
                            while (i4 >= 1 && alternativeAt3.cache[i4].containsEpsilon()) {
                                i4--;
                            }
                            if (i4 == i11) {
                                zLookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, i4);
                            } else if (this.DEBUG_CODE_GENERATOR) {
                                System.out.println("ignoring alt because effectiveDepth!=altDepth;" + i4 + "!=" + i11);
                            }
                        } else {
                            zLookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, this.grammar.maxk);
                            i4 = this.grammar.maxk;
                        }
                        String lookaheadTestExpression = getLookaheadTestExpression(alternativeAt3, i4);
                        i7 = this.defaultLine;
                        z5 = z2;
                        try {
                            this.defaultLine = alternativeAt3.head.getLine();
                            z6 = z7;
                            i5 = i11;
                            i6 = i15;
                            String str8 = str7;
                            if (alternativeAt3.cache[1].fset.degree() <= 127 || !suitableForCaseExpression(alternativeAt3)) {
                                if (zLookaheadIsEmpty && alternativeAt3.semPred == null && alternativeAt3.synPred == null) {
                                    if (i14 == 0) {
                                        println(str6);
                                    } else {
                                        println("else {");
                                    }
                                    javaBlockFinishingInfo3.needAnErrorClause = false;
                                    javaBlockFinishingInfo2 = javaBlockFinishingInfo3;
                                    str3 = str6;
                                } else {
                                    if (alternativeAt3.semPred != null) {
                                        str3 = str6;
                                        javaBlockFinishingInfo2 = javaBlockFinishingInfo3;
                                        String strProcessActionForSpecialSymbols = processActionForSpecialSymbols(alternativeAt3.semPred, alternativeBlock.line, this.currentRule, new ActionTransInfo());
                                        if (((this.grammar instanceof ParserGrammar) || (this.grammar instanceof LexerGrammar)) && this.grammar.debuggingOutput) {
                                            sb2 = new StringBuilder();
                                            sb2.append("(");
                                            sb2.append(lookaheadTestExpression);
                                            sb2.append("&& fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.PREDICTING,");
                                            sb2.append(addSemPred(this.charFormatter.escapeString(strProcessActionForSpecialSymbols)));
                                            sb2.append(",");
                                            sb2.append(strProcessActionForSpecialSymbols);
                                        } else {
                                            sb2 = new StringBuilder();
                                            sb2.append("(");
                                            sb2.append(lookaheadTestExpression);
                                            sb2.append("&&(");
                                            sb2.append(strProcessActionForSpecialSymbols);
                                        }
                                        sb2.append("))");
                                        lookaheadTestExpression = sb2.toString();
                                    } else {
                                        javaBlockFinishingInfo2 = javaBlockFinishingInfo3;
                                        str3 = str6;
                                    }
                                    if (i14 > 0) {
                                        SynPredBlock synPredBlock = alternativeAt3.synPred;
                                        if (synPredBlock != null) {
                                            println("else {", synPredBlock.getLine());
                                            this.tabs++;
                                            genSynPred(alternativeAt3.synPred, lookaheadTestExpression);
                                            i13++;
                                        } else {
                                            sb = new StringBuilder();
                                            sb.append("else if ");
                                            sb.append(lookaheadTestExpression);
                                            sb.append(" {");
                                            string = sb.toString();
                                        }
                                    } else {
                                        SynPredBlock synPredBlock2 = alternativeAt3.synPred;
                                        if (synPredBlock2 != null) {
                                            genSynPred(synPredBlock2, lookaheadTestExpression);
                                        } else {
                                            if (this.grammar instanceof TreeWalkerGrammar) {
                                                println("if (_t==null) _t=ASTNULL;");
                                            }
                                            sb = new StringBuilder();
                                            sb.append("if ");
                                            sb.append(lookaheadTestExpression);
                                            sb.append(" {");
                                            string = sb.toString();
                                        }
                                    }
                                }
                                this.defaultLine = i7;
                                i14++;
                                this.tabs++;
                                genAlt(alternativeAt3, alternativeBlock);
                                this.tabs--;
                                str4 = str8;
                                println(str4);
                                i15 = i6 + 1;
                                z2 = z5;
                                str7 = str4;
                                i2 = i3;
                                z8 = z4;
                                z7 = z6;
                                str6 = str3;
                                i11 = i5;
                                javaBlockFinishingInfo3 = javaBlockFinishingInfo2;
                            } else {
                                string = i14 == 0 ? "if " + lookaheadTestExpression + " {" : "else if " + lookaheadTestExpression + " {";
                                javaBlockFinishingInfo2 = javaBlockFinishingInfo3;
                                str3 = str6;
                            }
                            println(string);
                            this.defaultLine = i7;
                            i14++;
                            this.tabs++;
                            genAlt(alternativeAt3, alternativeBlock);
                            this.tabs--;
                            str4 = str8;
                            println(str4);
                            i15 = i6 + 1;
                            z2 = z5;
                            str7 = str4;
                            i2 = i3;
                            z8 = z4;
                            z7 = z6;
                            str6 = str3;
                            i11 = i5;
                            javaBlockFinishingInfo3 = javaBlockFinishingInfo2;
                        } finally {
                            this.defaultLine = i7;
                        }
                    } else if (this.DEBUG_CODE_GENERATOR) {
                        System.out.println("ignoring alt because it was in the switch");
                    }
                    javaBlockFinishingInfo2 = javaBlockFinishingInfo3;
                    z6 = z7;
                    z5 = z2;
                    i5 = i11;
                    i6 = i15;
                    str3 = str6;
                    str4 = str7;
                    i15 = i6 + 1;
                    z2 = z5;
                    str7 = str4;
                    i2 = i3;
                    z8 = z4;
                    z7 = z6;
                    str6 = str3;
                    i11 = i5;
                    javaBlockFinishingInfo3 = javaBlockFinishingInfo2;
                }
                i11--;
                i12 = i14;
                str = str2;
                i2 = i2;
            }
            int i16 = i2;
            JavaBlockFinishingInfo javaBlockFinishingInfo4 = javaBlockFinishingInfo3;
            boolean z9 = z7;
            boolean z10 = z8;
            boolean z11 = z2;
            String str9 = str7;
            String str10 = str;
            for (int i17 = 1; i17 <= i13; i17++) {
                str10 = str10 + str9;
            }
            this.genAST = z9;
            this.saveText = z10;
            if (z11) {
                this.tabs--;
                javaBlockFinishingInfo = javaBlockFinishingInfo4;
                javaBlockFinishingInfo.postscript = str10 + str9;
                z3 = true;
                javaBlockFinishingInfo.generatedSwitch = true;
                if (i12 <= 0) {
                    z3 = false;
                }
            } else {
                javaBlockFinishingInfo = javaBlockFinishingInfo4;
                z3 = true;
                javaBlockFinishingInfo.postscript = str10;
                javaBlockFinishingInfo.generatedSwitch = false;
                if (i12 <= 0) {
                    z3 = false;
                }
            }
            javaBlockFinishingInfo.generatedAnIf = z3;
            this.defaultLine = i16;
            return javaBlockFinishingInfo;
        } catch (Throwable th2) {
            th = th2;
            i = i7;
        }
    }

    public void genHeader() {
        StringBuilder sbM5a = C0000a.m5a("// $ANTLR ");
        sbM5a.append(Tool.version);
        sbM5a.append(": \"");
        Tool tool = this.antlrTool;
        sbM5a.append(tool.fileMinusPath(tool.grammarFile));
        sbM5a.append("\" -> \"");
        sbM5a.append(this.grammar.getClassName());
        sbM5a.append(".java\"$");
        println(sbM5a.toString(), NO_MAPPING);
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
        int i = this.defaultLine;
        try {
            this.defaultLine = grammarAtom.getLine();
            String str = this.grammar instanceof TreeWalkerGrammar ? "_t," : "";
            if ((this.grammar instanceof LexerGrammar) && (!this.saveText || grammarAtom.getAutoGenType() == 3)) {
                println("_saveIndex=text.length();");
            }
            print(grammarAtom.not ? "matchNot(" : "match(");
            _print(str, NO_MAPPING);
            _print(grammarAtom.atomText.equals("EOF") ? "Token.EOF_TYPE" : grammarAtom.atomText);
            _println(");");
            if ((this.grammar instanceof LexerGrammar) && (!this.saveText || grammarAtom.getAutoGenType() == 3)) {
                println("text.setLength(_saveIndex);");
            }
        } finally {
            this.defaultLine = i;
        }
    }

    public void genMatchUsingAtomTokenType(GrammarAtom grammarAtom) {
        StringBuilder sbM5a = C0000a.m5a(this.grammar instanceof TreeWalkerGrammar ? "_t," : "");
        sbM5a.append(getValueString(grammarAtom.getType()));
        String string = sbM5a.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(grammarAtom.not ? "matchNot(" : "match(");
        sb.append(string);
        sb.append(");");
        println(sb.toString(), grammarAtom.getLine());
    }

    public void genNextToken() {
        boolean z;
        StringBuilder sb;
        String str;
        Tool tool;
        StringBuilder sb2;
        int i = this.defaultLine;
        try {
            this.defaultLine = NO_MAPPING;
            int i2 = 0;
            while (true) {
                if (i2 >= this.grammar.rules.size()) {
                    z = false;
                    break;
                }
                RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.rules.elementAt(i2);
                if (ruleSymbol.isDefined() && ruleSymbol.access.equals("public")) {
                    z = true;
                    break;
                }
                i2++;
            }
            if (!z) {
                println("");
                println("public Token nextToken() throws TokenStreamException {");
                println("\ttry {uponEOF();}");
                println("\tcatch(CharStreamIOException csioe) {");
                println("\t\tthrow new TokenStreamIOException(csioe.io);");
                println("\t}");
                println("\tcatch(CharStreamException cse) {");
                println("\t\tthrow new TokenStreamException(cse.getMessage());");
                println("\t}");
                println("\treturn new CommonToken(Token.EOF_TYPE, \"\");");
                println("}");
                println("");
                return;
            }
            RuleBlock ruleBlockCreateNextTokenRule = MakeGrammar.createNextTokenRule(this.grammar, this.grammar.rules, "nextToken");
            RuleSymbol ruleSymbol2 = new RuleSymbol("mnextToken");
            ruleSymbol2.setDefined();
            ruleSymbol2.setBlock(ruleBlockCreateNextTokenRule);
            ruleSymbol2.access = "private";
            this.grammar.define(ruleSymbol2);
            this.grammar.theLLkAnalyzer.deterministic(ruleBlockCreateNextTokenRule);
            String str2 = ((LexerGrammar) this.grammar).filterMode ? ((LexerGrammar) this.grammar).filterRule : null;
            println("");
            println("public Token nextToken() throws TokenStreamException {");
            this.tabs++;
            println("Token theRetToken=null;");
            _println("tryAgain:");
            println("for (;;) {");
            this.tabs++;
            println("Token _token = null;");
            println("int _ttype = Token.INVALID_TYPE;");
            if (((LexerGrammar) this.grammar).filterMode) {
                println("setCommitToPath(false);");
                if (str2 != null) {
                    if (this.grammar.isDefined(CodeGenerator.encodeLexerRuleName(str2))) {
                        RuleSymbol ruleSymbol3 = (RuleSymbol) this.grammar.getSymbol(CodeGenerator.encodeLexerRuleName(str2));
                        if (ruleSymbol3.isDefined()) {
                            if (ruleSymbol3.access.equals("public")) {
                                tool = this.grammar.antlrTool;
                                sb2 = new StringBuilder();
                                sb2.append("Filter rule ");
                                sb2.append(str2);
                                sb2.append(" must be protected");
                            }
                            println("int _m;");
                            println("_m = mark();");
                        } else {
                            tool = this.grammar.antlrTool;
                            sb2 = new StringBuilder();
                            sb2.append("Filter rule ");
                            sb2.append(str2);
                            sb2.append(" does not exist in this lexer");
                        }
                    } else {
                        tool = this.grammar.antlrTool;
                        sb2 = new StringBuilder();
                        sb2.append("Filter rule ");
                        sb2.append(str2);
                        sb2.append(" does not exist in this lexer");
                    }
                    tool.error(sb2.toString());
                    println("int _m;");
                    println("_m = mark();");
                }
            }
            println("resetText();");
            println("try {   // for char stream error handling");
            this.tabs++;
            println("try {   // for lexical error handling");
            this.tabs++;
            for (int i3 = 0; i3 < ruleBlockCreateNextTokenRule.getAlternatives().size(); i3++) {
                Alternative alternativeAt = ruleBlockCreateNextTokenRule.getAlternativeAt(i3);
                if (alternativeAt.cache[1].containsEpsilon()) {
                    String strDecodeLexerRuleName = CodeGenerator.decodeLexerRuleName(((RuleRefElement) alternativeAt.head).targetRule);
                    this.antlrTool.warning("public lexical rule " + strDecodeLexerRuleName + " is optional (can match \"nothing\")");
                }
            }
            String property = System.getProperty("line.separator");
            JavaBlockFinishingInfo javaBlockFinishingInfoGenCommonBlock = genCommonBlock(ruleBlockCreateNextTokenRule, false);
            String str3 = "if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}" + property + "\t\t\t\t";
            if (!((LexerGrammar) this.grammar).filterMode) {
                sb = new StringBuilder();
                sb.append(str3);
                sb.append("else {");
                sb.append(this.throwNoViable);
                sb.append("}");
            } else if (str2 == null) {
                sb = new StringBuilder();
                sb.append(str3);
                sb.append("else {consume(); continue tryAgain;}");
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append("else {");
                sb3.append(property);
                sb3.append("\t\t\t\t\tcommit();");
                sb3.append(property);
                sb3.append("\t\t\t\t\ttry {m");
                sb3.append(str2);
                sb3.append("(false);}");
                sb3.append(property);
                sb3.append("\t\t\t\t\tcatch(RecognitionException e) {");
                sb3.append(property);
                sb3.append("\t\t\t\t\t\t// catastrophic failure");
                sb3.append(property);
                sb3.append("\t\t\t\t\t\treportError(e);");
                sb3.append(property);
                sb3.append("\t\t\t\t\t\tconsume();");
                sb3.append(property);
                sb3.append("\t\t\t\t\t}");
                sb3.append(property);
                sb3.append("\t\t\t\t\tcontinue tryAgain;");
                sb3.append(property);
                sb3.append("\t\t\t\t}");
                sb = sb3;
            }
            genBlockFinish(javaBlockFinishingInfoGenCommonBlock, sb.toString(), ruleBlockCreateNextTokenRule.getLine());
            if (((LexerGrammar) this.grammar).filterMode && str2 != null) {
                println("commit();");
            }
            println("if ( _returnToken==null ) continue tryAgain; // found SKIP token");
            println("_ttype = _returnToken.getType();");
            if (((LexerGrammar) this.grammar).getTestLiterals()) {
                genLiteralsTest();
            }
            println("_returnToken.setType(_ttype);");
            println("return _returnToken;");
            this.tabs--;
            println("}");
            println("catch (RecognitionException e) {");
            this.tabs++;
            if (((LexerGrammar) this.grammar).filterMode) {
                if (str2 == null) {
                    println("if ( !getCommitToPath() ) {consume(); continue tryAgain;}");
                } else {
                    println("if ( !getCommitToPath() ) {");
                    this.tabs++;
                    println("rewind(_m);");
                    println("resetText();");
                    println("try {m" + str2 + "(false);}");
                    println("catch(RecognitionException ee) {");
                    println("\t// horrendous failure: error in filter rule");
                    println("\treportError(ee);");
                    println("\tconsume();");
                    println("}");
                    println("continue tryAgain;");
                    this.tabs--;
                    println("}");
                }
            }
            if (ruleBlockCreateNextTokenRule.getDefaultErrorHandler()) {
                println("reportError(e);");
                str = "consume();";
            } else {
                str = "throw new TokenStreamRecognitionException(e);";
            }
            println(str);
            this.tabs--;
            println("}");
            this.tabs--;
            println("}");
            println("catch (CharStreamException cse) {");
            println("\tif ( cse instanceof CharStreamIOException ) {");
            println("\t\tthrow new TokenStreamIOException(((CharStreamIOException)cse).io);");
            println("\t}");
            println("\telse {");
            println("\t\tthrow new TokenStreamException(cse.getMessage());");
            println("\t}");
            println("}");
            this.tabs--;
            println("}");
            this.tabs--;
            println("}");
            println("");
        } finally {
            this.defaultLine = i;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x0327 A[Catch: all -> 0x052f, TRY_LEAVE, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x034c A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0357 A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x035c A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x03f9 A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x041c A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0427 A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x043e A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0459 A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x048e A[Catch: all -> 0x052f, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:156:0x04a4 A[Catch: all -> 0x052f, TRY_LEAVE, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x04dc A[Catch: all -> 0x052f, TRY_LEAVE, TryCatch #1 {all -> 0x052f, blocks: (B:105:0x0303, B:107:0x0323, B:111:0x0346, B:115:0x0357, B:130:0x03f3, B:132:0x03f9, B:133:0x0416, B:135:0x041c, B:136:0x0421, B:138:0x0427, B:140:0x0431, B:141:0x0435, B:142:0x0438, B:144:0x043e, B:145:0x0455, B:147:0x0459, B:148:0x047e, B:150:0x0484, B:173:0x0519, B:154:0x048e, B:156:0x04a4, B:159:0x04ac, B:163:0x04cf, B:160:0x04bb, B:162:0x04c1, B:164:0x04d6, B:166:0x04dc, B:169:0x04e4, B:171:0x0509, B:170:0x04f7, B:172:0x0510, B:116:0x035c, B:118:0x0362, B:120:0x0389, B:121:0x0394, B:123:0x039f, B:124:0x03ca, B:126:0x03d1, B:128:0x03d7, B:129:0x03ea, B:113:0x034c, B:108:0x0327), top: B:184:0x02e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0138 A[Catch: all -> 0x0533, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x017d A[Catch: all -> 0x0533, TRY_ENTER, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01a0 A[Catch: all -> 0x0533, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01da A[Catch: all -> 0x0533, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0218 A[Catch: all -> 0x0533, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0256 A[Catch: all -> 0x0533, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0266 A[Catch: all -> 0x0533, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0298 A[Catch: all -> 0x0533, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x02d7 A[Catch: all -> 0x0533, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x02e9 A[Catch: all -> 0x0533, TryCatch #0 {all -> 0x0533, blocks: (B:10:0x004b, B:12:0x0064, B:16:0x006d, B:18:0x0079, B:19:0x007c, B:22:0x0098, B:23:0x00b5, B:25:0x00bc, B:27:0x00e1, B:29:0x00e5, B:30:0x00ea, B:33:0x00f0, B:35:0x0109, B:38:0x0127, B:42:0x0134, B:44:0x0138, B:46:0x013e, B:47:0x0157, B:48:0x016d, B:51:0x017d, B:52:0x0191, B:55:0x01a0, B:58:0x01a8, B:60:0x01cd, B:59:0x01bb, B:61:0x01d4, B:63:0x01da, B:66:0x01e8, B:68:0x0209, B:67:0x01ec, B:69:0x020e, B:72:0x0218, B:75:0x0220, B:79:0x0243, B:76:0x022f, B:78:0x0235, B:80:0x024a, B:82:0x0250, B:85:0x0260, B:87:0x0266, B:88:0x0292, B:90:0x0298, B:91:0x02c2, B:93:0x02d1, B:96:0x02e1, B:98:0x02e9, B:100:0x02f2, B:101:0x02f9, B:103:0x02fd, B:95:0x02d7, B:84:0x0256, B:39:0x012b, B:34:0x0106), top: B:182:0x004b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void genRule(RuleSymbol ruleSymbol, boolean z, int i) throws Throwable {
        int i2;
        String str;
        ExceptionSpec exceptionSpecFindExceptionSpec;
        int i3;
        boolean z2;
        String str2;
        String str3;
        int i4;
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        String str4;
        StringBuilder sb4;
        String str5;
        this.tabs = 1;
        if (this.DEBUG_CODE_GENERATOR) {
            PrintStream printStream = System.out;
            StringBuilder sbM5a = C0000a.m5a("genRule(");
            sbM5a.append(ruleSymbol.getId());
            sbM5a.append(")");
            printStream.println(sbM5a.toString());
        }
        if (!ruleSymbol.isDefined()) {
            Tool tool = this.antlrTool;
            StringBuilder sbM5a2 = C0000a.m5a("undefined rule: ");
            sbM5a2.append(ruleSymbol.getId());
            tool.error(sbM5a2.toString());
            return;
        }
        RuleBlock block = ruleSymbol.getBlock();
        int i5 = this.defaultLine;
        try {
            this.defaultLine = block.getLine();
            this.currentRule = block;
            this.currentASTResult = ruleSymbol.getId();
            this.declaredASTVariables.clear();
            boolean z3 = this.genAST;
            this.genAST = this.genAST && block.getAutoGen();
            this.saveText = block.getAutoGen();
            String str6 = ruleSymbol.comment;
            if (str6 != null) {
                _println(str6);
            }
            print(ruleSymbol.access + " final ");
            if (block.returnAction != null) {
                str = extractTypeOfAction(block.returnAction, block.getLine(), block.getColumn()) + " ";
            } else {
                str = "void ";
            }
            _print(str);
            _print(ruleSymbol.getId() + "(");
            _print(this.commonExtraParams);
            if (this.commonExtraParams.length() != 0 && block.argAction != null) {
                _print(",");
            }
            if (block.argAction != null) {
                _println("");
                this.tabs++;
                println(block.argAction);
                this.tabs--;
                print(")");
            } else {
                _print(")");
            }
            _print(" throws " + this.exceptionThrown);
            if (!(this.grammar instanceof ParserGrammar)) {
                str5 = this.grammar instanceof LexerGrammar ? ", CharStreamException, TokenStreamException" : ", TokenStreamException";
                if (block.throwsSpec != null) {
                    if (this.grammar instanceof LexerGrammar) {
                        this.antlrTool.error("user-defined throws spec not allowed (yet) for lexer rule " + block.ruleName);
                    } else {
                        _print(", " + block.throwsSpec);
                    }
                }
                _println(" {");
                this.tabs++;
                if (block.returnAction != null) {
                    println(block.returnAction + ";");
                }
                println(this.commonLocalVars);
                if (this.grammar.traceRules) {
                    if (this.grammar instanceof TreeWalkerGrammar) {
                        sb4 = new StringBuilder();
                        sb4.append("traceIn(\"");
                        sb4.append(ruleSymbol.getId());
                        sb4.append("\",_t);");
                    } else {
                        sb4 = new StringBuilder();
                        sb4.append("traceIn(\"");
                        sb4.append(ruleSymbol.getId());
                        sb4.append("\");");
                    }
                    println(sb4.toString());
                }
                if (this.grammar instanceof LexerGrammar) {
                    if (ruleSymbol.getId().equals("mEOF")) {
                        str4 = "_ttype = Token.EOF_TYPE;";
                    } else {
                        str4 = "_ttype = " + ruleSymbol.getId().substring(1) + ";";
                    }
                    println(str4);
                    println("int _saveIndex;");
                }
                if (this.grammar.debuggingOutput) {
                    if (this.grammar instanceof ParserGrammar) {
                        sb3 = new StringBuilder();
                        sb3.append("fireEnterRule(");
                        sb3.append(i);
                        sb3.append(",0);");
                    } else if (this.grammar instanceof LexerGrammar) {
                        sb3 = new StringBuilder();
                        sb3.append("fireEnterRule(");
                        sb3.append(i);
                        sb3.append(",_ttype);");
                    }
                    println(sb3.toString());
                }
                if (!this.grammar.debuggingOutput || this.grammar.traceRules) {
                    println("try { // debugging");
                    this.tabs++;
                }
                if (this.grammar instanceof TreeWalkerGrammar) {
                    println(this.labeledElementASTType + " " + ruleSymbol.getId() + "_AST_in = (_t == ASTNULL) ? null : (" + this.labeledElementASTType + ")_t;", NO_MAPPING);
                }
                if (this.grammar.buildAST) {
                    println("returnAST = null;");
                    println("ASTPair currentAST = new ASTPair();");
                    println(this.labeledElementASTType + " " + ruleSymbol.getId() + "_AST = null;");
                }
                genBlockPreamble(block);
                genBlockInitAction(block);
                println("");
                exceptionSpecFindExceptionSpec = block.findExceptionSpec("");
                if (exceptionSpecFindExceptionSpec == null || block.getDefaultErrorHandler()) {
                    println("try {      // for error handling");
                    this.tabs++;
                }
                try {
                    if (block.alternatives.size() != 1) {
                        Alternative alternativeAt = block.getAlternativeAt(0);
                        String str7 = alternativeAt.semPred;
                        if (str7 != null) {
                            genSemPred(str7, this.currentRule.line);
                        }
                        if (alternativeAt.synPred != null) {
                            i3 = i5;
                            z2 = z3;
                            str2 = "";
                            this.antlrTool.warning("Syntactic predicate ignored for single alternative", this.grammar.getFilename(), alternativeAt.synPred.getLine(), alternativeAt.synPred.getColumn());
                        } else {
                            i3 = i5;
                            z2 = z3;
                            str2 = "";
                        }
                        genAlt(alternativeAt, block);
                    } else {
                        i3 = i5;
                        z2 = z3;
                        str2 = "";
                        this.grammar.theLLkAnalyzer.deterministic(block);
                        genBlockFinish(genCommonBlock(block, false), this.throwNoViable, block.getLine());
                    }
                    if (exceptionSpecFindExceptionSpec == null || block.getDefaultErrorHandler()) {
                        this.tabs--;
                        println("}");
                    }
                    if (exceptionSpecFindExceptionSpec == null) {
                        genErrorHandler(exceptionSpecFindExceptionSpec);
                    } else if (block.getDefaultErrorHandler()) {
                        println("catch (" + this.exceptionThrown + " ex) {");
                        this.tabs = this.tabs + 1;
                        if (this.grammar.hasSyntacticPredicate) {
                            println("if (inputState.guessing==0) {");
                            this.tabs++;
                        }
                        println("reportError(ex);");
                        if (this.grammar instanceof TreeWalkerGrammar) {
                            str3 = "if (_t!=null) {_t = _t.getNextSibling();}";
                        } else {
                            str3 = "recover(ex," + getBitsetName(markBitsetForGen(this.grammar.theLLkAnalyzer.FOLLOW(1, block.endNode).fset)) + ");";
                        }
                        println(str3);
                        if (this.grammar.hasSyntacticPredicate) {
                            this.tabs--;
                            println("} else {");
                            println("  throw ex;");
                            println("}");
                        }
                        this.tabs--;
                        println("}");
                    }
                    if (this.grammar.buildAST) {
                        println("returnAST = " + ruleSymbol.getId() + "_AST;");
                    }
                    if (this.grammar instanceof TreeWalkerGrammar) {
                        println("_retTree = _t;");
                    }
                    if (block.getTestLiterals()) {
                        if (ruleSymbol.access.equals("protected")) {
                            genLiteralsTestForPartialToken();
                        } else {
                            genLiteralsTest();
                        }
                    }
                    if (this.grammar instanceof LexerGrammar) {
                        println("if ( _createToken && _token==null && _ttype!=Token.SKIP ) {");
                        println("\t_token = makeToken(_ttype);");
                        println("\t_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));");
                        println("}");
                        println("_returnToken = _token;");
                    }
                    if (block.returnAction != null) {
                        println("return " + extractIdOfAction(block.returnAction, block.getLine(), block.getColumn()) + ";");
                    }
                    if (!this.grammar.debuggingOutput || this.grammar.traceRules) {
                        this.tabs--;
                        println("} finally { // debugging");
                        this.tabs++;
                        if (this.grammar.debuggingOutput) {
                            if (this.grammar instanceof ParserGrammar) {
                                sb2 = new StringBuilder();
                                sb2.append("fireExitRule(");
                                sb2.append(i);
                                sb2.append(",0);");
                            } else if (this.grammar instanceof LexerGrammar) {
                                sb2 = new StringBuilder();
                                sb2.append("fireExitRule(");
                                sb2.append(i);
                                sb2.append(",_ttype);");
                            }
                            println(sb2.toString());
                        }
                        if (this.grammar.traceRules) {
                            if (this.grammar instanceof TreeWalkerGrammar) {
                                sb = new StringBuilder();
                                sb.append("traceOut(\"");
                                sb.append(ruleSymbol.getId());
                                sb.append("\",_t);");
                            } else {
                                sb = new StringBuilder();
                                sb.append("traceOut(\"");
                                sb.append(ruleSymbol.getId());
                                sb.append("\");");
                            }
                            println(sb.toString());
                        }
                        i4 = 1;
                        this.tabs--;
                        println("}");
                    } else {
                        i4 = 1;
                    }
                    this.tabs -= i4;
                    println("}");
                    println(str2);
                    this.genAST = z2;
                    this.defaultLine = i3;
                    return;
                } catch (Throwable th) {
                    th = th;
                    i2 = i3;
                }
            }
            _print(str5);
            if (block.throwsSpec != null) {
            }
            _println(" {");
            this.tabs++;
            if (block.returnAction != null) {
            }
            println(this.commonLocalVars);
            if (this.grammar.traceRules) {
            }
            if (this.grammar instanceof LexerGrammar) {
            }
            if (this.grammar.debuggingOutput) {
            }
            if (!this.grammar.debuggingOutput) {
                println("try { // debugging");
                this.tabs++;
                if (this.grammar instanceof TreeWalkerGrammar) {
                }
                if (this.grammar.buildAST) {
                }
                genBlockPreamble(block);
                genBlockInitAction(block);
                println("");
                exceptionSpecFindExceptionSpec = block.findExceptionSpec("");
                if (exceptionSpecFindExceptionSpec == null) {
                    println("try {      // for error handling");
                    this.tabs++;
                    if (block.alternatives.size() != 1) {
                    }
                    if (exceptionSpecFindExceptionSpec == null) {
                        this.tabs--;
                        println("}");
                    }
                    if (exceptionSpecFindExceptionSpec == null) {
                    }
                    if (this.grammar.buildAST) {
                    }
                    if (this.grammar instanceof TreeWalkerGrammar) {
                    }
                    if (block.getTestLiterals()) {
                    }
                    if (this.grammar instanceof LexerGrammar) {
                    }
                    if (block.returnAction != null) {
                    }
                    if (this.grammar.debuggingOutput) {
                        this.tabs--;
                        println("} finally { // debugging");
                        this.tabs++;
                        if (this.grammar.debuggingOutput) {
                        }
                        if (this.grammar.traceRules) {
                        }
                        i4 = 1;
                        this.tabs--;
                        println("}");
                    }
                    this.tabs -= i4;
                    println("}");
                    println(str2);
                    this.genAST = z2;
                    this.defaultLine = i3;
                    return;
                }
            }
        } catch (Throwable th2) {
            th = th2;
            i2 = i5;
        }
        this.defaultLine = i2;
        throw th;
    }

    public void genSemPred(String str, int i) {
        String strProcessActionForSpecialSymbols = processActionForSpecialSymbols(str, i, this.currentRule, new ActionTransInfo());
        String strEscapeString = this.charFormatter.escapeString(strProcessActionForSpecialSymbols);
        Grammar grammar = this.grammar;
        if (grammar.debuggingOutput && ((grammar instanceof ParserGrammar) || (grammar instanceof LexerGrammar))) {
            StringBuilder sbM5a = C0000a.m5a("fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.VALIDATING,");
            sbM5a.append(addSemPred(strEscapeString));
            sbM5a.append(",");
            sbM5a.append(strProcessActionForSpecialSymbols);
            sbM5a.append(")");
            strProcessActionForSpecialSymbols = sbM5a.toString();
        }
        println("if (!(" + strProcessActionForSpecialSymbols + "))", i);
        println("  throw new SemanticException(\"" + strEscapeString + "\");", i);
    }

    public void genSemPredMap() {
        Enumeration enumerationElements = this.semPreds.elements();
        String string = "private String _semPredNames[] = {";
        while (true) {
            println(string, NO_MAPPING);
            if (!enumerationElements.hasMoreElements()) {
                println("};", NO_MAPPING);
                return;
            }
            StringBuilder sbM5a = C0000a.m5a("\"");
            sbM5a.append(enumerationElements.nextElement());
            sbM5a.append("\",");
            string = sbM5a.toString();
        }
    }

    public void genSynPred(SynPredBlock synPredBlock, String str) {
        StringBuilder sb;
        StringBuilder sb2;
        int i = this.defaultLine;
        try {
            this.defaultLine = synPredBlock.getLine();
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("gen=>(" + synPredBlock + ")");
            }
            println("boolean synPredMatched" + synPredBlock.f302ID + " = false;");
            if (this.grammar instanceof TreeWalkerGrammar) {
                println("if (_t==null) _t=ASTNULL;");
            }
            println("if (" + str + ") {");
            this.tabs = this.tabs + 1;
            if (this.grammar instanceof TreeWalkerGrammar) {
                sb = new StringBuilder();
                sb.append("AST __t");
                sb.append(synPredBlock.f302ID);
                sb.append(" = _t;");
            } else {
                sb = new StringBuilder();
                sb.append("int _m");
                sb.append(synPredBlock.f302ID);
                sb.append(" = mark();");
            }
            println(sb.toString());
            println("synPredMatched" + synPredBlock.f302ID + " = true;");
            println("inputState.guessing++;");
            if (this.grammar.debuggingOutput && ((this.grammar instanceof ParserGrammar) || (this.grammar instanceof LexerGrammar))) {
                println("fireSyntacticPredicateStarted();");
            }
            this.syntacticPredLevel++;
            println("try {");
            this.tabs++;
            gen(synPredBlock);
            this.tabs--;
            println("}");
            println("catch (" + this.exceptionThrown + " pe) {");
            this.tabs = this.tabs + 1;
            println("synPredMatched" + synPredBlock.f302ID + " = false;");
            this.tabs = this.tabs + (-1);
            println("}");
            if (this.grammar instanceof TreeWalkerGrammar) {
                sb2 = new StringBuilder();
                sb2.append("_t = __t");
                sb2.append(synPredBlock.f302ID);
                sb2.append(";");
            } else {
                sb2 = new StringBuilder();
                sb2.append("rewind(_m");
                sb2.append(synPredBlock.f302ID);
                sb2.append(");");
            }
            println(sb2.toString());
            _println("inputState.guessing--;");
            if (this.grammar.debuggingOutput && ((this.grammar instanceof ParserGrammar) || (this.grammar instanceof LexerGrammar))) {
                println("if (synPredMatched" + synPredBlock.f302ID + ")");
                println("  fireSyntacticPredicateSucceeded();");
                println("else");
                println("  fireSyntacticPredicateFailed();");
            }
            this.syntacticPredLevel--;
            this.tabs--;
            println("}");
            println("if ( synPredMatched" + synPredBlock.f302ID + " ) {");
        } finally {
            this.defaultLine = i;
        }
    }

    public void genTokenASTNodeMap() {
        TokenSymbol tokenSymbol;
        int i = this.defaultLine;
        try {
            this.defaultLine = NO_MAPPING;
            println("");
            println("protected void buildTokenTypeASTClassMap() {");
            this.tabs++;
            Vector vocabulary = this.grammar.tokenManager.getVocabulary();
            int i2 = 0;
            boolean z = false;
            for (int i3 = 0; i3 < vocabulary.size(); i3++) {
                String str = (String) vocabulary.elementAt(i3);
                if (str != null && (tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str)) != null && tokenSymbol.getASTNodeType() != null) {
                    i2++;
                    if (!z) {
                        println("tokenTypeToASTClassMap = new Hashtable();");
                        z = true;
                    }
                    println("tokenTypeToASTClassMap.put(new Integer(" + tokenSymbol.getTokenType() + "), " + tokenSymbol.getASTNodeType() + ".class);");
                }
            }
            if (i2 == 0) {
                println("tokenTypeToASTClassMap=null;");
            }
            this.tabs--;
            println("};");
        } finally {
            this.defaultLine = i;
        }
    }

    public void genTokenStrings() {
        TokenSymbol tokenSymbol;
        int i = this.defaultLine;
        try {
            this.defaultLine = NO_MAPPING;
            println("");
            println("public static final String[] _tokenNames = {");
            this.tabs++;
            Vector vocabulary = this.grammar.tokenManager.getVocabulary();
            for (int i2 = 0; i2 < vocabulary.size(); i2++) {
                String strStripFrontBack = (String) vocabulary.elementAt(i2);
                if (strStripFrontBack == null) {
                    strStripFrontBack = "<" + String.valueOf(i2) + ">";
                }
                if (!strStripFrontBack.startsWith("\"") && !strStripFrontBack.startsWith("<") && (tokenSymbol = this.grammar.tokenManager.getTokenSymbol(strStripFrontBack)) != null && tokenSymbol.getParaphrase() != null) {
                    strStripFrontBack = StringUtils.stripFrontBack(tokenSymbol.getParaphrase(), "\"", "\"");
                }
                print(this.charFormatter.literalString(strStripFrontBack));
                if (i2 != vocabulary.size() - 1) {
                    _print(",");
                }
                _println("");
            }
            this.tabs--;
            println("};");
        } finally {
            this.defaultLine = i;
        }
    }

    public void genTokenTypes(TokenManager tokenManager) {
        StringBuilder sb;
        String string;
        int i = this.defaultLine;
        try {
            this.defaultLine = NO_MAPPING;
            this.currentOutput = getPrintWriterManager().setupOutput(this.antlrTool, tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
            this.tabs = 0;
            genHeader();
            try {
                this.defaultLine = this.behavior.getHeaderActionLine("");
                println(this.behavior.getHeaderAction(""));
                this.defaultLine = NO_MAPPING;
                println("public interface " + tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix + " {");
                this.tabs = this.tabs + 1;
                Vector vocabulary = tokenManager.getVocabulary();
                println("int EOF = 1;");
                println("int NULL_TREE_LOOKAHEAD = 3;");
                for (int i2 = 4; i2 < vocabulary.size(); i2++) {
                    String str = (String) vocabulary.elementAt(i2);
                    if (str != null) {
                        if (str.startsWith("\"")) {
                            StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenManager.getTokenSymbol(str);
                            if (stringLiteralSymbol == null) {
                                this.antlrTool.panic("String literal " + str + " not in symbol table");
                            } else if (stringLiteralSymbol.label != null) {
                                string = "int " + stringLiteralSymbol.label + " = " + i2 + ";";
                            } else {
                                String strMangleLiteral = mangleLiteral(str);
                                if (strMangleLiteral != null) {
                                    println("int " + strMangleLiteral + " = " + i2 + ";");
                                    stringLiteralSymbol.label = strMangleLiteral;
                                } else {
                                    sb = new StringBuilder();
                                    sb.append("// ");
                                    sb.append(str);
                                    sb.append(" = ");
                                    sb.append(i2);
                                    string = sb.toString();
                                }
                            }
                        } else if (!str.startsWith("<")) {
                            sb = new StringBuilder();
                            sb.append("int ");
                            sb.append(str);
                            sb.append(" = ");
                            sb.append(i2);
                            sb.append(";");
                            string = sb.toString();
                        }
                        println(string);
                    }
                }
                this.tabs--;
                println("}");
                getPrintWriterManager().finishOutput();
                exitIfError();
            } catch (Throwable th) {
                this.defaultLine = NO_MAPPING;
                throw th;
            }
        } finally {
            this.defaultLine = i;
        }
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(GrammarAtom grammarAtom, String str) {
        if (grammarAtom == null || grammarAtom.getASTNodeType() == null) {
            return getASTCreateString(str);
        }
        StringBuilder sbM5a = C0000a.m5a("(");
        sbM5a.append(grammarAtom.getASTNodeType());
        sbM5a.append(")astFactory.create(");
        sbM5a.append(str);
        sbM5a.append(",\"");
        sbM5a.append(grammarAtom.getASTNodeType());
        sbM5a.append("\")");
        return sbM5a.toString();
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(Vector vector) {
        if (vector.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder sbM5a = C0000a.m5a("(");
        sbM5a.append(this.labeledElementASTType);
        sbM5a.append(")astFactory.make( (new ASTArray(");
        sbM5a.append(vector.size());
        sbM5a.append("))");
        stringBuffer.append(sbM5a.toString());
        for (int i = 0; i < vector.size(); i++) {
            StringBuilder sbM5a2 = C0000a.m5a(".add(");
            sbM5a2.append(vector.elementAt(i));
            sbM5a2.append(")");
            stringBuffer.append(sbM5a2.toString());
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
            StringBuilder sbM5a = C0000a.m5a("(");
            sbM5a.append(this.labeledElementASTType);
            sbM5a.append(")astFactory.create(");
            sbM5a.append(str);
            sbM5a.append(")");
            return sbM5a.toString();
        }
        int iIndexOf = str.indexOf(44);
        str.lastIndexOf(44);
        TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(i > 0 ? str.substring(0, iIndexOf) : str);
        if (tokenSymbol != null) {
            String aSTNodeType = tokenSymbol.getASTNodeType();
            String str2 = i == 0 ? ",\"\"" : "";
            if (aSTNodeType != null) {
                return "(" + aSTNodeType + ")astFactory.create(" + str + str2 + ",\"" + aSTNodeType + "\")";
            }
        }
        if (this.labeledElementASTType.equals("AST")) {
            return C0000a.m2a("astFactory.create(", str, ")");
        }
        StringBuilder sbM5a2 = C0000a.m5a("(");
        sbM5a2.append(this.labeledElementASTType);
        sbM5a2.append(")astFactory.create(");
        sbM5a2.append(str);
        sbM5a2.append(")");
        return sbM5a2.toString();
    }

    public String getLookaheadTestExpression(Alternative alternative, int i) {
        int i2 = alternative.lookaheadDepth;
        if (i2 == Integer.MAX_VALUE) {
            i2 = this.grammar.maxk;
        }
        return i == 0 ? "( true )" : C0000a.m3a(C0000a.m5a("("), getLookaheadTestExpression(alternative.cache, i2), ")");
    }

    public String getLookaheadTestExpression(Lookahead[] lookaheadArr, int i) {
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append("(");
        boolean z = true;
        for (int i2 = 1; i2 <= i; i2++) {
            BitSet bitSet = lookaheadArr[i2].fset;
            if (!z) {
                stringBuffer.append(") && (");
            }
            z = false;
            stringBuffer.append(lookaheadArr[i2].containsEpsilon() ? "true" : getLookaheadTestTerm(i2, bitSet));
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public String getLookaheadTestTerm(int i, BitSet bitSet) {
        String strLookaheadString = lookaheadString(i);
        int[] array = bitSet.toArray();
        if (CodeGenerator.elementsAreRange(array)) {
            return getRangeExpression(i, array);
        }
        int iDegree = bitSet.degree();
        if (iDegree == 0) {
            return "true";
        }
        if (iDegree >= this.bitsetTestThreshold) {
            return getBitsetName(markBitsetForGen(bitSet)) + ".member(" + strLookaheadString + ")";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < array.length; i2++) {
            String valueString = getValueString(array[i2]);
            if (i2 > 0) {
                stringBuffer.append("||");
            }
            stringBuffer.append(strLookaheadString);
            stringBuffer.append("==");
            stringBuffer.append(valueString);
        }
        return stringBuffer.toString();
    }

    public JavaCodeGeneratorPrintWriterManager getPrintWriterManager() {
        if (this.printWriterManager == null) {
            this.printWriterManager = new DefaultJavaCodeGeneratorPrintWriterManager();
        }
        return this.printWriterManager;
    }

    public String getRangeExpression(int i, int[] iArr) {
        if (!CodeGenerator.elementsAreRange(iArr)) {
            this.antlrTool.panic("getRangeExpression called with non-range");
        }
        int i2 = iArr[0];
        int i3 = iArr[iArr.length - 1];
        StringBuilder sbM5a = C0000a.m5a("(");
        sbM5a.append(lookaheadString(i));
        sbM5a.append(" >= ");
        sbM5a.append(getValueString(i2));
        sbM5a.append(" && ");
        sbM5a.append(lookaheadString(i));
        sbM5a.append(" <= ");
        return C0000a.m3a(sbM5a, getValueString(i3), ")");
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

    /* JADX WARN: Removed duplicated region for block: B:15:0x0030  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String mapTreeId(String str, ActionTransInfo actionTransInfo) {
        if (this.currentRule == null) {
            return str;
        }
        Grammar grammar = this.grammar;
        boolean z = true;
        if (!(grammar instanceof TreeWalkerGrammar)) {
            z = false;
        } else if (grammar.buildAST) {
            if (str.length() > 3 && str.lastIndexOf("_in") == str.length() - 3) {
                str = str.substring(0, str.length() - 3);
            }
        }
        for (int i = 0; i < this.currentRule.labeledElements.size(); i++) {
            if (((AlternativeElement) this.currentRule.labeledElements.elementAt(i)).getLabel().equals(str)) {
                return z ? str : C0000a.m1a(str, "_AST");
            }
        }
        String str2 = (String) this.treeVariableMap.get(str);
        if (str2 == null) {
            if (!str.equals(this.currentRule.getRuleName())) {
                return str;
            }
            String strM1a = z ? C0000a.m1a(str, "_AST_in") : C0000a.m1a(str, "_AST");
            if (actionTransInfo != null && !z) {
                actionTransInfo.refRuleRoot = strM1a;
            }
            return strM1a;
        }
        if (str2 != NONUNIQUE && !str2.equals(this.currentRule.getRuleName())) {
            return z ? C0000a.m1a(str2, "_in") : str2;
        }
        Tool tool = this.antlrTool;
        StringBuilder sbM10b = C0000a.m10b("Ambiguous reference to AST element ", str, " in rule ");
        sbM10b.append(this.currentRule.getRuleName());
        tool.error(sbM10b.toString());
        return null;
    }

    @Override // antlr.CodeGenerator
    public void print(String str) {
        print(str, this.defaultLine);
    }

    public void print(String str, int i) {
        if (i > 0 || i == -888) {
            getPrintWriterManager().startMapping(i);
        }
        super.print(str);
        if (i > 0 || i == -888) {
            getPrintWriterManager().endMapping();
        }
    }

    @Override // antlr.CodeGenerator
    public void printAction(String str) {
        printAction(str, this.defaultLine);
    }

    public void printAction(String str, int i) {
        getPrintWriterManager().startMapping(i);
        super.printAction(str);
        getPrintWriterManager().endMapping();
    }

    @Override // antlr.CodeGenerator
    public void println(String str) {
        println(str, this.defaultLine);
    }

    public void println(String str, int i) {
        if (i > 0 || i == -888) {
            getPrintWriterManager().startSingleSourceLineMapping(i);
        }
        super.println(str);
        if (i > 0 || i == -888) {
            getPrintWriterManager().endMapping();
        }
    }

    @Override // antlr.CodeGenerator
    public String processActionForSpecialSymbols(String str, int i, RuleBlock ruleBlock, ActionTransInfo actionTransInfo) {
        Tool tool;
        StringBuilder sb;
        if (str == null || str.length() == 0) {
            return null;
        }
        Grammar grammar = this.grammar;
        if (grammar == null) {
            return str;
        }
        if (!grammar.buildAST || str.indexOf(35) == -1) {
            Grammar grammar2 = this.grammar;
            if (!(grammar2 instanceof TreeWalkerGrammar) && ((!(grammar2 instanceof LexerGrammar) && !(grammar2 instanceof ParserGrammar)) || str.indexOf(36) == -1)) {
                return str;
            }
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

    public void setPrintWriterManager(JavaCodeGeneratorPrintWriterManager javaCodeGeneratorPrintWriterManager) {
        this.printWriterManager = javaCodeGeneratorPrintWriterManager;
    }

    @Override // antlr.CodeGenerator
    public void setTool(Tool tool) {
        super.setTool(tool);
    }
}
