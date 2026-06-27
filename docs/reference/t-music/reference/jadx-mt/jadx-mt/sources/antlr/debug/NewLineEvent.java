package antlr.debug;

import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class NewLineEvent extends Event {
    public int line;

    public NewLineEvent(Object obj) {
        super(obj);
    }

    public NewLineEvent(Object obj, int i) {
        super(obj);
        setValues(i);
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int i) {
        this.line = i;
    }

    @Override // antlr.debug.Event
    public void setValues(int i) {
        setLine(i);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sbM5a = C0000a.m5a("NewLineEvent [");
        sbM5a.append(this.line);
        sbM5a.append("]");
        return sbM5a.toString();
    }
}
