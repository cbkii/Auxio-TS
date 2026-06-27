.class public Lantlr/TreeElement;
.super Lantlr/AlternativeBlock;
.source ""


# instance fields
.field public root:Lantlr/GrammarAtom;


# direct methods
.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;)V
    .locals 1

    const/4 v0, 0x0

    invoke-direct {p0, p1, p2, v0}, Lantlr/AlternativeBlock;-><init>(Lantlr/Grammar;Lantlr/Token;Z)V

    return-void
.end method


# virtual methods
.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/TreeElement;)V

    return-void
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/TreeElement;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    const-string v0, " #("

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/Alternative;

    iget-object p0, p0, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_0
    if-eqz p0, :cond_0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_0

    :cond_0
    const-string p0, " )"

    invoke-static {v0, p0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
