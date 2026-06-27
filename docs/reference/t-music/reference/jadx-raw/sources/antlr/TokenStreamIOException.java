package antlr;

import java.io.IOException;

/* loaded from: classes3.dex */
public class TokenStreamIOException extends TokenStreamException {

    /* renamed from: io */
    public IOException f306io;

    public TokenStreamIOException(IOException iOException) {
        super(iOException.getMessage());
        this.f306io = iOException;
    }
}
