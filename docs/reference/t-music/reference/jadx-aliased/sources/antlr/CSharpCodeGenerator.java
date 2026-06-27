package antlr;

import antlr.actions.csharp.ActionLexer;
import antlr.collections.impl.BitSet;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class CSharpCodeGenerator extends CodeGenerator {
    public static final int caseSizeThreshold = 127;
    public Vector astTypes;
    public int blockNestingLevel;
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
    public int saveIndexCreateLevel;
    public antlr.collections.impl.Vector semPreds;
    public String throwNoViable;
    public static final String NONUNIQUE = new String();
    public static CSharpNameSpace nameSpace = null;
    public int syntacticPredLevel = 0;
    public boolean genAST = false;
    public boolean saveText = false;
    public boolean usingCustomAST = false;
    public Hashtable treeVariableMap = new Hashtable();
    public Hashtable declaredASTVariables = new Hashtable();
    public int astVarNumber = 1;

    public CSharpCodeGenerator() {
        this.charFormatter = new CSharpCharFormatter();
    }

    private void GenRuleInvocation(RuleRefElement ruleRefElement) {
        Tool tool;
        String sb;
        _print(ruleRefElement.targetRule + "(");
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
            String processActionForSpecialSymbols = processActionForSpecialSymbols(ruleRefElement.args, 0, this.currentRule, actionTransInfo);
            if (actionTransInfo.assignToRoot || actionTransInfo.refRuleRoot != null) {
                Tool tool2 = this.antlrTool;
                StringBuilder m5a = C0000a.m5a("Arguments of rule reference '");
                m5a.append(ruleRefElement.targetRule);
                m5a.append("' cannot set or ref #");
                m5a.append(this.currentRule.getRuleName());
                tool2.error(m5a.toString(), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
            }
            _print(processActionForSpecialSymbols);
            if (ruleSymbol.block.argAction == null) {
                tool = this.antlrTool;
                sb = C0000a.m3a(C0000a.m5a("Rule '"), ruleRefElement.targetRule, "' accepts no arguments");
                tool.warning(sb, this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
            }
        } else if (ruleSymbol.block.argAction != null) {
            tool = this.antlrTool;
            StringBuilder m5a2 = C0000a.m5a("Missing parameters on reference to rule ");
            m5a2.append(ruleRefElement.targetRule);
            sb = m5a2.toString();
            tool.warning(sb, this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
        }
        _println(");");
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = retTree_;");
        }
    }

    public static String OctalToUnicode(String str) {
        if (4 > str.length() || '\'' != str.charAt(0) || '\\' != str.charAt(1) || '0' > str.charAt(2) || '7' < str.charAt(2) || '\'' != str.charAt(str.length() - 1)) {
            return str;
        }
        Integer valueOf = Integer.valueOf(str.substring(2, str.length() - 1), 8);
        StringBuilder m5a = C0000a.m5a("'\\x");
        m5a.append(Integer.toHexString(valueOf.intValue()));
        m5a.append("'");
        return m5a.toString();
    }

    private void declareSaveIndexVariableIfNeeded() {
        if (this.saveIndexCreateLevel == 0) {
            println("int _saveIndex = 0;");
            this.saveIndexCreateLevel = this.blockNestingLevel;
        }
    }

    private void genBitSet(BitSet bitSet, int i) {
        int i2;
        StringBuilder sb;
        String str;
        StringBuilder m5a = C0000a.m5a("private static long[] mk_");
        m5a.append(getBitsetName(i));
        m5a.append("()");
        println(m5a.toString());
        println("{");
        this.tabs++;
        int lengthInLongWords = bitSet.lengthInLongWords();
        if (lengthInLongWords < 8) {
            StringBuilder m5a2 = C0000a.m5a("long[] data = { ");
            m5a2.append(bitSet.toStringOfWords());
            m5a2.append("};");
            println(m5a2.toString());
        } else {
            println("long[] data = new long[" + lengthInLongWords + "];");
            long[] packedArray = bitSet.toPackedArray();
            for (int i3 = 0; i3 < packedArray.length; i3 = i2) {
                i2 = i3 + 1;
                if (i2 == packedArray.length || packedArray[i3] != packedArray[i2]) {
                    sb = new StringBuilder();
                    sb.append("data[");
                    sb.append(i3);
                    sb.append("]=");
                    sb.append(packedArray[i3]);
                    str = "L;";
                } else {
                    while (i2 < packedArray.length && packedArray[i2] == packedArray[i3]) {
                        i2++;
                    }
                    sb = new StringBuilder();
                    sb.append("for (int i = ");
                    sb.append(i3);
                    sb.append("; i<=");
                    sb.append(i2 - 1);
                    sb.append("; i++) { data[i]=");
                    sb.append(packedArray[i3]);
                    str = "L; }";
                }
                sb.append(str);
                println(sb.toString());
            }
        }
        println("return data;");
        this.tabs--;
        println("}");
        println("public static readonly BitSet " + getBitsetName(i) + " = new BitSet(mk_" + getBitsetName(i) + "());");
    }

    private void genBlockFinish(CSharpBlockFinishingInfo cSharpBlockFinishingInfo, String str) {
        String str2;
        if (cSharpBlockFinishingInfo.needAnErrorClause && (cSharpBlockFinishingInfo.generatedAnIf || cSharpBlockFinishingInfo.generatedSwitch)) {
            if (cSharpBlockFinishingInfo.generatedAnIf) {
                println("else");
            }
            println("{");
            this.tabs++;
            println(str);
            this.tabs--;
            println("}");
        }
        if (cSharpBlockFinishingInfo.postscript != null) {
            if (cSharpBlockFinishingInfo.needAnErrorClause && cSharpBlockFinishingInfo.generatedSwitch && !cSharpBlockFinishingInfo.generatedAnIf && str != null && (str.indexOf("throw") == 0 || str.indexOf("goto") == 0)) {
                str2 = cSharpBlockFinishingInfo.postscript.substring(cSharpBlockFinishingInfo.postscript.indexOf("break;") + 6);
            } else {
                str2 = cSharpBlockFinishingInfo.postscript;
            }
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
                StringBuilder sb4 = new StringBuilder();
                sb4.append(this.labeledElementASTType);
                sb4.append(" ");
                sb4.append(sb3);
                sb4.append("_in = ");
                C0000a.m7a(sb4, str4, ";", this);
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
                println(this.labeledElementASTType + " " + m1a + "_in = null;");
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
                m9b2.append(";");
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
                m9b.append(";");
                println(m9b.toString());
                if (this.grammar instanceof TreeWalkerGrammar) {
                    println(m1a + "_in = " + str5 + ";");
                }
            }
            if (this.genAST) {
                int autoGenType = alternativeElement.getAutoGenType();
                if (autoGenType != 1) {
                    if (autoGenType != 2) {
                        return;
                    }
                    if (this.usingCustomAST || ((alternativeElement instanceof GrammarAtom) && ((GrammarAtom) alternativeElement).getASTNodeType() != null)) {
                        sb2 = new StringBuilder();
                        str2 = "astFactory.makeASTRoot(ref currentAST, (AST)";
                    } else {
                        sb2 = new StringBuilder();
                        str2 = "astFactory.makeASTRoot(ref currentAST, ";
                    }
                } else if (this.usingCustomAST || ((alternativeElement instanceof GrammarAtom) && ((GrammarAtom) alternativeElement).getASTNodeType() != null)) {
                    sb2 = new StringBuilder();
                    str2 = "astFactory.addASTChild(ref currentAST, (AST)";
                } else {
                    sb2 = new StringBuilder();
                    str2 = "astFactory.addASTChild(ref currentAST, ";
                }
                sb2.append(str2);
                sb2.append(m1a);
                sb2.append(");");
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
            println("}");
            genErrorHandler(findExceptionSpec);
        }
    }

    private void genErrorHandler(ExceptionSpec exceptionSpec) {
        for (int i = 0; i < exceptionSpec.handlers.size(); i++) {
            ExceptionHandler exceptionHandler = (ExceptionHandler) exceptionSpec.handlers.elementAt(i);
            StringBuilder m5a = C0000a.m5a("catch (");
            m5a.append(exceptionHandler.exceptionTypeAndName.getText());
            m5a.append(")");
            println(m5a.toString());
            println("{");
            this.tabs++;
            if (this.grammar.hasSyntacticPredicate) {
                println("if (0 == inputState.guessing)");
                println("{");
                this.tabs++;
            }
            printAction(processActionForSpecialSymbols(exceptionHandler.action.getText(), exceptionHandler.action.getLine(), this.currentRule, new ActionTransInfo()));
            if (this.grammar.hasSyntacticPredicate) {
                this.tabs--;
                println("}");
                println("else");
                println("{");
                this.tabs++;
                println("throw;");
                this.tabs--;
                println("}");
            }
            this.tabs--;
            println("}");
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
            println("try   // for error handling");
            println("{");
            this.tabs++;
        }
    }

    private void genLiteralsTest() {
        println("_ttype = testLiteralsTable(_ttype);");
    }

    private void genLiteralsTestForPartialToken() {
        println("_ttype = testLiteralsTable(text.ToString(_begin, text.Length-_begin), _ttype);");
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
        String mangleLiteral = mangleLiteral(id);
        return mangleLiteral == null ? String.valueOf(i) : mangleLiteral;
    }

    private String lookaheadString(int i) {
        Grammar grammar = this.grammar;
        if (grammar instanceof TreeWalkerGrammar) {
            return "_t.Type";
        }
        if (grammar instanceof LexerGrammar) {
            if (i == 1) {
                return "cached_LA1";
            }
            if (i == 2) {
                return "cached_LA2";
            }
        }
        return "LA(" + i + ")";
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
        String str;
        Token option2;
        String stripFrontBack;
        Token option3;
        String stripFrontBack2;
        boolean z = grammar instanceof ParserGrammar;
        if (z || (grammar instanceof LexerGrammar) || (grammar instanceof TreeWalkerGrammar)) {
            NameSpace nameSpace2 = this.antlrTool.nameSpace;
            if (nameSpace2 != null) {
                nameSpace = new CSharpNameSpace(nameSpace2.getName());
            }
            if (grammar.hasOption("namespace") && (option = grammar.getOption("namespace")) != null) {
                nameSpace = new CSharpNameSpace(option.getText());
            }
        }
        if (z) {
            this.labeledElementASTType = "AST";
            if (grammar.hasOption("ASTLabelType") && (option3 = grammar.getOption("ASTLabelType")) != null && (stripFrontBack2 = StringUtils.stripFrontBack(option3.getText(), "\"", "\"")) != null) {
                this.usingCustomAST = true;
                this.labeledElementASTType = stripFrontBack2;
            }
            this.labeledElementType = "IToken ";
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
            this.commonExtraParams = "bool _createToken";
            this.commonLocalVars = "int _ttype; IToken _token=null; int _begin=text.Length;";
            this.lt1Value = "cached_LA1";
            this.exceptionThrown = "RecognitionException";
            str = "throw new NoViableAltForCharException(cached_LA1, getFilename(), getLine(), getColumn());";
        } else {
            if (!(grammar instanceof TreeWalkerGrammar)) {
                this.antlrTool.panic("Unknown grammar type");
                return;
            }
            this.labeledElementASTType = "AST";
            this.labeledElementType = "AST";
            if (grammar.hasOption("ASTLabelType") && (option2 = grammar.getOption("ASTLabelType")) != null && (stripFrontBack = StringUtils.stripFrontBack(option2.getText(), "\"", "\"")) != null) {
                this.usingCustomAST = true;
                this.labeledElementASTType = stripFrontBack;
                this.labeledElementType = stripFrontBack;
            }
            if (!grammar.hasOption("ASTLabelType")) {
                grammar.setOption("ASTLabelType", new Token(6, "AST"));
            }
            this.labeledElementInit = "null";
            this.commonExtraArgs = "_t";
            this.commonExtraParams = "AST _t";
            this.commonLocalVars = "";
            this.lt1Value = this.usingCustomAST ? C0000a.m3a(C0000a.m5a("(_t==ASTNULL) ? null : ("), this.labeledElementASTType, ")_t") : "_t";
            this.exceptionThrown = "RecognitionException";
            str = "throw new NoViableAltException(_t);";
        }
        this.throwNoViable = str;
    }

    public static boolean suitableForCaseExpression(Alternative alternative) {
        return alternative.lookaheadDepth == 1 && alternative.semPred == null && !alternative.cache[1].containsEpsilon() && alternative.cache[1].fset.degree() <= 127;
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
            Enumeration elements2 = this.behavior.tokenManagers.elements();
            while (elements2.hasMoreElements()) {
                TokenManager tokenManager = (TokenManager) elements2.nextElement();
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
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genAction(" + actionElement + ")");
        }
        if (actionElement.isSemPred) {
            genSemPred(actionElement.actionText, actionElement.line);
            return;
        }
        if (this.grammar.hasSyntacticPredicate) {
            println("if (0==inputState.guessing)");
            println("{");
            this.tabs++;
        }
        ActionTransInfo actionTransInfo = new ActionTransInfo();
        String processActionForSpecialSymbols = processActionForSpecialSymbols(actionElement.actionText, actionElement.getLine(), this.currentRule, actionTransInfo);
        if (actionTransInfo.refRuleRoot != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(actionTransInfo.refRuleRoot);
            sb.append(" = (");
            C0000a.m7a(sb, this.labeledElementASTType, ")currentAST.root;", this);
        }
        printAction(processActionForSpecialSymbols);
        if (actionTransInfo.assignToRoot) {
            StringBuilder m5a = C0000a.m5a("currentAST.root = ");
            m5a.append(actionTransInfo.refRuleRoot);
            m5a.append(";");
            println(m5a.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("if ( (null != ");
            sb2.append(actionTransInfo.refRuleRoot);
            sb2.append(") && (null != ");
            C0000a.m7a(sb2, actionTransInfo.refRuleRoot, ".getFirstChild()) )", this);
            this.tabs++;
            C0000a.m7a(C0000a.m5a("currentAST.child = "), actionTransInfo.refRuleRoot, ".getFirstChild();", this);
            this.tabs--;
            println("else");
            this.tabs++;
            C0000a.m7a(C0000a.m5a("currentAST.child = "), actionTransInfo.refRuleRoot, ";", this);
            this.tabs--;
            println("currentAST.advanceChildToEnd();");
        }
        if (this.grammar.hasSyntacticPredicate) {
            this.tabs--;
            println("}");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(AlternativeBlock alternativeBlock) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("gen(" + alternativeBlock + ")");
        }
        println("{");
        this.tabs++;
        genBlockPreamble(alternativeBlock);
        genBlockInitAction(alternativeBlock);
        String str = this.currentASTResult;
        if (alternativeBlock.getLabel() != null) {
            this.currentASTResult = alternativeBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(alternativeBlock);
        genBlockFinish(genCommonBlock(alternativeBlock, true), this.throwNoViable);
        this.tabs--;
        println("}");
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
            C0000a.m7a(sb, this.lt1Value, ";", this);
        }
        boolean z = this.saveText;
        this.saveText = z && charLiteralElement.getAutoGenType() == 1;
        genMatch(charLiteralElement);
        this.saveText = z;
    }

    @Override // antlr.CodeGenerator
    public void gen(CharRangeElement charRangeElement) {
        if (charRangeElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(charRangeElement.getLabel());
            sb.append(" = ");
            C0000a.m7a(sb, this.lt1Value, ";", this);
        }
        boolean z = (this.grammar instanceof LexerGrammar) && (!this.saveText || charRangeElement.getAutoGenType() == 3);
        if (z) {
            println("_saveIndex = text.Length;");
        }
        StringBuilder m5a = C0000a.m5a("matchRange(");
        m5a.append(OctalToUnicode(charRangeElement.beginText));
        m5a.append(",");
        m5a.append(OctalToUnicode(charRangeElement.endText));
        m5a.append(");");
        println(m5a.toString());
        if (z) {
            println("text.Length = _saveIndex;");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(LexerGrammar lexerGrammar) {
        if (lexerGrammar.debuggingOutput) {
            this.semPreds = new antlr.collections.impl.Vector();
        }
        setGrammar(lexerGrammar);
        if (!(this.grammar instanceof LexerGrammar)) {
            this.antlrTool.panic("Internal error generating lexer");
        }
        genBody(lexerGrammar);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01a7  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(OneOrMoreBlock oneOrMoreBlock) {
        StringBuilder m5a;
        String sb;
        int i;
        boolean z;
        int i2;
        int i3;
        int i4;
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("gen+(" + oneOrMoreBlock + ")");
        }
        println("{ // ( ... )+");
        this.tabs++;
        this.blockNestingLevel++;
        genBlockPreamble(oneOrMoreBlock);
        if (oneOrMoreBlock.getLabel() != null) {
            m5a = C0000a.m5a("_cnt_");
            m5a.append(oneOrMoreBlock.getLabel());
        } else {
            m5a = C0000a.m5a("_cnt");
            m5a.append(oneOrMoreBlock.f302ID);
        }
        String sb2 = m5a.toString();
        println("int " + sb2 + "=0;");
        if (oneOrMoreBlock.getLabel() != null) {
            sb = oneOrMoreBlock.getLabel();
        } else {
            StringBuilder m5a2 = C0000a.m5a("_loop");
            m5a2.append(oneOrMoreBlock.f302ID);
            sb = m5a2.toString();
        }
        println("for (;;)");
        println("{");
        this.tabs++;
        this.blockNestingLevel++;
        genBlockInitAction(oneOrMoreBlock);
        String str = this.currentASTResult;
        if (oneOrMoreBlock.getLabel() != null) {
            this.currentASTResult = oneOrMoreBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(oneOrMoreBlock);
        int i5 = this.grammar.maxk;
        if (!oneOrMoreBlock.greedy && (i4 = oneOrMoreBlock.exitLookaheadDepth) <= i5 && oneOrMoreBlock.exitCache[i4].containsEpsilon()) {
            i5 = oneOrMoreBlock.exitLookaheadDepth;
        } else if (oneOrMoreBlock.greedy || oneOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
            i = i5;
            z = false;
            if (z) {
                if (this.DEBUG_CODE_GENERATOR) {
                    PrintStream printStream = System.out;
                    StringBuilder m5a3 = C0000a.m5a("nongreedy (...)+ loop; exit depth is ");
                    m5a3.append(oneOrMoreBlock.exitLookaheadDepth);
                    printStream.println(m5a3.toString());
                }
                String lookaheadTestExpression = getLookaheadTestExpression(oneOrMoreBlock.exitCache, i);
                println("// nongreedy exit test");
                println("if ((" + sb2 + " >= 1) && " + lookaheadTestExpression + ") goto " + sb + "_breakloop;");
            }
            CSharpBlockFinishingInfo genCommonBlock = genCommonBlock(oneOrMoreBlock, false);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("if (");
            sb3.append(sb2);
            sb3.append(" >= 1) { goto ");
            sb3.append(sb);
            sb3.append("_breakloop; } else { ");
            genBlockFinish(genCommonBlock, C0000a.m3a(sb3, this.throwNoViable, "; }"));
            println(sb2 + "++;");
            this.tabs = this.tabs - 1;
            i2 = this.blockNestingLevel;
            this.blockNestingLevel = i2 + (-1);
            if (i2 == this.saveIndexCreateLevel) {
                this.saveIndexCreateLevel = 0;
            }
            println("}");
            _print(sb + "_breakloop:");
            println(";");
            this.tabs = this.tabs - 1;
            i3 = this.blockNestingLevel;
            this.blockNestingLevel = i3 - 1;
            if (i3 == this.saveIndexCreateLevel) {
                this.saveIndexCreateLevel = 0;
            }
            println("}    // ( ... )+");
            this.currentASTResult = str;
        }
        i = i5;
        z = true;
        if (z) {
        }
        CSharpBlockFinishingInfo genCommonBlock2 = genCommonBlock(oneOrMoreBlock, false);
        StringBuilder sb32 = new StringBuilder();
        sb32.append("if (");
        sb32.append(sb2);
        sb32.append(" >= 1) { goto ");
        sb32.append(sb);
        sb32.append("_breakloop; } else { ");
        genBlockFinish(genCommonBlock2, C0000a.m3a(sb32, this.throwNoViable, "; }"));
        println(sb2 + "++;");
        this.tabs = this.tabs - 1;
        i2 = this.blockNestingLevel;
        this.blockNestingLevel = i2 + (-1);
        if (i2 == this.saveIndexCreateLevel) {
        }
        println("}");
        _print(sb + "_breakloop:");
        println(";");
        this.tabs = this.tabs - 1;
        i3 = this.blockNestingLevel;
        this.blockNestingLevel = i3 - 1;
        if (i3 == this.saveIndexCreateLevel) {
        }
        println("}    // ( ... )+");
        this.currentASTResult = str;
    }

    @Override // antlr.CodeGenerator
    public void gen(ParserGrammar parserGrammar) {
        if (parserGrammar.debuggingOutput) {
            this.semPreds = new antlr.collections.impl.Vector();
        }
        setGrammar(parserGrammar);
        if (!(this.grammar instanceof ParserGrammar)) {
            this.antlrTool.panic("Internal error generating parser");
        }
        genBody(parserGrammar);
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
            StringBuilder sb = new StringBuilder();
            sb.append(ruleRefElement.getLabel());
            sb.append(" = _t==ASTNULL ? null : ");
            C0000a.m7a(sb, this.lt1Value, ";", this);
        }
        if ((this.grammar instanceof LexerGrammar) && (!this.saveText || ruleRefElement.getAutoGenType() == 3)) {
            declareSaveIndexVariableIfNeeded();
            println("_saveIndex = text.Length;");
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
            declareSaveIndexVariableIfNeeded();
            println("text.Length = _saveIndex;");
        }
        if (this.syntacticPredLevel == 0) {
            Grammar grammar = this.grammar;
            boolean z = grammar.hasSyntacticPredicate && ((grammar.buildAST && ruleRefElement.getLabel() != null) || (this.genAST && ruleRefElement.getAutoGenType() == 1));
            if (z) {
                println("if (0 == inputState.guessing)");
                println("{");
                this.tabs++;
            }
            if (this.grammar.buildAST && ruleRefElement.getLabel() != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(ruleRefElement.getLabel());
                sb2.append("_AST = (");
                C0000a.m7a(sb2, this.labeledElementASTType, ")returnAST;", this);
            }
            if (this.genAST) {
                int autoGenType = ruleRefElement.getAutoGenType();
                if (autoGenType == 1) {
                    println(this.usingCustomAST ? "astFactory.addASTChild(ref currentAST, (AST)returnAST);" : "astFactory.addASTChild(ref currentAST, returnAST);");
                } else if (autoGenType == 2) {
                    this.antlrTool.error("Internal: encountered ^ after rule reference");
                }
            }
            if ((this.grammar instanceof LexerGrammar) && ruleRefElement.getLabel() != null) {
                println(ruleRefElement.getLabel() + " = returnToken_;");
            }
            if (z) {
                this.tabs--;
                println("}");
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
            StringBuilder sb = new StringBuilder();
            sb.append(stringLiteralElement.getLabel());
            sb.append(" = ");
            C0000a.m7a(sb, this.lt1Value, ";", this);
        }
        genElementAST(stringLiteralElement);
        boolean z = this.saveText;
        this.saveText = z && stringLiteralElement.getAutoGenType() == 1;
        genMatch(stringLiteralElement);
        this.saveText = z;
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t.getNextSibling();");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRangeElement tokenRangeElement) {
        genErrorTryForElement(tokenRangeElement);
        if (tokenRangeElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(tokenRangeElement.getLabel());
            sb.append(" = ");
            C0000a.m7a(sb, this.lt1Value, ";", this);
        }
        genElementAST(tokenRangeElement);
        StringBuilder m5a = C0000a.m5a("matchRange(");
        m5a.append(OctalToUnicode(tokenRangeElement.beginText));
        m5a.append(",");
        m5a.append(OctalToUnicode(tokenRangeElement.endText));
        m5a.append(");");
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
            StringBuilder sb = new StringBuilder();
            sb.append(tokenRefElement.getLabel());
            sb.append(" = ");
            C0000a.m7a(sb, this.lt1Value, ";", this);
        }
        genElementAST(tokenRefElement);
        genMatch(tokenRefElement);
        genErrorCatchForElement(tokenRefElement);
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t.getNextSibling();");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeElement treeElement) {
        StringBuilder m5a = C0000a.m5a("AST __t");
        m5a.append(treeElement.f302ID);
        m5a.append(" = _t;");
        println(m5a.toString());
        if (treeElement.root.getLabel() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(treeElement.root.getLabel());
            sb.append(" = (ASTNULL == _t) ? null : (");
            C0000a.m7a(sb, this.labeledElementASTType, ")_t;", this);
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
            StringBuilder m5a2 = C0000a.m5a("ASTPair __currentAST");
            m5a2.append(treeElement.f302ID);
            m5a2.append(" = currentAST.copy();");
            println(m5a2.toString());
            println("currentAST.root = currentAST.child;");
            println("currentAST.child = null;");
        }
        GrammarAtom grammarAtom = treeElement.root;
        if (grammarAtom instanceof WildcardElement) {
            println("if (null == _t) throw new MismatchedTokenException();");
        } else {
            genMatch(grammarAtom);
        }
        println("_t = _t.getFirstChild();");
        for (int i = 0; i < treeElement.getAlternatives().size(); i++) {
            for (AlternativeElement alternativeElement = treeElement.getAlternativeAt(i).head; alternativeElement != null; alternativeElement = alternativeElement.next) {
                alternativeElement.generate();
            }
        }
        if (this.grammar.buildAST) {
            StringBuilder m5a3 = C0000a.m5a("currentAST = __currentAST");
            m5a3.append(treeElement.f302ID);
            m5a3.append(";");
            println(m5a3.toString());
        }
        StringBuilder m5a4 = C0000a.m5a("_t = __t");
        m5a4.append(treeElement.f302ID);
        m5a4.append(";");
        println(m5a4.toString());
        println("_t = _t.getNextSibling();");
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeWalkerGrammar treeWalkerGrammar) {
        setGrammar(treeWalkerGrammar);
        if (!(this.grammar instanceof TreeWalkerGrammar)) {
            this.antlrTool.panic("Internal error generating tree-walker");
        }
        genBody(treeWalkerGrammar);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(WildcardElement wildcardElement) {
        String str;
        if (wildcardElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(wildcardElement.getLabel());
            sb.append(" = ");
            C0000a.m7a(sb, this.lt1Value, ";", this);
        }
        genElementAST(wildcardElement);
        Grammar grammar = this.grammar;
        if (!(grammar instanceof TreeWalkerGrammar)) {
            boolean z = grammar instanceof LexerGrammar;
            if (z) {
                if (z && (!this.saveText || wildcardElement.getAutoGenType() == 3)) {
                    declareSaveIndexVariableIfNeeded();
                    println("_saveIndex = text.Length;");
                }
                println("matchNot(EOF/*_CHAR*/);");
                if ((this.grammar instanceof LexerGrammar) && (!this.saveText || wildcardElement.getAutoGenType() == 3)) {
                    declareSaveIndexVariableIfNeeded();
                    str = "text.Length = _saveIndex;";
                }
            } else {
                C0000a.m7a(C0000a.m5a("matchNot("), getValueString(1), ");", this);
            }
            if (this.grammar instanceof TreeWalkerGrammar) {
                return;
            }
            println("_t = _t.getNextSibling();");
            return;
        }
        str = "if (null == _t) throw new MismatchedTokenException();";
        println(str);
        if (this.grammar instanceof TreeWalkerGrammar) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0129  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(ZeroOrMoreBlock zeroOrMoreBlock) {
        String sb;
        int i;
        boolean z;
        int i2;
        int i3;
        int i4;
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("gen*(" + zeroOrMoreBlock + ")");
        }
        println("{    // ( ... )*");
        this.tabs++;
        this.blockNestingLevel++;
        genBlockPreamble(zeroOrMoreBlock);
        if (zeroOrMoreBlock.getLabel() != null) {
            sb = zeroOrMoreBlock.getLabel();
        } else {
            StringBuilder m5a = C0000a.m5a("_loop");
            m5a.append(zeroOrMoreBlock.f302ID);
            sb = m5a.toString();
        }
        println("for (;;)");
        println("{");
        this.tabs++;
        this.blockNestingLevel++;
        genBlockInitAction(zeroOrMoreBlock);
        String str = this.currentASTResult;
        if (zeroOrMoreBlock.getLabel() != null) {
            this.currentASTResult = zeroOrMoreBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(zeroOrMoreBlock);
        int i5 = this.grammar.maxk;
        if (!zeroOrMoreBlock.greedy && (i4 = zeroOrMoreBlock.exitLookaheadDepth) <= i5 && zeroOrMoreBlock.exitCache[i4].containsEpsilon()) {
            i5 = zeroOrMoreBlock.exitLookaheadDepth;
        } else if (zeroOrMoreBlock.greedy || zeroOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
            i = i5;
            z = false;
            if (z) {
                if (this.DEBUG_CODE_GENERATOR) {
                    PrintStream printStream = System.out;
                    StringBuilder m5a2 = C0000a.m5a("nongreedy (...)* loop; exit depth is ");
                    m5a2.append(zeroOrMoreBlock.exitLookaheadDepth);
                    printStream.println(m5a2.toString());
                }
                String lookaheadTestExpression = getLookaheadTestExpression(zeroOrMoreBlock.exitCache, i);
                println("// nongreedy exit test");
                StringBuilder sb2 = new StringBuilder();
                sb2.append("if (");
                sb2.append(lookaheadTestExpression);
                sb2.append(") goto ");
                C0000a.m7a(sb2, sb, "_breakloop;", this);
            }
            genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), C0000a.m2a("goto ", sb, "_breakloop;"));
            this.tabs--;
            i2 = this.blockNestingLevel;
            this.blockNestingLevel = i2 - 1;
            if (i2 == this.saveIndexCreateLevel) {
                this.saveIndexCreateLevel = 0;
            }
            println("}");
            _print(sb + "_breakloop:");
            println(";");
            this.tabs = this.tabs - 1;
            i3 = this.blockNestingLevel;
            this.blockNestingLevel = i3 - 1;
            if (i3 == this.saveIndexCreateLevel) {
                this.saveIndexCreateLevel = 0;
            }
            println("}    // ( ... )*");
            this.currentASTResult = str;
        }
        i = i5;
        z = true;
        if (z) {
        }
        genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), C0000a.m2a("goto ", sb, "_breakloop;"));
        this.tabs--;
        i2 = this.blockNestingLevel;
        this.blockNestingLevel = i2 - 1;
        if (i2 == this.saveIndexCreateLevel) {
        }
        println("}");
        _print(sb + "_breakloop:");
        println(";");
        this.tabs = this.tabs - 1;
        i3 = this.blockNestingLevel;
        this.blockNestingLevel = i3 - 1;
        if (i3 == this.saveIndexCreateLevel) {
        }
        println("}    // ( ... )*");
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
            println("try        // for error handling");
            println("{");
            this.tabs++;
        }
        for (AlternativeElement alternativeElement = alternative.head; !(alternativeElement instanceof BlockEndElement); alternativeElement = alternativeElement.next) {
            alternativeElement.generate();
        }
        if (this.genAST) {
            if (alternativeBlock instanceof RuleBlock) {
                RuleBlock ruleBlock = (RuleBlock) alternativeBlock;
                if (this.usingCustomAST) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ruleBlock.getRuleName());
                    sb.append("_AST = (");
                    C0000a.m7a(sb, this.labeledElementASTType, ")currentAST.root;", this);
                } else {
                    println(ruleBlock.getRuleName() + "_AST = currentAST.root;");
                }
            } else if (alternativeBlock.getLabel() != null) {
                this.antlrTool.warning("Labeled subrules not yet supported", this.grammar.getFilename(), alternativeBlock.getLine(), alternativeBlock.getColumn());
            }
        }
        if (alternative.exceptionSpec != null) {
            this.tabs--;
            println("}");
            genErrorHandler(alternative.exceptionSpec);
        }
        this.genAST = z;
        this.saveText = z3;
        this.treeVariableMap = hashtable;
    }

    public void genBitsets(antlr.collections.impl.Vector vector, int i) {
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
                                StringBuilder sb = new StringBuilder();
                                sb.append(this.labeledElementType);
                                sb.append(" ");
                                sb.append(alternativeElement.getLabel());
                                sb.append(" = ");
                                C0000a.m7a(sb, this.labeledElementInit, ";", this);
                                if (!this.grammar.buildAST) {
                                }
                                genASTDeclaration(alternativeElement);
                            }
                        }
                        if (this.grammar.buildAST) {
                            genASTDeclaration(alternativeElement);
                        }
                        if (this.grammar instanceof LexerGrammar) {
                            StringBuilder m5a = C0000a.m5a("IToken ");
                            m5a.append(alternativeElement.getLabel());
                            m5a.append(" = null;");
                            println(m5a.toString());
                        }
                        if (this.grammar instanceof TreeWalkerGrammar) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(this.labeledElementType);
                            sb2.append(" ");
                            sb2.append(alternativeElement.getLabel());
                            sb2.append(" = ");
                            C0000a.m7a(sb2, this.labeledElementInit, ";", this);
                        }
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(this.labeledElementType);
                        sb3.append(" ");
                        sb3.append(alternativeElement.getLabel());
                        sb3.append(" = ");
                        C0000a.m7a(sb3, this.labeledElementInit, ";", this);
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

    public void genBody(LexerGrammar lexerGrammar) {
        String stripFrontBack;
        String stripFrontBack2;
        setupOutput(this.grammar.getClassName());
        this.genAST = false;
        this.saveText = true;
        this.tabs = 0;
        genHeader();
        println(this.behavior.getHeaderAction(""));
        CSharpNameSpace cSharpNameSpace = nameSpace;
        if (cSharpNameSpace != null) {
            cSharpNameSpace.emitDeclarations(this.currentOutput);
        }
        this.tabs++;
        println("// Generate header specific to lexer CSharp file");
        println("using System;");
        println("using Stream                          = System.IO.Stream;");
        println("using TextReader                      = System.IO.TextReader;");
        println("using Hashtable                       = System.Collections.Hashtable;");
        println("using Comparer                        = System.Collections.Comparer;");
        if (!lexerGrammar.caseSensitiveLiterals) {
            println("using CaseInsensitiveHashCodeProvider = System.Collections.CaseInsensitiveHashCodeProvider;");
            println("using CaseInsensitiveComparer         = System.Collections.CaseInsensitiveComparer;");
        }
        println("");
        println("using TokenStreamException            = antlr.TokenStreamException;");
        println("using TokenStreamIOException          = antlr.TokenStreamIOException;");
        println("using TokenStreamRecognitionException = antlr.TokenStreamRecognitionException;");
        println("using CharStreamException             = antlr.CharStreamException;");
        println("using CharStreamIOException           = antlr.CharStreamIOException;");
        println("using ANTLRException                  = antlr.ANTLRException;");
        println("using CharScanner                     = antlr.CharScanner;");
        println("using InputBuffer                     = antlr.InputBuffer;");
        println("using ByteBuffer                      = antlr.ByteBuffer;");
        println("using CharBuffer                      = antlr.CharBuffer;");
        println("using Token                           = antlr.Token;");
        println("using IToken                          = antlr.IToken;");
        println("using CommonToken                     = antlr.CommonToken;");
        println("using SemanticException               = antlr.SemanticException;");
        println("using RecognitionException            = antlr.RecognitionException;");
        println("using NoViableAltForCharException     = antlr.NoViableAltForCharException;");
        println("using MismatchedCharException         = antlr.MismatchedCharException;");
        println("using TokenStream                     = antlr.TokenStream;");
        println("using LexerSharedInputState           = antlr.LexerSharedInputState;");
        println("using BitSet                          = antlr.collections.impl.BitSet;");
        println(this.grammar.preambleAction.getText());
        String str = this.grammar.superClass;
        if (str == null) {
            StringBuilder m5a = C0000a.m5a("antlr.");
            m5a.append(this.grammar.getSuperClass());
            str = m5a.toString();
        }
        String str2 = this.grammar.comment;
        if (str2 != null) {
            _println(str2);
        }
        Token token = (Token) this.grammar.options.get("classHeaderPrefix");
        if (token == null || (stripFrontBack2 = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) == null) {
            print("public ");
        } else {
            print(stripFrontBack2 + " ");
        }
        StringBuilder m5a2 = C0000a.m5a("class ");
        m5a2.append(this.grammar.getClassName());
        m5a2.append(" : ");
        m5a2.append(str);
        print(m5a2.toString());
        println(", TokenStream");
        Token token2 = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token2 != null && (stripFrontBack = StringUtils.stripFrontBack(token2.getText(), "\"", "\"")) != null) {
            print(", " + stripFrontBack);
        }
        println(" {");
        this.tabs++;
        genTokenDefinitions(this.grammar.tokenManager);
        print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null));
        println("public " + this.grammar.getClassName() + "(Stream ins) : this(new ByteBuffer(ins))");
        println("{");
        println("}");
        println("");
        println("public " + this.grammar.getClassName() + "(TextReader r) : this(new CharBuffer(r))");
        println("{");
        println("}");
        println("");
        print("public " + this.grammar.getClassName() + "(InputBuffer ib)");
        println(this.grammar.debuggingOutput ? " : this(new LexerSharedInputState(new antlr.debug.DebuggingInputBuffer(ib)))" : " : this(new LexerSharedInputState(ib))");
        println("{");
        println("}");
        println("");
        println("public " + this.grammar.getClassName() + "(LexerSharedInputState state) : base(state)");
        println("{");
        this.tabs = this.tabs + 1;
        println("initialize();");
        this.tabs = this.tabs - 1;
        println("}");
        println("private void initialize()");
        println("{");
        this.tabs++;
        if (this.grammar.debuggingOutput) {
            println("ruleNames  = _ruleNames;");
            println("semPredNames = _semPredNames;");
            println("setupDebugging();");
        }
        StringBuilder m5a3 = C0000a.m5a("caseSensitiveLiterals = ");
        m5a3.append(lexerGrammar.caseSensitiveLiterals);
        m5a3.append(";");
        println(m5a3.toString());
        println("setCaseSensitive(" + lexerGrammar.caseSensitive + ");");
        println(lexerGrammar.caseSensitiveLiterals ? "literals = new Hashtable(100, (float) 0.4, null, Comparer.Default);" : "literals = new Hashtable(100, (float) 0.4, CaseInsensitiveHashCodeProvider.Default, CaseInsensitiveComparer.Default);");
        Enumeration tokenSymbolKeys = this.grammar.tokenManager.getTokenSymbolKeys();
        while (tokenSymbolKeys.hasMoreElements()) {
            String str3 = (String) tokenSymbolKeys.nextElement();
            if (str3.charAt(0) == '\"') {
                TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str3);
                if (tokenSymbol instanceof StringLiteralSymbol) {
                    StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenSymbol;
                    StringBuilder m5a4 = C0000a.m5a("literals.Add(");
                    m5a4.append(stringLiteralSymbol.getId());
                    m5a4.append(", ");
                    m5a4.append(stringLiteralSymbol.getTokenType());
                    m5a4.append(");");
                    println(m5a4.toString());
                }
            }
        }
        this.tabs--;
        println("}");
        if (this.grammar.debuggingOutput) {
            println("private static readonly string[] _ruleNames = new string[] {");
            Enumeration elements = this.grammar.rules.elements();
            while (elements.hasMoreElements()) {
                GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
                if (grammarSymbol instanceof RuleSymbol) {
                    StringBuilder m5a5 = C0000a.m5a("  \"");
                    m5a5.append(((RuleSymbol) grammarSymbol).getId());
                    m5a5.append("\",");
                    println(m5a5.toString());
                }
            }
            println("};");
        }
        genNextToken();
        Enumeration elements2 = this.grammar.rules.elements();
        int i = 0;
        while (elements2.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) elements2.nextElement();
            if (!ruleSymbol.getId().equals("mnextToken")) {
                genRule(ruleSymbol, false, i, this.grammar.tokenManager);
                i++;
            }
            exitIfError();
        }
        if (this.grammar.debuggingOutput) {
            genSemPredMap();
        }
        genBitsets(this.bitsetsUsed, ((LexerGrammar) this.grammar).charVocabulary.size());
        println("");
        this.tabs--;
        println("}");
        this.tabs--;
        CSharpNameSpace cSharpNameSpace2 = nameSpace;
        if (cSharpNameSpace2 != null) {
            cSharpNameSpace2.emitClosures(this.currentOutput);
        }
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genBody(ParserGrammar parserGrammar) {
        String stripFrontBack;
        String stripFrontBack2;
        setupOutput(this.grammar.getClassName());
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        genHeader();
        println(this.behavior.getHeaderAction(""));
        CSharpNameSpace cSharpNameSpace = nameSpace;
        if (cSharpNameSpace != null) {
            cSharpNameSpace.emitDeclarations(this.currentOutput);
        }
        this.tabs++;
        println("// Generate the header common to all output files.");
        println("using System;");
        println("");
        println("using TokenBuffer              = antlr.TokenBuffer;");
        println("using TokenStreamException     = antlr.TokenStreamException;");
        println("using TokenStreamIOException   = antlr.TokenStreamIOException;");
        println("using ANTLRException           = antlr.ANTLRException;");
        String superClass = this.grammar.getSuperClass();
        String[] split = split(superClass, ".");
        StringBuilder m5a = C0000a.m5a("using ");
        m5a.append(split[split.length - 1]);
        m5a.append(" = antlr.");
        m5a.append(superClass);
        m5a.append(";");
        println(m5a.toString());
        println("using Token                    = antlr.Token;");
        println("using IToken                   = antlr.IToken;");
        println("using TokenStream              = antlr.TokenStream;");
        println("using RecognitionException     = antlr.RecognitionException;");
        println("using NoViableAltException     = antlr.NoViableAltException;");
        println("using MismatchedTokenException = antlr.MismatchedTokenException;");
        println("using SemanticException        = antlr.SemanticException;");
        println("using ParserSharedInputState   = antlr.ParserSharedInputState;");
        println("using BitSet                   = antlr.collections.impl.BitSet;");
        if (this.genAST) {
            println("using AST                      = antlr.collections.AST;");
            println("using ASTPair                  = antlr.ASTPair;");
            println("using ASTFactory               = antlr.ASTFactory;");
            println("using ASTArray                 = antlr.collections.impl.ASTArray;");
        }
        println(this.grammar.preambleAction.getText());
        String str = this.grammar.superClass;
        if (str == null) {
            StringBuilder m5a2 = C0000a.m5a("antlr.");
            m5a2.append(this.grammar.getSuperClass());
            str = m5a2.toString();
        }
        String str2 = this.grammar.comment;
        if (str2 != null) {
            _println(str2);
        }
        Token token = (Token) this.grammar.options.get("classHeaderPrefix");
        if (token == null || (stripFrontBack2 = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) == null) {
            print("public ");
        } else {
            print(stripFrontBack2 + " ");
        }
        StringBuilder m5a3 = C0000a.m5a("class ");
        m5a3.append(this.grammar.getClassName());
        m5a3.append(" : ");
        m5a3.append(str);
        println(m5a3.toString());
        Token token2 = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token2 != null && (stripFrontBack = StringUtils.stripFrontBack(token2.getText(), "\"", "\"")) != null) {
            print("              , " + stripFrontBack);
        }
        println("{");
        this.tabs++;
        genTokenDefinitions(this.grammar.tokenManager);
        if (this.grammar.debuggingOutput) {
            println("private static readonly string[] _ruleNames = new string[] {");
            this.tabs++;
            Enumeration elements = this.grammar.rules.elements();
            while (elements.hasMoreElements()) {
                GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
                if (grammarSymbol instanceof RuleSymbol) {
                    StringBuilder m5a4 = C0000a.m5a("  \"");
                    m5a4.append(((RuleSymbol) grammarSymbol).getId());
                    m5a4.append("\",");
                    println(m5a4.toString());
                }
            }
            this.tabs--;
            println("};");
        }
        print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null));
        println("");
        println("protected void initialize()");
        println("{");
        this.tabs++;
        println("tokenNames = tokenNames_;");
        if (this.grammar.buildAST) {
            println("initializeFactory();");
        }
        if (this.grammar.debuggingOutput) {
            println("ruleNames  = _ruleNames;");
            println("semPredNames = _semPredNames;");
            println("setupDebugging(tokenBuf);");
        }
        this.tabs--;
        println("}");
        println("");
        println("");
        println("protected " + this.grammar.getClassName() + "(TokenBuffer tokenBuf, int k) : base(tokenBuf, k)");
        println("{");
        this.tabs = this.tabs + 1;
        println("initialize();");
        this.tabs = this.tabs - 1;
        println("}");
        println("");
        StringBuilder sb = new StringBuilder();
        sb.append("public ");
        C0000a.m4a(this.grammar, sb, "(TokenBuffer tokenBuf) : this(tokenBuf,");
        sb.append(this.grammar.maxk);
        sb.append(")");
        println(sb.toString());
        println("{");
        println("}");
        println("");
        println("protected " + this.grammar.getClassName() + "(TokenStream lexer, int k) : base(lexer,k)");
        println("{");
        this.tabs = this.tabs + 1;
        println("initialize();");
        this.tabs = this.tabs - 1;
        println("}");
        println("");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("public ");
        C0000a.m4a(this.grammar, sb2, "(TokenStream lexer) : this(lexer,");
        sb2.append(this.grammar.maxk);
        sb2.append(")");
        println(sb2.toString());
        println("{");
        println("}");
        println("");
        StringBuilder sb3 = new StringBuilder();
        sb3.append("public ");
        C0000a.m4a(this.grammar, sb3, "(ParserSharedInputState state) : base(state,");
        sb3.append(this.grammar.maxk);
        sb3.append(")");
        println(sb3.toString());
        println("{");
        this.tabs++;
        println("initialize();");
        this.tabs--;
        println("}");
        println("");
        this.astTypes = new Vector(100);
        Enumeration elements2 = this.grammar.rules.elements();
        int i = 0;
        while (elements2.hasMoreElements()) {
            GrammarSymbol grammarSymbol2 = (GrammarSymbol) elements2.nextElement();
            if (grammarSymbol2 instanceof RuleSymbol) {
                RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol2;
                genRule(ruleSymbol, ruleSymbol.references.size() == 0, i, this.grammar.tokenManager);
                i++;
            }
            exitIfError();
        }
        if (this.usingCustomAST) {
            StringBuilder m5a5 = C0000a.m5a("public new ");
            m5a5.append(this.labeledElementASTType);
            m5a5.append(" getAST()");
            println(m5a5.toString());
            println("{");
            this.tabs++;
            C0000a.m7a(C0000a.m5a("return ("), this.labeledElementASTType, ") returnAST;", this);
            this.tabs--;
            println("}");
            println("");
        }
        println("private void initializeFactory()");
        println("{");
        this.tabs++;
        if (this.grammar.buildAST) {
            println("if (astFactory == null)");
            println("{");
            this.tabs++;
            if (this.usingCustomAST) {
                C0000a.m7a(C0000a.m5a("astFactory = new ASTFactory(\""), this.labeledElementASTType, "\");", this);
            } else {
                println("astFactory = new ASTFactory();");
            }
            this.tabs--;
            println("}");
            println("initializeASTFactory( astFactory );");
        }
        this.tabs--;
        println("}");
        genInitFactory(parserGrammar);
        genTokenStrings();
        genBitsets(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType());
        if (this.grammar.debuggingOutput) {
            genSemPredMap();
        }
        println("");
        this.tabs--;
        println("}");
        this.tabs--;
        CSharpNameSpace cSharpNameSpace2 = nameSpace;
        if (cSharpNameSpace2 != null) {
            cSharpNameSpace2.emitClosures(this.currentOutput);
        }
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genBody(TreeWalkerGrammar treeWalkerGrammar) {
        String stripFrontBack;
        String stripFrontBack2;
        setupOutput(this.grammar.getClassName());
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        genHeader();
        println(this.behavior.getHeaderAction(""));
        CSharpNameSpace cSharpNameSpace = nameSpace;
        if (cSharpNameSpace != null) {
            cSharpNameSpace.emitDeclarations(this.currentOutput);
        }
        this.tabs++;
        println("// Generate header specific to the tree-parser CSharp file");
        println("using System;");
        println("");
        println("using " + this.grammar.getSuperClass() + " = antlr." + this.grammar.getSuperClass() + ";");
        println("using Token                    = antlr.Token;");
        println("using IToken                   = antlr.IToken;");
        println("using AST                      = antlr.collections.AST;");
        println("using RecognitionException     = antlr.RecognitionException;");
        println("using ANTLRException           = antlr.ANTLRException;");
        println("using NoViableAltException     = antlr.NoViableAltException;");
        println("using MismatchedTokenException = antlr.MismatchedTokenException;");
        println("using SemanticException        = antlr.SemanticException;");
        println("using BitSet                   = antlr.collections.impl.BitSet;");
        println("using ASTPair                  = antlr.ASTPair;");
        println("using ASTFactory               = antlr.ASTFactory;");
        println("using ASTArray                 = antlr.collections.impl.ASTArray;");
        println(this.grammar.preambleAction.getText());
        String str = this.grammar.superClass;
        if (str == null) {
            StringBuilder m5a = C0000a.m5a("antlr.");
            m5a.append(this.grammar.getSuperClass());
            str = m5a.toString();
        }
        println("");
        String str2 = this.grammar.comment;
        if (str2 != null) {
            _println(str2);
        }
        Token token = (Token) this.grammar.options.get("classHeaderPrefix");
        if (token == null || (stripFrontBack2 = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) == null) {
            print("public ");
        } else {
            print(stripFrontBack2 + " ");
        }
        StringBuilder m5a2 = C0000a.m5a("class ");
        m5a2.append(this.grammar.getClassName());
        m5a2.append(" : ");
        m5a2.append(str);
        println(m5a2.toString());
        Token token2 = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token2 != null && (stripFrontBack = StringUtils.stripFrontBack(token2.getText(), "\"", "\"")) != null) {
            print("              , " + stripFrontBack);
        }
        println("{");
        this.tabs++;
        genTokenDefinitions(this.grammar.tokenManager);
        print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null));
        println("public " + this.grammar.getClassName() + "()");
        println("{");
        this.tabs = this.tabs + 1;
        println("tokenNames = tokenNames_;");
        this.tabs = this.tabs - 1;
        println("}");
        println("");
        this.astTypes = new Vector();
        Enumeration elements = this.grammar.rules.elements();
        int i = 0;
        while (elements.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol;
                genRule(ruleSymbol, ruleSymbol.references.size() == 0, i, this.grammar.tokenManager);
                i++;
            }
            exitIfError();
        }
        if (this.usingCustomAST) {
            StringBuilder m5a3 = C0000a.m5a("public new ");
            m5a3.append(this.labeledElementASTType);
            m5a3.append(" getAST()");
            println(m5a3.toString());
            println("{");
            this.tabs++;
            C0000a.m7a(C0000a.m5a("return ("), this.labeledElementASTType, ") returnAST;", this);
            this.tabs--;
            println("}");
            println("");
        }
        genInitFactory(this.grammar);
        genTokenStrings();
        genBitsets(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType());
        this.tabs--;
        println("}");
        println("");
        this.tabs--;
        CSharpNameSpace cSharpNameSpace2 = nameSpace;
        if (cSharpNameSpace2 != null) {
            cSharpNameSpace2.emitClosures(this.currentOutput);
        }
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genCases(BitSet bitSet) {
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genCases(" + bitSet + ")");
        }
        int[] array = bitSet.toArray();
        int i = this.grammar instanceof LexerGrammar ? 4 : 1;
        boolean z = true;
        int i2 = 1;
        for (int i3 : array) {
            if (i2 == 1) {
                print("");
            } else {
                _print("  ");
            }
            StringBuilder m5a = C0000a.m5a("case ");
            m5a.append(getValueString(i3));
            m5a.append(":");
            _print(m5a.toString());
            if (i2 == i) {
                _println("");
                z = true;
                i2 = 1;
            } else {
                i2++;
                z = false;
            }
        }
        if (z) {
            return;
        }
        _println("");
    }

    /* JADX WARN: Code restructure failed: missing block: B:191:0x0490, code lost:
    
        if (r9 > 0) goto L183;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x049d, code lost:
    
        r4 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x049b, code lost:
    
        if (r9 > 0) goto L183;
     */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0415  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CSharpBlockFinishingInfo genCommonBlock(AlternativeBlock alternativeBlock, boolean z) {
        String str;
        boolean z2;
        CSharpBlockFinishingInfo cSharpBlockFinishingInfo;
        boolean z3;
        boolean z4;
        boolean z5;
        String str2;
        boolean lookaheadIsEmpty;
        int i;
        boolean z6;
        boolean z7;
        int i2;
        CSharpBlockFinishingInfo cSharpBlockFinishingInfo2;
        int i3;
        String str3;
        int i4;
        String str4;
        PrintStream printStream;
        String str5;
        CSharpBlockFinishingInfo cSharpBlockFinishingInfo3 = new CSharpBlockFinishingInfo();
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("genCommonBlock(" + alternativeBlock + ")");
        }
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
                StringBuilder sb = new StringBuilder();
                sb.append(alternativeBlock.getLabel());
                sb.append(" = ");
                C0000a.m7a(sb, this.lt1Value, ";", this);
            }
            genElementAST(alternativeBlock);
            StringBuilder m9b = C0000a.m9b("match(", this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? "(AST)_t," : "_t," : "");
            m9b.append(getBitsetName(markBitsetForGen(look.fset)));
            m9b.append(");");
            println(m9b.toString());
            if (this.grammar instanceof TreeWalkerGrammar) {
                println("_t = _t.getNextSibling();");
            }
            return cSharpBlockFinishingInfo3;
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
                return cSharpBlockFinishingInfo3;
            }
        }
        int i5 = 0;
        for (int i6 = 0; i6 < alternativeBlock.getAlternatives().size(); i6++) {
            if (suitableForCaseExpression(alternativeBlock.getAlternativeAt(i6))) {
                i5++;
            }
        }
        String str7 = "}";
        if (i5 >= this.makeSwitchThreshold) {
            String lookaheadString = lookaheadString(1);
            if (this.grammar instanceof TreeWalkerGrammar) {
                println("if (null == _t)");
                this.tabs++;
                println("_t = ASTNULL;");
                this.tabs--;
            }
            println("switch ( " + lookaheadString + " )");
            println("{");
            this.blockNestingLevel = this.blockNestingLevel + 1;
            for (int i7 = 0; i7 < alternativeBlock.alternatives.size(); i7++) {
                Alternative alternativeAt2 = alternativeBlock.getAlternativeAt(i7);
                if (suitableForCaseExpression(alternativeAt2)) {
                    Lookahead lookahead = alternativeAt2.cache[1];
                    if (lookahead.fset.degree() != 0 || lookahead.containsEpsilon()) {
                        genCases(lookahead.fset);
                        println("{");
                        this.tabs++;
                        this.blockNestingLevel++;
                        genAlt(alternativeAt2, alternativeBlock);
                        println("break;");
                        int i8 = this.blockNestingLevel;
                        this.blockNestingLevel = i8 - 1;
                        if (i8 == this.saveIndexCreateLevel) {
                            this.saveIndexCreateLevel = 0;
                        }
                        this.tabs--;
                        println("}");
                    } else {
                        this.antlrTool.warning("Alternate omitted due to empty prediction set", this.grammar.getFilename(), alternativeAt2.head.getLine(), alternativeAt2.head.getColumn());
                    }
                }
            }
            println("default:");
            this.tabs++;
            z2 = true;
        } else {
            z2 = false;
        }
        Grammar grammar = this.grammar;
        int i9 = grammar instanceof LexerGrammar ? grammar.maxk : 0;
        int i10 = 0;
        int i11 = 0;
        while (i9 >= 0) {
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("checking depth " + i9);
            }
            int i12 = 0;
            while (i12 < alternativeBlock.alternatives.size()) {
                Alternative alternativeAt3 = alternativeBlock.getAlternativeAt(i12);
                if (this.DEBUG_CODE_GENERATOR) {
                    PrintStream printStream2 = System.out;
                    str2 = str;
                    StringBuilder sb2 = new StringBuilder();
                    z5 = z9;
                    sb2.append("genAlt: ");
                    sb2.append(i12);
                    printStream2.println(sb2.toString());
                } else {
                    z5 = z9;
                    str2 = str;
                }
                if (z2 && suitableForCaseExpression(alternativeAt3)) {
                    if (this.DEBUG_CODE_GENERATOR) {
                        printStream = System.out;
                        str5 = "ignoring alt because it was in the switch";
                        printStream.println(str5);
                    }
                    cSharpBlockFinishingInfo2 = cSharpBlockFinishingInfo3;
                    z7 = z8;
                    z6 = z2;
                    i3 = i12;
                    i2 = i9;
                    str4 = str7;
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
                        if (i != i9) {
                            if (this.DEBUG_CODE_GENERATOR) {
                                printStream = System.out;
                                str5 = "ignoring alt because effectiveDepth!=altDepth;" + i + "!=" + i9;
                                printStream.println(str5);
                            }
                            cSharpBlockFinishingInfo2 = cSharpBlockFinishingInfo3;
                            z7 = z8;
                            z6 = z2;
                            i3 = i12;
                            i2 = i9;
                            str4 = str7;
                        } else {
                            lookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, i);
                        }
                    } else {
                        lookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, grammar2.maxk);
                        i = this.grammar.maxk;
                    }
                    String lookaheadTestExpression = getLookaheadTestExpression(alternativeAt3, i);
                    z6 = z2;
                    z7 = z8;
                    i2 = i9;
                    if (alternativeAt3.cache[1].fset.degree() > 127 && suitableForCaseExpression(alternativeAt3)) {
                        println(i10 == 0 ? "if " + lookaheadTestExpression : "else if " + lookaheadTestExpression);
                        println("{");
                    } else if (lookaheadIsEmpty && alternativeAt3.semPred == null && alternativeAt3.synPred == null) {
                        if (i10 == 0) {
                            println("{");
                        } else {
                            println("else {");
                        }
                        cSharpBlockFinishingInfo3.needAnErrorClause = false;
                    } else {
                        if (alternativeAt3.semPred != null) {
                            cSharpBlockFinishingInfo2 = cSharpBlockFinishingInfo3;
                            i3 = i12;
                            String processActionForSpecialSymbols = processActionForSpecialSymbols(alternativeAt3.semPred, alternativeBlock.line, this.currentRule, new ActionTransInfo());
                            Grammar grammar3 = this.grammar;
                            str3 = str7;
                            if (((grammar3 instanceof ParserGrammar) || (grammar3 instanceof LexerGrammar)) && this.grammar.debuggingOutput) {
                                StringBuilder m10b = C0000a.m10b("(", lookaheadTestExpression, "&& fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEventArgs.PREDICTING,");
                                m10b.append(addSemPred(this.charFormatter.escapeString(processActionForSpecialSymbols)));
                                m10b.append(",");
                                lookaheadTestExpression = C0000a.m3a(m10b, processActionForSpecialSymbols, "))");
                            } else {
                                lookaheadTestExpression = "(" + lookaheadTestExpression + "&&(" + processActionForSpecialSymbols + "))";
                            }
                        } else {
                            cSharpBlockFinishingInfo2 = cSharpBlockFinishingInfo3;
                            i3 = i12;
                            str3 = str7;
                        }
                        SynPredBlock synPredBlock = alternativeAt3.synPred;
                        if (i10 > 0) {
                            if (synPredBlock != null) {
                                println("else {");
                                this.tabs++;
                                this.blockNestingLevel++;
                                genSynPred(alternativeAt3.synPred, lookaheadTestExpression);
                                i11++;
                            } else {
                                println("else if " + lookaheadTestExpression + " {");
                            }
                        } else if (synPredBlock != null) {
                            genSynPred(synPredBlock, lookaheadTestExpression);
                        } else {
                            if (this.grammar instanceof TreeWalkerGrammar) {
                                println("if (_t == null)");
                                this.tabs++;
                                println("_t = ASTNULL;");
                                this.tabs--;
                            }
                            println("if " + lookaheadTestExpression);
                            println("{");
                        }
                        this.blockNestingLevel++;
                        i10++;
                        this.tabs++;
                        genAlt(alternativeAt3, alternativeBlock);
                        this.tabs--;
                        i4 = this.blockNestingLevel;
                        this.blockNestingLevel = i4 - 1;
                        if (i4 == this.saveIndexCreateLevel) {
                            this.saveIndexCreateLevel = 0;
                        }
                        str4 = str3;
                        println(str4);
                    }
                    cSharpBlockFinishingInfo2 = cSharpBlockFinishingInfo3;
                    i3 = i12;
                    str3 = str7;
                    this.blockNestingLevel++;
                    i10++;
                    this.tabs++;
                    genAlt(alternativeAt3, alternativeBlock);
                    this.tabs--;
                    i4 = this.blockNestingLevel;
                    this.blockNestingLevel = i4 - 1;
                    if (i4 == this.saveIndexCreateLevel) {
                    }
                    str4 = str3;
                    println(str4);
                }
                i12 = i3 + 1;
                z2 = z6;
                str7 = str4;
                str = str2;
                z9 = z5;
                z8 = z7;
                i9 = i2;
                cSharpBlockFinishingInfo3 = cSharpBlockFinishingInfo2;
            }
            i9--;
            cSharpBlockFinishingInfo3 = cSharpBlockFinishingInfo3;
        }
        CSharpBlockFinishingInfo cSharpBlockFinishingInfo4 = cSharpBlockFinishingInfo3;
        boolean z10 = z8;
        boolean z11 = z2;
        boolean z12 = z9;
        String str8 = str7;
        String str9 = str;
        for (int i13 = 1; i13 <= i11; i13++) {
            str9 = C0000a.m1a(str9, str8);
            int i14 = this.blockNestingLevel;
            this.blockNestingLevel = i14 - 1;
            if (i14 == this.saveIndexCreateLevel) {
                this.saveIndexCreateLevel = 0;
            }
        }
        this.genAST = z10;
        this.saveText = z12;
        if (z11) {
            this.tabs--;
            cSharpBlockFinishingInfo = cSharpBlockFinishingInfo4;
            cSharpBlockFinishingInfo.postscript = C0000a.m1a(str9, "break; }");
            int i15 = this.blockNestingLevel;
            this.blockNestingLevel = i15 - 1;
            z3 = false;
            if (i15 == this.saveIndexCreateLevel) {
                this.saveIndexCreateLevel = 0;
            }
            z4 = true;
            cSharpBlockFinishingInfo.generatedSwitch = true;
        } else {
            cSharpBlockFinishingInfo = cSharpBlockFinishingInfo4;
            z3 = false;
            z4 = true;
            cSharpBlockFinishingInfo.postscript = str9;
            cSharpBlockFinishingInfo.generatedSwitch = false;
        }
        cSharpBlockFinishingInfo.generatedAnIf = z3;
        return cSharpBlockFinishingInfo;
    }

    public void genHeader() {
        StringBuilder m5a = C0000a.m5a("// $ANTLR ");
        m5a.append(Tool.version);
        m5a.append(": \"");
        Tool tool = this.antlrTool;
        m5a.append(tool.fileMinusPath(tool.grammarFile));
        m5a.append("\" -> \"");
        m5a.append(this.grammar.getClassName());
        m5a.append(".cs\"$");
        println(m5a.toString());
    }

    public void genInitFactory(Grammar grammar) {
        TokenSymbol tokenSymbol;
        if (grammar.buildAST) {
            println("static public void initializeASTFactory( ASTFactory factory )");
            println("{");
            this.tabs++;
            StringBuilder m5a = C0000a.m5a("factory.setMaxNodeType(");
            m5a.append(grammar.tokenManager.maxTokenType());
            m5a.append(");");
            println(m5a.toString());
            antlr.collections.impl.Vector vocabulary = grammar.tokenManager.getVocabulary();
            for (int i = 0; i < vocabulary.size(); i++) {
                String str = (String) vocabulary.elementAt(i);
                if (str != null && (tokenSymbol = grammar.tokenManager.getTokenSymbol(str)) != null && tokenSymbol.getASTNodeType() != null) {
                    StringBuilder m10b = C0000a.m10b("factory.setTokenTypeASTNodeType(", str, ", \"");
                    m10b.append(tokenSymbol.getASTNodeType());
                    m10b.append("\");");
                    println(m10b.toString());
                }
            }
            this.tabs--;
            println("}");
        }
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
        String str = this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? "(AST)_t," : "_t," : "";
        if ((this.grammar instanceof LexerGrammar) && (!this.saveText || grammarAtom.getAutoGenType() == 3)) {
            declareSaveIndexVariableIfNeeded();
            println("_saveIndex = text.Length;");
        }
        print(grammarAtom.not ? "matchNot(" : "match(");
        _print(str);
        _print(grammarAtom.atomText.equals("EOF") ? "Token.EOF_TYPE" : grammarAtom.atomText);
        _println(");");
        if (this.grammar instanceof LexerGrammar) {
            if (!this.saveText || grammarAtom.getAutoGenType() == 3) {
                declareSaveIndexVariableIfNeeded();
                println("text.Length = _saveIndex;");
            }
        }
    }

    public void genMatchUsingAtomTokenType(GrammarAtom grammarAtom) {
        StringBuilder m5a = C0000a.m5a(this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? "(AST)_t," : "_t," : "");
        m5a.append(getValueString(grammarAtom.getType()));
        String sb = m5a.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(grammarAtom.not ? "matchNot(" : "match(");
        sb2.append(sb);
        sb2.append(");");
        println(sb2.toString());
    }

    public void genNextToken() {
        boolean z;
        String m3a;
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
            String str = lexerGrammar.filterMode ? lexerGrammar.filterRule : null;
            println("");
            println("override public IToken nextToken()\t\t\t//throws TokenStreamException");
            println("{");
            this.tabs++;
            this.blockNestingLevel = 1;
            this.saveIndexCreateLevel = 0;
            println("IToken theRetToken = null;");
            _println("tryAgain:");
            println("for (;;)");
            println("{");
            this.tabs++;
            println("IToken _token = null;");
            println("int _ttype = Token.INVALID_TYPE;");
            if (((LexerGrammar) this.grammar).filterMode) {
                println("setCommitToPath(false);");
                if (str != null) {
                    String str2 = " does not exist in this lexer";
                    if (this.grammar.isDefined(CodeGenerator.encodeLexerRuleName(str))) {
                        RuleSymbol ruleSymbol3 = (RuleSymbol) this.grammar.getSymbol(CodeGenerator.encodeLexerRuleName(str));
                        if (ruleSymbol3.isDefined()) {
                            if (ruleSymbol3.access.equals("public")) {
                                tool = this.grammar.antlrTool;
                                sb = new StringBuilder();
                                sb.append("Filter rule ");
                                sb.append(str);
                                str2 = " must be protected";
                                sb.append(str2);
                                tool.error(sb.toString());
                            }
                            println("int _m;");
                            println("_m = mark();");
                        } else {
                            tool = this.grammar.antlrTool;
                            sb = new StringBuilder();
                        }
                    } else {
                        tool = this.grammar.antlrTool;
                        sb = new StringBuilder();
                    }
                    sb.append("Filter rule ");
                    sb.append(str);
                    sb.append(str2);
                    tool.error(sb.toString());
                    println("int _m;");
                    println("_m = mark();");
                }
            }
            println("resetText();");
            println("try     // for char stream error handling");
            println("{");
            this.tabs++;
            println("try     // for lexical error handling");
            println("{");
            this.tabs++;
            for (int i2 = 0; i2 < createNextTokenRule.getAlternatives().size(); i2++) {
                Alternative alternativeAt = createNextTokenRule.getAlternativeAt(i2);
                if (alternativeAt.cache[1].containsEpsilon()) {
                    String decodeLexerRuleName = CodeGenerator.decodeLexerRuleName(((RuleRefElement) alternativeAt.head).targetRule);
                    this.antlrTool.warning("public lexical rule " + decodeLexerRuleName + " is optional (can match \"nothing\")");
                }
            }
            String property = System.getProperty("line.separator");
            CSharpBlockFinishingInfo genCommonBlock = genCommonBlock(createNextTokenRule, false);
            String m2a = C0000a.m2a("if (cached_LA1==EOF_CHAR) { uponEOF(); returnToken_ = makeToken(Token.EOF_TYPE); }", property, "\t\t\t\t");
            if (!((LexerGrammar) this.grammar).filterMode) {
                m3a = C0000a.m3a(C0000a.m9b(m2a, "else {"), this.throwNoViable, "}");
            } else if (str == null) {
                m3a = C0000a.m1a(C0000a.m1a(C0000a.m1a(C0000a.m1a(C0000a.m1a(m2a, "\t\t\t\telse"), "\t\t\t\t{"), "\t\t\t\t\tconsume();"), "\t\t\t\t\tgoto tryAgain;"), "\t\t\t\t}");
            } else {
                m3a = m2a + "\t\t\t\t\telse" + property + "\t\t\t\t\t{" + property + "\t\t\t\t\tcommit();" + property + "\t\t\t\t\ttry {m" + str + "(false);}" + property + "\t\t\t\t\tcatch(RecognitionException e)" + property + "\t\t\t\t\t{" + property + "\t\t\t\t\t\t// catastrophic failure" + property + "\t\t\t\t\t\treportError(e);" + property + "\t\t\t\t\t\tconsume();" + property + "\t\t\t\t\t}" + property + "\t\t\t\t\tgoto tryAgain;" + property + "\t\t\t\t}";
            }
            genBlockFinish(genCommonBlock, m3a);
            if (((LexerGrammar) this.grammar).filterMode && str != null) {
                println("commit();");
            }
            println("if ( null==returnToken_ ) goto tryAgain; // found SKIP token");
            println("_ttype = returnToken_.Type;");
            if (((LexerGrammar) this.grammar).getTestLiterals()) {
                genLiteralsTest();
            }
            println("returnToken_.Type = _ttype;");
            println("return returnToken_;");
            this.tabs--;
            println("}");
            println("catch (RecognitionException e) {");
            this.tabs++;
            if (((LexerGrammar) this.grammar).filterMode) {
                println("if (!getCommitToPath())");
                println("{");
                this.tabs++;
                if (str == null) {
                    println("consume();");
                    println("goto tryAgain;");
                    this.tabs--;
                    println("}");
                } else {
                    println("rewind(_m);");
                    println("resetText();");
                    println("try {m" + str + "(false);}");
                    println("catch(RecognitionException ee) {");
                    println("\t// horrendous failure: error in filter rule");
                    println("\treportError(ee);");
                    println("\tconsume();");
                    println("}");
                    this.tabs--;
                    println("}");
                    println("else");
                }
            }
            if (createNextTokenRule.getDefaultErrorHandler()) {
                println("{");
                this.tabs++;
                println("reportError(e);");
                println("consume();");
                this.tabs--;
                println("}");
            } else {
                this.tabs++;
                println("throw new TokenStreamRecognitionException(e);");
                this.tabs--;
            }
            this.tabs--;
            println("}");
            this.tabs--;
            println("}");
            println("catch (CharStreamException cse) {");
            println("\tif ( cse is CharStreamIOException ) {");
            println("\t\tthrow new TokenStreamIOException(((CharStreamIOException)cse).io);");
            println("\t}");
            println("\telse {");
            println("\t\tthrow new TokenStreamException(cse.Message);");
            println("\t}");
            println("}");
            this.tabs--;
            println("}");
        } else {
            println("");
            println("override public IToken nextToken()\t\t\t//throws TokenStreamException");
            println("{");
            this.tabs++;
            println("try");
            println("{");
            this.tabs++;
            println("uponEOF();");
            this.tabs--;
            println("}");
            println("catch(CharStreamIOException csioe)");
            println("{");
            this.tabs++;
            println("throw new TokenStreamIOException(csioe.io);");
            this.tabs--;
            println("}");
            println("catch(CharStreamException cse)");
            println("{");
            this.tabs++;
            println("throw new TokenStreamException(cse.Message);");
            this.tabs--;
            println("}");
            println("return new CommonToken(Token.EOF_TYPE, \"\");");
        }
        this.tabs--;
        println("}");
        println("");
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0411  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x041c  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0433  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x045c  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x04a9  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x04de  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0330  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x02ff  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0126  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x032b  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x03f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void genRule(RuleSymbol ruleSymbol, boolean z, int i, TokenManager tokenManager) {
        String str;
        Grammar grammar;
        Grammar grammar2;
        Grammar grammar3;
        ExceptionSpec findExceptionSpec;
        int i2;
        boolean z2;
        String str2;
        String str3;
        int i3;
        Grammar grammar4;
        Grammar grammar5;
        Grammar grammar6;
        int i4;
        StringBuilder m5a;
        StringBuilder sb;
        StringBuilder sb2;
        String sb3;
        StringBuilder m5a2;
        String str4;
        this.tabs = 1;
        if (this.DEBUG_CODE_GENERATOR) {
            PrintStream printStream = System.out;
            StringBuilder m5a3 = C0000a.m5a("genRule(");
            m5a3.append(ruleSymbol.getId());
            m5a3.append(")");
            printStream.println(m5a3.toString());
        }
        if (!ruleSymbol.isDefined()) {
            Tool tool = this.antlrTool;
            StringBuilder m5a4 = C0000a.m5a("undefined rule: ");
            m5a4.append(ruleSymbol.getId());
            tool.error(m5a4.toString());
            return;
        }
        RuleBlock block = ruleSymbol.getBlock();
        this.currentRule = block;
        this.currentASTResult = ruleSymbol.getId();
        this.declaredASTVariables.clear();
        boolean z3 = this.genAST;
        this.genAST = z3 && block.getAutoGen();
        this.saveText = block.getAutoGen();
        String str5 = ruleSymbol.comment;
        if (str5 != null) {
            _println(str5);
        }
        print(ruleSymbol.access + " ");
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
        StringBuilder m5a5 = C0000a.m5a(" //throws ");
        m5a5.append(this.exceptionThrown);
        _print(m5a5.toString());
        Grammar grammar7 = this.grammar;
        if (!(grammar7 instanceof ParserGrammar)) {
            str4 = grammar7 instanceof LexerGrammar ? ", CharStreamException, TokenStreamException" : ", TokenStreamException";
            if (block.throwsSpec != null) {
                if (this.grammar instanceof LexerGrammar) {
                    Tool tool2 = this.antlrTool;
                    StringBuilder m5a6 = C0000a.m5a("user-defined throws spec not allowed (yet) for lexer rule ");
                    m5a6.append(block.ruleName);
                    tool2.error(m5a6.toString());
                } else {
                    StringBuilder m5a7 = C0000a.m5a(", ");
                    m5a7.append(block.throwsSpec);
                    _print(m5a7.toString());
                }
            }
            _println("");
            _println("{");
            this.tabs++;
            if (block.returnAction != null) {
                C0000a.m7a(new StringBuilder(), block.returnAction, ";", this);
            }
            println(this.commonLocalVars);
            grammar = this.grammar;
            if (grammar.traceRules) {
                if (!(grammar instanceof TreeWalkerGrammar)) {
                    m5a2 = C0000a.m5a("traceIn(\"");
                    m5a2.append(ruleSymbol.getId());
                    m5a2.append("\");");
                } else if (this.usingCustomAST) {
                    m5a2 = C0000a.m5a("traceIn(\"");
                    m5a2.append(ruleSymbol.getId());
                    m5a2.append("\",(AST)_t);");
                } else {
                    m5a2 = C0000a.m5a("traceIn(\"");
                    m5a2.append(ruleSymbol.getId());
                    m5a2.append("\",_t);");
                }
                println(m5a2.toString());
            }
            if (this.grammar instanceof LexerGrammar) {
                if (ruleSymbol.getId().equals("mEOF")) {
                    sb3 = "_ttype = Token.EOF_TYPE;";
                } else {
                    StringBuilder m5a8 = C0000a.m5a("_ttype = ");
                    m5a8.append(ruleSymbol.getId().substring(1));
                    m5a8.append(";");
                    sb3 = m5a8.toString();
                }
                println(sb3);
                this.blockNestingLevel = 1;
                this.saveIndexCreateLevel = 0;
            }
            grammar2 = this.grammar;
            if (grammar2.debuggingOutput) {
                if (grammar2 instanceof ParserGrammar) {
                    sb2 = new StringBuilder();
                    sb2.append("fireEnterRule(");
                    sb2.append(i);
                    sb2.append(",0);");
                } else if (grammar2 instanceof LexerGrammar) {
                    sb2 = new StringBuilder();
                    sb2.append("fireEnterRule(");
                    sb2.append(i);
                    sb2.append(",_ttype);");
                }
                println(sb2.toString());
            }
            grammar3 = this.grammar;
            if (!grammar3.debuggingOutput || grammar3.traceRules) {
                println("try { // debugging");
                this.tabs++;
            }
            if (this.grammar instanceof TreeWalkerGrammar) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(this.labeledElementASTType);
                sb4.append(" ");
                sb4.append(ruleSymbol.getId());
                sb4.append("_AST_in = (");
                C0000a.m7a(sb4, this.labeledElementASTType, ")_t;", this);
            }
            if (this.grammar.buildAST) {
                println("returnAST = null;");
                println("ASTPair currentAST = new ASTPair();");
                println(this.labeledElementASTType + " " + ruleSymbol.getId() + "_AST = null;");
            }
            genBlockPreamble(block);
            genBlockInitAction(block);
            println("");
            findExceptionSpec = block.findExceptionSpec("");
            if (findExceptionSpec == null || block.getDefaultErrorHandler()) {
                println("try {      // for error handling");
                i2 = 1;
                this.tabs++;
            } else {
                i2 = 1;
            }
            if (block.alternatives.size() != i2) {
                Alternative alternativeAt = block.getAlternativeAt(0);
                String str6 = alternativeAt.semPred;
                if (str6 != null) {
                    genSemPred(str6, this.currentRule.line);
                }
                if (alternativeAt.synPred != null) {
                    z2 = z3;
                    str2 = "";
                    str3 = "\");";
                    this.antlrTool.warning("Syntactic predicate ignored for single alternative", this.grammar.getFilename(), alternativeAt.synPred.getLine(), alternativeAt.synPred.getColumn());
                } else {
                    z2 = z3;
                    str2 = "";
                    str3 = "\");";
                }
                genAlt(alternativeAt, block);
            } else {
                z2 = z3;
                str2 = "";
                str3 = "\");";
                this.grammar.theLLkAnalyzer.deterministic(block);
                genBlockFinish(genCommonBlock(block, false), this.throwNoViable);
            }
            if (findExceptionSpec == null || block.getDefaultErrorHandler()) {
                this.tabs--;
                println("}");
            }
            if (findExceptionSpec == null) {
                genErrorHandler(findExceptionSpec);
            } else if (block.getDefaultErrorHandler()) {
                StringBuilder m5a9 = C0000a.m5a("catch (");
                m5a9.append(this.exceptionThrown);
                m5a9.append(" ex)");
                println(m5a9.toString());
                println("{");
                this.tabs++;
                if (this.grammar.hasSyntacticPredicate) {
                    println("if (0 == inputState.guessing)");
                    println("{");
                    this.tabs++;
                }
                println("reportError(ex);");
                Grammar grammar8 = this.grammar;
                if (grammar8 instanceof TreeWalkerGrammar) {
                    println("if (null != _t)");
                    println("{");
                    i3 = 1;
                    this.tabs++;
                    println("_t = _t.getNextSibling();");
                    this.tabs--;
                    println("}");
                } else {
                    println("recover(ex," + getBitsetName(markBitsetForGen(grammar8.theLLkAnalyzer.FOLLOW(1, block.endNode).fset)) + ");");
                    i3 = 1;
                }
                if (this.grammar.hasSyntacticPredicate) {
                    this.tabs -= i3;
                    println("}");
                    println("else");
                    println("{");
                    this.tabs += i3;
                    println("throw ex;");
                    this.tabs -= i3;
                    println("}");
                }
                this.tabs -= i3;
                println("}");
            }
            if (this.grammar.buildAST) {
                StringBuilder m5a10 = C0000a.m5a("returnAST = ");
                m5a10.append(ruleSymbol.getId());
                m5a10.append("_AST;");
                println(m5a10.toString());
            }
            if (this.grammar instanceof TreeWalkerGrammar) {
                println("retTree_ = _t;");
            }
            if (block.getTestLiterals()) {
                if (ruleSymbol.access.equals("protected")) {
                    genLiteralsTestForPartialToken();
                } else {
                    genLiteralsTest();
                }
            }
            if (this.grammar instanceof LexerGrammar) {
                println("if (_createToken && (null == _token) && (_ttype != Token.SKIP))");
                println("{");
                this.tabs++;
                println("_token = makeToken(_ttype);");
                println("_token.setText(text.ToString(_begin, text.Length-_begin));");
                this.tabs--;
                println("}");
                println("returnToken_ = _token;");
            }
            if (block.returnAction != null) {
                StringBuilder m5a11 = C0000a.m5a("return ");
                m5a11.append(extractIdOfAction(block.returnAction, block.getLine(), block.getColumn()));
                m5a11.append(";");
                println(m5a11.toString());
            }
            grammar4 = this.grammar;
            if (!grammar4.debuggingOutput || grammar4.traceRules) {
                this.tabs--;
                println("}");
                println("finally");
                println("{ // debugging");
                this.tabs++;
                grammar5 = this.grammar;
                if (grammar5.debuggingOutput) {
                    if (grammar5 instanceof ParserGrammar) {
                        sb = new StringBuilder();
                        sb.append("fireExitRule(");
                        sb.append(i);
                        sb.append(",0);");
                    } else if (grammar5 instanceof LexerGrammar) {
                        sb = new StringBuilder();
                        sb.append("fireExitRule(");
                        sb.append(i);
                        sb.append(",_ttype);");
                    }
                    println(sb.toString());
                }
                grammar6 = this.grammar;
                if (grammar6.traceRules) {
                    if (grammar6 instanceof TreeWalkerGrammar) {
                        m5a = C0000a.m5a("traceOut(\"");
                        m5a.append(ruleSymbol.getId());
                        m5a.append("\",_t);");
                    } else {
                        m5a = C0000a.m5a("traceOut(\"");
                        m5a.append(ruleSymbol.getId());
                        m5a.append(str3);
                    }
                    println(m5a.toString());
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
        }
        _print(str4);
        if (block.throwsSpec != null) {
        }
        _println("");
        _println("{");
        this.tabs++;
        if (block.returnAction != null) {
        }
        println(this.commonLocalVars);
        grammar = this.grammar;
        if (grammar.traceRules) {
        }
        if (this.grammar instanceof LexerGrammar) {
        }
        grammar2 = this.grammar;
        if (grammar2.debuggingOutput) {
        }
        grammar3 = this.grammar;
        if (!grammar3.debuggingOutput) {
        }
        println("try { // debugging");
        this.tabs++;
        if (this.grammar instanceof TreeWalkerGrammar) {
        }
        if (this.grammar.buildAST) {
        }
        genBlockPreamble(block);
        genBlockInitAction(block);
        println("");
        findExceptionSpec = block.findExceptionSpec("");
        if (findExceptionSpec == null) {
        }
        println("try {      // for error handling");
        i2 = 1;
        this.tabs++;
        if (block.alternatives.size() != i2) {
        }
        if (findExceptionSpec == null) {
        }
        this.tabs--;
        println("}");
        if (findExceptionSpec == null) {
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
        grammar4 = this.grammar;
        if (grammar4.debuggingOutput) {
        }
        this.tabs--;
        println("}");
        println("finally");
        println("{ // debugging");
        this.tabs++;
        grammar5 = this.grammar;
        if (grammar5.debuggingOutput) {
        }
        grammar6 = this.grammar;
        if (grammar6.traceRules) {
        }
        i4 = 1;
        this.tabs--;
        println("}");
        this.tabs -= i4;
        println("}");
        println(str2);
        this.genAST = z2;
    }

    public void genSemPred(String str, int i) {
        String processActionForSpecialSymbols = processActionForSpecialSymbols(str, i, this.currentRule, new ActionTransInfo());
        String escapeString = this.charFormatter.escapeString(processActionForSpecialSymbols);
        Grammar grammar = this.grammar;
        if (grammar.debuggingOutput && ((grammar instanceof ParserGrammar) || (grammar instanceof LexerGrammar))) {
            StringBuilder m5a = C0000a.m5a("fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.VALIDATING,");
            m5a.append(addSemPred(escapeString));
            m5a.append(",");
            m5a.append(processActionForSpecialSymbols);
            m5a.append(")");
            processActionForSpecialSymbols = m5a.toString();
        }
        println("if (!(" + processActionForSpecialSymbols + "))");
        StringBuilder sb = new StringBuilder();
        sb.append("  throw new SemanticException(\"");
        C0000a.m7a(sb, escapeString, "\");", this);
    }

    public void genSemPredMap() {
        Enumeration elements = this.semPreds.elements();
        println("private string[] _semPredNames = {");
        this.tabs++;
        while (elements.hasMoreElements()) {
            StringBuilder m5a = C0000a.m5a("\"");
            m5a.append(elements.nextElement());
            m5a.append("\",");
            println(m5a.toString());
        }
        this.tabs--;
        println("};");
    }

    public void genSynPred(SynPredBlock synPredBlock, String str) {
        StringBuilder m5a;
        String str2;
        StringBuilder m5a2;
        String str3;
        if (this.DEBUG_CODE_GENERATOR) {
            System.out.println("gen=>(" + synPredBlock + ")");
        }
        StringBuilder m5a3 = C0000a.m5a("bool synPredMatched");
        m5a3.append(synPredBlock.f302ID);
        m5a3.append(" = false;");
        println(m5a3.toString());
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("if (_t==null) _t=ASTNULL;");
        }
        println("if (" + str + ")");
        println("{");
        this.tabs = this.tabs + 1;
        if (this.grammar instanceof TreeWalkerGrammar) {
            m5a = C0000a.m5a("AST __t");
            m5a.append(synPredBlock.f302ID);
            str2 = " = _t;";
        } else {
            m5a = C0000a.m5a("int _m");
            m5a.append(synPredBlock.f302ID);
            str2 = " = mark();";
        }
        m5a.append(str2);
        println(m5a.toString());
        StringBuilder m5a4 = C0000a.m5a("synPredMatched");
        m5a4.append(synPredBlock.f302ID);
        m5a4.append(" = true;");
        println(m5a4.toString());
        println("inputState.guessing++;");
        Grammar grammar = this.grammar;
        if (grammar.debuggingOutput && ((grammar instanceof ParserGrammar) || (grammar instanceof LexerGrammar))) {
            println("fireSyntacticPredicateStarted();");
        }
        this.syntacticPredLevel++;
        println("try {");
        this.tabs++;
        gen(synPredBlock);
        this.tabs--;
        println("}");
        println("catch (" + this.exceptionThrown + ")");
        println("{");
        this.tabs = this.tabs + 1;
        StringBuilder m5a5 = C0000a.m5a("synPredMatched");
        m5a5.append(synPredBlock.f302ID);
        m5a5.append(" = false;");
        println(m5a5.toString());
        this.tabs--;
        println("}");
        if (this.grammar instanceof TreeWalkerGrammar) {
            m5a2 = C0000a.m5a("_t = __t");
            m5a2.append(synPredBlock.f302ID);
            str3 = ";";
        } else {
            m5a2 = C0000a.m5a("rewind(_m");
            m5a2.append(synPredBlock.f302ID);
            str3 = ");";
        }
        m5a2.append(str3);
        println(m5a2.toString());
        println("inputState.guessing--;");
        Grammar grammar2 = this.grammar;
        if (grammar2.debuggingOutput && ((grammar2 instanceof ParserGrammar) || (grammar2 instanceof LexerGrammar))) {
            StringBuilder m5a6 = C0000a.m5a("if (synPredMatched");
            m5a6.append(synPredBlock.f302ID);
            m5a6.append(")");
            println(m5a6.toString());
            println("  fireSyntacticPredicateSucceeded();");
            println("else");
            println("  fireSyntacticPredicateFailed();");
        }
        this.syntacticPredLevel--;
        this.tabs--;
        println("}");
        println("if ( synPredMatched" + synPredBlock.f302ID + " )");
        println("{");
    }

    public void genTokenDefinitions(TokenManager tokenManager) {
        StringBuilder sb;
        String sb2;
        antlr.collections.impl.Vector vocabulary = tokenManager.getVocabulary();
        println("public const int EOF = 1;");
        println("public const int NULL_TREE_LOOKAHEAD = 3;");
        for (int i = 4; i < vocabulary.size(); i++) {
            String str = (String) vocabulary.elementAt(i);
            if (str != null) {
                if (str.startsWith("\"")) {
                    StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenManager.getTokenSymbol(str);
                    if (stringLiteralSymbol == null) {
                        this.antlrTool.panic("String literal " + str + " not in symbol table");
                    } else if (stringLiteralSymbol.label != null) {
                        StringBuilder m5a = C0000a.m5a("public const int ");
                        m5a.append(stringLiteralSymbol.label);
                        m5a.append(" = ");
                        m5a.append(i);
                        m5a.append(";");
                        sb2 = m5a.toString();
                    } else {
                        String mangleLiteral = mangleLiteral(str);
                        if (mangleLiteral != null) {
                            println("public const int " + mangleLiteral + " = " + i + ";");
                            stringLiteralSymbol.label = mangleLiteral;
                        } else {
                            sb = new StringBuilder();
                            sb.append("// ");
                            sb.append(str);
                            sb.append(" = ");
                            sb.append(i);
                            sb2 = sb.toString();
                        }
                    }
                } else if (!str.startsWith("<")) {
                    sb = new StringBuilder();
                    sb.append("public const int ");
                    sb.append(str);
                    sb.append(" = ");
                    sb.append(i);
                    sb.append(";");
                    sb2 = sb.toString();
                }
                println(sb2);
            }
        }
        println("");
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0064, code lost:
    
        if (r3.startsWith("\"") != false) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void genTokenStrings() {
        println("");
        println("public static readonly string[] tokenNames_ = new string[] {");
        this.tabs++;
        antlr.collections.impl.Vector vocabulary = this.grammar.tokenManager.getVocabulary();
        for (int i = 0; i < vocabulary.size(); i++) {
            String str = (String) vocabulary.elementAt(i);
            if (str == null) {
                StringBuilder m5a = C0000a.m5a("<");
                m5a.append(String.valueOf(i));
                m5a.append(">");
                str = m5a.toString();
            }
            if (!str.startsWith("\"") && !str.startsWith("<")) {
                TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str);
                if (tokenSymbol != null && tokenSymbol.getParaphrase() != null) {
                    str = tokenSymbol.getParaphrase();
                    str = StringUtils.stripFrontBack(str, "\"", "\"");
                }
                print(this.charFormatter.literalString(str));
                if (i != vocabulary.size() - 1) {
                    _print(",");
                }
                _println("");
            }
        }
        this.tabs--;
        println("};");
    }

    public void genTokenTypes(TokenManager tokenManager) {
        setupOutput(tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
        this.tabs = 0;
        genHeader();
        println(this.behavior.getHeaderAction(""));
        CSharpNameSpace cSharpNameSpace = nameSpace;
        if (cSharpNameSpace != null) {
            cSharpNameSpace.emitDeclarations(this.currentOutput);
        }
        this.tabs++;
        StringBuilder m5a = C0000a.m5a("public class ");
        m5a.append(tokenManager.getName());
        m5a.append(CodeGenerator.TokenTypesFileSuffix);
        println(m5a.toString());
        println("{");
        this.tabs++;
        genTokenDefinitions(tokenManager);
        this.tabs--;
        println("}");
        this.tabs--;
        CSharpNameSpace cSharpNameSpace2 = nameSpace;
        if (cSharpNameSpace2 != null) {
            cSharpNameSpace2.emitClosures(this.currentOutput);
        }
        this.currentOutput.close();
        this.currentOutput = null;
        exitIfError();
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(GrammarAtom grammarAtom, String str) {
        StringBuilder m5a;
        String str2;
        String m2a = C0000a.m2a("astFactory.create(", str, ")");
        if (grammarAtom == null) {
            return getASTCreateString(str);
        }
        if (grammarAtom.getASTNodeType() != null) {
            TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(grammarAtom.getText());
            if (tokenSymbol == null || tokenSymbol.getASTNodeType() != grammarAtom.getASTNodeType()) {
                StringBuilder m5a2 = C0000a.m5a("(");
                m5a2.append(grammarAtom.getASTNodeType());
                m5a2.append(") astFactory.create(");
                m5a2.append(str);
                m5a2.append(", \"");
                m5a2.append(grammarAtom.getASTNodeType());
                m5a2.append("\")");
                return m5a2.toString();
            }
            if (tokenSymbol.getASTNodeType() == null) {
                return m2a;
            }
            m5a = C0000a.m5a("(");
            str2 = tokenSymbol.getASTNodeType();
        } else {
            if (!this.usingCustomAST) {
                return m2a;
            }
            m5a = C0000a.m5a("(");
            str2 = this.labeledElementASTType;
        }
        m5a.append(str2);
        m5a.append(") ");
        m5a.append(m2a);
        return m5a.toString();
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(antlr.collections.impl.Vector vector) {
        if (vector.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder m5a = C0000a.m5a("(");
        m5a.append(this.labeledElementASTType);
        m5a.append(") astFactory.make(");
        stringBuffer.append(m5a.toString());
        stringBuffer.append(vector.elementAt(0));
        for (int i = 1; i < vector.size(); i++) {
            StringBuilder m5a2 = C0000a.m5a(", ");
            m5a2.append(vector.elementAt(i));
            stringBuffer.append(m5a2.toString());
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public String getASTCreateString(String str) {
        if (str == null) {
            str = "";
        }
        String m2a = C0000a.m2a("astFactory.create(", str, ")");
        int indexOf = str.indexOf(44);
        if (indexOf != -1) {
            String substring = str.substring(0, indexOf);
            str.substring(indexOf + 1, str.length()).indexOf(44);
            str = substring;
        }
        TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str);
        if (tokenSymbol != null && tokenSymbol.getASTNodeType() != null) {
            StringBuilder m5a = C0000a.m5a("(");
            m5a.append(tokenSymbol.getASTNodeType());
            m5a.append(") ");
            m5a.append(m2a);
            return m5a.toString();
        }
        if (!this.usingCustomAST) {
            return m2a;
        }
        StringBuilder m5a2 = C0000a.m5a("(");
        m5a2.append(this.labeledElementASTType);
        m5a2.append(") ");
        m5a2.append(m2a);
        return m5a2.toString();
    }

    @Override // antlr.CodeGenerator
    public String getBitsetName(int i) {
        return "tokenSet_" + i + "_";
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
        String lookaheadString = lookaheadString(i);
        int[] array = bitSet.toArray();
        if (CodeGenerator.elementsAreRange(array)) {
            return getRangeExpression(i, array);
        }
        int degree = bitSet.degree();
        if (degree == 0) {
            return "true";
        }
        if (degree >= this.bitsetTestThreshold) {
            return getBitsetName(markBitsetForGen(bitSet)) + ".member(" + lookaheadString + ")";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < array.length; i2++) {
            String valueString = getValueString(array[i2]);
            if (i2 > 0) {
                stringBuffer.append("||");
            }
            stringBuffer.append(lookaheadString);
            stringBuffer.append("==");
            stringBuffer.append(valueString);
        }
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
        m5a.append(getValueString(i2));
        m5a.append(" && ");
        m5a.append(lookaheadString(i));
        m5a.append(" <= ");
        return C0000a.m3a(m5a, getValueString(i3), ")");
    }

    public String getTokenTypesClassName() {
        return new String(this.grammar.tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
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

    @Override // antlr.CodeGenerator
    public String processStringForASTConstructor(String str) {
        if (this.usingCustomAST) {
            Grammar grammar = this.grammar;
            if (((grammar instanceof TreeWalkerGrammar) || (grammar instanceof ParserGrammar)) && !this.grammar.tokenManager.tokenDefined(str)) {
                return C0000a.m1a("(AST)", str);
            }
        }
        return str;
    }

    public void setupOutput(String str) {
        this.currentOutput = this.antlrTool.openOutputFile(str + ".cs");
    }

    public String[] split(String str, String str2) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, str2);
        String[] strArr = new String[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            strArr[i] = stringTokenizer.nextToken();
            i++;
        }
        return strArr;
    }
}
