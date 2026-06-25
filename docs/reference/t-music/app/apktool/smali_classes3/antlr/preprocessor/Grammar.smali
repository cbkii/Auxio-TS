.class public Lantlr/preprocessor/Grammar;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public alreadyExpanded:Z

.field public antlrTool:Lantlr/Tool;

.field public exportVocab:Ljava/lang/String;

.field public fileName:Ljava/lang/String;

.field public hier:Lantlr/preprocessor/Hierarchy;

.field public importVocab:Ljava/lang/String;

.field public memberAction:Ljava/lang/String;

.field public name:Ljava/lang/String;

.field public options:Lantlr/collections/impl/IndexedVector;

.field public preambleAction:Ljava/lang/String;

.field public predefined:Z

.field public rules:Lantlr/collections/impl/IndexedVector;

.field public specifiedVocabulary:Z

.field public superClass:Ljava/lang/String;

.field public superGrammar:Ljava/lang/String;

.field public tokenSection:Ljava/lang/String;

.field public type:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lantlr/Tool;Ljava/lang/String;Ljava/lang/String;Lantlr/collections/impl/IndexedVector;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/preprocessor/Grammar;->predefined:Z

    iput-boolean v0, p0, Lantlr/preprocessor/Grammar;->alreadyExpanded:Z

    iput-boolean v0, p0, Lantlr/preprocessor/Grammar;->specifiedVocabulary:Z

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/preprocessor/Grammar;->superClass:Ljava/lang/String;

    iput-object v0, p0, Lantlr/preprocessor/Grammar;->importVocab:Ljava/lang/String;

    iput-object v0, p0, Lantlr/preprocessor/Grammar;->exportVocab:Ljava/lang/String;

    iput-object p2, p0, Lantlr/preprocessor/Grammar;->name:Ljava/lang/String;

    iput-object p3, p0, Lantlr/preprocessor/Grammar;->superGrammar:Ljava/lang/String;

    iput-object p4, p0, Lantlr/preprocessor/Grammar;->rules:Lantlr/collections/impl/IndexedVector;

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->antlrTool:Lantlr/Tool;

    return-void
.end method


# virtual methods
.method public addOption(Lantlr/preprocessor/Option;)V
    .locals 1

    iget-object v0, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    if-nez v0, :cond_0

    new-instance v0, Lantlr/collections/impl/IndexedVector;

    invoke-direct {v0}, Lantlr/collections/impl/IndexedVector;-><init>()V

    iput-object v0, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    :cond_0
    iget-object p0, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {p1}, Lantlr/preprocessor/Option;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0, p1}, Lantlr/collections/impl/IndexedVector;->appendElement(Ljava/lang/Object;Ljava/lang/Object;)V

    return-void
.end method

.method public addRule(Lantlr/preprocessor/Rule;)V
    .locals 1

    iget-object p0, p0, Lantlr/preprocessor/Grammar;->rules:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {p1}, Lantlr/preprocessor/Rule;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0, p1}, Lantlr/collections/impl/IndexedVector;->appendElement(Ljava/lang/Object;Ljava/lang/Object;)V

    return-void
.end method

.method public expandInPlace()V
    .locals 6

    iget-boolean v0, p0, Lantlr/preprocessor/Grammar;->alreadyExpanded:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    invoke-virtual {p0}, Lantlr/preprocessor/Grammar;->getSuperGrammar()Lantlr/preprocessor/Grammar;

    move-result-object v0

    if-nez v0, :cond_1

    return-void

    :cond_1
    iget-object v1, p0, Lantlr/preprocessor/Grammar;->exportVocab:Ljava/lang/String;

    if-nez v1, :cond_2

    invoke-virtual {p0}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v1

    iput-object v1, p0, Lantlr/preprocessor/Grammar;->exportVocab:Ljava/lang/String;

    :cond_2
    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->isPredefined()Z

    move-result v1

    if-eqz v1, :cond_3

    return-void

    :cond_3
    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->expandInPlace()V

    const/4 v1, 0x1

    iput-boolean v1, p0, Lantlr/preprocessor/Grammar;->alreadyExpanded:Z

    iget-object v2, p0, Lantlr/preprocessor/Grammar;->hier:Lantlr/preprocessor/Hierarchy;

    invoke-virtual {p0}, Lantlr/preprocessor/Grammar;->getFileName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Lantlr/preprocessor/Hierarchy;->getFile(Ljava/lang/String;)Lantlr/preprocessor/GrammarFile;

    move-result-object v2

    invoke-virtual {v2, v1}, Lantlr/preprocessor/GrammarFile;->setExpanded(Z)V

    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->getRules()Lantlr/collections/impl/IndexedVector;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/collections/impl/IndexedVector;->elements()Ljava/util/Enumeration;

    move-result-object v1

    :goto_0
    invoke-interface {v1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_4

    invoke-interface {v1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/preprocessor/Rule;

    invoke-virtual {p0, v2, v0}, Lantlr/preprocessor/Grammar;->inherit(Lantlr/preprocessor/Rule;Lantlr/preprocessor/Grammar;)V

    goto :goto_0

    :cond_4
    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->getOptions()Lantlr/collections/impl/IndexedVector;

    move-result-object v1

    if-eqz v1, :cond_5

    invoke-virtual {v1}, Lantlr/collections/impl/IndexedVector;->elements()Ljava/util/Enumeration;

    move-result-object v1

    :goto_1
    invoke-interface {v1}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_5

    invoke-interface {v1}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/preprocessor/Option;

    invoke-virtual {p0, v2, v0}, Lantlr/preprocessor/Grammar;->inherit(Lantlr/preprocessor/Option;Lantlr/preprocessor/Grammar;)V

    goto :goto_1

    :cond_5
    iget-object v1, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    const-string v2, "importVocab"

    if-eqz v1, :cond_6

    invoke-virtual {v1, v2}, Lantlr/collections/impl/IndexedVector;->getElement(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    if-eqz v1, :cond_7

    :cond_6
    iget-object v1, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    if-nez v1, :cond_9

    :cond_7
    new-instance v1, Lantlr/preprocessor/Option;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v4, v0, Lantlr/preprocessor/Grammar;->exportVocab:Ljava/lang/String;

    const-string v5, ";"

    invoke-static {v3, v4, v5}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-direct {v1, v2, v3, p0}, Lantlr/preprocessor/Option;-><init>(Ljava/lang/String;Ljava/lang/String;Lantlr/preprocessor/Grammar;)V

    invoke-virtual {p0, v1}, Lantlr/preprocessor/Grammar;->addOption(Lantlr/preprocessor/Option;)V

    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->getFileName()Ljava/lang/String;

    move-result-object v1

    iget-object v2, p0, Lantlr/preprocessor/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {v2, v1}, Lantlr/Tool;->pathToFile(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget-object v3, v0, Lantlr/preprocessor/Grammar;->exportVocab:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CodeGenerator;->TokenTypesFileSuffix:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget-object v3, Lantlr/CodeGenerator;->TokenTypesFileExt:Ljava/lang/String;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/preprocessor/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {v3, v2}, Lantlr/Tool;->fileMinusPath(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    const-string v4, "."

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    const-string v5, "file.separator"

    invoke-static {v5}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v1, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_8

    goto :goto_2

    :cond_8
    :try_start_0
    iget-object v1, p0, Lantlr/preprocessor/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1, v2, v3}, Lantlr/Tool;->copyFile(Ljava/lang/String;Ljava/lang/String;)V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_2

    :catch_0
    iget-object p0, p0, Lantlr/preprocessor/Grammar;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "cannot find/copy importVocab file "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/Tool;->toolError(Ljava/lang/String;)V

    return-void

    :cond_9
    :goto_2
    iget-object v1, v0, Lantlr/preprocessor/Grammar;->memberAction:Ljava/lang/String;

    invoke-virtual {p0, v1, v0}, Lantlr/preprocessor/Grammar;->inherit(Ljava/lang/String;Lantlr/preprocessor/Grammar;)V

    return-void
.end method

.method public getFileName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Grammar;->fileName:Ljava/lang/String;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Grammar;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getOptions()Lantlr/collections/impl/IndexedVector;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    return-object p0
.end method

.method public getRules()Lantlr/collections/impl/IndexedVector;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Grammar;->rules:Lantlr/collections/impl/IndexedVector;

    return-object p0
.end method

.method public getSuperGrammar()Lantlr/preprocessor/Grammar;
    .locals 1

    iget-object v0, p0, Lantlr/preprocessor/Grammar;->superGrammar:Ljava/lang/String;

    if-nez v0, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    iget-object p0, p0, Lantlr/preprocessor/Grammar;->hier:Lantlr/preprocessor/Hierarchy;

    invoke-virtual {p0, v0}, Lantlr/preprocessor/Hierarchy;->getGrammar(Ljava/lang/String;)Lantlr/preprocessor/Grammar;

    move-result-object p0

    return-object p0
.end method

.method public getSuperGrammarName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Grammar;->superGrammar:Ljava/lang/String;

    return-object p0
.end method

.method public getType()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Grammar;->type:Ljava/lang/String;

    return-object p0
.end method

.method public inherit(Lantlr/preprocessor/Option;Lantlr/preprocessor/Grammar;)V
    .locals 1

    invoke-virtual {p1}, Lantlr/preprocessor/Option;->getName()Ljava/lang/String;

    move-result-object p2

    const-string v0, "importVocab"

    invoke-virtual {p2, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-nez p2, :cond_2

    invoke-virtual {p1}, Lantlr/preprocessor/Option;->getName()Ljava/lang/String;

    move-result-object p2

    const-string v0, "exportVocab"

    invoke-virtual {p2, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p2

    if-eqz p2, :cond_0

    goto :goto_0

    :cond_0
    const/4 p2, 0x0

    iget-object v0, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    if-eqz v0, :cond_1

    invoke-virtual {p1}, Lantlr/preprocessor/Option;->getName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v0, p2}, Lantlr/collections/impl/IndexedVector;->getElement(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Lantlr/preprocessor/Option;

    :cond_1
    if-nez p2, :cond_2

    invoke-virtual {p0, p1}, Lantlr/preprocessor/Grammar;->addOption(Lantlr/preprocessor/Option;)V

    :cond_2
    :goto_0
    return-void
.end method

.method public inherit(Lantlr/preprocessor/Rule;Lantlr/preprocessor/Grammar;)V
    .locals 3

    iget-object v0, p0, Lantlr/preprocessor/Grammar;->rules:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {p1}, Lantlr/preprocessor/Rule;->getName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/collections/impl/IndexedVector;->getElement(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/preprocessor/Rule;

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Lantlr/preprocessor/Rule;->sameSignature(Lantlr/preprocessor/Rule;)Z

    move-result p1

    if-nez p1, :cond_1

    iget-object p1, p0, Lantlr/preprocessor/Grammar;->antlrTool:Lantlr/Tool;

    const-string v1, "rule "

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "."

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Lantlr/preprocessor/Rule;->getName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " has different signature than "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Lantlr/preprocessor/Rule;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/preprocessor/Grammar;->addRule(Lantlr/preprocessor/Rule;)V

    :cond_1
    :goto_0
    return-void
.end method

.method public inherit(Ljava/lang/String;Lantlr/preprocessor/Grammar;)V
    .locals 0

    iget-object p2, p0, Lantlr/preprocessor/Grammar;->memberAction:Ljava/lang/String;

    if-eqz p2, :cond_0

    return-void

    :cond_0
    if-eqz p1, :cond_1

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->memberAction:Ljava/lang/String;

    :cond_1
    return-void
.end method

.method public isPredefined()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/preprocessor/Grammar;->predefined:Z

    return p0
.end method

.method public setFileName(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->fileName:Ljava/lang/String;

    return-void
.end method

.method public setHierarchy(Lantlr/preprocessor/Hierarchy;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->hier:Lantlr/preprocessor/Hierarchy;

    return-void
.end method

.method public setMemberAction(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->memberAction:Ljava/lang/String;

    return-void
.end method

.method public setOptions(Lantlr/collections/impl/IndexedVector;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    return-void
.end method

.method public setPreambleAction(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->preambleAction:Ljava/lang/String;

    return-void
.end method

.method public setPredefined(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/preprocessor/Grammar;->predefined:Z

    return-void
.end method

.method public setTokenSection(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->tokenSection:Ljava/lang/String;

    return-void
.end method

.method public setType(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Grammar;->type:Ljava/lang/String;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 6

    new-instance v0, Ljava/lang/StringBuffer;

    const/16 v1, 0x2710

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(I)V

    iget-object v1, p0, Lantlr/preprocessor/Grammar;->preambleAction:Ljava/lang/String;

    if-eqz v1, :cond_0

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_0
    iget-object v1, p0, Lantlr/preprocessor/Grammar;->superGrammar:Ljava/lang/String;

    const-string v2, ";"

    const-string v3, "class "

    if-nez v1, :cond_1

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/preprocessor/Grammar;->name:Ljava/lang/String;

    invoke-static {v0, p0, v2}, La/a/a/a/a;->a(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_1
    iget-object v1, p0, Lantlr/preprocessor/Grammar;->superClass:Ljava/lang/String;

    const-string v4, " extends "

    if-eqz v1, :cond_2

    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, p0, Lantlr/preprocessor/Grammar;->name:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/preprocessor/Grammar;->superClass:Ljava/lang/String;

    goto :goto_0

    :cond_2
    invoke-static {v3}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v3, p0, Lantlr/preprocessor/Grammar;->name:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v3, p0, Lantlr/preprocessor/Grammar;->type:Ljava/lang/String;

    :goto_0
    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "line.separator"

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget-object v1, p0, Lantlr/preprocessor/Grammar;->options:Lantlr/collections/impl/IndexedVector;

    if-eqz v1, :cond_3

    invoke-static {v1}, Lantlr/preprocessor/Hierarchy;->optionsToString(Lantlr/collections/impl/IndexedVector;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_3
    iget-object v1, p0, Lantlr/preprocessor/Grammar;->tokenSection:Ljava/lang/String;

    if-eqz v1, :cond_4

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/preprocessor/Grammar;->tokenSection:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "\n"

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_4
    iget-object v1, p0, Lantlr/preprocessor/Grammar;->memberAction:Ljava/lang/String;

    if-eqz v1, :cond_5

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/preprocessor/Grammar;->memberAction:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_5
    const/4 v1, 0x0

    :goto_1
    iget-object v3, p0, Lantlr/preprocessor/Grammar;->rules:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {v3}, Lantlr/collections/impl/IndexedVector;->size()I

    move-result v3

    if-ge v1, v3, :cond_7

    iget-object v3, p0, Lantlr/preprocessor/Grammar;->rules:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {v3, v1}, Lantlr/collections/impl/IndexedVector;->elementAt(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lantlr/preprocessor/Rule;

    invoke-virtual {p0}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v4

    iget-object v5, v3, Lantlr/preprocessor/Rule;->enclosingGrammar:Lantlr/preprocessor/Grammar;

    invoke-virtual {v5}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-nez v4, :cond_6

    const-string v4, "// inherited from grammar "

    invoke-static {v4}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-object v5, v3, Lantlr/preprocessor/Rule;->enclosingGrammar:Lantlr/preprocessor/Grammar;

    invoke-virtual {v5}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :cond_6
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v2}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/lit8 v1, v1, 0x1

    goto :goto_1

    :cond_7
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
