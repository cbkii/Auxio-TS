package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class CommonToken extends Token {
    public int col;
    public int line;
    public String text;

    public CommonToken() {
        this.text = null;
    }

    public CommonToken(int i, String str) {
        this.text = null;
        this.type = i;
        setText(str);
    }

    public CommonToken(String str) {
        this.text = null;
        this.text = str;
    }

    @Override // antlr.Token
    public int getColumn() {
        return this.col;
    }

    @Override // antlr.Token
    public int getLine() {
        return this.line;
    }

    @Override // antlr.Token
    public String getText() {
        return this.text;
    }

    @Override // antlr.Token
    public void setColumn(int i) {
        this.col = i;
    }

    @Override // antlr.Token
    public void setLine(int i) {
        this.line = i;
    }

    @Override // antlr.Token
    public void setText(String str) {
        this.text = str;
    }

    @Override // antlr.Token
    public String toString() {
        StringBuilder sbM5a = C0000a.m5a("[\"");
        sbM5a.append(getText());
        sbM5a.append("\",<");
        sbM5a.append(this.type);
        sbM5a.append(">,line=");
        sbM5a.append(this.line);
        sbM5a.append(",col=");
        sbM5a.append(this.col);
        sbM5a.append("]");
        return sbM5a.toString();
    }
}
