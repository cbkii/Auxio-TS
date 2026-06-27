package antlr.debug;

/* loaded from: classes3.dex */
public interface TraceListener extends ListenerBase {
    void enterRule(TraceEvent traceEvent);

    void exitRule(TraceEvent traceEvent);
}
