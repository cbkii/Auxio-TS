.class public Lantlr/TokenSymbol;
.super Lantlr/GrammarSymbol;
.source ""


# instance fields
.field public ASTNodeType:Ljava/lang/String;

.field public paraphrase:Ljava/lang/String;

.field public ttype:I


# direct methods
.method public constructor <init>(Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/GrammarSymbol;-><init>(Ljava/lang/String;)V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/TokenSymbol;->paraphrase:Ljava/lang/String;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/TokenSymbol;->ttype:I

    return-void
.end method


# virtual methods
.method public getASTNodeType()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TokenSymbol;->ASTNodeType:Ljava/lang/String;

    return-object p0
.end method

.method public getParaphrase()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TokenSymbol;->paraphrase:Ljava/lang/String;

    return-object p0
.end method

.method public getTokenType()I
    .locals 0

    iget p0, p0, Lantlr/TokenSymbol;->ttype:I

    return p0
.end method

.method public setASTNodeType(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/TokenSymbol;->ASTNodeType:Ljava/lang/String;

    return-void
.end method

.method public setParaphrase(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/TokenSymbol;->paraphrase:Ljava/lang/String;

    return-void
.end method

.method public setTokenType(I)V
    .locals 0

    iput p1, p0, Lantlr/TokenSymbol;->ttype:I

    return-void
.end method
