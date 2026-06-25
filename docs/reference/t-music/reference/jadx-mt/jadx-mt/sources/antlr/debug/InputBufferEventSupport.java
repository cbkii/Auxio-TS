package antlr.debug;

import java.util.Vector;

/* JADX INFO: loaded from: classes3.dex */
public class InputBufferEventSupport {
    public static final int CONSUME = 0;

    /* JADX INFO: renamed from: LA */
    public static final int f323LA = 1;
    public static final int MARK = 2;
    public static final int REWIND = 3;
    public InputBufferEvent inputBufferEvent;
    public Vector inputBufferListeners;
    public Object source;

    public InputBufferEventSupport(Object obj) {
        this.inputBufferEvent = new InputBufferEvent(obj);
        this.source = obj;
    }

    public void addInputBufferListener(InputBufferListener inputBufferListener) {
        if (this.inputBufferListeners == null) {
            this.inputBufferListeners = new Vector();
        }
        this.inputBufferListeners.addElement(inputBufferListener);
    }

    public void fireConsume(char c2) {
        this.inputBufferEvent.setValues(0, c2, 0);
        fireEvents(0, this.inputBufferListeners);
    }

    public void fireEvent(int i, ListenerBase listenerBase) {
        if (i == 0) {
            ((InputBufferListener) listenerBase).inputBufferConsume(this.inputBufferEvent);
            return;
        }
        if (i == 1) {
            ((InputBufferListener) listenerBase).inputBufferLA(this.inputBufferEvent);
            return;
        }
        if (i == 2) {
            ((InputBufferListener) listenerBase).inputBufferMark(this.inputBufferEvent);
            return;
        }
        if (i == 3) {
            ((InputBufferListener) listenerBase).inputBufferRewind(this.inputBufferEvent);
            return;
        }
        throw new IllegalArgumentException("bad type " + i + " for fireEvent()");
    }

    public void fireEvents(int i, Vector vector) {
        synchronized (this) {
            if (vector == null) {
                return;
            }
            Vector vector2 = (Vector) vector.clone();
            if (vector2 != null) {
                for (int i2 = 0; i2 < vector2.size(); i2++) {
                    fireEvent(i, (ListenerBase) vector2.elementAt(i2));
                }
            }
        }
    }

    public void fireLA(char c2, int i) {
        this.inputBufferEvent.setValues(1, c2, i);
        fireEvents(1, this.inputBufferListeners);
    }

    public void fireMark(int i) {
        this.inputBufferEvent.setValues(2, ' ', i);
        fireEvents(2, this.inputBufferListeners);
    }

    public void fireRewind(int i) {
        this.inputBufferEvent.setValues(3, ' ', i);
        fireEvents(3, this.inputBufferListeners);
    }

    public Vector getInputBufferListeners() {
        return this.inputBufferListeners;
    }

    public void refresh(Vector vector) {
        Vector vector2;
        synchronized (vector) {
            vector2 = (Vector) vector.clone();
        }
        if (vector2 != null) {
            for (int i = 0; i < vector2.size(); i++) {
                ((ListenerBase) vector2.elementAt(i)).refresh();
            }
        }
    }

    public void refreshListeners() {
        refresh(this.inputBufferListeners);
    }

    public void removeInputBufferListener(InputBufferListener inputBufferListener) {
        Vector vector = this.inputBufferListeners;
        if (vector != null) {
            vector.removeElement(inputBufferListener);
        }
    }
}
