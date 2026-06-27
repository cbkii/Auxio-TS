.class public Lantlr/TokenRangeElement;
.super Lantlr/AlternativeElement;
.source ""


# instance fields
.field public begin:I

.field public beginText:Ljava/lang/String;

.field public end:I

.field public endText:Ljava/lang/String;

.field public label:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;Lantlr/Token;I)V
    .locals 0

    invoke-direct {p0, p1, p2, p4}, Lantlr/AlternativeElement;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    const/4 p1, 0x0

    iput p1, p0, Lantlr/TokenRangeElement;->begin:I

    iput p1, p0, Lantlr/TokenRangeElement;->end:I

    iget-object p1, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p4

    invoke-interface {p1, p4}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p1

    invoke-virtual {p1}, Lantlr/TokenSymbol;->getTokenType()I

    move-result p1

    iput p1, p0, Lantlr/TokenRangeElement;->begin:I

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/TokenRangeElement;->beginText:Ljava/lang/String;

    iget-object p1, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p4

    invoke-interface {p1, p4}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p1

    invoke-virtual {p1}, Lantlr/TokenSymbol;->getTokenType()I

    move-result p1

    iput p1, p0, Lantlr/TokenRangeElement;->end:I

    invoke-virtual {p3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/TokenRangeElement;->endText:Ljava/lang/String;

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

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/TokenRangeElement;)V

    return-void
.end method

.method public getLabel()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/TokenRangeElement;->label:Ljava/lang/String;

    return-object p0
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/TokenRangeElement;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public setLabel(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/TokenRangeElement;->label:Ljava/lang/String;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    iget-object v0, p0, Lantlr/TokenRangeElement;->label:Ljava/lang/String;

    const-string v1, ".."

    const-string v2, " "

    if-eqz v0, :cond_0

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v2, p0, Lantlr/TokenRangeElement;->label:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ":"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_0
    iget-object v2, p0, Lantlr/TokenRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lantlr/TokenRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    goto :goto_0
.end method
