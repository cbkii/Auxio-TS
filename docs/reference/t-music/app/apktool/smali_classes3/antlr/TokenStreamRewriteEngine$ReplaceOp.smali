.class public Lantlr/TokenStreamRewriteEngine$ReplaceOp;
.super Lantlr/TokenStreamRewriteEngine$RewriteOperation;
.source ""


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lantlr/TokenStreamRewriteEngine;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x9
    name = "ReplaceOp"
.end annotation


# instance fields
.field public lastIndex:I


# direct methods
.method public constructor <init>(IILjava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1, p3}, Lantlr/TokenStreamRewriteEngine$RewriteOperation;-><init>(ILjava/lang/String;)V

    iput p2, p0, Lantlr/TokenStreamRewriteEngine$ReplaceOp;->lastIndex:I

    return-void
.end method


# virtual methods
.method public execute(Ljava/lang/StringBuffer;)I
    .locals 1

    iget-object v0, p0, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->text:Ljava/lang/String;

    if-eqz v0, :cond_0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_0
    iget p0, p0, Lantlr/TokenStreamRewriteEngine$ReplaceOp;->lastIndex:I

    add-int/lit8 p0, p0, 0x1

    return p0
.end method
