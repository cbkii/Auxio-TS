package antlr;

import antlr.collections.AST;
import antlr.collections.impl.BitSet;
import java.io.PrintStream;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
public class TreeParser {
    public static ASTNULLType ASTNULL = new ASTNULLType();
    public AST _retTree;
    public AST returnAST;
    public String[] tokenNames;
    public ASTFactory astFactory = new ASTFactory();
    public int traceDepth = 0;
    public TreeParserSharedInputState inputState = new TreeParserSharedInputState();

    public static void panic() {
        System.err.println("TreeWalker: panic");
        Utils.error("");
        throw null;
    }

    public AST getAST() {
        return this.returnAST;
    }

    public ASTFactory getASTFactory() {
        return this.astFactory;
    }

    public String getTokenName(int i) {
        return this.tokenNames[i];
    }

    public String[] getTokenNames() {
        return this.tokenNames;
    }

    public void match(AST ast, int i) {
        if (ast == null || ast == ASTNULL || ast.getType() != i) {
            throw new MismatchedTokenException(getTokenNames(), ast, i, false);
        }
    }

    public void match(AST ast, BitSet bitSet) {
        if (ast == null || ast == ASTNULL || !bitSet.member(ast.getType())) {
            throw new MismatchedTokenException(getTokenNames(), ast, bitSet, false);
        }
    }

    public void matchNot(AST ast, int i) {
        if (ast == null || ast == ASTNULL || ast.getType() == i) {
            throw new MismatchedTokenException(getTokenNames(), ast, i, true);
        }
    }

    public void reportError(RecognitionException recognitionException) {
        System.err.println(recognitionException.toString());
    }

    public void reportError(String str) {
        System.err.println("error: " + str);
    }

    public void reportWarning(String str) {
        System.err.println("warning: " + str);
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

    public void traceIn(String str, AST ast) {
        this.traceDepth++;
        traceIndent();
        PrintStream printStream = System.out;
        StringBuilder m10b = C0000a.m10b("> ", str, "(");
        m10b.append(ast != null ? ast.toString() : "null");
        m10b.append(")");
        m10b.append(this.inputState.guessing > 0 ? " [guessing]" : "");
        printStream.println(m10b.toString());
    }

    public void traceIndent() {
        for (int i = 0; i < this.traceDepth; i++) {
            System.out.print(" ");
        }
    }

    public void traceOut(String str, AST ast) {
        traceIndent();
        PrintStream printStream = System.out;
        StringBuilder m10b = C0000a.m10b("< ", str, "(");
        m10b.append(ast != null ? ast.toString() : "null");
        m10b.append(")");
        m10b.append(this.inputState.guessing > 0 ? " [guessing]" : "");
        printStream.println(m10b.toString());
        this.traceDepth--;
    }
}
