.class public Lantlr/LexerGrammar;
.super Lantlr/Grammar;
.source ""


# instance fields
.field public caseSensitive:Z

.field public caseSensitiveLiterals:Z

.field public charVocabulary:Lantlr/collections/impl/BitSet;

.field public filterMode:Z

.field public filterRule:Ljava/lang/String;

.field public testLiterals:Z


# direct methods
.method public constructor <init>(Ljava/lang/String;Lantlr/Tool;Ljava/lang/String;)V
    .locals 1

    invoke-direct {p0, p1, p2, p3}, Lantlr/Grammar;-><init>(Ljava/lang/String;Lantlr/Tool;Ljava/lang/String;)V

    const/4 p1, 0x1

    iput-boolean p1, p0, Lantlr/LexerGrammar;->testLiterals:Z

    iput-boolean p1, p0, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    iput-boolean p1, p0, Lantlr/LexerGrammar;->caseSensitive:Z

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/LexerGrammar;->filterMode:Z

    const/4 p2, 0x0

    iput-object p2, p0, Lantlr/LexerGrammar;->filterRule:Ljava/lang/String;

    new-instance p2, Lantlr/collections/impl/BitSet;

    invoke-direct {p2}, Lantlr/collections/impl/BitSet;-><init>()V

    move p3, p1

    :goto_0
    const/16 v0, 0x7f

    if-gt p3, v0, :cond_0

    invoke-virtual {p2, p3}, Lantlr/collections/impl/BitSet;->add(I)V

    add-int/lit8 p3, p3, 0x1

    goto :goto_0

    :cond_0
    invoke-virtual {p0, p2}, Lantlr/LexerGrammar;->setCharVocabulary(Lantlr/collections/impl/BitSet;)V

    iput-boolean p1, p0, Lantlr/Grammar;->defaultErrorHandler:Z

    return-void
.end method


# virtual methods
.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/LexerGrammar;)V

    return-void
.end method

.method public getSuperClass()Ljava/lang/String;
    .locals 0

    iget-boolean p0, p0, Lantlr/Grammar;->debuggingOutput:Z

    if-eqz p0, :cond_0

    const-string p0, "debug.DebuggingCharScanner"

    return-object p0

    :cond_0
    const-string p0, "CharScanner"

    return-object p0
.end method

.method public getTestLiterals()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/LexerGrammar;->testLiterals:Z

    return p0
.end method

.method public processArguments([Ljava/lang/String;)V
    .locals 4

    const/4 v0, 0x0

    :goto_0
    array-length v1, p1

    if-ge v0, v1, :cond_3

    aget-object v1, p1, v0

    const-string v2, "-trace"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    const/4 v2, 0x1

    if-eqz v1, :cond_0

    goto :goto_1

    :cond_0
    aget-object v1, p1, v0

    const-string v3, "-traceLexer"

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_1

    :goto_1
    iput-boolean v2, p0, Lantlr/Grammar;->traceRules:Z

    :goto_2
    iget-object v1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1, v0}, Lantlr/Tool;->setArgOK(I)V

    goto :goto_3

    :cond_1
    aget-object v1, p1, v0

    const-string v3, "-debug"

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_2

    iput-boolean v2, p0, Lantlr/Grammar;->debuggingOutput:Z

    goto :goto_2

    :cond_2
    :goto_3
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_3
    return-void
.end method

.method public setCharVocabulary(Lantlr/collections/impl/BitSet;)V
    .locals 0

    iput-object p1, p0, Lantlr/LexerGrammar;->charVocabulary:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public setOption(Ljava/lang/String;Lantlr/Token;)Z
    .locals 6

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v0

    const-string v1, "buildAST"

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    const/4 v2, 0x1

    if-eqz v1, :cond_0

    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "buildAST option is not valid for lexer"

    :goto_0
    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    return v2

    :cond_0
    const-string v1, "testLiterals"

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    const-string v3, "false"

    const-string v4, "true"

    const/4 v5, 0x0

    if-eqz v1, :cond_3

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_1

    iput-boolean v2, p0, Lantlr/LexerGrammar;->testLiterals:Z

    goto :goto_1

    :cond_1
    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_2

    iput-boolean v5, p0, Lantlr/LexerGrammar;->testLiterals:Z

    goto :goto_1

    :cond_2
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "testLiterals option must be true or false"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_1
    return v2

    :cond_3
    const-string v1, "interactive"

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_6

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_4

    iput-boolean v2, p0, Lantlr/Grammar;->interactive:Z

    goto :goto_2

    :cond_4
    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_5

    iput-boolean v5, p0, Lantlr/Grammar;->interactive:Z

    goto :goto_2

    :cond_5
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "interactive option must be true or false"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_2
    return v2

    :cond_6
    const-string v1, "caseSensitive"

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_9

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_7

    iput-boolean v2, p0, Lantlr/LexerGrammar;->caseSensitive:Z

    goto :goto_3

    :cond_7
    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_8

    iput-boolean v5, p0, Lantlr/LexerGrammar;->caseSensitive:Z

    goto :goto_3

    :cond_8
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "caseSensitive option must be true or false"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_3
    return v2

    :cond_9
    const-string v1, "caseSensitiveLiterals"

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_c

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_a

    iput-boolean v2, p0, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    goto :goto_4

    :cond_a
    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_b

    iput-boolean v5, p0, Lantlr/LexerGrammar;->caseSensitiveLiterals:Z

    goto :goto_4

    :cond_b
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "caseSensitiveLiterals option must be true or false"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_4
    return v2

    :cond_c
    const-string v1, "filter"

    invoke-virtual {p1, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_10

    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_d

    iput-boolean v2, p0, Lantlr/LexerGrammar;->filterMode:Z

    goto :goto_5

    :cond_d
    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_e

    iput-boolean v5, p0, Lantlr/LexerGrammar;->filterMode:Z

    goto :goto_5

    :cond_e
    invoke-virtual {p2}, Lantlr/Token;->getType()I

    move-result p1

    const/16 v1, 0x18

    if-ne p1, v1, :cond_f

    iput-boolean v2, p0, Lantlr/LexerGrammar;->filterMode:Z

    iput-object v0, p0, Lantlr/LexerGrammar;->filterRule:Ljava/lang/String;

    goto :goto_5

    :cond_f
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "filter option must be true, false, or a lexer rule name"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_5
    return v2

    :cond_10
    const-string v0, "longestPossible"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_11

    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "longestPossible option has been deprecated; ignoring it..."

    goto/16 :goto_0

    :cond_11
    const-string v0, "className"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_12

    invoke-super {p0, p1, p2}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    return v2

    :cond_12
    invoke-super {p0, p1, p2}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    move-result v0

    if-eqz v0, :cond_13

    return v2

    :cond_13
    iget-object v0, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v1, "Invalid option: "

    invoke-static {v1, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v1

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    invoke-virtual {v0, p1, p0, v1, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return v5
.end method
