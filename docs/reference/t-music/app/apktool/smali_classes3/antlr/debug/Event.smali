.class public abstract Lantlr/debug/Event;
.super Ljava/util/EventObject;
.source ""


# instance fields
.field public type:I


# direct methods
.method public constructor <init>(Ljava/lang/Object;)V
    .locals 0

    invoke-direct {p0, p1}, Ljava/util/EventObject;-><init>(Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;I)V
    .locals 0

    invoke-direct {p0, p1}, Ljava/util/EventObject;-><init>(Ljava/lang/Object;)V

    invoke-virtual {p0, p2}, Lantlr/debug/Event;->setType(I)V

    return-void
.end method


# virtual methods
.method public getType()I
    .locals 0

    iget p0, p0, Lantlr/debug/Event;->type:I

    return p0
.end method

.method public setType(I)V
    .locals 0

    iput p1, p0, Lantlr/debug/Event;->type:I

    return-void
.end method

.method public setValues(I)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/debug/Event;->setType(I)V

    return-void
.end method
