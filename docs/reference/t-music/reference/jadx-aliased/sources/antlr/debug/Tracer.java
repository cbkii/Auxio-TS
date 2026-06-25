package antlr.debug;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class Tracer extends TraceAdapter implements TraceListener {
    public String indent = "";

    public void dedent() {
        this.indent = this.indent.length() < 2 ? "" : this.indent.substring(2);
    }

    @Override // antlr.debug.TraceAdapter, antlr.debug.TraceListener
    public void enterRule(TraceEvent traceEvent) {
        System.out.println(this.indent + traceEvent);
        indent();
    }

    @Override // antlr.debug.TraceAdapter, antlr.debug.TraceListener
    public void exitRule(TraceEvent traceEvent) {
        dedent();
        System.out.println(this.indent + traceEvent);
    }

    public void indent() {
        this.indent = C0000a.m3a(new StringBuilder(), this.indent, "  ");
    }
}
