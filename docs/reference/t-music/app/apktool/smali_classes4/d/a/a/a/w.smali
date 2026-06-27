.class public Ld/a/a/a/w;
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

    sput-object p0, Ld/a/a/a/w;->co:[I

    sget-object p0, Ld/a/a/a/w;->co:[I

    const/4 v0, 0x0

    const v1, 0x11111110

    aput v1, p0, v0

    const/4 v1, 0x1

    const v2, 0x111111

    aput v2, p0, v1

    const/4 v2, 0x2

    const v3, 0x11111111

    aput v3, p0, v2

    const/4 v4, 0x3

    const v5, 0x11110111

    aput v5, p0, v4

    const/4 v5, 0x4

    aput v3, p0, v5

    const/4 v5, 0x5

    aput v3, p0, v5

    const/4 v5, 0x6

    aput v3, p0, v5

    const/4 v6, 0x7

    aput v3, p0, v6

    const v3, 0x22222222

    const/16 v6, 0x8

    aput v3, p0, v6

    const/16 v6, 0x9

    aput v3, p0, v6

    const/16 v6, 0xa

    aput v3, p0, v6

    const/16 v6, 0xb

    aput v3, p0, v6

    const/16 v6, 0xc

    aput v3, p0, v6

    const/16 v6, 0xd

    aput v3, p0, v6

    const/16 v6, 0xe

    aput v3, p0, v6

    const/16 v6, 0xf

    const v7, 0x12222222

    aput v7, p0, v6

    const v6, 0x33333333

    const/16 v7, 0x10

    aput v6, p0, v7

    const/16 v7, 0x11

    aput v6, p0, v7

    const/16 v7, 0x12

    aput v6, p0, v7

    const/16 v7, 0x13

    aput v6, p0, v7

    const/16 v7, 0x14

    const v8, 0x22222224

    aput v8, p0, v7

    const/16 v7, 0x15

    aput v3, p0, v7

    const/16 v7, 0x16

    aput v3, p0, v7

    const/16 v7, 0x17

    aput v3, p0, v7

    const/16 v7, 0x18

    aput v3, p0, v7

    const/16 v7, 0x19

    aput v3, p0, v7

    const/16 v7, 0x1a

    aput v3, p0, v7

    const/16 v7, 0x1b

    aput v3, p0, v7

    const/16 v3, 0x1c

    aput v6, p0, v3

    const/16 v3, 0x1d

    const v6, 0x44455333    # 789.3f

    aput v6, p0, v3

    const/16 v3, 0x1e

    const v6, 0x44444444

    aput v6, p0, v3

    const/16 v3, 0x1f

    const v6, 0x44444

    aput v6, p0, v3

    new-array p0, v4, [I

    sput-object p0, Ld/a/a/a/w;->states:[I

    sget-object p0, Ld/a/a/a/w;->states:[I

    const v3, 0x11113001

    aput v3, p0, v0

    const v0, 0x22221111

    aput v0, p0, v1

    const/16 v0, 0x1122

    aput v0, p0, v2

    const-string p0, "Shift_JIS"

    sput-object p0, Ld/a/a/a/w;->eo:Ljava/lang/String;

    sput v5, Ld/a/a/a/w;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/w;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/w;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/w;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/w;->states:[I

    return-object p0
.end method
