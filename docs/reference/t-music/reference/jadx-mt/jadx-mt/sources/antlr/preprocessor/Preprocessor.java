package antlr.preprocessor;

import antlr.LLkParser;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.collections.impl.BitSet;
import antlr.collections.impl.IndexedVector;

/* JADX INFO: loaded from: classes3.dex */
public class Preprocessor extends LLkParser implements PreprocessorTokenTypes {
    public static final String[] _tokenNames = {"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "\"tokens\"", "HEADER_ACTION", "SUBRULE_BLOCK", "ACTION", "\"class\"", "ID", "\"extends\"", "SEMI", "TOKENS_SPEC", "OPTIONS_START", "ASSIGN_RHS", "RCURLY", "\"protected\"", "\"private\"", "\"public\"", "BANG", "ARG_ACTION", "\"returns\"", "RULE_BLOCK", "\"throws\"", "COMMA", "\"exception\"", "\"catch\"", "ALT", "ELEMENT", "LPAREN", "RPAREN", "ID_OR_KEYWORD", "CURLY_BLOCK_SCARF", "WS", "NEWLINE", "COMMENT", "SL_COMMENT", "ML_COMMENT", "CHAR_LITERAL", "STRING_LITERAL", "ESC", "DIGIT", "XDIGIT"};
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
    public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
    public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
    public antlr.Tool antlrTool;

    public Preprocessor(ParserSharedInputState parserSharedInputState) {
        super(parserSharedInputState, 1);
        this.tokenNames = _tokenNames;
    }

    public Preprocessor(TokenBuffer tokenBuffer) {
        this(tokenBuffer, 1);
    }

    public Preprocessor(TokenBuffer tokenBuffer, int i) {
        super(tokenBuffer, i);
        this.tokenNames = _tokenNames;
    }

    public Preprocessor(TokenStream tokenStream) {
        this(tokenStream, 1);
    }

    public Preprocessor(TokenStream tokenStream, int i) {
        super(tokenStream, i);
        this.tokenNames = _tokenNames;
    }

    public static final long[] mk_tokenSet_0() {
        return new long[]{2, 0};
    }

    public static final long[] mk_tokenSet_1() {
        return new long[]{4658050, 0};
    }

    public static final long[] mk_tokenSet_2() {
        return new long[]{459264, 0};
    }

    public static final long[] mk_tokenSet_3() {
        return new long[]{386, 0};
    }

    public static final long[] mk_tokenSet_4() {
        return new long[]{2048, 0};
    }

    public static final long[] mk_tokenSet_5() {
        return new long[]{459650, 0};
    }

    public static final long[] mk_tokenSet_6() {
        return new long[]{4202624, 0};
    }

    public static final long[] mk_tokenSet_7() {
        return new long[]{34014082, 0};
    }

    public static final long[] mk_tokenSet_8() {
        return new long[]{101122946, 0};
    }

    public final Grammar class_def(String str, Hierarchy hierarchy) {
        RecognitionException e;
        Grammar grammar;
        Token tokenMo108LT;
        Token tokenMo108LT2;
        Token tokenMo108LT3;
        String strSuperClass;
        Grammar grammar2;
        int i;
        IndexedVector indexedVector = new IndexedVector(100);
        IndexedVector indexedVectorOptionSpec = null;
        try {
            int iMo107LA = mo107LA(1);
            if (iMo107LA == 7) {
                tokenMo108LT = mo108LT(1);
                match(7);
            } else {
                if (iMo107LA != 8) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                tokenMo108LT = null;
            }
            match(8);
            tokenMo108LT2 = mo108LT(1);
            match(9);
            match(10);
            tokenMo108LT3 = mo108LT(1);
            match(9);
            int iMo107LA2 = mo107LA(1);
            if (iMo107LA2 == 6) {
                strSuperClass = superClass();
            } else {
                if (iMo107LA2 != 11) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                strSuperClass = null;
            }
            match(11);
            grammar2 = hierarchy.getGrammar(tokenMo108LT2.getText());
        } catch (RecognitionException e2) {
            e = e2;
            grammar = null;
        }
        if (grammar2 != null) {
            throw new SemanticException("redefinition of grammar " + tokenMo108LT2.getText(), str, tokenMo108LT2.getLine(), tokenMo108LT2.getColumn());
        }
        try {
            grammar = new Grammar(hierarchy.getTool(), tokenMo108LT2.getText(), tokenMo108LT3.getText(), indexedVector);
        } catch (RecognitionException e3) {
            e = e3;
            grammar = grammar2;
        }
        try {
            grammar.superClass = strSuperClass;
            if (tokenMo108LT != null) {
                grammar.setPreambleAction(tokenMo108LT.getText());
            }
            int iMo107LA3 = mo107LA(1);
            if (iMo107LA3 != 7 && iMo107LA3 != 9 && iMo107LA3 != 12) {
                if (iMo107LA3 != 13) {
                    switch (iMo107LA3) {
                        case 16:
                        case 17:
                        case 18:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                } else {
                    indexedVectorOptionSpec = optionSpec(grammar);
                }
            }
            grammar.setOptions(indexedVectorOptionSpec);
            int iMo107LA4 = mo107LA(1);
            if (iMo107LA4 != 7 && iMo107LA4 != 9) {
                if (iMo107LA4 != 12) {
                    switch (iMo107LA4) {
                        case 16:
                        case 17:
                        case 18:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                } else {
                    Token tokenMo108LT4 = mo108LT(1);
                    match(12);
                    grammar.setTokenSection(tokenMo108LT4.getText());
                }
            }
            int iMo107LA5 = mo107LA(1);
            if (iMo107LA5 == 7) {
                Token tokenMo108LT5 = mo108LT(1);
                match(7);
                grammar.setMemberAction(tokenMo108LT5.getText());
            } else if (iMo107LA5 != 9) {
                switch (iMo107LA5) {
                    case 16:
                    case 17:
                    case 18:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            i = 0;
            while (_tokenSet_2.member(mo107LA(1))) {
                rule(grammar);
                i++;
            }
        } catch (RecognitionException e4) {
            e = e4;
            reportError(e);
            consume();
            consumeUntil(_tokenSet_3);
        }
        if (i >= 1) {
            return grammar;
        }
        throw new NoViableAltException(mo108LT(1), getFilename());
    }

    public final String exceptionGroup() {
        String str = "";
        while (mo107LA(1) == 25) {
            try {
                str = str + exceptionSpec();
            } catch (RecognitionException e) {
                reportError(e);
                consume();
                consumeUntil(_tokenSet_5);
            }
        }
        return str;
    }

    public final String exceptionHandler() {
        try {
            match(26);
            Token tokenMo108LT = mo108LT(1);
            match(20);
            Token tokenMo108LT2 = mo108LT(1);
            match(7);
            return System.getProperty("line.separator") + "catch " + tokenMo108LT.getText() + " " + tokenMo108LT2.getText();
        } catch (RecognitionException e) {
            this.reportError(e);
            this.consume();
            this.consumeUntil(_tokenSet_8);
            return null;
        }
    }

    /* JADX WARN: Path cross not found for [B:20:0x0060, B:5:0x0025], limit reached: 28 */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0066 A[Catch: RecognitionException -> 0x0078, TryCatch #0 {RecognitionException -> 0x0078, blocks: (B:3:0x0019, B:15:0x0038, B:16:0x003b, B:17:0x0048, B:18:0x0049, B:19:0x005c, B:23:0x0073, B:20:0x0060, B:22:0x0066), top: B:28:0x0019 }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0073 -> B:20:0x0060). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final String exceptionSpec() {
        String strExceptionHandler;
        StringBuilder sb;
        String string = System.getProperty("line.separator") + "exception ";
        try {
            match(25);
            int iMo107LA = mo107LA(1);
            if (iMo107LA != 1) {
                if (iMo107LA == 20) {
                    Token tokenMo108LT = mo108LT(1);
                    match(20);
                    sb = new StringBuilder();
                    sb.append(string);
                    strExceptionHandler = tokenMo108LT.getText();
                    sb.append(strExceptionHandler);
                    string = sb.toString();
                } else if (iMo107LA != 7 && iMo107LA != 8 && iMo107LA != 9 && iMo107LA != 25 && iMo107LA != 26) {
                    switch (iMo107LA) {
                        case 16:
                        case 17:
                        case 18:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                }
            }
            if (mo107LA(1) == 26) {
                strExceptionHandler = exceptionHandler();
                sb = new StringBuilder();
                sb.append(string);
                sb.append(strExceptionHandler);
                string = sb.toString();
                if (mo107LA(1) == 26) {
                }
            }
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_7);
        }
        return string;
    }

    public antlr.Tool getTool() {
        return this.antlrTool;
    }

    public final void grammarFile(Hierarchy hierarchy, String str) {
        while (mo107LA(1) == 5) {
            try {
                Token tokenMo108LT = mo108LT(1);
                match(5);
                hierarchy.getFile(str).addHeaderAction(tokenMo108LT.getText());
            } catch (RecognitionException e) {
                reportError(e);
                consume();
                consumeUntil(_tokenSet_0);
                return;
            }
        }
        int iMo107LA = mo107LA(1);
        IndexedVector indexedVectorOptionSpec = null;
        if (iMo107LA != 1) {
            if (iMo107LA == 13) {
                indexedVectorOptionSpec = optionSpec(null);
            } else if (iMo107LA != 7 && iMo107LA != 8) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
        }
        while (true) {
            if (mo107LA(1) != 7 && mo107LA(1) != 8) {
                match(1);
                return;
            }
            Grammar grammarClass_def = class_def(str, hierarchy);
            if (grammarClass_def != null && indexedVectorOptionSpec != null) {
                hierarchy.getFile(str).setOptions(indexedVectorOptionSpec);
            }
            if (grammarClass_def != null) {
                grammarClass_def.setFileName(str);
                hierarchy.addGrammar(grammarClass_def);
            }
        }
    }

    public final IndexedVector optionSpec(Grammar grammar) {
        IndexedVector indexedVector = new IndexedVector();
        try {
            match(13);
            while (mo107LA(1) == 9) {
                Token tokenMo108LT = mo108LT(1);
                match(9);
                Token tokenMo108LT2 = mo108LT(1);
                match(14);
                Option option = new Option(tokenMo108LT.getText(), tokenMo108LT2.getText(), grammar);
                indexedVector.appendElement(option.getName(), option);
                if (grammar != null && tokenMo108LT.getText().equals("importVocab")) {
                    grammar.specifiedVocabulary = true;
                    grammar.importVocab = tokenMo108LT2.getText();
                } else if (grammar != null && tokenMo108LT.getText().equals("exportVocab")) {
                    grammar.exportVocab = tokenMo108LT2.getText().substring(0, tokenMo108LT2.getText().length() - 1);
                    grammar.exportVocab = grammar.exportVocab.trim();
                }
            }
            match(15);
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_1);
        }
        return indexedVector;
    }

    @Override // antlr.Parser
    public void reportError(RecognitionException recognitionException) {
        if (getTool() != null) {
            getTool().error(recognitionException.getErrorMessage(), recognitionException.getFilename(), recognitionException.getLine(), recognitionException.getColumn());
        } else {
            super.reportError(recognitionException);
        }
    }

    @Override // antlr.Parser
    public void reportError(String str) {
        if (getTool() != null) {
            getTool().error(str, getFilename(), -1, -1);
        } else {
            super.reportError(str);
        }
    }

    @Override // antlr.Parser
    public void reportWarning(String str) {
        if (getTool() != null) {
            getTool().warning(str, getFilename(), -1, -1);
        } else {
            super.reportWarning(str);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0060  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void rule(Grammar grammar) {
        String str;
        boolean z;
        Token tokenMo108LT;
        Token tokenMo108LT2;
        String strThrowsSpec;
        IndexedVector indexedVectorOptionSpec;
        try {
            int iMo107LA = mo107LA(1);
            Token tokenMo108LT3 = null;
            if (iMo107LA != 9) {
                switch (iMo107LA) {
                    case 16:
                        match(16);
                        str = "protected";
                        break;
                    case 17:
                        match(17);
                        str = "private";
                        break;
                    case 18:
                        match(18);
                        str = "public";
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            } else {
                str = null;
            }
            Token tokenMo108LT4 = mo108LT(1);
            match(9);
            int iMo107LA2 = mo107LA(1);
            if (iMo107LA2 == 7 || iMo107LA2 == 13) {
                z = false;
            } else {
                switch (iMo107LA2) {
                    case 19:
                        match(19);
                        z = true;
                        break;
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            int iMo107LA3 = mo107LA(1);
            if (iMo107LA3 == 7 || iMo107LA3 == 13) {
                tokenMo108LT = null;
            } else {
                switch (iMo107LA3) {
                    case 20:
                        tokenMo108LT = mo108LT(1);
                        match(20);
                        break;
                    case 21:
                    case 22:
                    case 23:
                        tokenMo108LT = null;
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            int iMo107LA4 = mo107LA(1);
            if (iMo107LA4 == 7 || iMo107LA4 == 13) {
                tokenMo108LT2 = null;
            } else {
                switch (iMo107LA4) {
                    case 21:
                        match(21);
                        tokenMo108LT2 = mo108LT(1);
                        match(20);
                        break;
                    case 22:
                    case 23:
                        tokenMo108LT2 = null;
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            int iMo107LA5 = mo107LA(1);
            if (iMo107LA5 == 7 || iMo107LA5 == 13 || iMo107LA5 == 22) {
                strThrowsSpec = "";
            } else {
                if (iMo107LA5 != 23) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                strThrowsSpec = throwsSpec();
            }
            int iMo107LA6 = mo107LA(1);
            if (iMo107LA6 == 7) {
                indexedVectorOptionSpec = null;
            } else if (iMo107LA6 == 13) {
                indexedVectorOptionSpec = optionSpec(null);
            } else {
                if (iMo107LA6 != 22) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                indexedVectorOptionSpec = null;
            }
            int iMo107LA7 = mo107LA(1);
            if (iMo107LA7 == 7) {
                tokenMo108LT3 = mo108LT(1);
                match(7);
            } else if (iMo107LA7 != 22) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            Token tokenMo108LT5 = mo108LT(1);
            match(22);
            Rule rule = new Rule(tokenMo108LT4.getText(), tokenMo108LT5.getText() + exceptionGroup(), indexedVectorOptionSpec, grammar);
            rule.setThrowsSpec(strThrowsSpec);
            if (tokenMo108LT != null) {
                rule.setArgs(tokenMo108LT.getText());
            }
            if (tokenMo108LT2 != null) {
                rule.setReturnValue(tokenMo108LT2.getText());
            }
            if (tokenMo108LT3 != null) {
                rule.setInitAction(tokenMo108LT3.getText());
            }
            if (z) {
                rule.setBang();
            }
            rule.setVisibility(str);
            if (grammar != null) {
                grammar.addRule(rule);
            }
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_5);
        }
    }

    public void setTool(antlr.Tool tool) {
        if (this.antlrTool != null) {
            throw new IllegalStateException("antlr.Tool already registered");
        }
        this.antlrTool = tool;
    }

    public final String superClass() {
        String text = mo108LT(1).getText();
        try {
            match(6);
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_4);
        }
        return text;
    }

    public final String throwsSpec() {
        String string = "throws ";
        try {
            match(23);
            Token tokenMo108LT = mo108LT(1);
            match(9);
            StringBuilder sb = new StringBuilder();
            sb.append("throws ");
            String text = tokenMo108LT.getText();
            while (true) {
                sb.append(text);
                string = sb.toString();
                if (mo107LA(1) != 24) {
                    break;
                }
                match(24);
                Token tokenMo108LT2 = mo108LT(1);
                match(9);
                sb = new StringBuilder();
                sb.append(string);
                sb.append(",");
                text = tokenMo108LT2.getText();
            }
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_6);
        }
        return string;
    }
}
