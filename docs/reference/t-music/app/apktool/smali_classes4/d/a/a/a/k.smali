.class public Ld/a/a/a/k;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public In:I

.field public Jn:[I

.field public Kn:[I

.field public Ln:[F

.field public Mn:[F

.field public mState:I

.field public mThreshold:I


# direct methods
.method public constructor <init>()V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput v0, p0, Ld/a/a/a/k;->In:I

    const/16 v1, 0xc8

    iput v1, p0, Ld/a/a/a/k;->mThreshold:I

    iput v0, p0, Ld/a/a/a/k;->mState:I

    const/16 v0, 0x5e

    new-array v1, v0, [I

    iput-object v1, p0, Ld/a/a/a/k;->Jn:[I

    new-array v1, v0, [I

    iput-object v1, p0, Ld/a/a/a/k;->Kn:[I

    new-array v1, v0, [F

    iput-object v1, p0, Ld/a/a/a/k;->Ln:[F

    new-array v0, v0, [F

    iput-object v0, p0, Ld/a/a/a/k;->Mn:[F

    invoke-virtual {p0}, Ld/a/a/a/k;->Reset()V

    return-void
.end method


# virtual methods
.method public Reset()V
    .locals 4

    const/4 v0, 0x0

    iput v0, p0, Ld/a/a/a/k;->In:I

    iput v0, p0, Ld/a/a/a/k;->mState:I

    move v1, v0

    :goto_0
    const/16 v2, 0x5e

    if-ge v1, v2, :cond_0

    iget-object v2, p0, Ld/a/a/a/k;->Jn:[I

    iget-object v3, p0, Ld/a/a/a/k;->Kn:[I

    aput v0, v3, v1

    aput v0, v2, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public Zd()V
    .locals 4

    const/4 v0, 0x0

    :goto_0
    const/16 v1, 0x5e

    if-ge v0, v1, :cond_0

    iget-object v1, p0, Ld/a/a/a/k;->Ln:[F

    iget-object v2, p0, Ld/a/a/a/k;->Jn:[I

    aget v2, v2, v0

    int-to-float v2, v2

    iget v3, p0, Ld/a/a/a/k;->In:I

    int-to-float v3, v3

    div-float/2addr v2, v3

    aput v2, v1, v0

    iget-object v1, p0, Ld/a/a/a/k;->Mn:[F

    iget-object v2, p0, Ld/a/a/a/k;->Kn:[I

    aget v2, v2, v0

    int-to-float v2, v2

    div-float/2addr v2, v3

    aput v2, v1, v0

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public _d()Z
    .locals 1

    iget v0, p0, Ld/a/a/a/k;->In:I

    iget p0, p0, Ld/a/a/a/k;->mThreshold:I

    if-le v0, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public a([FF[FF)F
    .locals 1

    iget-object v0, p0, Ld/a/a/a/k;->Ln:[F

    invoke-virtual {p0, p1, v0}, Ld/a/a/a/k;->a([F[F)F

    move-result p1

    mul-float/2addr p1, p2

    iget-object p2, p0, Ld/a/a/a/k;->Mn:[F

    invoke-virtual {p0, p3, p2}, Ld/a/a/a/k;->a([F[F)F

    move-result p0

    mul-float/2addr p0, p4

    add-float/2addr p0, p1

    return p0
.end method

.method public a([F[F)F
    .locals 3

    const/4 p0, 0x0

    const/4 v0, 0x0

    :goto_0
    const/16 v1, 0x5e

    if-ge v0, v1, :cond_0

    aget v1, p1, v0

    aget v2, p2, v0

    sub-float/2addr v1, v2

    mul-float/2addr v1, v1

    add-float/2addr p0, v1

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    float-to-double p0, p0

    invoke-static {p0, p1}, Ljava/lang/Math;->sqrt(D)D

    move-result-wide p0

    double-to-float p0, p0

    const/high16 p1, 0x42bc0000    # 94.0f

    div-float/2addr p0, p1

    return p0
.end method

.method public ae()Z
    .locals 1

    iget p0, p0, Ld/a/a/a/k;->In:I

    const/4 v0, 0x1

    if-le p0, v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public h([BI)Z
    .locals 9

    iget v0, p0, Ld/a/a/a/k;->mState:I

    const/4 v1, 0x0

    const/4 v2, 0x1

    if-ne v0, v2, :cond_0

    return v1

    :cond_0
    move v0, v1

    move v3, v0

    :goto_0
    if-ge v0, p2, :cond_7

    iget v4, p0, Ld/a/a/a/k;->mState:I

    if-eq v2, v4, :cond_7

    const/4 v5, 0x2

    const/16 v6, 0xa1

    const/16 v7, 0xff

    if-eqz v4, :cond_3

    if-eq v4, v2, :cond_6

    if-eq v4, v5, :cond_1

    goto :goto_1

    :cond_1
    aget-byte v4, p1, v3

    and-int/lit16 v4, v4, 0x80

    if-eqz v4, :cond_5

    aget-byte v4, p1, v3

    and-int/2addr v4, v7

    if-eq v7, v4, :cond_5

    aget-byte v4, p1, v3

    and-int/2addr v4, v7

    if-le v6, v4, :cond_2

    goto :goto_1

    :cond_2
    iget v4, p0, Ld/a/a/a/k;->In:I

    add-int/2addr v4, v2

    iput v4, p0, Ld/a/a/a/k;->In:I

    iget-object v4, p0, Ld/a/a/a/k;->Kn:[I

    aget-byte v5, p1, v3

    and-int/2addr v5, v7

    sub-int/2addr v5, v6

    aget v6, v4, v5

    add-int/2addr v6, v2

    aput v6, v4, v5

    iput v1, p0, Ld/a/a/a/k;->mState:I

    goto :goto_2

    :cond_3
    aget-byte v4, p1, v3

    and-int/lit16 v4, v4, 0x80

    if-eqz v4, :cond_6

    aget-byte v4, p1, v3

    and-int/2addr v4, v7

    if-eq v7, v4, :cond_5

    aget-byte v4, p1, v3

    and-int/2addr v4, v7

    if-le v6, v4, :cond_4

    goto :goto_1

    :cond_4
    iget v4, p0, Ld/a/a/a/k;->In:I

    add-int/2addr v4, v2

    iput v4, p0, Ld/a/a/a/k;->In:I

    iget-object v4, p0, Ld/a/a/a/k;->Jn:[I

    aget-byte v8, p1, v3

    and-int/2addr v7, v8

    sub-int/2addr v7, v6

    aget v6, v4, v7

    add-int/2addr v6, v2

    aput v6, v4, v7

    iput v5, p0, Ld/a/a/a/k;->mState:I

    goto :goto_2

    :cond_5
    :goto_1
    iput v2, p0, Ld/a/a/a/k;->mState:I

    :cond_6
    :goto_2
    add-int/lit8 v0, v0, 0x1

    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_7
    iget p0, p0, Ld/a/a/a/k;->mState:I

    if-eq v2, p0, :cond_8

    move v1, v2

    :cond_8
    return v1
.end method
