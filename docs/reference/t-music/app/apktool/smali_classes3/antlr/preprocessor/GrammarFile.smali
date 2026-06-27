.class public Lantlr/preprocessor/GrammarFile;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public expanded:Z

.field public fileName:Ljava/lang/String;

.field public grammars:Lantlr/collections/impl/IndexedVector;

.field public headerAction:Ljava/lang/String;

.field public options:Lantlr/collections/impl/IndexedVector;

.field public tool:Lantlr/Tool;


# direct methods
.method public constructor <init>(Lantlr/Tool;Ljava/lang/String;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const-string v0, ""

    iput-object v0, p0, Lantlr/preprocessor/GrammarFile;->headerAction:Ljava/lang/String;

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/preprocessor/GrammarFile;->expanded:Z

    iput-object p2, p0, Lantlr/preprocessor/GrammarFile;->fileName:Ljava/lang/String;

    new-instance p2, Lantlr/collections/impl/IndexedVector;

    invoke-direct {p2}, Lantlr/collections/impl/IndexedVector;-><init>()V

    iput-object p2, p0, Lantlr/preprocessor/GrammarFile;->grammars:Lantlr/collections/impl/IndexedVector;

    iput-object p1, p0, Lantlr/preprocessor/GrammarFile;->tool:Lantlr/Tool;

    return-void
.end method


# virtual methods
.method public addGrammar(Lantlr/preprocessor/Grammar;)V
    .locals 1

    iget-object p0, p0, Lantlr/preprocessor/GrammarFile;->grammars:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {p1}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0, p1}, Lantlr/collections/impl/IndexedVector;->appendElement(Ljava/lang/Object;Ljava/lang/Object;)V

    return-void
.end method

.method public addHeaderAction(Ljava/lang/String;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v1, p0, Lantlr/preprocessor/GrammarFile;->headerAction:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "line.separator"

    invoke-static {p1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lantlr/preprocessor/GrammarFile;->headerAction:Ljava/lang/String;

    return-void
.end method

.method public generateExpandedFile()V
    .locals 2

    iget-boolean v0, p0, Lantlr/preprocessor/GrammarFile;->expanded:Z

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-virtual {p0}, Lantlr/preprocessor/GrammarFile;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/preprocessor/GrammarFile;->nameForExpandedGrammarFile(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lantlr/preprocessor/GrammarFile;->tool:Lantlr/Tool;

    invoke-virtual {v1, v0}, Lantlr/Tool;->openOutputFile(Ljava/lang/String;)Ljava/io/PrintWriter;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/preprocessor/GrammarFile;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/PrintWriter;->println(Ljava/lang/String;)V

    invoke-virtual {v0}, Ljava/io/PrintWriter;->close()V

    return-void
.end method

.method public getGrammars()Lantlr/collections/impl/IndexedVector;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/GrammarFile;->grammars:Lantlr/collections/impl/IndexedVector;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/GrammarFile;->fileName:Ljava/lang/String;

    return-object p0
.end method

.method public nameForExpandedGrammarFile(Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    iget-boolean v0, p0, Lantlr/preprocessor/GrammarFile;->expanded:Z

    if-eqz v0, :cond_0

    const-string v0, "expanded"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object p0, p0, Lantlr/preprocessor/GrammarFile;->tool:Lantlr/Tool;

    invoke-virtual {p0, p1}, Lantlr/Tool;->fileMinusPath(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    return-object p1
.end method

.method public setExpanded(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/preprocessor/GrammarFile;->expanded:Z

    return-void
.end method

.method public setOptions(Lantlr/collections/impl/IndexedVector;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/GrammarFile;->options:Lantlr/collections/impl/IndexedVector;

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 4

    iget-object v0, p0, Lantlr/preprocessor/GrammarFile;->headerAction:Ljava/lang/String;

    const-string v1, ""

    if-nez v0, :cond_0

    move-object v0, v1

    :cond_0
    iget-object v2, p0, Lantlr/preprocessor/GrammarFile;->options:Lantlr/collections/impl/IndexedVector;

    if-nez v2, :cond_1

    goto :goto_0

    :cond_1
    invoke-static {v2}, Lantlr/preprocessor/Hierarchy;->optionsToString(Lantlr/collections/impl/IndexedVector;)Ljava/lang/String;

    move-result-object v1

    :goto_0
    new-instance v2, Ljava/lang/StringBuffer;

    const/16 v3, 0x2710

    invoke-direct {v2, v3}, Ljava/lang/StringBuffer;-><init>(I)V

    invoke-virtual {v2, v0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    iget-object p0, p0, Lantlr/preprocessor/GrammarFile;->grammars:Lantlr/collections/impl/IndexedVector;

    invoke-virtual {p0}, Lantlr/collections/impl/IndexedVector;->elements()Ljava/util/Enumeration;

    move-result-object p0

    :goto_1
    invoke-interface {p0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-interface {p0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/preprocessor/Grammar;

    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v2, v0}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto :goto_1

    :cond_2
    invoke-virtual {v2}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
