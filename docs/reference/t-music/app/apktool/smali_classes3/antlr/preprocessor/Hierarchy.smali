.class public Lantlr/preprocessor/Hierarchy;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public LexerRoot:Lantlr/preprocessor/Grammar;

.field public ParserRoot:Lantlr/preprocessor/Grammar;

.field public TreeParserRoot:Lantlr/preprocessor/Grammar;

.field public antlrTool:Lantlr/Tool;

.field public files:Ljava/util/Hashtable;

.field public symbols:Ljava/util/Hashtable;


# direct methods
.method public constructor <init>(Lantlr/Tool;)V
    .locals 3

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/preprocessor/Hierarchy;->LexerRoot:Lantlr/preprocessor/Grammar;

    iput-object v0, p0, Lantlr/preprocessor/Hierarchy;->ParserRoot:Lantlr/preprocessor/Grammar;

    iput-object v0, p0, Lantlr/preprocessor/Hierarchy;->TreeParserRoot:Lantlr/preprocessor/Grammar;

    iput-object p1, p0, Lantlr/preprocessor/Hierarchy;->antlrTool:Lantlr/Tool;

    new-instance v1, Lantlr/preprocessor/Grammar;

    const-string v2, "Lexer"

    invoke-direct {v1, p1, v2, v0, v0}, Lantlr/preprocessor/Grammar;-><init>(Lantlr/Tool;Ljava/lang/String;Ljava/lang/String;Lantlr/collections/impl/IndexedVector;)V

    iput-object v1, p0, Lantlr/preprocessor/Hierarchy;->LexerRoot:Lantlr/preprocessor/Grammar;

    new-instance v1, Lantlr/preprocessor/Grammar;

    const-string v2, "Parser"

    invoke-direct {v1, p1, v2, v0, v0}, Lantlr/preprocessor/Grammar;-><init>(Lantlr/Tool;Ljava/lang/String;Ljava/lang/String;Lantlr/collections/impl/IndexedVector;)V

    iput-object v1, p0, Lantlr/preprocessor/Hierarchy;->ParserRoot:Lantlr/preprocessor/Grammar;

    new-instance v1, Lantlr/preprocessor/Grammar;

    const-string v2, "TreeParser"

    invoke-direct {v1, p1, v2, v0, v0}, Lantlr/preprocessor/Grammar;-><init>(Lantlr/Tool;Ljava/lang/String;Ljava/lang/String;Lantlr/collections/impl/IndexedVector;)V

    iput-object v1, p0, Lantlr/preprocessor/Hierarchy;->TreeParserRoot:Lantlr/preprocessor/Grammar;

    new-instance p1, Ljava/util/Hashtable;

    const/16 v0, 0xa

    invoke-direct {p1, v0}, Ljava/util/Hashtable;-><init>(I)V

    iput-object p1, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    new-instance p1, Ljava/util/Hashtable;

    invoke-direct {p1, v0}, Ljava/util/Hashtable;-><init>(I)V

    iput-object p1, p0, Lantlr/preprocessor/Hierarchy;->files:Ljava/util/Hashtable;

    iget-object p1, p0, Lantlr/preprocessor/Hierarchy;->LexerRoot:Lantlr/preprocessor/Grammar;

    const/4 v0, 0x1

    invoke-virtual {p1, v0}, Lantlr/preprocessor/Grammar;->setPredefined(Z)V

    iget-object p1, p0, Lantlr/preprocessor/Hierarchy;->ParserRoot:Lantlr/preprocessor/Grammar;

    invoke-virtual {p1, v0}, Lantlr/preprocessor/Grammar;->setPredefined(Z)V

    iget-object p1, p0, Lantlr/preprocessor/Hierarchy;->TreeParserRoot:Lantlr/preprocessor/Grammar;

    invoke-virtual {p1, v0}, Lantlr/preprocessor/Grammar;->setPredefined(Z)V

    iget-object p1, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    iget-object v0, p0, Lantlr/preprocessor/Hierarchy;->LexerRoot:Lantlr/preprocessor/Grammar;

    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lantlr/preprocessor/Hierarchy;->LexerRoot:Lantlr/preprocessor/Grammar;

    invoke-virtual {p1, v0, v1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p1, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    iget-object v0, p0, Lantlr/preprocessor/Hierarchy;->ParserRoot:Lantlr/preprocessor/Grammar;

    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lantlr/preprocessor/Hierarchy;->ParserRoot:Lantlr/preprocessor/Grammar;

    invoke-virtual {p1, v0, v1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object p1, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    iget-object v0, p0, Lantlr/preprocessor/Hierarchy;->TreeParserRoot:Lantlr/preprocessor/Grammar;

    invoke-virtual {v0}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/preprocessor/Hierarchy;->TreeParserRoot:Lantlr/preprocessor/Grammar;

    invoke-virtual {p1, v0, p0}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public static optionsToString(Lantlr/collections/impl/IndexedVector;)Ljava/lang/String;
    .locals 3

    const-string v0, "options {"

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v1, "line.separator"

    invoke-static {v1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/collections/impl/IndexedVector;->elements()Ljava/util/Enumeration;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v2

    if-eqz v2, :cond_0

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-interface {p0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lantlr/preprocessor/Option;

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-static {v1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_0
    const-string p0, "}"

    invoke-static {v0, p0}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p0

    invoke-static {v1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v1}, Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method


# virtual methods
.method public addGrammar(Lantlr/preprocessor/Grammar;)V
    .locals 2

    invoke-virtual {p1, p0}, Lantlr/preprocessor/Grammar;->setHierarchy(Lantlr/preprocessor/Hierarchy;)V

    iget-object v0, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    invoke-virtual {p1}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {p1}, Lantlr/preprocessor/Grammar;->getFileName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lantlr/preprocessor/Hierarchy;->getFile(Ljava/lang/String;)Lantlr/preprocessor/GrammarFile;

    move-result-object p0

    invoke-virtual {p0, p1}, Lantlr/preprocessor/GrammarFile;->addGrammar(Lantlr/preprocessor/Grammar;)V

    return-void
.end method

.method public addGrammarFile(Lantlr/preprocessor/GrammarFile;)V
    .locals 1

    iget-object p0, p0, Lantlr/preprocessor/Hierarchy;->files:Ljava/util/Hashtable;

    invoke-virtual {p1}, Lantlr/preprocessor/GrammarFile;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0, p1}, Ljava/util/Hashtable;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method public expandGrammarsInFile(Ljava/lang/String;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/preprocessor/Hierarchy;->getFile(Ljava/lang/String;)Lantlr/preprocessor/GrammarFile;

    move-result-object p0

    invoke-virtual {p0}, Lantlr/preprocessor/GrammarFile;->getGrammars()Lantlr/collections/impl/IndexedVector;

    move-result-object p0

    invoke-virtual {p0}, Lantlr/collections/impl/IndexedVector;->elements()Ljava/util/Enumeration;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result p1

    if-eqz p1, :cond_0

    invoke-interface {p0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lantlr/preprocessor/Grammar;

    invoke-virtual {p1}, Lantlr/preprocessor/Grammar;->expandInPlace()V

    goto :goto_0

    :cond_0
    return-void
.end method

.method public findRoot(Lantlr/preprocessor/Grammar;)Lantlr/preprocessor/Grammar;
    .locals 1

    invoke-virtual {p1}, Lantlr/preprocessor/Grammar;->getSuperGrammarName()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    return-object p1

    :cond_0
    invoke-virtual {p1}, Lantlr/preprocessor/Grammar;->getSuperGrammar()Lantlr/preprocessor/Grammar;

    move-result-object v0

    if-nez v0, :cond_1

    return-object p1

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/preprocessor/Hierarchy;->findRoot(Lantlr/preprocessor/Grammar;)Lantlr/preprocessor/Grammar;

    move-result-object p0

    return-object p0
.end method

.method public getFile(Ljava/lang/String;)Lantlr/preprocessor/GrammarFile;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Hierarchy;->files:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/preprocessor/GrammarFile;

    return-object p0
.end method

.method public getGrammar(Ljava/lang/String;)Lantlr/preprocessor/Grammar;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    invoke-virtual {p0, p1}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/preprocessor/Grammar;

    return-object p0
.end method

.method public getTool()Lantlr/Tool;
    .locals 0

    iget-object p0, p0, Lantlr/preprocessor/Hierarchy;->antlrTool:Lantlr/Tool;

    return-object p0
.end method

.method public readGrammarFile(Ljava/lang/String;)V
    .locals 3

    new-instance v0, Ljava/io/BufferedReader;

    new-instance v1, Ljava/io/FileReader;

    invoke-direct {v1, p1}, Ljava/io/FileReader;-><init>(Ljava/lang/String;)V

    invoke-direct {v0, v1}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V

    new-instance v1, Lantlr/preprocessor/GrammarFile;

    iget-object v2, p0, Lantlr/preprocessor/Hierarchy;->antlrTool:Lantlr/Tool;

    invoke-direct {v1, v2, p1}, Lantlr/preprocessor/GrammarFile;-><init>(Lantlr/Tool;Ljava/lang/String;)V

    invoke-virtual {p0, v1}, Lantlr/preprocessor/Hierarchy;->addGrammarFile(Lantlr/preprocessor/GrammarFile;)V

    new-instance v1, Lantlr/preprocessor/PreprocessorLexer;

    invoke-direct {v1, v0}, Lantlr/preprocessor/PreprocessorLexer;-><init>(Ljava/io/Reader;)V

    invoke-virtual {v1, p1}, Lantlr/CharScanner;->setFilename(Ljava/lang/String;)V

    new-instance v0, Lantlr/preprocessor/Preprocessor;

    invoke-direct {v0, v1}, Lantlr/preprocessor/Preprocessor;-><init>(Lantlr/TokenStream;)V

    iget-object v1, p0, Lantlr/preprocessor/Hierarchy;->antlrTool:Lantlr/Tool;

    invoke-virtual {v0, v1}, Lantlr/preprocessor/Preprocessor;->setTool(Lantlr/Tool;)V

    invoke-virtual {v0, p1}, Lantlr/Parser;->setFilename(Ljava/lang/String;)V

    :try_start_0
    invoke-virtual {v0, p0, p1}, Lantlr/preprocessor/Preprocessor;->grammarFile(Lantlr/preprocessor/Hierarchy;Ljava/lang/String;)V
    :try_end_0
    .catch Lantlr/TokenStreamException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Lantlr/ANTLRException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception p1

    iget-object p0, p0, Lantlr/preprocessor/Hierarchy;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "error reading grammar(s):\n"

    goto :goto_0

    :catch_1
    move-exception p1

    iget-object p0, p0, Lantlr/preprocessor/Hierarchy;->antlrTool:Lantlr/Tool;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Token stream error reading grammar(s):\n"

    :goto_0
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/Tool;->toolError(Ljava/lang/String;)V

    :goto_1
    return-void
.end method

.method public setTool(Lantlr/Tool;)V
    .locals 0

    iput-object p1, p0, Lantlr/preprocessor/Hierarchy;->antlrTool:Lantlr/Tool;

    return-void
.end method

.method public verifyThatHierarchyIsComplete()Z
    .locals 7

    iget-object v0, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v0

    const/4 v1, 0x0

    const/4 v2, 0x1

    move v3, v2

    :cond_0
    :goto_0
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v4

    if-eqz v4, :cond_2

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lantlr/preprocessor/Grammar;

    invoke-virtual {v4}, Lantlr/preprocessor/Grammar;->getSuperGrammarName()Ljava/lang/String;

    move-result-object v5

    if-nez v5, :cond_1

    goto :goto_0

    :cond_1
    invoke-virtual {v4}, Lantlr/preprocessor/Grammar;->getSuperGrammar()Lantlr/preprocessor/Grammar;

    move-result-object v5

    if-nez v5, :cond_0

    iget-object v3, p0, Lantlr/preprocessor/Hierarchy;->antlrTool:Lantlr/Tool;

    const-string v5, "grammar "

    invoke-static {v5}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v4}, Lantlr/preprocessor/Grammar;->getSuperGrammarName()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v6, " not defined"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3, v5}, Lantlr/Tool;->toolError(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    invoke-virtual {v4}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/util/Hashtable;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    move v3, v1

    goto :goto_0

    :cond_2
    if-nez v3, :cond_3

    return v1

    :cond_3
    iget-object v0, p0, Lantlr/preprocessor/Hierarchy;->symbols:Ljava/util/Hashtable;

    invoke-virtual {v0}, Ljava/util/Hashtable;->elements()Ljava/util/Enumeration;

    move-result-object v0

    :goto_1
    invoke-interface {v0}, Ljava/util/Enumeration;->hasMoreElements()Z

    move-result v1

    if-eqz v1, :cond_5

    invoke-interface {v0}, Ljava/util/Enumeration;->nextElement()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lantlr/preprocessor/Grammar;

    invoke-virtual {v1}, Lantlr/preprocessor/Grammar;->getSuperGrammarName()Ljava/lang/String;

    move-result-object v3

    if-nez v3, :cond_4

    goto :goto_1

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/preprocessor/Hierarchy;->findRoot(Lantlr/preprocessor/Grammar;)Lantlr/preprocessor/Grammar;

    move-result-object v3

    invoke-virtual {v3}, Lantlr/preprocessor/Grammar;->getName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v1, v3}, Lantlr/preprocessor/Grammar;->setType(Ljava/lang/String;)V

    goto :goto_1

    :cond_5
    return v2
.end method
