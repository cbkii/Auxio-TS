package cpdetector.p074io.parser;

import antlr.LLkParser;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.collections.impl.BitSet;

/* loaded from: classes4.dex */
public class EncodingParser extends LLkParser implements EncodingParserTokenTypes {
    public static final String[] _tokenNames = {"<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "META_CONTENT_TYPE", "XML_ENCODING_DECL", "IDENTIFIER", "SPACING", "NEWLINE", "SPACE", "DIGIT", "LETTER"};
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    public EncodingParser(ParserSharedInputState parserSharedInputState) {
        super(parserSharedInputState, 1);
        this.tokenNames = _tokenNames;
    }

    public EncodingParser(TokenBuffer tokenBuffer) {
        this(tokenBuffer, 1);
    }

    public EncodingParser(TokenBuffer tokenBuffer, int i) {
        super(tokenBuffer, i);
        this.tokenNames = _tokenNames;
    }

    public EncodingParser(TokenStream tokenStream) {
        this(tokenStream, 1);
    }

    public EncodingParser(TokenStream tokenStream, int i) {
        super(tokenStream, i);
        this.tokenNames = _tokenNames;
    }

    public static final long[] mk_tokenSet_0() {
        return new long[]{2, 0};
    }

    public final String htmlDocument() {
        Token mo108LT;
        try {
            int mo107LA = mo107LA(1);
            if (mo107LA == 1) {
                return null;
            }
            int i = 4;
            if (mo107LA != 4) {
                i = 5;
                if (mo107LA != 5) {
                    throw new NoViableAltException(mo108LT(1), getFilename());
                }
                mo108LT = mo108LT(1);
            } else {
                mo108LT = mo108LT(1);
            }
            match(i);
            return mo108LT.getText();
        } catch (RecognitionException e) {
            reportError(e);
            consume();
            consumeUntil(_tokenSet_0);
            return null;
        }
    }
}
