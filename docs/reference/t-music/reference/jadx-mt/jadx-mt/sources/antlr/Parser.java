package antlr;

import antlr.collections.AST;
import antlr.collections.impl.BitSet;
import antlr.debug.MessageListener;
import antlr.debug.ParserListener;
import antlr.debug.ParserMatchListener;
import antlr.debug.ParserTokenListener;
import antlr.debug.SemanticPredicateListener;
import antlr.debug.SyntacticPredicateListener;
import antlr.debug.TraceListener;
import java.io.PrintStream;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public abstract class Parser {
    public ASTFactory astFactory;
    public boolean ignoreInvalidDebugCalls;
    public ParserSharedInputState inputState;
    public AST returnAST;
    public String[] tokenNames;
    public Hashtable tokenTypeToASTClassMap;
    public int traceDepth;

    public Parser() {
        this(new ParserSharedInputState());
    }

    public Parser(ParserSharedInputState parserSharedInputState) {
        this.astFactory = null;
        this.tokenTypeToASTClassMap = null;
        this.ignoreInvalidDebugCalls = false;
        this.traceDepth = 0;
        this.inputState = parserSharedInputState;
    }

    public static void panic() {
        System.err.println("Parser: panic");
        System.exit(1);
    }

    /* JADX INFO: renamed from: LA */
    public abstract int mo107LA(int i);

    /* JADX INFO: renamed from: LT */
    public abstract Token mo108LT(int i);

    public void addMessageListener(MessageListener messageListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("addMessageListener() is only valid if parser built for debugging");
        }
    }

    public void addParserListener(ParserListener parserListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("addParserListener() is only valid if parser built for debugging");
        }
    }

    public void addParserMatchListener(ParserMatchListener parserMatchListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("addParserMatchListener() is only valid if parser built for debugging");
        }
    }

    public void addParserTokenListener(ParserTokenListener parserTokenListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("addParserTokenListener() is only valid if parser built for debugging");
        }
    }

    public void addSemanticPredicateListener(SemanticPredicateListener semanticPredicateListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("addSemanticPredicateListener() is only valid if parser built for debugging");
        }
    }

    public void addSyntacticPredicateListener(SyntacticPredicateListener syntacticPredicateListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("addSyntacticPredicateListener() is only valid if parser built for debugging");
        }
    }

    public void addTraceListener(TraceListener traceListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("addTraceListener() is only valid if parser built for debugging");
        }
    }

    public abstract void consume();

    public void consumeUntil(int i) {
        while (mo107LA(1) != 1 && mo107LA(1) != i) {
            consume();
        }
    }

    public void consumeUntil(BitSet bitSet) {
        while (mo107LA(1) != 1 && !bitSet.member(mo107LA(1))) {
            consume();
        }
    }

    public void defaultDebuggingSetup(TokenStream tokenStream, TokenBuffer tokenBuffer) {
    }

    public AST getAST() {
        return this.returnAST;
    }

    public ASTFactory getASTFactory() {
        return this.astFactory;
    }

    public String getFilename() {
        return this.inputState.filename;
    }

    public ParserSharedInputState getInputState() {
        return this.inputState;
    }

    public String getTokenName(int i) {
        return this.tokenNames[i];
    }

    public String[] getTokenNames() {
        return this.tokenNames;
    }

    public Hashtable getTokenTypeToASTClassMap() {
        return this.tokenTypeToASTClassMap;
    }

    public boolean isDebugMode() {
        return false;
    }

    public int mark() {
        return this.inputState.input.mark();
    }

    public void match(int i) {
        if (mo107LA(1) != i) {
            throw new MismatchedTokenException(this.tokenNames, mo108LT(1), i, false, getFilename());
        }
        consume();
    }

    public void match(BitSet bitSet) throws MismatchedTokenException {
        if (!bitSet.member(mo107LA(1))) {
            throw new MismatchedTokenException(this.tokenNames, mo108LT(1), bitSet, false, getFilename());
        }
        consume();
    }

    public void matchNot(int i) throws MismatchedTokenException {
        if (mo107LA(1) == i) {
            throw new MismatchedTokenException(this.tokenNames, mo108LT(1), i, true, getFilename());
        }
        consume();
    }

    public void recover(RecognitionException recognitionException, BitSet bitSet) {
        consume();
        consumeUntil(bitSet);
    }

    public void removeMessageListener(MessageListener messageListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new RuntimeException("removeMessageListener() is only valid if parser built for debugging");
        }
    }

    public void removeParserListener(ParserListener parserListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new RuntimeException("removeParserListener() is only valid if parser built for debugging");
        }
    }

    public void removeParserMatchListener(ParserMatchListener parserMatchListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new RuntimeException("removeParserMatchListener() is only valid if parser built for debugging");
        }
    }

    public void removeParserTokenListener(ParserTokenListener parserTokenListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new RuntimeException("removeParserTokenListener() is only valid if parser built for debugging");
        }
    }

    public void removeSemanticPredicateListener(SemanticPredicateListener semanticPredicateListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("removeSemanticPredicateListener() is only valid if parser built for debugging");
        }
    }

    public void removeSyntacticPredicateListener(SyntacticPredicateListener syntacticPredicateListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new IllegalArgumentException("removeSyntacticPredicateListener() is only valid if parser built for debugging");
        }
    }

    public void removeTraceListener(TraceListener traceListener) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new RuntimeException("removeTraceListener() is only valid if parser built for debugging");
        }
    }

    public void reportError(RecognitionException recognitionException) {
        System.err.println(recognitionException);
    }

    public void reportError(String str) {
        if (getFilename() == null) {
            System.err.println("error: " + str);
            return;
        }
        System.err.println(getFilename() + ": error: " + str);
    }

    public void reportWarning(String str) {
        if (getFilename() == null) {
            System.err.println("warning: " + str);
            return;
        }
        System.err.println(getFilename() + ": warning: " + str);
    }

    public void rewind(int i) {
        this.inputState.input.rewind(i);
    }

    public void setASTFactory(ASTFactory aSTFactory) {
        this.astFactory = aSTFactory;
    }

    public void setASTNodeClass(String str) {
        this.astFactory.setASTNodeType(str);
    }

    public void setASTNodeType(String str) {
        setASTNodeClass(str);
    }

    public void setDebugMode(boolean z) {
        if (!this.ignoreInvalidDebugCalls) {
            throw new RuntimeException("setDebugMode() only valid if parser built for debugging");
        }
    }

    public void setFilename(String str) {
        this.inputState.filename = str;
    }

    public void setIgnoreInvalidDebugCalls(boolean z) {
        this.ignoreInvalidDebugCalls = z;
    }

    public void setInputState(ParserSharedInputState parserSharedInputState) {
        this.inputState = parserSharedInputState;
    }

    public void setTokenBuffer(TokenBuffer tokenBuffer) {
        this.inputState.input = tokenBuffer;
    }

    public void traceIn(String str) {
        this.traceDepth++;
        traceIndent();
        PrintStream printStream = System.out;
        StringBuilder sbM10b = C0000a.m10b("> ", str, "; LA(1)==");
        sbM10b.append(mo108LT(1).getText());
        sbM10b.append(this.inputState.guessing > 0 ? " [guessing]" : "");
        printStream.println(sbM10b.toString());
    }

    public void traceIndent() {
        for (int i = 0; i < this.traceDepth; i++) {
            System.out.print(" ");
        }
    }

    public void traceOut(String str) {
        traceIndent();
        PrintStream printStream = System.out;
        StringBuilder sbM10b = C0000a.m10b("< ", str, "; LA(1)==");
        sbM10b.append(mo108LT(1).getText());
        sbM10b.append(this.inputState.guessing > 0 ? " [guessing]" : "");
        printStream.println(sbM10b.toString());
        this.traceDepth--;
    }
}
