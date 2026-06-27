.class public abstract Lantlr/InputBuffer;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public markerOffset:I

.field public nMarkers:I

.field public numToConsume:I

.field public queue:Lantlr/CharQueue;


# direct methods
.method public constructor <init>()V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/InputBuffer;->nMarkers:I

    iput v0, p0, Lantlr/InputBuffer;->markerOffset:I

    iput v0, p0, Lantlr/InputBuffer;->numToConsume:I

    new-instance v0, Lantlr/CharQueue;

    const/4 v1, 0x1

    invoke-direct {v0, v1}, Lantlr/CharQueue;-><init>(I)V

    iput-object v0, p0, Lantlr/InputBuffer;->queue:Lantlr/CharQueue;

    return-void
.end method


# virtual methods
.method public LA(I)C
    .locals 1

    invoke-virtual {p0, p1}, Lantlr/InputBuffer;->fill(I)V

    iget-object v0, p0, Lantlr/InputBuffer;->queue:Lantlr/CharQueue;

    iget p0, p0, Lantlr/InputBuffer;->markerOffset:I

    add-int/2addr p0, p1

    add-int/lit8 p0, p0, -0x1

    invoke-virtual {v0, p0}, Lantlr/CharQueue;->elementAt(I)C

    move-result p0

    return p0
.end method

.method public commit()V
    .locals 1

    iget v0, p0, Lantlr/InputBuffer;->nMarkers:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/InputBuffer;->nMarkers:I

    return-void
.end method

.method public consume()V
    .locals 1

    iget v0, p0, Lantlr/InputBuffer;->numToConsume:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/InputBuffer;->numToConsume:I

    return-void
.end method

.method public abstract fill(I)V
.end method

.method public getLAChars()Ljava/lang/String;
    .locals 4

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    iget v1, p0, Lantlr/InputBuffer;->markerOffset:I

    :goto_0
    iget-object v2, p0, Lantlr/InputBuffer;->queue:Lantlr/CharQueue;

    iget v3, v2, Lantlr/CharQueue;->nbrEntries:I

    if-ge v1, v3, :cond_0

    invoke-virtual {v2, v1}, Lantlr/CharQueue;->elementAt(I)C

    move-result v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getMarkedChars()Ljava/lang/String;
    .locals 3

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    const/4 v1, 0x0

    :goto_0
    iget v2, p0, Lantlr/InputBuffer;->markerOffset:I

    if-ge v1, v2, :cond_0

    iget-object v2, p0, Lantlr/InputBuffer;->queue:Lantlr/CharQueue;

    invoke-virtual {v2, v1}, Lantlr/CharQueue;->elementAt(I)C

    move-result v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(C)Ljava/lang/StringBuffer;

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public isMarked()Z
    .locals 0

    iget p0, p0, Lantlr/InputBuffer;->nMarkers:I

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public mark()I
    .locals 1

    invoke-virtual {p0}, Lantlr/InputBuffer;->syncConsume()V

    iget v0, p0, Lantlr/InputBuffer;->nMarkers:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/InputBuffer;->nMarkers:I

    iget p0, p0, Lantlr/InputBuffer;->markerOffset:I

    return p0
.end method

.method public reset()V
    .locals 1

    const/4 v0, 0x0

    iput v0, p0, Lantlr/InputBuffer;->nMarkers:I

    iput v0, p0, Lantlr/InputBuffer;->markerOffset:I

    iput v0, p0, Lantlr/InputBuffer;->numToConsume:I

    iget-object p0, p0, Lantlr/InputBuffer;->queue:Lantlr/CharQueue;

    invoke-virtual {p0}, Lantlr/CharQueue;->reset()V

    return-void
.end method

.method public rewind(I)V
    .locals 0

    invoke-virtual {p0}, Lantlr/InputBuffer;->syncConsume()V

    iput p1, p0, Lantlr/InputBuffer;->markerOffset:I

    iget p1, p0, Lantlr/InputBuffer;->nMarkers:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/InputBuffer;->nMarkers:I

    return-void
.end method

.method public syncConsume()V
    .locals 1

    :goto_0
    iget v0, p0, Lantlr/InputBuffer;->numToConsume:I

    if-lez v0, :cond_1

    iget v0, p0, Lantlr/InputBuffer;->nMarkers:I

    if-lez v0, :cond_0

    iget v0, p0, Lantlr/InputBuffer;->markerOffset:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/InputBuffer;->markerOffset:I

    goto :goto_1

    :cond_0
    iget-object v0, p0, Lantlr/InputBuffer;->queue:Lantlr/CharQueue;

    invoke-virtual {v0}, Lantlr/CharQueue;->removeFirst()V

    :goto_1
    iget v0, p0, Lantlr/InputBuffer;->numToConsume:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/InputBuffer;->numToConsume:I

    goto :goto_0

    :cond_1
    return-void
.end method
