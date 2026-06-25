package antlr.debug;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class ParserMatchEvent extends GuessingEvent {
    public static int BITSET = 1;
    public static int CHAR = 2;
    public static int CHAR_BITSET = 3;
    public static int CHAR_RANGE = 5;
    public static int STRING = 4;
    public static int TOKEN;
    public boolean inverse;
    public boolean matched;
    public Object target;
    public String text;
    public int value;

    public ParserMatchEvent(Object obj) {
        super(obj);
    }

    public ParserMatchEvent(Object obj, int i, int i2, Object obj2, String str, int i3, boolean z, boolean z2) {
        super(obj);
        setValues(i, i2, obj2, str, i3, z, z2);
    }

    public Object getTarget() {
        return this.target;
    }

    public String getText() {
        return this.text;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isInverse() {
        return this.inverse;
    }

    public boolean isMatched() {
        return this.matched;
    }

    public void setInverse(boolean z) {
        this.inverse = z;
    }

    public void setMatched(boolean z) {
        this.matched = z;
    }

    public void setTarget(Object obj) {
        this.target = obj;
    }

    public void setText(String str) {
        this.text = str;
    }

    public void setValue(int i) {
        this.value = i;
    }

    public void setValues(int i, int i2, Object obj, String str, int i3, boolean z, boolean z2) {
        super.setValues(i, i3);
        setValue(i2);
        setTarget(obj);
        setInverse(z);
        setMatched(z2);
        setText(str);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sbM5a = C0000a.m5a("ParserMatchEvent [");
        sbM5a.append(isMatched() ? "ok," : "bad,");
        sbM5a.append(isInverse() ? "NOT " : "");
        sbM5a.append(getType() == TOKEN ? "token," : "bitset,");
        sbM5a.append(getValue());
        sbM5a.append(",");
        sbM5a.append(getTarget());
        sbM5a.append(",");
        sbM5a.append(getGuessing());
        sbM5a.append("]");
        return sbM5a.toString();
    }
}
