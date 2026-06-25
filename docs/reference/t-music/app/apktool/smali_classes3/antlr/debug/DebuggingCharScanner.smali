.class public abstract Lantlr/debug/DebuggingCharScanner;
.super Lantlr/CharScanner;
.source ""

# interfaces
.implements Lantlr/debug/DebuggingParser;


# instance fields
.field public _notDebugMode:Z

.field public parserEventSupport:Lantlr/debug/ParserEventSupport;

.field public ruleNames:[Ljava/lang/String;

.field public semPredNames:[Ljava/lang/String;


# direct methods
.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/CharScanner;-><init>(Lantlr/InputBuffer;)V

    new-instance p1, Lantlr/debug/ParserEventSupport;

    invoke-direct {p1, p0}, Lantlr/debug/ParserEventSupport;-><init>(Ljava/lang/Object;)V

    iput-object p1, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/debug/DebuggingCharScanner;->_notDebugMode:Z

    return-void
.end method

.method public constructor <init>(Lantlr/LexerSharedInputState;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/CharScanner;-><init>(Lantlr/LexerSharedInputState;)V

    new-instance p1, Lantlr/debug/ParserEventSupport;

    invoke-direct {p1, p0}, Lantlr/debug/ParserEventSupport;-><init>(Ljava/lang/Object;)V

    iput-object p1, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    const/4 p1, 0x0

    iput-boolean p1, p0, Lantlr/debug/DebuggingCharScanner;->_notDebugMode:Z

    return-void
.end method


# virtual methods
.method public LA(I)C
    .locals 1

    invoke-super {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1, v0}, Lantlr/debug/ParserEventSupport;->fireLA(II)V

    return v0
.end method

.method public addMessageListener(Lantlr/debug/MessageListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addMessageListener(Lantlr/debug/MessageListener;)V

    return-void
.end method

.method public addNewLineListener(Lantlr/debug/NewLineListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addNewLineListener(Lantlr/debug/NewLineListener;)V

    return-void
.end method

.method public addParserListener(Lantlr/debug/ParserListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addParserListener(Lantlr/debug/ParserListener;)V

    return-void
.end method

.method public addParserMatchListener(Lantlr/debug/ParserMatchListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addParserMatchListener(Lantlr/debug/ParserMatchListener;)V

    return-void
.end method

.method public addParserTokenListener(Lantlr/debug/ParserTokenListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addParserTokenListener(Lantlr/debug/ParserTokenListener;)V

    return-void
.end method

.method public addSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V

    return-void
.end method

.method public addSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V

    return-void
.end method

.method public addTraceListener(Lantlr/debug/TraceListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->addTraceListener(Lantlr/debug/TraceListener;)V

    return-void
.end method

.method public consume()V
    .locals 1

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/debug/DebuggingCharScanner;->LA(I)C

    move-result v0
    :try_end_0
    .catch Lantlr/CharStreamException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    const/16 v0, -0x63

    :goto_0
    invoke-super {p0}, Lantlr/CharScanner;->consume()V

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, v0}, Lantlr/debug/ParserEventSupport;->fireConsume(I)V

    return-void
.end method

.method public fireEnterRule(II)V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/DebuggingCharScanner;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v0, p1, p0, p2}, Lantlr/debug/ParserEventSupport;->fireEnterRule(III)V

    :cond_0
    return-void
.end method

.method public fireExitRule(II)V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/DebuggingCharScanner;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v0, p1, p0, p2}, Lantlr/debug/ParserEventSupport;->fireExitRule(III)V

    :cond_0
    return-void
.end method

.method public fireSemanticPredicateEvaluated(IIZ)Z
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/DebuggingCharScanner;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v0, p1, p2, p3, p0}, Lantlr/debug/ParserEventSupport;->fireSemanticPredicateEvaluated(IIZI)Z

    move-result p0

    return p0

    :cond_0
    return p3
.end method

.method public fireSyntacticPredicateFailed()V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/DebuggingCharScanner;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v0, p0}, Lantlr/debug/ParserEventSupport;->fireSyntacticPredicateFailed(I)V

    :cond_0
    return-void
.end method

.method public fireSyntacticPredicateStarted()V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/DebuggingCharScanner;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v0, p0}, Lantlr/debug/ParserEventSupport;->fireSyntacticPredicateStarted(I)V

    :cond_0
    return-void
.end method

.method public fireSyntacticPredicateSucceeded()V
    .locals 1

    invoke-virtual {p0}, Lantlr/debug/DebuggingCharScanner;->isDebugMode()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v0, p0}, Lantlr/debug/ParserEventSupport;->fireSyntacticPredicateSucceeded(I)V

    :cond_0
    return-void
.end method

.method public getRuleName(I)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->ruleNames:[Ljava/lang/String;

    aget-object p0, p0, p1

    return-object p0
.end method

.method public getSemPredName(I)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->semPredNames:[Ljava/lang/String;

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

    iget-boolean p0, p0, Lantlr/debug/DebuggingCharScanner;->_notDebugMode:Z

    xor-int/lit8 p0, p0, 0x1

    return p0
.end method

.method public makeToken(I)Lantlr/Token;
    .locals 0

    invoke-super {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p0

    return-object p0
.end method

.method public match(C)V
    .locals 3

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/debug/DebuggingCharScanner;->LA(I)C

    move-result v0

    :try_start_0
    invoke-super {p0, p1}, Lantlr/CharScanner;->match(C)V

    iget-object v1, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object v2, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v2, v2, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v1, p1, v2}, Lantlr/debug/ParserEventSupport;->fireMatch(CI)V
    :try_end_0
    .catch Lantlr/MismatchedCharException; {:try_start_0 .. :try_end_0} :catch_0

    return-void

    :catch_0
    move-exception v1

    iget-object v2, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v2, v2, Lantlr/LexerSharedInputState;->guessing:I

    if-nez v2, :cond_0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, v0, p1, v2}, Lantlr/debug/ParserEventSupport;->fireMismatch(CCI)V

    :cond_0
    throw v1
.end method

.method public match(Lantlr/collections/impl/BitSet;)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->toString()Ljava/lang/String;

    move-result-object v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/debug/DebuggingCharScanner;->LA(I)C

    move-result v1

    :try_start_0
    invoke-super {p0, p1}, Lantlr/CharScanner;->match(Lantlr/collections/impl/BitSet;)V

    iget-object v2, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object v3, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v3, v3, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v2, v1, p1, v0, v3}, Lantlr/debug/ParserEventSupport;->fireMatch(ILantlr/collections/impl/BitSet;Ljava/lang/String;I)V
    :try_end_0
    .catch Lantlr/MismatchedCharException; {:try_start_0 .. :try_end_0} :catch_0

    return-void

    :catch_0
    move-exception v2

    iget-object v3, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v3, v3, Lantlr/LexerSharedInputState;->guessing:I

    if-nez v3, :cond_0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, v1, p1, v0, v3}, Lantlr/debug/ParserEventSupport;->fireMismatch(ILantlr/collections/impl/BitSet;Ljava/lang/String;I)V

    :cond_0
    throw v2
.end method

.method public match(Ljava/lang/String;)V
    .locals 4

    new-instance v0, Ljava/lang/StringBuffer;

    const-string v1, ""

    invoke-direct {v0, v1}, Ljava/lang/StringBuffer;-><init>(Ljava/lang/String;)V

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v1

    const/4 v2, 0x1

    :goto_0
    if-gt v2, v1, :cond_0

    :try_start_0
    invoke-super {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :catch_0
    :cond_0
    :try_start_1
    invoke-super {p0, p1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object v2, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v2, v2, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v1, p1, v2}, Lantlr/debug/ParserEventSupport;->fireMatch(Ljava/lang/String;I)V
    :try_end_1
    .catch Lantlr/MismatchedCharException; {:try_start_1 .. :try_end_1} :catch_1

    return-void

    :catch_1
    move-exception v1

    iget-object v2, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v2, v2, Lantlr/LexerSharedInputState;->guessing:I

    if-nez v2, :cond_1

    iget-object v2, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v2, v0, p1, p0}, Lantlr/debug/ParserEventSupport;->fireMismatch(Ljava/lang/String;Ljava/lang/String;I)V

    :cond_1
    throw v1
.end method

.method public matchNot(C)V
    .locals 3

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/debug/DebuggingCharScanner;->LA(I)C

    move-result v0

    :try_start_0
    invoke-super {p0, p1}, Lantlr/CharScanner;->matchNot(C)V

    iget-object v1, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    iget-object v2, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v2, v2, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v1, v0, p1, v2}, Lantlr/debug/ParserEventSupport;->fireMatchNot(CCI)V
    :try_end_0
    .catch Lantlr/MismatchedCharException; {:try_start_0 .. :try_end_0} :catch_0

    return-void

    :catch_0
    move-exception v1

    iget-object v2, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v2, v2, Lantlr/LexerSharedInputState;->guessing:I

    if-nez v2, :cond_0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, v0, p1, v2}, Lantlr/debug/ParserEventSupport;->fireMismatchNot(CCI)V

    :cond_0
    throw v1
.end method

.method public matchRange(CC)V
    .locals 5

    const-string v0, ""

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/debug/DebuggingCharScanner;->LA(I)C

    move-result v1

    :try_start_0
    invoke-super {p0, p1, p2}, Lantlr/CharScanner;->matchRange(CC)V

    iget-object v2, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p2}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    iget-object v4, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v4, v4, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v2, v1, v3, v4}, Lantlr/debug/ParserEventSupport;->fireMatch(CLjava/lang/String;I)V
    :try_end_0
    .catch Lantlr/MismatchedCharException; {:try_start_0 .. :try_end_0} :catch_0

    return-void

    :catch_0
    move-exception v2

    iget-object v3, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v3, v3, Lantlr/LexerSharedInputState;->guessing:I

    if-nez v3, :cond_0

    iget-object v3, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, p1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->guessing:I

    invoke-virtual {v3, v1, p1, p0}, Lantlr/debug/ParserEventSupport;->fireMismatch(CLjava/lang/String;I)V

    :cond_0
    throw v2
.end method

.method public newline()V
    .locals 1

    invoke-super {p0}, Lantlr/CharScanner;->newline()V

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result p0

    invoke-virtual {v0, p0}, Lantlr/debug/ParserEventSupport;->fireNewLine(I)V

    return-void
.end method

.method public removeMessageListener(Lantlr/debug/MessageListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeMessageListener(Lantlr/debug/MessageListener;)V

    return-void
.end method

.method public removeNewLineListener(Lantlr/debug/NewLineListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeNewLineListener(Lantlr/debug/NewLineListener;)V

    return-void
.end method

.method public removeParserListener(Lantlr/debug/ParserListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeParserListener(Lantlr/debug/ParserListener;)V

    return-void
.end method

.method public removeParserMatchListener(Lantlr/debug/ParserMatchListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeParserMatchListener(Lantlr/debug/ParserMatchListener;)V

    return-void
.end method

.method public removeParserTokenListener(Lantlr/debug/ParserTokenListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeParserTokenListener(Lantlr/debug/ParserTokenListener;)V

    return-void
.end method

.method public removeSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeSemanticPredicateListener(Lantlr/debug/SemanticPredicateListener;)V

    return-void
.end method

.method public removeSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeSyntacticPredicateListener(Lantlr/debug/SyntacticPredicateListener;)V

    return-void
.end method

.method public removeTraceListener(Lantlr/debug/TraceListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/ParserEventSupport;->removeTraceListener(Lantlr/debug/TraceListener;)V

    return-void
.end method

.method public reportError(Lantlr/MismatchedCharException;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {v0, p1}, Lantlr/debug/ParserEventSupport;->fireReportError(Ljava/lang/Exception;)V

    invoke-super {p0, p1}, Lantlr/CharScanner;->reportError(Lantlr/RecognitionException;)V

    return-void
.end method

.method public reportError(Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {v0, p1}, Lantlr/debug/ParserEventSupport;->fireReportError(Ljava/lang/String;)V

    invoke-super {p0, p1}, Lantlr/CharScanner;->reportError(Ljava/lang/String;)V

    return-void
.end method

.method public reportWarning(Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/DebuggingCharScanner;->parserEventSupport:Lantlr/debug/ParserEventSupport;

    invoke-virtual {v0, p1}, Lantlr/debug/ParserEventSupport;->fireReportWarning(Ljava/lang/String;)V

    invoke-super {p0, p1}, Lantlr/CharScanner;->reportWarning(Ljava/lang/String;)V

    return-void
.end method

.method public setDebugMode(Z)V
    .locals 0

    xor-int/lit8 p1, p1, 0x1

    iput-boolean p1, p0, Lantlr/debug/DebuggingCharScanner;->_notDebugMode:Z

    return-void
.end method

.method public setupDebugging()V
    .locals 0

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
