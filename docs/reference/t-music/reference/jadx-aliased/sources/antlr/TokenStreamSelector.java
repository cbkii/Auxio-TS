package antlr;

import antlr.ASdebug.ASDebugStream;
import antlr.ASdebug.IASDebugStream;
import antlr.ASdebug.TokenOffsetInfo;
import antlr.collections.Stack;
import antlr.collections.impl.LList;
import java.util.Hashtable;
import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class TokenStreamSelector implements TokenStream, IASDebugStream {
    public TokenStream input;
    public Stack streamStack = new LList();
    public Hashtable inputStreamNames = new Hashtable();

    public void addInputStream(TokenStream tokenStream, String str) {
        this.inputStreamNames.put(str, tokenStream);
    }

    public TokenStream getCurrentStream() {
        return this.input;
    }

    @Override // antlr.ASdebug.IASDebugStream
    public String getEntireText() {
        return ASDebugStream.getEntireText(this.input);
    }

    @Override // antlr.ASdebug.IASDebugStream
    public TokenOffsetInfo getOffsetInfo(Token token) {
        return ASDebugStream.getOffsetInfo(this.input, token);
    }

    public TokenStream getStream(String str) {
        TokenStream tokenStream = (TokenStream) this.inputStreamNames.get(str);
        if (tokenStream != null) {
            return tokenStream;
        }
        throw new IllegalArgumentException(C0000a.m2a("TokenStream ", str, " not found"));
    }

    @Override // antlr.TokenStream
    public Token nextToken() {
        while (true) {
            try {
                return this.input.nextToken();
            } catch (TokenStreamRetryException unused) {
            }
        }
    }

    public TokenStream pop() {
        TokenStream tokenStream = (TokenStream) this.streamStack.pop();
        select(tokenStream);
        return tokenStream;
    }

    public void push(TokenStream tokenStream) {
        this.streamStack.push(this.input);
        select(tokenStream);
    }

    public void push(String str) {
        this.streamStack.push(this.input);
        select(str);
    }

    public void retry() {
        throw new TokenStreamRetryException();
    }

    public void select(TokenStream tokenStream) {
        this.input = tokenStream;
    }

    public void select(String str) {
        this.input = getStream(str);
    }
}
