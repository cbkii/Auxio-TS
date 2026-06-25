package antlr;

import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
public class CharStreamIOException extends CharStreamException {

    /* JADX INFO: renamed from: io */
    public IOException f303io;

    public CharStreamIOException(IOException iOException) {
        super(iOException.getMessage());
        this.f303io = iOException;
    }
}
