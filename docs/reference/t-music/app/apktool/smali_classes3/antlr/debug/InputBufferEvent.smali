.class public Lantlr/debug/InputBufferEvent;
.super Lantlr/debug/Event;
.source ""


# static fields
.field public static final CONSUME:I = 0x0

.field public static final LA:I = 0x1

.field public static final MARK:I = 0x2

.field public static final REWIND:I = 0x3


# instance fields
.field public c:C

.field public lookaheadAmount:I


# direct methods
.method public constructor <init>(Ljava/lang/Object;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;ICI)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;)V

    invoke-virtual {p0, p2, p3, p4}, Lantlr/debug/InputBufferEvent;->setValues(ICI)V

    return-void
.end method


# virtual methods
.method public getChar()C
    .locals 0

    iget-char p0, p0, Lantlr/debug/InputBufferEvent;->c:C

    return p0
.end method

.method public getLookaheadAmount()I
    .locals 0

    iget p0, p0, Lantlr/debug/InputBufferEvent;->lookaheadAmount:I

    return p0
.end method

.method public setChar(C)V
    .locals 0

    iput-char p1, p0, Lantlr/debug/InputBufferEvent;->c:C

    return-void
.end method

.method public setLookaheadAmount(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/InputBufferEvent;->lookaheadAmount:I

    return-void
.end method

.method public setValues(ICI)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/debug/Event;->setValues(I)V

    invoke-virtual {p0, p2}, Lantlr/debug/InputBufferEvent;->setChar(C)V

    invoke-virtual {p0, p3}, Lantlr/debug/InputBufferEvent;->setLookaheadAmount(I)V

    return-void
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    const-string v0, "CharBufferEvent ["

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lantlr/debug/Event;->getType()I

    move-result v1

    if-nez v1, :cond_0

    const-string v1, "CONSUME, "

    goto :goto_0

    :cond_0
    const-string v1, "LA, "

    :goto_0
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/debug/InputBufferEvent;->getChar()C

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lantlr/debug/InputBufferEvent;->getLookaheadAmount()I

    move-result p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, "]"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
