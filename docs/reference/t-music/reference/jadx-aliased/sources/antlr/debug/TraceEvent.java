package antlr.debug;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class TraceEvent extends GuessingEvent {
    public static int DONE_PARSING = 2;
    public static int ENTER = 0;
    public static int EXIT = 1;
    public int data;
    public int ruleNum;

    public TraceEvent(Object obj) {
        super(obj);
    }

    public TraceEvent(Object obj, int i, int i2, int i3, int i4) {
        super(obj);
        setValues(i, i2, i3, i4);
    }

    public int getData() {
        return this.data;
    }

    public int getRuleNum() {
        return this.ruleNum;
    }

    public void setData(int i) {
        this.data = i;
    }

    public void setRuleNum(int i) {
        this.ruleNum = i;
    }

    public void setValues(int i, int i2, int i3, int i4) {
        super.setValues(i, i3);
        setRuleNum(i2);
        setData(i4);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder m5a = C0000a.m5a("ParserTraceEvent [");
        m5a.append(getType() == ENTER ? "enter," : "exit,");
        m5a.append(getRuleNum());
        m5a.append(",");
        m5a.append(getGuessing());
        m5a.append("]");
        return m5a.toString();
    }
}
