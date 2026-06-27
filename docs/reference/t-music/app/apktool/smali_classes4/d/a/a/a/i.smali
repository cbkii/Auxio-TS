.class public Ld/a/a/a/i;
.super Ld/a/a/a/A;
.source ""


# static fields
.field public static co:[I

.field public static do:I

.field public static eo:Ljava/lang/String;

.field public static states:[I


# direct methods
.method public constructor <init>()V
    .locals 10

    invoke-direct {p0}, Ld/a/a/a/A;-><init>()V

    const/16 p0, 0x20

    new-array p0, p0, [I

    sput-object p0, Ld/a/a/a/i;->co:[I

    sget-object p0, Ld/a/a/a/i;->co:[I

    const/4 v0, 0x0

    const v1, 0x44444444

    aput v1, p0, v0

    const/4 v2, 0x1

    const v3, 0x55444444

    aput v3, p0, v2

    const/4 v3, 0x2

    aput v1, p0, v3

    const/4 v4, 0x3

    const v5, 0x44445444

    aput v5, p0, v4

    const/4 v5, 0x4

    aput v1, p0, v5

    const/4 v6, 0x5

    aput v1, p0, v6

    const/4 v7, 0x6

    aput v1, p0, v7

    const/4 v8, 0x7

    aput v1, p0, v8

    const/16 v8, 0x8

    aput v1, p0, v8

    const/16 v8, 0x9

    aput v1, p0, v8

    const/16 v8, 0xa

    aput v1, p0, v8

    const/16 v8, 0xb

    aput v1, p0, v8

    const/16 v8, 0xc

    aput v1, p0, v8

    const/16 v8, 0xd

    aput v1, p0, v8

    const/16 v8, 0xe

    aput v1, p0, v8

    const/16 v8, 0xf

    aput v1, p0, v8

    const v1, 0x55555555

    const/16 v8, 0x10

    aput v1, p0, v8

    const/16 v8, 0x11

    const v9, 0x31555555

    aput v9, p0, v8

    const/16 v8, 0x12

    aput v1, p0, v8

    const/16 v8, 0x13

    aput v1, p0, v8

    const/16 v1, 0x14

    const v8, 0x22222225

    aput v8, p0, v1

    const v1, 0x22222222

    const/16 v8, 0x15

    aput v1, p0, v8

    const/16 v8, 0x16

    aput v1, p0, v8

    const/16 v8, 0x17

    aput v1, p0, v8

    const/16 v8, 0x18

    aput v1, p0, v8

    const/16 v8, 0x19

    aput v1, p0, v8

    const/16 v8, 0x1a

    aput v1, p0, v8

    const/16 v8, 0x1b

    aput v1, p0, v8

    const/16 v1, 0x1c

    aput v0, p0, v1

    const/16 v1, 0x1d

    aput v0, p0, v1

    const/16 v1, 0x1e

    aput v0, p0, v1

    const/16 v1, 0x1f

    const/high16 v8, 0x50000000

    aput v8, p0, v1

    new-array p0, v6, [I

    sput-object p0, Ld/a/a/a/i;->states:[I

    sget-object p0, Ld/a/a/a/i;->states:[I

    const v1, 0x11105343

    aput v1, p0, v0

    const v0, 0x22221111

    aput v0, p0, v2

    const v0, 0x11101022

    aput v0, p0, v3

    const v0, 0x13111011

    aput v0, p0, v4

    const/16 v0, 0x1113

    aput v0, p0, v5

    const-string p0, "EUC-JP"

    sput-object p0, Ld/a/a/a/i;->eo:Ljava/lang/String;

    sput v7, Ld/a/a/a/i;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/i;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/i;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/i;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/i;->states:[I

    return-object p0
.end method
