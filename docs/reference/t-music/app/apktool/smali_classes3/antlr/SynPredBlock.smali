.class public Lantlr/SynPredBlock;
.super Lantlr/AlternativeBlock;
.source ""


# direct methods
.method public constructor <init>(Lantlr/Grammar;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/AlternativeBlock;-><init>(Lantlr/Grammar;)V

    return-void
.end method

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

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/AlternativeBlock;)V

    return-void
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/SynPredBlock;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-super {p0}, Lantlr/AlternativeBlock;->toString()Ljava/lang/String;

    move-result-object p0

    const-string v1, "=>"

    invoke-static {v0, p0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
