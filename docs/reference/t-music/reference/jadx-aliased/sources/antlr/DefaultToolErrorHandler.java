package antlr;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class DefaultToolErrorHandler implements ToolErrorHandler {
    public final Tool antlrTool;
    public CharFormatter javaCharFormatter = new JavaCharFormatter();

    public DefaultToolErrorHandler(Tool tool) {
        this.antlrTool = tool;
    }

    private void dumpSets(String[] strArr, int i, Grammar grammar, boolean z, int i2, Lookahead[] lookaheadArr) {
        String bitSet;
        StringBuffer stringBuffer = new StringBuffer(100);
        int i3 = 1;
        while (i3 <= i2) {
            stringBuffer.append("k==");
            stringBuffer.append(i3);
            stringBuffer.append(':');
            if (z) {
                bitSet = lookaheadArr[i3].fset.toStringWithRanges(",", this.javaCharFormatter);
                if (lookaheadArr[i3].containsEpsilon()) {
                    stringBuffer.append("<end-of-token>");
                    if (bitSet.length() > 0) {
                        stringBuffer.append(',');
                    }
                }
            } else {
                bitSet = lookaheadArr[i3].fset.toString(",", grammar.tokenManager.getVocabulary());
            }
            stringBuffer.append(bitSet);
            strArr[i] = stringBuffer.toString();
            stringBuffer.setLength(0);
            i3++;
            i++;
        }
    }

    @Override // antlr.ToolErrorHandler
    public void warnAltAmbiguity(Grammar grammar, AlternativeBlock alternativeBlock, boolean z, int i, Lookahead[] lookaheadArr, int i2, int i3) {
        String str;
        StringBuffer stringBuffer = new StringBuffer(100);
        if ((alternativeBlock instanceof RuleBlock) && ((RuleBlock) alternativeBlock).isLexerAutoGenRule()) {
            Alternative alternativeAt = alternativeBlock.getAlternativeAt(i2);
            Alternative alternativeAt2 = alternativeBlock.getAlternativeAt(i3);
            RuleRefElement ruleRefElement = (RuleRefElement) alternativeAt.head;
            RuleRefElement ruleRefElement2 = (RuleRefElement) alternativeAt2.head;
            String reverseLexerRuleName = CodeGenerator.reverseLexerRuleName(ruleRefElement.targetRule);
            String reverseLexerRuleName2 = CodeGenerator.reverseLexerRuleName(ruleRefElement2.targetRule);
            stringBuffer.append("lexical nondeterminism between rules ");
            stringBuffer.append(reverseLexerRuleName);
            stringBuffer.append(" and ");
            stringBuffer.append(reverseLexerRuleName2);
            str = " upon";
        } else {
            if (z) {
                stringBuffer.append("lexical ");
            }
            stringBuffer.append("nondeterminism between alts ");
            stringBuffer.append(i2 + 1);
            stringBuffer.append(" and ");
            stringBuffer.append(i3 + 1);
            str = " of block upon";
        }
        stringBuffer.append(str);
        String[] strArr = new String[i + 1];
        strArr[0] = stringBuffer.toString();
        dumpSets(strArr, 1, grammar, z, i, lookaheadArr);
        this.antlrTool.warning(strArr, grammar.getFilename(), alternativeBlock.getLine(), alternativeBlock.getColumn());
    }

    @Override // antlr.ToolErrorHandler
    public void warnAltExitAmbiguity(Grammar grammar, BlockWithImpliedExitPath blockWithImpliedExitPath, boolean z, int i, Lookahead[] lookaheadArr, int i2) {
        String[] strArr = new String[i + 2];
        strArr[0] = C0000a.m3a(new StringBuilder(), z ? "lexical " : "", "nondeterminism upon");
        dumpSets(strArr, 1, grammar, z, i, lookaheadArr);
        StringBuilder m5a = C0000a.m5a("between alt ");
        m5a.append(i2 + 1);
        m5a.append(" and exit branch of block");
        strArr[i + 1] = m5a.toString();
        this.antlrTool.warning(strArr, grammar.getFilename(), blockWithImpliedExitPath.getLine(), blockWithImpliedExitPath.getColumn());
    }
}
