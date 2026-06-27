.class public Lantlr/AlternativeBlock;
.super Lantlr/AlternativeElement;
.source ""


# static fields
.field public static nblks:I


# instance fields
.field public ID:I

.field public alternatives:Lantlr/collections/impl/Vector;

.field public alti:I

.field public altj:I

.field public analysisAlt:I

.field public doAutoGen:Z

.field public generateAmbigWarnings:Z

.field public greedy:Z

.field public greedySet:Z

.field public hasASynPred:Z

.field public hasAnAction:Z

.field public initAction:Ljava/lang/String;

.field public label:Ljava/lang/String;

.field public not:Z

.field public warnWhenFollowAmbig:Z


# direct methods
.method public constructor <init>(Lantlr/Grammar;)V
    .locals 3

    invoke-direct {p0, p1}, Lantlr/AlternativeElement;-><init>(Lantlr/Grammar;)V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->hasAnAction:Z

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->hasASynPred:Z

    iput p1, p0, Lantlr/AlternativeBlock;->ID:I

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->not:Z

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->greedy:Z

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->greedySet:Z

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->doAutoGen:Z

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->warnWhenFollowAmbig:Z

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    new-instance v1, Lantlr/collections/impl/Vector;

    const/4 v2, 0x5

    invoke-direct {v1, v2}, Lantlr/collections/impl/Vector;-><init>(I)V

    iput-object v1, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->not:Z

    sget p1, Lantlr/AlternativeBlock;->nblks:I

    add-int/2addr p1, v0

    sput p1, Lantlr/AlternativeBlock;->nblks:I

    sget p1, Lantlr/AlternativeBlock;->nblks:I

    iput p1, p0, Lantlr/AlternativeBlock;->ID:I

    return-void
.end method

.method public constructor <init>(Lantlr/Grammar;Lantlr/Token;Z)V
    .locals 1

    invoke-direct {p0, p1, p2}, Lantlr/AlternativeElement;-><init>(Lantlr/Grammar;Lantlr/Token;)V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->hasAnAction:Z

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->hasASynPred:Z

    iput p1, p0, Lantlr/AlternativeBlock;->ID:I

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->not:Z

    const/4 p2, 0x1

    iput-boolean p2, p0, Lantlr/AlternativeBlock;->greedy:Z

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->greedySet:Z

    iput-boolean p2, p0, Lantlr/AlternativeBlock;->doAutoGen:Z

    iput-boolean p2, p0, Lantlr/AlternativeBlock;->warnWhenFollowAmbig:Z

    iput-boolean p2, p0, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    new-instance p1, Lantlr/collections/impl/Vector;

    const/4 v0, 0x5

    invoke-direct {p1, v0}, Lantlr/collections/impl/Vector;-><init>(I)V

    iput-object p1, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    iput-boolean p3, p0, Lantlr/AlternativeBlock;->not:Z

    sget p1, Lantlr/AlternativeBlock;->nblks:I

    add-int/2addr p1, p2

    sput p1, Lantlr/AlternativeBlock;->nblks:I

    sget p1, Lantlr/AlternativeBlock;->nblks:I

    iput p1, p0, Lantlr/AlternativeBlock;->ID:I

    return-void
.end method


# virtual methods
.method public addAlternative(Lantlr/Alternative;)V
    .locals 0

    iget-object p0, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    return-void
.end method

.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/AlternativeBlock;)V

    return-void
.end method

.method public getAlternativeAt(I)Lantlr/Alternative;
    .locals 0

    iget-object p0, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/Alternative;

    return-object p0
.end method

.method public getAlternatives()Lantlr/collections/impl/Vector;
    .locals 0

    iget-object p0, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    return-object p0
.end method

.method public getAutoGen()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/AlternativeBlock;->doAutoGen:Z

    return p0
.end method

.method public getInitAction()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    return-object p0
.end method

.method public getLabel()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/AlternativeBlock;->label:Ljava/lang/String;

    return-object p0
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public prepareForAnalysis()V
    .locals 3

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    iget-object v1, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/Alternative;

    iget-object v2, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget v2, v2, Lantlr/Grammar;->maxk:I

    add-int/lit8 v2, v2, 0x1

    new-array v2, v2, [Lantlr/Lookahead;

    iput-object v2, v1, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    const/4 v2, -0x1

    iput v2, v1, Lantlr/Alternative;->lookaheadDepth:I

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public removeTrackingOfRuleRefs(Lantlr/Grammar;)V
    .locals 5

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_4

    invoke-virtual {p0, v0}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v1

    iget-object v1, v1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    :goto_1
    if-eqz v1, :cond_3

    instance-of v2, v1, Lantlr/RuleRefElement;

    if-eqz v2, :cond_1

    move-object v2, v1

    check-cast v2, Lantlr/RuleRefElement;

    iget-object v3, v2, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {p1, v3}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v3

    check-cast v3, Lantlr/RuleSymbol;

    if-nez v3, :cond_0

    iget-object v3, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v3, v3, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v4, "rule "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-object v2, v2, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " referenced in (...)=>, but not defined"

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v3, v2}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_2

    :cond_0
    iget-object v3, v3, Lantlr/RuleSymbol;->references:Lantlr/collections/impl/Vector;

    invoke-virtual {v3, v2}, Lantlr/collections/impl/Vector;->removeElement(Ljava/lang/Object;)Z

    goto :goto_2

    :cond_1
    instance-of v2, v1, Lantlr/AlternativeBlock;

    if-eqz v2, :cond_2

    move-object v2, v1

    check-cast v2, Lantlr/AlternativeBlock;

    invoke-virtual {v2, p1}, Lantlr/AlternativeBlock;->removeTrackingOfRuleRefs(Lantlr/Grammar;)V

    :cond_2
    :goto_2
    iget-object v1, v1, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_1

    :cond_3
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_4
    return-void
.end method

.method public setAlternatives(Lantlr/collections/impl/Vector;)V
    .locals 0

    iput-object p1, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    return-void
.end method

.method public setAutoGen(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->doAutoGen:Z

    return-void
.end method

.method public setInitAction(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    return-void
.end method

.method public setLabel(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/AlternativeBlock;->label:Ljava/lang/String;

    return-void
.end method

.method public setOption(Lantlr/Token;Lantlr/Token;)V
    .locals 6

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "warnWhenFollowAmbig"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/4 v1, 0x0

    const-string v2, "false"

    const-string v3, "true"

    const/4 v4, 0x1

    if-eqz v0, :cond_2

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    iput-boolean v4, p0, Lantlr/AlternativeBlock;->warnWhenFollowAmbig:Z

    goto/16 :goto_2

    :cond_0
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p2, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_1

    iput-boolean v1, p0, Lantlr/AlternativeBlock;->warnWhenFollowAmbig:Z

    goto/16 :goto_2

    :cond_1
    iget-object p0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v1, "Value for warnWhenFollowAmbig must be true or false"

    :goto_0
    invoke-virtual {p2, v1, p0, v0, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto/16 :goto_2

    :cond_2
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v5, "generateAmbigWarnings"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_3

    iput-boolean v4, p0, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    goto/16 :goto_2

    :cond_3
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p2, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_4

    iput-boolean v1, p0, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    goto :goto_2

    :cond_4
    iget-object p0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v1, "Value for generateAmbigWarnings must be true or false"

    goto :goto_0

    :cond_5
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v5, "greedy"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_8

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_6

    iput-boolean v4, p0, Lantlr/AlternativeBlock;->greedy:Z

    :goto_1
    iput-boolean v4, p0, Lantlr/AlternativeBlock;->greedySet:Z

    goto :goto_2

    :cond_6
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p2, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_7

    iput-boolean v1, p0, Lantlr/AlternativeBlock;->greedy:Z

    goto :goto_1

    :cond_7
    iget-object p0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v1, "Value for greedy must be true or false"

    goto :goto_0

    :cond_8
    iget-object p2, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v0, "Invalid subrule option: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-static {p1, v0}, La/a/a/a/a;->a(Lantlr/Token;Ljava/lang/StringBuilder;)Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    invoke-virtual {p2, v0, p0, v1, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_2
    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 10

    iget-object v0, p0, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    const-string v1, " ("

    if-eqz v0, :cond_0

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p0, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :cond_0
    const/4 v0, 0x0

    :goto_0
    iget-object v2, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    if-ge v0, v2, :cond_8

    invoke-virtual {p0, v0}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v2

    iget-object v3, v2, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    iget v4, v2, Lantlr/Alternative;->lookaheadDepth:I

    const/4 v5, -0x1

    const/4 v6, 0x1

    if-ne v4, v5, :cond_1

    goto :goto_2

    :cond_1
    const v5, 0x7fffffff

    if-ne v4, v5, :cond_2

    const-string v3, "{?}:"

    invoke-static {v1, v3}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    goto :goto_2

    :cond_2
    const-string v5, " {"

    invoke-static {v1, v5}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    move-object v5, v1

    move v1, v6

    :goto_1
    if-gt v1, v4, :cond_4

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    aget-object v7, v3, v1

    iget-object v8, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v8, v8, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v8}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v8

    const-string v9, ","

    invoke-virtual {v7, v9, v8}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/collections/impl/Vector;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    if-ge v1, v4, :cond_3

    add-int/lit8 v7, v1, 0x1

    aget-object v7, v3, v7

    if-eqz v7, :cond_3

    const-string v7, ";"

    invoke-static {v5, v7}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    :cond_3
    add-int/lit8 v1, v1, 0x1

    goto :goto_1

    :cond_4
    const-string v1, "}:"

    invoke-static {v5, v1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :goto_2
    iget-object v3, v2, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    iget-object v2, v2, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v2, :cond_5

    invoke-static {v1, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_5
    :goto_3
    if-eqz v3, :cond_6

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    iget-object v3, v3, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    goto :goto_3

    :cond_6
    iget-object v2, p0, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    sub-int/2addr v2, v6

    if-ge v0, v2, :cond_7

    const-string v2, " |"

    invoke-static {v1, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_7
    add-int/lit8 v0, v0, 0x1

    goto/16 :goto_0

    :cond_8
    const-string p0, " )"

    invoke-static {v1, p0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
