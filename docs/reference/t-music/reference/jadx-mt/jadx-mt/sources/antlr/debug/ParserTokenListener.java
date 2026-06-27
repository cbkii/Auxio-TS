package antlr.debug;

/* JADX INFO: loaded from: classes3.dex */
public interface ParserTokenListener extends ListenerBase {
    void parserConsume(ParserTokenEvent parserTokenEvent);

    void parserLA(ParserTokenEvent parserTokenEvent);
}
