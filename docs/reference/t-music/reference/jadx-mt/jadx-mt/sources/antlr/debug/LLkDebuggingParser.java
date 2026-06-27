package antlr.debug;

import antlr.LLkParser;
import antlr.MismatchedTokenException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.Utils;
import antlr.collections.impl.BitSet;

/* JADX INFO: loaded from: classes3.dex */
public class LLkDebuggingParser extends LLkParser implements DebuggingParser {
    public boolean _notDebugMode;
    public ParserEventSupport parserEventSupport;
    public String[] ruleNames;
    public String[] semPredNames;

    public LLkDebuggingParser(int i) {
        super(i);
        this.parserEventSupport = new ParserEventSupport(this);
        this._notDebugMode = false;
    }

    public LLkDebuggingParser(ParserSharedInputState parserSharedInputState, int i) {
        super(parserSharedInputState, i);
        this.parserEventSupport = new ParserEventSupport(this);
        this._notDebugMode = false;
    }

    public LLkDebuggingParser(TokenBuffer tokenBuffer, int i) {
        super(tokenBuffer, i);
        this.parserEventSupport = new ParserEventSupport(this);
        this._notDebugMode = false;
    }

    public LLkDebuggingParser(TokenStream tokenStream, int i) {
        super(tokenStream, i);
        this.parserEventSupport = new ParserEventSupport(this);
        this._notDebugMode = false;
    }

    @Override // antlr.LLkParser, antlr.Parser
    /* JADX INFO: renamed from: LA */
    public int mo107LA(int i) {
        int iMo107LA = super.mo107LA(i);
        this.parserEventSupport.fireLA(i, iMo107LA);
        return iMo107LA;
    }

    @Override // antlr.Parser
    public void addMessageListener(MessageListener messageListener) {
        this.parserEventSupport.addMessageListener(messageListener);
    }

    @Override // antlr.Parser
    public void addParserListener(ParserListener parserListener) {
        this.parserEventSupport.addParserListener(parserListener);
    }

    @Override // antlr.Parser
    public void addParserMatchListener(ParserMatchListener parserMatchListener) {
        this.parserEventSupport.addParserMatchListener(parserMatchListener);
    }

    @Override // antlr.Parser
    public void addParserTokenListener(ParserTokenListener parserTokenListener) {
        this.parserEventSupport.addParserTokenListener(parserTokenListener);
    }

    @Override // antlr.Parser
    public void addSemanticPredicateListener(SemanticPredicateListener semanticPredicateListener) {
        this.parserEventSupport.addSemanticPredicateListener(semanticPredicateListener);
    }

    @Override // antlr.Parser
    public void addSyntacticPredicateListener(SyntacticPredicateListener syntacticPredicateListener) {
        this.parserEventSupport.addSyntacticPredicateListener(syntacticPredicateListener);
    }

    @Override // antlr.Parser
    public void addTraceListener(TraceListener traceListener) {
        this.parserEventSupport.addTraceListener(traceListener);
    }

    @Override // antlr.LLkParser, antlr.Parser
    public void consume() {
        int iMo107LA = mo107LA(1);
        super.consume();
        this.parserEventSupport.fireConsume(iMo107LA);
    }

    public void fireEnterRule(int i, int i2) {
        if (isDebugMode()) {
            this.parserEventSupport.fireEnterRule(i, this.inputState.guessing, i2);
        }
    }

    public void fireExitRule(int i, int i2) {
        if (isDebugMode()) {
            this.parserEventSupport.fireExitRule(i, this.inputState.guessing, i2);
        }
    }

    public boolean fireSemanticPredicateEvaluated(int i, int i2, boolean z) {
        return isDebugMode() ? this.parserEventSupport.fireSemanticPredicateEvaluated(i, i2, z, this.inputState.guessing) : z;
    }

    public void fireSyntacticPredicateFailed() {
        if (isDebugMode()) {
            this.parserEventSupport.fireSyntacticPredicateFailed(this.inputState.guessing);
        }
    }

    public void fireSyntacticPredicateStarted() {
        if (isDebugMode()) {
            this.parserEventSupport.fireSyntacticPredicateStarted(this.inputState.guessing);
        }
    }

    public void fireSyntacticPredicateSucceeded() {
        if (isDebugMode()) {
            this.parserEventSupport.fireSyntacticPredicateSucceeded(this.inputState.guessing);
        }
    }

    @Override // antlr.debug.DebuggingParser
    public String getRuleName(int i) {
        return this.ruleNames[i];
    }

    @Override // antlr.debug.DebuggingParser
    public String getSemPredName(int i) {
        return this.semPredNames[i];
    }

    public synchronized void goToSleep() {
        try {
            wait();
        } catch (InterruptedException unused) {
        }
    }

    @Override // antlr.Parser
    public boolean isDebugMode() {
        return !this._notDebugMode;
    }

    public boolean isGuessing() {
        return this.inputState.guessing > 0;
    }

    @Override // antlr.Parser
    public void match(int i) throws MismatchedTokenException {
        String text = mo108LT(1).getText();
        int iMo107LA = mo107LA(1);
        try {
            super.match(i);
            this.parserEventSupport.fireMatch(i, text, this.inputState.guessing);
        } catch (MismatchedTokenException e) {
            int i2 = this.inputState.guessing;
            if (i2 == 0) {
                this.parserEventSupport.fireMismatch(iMo107LA, i, text, i2);
            }
            throw e;
        }
    }

    @Override // antlr.Parser
    public void match(BitSet bitSet) throws MismatchedTokenException {
        String text = mo108LT(1).getText();
        int iMo107LA = mo107LA(1);
        try {
            super.match(bitSet);
            this.parserEventSupport.fireMatch(iMo107LA, bitSet, text, this.inputState.guessing);
        } catch (MismatchedTokenException e) {
            int i = this.inputState.guessing;
            if (i == 0) {
                this.parserEventSupport.fireMismatch(iMo107LA, bitSet, text, i);
            }
            throw e;
        }
    }

    @Override // antlr.Parser
    public void matchNot(int i) throws MismatchedTokenException {
        String text = mo108LT(1).getText();
        int iMo107LA = mo107LA(1);
        try {
            super.matchNot(i);
            this.parserEventSupport.fireMatchNot(iMo107LA, i, text, this.inputState.guessing);
        } catch (MismatchedTokenException e) {
            int i2 = this.inputState.guessing;
            if (i2 == 0) {
                this.parserEventSupport.fireMismatchNot(iMo107LA, i, text, i2);
            }
            throw e;
        }
    }

    @Override // antlr.Parser
    public void removeMessageListener(MessageListener messageListener) {
        this.parserEventSupport.removeMessageListener(messageListener);
    }

    @Override // antlr.Parser
    public void removeParserListener(ParserListener parserListener) {
        this.parserEventSupport.removeParserListener(parserListener);
    }

    @Override // antlr.Parser
    public void removeParserMatchListener(ParserMatchListener parserMatchListener) {
        this.parserEventSupport.removeParserMatchListener(parserMatchListener);
    }

    @Override // antlr.Parser
    public void removeParserTokenListener(ParserTokenListener parserTokenListener) {
        this.parserEventSupport.removeParserTokenListener(parserTokenListener);
    }

    @Override // antlr.Parser
    public void removeSemanticPredicateListener(SemanticPredicateListener semanticPredicateListener) {
        this.parserEventSupport.removeSemanticPredicateListener(semanticPredicateListener);
    }

    @Override // antlr.Parser
    public void removeSyntacticPredicateListener(SyntacticPredicateListener syntacticPredicateListener) {
        this.parserEventSupport.removeSyntacticPredicateListener(syntacticPredicateListener);
    }

    @Override // antlr.Parser
    public void removeTraceListener(TraceListener traceListener) {
        this.parserEventSupport.removeTraceListener(traceListener);
    }

    @Override // antlr.Parser
    public void reportError(RecognitionException recognitionException) {
        this.parserEventSupport.fireReportError(recognitionException);
        super.reportError(recognitionException);
    }

    @Override // antlr.Parser
    public void reportError(String str) {
        this.parserEventSupport.fireReportError(str);
        super.reportError(str);
    }

    @Override // antlr.Parser
    public void reportWarning(String str) {
        this.parserEventSupport.fireReportWarning(str);
        super.reportWarning(str);
    }

    @Override // antlr.Parser
    public void setDebugMode(boolean z) {
        this._notDebugMode = !z;
    }

    public void setupDebugging(TokenBuffer tokenBuffer) {
        setupDebugging(null, tokenBuffer);
    }

    public void setupDebugging(TokenStream tokenStream) {
        setupDebugging(tokenStream, null);
    }

    public void setupDebugging(TokenStream tokenStream, TokenBuffer tokenBuffer) {
        setDebugMode(true);
        try {
            try {
                Utils.loadClass("javax.swing.JButton");
            } catch (ClassNotFoundException unused) {
                System.err.println("Swing is required to use ParseView, but is not present in your CLASSPATH");
                System.exit(1);
            }
            Utils.loadClass("antlr.parseview.ParseView").getConstructor(LLkDebuggingParser.class, TokenStream.class, TokenBuffer.class).newInstance(this, tokenStream, tokenBuffer);
        } catch (Exception e) {
            System.err.println("Error initializing ParseView: " + e);
            System.err.println("Please report this to Scott Stanchfield, thetick@magelang.com");
            System.exit(1);
        }
    }

    public synchronized void wakeUp() {
        notify();
    }
}
