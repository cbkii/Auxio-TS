.class public Lantlr/ParserSharedInputState;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public filename:Ljava/lang/String;

.field public guessing:I

.field public input:Lantlr/TokenBuffer;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/ParserSharedInputState;->guessing:I

    return-void
.end method


# virtual methods
.method public getFilename()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lantlr/ParserSharedInputState;->filename:Ljava/lang/String;

    return-object p0
.end method

.method public getInput()Lantlr/TokenBuffer;
    .locals 0

    iget-object p0, p0, Lantlr/ParserSharedInputState;->input:Lantlr/TokenBuffer;

    return-object p0
.end method

.method public reset()V
    .locals 1

    const/4 v0, 0x0

    iput v0, p0, Lantlr/ParserSharedInputState;->guessing:I

    const/4 v0, 0x0

    iput-object v0, p0, Lantlr/ParserSharedInputState;->filename:Ljava/lang/String;

    iget-object p0, p0, Lantlr/ParserSharedInputState;->input:Lantlr/TokenBuffer;

    invoke-virtual {p0}, Lantlr/TokenBuffer;->reset()V

    return-void
.end method
