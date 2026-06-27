package antlr.debug;

/* JADX INFO: loaded from: classes3.dex */
public class ParserAdapter implements ParserListener {
    @Override // antlr.debug.ListenerBase
    public void doneParsing(TraceEvent traceEvent) {
    }

    @Override // antlr.debug.TraceListener
    public void enterRule(TraceEvent traceEvent) {
    }

    @Override // antlr.debug.TraceListener
    public void exitRule(TraceEvent traceEvent) {
    }

    @Override // antlr.debug.ParserTokenListener
    public void parserConsume(ParserTokenEvent parserTokenEvent) {
    }

    @Override // antlr.debug.ParserTokenListener
    public void parserLA(ParserTokenEvent parserTokenEvent) {
    }

    @Override // antlr.debug.ParserMatchListener
    public void parserMatch(ParserMatchEvent parserMatchEvent) {
    }

    @Override // antlr.debug.ParserMatchListener
    public void parserMatchNot(ParserMatchEvent parserMatchEvent) {
    }

    @Override // antlr.debug.ParserMatchListener
    public void parserMismatch(ParserMatchEvent parserMatchEvent) {
    }

    @Override // antlr.debug.ParserMatchListener
    public void parserMismatchNot(ParserMatchEvent parserMatchEvent) {
    }

    @Override // antlr.debug.ListenerBase
    public void refresh() {
    }

    @Override // antlr.debug.MessageListener
    public void reportError(MessageEvent messageEvent) {
    }

    @Override // antlr.debug.MessageListener
    public void reportWarning(MessageEvent messageEvent) {
    }

    @Override // antlr.debug.SemanticPredicateListener
    public void semanticPredicateEvaluated(SemanticPredicateEvent semanticPredicateEvent) {
    }

    @Override // antlr.debug.SyntacticPredicateListener
    public void syntacticPredicateFailed(SyntacticPredicateEvent syntacticPredicateEvent) {
    }

    @Override // antlr.debug.SyntacticPredicateListener
    public void syntacticPredicateStarted(SyntacticPredicateEvent syntacticPredicateEvent) {
    }

    @Override // antlr.debug.SyntacticPredicateListener
    public void syntacticPredicateSucceeded(SyntacticPredicateEvent syntacticPredicateEvent) {
    }
}
