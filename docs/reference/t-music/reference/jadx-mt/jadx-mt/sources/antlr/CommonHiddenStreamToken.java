package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class CommonHiddenStreamToken extends CommonToken {
    public CommonHiddenStreamToken hiddenAfter;
    public CommonHiddenStreamToken hiddenBefore;

    public CommonHiddenStreamToken() {
    }

    public CommonHiddenStreamToken(int i, String str) {
        super(i, str);
    }

    public CommonHiddenStreamToken(String str) {
        super(str);
    }

    public CommonHiddenStreamToken getHiddenAfter() {
        return this.hiddenAfter;
    }

    public CommonHiddenStreamToken getHiddenBefore() {
        return this.hiddenBefore;
    }

    public void setHiddenAfter(CommonHiddenStreamToken commonHiddenStreamToken) {
        this.hiddenAfter = commonHiddenStreamToken;
    }

    public void setHiddenBefore(CommonHiddenStreamToken commonHiddenStreamToken) {
        this.hiddenBefore = commonHiddenStreamToken;
    }
}
