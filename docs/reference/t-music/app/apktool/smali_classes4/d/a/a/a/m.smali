.class public Ld/a/a/a/m;
.super Ld/a/a/a/A;
.source ""


# static fields
.field public static co:[I

.field public static do:I

.field public static eo:Ljava/lang/String;

.field public static states:[I


# direct methods
.method public constructor <init>()V
    .locals 11

    invoke-direct {p0}, Ld/a/a/a/A;-><init>()V

    const/16 p0, 0x20

    new-array p0, p0, [I

    sput-object p0, Ld/a/a/a/m;->co:[I

    sget-object p0, Ld/a/a/a/m;->co:[I

    const/4 v0, 0x0

    const v1, 0x22222222

    aput v1, p0, v0

    const/4 v2, 0x1

    const v3, 0x222222

    aput v3, p0, v2

    const/4 v3, 0x2

    aput v1, p0, v3

    const/4 v4, 0x3

    const v5, 0x22220222

    aput v5, p0, v4

    const/4 v5, 0x4

    aput v1, p0, v5

    const/4 v6, 0x5

    aput v1, p0, v6

    const/4 v7, 0x6

    aput v1, p0, v7

    const/4 v8, 0x7

    aput v1, p0, v8

    const/16 v9, 0x8

    aput v1, p0, v9

    const/16 v9, 0x9

    aput v1, p0, v9

    const/16 v9, 0xa

    aput v1, p0, v9

    const/16 v9, 0xb

    aput v1, p0, v9

    const/16 v9, 0xc

    aput v1, p0, v9

    const/16 v9, 0xd

    aput v1, p0, v9

    const/16 v9, 0xe

    aput v1, p0, v9

    const/16 v9, 0xf

    aput v1, p0, v9

    const/16 v1, 0x10

    aput v0, p0, v1

    const/16 v9, 0x11

    const/high16 v10, 0x6000000

    aput v10, p0, v9

    const/16 v9, 0x12

    aput v0, p0, v9

    const/16 v9, 0x13

    aput v0, p0, v9

    const/16 v9, 0x14

    const v10, 0x44444430

    aput v10, p0, v9

    const/16 v9, 0x15

    const v10, 0x11111155

    aput v10, p0, v9

    const v9, 0x11111111

    const/16 v10, 0x16

    aput v9, p0, v10

    const/16 v10, 0x17

    aput v9, p0, v10

    const/16 v9, 0x18

    const v10, 0x33331311

    aput v10, p0, v9

    const v9, 0x33333333

    const/16 v10, 0x19

    aput v9, p0, v10

    const/16 v10, 0x1a

    aput v9, p0, v10

    const/16 v10, 0x1b

    aput v9, p0, v10

    const/16 v10, 0x1c

    aput v9, p0, v10

    const/16 v10, 0x1d

    aput v9, p0, v10

    const/16 v10, 0x1e

    aput v9, p0, v10

    const/16 v9, 0x1f

    const v10, 0x3333333

    aput v10, p0, v9

    new-array p0, v7, [I

    sput-object p0, Ld/a/a/a/m;->states:[I

    sget-object p0, Ld/a/a/a/m;->states:[I

    const v7, 0x14333011

    aput v7, p0, v0

    const v0, 0x22111111

    aput v0, p0, v2

    const v0, 0x10122222

    aput v0, p0, v3

    const v0, 0x11111000

    aput v0, p0, v4

    const v0, 0x101115

    aput v0, p0, v5

    aput v1, p0, v6

    const-string p0, "x-euc-tw"

    sput-object p0, Ld/a/a/a/m;->eo:Ljava/lang/String;

    sput v8, Ld/a/a/a/m;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/m;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/m;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/m;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/m;->states:[I

    return-object p0
.end method
