package antlr;

import antlr.actions.cpp.ActionLexer;
import antlr.collections.impl.BitSet;
import antlr.collections.impl.Vector;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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
            String processActionForSpecialSymbols = processActionForSpecialSymbols(ruleRefElement.args, ruleRefElement.line, this.currentRule, actionTransInfo);
            if (actionTransInfo.assignToRoot || actionTransInfo.refRuleRoot != null) {
                Tool tool = this.antlrTool;
                StringBuilder m5a = C0000a.m5a("Arguments of rule reference '");
                m5a.append(ruleRefElement.targetRule);
                m5a.append("' cannot set or ref #");
                m5a.append(this.currentRule.getRuleName());
                m5a.append(" on line ");
                m5a.append(ruleRefElement.getLine());
                tool.error(m5a.toString());
            }
            _print(processActionForSpecialSymbols);
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

    /* JADX WARN: Removed duplicated region for block: B:53:0x01cb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x028a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String convertJavaToCppString(String str, boolean z) {
        Tool tool;
        StringBuilder sb;
        String str2;
        String str3;
        String str4;
        int i;
        int i2;
        int i3;
        int i4;
        int charAt;
        char c2;
        boolean z2;
        StringBuilder m5a;
        String escapeChar;
        String sb2;
        int charAt2;
        String str5 = new String();
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
        String substring = str.substring(1, str.length() - 1);
        Grammar grammar = this.grammar;
        str3 = "";
        if (grammar instanceof LexerGrammar) {
            int size = ((LexerGrammar) grammar).charVocabulary.size() - 1;
            str3 = size > 255 ? "L" : "";
            i = size;
            i3 = 0;
            str4 = str5;
            i2 = 0;
        } else {
            str4 = str5;
            i = 255;
            i2 = 0;
            i3 = 0;
        }
        while (i2 < substring.length()) {
            if (substring.charAt(i2) == '\\') {
                int i5 = i2 + 1;
                if (substring.length() == i5) {
                    Tool tool2 = this.antlrTool;
                    StringBuilder m10b = C0000a.m10b("Invalid escape in char literal: '", str, "' looking at '");
                    m10b.append(substring.substring(i2));
                    m10b.append("'");
                    tool2.error(m10b.toString());
                }
                char charAt3 = substring.charAt(i5);
                if (charAt3 == '\"' || charAt3 == '\'' || charAt3 == '\\') {
                    charAt2 = substring.charAt(i5);
                } else if (charAt3 == 'f') {
                    charAt2 = 12;
                } else if (charAt3 == 'n') {
                    charAt2 = 10;
                } else if (charAt3 == 'r') {
                    charAt2 = 13;
                } else if (charAt3 != 'a') {
                    if (charAt3 == 'b') {
                        i2 += 2;
                        i3 = 8;
                    } else if (charAt3 == 't') {
                        charAt2 = 9;
                    } else if (charAt3 != 'u') {
                        switch (charAt3) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                                i4 = i2 + 2;
                                if (!charIsDigit(substring, i4)) {
                                    charAt = substring.charAt(i5) - '0';
                                    break;
                                } else {
                                    int i6 = i2 + 3;
                                    if (!charIsDigit(substring, i6)) {
                                        charAt2 = (substring.charAt(i4) - '0') + ((substring.charAt(i5) - '0') * 8);
                                        i2 = i6;
                                        i3 = charAt2;
                                        break;
                                    } else {
                                        i3 = (substring.charAt(i6) - '0') + ((substring.charAt(i4) - '0') * 8) + ((substring.charAt(i5) - '0') * 8 * 8);
                                        i2 += 4;
                                        break;
                                    }
                                }
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                                int i7 = i2 + 2;
                                boolean charIsDigit = charIsDigit(substring, i7);
                                substring.charAt(i5);
                                if (charIsDigit) {
                                    substring.charAt(i7);
                                    i2 += 3;
                                } else {
                                    i2 = i7;
                                }
                            default:
                                Tool tool3 = this.antlrTool;
                                StringBuilder m10b2 = C0000a.m10b("Unhandled escape in char literal: '", str, "' looking at '");
                                m10b2.append(substring.substring(i2));
                                m10b2.append("'");
                                tool3.error(m10b2.toString());
                                i3 = 0;
                                break;
                        }
                    } else {
                        int i8 = i2 + 5;
                        if (i8 < substring.length()) {
                            int digit = (Character.digit(substring.charAt(i2 + 4), 16) * 16) + (Character.digit(substring.charAt(i2 + 3), 16) * 16 * 16) + (Character.digit(substring.charAt(i2 + 2), 16) * 16 * 16 * 16);
                            i2 += 6;
                            i3 = Character.digit(substring.charAt(i8), 16) + digit;
                        } else {
                            Tool tool4 = this.antlrTool;
                            StringBuilder m10b3 = C0000a.m10b("Invalid escape in char literal: '", str, "' looking at '");
                            m10b3.append(substring.substring(i2));
                            m10b3.append("'");
                            tool4.error(m10b3.toString());
                        }
                    }
                    if ((this.grammar instanceof LexerGrammar) && i3 > i) {
                        if (32 <= i3 || i3 >= 127) {
                            StringBuilder m5a2 = C0000a.m5a("0x");
                            m5a2.append(Integer.toString(i3, 16));
                            sb2 = m5a2.toString();
                        } else {
                            sb2 = this.charFormatter.escapeChar(i3, true);
                        }
                        Tool tool5 = this.antlrTool;
                        StringBuilder m5a3 = C0000a.m5a("Character out of range in ");
                        m5a3.append(!z ? "char literal" : "string constant");
                        m5a3.append(": '");
                        m5a3.append(substring);
                        m5a3.append("'");
                        tool5.error(m5a3.toString());
                        this.antlrTool.error("Vocabulary size: " + i + " Character " + sb2);
                    }
                    if (z) {
                        if (i2 != substring.length()) {
                            this.antlrTool.error("Invalid char literal: '" + str + "'");
                        }
                        c2 = 255;
                        if (i > 255) {
                            z2 = true;
                            m5a = C0000a.m5a("L'");
                        } else if (i3 > 255 || (i3 & 128) == 0) {
                            z2 = true;
                            m5a = C0000a.m5a("'");
                        } else {
                            m5a = C0000a.m5a("static_cast<unsigned char>('");
                            z2 = true;
                            m5a.append(this.charFormatter.escapeChar(i3, true));
                            escapeChar = "')";
                        }
                        m5a.append(this.charFormatter.escapeChar(i3, z2));
                        m5a.append("'");
                        str4 = m5a.toString();
                    } else {
                        c2 = 255;
                        z2 = true;
                        m5a = C0000a.m5a(str4);
                        escapeChar = this.charFormatter.escapeChar(i3, true);
                    }
                    m5a.append(escapeChar);
                    str4 = m5a.toString();
                } else {
                    charAt2 = 7;
                }
                i2 += 2;
                i3 = charAt2;
                if (this.grammar instanceof LexerGrammar) {
                    if (32 <= i3) {
                    }
                    StringBuilder m5a22 = C0000a.m5a("0x");
                    m5a22.append(Integer.toString(i3, 16));
                    sb2 = m5a22.toString();
                    Tool tool52 = this.antlrTool;
                    StringBuilder m5a32 = C0000a.m5a("Character out of range in ");
                    m5a32.append(!z ? "char literal" : "string constant");
                    m5a32.append(": '");
                    m5a32.append(substring);
                    m5a32.append("'");
                    tool52.error(m5a32.toString());
                    this.antlrTool.error("Vocabulary size: " + i + " Character " + sb2);
                }
                if (z) {
                }
                m5a.append(escapeChar);
                str4 = m5a.toString();
            } else {
                i4 = i2 + 1;
                charAt = substring.charAt(i2);
            }
            i3 = charAt;
            i2 = i4;
            if (this.grammar instanceof LexerGrammar) {
            }
            if (z) {
            }
            m5a.append(escapeChar);
            str4 = m5a.toString();
        }
        if (z) {
            return str4;
        }
        return str3 + "\"" + str4 + "\"";
    }

    private String fixNameSpaceOption(String str) {
        String stripFrontBack = StringUtils.stripFrontBack(str, "\"", "\"");
        return (stripFrontBack.length() <= 2 || stripFrontBack.substring(stripFrontBack.length() - 2, stripFrontBack.length()).equals("::")) ? stripFrontBack : C0000a.m1a(stripFrontBack, "::");
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

    private void genElementAST(AlternativeElement alternativeElement) {
        String str;
        String sb;
        String str2;
        StringBuilder sb2;
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
                C0000a.m8a(sb4, str4, ";", this);
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
                str = alternativeElement.getLabel();
                sb = alternativeElement.getLabel();
            } else {
                str = this.lt1Value;
                StringBuilder m5a2 = C0000a.m5a("tmp");
                m5a2.append(this.astVarNumber);
                sb = m5a2.toString();
                this.astVarNumber++;
            }
            if (z2) {
                if (alternativeElement instanceof GrammarAtom) {
                    GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
                    if (grammarAtom.getASTNodeType() != null) {
                        StringBuilder m5a3 = C0000a.m5a("Ref");
                        m5a3.append(grammarAtom.getASTNodeType());
                        str3 = m5a3.toString();
                        genASTDeclaration(alternativeElement, sb, str3);
                    }
                }
                str3 = this.labeledElementASTType;
                genASTDeclaration(alternativeElement, sb, str3);
            }
            String m1a = C0000a.m1a(sb, "_AST");
            mapTreeVariable(alternativeElement, m1a);
            if (this.grammar instanceof TreeWalkerGrammar) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(this.labeledElementASTType);
                sb5.append(" ");
                sb5.append(m1a);
                sb5.append("_in = ");
                C0000a.m8a(sb5, this.labeledElementASTInit, ";", this);
            }
            if (z) {
                println("if ( inputState->guessing == 0 ) {");
                this.tabs++;
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
            if (alternativeElement.getLabel() == null && z2) {
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
                if (autoGenType == 1) {
                    str2 = "astFactory->addASTChild(currentAST, ";
                    if (!this.usingCustomAST && (!(alternativeElement instanceof GrammarAtom) || ((GrammarAtom) alternativeElement).getASTNodeType() == null)) {
                        sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(m1a);
                        sb2.append(");");
                    }
                    sb2 = C0000a.m5a(str2);
                    sb2.append(namespaceAntlr);
                    sb2.append("RefAST(");
                    sb2.append(m1a);
                    sb2.append("));");
                } else if (autoGenType == 2) {
                    str2 = "astFactory->makeASTRoot(currentAST, ";
                    if (!this.usingCustomAST && (!(alternativeElement instanceof GrammarAtom) || ((GrammarAtom) alternativeElement).getASTNodeType() == null)) {
                        sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(m1a);
                        sb2.append(");");
                    }
                    sb2 = C0000a.m5a(str2);
                    sb2.append(namespaceAntlr);
                    sb2.append("RefAST(");
                    sb2.append(m1a);
                    sb2.append("));");
                }
                println(sb2.toString());
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
            m5a.append(") {");
            println(m5a.toString());
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
        String str = alternativeElement.enclosingRuleName;
        if (this.grammar instanceof LexerGrammar) {
            str = CodeGenerator.encodeLexerRuleName(str);
        }
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(str);
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
        String mangleLiteral = mangleLiteral(id);
        return mangleLiteral == null ? String.valueOf(i) : mangleLiteral;
    }

    private String lookaheadString(int i) {
        if (this.grammar instanceof TreeWalkerGrammar) {
            return "_t->getType()";
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
        Token option2;
        String stripFrontBack;
        Token option3;
        String stripFrontBack2;
        Token option4;
        Token option5;
        String stripFrontBack3;
        StringBuilder m5a;
        String str;
        String str2;
        Token option6;
        String stripFrontBack4;
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
            if (grammar.hasOption("namespaceAntlr") && (option3 = grammar.getOption("namespaceAntlr")) != null && (stripFrontBack2 = StringUtils.stripFrontBack(option3.getText(), "\"", "\"")) != null) {
                if (stripFrontBack2.length() > 2 && !stripFrontBack2.substring(stripFrontBack2.length() - 2, stripFrontBack2.length()).equals("::")) {
                    stripFrontBack2 = C0000a.m1a(stripFrontBack2, "::");
                }
                namespaceAntlr = stripFrontBack2;
            }
            if (grammar.hasOption("namespaceStd") && (option2 = grammar.getOption("namespaceStd")) != null && (stripFrontBack = StringUtils.stripFrontBack(option2.getText(), "\"", "\"")) != null) {
                if (stripFrontBack.length() > 2 && !stripFrontBack.substring(stripFrontBack.length() - 2, stripFrontBack.length()).equals("::")) {
                    stripFrontBack = C0000a.m1a(stripFrontBack, "::");
                }
                namespaceStd = stripFrontBack;
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
            if (grammar.hasOption("ASTLabelType") && (option6 = grammar.getOption("ASTLabelType")) != null && (stripFrontBack4 = StringUtils.stripFrontBack(option6.getText(), "\"", "\"")) != null) {
                this.usingCustomAST = true;
                this.labeledElementASTType = stripFrontBack4;
                this.labeledElementASTInit = C0000a.m3a(C0000a.m9b(stripFrontBack4, "("), namespaceAntlr, "nullAST)");
            }
            this.labeledElementType = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefToken ");
            this.labeledElementInit = C0000a.m3a(new StringBuilder(), namespaceAntlr, "nullToken");
            this.commonExtraArgs = "";
            this.commonExtraParams = "";
            this.commonLocalVars = "";
            this.lt1Value = "LT(1)";
            this.exceptionThrown = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RecognitionException");
            m5a = C0000a.m5a("throw ");
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
                if (grammar.hasOption("ASTLabelType") && (option5 = grammar.getOption("ASTLabelType")) != null && (stripFrontBack3 = StringUtils.stripFrontBack(option5.getText(), "\"", "\"")) != null) {
                    this.usingCustomAST = true;
                    this.labeledElementASTType = stripFrontBack3;
                    this.labeledElementType = stripFrontBack3;
                    this.labeledElementInit = C0000a.m3a(C0000a.m9b(stripFrontBack3, "("), namespaceAntlr, "nullAST)");
                    this.labeledElementASTInit = this.labeledElementInit;
                    this.commonExtraParams = C0000a.m1a(stripFrontBack3, " _t");
                    StringBuilder m5a2 = C0000a.m5a("throw ");
                    m5a2.append(namespaceAntlr);
                    m5a2.append("NoViableAltException(");
                    this.throwNoViable = C0000a.m3a(m5a2, namespaceAntlr, "RefAST(_t));");
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
            StringBuilder m5a3 = C0000a.m5a("int _ttype; ");
            m5a3.append(namespaceAntlr);
            m5a3.append("RefToken _token; ");
            this.commonLocalVars = C0000a.m3a(m5a3, namespaceStd, "string::size_type _begin = text.length();");
            this.lt1Value = "LA(1)";
            this.exceptionThrown = C0000a.m3a(new StringBuilder(), namespaceAntlr, "RecognitionException");
            m5a = C0000a.m5a("throw ");
            str = namespaceAntlr;
            str2 = "NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());";
        }
        this.throwNoViable = C0000a.m3a(m5a, str, str2);
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
            Enumeration elements = this.behavior.grammars.elements();
            while (elements.hasMoreElements()) {
                Grammar grammar = (Grammar) elements.nextElement();
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
        String processActionForSpecialSymbols = processActionForSpecialSymbols(actionElement.actionText, actionElement.getLine(), this.currentRule, actionTransInfo);
        if (actionTransInfo.refRuleRoot != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(actionTransInfo.refRuleRoot);
            sb.append(" = ");
            C0000a.m8a(sb, this.labeledElementASTType, "(currentAST.root);", this);
        }
        genLineNo(actionElement);
        printAction(processActionForSpecialSymbols);
        genLineNo2();
        if (actionTransInfo.assignToRoot) {
            StringBuilder m5a = C0000a.m5a("currentAST.root = ");
            m5a.append(actionTransInfo.refRuleRoot);
            m5a.append(";");
            println(m5a.toString());
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
            StringBuilder m5a = C0000a.m5a("genCharRangeElement(");
            m5a.append(charRangeElement.beginText);
            m5a.append("..");
            m5a.append(charRangeElement.endText);
            m5a.append(")");
            printStream.println(m5a.toString());
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
        StringBuilder m5a2 = C0000a.m5a("matchRange(");
        m5a2.append(convertJavaToCppString(charRangeElement.beginText, true));
        m5a2.append(",");
        C0000a.m8a(m5a2, convertJavaToCppString(charRangeElement.endText, true), ");", this);
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

    /* JADX WARN: Removed duplicated region for block: B:24:0x00d2  */
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
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("gen+(" + oneOrMoreBlock + ")");
        }
        println("{ // ( ... )+");
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
        println("for (;;) {");
        this.tabs++;
        genBlockInitAction(oneOrMoreBlock);
        String str = this.currentASTResult;
        if (oneOrMoreBlock.getLabel() != null) {
            this.currentASTResult = oneOrMoreBlock.getLabel();
        }
        this.grammar.theLLkAnalyzer.deterministic(oneOrMoreBlock);
        int i3 = this.grammar.maxk;
        if (!oneOrMoreBlock.greedy && (i2 = oneOrMoreBlock.exitLookaheadDepth) <= i3 && oneOrMoreBlock.exitCache[i2].containsEpsilon()) {
            i3 = oneOrMoreBlock.exitLookaheadDepth;
        } else if (oneOrMoreBlock.greedy || oneOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
            i = i3;
            z = false;
            if (z) {
                if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
                    PrintStream printStream = System.out;
                    StringBuilder m5a3 = C0000a.m5a("nongreedy (...)+ loop; exit depth is ");
                    m5a3.append(oneOrMoreBlock.exitLookaheadDepth);
                    printStream.println(m5a3.toString());
                }
                String lookaheadTestExpression = getLookaheadTestExpression(oneOrMoreBlock.exitCache, i);
                println("// nongreedy exit test");
                println("if ( " + sb2 + ">=1 && " + lookaheadTestExpression + ") goto " + sb + ";");
            }
            CppBlockFinishingInfo genCommonBlock = genCommonBlock(oneOrMoreBlock, false);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("if ( ");
            sb3.append(sb2);
            sb3.append(">=1 ) { goto ");
            sb3.append(sb);
            sb3.append("; } else {");
            genBlockFinish(genCommonBlock, C0000a.m3a(sb3, this.throwNoViable, "}"));
            println(sb2 + "++;");
            this.tabs = this.tabs - 1;
            println("}");
            println(sb + ":;");
            println("}  // ( ... )+");
            this.currentASTResult = str;
        }
        i = i3;
        z = true;
        if (z) {
        }
        CppBlockFinishingInfo genCommonBlock2 = genCommonBlock(oneOrMoreBlock, false);
        StringBuilder sb32 = new StringBuilder();
        sb32.append("if ( ");
        sb32.append(sb2);
        sb32.append(">=1 ) { goto ");
        sb32.append(sb);
        sb32.append("; } else {");
        genBlockFinish(genCommonBlock2, C0000a.m3a(sb32, this.throwNoViable, "}"));
        println(sb2 + "++;");
        this.tabs = this.tabs - 1;
        println("}");
        println(sb + ":;");
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
        StringBuilder m5a = C0000a.m5a("matchRange(");
        m5a.append(tokenRangeElement.beginText);
        m5a.append(",");
        C0000a.m8a(m5a, tokenRangeElement.endText, ");", this);
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
            StringBuilder m5a = C0000a.m5a("currentAST = __currentAST");
            m5a.append(treeElement.f302ID);
            m5a.append(";");
            println(m5a.toString());
        }
        StringBuilder m5a2 = C0000a.m5a("_t = __t");
        m5a2.append(treeElement.f302ID);
        m5a2.append(";");
        println(m5a2.toString());
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

    /* JADX WARN: Removed duplicated region for block: B:12:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(WildcardElement wildcardElement) {
        StringBuilder m5a;
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
            m5a = C0000a.m5a("if ( _t == ");
            m5a.append(this.labeledElementASTInit);
            m5a.append(" ) throw ");
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
            m5a = C0000a.m5a("matchNot(");
            valueString = getValueString(1);
            str = ");";
        }
        C0000a.m8a(m5a, valueString, str, this);
        if (this.grammar instanceof TreeWalkerGrammar) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0096  */
    @Override // antlr.CodeGenerator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void gen(ZeroOrMoreBlock zeroOrMoreBlock) {
        String sb;
        int i;
        boolean z;
        int i2;
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("gen*(" + zeroOrMoreBlock + ")");
        }
        println("{ // ( ... )*");
        genBlockPreamble(zeroOrMoreBlock);
        if (zeroOrMoreBlock.getLabel() != null) {
            sb = zeroOrMoreBlock.getLabel();
        } else {
            StringBuilder m5a = C0000a.m5a("_loop");
            m5a.append(zeroOrMoreBlock.f302ID);
            sb = m5a.toString();
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
        if (!zeroOrMoreBlock.greedy && (i2 = zeroOrMoreBlock.exitLookaheadDepth) <= i3 && zeroOrMoreBlock.exitCache[i2].containsEpsilon()) {
            i3 = zeroOrMoreBlock.exitLookaheadDepth;
        } else if (zeroOrMoreBlock.greedy || zeroOrMoreBlock.exitLookaheadDepth != Integer.MAX_VALUE) {
            i = i3;
            z = false;
            if (z) {
                if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
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
                C0000a.m8a(sb2, sb, ";", this);
            }
            genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), C0000a.m2a("goto ", sb, ";"));
            this.tabs--;
            println("}");
            println(sb + ":;");
            println("} // ( ... )*");
            this.currentASTResult = str;
        }
        i = i3;
        z = true;
        if (z) {
        }
        genBlockFinish(genCommonBlock(zeroOrMoreBlock, false), C0000a.m2a("goto ", sb, ";"));
        this.tabs--;
        println("}");
        println(sb + ":;");
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
        String str3 = this.labeledElementASTInit;
        if (alternativeElement instanceof GrammarAtom) {
            GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
            if (grammarAtom.getASTNodeType() != null) {
                StringBuilder m5a = C0000a.m5a("Ref");
                m5a.append(grammarAtom.getASTNodeType());
                m5a.append("(");
                str3 = C0000a.m3a(m5a, this.labeledElementASTInit, ")");
            }
        }
        println(str2 + " " + str + "_AST = " + str3 + ";");
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
        StringBuilder m5a;
        String tokenStringAt;
        TokenManager tokenManager = this.grammar.tokenManager;
        println("");
        for (int i2 = 0; i2 < vector.size(); i2++) {
            BitSet bitSet = (BitSet) vector.elementAt(i2);
            bitSet.growToInclude(i);
            println("const unsigned long " + str + getBitsetName(i2) + "_data_[] = { " + bitSet.toStringOfHalfWords() + " };");
            String str2 = "// ";
            for (int i3 = 0; i3 < tokenManager.getVocabulary().size(); i3++) {
                if (bitSet.member(i3)) {
                    if (!(this.grammar instanceof LexerGrammar)) {
                        m5a = C0000a.m5a(str2);
                        tokenStringAt = tokenManager.getTokenStringAt(i3);
                    } else if (32 > i3 || i3 >= 127 || i3 == 92) {
                        m5a = C0000a.m9b(str2, "0x");
                        tokenStringAt = Integer.toString(i3, 16);
                    } else {
                        m5a = C0000a.m5a(str2);
                        tokenStringAt = this.charFormatter.escapeChar(i3, true);
                    }
                    m5a.append(tokenStringAt);
                    m5a.append(" ");
                    str2 = m5a.toString();
                    if (str2.length() > 70) {
                        println(str2);
                        str2 = "// ";
                    }
                }
            }
            if (str2 != "// ") {
                println(str2);
            }
            StringBuilder m5a2 = C0000a.m5a("const ");
            m5a2.append(namespaceAntlr);
            m5a2.append("BitSet ");
            m5a2.append(str);
            m5a2.append(getBitsetName(i2));
            m5a2.append("(");
            m5a2.append(getBitsetName(i2));
            m5a2.append("_data_,");
            m5a2.append(bitSet.size() / 32);
            m5a2.append(");");
            println(m5a2.toString());
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
                                C0000a.m8a(sb, this.labeledElementInit, ";", this);
                                if (!this.grammar.buildAST) {
                                }
                                genASTDeclaration(alternativeElement);
                            }
                        }
                        if (this.grammar.buildAST) {
                            genASTDeclaration(alternativeElement);
                        }
                        if (this.grammar instanceof LexerGrammar) {
                            println(namespaceAntlr + "RefToken " + alternativeElement.getLabel() + ";");
                        }
                        if (this.grammar instanceof TreeWalkerGrammar) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(this.labeledElementType);
                            sb2.append(" ");
                            sb2.append(alternativeElement.getLabel());
                            sb2.append(" = ");
                            C0000a.m8a(sb2, this.labeledElementInit, ";", this);
                        }
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(this.labeledElementType);
                        sb3.append(" ");
                        sb3.append(alternativeElement.getLabel());
                        sb3.append(" = ");
                        C0000a.m8a(sb3, this.labeledElementInit, ";", this);
                        if (this.grammar.buildAST) {
                            if (alternativeElement instanceof GrammarAtom) {
                                GrammarAtom grammarAtom = (GrammarAtom) alternativeElement;
                                if (grammarAtom.getASTNodeType() != null) {
                                    StringBuilder m5a = C0000a.m5a("Ref");
                                    m5a.append(grammarAtom.getASTNodeType());
                                    genASTDeclaration(alternativeElement, m5a.toString());
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
        StringBuilder m10b;
        String str;
        StringBuilder m10b2;
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
        String str2 = grammar.superClass;
        if (str2 == null) {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            str2 = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
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
            m10b = C0000a.m10b(": ", str2, "(new ");
            m10b.append(namespaceAntlr);
            m10b.append("DebuggingInputBuffer(new ");
            m10b.append(namespaceAntlr);
            str = "CharBuffer(in)),";
        } else {
            m10b = C0000a.m10b(": ", str2, "(new ");
            m10b.append(namespaceAntlr);
            str = "CharBuffer(in),";
        }
        m10b.append(str);
        m10b.append(lexerGrammar.caseSensitive);
        m10b.append(")");
        println(m10b.toString());
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
            m10b2 = C0000a.m10b(": ", str2, "(new ");
            m10b2.append(namespaceAntlr);
            m10b2.append("DebuggingInputBuffer(ib),");
        } else {
            m10b2 = C0000a.m10b(": ", str2, "(ib,");
        }
        m10b2.append(lexerGrammar.caseSensitive);
        m10b2.append(")");
        println(m10b2.toString());
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
        StringBuilder m10b3 = C0000a.m10b(": ", str2, "(state,");
        m10b3.append(lexerGrammar.caseSensitive);
        m10b3.append(")");
        println(m10b3.toString());
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
        StringBuilder m5a = C0000a.m5a("void ");
        m5a.append(this.grammar.getClassName());
        m5a.append("::initLiterals()");
        println(m5a.toString());
        println("{");
        this.tabs++;
        Enumeration tokenSymbolKeys = this.grammar.tokenManager.getTokenSymbolKeys();
        while (tokenSymbolKeys.hasMoreElements()) {
            String str3 = (String) tokenSymbolKeys.nextElement();
            if (str3.charAt(0) == '\"') {
                TokenSymbol tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str3);
                if (tokenSymbol instanceof StringLiteralSymbol) {
                    StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenSymbol;
                    StringBuilder m5a2 = C0000a.m5a("literals[");
                    m5a2.append(stringLiteralSymbol.getId());
                    m5a2.append("] = ");
                    m5a2.append(stringLiteralSymbol.getTokenType());
                    m5a2.append(";");
                    println(m5a2.toString());
                }
            }
        }
        this.tabs--;
        println("}");
        if (this.grammar.debuggingOutput) {
            StringBuilder m5a3 = C0000a.m5a("const char* ");
            m5a3.append(this.grammar.getClassName());
            m5a3.append("::_ruleNames[] = {");
            println(m5a3.toString());
            this.tabs++;
            Enumeration elements = this.grammar.rules.elements();
            while (elements.hasMoreElements()) {
                GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
                if (grammarSymbol instanceof RuleSymbol) {
                    StringBuilder m5a4 = C0000a.m5a("\"");
                    m5a4.append(((RuleSymbol) grammarSymbol).getId());
                    m5a4.append("\",");
                    println(m5a4.toString());
                }
            }
            println("0");
            this.tabs--;
            println("};");
        }
        genNextToken();
        Enumeration elements2 = this.grammar.rules.elements();
        int i = 0;
        while (elements2.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) elements2.nextElement();
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
        String str = grammar.superClass;
        if (str == null) {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            str = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        if (this.grammar.debuggingOutput) {
            StringBuilder m5a = C0000a.m5a("const char* ");
            m5a.append(this.grammar.getClassName());
            m5a.append("::_ruleNames[] = {");
            println(m5a.toString());
            this.tabs++;
            Enumeration elements = this.grammar.rules.elements();
            while (elements.hasMoreElements()) {
                GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
                if (grammarSymbol instanceof RuleSymbol) {
                    StringBuilder m5a2 = C0000a.m5a("\"");
                    m5a2.append(((RuleSymbol) grammarSymbol).getId());
                    m5a2.append("\",");
                    println(m5a2.toString());
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
        println(": " + str + "(tokenBuf,k)");
        println("{");
        println("}");
        println("");
        StringBuilder sb2 = new StringBuilder();
        C0000a.m4a(this.grammar, sb2, "::");
        sb2.append(this.grammar.getClassName());
        print(sb2.toString());
        println("(" + namespaceAntlr + "TokenBuffer& tokenBuf)");
        println(": " + str + "(tokenBuf," + this.grammar.maxk + ")");
        println("{");
        println("}");
        println("");
        StringBuilder sb3 = new StringBuilder();
        C0000a.m4a(this.grammar, sb3, "::");
        sb3.append(this.grammar.getClassName());
        print(sb3.toString());
        println("(" + namespaceAntlr + "TokenStream& lexer, int k)");
        println(": " + str + "(lexer,k)");
        println("{");
        println("}");
        println("");
        StringBuilder sb4 = new StringBuilder();
        C0000a.m4a(this.grammar, sb4, "::");
        sb4.append(this.grammar.getClassName());
        print(sb4.toString());
        println("(" + namespaceAntlr + "TokenStream& lexer)");
        println(": " + str + "(lexer," + this.grammar.maxk + ")");
        println("{");
        println("}");
        println("");
        StringBuilder sb5 = new StringBuilder();
        C0000a.m4a(this.grammar, sb5, "::");
        sb5.append(this.grammar.getClassName());
        print(sb5.toString());
        println("(const " + namespaceAntlr + "ParserSharedInputState& state)");
        println(": " + str + "(state," + this.grammar.maxk + ")");
        println("{");
        println("}");
        println("");
        if (this.noConstructors) {
            println("// constructor creation turned of with 'noConstructor' option");
            println("#endif");
        }
        this.astTypes = new Vector();
        Enumeration elements2 = this.grammar.rules.elements();
        int i = 0;
        while (elements2.hasMoreElements()) {
            GrammarSymbol grammarSymbol2 = (GrammarSymbol) elements2.nextElement();
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
        Enumeration elements = this.grammar.rules.elements();
        int i = 0;
        while (elements.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
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

    /* JADX WARN: Code restructure failed: missing block: B:196:0x049a, code lost:
    
        if (r29 > 0) goto L187;
     */
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
        boolean lookaheadIsEmpty;
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
            Lookahead look = this.analyzer.look(1, alternativeBlock2);
            if (alternativeBlock.getLabel() != null && this.syntacticPredLevel == 0) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(alternativeBlock.getLabel());
                sb3.append(" = ");
                C0000a.m8a(sb3, this.lt1Value, ";", this);
            }
            genElementAST(alternativeBlock);
            StringBuilder m9b = C0000a.m9b("match(", this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST(_t),") : "_t," : "");
            m9b.append(getBitsetName(markBitsetForGen(look.fset)));
            m9b.append(");");
            println(m9b.toString());
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
            String lookaheadString = lookaheadString(1);
            if (this.grammar instanceof TreeWalkerGrammar) {
                C0000a.m8a(C0000a.m5a("if (_t == "), this.labeledElementASTInit, " )", this);
                this.tabs++;
                println("_t = ASTNULL;");
                this.tabs--;
            }
            println("switch ( " + lookaheadString + ") {");
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
                    i13 = i4 + 1;
                    str10 = str5;
                    z7 = z8;
                    z6 = z4;
                    z2 = z5;
                    i9 = i3;
                    str9 = str4;
                    cppBlockFinishingInfo3 = cppBlockFinishingInfo2;
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
                            i13 = i4 + 1;
                            str10 = str5;
                            z7 = z8;
                            z6 = z4;
                            z2 = z5;
                            i9 = i3;
                            str9 = str4;
                            cppBlockFinishingInfo3 = cppBlockFinishingInfo2;
                        } else {
                            lookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, i2);
                        }
                    } else {
                        lookaheadIsEmpty = lookaheadIsEmpty(alternativeAt3, grammar2.maxk);
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
                    } else if (lookaheadIsEmpty && alternativeAt3.semPred == null && alternativeAt3.synPred == null) {
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
                            String processActionForSpecialSymbols = processActionForSpecialSymbols(alternativeAt3.semPred, alternativeBlock2.line, this.currentRule, new ActionTransInfo());
                            Grammar grammar3 = this.grammar;
                            if (grammar3.debuggingOutput && ((grammar3 instanceof ParserGrammar) || (grammar3 instanceof LexerGrammar))) {
                                StringBuilder m10b = C0000a.m10b("(", lookaheadTestExpression, "&& fireSemanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent.PREDICTING,");
                                m10b.append(addSemPred(this.charFormatter.escapeString(processActionForSpecialSymbols)));
                                m10b.append(",");
                                lookaheadTestExpression = C0000a.m3a(m10b, processActionForSpecialSymbols, "))");
                            } else {
                                lookaheadTestExpression = "(" + lookaheadTestExpression + "&&(" + processActionForSpecialSymbols + "))";
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
                                i13 = i4 + 1;
                                str10 = str5;
                                z7 = z8;
                                z6 = z4;
                                z2 = z5;
                                i9 = i3;
                                str9 = str4;
                                cppBlockFinishingInfo3 = cppBlockFinishingInfo2;
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
                            i13 = i4 + 1;
                            str10 = str5;
                            z7 = z8;
                            z6 = z4;
                            z2 = z5;
                            i9 = i3;
                            str9 = str4;
                            cppBlockFinishingInfo3 = cppBlockFinishingInfo2;
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
                        i13 = i4 + 1;
                        str10 = str5;
                        z7 = z8;
                        z6 = z4;
                        z2 = z5;
                        i9 = i3;
                        str9 = str4;
                        cppBlockFinishingInfo3 = cppBlockFinishingInfo2;
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
                    i13 = i4 + 1;
                    str10 = str5;
                    z7 = z8;
                    z6 = z4;
                    z2 = z5;
                    i9 = i3;
                    str9 = str4;
                    cppBlockFinishingInfo3 = cppBlockFinishingInfo2;
                }
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
        String str12 = str2;
        for (int i16 = 1; i16 <= i10; i16++) {
            this.tabs--;
            str12 = C0000a.m1a(str12, str11);
        }
        this.genAST = z9;
        this.saveText = z11;
        if (z10) {
            this.tabs--;
            cppBlockFinishingInfo = cppBlockFinishingInfo4;
            cppBlockFinishingInfo.postscript = C0000a.m1a(str12, str11);
            cppBlockFinishingInfo.generatedSwitch = true;
            if (i15 <= 0) {
                z3 = false;
            }
            z3 = true;
        } else {
            cppBlockFinishingInfo = cppBlockFinishingInfo4;
            cppBlockFinishingInfo.postscript = str12;
            z3 = false;
            cppBlockFinishingInfo.generatedSwitch = false;
        }
        cppBlockFinishingInfo.generatedAnIf = z3;
        return cppBlockFinishingInfo;
    }

    public void genHeader(String str) {
        StringBuilder m5a = C0000a.m5a("/* $ANTLR ");
        m5a.append(Tool.version);
        m5a.append(": \"");
        Tool tool = this.antlrTool;
        m5a.append(tool.fileMinusPath(tool.grammarFile));
        m5a.append("\" -> \"");
        m5a.append(str);
        m5a.append("\"$ */");
        println(m5a.toString());
    }

    public void genInclude(LexerGrammar lexerGrammar) {
        String stripFrontBack;
        this.outputFile = this.grammar.getClassName() + ".hpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = false;
        this.saveText = true;
        this.tabs = 0;
        StringBuilder m5a = C0000a.m5a("#ifndef INC_");
        m5a.append(this.grammar.getClassName());
        m5a.append("_hpp_");
        println(m5a.toString());
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
        String str = grammar.superClass;
        if (str != null) {
            println("\n// Include correct superclass header with a header statement for example:");
            println("// header \"post_include_hpp\" {");
            println("// #include \"" + str + ".hpp\"");
            println("// }");
            println("// Or....");
            println("// header {");
            println("// #include \"" + str + ".hpp\"");
            println("// }\n");
        } else {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            println("#include <antlr/" + superClass + ".hpp>");
            str = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        printHeaderAction(postIncludeHpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printHeaderAction("");
        String str2 = this.grammar.comment;
        if (str2 != null) {
            _println(str2);
        }
        StringBuilder m5a2 = C0000a.m5a("class CUSTOM_API ");
        m5a2.append(this.grammar.getClassName());
        m5a2.append(" : public ");
        m5a2.append(str);
        print(m5a2.toString());
        println(", public " + this.grammar.tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
        Token token = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token != null && (stripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) != null) {
            print(", " + stripFrontBack);
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
        StringBuilder m5a3 = C0000a.m5a("return ");
        m5a3.append(lexerGrammar.caseSensitiveLiterals);
        m5a3.append(";");
        println(m5a3.toString());
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
        Enumeration elements = this.grammar.rules.elements();
        while (elements.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) elements.nextElement();
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
        StringBuilder m5a4 = C0000a.m5a("#endif /*INC_");
        m5a4.append(this.grammar.getClassName());
        m5a4.append("_hpp_*/");
        println(m5a4.toString());
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genInclude(ParserGrammar parserGrammar) {
        String stripFrontBack;
        this.outputFile = this.grammar.getClassName() + ".hpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        StringBuilder m5a = C0000a.m5a("#ifndef INC_");
        m5a.append(this.grammar.getClassName());
        m5a.append("_hpp_");
        println(m5a.toString());
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
        String str = grammar.superClass;
        if (str != null) {
            println("\n// Include correct superclass header with a header statement for example:");
            println("// header \"post_include_hpp\" {");
            println("// #include \"" + str + ".hpp\"");
            println("// }");
            println("// Or....");
            println("// header {");
            println("// #include \"" + str + ".hpp\"");
            println("// }\n");
        } else {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            println("#include <antlr/" + superClass + ".hpp>");
            str = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        println("");
        printHeaderAction(postIncludeHpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printHeaderAction("");
        String str2 = this.grammar.comment;
        if (str2 != null) {
            _println(str2);
        }
        StringBuilder m5a2 = C0000a.m5a("class CUSTOM_API ");
        m5a2.append(this.grammar.getClassName());
        m5a2.append(" : public ");
        m5a2.append(str);
        print(m5a2.toString());
        println(", public " + this.grammar.tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
        Token token = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token != null && (stripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) != null) {
            print(", " + stripFrontBack);
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
        StringBuilder m5a3 = C0000a.m5a("return ");
        m5a3.append(this.grammar.getClassName());
        m5a3.append("::NUM_TOKENS;");
        println(m5a3.toString());
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
        StringBuilder m5a4 = C0000a.m5a("return ");
        m5a4.append(this.grammar.getClassName());
        m5a4.append("::tokenNames;");
        println(m5a4.toString());
        this.tabs--;
        println("}");
        Enumeration elements = this.grammar.rules.elements();
        while (elements.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
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
        StringBuilder m5a5 = C0000a.m5a("#endif /*INC_");
        m5a5.append(this.grammar.getClassName());
        m5a5.append("_hpp_*/");
        println(m5a5.toString());
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genInclude(TreeWalkerGrammar treeWalkerGrammar) {
        String stripFrontBack;
        this.outputFile = this.grammar.getClassName() + ".hpp";
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.genAST = this.grammar.buildAST;
        this.tabs = 0;
        StringBuilder m5a = C0000a.m5a("#ifndef INC_");
        m5a.append(this.grammar.getClassName());
        m5a.append("_hpp_");
        println(m5a.toString());
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
        String str = grammar.superClass;
        if (str != null) {
            println("\n// Include correct superclass header with a header statement for example:");
            println("// header \"post_include_hpp\" {");
            println("// #include \"" + str + ".hpp\"");
            println("// }");
            println("// Or....");
            println("// header {");
            println("// #include \"" + str + ".hpp\"");
            println("// }\n");
        } else {
            String superClass = grammar.getSuperClass();
            if (superClass.lastIndexOf(46) != -1) {
                superClass = superClass.substring(superClass.lastIndexOf(46) + 1);
            }
            println("#include <antlr/" + superClass + ".hpp>");
            str = C0000a.m3a(new StringBuilder(), namespaceAntlr, superClass);
        }
        println("");
        printHeaderAction(postIncludeHpp);
        NameSpace nameSpace2 = nameSpace;
        if (nameSpace2 != null) {
            nameSpace2.emitDeclarations(this.currentOutput);
        }
        printHeaderAction("");
        String str2 = this.grammar.comment;
        if (str2 != null) {
            _println(str2);
        }
        StringBuilder m5a2 = C0000a.m5a("class CUSTOM_API ");
        m5a2.append(this.grammar.getClassName());
        m5a2.append(" : public ");
        m5a2.append(str);
        print(m5a2.toString());
        println(", public " + this.grammar.tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix);
        Token token = (Token) this.grammar.options.get("classHeaderSuffix");
        if (token != null && (stripFrontBack = StringUtils.stripFrontBack(token.getText(), "\"", "\"")) != null) {
            print(", " + stripFrontBack);
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
        StringBuilder m5a3 = C0000a.m5a("static void initializeASTFactory( ");
        m5a3.append(namespaceAntlr);
        m5a3.append("ASTFactory& factory );");
        println(m5a3.toString());
        println("int getNumTokens() const");
        println("{");
        this.tabs++;
        StringBuilder m5a4 = C0000a.m5a("return ");
        m5a4.append(this.grammar.getClassName());
        m5a4.append("::NUM_TOKENS;");
        println(m5a4.toString());
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
        StringBuilder m5a5 = C0000a.m5a("return ");
        m5a5.append(this.grammar.getClassName());
        m5a5.append("::tokenNames;");
        println(m5a5.toString());
        this.tabs--;
        println("}");
        Enumeration elements = this.grammar.rules.elements();
        while (elements.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
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
        StringBuilder m5a6 = C0000a.m5a("#endif /*INC_");
        m5a6.append(this.grammar.getClassName());
        m5a6.append("_hpp_*/");
        println(m5a6.toString());
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public void genInitFactory(Grammar grammar) {
        String str = !grammar.buildAST ? "" : "factory ";
        StringBuilder m5a = C0000a.m5a("void ");
        C0000a.m4a(grammar, m5a, "::initializeASTFactory( ");
        m5a.append(namespaceAntlr);
        m5a.append("ASTFactory& ");
        m5a.append(str);
        m5a.append(")");
        println(m5a.toString());
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
                        StringBuilder m10b = C0000a.m10b("  using ", str3, " ignoring ");
                        m10b.append(tokenSymbol.getASTNodeType());
                        tool.warning(m10b.toString(), this.grammar.getFilename(), 1, 1);
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
            StringBuilder m5a2 = C0000a.m5a("factory.setMaxNodeType(");
            m5a2.append(this.grammar.tokenManager.maxTokenType());
            m5a2.append(");");
            println(m5a2.toString());
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
            StringBuilder m5a = C0000a.m5a("#line ");
            m5a.append(this.outputLine + 1);
            m5a.append(" \"");
            m5a.append(this.outputFile);
            m5a.append("\"");
            _println(m5a.toString());
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
        String convertJavaToCppString;
        String m3a = this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST(_t),") : "_t," : "";
        if ((this.grammar instanceof LexerGrammar) && (!this.saveText || grammarAtom.getAutoGenType() == 3)) {
            println("_saveIndex = text.length();");
        }
        print(grammarAtom.not ? "matchNot(" : "match(");
        _print(m3a);
        if (grammarAtom.atomText.equals("EOF")) {
            convertJavaToCppString = namespaceAntlr + "Token::EOF_TYPE";
        } else {
            convertJavaToCppString = this.grammar instanceof LexerGrammar ? convertJavaToCppString(grammarAtom.atomText, false) : grammarAtom.atomText;
        }
        _print(convertJavaToCppString);
        _println(");");
        if (this.grammar instanceof LexerGrammar) {
            if (!this.saveText || grammarAtom.getAutoGenType() == 3) {
                println("text.erase(_saveIndex);");
            }
        }
    }

    public void genMatchUsingAtomTokenType(GrammarAtom grammarAtom) {
        StringBuilder m5a = C0000a.m5a(this.grammar instanceof TreeWalkerGrammar ? this.usingCustomAST ? C0000a.m3a(new StringBuilder(), namespaceAntlr, "RefAST(_t),") : "_t," : "");
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
            for (int i2 = 0; i2 < createNextTokenRule.getAlternatives().size(); i2++) {
                if (createNextTokenRule.getAlternativeAt(i2).cache[1].containsEpsilon()) {
                    this.antlrTool.warning("found optional path in nextToken()");
                }
            }
            String property = System.getProperty("line.separator");
            CppBlockFinishingInfo genCommonBlock = genCommonBlock(createNextTokenRule, false);
            String m2a = C0000a.m2a("if (LA(1)==EOF_CHAR)" + property + "\t\t\t\t{" + property + "\t\t\t\t\tuponEOF();" + property + "\t\t\t\t\t_returnToken = makeToken(" + namespaceAntlr + "Token::EOF_TYPE);" + property + "\t\t\t\t}", property, "\t\t\t\t");
            if (!((LexerGrammar) this.grammar).filterMode) {
                m3a = C0000a.m3a(C0000a.m9b(m2a, "else {"), this.throwNoViable, "}");
            } else if (str == null) {
                m3a = C0000a.m1a(m2a, "else {consume(); goto tryAgain;}");
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(m2a);
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
                m3a = C0000a.m3a(sb3, property, "\t\t\t\t}");
            }
            genBlockFinish(genCommonBlock, m3a);
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
            if (createNextTokenRule.getDefaultErrorHandler()) {
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
        StringBuilder m5a;
        String str4;
        String str5;
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            PrintStream printStream = System.out;
            StringBuilder m5a2 = C0000a.m5a("genRule(");
            m5a2.append(ruleSymbol.getId());
            m5a2.append(")");
            printStream.println(m5a2.toString());
        }
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
        String str6 = ruleSymbol.comment;
        if (str6 != null) {
            _println(str6);
        }
        if (block.returnAction != null) {
            str2 = extractTypeOfAction(block.returnAction, block.getLine(), block.getColumn()) + " ";
        } else {
            str2 = "void ";
        }
        _print(str2);
        StringBuilder m5a4 = C0000a.m5a(str);
        m5a4.append(ruleSymbol.getId());
        m5a4.append("(");
        _print(m5a4.toString());
        _print(this.commonExtraParams);
        if (this.commonExtraParams.length() != 0 && block.argAction != null) {
            _print(",");
        }
        if (block.argAction != null) {
            _println("");
            this.tabs++;
            String str7 = block.argAction;
            int indexOf = str7.indexOf(61);
            if (indexOf != -1) {
                String str8 = str7;
                String str9 = "";
                str5 = str9;
                int i5 = 0;
                while (i5 != -1 && indexOf != -1) {
                    StringBuilder m9b = C0000a.m9b(str5, str9);
                    m9b.append(str8.substring(0, indexOf).trim());
                    str5 = m9b.toString();
                    str9 = ", ";
                    i5 = str8.indexOf(44, indexOf);
                    if (i5 != -1 && (indexOf = (str8 = str8.substring(i5 + 1).trim()).indexOf(61)) == -1) {
                        str5 = C0000a.m2a(str5, ", ", str8);
                    }
                }
            } else {
                str5 = str7;
            }
            println(str5);
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
                m5a = C0000a.m5a("Tracer traceInOut(this, \"");
                m5a.append(ruleSymbol.getId());
                str4 = "\");";
            } else if (this.usingCustomAST) {
                StringBuilder m5a5 = C0000a.m5a("Tracer traceInOut(this,\"");
                m5a5.append(ruleSymbol.getId());
                m5a5.append("\",");
                C0000a.m8a(m5a5, namespaceAntlr, "RefAST(_t));", this);
            } else {
                m5a = C0000a.m5a("Tracer traceInOut(this,\"");
                m5a.append(ruleSymbol.getId());
                str4 = "\",_t);";
            }
            m5a.append(str4);
            println(m5a.toString());
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
                StringBuilder m5a6 = C0000a.m5a("_ttype = ");
                m5a6.append(ruleSymbol.getId().substring(1));
                m5a6.append(";");
                println(m5a6.toString());
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
            StringBuilder m5a7 = C0000a.m5a("returnAST = ");
            m5a7.append(this.labeledElementASTInit);
            m5a7.append(";");
            println(m5a7.toString());
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
        ExceptionSpec findExceptionSpec = block.findExceptionSpec("");
        if (findExceptionSpec != null || block.getDefaultErrorHandler()) {
            println("try {      // for error handling");
            i3 = 1;
            this.tabs++;
        } else {
            i3 = 1;
        }
        if (block.alternatives.size() == i3) {
            Alternative alternativeAt = block.getAlternativeAt(0);
            String str10 = alternativeAt.semPred;
            if (str10 != null) {
                genSemPred(str10, this.currentRule.line);
            }
            if (alternativeAt.synPred != null) {
                this.antlrTool.warning("Syntactic predicate ignored for single alternative", this.grammar.getFilename(), alternativeAt.synPred.getLine(), alternativeAt.synPred.getColumn());
            }
            genAlt(alternativeAt, block);
        } else {
            this.grammar.theLLkAnalyzer.deterministic(block);
            genBlockFinish(genCommonBlock(block, false), this.throwNoViable);
        }
        if (findExceptionSpec != null || block.getDefaultErrorHandler()) {
            this.tabs--;
            println("}");
        }
        if (findExceptionSpec != null) {
            genErrorHandler(findExceptionSpec);
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
            StringBuilder m5a8 = C0000a.m5a("returnAST = ");
            m5a8.append(ruleSymbol.getId());
            m5a8.append("_AST;");
            println(m5a8.toString());
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
            StringBuilder m5a9 = C0000a.m5a("if ( _createToken && _token==");
            m5a9.append(namespaceAntlr);
            m5a9.append("nullToken && _ttype!=");
            m5a9.append(namespaceAntlr);
            m5a9.append("Token::SKIP ) {");
            println(m5a9.toString());
            println("   _token = makeToken(_ttype);");
            println("   _token->setText(text.substr(_begin, text.length()-_begin));");
            println("}");
            println("_returnToken = _token;");
            println("_saveIndex=0;");
        }
        if (block.returnAction != null) {
            StringBuilder m5a10 = C0000a.m5a("return ");
            m5a10.append(extractIdOfAction(block.returnAction, block.getLine(), block.getColumn()));
            m5a10.append(";");
            println(m5a10.toString());
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
            StringBuilder m5a = C0000a.m5a("genRuleHeader(");
            m5a.append(ruleSymbol.getId());
            m5a.append(")");
            printStream.println(m5a.toString());
        }
        if (!ruleSymbol.isDefined()) {
            Tool tool = this.antlrTool;
            StringBuilder m5a2 = C0000a.m5a("undefined rule: ");
            m5a2.append(ruleSymbol.getId());
            tool.error(m5a2.toString());
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
        this.tabs = this.tabs + 1;
        StringBuilder m5a2 = C0000a.m5a("throw ");
        m5a2.append(namespaceAntlr);
        m5a2.append("SemanticException(\"");
        m5a2.append(escapeString);
        m5a2.append("\");");
        println(m5a2.toString());
        this.tabs--;
    }

    public void genSemPredMap(String str) {
        Enumeration elements = this.semPreds.elements();
        println("const char* " + str + "_semPredNames[] = {");
        this.tabs = this.tabs + 1;
        while (elements.hasMoreElements()) {
            StringBuilder m5a = C0000a.m5a("\"");
            m5a.append(elements.nextElement());
            m5a.append("\",");
            println(m5a.toString());
        }
        println("0");
        this.tabs--;
        println("};");
    }

    public void genSynPred(SynPredBlock synPredBlock, String str) {
        StringBuilder m5a;
        String str2;
        StringBuilder m5a2;
        String str3;
        if (this.DEBUG_CODE_GENERATOR || this.DEBUG_CPP_CODE_GENERATOR) {
            System.out.println("gen=>(" + synPredBlock + ")");
        }
        StringBuilder m5a3 = C0000a.m5a("bool synPredMatched");
        m5a3.append(synPredBlock.f302ID);
        m5a3.append(" = false;");
        println(m5a3.toString());
        if (this.grammar instanceof TreeWalkerGrammar) {
            C0000a.m8a(C0000a.m5a("if (_t == "), this.labeledElementASTInit, " )", this);
            this.tabs++;
            println("_t = ASTNULL;");
            this.tabs--;
        }
        println("if (" + str + ") {");
        this.tabs = this.tabs + 1;
        if (this.grammar instanceof TreeWalkerGrammar) {
            m5a = new StringBuilder();
            m5a.append(this.labeledElementType);
            m5a.append(" __t");
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
        println("inputState->guessing--;");
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
        println("if ( synPredMatched" + synPredBlock.f302ID + " ) {");
    }

    public void genTokenStrings(String str) {
        TokenSymbol tokenSymbol;
        println("const char* " + str + "tokenNames[] = {");
        this.tabs = this.tabs + 1;
        Vector vocabulary = this.grammar.tokenManager.getVocabulary();
        for (int i = 0; i < vocabulary.size(); i++) {
            String str2 = (String) vocabulary.elementAt(i);
            if (str2 == null) {
                StringBuilder m5a = C0000a.m5a("<");
                m5a.append(String.valueOf(i));
                m5a.append(">");
                str2 = m5a.toString();
            }
            if (!str2.startsWith("\"") && !str2.startsWith("<") && (tokenSymbol = this.grammar.tokenManager.getTokenSymbol(str2)) != null && tokenSymbol.getParaphrase() != null) {
                str2 = StringUtils.stripFrontBack(tokenSymbol.getParaphrase(), "\"", "\"");
            }
            print(this.charFormatter.literalString(str2));
            _println(",");
        }
        println("0");
        this.tabs--;
        println("};");
    }

    public void genTokenTypes(TokenManager tokenManager) {
        StringBuilder sb;
        String sb2;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(tokenManager.getName());
        this.outputFile = C0000a.m3a(sb3, CodeGenerator.TokenTypesFileSuffix, ".hpp");
        this.outputLine = 1;
        this.currentOutput = this.antlrTool.openOutputFile(this.outputFile);
        this.tabs = 0;
        StringBuilder m5a = C0000a.m5a("#ifndef INC_");
        m5a.append(tokenManager.getName());
        m5a.append(CodeGenerator.TokenTypesFileSuffix);
        m5a.append("_hpp_");
        println(m5a.toString());
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
                        sb2 = stringLiteralSymbol.label + " = " + i + ",";
                    } else {
                        String mangleLiteral = mangleLiteral(str);
                        if (mangleLiteral != null) {
                            println(mangleLiteral + " = " + i + ",");
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
                    sb.append(str);
                    sb.append(" = ");
                    sb.append(i);
                    sb.append(",");
                    sb2 = sb.toString();
                }
                println(sb2);
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
        StringBuilder m5a2 = C0000a.m5a("#endif /*INC_");
        m5a2.append(tokenManager.getName());
        C0000a.m8a(m5a2, CodeGenerator.TokenTypesFileSuffix, "_hpp_*/", this);
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
                    StringBuilder m5a = C0000a.m5a("astFactory->create(");
                    m5a.append(namespaceAntlr);
                    m5a.append("RefAST(");
                    m5a.append(str);
                    m5a.append("))");
                    return m5a.toString();
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
            StringBuilder m5a2 = C0000a.m5a("Attempt to redefine AST type for ");
            m5a2.append(grammarAtom.getText());
            tool.warning(m5a2.toString(), this.grammar.getFilename(), grammarAtom.getLine(), grammarAtom.getColumn());
            Tool tool2 = this.antlrTool;
            StringBuilder m10b = C0000a.m10b(" from \"", str2, "\" to \"");
            m10b.append(grammarAtom.getASTNodeType());
            m10b.append("\" sticking to \"");
            m10b.append(str2);
            m10b.append("\"");
            tool2.warning(m10b.toString(), this.grammar.getFilename(), grammarAtom.getLine(), grammarAtom.getColumn());
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
            StringBuilder m5a = C0000a.m5a("->add(");
            m5a.append(vector.elementAt(i));
            m5a.append(")");
            stringBuffer.append(m5a.toString());
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
                stringBuffer.append(" || ");
            }
            stringBuffer.append(lookaheadString);
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
        StringBuilder m5a = C0000a.m5a("(");
        m5a.append(lookaheadString(i));
        m5a.append(" >= ");
        m5a.append(getValueString(i2));
        m5a.append(" && ");
        m5a.append(lookaheadString(i));
        m5a.append(" <= ");
        return C0000a.m3a(m5a, getValueString(i3), ")");
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
