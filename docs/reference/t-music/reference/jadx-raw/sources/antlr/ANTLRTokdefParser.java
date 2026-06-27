package antlr;

import antlr.collections.impl.BitSet;

/* loaded from: classes3.dex */
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

    /* JADX WARN: Removed duplicated region for block: B:15:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x008f A[Catch: RecognitionException -> 0x00e2, TryCatch #0 {RecognitionException -> 0x00e2, blocks: (B:3:0x0001, B:5:0x000c, B:6:0x0036, B:7:0x007b, B:9:0x008f, B:11:0x009c, B:16:0x00b7, B:18:0x00c4, B:22:0x0013, B:24:0x001c, B:26:0x0022, B:28:0x0028, B:29:0x003d, B:31:0x0043, B:33:0x004a, B:34:0x0061, B:36:0x0067, B:38:0x006d, B:40:0x0073, B:41:0x00d4, B:42:0x00e1), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void line(ImportVocabTokenManager importVocabTokenManager) {
        Token mo108LT;
        Token token;
        Token mo108LT2;
        try {
            Token token2 = null;
            if (mo107LA(1) == 5) {
                mo108LT2 = mo108LT(1);
                mo108LT = null;
            } else {
                if (mo107LA(1) != 4 || mo107LA(2) != 6 || mo107LA(3) != 5) {
                    if (mo107LA(1) == 4 && mo107LA(2) == 7) {
                        mo108LT = mo108LT(1);
                        match(4);
                        match(7);
                        token = mo108LT(1);
                        match(5);
                        match(8);
                    } else {
                        if (mo107LA(1) != 4 || mo107LA(2) != 6 || mo107LA(3) != 9) {
                            throw new NoViableAltException(mo108LT(1), getFilename());
                        }
                        mo108LT = mo108LT(1);
                        match(4);
                        token = null;
                    }
                    match(6);
                    Token mo108LT3 = mo108LT(1);
                    match(9);
                    Integer valueOf = Integer.valueOf(mo108LT3.getText());
                    if (token2 == null) {
                        importVocabTokenManager.define(token2.getText(), valueOf.intValue());
                        if (mo108LT != null) {
                            StringLiteralSymbol stringLiteralSymbol = (StringLiteralSymbol) importVocabTokenManager.getTokenSymbol(token2.getText());
                            stringLiteralSymbol.setLabel(mo108LT.getText());
                            importVocabTokenManager.mapToTokenSymbol(mo108LT.getText(), stringLiteralSymbol);
                            return;
                        }
                        return;
                    }
                    if (mo108LT != null) {
                        importVocabTokenManager.define(mo108LT.getText(), valueOf.intValue());
                        if (token != null) {
                            importVocabTokenManager.getTokenSymbol(mo108LT.getText()).setParaphrase(token.getText());
                            return;
                        }
                        return;
                    }
                    return;
                }
                mo108LT = mo108LT(1);
                match(4);
                match(6);
                mo108LT2 = mo108LT(1);
            }
            match(5);
            Token token3 = mo108LT2;
            token = null;
            token2 = token3;
            match(6);
            Token mo108LT32 = mo108LT(1);
            match(9);
            Integer valueOf2 = Integer.valueOf(mo108LT32.getText());
            if (token2 == null) {
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
