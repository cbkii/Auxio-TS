.class public Lantlr/TokenStreamRewriteEngine$InsertBeforeOp;
.super Lantlr/TokenStreamRewriteEngine$RewriteOperation;
.source ""


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lantlr/TokenStreamRewriteEngine;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x9
    name = "InsertBeforeOp"
.end annotation


# direct methods
.method public constructor <init>(ILjava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/TokenStreamRewriteEngine$RewriteOperation;-><init>(ILjava/lang/String;)V

    return-void
.end method


# virtual methods
.method public execute(Ljava/lang/StringBuffer;)I
    .locals 1

    iget-object v0, p0, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->text:Ljava/lang/String;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget p0, p0, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    return p0
.end method
