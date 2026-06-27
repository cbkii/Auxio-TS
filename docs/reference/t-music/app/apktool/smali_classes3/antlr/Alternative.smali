.class public Lantlr/Alternative;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public cache:[Lantlr/Lookahead;

.field public doAutoGen:Z

.field public exceptionSpec:Lantlr/ExceptionSpec;

.field public head:Lantlr/AlternativeElement;

.field public lookaheadDepth:I

.field public semPred:Ljava/lang/String;

.field public synPred:Lantlr/SynPredBlock;

.field public tail:Lantlr/AlternativeElement;

.field public treeSpecifier:Lantlr/Token;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/Alternative;->treeSpecifier:Lantlr/Token;

    return-void
.end method

.method public constructor <init>(Lantlr/AlternativeElement;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/Alternative;->treeSpecifier:Lantlr/Token;

    invoke-virtual {p0, p1}, Lantlr/Alternative;->addElement(Lantlr/AlternativeElement;)V

    return-void
.end method


# virtual methods
.method public addElement(Lantlr/AlternativeElement;)V
    .locals 1

    iget-object v0, p0, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    if-nez v0, :cond_0

    iput-object p1, p0, Lantlr/Alternative;->tail:Lantlr/AlternativeElement;

    iput-object p1, p0, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    goto :goto_0

    :cond_0
    iget-object v0, p0, Lantlr/Alternative;->tail:Lantlr/AlternativeElement;

    iput-object p1, v0, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    iput-object p1, p0, Lantlr/Alternative;->tail:Lantlr/AlternativeElement;

    :goto_0
    return-void
.end method

.method public atStart()Z
    .locals 0

    iget-object p0, p0, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public getAutoGen()Z
    .locals 1

    iget-boolean v0, p0, Lantlr/Alternative;->doAutoGen:Z

    if-eqz v0, :cond_0

    iget-object p0, p0, Lantlr/Alternative;->treeSpecifier:Lantlr/Token;

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public getTreeSpecifier()Lantlr/Token;
    .locals 0

    iget-object p0, p0, Lantlr/Alternative;->treeSpecifier:Lantlr/Token;

    return-object p0
.end method

.method public setAutoGen(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/Alternative;->doAutoGen:Z

    return-void
.end method
