.class public Lantlr/BlockEndElement;
.super Lantlr/AlternativeElement;
.source ""


# instance fields
.field public block:Lantlr/AlternativeBlock;

.field public lock:[Z


# direct methods
.method public constructor <init>(Lantlr/Grammar;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/AlternativeElement;-><init>(Lantlr/Grammar;)V

    iget p1, p1, Lantlr/Grammar;->maxk:I

    add-int/lit8 p1, p1, 0x1

    new-array p1, p1, [Z

    iput-object p1, p0, Lantlr/BlockEndElement;->lock:[Z

    return-void
.end method


# virtual methods
.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/BlockEndElement;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public toString()Ljava/lang/String;
    .locals 0

    const-string p0, ""

    return-object p0
.end method
