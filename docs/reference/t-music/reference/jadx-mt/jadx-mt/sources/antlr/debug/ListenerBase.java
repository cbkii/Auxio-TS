package antlr.debug;

import java.util.EventListener;

/* JADX INFO: loaded from: classes3.dex */
public interface ListenerBase extends EventListener {
    void doneParsing(TraceEvent traceEvent);

    void refresh();
}
