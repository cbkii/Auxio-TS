package antlr.debug;

import java.util.EventObject;

/* loaded from: classes3.dex */
public abstract class Event extends EventObject {
    public int type;

    public Event(Object obj) {
        super(obj);
    }

    public Event(Object obj, int i) {
        super(obj);
        setType(i);
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public void setValues(int i) {
        setType(i);
    }
}
