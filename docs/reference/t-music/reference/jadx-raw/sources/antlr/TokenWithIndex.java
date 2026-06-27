package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
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
        StringBuilder m5a = C0000a.m5a("[");
        m5a.append(this.index);
        m5a.append(":\"");
        m5a.append(getText());
        m5a.append("\",<");
        m5a.append(getType());
        m5a.append(">,line=");
        m5a.append(this.line);
        m5a.append(",col=");
        m5a.append(this.col);
        m5a.append("]\n");
        return m5a.toString();
    }
}
