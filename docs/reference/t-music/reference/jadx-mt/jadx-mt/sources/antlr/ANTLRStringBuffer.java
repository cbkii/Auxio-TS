package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class ANTLRStringBuffer {
    public char[] buffer;
    public int length = 0;

    public ANTLRStringBuffer() {
        this.buffer = null;
        this.buffer = new char[50];
    }

    public ANTLRStringBuffer(int i) {
        this.buffer = null;
        this.buffer = new char[i];
    }

    public final void append(char c2) {
        int i = this.length;
        char[] cArr = this.buffer;
        if (i >= cArr.length) {
            int length = cArr.length;
            while (this.length >= length) {
                length *= 2;
            }
            char[] cArr2 = new char[length];
            for (int i2 = 0; i2 < this.length; i2++) {
                cArr2[i2] = this.buffer[i2];
            }
            this.buffer = cArr2;
        }
        char[] cArr3 = this.buffer;
        int i3 = this.length;
        cArr3[i3] = c2;
        this.length = i3 + 1;
    }

    public final void append(String str) {
        for (int i = 0; i < str.length(); i++) {
            append(str.charAt(i));
        }
    }

    public final char charAt(int i) {
        return this.buffer[i];
    }

    public final char[] getBuffer() {
        return this.buffer;
    }

    public final int length() {
        return this.length;
    }

    public final void setCharAt(int i, char c2) {
        this.buffer[i] = c2;
    }

    public final void setLength(int i) {
        if (i < this.length) {
            this.length = i;
        } else {
            while (i > this.length) {
                append((char) 0);
            }
        }
    }

    public final String toString() {
        return new String(this.buffer, 0, this.length);
    }
}
