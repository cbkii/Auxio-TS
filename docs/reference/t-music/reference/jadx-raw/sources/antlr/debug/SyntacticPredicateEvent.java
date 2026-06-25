package antlr.debug;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class SyntacticPredicateEvent extends GuessingEvent {
    public SyntacticPredicateEvent(Object obj) {
        super(obj);
    }

    public SyntacticPredicateEvent(Object obj, int i) {
        super(obj, i);
    }

    @Override // antlr.debug.GuessingEvent
    public void setValues(int i, int i2) {
        super.setValues(i, i2);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder m5a = C0000a.m5a("SyntacticPredicateEvent [");
        m5a.append(getGuessing());
        m5a.append("]");
        return m5a.toString();
    }
}
