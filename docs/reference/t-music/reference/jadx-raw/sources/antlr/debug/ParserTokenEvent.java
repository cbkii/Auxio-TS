package antlr.debug;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class ParserTokenEvent extends Event {
    public static int CONSUME = 1;

    /* renamed from: LA */
    public static int f325LA;
    public int amount;
    public int value;

    public ParserTokenEvent(Object obj) {
        super(obj);
    }

    public ParserTokenEvent(Object obj, int i, int i2, int i3) {
        super(obj);
        setValues(i, i2, i3);
    }

    public int getAmount() {
        return this.amount;
    }

    public int getValue() {
        return this.value;
    }

    public void setAmount(int i) {
        this.amount = i;
    }

    public void setValue(int i) {
        this.value = i;
    }

    public void setValues(int i, int i2, int i3) {
        super.setValues(i);
        setAmount(i2);
        setValue(i3);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder m5a;
        if (getType() == f325LA) {
            m5a = C0000a.m5a("ParserTokenEvent [LA,");
            m5a.append(getAmount());
            m5a.append(",");
        } else {
            m5a = C0000a.m5a("ParserTokenEvent [consume,1,");
        }
        m5a.append(getValue());
        m5a.append("]");
        return m5a.toString();
    }
}
