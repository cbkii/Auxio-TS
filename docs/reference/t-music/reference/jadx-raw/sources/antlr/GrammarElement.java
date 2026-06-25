package antlr;

/* loaded from: classes3.dex */
public abstract class GrammarElement {
    public static final int AUTO_GEN_BANG = 3;
    public static final int AUTO_GEN_CARET = 2;
    public static final int AUTO_GEN_NONE = 1;
    public int column;
    public Grammar grammar;
    public int line;

    public GrammarElement(Grammar grammar) {
        this.grammar = grammar;
        this.line = -1;
        this.column = -1;
    }

    public GrammarElement(Grammar grammar, Token token) {
        this.grammar = grammar;
        this.line = token.getLine();
        this.column = token.getColumn();
    }

    public void generate() {
    }

    public int getColumn() {
        return this.column;
    }

    public int getLine() {
        return this.line;
    }

    public Lookahead look(int i) {
        return null;
    }

    public abstract String toString();
}
