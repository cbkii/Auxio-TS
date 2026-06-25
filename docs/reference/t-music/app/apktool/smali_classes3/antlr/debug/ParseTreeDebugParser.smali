.class public Lantlr/debug/ParseTreeDebugParser;
.super Lantlr/LLkParser;
.source ""


# instance fields
.field public currentParseTreeRoot:Ljava/util/Stack;

.field public mostRecentParseTreeRoot:Lantlr/ParseTreeRule;

.field public numberOfDerivationSteps:I


# direct methods
.method public constructor <init>(I)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/LLkParser;-><init>(I)V

    new-instance p1, Ljava/util/Stack;

    invoke-direct {p1}, Ljava/util/Stack;-><init>()V

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->mostRecentParseTreeRoot:Lantlr/ParseTreeRule;

    const/4 p1, 0x1

    iput p1, p0, Lantlr/debug/ParseTreeDebugParser;->numberOfDerivationSteps:I

    return-void
.end method

.method public constructor <init>(Lantlr/ParserSharedInputState;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/ParserSharedInputState;I)V

    new-instance p1, Ljava/util/Stack;

    invoke-direct {p1}, Ljava/util/Stack;-><init>()V

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->mostRecentParseTreeRoot:Lantlr/ParseTreeRule;

    const/4 p1, 0x1

    iput p1, p0, Lantlr/debug/ParseTreeDebugParser;->numberOfDerivationSteps:I

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenBuffer;I)V

    new-instance p1, Ljava/util/Stack;

    invoke-direct {p1}, Ljava/util/Stack;-><init>()V

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->mostRecentParseTreeRoot:Lantlr/ParseTreeRule;

    const/4 p1, 0x1

    iput p1, p0, Lantlr/debug/ParseTreeDebugParser;->numberOfDerivationSteps:I

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenStream;I)V

    new-instance p1, Ljava/util/Stack;

    invoke-direct {p1}, Ljava/util/Stack;-><init>()V

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->mostRecentParseTreeRoot:Lantlr/ParseTreeRule;

    const/4 p1, 0x1

    iput p1, p0, Lantlr/debug/ParseTreeDebugParser;->numberOfDerivationSteps:I

    return-void
.end method


# virtual methods
.method public addCurrentTokenToParseTree()V
    .locals 3

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-lez v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    invoke-virtual {v0}, Ljava/util/Stack;->peek()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/ParseTreeRule;

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LA(I)I

    move-result v2

    if-ne v2, v1, :cond_1

    new-instance p0, Lantlr/ParseTreeToken;

    new-instance v1, Lantlr/CommonToken;

    const-string v2, "EOF"

    invoke-direct {v1, v2}, Lantlr/CommonToken;-><init>(Ljava/lang/String;)V

    invoke-direct {p0, v1}, Lantlr/ParseTreeToken;-><init>(Lantlr/Token;)V

    goto :goto_0

    :cond_1
    new-instance v2, Lantlr/ParseTreeToken;

    invoke-virtual {p0, v1}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object p0

    invoke-direct {v2, p0}, Lantlr/ParseTreeToken;-><init>(Lantlr/Token;)V

    move-object p0, v2

    :goto_0
    invoke-virtual {v0, p0}, Lantlr/BaseAST;->addChild(Lantlr/collections/AST;)V

    return-void
.end method

.method public getNumberOfDerivationSteps()I
    .locals 0

    iget p0, p0, Lantlr/debug/ParseTreeDebugParser;->numberOfDerivationSteps:I

    return p0
.end method

.method public getParseTree()Lantlr/ParseTree;
    .locals 0

    iget-object p0, p0, Lantlr/debug/ParseTreeDebugParser;->mostRecentParseTreeRoot:Lantlr/ParseTreeRule;

    return-object p0
.end method

.method public match(I)V
    .locals 0

    invoke-virtual {p0}, Lantlr/debug/ParseTreeDebugParser;->addCurrentTokenToParseTree()V

    invoke-super {p0, p1}, Lantlr/Parser;->match(I)V

    return-void
.end method

.method public match(Lantlr/collections/impl/BitSet;)V
    .locals 0

    invoke-virtual {p0}, Lantlr/debug/ParseTreeDebugParser;->addCurrentTokenToParseTree()V

    invoke-super {p0, p1}, Lantlr/Parser;->match(Lantlr/collections/impl/BitSet;)V

    return-void
.end method

.method public matchNot(I)V
    .locals 0

    invoke-virtual {p0}, Lantlr/debug/ParseTreeDebugParser;->addCurrentTokenToParseTree()V

    invoke-super {p0, p1}, Lantlr/Parser;->matchNot(I)V

    return-void
.end method

.method public traceIn(Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v0, v0, Lantlr/ParserSharedInputState;->guessing:I

    if-lez v0, :cond_0

    return-void

    :cond_0
    new-instance v0, Lantlr/ParseTreeRule;

    invoke-direct {v0, p1}, Lantlr/ParseTreeRule;-><init>(Ljava/lang/String;)V

    iget-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    invoke-virtual {p1}, Ljava/util/Stack;->size()I

    move-result p1

    if-lez p1, :cond_1

    iget-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    invoke-virtual {p1}, Ljava/util/Stack;->peek()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lantlr/ParseTreeRule;

    invoke-virtual {p1, v0}, Lantlr/BaseAST;->addChild(Lantlr/collections/AST;)V

    :cond_1
    iget-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    invoke-virtual {p1, v0}, Ljava/util/Stack;->push(Ljava/lang/Object;)Ljava/lang/Object;

    iget p1, p0, Lantlr/debug/ParseTreeDebugParser;->numberOfDerivationSteps:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lantlr/debug/ParseTreeDebugParser;->numberOfDerivationSteps:I

    return-void
.end method

.method public traceOut(Ljava/lang/String;)V
    .locals 0

    iget-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p1, p1, Lantlr/ParserSharedInputState;->guessing:I

    if-lez p1, :cond_0

    return-void

    :cond_0
    iget-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->currentParseTreeRoot:Ljava/util/Stack;

    invoke-virtual {p1}, Ljava/util/Stack;->pop()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lantlr/ParseTreeRule;

    iput-object p1, p0, Lantlr/debug/ParseTreeDebugParser;->mostRecentParseTreeRoot:Lantlr/ParseTreeRule;

    return-void
.end method
