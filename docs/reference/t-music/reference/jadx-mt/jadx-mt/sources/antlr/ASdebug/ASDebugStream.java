package antlr.ASdebug;

import antlr.Token;
import antlr.TokenStream;

/* JADX INFO: loaded from: classes3.dex */
public final class ASDebugStream {
    public static String getEntireText(TokenStream tokenStream) {
        if (tokenStream instanceof IASDebugStream) {
            return ((IASDebugStream) tokenStream).getEntireText();
        }
        return null;
    }

    public static TokenOffsetInfo getOffsetInfo(TokenStream tokenStream, Token token) {
        if (tokenStream instanceof IASDebugStream) {
            return ((IASDebugStream) tokenStream).getOffsetInfo(token);
        }
        return null;
    }
}
