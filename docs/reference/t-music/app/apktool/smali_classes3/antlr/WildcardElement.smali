.class public Lantlr/WildcardElement;
.super Lantlr/GrammarAtom;
.source ""


# instance fields
.field public label:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;I)V
    .locals 0

    invoke-direct {p0, p1, p2, p3}, Lantlr/GrammarAtom;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result p1

    iput p1, p0, Lantlr/GrammarElement;->line:I

    return-void
.end method


# virtual methods
.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/WildcardElement;)V

    return-void
.end method

.method public getLabel()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/WildcardElement;->label:Ljava/lang/String;

    return-object p0
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/WildcardElement;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public setLabel(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/WildcardElement;->label:Ljava/lang/String;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    iget-object v0, p0, Lantlr/WildcardElement;->label:Ljava/lang/String;

    const-string v1, " "

    if-eqz v0, :cond_0

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/WildcardElement;->label:Ljava/lang/String;

    const-string v1, ":"

    invoke-static {v0, p0, v1}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_0
    const-string p0, "."

    invoke-static {v1, p0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
