.class public Lantlr/TreeSpecifierNode;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public firstChild:Lantlr/TreeSpecifierNode;

.field public nextSibling:Lantlr/TreeSpecifierNode;

.field public parent:Lantlr/TreeSpecifierNode;

.field public tok:Lantlr/Token;


# direct methods
.method public constructor <init>(Lantlr/Token;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/TreeSpecifierNode;->parent:Lantlr/TreeSpecifierNode;

    iput-object v0, p0, Lantlr/TreeSpecifierNode;->firstChild:Lantlr/TreeSpecifierNode;

    iput-object v0, p0, Lantlr/TreeSpecifierNode;->nextSibling:Lantlr/TreeSpecifierNode;

    iput-object p1, p0, Lantlr/TreeSpecifierNode;->tok:Lantlr/Token;

    return-void
.end method


# virtual methods
.method public getFirstChild()Lantlr/TreeSpecifierNode;
    .locals 0

    iget-object p0, p0, Lantlr/TreeSpecifierNode;->firstChild:Lantlr/TreeSpecifierNode;

    return-object p0
.end method

.method public getNextSibling()Lantlr/TreeSpecifierNode;
    .locals 0

    iget-object p0, p0, Lantlr/TreeSpecifierNode;->nextSibling:Lantlr/TreeSpecifierNode;

    return-object p0
.end method

.method public getParent()Lantlr/TreeSpecifierNode;
    .locals 0

    iget-object p0, p0, Lantlr/TreeSpecifierNode;->parent:Lantlr/TreeSpecifierNode;

    return-object p0
.end method

.method public getToken()Lantlr/Token;
    .locals 0

    iget-object p0, p0, Lantlr/TreeSpecifierNode;->tok:Lantlr/Token;

    return-object p0
.end method

.method public setFirstChild(Lantlr/TreeSpecifierNode;)V
    .locals 0

    iput-object p1, p0, Lantlr/TreeSpecifierNode;->firstChild:Lantlr/TreeSpecifierNode;

    iput-object p0, p1, Lantlr/TreeSpecifierNode;->parent:Lantlr/TreeSpecifierNode;

    return-void
.end method

.method public setNextSibling(Lantlr/TreeSpecifierNode;)V
    .locals 0

    iput-object p1, p0, Lantlr/TreeSpecifierNode;->nextSibling:Lantlr/TreeSpecifierNode;

    iget-object p0, p0, Lantlr/TreeSpecifierNode;->parent:Lantlr/TreeSpecifierNode;

    iput-object p0, p1, Lantlr/TreeSpecifierNode;->parent:Lantlr/TreeSpecifierNode;

    return-void
.end method
