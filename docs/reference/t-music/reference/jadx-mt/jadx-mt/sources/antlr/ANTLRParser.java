package antlr;

import antlr.collections.impl.BitSet;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class ANTLRParser extends LLkParser implements ANTLRTokenTypes {
    public static final boolean DEBUG_PARSER = false;
    public Tool antlrTool;
    public ANTLRGrammarParseBehavior behavior;
    public int blockNesting;
    public static final String[] _tokenNames = {"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "\"tokens\"", "\"header\"", "STRING_LITERAL", "ACTION", "DOC_COMMENT", "\"lexclass\"", "\"class\"", "\"extends\"", "\"Lexer\"", "\"TreeParser\"", "OPTIONS", "ASSIGN", "SEMI", "RCURLY", "\"charVocabulary\"", "CHAR_LITERAL", "INT", "OR", "RANGE", "TOKENS", "TOKEN_REF", "OPEN_ELEMENT_OPTION", "CLOSE_ELEMENT_OPTION", "LPAREN", "RPAREN", "\"Parser\"", "\"protected\"", "\"public\"", "\"private\"", "BANG", "ARG_ACTION", "\"returns\"", "COLON", "\"throws\"", "COMMA", "\"exception\"", "\"catch\"", "RULE_REF", "NOT_OP", "SEMPRED", "TREE_BEGIN", "QUESTION", "STAR", "PLUS", "IMPLIES", "CARET", "WILDCARD", "\"options\"", "WS", "COMMENT", "SL_COMMENT", "ML_COMMENT", "ESC", "DIGIT", "XDIGIT", "NESTED_ARG_ACTION", "NESTED_ACTION", "WS_LOOP", "INTERNAL_RULE_REF", "WS_OPT"};
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
    public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
    public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
    public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
    public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
    public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());

    public ANTLRParser(ParserSharedInputState parserSharedInputState) {
        super(parserSharedInputState, 2);
        this.blockNesting = -1;
        this.tokenNames = _tokenNames;
    }

    public ANTLRParser(TokenBuffer tokenBuffer) {
        this(tokenBuffer, 2);
    }

    public ANTLRParser(TokenBuffer tokenBuffer, int i) {
        super(tokenBuffer, i);
        this.blockNesting = -1;
        this.tokenNames = _tokenNames;
    }

    public ANTLRParser(TokenBuffer tokenBuffer, ANTLRGrammarParseBehavior aNTLRGrammarParseBehavior, Tool tool) {
        super(tokenBuffer, 1);
        this.blockNesting = -1;
        this.tokenNames = _tokenNames;
        this.behavior = aNTLRGrammarParseBehavior;
        this.antlrTool = tool;
    }

    public ANTLRParser(TokenStream tokenStream) {
        this(tokenStream, 2);
    }

    public ANTLRParser(TokenStream tokenStream, int i) {
        super(tokenStream, i);
        this.blockNesting = -1;
        this.tokenNames = _tokenNames;
    }

    private void checkForMissingEndRule(Token token) {
        if (token.getColumn() == 1) {
            this.antlrTool.warning("did you forget to terminate previous rule?", getFilename(), token.getLine(), token.getColumn());
        }
    }

    private boolean lastInRule() {
        if (this.blockNesting == 0) {
            return mo107LA(1) == 16 || mo107LA(1) == 39 || mo107LA(1) == 21;
        }
        return false;
    }

    public static final long[] mk_tokenSet_0() {
        return new long[]{2206556225792L, 0};
    }

    public static final long[] mk_tokenSet_1() {
        return new long[]{2472844214400L, 0};
    }

    public static final long[] mk_tokenSet_10() {
        return new long[]{2251345007067328L, 0};
    }

    public static final long[] mk_tokenSet_11() {
        return new long[]{1721861130420416L, 0};
    }

    public static final long[] mk_tokenSet_2() {
        return new long[]{1158885407195328L, 0};
    }

    public static final long[] mk_tokenSet_3() {
        return new long[]{1159461236965568L, 0};
    }

    public static final long[] mk_tokenSet_4() {
        return new long[]{1132497128128576L, 0};
    }

    public static final long[] mk_tokenSet_5() {
        return new long[]{1722479914074304L, 0};
    }

    public static final long[] mk_tokenSet_6() {
        return new long[]{1722411194597568L, 0};
    }

    public static final long[] mk_tokenSet_7() {
        return new long[]{1125899924144192L, 0};
    }

    public static final long[] mk_tokenSet_8() {
        return new long[]{1722411190386880L, 0};
    }

    public static final long[] mk_tokenSet_9() {
        return new long[]{1159444023476416L, 0};
    }

    public final void alternative() throws NoViableAltException {
        boolean z;
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 6 || iMo107LA == 7 || iMo107LA == 16 || iMo107LA == 19 || iMo107LA == 21 || iMo107LA == 24) {
            z = true;
        } else if (iMo107LA == 33) {
            match(33);
            z = this.inputState.guessing != 0;
        } else if (iMo107LA == 39 || iMo107LA == 50 || iMo107LA == 27 || iMo107LA == 28) {
            z = true;
        } else {
            switch (iMo107LA) {
                case 41:
                case 42:
                case 43:
                case 44:
                    z = true;
                    break;
                default:
                    throw new NoViableAltException(mo108LT(1), getFilename());
            }
        }
        if (this.inputState.guessing == 0) {
            this.behavior.beginAlt(z);
        }
        while (_tokenSet_2.member(mo107LA(1))) {
            element();
        }
        int iMo107LA2 = mo107LA(1);
        if (iMo107LA2 != 16 && iMo107LA2 != 21 && iMo107LA2 != 28) {
            if (iMo107LA2 != 39) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            exceptionSpecNoLabel();
        }
        if (this.inputState.guessing == 0) {
            this.behavior.endAlt();
        }
    }

    public final int ast_type_spec() throws NoViableAltException {
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 6 || iMo107LA == 7 || iMo107LA == 16 || iMo107LA == 19 || iMo107LA == 21 || iMo107LA == 39 || iMo107LA == 24 || iMo107LA == 25 || iMo107LA == 27 || iMo107LA == 28) {
            return 1;
        }
        if (iMo107LA == 33) {
            match(33);
            return this.inputState.guessing == 0 ? 3 : 1;
        }
        if (iMo107LA == 34) {
            return 1;
        }
        if (iMo107LA == 49) {
            match(49);
            return this.inputState.guessing == 0 ? 2 : 1;
        }
        if (iMo107LA == 50) {
            return 1;
        }
        switch (iMo107LA) {
            case 41:
            case 42:
            case 43:
            case 44:
                return 1;
            default:
                throw new NoViableAltException(mo108LT(1), getFilename());
        }
    }

    public final void block() throws NoViableAltException {
        if (this.inputState.guessing == 0) {
            this.blockNesting++;
        }
        while (true) {
            alternative();
            if (mo107LA(1) != 21) {
                break;
            } else {
                match(21);
            }
        }
        if (this.inputState.guessing == 0) {
            this.blockNesting--;
        }
    }

    public final BitSet charSet() throws NoViableAltException {
        BitSet blockElement = setBlockElement();
        while (mo107LA(1) == 21) {
            match(21);
            BitSet blockElement2 = setBlockElement();
            if (this.inputState.guessing == 0) {
                blockElement.orInPlace(blockElement2);
            }
        }
        return blockElement;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0096 A[Catch: RecognitionException -> 0x009b, TRY_LEAVE, TryCatch #0 {RecognitionException -> 0x009b, blocks: (B:27:0x0071, B:30:0x0079, B:31:0x0088, B:32:0x0095, B:33:0x0096), top: B:88:0x0071 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a8 A[Catch: RecognitionException -> 0x0136, TryCatch #2 {RecognitionException -> 0x0136, blocks: (B:3:0x0008, B:4:0x000c, B:5:0x000f, B:67:0x012a, B:68:0x0135, B:6:0x0013, B:8:0x0021, B:9:0x0026, B:10:0x002a, B:11:0x002d, B:65:0x011e, B:66:0x0129, B:12:0x0031, B:14:0x003e, B:16:0x0044, B:18:0x0051, B:38:0x00a8, B:60:0x0101, B:62:0x010a, B:39:0x00ac, B:41:0x00b2, B:43:0x00b8, B:45:0x00be, B:49:0x00da, B:52:0x00e8, B:53:0x00ec, B:55:0x00f2, B:57:0x00f8, B:59:0x00fe, B:63:0x0110, B:64:0x011d, B:20:0x0057, B:22:0x005d, B:26:0x0066, B:36:0x009c), top: B:91:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ac A[Catch: RecognitionException -> 0x0136, TryCatch #2 {RecognitionException -> 0x0136, blocks: (B:3:0x0008, B:4:0x000c, B:5:0x000f, B:67:0x012a, B:68:0x0135, B:6:0x0013, B:8:0x0021, B:9:0x0026, B:10:0x002a, B:11:0x002d, B:65:0x011e, B:66:0x0129, B:12:0x0031, B:14:0x003e, B:16:0x0044, B:18:0x0051, B:38:0x00a8, B:60:0x0101, B:62:0x010a, B:39:0x00ac, B:41:0x00b2, B:43:0x00b8, B:45:0x00be, B:49:0x00da, B:52:0x00e8, B:53:0x00ec, B:55:0x00f2, B:57:0x00f8, B:59:0x00fe, B:63:0x0110, B:64:0x011d, B:20:0x0057, B:22:0x005d, B:26:0x0066, B:36:0x009c), top: B:91:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x010a A[Catch: RecognitionException -> 0x0136, TryCatch #2 {RecognitionException -> 0x0136, blocks: (B:3:0x0008, B:4:0x000c, B:5:0x000f, B:67:0x012a, B:68:0x0135, B:6:0x0013, B:8:0x0021, B:9:0x0026, B:10:0x002a, B:11:0x002d, B:65:0x011e, B:66:0x0129, B:12:0x0031, B:14:0x003e, B:16:0x0044, B:18:0x0051, B:38:0x00a8, B:60:0x0101, B:62:0x010a, B:39:0x00ac, B:41:0x00b2, B:43:0x00b8, B:45:0x00be, B:49:0x00da, B:52:0x00e8, B:53:0x00ec, B:55:0x00f2, B:57:0x00f8, B:59:0x00fe, B:63:0x0110, B:64:0x011d, B:20:0x0057, B:22:0x005d, B:26:0x0066, B:36:0x009c), top: B:91:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x017b A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void classDef() throws RecognitionException {
        String string;
        boolean z;
        int iMo107LA;
        boolean z2;
        try {
            switch (mo107LA(1)) {
                case 7:
                    Token tokenMo108LT = mo108LT(1);
                    match(7);
                    if (this.inputState.guessing == 0) {
                        this.behavior.refPreambleAction(tokenMo108LT);
                    }
                    break;
                case 8:
                case 9:
                case 10:
                    break;
                default:
                    throw new NoViableAltException(mo108LT(1), getFilename());
            }
            switch (mo107LA(1)) {
                case 8:
                    Token tokenMo108LT2 = mo108LT(1);
                    match(8);
                    String text = this.inputState.guessing == 0 ? tokenMo108LT2.getText() : null;
                    if ((mo107LA(1) != 9 || mo107LA(1) == 10) && (mo107LA(2) == 24 || mo107LA(2) == 41)) {
                        int iMark = mark();
                        this.inputState.guessing++;
                        try {
                            iMo107LA = mo107LA(1);
                            if (iMo107LA == 9) {
                                match(9);
                            } else {
                                if (iMo107LA != 10) {
                                    throw new NoViableAltException(mo108LT(1), getFilename());
                                }
                                match(10);
                                m104id();
                                match(11);
                                match(12);
                            }
                            z = true;
                        } catch (RecognitionException unused) {
                            z = false;
                        }
                        rewind(iMark);
                        this.inputState.guessing--;
                    } else {
                        z = false;
                    }
                    if (z) {
                        if (mo107LA(1) == 10 && (mo107LA(2) == 24 || mo107LA(2) == 41)) {
                            int iMark2 = mark();
                            this.inputState.guessing++;
                            try {
                                match(10);
                                m104id();
                                match(11);
                                match(13);
                                z2 = true;
                            } catch (RecognitionException unused2) {
                                z2 = false;
                            }
                            rewind(iMark2);
                            this.inputState.guessing--;
                        } else {
                            z2 = false;
                        }
                        if (z2) {
                            treeParserSpec(text);
                        } else {
                            if (mo107LA(1) != 10 || (mo107LA(2) != 24 && mo107LA(2) != 41)) {
                                throw new NoViableAltException(mo108LT(1), getFilename());
                            }
                            parserSpec(text);
                        }
                    } else {
                        lexerSpec(text);
                    }
                    rules();
                    if (this.inputState.guessing != 0) {
                        this.behavior.endGrammar();
                        return;
                    }
                    return;
                case 9:
                case 10:
                    if (mo107LA(1) != 9) {
                        int iMark3 = mark();
                        this.inputState.guessing++;
                        iMo107LA = mo107LA(1);
                        if (iMo107LA == 9) {
                        }
                        z = true;
                        rewind(iMark3);
                        this.inputState.guessing--;
                    } else {
                        int iMark32 = mark();
                        this.inputState.guessing++;
                        iMo107LA = mo107LA(1);
                        if (iMo107LA == 9) {
                        }
                        z = true;
                        rewind(iMark32);
                        this.inputState.guessing--;
                    }
                    if (z) {
                    }
                    rules();
                    if (this.inputState.guessing != 0) {
                    }
                    break;
                default:
                    throw new NoViableAltException(mo108LT(1), getFilename());
            }
        } catch (RecognitionException e) {
            if (this.inputState.guessing != 0) {
                throw e;
            }
            if ((e instanceof NoViableAltException) && ((NoViableAltException) e).token.getType() == 8) {
                string = "JAVADOC comments may only prefix rules and grammars";
            } else {
                StringBuilder sbM5a = C0000a.m5a("rule classDef trapped:\n");
                sbM5a.append(e.toString());
                string = sbM5a.toString();
            }
            reportError(e, string);
            this.behavior.abortGrammar();
            boolean z3 = true;
            while (z3) {
                consume();
                int iMo107LA2 = mo107LA(1);
                if (iMo107LA2 == 1 || iMo107LA2 == 9 || iMo107LA2 == 10) {
                    z3 = false;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0062 A[PHI: r13
      0x0062: PHI (r13v10 antlr.Token) = (r13v9 antlr.Token), (r13v25 antlr.Token) binds: [B:21:0x0060, B:14:0x0045] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00db  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void ebnf(Token token, boolean z) throws NoViableAltException {
        Token tokenMo108LT;
        Token tokenMo108LT2 = mo108LT(1);
        match(27);
        if (this.inputState.guessing == 0) {
            this.behavior.beginSubRule(token, tokenMo108LT2, z);
        }
        if (mo107LA(1) == 14) {
            subruleOptionsSpec();
            int iMo107LA = mo107LA(1);
            if (iMo107LA == 7) {
                tokenMo108LT = mo108LT(1);
                match(7);
                if (this.inputState.guessing == 0) {
                    this.behavior.refInitAction(tokenMo108LT);
                }
            } else if (iMo107LA != 36) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            match(36);
        } else if (mo107LA(1) == 7 && mo107LA(2) == 36) {
            tokenMo108LT = mo108LT(1);
            match(7);
            if (this.inputState.guessing == 0) {
            }
            match(36);
        } else if (!_tokenSet_9.member(mo107LA(1)) || !_tokenSet_10.member(mo107LA(2))) {
            throw new NoViableAltException(mo108LT(1), getFilename());
        }
        block();
        match(28);
        int iMo107LA2 = mo107LA(1);
        if (iMo107LA2 == 6 || iMo107LA2 == 7 || iMo107LA2 == 16 || iMo107LA2 == 19 || iMo107LA2 == 21 || iMo107LA2 == 33 || iMo107LA2 == 39 || iMo107LA2 == 50 || iMo107LA2 == 24 || iMo107LA2 == 25 || iMo107LA2 == 27 || iMo107LA2 == 28) {
            int iMo107LA3 = mo107LA(1);
            if (iMo107LA3 != 6 && iMo107LA3 != 7 && iMo107LA3 != 16 && iMo107LA3 != 19 && iMo107LA3 != 21 && iMo107LA3 != 33 && iMo107LA3 != 39 && iMo107LA3 != 50 && iMo107LA3 != 24 && iMo107LA3 != 25 && iMo107LA3 != 27 && iMo107LA3 != 28) {
                switch (iMo107LA3) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        break;
                    case 45:
                        match(45);
                        if (this.inputState.guessing == 0) {
                            this.behavior.optionalSubRule();
                        }
                        break;
                    case 46:
                        match(46);
                        if (this.inputState.guessing == 0) {
                            this.behavior.zeroOrMoreSubRule();
                        }
                        break;
                    case 47:
                        match(47);
                        if (this.inputState.guessing == 0) {
                            this.behavior.oneOrMoreSubRule();
                        }
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            int iMo107LA4 = mo107LA(1);
            if (iMo107LA4 != 6 && iMo107LA4 != 7 && iMo107LA4 != 16 && iMo107LA4 != 19 && iMo107LA4 != 21) {
                if (iMo107LA4 == 33) {
                    match(33);
                    if (this.inputState.guessing == 0) {
                        this.behavior.noASTSubRule();
                    }
                } else if (iMo107LA4 != 39 && iMo107LA4 != 50 && iMo107LA4 != 24 && iMo107LA4 != 25 && iMo107LA4 != 27 && iMo107LA4 != 28) {
                    switch (iMo107LA4) {
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                }
            }
        } else {
            switch (iMo107LA2) {
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                    break;
                case 48:
                    match(48);
                    if (this.inputState.guessing == 0) {
                        this.behavior.synPred();
                    }
                    break;
                default:
                    throw new NoViableAltException(mo108LT(1), getFilename());
            }
        }
        if (this.inputState.guessing == 0) {
            this.behavior.endSubRule();
        }
    }

    public final void element() throws NoViableAltException {
        elementNoOptionSpec();
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 6 || iMo107LA == 7 || iMo107LA == 16 || iMo107LA == 19 || iMo107LA == 21 || iMo107LA == 39 || iMo107LA == 50 || iMo107LA == 24) {
            return;
        }
        if (iMo107LA == 25) {
            elementOptionSpec();
        } else {
            if (iMo107LA == 27 || iMo107LA == 28) {
                return;
            }
            switch (iMo107LA) {
                case 41:
                case 42:
                case 43:
                case 44:
                    return;
                default:
                    throw new NoViableAltException(mo108LT(1), getFilename());
            }
        }
    }

    public final void elementNoOptionSpec() throws NoViableAltException {
        Token token;
        Token token2;
        Token token3;
        Token token4;
        Token token5;
        Token token6;
        Token tokenM104id;
        boolean z = true;
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 7) {
            Token tokenMo108LT = mo108LT(1);
            match(7);
            if (this.inputState.guessing == 0) {
                this.behavior.refAction(tokenMo108LT);
                return;
            }
            return;
        }
        if (iMo107LA == 43) {
            Token tokenMo108LT2 = mo108LT(1);
            match(43);
            if (this.inputState.guessing == 0) {
                this.behavior.refSemPred(tokenMo108LT2);
                return;
            }
            return;
        }
        if (iMo107LA == 44) {
            tree();
            return;
        }
        Token token7 = null;
        int i = 1;
        if ((mo107LA(1) != 24 && mo107LA(1) != 41) || mo107LA(2) != 15) {
            if (!_tokenSet_4.member(mo107LA(1)) || !_tokenSet_5.member(mo107LA(2))) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            if ((mo107LA(1) == 24 || mo107LA(1) == 41) && mo107LA(2) == 36) {
                tokenM104id = m104id();
                match(36);
                if (this.inputState.guessing == 0) {
                    checkForMissingEndRule(tokenM104id);
                }
            } else {
                if (!_tokenSet_4.member(mo107LA(1)) || !_tokenSet_6.member(mo107LA(2))) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                tokenM104id = null;
            }
            int iMo107LA2 = mo107LA(1);
            if (iMo107LA2 == 27) {
                z = false;
            } else if (iMo107LA2 == 41) {
                Token tokenMo108LT3 = mo108LT(1);
                match(41);
                int iMo107LA3 = mo107LA(1);
                if (iMo107LA3 != 6 && iMo107LA3 != 7 && iMo107LA3 != 16 && iMo107LA3 != 19 && iMo107LA3 != 21 && iMo107LA3 != 39 && iMo107LA3 != 50 && iMo107LA3 != 24 && iMo107LA3 != 25 && iMo107LA3 != 27 && iMo107LA3 != 28 && iMo107LA3 != 33) {
                    if (iMo107LA3 != 34) {
                        switch (iMo107LA3) {
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                                break;
                            default:
                                throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                    } else {
                        Token tokenMo108LT4 = mo108LT(1);
                        match(34);
                        if (this.inputState.guessing == 0) {
                            token7 = tokenMo108LT4;
                        }
                    }
                }
                int iMo107LA4 = mo107LA(1);
                if (iMo107LA4 != 6 && iMo107LA4 != 7 && iMo107LA4 != 16 && iMo107LA4 != 19 && iMo107LA4 != 21) {
                    if (iMo107LA4 == 33) {
                        match(33);
                        if (this.inputState.guessing == 0) {
                            i = 3;
                        }
                    } else if (iMo107LA4 != 39 && iMo107LA4 != 50 && iMo107LA4 != 24 && iMo107LA4 != 25 && iMo107LA4 != 27 && iMo107LA4 != 28) {
                        switch (iMo107LA4) {
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                                break;
                            default:
                                throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                    }
                }
                if (this.inputState.guessing != 0) {
                    return;
                }
                token6 = tokenM104id;
                token3 = tokenMo108LT3;
                token4 = token7;
                token5 = null;
            } else {
                if (iMo107LA2 != 42) {
                    if ((mo107LA(1) == 6 || mo107LA(1) == 19 || mo107LA(1) == 24) && mo107LA(2) == 22) {
                        range(tokenM104id);
                        return;
                    } else {
                        if (!_tokenSet_7.member(mo107LA(1)) || !_tokenSet_8.member(mo107LA(2))) {
                            throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                        terminal(tokenM104id);
                        return;
                    }
                }
                match(42);
                int iMo107LA5 = mo107LA(1);
                if (iMo107LA5 == 19 || iMo107LA5 == 24) {
                    notTerminal(tokenM104id);
                    return;
                } else if (iMo107LA5 != 27) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            ebnf(tokenM104id, z);
            return;
        }
        Token tokenM104id2 = m104id();
        match(15);
        if ((mo107LA(1) == 24 || mo107LA(1) == 41) && mo107LA(2) == 36) {
            Token tokenM104id3 = m104id();
            match(36);
            if (this.inputState.guessing == 0) {
                checkForMissingEndRule(tokenM104id3);
            }
            token = tokenM104id3;
        } else {
            if ((mo107LA(1) != 24 && mo107LA(1) != 41) || !_tokenSet_3.member(mo107LA(2))) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            token = null;
        }
        int iMo107LA6 = mo107LA(1);
        if (iMo107LA6 == 24) {
            Token tokenMo108LT5 = mo108LT(1);
            match(24);
            int iMo107LA7 = mo107LA(1);
            if (iMo107LA7 == 6 || iMo107LA7 == 7 || iMo107LA7 == 16 || iMo107LA7 == 19 || iMo107LA7 == 21) {
                token2 = null;
            } else if (iMo107LA7 == 34) {
                Token tokenMo108LT6 = mo108LT(1);
                match(34);
                token2 = this.inputState.guessing == 0 ? tokenMo108LT6 : null;
            } else if (iMo107LA7 == 39 || iMo107LA7 == 50 || iMo107LA7 == 24 || iMo107LA7 == 25 || iMo107LA7 == 27 || iMo107LA7 == 28) {
                token2 = null;
            } else {
                switch (iMo107LA7) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        token2 = null;
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            if (this.inputState.guessing == 0) {
                this.behavior.refToken(tokenM104id2, tokenMo108LT5, token, token2, false, 1, lastInRule());
                return;
            }
            return;
        }
        if (iMo107LA6 != 41) {
            throw new NoViableAltException(mo108LT(1), getFilename());
        }
        Token tokenMo108LT7 = mo108LT(1);
        match(41);
        int iMo107LA8 = mo107LA(1);
        if (iMo107LA8 != 6 && iMo107LA8 != 7 && iMo107LA8 != 16 && iMo107LA8 != 19 && iMo107LA8 != 21 && iMo107LA8 != 39 && iMo107LA8 != 50 && iMo107LA8 != 24 && iMo107LA8 != 25 && iMo107LA8 != 27 && iMo107LA8 != 28 && iMo107LA8 != 33) {
            if (iMo107LA8 != 34) {
                switch (iMo107LA8) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            } else {
                Token tokenMo108LT8 = mo108LT(1);
                match(34);
                if (this.inputState.guessing == 0) {
                    token7 = tokenMo108LT8;
                }
            }
        }
        int iMo107LA9 = mo107LA(1);
        if (iMo107LA9 != 6 && iMo107LA9 != 7 && iMo107LA9 != 16 && iMo107LA9 != 19 && iMo107LA9 != 21) {
            if (iMo107LA9 == 33) {
                match(33);
                if (this.inputState.guessing == 0) {
                    i = 3;
                }
            } else if (iMo107LA9 != 39 && iMo107LA9 != 50 && iMo107LA9 != 24 && iMo107LA9 != 25 && iMo107LA9 != 27 && iMo107LA9 != 28) {
                switch (iMo107LA9) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        }
        if (this.inputState.guessing != 0) {
            return;
        }
        token3 = tokenMo108LT7;
        token4 = token7;
        token5 = tokenM104id2;
        token6 = token;
        this.behavior.refRule(token5, token3, token6, token4, i);
    }

    public final void elementOptionSpec() throws NoViableAltException {
        Token tokenM104id;
        Token tokenOptionValue;
        match(25);
        Token tokenM104id2 = m104id();
        match(15);
        Token tokenOptionValue2 = optionValue();
        if (this.inputState.guessing != 0) {
            this.behavior.refElementOption(tokenM104id2, tokenOptionValue2);
            while (mo107LA(1) == 16) {
                match(16);
                tokenM104id = m104id();
                match(15);
                tokenOptionValue = optionValue();
                if (this.inputState.guessing == 0) {
                }
            }
            match(26);
            return;
        }
        this.behavior.refElementOption(tokenM104id, tokenOptionValue);
    }

    public final void exceptionGroup() throws NoViableAltException {
        if (this.inputState.guessing == 0) {
            this.behavior.beginExceptionGroup();
        }
        int i = 0;
        while (mo107LA(1) == 39) {
            exceptionSpec();
            i++;
        }
        if (i < 1) {
            throw new NoViableAltException(mo108LT(1), getFilename());
        }
        if (this.inputState.guessing == 0) {
            this.behavior.endExceptionGroup();
        }
    }

    public final void exceptionHandler() {
        match(40);
        Token tokenMo108LT = mo108LT(1);
        match(34);
        Token tokenMo108LT2 = mo108LT(1);
        match(7);
        if (this.inputState.guessing == 0) {
            this.behavior.refExceptionHandler(tokenMo108LT, tokenMo108LT2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0039 A[FALL_THROUGH] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void exceptionSpec() throws NoViableAltException {
        Token tokenMo108LT;
        match(39);
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 1 || iMo107LA == 24) {
            tokenMo108LT = null;
        } else {
            if (iMo107LA != 34) {
                switch (iMo107LA) {
                    default:
                        switch (iMo107LA) {
                            default:
                                switch (iMo107LA) {
                                    case 39:
                                    case 40:
                                    case 41:
                                        break;
                                    default:
                                        throw new NoViableAltException(mo108LT(1), getFilename());
                                }
                            case 30:
                            case 31:
                            case 32:
                                break;
                        }
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        break;
                }
            } else {
                tokenMo108LT = mo108LT(1);
                match(34);
                if (this.inputState.guessing != 0) {
                }
            }
            tokenMo108LT = null;
        }
        if (this.inputState.guessing == 0) {
            this.behavior.beginExceptionSpec(tokenMo108LT);
        }
        while (mo107LA(1) == 40) {
            exceptionHandler();
        }
        if (this.inputState.guessing == 0) {
            this.behavior.endExceptionSpec();
        }
    }

    public final void exceptionSpecNoLabel() {
        match(39);
        if (this.inputState.guessing == 0) {
            this.behavior.beginExceptionSpec(null);
        }
        while (mo107LA(1) == 40) {
            exceptionHandler();
        }
        if (this.inputState.guessing == 0) {
            this.behavior.endExceptionSpec();
        }
    }

    public final void fileOptionsSpec() throws NoViableAltException {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token tokenM104id = m104id();
            match(15);
            Token tokenOptionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setFileOption(tokenM104id, tokenOptionValue, getInputState().filename);
            }
            i = 16;
        }
    }

    public final void grammar() {
        Token tokenMo108LT = null;
        while (mo107LA(1) == 5) {
            try {
                if (this.inputState.guessing == 0) {
                    tokenMo108LT = null;
                }
                match(5);
                int iMo107LA = mo107LA(1);
                if (iMo107LA == 6) {
                    tokenMo108LT = mo108LT(1);
                    match(6);
                } else if (iMo107LA != 7) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                Token tokenMo108LT2 = mo108LT(1);
                match(7);
                if (this.inputState.guessing == 0) {
                    this.behavior.refHeaderAction(tokenMo108LT, tokenMo108LT2);
                }
            } catch (RecognitionException e) {
                if (this.inputState.guessing != 0) {
                    throw e;
                }
                StringBuilder sbM5a = C0000a.m5a("rule grammar trapped:\n");
                sbM5a.append(e.toString());
                reportError(e, sbM5a.toString());
                consumeUntil(1);
                return;
            }
        }
        int iMo107LA2 = mo107LA(1);
        if (iMo107LA2 != 1) {
            if (iMo107LA2 != 14) {
                switch (iMo107LA2) {
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            } else {
                fileOptionsSpec();
            }
        }
        while (mo107LA(1) >= 7 && mo107LA(1) <= 10) {
            classDef();
        }
        match(1);
    }

    /* JADX INFO: renamed from: id */
    public final Token m104id() throws NoViableAltException {
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 24) {
            Token tokenMo108LT = mo108LT(1);
            match(24);
            if (this.inputState.guessing == 0) {
                return tokenMo108LT;
            }
        } else {
            if (iMo107LA != 41) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            Token tokenMo108LT2 = mo108LT(1);
            match(41);
            if (this.inputState.guessing == 0) {
                return tokenMo108LT2;
            }
        }
        return null;
    }

    public final void lexerOptionsSpec() throws NoViableAltException {
        match(14);
        while (true) {
            int iMo107LA = mo107LA(1);
            if (iMo107LA == 18) {
                match(18);
                match(15);
                BitSet bitSetCharSet = charSet();
                match(16);
                if (this.inputState.guessing == 0) {
                    this.behavior.setCharVocabulary(bitSetCharSet);
                }
            } else {
                if (iMo107LA != 24 && iMo107LA != 41) {
                    match(17);
                    return;
                }
                Token tokenM104id = m104id();
                match(15);
                Token tokenOptionValue = optionValue();
                if (this.inputState.guessing == 0) {
                    this.behavior.setGrammarOption(tokenM104id, tokenOptionValue);
                }
                match(16);
            }
        }
    }

    public final void lexerSpec(String str) throws NoViableAltException {
        Token tokenM104id;
        int iMo107LA = mo107LA(1);
        String strSuperClass = null;
        if (iMo107LA == 9) {
            Token tokenMo108LT = mo108LT(1);
            match(9);
            Token tokenM104id2 = m104id();
            if (this.inputState.guessing == 0) {
                this.antlrTool.warning("lexclass' is deprecated; use 'class X extends Lexer'", getFilename(), tokenMo108LT.getLine(), tokenMo108LT.getColumn());
            }
            tokenM104id = tokenM104id2;
        } else {
            if (iMo107LA != 10) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            match(10);
            tokenM104id = m104id();
            match(11);
            match(12);
            int iMo107LA2 = mo107LA(1);
            if (iMo107LA2 != 16) {
                if (iMo107LA2 != 27) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                strSuperClass = superClass();
            }
        }
        if (this.inputState.guessing == 0) {
            this.behavior.startLexer(getFilename(), tokenM104id, strSuperClass, str);
        }
        match(16);
        int iMo107LA3 = mo107LA(1);
        if (iMo107LA3 != 7 && iMo107LA3 != 8) {
            if (iMo107LA3 == 14) {
                lexerOptionsSpec();
            } else if (iMo107LA3 != 41 && iMo107LA3 != 23 && iMo107LA3 != 24) {
                switch (iMo107LA3) {
                    case 30:
                    case 31:
                    case 32:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        }
        if (this.inputState.guessing == 0) {
            this.behavior.endOptions();
        }
        int iMo107LA4 = mo107LA(1);
        if (iMo107LA4 != 7 && iMo107LA4 != 8) {
            if (iMo107LA4 == 23) {
                tokensSpec();
            } else if (iMo107LA4 != 24 && iMo107LA4 != 41) {
                switch (iMo107LA4) {
                    case 30:
                    case 31:
                    case 32:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        }
        int iMo107LA5 = mo107LA(1);
        if (iMo107LA5 == 7) {
            Token tokenMo108LT2 = mo108LT(1);
            match(7);
            if (this.inputState.guessing == 0) {
                this.behavior.refMemberAction(tokenMo108LT2);
                return;
            }
            return;
        }
        if (iMo107LA5 == 8 || iMo107LA5 == 24 || iMo107LA5 == 41) {
            return;
        }
        switch (iMo107LA5) {
            case 30:
            case 31:
            case 32:
                return;
            default:
                throw new NoViableAltException(mo108LT(1), getFilename());
        }
    }

    public final void notTerminal(Token token) throws NoViableAltException {
        int i = 1;
        int iMo107LA = mo107LA(1);
        if (iMo107LA != 19) {
            if (iMo107LA != 24) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            Token tokenMo108LT = mo108LT(1);
            match(24);
            int iAst_type_spec = ast_type_spec();
            if (this.inputState.guessing == 0) {
                this.behavior.refToken(null, tokenMo108LT, token, null, true, iAst_type_spec, lastInRule());
                return;
            }
            return;
        }
        Token tokenMo108LT2 = mo108LT(1);
        match(19);
        int iMo107LA2 = mo107LA(1);
        if (iMo107LA2 != 6 && iMo107LA2 != 7 && iMo107LA2 != 16 && iMo107LA2 != 19 && iMo107LA2 != 21) {
            if (iMo107LA2 == 33) {
                match(33);
                if (this.inputState.guessing == 0) {
                    i = 3;
                }
            } else if (iMo107LA2 != 39 && iMo107LA2 != 50 && iMo107LA2 != 24 && iMo107LA2 != 25 && iMo107LA2 != 27 && iMo107LA2 != 28) {
                switch (iMo107LA2) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        }
        int i2 = i;
        if (this.inputState.guessing == 0) {
            this.behavior.refCharLiteral(tokenMo108LT2, token, true, i2, lastInRule());
        }
    }

    public final Token optionValue() throws NoViableAltException {
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 6) {
            Token tokenMo108LT = mo108LT(1);
            match(6);
            if (this.inputState.guessing == 0) {
                return tokenMo108LT;
            }
        } else {
            if (iMo107LA == 24 || iMo107LA == 41) {
                return qualifiedID();
            }
            if (iMo107LA == 19) {
                Token tokenMo108LT2 = mo108LT(1);
                match(19);
                if (this.inputState.guessing == 0) {
                    return tokenMo108LT2;
                }
            } else {
                if (iMo107LA != 20) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                Token tokenMo108LT3 = mo108LT(1);
                match(20);
                if (this.inputState.guessing == 0) {
                    return tokenMo108LT3;
                }
            }
        }
        return null;
    }

    public final void parserOptionsSpec() throws NoViableAltException {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token tokenM104id = m104id();
            match(15);
            Token tokenOptionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setGrammarOption(tokenM104id, tokenOptionValue);
            }
            i = 16;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void parserSpec(String str) throws NoViableAltException {
        String strSuperClass;
        int iMo107LA;
        int iMo107LA2;
        int iMo107LA3;
        match(10);
        Token tokenM104id = m104id();
        int iMo107LA4 = mo107LA(1);
        if (iMo107LA4 == 11) {
            match(11);
            match(29);
            int iMo107LA5 = mo107LA(1);
            if (iMo107LA5 != 16) {
                if (iMo107LA5 != 27) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                strSuperClass = superClass();
            }
            if (this.inputState.guessing == 0) {
                this.behavior.startParser(getFilename(), tokenM104id, strSuperClass, str);
            }
            match(16);
            iMo107LA = mo107LA(1);
            if (iMo107LA != 7 && iMo107LA != 8) {
                if (iMo107LA != 14) {
                    parserOptionsSpec();
                } else if (iMo107LA != 41 && iMo107LA != 23 && iMo107LA != 24) {
                    switch (iMo107LA) {
                        case 30:
                        case 31:
                        case 32:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                }
            }
            if (this.inputState.guessing == 0) {
                this.behavior.endOptions();
            }
            iMo107LA2 = mo107LA(1);
            if (iMo107LA2 != 7 && iMo107LA2 != 8) {
                if (iMo107LA2 != 23) {
                    tokensSpec();
                } else if (iMo107LA2 != 24 && iMo107LA2 != 41) {
                    switch (iMo107LA2) {
                        case 30:
                        case 31:
                        case 32:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                }
            }
            iMo107LA3 = mo107LA(1);
            if (iMo107LA3 != 7) {
                Token tokenMo108LT = mo108LT(1);
                match(7);
                if (this.inputState.guessing == 0) {
                    this.behavior.refMemberAction(tokenMo108LT);
                    return;
                }
                return;
            }
            if (iMo107LA3 == 8 || iMo107LA3 == 24 || iMo107LA3 == 41) {
                return;
            }
            switch (iMo107LA3) {
                case 30:
                case 31:
                case 32:
                    return;
                default:
                    throw new NoViableAltException(mo108LT(1), getFilename());
            }
        }
        if (iMo107LA4 != 16) {
            throw new NoViableAltException(mo108LT(1), getFilename());
        }
        if (this.inputState.guessing == 0) {
            this.antlrTool.warning("use 'class X extends Parser'", getFilename(), tokenM104id.getLine(), tokenM104id.getColumn());
        }
        strSuperClass = null;
        if (this.inputState.guessing == 0) {
        }
        match(16);
        iMo107LA = mo107LA(1);
        if (iMo107LA != 7) {
            if (iMo107LA != 14) {
            }
        }
        if (this.inputState.guessing == 0) {
        }
        iMo107LA2 = mo107LA(1);
        if (iMo107LA2 != 7) {
            if (iMo107LA2 != 23) {
            }
        }
        iMo107LA3 = mo107LA(1);
        if (iMo107LA3 != 7) {
        }
    }

    public final Token qualifiedID() throws NoViableAltException {
        StringBuffer stringBuffer = new StringBuffer(30);
        Token tokenM104id = m104id();
        if (this.inputState.guessing == 0) {
            stringBuffer.append(tokenM104id.getText());
        }
        while (mo107LA(1) == 50) {
            match(50);
            tokenM104id = m104id();
            if (this.inputState.guessing == 0) {
                stringBuffer.append('.');
                stringBuffer.append(tokenM104id.getText());
            }
        }
        if (this.inputState.guessing != 0) {
            return null;
        }
        CommonToken commonToken = new CommonToken(24, stringBuffer.toString());
        commonToken.setLine(tokenM104id.getLine());
        return commonToken;
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x00c3 A[PHI: r1
      0x00c3: PHI (r1v3 antlr.Token) = (r1v2 antlr.Token), (r1v9 antlr.Token) binds: [B:50:0x00c1, B:45:0x00a5] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00fa A[PHI: r0
      0x00fa: PHI (r0v2 antlr.Token) = (r0v1 antlr.Token), (r0v6 antlr.Token) binds: [B:62:0x00f8, B:57:0x00dc] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00fc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void range(Token token) throws NoViableAltException {
        Token tokenMo108LT;
        Token token2;
        Token tokenMo108LT2;
        Token token3;
        int i = 1;
        int iMo107LA = mo107LA(1);
        if (iMo107LA != 6) {
            if (iMo107LA == 19) {
                Token tokenMo108LT3 = mo108LT(1);
                match(19);
                match(22);
                Token tokenMo108LT4 = mo108LT(1);
                match(19);
                int iMo107LA2 = mo107LA(1);
                if (iMo107LA2 != 6 && iMo107LA2 != 7 && iMo107LA2 != 16 && iMo107LA2 != 19 && iMo107LA2 != 21) {
                    if (iMo107LA2 == 33) {
                        match(33);
                        if (this.inputState.guessing == 0) {
                            i = 3;
                        }
                    } else if (iMo107LA2 != 39 && iMo107LA2 != 50 && iMo107LA2 != 24 && iMo107LA2 != 25 && iMo107LA2 != 27 && iMo107LA2 != 28) {
                        switch (iMo107LA2) {
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                                break;
                            default:
                                throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                    }
                }
                int i2 = i;
                if (this.inputState.guessing == 0) {
                    this.behavior.refCharRange(tokenMo108LT3, tokenMo108LT4, token, i2, lastInRule());
                    return;
                }
                return;
            }
            if (iMo107LA != 24) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
        }
        int iMo107LA3 = mo107LA(1);
        if (iMo107LA3 == 6) {
            tokenMo108LT = mo108LT(1);
            match(6);
            if (this.inputState.guessing == 0) {
            }
        } else {
            if (iMo107LA3 != 24) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            tokenMo108LT = mo108LT(1);
            match(24);
            token2 = this.inputState.guessing == 0 ? tokenMo108LT : null;
        }
        match(22);
        int iMo107LA4 = mo107LA(1);
        if (iMo107LA4 == 6) {
            tokenMo108LT2 = mo108LT(1);
            match(6);
            if (this.inputState.guessing == 0) {
            }
        } else {
            if (iMo107LA4 != 24) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            tokenMo108LT2 = mo108LT(1);
            match(24);
            token3 = this.inputState.guessing == 0 ? tokenMo108LT2 : null;
        }
        int iAst_type_spec = ast_type_spec();
        if (this.inputState.guessing == 0) {
            this.behavior.refTokenRange(token2, token3, token, iAst_type_spec, lastInRule());
        }
    }

    @Override // antlr.Parser
    public void reportError(RecognitionException recognitionException) {
        reportError(recognitionException, recognitionException.getErrorMessage());
    }

    public void reportError(RecognitionException recognitionException, String str) {
        this.antlrTool.error(str, recognitionException.getFilename(), recognitionException.getLine(), recognitionException.getColumn());
    }

    @Override // antlr.Parser
    public void reportError(String str) {
        this.antlrTool.error(str, getFilename(), -1, -1);
    }

    @Override // antlr.Parser
    public void reportWarning(String str) {
        this.antlrTool.warning(str, getFilename(), -1, -1);
    }

    public final void rootNode() throws NoViableAltException {
        Token tokenM104id;
        if ((mo107LA(1) == 24 || mo107LA(1) == 41) && mo107LA(2) == 36) {
            tokenM104id = m104id();
            match(36);
            if (this.inputState.guessing == 0) {
                checkForMissingEndRule(tokenM104id);
            }
        } else {
            if (!_tokenSet_7.member(mo107LA(1)) || !_tokenSet_11.member(mo107LA(2))) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            tokenM104id = null;
        }
        terminal(tokenM104id);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x003e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0080 A[PHI: r2
      0x0080: PHI (r2v14 antlr.Token) = (r2v12 antlr.Token), (r2v13 antlr.Token), (r2v16 antlr.Token) binds: [B:26:0x007e, B:23:0x006e, B:20:0x005e] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0094 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00b4 A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00c8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00f3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0125 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0195 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void rule() throws NoViableAltException {
        String text;
        int iMo107LA;
        String text2;
        int iMo107LA2;
        boolean z;
        int iMo107LA3;
        int iMo107LA4;
        int iMo107LA5;
        int iMo107LA6;
        int iMo107LA7;
        int iMo107LA8;
        Token tokenMo108LT;
        this.blockNesting = -1;
        int iMo107LA9 = mo107LA(1);
        if (iMo107LA9 == 8) {
            Token tokenMo108LT2 = mo108LT(1);
            match(8);
            text = this.inputState.guessing == 0 ? tokenMo108LT2.getText() : null;
            iMo107LA = mo107LA(1);
            if (iMo107LA == 24) {
                text2 = "public";
            }
            Token tokenM104id = m104id();
            iMo107LA2 = mo107LA(1);
            z = iMo107LA2 != 7 ? true : true;
            if (this.inputState.guessing == 0) {
            }
            iMo107LA3 = mo107LA(1);
            if (iMo107LA3 != 7) {
                switch (iMo107LA3) {
                }
            }
            iMo107LA4 = mo107LA(1);
            if (iMo107LA4 != 7) {
                switch (iMo107LA4) {
                }
            }
            iMo107LA5 = mo107LA(1);
            if (iMo107LA5 != 7) {
                if (iMo107LA5 == 37) {
                }
            }
            iMo107LA6 = mo107LA(1);
            if (iMo107LA6 != 7) {
            }
            iMo107LA7 = mo107LA(1);
            if (iMo107LA7 == 7) {
            }
            match(36);
            block();
            match(16);
            iMo107LA8 = mo107LA(1);
            if (iMo107LA8 != 1) {
                if (iMo107LA8 == 39) {
                }
            }
            if (this.inputState.guessing == 0) {
            }
        } else if (iMo107LA9 != 24 && iMo107LA9 != 41) {
            switch (iMo107LA9) {
                case 30:
                case 31:
                case 32:
                    break;
                default:
                    throw new NoViableAltException(mo108LT(1), getFilename());
            }
            iMo107LA = mo107LA(1);
            if (iMo107LA == 24 || iMo107LA == 41) {
                text2 = "public";
            } else {
                switch (iMo107LA) {
                    case 30:
                        tokenMo108LT = mo108LT(1);
                        match(30);
                        if (this.inputState.guessing == 0) {
                            text2 = tokenMo108LT.getText();
                            break;
                        }
                        break;
                    case 31:
                        tokenMo108LT = mo108LT(1);
                        match(31);
                        if (this.inputState.guessing == 0) {
                        }
                        break;
                    case 32:
                        tokenMo108LT = mo108LT(1);
                        match(32);
                        if (this.inputState.guessing == 0) {
                        }
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            Token tokenM104id2 = m104id();
            iMo107LA2 = mo107LA(1);
            if (iMo107LA2 != 7 && iMo107LA2 != 14) {
                switch (iMo107LA2) {
                    case 33:
                        match(33);
                        z = this.inputState.guessing != 0;
                        break;
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                        z = true;
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            if (this.inputState.guessing == 0) {
                this.behavior.defineRuleName(tokenM104id2, text2, z, text);
            }
            iMo107LA3 = mo107LA(1);
            if (iMo107LA3 != 7 && iMo107LA3 != 14) {
                switch (iMo107LA3) {
                    case 34:
                        Token tokenMo108LT3 = mo108LT(1);
                        match(34);
                        if (this.inputState.guessing == 0) {
                            this.behavior.refArgAction(tokenMo108LT3);
                        }
                        break;
                    case 35:
                    case 36:
                    case 37:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            iMo107LA4 = mo107LA(1);
            if (iMo107LA4 != 7 && iMo107LA4 != 14) {
                switch (iMo107LA4) {
                    case 35:
                        match(35);
                        Token tokenMo108LT4 = mo108LT(1);
                        match(34);
                        if (this.inputState.guessing == 0) {
                            this.behavior.refReturnAction(tokenMo108LT4);
                        }
                        break;
                    case 36:
                    case 37:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            iMo107LA5 = mo107LA(1);
            if (iMo107LA5 != 7 && iMo107LA5 != 14 && iMo107LA5 != 36) {
                if (iMo107LA5 == 37) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                throwsSpec();
            }
            iMo107LA6 = mo107LA(1);
            if (iMo107LA6 != 7) {
                if (iMo107LA6 == 14) {
                    ruleOptionsSpec();
                } else if (iMo107LA6 != 36) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            iMo107LA7 = mo107LA(1);
            if (iMo107LA7 == 7) {
                Token tokenMo108LT5 = mo108LT(1);
                match(7);
                if (this.inputState.guessing == 0) {
                    this.behavior.refInitAction(tokenMo108LT5);
                }
            } else if (iMo107LA7 != 36) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            match(36);
            block();
            match(16);
            iMo107LA8 = mo107LA(1);
            if (iMo107LA8 != 1 && iMo107LA8 != 24) {
                if (iMo107LA8 == 39) {
                    if (iMo107LA8 != 41) {
                        switch (iMo107LA8) {
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                                break;
                            default:
                                switch (iMo107LA8) {
                                    case 30:
                                    case 31:
                                    case 32:
                                        break;
                                    default:
                                        throw new NoViableAltException(mo108LT(1), getFilename());
                                }
                                break;
                        }
                    }
                } else {
                    exceptionGroup();
                }
            }
            if (this.inputState.guessing == 0) {
                this.behavior.endRule(tokenM104id2.getText());
                return;
            }
            return;
        }
        iMo107LA = mo107LA(1);
        if (iMo107LA == 24) {
        }
        Token tokenM104id22 = m104id();
        iMo107LA2 = mo107LA(1);
        if (iMo107LA2 != 7) {
        }
        if (this.inputState.guessing == 0) {
        }
        iMo107LA3 = mo107LA(1);
        if (iMo107LA3 != 7) {
        }
        iMo107LA4 = mo107LA(1);
        if (iMo107LA4 != 7) {
        }
        iMo107LA5 = mo107LA(1);
        if (iMo107LA5 != 7) {
        }
        iMo107LA6 = mo107LA(1);
        if (iMo107LA6 != 7) {
        }
        iMo107LA7 = mo107LA(1);
        if (iMo107LA7 == 7) {
        }
        match(36);
        block();
        match(16);
        iMo107LA8 = mo107LA(1);
        if (iMo107LA8 != 1) {
        }
        if (this.inputState.guessing == 0) {
        }
    }

    public final void ruleOptionsSpec() throws NoViableAltException {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token tokenM104id = m104id();
            match(15);
            Token tokenOptionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setRuleOption(tokenM104id, tokenOptionValue);
            }
            i = 16;
        }
    }

    public final void rules() throws NoViableAltException {
        int i = 0;
        while (_tokenSet_0.member(mo107LA(1)) && _tokenSet_1.member(mo107LA(2))) {
            rule();
            i++;
        }
        if (i < 1) {
            throw new NoViableAltException(mo108LT(1), getFilename());
        }
    }

    public final BitSet setBlockElement() throws NoViableAltException {
        BitSet bitSetM114of;
        int i;
        Token tokenMo108LT = mo108LT(1);
        match(19);
        if (this.inputState.guessing == 0) {
            i = ANTLRLexer.tokenTypeForCharLiteral(tokenMo108LT.getText());
            bitSetM114of = BitSet.m114of(i);
        } else {
            bitSetM114of = null;
            i = 0;
        }
        int iMo107LA = mo107LA(1);
        if (iMo107LA != 16 && iMo107LA != 21) {
            if (iMo107LA != 22) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            match(22);
            Token tokenMo108LT2 = mo108LT(1);
            match(19);
            if (this.inputState.guessing == 0) {
                int i2 = ANTLRLexer.tokenTypeForCharLiteral(tokenMo108LT2.getText());
                if (i2 < i) {
                    this.antlrTool.error("Malformed range line ", getFilename(), tokenMo108LT.getLine(), tokenMo108LT.getColumn());
                }
                for (int i3 = i + 1; i3 <= i2; i3++) {
                    bitSetM114of.add(i3);
                }
            }
        }
        return bitSetM114of;
    }

    public final void subruleOptionsSpec() throws NoViableAltException {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token tokenM104id = m104id();
            match(15);
            Token tokenOptionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setSubruleOption(tokenM104id, tokenOptionValue);
            }
            i = 16;
        }
    }

    public final String superClass() {
        match(27);
        String strStripFrontBack = this.inputState.guessing == 0 ? StringUtils.stripFrontBack(mo108LT(1).getText(), "\"", "\"") : null;
        match(6);
        match(28);
        return strStripFrontBack;
    }

    public final void terminal(Token token) throws NoViableAltException {
        Token tokenMo108LT;
        int i = 1;
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 6) {
            Token tokenMo108LT2 = mo108LT(1);
            match(6);
            int iAst_type_spec = ast_type_spec();
            if (this.inputState.guessing == 0) {
                this.behavior.refStringLiteral(tokenMo108LT2, token, iAst_type_spec, lastInRule());
                return;
            }
            return;
        }
        if (iMo107LA == 19) {
            Token tokenMo108LT3 = mo108LT(1);
            match(19);
            int iMo107LA2 = mo107LA(1);
            if (iMo107LA2 != 6 && iMo107LA2 != 7 && iMo107LA2 != 16 && iMo107LA2 != 19 && iMo107LA2 != 21) {
                if (iMo107LA2 == 33) {
                    match(33);
                    if (this.inputState.guessing == 0) {
                        i = 3;
                    }
                } else if (iMo107LA2 != 39 && iMo107LA2 != 50 && iMo107LA2 != 24 && iMo107LA2 != 25 && iMo107LA2 != 27 && iMo107LA2 != 28) {
                    switch (iMo107LA2) {
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                }
            }
            int i2 = i;
            if (this.inputState.guessing == 0) {
                this.behavior.refCharLiteral(tokenMo108LT3, token, false, i2, lastInRule());
                return;
            }
            return;
        }
        if (iMo107LA != 24) {
            if (iMo107LA != 50) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            Token tokenMo108LT4 = mo108LT(1);
            match(50);
            int iAst_type_spec2 = ast_type_spec();
            if (this.inputState.guessing == 0) {
                this.behavior.refWildcard(tokenMo108LT4, token, iAst_type_spec2);
                return;
            }
            return;
        }
        Token tokenMo108LT5 = mo108LT(1);
        match(24);
        int iAst_type_spec3 = ast_type_spec();
        int iMo107LA3 = mo107LA(1);
        if (iMo107LA3 == 6 || iMo107LA3 == 7 || iMo107LA3 == 16 || iMo107LA3 == 19 || iMo107LA3 == 21) {
            tokenMo108LT = null;
        } else {
            if (iMo107LA3 == 34) {
                tokenMo108LT = mo108LT(1);
                match(34);
                if (this.inputState.guessing != 0) {
                }
            } else if (iMo107LA3 != 39 && iMo107LA3 != 50 && iMo107LA3 != 24 && iMo107LA3 != 25 && iMo107LA3 != 27 && iMo107LA3 != 28) {
                switch (iMo107LA3) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            tokenMo108LT = null;
        }
        Token token2 = tokenMo108LT;
        if (this.inputState.guessing == 0) {
            this.behavior.refToken(null, tokenMo108LT5, token, token2, false, iAst_type_spec3, lastInRule());
        }
    }

    public final void throwsSpec() throws NoViableAltException {
        match(37);
        String text = this.inputState.guessing == 0 ? m104id().getText() : null;
        while (mo107LA(1) == 38) {
            match(38);
            Token tokenM104id = m104id();
            if (this.inputState.guessing == 0) {
                text = C0000a.m0a(tokenM104id, C0000a.m9b(text, ","));
            }
        }
        if (this.inputState.guessing == 0) {
            this.behavior.setUserExceptions(text);
        }
    }

    public final void tokensSpec() throws NoViableAltException {
        Token tokenMo108LT;
        match(23);
        int i = 0;
        Token tokenMo108LT2 = null;
        while (true) {
            if (mo107LA(1) != 6 && mo107LA(1) != 24) {
                if (i < 1) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                match(17);
                return;
            }
            int iMo107LA = mo107LA(1);
            if (iMo107LA == 6) {
                tokenMo108LT = mo108LT(1);
                match(6);
                if (this.inputState.guessing == 0) {
                    this.behavior.defineToken(null, tokenMo108LT);
                }
                int iMo107LA2 = mo107LA(1);
                if (iMo107LA2 == 16) {
                    continue;
                } else {
                    if (iMo107LA2 != 25) {
                        throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                    tokensSpecOptions(tokenMo108LT);
                }
            } else {
                if (iMo107LA != 24) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                if (this.inputState.guessing == 0) {
                    tokenMo108LT2 = null;
                }
                tokenMo108LT = mo108LT(1);
                match(24);
                int iMo107LA3 = mo107LA(1);
                if (iMo107LA3 == 15) {
                    match(15);
                    tokenMo108LT2 = mo108LT(1);
                    match(6);
                } else if (iMo107LA3 != 16 && iMo107LA3 != 25) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                if (this.inputState.guessing == 0) {
                    this.behavior.defineToken(tokenMo108LT, tokenMo108LT2);
                }
                int iMo107LA4 = mo107LA(1);
                if (iMo107LA4 == 16) {
                    continue;
                } else {
                    if (iMo107LA4 != 25) {
                        throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                    tokensSpecOptions(tokenMo108LT);
                }
            }
            match(16);
            i++;
        }
    }

    public final void tokensSpecOptions(Token token) throws NoViableAltException {
        Token tokenM104id;
        Token tokenOptionValue;
        match(25);
        Token tokenM104id2 = m104id();
        match(15);
        Token tokenOptionValue2 = optionValue();
        if (this.inputState.guessing != 0) {
            this.behavior.refTokensSpecElementOption(token, tokenM104id2, tokenOptionValue2);
            while (mo107LA(1) == 16) {
                match(16);
                tokenM104id = m104id();
                match(15);
                tokenOptionValue = optionValue();
                if (this.inputState.guessing == 0) {
                }
            }
            match(26);
            return;
        }
        this.behavior.refTokensSpecElementOption(token, tokenM104id, tokenOptionValue);
    }

    public final void tree() throws NoViableAltException {
        Token tokenMo108LT = mo108LT(1);
        match(44);
        if (this.inputState.guessing == 0) {
            this.behavior.beginTree(tokenMo108LT);
        }
        rootNode();
        if (this.inputState.guessing == 0) {
            this.behavior.beginChildList();
        }
        int i = 0;
        while (_tokenSet_2.member(mo107LA(1))) {
            element();
            i++;
        }
        if (i < 1) {
            throw new NoViableAltException(mo108LT(1), getFilename());
        }
        if (this.inputState.guessing == 0) {
            this.behavior.endChildList();
        }
        match(28);
        if (this.inputState.guessing == 0) {
            this.behavior.endTree();
        }
    }

    public final void treeParserOptionsSpec() throws NoViableAltException {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token tokenM104id = m104id();
            match(15);
            Token tokenOptionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setGrammarOption(tokenM104id, tokenOptionValue);
            }
            i = 16;
        }
    }

    public final void treeParserSpec(String str) throws NoViableAltException {
        String strSuperClass;
        match(10);
        Token tokenM104id = m104id();
        match(11);
        match(13);
        int iMo107LA = mo107LA(1);
        if (iMo107LA == 16) {
            strSuperClass = null;
        } else {
            if (iMo107LA != 27) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            strSuperClass = superClass();
        }
        if (this.inputState.guessing == 0) {
            this.behavior.startTreeWalker(getFilename(), tokenM104id, strSuperClass, str);
        }
        match(16);
        int iMo107LA2 = mo107LA(1);
        if (iMo107LA2 != 7 && iMo107LA2 != 8) {
            if (iMo107LA2 == 14) {
                treeParserOptionsSpec();
            } else if (iMo107LA2 != 41 && iMo107LA2 != 23 && iMo107LA2 != 24) {
                switch (iMo107LA2) {
                    case 30:
                    case 31:
                    case 32:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        }
        if (this.inputState.guessing == 0) {
            this.behavior.endOptions();
        }
        int iMo107LA3 = mo107LA(1);
        if (iMo107LA3 != 7 && iMo107LA3 != 8) {
            if (iMo107LA3 == 23) {
                tokensSpec();
            } else if (iMo107LA3 != 24 && iMo107LA3 != 41) {
                switch (iMo107LA3) {
                    case 30:
                    case 31:
                    case 32:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        }
        int iMo107LA4 = mo107LA(1);
        if (iMo107LA4 == 7) {
            Token tokenMo108LT = mo108LT(1);
            match(7);
            if (this.inputState.guessing == 0) {
                this.behavior.refMemberAction(tokenMo108LT);
                return;
            }
            return;
        }
        if (iMo107LA4 == 8 || iMo107LA4 == 24 || iMo107LA4 == 41) {
            return;
        }
        switch (iMo107LA4) {
            case 30:
            case 31:
            case 32:
                return;
            default:
                throw new NoViableAltException(mo108LT(1), getFilename());
        }
    }
}
