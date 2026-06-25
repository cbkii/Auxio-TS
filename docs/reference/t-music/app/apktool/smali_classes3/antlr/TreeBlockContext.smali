.class public Lantlr/TreeBlockContext;
.super Lantlr/BlockContext;
.source ""


# instance fields
.field public nextElementIsRoot:Z


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lantlr/BlockContext;-><init>()V

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/TreeBlockContext;->nextElementIsRoot:Z

    return-void
.end method


# virtual methods
.method public addAlternativeElement(Lantlr/AlternativeElement;)V
    .locals 2

    iget-object v0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    check-cast v0, Lantlr/TreeElement;

    iget-boolean v1, p0, Lantlr/TreeBlockContext;->nextElementIsRoot:Z

    if-eqz v1, :cond_0

    check-cast p1, Lantlr/GrammarAtom;

    iput-object p1, v0, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/TreeBlockContext;->nextElementIsRoot:Z

    goto :goto_0

    :cond_0
    invoke-super {p0, p1}, Lantlr/BlockContext;->addAlternativeElement(Lantlr/AlternativeElement;)V

    :goto_0
    return-void
.end method
