.class public Lantlr/TreeWalkerGrammar;
.super Lantlr/Grammar;
.source ""


# instance fields
.field public transform:Z


# direct methods
.method public constructor <init>(Ljava/lang/String;Lantlr/Tool;Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1, p2, p3}, Lantlr/Grammar;-><init>(Ljava/lang/String;Lantlr/Tool;Ljava/lang/String;)V

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/TreeWalkerGrammar;->transform:Z

    return-void
.end method


# virtual methods
.method public generate()V
    .locals 1

    iget-object v0, p0, Lantlr/Grammar;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v0, p0}, Lantlr/CodeGenerator;->gen(Lantlr/TreeWalkerGrammar;)V

    return-void
.end method

.method public getSuperClass()Ljava/lang/String;
    .locals 0

    const-string p0, "TreeParser"

    return-object p0
.end method

.method public processArguments([Ljava/lang/String;)V
    .locals 3

    const/4 v0, 0x0

    :goto_0
    array-length v1, p1

    if-ge v0, v1, :cond_2

    aget-object v1, p1, v0

    const-string v2, "-trace"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_1

    :cond_0
    aget-object v1, p1, v0

    const-string v2, "-traceTreeParser"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_1

    :goto_1
    const/4 v1, 0x1

    iput-boolean v1, p0, Lantlr/Grammar;->traceRules:Z

    iget-object v1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {v1, v0}, Lantlr/Tool;->setArgOK(I)V

    :cond_1
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    return-void
.end method

.method public setOption(Ljava/lang/String;Lantlr/Token;)Z
    .locals 3

    const-string v0, "buildAST"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/4 v1, 0x0

    const/4 v2, 0x1

    if-eqz v0, :cond_2

    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    const-string v0, "true"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_0

    iput-boolean v2, p0, Lantlr/Grammar;->buildAST:Z

    goto :goto_0

    :cond_0
    invoke-virtual {p2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object p1

    const-string v0, "false"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_1

    iput-boolean v1, p0, Lantlr/Grammar;->buildAST:Z

    goto :goto_0

    :cond_1
    iget-object p1, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v0

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    const-string v1, "buildAST option must be true or false"

    invoke-virtual {p1, v1, p0, v0, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_0
    return v2

    :cond_2
    const-string v0, "ASTLabelType"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_3

    invoke-super {p0, p1, p2}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    return v2

    :cond_3
    const-string v0, "className"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_4

    invoke-super {p0, p1, p2}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    return v2

    :cond_4
    invoke-super {p0, p1, p2}, Lantlr/Grammar;->setOption(Ljava/lang/String;Lantlr/Token;)Z

    move-result v0

    if-eqz v0, :cond_5

    return v2

    :cond_5
    iget-object v0, p0, Lantlr/Grammar;->antlrTool:Lantlr/Tool;

    const-string v2, "Invalid option: "

    invoke-static {v2, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0}, Lantlr/Grammar;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p2}, Lantlr/Token;->getLine()I

    move-result v2

    invoke-virtual {p2}, Lantlr/Token;->getColumn()I

    move-result p2

    invoke-virtual {v0, p1, p0, v2, p2}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return v1
.end method
