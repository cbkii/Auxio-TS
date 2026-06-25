package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class CharQueue {
    public char[] buffer;
    public int nbrEntries;
    public int offset;
    public int sizeLessOne;

    public CharQueue(int i) {
        int i2;
        if (i < 0) {
            i2 = 16;
        } else {
            if (i < 1073741823) {
                int i3 = 2;
                while (i3 < i) {
                    i3 *= 2;
                }
                init(i3);
                return;
            }
            i2 = Integer.MAX_VALUE;
        }
        init(i2);
    }

    private final void expand() {
        char[] cArr = new char[this.buffer.length * 2];
        for (int i = 0; i < this.buffer.length; i++) {
            cArr[i] = elementAt(i);
        }
        this.buffer = cArr;
        this.sizeLessOne = this.buffer.length - 1;
        this.offset = 0;
    }

    public final void append(char c2) {
        if (this.nbrEntries == this.buffer.length) {
            expand();
        }
        char[] cArr = this.buffer;
        int i = this.offset;
        int i2 = this.nbrEntries;
        cArr[(i + i2) & this.sizeLessOne] = c2;
        this.nbrEntries = i2 + 1;
    }

    public final char elementAt(int i) {
        return this.buffer[this.sizeLessOne & (this.offset + i)];
    }

    public void init(int i) {
        this.buffer = new char[i];
        this.sizeLessOne = i - 1;
        this.offset = 0;
        this.nbrEntries = 0;
    }

    public final void removeFirst() {
        this.offset = (this.offset + 1) & this.sizeLessOne;
        this.nbrEntries--;
    }

    public final void reset() {
        this.offset = 0;
        this.nbrEntries = 0;
    }
}
