.class public Lantlr/debug/DebuggingInputBuffer;
.super Lantlr/InputBuffer;
.source ""


# instance fields
.field public buffer:Lantlr/InputBuffer;

.field public debugMode:Z

.field public inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;


# direct methods
.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    invoke-direct {p0}, Lantlr/InputBuffer;-><init>()V

    const/4 v0, 0x1

    iput-boolean v0, p0, Lantlr/debug/DebuggingInputBuffer;->debugMode:Z

    iput-object p1, p0, Lantlr/debug/DebuggingInputBuffer;->buffer:Lantlr/InputBuffer;

    new-instance p1, Lantlr/debug/InputBufferEventSupport;

    invoke-direct {p1, p0}, Lantlr/debug/InputBufferEventSupport;-><init>(Ljava/lang/Object;)V

    iput-object p1, p0, Lantlr/debug/DebuggingInputBuffer;->inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;

    return-void
.end method


# virtual methods
.method public LA(I)C
    .locals 2

    iget-object v0, p0, Lantlr/debug/DebuggingInputBuffer;->buffer:Lantlr/InputBuffer;

    invoke-virtual {v0, p1}, Lantlr/InputBuffer;->LA(I)C

    move-result v0

    iget-boolean v1, p0, Lantlr/debug/DebuggingInputBuffer;->debugMode:Z

    if-eqz v1, :cond_0

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;

    invoke-virtual {p0, v0, p1}, Lantlr/debug/InputBufferEventSupport;->fireLA(CI)V

    :cond_0
    return v0
.end method

.method public addInputBufferListener(Lantlr/debug/InputBufferListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/InputBufferEventSupport;->addInputBufferListener(Lantlr/debug/InputBufferListener;)V

    return-void
.end method

.method public consume()V
    .locals 2

    :try_start_0
    iget-object v0, p0, Lantlr/debug/DebuggingInputBuffer;->buffer:Lantlr/InputBuffer;

    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lantlr/InputBuffer;->LA(I)C

    move-result v0
    :try_end_0
    .catch Lantlr/CharStreamException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    const/16 v0, 0x20

    :goto_0
    iget-object v1, p0, Lantlr/debug/DebuggingInputBuffer;->buffer:Lantlr/InputBuffer;

    invoke-virtual {v1}, Lantlr/InputBuffer;->consume()V

    iget-boolean v1, p0, Lantlr/debug/DebuggingInputBuffer;->debugMode:Z

    if-eqz v1, :cond_0

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;

    invoke-virtual {p0, v0}, Lantlr/debug/InputBufferEventSupport;->fireConsume(C)V

    :cond_0
    return-void
.end method

.method public fill(I)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->buffer:Lantlr/InputBuffer;

    invoke-virtual {p0, p1}, Lantlr/InputBuffer;->fill(I)V

    return-void
.end method

.method public getInputBufferListeners()Ljava/util/Vector;
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;

    invoke-virtual {p0}, Lantlr/debug/InputBufferEventSupport;->getInputBufferListeners()Ljava/util/Vector;

    move-result-object p0

    return-object p0
.end method

.method public isDebugMode()Z
    .locals 0

    iget-boolean p0, p0, Lantlr/debug/DebuggingInputBuffer;->debugMode:Z

    return p0
.end method

.method public isMarked()Z
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->buffer:Lantlr/InputBuffer;

    invoke-virtual {p0}, Lantlr/InputBuffer;->isMarked()Z

    move-result p0

    return p0
.end method

.method public mark()I
    .locals 1

    iget-object v0, p0, Lantlr/debug/DebuggingInputBuffer;->buffer:Lantlr/InputBuffer;

    invoke-virtual {v0}, Lantlr/InputBuffer;->mark()I

    move-result v0

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;

    invoke-virtual {p0, v0}, Lantlr/debug/InputBufferEventSupport;->fireMark(I)V

    return v0
.end method

.method public removeInputBufferListener(Lantlr/debug/InputBufferListener;)V
    .locals 0

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1}, Lantlr/debug/InputBufferEventSupport;->removeInputBufferListener(Lantlr/debug/InputBufferListener;)V

    :cond_0
    return-void
.end method

.method public rewind(I)V
    .locals 1

    iget-object v0, p0, Lantlr/debug/DebuggingInputBuffer;->buffer:Lantlr/InputBuffer;

    invoke-virtual {v0, p1}, Lantlr/InputBuffer;->rewind(I)V

    iget-object p0, p0, Lantlr/debug/DebuggingInputBuffer;->inputBufferEventSupport:Lantlr/debug/InputBufferEventSupport;

    invoke-virtual {p0, p1}, Lantlr/debug/InputBufferEventSupport;->fireRewind(I)V

    return-void
.end method

.method public setDebugMode(Z)V
    .locals 0

    iput-boolean p1, p0, Lantlr/debug/DebuggingInputBuffer;->debugMode:Z

    return-void
.end method
