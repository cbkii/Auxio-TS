.class public Lantlr/Lookahead;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/lang/Cloneable;


# instance fields
.field public cycle:Ljava/lang/String;

.field public epsilonDepth:Lantlr/collections/impl/BitSet;

.field public fset:Lantlr/collections/impl/BitSet;

.field public hasEpsilon:Z


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/Lookahead;->hasEpsilon:Z

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-direct {v0}, Lantlr/collections/impl/BitSet;-><init>()V

    iput-object v0, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/collections/impl/BitSet;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/Lookahead;->hasEpsilon:Z

    iput-object p1, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0}, Lantlr/Lookahead;-><init>()V

    iput-object p1, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    return-void
.end method

.method public static of(I)Lantlr/Lookahead;
    .locals 2

    new-instance v0, Lantlr/Lookahead;

    invoke-direct {v0}, Lantlr/Lookahead;-><init>()V

    iget-object v1, v0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v1, p0}, Lantlr/collections/impl/BitSet;->add(I)V

    return-object v0
.end method


# virtual methods
.method public clone()Ljava/lang/Object;
    .locals 2

    :try_start_0
    invoke-super {p0}, Ljava/lang/Object;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Lookahead;

    iget-object v1, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v1}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/collections/impl/BitSet;

    iput-object v1, v0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    iget-object v1, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    iput-object v1, v0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    iget-object v1, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    if-eqz v1, :cond_0

    iget-object p0, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/collections/impl/BitSet;

    iput-object p0, v0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;
    :try_end_0
    .catch Ljava/lang/CloneNotSupportedException; {:try_start_0 .. :try_end_0} :catch_0

    :cond_0
    return-object v0

    :catch_0
    new-instance p0, Ljava/lang/InternalError;

    invoke-direct {p0}, Ljava/lang/InternalError;-><init>()V

    throw p0
.end method

.method public combineWith(Lantlr/Lookahead;)V
    .locals 2

    iget-object v0, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    if-nez v0, :cond_0

    iget-object v0, p1, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    iput-object v0, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    :cond_0
    invoke-virtual {p1}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v0

    if-eqz v0, :cond_1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/Lookahead;->hasEpsilon:Z

    :cond_1
    iget-object v0, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    if-eqz v0, :cond_2

    iget-object v1, p1, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    if-eqz v1, :cond_3

    invoke-virtual {v0, v1}, Lantlr/collections/impl/BitSet;->orInPlace(Lantlr/collections/impl/BitSet;)V

    goto :goto_0

    :cond_2
    iget-object v0, p1, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    if-eqz v0, :cond_3

    invoke-virtual {v0}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/collections/impl/BitSet;

    iput-object v0, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    :cond_3
    :goto_0
    iget-object p0, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    iget-object p1, p1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->orInPlace(Lantlr/collections/impl/BitSet;)V

    return-void
.end method

.method public containsEpsilon()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/Lookahead;->hasEpsilon:Z

    return p0
.end method

.method public intersection(Lantlr/Lookahead;)Lantlr/Lookahead;
    .locals 3

    new-instance v0, Lantlr/Lookahead;

    iget-object v1, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    iget-object v2, p1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v1, v2}, Lantlr/collections/impl/BitSet;->and(Lantlr/collections/impl/BitSet;)Lantlr/collections/impl/BitSet;

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/Lookahead;-><init>(Lantlr/collections/impl/BitSet;)V

    iget-boolean p0, p0, Lantlr/Lookahead;->hasEpsilon:Z

    if-eqz p0, :cond_0

    iget-boolean p0, p1, Lantlr/Lookahead;->hasEpsilon:Z

    if-eqz p0, :cond_0

    invoke-virtual {v0}, Lantlr/Lookahead;->setEpsilon()V

    :cond_0
    return-object v0
.end method

.method public nil()Z
    .locals 1

    iget-object v0, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0}, Lantlr/collections/impl/BitSet;->nil()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-boolean p0, p0, Lantlr/Lookahead;->hasEpsilon:Z

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public resetEpsilon()V
    .locals 1

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/Lookahead;->hasEpsilon:Z

    return-void
.end method

.method public setEpsilon()V
    .locals 1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/Lookahead;->hasEpsilon:Z

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 7

    iget-object v0, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    const-string v1, ","

    invoke-virtual {v0, v1}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v2

    const-string v3, ""

    if-eqz v2, :cond_0

    const-string v2, "+<epsilon>"

    goto :goto_0

    :cond_0
    move-object v2, v3

    :goto_0
    iget-object v4, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    if-eqz v4, :cond_1

    const-string v4, "; FOLLOW("

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-object v5, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    const-string v6, ")"

    invoke-static {v4, v5, v6}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v4

    goto :goto_1

    :cond_1
    move-object v4, v3

    :goto_1
    iget-object v5, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    if-eqz v5, :cond_2

    const-string v3, "; depths="

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object p0, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    :cond_2
    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toString(Ljava/lang/String;Lantlr/CharFormatter;)Ljava/lang/String;
    .locals 4

    iget-object v0, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, p1, p2}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;Lantlr/CharFormatter;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result p2

    const-string v0, ""

    if-eqz p2, :cond_0

    const-string p2, "+<epsilon>"

    goto :goto_0

    :cond_0
    move-object p2, v0

    :goto_0
    iget-object v1, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    if-eqz v1, :cond_1

    const-string v1, "; FOLLOW("

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v2, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    const-string v3, ")"

    invoke-static {v1, v2, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    goto :goto_1

    :cond_1
    move-object v1, v0

    :goto_1
    iget-object v2, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    if-eqz v2, :cond_2

    const-string v0, "; depths="

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    const-string v2, ","

    invoke-virtual {p0, v2}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_2
    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;
    .locals 1

    instance-of v0, p3, Lantlr/LexerGrammar;

    if-eqz v0, :cond_0

    invoke-virtual {p0, p1, p2}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    iget-object p2, p3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object p2

    invoke-virtual {p0, p1, p2}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/collections/impl/Vector;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toString(Ljava/lang/String;Lantlr/collections/impl/Vector;)Ljava/lang/String;
    .locals 3

    iget-object v0, p0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, p1, p2}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;Lantlr/collections/impl/Vector;)Ljava/lang/String;

    move-result-object p1

    iget-object p2, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    const-string v0, ""

    if-eqz p2, :cond_0

    const-string p2, "; FOLLOW("

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    iget-object v1, p0, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    const-string v2, ")"

    invoke-static {p2, v1, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p2

    goto :goto_0

    :cond_0
    move-object p2, v0

    :goto_0
    iget-object v1, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    if-eqz v1, :cond_1

    const-string v0, "; depths="

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    const-string v1, ","

    invoke-virtual {p0, v1}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_1
    invoke-static {p1, p2, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
