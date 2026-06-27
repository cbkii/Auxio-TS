package antlr;

/* loaded from: classes3.dex */
public abstract class InputBuffer {
    public int nMarkers = 0;
    public int markerOffset = 0;
    public int numToConsume = 0;
    public CharQueue queue = new CharQueue(1);

    /* renamed from: LA */
    public char mo106LA(int i) {
        fill(i);
        return this.queue.elementAt((this.markerOffset + i) - 1);
    }

    public void commit() {
        this.nMarkers--;
    }

    public void consume() {
        this.numToConsume++;
    }

    public abstract void fill(int i);

    public String getLAChars() {
        StringBuffer stringBuffer = new StringBuffer();
        int i = this.markerOffset;
        while (true) {
            CharQueue charQueue = this.queue;
            if (i >= charQueue.nbrEntries) {
                return stringBuffer.toString();
            }
            stringBuffer.append(charQueue.elementAt(i));
            i++;
        }
    }

    public String getMarkedChars() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.markerOffset; i++) {
            stringBuffer.append(this.queue.elementAt(i));
        }
        return stringBuffer.toString();
    }

    public boolean isMarked() {
        return this.nMarkers != 0;
    }

    public int mark() {
        syncConsume();
        this.nMarkers++;
        return this.markerOffset;
    }

    public void reset() {
        this.nMarkers = 0;
        this.markerOffset = 0;
        this.numToConsume = 0;
        this.queue.reset();
    }

    public void rewind(int i) {
        syncConsume();
        this.markerOffset = i;
        this.nMarkers--;
    }

    public void syncConsume() {
        while (this.numToConsume > 0) {
            if (this.nMarkers > 0) {
                this.markerOffset++;
            } else {
                this.queue.removeFirst();
            }
            this.numToConsume--;
        }
    }
}
