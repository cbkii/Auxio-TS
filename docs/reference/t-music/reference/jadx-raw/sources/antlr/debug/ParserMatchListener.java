package antlr.debug;

/* loaded from: classes3.dex */
public interface ParserMatchListener extends ListenerBase {
    void parserMatch(ParserMatchEvent parserMatchEvent);

    void parserMatchNot(ParserMatchEvent parserMatchEvent);

    void parserMismatch(ParserMatchEvent parserMatchEvent);

    void parserMismatchNot(ParserMatchEvent parserMatchEvent);
}
