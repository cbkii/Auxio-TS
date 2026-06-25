.class public Lantlr/NoViableAltException;
.super Lantlr/RecognitionException;
.source ""


# instance fields
.field public node:Lantlr/collections/AST;

.field public token:Lantlr/Token;


# direct methods
.method public constructor <init>(Lantlr/Token;Ljava/lang/String;)V
    .locals 3

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v1

    const-string v2, "NoViableAlt"

    invoke-direct {p0, v2, p2, v0, v1}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    iput-object p1, p0, Lantlr/NoViableAltException;->token:Lantlr/Token;

    return-void
.end method

.method public constructor <init>(Lantlr/collections/AST;)V
    .locals 4

    invoke-interface {p1}, Lantlr/collections/AST;->getLine()I

    move-result v0

    invoke-interface {p1}, Lantlr/collections/AST;->getColumn()I

    move-result v1

    const-string v2, "NoViableAlt"

    const-string v3, "<AST>"

    invoke-direct {p0, v2, v3, v0, v1}, Lantlr/RecognitionException;-><init>(Ljava/lang/String;Ljava/lang/String;II)V

    iput-object p1, p0, Lantlr/NoViableAltException;->node:Lantlr/collections/AST;

    return-void
.end method


# virtual methods
.method public getMessage()Ljava/lang/String;
    .locals 2

    iget-object v0, p0, Lantlr/NoViableAltException;->token:Lantlr/Token;

    if-eqz v0, :cond_0

    const-string v0, "unexpected token: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/NoViableAltException;->token:Lantlr/Token;

    invoke-static {p0, v0}, La/a/a/a/a;->a(Lantlr/Token;Ljava/lang/StringBuilder;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    iget-object v0, p0, Lantlr/NoViableAltException;->node:Lantlr/collections/AST;

    sget-object v1, Lantlr/TreeParser;->ASTNULL:Lantlr/ASTNULLType;

    if-ne v0, v1, :cond_1

    const-string p0, "unexpected end of subtree"

    return-object p0

    :cond_1
    const-string v0, "unexpected AST node: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/NoViableAltException;->node:Lantlr/collections/AST;

    invoke-interface {p0}, Lantlr/collections/AST;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
