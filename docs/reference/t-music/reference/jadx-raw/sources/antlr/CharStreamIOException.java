package antlr;

import java.io.IOException;

/* loaded from: classes3.dex */
public class CharStreamIOException extends CharStreamException {

    /* renamed from: io */
    public IOException f303io;

    public CharStreamIOException(IOException iOException) {
        super(iOException.getMessage());
        this.f303io = iOException;
    }
}
