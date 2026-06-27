.class public Lantlr/MakeGrammar;
.super Lantlr/DefineGrammarSymbols;
.source ""


# instance fields
.field public blocks:Lantlr/collections/Stack;

.field public currentExceptionSpec:Lantlr/ExceptionSpec;

.field public grammarError:Z

.field public lastRuleRef:Lantlr/RuleRefElement;

.field public nested:I

.field public ruleBlock:Lantlr/RuleBlock;

.field public ruleEnd:Lantlr/RuleEndElement;


# direct methods
.method public constructor <init>(Lantlr/Tool;[Ljava/lang/String;Lantlr/LLkAnalyzer;)V
    .locals 0

    invoke-direct {p0, p1, p2, p3}, Lantlr/DefineGrammarSymbols;-><init>(Lantlr/Tool;[Ljava/lang/String;Lantlr/LLkAnalyzer;)V

    new-instance p1, Lantlr/collections/impl/LList;

    invoke-direct {p1}, Lantlr/collections/impl/LList;-><init>()V

    iput-object p1, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/MakeGrammar;->nested:I

    iput-boolean p1, p0, Lantlr/MakeGrammar;->grammarError:Z

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    return-void
.end method

.method public static createNextTokenRule(Lantlr/Grammar;Lantlr/collections/impl/Vector;Ljava/lang/String;)Lantlr/RuleBlock;
    .locals 10

    new-instance v0, Lantlr/RuleBlock;

    invoke-direct {v0, p0, p2}, Lantlr/RuleBlock;-><init>(Lantlr/Grammar;Ljava/lang/String;)V

    invoke-virtual {p0}, Lantlr/Grammar;->getDefaultErrorHandler()Z

    move-result p2

    invoke-virtual {v0, p2}, Lantlr/RuleBlock;->setDefaultErrorHandler(Z)V

    new-instance p2, Lantlr/RuleEndElement;

    invoke-direct {p2, p0}, Lantlr/RuleEndElement;-><init>(Lantlr/Grammar;)V

    invoke-virtual {v0, p2}, Lantlr/RuleBlock;->setEndElement(Lantlr/RuleEndElement;)V

    iput-object v0, p2, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    invoke-virtual {p1}, Lantlr/collections/impl/Vector;->size()I

    move-result v3

    const/4 v4, 0x1

    if-ge v2, v3, :cond_3

    invoke-virtual {p1, v2}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/RuleSymbol;

    invoke-virtual {v3}, Lantlr/RuleSymbol;->isDefined()Z

    move-result v5

    if-nez v5, :cond_0

    iget-object v5, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v6, "Lexer rule "

    invoke-static {v6}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    iget-object v3, v3, Lantlr/GrammarSymbol;->id:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " is not defined"

    invoke-virtual {v6, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v5, v3}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_1

    :cond_0
    iget-object v5, v3, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    const-string v6, "public"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    new-instance v5, Lantlr/Alternative;

    invoke-direct {v5}, Lantlr/Alternative;-><init>()V

    invoke-virtual {v3}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v6

    invoke-virtual {v6}, Lantlr/RuleBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v6

    if-eqz v6, :cond_1

    invoke-virtual {v6}, Lantlr/collections/impl/Vector;->size()I

    move-result v7

    if-ne v7, v4, :cond_1

    invoke-virtual {v6, v1}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lantlr/Alternative;

    iget-object v6, v6, Lantlr/Alternative;->semPred:Ljava/lang/String;

    if-eqz v6, :cond_1

    iput-object v6, v5, Lantlr/Alternative;->semPred:Ljava/lang/String;

    :cond_1
    new-instance v6, Lantlr/RuleRefElement;

    new-instance v7, Lantlr/CommonToken;

    invoke-virtual {v3}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v8

    const/16 v9, 0x29

    invoke-direct {v7, v9, v8}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    invoke-direct {v6, p0, v7, v4}, Lantlr/RuleRefElement;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    const-string v7, "theRetToken"

    invoke-virtual {v6, v7}, Lantlr/RuleRefElement;->setLabel(Ljava/lang/String;)V

    const-string v7, "nextToken"

    iput-object v7, v6, Lantlr/AlternativeElement;->enclosingRuleName:Ljava/lang/String;

    iput-object p2, v6, Lantlr/AlternativeElement;->next:Lantlr/AlternativeElement;

    invoke-virtual {v5, v6}, Lantlr/Alternative;->addElement(Lantlr/AlternativeElement;)V

    invoke-virtual {v5, v4}, Lantlr/Alternative;->setAutoGen(Z)V

    invoke-virtual {v0, v5}, Lantlr/RuleBlock;->addAlternative(Lantlr/Alternative;)V

    invoke-virtual {v3, v6}, Lantlr/RuleSymbol;->addReference(Lantlr/RuleRefElement;)V

    :cond_2
    :goto_1
    add-int/lit8 v2, v2, 0x1

    goto/16 :goto_0

    :cond_3
    invoke-virtual {v0, v4}, Lantlr/RuleBlock;->setAutoGen(Z)V

    invoke-virtual {v0}, Lantlr/RuleBlock;->prepareForAnalysis()V

    return-object v0
.end method

.method private createOptionalRuleRef(Ljava/lang/String;Lantlr/Token;)Lantlr/AlternativeBlock;
    .locals 4

    new-instance v0, Lantlr/AlternativeBlock;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    const/4 v2, 0x0

    invoke-direct {v0, v1, p2, v2}, Lantlr/AlternativeBlock;-><init>(Lantlr/Grammar;Lantlr/Token;Z)V

    invoke-static {p1}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iget-object v2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v2, v1}, Lantlr/Grammar;->isDefined(Ljava/lang/String;)Z

    move-result v2

    if-nez v2, :cond_0

    iget-object v2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    new-instance v3, Lantlr/RuleSymbol;

    invoke-direct {v3, v1}, Lantlr/RuleSymbol;-><init>(Ljava/lang/String;)V

    invoke-virtual {v2, v3}, Lantlr/Grammar;->define(Lantlr/RuleSymbol;)V

    :cond_0
    new-instance v1, Lantlr/CommonToken;

    const/16 v2, 0x18

    invoke-direct {v1, v2, p1}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result p1

    invoke-virtual {v1, p1}, Lantlr/CommonToken;->setLine(I)V

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p1

    invoke-virtual {v1, p1}, Lantlr/CommonToken;->setLine(I)V

    new-instance p1, Lantlr/RuleRefElement;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    const/4 v2, 0x1

    invoke-direct {p1, p2, v1, v2}, Lantlr/RuleRefElement;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    iget-object p2, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    iget-object p2, p2, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    iput-object p2, p1, Lantlr/AlternativeElement;->enclosingRuleName:Ljava/lang/String;

    new-instance p2, Lantlr/BlockEndElement;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {p2, p0}, Lantlr/BlockEndElement;-><init>(Lantlr/Grammar;)V

    iput-object v0, p2, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    new-instance p0, Lantlr/Alternative;

    invoke-direct {p0, p1}, Lantlr/Alternative;-><init>(Lantlr/AlternativeElement;)V

    invoke-virtual {p0, p2}, Lantlr/Alternative;->addElement(Lantlr/AlternativeElement;)V

    invoke-virtual {v0, p0}, Lantlr/AlternativeBlock;->addAlternative(Lantlr/Alternative;)V

    new-instance p0, Lantlr/Alternative;

    invoke-direct {p0}, Lantlr/Alternative;-><init>()V

    invoke-virtual {p0, p2}, Lantlr/Alternative;->addElement(Lantlr/AlternativeElement;)V

    invoke-virtual {v0, p0}, Lantlr/AlternativeBlock;->addAlternative(Lantlr/Alternative;)V

    invoke-virtual {v0}, Lantlr/AlternativeBlock;->prepareForAnalysis()V

    return-object v0
.end method

.method private labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V
    .locals 3

    if-eqz p2, :cond_2

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v1}, Lantlr/collections/impl/Vector;->size()I

    move-result v1

    if-ge v0, v1, :cond_1

    iget-object v1, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    iget-object v1, v1, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {v1, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/AlternativeElement;

    invoke-virtual {v1}, Lantlr/AlternativeElement;->getLabel()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_0

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v0, "Label \'"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\' has already been defined"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    invoke-virtual {p1, v0, p0, v1, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_0
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Lantlr/AlternativeElement;->setLabel(Ljava/lang/String;)V

    iget-object p0, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    iget-object p0, p0, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    :cond_2
    return-void
.end method

.method public static setBlock(Lantlr/AlternativeBlock;Lantlr/AlternativeBlock;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/AlternativeBlock;->setAlternatives(Lantlr/collections/impl/Vector;)V

    iget-object v0, p1, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    iput-object v0, p0, Lantlr/AlternativeBlock;->initAction:Ljava/lang/String;

    iget-object v0, p1, Lantlr/AlternativeBlock;->label:Ljava/lang/String;

    iput-object v0, p0, Lantlr/AlternativeBlock;->label:Ljava/lang/String;

    iget-boolean v0, p1, Lantlr/AlternativeBlock;->hasASynPred:Z

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->hasASynPred:Z

    iget-boolean v0, p1, Lantlr/AlternativeBlock;->hasAnAction:Z

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->hasAnAction:Z

    iget-boolean v0, p1, Lantlr/AlternativeBlock;->warnWhenFollowAmbig:Z

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->warnWhenFollowAmbig:Z

    iget-boolean v0, p1, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    iget v0, p1, Lantlr/GrammarElement;->line:I

    iput v0, p0, Lantlr/GrammarElement;->line:I

    iget-boolean v0, p1, Lantlr/AlternativeBlock;->greedy:Z

    iput-boolean v0, p0, Lantlr/AlternativeBlock;->greedy:Z

    iget-boolean p1, p1, Lantlr/AlternativeBlock;->greedySet:Z

    iput-boolean p1, p0, Lantlr/AlternativeBlock;->greedySet:Z

    return-void
.end method


# virtual methods
.method public abortGrammar()V
    .locals 4

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Lantlr/Grammar;->getClassName()Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_0
    const-string v0, "unknown grammar"

    :goto_0
    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "aborting grammar \'"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "\' due to errors"

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Lantlr/Tool;->error(Ljava/lang/String;)V

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->abortGrammar()V

    return-void
.end method

.method public addElementToCurrentAlt(Lantlr/AlternativeElement;)V
    .locals 1

    iget-object v0, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    iget-object v0, v0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    iput-object v0, p1, Lantlr/AlternativeElement;->enclosingRuleName:Ljava/lang/String;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    invoke-virtual {p0, p1}, Lantlr/BlockContext;->addAlternativeElement(Lantlr/AlternativeElement;)V

    return-void
.end method

.method public beginAlt(Z)V
    .locals 1

    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->beginAlt(Z)V

    new-instance v0, Lantlr/Alternative;

    invoke-direct {v0}, Lantlr/Alternative;-><init>()V

    invoke-virtual {v0, p1}, Lantlr/Alternative;->setAutoGen(Z)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0, v0}, Lantlr/AlternativeBlock;->addAlternative(Lantlr/Alternative;)V

    return-void
.end method

.method public beginChildList()V
    .locals 1

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->beginChildList()V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    new-instance v0, Lantlr/Alternative;

    invoke-direct {v0}, Lantlr/Alternative;-><init>()V

    invoke-virtual {p0, v0}, Lantlr/AlternativeBlock;->addAlternative(Lantlr/Alternative;)V

    return-void
.end method

.method public beginExceptionGroup()V
    .locals 1

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->beginExceptionGroup()V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    instance-of v0, v0, Lantlr/RuleBlock;

    if-nez v0, :cond_0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v0, "beginExceptionGroup called outside of rule block"

    invoke-virtual {p0, v0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public beginExceptionSpec(Lantlr/Token;)V
    .locals 2

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, " \n\r\t"

    invoke-static {v0, v1}, Lantlr/StringUtils;->stripBack(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0, v1}, Lantlr/StringUtils;->stripFront(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Lantlr/Token;->setText(Ljava/lang/String;)V

    :cond_0
    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->beginExceptionSpec(Lantlr/Token;)V

    new-instance v0, Lantlr/ExceptionSpec;

    invoke-direct {v0, p1}, Lantlr/ExceptionSpec;-><init>(Lantlr/Token;)V

    iput-object v0, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    return-void
.end method

.method public beginSubRule(Lantlr/Token;Lantlr/Token;Z)V
    .locals 3

    invoke-super {p0, p1, p2, p3}, Lantlr/DefineGrammarSymbols;->beginSubRule(Lantlr/Token;Lantlr/Token;Z)V

    iget-object v0, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    new-instance v1, Lantlr/BlockContext;

    invoke-direct {v1}, Lantlr/BlockContext;-><init>()V

    invoke-interface {v0, v1}, Lantlr/collections/Stack;->push(Ljava/lang/Object;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    new-instance v1, Lantlr/AlternativeBlock;

    iget-object v2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v1, v2, p2, p3}, Lantlr/AlternativeBlock;-><init>(Lantlr/Grammar;Lantlr/Token;Z)V

    iput-object v1, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p2

    const/4 p3, 0x0

    iput p3, p2, Lantlr/BlockContext;->altNum:I

    iget p2, p0, Lantlr/MakeGrammar;->nested:I

    add-int/lit8 p2, p2, 0x1

    iput p2, p0, Lantlr/MakeGrammar;->nested:I

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p2

    new-instance p3, Lantlr/BlockEndElement;

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {p3, v0}, Lantlr/BlockEndElement;-><init>(Lantlr/Grammar;)V

    iput-object p3, p2, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p2

    iget-object p2, p2, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p3

    iget-object p3, p3, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iput-object p3, p2, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p2

    iget-object p2, p2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-direct {p0, p2, p1}, Lantlr/MakeGrammar;->labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V

    return-void
.end method

.method public beginTree(Lantlr/Token;)V
    .locals 3

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_0

    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->beginTree(Lantlr/Token;)V

    iget-object v0, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    new-instance v1, Lantlr/TreeBlockContext;

    invoke-direct {v1}, Lantlr/TreeBlockContext;-><init>()V

    invoke-interface {v0, v1}, Lantlr/collections/Stack;->push(Ljava/lang/Object;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    new-instance v1, Lantlr/TreeElement;

    iget-object v2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v1, v2, p1}, Lantlr/TreeElement;-><init>(Lantlr/Grammar;Lantlr/Token;)V

    iput-object v1, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    const/4 p1, 0x0

    iput p1, p0, Lantlr/BlockContext;->altNum:I

    return-void

    :cond_0
    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v2, "Trees only allowed in TreeParser"

    invoke-virtual {p0, v2, v0, v1, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    new-instance p0, Lantlr/SemanticException;

    invoke-direct {p0, v2}, Lantlr/SemanticException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public context()Lantlr/BlockContext;
    .locals 1

    iget-object v0, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    invoke-interface {v0}, Lantlr/collections/Stack;->height()I

    move-result v0

    if-nez v0, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    iget-object p0, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    invoke-interface {p0}, Lantlr/collections/Stack;->top()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/BlockContext;

    return-object p0
.end method

.method public defineRuleName(Lantlr/Token;Ljava/lang/String;ZLjava/lang/String;)V
    .locals 6

    iget v0, p1, Lantlr/Token;->type:I

    const/16 v1, 0x18

    if-ne v0, v1, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-nez v0, :cond_1

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v2, "Lexical rule "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, " defined outside of lexer"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v4

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v5

    invoke-virtual {v0, v2, v3, v4, v5}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/String;->toLowerCase()Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_0
    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_1

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v2, "Lexical rule names must be upper case, \'"

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "\' is not"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v3}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v4

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v5

    invoke-virtual {v0, v2, v3, v4, v5}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/String;->toUpperCase()Ljava/lang/String;

    move-result-object v0

    :goto_0
    invoke-virtual {p1, v0}, Lantlr/Token;->setText(Ljava/lang/String;)V

    :cond_1
    invoke-super {p0, p1, p2, p3, p4}, Lantlr/DefineGrammarSymbols;->defineRuleName(Lantlr/Token;Ljava/lang/String;ZLjava/lang/String;)V

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    iget p4, p1, Lantlr/Token;->type:I

    if-ne p4, v1, :cond_2

    invoke-static {p2}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p2

    :cond_2
    iget-object p4, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p4, p2}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object p2

    check-cast p2, Lantlr/RuleSymbol;

    new-instance p4, Lantlr/RuleBlock;

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p1

    invoke-direct {p4, v0, v1, p1, p3}, Lantlr/RuleBlock;-><init>(Lantlr/Grammar;Ljava/lang/String;IZ)V

    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p1}, Lantlr/Grammar;->getDefaultErrorHandler()Z

    move-result p1

    invoke-virtual {p4, p1}, Lantlr/RuleBlock;->setDefaultErrorHandler(Z)V

    iput-object p4, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    iget-object p1, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    new-instance p3, Lantlr/BlockContext;

    invoke-direct {p3}, Lantlr/BlockContext;-><init>()V

    invoke-interface {p1, p3}, Lantlr/collections/Stack;->push(Ljava/lang/Object;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p1

    iput-object p4, p1, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p2, p4}, Lantlr/RuleSymbol;->setBlock(Lantlr/RuleBlock;)V

    new-instance p1, Lantlr/RuleEndElement;

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {p1, p2}, Lantlr/RuleEndElement;-><init>(Lantlr/Grammar;)V

    iput-object p1, p0, Lantlr/MakeGrammar;->ruleEnd:Lantlr/RuleEndElement;

    iget-object p1, p0, Lantlr/MakeGrammar;->ruleEnd:Lantlr/RuleEndElement;

    invoke-virtual {p4, p1}, Lantlr/RuleBlock;->setEndElement(Lantlr/RuleEndElement;)V

    const/4 p1, 0x0

    iput p1, p0, Lantlr/MakeGrammar;->nested:I

    return-void
.end method

.method public endAlt()V
    .locals 1

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->endAlt()V

    iget v0, p0, Lantlr/MakeGrammar;->nested:I

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/MakeGrammar;->ruleEnd:Lantlr/RuleEndElement;

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    :goto_0
    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget v0, p0, Lantlr/BlockContext;->altNum:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/BlockContext;->altNum:I

    return-void
.end method

.method public endChildList()V
    .locals 2

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->endChildList()V

    new-instance v0, Lantlr/BlockEndElement;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1}, Lantlr/BlockEndElement;-><init>(Lantlr/Grammar;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v1

    iget-object v1, v1, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iput-object v1, v0, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    return-void
.end method

.method public endExceptionGroup()V
    .locals 0

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->endExceptionGroup()V

    return-void
.end method

.method public endExceptionSpec()V
    .locals 5

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->endExceptionSpec()V

    iget-object v0, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v1, "exception processing internal error -- no active exception spec"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    instance-of v0, v0, Lantlr/RuleBlock;

    if-eqz v0, :cond_1

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    check-cast v0, Lantlr/RuleBlock;

    iget-object v1, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    invoke-virtual {v0, v1}, Lantlr/RuleBlock;->addExceptionSpec(Lantlr/ExceptionSpec;)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    invoke-virtual {v0}, Lantlr/BlockContext;->currentAlt()Lantlr/Alternative;

    move-result-object v0

    iget-object v0, v0, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    if-eqz v0, :cond_2

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iget-object v2, v2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v2}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v3

    iget-object v3, v3, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v3}, Lantlr/GrammarElement;->getColumn()I

    move-result v3

    const-string v4, "Alternative already has an exception specification"

    invoke-virtual {v0, v4, v1, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    invoke-virtual {v0}, Lantlr/BlockContext;->currentAlt()Lantlr/Alternative;

    move-result-object v0

    iget-object v1, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    iput-object v1, v0, Lantlr/Alternative;->exceptionSpec:Lantlr/ExceptionSpec;

    :goto_0
    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    return-void
.end method

.method public endGrammar()V
    .locals 1

    iget-boolean v0, p0, Lantlr/MakeGrammar;->grammarError:Z

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/MakeGrammar;->abortGrammar()V

    goto :goto_0

    :cond_0
    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->endGrammar()V

    :goto_0
    return-void
.end method

.method public endRule(Ljava/lang/String;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->endRule(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    invoke-interface {p1}, Lantlr/collections/Stack;->pop()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lantlr/BlockContext;

    iget-object p0, p0, Lantlr/MakeGrammar;->ruleEnd:Lantlr/RuleEndElement;

    iget-object p1, p1, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iput-object p1, p0, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    iget-object p0, p0, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0}, Lantlr/AlternativeBlock;->prepareForAnalysis()V

    return-void
.end method

.method public endSubRule()V
    .locals 8

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->endSubRule()V

    iget v0, p0, Lantlr/MakeGrammar;->nested:I

    const/4 v1, 0x1

    sub-int/2addr v0, v1

    iput v0, p0, Lantlr/MakeGrammar;->nested:I

    iget-object v0, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    invoke-interface {v0}, Lantlr/collections/Stack;->pop()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/BlockContext;

    iget-object v2, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iget-boolean v3, v2, Lantlr/AlternativeBlock;->not:Z

    if-eqz v3, :cond_0

    instance-of v3, v2, Lantlr/SynPredBlock;

    if-nez v3, :cond_0

    instance-of v3, v2, Lantlr/ZeroOrMoreBlock;

    if-nez v3, :cond_0

    instance-of v3, v2, Lantlr/OneOrMoreBlock;

    if-nez v3, :cond_0

    iget-object v3, p0, Lantlr/DefineGrammarSymbols;->analyzer:Lantlr/LLkAnalyzer;

    iget-object v4, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v4, v4, Lantlr/LexerGrammar;

    invoke-virtual {v3, v2, v4}, Lantlr/LLkAnalyzer;->subruleCanBeInverted(Lantlr/AlternativeBlock;Z)Z

    move-result v3

    if-nez v3, :cond_0

    const-string v3, "line.separator"

    invoke-static {v3}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    iget-object v4, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "This subrule cannot be inverted.  Only subrules of the form:"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "    (T1|T2|T3...) or"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "    (\'c1\'|\'c2\'|\'c3\'...)"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, "may be inverted (ranges are also allowed)."

    invoke-static {v5, v3, v6}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    iget-object v5, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v5}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2}, Lantlr/GrammarElement;->getLine()I

    move-result v6

    invoke-virtual {v2}, Lantlr/GrammarElement;->getColumn()I

    move-result v7

    invoke-virtual {v4, v3, v5, v6, v7}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    instance-of v3, v2, Lantlr/SynPredBlock;

    if-eqz v3, :cond_1

    check-cast v2, Lantlr/SynPredBlock;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v3

    iget-object v3, v3, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iput-boolean v1, v3, Lantlr/AlternativeBlock;->hasASynPred:Z

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v3

    invoke-virtual {v3}, Lantlr/BlockContext;->currentAlt()Lantlr/Alternative;

    move-result-object v3

    iput-object v2, v3, Lantlr/Alternative;->synPred:Lantlr/SynPredBlock;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iput-boolean v1, p0, Lantlr/Grammar;->hasSyntacticPredicate:Z

    invoke-virtual {v2, p0}, Lantlr/AlternativeBlock;->removeTrackingOfRuleRefs(Lantlr/Grammar;)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v2}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    :goto_0
    iget-object p0, v0, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    iget-object p0, p0, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0}, Lantlr/AlternativeBlock;->prepareForAnalysis()V

    return-void
.end method

.method public endTree()V
    .locals 1

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->endTree()V

    iget-object v0, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    invoke-interface {v0}, Lantlr/collections/Stack;->pop()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/BlockContext;

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    return-void
.end method

.method public hasError()V
    .locals 1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/MakeGrammar;->grammarError:Z

    return-void
.end method

.method public noAutoGenSubRule()V
    .locals 1

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lantlr/AlternativeBlock;->setAutoGen(Z)V

    return-void
.end method

.method public oneOrMoreSubRule()V
    .locals 5

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iget-boolean v0, v0, Lantlr/AlternativeBlock;->not:Z

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iget-object v2, v2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v2}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v3

    iget-object v3, v3, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v3}, Lantlr/GrammarElement;->getColumn()I

    move-result v3

    const-string v4, "\'~\' cannot be applied to (...)* subrule"

    invoke-virtual {v0, v4, v1, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    new-instance v0, Lantlr/OneOrMoreBlock;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1}, Lantlr/OneOrMoreBlock;-><init>(Lantlr/Grammar;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v1

    iget-object v1, v1, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-static {v0, v1}, Lantlr/MakeGrammar;->setBlock(Lantlr/AlternativeBlock;Lantlr/AlternativeBlock;)V

    iget-object v1, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    invoke-interface {v1}, Lantlr/collections/Stack;->pop()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/BlockContext;

    iget-object v2, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    new-instance v3, Lantlr/BlockContext;

    invoke-direct {v3}, Lantlr/BlockContext;-><init>()V

    invoke-interface {v2, v3}, Lantlr/collections/Stack;->push(Ljava/lang/Object;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iput-object v0, v2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iget-object v1, v1, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    iput-object v1, v2, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    iput-object v0, p0, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    return-void
.end method

.method public optionalSubRule()V
    .locals 5

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iget-boolean v0, v0, Lantlr/AlternativeBlock;->not:Z

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iget-object v2, v2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v2}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v3

    iget-object v3, v3, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v3}, Lantlr/GrammarElement;->getColumn()I

    move-result v3

    const-string v4, "\'~\' cannot be applied to (...)? subrule"

    invoke-virtual {v0, v4, v1, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->beginAlt(Z)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->endAlt()V

    return-void
.end method

.method public refAction(Lantlr/Token;)V
    .locals 2

    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->refAction(Lantlr/Token;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    const/4 v1, 0x1

    iput-boolean v1, v0, Lantlr/AlternativeBlock;->hasAnAction:Z

    new-instance v0, Lantlr/ActionElement;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1, p1}, Lantlr/ActionElement;-><init>(Lantlr/Grammar;Lantlr/Token;)V

    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    return-void
.end method

.method public refArgAction(Lantlr/Token;)V
    .locals 0

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    check-cast p0, Lantlr/RuleBlock;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    return-void
.end method

.method public refCharLiteral(Lantlr/Token;Lantlr/Token;ZIZ)V
    .locals 4

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-nez v1, :cond_0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p3

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string p4, "Character literal only valid in lexer"

    invoke-virtual {p0, p4, p2, p3, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_0
    invoke-super/range {p0 .. p5}, Lantlr/DefineGrammarSymbols;->refCharLiteral(Lantlr/Token;Lantlr/Token;ZIZ)V

    new-instance v0, Lantlr/CharLiteralElement;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    check-cast v1, Lantlr/LexerGrammar;

    invoke-direct {v0, v1, p1, p3, p4}, Lantlr/CharLiteralElement;-><init>(Lantlr/LexerGrammar;Lantlr/Token;ZI)V

    iget-object p3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    check-cast p3, Lantlr/LexerGrammar;

    iget-boolean p3, p3, Lantlr/LexerGrammar;->caseSensitive:Z

    if-nez p3, :cond_1

    invoke-virtual {v0}, Lantlr/GrammarAtom;->getType()I

    move-result p3

    const/16 p4, 0x80

    if-ge p3, p4, :cond_1

    invoke-virtual {v0}, Lantlr/GrammarAtom;->getType()I

    move-result p3

    int-to-char p3, p3

    invoke-static {p3}, Ljava/lang/Character;->toLowerCase(C)C

    move-result p3

    invoke-virtual {v0}, Lantlr/GrammarAtom;->getType()I

    move-result p4

    int-to-char p4, p4

    if-eq p3, p4, :cond_1

    iget-object p3, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object p4, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p4}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p4

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v2

    const-string v3, "Character literal must be lowercase when caseSensitive=false"

    invoke-virtual {p3, v3, p4, v1, v2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    invoke-direct {p0, v0, p2}, Lantlr/MakeGrammar;->labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V

    iget-object p2, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    invoke-virtual {p2}, Lantlr/RuleBlock;->getIgnoreRule()Ljava/lang/String;

    move-result-object p2

    if-nez p5, :cond_2

    if-eqz p2, :cond_2

    invoke-direct {p0, p2, p1}, Lantlr/MakeGrammar;->createOptionalRuleRef(Ljava/lang/String;Lantlr/Token;)Lantlr/AlternativeBlock;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    :cond_2
    return-void
.end method

.method public refCharRange(Lantlr/Token;Lantlr/Token;Lantlr/Token;IZ)V
    .locals 7

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-nez v1, :cond_0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p3

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string p4, "Character range only valid in lexer"

    invoke-virtual {p0, p4, p2, p3, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_0
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lantlr/ANTLRLexer;->tokenTypeForCharLiteral(Ljava/lang/String;)I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Lantlr/ANTLRLexer;->tokenTypeForCharLiteral(Ljava/lang/String;)I

    move-result v1

    if-ge v1, v0, :cond_1

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p3

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string p4, "Malformed range."

    invoke-virtual {p2, p4, p0, p3, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_1
    iget-object v2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    check-cast v2, Lantlr/LexerGrammar;

    iget-boolean v2, v2, Lantlr/LexerGrammar;->caseSensitive:Z

    if-nez v2, :cond_3

    const-string v2, "Character literal must be lowercase when caseSensitive=false"

    const/16 v3, 0x80

    if-ge v0, v3, :cond_2

    int-to-char v0, v0

    invoke-static {v0}, Ljava/lang/Character;->toLowerCase(C)C

    move-result v4

    if-eq v4, v0, :cond_2

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v4, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v4}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v5

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v6

    invoke-virtual {v0, v2, v4, v5, v6}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_2
    if-ge v1, v3, :cond_3

    int-to-char v0, v1

    invoke-static {v0}, Ljava/lang/Character;->toLowerCase(C)C

    move-result v1

    if-eq v1, v0, :cond_3

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v3

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result v4

    invoke-virtual {v0, v2, v1, v3, v4}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_3
    invoke-super/range {p0 .. p5}, Lantlr/DefineGrammarSymbols;->refCharRange(Lantlr/Token;Lantlr/Token;Lantlr/Token;IZ)V

    new-instance v0, Lantlr/CharRangeElement;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    check-cast v1, Lantlr/LexerGrammar;

    invoke-direct {v0, v1, p1, p2, p4}, Lantlr/CharRangeElement;-><init>(Lantlr/LexerGrammar;Lantlr/Token;Lantlr/Token;I)V

    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    invoke-direct {p0, v0, p3}, Lantlr/MakeGrammar;->labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V

    iget-object p2, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    invoke-virtual {p2}, Lantlr/RuleBlock;->getIgnoreRule()Ljava/lang/String;

    move-result-object p2

    if-nez p5, :cond_4

    if-eqz p2, :cond_4

    invoke-direct {p0, p2, p1}, Lantlr/MakeGrammar;->createOptionalRuleRef(Ljava/lang/String;Lantlr/Token;)Lantlr/AlternativeBlock;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    :cond_4
    return-void
.end method

.method public refElementOption(Lantlr/Token;Lantlr/Token;)V
    .locals 2

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    invoke-virtual {v0}, Lantlr/BlockContext;->currentElement()Lantlr/AlternativeElement;

    move-result-object v0

    instance-of v1, v0, Lantlr/StringLiteralElement;

    if-nez v1, :cond_1

    instance-of v1, v0, Lantlr/TokenRefElement;

    if-nez v1, :cond_1

    instance-of v1, v0, Lantlr/WildcardElement;

    if-eqz v1, :cond_0

    goto :goto_0

    :cond_0
    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v0, "cannot use element option ("

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, ") for this kind of element"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    invoke-virtual {p2, v0, p0, v1, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_1

    :cond_1
    :goto_0
    check-cast v0, Lantlr/GrammarAtom;

    invoke-virtual {v0, p1, p2}, Lantlr/GrammarAtom;->setOption(Lantlr/Token;Lantlr/Token;)V

    :goto_1
    return-void
.end method

.method public refExceptionHandler(Lantlr/Token;Lantlr/Token;)V
    .locals 2

    invoke-super {p0, p1, p2}, Lantlr/DefineGrammarSymbols;->refExceptionHandler(Lantlr/Token;Lantlr/Token;)V

    iget-object v0, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    if-nez v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v1, "exception handler processing internal error"

    invoke-virtual {v0, v1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    iget-object p0, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    new-instance v0, Lantlr/ExceptionHandler;

    invoke-direct {v0, p1, p2}, Lantlr/ExceptionHandler;-><init>(Lantlr/Token;Lantlr/Token;)V

    invoke-virtual {p0, v0}, Lantlr/ExceptionSpec;->addHandler(Lantlr/ExceptionHandler;)V

    return-void
.end method

.method public refInitAction(Lantlr/Token;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->refAction(Lantlr/Token;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/AlternativeBlock;->setInitAction(Ljava/lang/String;)V

    return-void
.end method

.method public refMemberAction(Lantlr/Token;)V
    .locals 0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iput-object p1, p0, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    return-void
.end method

.method public refPreambleAction(Lantlr/Token;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->refPreambleAction(Lantlr/Token;)V

    return-void
.end method

.method public refReturnAction(Lantlr/Token;)V
    .locals 3

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v0, v0, Lantlr/LexerGrammar;

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    check-cast v0, Lantlr/RuleBlock;

    invoke-virtual {v0}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1, v0}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object v0

    check-cast v0, Lantlr/RuleSymbol;

    iget-object v0, v0, Lantlr/RuleSymbol;->access:Ljava/lang/String;

    const-string v1, "public"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v2, "public Lexical rules cannot specify return type"

    invoke-virtual {v0, v2, p0, v1, p1}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_0
    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    check-cast p0, Lantlr/RuleBlock;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    return-void
.end method

.method public refRule(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;I)V
    .locals 6

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    const/16 v2, 0x18

    if-eqz v1, :cond_1

    iget v1, p2, Lantlr/Token;->type:I

    if-eq v1, v2, :cond_0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string p1, "Parser rule "

    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, " referenced in lexer"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->error(Ljava/lang/String;)V

    return-void

    :cond_0
    const/4 v1, 0x2

    if-ne p5, v1, :cond_1

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v3

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result v4

    const-string v5, "AST specification ^ not allowed in lexer"

    invoke-virtual {v1, v5, v0, v3, v4}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_1
    invoke-super/range {p0 .. p5}, Lantlr/DefineGrammarSymbols;->refRule(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;I)V

    new-instance v0, Lantlr/RuleRefElement;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1, p2, p5}, Lantlr/RuleRefElement;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    iput-object v0, p0, Lantlr/MakeGrammar;->lastRuleRef:Lantlr/RuleRefElement;

    if-eqz p4, :cond_2

    iget-object p5, p0, Lantlr/MakeGrammar;->lastRuleRef:Lantlr/RuleRefElement;

    invoke-virtual {p4}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p4

    invoke-virtual {p5, p4}, Lantlr/RuleRefElement;->setArgs(Ljava/lang/String;)V

    :cond_2
    if-eqz p1, :cond_3

    iget-object p4, p0, Lantlr/MakeGrammar;->lastRuleRef:Lantlr/RuleRefElement;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p4, p1}, Lantlr/RuleRefElement;->setIdAssign(Ljava/lang/String;)V

    :cond_3
    iget-object p1, p0, Lantlr/MakeGrammar;->lastRuleRef:Lantlr/RuleRefElement;

    invoke-virtual {p0, p1}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iget p2, p2, Lantlr/Token;->type:I

    if-ne p2, v2, :cond_4

    invoke-static {p1}, Lantlr/CodeGenerator;->encodeLexerRuleName(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    :cond_4
    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p2, p1}, Lantlr/Grammar;->getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;

    move-result-object p1

    check-cast p1, Lantlr/RuleSymbol;

    iget-object p2, p0, Lantlr/MakeGrammar;->lastRuleRef:Lantlr/RuleRefElement;

    invoke-virtual {p1, p2}, Lantlr/RuleSymbol;->addReference(Lantlr/RuleRefElement;)V

    iget-object p1, p0, Lantlr/MakeGrammar;->lastRuleRef:Lantlr/RuleRefElement;

    invoke-direct {p0, p1, p3}, Lantlr/MakeGrammar;->labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V

    return-void
.end method

.method public refSemPred(Lantlr/Token;)V
    .locals 2

    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->refSemPred(Lantlr/Token;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    invoke-virtual {v0}, Lantlr/BlockContext;->currentAlt()Lantlr/Alternative;

    move-result-object v0

    invoke-virtual {v0}, Lantlr/Alternative;->atStart()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    invoke-virtual {p0}, Lantlr/BlockContext;->currentAlt()Lantlr/Alternative;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/Alternative;->semPred:Ljava/lang/String;

    goto :goto_0

    :cond_0
    new-instance v0, Lantlr/ActionElement;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1, p1}, Lantlr/ActionElement;-><init>(Lantlr/Grammar;Lantlr/Token;)V

    const/4 p1, 0x1

    iput-boolean p1, v0, Lantlr/ActionElement;->isSemPred:Z

    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    :goto_0
    return-void
.end method

.method public refStringLiteral(Lantlr/Token;Lantlr/Token;IZ)V
    .locals 5

    invoke-super {p0, p1, p2, p3, p4}, Lantlr/DefineGrammarSymbols;->refStringLiteral(Lantlr/Token;Lantlr/Token;IZ)V

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/TreeWalkerGrammar;

    if-eqz v1, :cond_0

    const/4 v1, 0x2

    if-ne p3, v1, :cond_0

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v3

    const-string v4, "^ not allowed in here for tree-walker"

    invoke-virtual {v1, v4, v0, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    new-instance v0, Lantlr/StringLiteralElement;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1, p1, p3}, Lantlr/StringLiteralElement;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    iget-object p3, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, p3, Lantlr/LexerGrammar;

    if-eqz v1, :cond_2

    check-cast p3, Lantlr/LexerGrammar;

    iget-boolean p3, p3, Lantlr/LexerGrammar;->caseSensitive:Z

    if-nez p3, :cond_2

    const/4 p3, 0x1

    move v1, p3

    :goto_0
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v2

    sub-int/2addr v2, p3

    if-ge v1, v2, :cond_2

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2, v1}, Ljava/lang/String;->charAt(I)C

    move-result v2

    const/16 v3, 0x80

    if-ge v2, v3, :cond_1

    invoke-static {v2}, Ljava/lang/Character;->toLowerCase(C)C

    move-result v3

    if-eq v3, v2, :cond_1

    iget-object p3, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v3

    const-string v4, "Characters of string literal must be lowercase when caseSensitive=false"

    invoke-virtual {p3, v4, v1, v2, v3}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    goto :goto_1

    :cond_1
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    :goto_1
    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    invoke-direct {p0, v0, p2}, Lantlr/MakeGrammar;->labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V

    iget-object p2, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    invoke-virtual {p2}, Lantlr/RuleBlock;->getIgnoreRule()Ljava/lang/String;

    move-result-object p2

    if-nez p4, :cond_3

    if-eqz p2, :cond_3

    invoke-direct {p0, p2, p1}, Lantlr/MakeGrammar;->createOptionalRuleRef(Ljava/lang/String;Lantlr/Token;)Lantlr/AlternativeBlock;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    :cond_3
    return-void
.end method

.method public refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V
    .locals 6

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-eqz v1, :cond_2

    const/4 v1, 0x2

    if-ne p6, v1, :cond_0

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result v3

    const-string v4, "AST specification ^ not allowed in lexer"

    invoke-virtual {v1, v4, v0, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    if-eqz p5, :cond_1

    iget-object p5, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result v2

    const-string v3, "~TOKEN is not allowed in lexer"

    invoke-virtual {p5, v3, v0, v1, v2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_1
    move-object v0, p0

    move-object v1, p1

    move-object v2, p2

    move-object v3, p3

    move-object v4, p4

    move v5, p6

    invoke-virtual/range {v0 .. v5}, Lantlr/MakeGrammar;->refRule(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;I)V

    iget-object p1, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    invoke-virtual {p1}, Lantlr/RuleBlock;->getIgnoreRule()Ljava/lang/String;

    move-result-object p1

    if-nez p7, :cond_5

    if-eqz p1, :cond_5

    invoke-direct {p0, p1, p2}, Lantlr/MakeGrammar;->createOptionalRuleRef(Ljava/lang/String;Lantlr/Token;)Lantlr/AlternativeBlock;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    goto :goto_0

    :cond_2
    if-eqz p1, :cond_3

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result v3

    const-string v4, "Assignment from token reference only allowed in lexer"

    invoke-virtual {v1, v4, v0, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_3
    if-eqz p4, :cond_4

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p4}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {p4}, Lantlr/Token;->getColumn()I

    move-result v3

    const-string v4, "Token reference arguments only allowed in lexer"

    invoke-virtual {v0, v4, v1, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_4
    invoke-super/range {p0 .. p7}, Lantlr/DefineGrammarSymbols;->refToken(Lantlr/Token;Lantlr/Token;Lantlr/Token;Lantlr/Token;ZIZ)V

    new-instance p1, Lantlr/TokenRefElement;

    iget-object p4, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {p1, p4, p2, p5, p6}, Lantlr/TokenRefElement;-><init>(Lantlr/Grammar;Lantlr/Token;ZI)V

    invoke-virtual {p0, p1}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    invoke-direct {p0, p1, p3}, Lantlr/MakeGrammar;->labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V

    :cond_5
    :goto_0
    return-void
.end method

.method public refTokenRange(Lantlr/Token;Lantlr/Token;Lantlr/Token;IZ)V
    .locals 2

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-eqz v1, :cond_0

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p3

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string p4, "Token range not allowed in lexer"

    invoke-virtual {p0, p4, p2, p3, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_0
    invoke-super/range {p0 .. p5}, Lantlr/DefineGrammarSymbols;->refTokenRange(Lantlr/Token;Lantlr/Token;Lantlr/Token;IZ)V

    new-instance p5, Lantlr/TokenRangeElement;

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {p5, v0, p1, p2, p4}, Lantlr/TokenRangeElement;-><init>(Lantlr/Grammar;Lantlr/Token;Lantlr/Token;I)V

    iget p2, p5, Lantlr/TokenRangeElement;->end:I

    iget p4, p5, Lantlr/TokenRangeElement;->begin:I

    if-ge p2, p4, :cond_1

    iget-object p2, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result p3

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string p4, "Malformed range."

    invoke-virtual {p2, p4, p0, p3, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void

    :cond_1
    invoke-virtual {p0, p5}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    invoke-direct {p0, p5, p3}, Lantlr/MakeGrammar;->labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V

    return-void
.end method

.method public refTokensSpecElementOption(Lantlr/Token;Lantlr/Token;Lantlr/Token;)V
    .locals 3

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-interface {v0, v1}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object v0

    if-nez v0, :cond_0

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    const-string v2, "cannot find "

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "in tokens {...}"

    invoke-virtual {v2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v1, p1}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_0
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    const-string v1, "AST"

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_1

    invoke-virtual {p3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Lantlr/TokenSymbol;->setASTNodeType(Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    iget-object p1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string p3, "invalid tokens {...} element option:"

    invoke-static {p3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p3

    invoke-static {p2, p3}, La/a/a/a/a;->a(Lantlr/Token;Ljava/lang/StringBuilder;)Ljava/lang/String;

    move-result-object p3

    iget-object p0, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    invoke-virtual {p1, p3, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_0
    return-void
.end method

.method public refTreeSpecifier(Lantlr/Token;)V
    .locals 0

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    invoke-virtual {p0}, Lantlr/BlockContext;->currentAlt()Lantlr/Alternative;

    move-result-object p0

    iput-object p1, p0, Lantlr/Alternative;->treeSpecifier:Lantlr/Token;

    return-void
.end method

.method public refWildcard(Lantlr/Token;Lantlr/Token;I)V
    .locals 2

    invoke-super {p0, p1, p2, p3}, Lantlr/DefineGrammarSymbols;->refWildcard(Lantlr/Token;Lantlr/Token;I)V

    new-instance v0, Lantlr/WildcardElement;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1, p1, p3}, Lantlr/WildcardElement;-><init>(Lantlr/Grammar;Lantlr/Token;I)V

    invoke-virtual {p0, v0}, Lantlr/MakeGrammar;->addElementToCurrentAlt(Lantlr/AlternativeElement;)V

    invoke-direct {p0, v0, p2}, Lantlr/MakeGrammar;->labelElement(Lantlr/AlternativeElement;Lantlr/Token;)V

    return-void
.end method

.method public reset()V
    .locals 2

    invoke-super {p0}, Lantlr/DefineGrammarSymbols;->reset()V

    new-instance v0, Lantlr/collections/impl/LList;

    invoke-direct {v0}, Lantlr/collections/impl/LList;-><init>()V

    iput-object v0, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/MakeGrammar;->lastRuleRef:Lantlr/RuleRefElement;

    iput-object v0, p0, Lantlr/MakeGrammar;->ruleEnd:Lantlr/RuleEndElement;

    iput-object v0, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    const/4 v1, 0x0

    iput v1, p0, Lantlr/MakeGrammar;->nested:I

    iput-object v0, p0, Lantlr/MakeGrammar;->currentExceptionSpec:Lantlr/ExceptionSpec;

    iput-boolean v1, p0, Lantlr/MakeGrammar;->grammarError:Z

    return-void
.end method

.method public setArgOfRuleRef(Lantlr/Token;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/DefineGrammarSymbols;->setArgOfRuleRef(Lantlr/Token;)V

    iget-object p0, p0, Lantlr/MakeGrammar;->lastRuleRef:Lantlr/RuleRefElement;

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/RuleRefElement;->setArgs(Ljava/lang/String;)V

    return-void
.end method

.method public setRuleOption(Lantlr/Token;Lantlr/Token;)V
    .locals 0

    iget-object p0, p0, Lantlr/MakeGrammar;->ruleBlock:Lantlr/RuleBlock;

    invoke-virtual {p0, p1, p2}, Lantlr/RuleBlock;->setOption(Lantlr/Token;Lantlr/Token;)V

    return-void
.end method

.method public setSubruleOption(Lantlr/Token;Lantlr/Token;)V
    .locals 0

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0, p1, p2}, Lantlr/AlternativeBlock;->setOption(Lantlr/Token;Lantlr/Token;)V

    return-void
.end method

.method public setUserExceptions(Ljava/lang/String;)V
    .locals 0

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    check-cast p0, Lantlr/RuleBlock;

    iput-object p1, p0, Lantlr/RuleBlock;->throwsSpec:Ljava/lang/String;

    return-void
.end method

.method public synPred()V
    .locals 5

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iget-boolean v0, v0, Lantlr/AlternativeBlock;->not:Z

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iget-object v2, v2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v2}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v3

    iget-object v3, v3, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v3}, Lantlr/GrammarElement;->getColumn()I

    move-result v3

    const-string v4, "\'~\' cannot be applied to syntactic predicate"

    invoke-virtual {v0, v4, v1, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    new-instance v0, Lantlr/SynPredBlock;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1}, Lantlr/SynPredBlock;-><init>(Lantlr/Grammar;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v1

    iget-object v1, v1, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-static {v0, v1}, Lantlr/MakeGrammar;->setBlock(Lantlr/AlternativeBlock;Lantlr/AlternativeBlock;)V

    iget-object v1, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    invoke-interface {v1}, Lantlr/collections/Stack;->pop()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/BlockContext;

    iget-object v2, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    new-instance v3, Lantlr/BlockContext;

    invoke-direct {v3}, Lantlr/BlockContext;-><init>()V

    invoke-interface {v2, v3}, Lantlr/collections/Stack;->push(Ljava/lang/Object;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iput-object v0, v2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iget-object v1, v1, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    iput-object v1, v2, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    iput-object v0, p0, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    return-void
.end method

.method public zeroOrMoreSubRule()V
    .locals 5

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v0

    iget-object v0, v0, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    iget-boolean v0, v0, Lantlr/AlternativeBlock;->not:Z

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/DefineGrammarSymbols;->tool:Lantlr/Tool;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-virtual {v1}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iget-object v2, v2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v2}, Lantlr/GrammarElement;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v3

    iget-object v3, v3, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {v3}, Lantlr/GrammarElement;->getColumn()I

    move-result v3

    const-string v4, "\'~\' cannot be applied to (...)+ subrule"

    invoke-virtual {v0, v4, v1, v2, v3}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    new-instance v0, Lantlr/ZeroOrMoreBlock;

    iget-object v1, p0, Lantlr/DefineGrammarSymbols;->grammar:Lantlr/Grammar;

    invoke-direct {v0, v1}, Lantlr/ZeroOrMoreBlock;-><init>(Lantlr/Grammar;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v1

    iget-object v1, v1, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-static {v0, v1}, Lantlr/MakeGrammar;->setBlock(Lantlr/AlternativeBlock;Lantlr/AlternativeBlock;)V

    iget-object v1, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    invoke-interface {v1}, Lantlr/collections/Stack;->pop()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/BlockContext;

    iget-object v2, p0, Lantlr/MakeGrammar;->blocks:Lantlr/collections/Stack;

    new-instance v3, Lantlr/BlockContext;

    invoke-direct {v3}, Lantlr/BlockContext;-><init>()V

    invoke-interface {v2, v3}, Lantlr/collections/Stack;->push(Ljava/lang/Object;)V

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iput-object v0, v2, Lantlr/BlockContext;->block:Lantlr/AlternativeBlock;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object v2

    iget-object v1, v1, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    iput-object v1, v2, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    invoke-virtual {p0}, Lantlr/MakeGrammar;->context()Lantlr/BlockContext;

    move-result-object p0

    iget-object p0, p0, Lantlr/BlockContext;->blockEnd:Lantlr/BlockEndElement;

    iput-object v0, p0, Lantlr/BlockEndElement;->block:Lantlr/AlternativeBlock;

    return-void
.end method
