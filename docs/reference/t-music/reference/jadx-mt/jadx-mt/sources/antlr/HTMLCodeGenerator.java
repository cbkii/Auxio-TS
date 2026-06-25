package antlr;

import antlr.collections.impl.Vector;
import java.io.IOException;
import java.util.Enumeration;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class HTMLCodeGenerator extends CodeGenerator {
    public boolean firstElementInAlt;
    public int syntacticPredLevel = 0;
    public boolean doingLexRules = false;
    public AlternativeElement prevAltElem = null;

    public HTMLCodeGenerator() {
        this.charFormatter = new JavaCharFormatter();
    }

    public static String HTMLEncode(String str) {
        String str2;
        StringBuffer stringBuffer = new StringBuffer();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '&') {
                str2 = "&amp;";
            } else if (cCharAt == '\"') {
                str2 = "&quot;";
            } else if (cCharAt == '\'') {
                str2 = "&#039;";
            } else if (cCharAt == '<') {
                str2 = "&lt;";
            } else if (cCharAt == '>') {
                str2 = "&gt;";
            } else {
                stringBuffer.append(cCharAt);
            }
            stringBuffer.append(str2);
        }
        return stringBuffer.toString();
    }

    @Override // antlr.CodeGenerator
    public void gen() {
        try {
            Enumeration enumerationElements = this.behavior.grammars.elements();
            while (enumerationElements.hasMoreElements()) {
                Grammar grammar = (Grammar) enumerationElements.nextElement();
                grammar.setCodeGenerator(this);
                grammar.generate();
                if (this.antlrTool.hasError()) {
                    this.antlrTool.fatalError("Exiting due to errors.");
                }
            }
        } catch (IOException e) {
            this.antlrTool.reportException(e, null);
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(ActionElement actionElement) {
    }

    @Override // antlr.CodeGenerator
    public void gen(AlternativeBlock alternativeBlock) {
        genGenericBlock(alternativeBlock, "");
    }

    @Override // antlr.CodeGenerator
    public void gen(BlockEndElement blockEndElement) {
    }

    @Override // antlr.CodeGenerator
    public void gen(CharLiteralElement charLiteralElement) {
        if (charLiteralElement.not) {
            _print("~");
        }
        _print(HTMLEncode(charLiteralElement.atomText) + " ");
    }

    @Override // antlr.CodeGenerator
    public void gen(CharRangeElement charRangeElement) {
        print(charRangeElement.beginText + ".." + charRangeElement.endText + " ");
    }

    @Override // antlr.CodeGenerator
    public void gen(LexerGrammar lexerGrammar) {
        setGrammar(lexerGrammar);
        Tool tool = this.antlrTool;
        StringBuilder sbM5a = C0000a.m5a("Generating ");
        sbM5a.append(this.grammar.getClassName());
        sbM5a.append(".html");
        tool.reportProgress(sbM5a.toString());
        this.currentOutput = this.antlrTool.openOutputFile(this.grammar.getClassName() + ".html");
        this.tabs = 0;
        this.doingLexRules = true;
        genHeader();
        println("");
        String str = this.grammar.comment;
        if (str != null) {
            _println(HTMLEncode(str));
        }
        StringBuilder sbM5a2 = C0000a.m5a("Definition of lexer ");
        C0000a.m4a(this.grammar, sbM5a2, ", which is a subclass of ");
        sbM5a2.append(this.grammar.getSuperClass());
        sbM5a2.append(".");
        println(sbM5a2.toString());
        genNextToken();
        Enumeration enumerationElements = this.grammar.rules.elements();
        while (enumerationElements.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) enumerationElements.nextElement();
            if (!ruleSymbol.f304id.equals("mnextToken")) {
                genRule(ruleSymbol);
            }
        }
        this.currentOutput.close();
        this.currentOutput = null;
        this.doingLexRules = false;
    }

    @Override // antlr.CodeGenerator
    public void gen(OneOrMoreBlock oneOrMoreBlock) {
        genGenericBlock(oneOrMoreBlock, "+");
    }

    @Override // antlr.CodeGenerator
    public void gen(ParserGrammar parserGrammar) {
        setGrammar(parserGrammar);
        Tool tool = this.antlrTool;
        StringBuilder sbM5a = C0000a.m5a("Generating ");
        sbM5a.append(this.grammar.getClassName());
        sbM5a.append(".html");
        tool.reportProgress(sbM5a.toString());
        this.currentOutput = this.antlrTool.openOutputFile(this.grammar.getClassName() + ".html");
        this.tabs = 0;
        genHeader();
        println("");
        String str = this.grammar.comment;
        if (str != null) {
            _println(HTMLEncode(str));
        }
        StringBuilder sbM5a2 = C0000a.m5a("Definition of parser ");
        C0000a.m4a(this.grammar, sbM5a2, ", which is a subclass of ");
        sbM5a2.append(this.grammar.getSuperClass());
        sbM5a2.append(".");
        println(sbM5a2.toString());
        Enumeration enumerationElements = this.grammar.rules.elements();
        while (enumerationElements.hasMoreElements()) {
            println("");
            GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                genRule((RuleSymbol) grammarSymbol);
            }
        }
        this.tabs--;
        println("");
        genTail();
        this.currentOutput.close();
        this.currentOutput = null;
    }

    @Override // antlr.CodeGenerator
    public void gen(RuleRefElement ruleRefElement) {
        StringBuilder sbM5a = C0000a.m5a("<a href=\"");
        C0000a.m4a(this.grammar, sbM5a, ".html#");
        sbM5a.append(ruleRefElement.targetRule);
        sbM5a.append("\">");
        _print(sbM5a.toString());
        _print(ruleRefElement.targetRule);
        _print("</a>");
        _print(" ");
    }

    @Override // antlr.CodeGenerator
    public void gen(StringLiteralElement stringLiteralElement) {
        if (stringLiteralElement.not) {
            _print("~");
        }
        _print(HTMLEncode(stringLiteralElement.atomText));
        _print(" ");
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRangeElement tokenRangeElement) {
        print(tokenRangeElement.beginText + ".." + tokenRangeElement.endText + " ");
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRefElement tokenRefElement) {
        if (tokenRefElement.not) {
            _print("~");
        }
        _print(tokenRefElement.atomText);
        _print(" ");
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeElement treeElement) {
        print(treeElement + " ");
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeWalkerGrammar treeWalkerGrammar) {
        setGrammar(treeWalkerGrammar);
        Tool tool = this.antlrTool;
        StringBuilder sbM5a = C0000a.m5a("Generating ");
        sbM5a.append(this.grammar.getClassName());
        sbM5a.append(".html");
        tool.reportProgress(sbM5a.toString());
        this.currentOutput = this.antlrTool.openOutputFile(this.grammar.getClassName() + ".html");
        this.tabs = 0;
        genHeader();
        println("");
        println("");
        String str = this.grammar.comment;
        if (str != null) {
            _println(HTMLEncode(str));
        }
        StringBuilder sbM5a2 = C0000a.m5a("Definition of tree parser ");
        C0000a.m4a(this.grammar, sbM5a2, ", which is a subclass of ");
        sbM5a2.append(this.grammar.getSuperClass());
        sbM5a2.append(".");
        println(sbM5a2.toString());
        println("");
        this.tabs++;
        Enumeration enumerationElements = this.grammar.rules.elements();
        while (enumerationElements.hasMoreElements()) {
            println("");
            GrammarSymbol grammarSymbol = (GrammarSymbol) enumerationElements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                genRule((RuleSymbol) grammarSymbol);
            }
        }
        this.tabs--;
        println("");
        this.currentOutput.close();
        this.currentOutput = null;
    }

    @Override // antlr.CodeGenerator
    public void gen(WildcardElement wildcardElement) {
        _print(". ");
    }

    @Override // antlr.CodeGenerator
    public void gen(ZeroOrMoreBlock zeroOrMoreBlock) {
        genGenericBlock(zeroOrMoreBlock, "*");
    }

    public void genAlt(Alternative alternative) {
        if (alternative.getTreeSpecifier() != null) {
            _print(alternative.getTreeSpecifier().getText());
        }
        this.prevAltElem = null;
        for (AlternativeElement alternativeElement = alternative.head; !(alternativeElement instanceof BlockEndElement); alternativeElement = alternativeElement.next) {
            alternativeElement.generate();
            this.firstElementInAlt = false;
            this.prevAltElem = alternativeElement;
        }
    }

    public void genCommonBlock(AlternativeBlock alternativeBlock) {
        for (int i = 0; i < alternativeBlock.alternatives.size(); i++) {
            Alternative alternativeAt = alternativeBlock.getAlternativeAt(i);
            AlternativeElement alternativeElement = alternativeAt.head;
            if (i > 0 && alternativeBlock.alternatives.size() > 1) {
                _println("");
                print("|\t");
            }
            boolean z = this.firstElementInAlt;
            this.firstElementInAlt = true;
            this.tabs++;
            genAlt(alternativeAt);
            this.tabs--;
            this.firstElementInAlt = z;
        }
    }

    public void genFollowSetForRuleBlock(RuleBlock ruleBlock) {
        printSet(this.grammar.maxk, 1, this.grammar.theLLkAnalyzer.FOLLOW(1, ruleBlock.endNode));
    }

    public void genGenericBlock(AlternativeBlock alternativeBlock, String str) {
        AlternativeElement alternativeElement;
        if (alternativeBlock.alternatives.size() <= 1) {
            _print("( ");
        } else if (this.firstElementInAlt || !((alternativeElement = this.prevAltElem) == null || !(alternativeElement instanceof AlternativeBlock) || ((AlternativeBlock) alternativeElement).alternatives.size() == 1)) {
            _print("(\t");
        } else {
            _println("");
            print("(\t");
        }
        genCommonBlock(alternativeBlock);
        if (alternativeBlock.alternatives.size() <= 1) {
            _print(")" + str + " ");
            return;
        }
        _println("");
        print(")" + str + " ");
        if (alternativeBlock.next instanceof BlockEndElement) {
            return;
        }
        _println("");
        print("");
    }

    public void genHeader() {
        println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        println("<HTML>");
        println("<HEAD>");
        println("<TITLE>Grammar " + this.antlrTool.grammarFile + "</TITLE>");
        println("</HEAD>");
        println("<BODY>");
        println("<table summary=\"\" border=\"1\" cellpadding=\"5\">");
        println("<tr>");
        println("<td>");
        println("<font size=\"+2\">Grammar " + this.grammar.getClassName() + "</font><br>");
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"http://www.ANTLR.org\">ANTLR</a>-generated HTML file from ");
        sb.append(this.antlrTool.grammarFile);
        println(sb.toString());
        println("<p>");
        println("Terence Parr, <a href=\"http://www.magelang.com\">MageLang Institute</a>");
        println("<br>ANTLR Version " + Tool.version + "; 1989-2005");
        println("</td>");
        println("</tr>");
        println("</table>");
        println("<PRE>");
    }

    public void genLookaheadSetForAlt(Alternative alternative) {
        if (this.doingLexRules && alternative.cache[1].containsEpsilon()) {
            println("MATCHES ALL");
            return;
        }
        int i = alternative.lookaheadDepth;
        if (i == Integer.MAX_VALUE) {
            i = this.grammar.maxk;
        }
        for (int i2 = 1; i2 <= i; i2++) {
            printSet(i, i2, alternative.cache[i2]);
        }
    }

    public void genLookaheadSetForBlock(AlternativeBlock alternativeBlock) {
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i >= alternativeBlock.alternatives.size()) {
                break;
            }
            int i3 = alternativeBlock.getAlternativeAt(i).lookaheadDepth;
            if (i3 == Integer.MAX_VALUE) {
                i2 = this.grammar.maxk;
                break;
            } else {
                if (i2 < i3) {
                    i2 = i3;
                }
                i++;
            }
        }
        for (int i4 = 1; i4 <= i2; i4++) {
            printSet(i2, i4, this.grammar.theLLkAnalyzer.look(i4, alternativeBlock));
        }
    }

    public void genNextToken() {
        println("");
        println("/** Lexer nextToken rule:");
        println(" *  The lexer nextToken rule is synthesized from all of the user-defined");
        println(" *  lexer rules.  It logically consists of one big alternative block with");
        println(" *  each user-defined rule being an alternative.");
        println(" */");
        Grammar grammar = this.grammar;
        RuleBlock ruleBlockCreateNextTokenRule = MakeGrammar.createNextTokenRule(grammar, grammar.rules, "nextToken");
        RuleSymbol ruleSymbol = new RuleSymbol("mnextToken");
        ruleSymbol.setDefined();
        ruleSymbol.setBlock(ruleBlockCreateNextTokenRule);
        ruleSymbol.access = "private";
        this.grammar.define(ruleSymbol);
        genCommonBlock(ruleBlockCreateNextTokenRule);
    }

    public void genRule(RuleSymbol ruleSymbol) {
        if (ruleSymbol == null || !ruleSymbol.isDefined()) {
            return;
        }
        println("");
        String str = ruleSymbol.comment;
        if (str != null) {
            _println(HTMLEncode(str));
        }
        if (ruleSymbol.access.length() != 0 && !ruleSymbol.access.equals("public")) {
            _print(ruleSymbol.access + " ");
        }
        StringBuilder sbM5a = C0000a.m5a("<a name=\"");
        sbM5a.append(ruleSymbol.getId());
        sbM5a.append("\">");
        _print(sbM5a.toString());
        _print(ruleSymbol.getId());
        _print("</a>");
        RuleBlock block = ruleSymbol.getBlock();
        _println("");
        this.tabs++;
        print(":\t");
        genCommonBlock(block);
        _println("");
        println(";");
        this.tabs--;
    }

    public void genSynPred(SynPredBlock synPredBlock) {
        this.syntacticPredLevel++;
        genGenericBlock(synPredBlock, " =>");
        this.syntacticPredLevel--;
    }

    public void genTail() {
        println("</PRE>");
        println("</BODY>");
        println("</HTML>");
    }

    public void genTokenTypes(TokenManager tokenManager) {
        Tool tool = this.antlrTool;
        StringBuilder sbM5a = C0000a.m5a("Generating ");
        sbM5a.append(tokenManager.getName());
        sbM5a.append(CodeGenerator.TokenTypesFileSuffix);
        sbM5a.append(CodeGenerator.TokenTypesFileExt);
        tool.reportProgress(sbM5a.toString());
        this.currentOutput = this.antlrTool.openOutputFile(tokenManager.getName() + CodeGenerator.TokenTypesFileSuffix + CodeGenerator.TokenTypesFileExt);
        this.tabs = 0;
        genHeader();
        println("");
        println("*** Tokens used by the parser");
        println("This is a list of the token numeric values and the corresponding");
        println("token identifiers.  Some tokens are literals, and because of that");
        println("they have no identifiers.  Literals are double-quoted.");
        this.tabs++;
        Vector vocabulary = tokenManager.getVocabulary();
        for (int i = 4; i < vocabulary.size(); i++) {
            String str = (String) vocabulary.elementAt(i);
            if (str != null) {
                println(str + " = " + i);
            }
        }
        this.tabs--;
        println("*** End of tokens used by the parser");
        this.currentOutput.close();
        this.currentOutput = null;
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(GrammarAtom grammarAtom, String str) {
        return null;
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(Vector vector) {
        return null;
    }

    @Override // antlr.CodeGenerator
    public String mapTreeId(String str, ActionTransInfo actionTransInfo) {
        return str;
    }

    public void printSet(int i, int i2, Lookahead lookahead) {
        int[] array = lookahead.fset.toArray();
        print(i != 1 ? "k==" + i2 + ": {" : "{ ");
        if (array.length > 5) {
            _println("");
            this.tabs++;
            print("");
        }
        int i3 = 0;
        for (int i4 = 0; i4 < array.length; i4++) {
            i3++;
            if (i3 > 5) {
                _println("");
                print("");
                i3 = 0;
            }
            _print(this.doingLexRules ? this.charFormatter.literalChar(array[i4]) : (String) this.grammar.tokenManager.getVocabulary().elementAt(array[i4]));
            if (i4 != array.length - 1) {
                _print(", ");
            }
        }
        if (array.length > 5) {
            _println("");
            this.tabs--;
            print("");
        }
        _println(" }");
    }

    @Override // antlr.CodeGenerator
    public String processActionForSpecialSymbols(String str, int i, RuleBlock ruleBlock, ActionTransInfo actionTransInfo) {
        return str;
    }
}
