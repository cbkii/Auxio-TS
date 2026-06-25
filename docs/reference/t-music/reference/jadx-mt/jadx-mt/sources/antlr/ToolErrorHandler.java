package antlr;

/* JADX INFO: loaded from: classes3.dex */
public interface ToolErrorHandler {
    void warnAltAmbiguity(Grammar grammar, AlternativeBlock alternativeBlock, boolean z, int i, Lookahead[] lookaheadArr, int i2, int i3);

    void warnAltExitAmbiguity(Grammar grammar, BlockWithImpliedExitPath blockWithImpliedExitPath, boolean z, int i, Lookahead[] lookaheadArr, int i2);
}
