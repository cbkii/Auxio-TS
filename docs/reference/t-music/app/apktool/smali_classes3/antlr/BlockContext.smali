.class public Lantlr/BlockContext;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public altNum:I

.field public block:Lantlr/AlternativeBlock;

.field public blockEnd:Lantlr/BlockEndElement;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public addAlternativeElement(Lantlr/AlternativeElement;)V
    .locals 0

    invoke-virtual {p0}, Lantlr/BlockContext;->currentAlt()Lantlr/Alternative;

    move-result-object p0

    invoke-virtual {p0, p1}, Lantlr/Alternative;->addElement(Lantlr/AlternativeElement;)V

    return-void
.end method

.method public currentAlt()Lantlr/Alternative;
    .locals 1

    iget-object v0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iget-object v0, v0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    iget p0, p0, Lantlr/BlockContext;->altNum:I

    invoke-virtual {v0, p0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/Alternative;

    return-object p0
.end method

.method public currentElement()Lantlr/AlternativeElement;
    .locals 0

    invoke-virtual {p0}, Lantlr/BlockContext;->currentAlt()Lantlr/Alternative;

    move-result-object p0

    iget-object p0, p0, Lantlr/Alternative;->tail:Lantlr/AlternativeElement;

    return-object p0
.end method
