.class public Lantlr/collections/impl/BitSet;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/lang/Cloneable;


# static fields
.field public static final BITS:I = 0x40

.field public static final LOG_BITS:I = 0x6

.field public static final MOD_MASK:I = 0x3f

.field public static final NIBBLE:I = 0x4


# instance fields
.field public bits:[J


# direct methods
.method public constructor <init>()V
    .locals 1

    const/16 v0, 0x40

    invoke-direct {p0, v0}, Lantlr/collections/impl/BitSet;-><init>(I)V

    return-void
.end method

.method public constructor <init>(I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    add-int/lit8 p1, p1, -0x1

    shr-int/lit8 p1, p1, 0x6

    add-int/lit8 p1, p1, 0x1

    new-array p1, p1, [J

    iput-object p1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    return-void
.end method

.method public constructor <init>([J)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    return-void
.end method

.method public static final bitMask(I)J
    .locals 2

    and-int/lit8 p0, p0, 0x3f

    const-wide/16 v0, 0x1

    shl-long/2addr v0, p0

    return-wide v0
.end method

.method public static getRanges([I)Lantlr/collections/impl/Vector;
    .locals 9

    array-length v0, p0

    const/4 v1, 0x0

    if-nez v0, :cond_0

    return-object v1

    :cond_0
    const/4 v0, 0x0

    aget v2, p0, v0

    array-length v2, p0

    add-int/lit8 v2, v2, -0x1

    aget v2, p0, v2

    array-length v2, p0

    const/4 v3, 0x2

    if-gt v2, v3, :cond_1

    return-object v1

    :cond_1
    new-instance v1, Lantlr/collections/impl/Vector;

    const/4 v2, 0x5

    invoke-direct {v1, v2}, Lantlr/collections/impl/Vector;-><init>(I)V

    :goto_0
    array-length v2, p0

    sub-int/2addr v2, v3

    if-ge v0, v2, :cond_5

    array-length v2, p0

    add-int/lit8 v2, v2, -0x1

    add-int/lit8 v4, v0, 0x1

    move v5, v4

    :goto_1
    array-length v6, p0

    if-ge v5, v6, :cond_3

    aget v6, p0, v5

    add-int/lit8 v7, v5, -0x1

    aget v8, p0, v7

    add-int/lit8 v8, v8, 0x1

    if-eq v6, v8, :cond_2

    move v2, v7

    goto :goto_2

    :cond_2
    add-int/lit8 v5, v5, 0x1

    goto :goto_1

    :cond_3
    :goto_2
    sub-int v5, v2, v0

    if-le v5, v3, :cond_4

    new-instance v5, Lantlr/collections/impl/IntRange;

    aget v0, p0, v0

    aget v2, p0, v2

    invoke-direct {v5, v0, v2}, Lantlr/collections/impl/IntRange;-><init>(II)V

    invoke-virtual {v1, v5}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    :cond_4
    move v0, v4

    goto :goto_0

    :cond_5
    return-object v1
.end method

.method private final numWordsToHold(I)I
    .locals 0

    shr-int/lit8 p0, p1, 0x6

    add-int/lit8 p0, p0, 0x1

    return p0
.end method

.method public static of(I)Lantlr/collections/impl/BitSet;
    .locals 2

    new-instance v0, Lantlr/collections/impl/BitSet;

    add-int/lit8 v1, p0, 0x1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>(I)V

    invoke-virtual {v0, p0}, Lantlr/collections/impl/BitSet;->add(I)V

    return-object v0
.end method

.method private setSize(I)V
    .locals 3

    new-array v0, p1, [J

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v1

    invoke-static {p1, v1}, Ljava/lang/Math;->min(II)I

    move-result p1

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    const/4 v2, 0x0

    invoke-static {v1, v2, v0, v2, p1}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    iput-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    return-void
.end method

.method public static final wordNumber(I)I
    .locals 0

    shr-int/lit8 p0, p0, 0x6

    return p0
.end method


# virtual methods
.method public add(I)V
    .locals 5

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->wordNumber(I)I

    move-result v0

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v1

    if-lt v0, v1, :cond_0

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->growToInclude(I)V

    :cond_0
    iget-object p0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v1, p0, v0

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->bitMask(I)J

    move-result-wide v3

    or-long/2addr v1, v3

    aput-wide v1, p0, v0

    return-void
.end method

.method public and(Lantlr/collections/impl/BitSet;)Lantlr/collections/impl/BitSet;
    .locals 0

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->andInPlace(Lantlr/collections/impl/BitSet;)V

    return-object p0
.end method

.method public andInPlace(Lantlr/collections/impl/BitSet;)V
    .locals 7

    iget-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v0, v0

    iget-object v1, p1, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v1

    invoke-static {v0, v1}, Ljava/lang/Math;->min(II)I

    move-result v0

    add-int/lit8 v1, v0, -0x1

    :goto_0
    if-ltz v1, :cond_0

    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v3, v2, v1

    iget-object v5, p1, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v5, v5, v1

    and-long/2addr v3, v5

    aput-wide v3, v2, v1

    add-int/lit8 v1, v1, -0x1

    goto :goto_0

    :cond_0
    :goto_1
    iget-object p1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, p1

    if-ge v0, v1, :cond_1

    const-wide/16 v1, 0x0

    aput-wide v1, p1, v0

    add-int/lit8 v0, v0, 0x1

    goto :goto_1

    :cond_1
    return-void
.end method

.method public clear()V
    .locals 4

    iget-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v0, v0

    add-int/lit8 v0, v0, -0x1

    :goto_0
    if-ltz v0, :cond_0

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    const-wide/16 v2, 0x0

    aput-wide v2, v1, v0

    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public clear(I)V
    .locals 5

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->wordNumber(I)I

    move-result v0

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v1

    if-lt v0, v1, :cond_0

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->growToInclude(I)V

    :cond_0
    iget-object p0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v1, p0, v0

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->bitMask(I)J

    move-result-wide v3

    not-long v3, v3

    and-long/2addr v1, v3

    aput-wide v1, p0, v0

    return-void
.end method

.method public clone()Ljava/lang/Object;
    .locals 4

    :try_start_0
    invoke-super {p0}, Ljava/lang/Object;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lantlr/collections/impl/BitSet;

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v1

    new-array v1, v1, [J

    iput-object v1, v0, Lantlr/collections/impl/BitSet;->bits:[J

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    iget-object v2, v0, Lantlr/collections/impl/BitSet;->bits:[J

    iget-object p0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length p0, p0

    const/4 v3, 0x0

    invoke-static {v1, v3, v2, v3, p0}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V
    :try_end_0
    .catch Ljava/lang/CloneNotSupportedException; {:try_start_0 .. :try_end_0} :catch_0

    return-object v0

    :catch_0
    new-instance p0, Ljava/lang/InternalError;

    invoke-direct {p0}, Ljava/lang/InternalError;-><init>()V

    throw p0
.end method

.method public degree()I
    .locals 9

    iget-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v0, v0

    add-int/lit8 v0, v0, -0x1

    const/4 v1, 0x0

    :goto_0
    if-ltz v0, :cond_2

    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v2, v2, v0

    const-wide/16 v4, 0x0

    cmp-long v6, v2, v4

    if-eqz v6, :cond_1

    const/16 v6, 0x3f

    :goto_1
    if-ltz v6, :cond_1

    const-wide/16 v7, 0x1

    shl-long/2addr v7, v6

    and-long/2addr v7, v2

    cmp-long v7, v7, v4

    if-eqz v7, :cond_0

    add-int/lit8 v1, v1, 0x1

    :cond_0
    add-int/lit8 v6, v6, -0x1

    goto :goto_1

    :cond_1
    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_2
    return v1
.end method

.method public equals(Ljava/lang/Object;)Z
    .locals 8

    const/4 v0, 0x0

    if-eqz p1, :cond_6

    instance-of v1, p1, Lantlr/collections/impl/BitSet;

    if-eqz v1, :cond_6

    check-cast p1, Lantlr/collections/impl/BitSet;

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v1

    iget-object v2, p1, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, v2

    invoke-static {v1, v2}, Ljava/lang/Math;->min(II)I

    move-result v1

    move v2, v1

    :goto_0
    add-int/lit8 v3, v2, -0x1

    if-lez v2, :cond_1

    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v4, v2, v3

    iget-object v2, p1, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v6, v2, v3

    cmp-long v2, v4, v6

    if-eqz v2, :cond_0

    return v0

    :cond_0
    move v2, v3

    goto :goto_0

    :cond_1
    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v3, v2

    const-wide/16 v4, 0x0

    if-le v3, v1, :cond_3

    array-length p1, v2

    :goto_1
    add-int/lit8 v2, p1, -0x1

    if-le p1, v1, :cond_5

    iget-object p1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v6, p1, v2

    cmp-long p1, v6, v4

    if-eqz p1, :cond_2

    return v0

    :cond_2
    move p1, v2

    goto :goto_1

    :cond_3
    iget-object p0, p1, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, p0

    if-le v2, v1, :cond_5

    array-length p0, p0

    :goto_2
    add-int/lit8 v2, p0, -0x1

    if-le p0, v1, :cond_5

    iget-object p0, p1, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v6, p0, v2

    cmp-long p0, v6, v4

    if-eqz p0, :cond_4

    return v0

    :cond_4
    move p0, v2

    goto :goto_2

    :cond_5
    const/4 p0, 0x1

    return p0

    :cond_6
    return v0
.end method

.method public growToInclude(I)V
    .locals 3

    iget-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v0, v0

    shl-int/lit8 v0, v0, 0x1

    invoke-direct {p0, p1}, Lantlr/collections/impl/BitSet;->numWordsToHold(I)I

    move-result p1

    invoke-static {v0, p1}, Ljava/lang/Math;->max(II)I

    move-result p1

    new-array p1, p1, [J

    iget-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v0

    const/4 v2, 0x0

    invoke-static {v0, v2, p1, v2, v1}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    iput-object p1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    return-void
.end method

.method public lengthInLongWords()I
    .locals 0

    iget-object p0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length p0, p0

    return p0
.end method

.method public member(I)Z
    .locals 3

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->wordNumber(I)I

    move-result v0

    iget-object p0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, p0

    const/4 v2, 0x0

    if-lt v0, v1, :cond_0

    return v2

    :cond_0
    aget-wide v0, p0, v0

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->bitMask(I)J

    move-result-wide p0

    and-long/2addr p0, v0

    const-wide/16 v0, 0x0

    cmp-long p0, p0, v0

    if-eqz p0, :cond_1

    const/4 v2, 0x1

    :cond_1
    return v2
.end method

.method public nil()Z
    .locals 6

    iget-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v0, v0

    const/4 v1, 0x1

    sub-int/2addr v0, v1

    :goto_0
    if-ltz v0, :cond_1

    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v2, v2, v0

    const-wide/16 v4, 0x0

    cmp-long v2, v2, v4

    if-eqz v2, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_1
    return v1
.end method

.method public not()Lantlr/collections/impl/BitSet;
    .locals 0

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/collections/impl/BitSet;

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->notInPlace()V

    return-object p0
.end method

.method public notInPlace()V
    .locals 4

    iget-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v0, v0

    add-int/lit8 v0, v0, -0x1

    :goto_0
    if-ltz v0, :cond_0

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v2, v1, v0

    not-long v2, v2

    aput-wide v2, v1, v0

    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public notInPlace(I)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, v0, p1}, Lantlr/collections/impl/BitSet;->notInPlace(II)V

    return-void
.end method

.method public notInPlace(II)V
    .locals 6

    invoke-virtual {p0, p2}, Lantlr/collections/impl/BitSet;->growToInclude(I)V

    :goto_0
    if-gt p1, p2, :cond_0

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->wordNumber(I)I

    move-result v0

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v2, v1, v0

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->bitMask(I)J

    move-result-wide v4

    xor-long/2addr v2, v4

    aput-wide v2, v1, v0

    add-int/lit8 p1, p1, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public or(Lantlr/collections/impl/BitSet;)Lantlr/collections/impl/BitSet;
    .locals 0

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->clone()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->orInPlace(Lantlr/collections/impl/BitSet;)V

    return-object p0
.end method

.method public orInPlace(Lantlr/collections/impl/BitSet;)V
    .locals 6

    iget-object v0, p1, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v0

    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, v2

    if-le v1, v2, :cond_0

    array-length v0, v0

    invoke-direct {p0, v0}, Lantlr/collections/impl/BitSet;->setSize(I)V

    :cond_0
    iget-object v0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v0, v0

    iget-object v1, p1, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v1

    invoke-static {v0, v1}, Ljava/lang/Math;->min(II)I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    :goto_0
    if-ltz v0, :cond_1

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v2, v1, v0

    iget-object v4, p1, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v4, v4, v0

    or-long/2addr v2, v4

    aput-wide v2, v1, v0

    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method public remove(I)V
    .locals 5

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->wordNumber(I)I

    move-result v0

    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v1, v1

    if-lt v0, v1, :cond_0

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->growToInclude(I)V

    :cond_0
    iget-object p0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v1, p0, v0

    invoke-static {p1}, Lantlr/collections/impl/BitSet;->bitMask(I)J

    move-result-wide v3

    not-long v3, v3

    and-long/2addr v1, v3

    aput-wide v1, p0, v0

    return-void
.end method

.method public size()I
    .locals 0

    iget-object p0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length p0, p0

    shl-int/lit8 p0, p0, 0x6

    return p0
.end method

.method public subset(Lantlr/collections/impl/BitSet;)Z
    .locals 0

    if-eqz p1, :cond_0

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->and(Lantlr/collections/impl/BitSet;)Lantlr/collections/impl/BitSet;

    move-result-object p1

    invoke-virtual {p1, p0}, Lantlr/collections/impl/BitSet;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public subtractInPlace(Lantlr/collections/impl/BitSet;)V
    .locals 7

    if-nez p1, :cond_0

    return-void

    :cond_0
    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, v1

    if-ge v0, v2, :cond_1

    iget-object v2, p1, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v3, v2

    if-ge v0, v3, :cond_1

    aget-wide v3, v1, v0

    aget-wide v5, v2, v0

    not-long v5, v5

    and-long v2, v3, v5

    aput-wide v2, v1, v0

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method public toArray()[I
    .locals 4

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->degree()I

    move-result v0

    new-array v0, v0, [I

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    iget-object v3, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v3, v3

    shl-int/lit8 v3, v3, 0x6

    if-ge v1, v3, :cond_1

    invoke-virtual {p0, v1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_0

    add-int/lit8 v3, v2, 0x1

    aput v1, v0, v2

    move v2, v3

    :cond_0
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    return-object v0
.end method

.method public toPackedArray()[J
    .locals 0

    iget-object p0, p0, Lantlr/collections/impl/BitSet;->bits:[J

    return-object p0
.end method

.method public toString()Ljava/lang/String;
    .locals 1

    const-string v0, ","

    invoke-virtual {p0, v0}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public toString(Ljava/lang/String;)Ljava/lang/String;
    .locals 3

    const-string v0, ""

    const/4 v1, 0x0

    :goto_0
    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, v2

    shl-int/lit8 v2, v2, 0x6

    if-ge v1, v2, :cond_2

    invoke-virtual {p0, v1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v2

    if-lez v2, :cond_0

    invoke-static {v0, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_0
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_1
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    return-object v0
.end method

.method public toString(Ljava/lang/String;Lantlr/CharFormatter;)Ljava/lang/String;
    .locals 3

    const-string v0, ""

    const/4 v1, 0x0

    :goto_0
    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, v2

    shl-int/lit8 v2, v2, 0x6

    if-ge v1, v2, :cond_2

    invoke-virtual {p0, v1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v2

    if-lez v2, :cond_0

    invoke-static {v0, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_0
    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-interface {p2, v1}, Lantlr/CharFormatter;->literalChar(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    :cond_1
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    return-object v0
.end method

.method public toString(Ljava/lang/String;Lantlr/collections/impl/Vector;)Ljava/lang/String;
    .locals 4

    if-nez p2, :cond_0

    invoke-virtual {p0, p1}, Lantlr/collections/impl/BitSet;->toString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    const/4 v0, 0x0

    const-string v1, ""

    :goto_0
    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, v2

    shl-int/lit8 v2, v2, 0x6

    if-ge v0, v2, :cond_5

    invoke-virtual {p0, v0}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_4

    invoke-virtual {v1}, Ljava/lang/String;->length()I

    move-result v2

    if-lez v2, :cond_1

    invoke-static {v1, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_1
    invoke-virtual {p2}, Lantlr/collections/impl/Vector;->size()I

    move-result v2

    const-string v3, ">"

    if-lt v0, v2, :cond_2

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "<bad element "

    :goto_1
    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    goto :goto_2

    :cond_2
    invoke-virtual {p2, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v2

    if-nez v2, :cond_3

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "<"

    goto :goto_1

    :cond_3
    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p2, v0}, Lantlr/collections/impl/Vector;->elementAt(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    :cond_4
    :goto_2
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_5
    return-object v1
.end method

.method public toStringOfHalfWords()Ljava/lang/String;
    .locals 8

    new-instance v0, Ljava/lang/String;

    invoke-direct {v0}, Ljava/lang/String;-><init>()V

    const/4 v1, 0x0

    :goto_0
    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, v2

    if-ge v1, v2, :cond_1

    const-string v2, ", "

    if-eqz v1, :cond_0

    invoke-static {v0, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_0
    iget-object v3, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v3, v3, v1

    const-wide v5, 0xffffffffL

    and-long/2addr v3, v5

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7, v3, v4}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    const-string v0, "UL"

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v3, v3, v1

    const/16 v7, 0x20

    ushr-long/2addr v3, v7

    and-long/2addr v3, v5

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v3, v4}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    return-object v0
.end method

.method public toStringOfWords()Ljava/lang/String;
    .locals 4

    new-instance v0, Ljava/lang/String;

    invoke-direct {v0}, Ljava/lang/String;-><init>()V

    const/4 v1, 0x0

    :goto_0
    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    array-length v2, v2

    if-ge v1, v2, :cond_1

    if-eqz v1, :cond_0

    const-string v2, ", "

    invoke-static {v0, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    :cond_0
    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget-object v2, p0, Lantlr/collections/impl/BitSet;->bits:[J

    aget-wide v2, v2, v1

    invoke-virtual {v0, v2, v3}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    const-string v2, "L"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    return-object v0
.end method

.method public toStringWithRanges(Ljava/lang/String;Lantlr/CharFormatter;)Ljava/lang/String;
    .locals 8

    invoke-virtual {p0}, Lantlr/collections/impl/BitSet;->toArray()[I

    move-result-object p0

    array-length v0, p0

    const-string v1, ""

    if-nez v0, :cond_0

    return-object v1

    :cond_0
    const/4 v0, 0x0

    move-object v2, v1

    move v1, v0

    :goto_0
    array-length v3, p0

    if-ge v1, v3, :cond_5

    add-int/lit8 v3, v1, 0x1

    move v4, v0

    :goto_1
    array-length v5, p0

    if-ge v3, v5, :cond_2

    aget v5, p0, v3

    add-int/lit8 v6, v3, -0x1

    aget v6, p0, v6

    add-int/lit8 v6, v6, 0x1

    if-eq v5, v6, :cond_1

    goto :goto_2

    :cond_1
    add-int/lit8 v4, v3, 0x1

    move v7, v4

    move v4, v3

    move v3, v7

    goto :goto_1

    :cond_2
    :goto_2
    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v3

    if-lez v3, :cond_3

    invoke-static {v2, p1}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    :cond_3
    sub-int v3, v4, v1

    const/4 v5, 0x2

    invoke-static {v2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    if-lt v3, v5, :cond_4

    aget v1, p0, v1

    invoke-interface {p2, v1}, Lantlr/CharFormatter;->literalChar(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    const-string v2, ".."

    invoke-static {v1, v2}, La/a/a/a/a;->a(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    aget v2, p0, v4

    invoke-interface {p2, v2}, Lantlr/CharFormatter;->literalChar(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    move-object v2, v1

    move v1, v4

    goto :goto_3

    :cond_4
    aget v3, p0, v1

    invoke-interface {p2, v3}, Lantlr/CharFormatter;->literalChar(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    :goto_3
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_5
    return-object v2
.end method
