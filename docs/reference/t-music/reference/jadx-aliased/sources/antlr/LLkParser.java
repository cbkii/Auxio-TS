package antlr;

import java.io.PrintStream;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class LLkParser extends Parser {

    /* renamed from: k */
    public int f305k;

    public LLkParser(int i) {
        this.f305k = i;
    }

    public LLkParser(ParserSharedInputState parserSharedInputState, int i) {
        super(parserSharedInputState);
        this.f305k = i;
    }

    public LLkParser(TokenBuffer tokenBuffer, int i) {
        this.f305k = i;
        setTokenBuffer(tokenBuffer);
    }

    public LLkParser(TokenStream tokenStream, int i) {
        this.f305k = i;
        setTokenBuffer(new TokenBuffer(tokenStream));
    }

    private void trace(String str, String str2) {
        PrintStream printStream;
        StringBuilder sb;
        String str3;
        traceIndent();
        PrintStream printStream2 = System.out;
        StringBuilder m9b = C0000a.m9b(str, str2);
        m9b.append(this.inputState.guessing > 0 ? "; [guessing]" : "; ");
        printStream2.print(m9b.toString());
        for (int i = 1; i <= this.f305k; i++) {
            if (i != 1) {
                System.out.print(", ");
            }
            if (mo108LT(i) != null) {
                printStream = System.out;
                sb = new StringBuilder();
                sb.append("LA(");
                sb.append(i);
                sb.append(")==");
                str3 = mo108LT(i).getText();
            } else {
                printStream = System.out;
                sb = new StringBuilder();
                sb.append("LA(");
                sb.append(i);
                str3 = ")==null";
            }
            sb.append(str3);
            printStream.print(sb.toString());
        }
        System.out.println("");
    }

    @Override // antlr.Parser
    /* renamed from: LA */
    public int mo107LA(int i) {
        return this.inputState.input.m111LA(i);
    }

    @Override // antlr.Parser
    /* renamed from: LT */
    public Token mo108LT(int i) {
        return this.inputState.input.m112LT(i);
    }

    @Override // antlr.Parser
    public void consume() {
        this.inputState.input.consume();
    }

    @Override // antlr.Parser
    public void traceIn(String str) {
        this.traceDepth++;
        trace("> ", str);
    }

    @Override // antlr.Parser
    public void traceOut(String str) {
        trace("< ", str);
        this.traceDepth--;
    }
}
