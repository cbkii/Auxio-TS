.class public Lantlr/debug/LLkDebuggingParser;
.super Lantlr/LLkParser;
.source ""

# interfaces
.implements Lantlr/debug/DebuggingParser;


# instance fields
.field public _notDebugMode:Z

.field public parserEventSupport:Lantlr/debug/ParserEventSupport;

.field public ruleNames:[Ljava/lang/String;

.field public semPredNames:[Ljava/lang/String;


# direct methods
.method public constructor <init>(I)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/LLkParser;-><init>(I)V

    new-instance p1, Lantlr/debug/ParserEventSupport;

    invoke-direct {p1, p0}, Lantlr/debug/ParserEventSupport;-><init>(Ljava/lang/Object;)V

    iput-object p1, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/debug/LLkDebuggingParser;->_notDebugMode:Z

    return-void
.end method

.method public constructor <init>(Lantlr/ParserSharedInputState;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/ParserSharedInputState;I)V

    new-instance p1, Lantlr/debug/ParserEventSupport;

    invoke-direct {p1, p0}, Lantlr/debug/ParserEventSupport;-><init>(Ljava/lang/Object;)V

    iput-object p1, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/debug/LLkDebuggingParser;->_notDebugMode:Z

    return-void
.end method

.method public constructor <init>(Lantlr/TokenBuffer;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenBuffer;I)V

    new-instance p1, Lantlr/debug/ParserEventSupport;

    invoke-direct {p1, p0}, Lantlr/debug/ParserEventSupport;-><init>(Ljava/lang/Object;)V

    iput-object p1, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/debug/LLkDebuggingParser;->_notDebugMode:Z

    return-void
.end method

.method public constructor <init>(Lantlr/TokenStream;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/LLkParser;-><init>(Lantlr/TokenStream;I)V

    new-instance p1, Lantlr/debug/ParserEventSupport;

    invoke-direct {p1, p0}, Lantlr/debug/ParserEventSupport;-><init>(Ljava/lang/Object;)V

    iput-object p1, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/debug/LLkDebuggingParser;->_notDebugMode:Z

    return-void
.end method


# virtual methods
.method public LA(I)I
    .locals 1

    invoke-super {p0, p1}, Lantlr/LLkParser;->LA(I)I

    move-result v0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1, v0}, Lantlr/debug/ParserEventSupport;->fireLA(II)V

    return v0
.end method

.method public addMessageListener(Lantlr/debug/MessageListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addMessageListener(Lantlr/debug/MessageListener;)V

    return-void
.end method

.method public addParserListener(Lantlr/debug/ParserListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addParserListener(Lantlr/debug/ParserListener;)V

    return-void
.end method

.method public addParserMatchListener(Lantlr/debug/ParserMatchListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addParserMatchListener(Lantlr/debug/ParserMatchListener;)V

    return-void
.end method

.method public addParserTokenListener(Lantlr/debug/ParserTokenListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addParserTokenListener(Lantlr/debug/ParserTokenListener;)V

    return-void
.end method

.method public addSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V

    return-void
.end method

.method public addSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V

    return-void
.end method

.method public addTraceListener(Lantlr/debug/TraceListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addTraceListener(Lantlr/debug/TraceListener;)V

    return-void
.end method

.method public consume()V
    .locals 1

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/debug/LLkDebuggingParser;->LA(I)I

    move-result v0

    invoke-super {p0}, Lantlr/LLkParser;->consume()V

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, v0}, Lantlr/debug/ParserEventSupport;->fireConsume(I)V

    return-void
.end method

.method public fireEnterRule(II)V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/LLkDebuggingParser;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v0, p1, p0, p2}, Lantlr/debug/ParserEventSupport;->fireEnterRule(III)V

    :cond_0
    return-void
.end method

.method public fireExitRule(II)V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/LLkDebuggingParser;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v0, p1, p0, p2}, Lantlr/debug/ParserEventSupport;->fireExitRule(III)V

    :cond_0
    return-void
.end method

.method public fireSemanticPredicateEvaluated(IIZ)Z
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/LLkDebuggingParser;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v0, p1, p2, p3, p0}, Lantlr/debug/ParserEventSupport;->fireSemanticPredicateEvaluated(IIZI)Z

    move-result p0

    return p0

    :cond_0
    return p3
.end method

.method public fireSyntacticPredicateFailed()V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/LLkDebuggingParser;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v0, p0}, Lantlr/debug/ParserEventSupport;->fireSyntacticPredicateFailed(I)V

    :cond_0
    return-void
.end method

.method public fireSyntacticPredicateStarted()V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/LLkDebuggingParser;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v0, p0}, Lantlr/debug/ParserEventSupport;->fireSyntacticPredicateStarted(I)V

    :cond_0
    return-void
.end method

.method public fireSyntacticPredicateSucceeded()V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/LLkDebuggingParser;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v0, p0}, Lantlr/debug/ParserEventSupport;->fireSyntacticPredicateSucceeded(I)V

    :cond_0
    return-void
.end method

.method public getRuleName(I)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->ruleNames:[Ljava/lang/String;

    aget-object p0, p0, p1

    return-object p0
.end method

.method public getSemPredName(I)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->semPredNames:[Ljava/lang/String;

    aget-object p0, p0, p1

    return-object p0
.end method

.method public declared-synchronized goToSleep()V
    .locals 1

    monitor-enter p0

    :try_start_0
    invoke-virtual {p0}, Ljava/lang/Object;->wait()V
    :try_end_0
    .catch Ljava/lang/InterruptedException; {:try_start_0 .. :try_end_0} :catch_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    goto :goto_0

    :catchall_0
    move-exception v0

    monitor-exit p0

    throw v0

    :catch_0
    :goto_0
    monitor-exit p0

    return-void
.end method

.method public isDebugMode()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/debug/LLkDebuggingParser;->_notDebugMode:Z

    xor-int/lit8 p0, p0, 0x1

    return p0
.end method

.method public isGuessing()Z
    .locals 0

    iget-object p0, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget p0, p0, Lantlr/ParserSharedInputState;->guessing:I

    if-lez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public match(I)V
    .locals 4

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v0}, Lantlr/debug/LLkDebuggingParser;->LA(I)I

    move-result v0

    :try_start_0
    invoke-super {p0, p1}, Lantlr/Parser;->match(I)V

    iget-object v2, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v2, p1, v1, v3}, Lantlr/debug/ParserEventSupport;->fireMatch(ILjava/lang/String;I)V
    :try_end_0
    .catch Lantlr/MismatchedTokenException; {:try_start_0 .. :try_end_0} :catch_0

    return-void

    :catch_0
    move-exception v2

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, v0, p1, v1, v3}, Lantlr/debug/ParserEventSupport;->fireMismatch(IILjava/lang/String;I)V

    :cond_0
    throw v2
.end method

.method public match(Lantlr/collections/impl/BitSet;)V
    .locals 4

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v0}, Lantlr/debug/LLkDebuggingParser;->LA(I)I

    move-result v0

    :try_start_0
    invoke-super {p0, p1}, Lantlr/Parser;->match(Lantlr/collections/impl/BitSet;)V

    iget-object v2, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v2, v0, p1, v1, v3}, Lantlr/debug/ParserEventSupport;->fireMatch(ILantlr/collections/impl/BitSet;Ljava/lang/String;I)V
    :try_end_0
    .catch Lantlr/MismatchedTokenException; {:try_start_0 .. :try_end_0} :catch_0

    return-void

    :catch_0
    move-exception v2

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, v0, p1, v1, v3}, Lantlr/debug/ParserEventSupport;->fireMismatch(ILantlr/collections/impl/BitSet;Ljava/lang/String;I)V

    :cond_0
    throw v2
.end method

.method public matchNot(I)V
    .locals 4

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/LLkParser;->LT(I)Lantlr/Token;

    move-result-object v1

    invoke-virtual {v1}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v0}, Lantlr/debug/LLkDebuggingParser;->LA(I)I

    move-result v0

    :try_start_0
    invoke-super {p0, p1}, Lantlr/Parser;->matchNot(I)V

    iget-object v2, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    invoke-virtual {v2, v0, p1, v1, v3}, Lantlr/debug/ParserEventSupport;->fireMatchNot(IILjava/lang/String;I)V
    :try_end_0
    .catch Lantlr/MismatchedTokenException; {:try_start_0 .. :try_end_0} :catch_0

    return-void

    :catch_0
    move-exception v2

    iget-object v3, p0, Lantlr/Parser;->inputState:Lantlr/ParserSharedInputState;

    iget v3, v3, Lantlr/ParserSharedInputState;->guessing:I

    if-nez v3, :cond_0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, v0, p1, v1, v3}, Lantlr/debug/ParserEventSupport;->fireMismatchNot(IILjava/lang/String;I)V

    :cond_0
    throw v2
.end method

.method public removeMessageListener(Lantlr/debug/MessageListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeMessageListener(Lantlr/debug/MessageListener;)V

    return-void
.end method

.method public removeParserListener(Lantlr/debug/ParserListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeParserListener(Lantlr/debug/ParserListener;)V

    return-void
.end method

.method public removeParserMatchListener(Lantlr/debug/ParserMatchListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeParserMatchListener(Lantlr/debug/ParserMatchListener;)V

    return-void
.end method

.method public removeParserTokenListener(Lantlr/debug/ParserTokenListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeParserTokenListener(Lantlr/debug/ParserTokenListener;)V

    return-void
.end method

.method public removeSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V

    return-void
.end method

.method public removeSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V

    return-void
.end method

.method public removeTraceListener(Lantlr/debug/TraceListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeTraceListener(Lantlr/debug/TraceListener;)V

    return-void
.end method

.method public reportError(Lantlr/RecognitionException;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {v0, p1}, Lantlr/debug/ParserEventSupport;->fireReportError(Ljava/lang/Exception;)V

    invoke-super {p0, p1}, Lantlr/Parser;->reportError(Lantlr/RecognitionException;)V

    return-void
.end method

.method public reportError(Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {v0, p1}, Lantlr/debug/ParserEventSupport;->fireReportError(Ljava/lang/String;)V

    invoke-super {p0, p1}, Lantlr/Parser;->reportError(Ljava/lang/String;)V

    return-void
.end method

.method public reportWarning(Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/LLkDebuggingParser;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {v0, p1}, Lantlr/debug/ParserEventSupport;->fireReportWarning(Ljava/lang/String;)V

    invoke-super {p0, p1}, Lantlr/Parser;->reportWarning(Ljava/lang/String;)V

    return-void
.end method

.method public setDebugMode(Z)V
    .locals 0

    xor-int/lit8 p1, p1, 0x1

    iput-boolean p1, p0, Lantlr/debug/LLkDebuggingParser;->_notDebugMode:Z

    return-void
.end method

.method public setupDebugging(Lantlr/TokenBuffer;)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, v0, p1}, Lantlr/debug/LLkDebuggingParser;->setupDebugging(Lantlr/TokenStream;Lantlr/TokenBuffer;)V

    return-void
.end method

.method public setupDebugging(Lantlr/TokenStream;)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, v0}, Lantlr/debug/LLkDebuggingParser;->setupDebugging(Lantlr/TokenStream;Lantlr/TokenBuffer;)V

    return-void
.end method

.method public setupDebugging(Lantlr/TokenStream;Lantlr/TokenBuffer;)V
    .locals 7

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/debug/LLkDebuggingParser;->setDebugMode(Z)V

    :try_start_0
    const-string v1, "javax.swing.JButton"

    invoke-static {v1}, Lantlr/Utils;->loadClass(Ljava/lang/String;)Ljava/lang/Class;
    :try_end_0
    .catch Ljava/lang/ClassNotFoundException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception p0

    goto :goto_1

    :catch_1
    :try_start_1
    sget-object v1, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v2, "Swing is required to use ParseView, but is not present in your CLASSPATH"

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    invoke-static {v0}, Ljava/lang/System;->exit(I)V

    :goto_0
    const-string v1, "antlr.parseview.ParseView"

    invoke-static {v1}, Lantlr/Utils;->loadClass(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    const/4 v2, 0x3

    new-array v3, v2, [Ljava/lang/Class;

    const-class v4, Lantlr/debug/LLkDebuggingParser;

    const/4 v5, 0x0

    aput-object v4, v3, v5

    const-class v4, Lantlr/TokenStream;

    aput-object v4, v3, v0

    const-class v4, Lantlr/TokenBuffer;

    const/4 v6, 0x2

    aput-object v4, v3, v6

    invoke-virtual {v1, v3}, Ljava/lang/Class;->getConstructor([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;

    move-result-object v1

    new-array v2, v2, [Ljava/lang/Object;

    aput-object p0, v2, v5

    aput-object p1, v2, v0

    aput-object p2, v2, v6

    invoke-virtual {v1, v2}, Ljava/lang/reflect/Constructor;->newInstance([Ljava/lang/Object;)Ljava/lang/Object;
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0

    goto :goto_2

    :goto_1
    sget-object p1, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Error initializing ParseView: "

    invoke-virtual {p2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string p1, "Please report this to Scott Stanchfield, thetick@magelang.com"

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    invoke-static {v0}, Ljava/lang/System;->exit(I)V

    :goto_2
    return-void
.end method

.method public declared-synchronized wakeUp()V
    .locals 1

    monitor-enter p0

    :try_start_0
    invoke-virtual {p0}, Ljava/lang/Object;->notify()V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    monitor-exit p0

    return-void

    :catchall_0
    move-exception v0

    monitor-exit p0

    throw v0
.end method
