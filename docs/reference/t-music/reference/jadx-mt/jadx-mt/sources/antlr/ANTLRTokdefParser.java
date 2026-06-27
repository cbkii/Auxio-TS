package antlr;

import antlr.collections.impl.BitSet;

/* JADX INFO: loaded from: classes3.dex */
public class ANTLRTokdefParser extends LLkParser implements ANTLRTokdefParserTokenTypes {
    public static final String[] _tokenNames = {"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "ID", "STRING", "ASSIGN", "LPAREN", "RPAREN", "INT", "WS", "SL_COMMENT", "ML_COMMENT", "ESC", "DIGIT", "XDIGIT"};
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public Tool antlrTool;

    public ANTLRTokdefParser(ParserSharedInputState parserSharedInputState) {
        super(parserSharedInputState, 3);
        this.tokenNames = _tokenNames;
    }

    public ANTLRTokdefParser(TokenBuffer tokenBuffer) {
        this(tokenBuffer, 3);
    }

    public ANTLRTokdefParser(TokenBuffer tokenBuffer, int i) {
        super(tokenBuffer, i);
        this.tokenNames = _tokenNames;
    }

    public ANTLRTokdefParser(TokenStream tokenStream) {
        this(tokenStream, 3);
    }

    public ANTLRTokdefParser(TokenStream tokenStream, int i) {
        super(tokenStream, i);
        this.tokenNames = _tokenNames;
    }

    public static final long[] mk_tokenSet_0() {
        return new long[]{2, 0};
    }

    public static final long[] mk_tokenSet_1() {
        return new long[]{50, 0};
    }

    public final void file(ImportVocabTokenManager importVocabTokenManager) {
        try {
            mo108LT(1);
            match(4);
            while (true) {
                if (mo107LA(1) != 4 && mo107LA(1) != 5) {
                    return;
                } else {
                    line(importVocabTokenManager);
                }
            }
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_0);
        }
    }

    public Tool getTool() {
        return this.antlrTool;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x008f A[Catch: RecognitionException -> 0x00e2, TryCatch #0 {RecognitionException -> 0x00e2, blocks: (B:3:0x0001, B:5:0x000c, B:13:0x0036, B:26:0x007b, B:28:0x008f, B:30:0x009c, B:32:0x00b7, B:34:0x00c4, B:6:0x0013, B:8:0x001c, B:10:0x0022, B:12:0x0028, B:14:0x003d, B:16:0x0043, B:18:0x004a, B:19:0x0061, B:21:0x0067, B:23:0x006d, B:25:0x0073, B:35:0x00d4, B:36:0x00e1), top: B:40:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void line(ImportVocabTokenManager importVocabTokenManager) {
        Token tokenMo108LT;
        Token tokenMo108LT2;
        Token tokenMo108LT3;
        try {
            Token token = null;
            if (mo107LA(1) == 5) {
                tokenMo108LT3 = mo108LT(1);
                tokenMo108LT = null;
            } else {
                if (mo107LA(1) != 4 || mo107LA(2) != 6 || mo107LA(3) != 5) {
                    if (mo107LA(1) == 4 && mo107LA(2) == 7) {
                        tokenMo108LT = mo108LT(1);
                        match(4);
                        match(7);
                        tokenMo108LT2 = mo108LT(1);
                        match(5);
                        match(8);
                    } else {
                        if (mo107LA(1) != 4 || mo107LA(2) != 6 || mo107LA(3) != 9) {
                            throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                        tokenMo108LT = mo108LT(1);
                        match(4);
                        tokenMo108LT2 = null;
                    }
                    match(6);
                    Token tokenMo108LT4 = mo108LT(1);
                    match(9);
                    Integer numValueOf = Integer.valueOf(tokenMo108LT4.getText());
                    if (token == null) {
                        importVocabTokenManager.define(token.getText(), numValueOf.intValue());
                        if (tokenMo108LT != null) {
                            StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) importVocabTokenManager.getTokenSymbol(token.getText());
                            stringLiteralSymbol.setLabel(tokenMo108LT.getText());
                            importVocabTokenManager.mapToTokenSymbol(tokenMo108LT.getText(), stringLiteralSymbol);
                            return;
                        }
                        return;
                    }
                    if (tokenMo108LT != null) {
                        importVocabTokenManager.define(tokenMo108LT.getText(), numValueOf.intValue());
                        if (tokenMo108LT2 != null) {
                            importVocabTokenManager.getTokenSymbol(tokenMo108LT.getText()).setParaphrase(tokenMo108LT2.getText());
                            return;
                        }
                        return;
                    }
                    return;
                }
                tokenMo108LT = mo108LT(1);
                match(4);
                match(6);
                tokenMo108LT3 = mo108LT(1);
            }
            match(5);
            Token token2 = tokenMo108LT3;
            tokenMo108LT2 = null;
            token = token2;
            match(6);
            Token tokenMo108LT42 = mo108LT(1);
            match(9);
            Integer numValueOf2 = Integer.valueOf(tokenMo108LT42.getText());
            if (token == null) {
            }
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_1);
        }
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

    public void setTool(Tool tool) {
        if (this.antlrTool != null) {
            throw new IllegalStateException("antlr.Tool already registered");
        }
        this.antlrTool = tool;
    }
}
