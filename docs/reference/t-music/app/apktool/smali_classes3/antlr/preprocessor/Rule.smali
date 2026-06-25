.class public Lantlr/preprocessor/Rule;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public args:Ljava/lang/String;

.field public bang:Z

.field public block:Ljava/lang/String;

.field public enclosingGrammar:Lantlr/preprocessor/Grammar;

.field public initAction:Ljava/lang/String;

.field public name:Ljava/lang/String;

.field public options:Lantlr/collections/impl/IndexedVector;

.field public returnValue:Ljava/lang/String;

.field public throwsSpec:Ljava/lang/String;

.field public visibility:Ljava/lang/String;


# direct methods
.method public constructor <init>(Ljava/lang/String;Ljava/lang/String;Lantlr/collections/impl/IndexedVector;Lantlr/preprocessor/Grammar;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/preprocessor/Rule;->bang:Z

    iput-object p1, p0, Lantlr/preprocessor/Rule;->name:Ljava/lang/String;

    iput-object p2, p0, Lantlr/preprocessor/Rule;->block:Ljava/lang/String;

    iput-object p3, p0, Lantlr/preprocessor/Rule;->options:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {p0, p4}, Lantlr/preprocessor/Rule;->setEnclosingGrammar(Lantlr/preprocessor/Grammar;)V

    return-void
.end method


# virtual methods
.method public getArgs()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Rule;->args:Ljava/lang/String;

    return-object p0
.end method

.method public getBang()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/preprocessor/Rule;->bang:Z

    return p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Rule;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getReturnValue()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Rule;->returnValue:Ljava/lang/String;

    return-object p0
.end method

.method public getVisibility()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Rule;->visibility:Ljava/lang/String;

    return-object p0
.end method

.method public narrowerVisibility(Lantlr/preprocessor/Rule;)Z
    .locals 4

    iget-object v0, p0, Lantlr/preprocessor/Rule;->visibility:Ljava/lang/String;

    const-string v1, "public"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/4 v2, 0x1

    const/4 v3, 0x0

    if-eqz v0, :cond_1

    invoke-virtual {p1, v1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-nez p0, :cond_0

    return v2

    :cond_0
    return v3

    :cond_1
    iget-object v0, p0, Lantlr/preprocessor/Rule;->visibility:Ljava/lang/String;

    const-string v1, "protected"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const-string v1, "private"

    if-eqz v0, :cond_3

    invoke-virtual {p1, v1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_2

    return v2

    :cond_2
    return v3

    :cond_3
    iget-object p0, p0, Lantlr/preprocessor/Rule;->visibility:Ljava/lang/String;

    invoke-virtual {p0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return v3
.end method

.method public sameSignature(Lantlr/preprocessor/Rule;)Z
    .locals 4

    iget-object v0, p0, Lantlr/preprocessor/Rule;->name:Ljava/lang/String;

    invoke-virtual {p1}, Lantlr/preprocessor/Rule;->getName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    iget-object v1, p0, Lantlr/preprocessor/Rule;->args:Ljava/lang/String;

    const/4 v2, 0x1

    if-eqz v1, :cond_0

    invoke-virtual {p1}, Lantlr/preprocessor/Rule;->getArgs()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    goto :goto_0

    :cond_0
    move v1, v2

    :goto_0
    iget-object p0, p0, Lantlr/preprocessor/Rule;->returnValue:Ljava/lang/String;

    if-eqz p0, :cond_1

    invoke-virtual {p1}, Lantlr/preprocessor/Rule;->getReturnValue()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    goto :goto_1

    :cond_1
    move p0, v2

    :goto_1
    if-eqz v0, :cond_2

    if-eqz v1, :cond_2

    if-eqz p0, :cond_2

    goto :goto_2

    :cond_2
    const/4 v2, 0x0

    :goto_2
    return v2
.end method

.method public setArgs(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Rule;->args:Ljava/lang/String;

    return-void
.end method

.method public setBang()V
    .locals 1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/preprocessor/Rule;->bang:Z

    return-void
.end method

.method public setEnclosingGrammar(Lantlr/preprocessor/Grammar;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Rule;->enclosingGrammar:Lantlr/preprocessor/Grammar;

    return-void
.end method

.method public setInitAction(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Rule;->initAction:Ljava/lang/String;

    return-void
.end method

.method public setOptions(Lantlr/collections/impl/IndexedVector;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Rule;->options:Lantlr/collections/impl/IndexedVector;

    return-void
.end method

.method public setReturnValue(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Rule;->returnValue:Ljava/lang/String;

    return-void
.end method

.method public setThrowsSpec(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Rule;->throwsSpec:Ljava/lang/String;

    return-void
.end method

.method public setVisibility(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Rule;->visibility:Ljava/lang/String;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 7

    iget-object v0, p0, Lantlr/preprocessor/Rule;->returnValue:Ljava/lang/String;

    const-string v1, ""

    if-nez v0, :cond_0

    move-object v0, v1

    goto :goto_0

    :cond_0
    const-string v0, "returns "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v2, p0, Lantlr/preprocessor/Rule;->returnValue:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :goto_0
    iget-object v2, p0, Lantlr/preprocessor/Rule;->args:Ljava/lang/String;

    if-nez v2, :cond_1

    move-object v2, v1

    :cond_1
    invoke-virtual {p0}, Lantlr/preprocessor/Rule;->getBang()Z

    move-result v3

    if-eqz v3, :cond_2

    const-string v3, "!"

    goto :goto_1

    :cond_2
    move-object v3, v1

    :goto_1
    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-object v5, p0, Lantlr/preprocessor/Rule;->visibility:Ljava/lang/String;

    const-string v6, " "

    if-nez v5, :cond_3

    goto :goto_2

    :cond_3
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v5, p0, Lantlr/preprocessor/Rule;->visibility:Ljava/lang/String;

    invoke-static {v1, v5, v6}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :goto_2
    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v4, p0, Lantlr/preprocessor/Rule;->name:Ljava/lang/String;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v0, p0, Lantlr/preprocessor/Rule;->throwsSpec:Ljava/lang/String;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lantlr/preprocessor/Rule;->options:Lantlr/collections/impl/IndexedVector;

    const-string v2, "line.separator"

    if-eqz v1, :cond_5

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "options {"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lantlr/preprocessor/Rule;->options:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {v1}, Lantlr/collections/impl/IndexedVector;->elements()Ljava/util/Enumeration;

    move-result-object v1

    :goto_3
    invoke-interface {v1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v3

    if-eqz v3, :cond_4

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-interface {v1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/preprocessor/Option;

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    goto :goto_3

    :cond_4
    const-string v1, "}"

    invoke-static {v0, v1}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_5
    iget-object v1, p0, Lantlr/preprocessor/Rule;->initAction:Ljava/lang/String;

    if-eqz v1, :cond_6

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/preprocessor/Rule;->initAction:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_6
    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/preprocessor/Rule;->block:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
