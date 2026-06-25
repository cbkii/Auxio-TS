package antlr.debug;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class SemanticPredicateEvent extends GuessingEvent {
    public static final int PREDICTING = 1;
    public static final int VALIDATING = 0;
    public int condition;
    public boolean result;

    public SemanticPredicateEvent(Object obj) {
        super(obj);
    }

    public SemanticPredicateEvent(Object obj, int i) {
        super(obj, i);
    }

    public int getCondition() {
        return this.condition;
    }

    public boolean getResult() {
        return this.result;
    }

    public void setCondition(int i) {
        this.condition = i;
    }

    public void setResult(boolean z) {
        this.result = z;
    }

    public void setValues(int i, int i2, boolean z, int i3) {
        super.setValues(i, i3);
        setCondition(i2);
        setResult(z);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder m5a = C0000a.m5a("SemanticPredicateEvent [");
        m5a.append(getCondition());
        m5a.append(",");
        m5a.append(getResult());
        m5a.append(",");
        m5a.append(getGuessing());
        m5a.append("]");
        return m5a.toString();
    }
}
