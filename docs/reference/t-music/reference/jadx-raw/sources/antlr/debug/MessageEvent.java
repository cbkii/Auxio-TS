package antlr.debug;

import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class MessageEvent extends Event {
    public static int ERROR = 1;
    public static int WARNING;
    public String text;

    public MessageEvent(Object obj) {
        super(obj);
    }

    public MessageEvent(Object obj, int i, String str) {
        super(obj);
        setValues(i, str);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public void setValues(int i, String str) {
        super.setValues(i);
        setText(str);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder m5a = C0000a.m5a("ParserMessageEvent [");
        m5a.append(getType() == WARNING ? "warning," : "error,");
        m5a.append(getText());
        m5a.append("]");
        return m5a.toString();
    }
}
