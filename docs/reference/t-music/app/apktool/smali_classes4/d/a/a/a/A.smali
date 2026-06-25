.class public abstract Ld/a/a/a/A;
.super Ljava/lang/Object;
.source ""


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static a(Ld/a/a/a/A;BB)B
    .locals 4

    invoke-virtual {p0}, Ld/a/a/a/A;->ke()[I

    move-result-object v0

    invoke-virtual {p0}, Ld/a/a/a/A;->je()I

    move-result v1

    mul-int/2addr v1, p2

    invoke-virtual {p0}, Ld/a/a/a/A;->he()[I

    move-result-object v2

    and-int/lit16 v3, p1, 0xff

    shr-int/lit8 v3, v3, 0x3

    aget v2, v2, v3

    and-int/lit8 p1, p1, 0x7

    shl-int/lit8 p1, p1, 0x2

    shr-int/2addr v2, p1

    and-int/lit8 v2, v2, 0xf

    add-int/2addr v1, v2

    and-int/lit16 v1, v1, 0xff

    shr-int/lit8 v1, v1, 0x3

    aget v0, v0, v1

    invoke-virtual {p0}, Ld/a/a/a/A;->je()I

    move-result v1

    mul-int/2addr v1, p2

    invoke-virtual {p0}, Ld/a/a/a/A;->he()[I

    move-result-object p0

    aget p0, p0, v3

    shr-int/2addr p0, p1

    and-int/lit8 p0, p0, 0xf

    add-int/2addr v1, p0

    and-int/lit16 p0, v1, 0xff

    and-int/lit8 p0, p0, 0x7

    shl-int/lit8 p0, p0, 0x2

    shr-int p0, v0, p0

    and-int/lit8 p0, p0, 0xf

    and-int/lit16 p0, p0, 0xff

    int-to-byte p0, p0

    return p0
.end method


# virtual methods
.method public abstract charset()Ljava/lang/String;
.end method

.method public abstract he()[I
.end method

.method public abstract ie()Z
.end method

.method public abstract je()I
.end method

.method public abstract ke()[I
.end method
