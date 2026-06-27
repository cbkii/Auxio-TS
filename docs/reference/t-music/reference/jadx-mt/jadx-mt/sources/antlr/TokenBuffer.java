package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class TokenBuffer {
    public TokenStream input;
    public int nMarkers = 0;
    public int markerOffset = 0;
    public int numToConsume = 0;
    public TokenQueue queue = new TokenQueue(1);

    public TokenBuffer(TokenStream tokenStream) {
        this.input = tokenStream;
    }

    private final void fill(int i) {
        syncConsume();
        while (true) {
            TokenQueue tokenQueue = this.queue;
            if (tokenQueue.nbrEntries >= this.markerOffset + i) {
                return;
            } else {
                tokenQueue.append(this.input.nextToken());
            }
        }
    }

    private final void syncConsume() {
        while (this.numToConsume > 0) {
            if (this.nMarkers > 0) {
                this.markerOffset++;
            } else {
                this.queue.removeFirst();
            }
            this.numToConsume--;
        }
    }

    /* JADX INFO: renamed from: LA */
    public final int m111LA(int i) {
        fill(i);
        return this.queue.elementAt((this.markerOffset + i) - 1).getType();
    }

    /* JADX INFO: renamed from: LT */
    public final Token m112LT(int i) {
        fill(i);
        return this.queue.elementAt((this.markerOffset + i) - 1);
    }

    public final void consume() {
        this.numToConsume++;
    }

    public TokenStream getInput() {
        return this.input;
    }

    public final int mark() {
        syncConsume();
        this.nMarkers++;
        return this.markerOffset;
    }

    public final void reset() {
        this.nMarkers = 0;
        this.markerOffset = 0;
        this.numToConsume = 0;
        this.queue.reset();
    }

    public final void rewind(int i) {
        syncConsume();
        this.markerOffset = i;
        this.nMarkers--;
    }
}
