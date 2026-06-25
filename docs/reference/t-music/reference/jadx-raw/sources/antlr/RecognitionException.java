package antlr;

/* loaded from: classes3.dex */
public class RecognitionException extends ANTLRException {
    public int column;
    public String fileName;
    public int line;

    public RecognitionException() {
        super("parsing error");
        this.fileName = null;
        this.line = -1;
        this.column = -1;
    }

    public RecognitionException(String str) {
        super(str);
        this.fileName = null;
        this.line = -1;
        this.column = -1;
    }

    public RecognitionException(String str, String str2, int i) {
        this(str, str2, i, -1);
    }

    public RecognitionException(String str, String str2, int i, int i2) {
        super(str);
        this.fileName = str2;
        this.line = i;
        this.column = i2;
    }

    public int getColumn() {
        return this.column;
    }

    public String getErrorMessage() {
        return getMessage();
    }

    public String getFilename() {
        return this.fileName;
    }

    public int getLine() {
        return this.line;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return FileLineFormatter.getFormatter().getFormatString(this.fileName, this.line, this.column) + getMessage();
    }
}
