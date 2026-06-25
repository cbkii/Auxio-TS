package antlr.debug;

/* loaded from: classes3.dex */
public class InputBufferReporter implements InputBufferListener {
    @Override // antlr.debug.ListenerBase
    public void doneParsing(TraceEvent traceEvent) {
    }

    public void inputBufferChanged(InputBufferEvent inputBufferEvent) {
        System.out.println(inputBufferEvent);
    }

    @Override // antlr.debug.InputBufferListener
    public void inputBufferConsume(InputBufferEvent inputBufferEvent) {
        System.out.println(inputBufferEvent);
    }

    @Override // antlr.debug.InputBufferListener
    public void inputBufferLA(InputBufferEvent inputBufferEvent) {
        System.out.println(inputBufferEvent);
    }

    @Override // antlr.debug.InputBufferListener
    public void inputBufferMark(InputBufferEvent inputBufferEvent) {
        System.out.println(inputBufferEvent);
    }

    @Override // antlr.debug.InputBufferListener
    public void inputBufferRewind(InputBufferEvent inputBufferEvent) {
        System.out.println(inputBufferEvent);
    }

    @Override // antlr.debug.ListenerBase
    public void refresh() {
    }
}
