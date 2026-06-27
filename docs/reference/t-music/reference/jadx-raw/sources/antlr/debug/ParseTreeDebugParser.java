package antlr.debug;

import antlr.CommonToken;
import antlr.LLkParser;
import antlr.ParseTree;
import antlr.ParseTreeRule;
import antlr.ParseTreeToken;
import antlr.ParserSharedInputState;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.collections.impl.BitSet;
import java.util.Stack;

/* loaded from: classes3.dex */
public class ParseTreeDebugParser extends LLkParser {
    public Stack currentParseTreeRoot;
    public ParseTreeRule mostRecentParseTreeRoot;
    public int numberOfDerivationSteps;

    public ParseTreeDebugParser(int i) {
        super(i);
        this.currentParseTreeRoot = new Stack();
        this.mostRecentParseTreeRoot = null;
        this.numberOfDerivationSteps = 1;
    }

    public ParseTreeDebugParser(ParserSharedInputState parserSharedInputState, int i) {
        super(parserSharedInputState, i);
        this.currentParseTreeRoot = new Stack();
        this.mostRecentParseTreeRoot = null;
        this.numberOfDerivationSteps = 1;
    }

    public ParseTreeDebugParser(TokenBuffer tokenBuffer, int i) {
        super(tokenBuffer, i);
        this.currentParseTreeRoot = new Stack();
        this.mostRecentParseTreeRoot = null;
        this.numberOfDerivationSteps = 1;
    }

    public ParseTreeDebugParser(TokenStream tokenStream, int i) {
        super(tokenStream, i);
        this.currentParseTreeRoot = new Stack();
        this.mostRecentParseTreeRoot = null;
        this.numberOfDerivationSteps = 1;
    }

    public void addCurrentTokenToParseTree() {
        if (this.inputState.guessing > 0) {
            return;
        }
        ((ParseTreeRule) this.currentParseTreeRoot.peek()).addChild(mo107LA(1) == 1 ? new ParseTreeToken(new CommonToken("EOF")) : new ParseTreeToken(mo108LT(1)));
    }

    public int getNumberOfDerivationSteps() {
        return this.numberOfDerivationSteps;
    }

    public ParseTree getParseTree() {
        return this.mostRecentParseTreeRoot;
    }

    @Override // antlr.Parser
    public void match(int i) {
        addCurrentTokenToParseTree();
        super.match(i);
    }

    @Override // antlr.Parser
    public void match(BitSet bitSet) {
        addCurrentTokenToParseTree();
        super.match(bitSet);
    }

    @Override // antlr.Parser
    public void matchNot(int i) {
        addCurrentTokenToParseTree();
        super.matchNot(i);
    }

    @Override // antlr.LLkParser, antlr.Parser
    public void traceIn(String str) {
        if (this.inputState.guessing > 0) {
            return;
        }
        ParseTreeRule parseTreeRule = new ParseTreeRule(str);
        if (this.currentParseTreeRoot.size() > 0) {
            ((ParseTreeRule) this.currentParseTreeRoot.peek()).addChild(parseTreeRule);
        }
        this.currentParseTreeRoot.push(parseTreeRule);
        this.numberOfDerivationSteps++;
    }

    @Override // antlr.LLkParser, antlr.Parser
    public void traceOut(String str) {
        if (this.inputState.guessing > 0) {
            return;
        }
        this.mostRecentParseTreeRoot = (ParseTreeRule) this.currentParseTreeRoot.pop();
    }
}
