package antlr.debug;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class InputBufferEvent extends Event {
    public static final int CONSUME = 0;

    /* renamed from: LA */
    public static final int f321LA = 1;
    public static final int MARK = 2;
    public static final int REWIND = 3;

    /* renamed from: c */
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
        StringBuilder m5a = C0000a.m5a("CharBufferEvent [");
        m5a.append(getType() == 0 ? "CONSUME, " : "LA, ");
        m5a.append(getChar());
        m5a.append(",");
        m5a.append(getLookaheadAmount());
        m5a.append("]");
        return m5a.toString();
    }
}
