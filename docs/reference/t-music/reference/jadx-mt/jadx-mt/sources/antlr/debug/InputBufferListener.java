package antlr.debug;

/* JADX INFO: loaded from: classes3.dex */
public interface InputBufferListener extends ListenerBase {
    void inputBufferConsume(InputBufferEvent inputBufferEvent);

    void inputBufferLA(InputBufferEvent inputBufferEvent);

    void inputBufferMark(InputBufferEvent inputBufferEvent);

    void inputBufferRewind(InputBufferEvent inputBufferEvent);
}
