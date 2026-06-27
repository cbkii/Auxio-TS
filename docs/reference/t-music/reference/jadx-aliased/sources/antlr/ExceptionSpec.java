package antlr;

import antlr.collections.impl.Vector;

/* loaded from: classes3.dex */
public class ExceptionSpec {
    public Vector handlers = new Vector();
    public Token label;

    public ExceptionSpec(Token token) {
        this.label = token;
    }

    public void addHandler(ExceptionHandler exceptionHandler) {
        this.handlers.appendElement(exceptionHandler);
    }
}
