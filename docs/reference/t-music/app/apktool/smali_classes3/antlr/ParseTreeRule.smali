.class public Lantlr/ParseTreeRule;
.super Lantlr/ParseTree;
.source ""


# static fields
.field public static final INVALID_ALT:I = -0x1


# instance fields
.field public altNumber:I

.field public ruleName:Ljava/lang/String;


# direct methods
.method public constructor <init>(Ljava/lang/String;)V
    .locals 1

    const/4 v0, -0x1

    invoke-direct {p0, p1, v0}, Lantlr/ParseTreeRule;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0}, Lantlr/ParseTree;-><init>()V

    iput-object p1, p0, Lantlr/ParseTreeRule;->ruleName:Ljava/lang/String;

    iput p2, p0, Lantlr/ParseTreeRule;->altNumber:I

    return-void
.end method


# virtual methods
.method public getLeftmostDerivation(Ljava/lang/StringBuffer;I)I
    .locals 4

    const/16 v0, 0x20

    if-gtz p2, :cond_0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    invoke-virtual {p0}, Lantlr/ParseTreeRule;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const/4 p0, 0x0

    return p0

    :cond_0
    invoke-virtual {p0}, Lantlr/BaseAST;->getFirstChild()Lantlr/collections/AST;

    move-result-object p0

    const/4 v1, 0x1

    :goto_0
    if-eqz p0, :cond_3

    if-ge v1, p2, :cond_2

    instance-of v2, p0, Lantlr/ParseTreeToken;

    if-eqz v2, :cond_1

    goto :goto_1

    :cond_1
    sub-int v2, p2, v1

    move-object v3, p0

    check-cast v3, Lantlr/ParseTree;

    invoke-virtual {v3, p1, v2}, Lantlr/ParseTree;->getLeftmostDerivation(Ljava/lang/StringBuffer;I)I

    move-result v2

    add-int/2addr v2, v1

    move v1, v2

    goto :goto_2

    :cond_2
    :goto_1
    invoke-virtual {p1, v0}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    invoke-interface {p0}, Lantlr/collections/AST;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p1, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :goto_2
    invoke-interface {p0}, Lantlr/collections/AST;->getNextSibling()Lantlr/collections/AST;

    move-result-object p0

    goto :goto_0

    :cond_3
    return v1
.end method

.method public getRuleName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/ParseTreeRule;->ruleName:Ljava/lang/String;

    return-object p0
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    iget v0, p0, Lantlr/ParseTreeRule;->altNumber:I

    const/16 v1, 0x3c

    const/4 v2, -0x1

    if-ne v0, v2, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lantlr/ParseTreeRule;->ruleName:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/16 p0, 0x3e

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    :goto_0
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lantlr/ParseTreeRule;->ruleName:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "["

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget p0, p0, Lantlr/ParseTreeRule;->altNumber:I

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "]>"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0
.end method
