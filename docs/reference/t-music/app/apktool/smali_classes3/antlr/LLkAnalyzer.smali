.class public Lantlr/LLkAnalyzer;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/LLkGrammarAnalyzer;


# instance fields
.field public DEBUG_ANALYZER:Z

.field public charFormatter:Lantlr/CharFormatter;

.field public currentBlock:Lantlr/AlternativeBlock;

.field public grammar:Lantlr/Grammar;

.field public lexicalAnalysis:Z

.field public tool:Lantlr/Tool;


# direct methods
.method public constructor <init>(Lantlr/Tool;)V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const/4 v1, 0x0

    iput-object v1, p0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    iput-object v1, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iput-boolean v0, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    new-instance v0, Lantlr/JavaCharFormatter;

    invoke-direct {v0}, Lantlr/JavaCharFormatter;-><init>()V

    iput-object v0, p0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iput-object p1, p0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    return-void
.end method

.method private getAltLookahead(Lantlr/AlternativeBlock;II)Lantlr/Lookahead;
    .locals 1

    invoke-virtual {p1, p2}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object p0

    iget-object p1, p0, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    iget-object p2, p0, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aget-object v0, p2, p3

    if-nez v0, :cond_0

    invoke-virtual {p1, p3}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p1

    iget-object p0, p0, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    aput-object p1, p0, p3

    goto :goto_0

    :cond_0
    aget-object p1, p2, p3

    :goto_0
    return-object p1
.end method

.method public static lookaheadEquivForApproxAndFullAnalysis([Lantlr/Lookahead;I)Z
    .locals 3

    const/4 v0, 0x1

    move v1, v0

    :goto_0
    add-int/lit8 v2, p1, -0x1

    if-gt v1, v2, :cond_1

    aget-object v2, p0, v1

    iget-object v2, v2, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v2}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v2

    if-le v2, v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    return v0
.end method

.method private removeCompetingPredictionSets(Lantlr/collections/impl/BitSet;Lantlr/AlternativeElement;)V
    .locals 2

    iget-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iget v1, v0, Lantlr/AlternativeBlock;->analysisAlt:I

    invoke-virtual {v0, v1}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v0

    iget-object v0, v0, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v1, v0, Lantlr/TreeElement;

    if-eqz v1, :cond_0

    check-cast v0, Lantlr/TreeElement;

    iget-object v0, v0, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    if-eq v0, p2, :cond_1

    return-void

    :cond_0
    if-eq p2, v0, :cond_1

    return-void

    :cond_1
    const/4 p2, 0x0

    :goto_0
    iget-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iget v1, v0, Lantlr/AlternativeBlock;->analysisAlt:I

    if-ge p2, v1, :cond_2

    invoke-virtual {v0, p2}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v0

    iget-object v0, v0, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object v0

    iget-object v0, v0, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {p1, v0}, Lantlr/collections/impl/BitSet;->subtractInPlace(Lantlr/collections/impl/BitSet;)V

    add-int/lit8 p2, p2, 0x1

    goto :goto_0

    :cond_2
    return-void
.end method

.method private removeCompetingPredictionSetsFromWildcard([Lantlr/Lookahead;Lantlr/AlternativeElement;I)V
    .locals 3

    const/4 p2, 0x1

    :goto_0
    if-gt p2, p3, :cond_1

    const/4 v0, 0x0

    :goto_1
    iget-object v1, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iget v2, v1, Lantlr/AlternativeBlock;->analysisAlt:I

    if-ge v0, v2, :cond_0

    invoke-virtual {v1, v0}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v1

    iget-object v1, v1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    aget-object v2, p1, p2

    iget-object v2, v2, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v1, p2}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object v1

    iget-object v1, v1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v2, v1}, Lantlr/collections/impl/BitSet;->subtractInPlace(Lantlr/collections/impl/BitSet;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_1

    :cond_0
    add-int/lit8 p2, p2, 0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method private reset()V
    .locals 2

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    const/4 v1, 0x0

    iput-boolean v1, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    iput-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput-boolean v1, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    return-void
.end method


# virtual methods
.method public FOLLOW(ILantlr/RuleEndElement;)Lantlr/Lookahead;
    .locals 16

    move-object/from16 v0, p0

    move/from16 v1, p1

    move-object/from16 v2, p2

    iget-object v3, v2, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    check-cast v3, Lantlr/RuleBlock;

    iget-boolean v4, v0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    invoke-virtual {v3}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v3

    if-eqz v4, :cond_0

    invoke-static {v3}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    :cond_0
    iget-boolean v4, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const-string v5, ","

    if-eqz v4, :cond_1

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "FOLLOW("

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v7, ")"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_1
    iget-object v4, v2, Lantlr/BlockEndElement;->lock:[Z

    aget-boolean v6, v4, v1

    if-eqz v6, :cond_3

    iget-boolean v0, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_2

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "FOLLOW cycle to "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_2
    new-instance v0, Lantlr/Lookahead;

    invoke-direct {v0, v3}, Lantlr/Lookahead;-><init>(Ljava/lang/String;)V

    return-object v0

    :cond_3
    iget-object v6, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v6, v6, v1

    const/4 v7, 0x0

    const-string v8, "saving FOLLOW("

    const-string v9, ": "

    const-string v10, ") for "

    if-eqz v6, :cond_a

    iget-boolean v4, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v4, :cond_4

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v11, "cache entry FOLLOW("

    invoke-virtual {v6, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v11, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v11, v11, v1

    iget-object v12, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object v13, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v11, v5, v12, v13}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v6, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_4
    iget-object v4, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v6, v4, v1

    iget-object v6, v6, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    if-nez v6, :cond_5

    aget-object v0, v4, v1

    invoke-virtual {v0}, Lantlr/Lookahead;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Lookahead;

    return-object v0

    :cond_5
    iget-object v6, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    aget-object v4, v4, v1

    iget-object v4, v4, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    invoke-virtual {v6, v4}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v4

    check-cast v4, Lantlr/RuleSymbol;

    invoke-virtual {v4}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v4

    iget-object v4, v4, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    iget-object v6, v4, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v6, v6, v1

    if-nez v6, :cond_6

    iget-object v0, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v0, v0, v1

    invoke-virtual {v0}, Lantlr/Lookahead;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Lookahead;

    return-object v0

    :cond_6
    iget-boolean v6, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const-string v11, ": from "

    if-eqz v6, :cond_7

    sget-object v6, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    const-string v13, "combining FOLLOW("

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v13, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v13, v13, v1

    iget-object v14, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object v15, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v13, v5, v14, v15}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v13, " with FOLLOW for "

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v13, v4, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    check-cast v13, Lantlr/RuleBlock;

    invoke-virtual {v13}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v9, v4, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v9, v9, v1

    iget-object v13, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object v14, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v9, v5, v13, v14}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v12, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v6, v9}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_7
    iget-object v6, v4, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v9, v6, v1

    iget-object v9, v9, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    if-nez v9, :cond_8

    iget-object v4, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v4, v4, v1

    aget-object v6, v6, v1

    invoke-virtual {v4, v6}, Lantlr/Lookahead;->combineWith(Lantlr/Lookahead;)V

    iget-object v4, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v4, v4, v1

    iput-object v7, v4, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    goto :goto_0

    :cond_8
    invoke-virtual {v0, v1, v4}, Lantlr/LLkAnalyzer;->FOLLOW(ILantlr/RuleEndElement;)Lantlr/Lookahead;

    move-result-object v4

    iget-object v6, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v6, v6, v1

    invoke-virtual {v6, v4}, Lantlr/Lookahead;->combineWith(Lantlr/Lookahead;)V

    iget-object v6, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v6, v6, v1

    iget-object v4, v4, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    iput-object v4, v6, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    :goto_0
    iget-boolean v4, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v4, :cond_9

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v3, v3, v1

    iget-object v7, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object v0, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v3, v5, v7, v0}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v6, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v4, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_9
    iget-object v0, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    aget-object v0, v0, v1

    invoke-virtual {v0}, Lantlr/Lookahead;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Lookahead;

    return-object v0

    :cond_a
    const/4 v6, 0x1

    aput-boolean v6, v4, v1

    new-instance v4, Lantlr/Lookahead;

    invoke-direct {v4}, Lantlr/Lookahead;-><init>()V

    iget-object v11, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v11, v3}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v11

    check-cast v11, Lantlr/RuleSymbol;

    const/4 v13, 0x0

    :goto_1
    invoke-virtual {v11}, Lantlr/RuleSymbol;->numReferences()I

    move-result v14

    if-ge v13, v14, :cond_f

    invoke-virtual {v11, v13}, Lantlr/RuleSymbol;->getReference(I)Lantlr/RuleRefElement;

    move-result-object v14

    iget-boolean v15, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const-string v6, "] is "

    if-eqz v15, :cond_b

    sget-object v15, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v12, "next["

    invoke-static {v12, v3, v6}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    iget-object v7, v14, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {v7}, Lantlr/GrammarElement;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v12, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v15, v7}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_b
    iget-object v7, v14, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {v7, v1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object v7

    iget-boolean v12, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v12, :cond_c

    sget-object v12, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v14, "FIRST of next["

    const-string v15, "] ptr is "

    invoke-static {v14, v3, v15}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v14

    invoke-virtual {v7}, Lantlr/Lookahead;->toString()Ljava/lang/String;

    move-result-object v15

    invoke-virtual {v14, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v12, v14}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_c
    iget-object v12, v7, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    if-eqz v12, :cond_d

    invoke-virtual {v12, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v12

    if-eqz v12, :cond_d

    const/4 v12, 0x0

    iput-object v12, v7, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    goto :goto_2

    :cond_d
    const/4 v12, 0x0

    :goto_2
    invoke-virtual {v4, v7}, Lantlr/Lookahead;->combineWith(Lantlr/Lookahead;)V

    iget-boolean v7, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v7, :cond_e

    sget-object v7, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v14, "combined FOLLOW["

    invoke-static {v14, v3, v6}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v4}, Lantlr/Lookahead;->toString()Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v6, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v7, v6}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_e
    add-int/lit8 v13, v13, 0x1

    move-object v7, v12

    const/4 v6, 0x1

    goto :goto_1

    :cond_f
    iget-object v6, v2, Lantlr/BlockEndElement;->lock:[Z

    const/4 v7, 0x0

    aput-boolean v7, v6, v1

    iget-object v6, v4, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {v6}, Lantlr/collections/impl/BitSet;->nil()Z

    move-result v6

    if-eqz v6, :cond_12

    iget-object v6, v4, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    if-nez v6, :cond_12

    iget-object v6, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    instance-of v7, v6, Lantlr/TreeWalkerGrammar;

    if-eqz v7, :cond_10

    iget-object v6, v4, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    const/4 v7, 0x3

    :goto_3
    invoke-virtual {v6, v7}, Lantlr/collections/impl/BitSet;->add(I)V

    goto :goto_4

    :cond_10
    instance-of v6, v6, Lantlr/LexerGrammar;

    if-eqz v6, :cond_11

    invoke-virtual {v4}, Lantlr/Lookahead;->setEpsilon()V

    goto :goto_4

    :cond_11
    iget-object v6, v4, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    const/4 v7, 0x1

    goto :goto_3

    :cond_12
    :goto_4
    iget-boolean v6, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v6, :cond_13

    sget-object v6, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object v0, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v4, v5, v3, v0}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v6, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_13
    iget-object v0, v2, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    invoke-virtual {v4}, Lantlr/Lookahead;->clone()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/Lookahead;

    aput-object v2, v0, v1

    return-object v4
.end method

.method public altUsesWildcardDefault(Lantlr/Alternative;)Z
    .locals 1

    iget-object p0, p1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of p1, p0, Lantlr/TreeElement;

    const/4 v0, 0x1

    if-eqz p1, :cond_0

    move-object p1, p0

    check-cast p1, Lantlr/TreeElement;

    iget-object p1, p1, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    instance-of p1, p1, Lantlr/WildcardElement;

    if-eqz p1, :cond_0

    return v0

    :cond_0
    instance-of p1, p0, Lantlr/WildcardElement;

    if-eqz p1, :cond_1

    iget-object p0, p0, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    instance-of p0, p0, Lantlr/BlockEndElement;

    if-eqz p0, :cond_1

    return v0

    :cond_1
    const/4 p0, 0x0

    return p0
.end method

.method public deterministic(Lantlr/AlternativeBlock;)Z
    .locals 18

    move-object/from16 v0, p0

    move-object/from16 v9, p1

    iget-boolean v1, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v1, :cond_0

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "deterministic("

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v3, ")"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v1, v9, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v10

    iget-object v11, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput-object v9, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iget-boolean v1, v9, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v1, :cond_1

    instance-of v1, v9, Lantlr/OneOrMoreBlock;

    if-nez v1, :cond_1

    instance-of v1, v9, Lantlr/ZeroOrMoreBlock;

    if-nez v1, :cond_1

    iget-object v1, v0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    iget-object v2, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v2}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v2

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarElement;->getLine()I

    move-result v3

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v4

    const-string v5, "Being nongreedy only makes sense for (...)+ and (...)*"

    invoke-virtual {v1, v5, v2, v3, v4}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_1
    const/4 v12, 0x0

    const/4 v13, 0x1

    if-ne v10, v13, :cond_2

    invoke-virtual {v9, v12}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v1

    iget-object v1, v1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    iget-object v2, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput v12, v2, Lantlr/AlternativeBlock;->alti:I

    invoke-virtual {v9, v12}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v2

    iget-object v2, v2, Lantlr/Alternative;->cache:[Lantlr/Lookahead;

    invoke-virtual {v1, v13}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object v1

    aput-object v1, v2, v13

    invoke-virtual {v9, v12}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v1

    iput v13, v1, Lantlr/Alternative;->lookaheadDepth:I

    iput-object v11, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    return v13

    :cond_2
    move v14, v12

    move v1, v13

    :goto_0
    add-int/lit8 v2, v10, -0x1

    if-ge v14, v2, :cond_15

    iget-object v2, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput v14, v2, Lantlr/AlternativeBlock;->alti:I

    iput v14, v2, Lantlr/AlternativeBlock;->analysisAlt:I

    add-int/lit8 v15, v14, 0x1

    iput v15, v2, Lantlr/AlternativeBlock;->altj:I

    move v8, v15

    :goto_1
    if-ge v8, v10, :cond_14

    iget-object v2, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput v8, v2, Lantlr/AlternativeBlock;->altj:I

    iget-boolean v2, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v2, :cond_3

    sget-object v2, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "comparing "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v14}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " against alt "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_3
    iget-object v2, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput v8, v2, Lantlr/AlternativeBlock;->analysisAlt:I

    iget-object v2, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget v2, v2, Lantlr/Grammar;->maxk:I

    add-int/2addr v2, v13

    new-array v6, v2, [Lantlr/Lookahead;

    move v2, v13

    :goto_2
    iget-boolean v3, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v3, :cond_4

    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "checking depth "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v5, "<="

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v5, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget v5, v5, Lantlr/Grammar;->maxk:I

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_4
    invoke-direct {v0, v9, v14, v2}, Lantlr/LLkAnalyzer;->getAltLookahead(Lantlr/AlternativeBlock;II)Lantlr/Lookahead;

    move-result-object v3

    invoke-direct {v0, v9, v8, v2}, Lantlr/LLkAnalyzer;->getAltLookahead(Lantlr/AlternativeBlock;II)Lantlr/Lookahead;

    move-result-object v4

    iget-boolean v5, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const-string v7, ","

    if-eqz v5, :cond_5

    sget-object v5, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v16, "p is "

    invoke-static/range {v16 .. v16}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    iget-object v13, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    move/from16 v17, v1

    iget-object v1, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v3, v7, v13, v1}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v12, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v5, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_3

    :cond_5
    move/from16 v17, v1

    :goto_3
    iget-boolean v1, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v1, :cond_6

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v5, "q is "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    iget-object v12, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object v13, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v4, v7, v12, v13}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_6
    invoke-virtual {v3, v4}, Lantlr/Lookahead;->intersection(Lantlr/Lookahead;)Lantlr/Lookahead;

    move-result-object v1

    aput-object v1, v6, v2

    iget-boolean v1, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v1, :cond_7

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "intersection at depth "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " is "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget-object v4, v6, v2

    invoke-virtual {v4}, Lantlr/Lookahead;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_7
    aget-object v1, v6, v2

    invoke-virtual {v1}, Lantlr/Lookahead;->nil()Z

    move-result v1

    if-nez v1, :cond_8

    add-int/lit8 v2, v2, 0x1

    const/4 v1, 0x1

    goto :goto_4

    :cond_8
    const/4 v1, 0x0

    :goto_4
    if-eqz v1, :cond_a

    iget-object v3, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget v3, v3, Lantlr/Grammar;->maxk:I

    if-le v2, v3, :cond_9

    goto :goto_5

    :cond_9
    move/from16 v1, v17

    const/4 v12, 0x0

    const/4 v13, 0x1

    goto/16 :goto_2

    :cond_a
    :goto_5
    invoke-virtual {v9, v14}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v3

    invoke-virtual {v9, v8}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v4

    if-eqz v1, :cond_13

    const v1, 0x7fffffff

    iput v1, v3, Lantlr/Alternative;->lookaheadDepth:I

    iput v1, v4, Lantlr/Alternative;->lookaheadDepth:I

    iget-object v1, v3, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    const-string v2, "alt "

    if-eqz v1, :cond_c

    iget-boolean v1, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v1, :cond_b

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v14}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, " has a syn pred"

    :goto_6
    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_b
    :goto_7
    move v12, v8

    goto/16 :goto_8

    :cond_c
    iget-object v1, v3, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v1, :cond_d

    iget-boolean v1, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v1, :cond_b

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v14}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, " has a sem pred"

    goto :goto_6

    :cond_d
    invoke-virtual {v0, v4}, Lantlr/LLkAnalyzer;->altUsesWildcardDefault(Lantlr/Alternative;)Z

    move-result v1

    if-eqz v1, :cond_e

    goto :goto_7

    :cond_e
    iget-boolean v1, v9, Lantlr/AlternativeBlock;->warnWhenFollowAmbig:Z

    if-nez v1, :cond_f

    iget-object v1, v3, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v1, v1, Lantlr/BlockEndElement;

    if-nez v1, :cond_b

    iget-object v1, v4, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v1, v1, Lantlr/BlockEndElement;

    if-eqz v1, :cond_f

    goto :goto_7

    :cond_f
    iget-boolean v1, v9, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    if-nez v1, :cond_10

    goto :goto_7

    :cond_10
    iget-boolean v1, v9, Lantlr/AlternativeBlock;->greedySet:Z

    if-eqz v1, :cond_12

    iget-boolean v1, v9, Lantlr/AlternativeBlock;->greedy:Z

    if-eqz v1, :cond_12

    iget-object v1, v3, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v1, v1, Lantlr/BlockEndElement;

    if-eqz v1, :cond_11

    iget-object v1, v4, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v1, v1, Lantlr/BlockEndElement;

    if-eqz v1, :cond_b

    :cond_11
    iget-object v1, v4, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v1, v1, Lantlr/BlockEndElement;

    if-eqz v1, :cond_12

    iget-object v1, v3, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v1, v1, Lantlr/BlockEndElement;

    if-nez v1, :cond_12

    goto :goto_7

    :cond_12
    iget-object v1, v0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    iget-object v1, v1, Lantlr/Tool;->errorHandler:Lantlr/ToolErrorHandler;

    iget-object v2, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-boolean v4, v0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    iget v5, v2, Lantlr/Grammar;->maxk:I

    move-object/from16 v3, p1

    move v7, v14

    move v12, v8

    invoke-interface/range {v1 .. v8}, Lantlr/ToolErrorHandler;->warnAltAmbiguity(Lantlr/Grammar;Lantlr/AlternativeBlock;ZI[Lantlr/Lookahead;II)V

    :goto_8
    const/4 v1, 0x0

    goto :goto_9

    :cond_13
    move v12, v8

    iget v1, v3, Lantlr/Alternative;->lookaheadDepth:I

    invoke-static {v1, v2}, Ljava/lang/Math;->max(II)I

    move-result v1

    iput v1, v3, Lantlr/Alternative;->lookaheadDepth:I

    iget v1, v4, Lantlr/Alternative;->lookaheadDepth:I

    invoke-static {v1, v2}, Ljava/lang/Math;->max(II)I

    move-result v1

    iput v1, v4, Lantlr/Alternative;->lookaheadDepth:I

    move/from16 v1, v17

    :goto_9
    add-int/lit8 v8, v12, 0x1

    const/4 v12, 0x0

    const/4 v13, 0x1

    goto/16 :goto_1

    :cond_14
    move/from16 v17, v1

    move v14, v15

    goto/16 :goto_0

    :cond_15
    iput-object v11, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    return v1
.end method

.method public deterministic(Lantlr/OneOrMoreBlock;)Z
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "deterministic(...)+("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput-object p1, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    invoke-virtual {p0, p1}, Lantlr/LLkAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    move-result v1

    invoke-virtual {p0, p1}, Lantlr/LLkAnalyzer;->deterministicImpliedPath(Lantlr/BlockWithImpliedExitPath;)Z

    move-result p1

    iput-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    if-eqz p1, :cond_1

    if-eqz v1, :cond_1

    const/4 p0, 0x1

    goto :goto_0

    :cond_1
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public deterministic(Lantlr/ZeroOrMoreBlock;)Z
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "deterministic(...)*("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput-object p1, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    invoke-virtual {p0, p1}, Lantlr/LLkAnalyzer;->deterministic(Lantlr/AlternativeBlock;)Z

    move-result v1

    invoke-virtual {p0, p1}, Lantlr/LLkAnalyzer;->deterministicImpliedPath(Lantlr/BlockWithImpliedExitPath;)Z

    move-result p1

    iput-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    if-eqz p1, :cond_1

    if-eqz v1, :cond_1

    const/4 p0, 0x1

    goto :goto_0

    :cond_1
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public deterministicImpliedPath(Lantlr/BlockWithImpliedExitPath;)Z
    .locals 16

    move-object/from16 v0, p0

    move-object/from16 v8, p1

    invoke-virtual/range {p1 .. p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v9

    iget-object v1, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    const/4 v2, -0x1

    iput v2, v1, Lantlr/AlternativeBlock;->altj:I

    iget-boolean v1, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v1, :cond_0

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, "deterministicImpliedPath"

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const/4 v11, 0x1

    move v1, v11

    const/4 v12, 0x0

    :goto_0
    if-ge v12, v9, :cond_10

    invoke-virtual {v8, v12}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v2

    iget-object v3, v2, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v3, v3, Lantlr/BlockEndElement;

    if-eqz v3, :cond_1

    iget-object v3, v0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    iget-object v4, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v4

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarElement;->getLine()I

    move-result v5

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v6

    const-string v7, "empty alternative makes no sense in (...)* or (...)+"

    invoke-virtual {v3, v7, v4, v5, v6}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_1
    iget-object v3, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget v3, v3, Lantlr/Grammar;->maxk:I

    add-int/2addr v3, v11

    new-array v6, v3, [Lantlr/Lookahead;

    move v3, v11

    :goto_1
    iget-boolean v4, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v4, :cond_2

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "checking depth "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v7, "<="

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v7, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget v7, v7, Lantlr/Grammar;->maxk:I

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_2
    iget-object v4, v8, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {v4, v3}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object v4

    iget-object v5, v8, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    aput-object v4, v5, v3

    iget-object v5, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput v12, v5, Lantlr/AlternativeBlock;->alti:I

    invoke-direct {v0, v8, v12, v3}, Lantlr/LLkAnalyzer;->getAltLookahead(Lantlr/AlternativeBlock;II)Lantlr/Lookahead;

    move-result-object v5

    iget-boolean v7, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const-string v13, ","

    if-eqz v7, :cond_3

    sget-object v7, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v14, "follow is "

    invoke-static {v14}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v14

    iget-object v15, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object v10, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v4, v13, v15, v10}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v14, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v14}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v7, v10}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_3
    iget-boolean v7, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v7, :cond_4

    sget-object v7, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v10, "p is "

    invoke-static {v10}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v10

    iget-object v14, v0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object v15, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v5, v13, v14, v15}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v10, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v7, v10}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_4
    invoke-virtual {v4, v5}, Lantlr/Lookahead;->intersection(Lantlr/Lookahead;)Lantlr/Lookahead;

    move-result-object v4

    aput-object v4, v6, v3

    iget-boolean v4, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v4, :cond_5

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "intersection at depth "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v7, " is "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    aget-object v7, v6, v3

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_5
    aget-object v4, v6, v3

    invoke-virtual {v4}, Lantlr/Lookahead;->nil()Z

    move-result v4

    if-nez v4, :cond_6

    add-int/lit8 v3, v3, 0x1

    move v4, v3

    move v3, v11

    goto :goto_2

    :cond_6
    move v4, v3

    const/4 v3, 0x0

    :goto_2
    if-eqz v3, :cond_8

    iget-object v5, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget v5, v5, Lantlr/Grammar;->maxk:I

    if-le v4, v5, :cond_7

    goto :goto_3

    :cond_7
    move v3, v4

    goto/16 :goto_1

    :cond_8
    :goto_3
    if-eqz v3, :cond_f

    const v1, 0x7fffffff

    iput v1, v2, Lantlr/Alternative;->lookaheadDepth:I

    iput v1, v8, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    iget-object v1, v0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iget v1, v1, Lantlr/AlternativeBlock;->alti:I

    invoke-virtual {v8, v1}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v1

    iget-boolean v2, v8, Lantlr/AlternativeBlock;->warnWhenFollowAmbig:Z

    if-nez v2, :cond_9

    goto/16 :goto_4

    :cond_9
    iget-boolean v2, v8, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    if-nez v2, :cond_a

    goto :goto_4

    :cond_a
    iget-boolean v2, v8, Lantlr/AlternativeBlock;->greedy:Z

    if-ne v2, v11, :cond_b

    iget-boolean v2, v8, Lantlr/AlternativeBlock;->greedySet:Z

    if-eqz v2, :cond_b

    iget-object v2, v1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v2, v2, Lantlr/BlockEndElement;

    if-nez v2, :cond_b

    iget-boolean v1, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v1, :cond_e

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, "greedy loop"

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_4

    :cond_b
    iget-boolean v2, v8, Lantlr/AlternativeBlock;->greedy:Z

    if-nez v2, :cond_d

    iget-object v1, v1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v1, v1, Lantlr/BlockEndElement;

    if-nez v1, :cond_d

    iget-boolean v1, v0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v1, :cond_c

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, "nongreedy loop"

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_c
    iget-object v1, v8, Lantlr/BlockWithImpliedExitPath;->exitCache:[Lantlr/Lookahead;

    iget-object v2, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget v2, v2, Lantlr/Grammar;->maxk:I

    invoke-static {v1, v2}, Lantlr/LLkAnalyzer;->lookaheadEquivForApproxAndFullAnalysis([Lantlr/Lookahead;I)Z

    move-result v1

    if-nez v1, :cond_e

    iget-object v1, v0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    const-string v2, "nongreedy block may exit incorrectly due"

    const-string v3, "\tto limitations of linear approximate lookahead (first k-1 sets"

    const-string v4, "\tin lookahead not singleton)."

    filled-new-array {v2, v3, v4}, [Ljava/lang/String;

    move-result-object v2

    iget-object v3, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    invoke-virtual/range {p1 .. p1}, Lantlr/GrammarElement;->getColumn()I

    move-result v5

    invoke-virtual {v1, v2, v3, v4, v5}, Lantlr/Tool;->warning([Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_4

    :cond_d
    iget-object v1, v0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    iget-object v1, v1, Lantlr/Tool;->errorHandler:Lantlr/ToolErrorHandler;

    iget-object v2, v0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-boolean v4, v0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    iget v5, v2, Lantlr/Grammar;->maxk:I

    move-object/from16 v3, p1

    move v7, v12

    invoke-interface/range {v1 .. v7}, Lantlr/ToolErrorHandler;->warnAltExitAmbiguity(Lantlr/Grammar;Lantlr/BlockWithImpliedExitPath;ZI[Lantlr/Lookahead;I)V

    :cond_e
    :goto_4
    const/4 v1, 0x0

    goto :goto_5

    :cond_f
    iget v3, v2, Lantlr/Alternative;->lookaheadDepth:I

    invoke-static {v3, v4}, Ljava/lang/Math;->max(II)I

    move-result v3

    iput v3, v2, Lantlr/Alternative;->lookaheadDepth:I

    iget v2, v8, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    invoke-static {v2, v4}, Ljava/lang/Math;->max(II)I

    move-result v2

    iput v2, v8, Lantlr/BlockWithImpliedExitPath;->exitLookaheadDepth:I

    :goto_5
    add-int/lit8 v12, v12, 0x1

    goto/16 :goto_0

    :cond_10
    return v1
.end method

.method public look(ILantlr/ActionElement;)Lantlr/Lookahead;
    .locals 2

    iget-boolean p0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz p0, :cond_0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "lookAction("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v1, ")"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object p0, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {p0, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;
    .locals 8

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "lookAltBlk("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput-object p2, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    new-instance v1, Lantlr/Lookahead;

    invoke-direct {v1}, Lantlr/Lookahead;-><init>()V

    const/4 v2, 0x0

    move v3, v2

    :goto_0
    iget-object v4, p2, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v4}, Lantlr/collections/impl/Vector;->size()I

    move-result v4

    if-ge v3, v4, :cond_3

    iget-boolean v4, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const-string v5, "alt "

    if-eqz v4, :cond_1

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v7, " of "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v4, v6}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_1
    iget-object v4, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    iput v3, v4, Lantlr/AlternativeBlock;->analysisAlt:I

    invoke-virtual {p2, v3}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v4

    iget-object v6, v4, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    iget-boolean v7, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v7, :cond_2

    iget-object v4, v4, Lantlr/Alternative;->tail:Lantlr/AlternativeElement;

    if-ne v6, v4, :cond_2

    sget-object v4, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v5, " is empty"

    invoke-virtual {v7, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_2
    invoke-virtual {v6, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object v4

    invoke-virtual {v1, v4}, Lantlr/Lookahead;->combineWith(Lantlr/Lookahead;)V

    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_3
    const/4 v3, 0x1

    if-ne p1, v3, :cond_6

    iget-boolean p1, p2, Lantlr/AlternativeBlock;->not:Z

    if-eqz p1, :cond_6

    iget-boolean p1, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    invoke-virtual {p0, p2, p1}, Lantlr/LLkAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result p1

    if-eqz p1, :cond_6

    iget-boolean p1, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    if-eqz p1, :cond_5

    iget-object p1, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    check-cast p1, Lantlr/LexerGrammar;

    iget-object p1, p1, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lantlr/collections/impl/BitSet;

    iget-object p2, v1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-virtual {p2}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object p2

    :goto_1
    array-length v3, p2

    if-ge v2, v3, :cond_4

    aget v3, p2, v2

    invoke-virtual {p1, v3}, Lantlr/collections/impl/BitSet;->remove(I)V

    add-int/lit8 v2, v2, 0x1

    goto :goto_1

    :cond_4
    iput-object p1, v1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    goto :goto_2

    :cond_5
    iget-object p1, v1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    iget-object p2, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p2}, Lantlr/TokenManager;->maxTokenType()I

    move-result p2

    const/4 v2, 0x4

    invoke-virtual {p1, v2, p2}, Lantlr/collections/impl/BitSet;->notInPlace(II)V

    :cond_6
    :goto_2
    iput-object v0, p0, Lantlr/LLkAnalyzer;->currentBlock:Lantlr/AlternativeBlock;

    return-object v1
.end method

.method public look(ILantlr/BlockEndElement;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "lookBlockEnd("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ", "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p2, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, "); lock is "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p2, Lantlr/BlockEndElement;->lock:[Z

    aget-boolean v2, v2, p1

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p2, Lantlr/BlockEndElement;->lock:[Z

    aget-boolean v0, v0, p1

    if-eqz v0, :cond_1

    new-instance p0, Lantlr/Lookahead;

    invoke-direct {p0}, Lantlr/Lookahead;-><init>()V

    return-object p0

    :cond_1
    iget-object v0, p2, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    instance-of v1, v0, Lantlr/ZeroOrMoreBlock;

    if-nez v1, :cond_3

    instance-of v0, v0, Lantlr/OneOrMoreBlock;

    if-eqz v0, :cond_2

    goto :goto_0

    :cond_2
    new-instance p0, Lantlr/Lookahead;

    invoke-direct {p0}, Lantlr/Lookahead;-><init>()V

    goto :goto_1

    :cond_3
    :goto_0
    iget-object v0, p2, Lantlr/BlockEndElement;->lock:[Z

    const/4 v1, 0x1

    aput-boolean v1, v0, p1

    iget-object v0, p2, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0, p1, v0}, Lantlr/LLkAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object p0

    iget-object v0, p2, Lantlr/BlockEndElement;->lock:[Z

    const/4 v1, 0x0

    aput-boolean v1, v0, p1

    :goto_1
    iget-object p2, p2, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    instance-of v0, p2, Lantlr/TreeElement;

    if-eqz v0, :cond_4

    const/4 p1, 0x3

    invoke-static {p1}, Lantlr/Lookahead;->of(I)Lantlr/Lookahead;

    move-result-object p1

    :goto_2
    invoke-virtual {p0, p1}, Lantlr/Lookahead;->combineWith(Lantlr/Lookahead;)V

    goto :goto_3

    :cond_4
    instance-of v0, p2, Lantlr/SynPredBlock;

    if-eqz v0, :cond_5

    invoke-virtual {p0}, Lantlr/Lookahead;->setEpsilon()V

    goto :goto_3

    :cond_5
    iget-object p2, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {p2, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p1

    goto :goto_2

    :goto_3
    return-object p0
.end method

.method public look(ILantlr/CharLiteralElement;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "lookCharLiteral("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const/4 v0, 0x1

    if-le p1, v0, :cond_1

    iget-object p0, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    sub-int/2addr p1, v0

    invoke-virtual {p0, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_1
    iget-boolean p1, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    if-eqz p1, :cond_5

    iget-boolean p1, p2, Lantlr/GrammarAtom;->not:Z

    if-eqz p1, :cond_4

    iget-object p1, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    check-cast p1, Lantlr/LexerGrammar;

    iget-object p1, p1, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lantlr/collections/impl/BitSet;

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_2

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "charVocab is "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_2
    invoke-direct {p0, p1, p2}, Lantlr/LLkAnalyzer;->removeCompetingPredictionSets(Lantlr/collections/impl/BitSet;Lantlr/AlternativeElement;)V

    iget-boolean p0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz p0, :cond_3

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v0, "charVocab after removal of prior alt lookahead "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/collections/impl/BitSet;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_3
    invoke-virtual {p2}, Lantlr/GrammarAtom;->getType()I

    move-result p0

    invoke-virtual {p1, p0}, Lantlr/collections/impl/BitSet;->clear(I)V

    new-instance p0, Lantlr/Lookahead;

    invoke-direct {p0, p1}, Lantlr/Lookahead;-><init>(Lantlr/collections/impl/BitSet;)V

    return-object p0

    :cond_4
    invoke-virtual {p2}, Lantlr/GrammarAtom;->getType()I

    move-result p0

    invoke-static {p0}, Lantlr/Lookahead;->of(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_5
    iget-object p0, p0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    const-string p1, "Character literal reference found in parser"

    invoke-virtual {p0, p1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    invoke-virtual {p2}, Lantlr/GrammarAtom;->getType()I

    move-result p0

    invoke-static {p0}, Lantlr/Lookahead;->of(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public look(ILantlr/CharRangeElement;)Lantlr/Lookahead;
    .locals 2

    iget-boolean p0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz p0, :cond_0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "lookCharRange("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v1, ")"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const/4 p0, 0x1

    if-le p1, p0, :cond_1

    iget-object p2, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    sub-int/2addr p1, p0

    invoke-virtual {p2, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_1
    iget-char p1, p2, Lantlr/CharRangeElement;->begin:C

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->of(I)Lantlr/collections/impl/BitSet;

    move-result-object p1

    iget-char v0, p2, Lantlr/CharRangeElement;->begin:C

    add-int/2addr v0, p0

    :goto_0
    iget-char p0, p2, Lantlr/CharRangeElement;->end:C

    if-gt v0, p0, :cond_2

    invoke-virtual {p1, v0}, Lantlr/collections/impl/BitSet;->add(I)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    new-instance p0, Lantlr/Lookahead;

    invoke-direct {p0, p1}, Lantlr/Lookahead;-><init>(Lantlr/collections/impl/BitSet;)V

    return-object p0
.end method

.method public look(ILantlr/GrammarAtom;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "look("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, "["

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Lantlr/GrammarAtom;->getType()I

    move-result v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, "])"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    const-string v1, "token reference found in lexer"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_1
    const/4 v0, 0x1

    if-le p1, v0, :cond_2

    iget-object p0, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    sub-int/2addr p1, v0

    invoke-virtual {p0, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_2
    invoke-virtual {p2}, Lantlr/GrammarAtom;->getType()I

    move-result p1

    invoke-static {p1}, Lantlr/Lookahead;->of(I)Lantlr/Lookahead;

    move-result-object p1

    iget-boolean v0, p2, Lantlr/GrammarAtom;->not:Z

    if-eqz v0, :cond_3

    iget-object v0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->maxTokenType()I

    move-result v0

    iget-object v1, p1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x4

    invoke-virtual {v1, v2, v0}, Lantlr/collections/impl/BitSet;->notInPlace(II)V

    iget-object v0, p1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    invoke-direct {p0, v0, p2}, Lantlr/LLkAnalyzer;->removeCompetingPredictionSets(Lantlr/collections/impl/BitSet;Lantlr/AlternativeElement;)V

    :cond_3
    return-object p1
.end method

.method public look(ILantlr/OneOrMoreBlock;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "look+"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0, p1, p2}, Lantlr/LLkAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public look(ILantlr/RuleBlock;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "lookRuleBlk("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0, p1, p2}, Lantlr/LLkAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public look(ILantlr/RuleEndElement;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "lookRuleBlockEnd("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, "); noFOLLOW="

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean v2, p2, Lantlr/RuleEndElement;->noFOLLOW:Z

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string v2, "; lock is "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p2, Lantlr/BlockEndElement;->lock:[Z

    aget-boolean v2, v2, p1

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-boolean v0, p2, Lantlr/RuleEndElement;->noFOLLOW:Z

    if-eqz v0, :cond_1

    new-instance p0, Lantlr/Lookahead;

    invoke-direct {p0}, Lantlr/Lookahead;-><init>()V

    invoke-virtual {p0}, Lantlr/Lookahead;->setEpsilon()V

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->of(I)Lantlr/collections/impl/BitSet;

    move-result-object p1

    iput-object p1, p0, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    return-object p0

    :cond_1
    invoke-virtual {p0, p1, p2}, Lantlr/LLkAnalyzer;->FOLLOW(ILantlr/RuleEndElement;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public look(ILantlr/RuleRefElement;)Lantlr/Lookahead;
    .locals 6

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "lookRuleRef("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-object v1, p2, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    if-eqz v0, :cond_6

    iget-boolean v1, v0, Lantlr/RuleSymbol;->defined:Z

    if-nez v1, :cond_1

    goto/16 :goto_1

    :cond_1
    invoke-virtual {v0}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v0

    iget-object v0, v0, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    iget-boolean v1, v0, Lantlr/RuleEndElement;->noFOLLOW:Z

    const/4 v2, 0x1

    iput-boolean v2, v0, Lantlr/RuleEndElement;->noFOLLOW:Z

    iget-object v2, p2, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {p0, p1, v2}, Lantlr/LLkAnalyzer;->look(ILjava/lang/String;)Lantlr/Lookahead;

    move-result-object v2

    iget-boolean v3, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v3, :cond_2

    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v4, "back from rule ref to "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-object v5, p2, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_2
    iput-boolean v1, v0, Lantlr/RuleEndElement;->noFOLLOW:Z

    iget-object v0, v2, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    if-eqz v0, :cond_3

    iget-object v0, p0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    const-string v1, "infinite recursion to rule "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, v2, Lantlr/Lookahead;->cycle:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " from rule "

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p2, Lantlr/AlternativeElement;->enclosingRuleName:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    iget-object v3, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p2}, Lantlr/GrammarElement;->getLine()I

    move-result v4

    invoke-virtual {p2}, Lantlr/GrammarElement;->getColumn()I

    move-result v5

    invoke-virtual {v0, v1, v3, v4, v5}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_3
    invoke-virtual {v2}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v0

    if-eqz v0, :cond_5

    iget-boolean p0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz p0, :cond_4

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v0, "rule ref to "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p2, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, " has eps, depth: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, v2, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_4
    invoke-virtual {v2}, Lantlr/Lookahead;->resetEpsilon()V

    iget-object p0, v2, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object p0

    const/4 v0, 0x0

    iput-object v0, v2, Lantlr/Lookahead;->epsilonDepth:Lantlr/collections/impl/BitSet;

    const/4 v0, 0x0

    :goto_0
    array-length v1, p0

    if-ge v0, v1, :cond_5

    aget v1, p0, v0

    sub-int v1, p1, v1

    sub-int v1, p1, v1

    iget-object v3, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {v3, v1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object v1

    invoke-virtual {v2, v1}, Lantlr/Lookahead;->combineWith(Lantlr/Lookahead;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_5
    return-object v2

    :cond_6
    :goto_1
    iget-object p1, p0, Lantlr/LLkAnalyzer;->tool:Lantlr/Tool;

    const-string v0, "no definition of rule "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v1, p2, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/GrammarElement;->getLine()I

    move-result v1

    invoke-virtual {p2}, Lantlr/GrammarElement;->getColumn()I

    move-result p2

    invoke-virtual {p1, v0, p0, v1, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    new-instance p0, Lantlr/Lookahead;

    invoke-direct {p0}, Lantlr/Lookahead;-><init>()V

    return-object p0
.end method

.method public look(ILantlr/StringLiteralElement;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "lookStringLiteral("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_2

    iget-object p0, p2, Lantlr/StringLiteralElement;->processedAtomText:Ljava/lang/String;

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result p0

    if-le p1, p0, :cond_1

    iget-object p0, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    iget-object p2, p2, Lantlr/StringLiteralElement;->processedAtomText:Ljava/lang/String;

    invoke-virtual {p2}, Ljava/lang/String;->length()I

    move-result p2

    sub-int/2addr p1, p2

    invoke-virtual {p0, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_1
    iget-object p0, p2, Lantlr/StringLiteralElement;->processedAtomText:Ljava/lang/String;

    sub-int/2addr p1, v1

    invoke-virtual {p0, p1}, Ljava/lang/String;->charAt(I)C

    move-result p0

    invoke-static {p0}, Lantlr/Lookahead;->of(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_2
    if-le p1, v1, :cond_3

    iget-object p0, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    sub-int/2addr p1, v1

    invoke-virtual {p0, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_3
    invoke-virtual {p2}, Lantlr/GrammarAtom;->getType()I

    move-result p1

    invoke-static {p1}, Lantlr/Lookahead;->of(I)Lantlr/Lookahead;

    move-result-object p1

    iget-boolean p2, p2, Lantlr/GrammarAtom;->not:Z

    if-eqz p2, :cond_4

    iget-object p0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0}, Lantlr/TokenManager;->maxTokenType()I

    move-result p0

    iget-object p2, p1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    const/4 v0, 0x4

    invoke-virtual {p2, v0, p0}, Lantlr/collections/impl/BitSet;->notInPlace(II)V

    :cond_4
    return-object p1
.end method

.method public look(ILantlr/SynPredBlock;)Lantlr/Lookahead;
    .locals 2

    iget-boolean p0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz p0, :cond_0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "look=>("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v1, ")"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object p0, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {p0, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public look(ILantlr/TokenRangeElement;)Lantlr/Lookahead;
    .locals 2

    iget-boolean p0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz p0, :cond_0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "lookTokenRange("

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v1, ")"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const/4 p0, 0x1

    if-le p1, p0, :cond_1

    iget-object p2, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    sub-int/2addr p1, p0

    invoke-virtual {p2, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_1
    iget p1, p2, Lantlr/TokenRangeElement;->begin:I

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->of(I)Lantlr/collections/impl/BitSet;

    move-result-object p1

    iget v0, p2, Lantlr/TokenRangeElement;->begin:I

    add-int/2addr v0, p0

    :goto_0
    iget p0, p2, Lantlr/TokenRangeElement;->end:I

    if-gt v0, p0, :cond_2

    invoke-virtual {p1, v0}, Lantlr/collections/impl/BitSet;->add(I)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    new-instance p0, Lantlr/Lookahead;

    invoke-direct {p0, p1}, Lantlr/Lookahead;-><init>(Lantlr/collections/impl/BitSet;)V

    return-object p0
.end method

.method public look(ILantlr/TreeElement;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "look("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p2, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, "["

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p2, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    invoke-virtual {v2}, Lantlr/GrammarAtom;->getType()I

    move-result v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, "])"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const/4 v0, 0x1

    if-le p1, v0, :cond_1

    iget-object p0, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    sub-int/2addr p1, v0

    invoke-virtual {p0, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_1
    iget-object p1, p2, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    instance-of v1, p1, Lantlr/WildcardElement;

    if-eqz v1, :cond_2

    invoke-virtual {p1, v0}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    goto :goto_0

    :cond_2
    invoke-virtual {p1}, Lantlr/GrammarAtom;->getType()I

    move-result p1

    invoke-static {p1}, Lantlr/Lookahead;->of(I)Lantlr/Lookahead;

    move-result-object p1

    iget-object p2, p2, Lantlr/TreeElement;->root:Lantlr/GrammarAtom;

    iget-boolean p2, p2, Lantlr/GrammarAtom;->not:Z

    if-eqz p2, :cond_3

    iget-object p0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-object p0, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {p0}, Lantlr/TokenManager;->maxTokenType()I

    move-result p0

    iget-object p2, p1, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    const/4 v0, 0x4

    invoke-virtual {p2, v0, p0}, Lantlr/collections/impl/BitSet;->notInPlace(II)V

    :cond_3
    move-object p0, p1

    :goto_0
    return-object p0
.end method

.method public look(ILantlr/WildcardElement;)Lantlr/Lookahead;
    .locals 5

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const-string v1, ","

    const-string v2, "look("

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v4, ")"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const/4 v0, 0x1

    if-le p1, v0, :cond_1

    iget-object p0, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    sub-int/2addr p1, v0

    invoke-virtual {p0, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p0

    return-object p0

    :cond_1
    iget-boolean v3, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    if-eqz v3, :cond_2

    iget-object p0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    check-cast p0, Lantlr/LexerGrammar;

    iget-object p0, p0, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/collections/impl/BitSet;

    goto :goto_0

    :cond_2
    new-instance v3, Lantlr/collections/impl/BitSet;

    invoke-direct {v3, v0}, Lantlr/collections/impl/BitSet;-><init>(I)V

    iget-object v0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v0}, Lantlr/TokenManager;->maxTokenType()I

    move-result v0

    const/4 v4, 0x4

    invoke-virtual {v3, v4, v0}, Lantlr/collections/impl/BitSet;->notInPlace(II)V

    iget-boolean p0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz p0, :cond_3

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string p1, ") after not: "

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_3
    move-object p0, v3

    :goto_0
    new-instance p1, Lantlr/Lookahead;

    invoke-direct {p1, p0}, Lantlr/Lookahead;-><init>(Lantlr/collections/impl/BitSet;)V

    return-object p1
.end method

.method public look(ILantlr/ZeroOrMoreBlock;)Lantlr/Lookahead;
    .locals 3

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "look*("

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0, p1, p2}, Lantlr/LLkAnalyzer;->look(ILantlr/AlternativeBlock;)Lantlr/Lookahead;

    move-result-object p0

    iget-object p2, p2, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {p2, p1}, Lantlr/GrammarElement;->look(I)Lantlr/Lookahead;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Lookahead;->combineWith(Lantlr/Lookahead;)V

    return-object p0
.end method

.method public look(ILjava/lang/String;)Lantlr/Lookahead;
    .locals 8

    iget-boolean v0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    const-string v1, ","

    if-eqz v0, :cond_0

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "lookRuleName("

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, ")"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    iget-object v0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {v0, p2}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    invoke-virtual {v0}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v0

    iget-object v2, v0, Lantlr/RuleBlock;->lock:[Z

    aget-boolean v3, v2, p1

    if-eqz v3, :cond_2

    iget-boolean p0, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz p0, :cond_1

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string p1, "infinite recursion to rule "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {v0}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_1
    new-instance p0, Lantlr/Lookahead;

    invoke-direct {p0, p2}, Lantlr/Lookahead;-><init>(Ljava/lang/String;)V

    return-object p0

    :cond_2
    iget-object v3, v0, Lantlr/RuleBlock;->cache:[Lantlr/Lookahead;

    aget-object v3, v3, p1

    const-string v4, " cache: "

    const-string v5, " result in FIRST "

    if-eqz v3, :cond_4

    iget-boolean v2, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v2, :cond_3

    sget-object v2, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "found depth "

    invoke-virtual {v3, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p2, v0, Lantlr/RuleBlock;->cache:[Lantlr/Lookahead;

    aget-object p2, p2, p1

    iget-object v4, p0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object p0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {p2, v1, v4, p0}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v2, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_3
    iget-object p0, v0, Lantlr/RuleBlock;->cache:[Lantlr/Lookahead;

    aget-object p0, p0, p1

    invoke-virtual {p0}, Lantlr/Lookahead;->clone()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/Lookahead;

    return-object p0

    :cond_4
    const/4 v3, 0x1

    aput-boolean v3, v2, p1

    invoke-virtual {p0, p1, v0}, Lantlr/LLkAnalyzer;->look(ILantlr/RuleBlock;)Lantlr/Lookahead;

    move-result-object v2

    iget-object v3, v0, Lantlr/RuleBlock;->lock:[Z

    const/4 v6, 0x0

    aput-boolean v6, v3, p1

    iget-object v3, v0, Lantlr/RuleBlock;->cache:[Lantlr/Lookahead;

    invoke-virtual {v2}, Lantlr/Lookahead;->clone()Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lantlr/Lookahead;

    aput-object v6, v3, p1

    iget-boolean v3, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    if-eqz v3, :cond_5

    sget-object v3, Ljava/lang/System;->out:Ljava/io/PrintStream;

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "saving depth "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p2, v0, Lantlr/RuleBlock;->cache:[Lantlr/Lookahead;

    aget-object p1, p2, p1

    iget-object p2, p0, Lantlr/LLkAnalyzer;->charFormatter:Lantlr/CharFormatter;

    iget-object p0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    invoke-virtual {p1, v1, p2, p0}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/CharFormatter;Lantlr/Grammar;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v6, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v3, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_5
    return-object v2
.end method

.method public setGrammar(Lantlr/Grammar;)V
    .locals 1

    iget-object v0, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    if-eqz v0, :cond_0

    invoke-direct {p0}, Lantlr/LLkAnalyzer;->reset()V

    :cond_0
    iput-object p1, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    iget-object p1, p0, Lantlr/LLkAnalyzer;->grammar:Lantlr/Grammar;

    instance-of v0, p1, Lantlr/LexerGrammar;

    iput-boolean v0, p0, Lantlr/LLkAnalyzer;->lexicalAnalysis:Z

    iget-boolean p1, p1, Lantlr/Grammar;->analyzerDebug:Z

    iput-boolean p1, p0, Lantlr/LLkAnalyzer;->DEBUG_ANALYZER:Z

    return-void
.end method

.method public subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z
    .locals 4

    instance-of p0, p1, Lantlr/ZeroOrMoreBlock;

    const/4 v0, 0x0

    if-nez p0, :cond_7

    instance-of p0, p1, Lantlr/OneOrMoreBlock;

    if-nez p0, :cond_7

    instance-of p0, p1, Lantlr/SynPredBlock;

    if-eqz p0, :cond_0

    goto :goto_2

    :cond_0
    iget-object p0, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {p0}, Lantlr/collections/impl/Vector;->size()I

    move-result p0

    if-nez p0, :cond_1

    return v0

    :cond_1
    move p0, v0

    :goto_0
    iget-object v1, p1, Lantlr/AlternativeBlock;->alternatives:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    const/4 v2, 0x1

    if-ge p0, v1, :cond_6

    invoke-virtual {p1, p0}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object v1

    iget-object v3, v1, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    if-nez v3, :cond_5

    iget-object v3, v1, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-nez v3, :cond_5

    iget-object v3, v1, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz v3, :cond_2

    goto :goto_1

    :cond_2
    iget-object v1, v1, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    instance-of v3, v1, Lantlr/CharLiteralElement;

    if-nez v3, :cond_3

    instance-of v3, v1, Lantlr/TokenRefElement;

    if-nez v3, :cond_3

    instance-of v3, v1, Lantlr/CharRangeElement;

    if-nez v3, :cond_3

    instance-of v3, v1, Lantlr/TokenRangeElement;

    if-nez v3, :cond_3

    instance-of v3, v1, Lantlr/StringLiteralElement;

    if-eqz v3, :cond_5

    if-nez p2, :cond_5

    :cond_3
    iget-object v3, v1, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    instance-of v3, v3, Lantlr/BlockEndElement;

    if-eqz v3, :cond_5

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result v1

    if-eq v1, v2, :cond_4

    goto :goto_1

    :cond_4
    add-int/lit8 p0, p0, 0x1

    goto :goto_0

    :cond_5
    :goto_1
    return v0

    :cond_6
    return v2

    :cond_7
    :goto_2
    return v0
.end method
