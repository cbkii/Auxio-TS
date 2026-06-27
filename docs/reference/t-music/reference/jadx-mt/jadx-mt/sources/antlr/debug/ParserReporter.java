package antlr.debug;

/* JADX INFO: loaded from: classes3.dex */
public class ParserReporter extends Tracer implements ParserListener {
    @Override // antlr.debug.ParserTokenListener
    public void parserConsume(ParserTokenEvent parserTokenEvent) {
        System.out.println(this.indent + parserTokenEvent);
    }

    @Override // antlr.debug.ParserTokenListener
    public void parserLA(ParserTokenEvent parserTokenEvent) {
        System.out.println(this.indent + parserTokenEvent);
    }

    @Override // antlr.debug.ParserMatchListener
    public void parserMatch(ParserMatchEvent parserMatchEvent) {
        System.out.println(this.indent + parserMatchEvent);
    }

    @Override // antlr.debug.ParserMatchListener
    public void parserMatchNot(ParserMatchEvent parserMatchEvent) {
        System.out.println(this.indent + parserMatchEvent);
    }

    @Override // antlr.debug.ParserMatchListener
    public void parserMismatch(ParserMatchEvent parserMatchEvent) {
        System.out.println(this.indent + parserMatchEvent);
    }

    @Override // antlr.debug.ParserMatchListener
    public void parserMismatchNot(ParserMatchEvent parserMatchEvent) {
        System.out.println(this.indent + parserMatchEvent);
    }

    @Override // antlr.debug.MessageListener
    public void reportError(MessageEvent messageEvent) {
        System.out.println(this.indent + messageEvent);
    }

    @Override // antlr.debug.MessageListener
    public void reportWarning(MessageEvent messageEvent) {
        System.out.println(this.indent + messageEvent);
    }

    @Override // antlr.debug.SemanticPredicateListener
    public void semanticPredicateEvaluated(SemanticPredicateEvent semanticPredicateEvent) {
        System.out.println(this.indent + semanticPredicateEvent);
    }

    @Override // antlr.debug.SyntacticPredicateListener
    public void syntacticPredicateFailed(SyntacticPredicateEvent syntacticPredicateEvent) {
        System.out.println(this.indent + syntacticPredicateEvent);
    }

    @Override // antlr.debug.SyntacticPredicateListener
    public void syntacticPredicateStarted(SyntacticPredicateEvent syntacticPredicateEvent) {
        System.out.println(this.indent + syntacticPredicateEvent);
    }

    @Override // antlr.debug.SyntacticPredicateListener
    public void syntacticPredicateSucceeded(SyntacticPredicateEvent syntacticPredicateEvent) {
        System.out.println(this.indent + syntacticPredicateEvent);
    }
}
