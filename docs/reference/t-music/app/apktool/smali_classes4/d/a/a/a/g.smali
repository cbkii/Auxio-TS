.class public Ld/a/a/a/g;
.super Ld/a/a/a/A;
.source ""


# static fields
.field public static co:[I

.field public static do:I

.field public static eo:Ljava/lang/String;

.field public static states:[I


# direct methods
.method public constructor <init>()V
    .locals 7

    invoke-direct {p0}, Ld/a/a/a/A;-><init>()V

    const/16 p0, 0x20

    new-array p0, p0, [I

    sput-object p0, Ld/a/a/a/g;->co:[I

    sget-object p0, Ld/a/a/a/g;->co:[I

    const/4 v0, 0x0

    const v1, 0x22222221

    aput v1, p0, v0

    const/4 v1, 0x1

    const v2, 0x222222

    aput v2, p0, v1

    const/4 v2, 0x2

    const v3, 0x22222222

    aput v3, p0, v2

    const/4 v4, 0x3

    const v5, 0x22220222

    aput v5, p0, v4

    const/4 v5, 0x4

    aput v3, p0, v5

    const/4 v5, 0x5

    aput v3, p0, v5

    const/4 v5, 0x6

    aput v3, p0, v5

    const/4 v5, 0x7

    aput v3, p0, v5

    const/16 v5, 0x8

    aput v3, p0, v5

    const/16 v5, 0x9

    aput v3, p0, v5

    const/16 v5, 0xa

    aput v3, p0, v5

    const/16 v5, 0xb

    aput v3, p0, v5

    const/16 v5, 0xc

    aput v3, p0, v5

    const/16 v5, 0xd

    aput v3, p0, v5

    const/16 v5, 0xe

    aput v3, p0, v5

    const/16 v5, 0xf

    aput v3, p0, v5

    const/16 v5, 0x10

    const v6, 0x22222202

    aput v6, p0, v5

    const/16 v5, 0x11

    const v6, 0x1012122

    aput v6, p0, v5

    const/16 v5, 0x12

    const v6, 0x22222220

    aput v6, p0, v5

    const/16 v5, 0x13

    const v6, 0x11012122

    aput v6, p0, v5

    const/16 v5, 0x14

    aput v3, p0, v5

    const/16 v5, 0x15

    aput v3, p0, v5

    const/16 v5, 0x16

    aput v3, p0, v5

    const/16 v5, 0x17

    aput v3, p0, v5

    const v3, 0x11111111

    const/16 v5, 0x18

    aput v3, p0, v5

    const/16 v5, 0x19

    aput v3, p0, v5

    const v5, 0x21111111

    const/16 v6, 0x1a

    aput v5, p0, v6

    const/16 v6, 0x1b

    aput v3, p0, v6

    const/16 v6, 0x1c

    aput v3, p0, v6

    const/16 v6, 0x1d

    aput v3, p0, v6

    const/16 v6, 0x1e

    aput v5, p0, v6

    const/16 v5, 0x1f

    aput v3, p0, v5

    new-array p0, v4, [I

    sput-object p0, Ld/a/a/a/g;->states:[I

    sget-object p0, Ld/a/a/a/g;->states:[I

    const v3, 0x22111031

    aput v3, p0, v0

    const v0, 0x14510412

    aput v0, p0, v1

    const/16 v0, 0x41

    aput v0, p0, v2

    const-string p0, "windows-1252"

    sput-object p0, Ld/a/a/a/g;->eo:Ljava/lang/String;

    sput v4, Ld/a/a/a/g;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/g;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/g;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/g;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/g;->states:[I

    return-object p0
.end method
