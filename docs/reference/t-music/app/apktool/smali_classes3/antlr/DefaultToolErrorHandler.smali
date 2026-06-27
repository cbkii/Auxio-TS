.class public Lantlr/DefaultToolErrorHandler;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/ToolErrorHandler;


# instance fields
.field public final antlrTool:Lantlr/Tool;

.field public javaCharFormatter:Lantlr/CharFormatter;


# direct methods
.method public constructor <init>(Lantlr/Tool;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lantlr/JavaCharFormatter;

    invoke-direct {v0}, Lantlr/JavaCharFormatter;-><init>()V

    iput-object v0, p0, Lantlr/DefaultToolErrorHandler;->javaCharFormatter:Lantlr/CharFormatter;

    iput-object p1, p0, Lantlr/DefaultToolErrorHandler;->antlrTool:Lantlr/Tool;

    return-void
.end method

.method private dumpSets([Ljava/lang/String;ILantlr/Grammar;ZI[Lantlr/Lookahead;)V
    .locals 5

    new-instance v0, Ljava/lang/StringBuffer;

    const/16 v1, 0x64

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(I)V

    const/4 v1, 0x1

    :goto_0
    if-gt v1, p5, :cond_2

    const-string v2, "k=="

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(I)Ljava/lang/StringBuffer;

    const/16 v2, 0x3a

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    const-string v2, ","

    if-eqz p4, :cond_0

    aget-object v3, p6, v1

    iget-object v3, v3, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    iget-object v4, p0, Lantlr/DefaultToolErrorHandler;->javaCharFormatter:Lantlr/CharFormatter;

    invoke-virtual {v3, v2, v4}, Lantlr/collections/impl/BitSet;->toStringWithRanges(Ljava/lang/String;Lantlr/CharFormatter;)Ljava/lang/String;

    move-result-object v2

    aget-object v3, p6, v1

    invoke-virtual {v3}, Lantlr/Lookahead;->containsEpsilon()Z

    move-result v3

    if-eqz v3, :cond_1

    const-string v3, "<end-of-token>"

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v3

    if-lez v3, :cond_1

    const/16 v3, 0x2c

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    goto :goto_1

    :cond_0
    aget-object v3, p6, v1

    iget-object v3, v3, Lantlr/Lookahead;->fset:Lantlr/collections/impl/BitSet;

    iget-object v4, p3, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v4}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v4

    invoke-virtual {v3, v2, v4}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;Lantlr/collections/impl/Vector;)Ljava/lang/String;

    move-result-object v2

    :cond_1
    :goto_1
    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 v2, p2, 0x1

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v3

    aput-object v3, p1, p2

    const/4 p2, 0x0

    invoke-virtual {v0, p2}, Ljava/lang/StringBuffer;->setLength(I)V

    add-int/lit8 v1, v1, 0x1

    move p2, v2

    goto :goto_0

    :cond_2
    return-void
.end method


# virtual methods
.method public warnAltAmbiguity(Lantlr/Grammar;Lantlr/AlternativeBlock;ZI[Lantlr/Lookahead;II)V
    .locals 8

    new-instance v0, Ljava/lang/StringBuffer;

    const/16 v1, 0x64

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(I)V

    instance-of v1, p2, Lantlr/RuleBlock;

    const-string v2, " and "

    if-eqz v1, :cond_0

    move-object v1, p2

    check-cast v1, Lantlr/RuleBlock;

    invoke-virtual {v1}, Lantlr/RuleBlock;->isLexerAutoGenRule()Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {p2, p6}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object p6

    invoke-virtual {p2, p7}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object p7

    iget-object p6, p6, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    check-cast p6, Lantlr/RuleRefElement;

    iget-object p7, p7, Lantlr/Alternative;->head:Lantlr/AlternativeElement;

    check-cast p7, Lantlr/RuleRefElement;

    iget-object p6, p6, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-static {p6}, Lantlr/CodeGenerator;->reverseLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p6

    iget-object p7, p7, Lantlr/RuleRefElement;->targetRule:Ljava/lang/String;

    invoke-static {p7}, Lantlr/CodeGenerator;->reverseLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p7

    const-string v1, "lexical nondeterminism between rules "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0, p6}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v0, p7}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string p6, " upon"

    goto :goto_0

    :cond_0
    if-eqz p3, :cond_1

    const-string v1, "lexical "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_1
    const-string v1, "nondeterminism between alts "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 p6, p6, 0x1

    invoke-virtual {v0, p6}, Ljava/lang/StringBuffer;->append(I)Ljava/lang/StringBuffer;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 p7, p7, 0x1

    invoke-virtual {v0, p7}, Ljava/lang/StringBuffer;->append(I)Ljava/lang/StringBuffer;

    const-string p6, " of block upon"

    :goto_0
    invoke-virtual {v0, p6}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 p6, p4, 0x1

    new-array p6, p6, [Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p7

    const/4 v0, 0x0

    aput-object p7, p6, v0

    const/4 v3, 0x1

    move-object v1, p0

    move-object v2, p6

    move-object v4, p1

    move v5, p3

    move v6, p4

    move-object v7, p5

    invoke-direct/range {v1 .. v7}, Lantlr/DefaultToolErrorHandler;->dumpSets([Ljava/lang/String;ILantlr/Grammar;ZI[Lantlr/Lookahead;)V

    iget-object p0, p0, Lantlr/DefaultToolErrorHandler;->antlrTool:Lantlr/Tool;

    invoke-virtual {p1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/GrammarElement;->getLine()I

    move-result p3

    invoke-virtual {p2}, Lantlr/GrammarElement;->getColumn()I

    move-result p2

    invoke-virtual {p0, p6, p1, p3, p2}, Lantlr/Tool;->warning([Ljava/lang/String;Ljava/lang/String;II)V

    return-void
.end method

.method public warnAltExitAmbiguity(Lantlr/Grammar;Lantlr/BlockWithImpliedExitPath;ZI[Lantlr/Lookahead;I)V
    .locals 8

    add-int/lit8 v0, p4, 0x2

    new-array v0, v0, [Ljava/lang/String;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    if-eqz p3, :cond_0

    const-string v2, "lexical "

    goto :goto_0

    :cond_0
    const-string v2, ""

    :goto_0
    const-string v3, "nondeterminism upon"

    invoke-static {v1, v2, v3}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    const/4 v2, 0x0

    aput-object v1, v0, v2

    const/4 v3, 0x1

    move-object v1, p0

    move-object v2, v0

    move-object v4, p1

    move v5, p3

    move v6, p4

    move-object v7, p5

    invoke-direct/range {v1 .. v7}, Lantlr/DefaultToolErrorHandler;->dumpSets([Ljava/lang/String;ILantlr/Grammar;ZI[Lantlr/Lookahead;)V

    add-int/lit8 p4, p4, 0x1

    const-string p3, "between alt "

    invoke-static {p3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p3

    add-int/lit8 p6, p6, 0x1

    invoke-virtual {p3, p6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p5, " and exit branch of block"

    invoke-virtual {p3, p5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p3

    aput-object p3, v0, p4

    iget-object p0, p0, Lantlr/DefaultToolErrorHandler;->antlrTool:Lantlr/Tool;

    invoke-virtual {p1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/GrammarElement;->getLine()I

    move-result p3

    invoke-virtual {p2}, Lantlr/GrammarElement;->getColumn()I

    move-result p2

    invoke-virtual {p0, v0, p1, p3, p2}, Lantlr/Tool;->warning([Ljava/lang/String;Ljava/lang/String;II)V

    return-void
.end method
