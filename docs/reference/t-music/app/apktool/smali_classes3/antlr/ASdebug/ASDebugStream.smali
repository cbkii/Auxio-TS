.class public final Lantlr/ASdebug/ASDebugStream;
.super Ljava/lang/Object;
.source ""


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static getEntireText(Lantlr/TokenStream;)Ljava/lang/String;
    .locals 1

    instance-of v0, p0, Lantlr/ASdebug/IASDebugStream;

    if-eqz v0, :cond_0

    check-cast p0, Lantlr/ASdebug/IASDebugStream;

    invoke-interface {p0}, Lantlr/ASdebug/IASDebugStream;->getEntireText()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    const/4 p0, 0x0

    return-object p0
.end method

.method public static getOffsetInfo(Lantlr/TokenStream;Lantlr/Token;)Lantlr/ASdebug/TokenOffsetInfo;
    .locals 1

    instance-of v0, p0, Lantlr/ASdebug/IASDebugStream;

    if-eqz v0, :cond_0

    check-cast p0, Lantlr/ASdebug/IASDebugStream;

    invoke-interface {p0, p1}, Lantlr/ASdebug/IASDebugStream;->getOffsetInfo(Lantlr/Token;)Lantlr/ASdebug/TokenOffsetInfo;

    move-result-object p0

    return-object p0

    :cond_0
    const/4 p0, 0x0

    return-object p0
.end method
