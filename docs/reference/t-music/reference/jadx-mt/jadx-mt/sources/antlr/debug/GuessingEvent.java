package antlr.debug;

/* JADX INFO: loaded from: classes3.dex */
public abstract class GuessingEvent extends Event {
    public int guessing;

    public GuessingEvent(Object obj) {
        super(obj);
    }

    public GuessingEvent(Object obj, int i) {
        super(obj, i);
    }

    public int getGuessing() {
        return this.guessing;
    }

    public void setGuessing(int i) {
        this.guessing = i;
    }

    public void setValues(int i, int i2) {
        super.setValues(i);
        setGuessing(i2);
    }
}
