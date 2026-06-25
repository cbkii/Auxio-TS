.class public Lantlr/LexerSharedInputState;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public column:I

.field public filename:Ljava/lang/String;

.field public guessing:I

.field public input:Lantlr/InputBuffer;

.field public line:I

.field public tokenStartColumn:I

.field public tokenStartLine:I


# direct methods
.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x1

    iput v0, p0, Lantlr/LexerSharedInputState;->column:I

    iput v0, p0, Lantlr/LexerSharedInputState;->line:I

    iput v0, p0, Lantlr/LexerSharedInputState;->tokenStartColumn:I

    iput v0, p0, Lantlr/LexerSharedInputState;->tokenStartLine:I

    const/4 v0, 0x0

    iput v0, p0, Lantlr/LexerSharedInputState;->guessing:I

    iput-object p1, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    return-void
.end method

.method public constructor <init>(Ljava/io/InputStream;)V
    .locals 1

    new-instance v0, Lantlr/ByteBuffer;

    invoke-direct {v0, p1}, Lantlr/ByteBuffer;-><init>(Ljava/io/InputStream;)V

    invoke-direct {p0, v0}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/Reader;)V
    .locals 1

    new-instance v0, Lantlr/CharBuffer;

    invoke-direct {v0, p1}, Lantlr/CharBuffer;-><init>(Ljava/io/Reader;)V

    invoke-direct {p0, v0}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method


# virtual methods
.method public getColumn()I
    .locals 0

    iget p0, p0, Lantlr/LexerSharedInputState;->column:I

    return p0
.end method

.method public getFilename()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/LexerSharedInputState;->filename:Ljava/lang/String;

    return-object p0
.end method

.method public getInput()Lantlr/InputBuffer;
    .locals 0

    iget-object p0, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    return-object p0
.end method

.method public getLine()I
    .locals 0

    iget p0, p0, Lantlr/LexerSharedInputState;->line:I

    return p0
.end method

.method public getTokenStartColumn()I
    .locals 0

    iget p0, p0, Lantlr/LexerSharedInputState;->tokenStartColumn:I

    return p0
.end method

.method public getTokenStartLine()I
    .locals 0

    iget p0, p0, Lantlr/LexerSharedInputState;->tokenStartLine:I

    return p0
.end method

.method public reset()V
    .locals 1

    const/4 v0, 0x1

    iput v0, p0, Lantlr/LexerSharedInputState;->column:I

    iput v0, p0, Lantlr/LexerSharedInputState;->line:I

    iput v0, p0, Lantlr/LexerSharedInputState;->tokenStartColumn:I

    iput v0, p0, Lantlr/LexerSharedInputState;->tokenStartLine:I

    const/4 v0, 0x0

    iput v0, p0, Lantlr/LexerSharedInputState;->guessing:I

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/LexerSharedInputState;->filename:Ljava/lang/String;

    iget-object p0, p0, Lantlr/LexerSharedInputState;->input:Lantlr/InputBuffer;

    invoke-virtual {p0}, Lantlr/InputBuffer;->reset()V

    return-void
.end method
