package antlr.debug;

import antlr.CharStreamException;
import antlr.InputBuffer;
import java.util.Vector;

/* JADX INFO: loaded from: classes3.dex */
public class DebuggingInputBuffer extends InputBuffer {
    public InputBuffer buffer;
    public boolean debugMode = true;
    public InputBufferEventSupport inputBufferEventSupport = new InputBufferEventSupport(this);

    public DebuggingInputBuffer(InputBuffer inputBuffer) {
        this.buffer = inputBuffer;
    }

    @Override // antlr.InputBuffer
    /* JADX INFO: renamed from: LA */
    public char mo106LA(int i) {
        char cMo106LA = this.buffer.mo106LA(i);
        if (this.debugMode) {
            this.inputBufferEventSupport.fireLA(cMo106LA, i);
        }
        return cMo106LA;
    }

    public void addInputBufferListener(InputBufferListener inputBufferListener) {
        this.inputBufferEventSupport.addInputBufferListener(inputBufferListener);
    }

    @Override // antlr.InputBuffer
    public void consume() {
        char cMo106LA;
        try {
            cMo106LA = this.buffer.mo106LA(1);
        } catch (CharStreamException unused) {
            cMo106LA = ' ';
        }
        this.buffer.consume();
        if (this.debugMode) {
            this.inputBufferEventSupport.fireConsume(cMo106LA);
        }
    }

    @Override // antlr.InputBuffer
    public void fill(int i) {
        this.buffer.fill(i);
    }

    public Vector getInputBufferListeners() {
        return this.inputBufferEventSupport.getInputBufferListeners();
    }

    public boolean isDebugMode() {
        return this.debugMode;
    }

    @Override // antlr.InputBuffer
    public boolean isMarked() {
        return this.buffer.isMarked();
    }

    @Override // antlr.InputBuffer
    public int mark() {
        int iMark = this.buffer.mark();
        this.inputBufferEventSupport.fireMark(iMark);
        return iMark;
    }

    public void removeInputBufferListener(InputBufferListener inputBufferListener) {
        InputBufferEventSupport inputBufferEventSupport = this.inputBufferEventSupport;
        if (inputBufferEventSupport != null) {
            inputBufferEventSupport.removeInputBufferListener(inputBufferListener);
        }
    }

    @Override // antlr.InputBuffer
    public void rewind(int i) {
        this.buffer.rewind(i);
        this.inputBufferEventSupport.fireRewind(i);
    }

    public void setDebugMode(boolean z) {
        this.debugMode = z;
    }
}
