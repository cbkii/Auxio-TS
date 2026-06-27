package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class TokenWithIndex extends CommonToken {
    public int index;

    public TokenWithIndex() {
    }

    public TokenWithIndex(int i, String str) {
        super(i, str);
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    @Override // antlr.CommonToken, antlr.Token
    public String toString() {
        StringBuilder sbM5a = C0000a.m5a("[");
        sbM5a.append(this.index);
        sbM5a.append(":\"");
        sbM5a.append(getText());
        sbM5a.append("\",<");
        sbM5a.append(getType());
        sbM5a.append(">,line=");
        sbM5a.append(this.line);
        sbM5a.append(",col=");
        sbM5a.append(this.col);
        sbM5a.append("]\n");
        return sbM5a.toString();
    }
}
