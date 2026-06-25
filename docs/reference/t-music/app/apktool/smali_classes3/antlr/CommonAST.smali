.class public Lantlr/CommonAST;
.super Lantlr/BaseAST;
.source ""


# instance fields
.field public text:Ljava/lang/String;

.field public ttype:I


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lantlr/BaseAST;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CommonAST;->ttype:I

    return-void
.end method

.method public constructor <init>(Lantlr/Token;)V
    .locals 1

    invoke-direct {p0}, Lantlr/BaseAST;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CommonAST;->ttype:I

    invoke-virtual {p0, p1}, Lantlr/CommonAST;->initialize(Lantlr/Token;)V

    return-void
.end method


# virtual methods
.method public getText()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/CommonAST;->text:Ljava/lang/String;

    return-object p0
.end method

.method public getType()I
    .locals 0

    iget p0, p0, Lantlr/CommonAST;->ttype:I

    return p0
.end method

.method public initialize(ILjava/lang/String;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/CommonAST;->setType(I)V

    invoke-virtual {p0, p2}, Lantlr/CommonAST;->setText(Ljava/lang/String;)V

    return-void
.end method

.method public initialize(Lantlr/Token;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CommonAST;->setText(Ljava/lang/String;)V

    invoke-virtual {p1}, Lantlr/Token;->getType()I

    move-result p1

    invoke-virtual {p0, p1}, Lantlr/CommonAST;->setType(I)V

    return-void
.end method

.method public initialize(Lantlr/collections/AST;)V
    .locals 1

    invoke-interface {p1}, Lantlr/collections/AST;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/CommonAST;->setText(Ljava/lang/String;)V

    invoke-interface {p1}, Lantlr/collections/AST;->getType()I

    move-result p1

    invoke-virtual {p0, p1}, Lantlr/CommonAST;->setType(I)V

    return-void
.end method

.method public setText(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/CommonAST;->text:Ljava/lang/String;

    return-void
.end method

.method public setType(I)V
    .locals 0

    iput p1, p0, Lantlr/CommonAST;->ttype:I

    return-void
.end method
