.class public Lantlr/RuleBlock;
.super Lantlr/AlternativeBlock;
.source ""


# instance fields
.field public argAction:Ljava/lang/String;

.field public cache:[Lantlr/Lookahead;

.field public defaultErrorHandler:Z

.field public endNode:Lantlr/RuleEndElement;

.field public exceptionSpecs:Ljava/util/Hashtable;

.field public ignoreRule:Ljava/lang/String;

.field public labeledElements:Lantlr/collections/impl/Vector;

.field public lock:[Z

.field public returnAction:Ljava/lang/String;

.field public ruleName:Ljava/lang/String;

.field public testLiterals:Z

.field public throwsSpec:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lantlr/Grammar;Ljava/lang/String;)V
    .locals 2

    invoke-direct {p0, p1}, Lantlr/AlternativeBlock;-><init>(Lantlr/Grammar;)V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/RuleBlock;->argAction:Ljava/lang/String;

    iput-object v0, p0, Lantlr/RuleBlock;->throwsSpec:Ljava/lang/String;

    iput-object v0, p0, Lantlr/RuleBlock;->returnAction:Ljava/lang/String;

    const/4 v1, 0x0

    iput-boolean v1, p0, Lantlr/RuleBlock;->testLiterals:Z

    const/4 v1, 0x1

    iput-boolean v1, p0, Lantlr/RuleBlock;->defaultErrorHandler:Z

    iput-object v0, p0, Lantlr/RuleBlock;->ignoreRule:Ljava/lang/String;

    iput-object p2, p0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    new-instance p2, Lantlr/collections/impl/Vector;

    invoke-direct {p2}, Lantlr/collections/impl/Vector;-><init>()V

    iput-object p2, p0, Lantlr/RuleBlock;->labeledElements:Lantlr/collections/impl/Vector;

    iget p2, p1, Lantlr/Grammar;->maxk:I

    add-int/2addr p2, v1

    new-array p2, p2, [Lantlr/Lookahead;

    iput-object p2, p0, Lantlr/RuleBlock;->cache:[Lantlr/Lookahead;

    new-instance p2, Ljava/util/Hashtable;

    invoke-direct {p2}, Ljava/util/Hashtable;-><init>()V

    iput-object p2, p0, Lantlr/RuleBlock;->exceptionSpecs:Ljava/util/Hashtable;

    instance-of p1, p1, Lantlr/ParserGrammar;

    invoke-virtual {p0, p1}, Lantlr/RuleBlock;->setAutoGen(Z)V

    return-void
.end method

.method public constructor <init>(Lantlr/Grammar;Ljava/lang/String;IZ)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/RuleBlock;-><init>(Lantlr/Grammar;Ljava/lang/String;)V

    iput p3, p0, Lantlr/GrammarElement;->line:I

    invoke-virtual {p0, p4}, Lantlr/RuleBlock;->setAutoGen(Z)V

    return-void
.end method


# virtual methods
.method public bridge synthetic addAlternative(Lantlr/Alternative;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/AlternativeBlock;->addAlternative(Lantlr/Alternative;)V

    return-void
.end method

.method public addExceptionSpec(Lantlr/ExceptionSpec;)V
    .locals 2

    iget-object v0, p1, Lantlr/ExceptionSpec;->label:Lantlr/Token;

    invoke-virtual {p0, v0}, Lantlr/RuleBlock;->findExceptionSpec(Lantlr/Token;)Lantlr/ExceptionSpec;

    move-result-object v0

    if-eqz v0, :cond_1

    iget-object v0, p1, Lantlr/ExceptionSpec;->label:Lantlr/Token;

    const-string v1, "Rule \'"

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object p0, p0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "\' already has an exception handler for label: "

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p1, Lantlr/ExceptionSpec;->label:Lantlr/Token;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_1

    :cond_0
    iget-object p1, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p1, p1, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "\' already has an exception handler"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Lantlr/Tool;->error(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    iget-object p0, p0, Lantlr/RuleBlock;->exceptionSpecs:Ljava/util/Hashtable;

    iget-object v0, p1, Lantlr/ExceptionSpec;->label:Lantlr/Token;

    if-nez v0, :cond_2

    const-string v0, ""

    goto :goto_0

    :cond_2
    invoke-virtual {v0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    :goto_0
    invoke-virtual {p0, v0, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :goto_1
    return-void
.end method

.method public findExceptionSpec(Lantlr/Token;)Lantlr/ExceptionSpec;
    .locals 0

    iget-object p0, p0, Lantlr/RuleBlock;->exceptionSpecs:Ljava/util/Hashtable;

    if-nez p1, :cond_0

    const-string p1, ""

    goto :goto_0

    :cond_0
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    :goto_0
    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/ExceptionSpec;

    return-object p0
.end method

.method public findExceptionSpec(Ljava/lang/String;)Lantlr/ExceptionSpec;
    .locals 0

    iget-object p0, p0, Lantlr/RuleBlock;->exceptionSpecs:Ljava/util/Hashtable;

    if-nez p1, :cond_0

    const-string p1, ""

    :cond_0
    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/ExceptionSpec;

    return-object p0
.end method

.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/AlternativeBlock;)V

    return-void
.end method

.method public bridge synthetic getAlternativeAt(I)Lantlr/Alternative;
    .locals 0

    invoke-super {p0, p1}, Lantlr/AlternativeBlock;->getAlternativeAt(I)Lantlr/Alternative;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic getAlternatives()Lantlr/collections/impl/Vector;
    .locals 0

    invoke-super {p0}, Lantlr/AlternativeBlock;->getAlternatives()Lantlr/collections/impl/Vector;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic getAutoGen()Z
    .locals 0

    invoke-super {p0}, Lantlr/AlternativeBlock;->getAutoGen()Z

    move-result p0

    return p0
.end method

.method public bridge synthetic getAutoGenType()I
    .locals 0

    invoke-super {p0}, Lantlr/AlternativeElement;->getAutoGenType()I

    move-result p0

    return p0
.end method

.method public bridge synthetic getColumn()I
    .locals 0

    invoke-super {p0}, Lantlr/GrammarElement;->getColumn()I

    move-result p0

    return p0
.end method

.method public getDefaultErrorHandler()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/RuleBlock;->defaultErrorHandler:Z

    return p0
.end method

.method public getEndElement()Lantlr/RuleEndElement;
    .locals 0

    iget-object p0, p0, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    return-object p0
.end method

.method public getIgnoreRule()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/RuleBlock;->ignoreRule:Ljava/lang/String;

    return-object p0
.end method

.method public bridge synthetic getInitAction()Ljava/lang/String;
    .locals 0

    invoke-super {p0}, Lantlr/AlternativeBlock;->getInitAction()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic getLabel()Ljava/lang/String;
    .locals 0

    invoke-super {p0}, Lantlr/AlternativeBlock;->getLabel()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic getLine()I
    .locals 0

    invoke-super {p0}, Lantlr/GrammarElement;->getLine()I

    move-result p0

    return p0
.end method

.method public getRuleName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    return-object p0
.end method

.method public getTestLiterals()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/RuleBlock;->testLiterals:Z

    return p0
.end method

.method public isLexerAutoGenRule()Z
    .locals 1

    iget-object p0, p0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    const-string v0, "nextToken"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public look(I)Lantlr/Lookahead;
    .locals 1

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    invoke-interface {v0, p1, p0}, Lantlr/LLkGrammarAnalyzer;->look(ILantlr/RuleBlock;)Lantlr/Lookahead;

    move-result-object p0

    return-object p0
.end method

.method public prepareForAnalysis()V
    .locals 1

    invoke-super {p0}, Lantlr/AlternativeBlock;->prepareForAnalysis()V

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget v0, v0, Lantlr/Grammar;->maxk:I

    add-int/lit8 v0, v0, 0x1

    new-array v0, v0, [Z

    iput-object v0, p0, Lantlr/RuleBlock;->lock:[Z

    return-void
.end method

.method public bridge synthetic removeTrackingOfRuleRefs(Lantlr/Grammar;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/AlternativeBlock;->removeTrackingOfRuleRefs(Lantlr/Grammar;)V

    return-void
.end method

.method public bridge synthetic setAlternatives(Lantlr/collections/impl/Vector;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/AlternativeBlock;->setAlternatives(Lantlr/collections/impl/Vector;)V

    return-void
.end method

.method public bridge synthetic setAutoGen(Z)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/AlternativeBlock;->setAutoGen(Z)V

    return-void
.end method

.method public bridge synthetic setAutoGenType(I)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/AlternativeElement;->setAutoGenType(I)V

    return-void
.end method

.method public setDefaultErrorHandler(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/RuleBlock;->defaultErrorHandler:Z

    return-void
.end method

.method public setEndElement(Lantlr/RuleEndElement;)V
    .locals 0

    iput-object p1, p0, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    return-void
.end method

.method public bridge synthetic setInitAction(Ljava/lang/String;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/AlternativeBlock;->setInitAction(Ljava/lang/String;)V

    return-void
.end method

.method public bridge synthetic setLabel(Ljava/lang/String;)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/AlternativeBlock;->setLabel(Ljava/lang/String;)V

    return-void
.end method

.method public setOption(Lantlr/Token;Lantlr/Token;)V
    .locals 6

    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "defaultErrorHandler"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/4 v1, 0x0

    const/4 v2, 0x1

    const-string v3, "false"

    const-string v4, "true"

    if-eqz v0, :cond_2

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    iput-boolean v2, p0, Lantlr/RuleBlock;->defaultErrorHandler:Z

    goto/16 :goto_2

    :cond_0
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_1

    iput-boolean v1, p0, Lantlr/RuleBlock;->defaultErrorHandler:Z

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

    const-string v1, "Value for defaultErrorHandler must be true or false"

    :goto_0
    invoke-virtual {p2, v1, p0, v0, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto/16 :goto_2

    :cond_2
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v5, "testLiterals"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_6

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    instance-of v5, v0, Lantlr/LexerGrammar;

    if-nez v5, :cond_3

    iget-object p0, v0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v1, "testLiterals option only valid for lexer rules"

    :goto_1
    invoke-virtual {p0, v1, p2, v0, p1}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    goto/16 :goto_2

    :cond_3
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_4

    iput-boolean v2, p0, Lantlr/RuleBlock;->testLiterals:Z

    goto/16 :goto_2

    :cond_4
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_5

    iput-boolean v1, p0, Lantlr/RuleBlock;->testLiterals:Z

    goto/16 :goto_2

    :cond_5
    iget-object p0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v1, "Value for testLiterals must be true or false"

    goto :goto_0

    :cond_6
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v5, "ignore"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_8

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-nez v1, :cond_7

    iget-object p0, v0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v1, "ignore option only valid for lexer rules"

    goto :goto_1

    :cond_7
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/RuleBlock;->ignoreRule:Ljava/lang/String;

    goto/16 :goto_2

    :cond_8
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v5, "paraphrase"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_b

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    instance-of v1, v0, Lantlr/LexerGrammar;

    if-nez v1, :cond_9

    iget-object p0, v0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {v0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v1, "paraphrase option only valid for lexer rules"

    goto/16 :goto_1

    :cond_9
    iget-object p1, v0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    iget-object v0, p0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    invoke-interface {p1, v0}, Lantlr/TokenManager;->getTokenSymbol(Ljava/lang/String;)Lantlr/TokenSymbol;

    move-result-object p1

    if-nez p1, :cond_a

    iget-object v0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v0, v0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v1, "cannot find token associated with rule "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object p0, p0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Lantlr/Tool;->panic(Ljava/lang/String;)V

    :cond_a
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Lantlr/TokenSymbol;->setParaphrase(Ljava/lang/String;)V

    goto :goto_2

    :cond_b
    invoke-virtual {p1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v5, "generateAmbigWarnings"

    invoke-virtual {v0, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_e

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_c

    iput-boolean v2, p0, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    goto :goto_2

    :cond_c
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_d

    iput-boolean v1, p0, Lantlr/AlternativeBlock;->generateAmbigWarnings:Z

    goto :goto_2

    :cond_d
    iget-object p0, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p2, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p1}, Lantlr/Token;->getColumn()I

    move-result p1

    const-string v1, "Value for generateAmbigWarnings must be true or false"

    goto/16 :goto_0

    :cond_e
    iget-object p2, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object p2, p2, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v0, "Invalid rule option: "

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
    .locals 8

    iget-object v0, p0, Lantlr/RuleBlock;->endNode:Lantlr/RuleEndElement;

    iget-object v0, v0, Lantlr/RuleEndElement;->cache:[Lantlr/Lookahead;

    iget-object v1, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget v1, v1, Lantlr/Grammar;->maxk:I

    const/4 v2, 0x1

    const-string v3, " FOLLOW={"

    move-object v4, v3

    move v3, v2

    :goto_0
    if-gt v2, v1, :cond_2

    aget-object v5, v0, v2

    if-nez v5, :cond_0

    goto :goto_1

    :cond_0
    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    aget-object v4, v0, v2

    iget-object v5, p0, Lantlr/GrammarElement;->grammar:Lantlr/Grammar;

    iget-object v5, v5, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    invoke-interface {v5}, Lantlr/TokenManager;->getVocabulary()Lantlr/collections/impl/Vector;

    move-result-object v5

    const-string v6, ","

    invoke-virtual {v4, v6, v5}, Lantlr/Lookahead;->toString(Ljava/lang/String;Lantlr/collections/impl/Vector;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    const/4 v4, 0x0

    if-ge v2, v1, :cond_1

    add-int/lit8 v5, v2, 0x1

    aget-object v5, v0, v5

    if-eqz v5, :cond_1

    const-string v5, ";"

    invoke-static {v3, v5}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    :cond_1
    move v7, v4

    move-object v4, v3

    move v3, v7

    :goto_1
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_2
    const-string v0, "}"

    invoke-static {v4, v0}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    if-eqz v3, :cond_3

    const-string v0, ""

    :cond_3
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p0, Lantlr/RuleBlock;->ruleName:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, ": "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-super {p0}, Lantlr/AlternativeBlock;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, " ;"

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
