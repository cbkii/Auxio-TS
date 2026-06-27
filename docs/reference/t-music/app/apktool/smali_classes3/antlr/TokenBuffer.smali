.class public Lantlr/TokenBuffer;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public input:Lantlr/TokenStream;

.field public markerOffset:I

.field public nMarkers:I

.field public numToConsume:I

.field public queue:Lantlr/TokenQueue;


# direct methods
.method public constructor <init>(Lantlr/TokenStream;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Lantlr/TokenBuffer;->nMarkers:I

    iput v0, p0, Lantlr/TokenBuffer;->markerOffset:I

    iput v0, p0, Lantlr/TokenBuffer;->numToConsume:I

    iput-object p1, p0, Lantlr/TokenBuffer;->input:Lantlr/TokenStream;

    new-instance p1, Lantlr/TokenQueue;

    const/4 v0, 0x1

    invoke-direct {p1, v0}, Lantlr/TokenQueue;-><init>(I)V

    iput-object p1, p0, Lantlr/TokenBuffer;->queue:Lantlr/TokenQueue;

    return-void
.end method

.method private final fill(I)V
    .locals 3

    invoke-direct {p0}, Lantlr/TokenBuffer;->syncConsume()V

    :goto_0
    iget-object v0, p0, Lantlr/TokenBuffer;->queue:Lantlr/TokenQueue;

    iget v1, v0, Lantlr/TokenQueue;->nbrEntries:I

    iget v2, p0, Lantlr/TokenBuffer;->markerOffset:I

    add-int/2addr v2, p1

    if-ge v1, v2, :cond_0

    iget-object v1, p0, Lantlr/TokenBuffer;->input:Lantlr/TokenStream;

    invoke-interface {v1}, Lantlr/TokenStream;->nextToken()Lantlr/Token;

    move-result-object v1

    invoke-virtual {v0, v1}, Lantlr/TokenQueue;->append(Lantlr/Token;)V

    goto :goto_0

    :cond_0
    return-void
.end method

.method private final syncConsume()V
    .locals 1

    :goto_0
    iget v0, p0, Lantlr/TokenBuffer;->numToConsume:I

    if-lez v0, :cond_1

    iget v0, p0, Lantlr/TokenBuffer;->nMarkers:I

    if-lez v0, :cond_0

    iget v0, p0, Lantlr/TokenBuffer;->markerOffset:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/TokenBuffer;->markerOffset:I

    goto :goto_1

    :cond_0
    iget-object v0, p0, Lantlr/TokenBuffer;->queue:Lantlr/TokenQueue;

    invoke-virtual {v0}, Lantlr/TokenQueue;->removeFirst()V

    :goto_1
    iget v0, p0, Lantlr/TokenBuffer;->numToConsume:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/TokenBuffer;->numToConsume:I

    goto :goto_0

    :cond_1
    return-void
.end method


# virtual methods
.method public final LA(I)I
    .locals 1

    invoke-direct {p0, p1}, Lantlr/TokenBuffer;->fill(I)V

    iget-object v0, p0, Lantlr/TokenBuffer;->queue:Lantlr/TokenQueue;

    iget p0, p0, Lantlr/TokenBuffer;->markerOffset:I

    add-int/2addr p0, p1

    add-int/lit8 p0, p0, -0x1

    invoke-virtual {v0, p0}, Lantlr/TokenQueue;->elementAt(I)Lantlr/Token;

    move-result-object p0

    invoke-virtual {p0}, Lantlr/Token;->getType()I

    move-result p0

    return p0
.end method

.method public final LT(I)Lantlr/Token;
    .locals 1

    invoke-direct {p0, p1}, Lantlr/TokenBuffer;->fill(I)V

    iget-object v0, p0, Lantlr/TokenBuffer;->queue:Lantlr/TokenQueue;

    iget p0, p0, Lantlr/TokenBuffer;->markerOffset:I

    add-int/2addr p0, p1

    add-int/lit8 p0, p0, -0x1

    invoke-virtual {v0, p0}, Lantlr/TokenQueue;->elementAt(I)Lantlr/Token;

    move-result-object p0

    return-object p0
.end method

.method public final consume()V
    .locals 1

    iget v0, p0, Lantlr/TokenBuffer;->numToConsume:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/TokenBuffer;->numToConsume:I

    return-void
.end method

.method public getInput()Lantlr/TokenStream;
    .locals 0

    iget-object p0, p0, Lantlr/TokenBuffer;->input:Lantlr/TokenStream;

    return-object p0
.end method

.method public final mark()I
    .locals 1

    invoke-direct {p0}, Lantlr/TokenBuffer;->syncConsume()V

    iget v0, p0, Lantlr/TokenBuffer;->nMarkers:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lantlr/TokenBuffer;->nMarkers:I

    iget p0, p0, Lantlr/TokenBuffer;->markerOffset:I

    return p0
.end method

.method public final reset()V
    .locals 1

    const/4 v0, 0x0

    iput v0, p0, Lantlr/TokenBuffer;->nMarkers:I

    iput v0, p0, Lantlr/TokenBuffer;->markerOffset:I

    iput v0, p0, Lantlr/TokenBuffer;->numToConsume:I

    iget-object p0, p0, Lantlr/TokenBuffer;->queue:Lantlr/TokenQueue;

    invoke-virtual {p0}, Lantlr/TokenQueue;->reset()V

    return-void
.end method

.method public final rewind(I)V
    .locals 0

    invoke-direct {p0}, Lantlr/TokenBuffer;->syncConsume()V

    iput p1, p0, Lantlr/TokenBuffer;->markerOffset:I

    iget p1, p0, Lantlr/TokenBuffer;->nMarkers:I

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/TokenBuffer;->nMarkers:I

    return-void
.end method
