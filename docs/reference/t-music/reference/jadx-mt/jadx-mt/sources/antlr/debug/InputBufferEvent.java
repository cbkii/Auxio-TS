package antlr.debug;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class InputBufferEvent extends Event {
    public static final int CONSUME = 0;

    /* JADX INFO: renamed from: LA */
    public static final int f321LA = 1;
    public static final int MARK = 2;
    public static final int REWIND = 3;

    /* JADX INFO: renamed from: c */
    public char f322c;
    public int lookaheadAmount;

    public InputBufferEvent(Object obj) {
        super(obj);
    }

    public InputBufferEvent(Object obj, int i, char c2, int i2) {
        super(obj);
        setValues(i, c2, i2);
    }

    public char getChar() {
        return this.f322c;
    }

    public int getLookaheadAmount() {
        return this.lookaheadAmount;
    }

    public void setChar(char c2) {
        this.f322c = c2;
    }

    public void setLookaheadAmount(int i) {
        this.lookaheadAmount = i;
    }

    public void setValues(int i, char c2, int i2) {
        super.setValues(i);
        setChar(c2);
        setLookaheadAmount(i2);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sbM5a = C0000a.m5a("CharBufferEvent [");
        sbM5a.append(getType() == 0 ? "CONSUME, " : "LA, ");
        sbM5a.append(getChar());
        sbM5a.append(",");
        sbM5a.append(getLookaheadAmount());
        sbM5a.append("]");
        return sbM5a.toString();
    }
}
