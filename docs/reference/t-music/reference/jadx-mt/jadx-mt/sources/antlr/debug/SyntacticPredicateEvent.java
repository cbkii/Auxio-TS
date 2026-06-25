package antlr.debug;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
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
        StringBuilder sbM5a = C0000a.m5a("SyntacticPredicateEvent [");
        sbM5a.append(getGuessing());
        sbM5a.append("]");
        return sbM5a.toString();
    }
}
