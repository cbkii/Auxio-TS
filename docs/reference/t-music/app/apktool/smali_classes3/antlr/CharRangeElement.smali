.class public Lantlr/CharRangeElement;
.super Lantlr/AlternativeElement;
.source ""


# instance fields
.field public begin:C

.field public beginText:Ljava/lang/String;

.field public end:C

.field public endText:Ljava/lang/String;

.field public label:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lantlr/LexerGrammar;Lantlr/Token;Lantlr/Token;I)V
    .locals 1

    invoke-direct {p0, p1}, Lantlr/AlternativeElement;-><init>(Lantlr/Grammar;)V

    const/4 v0, 0x0

    iput-char v0, p0, Lantlr/CharRangeElement;->begin:C

    iput-char v0, p0, Lantlr/CharRangeElement;->end:C

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lantlr/ANTLRLexer;->tokenTypeForCharLiteral(Ljava/lang/String;)I

    move-result v0

    int-to-char v0, v0

    iput-char v0, p0, Lantlr/CharRangeElement;->begin:C

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {p3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lantlr/ANTLRLexer;->tokenTypeForCharLiteral(Ljava/lang/String;)I

    move-result v0

    int-to-char v0, v0

    iput-char v0, p0, Lantlr/CharRangeElement;->end:C

    invoke-virtual {p3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p3

    iput-object p3, p0, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result p2

    iput p2, p0, Lantlr/GrammarElement;->line:I

    iget-char p2, p0, Lantlr/CharRangeElement;->begin:C

    :goto_0
    iget-char p3, p0, Lantlr/CharRangeElement;->end:C

    if-gt p2, p3, :cond_0

    iget-object p3, p1, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {p3, p2}, Lantlr/collections/impl/BitSet;->add(I)V

    add-int/lit8 p2, p2, 0x1

    goto :goto_0

    :cond_0
    iput p4, p0, Lantlr/AlternativeElement;->autoGenType:I

    return-void
.end method


# virtual methods
.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/CharRangeElement;)V

    return-void
.end method

.method public getLabel()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/CharRangeElement;->label:Ljava/lang/String;

    return-object p0
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/CharRangeElement;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public setLabel(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/CharRangeElement;->label:Ljava/lang/String;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    iget-object v0, p0, Lantlr/CharRangeElement;->label:Ljava/lang/String;

    const-string v1, ".."

    const-string v2, " "

    if-eqz v0, :cond_0

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v2, p0, Lantlr/CharRangeElement;->label:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ":"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_0
    iget-object v2, p0, Lantlr/CharRangeElement;->beginText:Ljava/lang/String;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lantlr/CharRangeElement;->endText:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    goto :goto_0
.end method
