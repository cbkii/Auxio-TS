package antlr;

import antlr.collections.impl.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

/* loaded from: classes3.dex */
public class SimpleTokenManager implements TokenManager, Cloneable {
    public Tool antlrTool;
    public String name;
    public int maxToken = 4;
    public boolean readOnly = false;
    public Vector vocabulary = new Vector(1);
    public Hashtable table = new Hashtable();

    public SimpleTokenManager(String str, Tool tool) {
        this.antlrTool = tool;
        this.name = str;
        TokenSymbol tokenSymbol = new TokenSymbol("EOF");
        tokenSymbol.setTokenType(1);
        define(tokenSymbol);
        this.vocabulary.ensureCapacity(3);
        this.vocabulary.setElementAt("NULL_TREE_LOOKAHEAD", 3);
    }

    @Override // antlr.TokenManager
    public Object clone() {
        try {
            SimpleTokenManager simpleTokenManager = (SimpleTokenManager) super.clone();
            simpleTokenManager.vocabulary = (Vector) this.vocabulary.clone();
            simpleTokenManager.table = (Hashtable) this.table.clone();
            simpleTokenManager.maxToken = this.maxToken;
            simpleTokenManager.antlrTool = this.antlrTool;
            simpleTokenManager.name = this.name;
            return simpleTokenManager;
        } catch (CloneNotSupportedException unused) {
            this.antlrTool.panic("cannot clone token manager");
            return null;
        }
    }

    @Override // antlr.TokenManager
    public void define(TokenSymbol tokenSymbol) {
        this.vocabulary.ensureCapacity(tokenSymbol.getTokenType());
        this.vocabulary.setElementAt(tokenSymbol.getId(), tokenSymbol.getTokenType());
        mapToTokenSymbol(tokenSymbol.getId(), tokenSymbol);
    }

    @Override // antlr.TokenManager
    public String getName() {
        return this.name;
    }

    @Override // antlr.TokenManager
    public String getTokenStringAt(int i) {
        return (String) this.vocabulary.elementAt(i);
    }

    @Override // antlr.TokenManager
    public TokenSymbol getTokenSymbol(String str) {
        return (TokenSymbol) this.table.get(str);
    }

    @Override // antlr.TokenManager
    public TokenSymbol getTokenSymbolAt(int i) {
        return getTokenSymbol(getTokenStringAt(i));
    }

    @Override // antlr.TokenManager
    public Enumeration getTokenSymbolElements() {
        return this.table.elements();
    }

    @Override // antlr.TokenManager
    public Enumeration getTokenSymbolKeys() {
        return this.table.keys();
    }

    @Override // antlr.TokenManager
    public Vector getVocabulary() {
        return this.vocabulary;
    }

    @Override // antlr.TokenManager
    public boolean isReadOnly() {
        return false;
    }

    @Override // antlr.TokenManager
    public void mapToTokenSymbol(String str, TokenSymbol tokenSymbol) {
        this.table.put(str, tokenSymbol);
    }

    @Override // antlr.TokenManager
    public int maxTokenType() {
        return this.maxToken - 1;
    }

    @Override // antlr.TokenManager
    public int nextTokenType() {
        int i = this.maxToken;
        this.maxToken = i + 1;
        return i;
    }

    @Override // antlr.TokenManager
    public void setName(String str) {
        this.name = str;
    }

    @Override // antlr.TokenManager
    public void setReadOnly(boolean z) {
        this.readOnly = z;
    }

    @Override // antlr.TokenManager
    public boolean tokenDefined(String str) {
        return this.table.containsKey(str);
    }
}
