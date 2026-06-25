package antlr.ASdebug;

/* loaded from: classes3.dex */
public class TokenOffsetInfo {
    public final int beginOffset;
    public final int length;

    public TokenOffsetInfo(int i, int i2) {
        this.beginOffset = i;
        this.length = i2;
    }

    public int getEndOffset() {
        return (this.beginOffset + this.length) - 1;
    }
}
