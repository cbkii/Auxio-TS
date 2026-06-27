.class public Ld/a/a/a/u;
.super Ld/a/a/a/A;
.source ""


# static fields
.field public static co:[I

.field public static do:I

.field public static eo:Ljava/lang/String;

.field public static states:[I


# direct methods
.method public constructor <init>()V
    .locals 9

    invoke-direct {p0}, Ld/a/a/a/A;-><init>()V

    const/16 p0, 0x20

    new-array p0, p0, [I

    sput-object p0, Ld/a/a/a/u;->co:[I

    sget-object p0, Ld/a/a/a/u;->co:[I

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

    const/high16 v5, 0x30000

    aput v5, p0, v4

    const/4 v5, 0x5

    const/16 v6, 0x40

    aput v6, p0, v5

    const/4 v6, 0x6

    aput v1, p0, v6

    const/4 v7, 0x7

    aput v1, p0, v7

    const/16 v7, 0x8

    const/16 v8, 0x5000

    aput v8, p0, v7

    const/16 v7, 0x9

    aput v1, p0, v7

    const/16 v7, 0xa

    aput v1, p0, v7

    const/16 v7, 0xb

    aput v1, p0, v7

    const/16 v7, 0xc

    aput v1, p0, v7

    const/16 v7, 0xd

    aput v1, p0, v7

    const/16 v7, 0xe

    aput v1, p0, v7

    const/16 v7, 0xf

    aput v1, p0, v7

    const v7, 0x22222222

    const/16 v8, 0x10

    aput v7, p0, v8

    const/16 v8, 0x11

    aput v7, p0, v8

    const/16 v8, 0x12

    aput v7, p0, v8

    const/16 v8, 0x13

    aput v7, p0, v8

    const/16 v8, 0x14

    aput v7, p0, v8

    const/16 v8, 0x15

    aput v7, p0, v8

    const/16 v8, 0x16

    aput v7, p0, v8

    const/16 v8, 0x17

    aput v7, p0, v8

    const/16 v8, 0x18

    aput v7, p0, v8

    const/16 v8, 0x19

    aput v7, p0, v8

    const/16 v8, 0x1a

    aput v7, p0, v8

    const/16 v8, 0x1b

    aput v7, p0, v8

    const/16 v8, 0x1c

    aput v7, p0, v8

    const/16 v8, 0x1d

    aput v7, p0, v8

    const/16 v8, 0x1e

    aput v7, p0, v8

    const/16 v8, 0x1f

    aput v7, p0, v8

    new-array p0, v5, [I

    sput-object p0, Ld/a/a/a/u;->states:[I

    sget-object p0, Ld/a/a/a/u;->states:[I

    const v5, 0x11000130

    aput v5, p0, v1

    const v1, 0x22221111

    aput v1, p0, v2

    const v1, 0x11411122

    aput v1, p0, v0

    const v0, 0x11151111

    aput v0, p0, v3

    const/16 v0, 0x2111

    aput v0, p0, v4

    const-string p0, "ISO-2022-KR"

    sput-object p0, Ld/a/a/a/u;->eo:Ljava/lang/String;

    sput v6, Ld/a/a/a/u;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/u;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/u;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/u;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/u;->states:[I

    return-object p0
.end method
