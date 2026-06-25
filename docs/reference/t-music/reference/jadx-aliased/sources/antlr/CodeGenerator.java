package antlr;

import antlr.collections.impl.BitSet;
import antlr.collections.impl.Vector;
import java.io.PrintWriter;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public abstract class CodeGenerator {
    public static final int BITSET_OPTIMIZE_INIT_THRESHOLD = 8;
    public static final int DEFAULT_BITSET_TEST_THRESHOLD = 4;
    public static final int DEFAULT_MAKE_SWITCH_THRESHOLD = 2;
    public static boolean OLD_ACTION_TRANSLATOR = true;
    public static String TokenTypesFileExt = ".txt";
    public static String TokenTypesFileSuffix = "TokenTypes";
    public LLkGrammarAnalyzer analyzer;
    public Tool antlrTool;
    public DefineGrammarSymbols behavior;
    public Vector bitsetsUsed;
    public CharFormatter charFormatter;
    public transient PrintWriter currentOutput;
    public int tabs = 0;
    public Grammar grammar = null;
    public boolean DEBUG_CODE_GENERATOR = false;
    public int makeSwitchThreshold = 2;
    public int bitsetTestThreshold = 4;

    public static String decodeLexerRuleName(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(1, str.length());
    }

    public static boolean elementsAreRange(int[] iArr) {
        if (iArr.length == 0) {
            return false;
        }
        int i = iArr[0];
        int i2 = iArr[iArr.length - 1];
        if (iArr.length <= 2 || (i2 - i) + 1 > iArr.length) {
            return false;
        }
        int i3 = i + 1;
        for (int i4 = 1; i4 < iArr.length - 1; i4++) {
            if (i3 != iArr[i4]) {
                return false;
            }
            i3++;
        }
        return true;
    }

    public static String encodeLexerRuleName(String str) {
        return C0000a.m1a("m", str);
    }

    private void reset() {
        this.tabs = 0;
        this.bitsetsUsed = new Vector();
        this.currentOutput = null;
        this.grammar = null;
        this.DEBUG_CODE_GENERATOR = false;
        this.makeSwitchThreshold = 2;
        this.bitsetTestThreshold = 4;
    }

    public static String reverseLexerRuleName(String str) {
        return str.substring(1, str.length());
    }

    public void _print(String str) {
        if (str != null) {
            this.currentOutput.print(str);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x006b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0053 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void _printAction(String str) {
        int i;
        boolean z;
        if (str == null) {
            return;
        }
        int i2 = 0;
        while (i2 < str.length() && Character.isSpaceChar(str.charAt(i2))) {
            i2++;
        }
        int length = str.length() - 1;
        while (length > i2 && Character.isSpaceChar(str.charAt(length))) {
            length--;
        }
        while (i2 <= length) {
            char charAt = str.charAt(i2);
            int i3 = i2 + 1;
            if (charAt != '\n') {
                if (charAt != '\r') {
                    this.currentOutput.print(charAt);
                    i = i3;
                    z = false;
                    if (z) {
                        i2 = i;
                    } else {
                        this.currentOutput.println();
                        printTabs();
                        i2 = i;
                        while (i2 <= length && Character.isSpaceChar(str.charAt(i2))) {
                            i2++;
                        }
                    }
                } else if (i3 <= length && str.charAt(i3) == '\n') {
                    i3++;
                }
            }
            i = i3;
            z = true;
            if (z) {
            }
        }
        this.currentOutput.println();
    }

    public void _println(String str) {
        if (str != null) {
            this.currentOutput.println(str);
        }
    }

    public String extractIdOfAction(Token token) {
        return extractIdOfAction(token.getText(), token.getLine(), token.getColumn());
    }

    public String extractIdOfAction(String str, int i, int i2) {
        String removeAssignmentFromDeclaration = removeAssignmentFromDeclaration(str);
        for (int length = removeAssignmentFromDeclaration.length() - 2; length >= 0; length--) {
            if (!Character.isLetterOrDigit(removeAssignmentFromDeclaration.charAt(length)) && removeAssignmentFromDeclaration.charAt(length) != '_') {
                return removeAssignmentFromDeclaration.substring(length + 1);
            }
        }
        this.antlrTool.warning("Ill-formed action", this.grammar.getFilename(), i, i2);
        return "";
    }

    public String extractTypeOfAction(Token token) {
        return extractTypeOfAction(token.getText(), token.getLine(), token.getColumn());
    }

    public String extractTypeOfAction(String str, int i, int i2) {
        String removeAssignmentFromDeclaration = removeAssignmentFromDeclaration(str);
        for (int length = removeAssignmentFromDeclaration.length() - 2; length >= 0; length--) {
            if (!Character.isLetterOrDigit(removeAssignmentFromDeclaration.charAt(length)) && removeAssignmentFromDeclaration.charAt(length) != '_') {
                return removeAssignmentFromDeclaration.substring(0, length + 1);
            }
        }
        this.antlrTool.warning("Ill-formed action", this.grammar.getFilename(), i, i2);
        return "";
    }

    public abstract void gen();

    public abstract void gen(ActionElement actionElement);

    public abstract void gen(AlternativeBlock alternativeBlock);

    public abstract void gen(BlockEndElement blockEndElement);

    public abstract void gen(CharLiteralElement charLiteralElement);

    public abstract void gen(CharRangeElement charRangeElement);

    public abstract void gen(LexerGrammar lexerGrammar);

    public abstract void gen(OneOrMoreBlock oneOrMoreBlock);

    public abstract void gen(ParserGrammar parserGrammar);

    public abstract void gen(RuleRefElement ruleRefElement);

    public abstract void gen(StringLiteralElement stringLiteralElement);

    public abstract void gen(TokenRangeElement tokenRangeElement);

    public abstract void gen(TokenRefElement tokenRefElement);

    public abstract void gen(TreeElement treeElement);

    public abstract void gen(TreeWalkerGrammar treeWalkerGrammar);

    public abstract void gen(WildcardElement wildcardElement);

    public abstract void gen(ZeroOrMoreBlock zeroOrMoreBlock);

    public void genTokenInterchange(TokenManager tokenManager) {
        String str;
        String str2 = tokenManager.getName() + TokenTypesFileSuffix + TokenTypesFileExt;
        this.currentOutput = this.antlrTool.openOutputFile(str2);
        StringBuilder m5a = C0000a.m5a("// $ANTLR ");
        m5a.append(Tool.version);
        m5a.append(": ");
        Tool tool = this.antlrTool;
        m5a.append(tool.fileMinusPath(tool.grammarFile));
        m5a.append(" -> ");
        m5a.append(str2);
        m5a.append("$");
        println(m5a.toString());
        this.tabs = 0;
        println(tokenManager.getName() + "    // output token vocab name");
        Vector vocabulary = tokenManager.getVocabulary();
        for (int i = 4; i < vocabulary.size(); i++) {
            String str3 = (String) vocabulary.elementAt(i);
            if (this.DEBUG_CODE_GENERATOR) {
                System.out.println("gen persistence file entry for: " + str3);
            }
            if (str3 != null && !str3.startsWith("<")) {
                if (str3.startsWith("\"")) {
                    StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) tokenManager.getTokenSymbol(str3);
                    if (stringLiteralSymbol != null && stringLiteralSymbol.label != null) {
                        print(stringLiteralSymbol.label + "=");
                    }
                    str = str3 + "=" + i;
                } else {
                    print(str3);
                    TokenSymbol tokenSymbol = tokenManager.getTokenSymbol(str3);
                    if (tokenSymbol == null) {
                        this.antlrTool.warning("undefined token symbol: " + str3);
                    } else if (tokenSymbol.getParaphrase() != null) {
                        StringBuilder m5a2 = C0000a.m5a("(");
                        m5a2.append(tokenSymbol.getParaphrase());
                        m5a2.append(")");
                        print(m5a2.toString());
                    }
                    str = "=" + i;
                }
                println(str);
            }
        }
        this.currentOutput.close();
        this.currentOutput = null;
    }

    public abstract String getASTCreateString(GrammarAtom grammarAtom, String str);

    public abstract String getASTCreateString(Vector vector);

    public String getBitsetName(int i) {
        return "_tokenSet_" + i;
    }

    public String getFIRSTBitSet(String str, int i) {
        GrammarSymbol symbol = this.grammar.getSymbol(str);
        if (!(symbol instanceof RuleSymbol)) {
            return null;
        }
        return getBitsetName(markBitsetForGen(this.grammar.theLLkAnalyzer.look(i, ((RuleSymbol) symbol).getBlock()).fset));
    }

    public String getFOLLOWBitSet(String str, int i) {
        GrammarSymbol symbol = this.grammar.getSymbol(str);
        if (!(symbol instanceof RuleSymbol)) {
            return null;
        }
        return getBitsetName(markBitsetForGen(this.grammar.theLLkAnalyzer.FOLLOW(i, ((RuleSymbol) symbol).getBlock().endNode).fset));
    }

    public abstract String mapTreeId(String str, ActionTransInfo actionTransInfo);

    public int markBitsetForGen(BitSet bitSet) {
        for (int i = 0; i < this.bitsetsUsed.size(); i++) {
            if (bitSet.equals((BitSet) this.bitsetsUsed.elementAt(i))) {
                return i;
            }
        }
        this.bitsetsUsed.appendElement(bitSet.clone());
        return this.bitsetsUsed.size() - 1;
    }

    public void print(String str) {
        if (str != null) {
            printTabs();
            this.currentOutput.print(str);
        }
    }

    public void printAction(String str) {
        if (str != null) {
            printTabs();
            _printAction(str);
        }
    }

    public void printTabs() {
        for (int i = 1; i <= this.tabs; i++) {
            this.currentOutput.print("\t");
        }
    }

    public void println(String str) {
        if (str != null) {
            printTabs();
            this.currentOutput.println(str);
        }
    }

    public abstract String processActionForSpecialSymbols(String str, int i, RuleBlock ruleBlock, ActionTransInfo actionTransInfo);

    public String processStringForASTConstructor(String str) {
        return str;
    }

    public String removeAssignmentFromDeclaration(String str) {
        return str.indexOf(61) >= 0 ? str.substring(0, str.indexOf(61)).trim() : str;
    }

    public void setAnalyzer(LLkGrammarAnalyzer lLkGrammarAnalyzer) {
        this.analyzer = lLkGrammarAnalyzer;
    }

    public void setBehavior(DefineGrammarSymbols defineGrammarSymbols) {
        this.behavior = defineGrammarSymbols;
    }

    public void setGrammar(Grammar grammar) {
        boolean z;
        reset();
        this.grammar = grammar;
        if (this.grammar.hasOption("codeGenMakeSwitchThreshold")) {
            try {
                this.makeSwitchThreshold = this.grammar.getIntegerOption("codeGenMakeSwitchThreshold");
            } catch (NumberFormatException unused) {
                Token option = this.grammar.getOption("codeGenMakeSwitchThreshold");
                this.antlrTool.error("option 'codeGenMakeSwitchThreshold' must be an integer", this.grammar.getClassName(), option.getLine(), option.getColumn());
            }
        }
        if (this.grammar.hasOption("codeGenBitsetTestThreshold")) {
            try {
                this.bitsetTestThreshold = this.grammar.getIntegerOption("codeGenBitsetTestThreshold");
            } catch (NumberFormatException unused2) {
                Token option2 = this.grammar.getOption("codeGenBitsetTestThreshold");
                this.antlrTool.error("option 'codeGenBitsetTestThreshold' must be an integer", this.grammar.getClassName(), option2.getLine(), option2.getColumn());
            }
        }
        if (this.grammar.hasOption("codeGenDebug")) {
            Token option3 = this.grammar.getOption("codeGenDebug");
            if (option3.getText().equals("true")) {
                z = true;
            } else {
                if (!option3.getText().equals("false")) {
                    this.antlrTool.error("option 'codeGenDebug' must be true or false", this.grammar.getClassName(), option3.getLine(), option3.getColumn());
                    return;
                }
                z = false;
            }
            this.DEBUG_CODE_GENERATOR = z;
        }
    }

    public void setTool(Tool tool) {
        this.antlrTool = tool;
    }
}
