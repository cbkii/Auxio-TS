.class public Lantlr/RuleRefElement;
.super Lantlr/AlternativeElement;
.source ""


# instance fields
.field public args:Ljava/lang/String;

.field public idAssign:Ljava/lang/String;

.field public label:Ljava/lang/String;

.field public targetRule:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;I)V
    .locals 0

    invoke-direct {p0, p1, p2, p3}, Lantlr/AlternativeElement;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    iput-object p1, p0, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    iget p1, p2, Lantlr/Token;->type:I

    const/16 p2, 0x18

    if-ne p1, p2, :cond_0

    iget-object p1, p0, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-static {p1}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    :cond_0
    return-void
.end method


# virtual methods
.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/RuleRefElement;)V

    return-void
.end method

.method public getArgs()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    return-object p0
.end method

.method public getIdAssign()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    return-object p0
.end method

.method public getLabel()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/RuleRefElement;->label:Ljava/lang/String;

    return-object p0
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/RuleRefElement;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public setArgs(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    return-void
.end method

.method public setIdAssign(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/RuleRefElement;->idAssign:Ljava/lang/String;

    return-void
.end method

.method public setLabel(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/RuleRefElement;->label:Ljava/lang/String;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    iget-object v0, p0, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    const-string v1, " "

    if-eqz v0, :cond_0

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lantlr/RuleRefElement;->args:Ljava/lang/String;

    :goto_0
    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    goto :goto_0
.end method
