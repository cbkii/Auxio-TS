package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class TokenStreamRecognitionException extends TokenStreamException {
    public RecognitionException recog;

    public TokenStreamRecognitionException(RecognitionException recognitionException) {
        super(recognitionException.getMessage());
        this.recog = recognitionException;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return this.recog.toString();
    }
}
