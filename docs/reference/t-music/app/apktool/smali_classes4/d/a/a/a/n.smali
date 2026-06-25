.class public Ld/a/a/a/n;
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

    sput-object p0, Ld/a/a/a/n;->co:[I

    sget-object p0, Ld/a/a/a/n;->co:[I

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

    const/4 v5, 0x4

    aput v1, p0, v5

    const/4 v6, 0x5

    aput v1, p0, v6

    const/4 v1, 0x6

    const v7, 0x33333333

    aput v7, p0, v1

    const/4 v7, 0x7

    const v8, 0x11111133

    aput v8, p0, v7

    const v8, 0x22222222

    const/16 v9, 0x8

    aput v8, p0, v9

    const/16 v9, 0x9

    aput v8, p0, v9

    const/16 v9, 0xa

    aput v8, p0, v9

    const/16 v9, 0xb

    aput v8, p0, v9

    const/16 v9, 0xc

    aput v8, p0, v9

    const/16 v9, 0xd

    aput v8, p0, v9

    const/16 v9, 0xe

    aput v8, p0, v9

    const/16 v8, 0xf

    const v9, 0x42222222

    aput v9, p0, v8

    const/16 v8, 0x10

    const v9, 0x66666665

    aput v9, p0, v8

    const/16 v8, 0x11

    const v9, 0x66666666

    aput v9, p0, v8

    const/16 v10, 0x12

    aput v9, p0, v10

    const/16 v10, 0x13

    aput v9, p0, v10

    const/16 v10, 0x14

    aput v9, p0, v10

    const/16 v10, 0x15

    aput v9, p0, v10

    const/16 v10, 0x16

    aput v9, p0, v10

    const/16 v10, 0x17

    aput v9, p0, v10

    const/16 v10, 0x18

    aput v9, p0, v10

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

    const v10, 0x6666666

    aput v10, p0, v9

    new-array p0, v1, [I

    sput-object p0, Ld/a/a/a/n;->states:[I

    sget-object p0, Ld/a/a/a/n;->states:[I

    const v1, 0x13000001

    aput v1, p0, v0

    const v0, 0x22111111

    aput v0, p0, v2

    const v0, 0x1122222

    aput v0, p0, v3

    const v0, 0x11110014

    aput v0, p0, v4

    const v0, 0x12111511

    aput v0, p0, v5

    aput v8, p0, v6

    const-string p0, "GB18030"

    sput-object p0, Ld/a/a/a/n;->eo:Ljava/lang/String;

    sput v7, Ld/a/a/a/n;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/n;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/n;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/n;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/n;->states:[I

    return-object p0
.end method
