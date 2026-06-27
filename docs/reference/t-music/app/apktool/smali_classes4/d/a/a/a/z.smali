.class public Ld/a/a/a/z;
.super Ld/a/a/a/A;
.source ""


# static fields
.field public static co:[I

.field public static do:I

.field public static eo:Ljava/lang/String;

.field public static states:[I


# direct methods
.method public constructor <init>()V
    .locals 14

    invoke-direct {p0}, Ld/a/a/a/A;-><init>()V

    const/16 p0, 0x20

    new-array p0, p0, [I

    sput-object p0, Ld/a/a/a/z;->co:[I

    sget-object p0, Ld/a/a/a/z;->co:[I

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

    const/4 v7, 0x6

    aput v1, p0, v7

    const/4 v8, 0x7

    aput v1, p0, v8

    const/16 v9, 0x8

    aput v1, p0, v9

    const/16 v10, 0x9

    aput v1, p0, v10

    const/16 v11, 0xa

    aput v1, p0, v11

    const/16 v11, 0xb

    aput v1, p0, v11

    const/16 v11, 0xc

    aput v1, p0, v11

    const/16 v11, 0xd

    aput v1, p0, v11

    const/16 v11, 0xe

    aput v1, p0, v11

    const/16 v11, 0xf

    aput v1, p0, v11

    const/16 v11, 0x10

    const v12, 0x33332222

    aput v12, p0, v11

    const v12, 0x44444444

    const/16 v13, 0x11

    aput v12, p0, v13

    const/16 v13, 0x12

    aput v12, p0, v13

    const/16 v13, 0x13

    aput v12, p0, v13

    const v12, 0x55555555

    const/16 v13, 0x14

    aput v12, p0, v13

    const/16 v13, 0x15

    aput v12, p0, v13

    const/16 v13, 0x16

    aput v12, p0, v13

    const/16 v13, 0x17

    aput v12, p0, v13

    const/16 v12, 0x18

    const v13, 0x66666600

    aput v13, p0, v12

    const v12, 0x66666666

    const/16 v13, 0x19

    aput v12, p0, v13

    const/16 v13, 0x1a

    aput v12, p0, v13

    const/16 v13, 0x1b

    aput v12, p0, v13

    const/16 v12, 0x1c

    const v13, -0x77777779

    aput v13, p0, v12

    const/16 v12, 0x1d

    const v13, -0x77677778

    aput v13, p0, v12

    const/16 v12, 0x1e

    const v13, -0x44444446

    aput v13, p0, v12

    const/16 v12, 0x1f

    const v13, 0xfedddc

    aput v13, p0, v12

    const/16 p0, 0x1a

    new-array p0, p0, [I

    sput-object p0, Ld/a/a/a/z;->states:[I

    sget-object p0, Ld/a/a/a/z;->states:[I

    const v12, -0x53eeeeff

    aput v12, p0, v0

    const v0, 0x345678b9

    aput v0, p0, v2

    aput v1, p0, v3

    aput v1, p0, v4

    const v0, 0x22222222

    aput v0, p0, v5

    aput v0, p0, v6

    const v0, 0x11555511

    aput v0, p0, v7

    aput v1, p0, v8

    const v0, 0x11555111

    aput v0, p0, v9

    aput v1, p0, v10

    const/16 v0, 0xa

    const v2, 0x11777711

    aput v2, p0, v0

    const/16 v0, 0xb

    aput v1, p0, v0

    const/16 v0, 0xc

    const v2, 0x11771111

    aput v2, p0, v0

    const/16 v0, 0xd

    aput v1, p0, v0

    const/16 v0, 0xe

    const v2, 0x11999911

    aput v2, p0, v0

    const/16 v0, 0xf

    aput v1, p0, v0

    const v0, 0x11911111

    aput v0, p0, v11

    const/16 v0, 0x11

    aput v1, p0, v0

    const/16 v0, 0x12

    const v2, 0x11cccc11

    aput v2, p0, v0

    const/16 v0, 0x13

    aput v1, p0, v0

    const/16 v0, 0x14

    const v2, 0x11c11111

    aput v2, p0, v0

    const/16 v0, 0x15

    aput v1, p0, v0

    const/16 v0, 0x16

    const v2, 0x111ccc11

    aput v2, p0, v0

    const/16 v0, 0x17

    aput v1, p0, v0

    const/16 v0, 0x18

    const v2, 0x11000011

    aput v2, p0, v0

    const/16 v0, 0x19

    aput v1, p0, v0

    const-string p0, "UTF-8"

    sput-object p0, Ld/a/a/a/z;->eo:Ljava/lang/String;

    sput v11, Ld/a/a/a/z;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/z;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/z;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/z;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/z;->states:[I

    return-object p0
.end method
