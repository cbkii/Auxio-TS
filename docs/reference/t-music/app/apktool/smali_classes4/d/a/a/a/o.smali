.class public Ld/a/a/a/o;
.super Ld/a/a/a/A;
.source ""


# static fields
.field public static co:[I

.field public static do:I

.field public static eo:Ljava/lang/String;

.field public static states:[I


# direct methods
.method public constructor <init>()V
    .locals 6

    invoke-direct {p0}, Ld/a/a/a/A;-><init>()V

    const/16 p0, 0x20

    new-array p0, p0, [I

    sput-object p0, Ld/a/a/a/o;->co:[I

    sget-object p0, Ld/a/a/a/o;->co:[I

    const/4 v0, 0x0

    const v1, 0x11111111

    aput v1, p0, v0

    const/4 v2, 0x1

    const v3, 0x111111

    aput v3, p0, v2

    const/4 v3, 0x2

    aput v1, p0, v3

    const/4 v4, 0x3

    const v5, 0x11110111

    aput v5, p0, v4

    const/4 v4, 0x4

    aput v1, p0, v4

    const/4 v5, 0x5

    aput v1, p0, v5

    const/4 v5, 0x6

    aput v1, p0, v5

    const/4 v5, 0x7

    aput v1, p0, v5

    const/16 v5, 0x8

    aput v1, p0, v5

    const/16 v5, 0x9

    aput v1, p0, v5

    const/16 v5, 0xa

    aput v1, p0, v5

    const/16 v5, 0xb

    aput v1, p0, v5

    const/16 v5, 0xc

    aput v1, p0, v5

    const/16 v5, 0xd

    aput v1, p0, v5

    const/16 v5, 0xe

    aput v1, p0, v5

    const/16 v5, 0xf

    aput v1, p0, v5

    const/16 v1, 0x10

    aput v0, p0, v1

    const/16 v1, 0x11

    aput v0, p0, v1

    const/16 v1, 0x12

    aput v0, p0, v1

    const/16 v1, 0x13

    aput v0, p0, v1

    const/16 v1, 0x14

    const v5, 0x22222220

    aput v5, p0, v1

    const/16 v1, 0x15

    const v5, 0x33333322

    aput v5, p0, v1

    const v1, 0x22222222

    const/16 v5, 0x16

    aput v1, p0, v5

    const/16 v5, 0x17

    aput v1, p0, v5

    const/16 v5, 0x18

    aput v1, p0, v5

    const/16 v5, 0x19

    aput v1, p0, v5

    const/16 v5, 0x1a

    aput v1, p0, v5

    const/16 v5, 0x1b

    aput v1, p0, v5

    const/16 v5, 0x1c

    aput v1, p0, v5

    const/16 v5, 0x1d

    aput v1, p0, v5

    const/16 v5, 0x1e

    aput v1, p0, v5

    const/16 v1, 0x1f

    const v5, 0x2222222

    aput v5, p0, v1

    new-array p0, v3, [I

    sput-object p0, Ld/a/a/a/o;->states:[I

    sget-object p0, Ld/a/a/a/o;->states:[I

    const v1, 0x11111301

    aput v1, p0, v0

    const v0, 0x112222

    aput v0, p0, v2

    const-string p0, "GB2312"

    sput-object p0, Ld/a/a/a/o;->eo:Ljava/lang/String;

    sput v4, Ld/a/a/a/o;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/o;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/o;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/o;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/o;->states:[I

    return-object p0
.end method
