.class public Ld/a/a/a/x;
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

    sput-object p0, Ld/a/a/a/x;->co:[I

    sget-object p0, Ld/a/a/a/x;->co:[I

    const/4 v0, 0x0

    aput v0, p0, v0

    const/4 v1, 0x1

    const v2, 0x200100

    aput v2, p0, v1

    const/4 v2, 0x2

    aput v0, p0, v2

    const/4 v3, 0x3

    const/16 v4, 0x3000

    aput v4, p0, v3

    const/4 v4, 0x4

    aput v0, p0, v4

    const/4 v5, 0x5

    const v6, 0x333330

    aput v6, p0, v5

    const/4 v6, 0x6

    aput v0, p0, v6

    const/4 v7, 0x7

    aput v0, p0, v7

    const/16 v8, 0x8

    aput v0, p0, v8

    const/16 v8, 0x9

    aput v0, p0, v8

    const/16 v8, 0xa

    aput v0, p0, v8

    const/16 v8, 0xb

    aput v0, p0, v8

    const/16 v8, 0xc

    aput v0, p0, v8

    const/16 v8, 0xd

    aput v0, p0, v8

    const/16 v8, 0xe

    aput v0, p0, v8

    const/16 v8, 0xf

    aput v0, p0, v8

    const/16 v8, 0x10

    aput v0, p0, v8

    const/16 v8, 0x11

    aput v0, p0, v8

    const/16 v8, 0x12

    aput v0, p0, v8

    const/16 v8, 0x13

    aput v0, p0, v8

    const/16 v8, 0x14

    aput v0, p0, v8

    const/16 v8, 0x15

    aput v0, p0, v8

    const/16 v8, 0x16

    aput v0, p0, v8

    const/16 v8, 0x17

    aput v0, p0, v8

    const/16 v8, 0x18

    aput v0, p0, v8

    const/16 v8, 0x19

    aput v0, p0, v8

    const/16 v8, 0x1a

    aput v0, p0, v8

    const/16 v8, 0x1b

    aput v0, p0, v8

    const/16 v8, 0x1c

    aput v0, p0, v8

    const/16 v8, 0x1d

    aput v0, p0, v8

    const/16 v8, 0x1e

    aput v0, p0, v8

    const/16 v8, 0x1f

    const/high16 v9, 0x54000000

    aput v9, p0, v8

    new-array p0, v7, [I

    sput-object p0, Ld/a/a/a/x;->states:[I

    sget-object p0, Ld/a/a/a/x;->states:[I

    const v7, 0x11341775

    aput v7, p0, v0

    const v0, 0x22221111

    aput v0, p0, v1

    const v0, 0x11666622

    aput v0, p0, v2

    const v0, 0x66266666

    aput v0, p0, v3

    const v0, 0x17756666

    aput v0, p0, v4

    const v0, 0x66616685

    aput v0, p0, v5

    const v0, 0x116666

    aput v0, p0, v6

    const-string p0, "UTF-16BE"

    sput-object p0, Ld/a/a/a/x;->eo:Ljava/lang/String;

    sput v6, Ld/a/a/a/x;->do:I

    return-void
.end method


# virtual methods
.method public charset()Ljava/lang/String;
    .locals 0

    sget-object p0, Ld/a/a/a/x;->eo:Ljava/lang/String;

    return-object p0
.end method

.method public he()[I
    .locals 0

    sget-object p0, Ld/a/a/a/x;->co:[I

    return-object p0
.end method

.method public ie()Z
    .locals 0

    const/4 p0, 0x1

    return p0
.end method

.method public je()I
    .locals 0

    sget p0, Ld/a/a/a/x;->do:I

    return p0
.end method

.method public ke()[I
    .locals 0

    sget-object p0, Ld/a/a/a/x;->states:[I

    return-object p0
.end method
