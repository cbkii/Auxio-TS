package antlr;

import antlr.collections.impl.BitSet;

/* JADX INFO: loaded from: classes3.dex */
public interface ANTLRGrammarParseBehavior {
    void abortGrammar();

    void beginAlt(boolean z);

    void beginChildList();

    void beginExceptionGroup();

    void beginExceptionSpec(Token token);

    void beginSubRule(Token token, Token token2, boolean z);

    void beginTree(Token token);

    void defineRuleName(Token token, String str, boolean z, String str2);

    void defineToken(Token token, Token token2);

    void endAlt();

    void endChildList();

    void endExceptionGroup();

    void endExceptionSpec();

    void endGrammar();

    void endOptions();

    void endRule(String str);

    void endSubRule();

    void endTree();

    void hasError();

    void noASTSubRule();

    void oneOrMoreSubRule();

    void optionalSubRule();

    void refAction(Token token);

    void refArgAction(Token token);

    void refCharLiteral(Token token, Token token2, boolean z, int i, boolean z2);

    void refCharRange(Token token, Token token2, Token token3, int i, boolean z);

    void refElementOption(Token token, Token token2);

    void refExceptionHandler(Token token, Token token2);

    void refHeaderAction(Token token, Token token2);

    void refInitAction(Token token);

    void refMemberAction(Token token);

    void refPreambleAction(Token token);

    void refReturnAction(Token token);

    void refRule(Token token, Token token2, Token token3, Token token4, int i);

    void refSemPred(Token token);

    void refStringLiteral(Token token, Token token2, int i, boolean z);

    void refToken(Token token, Token token2, Token token3, Token token4, boolean z, int i, boolean z2);

    void refTokenRange(Token token, Token token2, Token token3, int i, boolean z);

    void refTokensSpecElementOption(Token token, Token token2, Token token3);

    void refTreeSpecifier(Token token);

    void refWildcard(Token token, Token token2, int i);

    void setArgOfRuleRef(Token token);

    void setCharVocabulary(BitSet bitSet);

    void setFileOption(Token token, Token token2, String str);

    void setGrammarOption(Token token, Token token2);

    void setRuleOption(Token token, Token token2);

    void setSubruleOption(Token token, Token token2);

    void setUserExceptions(String str);

    void startLexer(String str, Token token, String str2, String str3);

    void startParser(String str, Token token, String str2, String str3);

    void startTreeWalker(String str, Token token, String str2, String str3);

    void synPred();

    void zeroOrMoreSubRule();
}
