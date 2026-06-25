package antlr;

/* JADX INFO: loaded from: classes3.dex */
public class CharLiteralElement extends GrammarAtom {
    public CharLiteralElement(LexerGrammar lexerGrammar, Token token, boolean z, int i) {
        super(lexerGrammar, token, 1);
        this.tokenType = ANTLRLexer.tokenTypeForCharLiteral(token.getText());
        lexerGrammar.charVocabulary.add(this.tokenType);
        this.line = token.getLine();
        this.not = z;
        this.autoGenType = i;
    }

    @Override // antlr.GrammarElement
    public void generate() {
        this.grammar.generator.gen(this);
    }

    @Override // antlr.GrammarElement
    public Lookahead look(int i) {
        return this.grammar.theLLkAnalyzer.look(i, this);
    }
}
