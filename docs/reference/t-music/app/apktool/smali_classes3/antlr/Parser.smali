.class public abstract Lantlr/Parser;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public astFactory:Lantlr/ASTFactory;

.field public ignoreInvalidDebugCalls:Z

.field public inputState:Lantlr/ParserSharedInputState;

.field public returnAST:Lantlr/collections/AST;

.field public tokenNames:[Ljava/lang/String;

.field public tokenTypeToASTClassMap:Ljava/util/Hashtable;

.field public traceDepth:I


# direct methods
.method public constructor <init>()V
    .locals 1

    new-instance v0, Lantlr/ParserSharedInputState;

    invoke-direct {v0}, Lantlr/ParserSharedInputState;-><init>()V

    invoke-direct {p0, v0}, Lantlr/Parser;-><init>(Lantlr/ParserSharedInputState;)V

    return-void
.end method

.method public constructor <init>(Lantlr/ParserSharedInputState;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/Parser;->astFactory:Lantlr/ASTFactory;

    iput-object v0, p0, Lantlr/Parser;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    iput v0, p0, Lantlr/Parser;->traceDepth:I

    iput-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    return-void
.end method

.method public static panic()V
    .locals 2

    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v1, "Parser: panic"

    invoke-virtual {v0, v1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    const/4 v0, 0x1

    invoke-static {v0}, Ljava/lang/System;->exit(I)V

    return-void
.end method


# virtual methods
.method public abstract LA(I)I
.end method

.method public abstract LT(I)Lantlr/Token;
.end method

.method public addMessageListener(Lantlr/debug/MessageListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "addMessageListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public addParserListener(Lantlr/debug/ParserListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "addParserListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public addParserMatchListener(Lantlr/debug/ParserMatchListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "addParserMatchListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public addParserTokenListener(Lantlr/debug/ParserTokenListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "addParserTokenListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public addSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "addSemanticPredicateListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public addSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "addSyntacticPredicateListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public addTraceListener(Lantlr/debug/TraceListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "addTraceListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public abstract consume()V
.end method

.method public consumeUntil(I)V
    .locals 2

    :goto_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/Parser;->LA(I)I

    move-result v1

    if-eq v1, v0, :cond_0

    invoke-virtual {p0, v0}, Lantlr/Parser;->LA(I)I

    move-result v0

    if-eq v0, p1, :cond_0

    invoke-virtual {p0}, Lantlr/Parser;->consume()V

    goto :goto_0

    :cond_0
    return-void
.end method

.method public consumeUntil(Lantlr/collections/impl/BitSet;)V
    .locals 2

    :goto_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/Parser;->LA(I)I

    move-result v1

    if-eq v1, v0, :cond_0

    invoke-virtual {p0, v0}, Lantlr/Parser;->LA(I)I

    move-result v0

    invoke-virtual {p1, v0}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v0

    if-nez v0, :cond_0

    invoke-virtual {p0}, Lantlr/Parser;->consume()V

    goto :goto_0

    :cond_0
    return-void
.end method

.method public defaultDebuggingSetup(Lantlr/TokenStream;Lantlr/TokenBuffer;)V
    .locals 0

    return-void
.end method

.method public getAST()Lantlr/collections/AST;
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->returnAST:Lantlr/collections/AST;

    return-object p0
.end method

.method public getASTFactory()Lantlr/ASTFactory;
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->astFactory:Lantlr/ASTFactory;

    return-object p0
.end method

.method public getFilename()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget-object p0, p0, Lantlr/ParserSharedInputState;->filename:Ljava/lang/String;

    return-object p0
.end method

.method public getInputState()Lantlr/ParserSharedInputState;
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    return-object p0
.end method

.method public getTokenName(I)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    aget-object p0, p0, p1

    return-object p0
.end method

.method public getTokenNames()[Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    return-object p0
.end method

.method public getTokenTypeToASTClassMap()Ljava/util/Hashtable;
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->tokenTypeToASTClassMap:Ljava/util/Hashtable;

    return-object p0
.end method

.method public isDebugMode()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public mark()I
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget-object p0, p0, Lantlr/ParserSharedInputState;->input:Lantlr/TokenBuffer;

    invoke-virtual {p0}, Lantlr/TokenBuffer;->mark()I

    move-result p0

    return p0
.end method

.method public match(I)V
    .locals 8

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/Parser;->LA(I)I

    move-result v1

    if-ne v1, p1, :cond_0

    invoke-virtual {p0}, Lantlr/Parser;->consume()V

    return-void

    :cond_0
    new-instance v1, Lantlr/MismatchedTokenException;

    iget-object v3, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/Parser;->LT(I)Lantlr/Token;

    move-result-object v4

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v7

    const/4 v6, 0x0

    move-object v2, v1

    move v5, p1

    invoke-direct/range {v2 .. v7}, Lantlr/MismatchedTokenException;-><init>([Ljava/lang/String;Lantlr/Token;IZLjava/lang/String;)V

    throw v1
.end method

.method public match(Lantlr/collections/impl/BitSet;)V
    .locals 8

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/Parser;->LA(I)I

    move-result v1

    invoke-virtual {p1, v1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Lantlr/Parser;->consume()V

    return-void

    :cond_0
    new-instance v1, Lantlr/MismatchedTokenException;

    iget-object v3, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/Parser;->LT(I)Lantlr/Token;

    move-result-object v4

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v7

    const/4 v6, 0x0

    move-object v2, v1

    move-object v5, p1

    invoke-direct/range {v2 .. v7}, Lantlr/MismatchedTokenException;-><init>([Ljava/lang/String;Lantlr/Token;Lantlr/collections/impl/BitSet;ZLjava/lang/String;)V

    throw v1
.end method

.method public matchNot(I)V
    .locals 8

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/Parser;->LA(I)I

    move-result v1

    if-eq v1, p1, :cond_0

    invoke-virtual {p0}, Lantlr/Parser;->consume()V

    return-void

    :cond_0
    new-instance v1, Lantlr/MismatchedTokenException;

    iget-object v3, p0, Lantlr/Parser;->tokenNames:[Ljava/lang/String;

    invoke-virtual {p0, v0}, Lantlr/Parser;->LT(I)Lantlr/Token;

    move-result-object v4

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v7

    const/4 v6, 0x1

    move-object v2, v1

    move v5, p1

    invoke-direct/range {v2 .. v7}, Lantlr/MismatchedTokenException;-><init>([Ljava/lang/String;Lantlr/Token;IZLjava/lang/String;)V

    throw v1
.end method

.method public recover(Lantlr/RecognitionException;Lantlr/collections/impl/BitSet;)V
    .locals 0

    invoke-virtual {p0}, Lantlr/Parser;->consume()V

    invoke-virtual {p0, p2}, Lantlr/Parser;->consumeUntil(Lantlr/collections/impl/BitSet;)V

    return-void
.end method

.method public removeMessageListener(Lantlr/debug/MessageListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/RuntimeException;

    const-string p1, "removeMessageListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public removeParserListener(Lantlr/debug/ParserListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/RuntimeException;

    const-string p1, "removeParserListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public removeParserMatchListener(Lantlr/debug/ParserMatchListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/RuntimeException;

    const-string p1, "removeParserMatchListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public removeParserTokenListener(Lantlr/debug/ParserTokenListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/RuntimeException;

    const-string p1, "removeParserTokenListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public removeSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "removeSemanticPredicateListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public removeSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "removeSyntacticPredicateListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public removeTraceListener(Lantlr/debug/TraceListener;)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/RuntimeException;

    const-string p1, "removeTraceListener() is only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public reportError(Lantlr/RecognitionException;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/Object;)V

    return-void
.end method

.method public reportError(Ljava/lang/String;)V
    .locals 2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "error: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, ": error: "

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :goto_0
    return-void
.end method

.method public reportWarning(Ljava/lang/String;)V
    .locals 2

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "warning: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    sget-object v0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0}, Lantlr/Parser;->getFilename()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, ": warning: "

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :goto_0
    return-void
.end method

.method public rewind(I)V
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget-object p0, p0, Lantlr/ParserSharedInputState;->input:Lantlr/TokenBuffer;

    invoke-virtual {p0, p1}, Lantlr/TokenBuffer;->rewind(I)V

    return-void
.end method

.method public setASTFactory(Lantlr/ASTFactory;)V
    .locals 0

    iput-object p1, p0, Lantlr/Parser;->astFactory:Lantlr/ASTFactory;

    return-void
.end method

.method public setASTNodeClass(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->astFactory:Lantlr/ASTFactory;

    invoke-virtual {p0, p1}, Lantlr/ASTFactory;->setASTNodeType(Ljava/lang/String;)V

    return-void
.end method

.method public setASTNodeType(Ljava/lang/String;)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/Parser;->setASTNodeClass(Ljava/lang/String;)V

    return-void
.end method

.method public setDebugMode(Z)V
    .locals 0

    iget-boolean p0, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    if-eqz p0, :cond_0

    return-void

    :cond_0
    new-instance p0, Ljava/lang/RuntimeException;

    const-string p1, "setDebugMode() only valid if parser built for debugging"

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public setFilename(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iput-object p1, p0, Lantlr/ParserSharedInputState;->filename:Ljava/lang/String;

    return-void
.end method

.method public setIgnoreInvalidDebugCalls(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/Parser;->ignoreInvalidDebugCalls:Z

    return-void
.end method

.method public setInputState(Lantlr/ParserSharedInputState;)V
    .locals 0

    iput-object p1, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    return-void
.end method

.method public setTokenBuffer(Lantlr/TokenBuffer;)V
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iput-object p1, p0, Lantlr/ParserSharedInputState;->input:Lantlr/TokenBuffer;

    return-void
.end method

.method public traceIn(Ljava/lang/String;)V
    .locals 4

    iget v0, p0, Lantlr/Parser;->traceDepth:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/Parser;->traceDepth:I

    invoke-virtual {p0}, Lantlr/Parser;->traceIndent()V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, "> "

    const-string v3, "; LA(1)=="

    invoke-static {v2, p1, v3}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p0, v1}, Lantlr/Parser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-lez p0, :cond_0

    const-string p0, " [guessing]"

    goto :goto_0

    :cond_0
    const-string p0, ""

    :goto_0
    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public traceIndent()V
    .locals 3

    const/4 v0, 0x0

    :goto_0
    iget v1, p0, Lantlr/Parser;->traceDepth:I

    if-ge v0, v1, :cond_0

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, " "

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->print(Ljava/lang/String;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public traceOut(Ljava/lang/String;)V
    .locals 3

    invoke-virtual {p0}, Lantlr/Parser;->traceIndent()V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "< "

    const-string v2, "; LA(1)=="

    invoke-static {v1, p1, v2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/Parser;->LT(I)Lantlr/Token;

    move-result-object v2

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v2, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v2, v2, Lantlr/ParserSharedInputState;->guessing:I

    if-lez v2, :cond_0

    const-string v2, " [guessing]"

    goto :goto_0

    :cond_0
    const-string v2, ""

    :goto_0
    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/Parser;->traceDepth:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/Parser;->traceDepth:I

    return-void
.end method
