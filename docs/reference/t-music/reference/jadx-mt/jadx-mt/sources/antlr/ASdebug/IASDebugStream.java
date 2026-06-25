package antlr.ASdebug;

import antlr.Token;

/* JADX INFO: loaded from: classes3.dex */
public interface IASDebugStream {
    String getEntireText();

    TokenOffsetInfo getOffsetInfo(Token token);
}
