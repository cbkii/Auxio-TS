.class public Lantlr/preprocessor/Option;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public enclosingGrammar:Lantlr/preprocessor/Grammar;

.field public name:Ljava/lang/String;

.field public rhs:Ljava/lang/String;


# direct methods
.method public constructor <init>(Ljava/lang/String;Ljava/lang/String;Lantlr/preprocessor/Grammar;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/preprocessor/Option;->name:Ljava/lang/String;

    iput-object p2, p0, Lantlr/preprocessor/Option;->rhs:Ljava/lang/String;

    invoke-virtual {p0, p3}, Lantlr/preprocessor/Option;->setEnclosingGrammar(Lantlr/preprocessor/Grammar;)V

    return-void
.end method


# virtual methods
.method public getEnclosingGrammar()Lantlr/preprocessor/Grammar;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Option;->enclosingGrammar:Lantlr/preprocessor/Grammar;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Option;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getRHS()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Option;->rhs:Ljava/lang/String;

    return-object p0
.end method

.method public setEnclosingGrammar(Lantlr/preprocessor/Grammar;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Option;->enclosingGrammar:Lantlr/preprocessor/Grammar;

    return-void
.end method

.method public setName(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Option;->name:Ljava/lang/String;

    return-void
.end method

.method public setRHS(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Option;->rhs:Ljava/lang/String;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    const-string v0, "\t"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/preprocessor/Option;->name:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lantlr/preprocessor/Option;->rhs:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
