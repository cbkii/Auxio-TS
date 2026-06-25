.class public abstract Lantlr/CharScanner;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/TokenStream;


# static fields
.field public static final EOF_CHAR:C = '\uffff'

.field public static final NO_CHAR:C


# instance fields
.field public _returnToken:Lantlr/Token;

.field public caseSensitive:Z

.field public caseSensitiveLiterals:Z

.field public commitToPath:Z

.field public hashString:Lantlr/ANTLRHashString;

.field public inputState:Lantlr/LexerSharedInputState;

.field public literals:Ljava/util/Hashtable;

.field public saveConsumedInput:Z

.field public tabsize:I

.field public text:Lantlr/ANTLRStringBuffer;

.field public tokenObjectClass:Ljava/lang/Class;

.field public traceDepth:I


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/CharScanner;->saveConsumedInput:Z

    iput-boolean v0, p0, Lantlr/CharScanner;->caseSensitive:Z

    iput-boolean v0, p0, Lantlr/CharScanner;->caseSensitiveLiterals:Z

    const/16 v0, 0x8

    iput v0, p0, Lantlr/CharScanner;->tabsize:I

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    const/4 v0, 0x0

    iput-boolean v0, p0, Lantlr/CharScanner;->commitToPath:Z

    iput v0, p0, Lantlr/CharScanner;->traceDepth:I

    new-instance v0, Lantlr/ANTLRStringBuffer;

    invoke-direct {v0}, Lantlr/ANTLRStringBuffer;-><init>()V

    iput-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    new-instance v0, Lantlr/ANTLRHashString;

    invoke-direct {v0, p0}, Lantlr/ANTLRHashString;-><init>(Lantlr/CharScanner;)V

    iput-object v0, p0, Lantlr/CharScanner;->hashString:Lantlr/ANTLRHashString;

    const-string v0, "antlr.CommonToken"

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->setTokenObjectClass(Ljava/lang/String;)V

    return-void
.end method

.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    invoke-direct {p0}, Lantlr/CharScanner;-><init>()V

    new-instance v0, Lantlr/LexerSharedInputState;

    invoke-direct {v0, p1}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    iput-object v0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    return-void
.end method

.method public constructor <init>(Lantlr/LexerSharedInputState;)V
    .locals 0

    invoke-direct {p0}, Lantlr/CharScanner;-><init>()V

    iput-object p1, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    return-void
.end method


# virtual methods
.method public LA(I)C
    .locals 1

    iget-boolean v0, p0, Lantlr/CharScanner;->caseSensitive:Z

    if-eqz v0, :cond_0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object p0, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    invoke-virtual {p0, p1}, Lantlr/InputBuffer;->LA(I)C

    move-result p0

    return p0

    :cond_0
    iget-object v0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object v0, v0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    invoke-virtual {v0, p1}, Lantlr/InputBuffer;->LA(I)C

    move-result p1

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->toLower(C)C

    move-result p0

    return p0
.end method

.method public append(C)V
    .locals 1

    iget-boolean v0, p0, Lantlr/CharScanner;->saveConsumedInput:Z

    if-eqz v0, :cond_0

    iget-object p0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p0, p1}, Lantlr/ANTLRStringBuffer;->append(C)V

    :cond_0
    return-void
.end method

.method public append(Ljava/lang/String;)V
    .locals 1

    iget-boolean v0, p0, Lantlr/CharScanner;->saveConsumedInput:Z

    if-eqz v0, :cond_0

    iget-object p0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p0, p1}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    :cond_0
    return-void
.end method

.method public commit()V
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object p0, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    invoke-virtual {p0}, Lantlr/InputBuffer;->commit()V

    return-void
.end method

.method public consume()V
    .locals 3

    iget-object v0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v0, v0, Lantlr/LexerSharedInputState;->guessing:I

    if-nez v0, :cond_2

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    iget-boolean v2, p0, Lantlr/CharScanner;->caseSensitive:Z

    if-eqz v2, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->append(C)V

    goto :goto_0

    :cond_0
    iget-object v2, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object v2, v2, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    invoke-virtual {v2, v0}, Lantlr/InputBuffer;->LA(I)C

    move-result v2

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->append(C)V

    :goto_0
    const/16 v2, 0x9

    if-ne v1, v2, :cond_1

    invoke-virtual {p0}, Lantlr/CharScanner;->tab()V

    goto :goto_1

    :cond_1
    iget-object v1, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v2, v1, Lantlr/LexerSharedInputState;->column:I

    add-int/2addr v2, v0

    iput v2, v1, Lantlr/LexerSharedInputState;->column:I

    :cond_2
    :goto_1
    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object p0, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    invoke-virtual {p0}, Lantlr/InputBuffer;->consume()V

    return-void
.end method

.method public consumeUntil(I)V
    .locals 3

    :goto_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const v2, 0xffff

    if-eq v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    if-eq v0, p1, :cond_0

    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V

    goto :goto_0

    :cond_0
    return-void
.end method

.method public consumeUntil(Lantlr/collections/impl/BitSet;)V
    .locals 3

    :goto_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const v2, 0xffff

    if-eq v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p1, v0}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v0

    if-nez v0, :cond_0

    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V

    goto :goto_0

    :cond_0
    return-void
.end method

.method public getCaseSensitive()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/CharScanner;->caseSensitive:Z

    return p0
.end method

.method public final getCaseSensitiveLiterals()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/CharScanner;->caseSensitiveLiterals:Z

    return p0
.end method

.method public getColumn()I
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->column:I

    return p0
.end method

.method public getCommitToPath()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/CharScanner;->commitToPath:Z

    return p0
.end method

.method public getFilename()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object p0, p0, Lantlr/LexerSharedInputState;->filename:Ljava/lang/String;

    return-object p0
.end method

.method public getInputBuffer()Lantlr/InputBuffer;
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object p0, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    return-object p0
.end method

.method public getInputState()Lantlr/LexerSharedInputState;
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    return-object p0
.end method

.method public getLine()I
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p0, p0, Lantlr/LexerSharedInputState;->line:I

    return p0
.end method

.method public getTabSize()I
    .locals 0

    iget p0, p0, Lantlr/CharScanner;->tabsize:I

    return p0
.end method

.method public getText()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p0}, Lantlr/ANTLRStringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getTokenObject()Lantlr/Token;
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-object p0
.end method

.method public makeToken(I)Lantlr/Token;
    .locals 1

    :try_start_0
    iget-object v0, p0, Lantlr/CharScanner;->tokenObjectClass:Ljava/lang/Class;

    invoke-virtual {v0}, Ljava/lang/Class;->newInstance()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/Token;

    invoke-virtual {v0, p1}, Lantlr/Token;->setType(I)V

    iget-object p1, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p1, p1, Lantlr/LexerSharedInputState;->tokenStartColumn:I

    invoke-virtual {v0, p1}, Lantlr/Token;->setColumn(I)V

    iget-object p1, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget p1, p1, Lantlr/LexerSharedInputState;->tokenStartLine:I

    invoke-virtual {v0, p1}, Lantlr/Token;->setLine(I)V
    :try_end_0
    .catch Ljava/lang/InstantiationException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Ljava/lang/IllegalAccessException; {:try_start_0 .. :try_end_0} :catch_0

    return-object v0

    :catch_0
    const-string p1, "Token class is not accessible"

    goto :goto_0

    :catch_1
    const-string p1, "can\'t instantiate token: "

    :goto_0
    invoke-static {p1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    iget-object v0, p0, Lantlr/CharScanner;->tokenObjectClass:Ljava/lang/Class;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->panic(Ljava/lang/String;)V

    sget-object p0, Lantlr/Token;->badToken:Lantlr/Token;

    return-object p0
.end method

.method public mark()I
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object p0, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    invoke-virtual {p0}, Lantlr/InputBuffer;->mark()I

    move-result p0

    return p0
.end method

.method public match(C)V
    .locals 3

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, p1, :cond_0

    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V

    return-void

    :cond_0
    new-instance v1, Lantlr/MismatchedCharException;

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    const/4 v2, 0x0

    invoke-direct {v1, v0, p1, v2, p0}, Lantlr/MismatchedCharException;-><init>(CCZLantlr/CharScanner;)V

    throw v1
.end method

.method public match(Lantlr/collections/impl/BitSet;)V
    .locals 3

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    invoke-virtual {p1, v1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V

    return-void

    :cond_0
    new-instance v1, Lantlr/MismatchedCharException;

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    const/4 v2, 0x0

    invoke-direct {v1, v0, p1, v2, p0}, Lantlr/MismatchedCharException;-><init>(CLantlr/collections/impl/BitSet;ZLantlr/CharScanner;)V

    throw v1
.end method

.method public match(Ljava/lang/String;)V
    .locals 6

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v0

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    if-ge v2, v0, :cond_1

    const/4 v3, 0x1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {p1, v2}, Ljava/lang/String;->charAt(I)C

    move-result v5

    if-ne v4, v5, :cond_0

    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_0
    new-instance v0, Lantlr/MismatchedCharException;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {p1, v2}, Ljava/lang/String;->charAt(I)C

    move-result p1

    invoke-direct {v0, v3, p1, v1, p0}, Lantlr/MismatchedCharException;-><init>(CCZLantlr/CharScanner;)V

    throw v0

    :cond_1
    return-void
.end method

.method public matchNot(C)V
    .locals 3

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-eq v1, p1, :cond_0

    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V

    return-void

    :cond_0
    new-instance v1, Lantlr/MismatchedCharException;

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-direct {v1, v2, p1, v0, p0}, Lantlr/MismatchedCharException;-><init>(CCZLantlr/CharScanner;)V

    throw v1
.end method

.method public matchRange(CC)V
    .locals 8

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, p1, :cond_0

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, p2, :cond_0

    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V

    return-void

    :cond_0
    new-instance v1, Lantlr/MismatchedCharException;

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/4 v6, 0x0

    move-object v2, v1

    move v4, p1

    move v5, p2

    move-object v7, p0

    invoke-direct/range {v2 .. v7}, Lantlr/MismatchedCharException;-><init>(CCCZLantlr/CharScanner;)V

    throw v1
.end method

.method public newline()V
    .locals 2

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v0, p0, Lantlr/LexerSharedInputState;->line:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/LexerSharedInputState;->line:I

    iput v1, p0, Lantlr/LexerSharedInputState;->column:I

    return-void
.end method

.method public panic()V
    .locals 1

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    const-string v0, "CharScanner: panic"

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    const-string p0, ""

    invoke-static {p0}, Lantlr/Utils;->error(Ljava/lang/String;)V

    const/4 p0, 0x0

    throw p0
.end method

.method public panic(Ljava/lang/String;)V
    .locals 2

    sget-object p0, Ljava/lang/System;->err:Ljava/io/PrintStream;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "CharScanner; panic: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    invoke-static {p1}, Lantlr/Utils;->error(Ljava/lang/String;)V

    const/4 p0, 0x0

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

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

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

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

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

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

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

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

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

.method public resetText()V
    .locals 2

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget v0, p0, Lantlr/LexerSharedInputState;->column:I

    iput v0, p0, Lantlr/LexerSharedInputState;->tokenStartColumn:I

    iget v0, p0, Lantlr/LexerSharedInputState;->line:I

    iput v0, p0, Lantlr/LexerSharedInputState;->tokenStartLine:I

    return-void
.end method

.method public rewind(I)V
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iget-object p0, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    invoke-virtual {p0, p1}, Lantlr/InputBuffer;->rewind(I)V

    return-void
.end method

.method public setCaseSensitive(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/CharScanner;->caseSensitive:Z

    return-void
.end method

.method public setColumn(I)V
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iput p1, p0, Lantlr/LexerSharedInputState;->column:I

    return-void
.end method

.method public setCommitToPath(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/CharScanner;->commitToPath:Z

    return-void
.end method

.method public setFilename(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iput-object p1, p0, Lantlr/LexerSharedInputState;->filename:Ljava/lang/String;

    return-void
.end method

.method public setInputState(Lantlr/LexerSharedInputState;)V
    .locals 0

    iput-object p1, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    return-void
.end method

.method public setLine(I)V
    .locals 0

    iget-object p0, p0, Lantlr/CharScanner;->inputState:Lantlr/LexerSharedInputState;

    iput p1, p0, Lantlr/LexerSharedInputState;->line:I

    return-void
.end method

.method public setTabSize(I)V
    .locals 0

    iput p1, p0, Lantlr/CharScanner;->tabsize:I

    return-void
.end method

.method public setText(Ljava/lang/String;)V
    .locals 0

    invoke-virtual {p0}, Lantlr/CharScanner;->resetText()V

    iget-object p0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p0, p1}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    return-void
.end method

.method public setTokenObjectClass(Ljava/lang/String;)V
    .locals 2

    :try_start_0
    invoke-static {p1}, Lantlr/Utils;->loadClass(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    iput-object v0, p0, Lantlr/CharScanner;->tokenObjectClass:Ljava/lang/Class;
    :try_end_0
    .catch Ljava/lang/ClassNotFoundException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "ClassNotFoundException: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->panic(Ljava/lang/String;)V

    :goto_0
    return-void
.end method

.method public tab()V
    .locals 2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    iget v1, p0, Lantlr/CharScanner;->tabsize:I

    div-int/2addr v0, v1

    add-int/lit8 v0, v0, 0x1

    mul-int/2addr v0, v1

    add-int/lit8 v0, v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->setColumn(I)V

    return-void
.end method

.method public testLiteralsTable(I)I
    .locals 3

    iget-object v0, p0, Lantlr/CharScanner;->hashString:Lantlr/ANTLRHashString;

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v1

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {v0, v1, v2}, Lantlr/ANTLRHashString;->setBuffer([CI)V

    iget-object v0, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    iget-object p0, p0, Lantlr/CharScanner;->hashString:Lantlr/ANTLRHashString;

    invoke-virtual {v0, p0}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Integer;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p1

    :cond_0
    return p1
.end method

.method public testLiteralsTable(Ljava/lang/String;I)I
    .locals 1

    new-instance v0, Lantlr/ANTLRHashString;

    invoke-direct {v0, p1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    iget-object p0, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    invoke-virtual {p0, v0}, Ljava/util/Hashtable;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Integer;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p2

    :cond_0
    return p2
.end method

.method public toLower(C)C
    .locals 0

    invoke-static {p1}, Ljava/lang/Character;->toLowerCase(C)C

    move-result p0

    return p0
.end method

.method public traceIn(Ljava/lang/String;)V
    .locals 4

    iget v0, p0, Lantlr/CharScanner;->traceDepth:I

    const/4 v1, 0x1

    add-int/2addr v0, v1

    iput v0, p0, Lantlr/CharScanner;->traceDepth:I

    invoke-virtual {p0}, Lantlr/CharScanner;->traceIndent()V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v2, "> lexer "

    const-string v3, "; c=="

    invoke-static {v2, p1, v3}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public traceIndent()V
    .locals 3

    const/4 v0, 0x0

    :goto_0
    iget v1, p0, Lantlr/CharScanner;->traceDepth:I

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

    invoke-virtual {p0}, Lantlr/CharScanner;->traceIndent()V

    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v1, "< lexer "

    const-string v2, "; c=="

    invoke-static {v1, p1, v2}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {v0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    iget p1, p0, Lantlr/CharScanner;->traceDepth:I

    sub-int/2addr p1, v1

    iput p1, p0, Lantlr/CharScanner;->traceDepth:I

    return-void
.end method

.method public uponEOF()V
    .locals 0

    return-void
.end method
