package antlr;

import antlr.actions.cpp.ActionLexer;
import antlr.collections.impl.BitSet;
import antlr.collections.impl.Vector;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class CppCodeGenerator extends CodeGenerator {
    public static final int caseSizeThreshold = 127;
    public static final String postIncludeCpp = "post_include_cpp";
    public static final String postIncludeHpp = "post_include_hpp";
    public static final String preIncludeCpp = "pre_include_cpp";
    public static final String preIncludeHpp = "pre_include_hpp";
    public Vector astTypes;
    public String commonExtraArgs;
    public String commonExtraParams;
    public String commonLocalVars;
    public String currentASTResult;
    public RuleBlock currentRule;
    public String exceptionThrown;
    public String labeledElementASTInit;
    public String labeledElementASTType;
    public String labeledElementInit;
    public String labeledElementType;
    public String lt1Value;
    public String outputFile;
    public int outputLine;
    public Vector semPreds;
    public String throwNoViable;
    public static final String NONUNIQUE = new String();
    public static String namespaceStd = "ANTLR_USE_NAMESPACE(std)";
    public static String namespaceAntlr = "ANTLR_USE_NAMESPACE(antlr)";
    public static NameSpace nameSpace = null;
    public boolean DEBUG_CPP_CODE_GENERATOR = false;
    public int syntacticPredLevel = 0;
    public boolean genAST = false;
    public boolean saveText = false;
    public boolean genHashLines = true;
    public boolean noConstructors = false;
    public boolean usingCustomAST = false;
    public Hashtable treeVariableMap = new Hashtable();
    public Hashtable declaredASTVariables = new Hashtable();
    public int astVarNumber = 1;

    public CppCodeGenerator() {
        this.charFormatter = new CppCharFormatter();
    }

    private void GenRuleInvocation(RuleRefElement ruleRefElement) {
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
            String strProcessActionForSpecialSymbols = processActionForSpecialSymbols(ruleRefElement.args, ruleRefElement.line, this.currentRule, actionTransInfo);
            if (actionTransInfo.assignToRoot || actionTransInfo.refRuleRoot != null) {
                Tool tool = this.antlrTool;
                StringBuilder sbM5a = C0000a.m5a("Arguments of rule reference '");
                sbM5a.append(ruleRefElement.targetRule);
                sbM5a.append("' cannot set or ref #");
                sbM5a.append(this.currentRule.getRuleName());
                sbM5a.append(" on line ");
                sbM5a.append(ruleRefElement.getLine());
                tool.error(sbM5a.toString());
            }
            _print(strProcessActionForSpecialSymbols);
            if (ruleSymbol.block.argAction == null) {
                this.antlrTool.warning(C0000a.m3a(C0000a.m5a("Rule '"), ruleRefElement.targetRule, "' accepts no arguments"), this.grammar.getFilename(), ruleRefElement.getLine(), ruleRefElement.getColumn());
            }
        }
        _println(");");
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _retTree;");
        }
    }

    private boolean charIsDigit(String str, int i) {
        return i < str.length() && Character.isDigit(str.charAt(i));
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x028a  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01cb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0234  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String convertJavaToCppString(String str, boolean z) {
        Tool tool;
        StringBuilder sb;
        String str2;
        String str3;
        String string;
        int i;
        int i2;
        int iDigit;
        int i3;
        int iCharAt;
        char c2;
        boolean z2;
        StringBuilder sbM5a;
        String strEscapeChar;
        String string2;
        int iCharAt2;
        String str4 = new String();
        if (z) {
            if (!str.startsWith("'") || !str.endsWith("'")) {
                tool = this.antlrTool;
                sb = new StringBuilder();
                str2 = "Invalid character literal: '";
                sb.append(str2);
                sb.append(str);
                sb.append("'");
                tool.error(sb.toString());
            }
        } else if (!str.startsWith("\"") || !str.endsWith("\"")) {
            tool = this.antlrTool;
            sb = new StringBuilder();
            str2 = "Invalid character string: '";
            sb.append(str2);
            sb.append(str);
            sb.append("'");
            tool.error(sb.toString());
        }
        String strSubstring = str.substring(1, str.length() - 1);
        Grammar grammar = this.grammar;
        str3 = "";
        if (grammar instanceof LexerGrammar) {
            int size = ((LexerGrammar) grammar).charVocabulary.size() - 1;
            str3 = size > 255 ? "L" : "";
            i = size;
            iDigit = 0;
            string = str4;
            i2 = 0;
        } else {
            string = str4;
            i = 255;
            i2 = 0;
            iDigit = 0;
        }
        while (i2 < strSubstring.length()) {
            if (strSubstring.charAt(i2) == '\\') {
                int i4 = i2 + 1;
                if (strSubstring.length() == i4) {
                    Tool tool2 = this.antlrTool;
                    StringBuilder sbM10b = C0000a.m10b("Invalid escape in char literal: '", str, "' looking at '");
                    sbM10b.append(strSubstring.substring(i2));
                    sbM10b.append("'");
                    tool2.error(sbM10b.toString());
                }
                char cCharAt = strSubstring.charAt(i4);
                if (cCharAt == '\"' || cCharAt == '\'' || cCharAt == '\\') {
                    iCharAt2 = strSubstring.charAt(i4);
                } else if (cCharAt == 'f') {
                    iCharAt2 = 12;
                } else if (cCharAt == 'n') {
                    iCharAt2 = 10;
                } else if (cCharAt == 'r') {
                    iCharAt2 = 13;
                } else if (cCharAt != 'a') {
                    if (cCharAt == 'b') {
                        i2 += 2;
                        iDigit = 8;
                    } else if (cCharAt == 't') {
                        iCharAt2 = 9;
                    } else if (cCharAt != 'u') {
                        switch (cCharAt) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                                i3 = i2 + 2;
                                if (!charIsDigit(strSubstring, i3)) {
                                    iCharAt = strSubstring.charAt(i4) - '0';
                                } else {
                                    int i5 = i2 + 3;
                                    if (!charIsDigit(strSubstring, i5)) {
                                        iCharAt2 = (strSubstring.charAt(i3) - '0') + ((strSubstring.charAt(i4) - '0') * 8);
                                        i2 = i5;
                                        iDigit = iCharAt2;
                                    } else {
                                        iDigit = (strSubstring.charAt(i5) - '0') + ((strSubstring.charAt(i3) - '0') * 8) + ((strSubstring.charAt(i4) - '0') * 8 * 8);
                                        i2 += 4;
                                    }
                                }
                                break;
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                                int i6 = i2 + 2;
                                boolean zCharIsDigit = charIsDigit(strSubstring, i6);
                                strSubstring.charAt(i4);
                                if (zCharIsDigit) {
                                    strSubstring.charAt(i6);
                                    i2 += 3;
                                    break;
                                } else {
                                    i2 = i6;
                                    break;
                                }
                            default:
                                Tool tool3 = this.antlrTool;
                                StringBuilder sbM10b2 = C0000a.m10b("Unhandled escape in char literal: '", str, "' looking at '");
                                sbM10b2.append(strSubstring.substring(i2));
                                sbM10b2.append("'");
                                tool3.error(sbM10b2.toString());
                                iDigit = 0;
                                break;
                        }
                    } else {
                        int i7 = i2 + 5;
                        if (i7 < strSubstring.length()) {
                            int iDigit2 = (Character.digit(strSubstring.charAt(i2 + 4), 16) * 16) + (Character.digit(strSubstring.charAt(i2 + 3), 16) * 16 * 16) + (Character.digit(strSubstring.charAt(i2 + 2), 16) * 16 * 16 * 16);
                            i2 += 6;
                            iDigit = Character.digit(strSubstring.charAt(i7), 16) + iDigit2;
                        } else {
                            Tool tool4 = this.antlrTool;
                            StringBuilder sbM10b3 = C0000a.m10b("Invalid escape in char literal: '", str, "' looking at '");
                            sbM10b3.append(strSubstring.substring(i2));
                            sbM10b3.append("'");
                            tool4.error(sbM10b3.toString());
                        }
                    }
                    if ((this.grammar instanceof LexerGrammar) && iDigit > i) {
                        if (32 <= iDigit || iDigit >= 127) {
                            StringBuilder sbM5a2 = C0000a.m5a("0x");
                            sbM5a2.append(Integer.toString(iDigit, 16));
                            string2 = sbM5a2.toString();
                        } else {
                            string2 = this.charFormatter.escapeChar(iDigit, true);
                        }
                        Tool tool5 = this.antlrTool;
                        StringBuilder sbM5a3 = C0000a.m5a("Character out of range in ");
                        sbM5a3.append(!z ? "char literal" : "string constant");
                        sbM5a3.append(": '");
                        sbM5a3.append(strSubstring);
                        sbM5a3.append("'");
                        tool5.error(sbM5a3.toString());
                        this.antlrTool.error("Vocabulary size: " + i + " Character " + string2);
                    }
                    if (z) {
                        if (i2 != strSubstring.length()) {
                            this.antlrTool.error("Invalid char literal: '" + str + "'");
                        }
                        c2 = 255;
                        if (i > 255) {
                            z2 = true;
                            sbM5a = C0000a.m5a("L'");
                        } else if (iDigit > 255 || (iDigit & 128) == 0) {
                            z2 = true;
                            sbM5a = C0000a.m5a("'");
                        } else {
                            sbM5a = C0000a.m5a("static_cast<unsigned char>('");
                            z2 = true;
                            sbM5a.append(this.charFormatter.escapeChar(iDigit, true));
                            strEscapeChar = "')";
                        }
                        sbM5a.append(this.charFormatter.escapeChar(iDigit, z2));
                        sbM5a.append("'");
                        string = sbM5a.toString();
                    } else {
                        c2 = 255;
                        z2 = true;
                        sbM5a = C0000a.m5a(string);
                        strEscapeChar = this.charFormatter.escapeChar(iDigit, true);
                    }
                    sbM5a.append(strEscapeChar);
                    string = sbM5a.toString();
                } else {
                    iCharAt2 = 7;
                }
                i2 += 2;
                iDigit = iCharAt2;
                if (this.grammar instanceof LexerGrammar) {
                    if (32 <= iDigit) {
                        StringBuilder sbM5a22 = C0000a.m5a("0x");
                        sbM5a22.append(Integer.toString(iDigit, 16));
                        string2 = sbM5a22.toString();
                        Tool tool52 = this.antlrTool;
                        StringBuilder sbM5a32 = C0000a.m5a("Character out of range in ");
                        sbM5a32.append(!z ? "char literal" : "string constant");
                        sbM5a32.append(": '");
                        sbM5a32.append(strSubstring);
                        sbM5a32.append("'");
                        tool52.error(sbM5a32.toString());
                        this.antlrTool.error("Vocabulary size: " + i + " Character " + string2);
                    }
                }
                if (z) {
                }
                sbM5a.append(strEscapeChar);
                string = sbM5a.toString();
            } else {
                i3 = i2 + 1;
                iCharAt = strSubstring.charAt(i2);
            }
            iDigit = iCharAt;
            i2 = i3;
            if (this.grammar instanceof LexerGrammar) {
            }
            if (z) {
            }
            sbM5a.append(strEscapeChar);
            string = sbM5a.toString();
        }
        if (z) {
            return string;
        }
        return str3 + "\"" + string + "\"";
    }

    private String fixNameSpaceOption(String str) {
        String strStripFrontBack = StringUtils.stripFrontBack(str, "\"", "\"");
        return (strStripFrontBack.length() <= 2 || strStripFrontBack.substring(strStripFrontBack.length() - 2, strStripFrontBack.length()).equals("::")) ? strStripFrontBack : C0000a.m1a(strStripFrontBack, "::");
    }

    private void genBlockFinish(CppBlockFinishingInfo cppBlockFinishingInfo, String str) {
        if (cppBlockFinishingInfo.needAnErrorClause && (cppBlockFinishingInfo.generatedAnIf || cppBlockFinishingInfo.generatedSwitch)) {
            println(cppBlockFinishingInfo.generatedAnIf ? "else {" : "{");
            this.tabs++;
            println(str);
            this.tabs--;
            println("}");
        }
        String str2 = cppBlockFinishingInfo.postscript;
        if (str2 != null) {
            println(str2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void genElementAST(AlternativeElement alternativeElement) {
        String label;
        String string;
        String str;
        StringBuilder sb;
        StringBuilder sbM9b;
        String aSTCreateString;
        StringBuilder sbM9b2;
        String aSTCreateString2;
        String string2;
        Grammar grammar = this.grammar;
        if ((grammar instanceof TreeWalkerGrammar) && !grammar.buildAST) {
            if (alternativeElement.getLabel() == null) {
                String str2 = this.lt1Value;
                StringBuilder sbM5a = C0000a.m5a("tmp");
                sbM5a.append(this.astVarNumber);
                sbM5a.append("_AST");
                String string3 = sbM5a.toString();
                this.astVarNumber++;
                mapTreeVariable(alternativeElement, string3);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.labeledElementASTType);
                sb2.append(" ");
                sb2.append(string3);
                sb2.append("_in = ");
                C0000a.m8a(sb2, str2, ";", this);
                return;
            }
            return;
        }
        if (this.grammar.buildAST && this.syntacticPredLevel == 0) {
            boolean z = false;
            boolean z2 = this.genAST && !(alternativeElement.getLabel() == null && alternativeElement.getAutoGenType() == 3);
            if (alternativeElement.getAutoGenType() != 3 && (alternativeElement instanceof TokenRefElement)) {
                z2 = true;
            }
            if (this.grammar.hasSyntacticPredicate && z2) {
                z = true;
            }
            if (alternativeElement.getLabel() != null) {
                label = alternativeElement.getLabel();
                string = alternativeElement.getLabel();
            } else {
                label = this.lt1Value;
                StringBuilder sbM5a2 = C0000a.m5a("tmp");
                sbM5a2.append(this.astVarNumber);
                string = sbM5a2.toString();
                this.astVarNumber++;
            }
            if (z2) {
                if (alternativeElement instanceof GrammarAtom) {
                    GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
                    if (grammarAtom.getASTNodeType() != null) {
                        StringBuilder sbM5a3 = C0000a.m5a("Ref");
                        sbM5a3.append(grammarAtom.getASTNodeType());
                        string2 = sbM5a3.toString();
                    } else {
                        string2 = this.labeledElementASTType;
                    }
                    genASTDeclaration(alternativeElement, string, string2);
                }
            }
            String strM1a = C0000a.m1a(string, "_AST");
            mapTreeVariable(alternativeElement, strM1a);
            if (this.grammar instanceof TreeWalkerGrammar) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(this.labeledElementASTType);
                sb3.append(" ");
                sb3.append(strM1a);
                sb3.append("_in = ");
                C0000a.m8a(sb3, this.labeledElementASTInit, ";", this);
            }
            if (z) {
                println("if ( inputState->guessing == 0 ) {");
                this.tabs++;
            }
            if (alternativeElement.getLabel() != null) {
                if (alternativeElement instanceof GrammarAtom) {
                    sbM9b2 = C0000a.m9b(strM1a, " = ");
                    aSTCreateString2 = getASTCreateString((GrammarAtom) alternativeElement, label);
                } else {
                    sbM9b2 = C0000a.m9b(strM1a, " = ");
                    aSTCreateString2 = getASTCreateString(label);
                }
                sbM9b2.append(aSTCreateString2);
                sbM9b2.append(";");
                println(sbM9b2.toString());
            }
            if (alternativeElement.getLabel() == null && z2) {
                String str3 = this.lt1Value;
                if (alternativeElement instanceof GrammarAtom) {
                    sbM9b = C0000a.m9b(strM1a, " = ");
                    aSTCreateString = getASTCreateString((GrammarAtom) alternativeElement, str3);
                } else {
                    sbM9b = C0000a.m9b(strM1a, " = ");
                    aSTCreateString = getASTCreateString(str3);
                }
                sbM9b.append(aSTCreateString);
                sbM9b.append(";");
                println(sbM9b.toString());
                if (this.grammar instanceof TreeWalkerGrammar) {
                    println(strM1a + "_in = " + str3 + ";");
                }
            }
            if (this.genAST) {
                int autoGenType = alternativeElement.getAutoGenType();
                if (autoGenType == 1) {
                    str = "astFactory->addASTChild(currentAST, ";
                    if (!this.usingCustomAST && (!(alternativeElement instanceof GrammarAtom) || ((GrammarAtom) alternativeElement).getASTNodeType() == null)) {
                        sb = new StringBuilder();
                        sb.append(str);
                        sb.append(strM1a);
                        sb.append(");");
                    }
                    sb = C0000a.m5a(str);
                    sb.append(namespaceAntlr);
                    sb.append("RefAST(");
                    sb.append(strM1a);
                    sb.append("));");
                } else if (autoGenType == 2) {
                    str = "astFactory->makeASTRoot(currentAST, ";
                    if (!this.usingCustomAST && (!(alternativeElement instanceof GrammarAtom) || ((GrammarAtom) alternativeElement).getASTNodeType() == null)) {
                        sb = new StringBuilder();
                        sb.append(str);
                        sb.append(strM1a);
                        sb.append(");");
                    }
                    sb = C0000a.m5a(str);
                    sb.append(namespaceAntlr);
                    sb.append("RefAST(");
                    sb.append(strM1a);
                    sb.append("));");
                }
                println(sb.toString());
            }
            if (z) {
                this.tabs--;
                println("}");
            }
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
            println("}");
            genErrorHandler(exceptionSpecFindExceptionSpec);
        }
    }

    private void genErrorHandler(ExceptionSpec exceptionSpec) {
        for (int i = 0; i < exceptionSpec.handlers.size(); i++) {
            ExceptionHandler exceptionHandler = (ExceptionHandler) exceptionSpec.handlers.elementAt(i);
            StringBuilder sbM5a = C0000a.m5a("catch (");
            sbM5a.append(exceptionHandler.exceptionTypeAndName.getText());
            sbM5a.append(") {");
            println(sbM5a.toString());
            this.tabs++;
            if (this.grammar.hasSyntacticPredicate) {
                println("if (inputState->guessing==0) {");
                this.tabs++;
            }
            ActionTransInfo actionTransInfo = new ActionTransInfo();
            genLineNo(exceptionHandler.action);
            printAction(processActionForSpecialSymbols(exceptionHandler.action.getText(), exceptionHandler.action.getLine(), this.currentRule, actionTransInfo));
            genLineNo2();
            if (this.grammar.hasSyntacticPredicate) {
                this.tabs--;
                println("} else {");
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
        String strEncodeLexerRuleName = alternativeElement.enclosingRuleName;
        if (this.grammar instanceof LexerGrammar) {
            strEncodeLexerRuleName = CodeGenerator.encodeLexerRuleName(strEncodeLexerRuleName);
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(strEncodeLexerRuleName);
        if (ruleSymbol == null) {
            this.antlrTool.panic("Enclosing rule not found!");
        }
        if (ruleSymbol.block.findExceptionSpec(alternativeElement.getLabel()) != null) {
            println("try { // for error handling");
            this.tabs++;
        }
    }

    private void genLiteralsTest() {
        println("_ttype = testLiteralsTable(_ttype);");
    }

    private void genLiteralsTestForPartialToken() {
        println("_ttype = testLiteralsTable(text.substr(_begin, text.length()-_begin),_ttype);");
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
            return id.equals("EOF") ? C0000a.m3a(new StringBuilder(), namespaceAntlr, "Token::EOF_TYPE") : id;
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
            return "_t->getType()";
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
        Token option;
        Token option2;
        String strStripFrontBack;
        Token option3;
        String strStripFrontBack2;
        Token option4;
        Token option5;
        String strStripFrontBack3;
        StringBuilder sbM5a;
        String str;
        String str2;
        Token option6;
        String strStripFrontBack4;
        boolean z = grammar instanceof ParserGrammar;
        if (z || (grammar instanceof LexerGrammar) || (grammar instanceof TreeWalkerGrammar)) {
            NameSpace nameSpace2 = this.antlrTool.nameSpace;
            if (nameSpace2 != null) {
                nameSpace = nameSpace2;
            }
            String str3 = this.antlrTool.namespaceStd;
            if (str3 != null) {
                namespaceStd = fixNameSpaceOption(str3);
            }
            String str4 = this.antlrTool.namespaceAntlr;
            if (str4 != null) {
                namespaceAntlr = fixNameSpaceOption(str4);
            }
            this.genHashLines = this.antlrTool.genHashLines;
            if (grammar.hasOption("namespace") && (option4 = grammar.getOption("namespace")) != null) {
                nameSpace = new NameSpace(option4.getText());
            }
            if (grammar.hasOption("namespaceAntlr") && (option3 = grammar.getOption("namespaceAntlr")) != null && (strStripFrontBack2 = StringUtils.stripFrontBack(option3.getText(), "\"", "\"")) != null) {
                if (strStripFrontBack2.length() > 2 && !strStripFrontBack2.substring(strStripFrontBack2.length() - 2, strStripFrontBack2.length()).equals("::")) {
                    strStripFrontBack2 = C0000a.m1a(strStripFrontBack2, "::");
                }
                namespaceAntlr = strStripFrontBack2;
            }
            if (grammar.hasOption("namespaceStd") && (option2 = grammar.getOption("namespaceStd")) != null && (strStripFrontBack = StringUtils.stripFrontBack(option2.getText(), "\"", "\"")) != null) {
                if (strStripFrontBack.length() > 2 && !strStripFrontBack.substring(strStripFrontBack.length() - 2, strStripFrontBack.length()).equals("::")) {
                    strStripFrontBack = C0000a.m1a(strStripFrontBack, "::");
                }
                namespaceStd = strStripFrontBack;
            }
            if (grammar.hasOption("genHashLines") && (option = grammar.getOption("genHashLines")) != null) {
                this.genHashLines = StringUtils.stripFrontBack(option.getText(), "\"", "\"").equals("true");
            }
            this.noConstructors = this.antlrTool.noConstructors;
            if (grammar.hasOption("noConstructors")) {
                Token option7 = grammar.getOption("noConstructors");
                if (option7 != null && !option7.getText().equals("true") && !option7.getText().equals("false")) {
                    Tool tool = this.antlrTool;
                    tool.error("noConstructors option must be true or false", tool.getGrammarFile(), option7.getLine(), option7.getColumn());
                }
                this.noConstructors = option7.getText().equals("true");
            }
        }
        if (z) {
            this.labeledElementASTType = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST");
            this.labeledElementASTInit = C0000a.m3a(new StringBuilder(), namespaceAntlr, "nullAST");
            if (grammar.hasOption("ASTLabelType") && (option6 = grammar.getOption("ASTLabelType")) != null && (strStripFrontBack4 = StringUtils.stripFrontBack(option6.getText(), "\"", "\"")) != null) {
                this.usingCustomAST = true;
                this.labeledElementASTType = strStripFrontBack4;
                this.labeledElementASTInit = C0000a.m3a(C0000a.m9b(strStripFrontBack4, "("), namespaceAntlr, "nullAST)");
            }
            this.labeledElementType = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefToken ");
            this.labeledElementInit = C0000a.m3a(new StringBuilder(), namespaceAntlr, "nullToken");
            this.commonExtraArgs = "";
            this.commonExtraParams = "";
            this.commonLocalVars = "";
            this.lt1Value = "LT(1)";
            this.exceptionThrown = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RecognitionException");
            sbM5a = C0000a.m5a("throw ");
            str = namespaceAntlr;
            str2 = "NoViableAltException(LT(1), getFilename());";
        } else {
            if (!(grammar instanceof LexerGrammar)) {
                if (!(grammar instanceof TreeWalkerGrammar)) {
                    this.antlrTool.panic("Unknown grammar type");
                    return;
                }
                this.labeledElementInit = C0000a.m3a(new StringBuilder(), namespaceAntlr, "nullAST");
                this.labeledElementASTInit = C0000a.m3a(new StringBuilder(), namespaceAntlr, "nullAST");
                this.labeledElementASTType = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST");
                this.labeledElementType = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST");
                this.commonExtraParams = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST _t");
                this.throwNoViable = C0000a.m3a(C0000a.m5a("throw "), namespaceAntlr, "NoViableAltException(_t);");
                this.lt1Value = "_t";
                if (grammar.hasOption("ASTLabelType") && (option5 = grammar.getOption("ASTLabelType")) != null && (strStripFrontBack3 = StringUtils.stripFrontBack(option5.getText(), "\"", "\"")) != null) {
                    this.usingCustomAST = true;
                    this.labeledElementASTType = strStripFrontBack3;
                    this.labeledElementType = strStripFrontBack3;
                    this.labeledElementInit = C0000a.m3a(C0000a.m9b(strStripFrontBack3, "("), namespaceAntlr, "nullAST)");
                    this.labeledElementASTInit = this.labeledElementInit;
                    this.commonExtraParams = C0000a.m1a(strStripFrontBack3, " _t");
                    StringBuilder sbM5a2 = C0000a.m5a("throw ");
                    sbM5a2.append(namespaceAntlr);
                    sbM5a2.append("NoViableAltException(");
                    this.throwNoViable = C0000a.m3a(sbM5a2, namespaceAntlr, "RefAST(_t));");
                    this.lt1Value = "_t";
                }
                if (!grammar.hasOption("ASTLabelType")) {
                    grammar.setOption("ASTLabelType", new Token(6, C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST")));
                }
                this.commonExtraArgs = "_t";
                this.commonLocalVars = "";
                this.exceptionThrown = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RecognitionException");
                return;
            }
            this.labeledElementType = "char ";
            this.labeledElementInit = "'\\0'";
            this.commonExtraArgs = "";
            this.commonExtraParams = "bool _createToken";
            StringBuilder sbM5a3 = C0000a.m5a("int _ttype; ");
            sbM5a3.append(namespaceAntlr);
            sbM5a3.append("RefToken _token; ");
            this.commonLocalVars = C0000a.m3a(sbM5a3, namespaceStd, "string::size_type _begin = text.length();");
            this.lt1Value = "LA(1)";
            this.exceptionThrown = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RecognitionException");
            sbM5a = C0000a.m5a("throw ");
            str = namespaceAntlr;
            str2 = "NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());";
        }
        this.throwNoViable = C0000a.m3a(sbM5a, str, str2);
    }

    public static boolean suitableForCaseExpression(Alternative alternative) {
        return alternative.lookaheadDepth == 1 && alternative.semPred == null && !alternative.cache[1].containsEpsilon() && alternative.cache[1].fset.degree() <= 127;
    }

    @Override // antlr.CodeGenerator
    public void _print(String str) {
        if (str != null) {
            this.outputLine = countLines(str) + this.outputLine;
            this.currentOutput.print(str);
        }
    }

    @Override // antlr.CodeGenerator
    public void _printAction(String str) {
        if (str != null) {
            this.outputLine = countLines(str) + 1 + this.outputLine;
            super._printAction(str);
        }
    }

    @Override // antlr.CodeGenerator
    public void _println(String str) {
        if (str != null) {
            this.outputLine = countLines(str) + 1 + this.outputLine;
            this.currentOutput.println(str);
        }
    }

    public int addSemPred(String str) {
        this.semPreds.appendElement(str);
        return this.semPreds.size() - 1;
    }

    public int countLines(String str) {
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == '\n') {
                i++;
            }
        }
        return i;
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
                if (grammar.debuggingOutput) {
                    this.antlrTool.error(grammar.getFilename() + ": C++ mode does not support -debug");
                }
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
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("genAction(" + actionElement + ")");
        }
        if (actionElement.isSemPred) {
            genSemPred(actionElement.actionText, actionElement.line);
            return;
        }
        if (this.grammar.hasSyntacticPredicate) {
            println("if ( inputState->guessing==0 ) {");
            this.tabs++;
        }
        ActionTransInfo actionTransInfo = new ActionTransInfo();
        String strProcessActionForSpecialSymbols = processActionForSpecialSymbols(actionElement.actionText, actionElement.getLine(), this.currentRule, actionTransInfo);
        if (actionTransInfo.refRuleRoot != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(actionTransInfo.refRuleRoot);
            sb.append(" = ");
            C0000a.m8a(sb, this.labeledElementASTType, "(currentAST.root);", this);
        }
        genLineNo(actionElement);
        printAction(strProcessActionForSpecialSymbols);
        genLineNo2();
        if (actionTransInfo.assignToRoot) {
            StringBuilder sbM5a = C0000a.m5a("currentAST.root = ");
            sbM5a.append(actionTransInfo.refRuleRoot);
            sbM5a.append(";");
            println(sbM5a.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("if ( ");
            sb2.append(actionTransInfo.refRuleRoot);
            sb2.append("!=");
            C0000a.m8a(sb2, this.labeledElementASTInit, " &&", this);
            this.tabs++;
            println(actionTransInfo.refRuleRoot + "->getFirstChild() != " + this.labeledElementASTInit + " )");
            StringBuilder sb3 = new StringBuilder();
            sb3.append("  currentAST.child = ");
            C0000a.m8a(sb3, actionTransInfo.refRuleRoot, "->getFirstChild();", this);
            this.tabs = this.tabs + (-1);
            println("else");
            this.tabs++;
            C0000a.m8a(C0000a.m5a("currentAST.child = "), actionTransInfo.refRuleRoot, ";", this);
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
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("gen(" + alternativeBlock + ")");
        }
        println("{");
        genBlockPreamble(alternativeBlock);
        genBlockInitAction(alternativeBlock);
        String str = this.currentASTResult;
        if (alternativeBlock.getLabel() != null) {
            this.currentASTResult = alternativeBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(alternativeBlock);
        genBlockFinish(genCommonBlock(alternativeBlock, true), this.throwNoViable);
        println("}");
        this.currentASTResult = str;
    }

    @Override // antlr.CodeGenerator
    public void gen(BlockEndElement blockEndElement) {
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("genRuleEnd(" + blockEndElement + ")");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(CharLiteralElement charLiteralElement) {
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("genChar(" + charLiteralElement + ")");
        }
        if (!(this.grammar instanceof LexerGrammar)) {
            this.antlrTool.error("cannot ref character literals in grammar: " + charLiteralElement);
        }
        if (charLiteralElement.getLabel() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(charLiteralElement.getLabel());
            sb.append(" = ");
            C0000a.m8a(sb, this.lt1Value, ";", this);
        }
        boolean z = this.saveText;
        this.saveText = z && charLiteralElement.getAutoGenType() == 1;
        if (!this.saveText || charLiteralElement.getAutoGenType() == 3) {
            println("_saveIndex = text.length();");
        }
        print(charLiteralElement.not ? "matchNot(" : "match(");
        _print(convertJavaToCppString(charLiteralElement.atomText, true));
        _println(" /* charlit */ );");
        if (!this.saveText || charLiteralElement.getAutoGenType() == 3) {
            println("text.erase(_saveIndex);");
        }
        this.saveText = z;
    }

    @Override // antlr.CodeGenerator
    public void gen(CharRangeElement charRangeElement) {
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            PrintStream printStream = System.out;
            StringBuilder sbM5a = C0000a.m5a("genCharRangeElement(");
            sbM5a.append(charRangeElement.beginText);
            sbM5a.append("..");
            sbM5a.append(charRangeElement.endText);
            sbM5a.append(")");
            printStream.println(sbM5a.toString());
        }
        if (!(this.grammar instanceof LexerGrammar)) {
            this.antlrTool.error("cannot ref character range in grammar: " + charRangeElement);
        }
        if (charRangeElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(charRangeElement.getLabel());
            sb.append(" = ");
            C0000a.m8a(sb, this.lt1Value, ";", this);
        }
        boolean z = (this.grammar instanceof LexerGrammar) && (!this.saveText || charRangeElement.getAutoGenType() == 3);
        if (z) {
            println("_saveIndex=text.length();");
        }
        StringBuilder sbM5a2 = C0000a.m5a("matchRange(");
        sbM5a2.append(convertJavaToCppString(charRangeElement.beginText, true));
        sbM5a2.append(",");
        C0000a.m8a(sbM5a2, convertJavaToCppString(charRangeElement.endText, true), ");", this);
        if (z) {
            println("text.erase(_saveIndex);");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(LexerGrammar lexerGrammar) {
        if (lexerGrammar.debuggingOutput) {
            this.semPreds = new Vector();
        }
        if (lexerGrammar.charVocabulary.size() > 256) {
            this.antlrTool.warning(lexerGrammar.getFilename() + ": Vocabularies of this size still experimental in C++ mode (vocabulary size now: " + lexerGrammar.charVocabulary.size() + ")");
        }
        setGrammar(lexerGrammar);
        if (!(this.grammar instanceof LexerGrammar)) {
            this.antlrTool.panic("Internal error generating lexer");
        }
        genBody(lexerGrammar);
        genInclude(lexerGrammar);
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00d2  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(OneOrMoreBlock oneOrMoreBlock) {
        StringBuilder sbM5a;
        String string;
        int i;
        boolean z;
        int i2;
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("gen+(" + oneOrMoreBlock + ")");
        }
        println("{ // ( ... )+");
        genBlockPreamble(oneOrMoreBlock);
        if (oneOrMoreBlock.getLabel() != null) {
            sbM5a = C0000a.m5a("_cnt_");
            sbM5a.append(oneOrMoreBlock.getLabel());
        } else {
            sbM5a = C0000a.m5a("_cnt");
            sbM5a.append(oneOrMoreBlock.f302ID);
        }
        String string2 = sbM5a.toString();
        println("int " + string2 + "=0;");
        if (oneOrMoreBlock.getLabel() != null) {
            string = oneOrMoreBlock.getLabel();
        } else {
            StringBuilder sbM5a2 = C0000a.m5a("_loop");
            sbM5a2.append(oneOrMoreBlock.f302ID);
            string = sbM5a2.toString();
        }
        println("for (;;) {");
        this.tabs++;
        genBlockInitAction(oneOrMoreBlock);
        String str = this.currentASTResult;
        if (oneOrMoreBlock.getLabel() != null) {
            this.currentASTResult = oneOrMoreBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(oneOrMoreBlock);
        int i3 = this.grammar.maxk;
        if (oneOrMoreBlock.greedy || (i2 = oneOrMoreBlock.exitLookaheadDepth) > i3 || !oneOrMoreBlock.exitCache[i2].containsEpsilon()) {
            if (oneOrMoreBlock.greedy || oneOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
                i = i3;
                z = false;
            }
            if (z) {
                if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
                    PrintStream printStream = System.out;
                    StringBuilder sbM5a3 = C0000a.m5a("nongreedy (...)+ loop; exit depth is ");
                    sbM5a3.append(oneOrMoreBlock.exitLookaheadDepth);
                    printStream.println(sbM5a3.toString());
                }
                String lookaheadTestExpression = getLookaheadTestExpression(oneOrMoreBlock.exitCache, i);
                println("// nongreedy exit test");
                println("if ( " + string2 + ">=1 && " + lookaheadTestExpression + ") goto " + string + ";");
            }
            CppBlockFinishingInfo cppBlockFinishingInfoGenCommonBlock = genCommonBlock(oneOrMoreBlock, false);
            StringBuilder sb = new StringBuilder();
            sb.append("if ( ");
            sb.append(string2);
            sb.append(">=1 ) { goto ");
            sb.append(string);
            sb.append("; } else {");
            genBlockFinish(cppBlockFinishingInfoGenCommonBlock, C0000a.m3a(sb, this.throwNoViable, "}"));
            println(string2 + "++;");
            this.tabs = this.tabs - 1;
            println("}");
            println(string + ":;");
            println("}  // ( ... )+");
            this.currentASTResult = str;
        }
        i3 = oneOrMoreBlock.exitLookaheadDepth;
        i = i3;
        z = true;
        if (z) {
        }
        CppBlockFinishingInfo cppBlockFinishingInfoGenCommonBlock2 = genCommonBlock(oneOrMoreBlock, false);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("if ( ");
        sb2.append(string2);
        sb2.append(">=1 ) { goto ");
        sb2.append(string);
        sb2.append("; } else {");
        genBlockFinish(cppBlockFinishingInfoGenCommonBlock2, C0000a.m3a(sb2, this.throwNoViable, "}"));
        println(string2 + "++;");
        this.tabs = this.tabs - 1;
        println("}");
        println(string + ":;");
        println("}  // ( ... )+");
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
        genBody(parserGrammar);
        genInclude(parserGrammar);
    }

    @Override // antlr.CodeGenerator
    public void gen(RuleRefElement ruleRefElement) {
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
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
            sb.append(" = (_t == ASTNULL) ? ");
            sb.append(this.labeledElementASTInit);
            sb.append(" : ");
            C0000a.m8a(sb, this.lt1Value, ";", this);
        }
        if ((this.grammar instanceof LexerGrammar) && (!this.saveText || ruleRefElement.getAutoGenType() == 3)) {
            println("_saveIndex = text.length();");
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
            println("text.erase(_saveIndex);");
        }
        if (this.syntacticPredLevel == 0) {
            Grammar grammar = this.grammar;
            boolean z = grammar.hasSyntacticPredicate && ((grammar.buildAST && ruleRefElement.getLabel() != null) || (this.genAST && ruleRefElement.getAutoGenType() == 1));
            if (z) {
                println("if (inputState->guessing==0) {");
                this.tabs++;
            }
            if (this.grammar.buildAST && ruleRefElement.getLabel() != null) {
                println(ruleRefElement.getLabel() + "_AST = returnAST;");
            }
            if (this.genAST) {
                int autoGenType = ruleRefElement.getAutoGenType();
                if (autoGenType != 1) {
                    if (autoGenType == 2) {
                        this.antlrTool.error("Internal: encountered ^ after rule reference");
                    }
                } else if (this.usingCustomAST) {
                    C0000a.m8a(C0000a.m5a("astFactory->addASTChild(currentAST, "), namespaceAntlr, "RefAST(returnAST));", this);
                } else {
                    println("astFactory->addASTChild( currentAST, returnAST );");
                }
            }
            if ((this.grammar instanceof LexerGrammar) && ruleRefElement.getLabel() != null) {
                println(ruleRefElement.getLabel() + "=_returnToken;");
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
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("genString(" + stringLiteralElement + ")");
        }
        if (stringLiteralElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(stringLiteralElement.getLabel());
            sb.append(" = ");
            C0000a.m8a(sb, this.lt1Value, ";", this);
        }
        genElementAST(stringLiteralElement);
        boolean z = this.saveText;
        this.saveText = z && stringLiteralElement.getAutoGenType() == 1;
        genMatch(stringLiteralElement);
        this.saveText = z;
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t->getNextSibling();");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRangeElement tokenRangeElement) {
        genErrorTryForElement(tokenRangeElement);
        if (tokenRangeElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(tokenRangeElement.getLabel());
            sb.append(" = ");
            C0000a.m8a(sb, this.lt1Value, ";", this);
        }
        genElementAST(tokenRangeElement);
        StringBuilder sbM5a = C0000a.m5a("matchRange(");
        sbM5a.append(tokenRangeElement.beginText);
        sbM5a.append(",");
        C0000a.m8a(sbM5a, tokenRangeElement.endText, ");", this);
        genErrorCatchForElement(tokenRangeElement);
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRefElement tokenRefElement) {
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
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
            C0000a.m8a(sb, this.lt1Value, ";", this);
        }
        genElementAST(tokenRefElement);
        genMatch(tokenRefElement);
        genErrorCatchForElement(tokenRefElement);
        if (this.grammar instanceof TreeWalkerGrammar) {
            println("_t = _t->getNextSibling();");
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeElement treeElement) {
        println(this.labeledElementType + " __t" + treeElement.f302ID + " = _t;");
        if (treeElement.root.getLabel() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(treeElement.root.getLabel());
            sb.append(" = (_t == ");
            sb.append(this.labeledElementType);
            sb.append("(ASTNULL)) ? ");
            C0000a.m8a(sb, this.labeledElementASTInit, " : _t;", this);
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
            println(namespaceAntlr + "ASTPair __currentAST" + treeElement.f302ID + " = currentAST;");
            println("currentAST.root = currentAST.child;");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("currentAST.child = ");
            C0000a.m8a(sb2, this.labeledElementASTInit, ";", this);
        }
        GrammarAtom grammarAtom = treeElement.root;
        if (grammarAtom instanceof WildcardElement) {
            C0000a.m8a(C0000a.m5a("if ( _t == ASTNULL ) throw "), namespaceAntlr, "MismatchedTokenException();", this);
        } else {
            genMatch(grammarAtom);
        }
        println("_t = _t->getFirstChild();");
        for (int i = 0; i < treeElement.getAlternatives().size(); i++) {
            for (AlternativeElement alternativeElement = treeElement.getAlternativeAt(i).head; alternativeElement != null; alternativeElement = alternativeElement.next) {
                alternativeElement.generate();
            }
        }
        if (this.grammar.buildAST) {
            StringBuilder sbM5a = C0000a.m5a("currentAST = __currentAST");
            sbM5a.append(treeElement.f302ID);
            sbM5a.append(";");
            println(sbM5a.toString());
        }
        StringBuilder sbM5a2 = C0000a.m5a("_t = __t");
        sbM5a2.append(treeElement.f302ID);
        sbM5a2.append(";");
        println(sbM5a2.toString());
        println("_t = _t->getNextSibling();");
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeWalkerGrammar treeWalkerGrammar) {
        setGrammar(treeWalkerGrammar);
        if (!(this.grammar instanceof TreeWalkerGrammar)) {
            this.antlrTool.panic("Internal error generating tree-walker");
        }
        genBody(treeWalkerGrammar);
        genInclude(treeWalkerGrammar);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(WildcardElement wildcardElement) {
        StringBuilder sbM5a;
        String valueString;
        String str;
        if (wildcardElement.getLabel() != null && this.syntacticPredLevel == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(wildcardElement.getLabel());
            sb.append(" = ");
            C0000a.m8a(sb, this.lt1Value, ";", this);
        }
        genElementAST(wildcardElement);
        Grammar grammar = this.grammar;
        if (grammar instanceof TreeWalkerGrammar) {
            sbM5a = C0000a.m5a("if ( _t == ");
            sbM5a.append(this.labeledElementASTInit);
            sbM5a.append(" ) throw ");
            valueString = namespaceAntlr;
            str = "MismatchedTokenException();";
        } else {
            boolean z = grammar instanceof LexerGrammar;
            if (z) {
                if (z && (!this.saveText || wildcardElement.getAutoGenType() == 3)) {
                    println("_saveIndex = text.length();");
                }
                println("matchNot(EOF/*_CHAR*/);");
                if ((this.grammar instanceof LexerGrammar) && (!this.saveText || wildcardElement.getAutoGenType() == 3)) {
                    println("text.erase(_saveIndex);");
                }
                if (this.grammar instanceof TreeWalkerGrammar) {
                    return;
                }
                println("_t = _t->getNextSibling();");
                return;
            }
            sbM5a = C0000a.m5a("matchNot(");
            valueString = getValueString(1);
            str = ");";
        }
        C0000a.m8a(sbM5a, valueString, str, this);
        if (this.grammar instanceof TreeWalkerGrammar) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0096  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(ZeroOrMoreBlock zeroOrMoreBlock) {
        String string;
        int i;
        boolean z;
        int i2;
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("gen*(" + zeroOrMoreBlock + ")");
        }
        println("{ // ( ... )*");
        genBlockPreamble(zeroOrMoreBlock);
        if (zeroOrMoreBlock.getLabel() != null) {
            string = zeroOrMoreBlock.getLabel();
        } else {
            StringBuilder sbM5a = C0000a.m5a("_loop");
            sbM5a.append(zeroOrMoreBlock.f302ID);
            string = sbM5a.toString();
        }
        println("for (;;) {");
        this.tabs++;
        genBlockInitAction(zeroOrMoreBlock);
        String str = this.currentASTResult;
        if (zeroOrMoreBlock.getLabel() != null) {
            this.currentASTResult = zeroOrMoreBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(zeroOrMoreBlock);
        int i3 = this.grammar.maxk;
        if (zeroOrMoreBlock.greedy || (i2 = zeroOrMoreBlock.exitLookaheadDepth) > i3 || !zeroOrMoreBlock.exitCache[i2].containsEpsilon()) {
            if (zeroOrMoreBlock.greedy || zeroOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
                i = i3;
                z = false;
            }
            if (z) {
                if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
                    PrintStream printStream = System.out;
                    StringBuilder sbM5a2 = C0000a.m5a("nongreedy (...)* loop; exit depth is ");
                    sbM5a2.append(zeroOrMoreBlock.exitLookaheadDepth);
                    printStream.println(sbM5a2.toString());
                }
                String lookaheadTestExpression = getLookaheadTestExpression(zeroOrMoreBlock.exitCache, i);
                println("// nongreedy exit test");
                StringBuilder sb = new StringBuilder();
                sb.append("if (");
                sb.append(lookaheadTestExpression);
                sb.append(") goto ");
                C0000a.m8a(sb, string, ";", this);
            }
            genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), C0000a.m2a("goto ", string, ";"));
            this.tabs--;
            println("}");
            println(string + ":;");
            println("} // ( ... )*");
            this.currentASTResult = str;
        }
        i3 = zeroOrMoreBlock.exitLookaheadDepth;
        i = i3;
        z = true;
        if (z) {
        }
        genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), C0000a.m2a("goto ", string, ";"));
        this.tabs--;
        println("}");
        println(string + ":;");
        println("} // ( ... )*");
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
        String strM3a = this.labeledElementASTInit;
        if (alternativeElement instanceof GrammarAtom) {
            GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
            if (grammarAtom.getASTNodeType() != null) {
                StringBuilder sbM5a = C0000a.m5a("Ref");
                sbM5a.append(grammarAtom.getASTNodeType());
                sbM5a.append("(");
                strM3a = C0000a.m3a(sbM5a, this.labeledElementASTInit, ")");
            }
        }
        println(str2 + " " + str + "_AST = " + strM3a + ";");
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
            println("try {      // for error handling");
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
                    sb.append("_AST = ");
                    C0000a.m8a(sb, this.labeledElementASTType, "(currentAST.root);", this);
                } else {
                    println(ruleBlock.getRuleName() + "_AST = currentAST.root;");
                }
            } else if (alternativeBlock.getLabel() != null) {
                this.antlrTool.warning("Labeled subrules are not implemented", this.grammar.getFilename(), alternativeBlock.getLine(), alternativeBlock.getColumn());
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

    public void genBitsets(Vector vector, int i, String str) {
        StringBuilder sbM5a;
        String tokenStringAt;
        TokenManager tokenManager = this.grammar.tokenManager;
        println("");
        for (int i2 = 0; i2 < vector.size(); i2++) {
            BitSet bitSet = (BitSet) vector.elementAt(i2);
            bitSet.growToInclude(i);
            println("const unsigned long " + str + getBitsetName(i2) + "_data_[] = { " + bitSet.toStringOfHalfWords() + " };");
            String string = "// ";
            for (int i3 = 0; i3 < tokenManager.getVocabulary().size(); i3++) {
                if (bitSet.member(i3)) {
                    if (!(this.grammar instanceof LexerGrammar)) {
                        sbM5a = C0000a.m5a(string);
                        tokenStringAt = tokenManager.getTokenStringAt(i3);
                    } else if (32 > i3 || i3 >= 127 || i3 == 92) {
                        sbM5a = C0000a.m9b(string, "0x");
                        tokenStringAt = Integer.toString(i3, 16);
                    } else {
                        sbM5a = C0000a.m5a(string);
                        tokenStringAt = this.charFormatter.escapeChar(i3, true);
                    }
                    sbM5a.append(tokenStringAt);
                    sbM5a.append(" ");
                    string = sbM5a.toString();
                    if (string.length() > 70) {
                        println(string);
                        string = "// ";
                    }
                }
            }
            if (string != "// ") {
                println(string);
            }
            StringBuilder sbM5a2 = C0000a.m5a("const ");
            sbM5a2.append(namespaceAntlr);
            sbM5a2.append("BitSet ");
            sbM5a2.append(str);
            sbM5a2.append(getBitsetName(i2));
            sbM5a2.append("(");
            sbM5a2.append(getBitsetName(i2));
            sbM5a2.append("_data_,");
            sbM5a2.append(bitSet.size() / 32);
            sbM5a2.append(");");
            println(sbM5a2.toString());
        }
    }

    public void genBitsetsHeader(Vector vector, int i) {
        println("");
        for (int i2 = 0; i2 < vector.size(); i2++) {
            ((BitSet) vector.elementAt(i2)).growToInclude(i);
            println("static const unsigned long " + getBitsetName(i2) + "_data_[];");
            println("static const " + namespaceAntlr + "BitSet " + getBitsetName(i2) + ";");
        }
    }

    public void genBlockInitAction(AlternativeBlock alternativeBlock) {
        if (alternativeBlock.initAction != null) {
            genLineNo(alternativeBlock);
            printAction(processActionForSpecialSymbols(alternativeBlock.initAction, alternativeBlock.line, this.currentRule, null));
            genLineNo2();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void genBlockPreamble(AlternativeBlock alternativeBlock) {
        if (alternativeBlock instanceof RuleBlock) {
            RuleBlock ruleBlock = (RuleBlock) alternativeBlock;
            if (ruleBlock.labeledElements != null) {
                for (int i = 0; i < ruleBlock.labeledElements.size(); i++) {
                    AlternativeElement alternativeElement = (AlternativeElement) ruleBlock.labeledElements.elementAt(i);
                    boolean z = alternativeElement instanceof RuleRefElement;
                    if (!z && (!(alternativeElement instanceof AlternativeBlock) || (alternativeElement instanceof RuleBlock) || (alternativeElement instanceof SynPredBlock))) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(this.labeledElementType);
                        sb.append(" ");
                        sb.append(alternativeElement.getLabel());
                        sb.append(" = ");
                        C0000a.m8a(sb, this.labeledElementInit, ";", this);
                        if (this.grammar.buildAST) {
                            if (alternativeElement instanceof GrammarAtom) {
                                GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
                                if (grammarAtom.getASTNodeType() != null) {
                                    StringBuilder sbM5a = C0000a.m5a("Ref");
                                    sbM5a.append(grammarAtom.getASTNodeType());
                                    genASTDeclaration(alternativeElement, sbM5a.toString());
                                }
                            }
                        }
                    } else if (!z) {
                        AlternativeBlock alternativeBlock2 = (AlternativeBlock) alternativeElement;
                        if (alternativeBlock2.not && this.analyzer.subruleCanBeInverted(alternativeBlock2, this.grammar instanceof LexerGrammar)) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(this.labeledElementType);
                            sb2.append(" ");
                            sb2.append(alternativeElement.getLabel());
                            sb2.append(" = ");
                            C0000a.m8a(sb2, this.labeledElementInit, ";", this);
                            if (this.grammar.buildAST) {
                                genASTDeclaration(alternativeElement);
                            }
                        } else {
                            if (this.grammar.buildAST) {
                                genASTDeclaration(alternativeElement);
                            }
                            if (this.grammar instanceof LexerGrammar) {
                                println(namespaceAntlr + "RefToken " + alternativeElement.getLabel() + ";");
                            }
                            if (this.grammar instanceof TreeWalkerGrammar) {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(this.labeledElementType);
                                sb3.append(" ");
                                sb3.append(alternativeElement.getLabel());
                                sb3.append(" = ");
                                C0000a.m8a(sb3, this.labeledElementInit, ";", this);
                            }
                        }
                    }
                }
            }
        }
    }

    public void genBody(LexerGrammar lexerGrammar) {
        StringBuilder sbM10b;
        String str;
        StringBuilder sbM10b2;
        this.outputFile = this.grammar.getClassName() + ".cpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = false;
        this.saveText = true;
        this.tabs = 0;
        genHeader(this.outputFile);
        printHeaderAction(preIncludeCpp);
        println("#include \"" + this.grammar.getClassName() + ".hpp\"");
        println("#include <antlr/CharBuffer.hpp>");
        println("#include <antlr/TokenStreamException.hpp>");
        println("#include <antlr/TokenStreamIOException.hpp>");
        println("#include <antlr/TokenStreamRecognitionException.hpp>");
        println("#include <antlr/CharStreamException.hpp>");
        println("#include <antlr/CharStreamIOException.hpp>");
        println("#include <antlr/NoViableAltForCharException.hpp>");
        if (this.grammar.debuggingOutput) {
            println("#include <antlr/DebuggingInputBuffer.hpp>");
        }
        println("");
        printHeaderAction(postIncludeCpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printAction(this.grammar.preambleAction);
        Grammar grammar = this.grammar;
        String strM3a = grammar.superClass;
        if (strM3a == null) {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            strM3a = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        if (this.noConstructors) {
            println("#if 0");
            println("// constructor creation turned of with 'noConstructor' option");
        }
        StringBuilder sb = new StringBuilder();
        C0000a.m4a(this.grammar, sb, "::");
        C0000a.m4a(this.grammar, sb, "(");
        C0000a.m8a(sb, namespaceStd, "istream& in)", this);
        this.tabs++;
        if (this.grammar.debuggingOutput) {
            sbM10b = C0000a.m10b(": ", strM3a, "(new ");
            sbM10b.append(namespaceAntlr);
            sbM10b.append("DebuggingInputBuffer(new ");
            sbM10b.append(namespaceAntlr);
            str = "CharBuffer(in)),";
        } else {
            sbM10b = C0000a.m10b(": ", strM3a, "(new ");
            sbM10b.append(namespaceAntlr);
            str = "CharBuffer(in),";
        }
        sbM10b.append(str);
        sbM10b.append(lexerGrammar.caseSensitive);
        sbM10b.append(")");
        println(sbM10b.toString());
        this.tabs--;
        println("{");
        this.tabs++;
        if (this.grammar.debuggingOutput) {
            println("setRuleNames(_ruleNames);");
            println("setSemPredNames(_semPredNames);");
            println("setupDebugging();");
        }
        println("initLiterals();");
        this.tabs--;
        println("}");
        println("");
        StringBuilder sb2 = new StringBuilder();
        C0000a.m4a(this.grammar, sb2, "::");
        C0000a.m4a(this.grammar, sb2, "(");
        C0000a.m8a(sb2, namespaceAntlr, "InputBuffer& ib)", this);
        this.tabs++;
        if (this.grammar.debuggingOutput) {
            sbM10b2 = C0000a.m10b(": ", strM3a, "(new ");
            sbM10b2.append(namespaceAntlr);
            sbM10b2.append("DebuggingInputBuffer(ib),");
        } else {
            sbM10b2 = C0000a.m10b(": ", strM3a, "(ib,");
        }
        sbM10b2.append(lexerGrammar.caseSensitive);
        sbM10b2.append(")");
        println(sbM10b2.toString());
        this.tabs--;
        println("{");
        this.tabs++;
        if (this.grammar.debuggingOutput) {
            println("setRuleNames(_ruleNames);");
            println("setSemPredNames(_semPredNames);");
            println("setupDebugging();");
        }
        println("initLiterals();");
        this.tabs--;
        println("}");
        println("");
        StringBuilder sb3 = new StringBuilder();
        C0000a.m4a(this.grammar, sb3, "::");
        C0000a.m4a(this.grammar, sb3, "(const ");
        C0000a.m8a(sb3, namespaceAntlr, "LexerSharedInputState& state)", this);
        this.tabs++;
        StringBuilder sbM10b3 = C0000a.m10b(": ", strM3a, "(state,");
        sbM10b3.append(lexerGrammar.caseSensitive);
        sbM10b3.append(")");
        println(sbM10b3.toString());
        this.tabs--;
        println("{");
        this.tabs++;
        if (this.grammar.debuggingOutput) {
            println("setRuleNames(_ruleNames);");
            println("setSemPredNames(_semPredNames);");
            println("setupDebugging();");
        }
        println("initLiterals();");
        this.tabs--;
        println("}");
        println("");
        if (this.noConstructors) {
            println("// constructor creation turned of with 'noConstructor' option");
            println("#endif");
        }
        StringBuilder sbM5a = C0000a.m5a("void ");
        sbM5a.append(this.grammar.getClassName());
        sbM5a.append("::initLiterals()");
        println(sbM5a.toString());
        println("{");
        this.tabs++;
        Enumeration tokenSymbolKeys = this.grammar.tokenManager.getTokenSymbolKeys();
        while (tokenSymbolKeys.hasMoreElements()) {
            String str2 = (String) tokenSymbolKeys.nextElement();
            if (str2.charAt(0) == '\"') {
                TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str2);
                if (tokenSymbol instanceof StringLiteralSymbol) {
                    StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenSymbol;
                    StringBuilder sbM5a2 = C0000a.m5a("literals[");
                    sbM5a2.append(stringLiteralSymbol.getId());
                    sbM5a2.append("] = ");
                    sbM5a2.append(stringLiteralSymbol.getTokenType());
                    sbM5a2.append(";");
                    println(sbM5a2.toString());
                }
            }
        }
        this.tabs--;
        println("}");
        if (this.grammar.debuggingOutput) {
            StringBuilder sbM5a3 = C0000a.m5a("const char* ");
            sbM5a3.append(this.grammar.getClassName());
            sbM5a3.append("::_ruleNames[] = {");
            println(sbM5a3.toString());
            this.tabs++;
            Enumeration enumerationElements = this.grammar.rules.elements();
            while (enumerationElements.hasMoreElements()) {
                GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
                if (grammarSymbol instanceof RuleSymbol) {
                    StringBuilder sbM5a4 = C0000a.m5a("\"");
                    sbM5a4.append(((RuleSymbol) grammarSymbol).getId());
                    sbM5a4.append("\",");
                    println(sbM5a4.toString());
                }
            }
            println("0");
            this.tabs--;
            println("};");
        }
        genNextToken();
        Enumeration enumerationElements2 = this.grammar.rules.elements();
        int i = 0;
        while (enumerationElements2.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) enumerationElements2.nextElement();
            if (!ruleSymbol.getId().equals("mnextToken")) {
                genRule(ruleSymbol, false, i, this.grammar.getClassName() + "::");
                i++;
            }
            exitIfError();
        }
        if (this.grammar.debuggingOutput) {
            genSemPredMap(this.grammar.getClassName() + "::");
        }
        genBitsets(this.bitsetsUsed, ((LexerGrammar) this.grammar).charVocabulary.size(), this.grammar.getClassName() + "::");
        println("");
        NameSpace nameSpace3 = nameSpace;
        if (nameSpace3 != null) {
            nameSpace3.emitClosures(this.currentOutput);
        }
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genBody(ParserGrammar parserGrammar) {
        this.outputFile = this.grammar.getClassName() + ".cpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        genHeader(this.outputFile);
        printHeaderAction(preIncludeCpp);
        println("#include \"" + this.grammar.getClassName() + ".hpp\"");
        println("#include <antlr/NoViableAltException.hpp>");
        println("#include <antlr/SemanticException.hpp>");
        println("#include <antlr/ASTFactory.hpp>");
        printHeaderAction(postIncludeCpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printAction(this.grammar.preambleAction);
        Grammar grammar = this.grammar;
        String strM3a = grammar.superClass;
        if (strM3a == null) {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            strM3a = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        if (this.grammar.debuggingOutput) {
            StringBuilder sbM5a = C0000a.m5a("const char* ");
            sbM5a.append(this.grammar.getClassName());
            sbM5a.append("::_ruleNames[] = {");
            println(sbM5a.toString());
            this.tabs++;
            Enumeration enumerationElements = this.grammar.rules.elements();
            while (enumerationElements.hasMoreElements()) {
                GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
                if (grammarSymbol instanceof RuleSymbol) {
                    StringBuilder sbM5a2 = C0000a.m5a("\"");
                    sbM5a2.append(((RuleSymbol) grammarSymbol).getId());
                    sbM5a2.append("\",");
                    println(sbM5a2.toString());
                }
            }
            println("0");
            this.tabs--;
            println("};");
        }
        if (this.noConstructors) {
            println("#if 0");
            println("// constructor creation turned of with 'noConstructor' option");
        }
        StringBuilder sb = new StringBuilder();
        C0000a.m4a(this.grammar, sb, "::");
        sb.append(this.grammar.getClassName());
        print(sb.toString());
        println("(" + namespaceAntlr + "TokenBuffer& tokenBuf, int k)");
        println(": " + strM3a + "(tokenBuf,k)");
        println("{");
        println("}");
        println("");
        StringBuilder sb2 = new StringBuilder();
        C0000a.m4a(this.grammar, sb2, "::");
        sb2.append(this.grammar.getClassName());
        print(sb2.toString());
        println("(" + namespaceAntlr + "TokenBuffer& tokenBuf)");
        println(": " + strM3a + "(tokenBuf," + this.grammar.maxk + ")");
        println("{");
        println("}");
        println("");
        StringBuilder sb3 = new StringBuilder();
        C0000a.m4a(this.grammar, sb3, "::");
        sb3.append(this.grammar.getClassName());
        print(sb3.toString());
        println("(" + namespaceAntlr + "TokenStream& lexer, int k)");
        println(": " + strM3a + "(lexer,k)");
        println("{");
        println("}");
        println("");
        StringBuilder sb4 = new StringBuilder();
        C0000a.m4a(this.grammar, sb4, "::");
        sb4.append(this.grammar.getClassName());
        print(sb4.toString());
        println("(" + namespaceAntlr + "TokenStream& lexer)");
        println(": " + strM3a + "(lexer," + this.grammar.maxk + ")");
        println("{");
        println("}");
        println("");
        StringBuilder sb5 = new StringBuilder();
        C0000a.m4a(this.grammar, sb5, "::");
        sb5.append(this.grammar.getClassName());
        print(sb5.toString());
        println("(const " + namespaceAntlr + "ParserSharedInputState& state)");
        println(": " + strM3a + "(state," + this.grammar.maxk + ")");
        println("{");
        println("}");
        println("");
        if (this.noConstructors) {
            println("// constructor creation turned of with 'noConstructor' option");
            println("#endif");
        }
        this.astTypes = new Vector();
        Enumeration enumerationElements2 = this.grammar.rules.elements();
        int i = 0;
        while (enumerationElements2.hasMoreElements()) {
            GrammarSymbol grammarSymbol2 = (GrammarSymbol) enumerationElements2.nextElement();
            if (grammarSymbol2 instanceof RuleSymbol) {
                RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol2;
                genRule(ruleSymbol, ruleSymbol.references.size() == 0, i, this.grammar.getClassName() + "::");
                i++;
            }
            exitIfError();
        }
        genInitFactory(parserGrammar);
        genTokenStrings(this.grammar.getClassName() + "::");
        genBitsets(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType(), this.grammar.getClassName() + "::");
        if (this.grammar.debuggingOutput) {
            genSemPredMap(this.grammar.getClassName() + "::");
        }
        println("");
        println("");
        NameSpace nameSpace3 = nameSpace;
        if (nameSpace3 != null) {
            nameSpace3.emitClosures(this.currentOutput);
        }
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genBody(TreeWalkerGrammar treeWalkerGrammar) {
        this.outputFile = this.grammar.getClassName() + ".cpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        genHeader(this.outputFile);
        printHeaderAction(preIncludeCpp);
        println("#include \"" + this.grammar.getClassName() + ".hpp\"");
        println("#include <antlr/Token.hpp>");
        println("#include <antlr/AST.hpp>");
        println("#include <antlr/NoViableAltException.hpp>");
        println("#include <antlr/MismatchedTokenException.hpp>");
        println("#include <antlr/SemanticException.hpp>");
        println("#include <antlr/BitSet.hpp>");
        printHeaderAction(postIncludeCpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printAction(this.grammar.preambleAction);
        Grammar grammar = this.grammar;
        if (grammar.superClass == null) {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            String str = namespaceAntlr + superClass;
        }
        if (this.noConstructors) {
            println("#if 0");
            println("// constructor creation turned of with 'noConstructor' option");
        }
        StringBuilder sb = new StringBuilder();
        C0000a.m4a(this.grammar, sb, "::");
        sb.append(this.grammar.getClassName());
        sb.append("()");
        println(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("\t: ");
        C0000a.m8a(sb2, namespaceAntlr, "TreeParser() {", this);
        this.tabs++;
        this.tabs--;
        println("}");
        if (this.noConstructors) {
            println("// constructor creation turned of with 'noConstructor' option");
            println("#endif");
        }
        println("");
        this.astTypes = new Vector();
        Enumeration enumerationElements = this.grammar.rules.elements();
        int i = 0;
        while (enumerationElements.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol;
                genRule(ruleSymbol, ruleSymbol.references.size() == 0, i, this.grammar.getClassName() + "::");
                i++;
            }
            exitIfError();
        }
        genInitFactory(this.grammar);
        genTokenStrings(this.grammar.getClassName() + "::");
        genBitsets(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType(), this.grammar.getClassName() + "::");
        println("");
        println("");
        NameSpace nameSpace3 = nameSpace;
        if (nameSpace3 != null) {
            nameSpace3.emitClosures(this.currentOutput);
        }
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genCases(BitSet bitSet) {
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("genCases(" + bitSet + ")");
        }
        for (int i : bitSet.toArray()) {
            print("");
            _print("case " + getValueString(i) + ":");
            _println("");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:187:0x049c A[PHI: r1
      0x049c: PHI (r1v7 antlr.CppBlockFinishingInfo) = (r1v6 antlr.CppBlockFinishingInfo), (r1v11 antlr.CppBlockFinishingInfo) binds: [B:186:0x049a, B:182:0x048e] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CppBlockFinishingInfo genCommonBlock(AlternativeBlock alternativeBlock, boolean z) {
        String str;
        String str2;
        boolean z2;
        CppBlockFinishingInfo cppBlockFinishingInfo;
        boolean z3;
        int i;
        boolean z4;
        String str3;
        boolean zLookaheadIsEmpty;
        int i2;
        boolean z5;
        int i3;
        int i4;
        CppBlockFinishingInfo cppBlockFinishingInfo2;
        String str4;
        StringBuilder sb;
        String str5;
        StringBuilder sb2;
        PrintStream printStream;
        String str6;
        String str7;
        AlternativeBlock alternativeBlock2 = alternativeBlock;
        CppBlockFinishingInfo cppBlockFinishingInfo3 = new CppBlockFinishingInfo();
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("genCommonBlk(" + alternativeBlock2 + ")");
        }
        boolean z6 = this.genAST;
        int i5 = 1;
        this.genAST = z6 && alternativeBlock.getAutoGen();
        boolean z7 = this.saveText;
        this.saveText = z7 && alternativeBlock.getAutoGen();
        str = "";
        if (alternativeBlock2.not && this.analyzer.subruleCanBeInverted(alternativeBlock2, this.grammar instanceof LexerGrammar)) {
            Lookahead lookaheadLook = this.analyzer.look(1, alternativeBlock2);
            if (alternativeBlock.getLabel() != null && this.syntacticPredLevel == 0) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(alternativeBlock.getLabel());
                sb3.append(" = ");
                C0000a.m8a(sb3, this.lt1Value, ";", this);
            }
            genElementAST(alternativeBlock);
            StringBuilder sbM9b = C0000a.m9b("match(", this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST(_t),") : "_t," : "");
            sbM9b.append(getBitsetName(markBitsetForGen(lookaheadLook.fset)));
            sbM9b.append(");");
            println(sbM9b.toString());
            if (this.grammar instanceof TreeWalkerGrammar) {
                println("_t = _t->getNextSibling();");
            }
            return cppBlockFinishingInfo3;
        }
        if (alternativeBlock.getAlternatives().size() == 1) {
            Alternative alternativeAt = alternativeBlock2.getAlternativeAt(0);
            if (alternativeAt.synPred != null) {
                this.antlrTool.warning("Syntactic predicate superfluous for single alternative", this.grammar.getFilename(), alternativeBlock2.getAlternativeAt(0).synPred.getLine(), alternativeBlock2.getAlternativeAt(0).synPred.getColumn());
            }
            if (z) {
                String str8 = alternativeAt.semPred;
                if (str8 != null) {
                    genSemPred(str8, alternativeBlock2.line);
                }
                genAlt(alternativeAt, alternativeBlock2);
                return cppBlockFinishingInfo3;
            }
        }
        int i6 = 0;
        for (int i7 = 0; i7 < alternativeBlock.getAlternatives().size(); i7++) {
            if (suitableForCaseExpression(alternativeBlock2.getAlternativeAt(i7))) {
                i6++;
            }
        }
        String str9 = "{";
        String str10 = "}";
        if (i6 >= this.makeSwitchThreshold) {
            String strLookaheadString = lookaheadString(1);
            if (this.grammar instanceof TreeWalkerGrammar) {
                C0000a.m8a(C0000a.m5a("if (_t == "), this.labeledElementASTInit, " )", this);
                this.tabs++;
                println("_t = ASTNULL;");
                this.tabs--;
            }
            println("switch ( " + strLookaheadString + ") {");
            int i8 = 0;
            while (i8 < alternativeBlock2.alternatives.size()) {
                Alternative alternativeAt2 = alternativeBlock2.getAlternativeAt(i8);
                if (suitableForCaseExpression(alternativeAt2)) {
                    Lookahead lookahead = alternativeAt2.cache[i5];
                    if (lookahead.fset.degree() != 0 || lookahead.containsEpsilon()) {
                        str7 = str;
                        genCases(lookahead.fset);
                        println("{");
                        i5 = 1;
                        this.tabs++;
                        genAlt(alternativeAt2, alternativeBlock2);
                        println("break;");
                        this.tabs--;
                        println("}");
                    } else {
                        str7 = str;
                        this.antlrTool.warning("Alternate omitted due to empty prediction set", this.grammar.getFilename(), alternativeAt2.head.getLine(), alternativeAt2.head.getColumn());
                        i5 = 1;
                    }
                } else {
                    str7 = str;
                }
                i8++;
                str = str7;
            }
            str2 = str;
            println("default:");
            this.tabs += i5;
            z2 = true;
        } else {
            str2 = "";
            z2 = false;
        }
        Grammar grammar = this.grammar;
        int i9 = grammar instanceof LexerGrammar ? grammar.maxk : 0;
        int i10 = 0;
        int i11 = 0;
        while (i9 >= 0) {
            if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
                PrintStream printStream2 = System.out;
                StringBuilder sb4 = new StringBuilder();
                i = i11;
                sb4.append("checking depth ");
                sb4.append(i9);
                printStream2.println(sb4.toString());
            } else {
                i = i11;
            }
            i11 = i;
            int i12 = i10;
            int i13 = 0;
            while (i13 < alternativeBlock2.alternatives.size()) {
                Alternative alternativeAt3 = alternativeBlock2.getAlternativeAt(i13);
                boolean z8 = z7;
                if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
                    PrintStream printStream3 = System.out;
                    z4 = z6;
                    StringBuilder sb5 = new StringBuilder();
                    str3 = str10;
                    sb5.append("genAlt: ");
                    sb5.append(i13);
                    printStream3.println(sb5.toString());
                } else {
                    z4 = z6;
                    str3 = str10;
                }
                if (z2 && suitableForCaseExpression(alternativeAt3)) {
                    if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
                        printStream = System.out;
                        str6 = "ignoring alt because it was in the switch";
                        printStream.println(str6);
                    }
                    cppBlockFinishingInfo2 = cppBlockFinishingInfo3;
                    z5 = z2;
                    i3 = i9;
                    i4 = i13;
                    str4 = str9;
                    str5 = str3;
                } else {
                    Grammar grammar2 = this.grammar;
                    if (grammar2 instanceof LexerGrammar) {
                        i2 = alternativeAt3.lookaheadDepth;
                        if (i2 == Integer.MAX_VALUE) {
                            i2 = grammar2.maxk;
                        }
                        while (i2 >= 1 && alternativeAt3.cache[i2].containsEpsilon()) {
                            i2--;
                        }
                        if (i2 != i9) {
                            if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
                                printStream = System.out;
                                str6 = "ignoring alt because effectiveDepth!=altDepth;" + i2 + "!=" + i9;
                                printStream.println(str6);
                            }
                            cppBlockFinishingInfo2 = cppBlockFinishingInfo3;
                            z5 = z2;
                            i3 = i9;
                            i4 = i13;
                            str4 = str9;
                            str5 = str3;
                        } else {
                            zLookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, i2);
                        }
                    } else {
                        zLookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, grammar2.maxk);
                        i2 = this.grammar.maxk;
                    }
                    String lookaheadTestExpression = getLookaheadTestExpression(alternativeAt3, i2);
                    z5 = z2;
                    i3 = i9;
                    i4 = i13;
                    int i14 = i12;
                    if (alternativeAt3.cache[1].fset.degree() > 127 && suitableForCaseExpression(alternativeAt3)) {
                        if (i11 == 0) {
                            if (this.grammar instanceof TreeWalkerGrammar) {
                                C0000a.m8a(C0000a.m5a("if (_t == "), this.labeledElementASTInit, " )", this);
                                this.tabs++;
                                println("_t = ASTNULL;");
                                this.tabs--;
                            }
                            sb2 = new StringBuilder();
                            sb2.append("if ");
                        } else {
                            sb2 = new StringBuilder();
                            sb2.append("else if ");
                        }
                        sb2.append(lookaheadTestExpression);
                        sb2.append(" {");
                        println(sb2.toString());
                    } else if (zLookaheadIsEmpty && alternativeAt3.semPred == null && alternativeAt3.synPred == null) {
                        if (i11 == 0) {
                            println(str9);
                        } else {
                            println("else {");
                        }
                        cppBlockFinishingInfo3.needAnErrorClause = false;
                    } else {
                        if (alternativeAt3.semPred != null) {
                            str4 = str9;
                            cppBlockFinishingInfo2 = cppBlockFinishingInfo3;
                            String strProcessActionForSpecialSymbols = processActionForSpecialSymbols(alternativeAt3.semPred, alternativeBlock2.line, this.currentRule, new ActionTransInfo());
                            Grammar grammar3 = this.grammar;
                            if (grammar3.debuggingOutput && ((grammar3 instanceof ParserGrammar) || (grammar3 instanceof LexerGrammar))) {
                                StringBuilder sbM10b = C0000a.m10b("(", lookaheadTestExpression, "&& fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.PREDICTING,");
                                sbM10b.append(addSemPred(this.charFormatter.escapeString(strProcessActionForSpecialSymbols)));
                                sbM10b.append(",");
                                lookaheadTestExpression = C0000a.m3a(sbM10b, strProcessActionForSpecialSymbols, "))");
                            } else {
                                lookaheadTestExpression = "(" + lookaheadTestExpression + "&&(" + strProcessActionForSpecialSymbols + "))";
                            }
                        } else {
                            cppBlockFinishingInfo2 = cppBlockFinishingInfo3;
                            str4 = str9;
                        }
                        SynPredBlock synPredBlock = alternativeAt3.synPred;
                        if (i11 > 0) {
                            if (synPredBlock != null) {
                                println("else {");
                                this.tabs++;
                                genSynPred(alternativeAt3.synPred, lookaheadTestExpression);
                                i12 = i14 + 1;
                                i11++;
                                this.tabs++;
                                alternativeBlock2 = alternativeBlock;
                                genAlt(alternativeAt3, alternativeBlock2);
                                this.tabs--;
                                str5 = str3;
                                println(str5);
                            } else {
                                sb = new StringBuilder();
                                sb.append("else if ");
                            }
                        } else if (synPredBlock != null) {
                            genSynPred(synPredBlock, lookaheadTestExpression);
                            i12 = i14;
                            i11++;
                            this.tabs++;
                            alternativeBlock2 = alternativeBlock;
                            genAlt(alternativeAt3, alternativeBlock2);
                            this.tabs--;
                            str5 = str3;
                            println(str5);
                        } else {
                            if (this.grammar instanceof TreeWalkerGrammar) {
                                C0000a.m8a(C0000a.m5a("if (_t == "), this.labeledElementASTInit, " )", this);
                                this.tabs++;
                                println("_t = ASTNULL;");
                                this.tabs--;
                            }
                            sb = new StringBuilder();
                            sb.append("if ");
                        }
                        sb.append(lookaheadTestExpression);
                        sb.append(" {");
                        println(sb.toString());
                        i12 = i14;
                        i11++;
                        this.tabs++;
                        alternativeBlock2 = alternativeBlock;
                        genAlt(alternativeAt3, alternativeBlock2);
                        this.tabs--;
                        str5 = str3;
                        println(str5);
                    }
                    cppBlockFinishingInfo2 = cppBlockFinishingInfo3;
                    str4 = str9;
                    i12 = i14;
                    i11++;
                    this.tabs++;
                    alternativeBlock2 = alternativeBlock;
                    genAlt(alternativeAt3, alternativeBlock2);
                    this.tabs--;
                    str5 = str3;
                    println(str5);
                }
                i13 = i4 + 1;
                str10 = str5;
                z7 = z8;
                z6 = z4;
                z2 = z5;
                i9 = i3;
                str9 = str4;
                cppBlockFinishingInfo3 = cppBlockFinishingInfo2;
            }
            i9--;
            i10 = i12;
            cppBlockFinishingInfo3 = cppBlockFinishingInfo3;
        }
        CppBlockFinishingInfo cppBlockFinishingInfo4 = cppBlockFinishingInfo3;
        boolean z9 = z6;
        boolean z10 = z2;
        boolean z11 = z7;
        int i15 = i11;
        String str11 = str10;
        String strM1a = str2;
        for (int i16 = 1; i16 <= i10; i16++) {
            this.tabs--;
            strM1a = C0000a.m1a(strM1a, str11);
        }
        this.genAST = z9;
        this.saveText = z11;
        if (z10) {
            this.tabs--;
            cppBlockFinishingInfo = cppBlockFinishingInfo4;
            cppBlockFinishingInfo.postscript = C0000a.m1a(strM1a, str11);
            cppBlockFinishingInfo.generatedSwitch = true;
            z3 = i15 > 0;
        } else {
            cppBlockFinishingInfo = cppBlockFinishingInfo4;
            cppBlockFinishingInfo.postscript = strM1a;
            z3 = false;
            cppBlockFinishingInfo.generatedSwitch = false;
            if (i15 > 0) {
            }
        }
        cppBlockFinishingInfo.generatedAnIf = z3;
        return cppBlockFinishingInfo;
    }

    public void genHeader(String str) {
        StringBuilder sbM5a = C0000a.m5a("/* $ANTLR ");
        sbM5a.append(Tool.version);
        sbM5a.append(": \"");
        Tool tool = this.antlrTool;
        sbM5a.append(tool.fileMinusPath(tool.grammarFile));
        sbM5a.append("\" -> \"");
        sbM5a.append(str);
        sbM5a.append("\"$ */");
        println(sbM5a.toString());
    }

    public void genInclude(LexerGrammar lexerGrammar) {
        String strStripFrontBack;
        this.outputFile = this.grammar.getClassName() + ".hpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = false;
        this.saveText = true;
        this.tabs = 0;
        StringBuilder sbM5a = C0000a.m5a("#ifndef INC_");
        sbM5a.append(this.grammar.getClassName());
        sbM5a.append("_hpp_");
        println(sbM5a.toString());
        println("#define INC_" + this.grammar.getClassName() + "_hpp_");
        println("");
        printHeaderAction(preIncludeHpp);
        println("#include <antlr/config.hpp>");
        genHeader(this.outputFile);
        println("#include <antlr/CommonToken.hpp>");
        println("#include <antlr/InputBuffer.hpp>");
        println("#include <antlr/BitSet.hpp>");
        StringBuilder sb = new StringBuilder();
        sb.append("#include \"");
        sb.append(this.grammar.tokenManager.getName());
        C0000a.m8a(sb, CodeGenerator.TokenTypesFileSuffix, ".hpp\"", this);
        Grammar grammar = this.grammar;
        String strM3a = grammar.superClass;
        if (strM3a != null) {
            println("\n// Include correct superclass header with a header statement for example:");
            println("// header \"post_include_hpp\" {");
            println("// #include \"" + strM3a + ".hpp\"");
            println("// }");
            println("// Or....");
            println("// header {");
            println("// #include \"" + strM3a + ".hpp\"");
            println("// }\n");
        } else {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            println("#include <antlr/" + superClass + ".hpp>");
            strM3a = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        printHeaderAction(postIncludeHpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printHeaderAction("");
        String str = this.grammar.comment;
        if (str != null) {
            _println(str);
        }
        StringBuilder sbM5a2 = C0000a.m5a("class CUSTOM_API ");
        sbM5a2.append(this.grammar.getClassName());
        sbM5a2.append(" : public ");
        sbM5a2.append(strM3a);
        print(sbM5a2.toString());
        println(", public " + this.grammar.tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
        Token token = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token != null && (strStripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) != null) {
            print(", " + strStripFrontBack);
        }
        println("{");
        Token token2 = this.grammar.classMemberAction;
        if (token2 != null) {
            genLineNo(token2);
            print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null));
            genLineNo2();
        }
        this.tabs = 0;
        println("private:");
        this.tabs = 1;
        println("void initLiterals();");
        this.tabs = 0;
        println("public:");
        this.tabs = 1;
        println("bool getCaseSensitiveLiterals() const");
        println("{");
        this.tabs++;
        StringBuilder sbM5a3 = C0000a.m5a("return ");
        sbM5a3.append(lexerGrammar.caseSensitiveLiterals);
        sbM5a3.append(";");
        println(sbM5a3.toString());
        this.tabs--;
        println("}");
        this.tabs = 0;
        println("public:");
        this.tabs = 1;
        if (this.noConstructors) {
            this.tabs = 0;
            println("#if 0");
            println("// constructor creation turned of with 'noConstructor' option");
            this.tabs = 1;
        }
        StringBuilder sb2 = new StringBuilder();
        C0000a.m4a(this.grammar, sb2, "(");
        sb2.append(namespaceStd);
        sb2.append("istream& in);");
        println(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        C0000a.m4a(this.grammar, sb3, "(");
        sb3.append(namespaceAntlr);
        sb3.append("InputBuffer& ib);");
        println(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        C0000a.m4a(this.grammar, sb4, "(const ");
        C0000a.m8a(sb4, namespaceAntlr, "LexerSharedInputState& state);", this);
        if (this.noConstructors) {
            this.tabs = 0;
            println("// constructor creation turned of with 'noConstructor' option");
            println("#endif");
            this.tabs = 1;
        }
        C0000a.m8a(new StringBuilder(), namespaceAntlr, "RefToken nextToken();", this);
        Enumeration enumerationElements = this.grammar.rules.elements();
        while (enumerationElements.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) enumerationElements.nextElement();
            if (!ruleSymbol.getId().equals("mnextToken")) {
                genRuleHeader(ruleSymbol, false);
            }
            exitIfError();
        }
        this.tabs = 0;
        println("private:");
        this.tabs = 1;
        if (this.grammar.debuggingOutput) {
            println("static const char* _ruleNames[];");
        }
        if (this.grammar.debuggingOutput) {
            println("static const char* _semPredNames[];");
        }
        genBitsetsHeader(this.bitsetsUsed, ((LexerGrammar) this.grammar).charVocabulary.size());
        this.tabs = 0;
        println("};");
        println("");
        NameSpace nameSpace3 = nameSpace;
        if (nameSpace3 != null) {
            nameSpace3.emitClosures(this.currentOutput);
        }
        StringBuilder sbM5a4 = C0000a.m5a("#endif /*INC_");
        sbM5a4.append(this.grammar.getClassName());
        sbM5a4.append("_hpp_*/");
        println(sbM5a4.toString());
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genInclude(ParserGrammar parserGrammar) {
        String strStripFrontBack;
        this.outputFile = this.grammar.getClassName() + ".hpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        StringBuilder sbM5a = C0000a.m5a("#ifndef INC_");
        sbM5a.append(this.grammar.getClassName());
        sbM5a.append("_hpp_");
        println(sbM5a.toString());
        println("#define INC_" + this.grammar.getClassName() + "_hpp_");
        println("");
        printHeaderAction(preIncludeHpp);
        println("#include <antlr/config.hpp>");
        genHeader(this.outputFile);
        println("#include <antlr/TokenStream.hpp>");
        println("#include <antlr/TokenBuffer.hpp>");
        StringBuilder sb = new StringBuilder();
        sb.append("#include \"");
        sb.append(this.grammar.tokenManager.getName());
        C0000a.m8a(sb, CodeGenerator.TokenTypesFileSuffix, ".hpp\"", this);
        Grammar grammar = this.grammar;
        String strM3a = grammar.superClass;
        if (strM3a != null) {
            println("\n// Include correct superclass header with a header statement for example:");
            println("// header \"post_include_hpp\" {");
            println("// #include \"" + strM3a + ".hpp\"");
            println("// }");
            println("// Or....");
            println("// header {");
            println("// #include \"" + strM3a + ".hpp\"");
            println("// }\n");
        } else {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            println("#include <antlr/" + superClass + ".hpp>");
            strM3a = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        println("");
        printHeaderAction(postIncludeHpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printHeaderAction("");
        String str = this.grammar.comment;
        if (str != null) {
            _println(str);
        }
        StringBuilder sbM5a2 = C0000a.m5a("class CUSTOM_API ");
        sbM5a2.append(this.grammar.getClassName());
        sbM5a2.append(" : public ");
        sbM5a2.append(strM3a);
        print(sbM5a2.toString());
        println(", public " + this.grammar.tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
        Token token = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token != null && (strStripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) != null) {
            print(", " + strStripFrontBack);
        }
        println("{");
        if (this.grammar.debuggingOutput) {
            println("public: static const char* _ruleNames[];");
        }
        Token token2 = this.grammar.classMemberAction;
        if (token2 != null) {
            genLineNo(token2.getLine());
            print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null));
            genLineNo2();
        }
        println("public:");
        this.tabs = 1;
        C0000a.m8a(C0000a.m5a("void initializeASTFactory( "), namespaceAntlr, "ASTFactory& factory );", this);
        this.tabs = 0;
        if (this.noConstructors) {
            println("#if 0");
            println("// constructor creation turned of with 'noConstructor' option");
        }
        println("protected:");
        this.tabs = 1;
        StringBuilder sb2 = new StringBuilder();
        C0000a.m4a(this.grammar, sb2, "(");
        C0000a.m8a(sb2, namespaceAntlr, "TokenBuffer& tokenBuf, int k);", this);
        this.tabs = 0;
        println("public:");
        this.tabs = 1;
        StringBuilder sb3 = new StringBuilder();
        C0000a.m4a(this.grammar, sb3, "(");
        C0000a.m8a(sb3, namespaceAntlr, "TokenBuffer& tokenBuf);", this);
        this.tabs = 0;
        println("protected:");
        this.tabs = 1;
        StringBuilder sb4 = new StringBuilder();
        C0000a.m4a(this.grammar, sb4, "(");
        C0000a.m8a(sb4, namespaceAntlr, "TokenStream& lexer, int k);", this);
        this.tabs = 0;
        println("public:");
        this.tabs = 1;
        StringBuilder sb5 = new StringBuilder();
        C0000a.m4a(this.grammar, sb5, "(");
        sb5.append(namespaceAntlr);
        sb5.append("TokenStream& lexer);");
        println(sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        C0000a.m4a(this.grammar, sb6, "(const ");
        C0000a.m8a(sb6, namespaceAntlr, "ParserSharedInputState& state);", this);
        if (this.noConstructors) {
            this.tabs = 0;
            println("// constructor creation turned of with 'noConstructor' option");
            println("#endif");
            this.tabs = 1;
        }
        println("int getNumTokens() const");
        println("{");
        this.tabs++;
        StringBuilder sbM5a3 = C0000a.m5a("return ");
        sbM5a3.append(this.grammar.getClassName());
        sbM5a3.append("::NUM_TOKENS;");
        println(sbM5a3.toString());
        this.tabs--;
        println("}");
        println("const char* getTokenName( int type ) const");
        println("{");
        this.tabs++;
        println("if( type > getNumTokens() ) return 0;");
        println("return " + this.grammar.getClassName() + "::tokenNames[type];");
        this.tabs = this.tabs - 1;
        println("}");
        println("const char* const* getTokenNames() const");
        println("{");
        this.tabs++;
        StringBuilder sbM5a4 = C0000a.m5a("return ");
        sbM5a4.append(this.grammar.getClassName());
        sbM5a4.append("::tokenNames;");
        println(sbM5a4.toString());
        this.tabs--;
        println("}");
        Enumeration enumerationElements = this.grammar.rules.elements();
        while (enumerationElements.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol;
                genRuleHeader(ruleSymbol, ruleSymbol.references.size() == 0);
            }
            exitIfError();
        }
        this.tabs = 0;
        println("public:");
        this.tabs = 1;
        println(namespaceAntlr + "RefAST getAST()");
        println("{");
        if (this.usingCustomAST) {
            this.tabs++;
            C0000a.m8a(C0000a.m5a("return "), namespaceAntlr, "RefAST(returnAST);", this);
        } else {
            this.tabs++;
            println("return returnAST;");
        }
        this.tabs--;
        println("}");
        println("");
        this.tabs = 0;
        println("protected:");
        this.tabs = 1;
        C0000a.m8a(new StringBuilder(), this.labeledElementASTType, " returnAST;", this);
        this.tabs = 0;
        println("private:");
        this.tabs = 1;
        println("static const char* tokenNames[];");
        _println("#ifndef NO_STATIC_CONSTS");
        println("static const int NUM_TOKENS = " + this.grammar.tokenManager.getVocabulary().size() + ";");
        _println("#else");
        println("enum {");
        println("\tNUM_TOKENS = " + this.grammar.tokenManager.getVocabulary().size());
        println("};");
        _println("#endif");
        genBitsetsHeader(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType());
        if (this.grammar.debuggingOutput) {
            println("static const char* _semPredNames[];");
        }
        this.tabs = 0;
        println("};");
        println("");
        NameSpace nameSpace3 = nameSpace;
        if (nameSpace3 != null) {
            nameSpace3.emitClosures(this.currentOutput);
        }
        StringBuilder sbM5a5 = C0000a.m5a("#endif /*INC_");
        sbM5a5.append(this.grammar.getClassName());
        sbM5a5.append("_hpp_*/");
        println(sbM5a5.toString());
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genInclude(TreeWalkerGrammar treeWalkerGrammar) {
        String strStripFrontBack;
        this.outputFile = this.grammar.getClassName() + ".hpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        StringBuilder sbM5a = C0000a.m5a("#ifndef INC_");
        sbM5a.append(this.grammar.getClassName());
        sbM5a.append("_hpp_");
        println(sbM5a.toString());
        println("#define INC_" + this.grammar.getClassName() + "_hpp_");
        println("");
        printHeaderAction(preIncludeHpp);
        println("#include <antlr/config.hpp>");
        StringBuilder sb = new StringBuilder();
        sb.append("#include \"");
        sb.append(this.grammar.tokenManager.getName());
        C0000a.m8a(sb, CodeGenerator.TokenTypesFileSuffix, ".hpp\"", this);
        genHeader(this.outputFile);
        Grammar grammar = this.grammar;
        String strM3a = grammar.superClass;
        if (strM3a != null) {
            println("\n// Include correct superclass header with a header statement for example:");
            println("// header \"post_include_hpp\" {");
            println("// #include \"" + strM3a + ".hpp\"");
            println("// }");
            println("// Or....");
            println("// header {");
            println("// #include \"" + strM3a + ".hpp\"");
            println("// }\n");
        } else {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            println("#include <antlr/" + superClass + ".hpp>");
            strM3a = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        println("");
        printHeaderAction(postIncludeHpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printHeaderAction("");
        String str = this.grammar.comment;
        if (str != null) {
            _println(str);
        }
        StringBuilder sbM5a2 = C0000a.m5a("class CUSTOM_API ");
        sbM5a2.append(this.grammar.getClassName());
        sbM5a2.append(" : public ");
        sbM5a2.append(strM3a);
        print(sbM5a2.toString());
        println(", public " + this.grammar.tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
        Token token = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token != null && (strStripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) != null) {
            print(", " + strStripFrontBack);
        }
        println("{");
        Token token2 = this.grammar.classMemberAction;
        if (token2 != null) {
            genLineNo(token2.getLine());
            print(processActionForSpecialSymbols(this.grammar.classMemberAction.getText(), this.grammar.classMemberAction.getLine(), this.currentRule, null));
            genLineNo2();
        }
        this.tabs = 0;
        println("public:");
        if (this.noConstructors) {
            println("#if 0");
            println("// constructor creation turned of with 'noConstructor' option");
        }
        this.tabs = 1;
        println(this.grammar.getClassName() + "();");
        if (this.noConstructors) {
            this.tabs = 0;
            println("#endif");
            this.tabs = 1;
        }
        StringBuilder sbM5a3 = C0000a.m5a("static void initializeASTFactory( ");
        sbM5a3.append(namespaceAntlr);
        sbM5a3.append("ASTFactory& factory );");
        println(sbM5a3.toString());
        println("int getNumTokens() const");
        println("{");
        this.tabs++;
        StringBuilder sbM5a4 = C0000a.m5a("return ");
        sbM5a4.append(this.grammar.getClassName());
        sbM5a4.append("::NUM_TOKENS;");
        println(sbM5a4.toString());
        this.tabs--;
        println("}");
        println("const char* getTokenName( int type ) const");
        println("{");
        this.tabs++;
        println("if( type > getNumTokens() ) return 0;");
        println("return " + this.grammar.getClassName() + "::tokenNames[type];");
        this.tabs = this.tabs - 1;
        println("}");
        println("const char* const* getTokenNames() const");
        println("{");
        this.tabs++;
        StringBuilder sbM5a5 = C0000a.m5a("return ");
        sbM5a5.append(this.grammar.getClassName());
        sbM5a5.append("::tokenNames;");
        println(sbM5a5.toString());
        this.tabs--;
        println("}");
        Enumeration enumerationElements = this.grammar.rules.elements();
        while (enumerationElements.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                RuleSymbol ruleSymbol = (RuleSymbol) grammarSymbol;
                genRuleHeader(ruleSymbol, ruleSymbol.references.size() == 0);
            }
            exitIfError();
        }
        this.tabs = 0;
        println("public:");
        this.tabs = 1;
        println(namespaceAntlr + "RefAST getAST()");
        println("{");
        if (this.usingCustomAST) {
            this.tabs++;
            C0000a.m8a(C0000a.m5a("return "), namespaceAntlr, "RefAST(returnAST);", this);
        } else {
            this.tabs++;
            println("return returnAST;");
        }
        this.tabs--;
        println("}");
        println("");
        this.tabs = 0;
        println("protected:");
        this.tabs = 1;
        println(this.labeledElementASTType + " returnAST;");
        C0000a.m8a(new StringBuilder(), this.labeledElementASTType, " _retTree;", this);
        this.tabs = 0;
        println("private:");
        this.tabs = 1;
        println("static const char* tokenNames[];");
        _println("#ifndef NO_STATIC_CONSTS");
        println("static const int NUM_TOKENS = " + this.grammar.tokenManager.getVocabulary().size() + ";");
        _println("#else");
        println("enum {");
        println("\tNUM_TOKENS = " + this.grammar.tokenManager.getVocabulary().size());
        println("};");
        _println("#endif");
        genBitsetsHeader(this.bitsetsUsed, this.grammar.tokenManager.maxTokenType());
        this.tabs = 0;
        println("};");
        println("");
        NameSpace nameSpace3 = nameSpace;
        if (nameSpace3 != null) {
            nameSpace3.emitClosures(this.currentOutput);
        }
        StringBuilder sbM5a6 = C0000a.m5a("#endif /*INC_");
        sbM5a6.append(this.grammar.getClassName());
        sbM5a6.append("_hpp_*/");
        println(sbM5a6.toString());
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genInitFactory(Grammar grammar) {
        String str = !grammar.buildAST ? "" : "factory ";
        StringBuilder sbM5a = C0000a.m5a("void ");
        C0000a.m4a(grammar, sbM5a, "::initializeASTFactory( ");
        sbM5a.append(namespaceAntlr);
        sbM5a.append("ASTFactory& ");
        sbM5a.append(str);
        sbM5a.append(")");
        println(sbM5a.toString());
        println("{");
        this.tabs++;
        if (grammar.buildAST) {
            TokenManager tokenManager = this.grammar.tokenManager;
            Enumeration tokenSymbolKeys = tokenManager.getTokenSymbolKeys();
            while (tokenSymbolKeys.hasMoreElements()) {
                String str2 = (String) tokenSymbolKeys.nextElement();
                TokenSymbol tokenSymbol = tokenManager.getTokenSymbol(str2);
                if (tokenSymbol.getASTNodeType() != null) {
                    this.astTypes.ensureCapacity(tokenSymbol.getTokenType());
                    String str3 = (String) this.astTypes.elementAt(tokenSymbol.getTokenType());
                    if (str3 == null) {
                        this.astTypes.setElementAt(tokenSymbol.getASTNodeType(), tokenSymbol.getTokenType());
                    } else if (!tokenSymbol.getASTNodeType().equals(str3)) {
                        this.antlrTool.warning(C0000a.m2a("Token ", str2, " taking most specific AST type"), this.grammar.getFilename(), 1, 1);
                        Tool tool = this.antlrTool;
                        StringBuilder sbM10b = C0000a.m10b("  using ", str3, " ignoring ");
                        sbM10b.append(tokenSymbol.getASTNodeType());
                        tool.warning(sbM10b.toString(), this.grammar.getFilename(), 1, 1);
                    }
                }
            }
            for (int i = 0; i < this.astTypes.size(); i++) {
                String str4 = (String) this.astTypes.elementAt(i);
                if (str4 != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("factory.registerFactory(");
                    sb.append(i);
                    sb.append(", \"");
                    sb.append(str4);
                    sb.append("\", ");
                    C0000a.m8a(sb, str4, "::factory);", this);
                }
            }
            StringBuilder sbM5a2 = C0000a.m5a("factory.setMaxNodeType(");
            sbM5a2.append(this.grammar.tokenManager.maxTokenType());
            sbM5a2.append(");");
            println(sbM5a2.toString());
        }
        this.tabs--;
        println("}");
    }

    public void genLineNo(int i) {
        if (i == 0) {
            i++;
        }
        if (this.genHashLines) {
            StringBuilder sb = new StringBuilder();
            sb.append("#line ");
            sb.append(i);
            sb.append(" \"");
            Tool tool = this.antlrTool;
            sb.append(tool.fileMinusPath(tool.grammarFile));
            sb.append("\"");
            _println(sb.toString());
        }
    }

    public void genLineNo(GrammarElement grammarElement) {
        if (grammarElement != null) {
            genLineNo(grammarElement.getLine());
        }
    }

    public void genLineNo(Token token) {
        if (token != null) {
            genLineNo(token.getLine());
        }
    }

    public void genLineNo2() {
        if (this.genHashLines) {
            StringBuilder sbM5a = C0000a.m5a("#line ");
            sbM5a.append(this.outputLine + 1);
            sbM5a.append(" \"");
            sbM5a.append(this.outputFile);
            sbM5a.append("\"");
            _println(sbM5a.toString());
        }
    }

    public void genMatch(GrammarAtom grammarAtom) {
        if (grammarAtom instanceof StringLiteralElement) {
            if (this.grammar instanceof LexerGrammar) {
                genMatchUsingAtomText(grammarAtom);
                return;
            }
        } else {
            if (grammarAtom instanceof CharLiteralElement) {
                this.antlrTool.error("cannot ref character literals in grammar: " + grammarAtom);
                return;
            }
            if (!(grammarAtom instanceof TokenRefElement)) {
                if (grammarAtom instanceof WildcardElement) {
                    gen((WildcardElement) grammarAtom);
                    return;
                }
                return;
            }
        }
        genMatchUsingAtomTokenType(grammarAtom);
    }

    public void genMatch(BitSet bitSet) {
    }

    public void genMatchUsingAtomText(GrammarAtom grammarAtom) {
        String strConvertJavaToCppString;
        String strM3a = this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST(_t),") : "_t," : "";
        if ((this.grammar instanceof LexerGrammar) && (!this.saveText || grammarAtom.getAutoGenType() == 3)) {
            println("_saveIndex = text.length();");
        }
        print(grammarAtom.not ? "matchNot(" : "match(");
        _print(strM3a);
        if (grammarAtom.atomText.equals("EOF")) {
            strConvertJavaToCppString = namespaceAntlr + "Token::EOF_TYPE";
        } else {
            strConvertJavaToCppString = this.grammar instanceof LexerGrammar ? convertJavaToCppString(grammarAtom.atomText, false) : grammarAtom.atomText;
        }
        _print(strConvertJavaToCppString);
        _println(");");
        if (this.grammar instanceof LexerGrammar) {
            if (!this.saveText || grammarAtom.getAutoGenType() == 3) {
                println("text.erase(_saveIndex);");
            }
        }
    }

    public void genMatchUsingAtomTokenType(GrammarAtom grammarAtom) {
        StringBuilder sbM5a = C0000a.m5a(this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST(_t),") : "_t," : "");
        sbM5a.append(getValueString(grammarAtom.getType()));
        String string = sbM5a.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(grammarAtom.not ? "matchNot(" : "match(");
        sb.append(string);
        sb.append(");");
        println(sb.toString());
    }

    public void genNextToken() {
        boolean z;
        String strM3a;
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
            RuleBlock ruleBlockCreateNextTokenRule = MakeGrammar.createNextTokenRule(grammar, grammar.rules, "nextToken");
            RuleSymbol ruleSymbol2 = new RuleSymbol("mnextToken");
            ruleSymbol2.setDefined();
            ruleSymbol2.setBlock(ruleBlockCreateNextTokenRule);
            ruleSymbol2.access = "private";
            this.grammar.define(ruleSymbol2);
            this.grammar.theLLkAnalyzer.deterministic(ruleBlockCreateNextTokenRule);
            LexerGrammar lexerGrammar = (LexerGrammar) this.grammar;
            String str = lexerGrammar.filterMode ? lexerGrammar.filterRule : null;
            println("");
            println(namespaceAntlr + "RefToken " + this.grammar.getClassName() + "::nextToken()");
            println("{");
            this.tabs = this.tabs + 1;
            println(namespaceAntlr + "RefToken theRetToken;");
            println("for (;;) {");
            this.tabs = this.tabs + 1;
            println(namespaceAntlr + "RefToken theRetToken;");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("int _ttype = ");
            C0000a.m8a(sb2, namespaceAntlr, "Token::INVALID_TYPE;", this);
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
            println("try {   // for lexical and char stream error handling");
            this.tabs++;
            for (int i2 = 0; i2 < ruleBlockCreateNextTokenRule.getAlternatives().size(); i2++) {
                if (ruleBlockCreateNextTokenRule.getAlternativeAt(i2).cache[1].containsEpsilon()) {
                    this.antlrTool.warning("found optional path in nextToken()");
                }
            }
            String property = System.getProperty("line.separator");
            CppBlockFinishingInfo cppBlockFinishingInfoGenCommonBlock = genCommonBlock(ruleBlockCreateNextTokenRule, false);
            String strM2a = C0000a.m2a("if (LA(1)==EOF_CHAR)" + property + "\t\t\t\t{" + property + "\t\t\t\t\tuponEOF();" + property + "\t\t\t\t\t_returnToken = makeToken(" + namespaceAntlr + "Token::EOF_TYPE);" + property + "\t\t\t\t}", property, "\t\t\t\t");
            if (!((LexerGrammar) this.grammar).filterMode) {
                strM3a = C0000a.m3a(C0000a.m9b(strM2a, "else {"), this.throwNoViable, "}");
            } else if (str == null) {
                strM3a = C0000a.m1a(strM2a, "else {consume(); goto tryAgain;}");
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(strM2a);
                sb3.append("else {");
                sb3.append(property);
                sb3.append("\t\t\t\t\tcommit();");
                sb3.append(property);
                sb3.append("\t\t\t\t\ttry {m");
                sb3.append(str);
                sb3.append("(false);}");
                sb3.append(property);
                sb3.append("\t\t\t\t\tcatch(");
                sb3.append(namespaceAntlr);
                sb3.append("RecognitionException& e) {");
                sb3.append(property);
                sb3.append("\t\t\t\t\t\t// catastrophic failure");
                sb3.append(property);
                sb3.append("\t\t\t\t\t\treportError(e);");
                sb3.append(property);
                sb3.append("\t\t\t\t\t\tconsume();");
                sb3.append(property);
                sb3.append("\t\t\t\t\t}");
                sb3.append(property);
                sb3.append("\t\t\t\t\tgoto tryAgain;");
                strM3a = C0000a.m3a(sb3, property, "\t\t\t\t}");
            }
            genBlockFinish(cppBlockFinishingInfoGenCommonBlock, strM3a);
            if (((LexerGrammar) this.grammar).filterMode && str != null) {
                println("commit();");
            }
            println("if ( !_returnToken )" + property + "\t\t\t\tgoto tryAgain; // found SKIP token" + property);
            println("_ttype = _returnToken->getType();");
            if (((LexerGrammar) this.grammar).getTestLiterals()) {
                genLiteralsTest();
            }
            println("_returnToken->setType(_ttype);");
            println("return _returnToken;");
            this.tabs--;
            println("}");
            StringBuilder sb4 = new StringBuilder();
            sb4.append("catch (");
            C0000a.m8a(sb4, namespaceAntlr, "RecognitionException& e) {", this);
            this.tabs++;
            if (((LexerGrammar) this.grammar).filterMode) {
                println("if ( !getCommitToPath() ) {");
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
                    println("catch(" + namespaceAntlr + "RecognitionException& ee) {");
                    println("\t// horrendous failure: error in filter rule");
                    println("\treportError(ee);");
                    println("\tconsume();");
                    println("}");
                    this.tabs--;
                    println("}");
                    println("else");
                }
            }
            if (ruleBlockCreateNextTokenRule.getDefaultErrorHandler()) {
                println("{");
                this.tabs++;
                println("reportError(e);");
                println("consume();");
                this.tabs--;
                println("}");
            } else {
                this.tabs++;
                C0000a.m8a(C0000a.m5a("throw "), namespaceAntlr, "TokenStreamRecognitionException(e);", this);
                this.tabs--;
            }
            this.tabs--;
            println("}");
            println("catch (" + namespaceAntlr + "CharStreamIOException& csie) {");
            println("\tthrow " + namespaceAntlr + "TokenStreamIOException(csie.io);");
            println("}");
            println("catch (" + namespaceAntlr + "CharStreamException& cse) {");
            println("\tthrow " + namespaceAntlr + "TokenStreamException(cse.getMessage());");
            println("}");
            _println("tryAgain:;");
            this.tabs = this.tabs - 1;
            println("}");
            this.tabs--;
            println("}");
        } else {
            println("");
            StringBuilder sb5 = new StringBuilder();
            sb5.append(namespaceAntlr);
            sb5.append("RefToken ");
            C0000a.m4a(this.grammar, sb5, "::nextToken() { return ");
            sb5.append(namespaceAntlr);
            sb5.append("RefToken(new ");
            sb5.append(namespaceAntlr);
            sb5.append("CommonToken(");
            sb5.append(namespaceAntlr);
            sb5.append("Token::EOF_TYPE, \"\")); }");
            println(sb5.toString());
        }
        println("");
    }

    public void genRule(RuleSymbol ruleSymbol, boolean z, int i, String str) {
        String str2;
        int i2;
        int i3;
        int i4;
        StringBuilder sb;
        String str3;
        StringBuilder sbM5a;
        String str4;
        String string;
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            PrintStream printStream = System.out;
            StringBuilder sbM5a2 = C0000a.m5a("genRule(");
            sbM5a2.append(ruleSymbol.getId());
            sbM5a2.append(")");
            printStream.println(sbM5a2.toString());
        }
        if (!ruleSymbol.isDefined()) {
            Tool tool = this.antlrTool;
            StringBuilder sbM5a3 = C0000a.m5a("undefined rule: ");
            sbM5a3.append(ruleSymbol.getId());
            tool.error(sbM5a3.toString());
            return;
        }
        RuleBlock block = ruleSymbol.getBlock();
        this.currentRule = block;
        this.currentASTResult = ruleSymbol.getId();
        this.declaredASTVariables.clear();
        boolean z2 = this.genAST;
        this.genAST = z2 && block.getAutoGen();
        this.saveText = block.getAutoGen();
        String str5 = ruleSymbol.comment;
        if (str5 != null) {
            _println(str5);
        }
        if (block.returnAction != null) {
            str2 = extractTypeOfAction(block.returnAction, block.getLine(), block.getColumn()) + " ";
        } else {
            str2 = "void ";
        }
        _print(str2);
        StringBuilder sbM5a4 = C0000a.m5a(str);
        sbM5a4.append(ruleSymbol.getId());
        sbM5a4.append("(");
        _print(sbM5a4.toString());
        _print(this.commonExtraParams);
        if (this.commonExtraParams.length() != 0 && block.argAction != null) {
            _print(",");
        }
        if (block.argAction != null) {
            _println("");
            this.tabs++;
            String str6 = block.argAction;
            int iIndexOf = str6.indexOf(61);
            if (iIndexOf != -1) {
                String strTrim = str6;
                String str7 = "";
                string = str7;
                int iIndexOf2 = 0;
                while (iIndexOf2 != -1 && iIndexOf != -1) {
                    StringBuilder sbM9b = C0000a.m9b(string, str7);
                    sbM9b.append(strTrim.substring(0, iIndexOf).trim());
                    string = sbM9b.toString();
                    str7 = ", ";
                    iIndexOf2 = strTrim.indexOf(44, iIndexOf);
                    if (iIndexOf2 != -1 && (iIndexOf = (strTrim = strTrim.substring(iIndexOf2 + 1).trim()).indexOf(61)) == -1) {
                        string = C0000a.m2a(string, ", ", strTrim);
                    }
                }
            } else {
                string = str6;
            }
            println(string);
            i2 = 1;
            this.tabs--;
            print(") ");
        } else {
            i2 = 1;
            _print(") ");
        }
        _println("{");
        this.tabs += i2;
        Grammar grammar = this.grammar;
        if (grammar.traceRules) {
            if (!(grammar instanceof TreeWalkerGrammar)) {
                sbM5a = C0000a.m5a("Tracer traceInOut(this, \"");
                sbM5a.append(ruleSymbol.getId());
                str4 = "\");";
            } else if (this.usingCustomAST) {
                StringBuilder sbM5a5 = C0000a.m5a("Tracer traceInOut(this,\"");
                sbM5a5.append(ruleSymbol.getId());
                sbM5a5.append("\",");
                C0000a.m8a(sbM5a5, namespaceAntlr, "RefAST(_t));", this);
            } else {
                sbM5a = C0000a.m5a("Tracer traceInOut(this,\"");
                sbM5a.append(ruleSymbol.getId());
                str4 = "\",_t);";
            }
            sbM5a.append(str4);
            println(sbM5a.toString());
        }
        if (block.returnAction != null) {
            genLineNo(block);
            println(block.returnAction + ";");
            genLineNo2();
        }
        if (!this.commonLocalVars.equals("")) {
            println(this.commonLocalVars);
        }
        if (this.grammar instanceof LexerGrammar) {
            if (ruleSymbol.getId().equals("mEOF")) {
                C0000a.m8a(C0000a.m5a("_ttype = "), namespaceAntlr, "Token::EOF_TYPE;", this);
            } else {
                StringBuilder sbM5a6 = C0000a.m5a("_ttype = ");
                sbM5a6.append(ruleSymbol.getId().substring(1));
                sbM5a6.append(";");
                println(sbM5a6.toString());
            }
            C0000a.m8a(new StringBuilder(), namespaceStd, "string::size_type _saveIndex;", this);
        }
        Grammar grammar2 = this.grammar;
        if (grammar2.debuggingOutput) {
            if (grammar2 instanceof ParserGrammar) {
                sb = new StringBuilder();
                sb.append("fireEnterRule(");
                sb.append(i);
                str3 = ",0);";
            } else if (grammar2 instanceof LexerGrammar) {
                sb = new StringBuilder();
                sb.append("fireEnterRule(");
                sb.append(i);
                str3 = ",_ttype);";
            }
            sb.append(str3);
            println(sb.toString());
        }
        if (this.grammar instanceof TreeWalkerGrammar) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.labeledElementASTType);
            sb2.append(" ");
            sb2.append(ruleSymbol.getId());
            sb2.append("_AST_in = (_t == ");
            sb2.append(this.labeledElementASTType);
            sb2.append("(ASTNULL)) ? ");
            C0000a.m8a(sb2, this.labeledElementASTInit, " : _t;", this);
        }
        if (this.grammar.buildAST) {
            StringBuilder sbM5a7 = C0000a.m5a("returnAST = ");
            sbM5a7.append(this.labeledElementASTInit);
            sbM5a7.append(";");
            println(sbM5a7.toString());
            println(namespaceAntlr + "ASTPair currentAST;");
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.labeledElementASTType);
            sb3.append(" ");
            sb3.append(ruleSymbol.getId());
            sb3.append("_AST = ");
            C0000a.m8a(sb3, this.labeledElementASTInit, ";", this);
        }
        genBlockPreamble(block);
        genBlockInitAction(block);
        println("");
        ExceptionSpec exceptionSpecFindExceptionSpec = block.findExceptionSpec("");
        if (exceptionSpecFindExceptionSpec != null || block.getDefaultErrorHandler()) {
            println("try {      // for error handling");
            i3 = 1;
            this.tabs++;
        } else {
            i3 = 1;
        }
        if (block.alternatives.size() == i3) {
            Alternative alternativeAt = block.getAlternativeAt(0);
            String str8 = alternativeAt.semPred;
            if (str8 != null) {
                genSemPred(str8, this.currentRule.line);
            }
            if (alternativeAt.synPred != null) {
                this.antlrTool.warning("Syntactic predicate ignored for single alternative", this.grammar.getFilename(), alternativeAt.synPred.getLine(), alternativeAt.synPred.getColumn());
            }
            genAlt(alternativeAt, block);
        } else {
            this.grammar.theLLkAnalyzer.deterministic(block);
            genBlockFinish(genCommonBlock(block, false), this.throwNoViable);
        }
        if (exceptionSpecFindExceptionSpec != null || block.getDefaultErrorHandler()) {
            this.tabs--;
            println("}");
        }
        if (exceptionSpecFindExceptionSpec != null) {
            genErrorHandler(exceptionSpecFindExceptionSpec);
        } else if (block.getDefaultErrorHandler()) {
            C0000a.m8a(C0000a.m5a("catch ("), this.exceptionThrown, "& ex) {", this);
            this.tabs++;
            if (this.grammar.hasSyntacticPredicate) {
                println("if( inputState->guessing == 0 ) {");
                this.tabs++;
            }
            println("reportError(ex);");
            Grammar grammar3 = this.grammar;
            if (grammar3 instanceof TreeWalkerGrammar) {
                C0000a.m8a(C0000a.m5a("if ( _t != "), this.labeledElementASTInit, " )", this);
                i4 = 1;
                this.tabs++;
                println("_t = _t->getNextSibling();");
                this.tabs--;
            } else {
                println("recover(ex," + getBitsetName(markBitsetForGen(grammar3.theLLkAnalyzer.FOLLOW(1, block.endNode).fset)) + ");");
                i4 = 1;
            }
            if (this.grammar.hasSyntacticPredicate) {
                this.tabs -= i4;
                println("} else {");
                this.tabs += i4;
                println("throw;");
                this.tabs -= i4;
                println("}");
            }
            this.tabs -= i4;
            println("}");
        }
        if (this.grammar.buildAST) {
            StringBuilder sbM5a8 = C0000a.m5a("returnAST = ");
            sbM5a8.append(ruleSymbol.getId());
            sbM5a8.append("_AST;");
            println(sbM5a8.toString());
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
            StringBuilder sbM5a9 = C0000a.m5a("if ( _createToken && _token==");
            sbM5a9.append(namespaceAntlr);
            sbM5a9.append("nullToken && _ttype!=");
            sbM5a9.append(namespaceAntlr);
            sbM5a9.append("Token::SKIP ) {");
            println(sbM5a9.toString());
            println("   _token = makeToken(_ttype);");
            println("   _token->setText(text.substr(_begin, text.length()-_begin));");
            println("}");
            println("_returnToken = _token;");
            println("_saveIndex=0;");
        }
        if (block.returnAction != null) {
            StringBuilder sbM5a10 = C0000a.m5a("return ");
            sbM5a10.append(extractIdOfAction(block.returnAction, block.getLine(), block.getColumn()));
            sbM5a10.append(";");
            println(sbM5a10.toString());
        }
        this.tabs--;
        println("}");
        println("");
        this.genAST = z2;
    }

    public void genRuleHeader(RuleSymbol ruleSymbol, boolean z) {
        String str;
        this.tabs = 1;
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            PrintStream printStream = System.out;
            StringBuilder sbM5a = C0000a.m5a("genRuleHeader(");
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
        this.currentRule = block;
        this.currentASTResult = ruleSymbol.getId();
        boolean z2 = this.genAST;
        this.genAST = z2 && block.getAutoGen();
        this.saveText = block.getAutoGen();
        print(ruleSymbol.access + ": ");
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
        _println(";");
        this.tabs--;
        this.genAST = z2;
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
        println("if (!(" + strProcessActionForSpecialSymbols + "))");
        this.tabs = this.tabs + 1;
        StringBuilder sbM5a2 = C0000a.m5a("throw ");
        sbM5a2.append(namespaceAntlr);
        sbM5a2.append("SemanticException(\"");
        sbM5a2.append(strEscapeString);
        sbM5a2.append("\");");
        println(sbM5a2.toString());
        this.tabs--;
    }

    public void genSemPredMap(String str) {
        Enumeration enumerationElements = this.semPreds.elements();
        println("const char* " + str + "_semPredNames[] = {");
        this.tabs = this.tabs + 1;
        while (enumerationElements.hasMoreElements()) {
            StringBuilder sbM5a = C0000a.m5a("\"");
            sbM5a.append(enumerationElements.nextElement());
            sbM5a.append("\",");
            println(sbM5a.toString());
        }
        println("0");
        this.tabs--;
        println("};");
    }

    public void genSynPred(SynPredBlock synPredBlock, String str) {
        StringBuilder sbM5a;
        String str2;
        StringBuilder sbM5a2;
        String str3;
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("gen=>(" + synPredBlock + ")");
        }
        StringBuilder sbM5a3 = C0000a.m5a("bool synPredMatched");
        sbM5a3.append(synPredBlock.f302ID);
        sbM5a3.append(" = false;");
        println(sbM5a3.toString());
        if (this.grammar instanceof TreeWalkerGrammar) {
            C0000a.m8a(C0000a.m5a("if (_t == "), this.labeledElementASTInit, " )", this);
            this.tabs++;
            println("_t = ASTNULL;");
            this.tabs--;
        }
        println("if (" + str + ") {");
        this.tabs = this.tabs + 1;
        if (this.grammar instanceof TreeWalkerGrammar) {
            sbM5a = new StringBuilder();
            sbM5a.append(this.labeledElementType);
            sbM5a.append(" __t");
            sbM5a.append(synPredBlock.f302ID);
            str2 = " = _t;";
        } else {
            sbM5a = C0000a.m5a("int _m");
            sbM5a.append(synPredBlock.f302ID);
            str2 = " = mark();";
        }
        sbM5a.append(str2);
        println(sbM5a.toString());
        StringBuilder sbM5a4 = C0000a.m5a("synPredMatched");
        sbM5a4.append(synPredBlock.f302ID);
        sbM5a4.append(" = true;");
        println(sbM5a4.toString());
        println("inputState->guessing++;");
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
        StringBuilder sb = new StringBuilder();
        sb.append("catch (");
        C0000a.m8a(sb, this.exceptionThrown, "& pe) {", this);
        this.tabs++;
        StringBuilder sbM5a5 = C0000a.m5a("synPredMatched");
        sbM5a5.append(synPredBlock.f302ID);
        sbM5a5.append(" = false;");
        println(sbM5a5.toString());
        this.tabs--;
        println("}");
        if (this.grammar instanceof TreeWalkerGrammar) {
            sbM5a2 = C0000a.m5a("_t = __t");
            sbM5a2.append(synPredBlock.f302ID);
            str3 = ";";
        } else {
            sbM5a2 = C0000a.m5a("rewind(_m");
            sbM5a2.append(synPredBlock.f302ID);
            str3 = ");";
        }
        sbM5a2.append(str3);
        println(sbM5a2.toString());
        println("inputState->guessing--;");
        Grammar grammar2 = this.grammar;
        if (grammar2.debuggingOutput && ((grammar2 instanceof ParserGrammar) || (grammar2 instanceof LexerGrammar))) {
            StringBuilder sbM5a6 = C0000a.m5a("if (synPredMatched");
            sbM5a6.append(synPredBlock.f302ID);
            sbM5a6.append(")");
            println(sbM5a6.toString());
            println("  fireSyntacticPredicateSucceeded();");
            println("else");
            println("  fireSyntacticPredicateFailed();");
        }
        this.syntacticPredLevel--;
        this.tabs--;
        println("}");
        println("if ( synPredMatched" + synPredBlock.f302ID + " ) {");
    }

    public void genTokenStrings(String str) {
        TokenSymbol tokenSymbol;
        println("const char* " + str + "tokenNames[] = {");
        this.tabs = this.tabs + 1;
        Vector vocabulary = this.grammar.tokenManager.getVocabulary();
        for (int i = 0; i < vocabulary.size(); i++) {
            String strStripFrontBack = (String) vocabulary.elementAt(i);
            if (strStripFrontBack == null) {
                StringBuilder sbM5a = C0000a.m5a("<");
                sbM5a.append(String.valueOf(i));
                sbM5a.append(">");
                strStripFrontBack = sbM5a.toString();
            }
            if (!strStripFrontBack.startsWith("\"") && !strStripFrontBack.startsWith("<") && (tokenSymbol = this.grammar.tokenManager.getTokenSymbol(strStripFrontBack)) != null && tokenSymbol.getParaphrase() != null) {
                strStripFrontBack = StringUtils.stripFrontBack(tokenSymbol.getParaphrase(), "\"", "\"");
            }
            print(this.charFormatter.literalString(strStripFrontBack));
            _println(",");
        }
        println("0");
        this.tabs--;
        println("};");
    }

    public void genTokenTypes(TokenManager tokenManager) {
        StringBuilder sb;
        String string;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(tokenManager.getName());
        this.outputFile = C0000a.m3a(sb2, CodeGenerator.TokenTypesFileSuffix, ".hpp");
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.tabs = 0;
        StringBuilder sbM5a = C0000a.m5a("#ifndef INC_");
        sbM5a.append(tokenManager.getName());
        sbM5a.append(CodeGenerator.TokenTypesFileSuffix);
        sbM5a.append("_hpp_");
        println(sbM5a.toString());
        println("#define INC_" + tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix + "_hpp_");
        println("");
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        genHeader(this.outputFile);
        println("");
        println("#ifndef CUSTOM_API");
        println("# define CUSTOM_API");
        println("#endif");
        println("");
        println("#ifdef __cplusplus");
        println("struct CUSTOM_API " + tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix + " {");
        println("#endif");
        this.tabs = this.tabs + 1;
        println("enum {");
        this.tabs = this.tabs + 1;
        Vector vocabulary = tokenManager.getVocabulary();
        println("EOF_ = 1,");
        for (int i = 4; i < vocabulary.size(); i++) {
            String str = (String) vocabulary.elementAt(i);
            if (str != null) {
                if (str.startsWith("\"")) {
                    StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenManager.getTokenSymbol(str);
                    if (stringLiteralSymbol == null) {
                        this.antlrTool.panic("String literal " + str + " not in symbol table");
                    } else if (stringLiteralSymbol.label != null) {
                        string = stringLiteralSymbol.label + " = " + i + ",";
                    } else {
                        String strMangleLiteral = mangleLiteral(str);
                        if (strMangleLiteral != null) {
                            println(strMangleLiteral + " = " + i + ",");
                            stringLiteralSymbol.label = strMangleLiteral;
                        } else {
                            sb = new StringBuilder();
                            sb.append("// ");
                            sb.append(str);
                            sb.append(" = ");
                            sb.append(i);
                            string = sb.toString();
                        }
                    }
                } else if (!str.startsWith("<")) {
                    sb = new StringBuilder();
                    sb.append(str);
                    sb.append(" = ");
                    sb.append(i);
                    sb.append(",");
                    string = sb.toString();
                }
                println(string);
            }
        }
        println("NULL_TREE_LOOKAHEAD = 3");
        this.tabs--;
        println("};");
        this.tabs--;
        println("#ifdef __cplusplus");
        println("};");
        println("#endif");
        NameSpace nameSpace3 = nameSpace;
        if (nameSpace3 != null) {
            nameSpace3.emitClosures(this.currentOutput);
        }
        StringBuilder sbM5a2 = C0000a.m5a("#endif /*INC_");
        sbM5a2.append(tokenManager.getName());
        C0000a.m8a(sbM5a2, CodeGenerator.TokenTypesFileSuffix, "_hpp_*/", this);
        this.currentOutput.close();
        this.currentOutput = null;
        exitIfError();
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(GrammarAtom grammarAtom, String str) {
        if (grammarAtom == null || grammarAtom.getASTNodeType() == null) {
            boolean z = str.indexOf(44) != -1 ? this.grammar.tokenManager.tokenDefined(str.substring(0, str.indexOf(44))) : false;
            if (this.usingCustomAST) {
                Grammar grammar = this.grammar;
                if ((grammar instanceof TreeWalkerGrammar) && !grammar.tokenManager.tokenDefined(str) && !z) {
                    StringBuilder sbM5a = C0000a.m5a("astFactory->create(");
                    sbM5a.append(namespaceAntlr);
                    sbM5a.append("RefAST(");
                    sbM5a.append(str);
                    sbM5a.append("))");
                    return sbM5a.toString();
                }
            }
            return C0000a.m2a("astFactory->create(", str, ")");
        }
        this.astTypes.ensureCapacity(grammarAtom.getType());
        String str2 = (String) this.astTypes.elementAt(grammarAtom.getType());
        if (str2 == null || grammarAtom.getASTNodeType().equals(str2)) {
            this.astTypes.setElementAt(grammarAtom.getASTNodeType(), grammarAtom.getType());
        } else {
            Tool tool = this.antlrTool;
            StringBuilder sbM5a2 = C0000a.m5a("Attempt to redefine AST type for ");
            sbM5a2.append(grammarAtom.getText());
            tool.warning(sbM5a2.toString(), this.grammar.getFilename(), grammarAtom.getLine(), grammarAtom.getColumn());
            Tool tool2 = this.antlrTool;
            StringBuilder sbM10b = C0000a.m10b(" from \"", str2, "\" to \"");
            sbM10b.append(grammarAtom.getASTNodeType());
            sbM10b.append("\" sticking to \"");
            sbM10b.append(str2);
            sbM10b.append("\"");
            tool2.warning(sbM10b.toString(), this.grammar.getFilename(), grammarAtom.getLine(), grammarAtom.getColumn());
        }
        return C0000a.m2a("astFactory->create(", str, ")");
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(Vector vector) {
        if (vector.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.labeledElementASTType + "(astFactory->make((new " + namespaceAntlr + "ASTArray(" + vector.size() + "))");
        for (int i = 0; i < vector.size(); i++) {
            StringBuilder sbM5a = C0000a.m5a("->add(");
            sbM5a.append(vector.elementAt(i));
            sbM5a.append(")");
            stringBuffer.append(sbM5a.toString());
        }
        stringBuffer.append("))");
        return stringBuffer.toString();
    }

    public String getASTCreateString(String str) {
        if (!this.usingCustomAST) {
            return C0000a.m2a("astFactory->create(", str, ")");
        }
        return this.labeledElementASTType + "(astFactory->create(" + namespaceAntlr + "RefAST(" + str + ")))";
    }

    public String getLookaheadTestExpression(Alternative alternative, int i) {
        int i2 = alternative.lookaheadDepth;
        if (i2 == Integer.MAX_VALUE) {
            i2 = this.grammar.maxk;
        }
        return i == 0 ? "true" : C0000a.m3a(C0000a.m5a("("), getLookaheadTestExpression(alternative.cache, i2), ")");
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
                stringBuffer.append(" || ");
            }
            stringBuffer.append(strLookaheadString);
            stringBuffer.append(" == ");
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

    @Override // antlr.CodeGenerator
    public String mapTreeId(String str, ActionTransInfo actionTransInfo) {
        boolean z;
        if (this.currentRule == null) {
            return str;
        }
        Grammar grammar = this.grammar;
        if (grammar instanceof TreeWalkerGrammar) {
            z = !grammar.buildAST;
            if (str.length() > 3 && str.lastIndexOf("_in") == str.length() - 3) {
                str = str.substring(0, str.length() - 3);
                z = true;
            }
        } else {
            z = false;
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

    public void printAction(Token token) {
        if (token != null) {
            genLineNo(token.getLine());
            printTabs();
            _printAction(processActionForSpecialSymbols(token.getText(), token.getLine(), null, null));
            genLineNo2();
        }
    }

    public void printHeaderAction(String str) {
        Token token = (Token) this.behavior.headerActions.get(str);
        if (token != null) {
            genLineNo(token.getLine());
            println(processActionForSpecialSymbols(token.getText(), token.getLine(), null, null));
            genLineNo2();
        }
    }

    @Override // antlr.CodeGenerator
    public void println(String str) {
        if (str != null) {
            printTabs();
            this.outputLine = countLines(str) + 1 + this.outputLine;
            this.currentOutput.println(str);
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
                return namespaceAntlr + "RefAST(" + str + ")";
            }
        }
        return str;
    }
}
