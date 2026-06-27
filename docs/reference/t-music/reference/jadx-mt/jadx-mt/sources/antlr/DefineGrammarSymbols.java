package antlr;

import antlr.collections.impl.BitSet;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* JADX INFO: loaded from: classes3.dex */
public class DefineGrammarSymbols implements ANTLRGrammarParseBehavior {
    public static final String DEFAULT_TOKENMANAGER_NAME = "*default";
    public LLkAnalyzer analyzer;
    public String[] args;
    public Grammar grammar;
    public Tool tool;
    public Hashtable grammars = new Hashtable();
    public Hashtable tokenManagers = new Hashtable();
    public Hashtable headerActions = new Hashtable();
    public Token thePreambleAction = new CommonToken(0, "");
    public String language = "Java";
    public int numLexers = 0;
    public int numParsers = 0;
    public int numTreeParsers = 0;

    public DefineGrammarSymbols(Tool tool, String[] strArr, LLkAnalyzer lLkAnalyzer) {
        this.tool = tool;
        this.args = strArr;
        this.analyzer = lLkAnalyzer;
    }

    public void _refStringLiteral(Token token, Token token2, int i, boolean z) {
        if (this.grammar instanceof LexerGrammar) {
            return;
        }
        String text = token.getText();
        if (this.grammar.tokenManager.getTokenSymbol(text) != null) {
            return;
        }
        StringLiteralSymbol stringLiteralSymbol = new StringLiteralSymbol(text);
        stringLiteralSymbol.setTokenType(this.grammar.tokenManager.nextTokenType());
        this.grammar.tokenManager.define(stringLiteralSymbol);
    }

    public void _refToken(Token token, Token token2, Token token3, Token token4, boolean z, int i, boolean z2) {
        String text = token2.getText();
        if (this.grammar.tokenManager.tokenDefined(text)) {
            return;
        }
        int iNextTokenType = this.grammar.tokenManager.nextTokenType();
        TokenSymbol tokenSymbol = new TokenSymbol(text);
        tokenSymbol.setTokenType(iNextTokenType);
        this.grammar.tokenManager.define(tokenSymbol);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void abortGrammar() {
        Grammar grammar = this.grammar;
        if (grammar != null && grammar.getClassName() != null) {
            this.grammars.remove(this.grammar.getClassName());
        }
        this.grammar = null;
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void beginAlt(boolean z) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void beginChildList() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void beginExceptionGroup() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void beginExceptionSpec(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void beginSubRule(Token token, Token token2, boolean z) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void beginTree(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void defineRuleName(Token token, String str, boolean z, String str2) {
        RuleSymbol ruleSymbol;
        String text = token.getText();
        if (token.type == 24) {
            text = CodeGenerator.encodeLexerRuleName(text);
            if (!this.grammar.tokenManager.tokenDefined(token.getText())) {
                int iNextTokenType = this.grammar.tokenManager.nextTokenType();
                TokenSymbol tokenSymbol = new TokenSymbol(token.getText());
                tokenSymbol.setTokenType(iNextTokenType);
                this.grammar.tokenManager.define(tokenSymbol);
            }
        }
        if (this.grammar.isDefined(text)) {
            ruleSymbol = (RuleSymbol) this.grammar.getSymbol(text);
            if (ruleSymbol.isDefined()) {
                this.tool.error(C0000a.m1a("redefinition of rule ", text), this.grammar.getFilename(), token.getLine(), token.getColumn());
            }
        } else {
            ruleSymbol = new RuleSymbol(text);
            this.grammar.define(ruleSymbol);
        }
        ruleSymbol.setDefined();
        ruleSymbol.access = str;
        ruleSymbol.comment = str2;
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void defineToken(Token token, Token token2) {
        TokenSymbol tokenSymbol;
        String text = token != null ? token.getText() : null;
        String text2 = token2 != null ? token2.getText() : null;
        if (text2 == null) {
            if (this.grammar.tokenManager.tokenDefined(text)) {
                this.tool.warning(C0000a.m1a("Redefinition of token in tokens {...}: ", text), this.grammar.getFilename(), token.getLine(), token.getColumn());
                return;
            }
            int iNextTokenType = this.grammar.tokenManager.nextTokenType();
            TokenSymbol tokenSymbol2 = new TokenSymbol(text);
            tokenSymbol2.setTokenType(iNextTokenType);
            this.grammar.tokenManager.define(tokenSymbol2);
            return;
        }
        StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) this.grammar.tokenManager.getTokenSymbol(text2);
        if (stringLiteralSymbol != null) {
            if (text == null || stringLiteralSymbol.getLabel() != null) {
                this.tool.warning(C0000a.m1a("Redefinition of literal in tokens {...}: ", text2), this.grammar.getFilename(), token2.getLine(), token2.getColumn());
                return;
            } else {
                stringLiteralSymbol.setLabel(text);
                this.grammar.tokenManager.mapToTokenSymbol(text, stringLiteralSymbol);
            }
        }
        if (text == null || (tokenSymbol = this.grammar.tokenManager.getTokenSymbol(text)) == null) {
            StringLiteralSymbol stringLiteralSymbol2 = new StringLiteralSymbol(text2);
            stringLiteralSymbol2.setTokenType(this.grammar.tokenManager.nextTokenType());
            stringLiteralSymbol2.setLabel(text);
            this.grammar.tokenManager.define(stringLiteralSymbol2);
            if (text != null) {
                this.grammar.tokenManager.mapToTokenSymbol(text, stringLiteralSymbol2);
                return;
            }
            return;
        }
        if (tokenSymbol instanceof StringLiteralSymbol) {
            this.tool.warning(C0000a.m1a("Redefinition of token in tokens {...}: ", text), this.grammar.getFilename(), token2.getLine(), token2.getColumn());
            return;
        }
        int tokenType = tokenSymbol.getTokenType();
        StringLiteralSymbol stringLiteralSymbol3 = new StringLiteralSymbol(text2);
        stringLiteralSymbol3.setTokenType(tokenType);
        stringLiteralSymbol3.setLabel(text);
        this.grammar.tokenManager.define(stringLiteralSymbol3);
        this.grammar.tokenManager.mapToTokenSymbol(text, stringLiteralSymbol3);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endAlt() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endChildList() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endExceptionGroup() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endExceptionSpec() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endGrammar() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endOptions() {
        String str;
        Grammar grammar = this.grammar;
        if (grammar.exportVocab == null && grammar.importVocab == null) {
            grammar.exportVocab = grammar.getClassName();
            if (this.tokenManagers.containsKey(DEFAULT_TOKENMANAGER_NAME)) {
                this.grammar.exportVocab = DEFAULT_TOKENMANAGER_NAME;
                this.grammar.setTokenManager((TokenManager) this.tokenManagers.get(DEFAULT_TOKENMANAGER_NAME));
                return;
            } else {
                SimpleTokenManager simpleTokenManager = new SimpleTokenManager(this.grammar.exportVocab, this.tool);
                this.grammar.setTokenManager(simpleTokenManager);
                this.tokenManagers.put(this.grammar.exportVocab, simpleTokenManager);
                this.tokenManagers.put(DEFAULT_TOKENMANAGER_NAME, simpleTokenManager);
                return;
            }
        }
        Grammar grammar2 = this.grammar;
        if (grammar2.exportVocab == null && grammar2.importVocab != null) {
            grammar2.exportVocab = grammar2.getClassName();
            Grammar grammar3 = this.grammar;
            if (grammar3.importVocab.equals(grammar3.exportVocab)) {
                Tool tool = this.tool;
                StringBuilder sbM5a = C0000a.m5a("Grammar ");
                sbM5a.append(this.grammar.getClassName());
                sbM5a.append(" cannot have importVocab same as default output vocab (grammar name); ignored.");
                tool.warning(sbM5a.toString());
                this.grammar.importVocab = null;
                endOptions();
                return;
            }
            if (this.tokenManagers.containsKey(this.grammar.importVocab)) {
                TokenManager tokenManager = (TokenManager) ((TokenManager) this.tokenManagers.get(this.grammar.importVocab)).clone();
                tokenManager.setName(this.grammar.exportVocab);
                tokenManager.setReadOnly(false);
                this.grammar.setTokenManager(tokenManager);
                this.tokenManagers.put(this.grammar.exportVocab, tokenManager);
                return;
            }
            ImportVocabTokenManager importVocabTokenManager = new ImportVocabTokenManager(this.grammar, this.grammar.importVocab + CodeGenerator.TokenTypesFileSuffix + CodeGenerator.TokenTypesFileExt, this.grammar.exportVocab, this.tool);
            importVocabTokenManager.setReadOnly(false);
            this.tokenManagers.put(this.grammar.exportVocab, importVocabTokenManager);
            this.grammar.setTokenManager(importVocabTokenManager);
            if (this.tokenManagers.containsKey(DEFAULT_TOKENMANAGER_NAME)) {
                return;
            }
            this.tokenManagers.put(DEFAULT_TOKENMANAGER_NAME, importVocabTokenManager);
            return;
        }
        Grammar grammar4 = this.grammar;
        String str2 = grammar4.exportVocab;
        if (str2 != null && grammar4.importVocab == null) {
            if (this.tokenManagers.containsKey(str2)) {
                this.grammar.setTokenManager((TokenManager) this.tokenManagers.get(this.grammar.exportVocab));
                return;
            }
            SimpleTokenManager simpleTokenManager2 = new SimpleTokenManager(this.grammar.exportVocab, this.tool);
            this.grammar.setTokenManager(simpleTokenManager2);
            this.tokenManagers.put(this.grammar.exportVocab, simpleTokenManager2);
            if (this.tokenManagers.containsKey(DEFAULT_TOKENMANAGER_NAME)) {
                return;
            }
            this.tokenManagers.put(DEFAULT_TOKENMANAGER_NAME, simpleTokenManager2);
            return;
        }
        Grammar grammar5 = this.grammar;
        String str3 = grammar5.exportVocab;
        if (str3 == null || (str = grammar5.importVocab) == null) {
            return;
        }
        if (str.equals(str3)) {
            Tool tool2 = this.tool;
            StringBuilder sbM5a2 = C0000a.m5a("exportVocab of ");
            sbM5a2.append(this.grammar.exportVocab);
            sbM5a2.append(" same as importVocab; probably not what you want");
            tool2.error(sbM5a2.toString());
        }
        if (this.tokenManagers.containsKey(this.grammar.importVocab)) {
            TokenManager tokenManager2 = (TokenManager) ((TokenManager) this.tokenManagers.get(this.grammar.importVocab)).clone();
            tokenManager2.setName(this.grammar.exportVocab);
            tokenManager2.setReadOnly(false);
            this.grammar.setTokenManager(tokenManager2);
            this.tokenManagers.put(this.grammar.exportVocab, tokenManager2);
            return;
        }
        ImportVocabTokenManager importVocabTokenManager2 = new ImportVocabTokenManager(this.grammar, this.grammar.importVocab + CodeGenerator.TokenTypesFileSuffix + CodeGenerator.TokenTypesFileExt, this.grammar.exportVocab, this.tool);
        importVocabTokenManager2.setReadOnly(false);
        this.tokenManagers.put(this.grammar.exportVocab, importVocabTokenManager2);
        this.grammar.setTokenManager(importVocabTokenManager2);
        if (this.tokenManagers.containsKey(DEFAULT_TOKENMANAGER_NAME)) {
            return;
        }
        this.tokenManagers.put(DEFAULT_TOKENMANAGER_NAME, importVocabTokenManager2);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endRule(String str) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endSubRule() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void endTree() {
    }

    public String getHeaderAction(String str) {
        Token token = (Token) this.headerActions.get(str);
        return token == null ? "" : token.getText();
    }

    public int getHeaderActionLine(String str) {
        Token token = (Token) this.headerActions.get(str);
        if (token == null) {
            return 0;
        }
        return token.getLine();
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void hasError() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void noASTSubRule() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void oneOrMoreSubRule() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void optionalSubRule() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refAction(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refArgAction(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refCharLiteral(Token token, Token token2, boolean z, int i, boolean z2) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refCharRange(Token token, Token token2, Token token3, int i, boolean z) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refElementOption(Token token, Token token2) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refExceptionHandler(Token token, Token token2) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refHeaderAction(Token token, Token token2) {
        Tool tool;
        StringBuilder sb;
        String str;
        String strStripFrontBack = token == null ? "" : StringUtils.stripFrontBack(token.getText(), "\"", "\"");
        if (this.headerActions.containsKey(strStripFrontBack)) {
            if (strStripFrontBack.equals("")) {
                tool = this.tool;
                sb = new StringBuilder();
                sb.append(token2.getLine());
                str = ": header action already defined";
            } else {
                tool = this.tool;
                sb = new StringBuilder();
                sb.append(token2.getLine());
                sb.append(": header action '");
                sb.append(strStripFrontBack);
                str = "' already defined";
            }
            sb.append(str);
            tool.error(sb.toString());
        }
        this.headerActions.put(strStripFrontBack, token2);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refInitAction(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refMemberAction(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refPreambleAction(Token token) {
        this.thePreambleAction = token;
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refReturnAction(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refRule(Token token, Token token2, Token token3, Token token4, int i) {
        String text = token2.getText();
        if (token2.type == 24) {
            text = CodeGenerator.encodeLexerRuleName(text);
        }
        if (this.grammar.isDefined(text)) {
            return;
        }
        this.grammar.define(new RuleSymbol(text));
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refSemPred(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refStringLiteral(Token token, Token token2, int i, boolean z) {
        _refStringLiteral(token, token2, i, z);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refToken(Token token, Token token2, Token token3, Token token4, boolean z, int i, boolean z2) {
        _refToken(token, token2, token3, token4, z, i, z2);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refTokenRange(Token token, Token token2, Token token3, int i, boolean z) {
        if (token.getText().charAt(0) == '\"') {
            refStringLiteral(token, null, 1, z);
        } else {
            _refToken(null, token, null, null, false, 1, z);
        }
        if (token2.getText().charAt(0) == '\"') {
            _refStringLiteral(token2, null, 1, z);
        } else {
            _refToken(null, token2, null, null, false, 1, z);
        }
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refTokensSpecElementOption(Token token, Token token2, Token token3) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refTreeSpecifier(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void refWildcard(Token token, Token token2, int i) {
    }

    public void reset() {
        this.grammar = null;
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void setArgOfRuleRef(Token token) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void setCharVocabulary(BitSet bitSet) {
        ((LexerGrammar) this.grammar).setCharVocabulary(bitSet);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void setFileOption(Token token, Token token2, String str) {
        Tool tool;
        StringBuilder sb;
        Tool tool2;
        StringBuilder sb2;
        String str2;
        Tool tool3;
        String strM0a;
        int line;
        int column;
        Tool tool4;
        boolean z;
        String text;
        if (token.getText().equals(IjkMediaMeta.IJKM_KEY_LANGUAGE)) {
            if (token2.getType() == 6) {
                text = StringUtils.stripBack(StringUtils.stripFront(token2.getText(), '\"'), '\"');
            } else if (token2.getType() == 24 || token2.getType() == 41) {
                text = token2.getText();
            } else {
                tool3 = this.tool;
                line = token2.getLine();
                column = token2.getColumn();
                strM0a = "language option must be string or identifier";
            }
            this.language = text;
            return;
        }
        if (token.getText().equals("mangleLiteralPrefix")) {
            int type = token2.getType();
            tool3 = this.tool;
            if (type == 6) {
                tool3.literalsPrefix = StringUtils.stripFrontBack(token2.getText(), "\"", "\"");
                return;
            } else {
                line = token2.getLine();
                column = token2.getColumn();
                strM0a = "mangleLiteralPrefix option must be string";
            }
        } else {
            if (token.getText().equals("upperCaseMangledLiterals")) {
                if (token2.getText().equals("true")) {
                    tool4 = this.tool;
                    z = true;
                } else if (!token2.getText().equals("false")) {
                    this.grammar.antlrTool.error("Value for upperCaseMangledLiterals must be true or false", str, token.getLine(), token.getColumn());
                    return;
                } else {
                    tool4 = this.tool;
                    z = false;
                }
                tool4.upperCaseMangledLiterals = z;
                return;
            }
            if (token.getText().equals("namespaceStd") || token.getText().equals("namespaceAntlr") || token.getText().equals("genHashLines")) {
                if (!this.language.equals("Cpp")) {
                    tool2 = this.tool;
                    sb2 = new StringBuilder();
                    sb2.append(token.getText());
                    str2 = " option only valid for C++";
                    sb2.append(str2);
                    tool2.error(sb2.toString(), str, token.getLine(), token.getColumn());
                    return;
                }
                if (token.getText().equals("noConstructors")) {
                    if (!token2.getText().equals("true") && !token2.getText().equals("false")) {
                        this.tool.error("noConstructors option must be true or false", str, token2.getLine(), token2.getColumn());
                    }
                    this.tool.noConstructors = token2.getText().equals("true");
                    return;
                }
                if (token.getText().equals("genHashLines")) {
                    if (!token2.getText().equals("true") && !token2.getText().equals("false")) {
                        this.tool.error("genHashLines option must be true or false", str, token2.getLine(), token2.getColumn());
                    }
                    this.tool.genHashLines = token2.getText().equals("true");
                    return;
                }
                if (token2.getType() != 6) {
                    tool = this.tool;
                    sb = new StringBuilder();
                    sb.append(token.getText());
                    sb.append(" option must be a string");
                    tool.error(sb.toString(), str, token2.getLine(), token2.getColumn());
                    return;
                }
                if (token.getText().equals("namespaceStd")) {
                    this.tool.namespaceStd = token2.getText();
                    return;
                } else {
                    if (token.getText().equals("namespaceAntlr")) {
                        this.tool.namespaceAntlr = token2.getText();
                        return;
                    }
                    return;
                }
            }
            if (token.getText().equals("namespace")) {
                if (!this.language.equals("Cpp") && !this.language.equals("CSharp")) {
                    tool2 = this.tool;
                    sb2 = new StringBuilder();
                    sb2.append(token.getText());
                    str2 = " option only valid for C++ and C# (a.k.a CSharp)";
                    sb2.append(str2);
                    tool2.error(sb2.toString(), str, token.getLine(), token.getColumn());
                    return;
                }
                if (token2.getType() == 6) {
                    if (token.getText().equals("namespace")) {
                        this.tool.setNameSpace(token2.getText());
                        return;
                    }
                    return;
                } else {
                    tool = this.tool;
                    sb = new StringBuilder();
                    sb.append(token.getText());
                    sb.append(" option must be a string");
                    tool.error(sb.toString(), str, token2.getLine(), token2.getColumn());
                    return;
                }
            }
            tool3 = this.tool;
            strM0a = C0000a.m0a(token, C0000a.m5a("Invalid file-level option: "));
            line = token.getLine();
            column = token2.getColumn();
        }
        tool3.error(strM0a, str, line, column);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void setGrammarOption(Token token, Token token2) {
        Tool tool;
        String filename;
        int line;
        int column;
        String str;
        if (token.getText().equals("tokdef") || token.getText().equals("tokenVocabulary")) {
            tool = this.tool;
            filename = this.grammar.getFilename();
            line = token2.getLine();
            column = token2.getColumn();
            str = "tokdef/tokenVocabulary options are invalid >= ANTLR 2.6.0.\n  Use importVocab/exportVocab instead.  Please see the documentation.\n  The previous options were so heinous that Terence changed the whole\n  vocabulary mechanism; it was better to change the names rather than\n  subtly change the functionality of the known options.  Sorry!";
        } else {
            if (token.getText().equals("literal")) {
                Grammar grammar = this.grammar;
                if (grammar instanceof LexerGrammar) {
                    this.tool.error("the literal option is invalid >= ANTLR 2.6.0.\n  Use the \"tokens {...}\" mechanism instead.", grammar.getFilename(), token2.getLine(), token2.getColumn());
                    return;
                }
            }
            if (token.getText().equals("exportVocab")) {
                if (token2.getType() == 41 || token2.getType() == 24) {
                    this.grammar.exportVocab = token2.getText();
                    return;
                } else {
                    tool = this.tool;
                    filename = this.grammar.getFilename();
                    line = token2.getLine();
                    column = token2.getColumn();
                    str = "exportVocab must be an identifier";
                }
            } else if (token.getText().equals("importVocab")) {
                if (token2.getType() == 41 || token2.getType() == 24) {
                    this.grammar.importVocab = token2.getText();
                    return;
                } else {
                    tool = this.tool;
                    filename = this.grammar.getFilename();
                    line = token2.getLine();
                    column = token2.getColumn();
                    str = "importVocab must be an identifier";
                }
            } else {
                if (!token.getText().equals("k") || !(this.grammar instanceof TreeWalkerGrammar) || token2.getText().equals("1")) {
                    this.grammar.setOption(token.getText(), token2);
                    return;
                }
                tool = this.tool;
                filename = this.grammar.getFilename();
                line = token2.getLine();
                column = token2.getColumn();
                str = "Treewalkers only support k=1";
            }
        }
        tool.error(str, filename, line, column);
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void setRuleOption(Token token, Token token2) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void setSubruleOption(Token token, Token token2) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void setUserExceptions(String str) {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void startLexer(String str, Token token, String str2, String str3) {
        StringBuilder sbM5a;
        String str4;
        if (this.numLexers > 0) {
            Tool tool = this.tool;
            StringBuilder sbM5a2 = C0000a.m5a("You may only have one lexer per grammar file: class ");
            sbM5a2.append(token.getText());
            tool.panic(sbM5a2.toString());
        }
        this.numLexers++;
        reset();
        Grammar grammar = (Grammar) this.grammars.get(token);
        if (grammar == null) {
            LexerGrammar lexerGrammar = new LexerGrammar(token.getText(), this.tool, str2);
            lexerGrammar.comment = str3;
            lexerGrammar.processArguments(this.args);
            lexerGrammar.setFilename(str);
            this.grammars.put(lexerGrammar.getClassName(), lexerGrammar);
            lexerGrammar.preambleAction = this.thePreambleAction;
            this.thePreambleAction = new CommonToken(0, "");
            this.grammar = lexerGrammar;
            return;
        }
        boolean z = grammar instanceof LexerGrammar;
        Tool tool2 = this.tool;
        if (z) {
            sbM5a = C0000a.m5a("Lexer '");
            sbM5a.append(token.getText());
            str4 = "' is already defined";
        } else {
            sbM5a = C0000a.m5a("'");
            sbM5a.append(token.getText());
            str4 = "' is already defined as a non-lexer";
        }
        sbM5a.append(str4);
        tool2.panic(sbM5a.toString());
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void startParser(String str, Token token, String str2, String str3) {
        StringBuilder sbM5a;
        String str4;
        if (this.numParsers > 0) {
            Tool tool = this.tool;
            StringBuilder sbM5a2 = C0000a.m5a("You may only have one parser per grammar file: class ");
            sbM5a2.append(token.getText());
            tool.panic(sbM5a2.toString());
        }
        this.numParsers++;
        reset();
        Grammar grammar = (Grammar) this.grammars.get(token);
        if (grammar == null) {
            this.grammar = new ParserGrammar(token.getText(), this.tool, str2);
            Grammar grammar2 = this.grammar;
            grammar2.comment = str3;
            grammar2.processArguments(this.args);
            this.grammar.setFilename(str);
            this.grammars.put(this.grammar.getClassName(), this.grammar);
            this.grammar.preambleAction = this.thePreambleAction;
            this.thePreambleAction = new CommonToken(0, "");
            return;
        }
        boolean z = grammar instanceof ParserGrammar;
        Tool tool2 = this.tool;
        if (z) {
            sbM5a = C0000a.m5a("Parser '");
            sbM5a.append(token.getText());
            str4 = "' is already defined";
        } else {
            sbM5a = C0000a.m5a("'");
            sbM5a.append(token.getText());
            str4 = "' is already defined as a non-parser";
        }
        sbM5a.append(str4);
        tool2.panic(sbM5a.toString());
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void startTreeWalker(String str, Token token, String str2, String str3) {
        StringBuilder sbM5a;
        String str4;
        if (this.numTreeParsers > 0) {
            Tool tool = this.tool;
            StringBuilder sbM5a2 = C0000a.m5a("You may only have one tree parser per grammar file: class ");
            sbM5a2.append(token.getText());
            tool.panic(sbM5a2.toString());
        }
        this.numTreeParsers++;
        reset();
        Grammar grammar = (Grammar) this.grammars.get(token);
        if (grammar == null) {
            this.grammar = new TreeWalkerGrammar(token.getText(), this.tool, str2);
            Grammar grammar2 = this.grammar;
            grammar2.comment = str3;
            grammar2.processArguments(this.args);
            this.grammar.setFilename(str);
            this.grammars.put(this.grammar.getClassName(), this.grammar);
            this.grammar.preambleAction = this.thePreambleAction;
            this.thePreambleAction = new CommonToken(0, "");
            return;
        }
        boolean z = grammar instanceof TreeWalkerGrammar;
        Tool tool2 = this.tool;
        if (z) {
            sbM5a = C0000a.m5a("Tree-walker '");
            sbM5a.append(token.getText());
            str4 = "' is already defined";
        } else {
            sbM5a = C0000a.m5a("'");
            sbM5a.append(token.getText());
            str4 = "' is already defined as a non-tree-walker";
        }
        sbM5a.append(str4);
        tool2.panic(sbM5a.toString());
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void synPred() {
    }

    @Override // antlr.ANTLRGrammarParseBehavior
    public void zeroOrMoreSubRule() {
    }
}
