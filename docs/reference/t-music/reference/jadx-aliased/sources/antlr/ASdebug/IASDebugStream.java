package antlr.ASdebug;

import antlr.Token;

/* loaded from: classes3.dex */
public interface IASDebugStream {
    String getEntireText();

    TokenOffsetInfo getOffsetInfo(Token token);
}
