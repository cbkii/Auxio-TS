package antlr;

import java.io.InputStream;
import java.io.Reader;

/* JADX INFO: loaded from: classes3.dex */
public class LexerSharedInputState {
    public int column;
    public String filename;
    public int guessing;
    public InputBuffer input;
    public int line;
    public int tokenStartColumn;
    public int tokenStartLine;

    public LexerSharedInputState(InputBuffer inputBuffer) {
        this.column = 1;
        this.line = 1;
        this.tokenStartColumn = 1;
        this.tokenStartLine = 1;
        this.guessing = 0;
        this.input = inputBuffer;
    }

    public LexerSharedInputState(InputStream inputStream) {
        this(new ByteBuffer(inputStream));
    }

    public LexerSharedInputState(Reader reader) {
        this(new CharBuffer(reader));
    }

    public int getColumn() {
        return this.column;
    }

    public String getFilename() {
        return this.filename;
    }

    public InputBuffer getInput() {
        return this.input;
    }

    public int getLine() {
        return this.line;
    }

    public int getTokenStartColumn() {
        return this.tokenStartColumn;
    }

    public int getTokenStartLine() {
        return this.tokenStartLine;
    }

    public void reset() {
        this.column = 1;
        this.line = 1;
        this.tokenStartColumn = 1;
        this.tokenStartLine = 1;
        this.guessing = 0;
        this.filename = null;
        this.input.reset();
    }
}
