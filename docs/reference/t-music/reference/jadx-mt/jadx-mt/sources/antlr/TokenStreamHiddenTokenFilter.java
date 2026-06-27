package antlr;

import antlr.collections.impl.BitSet;

/* JADX INFO: loaded from: classes3.dex */
public class TokenStreamHiddenTokenFilter extends TokenStreamBasicFilter implements TokenStream {
    public CommonHiddenStreamToken firstHidden;
    public BitSet hideMask;
    public CommonHiddenStreamToken lastHiddenToken;
    public CommonHiddenStreamToken nextMonitoredToken;

    public TokenStreamHiddenTokenFilter(TokenStream tokenStream) {
        super(tokenStream);
        this.firstHidden = null;
        this.hideMask = new BitSet();
    }

    private void consumeFirst() {
        consume();
        CommonHiddenStreamToken commonHiddenStreamTokenM113LA = null;
        while (true) {
            if (!this.hideMask.member(m113LA(1).getType()) && !this.discardMask.member(m113LA(1).getType())) {
                return;
            }
            if (this.hideMask.member(m113LA(1).getType())) {
                if (commonHiddenStreamTokenM113LA != null) {
                    commonHiddenStreamTokenM113LA.setHiddenAfter(m113LA(1));
                    m113LA(1).setHiddenBefore(commonHiddenStreamTokenM113LA);
                }
                commonHiddenStreamTokenM113LA = m113LA(1);
                this.lastHiddenToken = commonHiddenStreamTokenM113LA;
                if (this.firstHidden == null) {
                    this.firstHidden = commonHiddenStreamTokenM113LA;
                }
            }
            consume();
        }
    }

    /* JADX INFO: renamed from: LA */
    public CommonHiddenStreamToken m113LA(int i) {
        return this.nextMonitoredToken;
    }

    public void consume() {
        this.nextMonitoredToken = (CommonHiddenStreamToken) this.input.nextToken();
    }

    public BitSet getDiscardMask() {
        return this.discardMask;
    }

    public CommonHiddenStreamToken getHiddenAfter(CommonHiddenStreamToken commonHiddenStreamToken) {
        return commonHiddenStreamToken.getHiddenAfter();
    }

    public CommonHiddenStreamToken getHiddenBefore(CommonHiddenStreamToken commonHiddenStreamToken) {
        return commonHiddenStreamToken.getHiddenBefore();
    }

    public BitSet getHideMask() {
        return this.hideMask;
    }

    public CommonHiddenStreamToken getInitialHiddenToken() {
        return this.firstHidden;
    }

    public void hide(int i) {
        this.hideMask.add(i);
    }

    public void hide(BitSet bitSet) {
        this.hideMask = bitSet;
    }

    @Override // antlr.TokenStreamBasicFilter, antlr.TokenStream
    public Token nextToken() {
        if (m113LA(1) == null) {
            consumeFirst();
        }
        CommonHiddenStreamToken commonHiddenStreamTokenM113LA = m113LA(1);
        commonHiddenStreamTokenM113LA.setHiddenBefore(this.lastHiddenToken);
        this.lastHiddenToken = null;
        consume();
        CommonHiddenStreamToken commonHiddenStreamTokenM113LA2 = commonHiddenStreamTokenM113LA;
        while (true) {
            if (!this.hideMask.member(m113LA(1).getType()) && !this.discardMask.member(m113LA(1).getType())) {
                return commonHiddenStreamTokenM113LA;
            }
            if (this.hideMask.member(m113LA(1).getType())) {
                commonHiddenStreamTokenM113LA2.setHiddenAfter(m113LA(1));
                if (commonHiddenStreamTokenM113LA2 != commonHiddenStreamTokenM113LA) {
                    m113LA(1).setHiddenBefore(commonHiddenStreamTokenM113LA2);
                }
                commonHiddenStreamTokenM113LA2 = m113LA(1);
                this.lastHiddenToken = commonHiddenStreamTokenM113LA2;
            }
            consume();
        }
    }
}
