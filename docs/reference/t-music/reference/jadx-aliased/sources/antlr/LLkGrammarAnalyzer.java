package antlr;

/* loaded from: classes3.dex */
public interface LLkGrammarAnalyzer extends GrammarAnalyzer {
    Lookahead FOLLOW(int i, RuleEndElement ruleEndElement);

    boolean deterministic(AlternativeBlock alternativeBlock);

    boolean deterministic(OneOrMoreBlock oneOrMoreBlock);

    boolean deterministic(ZeroOrMoreBlock zeroOrMoreBlock);

    Lookahead look(int i, ActionElement actionElement);

    Lookahead look(int i, AlternativeBlock alternativeBlock);

    Lookahead look(int i, BlockEndElement blockEndElement);

    Lookahead look(int i, CharLiteralElement charLiteralElement);

    Lookahead look(int i, CharRangeElement charRangeElement);

    Lookahead look(int i, GrammarAtom grammarAtom);

    Lookahead look(int i, OneOrMoreBlock oneOrMoreBlock);

    Lookahead look(int i, RuleBlock ruleBlock);

    Lookahead look(int i, RuleEndElement ruleEndElement);

    Lookahead look(int i, RuleRefElement ruleRefElement);

    Lookahead look(int i, StringLiteralElement stringLiteralElement);

    Lookahead look(int i, SynPredBlock synPredBlock);

    Lookahead look(int i, TokenRangeElement tokenRangeElement);

    Lookahead look(int i, TreeElement treeElement);

    Lookahead look(int i, WildcardElement wildcardElement);

    Lookahead look(int i, ZeroOrMoreBlock zeroOrMoreBlock);

    Lookahead look(int i, String str);

    void setGrammar(Grammar grammar);

    boolean subruleCanBeInverted(AlternativeBlock alternativeBlock, boolean z);
}
