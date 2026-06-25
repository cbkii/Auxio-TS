package antlr;

import antlr.collections.impl.Vector;
import java.util.Enumeration;

/* JADX INFO: loaded from: classes3.dex */
public interface TokenManager {
    Object clone();

    void define(TokenSymbol tokenSymbol);

    String getName();

    String getTokenStringAt(int i);

    TokenSymbol getTokenSymbol(String str);

    TokenSymbol getTokenSymbolAt(int i);

    Enumeration getTokenSymbolElements();

    Enumeration getTokenSymbolKeys();

    Vector getVocabulary();

    boolean isReadOnly();

    void mapToTokenSymbol(String str, TokenSymbol tokenSymbol);

    int maxTokenType();

    int nextTokenType();

    void setName(String str);

    void setReadOnly(boolean z);

    boolean tokenDefined(String str);
}
