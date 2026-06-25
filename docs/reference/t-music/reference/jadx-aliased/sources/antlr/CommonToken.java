package antlr;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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
        StringBuilder m5a = C0000a.m5a("[\"");
        m5a.append(getText());
        m5a.append("\",<");
        m5a.append(this.type);
        m5a.append(">,line=");
        m5a.append(this.line);
        m5a.append(",col=");
        m5a.append(this.col);
        m5a.append("]");
        return m5a.toString();
    }
}
