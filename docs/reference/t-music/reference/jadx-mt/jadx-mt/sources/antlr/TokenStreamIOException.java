package antlr;

import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
public class TokenStreamIOException extends TokenStreamException {

    /* JADX INFO: renamed from: io */
    public IOException f306io;

    public TokenStreamIOException(IOException iOException) {
        super(iOException.getMessage());
        this.f306io = iOException;
    }
}
