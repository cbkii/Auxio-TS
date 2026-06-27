.class public Lantlr/ASTPair;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public child:Lantlr/collections/AST;

.field public root:Lantlr/collections/AST;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final advanceChildToEnd()V
    .locals 1

    iget-object v0, p0, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    if-eqz v0, :cond_0

    :goto_0
    iget-object v0, p0, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    invoke-interface {v0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    invoke-interface {v0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object v0

    iput-object v0, p0, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    goto :goto_0

    :cond_0
    return-void
.end method

.method public copy()Lantlr/ASTPair;
    .locals 2

    new-instance v0, Lantlr/ASTPair;

    invoke-direct {v0}, Lantlr/ASTPair;-><init>()V

    iget-object v1, p0, Lantlr/ASTPair;->root:Lantlr/collections/AST;

    iput-object v1, v0, Lantlr/ASTPair;->root:Lantlr/collections/AST;

    iget-object p0, p0, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    iput-object p0, v0, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    return-object v0
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    iget-object v0, p0, Lantlr/ASTPair;->root:Lantlr/collections/AST;

    const-string v1, "null"

    if-nez v0, :cond_0

    move-object v0, v1

    goto :goto_0

    :cond_0
    invoke-interface {v0}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v0

    :goto_0
    iget-object p0, p0, Lantlr/ASTPair;->child:Lantlr/collections/AST;

    if-nez p0, :cond_1

    goto :goto_1

    :cond_1
    invoke-interface {p0}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v1

    :goto_1
    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "["

    invoke-virtual {p0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, ","

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "]"

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
