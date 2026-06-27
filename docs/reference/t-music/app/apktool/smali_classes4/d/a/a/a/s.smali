.class public Ld/a/a/a/s;
.super Ld/a/a/a/A;
.source ""


# static fields
.field public static co:[I

.field public static do:I

.field public static eo:Ljava/lang/String;

.field public static states:[I


# direct methods
.method public constructor <init>()V
    .locals 12

    invoke-direct {p0}, Ld/a/a/a/A;-><init>()V

    const/16 p0, 0x20

    new-array p0, p0, [I

    sput-object p0, Ld/a/a/a/s;->co:[I

    sget-object p0, Ld/a/a/a/s;->co:[I

    const/4 v0, 0x2

    const/4 v1, 0x0

    aput v0, p0, v1

    const/4 v2, 0x1

    aput v1, p0, v2

    aput v1, p0, v0

    const/4 v3, 0x3

    const/16 v4, 0x1000

    aput v4, p0, v3

    const/4 v4, 0x4

    aput v1, p0, v4

    const/4 v5, 0x5

    const/16 v6, 0x30

    aput v6, p0, v5

    const/4 v6, 0x6

    aput v1, p0, v6

    const/4 v7, 0x7

    aput v1, p0, v7

    const/16 v8, 0x8

    const/16 v9, 0x4000

    aput v9, p0, v8

    const/16 v9, 0x9

    aput v1, p0, v9

    const/16 v10, 0xa

    aput v1, p0, v10

    const/16 v10, 0xb

    aput v1, p0, v10

    const/16 v10, 0xc

    aput v1, p0, v10

    const/16 v10, 0xd

    aput v1, p0, v10

    const/16 v10, 0xe

    aput v1, p0, v10

    const/16 v10, 0xf

    aput v1, p0, v10

    const v10, 0x22222222

    const/16 v11, 0x10

    aput v10, p0, v11

    const/16 v11, 0x11

    aput v10, p0, v11

    const/16 v11, 0x12

    aput v10, p0, v11

    const/16 v11, 0x13

    aput v10, p0, v11

    const/16 v11, 0x14

    aput v10, p0, v11

    const/16 v11, 0x15

    aput v10, p0, v11

    const/16 v11, 0x16

    aput v10, p0, v11

    const/16 v11, 0x17

    aput v10, p0, v11

    const/16 v11, 0x18

    aput v10, p0, v11

    const/16 v11, 0x19

    aput v10, p0, v11

    const/16 v11, 0x1a

    aput v10, p0, v11

    const/16 v11, 0x1b

    aput v10, p0, v11

    const/16 v11, 0x1c

    aput v10, p0, v11

    const/16 v11, 0x1d

    aput v10, p0, v11

    const/16 v11, 0x1e

    aput v10, p0, v11

    const/16 v11, 0x1f

    aput v10, p0, v11

    new-array p0, v8, [I

    sput-object p0, Ld/a/a/a/s;->states:[I

    sget-object p0, Ld/a/a/a/s;->states:[I

    const/16 v8, 0x130

    aput v8, p0, v1

    const v1, 0x11111110

    aput v1, p0, v2

    const v1, 0x22222211

    aput v1, p0, v0

    const v0, 0x14111222

    aput v0, p0, v3

    const v0, 0x11112111

    aput v0, p0, v4

    const v1, 0x11111165

    aput v1, p0, v5

    aput v0, p0, v6

    const v0, 0x1211111

    aput v0, p0, v7

    const-string p0, "ISO-2022-CN"

    sput-object p0, Ld/a/a/a/s;->eo:Ljava/lang/String;

    sput v9, Ld/a/a/a/s;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/s;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/s;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/s;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/s;->states:[I

    return-object p0
.end method
