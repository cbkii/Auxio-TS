.class public Lantlr/CommonASTWithHiddenTokens;
.super Lantlr/CommonAST;
.source ""


# instance fields
.field public hiddenAfter:Lantlr/CommonHiddenStreamToken;

.field public hiddenBefore:Lantlr/CommonHiddenStreamToken;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lantlr/CommonAST;-><init>()V

    return-void
.end method

.method public constructor <init>(Lantlr/Token;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/CommonAST;-><init>(Lantlr/Token;)V

    return-void
.end method


# virtual methods
.method public getHiddenAfter()Lantlr/CommonHiddenStreamToken;
    .locals 0

    iget-object p0, p0, Lantlr/CommonASTWithHiddenTokens;->hiddenAfter:Lantlr/CommonHiddenStreamToken;

    return-object p0
.end method

.method public getHiddenBefore()Lantlr/CommonHiddenStreamToken;
    .locals 0

    iget-object p0, p0, Lantlr/CommonASTWithHiddenTokens;->hiddenBefore:Lantlr/CommonHiddenStreamToken;

    return-object p0
.end method

.method public initialize(Lantlr/Token;)V
    .locals 1

    check-cast p1, Lantlr/CommonHiddenStreamToken;

    invoke-super {p0, p1}, Lantlr/CommonAST;->initialize(Lantlr/Token;)V

    invoke-virtual {p1}, Lantlr/CommonHiddenStreamToken;->getHiddenBefore()Lantlr/CommonHiddenStreamToken;

    move-result-object v0

    iput-object v0, p0, Lantlr/CommonASTWithHiddenTokens;->hiddenBefore:Lantlr/CommonHiddenStreamToken;

    invoke-virtual {p1}, Lantlr/CommonHiddenStreamToken;->getHiddenAfter()Lantlr/CommonHiddenStreamToken;

    move-result-object p1

    iput-object p1, p0, Lantlr/CommonASTWithHiddenTokens;->hiddenAfter:Lantlr/CommonHiddenStreamToken;

    return-void
.end method

.method public initialize(Lantlr/collections/AST;)V
    .locals 2

    move-object v0, p1

    check-cast v0, Lantlr/CommonASTWithHiddenTokens;

    invoke-virtual {v0}, Lantlr/CommonASTWithHiddenTokens;->getHiddenBefore()Lantlr/CommonHiddenStreamToken;

    move-result-object v1

    iput-object v1, p0, Lantlr/CommonASTWithHiddenTokens;->hiddenBefore:Lantlr/CommonHiddenStreamToken;

    invoke-virtual {v0}, Lantlr/CommonASTWithHiddenTokens;->getHiddenAfter()Lantlr/CommonHiddenStreamToken;

    move-result-object v0

    iput-object v0, p0, Lantlr/CommonASTWithHiddenTokens;->hiddenAfter:Lantlr/CommonHiddenStreamToken;

    invoke-super {p0, p1}, Lantlr/CommonAST;->initialize(Lantlr/collections/AST;)V

    return-void
.end method
