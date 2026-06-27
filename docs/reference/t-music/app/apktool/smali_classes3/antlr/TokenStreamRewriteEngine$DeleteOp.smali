.class public Lantlr/TokenStreamRewriteEngine$DeleteOp;
.super Lantlr/TokenStreamRewriteEngine$ReplaceOp;
.source ""


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lantlr/TokenStreamRewriteEngine;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x9
    name = "DeleteOp"
.end annotation


# direct methods
.method public constructor <init>(II)V
    .locals 1

    const/4 v0, 0x0

    invoke-direct {p0, p1, p2, v0}, Lantlr/TokenStreamRewriteEngine$ReplaceOp;-><init>(IILjava/lang/String;)V

    return-void
.end method
