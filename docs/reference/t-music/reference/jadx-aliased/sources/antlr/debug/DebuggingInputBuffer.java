package antlr.debug;

import antlr.CharStreamException;
import antlr.InputBuffer;
import java.util.Vector;

/* loaded from: classes3.dex */
public class DebuggingInputBuffer extends InputBuffer {
    public InputBuffer buffer;
    public boolean debugMode = true;
    public InputBufferEventSupport inputBufferEventSupport = new InputBufferEventSupport(this);

    public DebuggingInputBuffer(InputBuffer inputBuffer) {
        this.buffer = inputBuffer;
    }

    @Override // antlr.InputBuffer
    /* renamed from: LA */
    public char mo106LA(int i) {
        char mo106LA = this.buffer.mo106LA(i);
        if (this.debugMode) {
            this.inputBufferEventSupport.fireLA(mo106LA, i);
        }
        return mo106LA;
    }

    public void addInputBufferListener(InputBufferListener inputBufferListener) {
        this.inputBufferEventSupport.addInputBufferListener(inputBufferListener);
    }

    @Override // antlr.InputBuffer
    public void consume() {
        char c2;
        try {
            c2 = this.buffer.mo106LA(1);
        } catch (CharStreamException unused) {
            c2 = ' ';
        }
        this.buffer.consume();
        if (this.debugMode) {
            this.inputBufferEventSupport.fireConsume(c2);
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
        int mark = this.buffer.mark();
        this.inputBufferEventSupport.fireMark(mark);
        return mark;
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
