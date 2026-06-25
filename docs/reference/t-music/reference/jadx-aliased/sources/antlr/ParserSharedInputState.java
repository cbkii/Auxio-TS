package antlr;

/* loaded from: classes3.dex */
public class ParserSharedInputState {
    public String filename;
    public int guessing = 0;
    public TokenBuffer input;

    public String getFilename() {
        return this.filename;
    }

    public TokenBuffer getInput() {
        return this.input;
    }

    public void reset() {
        this.guessing = 0;
        this.filename = null;
        this.input.reset();
    }
}
