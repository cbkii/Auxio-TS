package antlr;

import antlr.ASdebug.ASDebugStream;
import antlr.ASdebug.IASDebugStream;
import antlr.ASdebug.TokenOffsetInfo;
import antlr.collections.impl.BitSet;

/* JADX INFO: loaded from: classes3.dex */
public class TokenStreamBasicFilter implements TokenStream, IASDebugStream {
    public BitSet discardMask = new BitSet();
    public TokenStream input;

    public TokenStreamBasicFilter(TokenStream tokenStream) {
        this.input = tokenStream;
    }

    public void discard(int i) {
        this.discardMask.add(i);
    }

    public void discard(BitSet bitSet) {
        this.discardMask = bitSet;
    }

    @Override // antlr.ASdebug.IASDebugStream
    public String getEntireText() {
        return ASDebugStream.getEntireText(this.input);
    }

    @Override // antlr.ASdebug.IASDebugStream
    public TokenOffsetInfo getOffsetInfo(Token token) {
        return ASDebugStream.getOffsetInfo(this.input, token);
    }

    @Override // antlr.TokenStream
    public Token nextToken() {
        Token tokenNextToken;
        do {
            tokenNextToken = this.input.nextToken();
            if (tokenNextToken == null) {
                break;
            }
        } while (this.discardMask.member(tokenNextToken.getType()));
        return tokenNextToken;
    }
}
