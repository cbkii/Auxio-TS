package antlr;

import antlr.collections.impl.BitSet;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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

    /* JADX WARN: Removed duplicated region for block: B:28:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0063 A[LOOP:0: B:30:0x0057->B:32:0x0063, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x006d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void alternative() {
        boolean z;
        int mo107LA;
        int mo107LA2 = mo107LA(1);
        if (mo107LA2 != 6 && mo107LA2 != 7 && mo107LA2 != 16 && mo107LA2 != 19 && mo107LA2 != 21 && mo107LA2 != 24) {
            if (mo107LA2 == 33) {
                match(33);
                if (this.inputState.guessing == 0) {
                    z = false;
                    if (this.inputState.guessing == 0) {
                    }
                    while (_tokenSet_2.member(mo107LA(1))) {
                    }
                    mo107LA = mo107LA(1);
                    if (mo107LA != 16) {
                        if (mo107LA == 39) {
                        }
                    }
                    if (this.inputState.guessing == 0) {
                    }
                }
            } else if (mo107LA2 != 39 && mo107LA2 != 50 && mo107LA2 != 27 && mo107LA2 != 28) {
                switch (mo107LA2) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
                if (this.inputState.guessing == 0) {
                    this.behavior.beginAlt(z);
                }
                while (_tokenSet_2.member(mo107LA(1))) {
                    element();
                }
                mo107LA = mo107LA(1);
                if (mo107LA != 16 && mo107LA != 21 && mo107LA != 28) {
                    if (mo107LA == 39) {
                        throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                    exceptionSpecNoLabel();
                }
                if (this.inputState.guessing == 0) {
                    this.behavior.endAlt();
                    return;
                }
                return;
            }
        }
        z = true;
        if (this.inputState.guessing == 0) {
        }
        while (_tokenSet_2.member(mo107LA(1))) {
        }
        mo107LA = mo107LA(1);
        if (mo107LA != 16) {
        }
        if (this.inputState.guessing == 0) {
        }
    }

    public final int ast_type_spec() {
        int mo107LA = mo107LA(1);
        if (mo107LA == 6 || mo107LA == 7 || mo107LA == 16 || mo107LA == 19 || mo107LA == 21 || mo107LA == 39 || mo107LA == 24 || mo107LA == 25 || mo107LA == 27 || mo107LA == 28) {
            return 1;
        }
        if (mo107LA == 33) {
            match(33);
            return this.inputState.guessing == 0 ? 3 : 1;
        }
        if (mo107LA == 34) {
            return 1;
        }
        if (mo107LA == 49) {
            match(49);
            return this.inputState.guessing == 0 ? 2 : 1;
        }
        if (mo107LA == 50) {
            return 1;
        }
        switch (mo107LA) {
            case 41:
            case 42:
            case 43:
            case 44:
                return 1;
            default:
                throw new NoViableAltException(mo108LT(1), getFilename());
        }
    }

    public final void block() {
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

    public final BitSet charSet() {
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

    /* JADX WARN: Can't wrap try/catch for region: R(12:19|20|(4:24|(1:26)(3:32|(1:56)(5:38|39|40|41|42)|(1:44)(2:45|(2:52|53)(1:51)))|27|(2:29|30)(1:31))|62|63|64|(1:(1:67)(2:70|71))(1:72)|68|69|(0)(0)|27|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x009b, code lost:
    
        r11 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00a8 A[Catch: RecognitionException -> 0x0136, TryCatch #2 {RecognitionException -> 0x0136, blocks: (B:3:0x0008, B:4:0x000c, B:5:0x000f, B:6:0x012a, B:7:0x0135, B:9:0x0013, B:11:0x0021, B:12:0x0026, B:13:0x002a, B:14:0x002d, B:15:0x011e, B:16:0x0129, B:17:0x0031, B:19:0x003e, B:20:0x0044, B:22:0x0051, B:26:0x00a8, B:27:0x0101, B:29:0x010a, B:32:0x00ac, B:34:0x00b2, B:36:0x00b8, B:38:0x00be, B:42:0x00da, B:44:0x00e8, B:45:0x00ec, B:47:0x00f2, B:49:0x00f8, B:51:0x00fe, B:52:0x0110, B:53:0x011d, B:57:0x0057, B:59:0x005d, B:62:0x0066, B:69:0x009c), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x010a A[Catch: RecognitionException -> 0x0136, TryCatch #2 {RecognitionException -> 0x0136, blocks: (B:3:0x0008, B:4:0x000c, B:5:0x000f, B:6:0x012a, B:7:0x0135, B:9:0x0013, B:11:0x0021, B:12:0x0026, B:13:0x002a, B:14:0x002d, B:15:0x011e, B:16:0x0129, B:17:0x0031, B:19:0x003e, B:20:0x0044, B:22:0x0051, B:26:0x00a8, B:27:0x0101, B:29:0x010a, B:32:0x00ac, B:34:0x00b2, B:36:0x00b8, B:38:0x00be, B:42:0x00da, B:44:0x00e8, B:45:0x00ec, B:47:0x00f2, B:49:0x00f8, B:51:0x00fe, B:52:0x0110, B:53:0x011d, B:57:0x0057, B:59:0x005d, B:62:0x0066, B:69:0x009c), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x017b A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00ac A[Catch: RecognitionException -> 0x0136, TryCatch #2 {RecognitionException -> 0x0136, blocks: (B:3:0x0008, B:4:0x000c, B:5:0x000f, B:6:0x012a, B:7:0x0135, B:9:0x0013, B:11:0x0021, B:12:0x0026, B:13:0x002a, B:14:0x002d, B:15:0x011e, B:16:0x0129, B:17:0x0031, B:19:0x003e, B:20:0x0044, B:22:0x0051, B:26:0x00a8, B:27:0x0101, B:29:0x010a, B:32:0x00ac, B:34:0x00b2, B:36:0x00b8, B:38:0x00be, B:42:0x00da, B:44:0x00e8, B:45:0x00ec, B:47:0x00f2, B:49:0x00f8, B:51:0x00fe, B:52:0x0110, B:53:0x011d, B:57:0x0057, B:59:0x005d, B:62:0x0066, B:69:0x009c), top: B:2:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void classDef() {
        String sb;
        String str;
        boolean z;
        boolean z2;
        try {
            switch (mo107LA(1)) {
                case 7:
                    Token mo108LT = mo108LT(1);
                    match(7);
                    if (this.inputState.guessing == 0) {
                        this.behavior.refPreambleAction(mo108LT);
                        break;
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
                    Token mo108LT2 = mo108LT(1);
                    match(8);
                    if (this.inputState.guessing == 0) {
                        str = mo108LT2.getText();
                        if ((mo107LA(1) != 9 && mo107LA(1) != 10) || (mo107LA(2) != 24 && mo107LA(2) != 41)) {
                            z = false;
                            if (z) {
                                if (mo107LA(1) == 10 && (mo107LA(2) == 24 || mo107LA(2) == 41)) {
                                    int mark = mark();
                                    this.inputState.guessing++;
                                    try {
                                        match(10);
                                        m104id();
                                        match(11);
                                        match(13);
                                        z2 = true;
                                    } catch (RecognitionException unused) {
                                        z2 = false;
                                    }
                                    rewind(mark);
                                    this.inputState.guessing--;
                                } else {
                                    z2 = false;
                                }
                                if (z2) {
                                    treeParserSpec(str);
                                } else {
                                    if (mo107LA(1) != 10 || (mo107LA(2) != 24 && mo107LA(2) != 41)) {
                                        throw new NoViableAltException(mo108LT(1), getFilename());
                                    }
                                    parserSpec(str);
                                }
                            } else {
                                lexerSpec(str);
                            }
                            rules();
                            if (this.inputState.guessing != 0) {
                                this.behavior.endGrammar();
                                return;
                            }
                            return;
                        }
                        int mark2 = mark();
                        this.inputState.guessing++;
                        int mo107LA = mo107LA(1);
                        if (mo107LA == 9) {
                            match(9);
                        } else {
                            if (mo107LA != 10) {
                                throw new NoViableAltException(mo108LT(1), getFilename());
                            }
                            match(10);
                            m104id();
                            match(11);
                            match(12);
                        }
                        z = true;
                        rewind(mark2);
                        this.inputState.guessing--;
                        if (z) {
                        }
                        rules();
                        if (this.inputState.guessing != 0) {
                        }
                    }
                    break;
                case 9:
                case 10:
                    str = null;
                    if (mo107LA(1) != 9) {
                        z = false;
                        if (z) {
                        }
                        rules();
                        if (this.inputState.guessing != 0) {
                        }
                        break;
                    }
                    z = false;
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
                sb = "JAVADOC comments may only prefix rules and grammars";
            } else {
                StringBuilder m5a = C0000a.m5a("rule classDef trapped:\n");
                m5a.append(e.toString());
                sb = m5a.toString();
            }
            reportError(e, sb);
            this.behavior.abortGrammar();
            boolean z3 = true;
            while (z3) {
                consume();
                int mo107LA2 = mo107LA(1);
                if (mo107LA2 == 1 || mo107LA2 == 9 || mo107LA2 == 10) {
                    z3 = false;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0045, code lost:
    
        if (r12.inputState.guessing == 0) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0062, code lost:
    
        r12.behavior.refInitAction(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0060, code lost:
    
        if (r12.inputState.guessing == 0) goto L22;
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0167  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void ebnf(Token token, boolean z) {
        Token mo108LT;
        int mo107LA;
        int mo107LA2;
        int mo107LA3;
        Token mo108LT2 = mo108LT(1);
        match(27);
        if (this.inputState.guessing == 0) {
            this.behavior.beginSubRule(token, mo108LT2, z);
        }
        if (mo107LA(1) == 14) {
            subruleOptionsSpec();
            int mo107LA4 = mo107LA(1);
            if (mo107LA4 == 7) {
                mo108LT = mo108LT(1);
                match(7);
            } else if (mo107LA4 != 36) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            match(36);
            block();
            match(28);
            mo107LA = mo107LA(1);
            if (mo107LA != 6 && mo107LA != 7 && mo107LA != 16 && mo107LA != 19 && mo107LA != 21 && mo107LA != 33 && mo107LA != 39 && mo107LA != 50 && mo107LA != 24 && mo107LA != 25 && mo107LA != 27 && mo107LA != 28) {
                switch (mo107LA) {
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
                            break;
                        }
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
                if (this.inputState.guessing != 0) {
                    this.behavior.endSubRule();
                    return;
                }
                return;
            }
            mo107LA2 = mo107LA(1);
            if (mo107LA2 != 6 && mo107LA2 != 7 && mo107LA2 != 16 && mo107LA2 != 19 && mo107LA2 != 21 && mo107LA2 != 33 && mo107LA2 != 39 && mo107LA2 != 50 && mo107LA2 != 24 && mo107LA2 != 25 && mo107LA2 != 27 && mo107LA2 != 28) {
                switch (mo107LA2) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        break;
                    case 45:
                        match(45);
                        if (this.inputState.guessing == 0) {
                            this.behavior.optionalSubRule();
                            break;
                        }
                        break;
                    case 46:
                        match(46);
                        if (this.inputState.guessing == 0) {
                            this.behavior.zeroOrMoreSubRule();
                            break;
                        }
                        break;
                    case 47:
                        match(47);
                        if (this.inputState.guessing == 0) {
                            this.behavior.oneOrMoreSubRule();
                            break;
                        }
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            mo107LA3 = mo107LA(1);
            if (mo107LA3 != 6 && mo107LA3 != 7 && mo107LA3 != 16 && mo107LA3 != 19 && mo107LA3 != 21) {
                if (mo107LA3 != 33) {
                    match(33);
                    if (this.inputState.guessing == 0) {
                        this.behavior.noASTSubRule();
                    }
                } else if (mo107LA3 != 39 && mo107LA3 != 50 && mo107LA3 != 24 && mo107LA3 != 25 && mo107LA3 != 27 && mo107LA3 != 28) {
                    switch (mo107LA3) {
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
            }
        } else if (mo107LA(1) == 7 && mo107LA(2) == 36) {
            mo108LT = mo108LT(1);
            match(7);
        } else {
            if (!_tokenSet_9.member(mo107LA(1)) || !_tokenSet_10.member(mo107LA(2))) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            block();
            match(28);
            mo107LA = mo107LA(1);
            if (mo107LA != 6) {
                switch (mo107LA) {
                }
                if (this.inputState.guessing != 0) {
                }
            }
            mo107LA2 = mo107LA(1);
            if (mo107LA2 != 6) {
                switch (mo107LA2) {
                }
            }
            mo107LA3 = mo107LA(1);
            if (mo107LA3 != 6) {
                if (mo107LA3 != 33) {
                }
            }
            if (this.inputState.guessing != 0) {
            }
        }
    }

    public final void element() {
        elementNoOptionSpec();
        int mo107LA = mo107LA(1);
        if (mo107LA == 6 || mo107LA == 7 || mo107LA == 16 || mo107LA == 19 || mo107LA == 21 || mo107LA == 39 || mo107LA == 50 || mo107LA == 24) {
            return;
        }
        if (mo107LA == 25) {
            elementOptionSpec();
        } else {
            if (mo107LA == 27 || mo107LA == 28) {
                return;
            }
            switch (mo107LA) {
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

    /* JADX WARN: Removed duplicated region for block: B:210:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:212:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void elementNoOptionSpec() {
        Token token;
        Token token2;
        Token token3;
        Token token4;
        Token token5;
        Token token6;
        Token m104id;
        boolean z = true;
        int mo107LA = mo107LA(1);
        if (mo107LA == 7) {
            Token mo108LT = mo108LT(1);
            match(7);
            if (this.inputState.guessing == 0) {
                this.behavior.refAction(mo108LT);
                return;
            }
            return;
        }
        if (mo107LA == 43) {
            Token mo108LT2 = mo108LT(1);
            match(43);
            if (this.inputState.guessing == 0) {
                this.behavior.refSemPred(mo108LT2);
                return;
            }
            return;
        }
        if (mo107LA == 44) {
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
                m104id = m104id();
                match(36);
                if (this.inputState.guessing == 0) {
                    checkForMissingEndRule(m104id);
                }
            } else {
                if (!_tokenSet_4.member(mo107LA(1)) || !_tokenSet_6.member(mo107LA(2))) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                m104id = null;
            }
            int mo107LA2 = mo107LA(1);
            if (mo107LA2 == 27) {
                z = false;
            } else if (mo107LA2 == 41) {
                Token mo108LT3 = mo108LT(1);
                match(41);
                int mo107LA3 = mo107LA(1);
                if (mo107LA3 != 6 && mo107LA3 != 7 && mo107LA3 != 16 && mo107LA3 != 19 && mo107LA3 != 21 && mo107LA3 != 39 && mo107LA3 != 50 && mo107LA3 != 24 && mo107LA3 != 25 && mo107LA3 != 27 && mo107LA3 != 28 && mo107LA3 != 33) {
                    if (mo107LA3 != 34) {
                        switch (mo107LA3) {
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                                break;
                            default:
                                throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                    } else {
                        Token mo108LT4 = mo108LT(1);
                        match(34);
                        if (this.inputState.guessing == 0) {
                            token7 = mo108LT4;
                        }
                    }
                }
                int mo107LA4 = mo107LA(1);
                if (mo107LA4 != 6 && mo107LA4 != 7 && mo107LA4 != 16 && mo107LA4 != 19 && mo107LA4 != 21) {
                    if (mo107LA4 == 33) {
                        match(33);
                        if (this.inputState.guessing == 0) {
                            i = 3;
                        }
                    } else if (mo107LA4 != 39 && mo107LA4 != 50 && mo107LA4 != 24 && mo107LA4 != 25 && mo107LA4 != 27 && mo107LA4 != 28) {
                        switch (mo107LA4) {
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
                token6 = m104id;
                token3 = mo108LT3;
                token4 = token7;
                token5 = null;
            } else {
                if (mo107LA2 != 42) {
                    if ((mo107LA(1) == 6 || mo107LA(1) == 19 || mo107LA(1) == 24) && mo107LA(2) == 22) {
                        range(m104id);
                        return;
                    } else {
                        if (!_tokenSet_7.member(mo107LA(1)) || !_tokenSet_8.member(mo107LA(2))) {
                            throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                        terminal(m104id);
                        return;
                    }
                }
                match(42);
                int mo107LA5 = mo107LA(1);
                if (mo107LA5 == 19 || mo107LA5 == 24) {
                    notTerminal(m104id);
                    return;
                } else if (mo107LA5 != 27) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            ebnf(m104id, z);
            return;
        }
        Token m104id2 = m104id();
        match(15);
        if ((mo107LA(1) == 24 || mo107LA(1) == 41) && mo107LA(2) == 36) {
            Token m104id3 = m104id();
            match(36);
            if (this.inputState.guessing == 0) {
                checkForMissingEndRule(m104id3);
            }
            token = m104id3;
        } else {
            if ((mo107LA(1) != 24 && mo107LA(1) != 41) || !_tokenSet_3.member(mo107LA(2))) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            token = null;
        }
        int mo107LA6 = mo107LA(1);
        if (mo107LA6 == 24) {
            Token mo108LT5 = mo108LT(1);
            match(24);
            int mo107LA7 = mo107LA(1);
            if (mo107LA7 != 6 && mo107LA7 != 7 && mo107LA7 != 16 && mo107LA7 != 19 && mo107LA7 != 21) {
                if (mo107LA7 == 34) {
                    Token mo108LT6 = mo108LT(1);
                    match(34);
                    if (this.inputState.guessing == 0) {
                        token2 = mo108LT6;
                        if (this.inputState.guessing == 0) {
                        }
                    }
                } else if (mo107LA7 != 39 && mo107LA7 != 50 && mo107LA7 != 24 && mo107LA7 != 25 && mo107LA7 != 27 && mo107LA7 != 28) {
                    switch (mo107LA7) {
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                    if (this.inputState.guessing == 0) {
                        this.behavior.refToken(m104id2, mo108LT5, token, token2, false, 1, lastInRule());
                        return;
                    }
                    return;
                }
            }
            token2 = null;
            if (this.inputState.guessing == 0) {
            }
        } else {
            if (mo107LA6 != 41) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            Token mo108LT7 = mo108LT(1);
            match(41);
            int mo107LA8 = mo107LA(1);
            if (mo107LA8 != 6 && mo107LA8 != 7 && mo107LA8 != 16 && mo107LA8 != 19 && mo107LA8 != 21 && mo107LA8 != 39 && mo107LA8 != 50 && mo107LA8 != 24 && mo107LA8 != 25 && mo107LA8 != 27 && mo107LA8 != 28 && mo107LA8 != 33) {
                if (mo107LA8 != 34) {
                    switch (mo107LA8) {
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                            break;
                        default:
                            throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                } else {
                    Token mo108LT8 = mo108LT(1);
                    match(34);
                    if (this.inputState.guessing == 0) {
                        token7 = mo108LT8;
                    }
                }
            }
            int mo107LA9 = mo107LA(1);
            if (mo107LA9 != 6 && mo107LA9 != 7 && mo107LA9 != 16 && mo107LA9 != 19 && mo107LA9 != 21) {
                if (mo107LA9 == 33) {
                    match(33);
                    if (this.inputState.guessing == 0) {
                        i = 3;
                    }
                } else if (mo107LA9 != 39 && mo107LA9 != 50 && mo107LA9 != 24 && mo107LA9 != 25 && mo107LA9 != 27 && mo107LA9 != 28) {
                    switch (mo107LA9) {
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
            token3 = mo108LT7;
            token4 = token7;
            token5 = m104id2;
            token6 = token;
        }
        this.behavior.refRule(token5, token3, token6, token4, i);
    }

    public final void elementOptionSpec() {
        Token m104id;
        Token optionValue;
        match(25);
        Token m104id2 = m104id();
        match(15);
        Token optionValue2 = optionValue();
        if (this.inputState.guessing != 0) {
            this.behavior.refElementOption(m104id2, optionValue2);
            while (mo107LA(1) == 16) {
                match(16);
                m104id = m104id();
                match(15);
                optionValue = optionValue();
                if (this.inputState.guessing == 0) {
                }
            }
            match(26);
            return;
        }
        this.behavior.refElementOption(m104id, optionValue);
    }

    public final void exceptionGroup() {
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
        Token mo108LT = mo108LT(1);
        match(34);
        Token mo108LT2 = mo108LT(1);
        match(7);
        if (this.inputState.guessing == 0) {
            this.behavior.refExceptionHandler(mo108LT, mo108LT2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0036, code lost:
    
        if (r3.inputState.guessing == 0) goto L17;
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004d A[LOOP:0: B:19:0x0045->B:21:0x004d, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0039 A[FALL_THROUGH] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void exceptionSpec() {
        Token token;
        match(39);
        int mo107LA = mo107LA(1);
        if (mo107LA != 1 && mo107LA != 24) {
            if (mo107LA != 34) {
                switch (mo107LA) {
                    default:
                        switch (mo107LA) {
                            default:
                                switch (mo107LA) {
                                    case 39:
                                    case 40:
                                    case 41:
                                        break;
                                    default:
                                        throw new NoViableAltException(mo108LT(1), getFilename());
                                }
                                if (this.inputState.guessing == 0) {
                                    this.behavior.beginExceptionSpec(token);
                                }
                                while (mo107LA(1) == 40) {
                                    exceptionHandler();
                                }
                                if (this.inputState.guessing == 0) {
                                    this.behavior.endExceptionSpec();
                                    return;
                                }
                                return;
                            case 30:
                            case 31:
                            case 32:
                                token = null;
                                if (this.inputState.guessing == 0) {
                                }
                                while (mo107LA(1) == 40) {
                                }
                                if (this.inputState.guessing == 0) {
                                }
                                break;
                        }
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        break;
                }
            } else {
                token = mo108LT(1);
                match(34);
            }
        }
        token = null;
        if (this.inputState.guessing == 0) {
        }
        while (mo107LA(1) == 40) {
        }
        if (this.inputState.guessing == 0) {
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

    public final void fileOptionsSpec() {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token m104id = m104id();
            match(15);
            Token optionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setFileOption(m104id, optionValue, getInputState().filename);
            }
            i = 16;
        }
    }

    public final void grammar() {
        Token token = null;
        while (mo107LA(1) == 5) {
            try {
                if (this.inputState.guessing == 0) {
                    token = null;
                }
                match(5);
                int mo107LA = mo107LA(1);
                if (mo107LA == 6) {
                    token = mo108LT(1);
                    match(6);
                } else if (mo107LA != 7) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                Token mo108LT = mo108LT(1);
                match(7);
                if (this.inputState.guessing == 0) {
                    this.behavior.refHeaderAction(token, mo108LT);
                }
            } catch (RecognitionException e) {
                if (this.inputState.guessing != 0) {
                    throw e;
                }
                StringBuilder m5a = C0000a.m5a("rule grammar trapped:\n");
                m5a.append(e.toString());
                reportError(e, m5a.toString());
                consumeUntil(1);
                return;
            }
        }
        int mo107LA2 = mo107LA(1);
        if (mo107LA2 != 1) {
            if (mo107LA2 != 14) {
                switch (mo107LA2) {
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

    /* renamed from: id */
    public final Token m104id() {
        int mo107LA = mo107LA(1);
        if (mo107LA == 24) {
            Token mo108LT = mo108LT(1);
            match(24);
            if (this.inputState.guessing == 0) {
                return mo108LT;
            }
        } else {
            if (mo107LA != 41) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            Token mo108LT2 = mo108LT(1);
            match(41);
            if (this.inputState.guessing == 0) {
                return mo108LT2;
            }
        }
        return null;
    }

    public final void lexerOptionsSpec() {
        match(14);
        while (true) {
            int mo107LA = mo107LA(1);
            if (mo107LA == 18) {
                match(18);
                match(15);
                BitSet charSet = charSet();
                match(16);
                if (this.inputState.guessing == 0) {
                    this.behavior.setCharVocabulary(charSet);
                }
            } else {
                if (mo107LA != 24 && mo107LA != 41) {
                    match(17);
                    return;
                }
                Token m104id = m104id();
                match(15);
                Token optionValue = optionValue();
                if (this.inputState.guessing == 0) {
                    this.behavior.setGrammarOption(m104id, optionValue);
                }
                match(16);
            }
        }
    }

    public final void lexerSpec(String str) {
        Token token;
        int mo107LA = mo107LA(1);
        String str2 = null;
        if (mo107LA == 9) {
            Token mo108LT = mo108LT(1);
            match(9);
            Token m104id = m104id();
            if (this.inputState.guessing == 0) {
                this.antlrTool.warning("lexclass' is deprecated; use 'class X extends Lexer'", getFilename(), mo108LT.getLine(), mo108LT.getColumn());
            }
            token = m104id;
        } else {
            if (mo107LA != 10) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            match(10);
            token = m104id();
            match(11);
            match(12);
            int mo107LA2 = mo107LA(1);
            if (mo107LA2 != 16) {
                if (mo107LA2 != 27) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                str2 = superClass();
            }
        }
        if (this.inputState.guessing == 0) {
            this.behavior.startLexer(getFilename(), token, str2, str);
        }
        match(16);
        int mo107LA3 = mo107LA(1);
        if (mo107LA3 != 7 && mo107LA3 != 8) {
            if (mo107LA3 == 14) {
                lexerOptionsSpec();
            } else if (mo107LA3 != 41 && mo107LA3 != 23 && mo107LA3 != 24) {
                switch (mo107LA3) {
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
        int mo107LA4 = mo107LA(1);
        if (mo107LA4 != 7 && mo107LA4 != 8) {
            if (mo107LA4 == 23) {
                tokensSpec();
            } else if (mo107LA4 != 24 && mo107LA4 != 41) {
                switch (mo107LA4) {
                    case 30:
                    case 31:
                    case 32:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        }
        int mo107LA5 = mo107LA(1);
        if (mo107LA5 == 7) {
            Token mo108LT2 = mo108LT(1);
            match(7);
            if (this.inputState.guessing == 0) {
                this.behavior.refMemberAction(mo108LT2);
                return;
            }
            return;
        }
        if (mo107LA5 == 8 || mo107LA5 == 24 || mo107LA5 == 41) {
            return;
        }
        switch (mo107LA5) {
            case 30:
            case 31:
            case 32:
                return;
            default:
                throw new NoViableAltException(mo108LT(1), getFilename());
        }
    }

    public final void notTerminal(Token token) {
        int i = 1;
        int mo107LA = mo107LA(1);
        if (mo107LA != 19) {
            if (mo107LA != 24) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            Token mo108LT = mo108LT(1);
            match(24);
            int ast_type_spec = ast_type_spec();
            if (this.inputState.guessing == 0) {
                this.behavior.refToken(null, mo108LT, token, null, true, ast_type_spec, lastInRule());
                return;
            }
            return;
        }
        Token mo108LT2 = mo108LT(1);
        match(19);
        int mo107LA2 = mo107LA(1);
        if (mo107LA2 != 6 && mo107LA2 != 7 && mo107LA2 != 16 && mo107LA2 != 19 && mo107LA2 != 21) {
            if (mo107LA2 == 33) {
                match(33);
                if (this.inputState.guessing == 0) {
                    i = 3;
                }
            } else if (mo107LA2 != 39 && mo107LA2 != 50 && mo107LA2 != 24 && mo107LA2 != 25 && mo107LA2 != 27 && mo107LA2 != 28) {
                switch (mo107LA2) {
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
            this.behavior.refCharLiteral(mo108LT2, token, true, i2, lastInRule());
        }
    }

    public final Token optionValue() {
        int mo107LA = mo107LA(1);
        if (mo107LA == 6) {
            Token mo108LT = mo108LT(1);
            match(6);
            if (this.inputState.guessing == 0) {
                return mo108LT;
            }
        } else {
            if (mo107LA == 24 || mo107LA == 41) {
                return qualifiedID();
            }
            if (mo107LA == 19) {
                Token mo108LT2 = mo108LT(1);
                match(19);
                if (this.inputState.guessing == 0) {
                    return mo108LT2;
                }
            } else {
                if (mo107LA != 20) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                Token mo108LT3 = mo108LT(1);
                match(20);
                if (this.inputState.guessing == 0) {
                    return mo108LT3;
                }
            }
        }
        return null;
    }

    public final void parserOptionsSpec() {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token m104id = m104id();
            match(15);
            Token optionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setGrammarOption(m104id, optionValue);
            }
            i = 16;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void parserSpec(String str) {
        String superClass;
        int mo107LA;
        int mo107LA2;
        int mo107LA3;
        match(10);
        Token m104id = m104id();
        int mo107LA4 = mo107LA(1);
        if (mo107LA4 == 11) {
            match(11);
            match(29);
            int mo107LA5 = mo107LA(1);
            if (mo107LA5 != 16) {
                if (mo107LA5 != 27) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                superClass = superClass();
                if (this.inputState.guessing == 0) {
                    this.behavior.startParser(getFilename(), m104id, superClass, str);
                }
                match(16);
                mo107LA = mo107LA(1);
                if (mo107LA != 7 && mo107LA != 8) {
                    if (mo107LA != 14) {
                        parserOptionsSpec();
                    } else if (mo107LA != 41 && mo107LA != 23 && mo107LA != 24) {
                        switch (mo107LA) {
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
                mo107LA2 = mo107LA(1);
                if (mo107LA2 != 7 && mo107LA2 != 8) {
                    if (mo107LA2 != 23) {
                        tokensSpec();
                    } else if (mo107LA2 != 24 && mo107LA2 != 41) {
                        switch (mo107LA2) {
                            case 30:
                            case 31:
                            case 32:
                                break;
                            default:
                                throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                    }
                }
                mo107LA3 = mo107LA(1);
                if (mo107LA3 != 7) {
                    Token mo108LT = mo108LT(1);
                    match(7);
                    if (this.inputState.guessing == 0) {
                        this.behavior.refMemberAction(mo108LT);
                        return;
                    }
                    return;
                }
                if (mo107LA3 == 8 || mo107LA3 == 24 || mo107LA3 == 41) {
                    return;
                }
                switch (mo107LA3) {
                    case 30:
                    case 31:
                    case 32:
                        return;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        } else {
            if (mo107LA4 != 16) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            if (this.inputState.guessing == 0) {
                this.antlrTool.warning("use 'class X extends Parser'", getFilename(), m104id.getLine(), m104id.getColumn());
            }
        }
        superClass = null;
        if (this.inputState.guessing == 0) {
        }
        match(16);
        mo107LA = mo107LA(1);
        if (mo107LA != 7) {
            if (mo107LA != 14) {
            }
        }
        if (this.inputState.guessing == 0) {
        }
        mo107LA2 = mo107LA(1);
        if (mo107LA2 != 7) {
            if (mo107LA2 != 23) {
            }
        }
        mo107LA3 = mo107LA(1);
        if (mo107LA3 != 7) {
        }
    }

    public final Token qualifiedID() {
        StringBuffer stringBuffer = new StringBuffer(30);
        Token m104id = m104id();
        if (this.inputState.guessing == 0) {
            stringBuffer.append(m104id.getText());
        }
        while (mo107LA(1) == 50) {
            match(50);
            m104id = m104id();
            if (this.inputState.guessing == 0) {
                stringBuffer.append('.');
                stringBuffer.append(m104id.getText());
            }
        }
        if (this.inputState.guessing != 0) {
            return null;
        }
        CommonToken commonToken = new CommonToken(24, stringBuffer.toString());
        commonToken.setLine(m104id.getLine());
        return commonToken;
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00a5, code lost:
    
        if (r13.inputState.guessing == 0) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00c5, code lost:
    
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00dc, code lost:
    
        if (r13.inputState.guessing == 0) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00fc, code lost:
    
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00fa, code lost:
    
        r9 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00f8, code lost:
    
        if (r13.inputState.guessing == 0) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00c3, code lost:
    
        r8 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00c1, code lost:
    
        if (r13.inputState.guessing == 0) goto L51;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void range(Token token) {
        Token mo108LT;
        Token token2;
        Token mo108LT2;
        Token token3;
        int i = 1;
        int mo107LA = mo107LA(1);
        if (mo107LA != 6) {
            if (mo107LA == 19) {
                Token mo108LT3 = mo108LT(1);
                match(19);
                match(22);
                Token mo108LT4 = mo108LT(1);
                match(19);
                int mo107LA2 = mo107LA(1);
                if (mo107LA2 != 6 && mo107LA2 != 7 && mo107LA2 != 16 && mo107LA2 != 19 && mo107LA2 != 21) {
                    if (mo107LA2 == 33) {
                        match(33);
                        if (this.inputState.guessing == 0) {
                            i = 3;
                        }
                    } else if (mo107LA2 != 39 && mo107LA2 != 50 && mo107LA2 != 24 && mo107LA2 != 25 && mo107LA2 != 27 && mo107LA2 != 28) {
                        switch (mo107LA2) {
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
                    this.behavior.refCharRange(mo108LT3, mo108LT4, token, i2, lastInRule());
                    return;
                }
                return;
            }
            if (mo107LA != 24) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
        }
        int mo107LA3 = mo107LA(1);
        if (mo107LA3 == 6) {
            mo108LT = mo108LT(1);
            match(6);
        } else {
            if (mo107LA3 != 24) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            mo108LT = mo108LT(1);
            match(24);
        }
        match(22);
        int mo107LA4 = mo107LA(1);
        if (mo107LA4 == 6) {
            mo108LT2 = mo108LT(1);
            match(6);
        } else {
            if (mo107LA4 != 24) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            mo108LT2 = mo108LT(1);
            match(24);
        }
        int ast_type_spec = ast_type_spec();
        if (this.inputState.guessing == 0) {
            this.behavior.refTokenRange(token2, token3, token, ast_type_spec, lastInRule());
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

    public final void rootNode() {
        Token m104id;
        if ((mo107LA(1) == 24 || mo107LA(1) == 41) && mo107LA(2) == 36) {
            m104id = m104id();
            match(36);
            if (this.inputState.guessing == 0) {
                checkForMissingEndRule(m104id);
            }
        } else {
            if (!_tokenSet_7.member(mo107LA(1)) || !_tokenSet_11.member(mo107LA(2))) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            m104id = null;
        }
        terminal(m104id);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x005e, code lost:
    
        if (r10.inputState.guessing == 0) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0080, code lost:
    
        r2 = r2.getText();
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x006e, code lost:
    
        if (r10.inputState.guessing == 0) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x007e, code lost:
    
        if (r10.inputState.guessing == 0) goto L27;
     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x003e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0094 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00c8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00f3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0125 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0195 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01ba  */
    /* JADX WARN: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x00b4 A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0071  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void rule() {
        String text;
        int mo107LA;
        int mo107LA2;
        boolean z;
        int mo107LA3;
        int mo107LA4;
        int mo107LA5;
        int mo107LA6;
        int mo107LA7;
        int mo107LA8;
        Token mo108LT;
        this.blockNesting = -1;
        int mo107LA9 = mo107LA(1);
        if (mo107LA9 == 8) {
            Token mo108LT2 = mo108LT(1);
            match(8);
            if (this.inputState.guessing == 0) {
                text = mo108LT2.getText();
                mo107LA = mo107LA(1);
                if (mo107LA != 24) {
                    switch (mo107LA) {
                    }
                }
                String str = "public";
                Token m104id = m104id();
                mo107LA2 = mo107LA(1);
                if (mo107LA2 != 7) {
                    switch (mo107LA2) {
                    }
                }
                z = true;
                if (this.inputState.guessing == 0) {
                }
                mo107LA3 = mo107LA(1);
                if (mo107LA3 != 7) {
                }
                mo107LA4 = mo107LA(1);
                if (mo107LA4 != 7) {
                }
                mo107LA5 = mo107LA(1);
                if (mo107LA5 != 7) {
                }
                mo107LA6 = mo107LA(1);
                if (mo107LA6 != 7) {
                }
                mo107LA7 = mo107LA(1);
                if (mo107LA7 == 7) {
                }
                match(36);
                block();
                match(16);
                mo107LA8 = mo107LA(1);
                if (mo107LA8 != 1) {
                }
                if (this.inputState.guessing == 0) {
                }
            }
        } else if (mo107LA9 != 24 && mo107LA9 != 41) {
            switch (mo107LA9) {
                case 30:
                case 31:
                case 32:
                    break;
                default:
                    throw new NoViableAltException(mo108LT(1), getFilename());
            }
            mo107LA = mo107LA(1);
            if (mo107LA != 24 && mo107LA != 41) {
                switch (mo107LA) {
                    case 30:
                        mo108LT = mo108LT(1);
                        match(30);
                        break;
                    case 31:
                        mo108LT = mo108LT(1);
                        match(31);
                        break;
                    case 32:
                        mo108LT = mo108LT(1);
                        match(32);
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            String str2 = "public";
            Token m104id2 = m104id();
            mo107LA2 = mo107LA(1);
            if (mo107LA2 != 7 && mo107LA2 != 14) {
                switch (mo107LA2) {
                    case 33:
                        match(33);
                        if (this.inputState.guessing == 0) {
                            z = false;
                            if (this.inputState.guessing == 0) {
                                this.behavior.defineRuleName(m104id2, str2, z, text);
                            }
                            mo107LA3 = mo107LA(1);
                            if (mo107LA3 != 7 && mo107LA3 != 14) {
                                switch (mo107LA3) {
                                    case 34:
                                        Token mo108LT3 = mo108LT(1);
                                        match(34);
                                        if (this.inputState.guessing == 0) {
                                            this.behavior.refArgAction(mo108LT3);
                                            break;
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
                            mo107LA4 = mo107LA(1);
                            if (mo107LA4 != 7 && mo107LA4 != 14) {
                                switch (mo107LA4) {
                                    case 35:
                                        match(35);
                                        Token mo108LT4 = mo108LT(1);
                                        match(34);
                                        if (this.inputState.guessing == 0) {
                                            this.behavior.refReturnAction(mo108LT4);
                                            break;
                                        }
                                        break;
                                    case 36:
                                    case 37:
                                        break;
                                    default:
                                        throw new NoViableAltException(mo108LT(1), getFilename());
                                }
                            }
                            mo107LA5 = mo107LA(1);
                            if (mo107LA5 != 7 && mo107LA5 != 14 && mo107LA5 != 36) {
                                if (mo107LA5 == 37) {
                                    throw new NoViableAltException(mo108LT(1), getFilename());
                                }
                                throwsSpec();
                            }
                            mo107LA6 = mo107LA(1);
                            if (mo107LA6 != 7) {
                                if (mo107LA6 == 14) {
                                    ruleOptionsSpec();
                                } else if (mo107LA6 != 36) {
                                    throw new NoViableAltException(mo108LT(1), getFilename());
                                }
                            }
                            mo107LA7 = mo107LA(1);
                            if (mo107LA7 == 7) {
                                Token mo108LT5 = mo108LT(1);
                                match(7);
                                if (this.inputState.guessing == 0) {
                                    this.behavior.refInitAction(mo108LT5);
                                }
                            } else if (mo107LA7 != 36) {
                                throw new NoViableAltException(mo108LT(1), getFilename());
                            }
                            match(36);
                            block();
                            match(16);
                            mo107LA8 = mo107LA(1);
                            if (mo107LA8 != 1 && mo107LA8 != 24) {
                                if (mo107LA8 != 39) {
                                    exceptionGroup();
                                } else if (mo107LA8 != 41) {
                                    switch (mo107LA8) {
                                        case 7:
                                        case 8:
                                        case 9:
                                        case 10:
                                            break;
                                        default:
                                            switch (mo107LA8) {
                                                case 30:
                                                case 31:
                                                case 32:
                                                    break;
                                                default:
                                                    throw new NoViableAltException(mo108LT(1), getFilename());
                                            }
                                    }
                                }
                            }
                            if (this.inputState.guessing == 0) {
                                this.behavior.endRule(m104id2.getText());
                                return;
                            }
                            return;
                        }
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                        z = true;
                        if (this.inputState.guessing == 0) {
                        }
                        mo107LA3 = mo107LA(1);
                        if (mo107LA3 != 7) {
                            switch (mo107LA3) {
                            }
                        }
                        mo107LA4 = mo107LA(1);
                        if (mo107LA4 != 7) {
                            switch (mo107LA4) {
                            }
                        }
                        mo107LA5 = mo107LA(1);
                        if (mo107LA5 != 7) {
                            if (mo107LA5 == 37) {
                            }
                        }
                        mo107LA6 = mo107LA(1);
                        if (mo107LA6 != 7) {
                        }
                        mo107LA7 = mo107LA(1);
                        if (mo107LA7 == 7) {
                        }
                        match(36);
                        block();
                        match(16);
                        mo107LA8 = mo107LA(1);
                        if (mo107LA8 != 1) {
                            if (mo107LA8 != 39) {
                            }
                        }
                        if (this.inputState.guessing == 0) {
                        }
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
            z = true;
            if (this.inputState.guessing == 0) {
            }
            mo107LA3 = mo107LA(1);
            if (mo107LA3 != 7) {
            }
            mo107LA4 = mo107LA(1);
            if (mo107LA4 != 7) {
            }
            mo107LA5 = mo107LA(1);
            if (mo107LA5 != 7) {
            }
            mo107LA6 = mo107LA(1);
            if (mo107LA6 != 7) {
            }
            mo107LA7 = mo107LA(1);
            if (mo107LA7 == 7) {
            }
            match(36);
            block();
            match(16);
            mo107LA8 = mo107LA(1);
            if (mo107LA8 != 1) {
            }
            if (this.inputState.guessing == 0) {
            }
        }
        text = null;
        mo107LA = mo107LA(1);
        if (mo107LA != 24) {
        }
        String str22 = "public";
        Token m104id22 = m104id();
        mo107LA2 = mo107LA(1);
        if (mo107LA2 != 7) {
        }
        z = true;
        if (this.inputState.guessing == 0) {
        }
        mo107LA3 = mo107LA(1);
        if (mo107LA3 != 7) {
        }
        mo107LA4 = mo107LA(1);
        if (mo107LA4 != 7) {
        }
        mo107LA5 = mo107LA(1);
        if (mo107LA5 != 7) {
        }
        mo107LA6 = mo107LA(1);
        if (mo107LA6 != 7) {
        }
        mo107LA7 = mo107LA(1);
        if (mo107LA7 == 7) {
        }
        match(36);
        block();
        match(16);
        mo107LA8 = mo107LA(1);
        if (mo107LA8 != 1) {
        }
        if (this.inputState.guessing == 0) {
        }
    }

    public final void ruleOptionsSpec() {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token m104id = m104id();
            match(15);
            Token optionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setRuleOption(m104id, optionValue);
            }
            i = 16;
        }
    }

    public final void rules() {
        int i = 0;
        while (_tokenSet_0.member(mo107LA(1)) && _tokenSet_1.member(mo107LA(2))) {
            rule();
            i++;
        }
        if (i < 1) {
            throw new NoViableAltException(mo108LT(1), getFilename());
        }
    }

    public final BitSet setBlockElement() {
        BitSet bitSet;
        int i;
        Token mo108LT = mo108LT(1);
        match(19);
        if (this.inputState.guessing == 0) {
            i = ANTLRLexer.tokenTypeForCharLiteral(mo108LT.getText());
            bitSet = BitSet.m114of(i);
        } else {
            bitSet = null;
            i = 0;
        }
        int mo107LA = mo107LA(1);
        if (mo107LA != 16 && mo107LA != 21) {
            if (mo107LA != 22) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            match(22);
            Token mo108LT2 = mo108LT(1);
            match(19);
            if (this.inputState.guessing == 0) {
                int i2 = ANTLRLexer.tokenTypeForCharLiteral(mo108LT2.getText());
                if (i2 < i) {
                    this.antlrTool.error("Malformed range line ", getFilename(), mo108LT.getLine(), mo108LT.getColumn());
                }
                for (int i3 = i + 1; i3 <= i2; i3++) {
                    bitSet.add(i3);
                }
            }
        }
        return bitSet;
    }

    public final void subruleOptionsSpec() {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token m104id = m104id();
            match(15);
            Token optionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setSubruleOption(m104id, optionValue);
            }
            i = 16;
        }
    }

    public final String superClass() {
        match(27);
        String stripFrontBack = this.inputState.guessing == 0 ? StringUtils.stripFrontBack(mo108LT(1).getText(), "\"", "\"") : null;
        match(6);
        match(28);
        return stripFrontBack;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0090, code lost:
    
        if (r16.inputState.guessing == 0) goto L35;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void terminal(Token token) {
        Token token2;
        int i = 1;
        int mo107LA = mo107LA(1);
        if (mo107LA == 6) {
            Token mo108LT = mo108LT(1);
            match(6);
            int ast_type_spec = ast_type_spec();
            if (this.inputState.guessing == 0) {
                this.behavior.refStringLiteral(mo108LT, token, ast_type_spec, lastInRule());
                return;
            }
            return;
        }
        if (mo107LA == 19) {
            Token mo108LT2 = mo108LT(1);
            match(19);
            int mo107LA2 = mo107LA(1);
            if (mo107LA2 != 6 && mo107LA2 != 7 && mo107LA2 != 16 && mo107LA2 != 19 && mo107LA2 != 21) {
                if (mo107LA2 == 33) {
                    match(33);
                    if (this.inputState.guessing == 0) {
                        i = 3;
                    }
                } else if (mo107LA2 != 39 && mo107LA2 != 50 && mo107LA2 != 24 && mo107LA2 != 25 && mo107LA2 != 27 && mo107LA2 != 28) {
                    switch (mo107LA2) {
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
                this.behavior.refCharLiteral(mo108LT2, token, false, i2, lastInRule());
                return;
            }
            return;
        }
        if (mo107LA != 24) {
            if (mo107LA != 50) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            Token mo108LT3 = mo108LT(1);
            match(50);
            int ast_type_spec2 = ast_type_spec();
            if (this.inputState.guessing == 0) {
                this.behavior.refWildcard(mo108LT3, token, ast_type_spec2);
                return;
            }
            return;
        }
        Token mo108LT4 = mo108LT(1);
        match(24);
        int ast_type_spec3 = ast_type_spec();
        int mo107LA3 = mo107LA(1);
        if (mo107LA3 != 6 && mo107LA3 != 7 && mo107LA3 != 16 && mo107LA3 != 19 && mo107LA3 != 21) {
            if (mo107LA3 == 34) {
                token2 = mo108LT(1);
                match(34);
            } else if (mo107LA3 != 39 && mo107LA3 != 50 && mo107LA3 != 24 && mo107LA3 != 25 && mo107LA3 != 27 && mo107LA3 != 28) {
                switch (mo107LA3) {
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
                Token token3 = token2;
                if (this.inputState.guessing != 0) {
                    this.behavior.refToken(null, mo108LT4, token, token3, false, ast_type_spec3, lastInRule());
                    return;
                }
                return;
            }
        }
        token2 = null;
        Token token32 = token2;
        if (this.inputState.guessing != 0) {
        }
    }

    public final void throwsSpec() {
        match(37);
        String text = this.inputState.guessing == 0 ? m104id().getText() : null;
        while (mo107LA(1) == 38) {
            match(38);
            Token m104id = m104id();
            if (this.inputState.guessing == 0) {
                text = C0000a.m0a(m104id, C0000a.m9b(text, ","));
            }
        }
        if (this.inputState.guessing == 0) {
            this.behavior.setUserExceptions(text);
        }
    }

    public final void tokensSpec() {
        Token mo108LT;
        match(23);
        int i = 0;
        Token token = null;
        while (true) {
            if (mo107LA(1) != 6 && mo107LA(1) != 24) {
                if (i < 1) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                match(17);
                return;
            }
            int mo107LA = mo107LA(1);
            if (mo107LA == 6) {
                mo108LT = mo108LT(1);
                match(6);
                if (this.inputState.guessing == 0) {
                    this.behavior.defineToken(null, mo108LT);
                }
                int mo107LA2 = mo107LA(1);
                if (mo107LA2 == 16) {
                    continue;
                    match(16);
                    i++;
                } else {
                    if (mo107LA2 != 25) {
                        throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                    tokensSpecOptions(mo108LT);
                    match(16);
                    i++;
                }
            } else {
                if (mo107LA != 24) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                if (this.inputState.guessing == 0) {
                    token = null;
                }
                mo108LT = mo108LT(1);
                match(24);
                int mo107LA3 = mo107LA(1);
                if (mo107LA3 == 15) {
                    match(15);
                    token = mo108LT(1);
                    match(6);
                } else if (mo107LA3 != 16 && mo107LA3 != 25) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                if (this.inputState.guessing == 0) {
                    this.behavior.defineToken(mo108LT, token);
                }
                int mo107LA4 = mo107LA(1);
                if (mo107LA4 == 16) {
                    continue;
                    match(16);
                    i++;
                } else {
                    if (mo107LA4 != 25) {
                        throw new NoViableAltException(mo108LT(1), getFilename());
                    }
                    tokensSpecOptions(mo108LT);
                    match(16);
                    i++;
                }
            }
        }
    }

    public final void tokensSpecOptions(Token token) {
        Token m104id;
        Token optionValue;
        match(25);
        Token m104id2 = m104id();
        match(15);
        Token optionValue2 = optionValue();
        if (this.inputState.guessing != 0) {
            this.behavior.refTokensSpecElementOption(token, m104id2, optionValue2);
            while (mo107LA(1) == 16) {
                match(16);
                m104id = m104id();
                match(15);
                optionValue = optionValue();
                if (this.inputState.guessing == 0) {
                }
            }
            match(26);
            return;
        }
        this.behavior.refTokensSpecElementOption(token, m104id, optionValue);
    }

    public final void tree() {
        Token mo108LT = mo108LT(1);
        match(44);
        if (this.inputState.guessing == 0) {
            this.behavior.beginTree(mo108LT);
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

    public final void treeParserOptionsSpec() {
        int i = 14;
        while (true) {
            match(i);
            if (mo107LA(1) != 24 && mo107LA(1) != 41) {
                match(17);
                return;
            }
            Token m104id = m104id();
            match(15);
            Token optionValue = optionValue();
            if (this.inputState.guessing == 0) {
                this.behavior.setGrammarOption(m104id, optionValue);
            }
            i = 16;
        }
    }

    public final void treeParserSpec(String str) {
        String str2;
        match(10);
        Token m104id = m104id();
        match(11);
        match(13);
        int mo107LA = mo107LA(1);
        if (mo107LA == 16) {
            str2 = null;
        } else {
            if (mo107LA != 27) {
                throw new NoViableAltException(mo108LT(1), getFilename());
            }
            str2 = superClass();
        }
        if (this.inputState.guessing == 0) {
            this.behavior.startTreeWalker(getFilename(), m104id, str2, str);
        }
        match(16);
        int mo107LA2 = mo107LA(1);
        if (mo107LA2 != 7 && mo107LA2 != 8) {
            if (mo107LA2 == 14) {
                treeParserOptionsSpec();
            } else if (mo107LA2 != 41 && mo107LA2 != 23 && mo107LA2 != 24) {
                switch (mo107LA2) {
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
        int mo107LA3 = mo107LA(1);
        if (mo107LA3 != 7 && mo107LA3 != 8) {
            if (mo107LA3 == 23) {
                tokensSpec();
            } else if (mo107LA3 != 24 && mo107LA3 != 41) {
                switch (mo107LA3) {
                    case 30:
                    case 31:
                    case 32:
                        break;
                    default:
                        throw new NoViableAltException(mo108LT(1), getFilename());
                }
            }
        }
        int mo107LA4 = mo107LA(1);
        if (mo107LA4 == 7) {
            Token mo108LT = mo108LT(1);
            match(7);
            if (this.inputState.guessing == 0) {
                this.behavior.refMemberAction(mo108LT);
                return;
            }
            return;
        }
        if (mo107LA4 == 8 || mo107LA4 == 24 || mo107LA4 == 41) {
            return;
        }
        switch (mo107LA4) {
            case 30:
            case 31:
            case 32:
                return;
            default:
                throw new NoViableAltException(mo108LT(1), getFilename());
        }
    }
}
