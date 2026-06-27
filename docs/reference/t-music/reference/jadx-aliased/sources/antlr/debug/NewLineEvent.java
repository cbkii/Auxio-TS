package antlr.debug;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
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
        StringBuilder m5a = C0000a.m5a("NewLineEvent [");
        m5a.append(this.line);
        m5a.append("]");
        return m5a.toString();
    }
}
