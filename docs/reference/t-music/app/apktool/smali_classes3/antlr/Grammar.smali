.class public abstract Lantlr/Grammar;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public analyzerDebug:Z

.field public antlrTool:Lantlr/Tool;

.field public buildAST:Z

.field public classMemberAction:Lantlr/Token;

.field public className:Ljava/lang/String;

.field public comment:Ljava/lang/String;

.field public debuggingOutput:Z

.field public defaultErrorHandler:Z

.field public exportVocab:Ljava/lang/String;

.field public fileName:Ljava/lang/String;

.field public generator:Lantlr/CodeGenerator;

.field public hasSyntacticPredicate:Z

.field public hasUserErrorHandling:Z

.field public importVocab:Ljava/lang/String;

.field public interactive:Z

.field public maxk:I

.field public options:Ljava/util/Hashtable;

.field public preambleAction:Lantlr/Token;

.field public rules:Lantlr/collections/impl/Vector;

.field public superClass:Ljava/lang/String;

.field public symbols:Ljava/util/Hashtable;

.field public theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

.field public tokenManager:Lantlr/TokenManager;

.field public traceRules:Z


# direct methods
.method public constructor <init>(Ljava/lang/String;Lantlr/Tool;Ljava/lang/String;)V
    .locals 4

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/Grammar;->buildAST:Z

    iput-boolean v0, p0, Lantlr/Grammar;->analyzerDebug:Z

    iput-boolean v0, p0, Lantlr/Grammar;->interactive:Z

    const/4 v1, 0x0

    iput-object v1, p0, Lantlr/Grammar;->superClass:Ljava/lang/String;

    iput-object v1, p0, Lantlr/Grammar;->exportVocab:Ljava/lang/String;

    iput-object v1, p0, Lantlr/Grammar;->importVocab:Ljava/lang/String;

    new-instance v2, Lantlr/CommonToken;

    const-string v3, ""

    invoke-direct {v2, v0, v3}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    iput-object v2, p0, Lantlr/Grammar;->preambleAction:Lantlr/Token;

    iput-object v1, p0, Lantlr/Grammar;->className:Ljava/lang/String;

    iput-object v1, p0, Lantlr/Grammar;->fileName:Ljava/lang/String;

    new-instance v2, Lantlr/CommonToken;

    invoke-direct {v2, v0, v3}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    iput-object v2, p0, Lantlr/Grammar;->classMemberAction:Lantlr/Token;

    iput-boolean v0, p0, Lantlr/Grammar;->hasSyntacticPredicate:Z

    iput-boolean v0, p0, Lantlr/Grammar;->hasUserErrorHandling:Z

    const/4 v2, 0x1

    iput v2, p0, Lantlr/Grammar;->maxk:I

    iput-boolean v0, p0, Lantlr/Grammar;->traceRules:Z

    iput-boolean v0, p0, Lantlr/Grammar;->debuggingOutput:Z

    iput-boolean v2, p0, Lantlr/Grammar;->defaultErrorHandler:Z

    iput-object v1, p0, Lantlr/Grammar;->comment:Ljava/lang/String;

    iput-object p1, p0, Lantlr/Grammar;->className:Ljava/lang/String;

    iput-object p2, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance p1, Ljava/util/Hashtable;

    invoke-direct {p1}, Ljava/util/Hashtable;-><init>()V

    iput-object p1, p0, Lantlr/Grammar;->symbols:Ljava/util/Hashtable;

    new-instance p1, Ljava/util/Hashtable;

    invoke-direct {p1}, Ljava/util/Hashtable;-><init>()V

    iput-object p1, p0, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    new-instance p1, Lantlr/collections/impl/Vector;

    const/16 p2, 0x64

    invoke-direct {p1, p2}, Lantlr/collections/impl/Vector;-><init>(I)V

    iput-object p1, p0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    iput-object p3, p0, Lantlr/Grammar;->superClass:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public define(Lantlr/RuleSymbol;)V
    .locals 1

    iget-object v0, p0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {v0, p1}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    iget-object p0, p0, Lantlr/Grammar;->symbols:Ljava/util/Hashtable;

    invoke-virtual {p1}, Lantlr/GrammarSymbol;->getId()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public abstract generate()V
.end method

.method public getClassName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Grammar;->className:Ljava/lang/String;

    return-object p0
.end method

.method public getDefaultErrorHandler()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/Grammar;->defaultErrorHandler:Z

    return p0
.end method

.method public getFilename()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Grammar;->fileName:Ljava/lang/String;

    return-object p0
.end method

.method public getIntegerOption(Ljava/lang/String;)I
    .locals 1

    iget-object p0, p0, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/Token;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lantlr/Token;->getType()I

    move-result p1

    const/16 v0, 0x14

    if-ne p1, v0, :cond_0

    invoke-virtual {p0}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p0

    return p0

    :cond_0
    new-instance p0, Ljava/lang/NumberFormatException;

    invoke-direct {p0}, Ljava/lang/NumberFormatException;-><init>()V

    throw p0
.end method

.method public getOption(Ljava/lang/String;)Lantlr/Token;
    .locals 0

    iget-object p0, p0, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/Token;

    return-object p0
.end method

.method public abstract getSuperClass()Ljava/lang/String;
.end method

.method public getSymbol(Ljava/lang/String;)Lantlr/GrammarSymbol;
    .locals 0

    iget-object p0, p0, Lantlr/Grammar;->symbols:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/GrammarSymbol;

    return-object p0
.end method

.method public getSymbols()Ljava/util/Enumeration;
    .locals 0

    iget-object p0, p0, Lantlr/Grammar;->symbols:Ljava/util/Hashtable;

    invoke-virtual {p0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object p0

    return-object p0
.end method

.method public hasOption(Ljava/lang/String;)Z
    .locals 0

    iget-object p0, p0, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public isDefined(Ljava/lang/String;)Z
    .locals 0

    iget-object p0, p0, Lantlr/Grammar;->symbols:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->containsKey(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public abstract processArguments([Ljava/lang/String;)V
.end method

.method public setCodeGenerator(Lantlr/CodeGenerator;)V
    .locals 0

    iput-object p1, p0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    return-void
.end method

.method public setFilename(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/Grammar;->fileName:Ljava/lang/String;

    return-void
.end method

.method public setGrammarAnalyzer(Lantlr/LLkGrammarAnalyzer;)V
    .locals 0

    iput-object p1, p0, Lantlr/Grammar;->theLLkAnalyzer:Lantlr/LLkGrammarAnalyzer;

    return-void
.end method

.method public setOption(Ljava/lang/String;Lantlr/Token;)Z
    .locals 6

    const-string v0, ")"

    iget-object v1, p0, Lantlr/Grammar;->options:Ljava/util/Hashtable;

    invoke-virtual {v1, p1, p2}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    const-string v2, "k"

    invoke-virtual {p1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    const/4 v4, 0x1

    if-eqz v3, :cond_1

    :try_start_0
    invoke-virtual {p0, v2}, Lantlr/Grammar;->getIntegerOption(Ljava/lang/String;)I

    move-result p1

    iput p1, p0, Lantlr/Grammar;->maxk:I

    iget p1, p0, Lantlr/Grammar;->maxk:I

    if-gtz p1, :cond_0

    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "option \'k\' must be greater than 0 (was "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v3

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result v5

    invoke-virtual {p1, v1, v2, v3, v5}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    iput v4, p0, Lantlr/Grammar;->maxk:I
    :try_end_0
    .catch Ljava/lang/NumberFormatException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v1, "option \'k\' must be an integer (was "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    invoke-virtual {p1, v0, p0, v1, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :cond_0
    :goto_0
    return v4

    :cond_1
    const-string v0, "codeGenMakeSwitchThreshold"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_2

    :try_start_1
    invoke-virtual {p0, v0}, Lantlr/Grammar;->getIntegerOption(Ljava/lang/String;)I
    :try_end_1
    .catch Ljava/lang/NumberFormatException; {:try_start_1 .. :try_end_1} :catch_1

    goto :goto_1

    :catch_1
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "option \'codeGenMakeSwitchThreshold\' must be an integer"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_1
    return v4

    :cond_2
    const-string v0, "codeGenBitsetTestThreshold"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_3

    :try_start_2
    invoke-virtual {p0, v0}, Lantlr/Grammar;->getIntegerOption(Ljava/lang/String;)I
    :try_end_2
    .catch Ljava/lang/NumberFormatException; {:try_start_2 .. :try_end_2} :catch_2

    goto :goto_2

    :catch_2
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "option \'codeGenBitsetTestThreshold\' must be an integer"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_2
    return v4

    :cond_3
    const-string v0, "defaultErrorHandler"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const-string v2, "false"

    const-string v3, "true"

    const/4 v5, 0x0

    if-eqz v0, :cond_6

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_4

    iput-boolean v4, p0, Lantlr/Grammar;->defaultErrorHandler:Z

    goto :goto_3

    :cond_4
    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_5

    iput-boolean v5, p0, Lantlr/Grammar;->defaultErrorHandler:Z

    goto :goto_3

    :cond_5
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "Value for defaultErrorHandler must be true or false"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_3
    return v4

    :cond_6
    const-string v0, "analyzerDebug"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_9

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_7

    iput-boolean v4, p0, Lantlr/Grammar;->analyzerDebug:Z

    goto :goto_4

    :cond_7
    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_8

    iput-boolean v5, p0, Lantlr/Grammar;->analyzerDebug:Z

    goto :goto_4

    :cond_8
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "option \'analyzerDebug\' must be true or false"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_4
    return v4

    :cond_9
    const-string v0, "codeGenDebug"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_c

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_a

    iput-boolean v4, p0, Lantlr/Grammar;->analyzerDebug:Z

    goto :goto_5

    :cond_a
    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_b

    iput-boolean v5, p0, Lantlr/Grammar;->analyzerDebug:Z

    goto :goto_5

    :cond_b
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "option \'codeGenDebug\' must be true or false"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_5
    return v4

    :cond_c
    const-string p0, "classHeaderSuffix"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_d

    return v4

    :cond_d
    const-string p0, "classHeaderPrefix"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_e

    return v4

    :cond_e
    const-string p0, "namespaceAntlr"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_f

    return v4

    :cond_f
    const-string p0, "namespaceStd"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_10

    return v4

    :cond_10
    const-string p0, "genHashLines"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_11

    return v4

    :cond_11
    const-string p0, "noConstructors"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_12

    return v4

    :cond_12
    return v5
.end method

.method public setTokenManager(Lantlr/TokenManager;)V
    .locals 0

    iput-object p1, p0, Lantlr/Grammar;->tokenManager:Lantlr/TokenManager;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 4

    new-instance v0, Ljava/lang/StringBuffer;

    const/16 v1, 0x4e20

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(I)V

    iget-object p0, p0, Lantlr/Grammar;->rules:Lantlr/collections/impl/Vector;

    invoke-virtual {p0}, Lantlr/collections/impl/Vector;->elements()Ljava/util/Enumeration;

    move-result-object p0

    :cond_0
    :goto_0
    invoke-interface {p0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-interface {p0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/RuleSymbol;

    iget-object v2, v1, Lantlr/GrammarSymbol;->id:Ljava/lang/String;

    const-string v3, "mnextToken"

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_0

    invoke-virtual {v1}, Lantlr/RuleSymbol;->getBlock()Lantlr/RuleBlock;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/RuleBlock;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string v1, "\n\n"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_0

    :cond_1
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
