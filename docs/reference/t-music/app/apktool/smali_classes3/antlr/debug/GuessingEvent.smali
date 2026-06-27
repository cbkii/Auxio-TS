.class public abstract Lantlr/debug/GuessingEvent;
.super Lantlr/debug/Event;
.source ""


# instance fields
.field public guessing:I


# direct methods
.method public constructor <init>(Ljava/lang/Object;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lantlr/debug/Event;-><init>(Ljava/lang/Object;I)V

    return-void
.end method


# virtual methods
.method public getGuessing()I
    .locals 0

    iget p0, p0, Lantlr/debug/GuessingEvent;->guessing:I

    return p0
.end method

.method public setGuessing(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/GuessingEvent;->guessing:I

    return-void
.end method

.method public setValues(II)V
    .locals 0

    invoke-super {p0, p1}, Lantlr/debug/Event;->setValues(I)V

    invoke-virtual {p0, p2}, Lantlr/debug/GuessingEvent;->setGuessing(I)V

    return-void
.end method
