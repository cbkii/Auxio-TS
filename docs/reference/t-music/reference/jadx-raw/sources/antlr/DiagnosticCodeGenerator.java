package antlr;

import antlr.collections.impl.Vector;
import java.io.IOException;
import java.util.Enumeration;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class DiagnosticCodeGenerator extends CodeGenerator {
    public int syntacticPredLevel = 0;
    public boolean doingLexRules = false;

    public DiagnosticCodeGenerator() {
        this.charFormatter = new JavaCharFormatter();
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
                grammar.generate();
                if (this.antlrTool.hasError()) {
                    this.antlrTool.panic("Exiting due to errors.");
                }
            }
            Enumeration elements2 = this.behavior.tokenManagers.elements();
            while (elements2.hasMoreElements()) {
                TokenManager tokenManager = (TokenManager) elements2.nextElement();
                if (!tokenManager.isReadOnly()) {
                    genTokenTypes(tokenManager);
                }
            }
        } catch (IOException e) {
            this.antlrTool.reportException(e, null);
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(ActionElement actionElement) {
        if (actionElement.isSemPred) {
            return;
        }
        print("ACTION: ");
        _printAction(actionElement.actionText);
    }

    @Override // antlr.CodeGenerator
    public void gen(AlternativeBlock alternativeBlock) {
        println("Start of alternative block.");
        this.tabs++;
        genBlockPreamble(alternativeBlock);
        if (!this.grammar.theLLkAnalyzer.deterministic(alternativeBlock)) {
            println("Warning: This alternative block is non-deterministic");
        }
        genCommonBlock(alternativeBlock);
        this.tabs--;
    }

    @Override // antlr.CodeGenerator
    public void gen(BlockEndElement blockEndElement) {
    }

    @Override // antlr.CodeGenerator
    public void gen(CharLiteralElement charLiteralElement) {
        print("Match character ");
        if (charLiteralElement.not) {
            _print("NOT ");
        }
        _print(charLiteralElement.atomText);
        if (charLiteralElement.label != null) {
            StringBuilder m5a = C0000a.m5a(", label=");
            m5a.append(charLiteralElement.label);
            _print(m5a.toString());
        }
        _println("");
    }

    @Override // antlr.CodeGenerator
    public void gen(CharRangeElement charRangeElement) {
        StringBuilder m5a = C0000a.m5a("Match character range: ");
        m5a.append(charRangeElement.beginText);
        m5a.append("..");
        m5a.append(charRangeElement.endText);
        print(m5a.toString());
        if (charRangeElement.label != null) {
            StringBuilder m5a2 = C0000a.m5a(", label = ");
            m5a2.append(charRangeElement.label);
            _print(m5a2.toString());
        }
        _println("");
    }

    @Override // antlr.CodeGenerator
    public void gen(LexerGrammar lexerGrammar) {
        setGrammar(lexerGrammar);
        Tool tool = this.antlrTool;
        StringBuilder m5a = C0000a.m5a("Generating ");
        m5a.append(this.grammar.getClassName());
        m5a.append(CodeGenerator.TokenTypesFileExt);
        tool.reportProgress(m5a.toString());
        this.currentOutput = this.antlrTool.openOutputFile(this.grammar.getClassName() + CodeGenerator.TokenTypesFileExt);
        this.tabs = 0;
        this.doingLexRules = true;
        genHeader();
        println("");
        println("*** Lexer Preamble Action.");
        println("This action will appear before the declaration of your lexer class:");
        this.tabs++;
        println(this.grammar.preambleAction.getText());
        this.tabs--;
        println("*** End of Lexer Preamble Action");
        println("");
        StringBuilder sb = new StringBuilder();
        sb.append("*** Your lexer class is called '");
        C0000a.m4a(this.grammar, sb, "' and is a subclass of '");
        sb.append(this.grammar.getSuperClass());
        sb.append("'.");
        println(sb.toString());
        println("");
        println("*** User-defined lexer  class members:");
        println("These are the member declarations that you defined for your class:");
        this.tabs++;
        printAction(this.grammar.classMemberAction.getText());
        this.tabs--;
        println("*** End of user-defined lexer class members");
        println("");
        println("*** String literals used in the parser");
        println("The following string literals were used in the parser.");
        println("An actual code generator would arrange to place these literals");
        println("into a table in the generated lexer, so that actions in the");
        println("generated lexer could match token text against the literals.");
        println("String literals used in the lexer are not listed here, as they");
        println("are incorporated into the mainstream lexer processing.");
        this.tabs++;
        Enumeration symbols = this.grammar.getSymbols();
        while (symbols.hasMoreElements()) {
            GrammarSymbol grammarSymbol = (GrammarSymbol) symbols.nextElement();
            if (grammarSymbol instanceof StringLiteralSymbol) {
                StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) grammarSymbol;
                println(stringLiteralSymbol.getId() + " = " + stringLiteralSymbol.getTokenType());
            }
        }
        this.tabs--;
        println("*** End of string literals used by the parser");
        genNextToken();
        println("");
        println("*** User-defined Lexer rules:");
        this.tabs++;
        Enumeration elements = this.grammar.rules.elements();
        while (elements.hasMoreElements()) {
            RuleSymbol ruleSymbol = (RuleSymbol) elements.nextElement();
            if (!ruleSymbol.f304id.equals("mnextToken")) {
                genRule(ruleSymbol);
            }
        }
        this.tabs--;
        println("");
        println("*** End User-defined Lexer rules:");
        this.currentOutput.close();
        this.currentOutput = null;
        this.doingLexRules = false;
    }

    @Override // antlr.CodeGenerator
    public void gen(OneOrMoreBlock oneOrMoreBlock) {
        println("Start ONE-OR-MORE (...)+ block:");
        this.tabs++;
        genBlockPreamble(oneOrMoreBlock);
        if (!this.grammar.theLLkAnalyzer.deterministic(oneOrMoreBlock)) {
            println("Warning: This one-or-more block is non-deterministic");
        }
        genCommonBlock(oneOrMoreBlock);
        this.tabs--;
        println("End ONE-OR-MORE block.");
    }

    @Override // antlr.CodeGenerator
    public void gen(ParserGrammar parserGrammar) {
        setGrammar(parserGrammar);
        Tool tool = this.antlrTool;
        StringBuilder m5a = C0000a.m5a("Generating ");
        m5a.append(this.grammar.getClassName());
        m5a.append(CodeGenerator.TokenTypesFileExt);
        tool.reportProgress(m5a.toString());
        this.currentOutput = this.antlrTool.openOutputFile(this.grammar.getClassName() + CodeGenerator.TokenTypesFileExt);
        this.tabs = 0;
        genHeader();
        println("");
        println("*** Parser Preamble Action.");
        println("This action will appear before the declaration of your parser class:");
        this.tabs++;
        println(this.grammar.preambleAction.getText());
        this.tabs--;
        println("*** End of Parser Preamble Action");
        println("");
        StringBuilder sb = new StringBuilder();
        sb.append("*** Your parser class is called '");
        C0000a.m4a(this.grammar, sb, "' and is a subclass of '");
        sb.append(this.grammar.getSuperClass());
        sb.append("'.");
        println(sb.toString());
        println("");
        println("*** User-defined parser class members:");
        println("These are the member declarations that you defined for your class:");
        this.tabs++;
        printAction(this.grammar.classMemberAction.getText());
        this.tabs--;
        println("*** End of user-defined parser class members");
        println("");
        println("*** Parser rules:");
        this.tabs++;
        Enumeration elements = this.grammar.rules.elements();
        while (elements.hasMoreElements()) {
            println("");
            GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                genRule((RuleSymbol) grammarSymbol);
            }
        }
        this.tabs--;
        println("");
        println("*** End of parser rules");
        println("");
        println("*** End of parser");
        this.currentOutput.close();
        this.currentOutput = null;
    }

    @Override // antlr.CodeGenerator
    public void gen(RuleRefElement ruleRefElement) {
        StringBuilder m5a;
        String str;
        RuleSymbol ruleSymbol = (RuleSymbol) this.grammar.getSymbol(ruleRefElement.targetRule);
        StringBuilder m5a2 = C0000a.m5a("Rule Reference: ");
        m5a2.append(ruleRefElement.targetRule);
        print(m5a2.toString());
        if (ruleRefElement.idAssign != null) {
            StringBuilder m5a3 = C0000a.m5a(", assigned to '");
            m5a3.append(ruleRefElement.idAssign);
            m5a3.append("'");
            _print(m5a3.toString());
        }
        if (ruleRefElement.args != null) {
            StringBuilder m5a4 = C0000a.m5a(", arguments = ");
            m5a4.append(ruleRefElement.args);
            _print(m5a4.toString());
        }
        _println("");
        if (ruleSymbol == null || !ruleSymbol.isDefined()) {
            StringBuilder m5a5 = C0000a.m5a("Rule '");
            m5a5.append(ruleRefElement.targetRule);
            m5a5.append("' is referenced, but that rule is not defined.");
            println(m5a5.toString());
            println("\tPerhaps the rule is misspelled, or you forgot to define it.");
            return;
        }
        if (ruleRefElement.idAssign == null) {
            if (!(this.grammar instanceof LexerGrammar) && this.syntacticPredLevel == 0 && ruleSymbol.block.returnAction != null) {
                m5a = C0000a.m5a("Warning: Rule '");
                m5a.append(ruleRefElement.targetRule);
                str = "' returns a value";
                m5a.append(str);
                println(m5a.toString());
            }
            if (ruleRefElement.args == null) {
            } else {
                return;
            }
        }
        if (ruleSymbol.block.returnAction == null) {
            m5a = C0000a.m5a("Error: You assigned from Rule '");
            m5a.append(ruleRefElement.targetRule);
            str = "', but that rule has no return type.";
            m5a.append(str);
            println(m5a.toString());
        }
        if (ruleRefElement.args == null && ruleSymbol.block.argAction == null) {
            StringBuilder m5a6 = C0000a.m5a("Error: Rule '");
            m5a6.append(ruleRefElement.targetRule);
            m5a6.append("' accepts no arguments.");
            println(m5a6.toString());
        }
    }

    @Override // antlr.CodeGenerator
    public void gen(StringLiteralElement stringLiteralElement) {
        print("Match string literal ");
        _print(stringLiteralElement.atomText);
        if (stringLiteralElement.label != null) {
            StringBuilder m5a = C0000a.m5a(", label=");
            m5a.append(stringLiteralElement.label);
            _print(m5a.toString());
        }
        _println("");
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRangeElement tokenRangeElement) {
        StringBuilder m5a = C0000a.m5a("Match token range: ");
        m5a.append(tokenRangeElement.beginText);
        m5a.append("..");
        m5a.append(tokenRangeElement.endText);
        print(m5a.toString());
        if (tokenRangeElement.label != null) {
            StringBuilder m5a2 = C0000a.m5a(", label = ");
            m5a2.append(tokenRangeElement.label);
            _print(m5a2.toString());
        }
        _println("");
    }

    @Override // antlr.CodeGenerator
    public void gen(TokenRefElement tokenRefElement) {
        print("Match token ");
        if (tokenRefElement.not) {
            _print("NOT ");
        }
        _print(tokenRefElement.atomText);
        if (tokenRefElement.label != null) {
            StringBuilder m5a = C0000a.m5a(", label=");
            m5a.append(tokenRefElement.label);
            _print(m5a.toString());
        }
        _println("");
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeElement treeElement) {
        print("Tree reference: " + treeElement);
    }

    @Override // antlr.CodeGenerator
    public void gen(TreeWalkerGrammar treeWalkerGrammar) {
        setGrammar(treeWalkerGrammar);
        Tool tool = this.antlrTool;
        StringBuilder m5a = C0000a.m5a("Generating ");
        m5a.append(this.grammar.getClassName());
        m5a.append(CodeGenerator.TokenTypesFileExt);
        tool.reportProgress(m5a.toString());
        this.currentOutput = this.antlrTool.openOutputFile(this.grammar.getClassName() + CodeGenerator.TokenTypesFileExt);
        this.tabs = 0;
        genHeader();
        println("");
        println("*** Tree-walker Preamble Action.");
        println("This action will appear before the declaration of your tree-walker class:");
        this.tabs++;
        println(this.grammar.preambleAction.getText());
        this.tabs--;
        println("*** End of tree-walker Preamble Action");
        println("");
        StringBuilder sb = new StringBuilder();
        sb.append("*** Your tree-walker class is called '");
        C0000a.m4a(this.grammar, sb, "' and is a subclass of '");
        sb.append(this.grammar.getSuperClass());
        sb.append("'.");
        println(sb.toString());
        println("");
        println("*** User-defined tree-walker class members:");
        println("These are the member declarations that you defined for your class:");
        this.tabs++;
        printAction(this.grammar.classMemberAction.getText());
        this.tabs--;
        println("*** End of user-defined tree-walker class members");
        println("");
        println("*** tree-walker rules:");
        this.tabs++;
        Enumeration elements = this.grammar.rules.elements();
        while (elements.hasMoreElements()) {
            println("");
            GrammarSymbol grammarSymbol = (GrammarSymbol) elements.nextElement();
            if (grammarSymbol instanceof RuleSymbol) {
                genRule((RuleSymbol) grammarSymbol);
            }
        }
        this.tabs--;
        println("");
        println("*** End of tree-walker rules");
        println("");
        println("*** End of tree-walker");
        this.currentOutput.close();
        this.currentOutput = null;
    }

    @Override // antlr.CodeGenerator
    public void gen(WildcardElement wildcardElement) {
        print("Match wildcard");
        if (wildcardElement.getLabel() != null) {
            StringBuilder m5a = C0000a.m5a(", label = ");
            m5a.append(wildcardElement.getLabel());
            _print(m5a.toString());
        }
        _println("");
    }

    @Override // antlr.CodeGenerator
    public void gen(ZeroOrMoreBlock zeroOrMoreBlock) {
        println("Start ZERO-OR-MORE (...)+ block:");
        this.tabs++;
        genBlockPreamble(zeroOrMoreBlock);
        if (!this.grammar.theLLkAnalyzer.deterministic(zeroOrMoreBlock)) {
            println("Warning: This zero-or-more block is non-deterministic");
        }
        genCommonBlock(zeroOrMoreBlock);
        this.tabs--;
        println("End ZERO-OR-MORE block.");
    }

    public void genAlt(Alternative alternative) {
        for (AlternativeElement alternativeElement = alternative.head; !(alternativeElement instanceof BlockEndElement); alternativeElement = alternativeElement.next) {
            alternativeElement.generate();
        }
        if (alternative.getTreeSpecifier() != null) {
            StringBuilder m5a = C0000a.m5a("AST will be built as: ");
            m5a.append(alternative.getTreeSpecifier().getText());
            println(m5a.toString());
        }
    }

    public void genBlockPreamble(AlternativeBlock alternativeBlock) {
        if (alternativeBlock.initAction != null) {
            StringBuilder m5a = C0000a.m5a("Init action: ");
            m5a.append(alternativeBlock.initAction);
            printAction(m5a.toString());
        }
    }

    public void genCommonBlock(AlternativeBlock alternativeBlock) {
        int i = 0;
        boolean z = alternativeBlock.alternatives.size() == 1;
        println("Start of an alternative block.");
        this.tabs++;
        println("The lookahead set for this block is:");
        this.tabs++;
        genLookaheadSetForBlock(alternativeBlock);
        this.tabs--;
        if (z) {
            println("This block has a single alternative");
            if (alternativeBlock.getAlternativeAt(0).synPred != null) {
                println("Warning: you specified a syntactic predicate for this alternative,");
                println("and it is the only alternative of a block and will be ignored.");
            }
        } else {
            println("This block has multiple alternatives:");
            this.tabs++;
        }
        while (true) {
            if (i >= alternativeBlock.alternatives.size()) {
                break;
            }
            Alternative alternativeAt = alternativeBlock.getAlternativeAt(i);
            AlternativeElement alternativeElement = alternativeAt.head;
            println("");
            print(i != 0 ? "Otherwise, " : "");
            StringBuilder m5a = C0000a.m5a("Alternate(");
            i++;
            m5a.append(i);
            m5a.append(") will be taken IF:");
            _println(m5a.toString());
            println("The lookahead set: ");
            this.tabs++;
            genLookaheadSetForAlt(alternativeAt);
            this.tabs--;
            if (alternativeAt.semPred == null && alternativeAt.synPred == null) {
                println("is matched.");
            } else {
                print("is matched, AND ");
            }
            if (alternativeAt.semPred != null) {
                _println("the semantic predicate:");
                this.tabs++;
                println(alternativeAt.semPred);
                if (alternativeAt.synPred != null) {
                    print("is true, AND ");
                } else {
                    println("is true.");
                }
            }
            if (alternativeAt.synPred != null) {
                _println("the syntactic predicate:");
                this.tabs++;
                genSynPred(alternativeAt.synPred);
                this.tabs--;
                println("is matched.");
            }
            genAlt(alternativeAt);
        }
        println("");
        println("OTHERWISE, a NoViableAlt exception will be thrown");
        println("");
        if (!z) {
            this.tabs--;
            println("End of alternatives");
        }
        this.tabs--;
        println("End of alternative block.");
    }

    public void genFollowSetForRuleBlock(RuleBlock ruleBlock) {
        printSet(this.grammar.maxk, 1, this.grammar.theLLkAnalyzer.FOLLOW(1, ruleBlock.endNode));
    }

    public void genHeader() {
        StringBuilder m5a = C0000a.m5a("ANTLR-generated file resulting from grammar ");
        m5a.append(this.antlrTool.grammarFile);
        println(m5a.toString());
        println("Diagnostic output");
        println("");
        println("Terence Parr, MageLang Institute");
        println("with John Lilley, Empathy Software");
        println("ANTLR Version " + Tool.version + "; 1989-2005");
        println("");
        println("*** Header Action.");
        println("This action will appear at the top of all generated files.");
        this.tabs = this.tabs + 1;
        printAction(this.behavior.getHeaderAction(""));
        this.tabs--;
        println("*** End of Header Action");
        println("");
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
        println("*** Lexer nextToken rule:");
        println("The lexer nextToken rule is synthesized from all of the user-defined");
        println("lexer rules.  It logically consists of one big alternative block with");
        println("each user-defined rule being an alternative.");
        println("");
        Grammar grammar = this.grammar;
        RuleBlock createNextTokenRule = MakeGrammar.createNextTokenRule(grammar, grammar.rules, "nextToken");
        RuleSymbol ruleSymbol = new RuleSymbol("mnextToken");
        ruleSymbol.setDefined();
        ruleSymbol.setBlock(createNextTokenRule);
        ruleSymbol.access = "private";
        this.grammar.define(ruleSymbol);
        if (!this.grammar.theLLkAnalyzer.deterministic(createNextTokenRule)) {
            println("The grammar analyzer has determined that the synthesized");
            println("nextToken rule is non-deterministic (i.e., it has ambiguities)");
            println("This means that there is some overlap of the character");
            println("lookahead for two or more of your lexer rules.");
        }
        genCommonBlock(createNextTokenRule);
        println("*** End of nextToken lexer rule.");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x013e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void genRule(RuleSymbol ruleSymbol) {
        String str;
        ExceptionSpec findExceptionSpec;
        String str2;
        StringBuilder m10b;
        println("");
        String str3 = this.doingLexRules ? "Lexer" : "Parser";
        StringBuilder m10b2 = C0000a.m10b("*** ", str3, " Rule: ");
        m10b2.append(ruleSymbol.getId());
        println(m10b2.toString());
        if (ruleSymbol.isDefined()) {
            this.tabs++;
            if (ruleSymbol.access.length() != 0) {
                StringBuilder m5a = C0000a.m5a("Access: ");
                m5a.append(ruleSymbol.access);
                println(m5a.toString());
            }
            RuleBlock block = ruleSymbol.getBlock();
            if (block.returnAction != null) {
                StringBuilder m5a2 = C0000a.m5a("Return value(s): ");
                m5a2.append(block.returnAction);
                println(m5a2.toString());
                if (this.doingLexRules) {
                    println("Error: you specified return value(s) for a lexical rule.");
                    str = "\tLexical rules have an implicit return type of 'int'.";
                }
                if (block.argAction != null) {
                    StringBuilder m5a3 = C0000a.m5a("Arguments: ");
                    m5a3.append(block.argAction);
                    println(m5a3.toString());
                }
                genBlockPreamble(block);
                if (!this.grammar.theLLkAnalyzer.deterministic(block)) {
                    println("Error: This rule is non-deterministic");
                }
                genCommonBlock(block);
                findExceptionSpec = block.findExceptionSpec("");
                if (findExceptionSpec == null) {
                    println("You specified error-handler(s) for this rule:");
                    this.tabs++;
                    int i = 0;
                    while (i < findExceptionSpec.handlers.size()) {
                        if (i != 0) {
                            println("");
                        }
                        ExceptionHandler exceptionHandler = (ExceptionHandler) findExceptionSpec.handlers.elementAt(i);
                        StringBuilder m5a4 = C0000a.m5a("Error-handler(");
                        i++;
                        m5a4.append(i);
                        m5a4.append(") catches [");
                        m5a4.append(exceptionHandler.exceptionTypeAndName.getText());
                        m5a4.append("] and executes:");
                        println(m5a4.toString());
                        printAction(exceptionHandler.action.getText());
                    }
                    this.tabs--;
                    str2 = "End error-handlers.";
                } else {
                    if (!this.doingLexRules) {
                        println("Default error-handling will be generated, which catches all");
                        str2 = "parser exceptions and consumes tokens until the follow-set is seen.";
                    }
                    if (!this.doingLexRules) {
                        println("The follow set for this rule is:");
                        this.tabs++;
                        genFollowSetForRuleBlock(block);
                        this.tabs--;
                    }
                    this.tabs--;
                    m10b = C0000a.m10b("*** End ", str3, " Rule: ");
                }
                println(str2);
                if (!this.doingLexRules) {
                }
                this.tabs--;
                m10b = C0000a.m10b("*** End ", str3, " Rule: ");
            } else {
                str = this.doingLexRules ? "Return value: lexical rule returns an implicit token type" : "Return value: none";
            }
            println(str);
            if (block.argAction != null) {
            }
            genBlockPreamble(block);
            if (!this.grammar.theLLkAnalyzer.deterministic(block)) {
            }
            genCommonBlock(block);
            findExceptionSpec = block.findExceptionSpec("");
            if (findExceptionSpec == null) {
            }
            println(str2);
            if (!this.doingLexRules) {
            }
            this.tabs--;
            m10b = C0000a.m10b("*** End ", str3, " Rule: ");
        } else {
            println("This rule is undefined.");
            println("This means that the rule was referenced somewhere in the grammar,");
            println("but a definition for the rule was not encountered.");
            println("It is also possible that syntax errors during the parse of");
            println("your grammar file prevented correct processing of the rule.");
            m10b = new StringBuilder();
            m10b.append("*** End ");
            m10b.append(str3);
            m10b.append(" Rule: ");
        }
        m10b.append(ruleSymbol.getId());
        println(m10b.toString());
    }

    public void genSynPred(SynPredBlock synPredBlock) {
        this.syntacticPredLevel++;
        gen(synPredBlock);
        this.syntacticPredLevel--;
    }

    public void genTokenTypes(TokenManager tokenManager) {
        Tool tool = this.antlrTool;
        StringBuilder m5a = C0000a.m5a("Generating ");
        m5a.append(tokenManager.getName());
        m5a.append(CodeGenerator.TokenTypesFileSuffix);
        m5a.append(CodeGenerator.TokenTypesFileExt);
        tool.reportProgress(m5a.toString());
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
        return C0000a.m2a("[", str, "]");
    }

    @Override // antlr.CodeGenerator
    public String getASTCreateString(Vector vector) {
        StringBuilder m5a = C0000a.m5a("***Create an AST from a vector here***");
        m5a.append(System.getProperty("line.separator"));
        return m5a.toString();
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
