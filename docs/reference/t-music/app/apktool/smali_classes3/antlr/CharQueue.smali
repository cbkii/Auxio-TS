.class public Lantlr/CharQueue;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public buffer:[C

.field public nbrEntries:I

.field public offset:I

.field public sizeLessOne:I


# direct methods
.method public constructor <init>(I)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    if-gez p1, :cond_0

    const/16 p1, 0x10

    :goto_0
    invoke-virtual {p0, p1}, Lantlr/CharQueue;->init(I)V

    return-void

    :cond_0
    const v0, 0x3fffffff    # 1.9999999f

    if-lt p1, v0, :cond_1

    const p1, 0x7fffffff

    goto :goto_0

    :cond_1
    const/4 v0, 0x2

    :goto_1
    if-ge v0, p1, :cond_2

    mul-int/lit8 v0, v0, 0x2

    goto :goto_1

    :cond_2
    invoke-virtual {p0, v0}, Lantlr/CharQueue;->init(I)V

    return-void
.end method

.method private final expand()V
    .locals 4

    iget-object v0, p0, Lantlr/CharQueue;->buffer:[C

    array-length v0, v0

    mul-int/lit8 v0, v0, 0x2

    new-array v0, v0, [C

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    iget-object v3, p0, Lantlr/CharQueue;->buffer:[C

    array-length v3, v3

    if-ge v2, v3, :cond_0

    invoke-virtual {p0, v2}, Lantlr/CharQueue;->elementAt(I)C

    move-result v3

    aput-char v3, v0, v2

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_0
    iput-object v0, p0, Lantlr/CharQueue;->buffer:[C

    iget-object v0, p0, Lantlr/CharQueue;->buffer:[C

    array-length v0, v0

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CharQueue;->sizeLessOne:I

    iput v1, p0, Lantlr/CharQueue;->offset:I

    return-void
.end method


# virtual methods
.method public final append(C)V
    .locals 4

    iget v0, p0, Lantlr/CharQueue;->nbrEntries:I

    iget-object v1, p0, Lantlr/CharQueue;->buffer:[C

    array-length v1, v1

    if-ne v0, v1, :cond_0

    invoke-direct {p0}, Lantlr/CharQueue;->expand()V

    :cond_0
    iget-object v0, p0, Lantlr/CharQueue;->buffer:[C

    iget v1, p0, Lantlr/CharQueue;->offset:I

    iget v2, p0, Lantlr/CharQueue;->nbrEntries:I

    add-int/2addr v1, v2

    iget v3, p0, Lantlr/CharQueue;->sizeLessOne:I

    and-int/2addr v1, v3

    aput-char p1, v0, v1

    add-int/lit8 v2, v2, 0x1

    iput v2, p0, Lantlr/CharQueue;->nbrEntries:I

    return-void
.end method

.method public final elementAt(I)C
    .locals 2

    iget-object v0, p0, Lantlr/CharQueue;->buffer:[C

    iget v1, p0, Lantlr/CharQueue;->offset:I

    add-int/2addr v1, p1

    iget p0, p0, Lantlr/CharQueue;->sizeLessOne:I

    and-int/2addr p0, v1

    aget-char p0, v0, p0

    return p0
.end method

.method public init(I)V
    .locals 1

    new-array v0, p1, [C

    iput-object v0, p0, Lantlr/CharQueue;->buffer:[C

    add-int/lit8 p1, p1, -0x1

    iput p1, p0, Lantlr/CharQueue;->sizeLessOne:I

    const/4 p1, 0x0

    iput p1, p0, Lantlr/CharQueue;->offset:I

    iput p1, p0, Lantlr/CharQueue;->nbrEntries:I

    return-void
.end method

.method public final removeFirst()V
    .locals 2

    iget v0, p0, Lantlr/CharQueue;->offset:I

    add-int/lit8 v0, v0, 0x1

    iget v1, p0, Lantlr/CharQueue;->sizeLessOne:I

    and-int/2addr v0, v1

    iput v0, p0, Lantlr/CharQueue;->offset:I

    iget v0, p0, Lantlr/CharQueue;->nbrEntries:I

    add-int/lit8 v0, v0, -0x1

    iput v0, p0, Lantlr/CharQueue;->nbrEntries:I

    return-void
.end method

.method public final reset()V
    .locals 1

    const/4 v0, 0x0

    iput v0, p0, Lantlr/CharQueue;->offset:I

    iput v0, p0, Lantlr/CharQueue;->nbrEntries:I

    return-void
.end method
