.class public Lantlr/RuleSymbol;
.super Lantlr/GrammarSymbol;
.source ""


# instance fields
.field public access:Ljava/lang/String;

.field public block:Lantlr/RuleBlock;

.field public comment:Ljava/lang/String;

.field public defined:Z

.field public references:Lantlr/collections/impl/Vector;


# direct methods
.method public constructor <init>(Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/GrammarSymbol;-><init>(Ljava/lang/String;)V

    new-instance p1, Lantlr/collections/impl/Vector;

    invoke-direct {p1}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object p1, p0, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    return-void
.end method


# virtual methods
.method public addReference(Lantlr/RuleRefElement;)V
    .locals 0

    iget-object p0, p0, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    return-void
.end method

.method public getBlock()Lantlr/RuleBlock;
    .locals 0

    iget-object p0, p0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    return-object p0
.end method

.method public getReference(I)Lantlr/RuleRefElement;
    .locals 0

    iget-object p0, p0, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/RuleRefElement;

    return-object p0
.end method

.method public isDefined()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/RuleSymbol;->defined:Z

    return p0
.end method

.method public numReferences()I
    .locals 0

    iget-object p0, p0, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {p0}, Lantlr/collections/impl/Vector;->size()I

    move-result p0

    return p0
.end method

.method public setBlock(Lantlr/RuleBlock;)V
    .locals 0

    iput-object p1, p0, Lantlr/RuleSymbol;->block:Lantlr/RuleBlock;

    return-void
.end method

.method public setDefined()V
    .locals 1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/RuleSymbol;->defined:Z

    return-void
.end method
