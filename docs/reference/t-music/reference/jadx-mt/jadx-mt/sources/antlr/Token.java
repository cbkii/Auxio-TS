package antlr;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class Token implements Cloneable {
    public static final int EOF_TYPE = 1;
    public static final int INVALID_TYPE = 0;
    public static final int MIN_USER_TYPE = 4;
    public static final int NULL_TREE_LOOKAHEAD = 3;
    public static final int SKIP = -1;
    public static Token badToken = new Token(0, "<no text>");
    public int type;

    public Token() {
        this.type = 0;
    }

    public Token(int i) {
        this.type = 0;
        this.type = i;
    }

    public Token(int i, String str) {
        this.type = 0;
        this.type = i;
        setText(str);
    }

    public int getColumn() {
        return 0;
    }

    public String getFilename() {
        return null;
    }

    public int getLine() {
        return 0;
    }

    public String getText() {
        return "<no text>";
    }

    public int getType() {
        return this.type;
    }

    public void setColumn(int i) {
    }

    public void setFilename(String str) {
    }

    public void setLine(int i) {
    }

    public void setText(String str) {
    }

    public void setType(int i) {
        this.type = i;
    }

    public String toString() {
        StringBuilder sbM5a = C0000a.m5a("[\"");
        sbM5a.append(getText());
        sbM5a.append("\",<");
        sbM5a.append(getType());
        sbM5a.append(">]");
        return sbM5a.toString();
    }
}
