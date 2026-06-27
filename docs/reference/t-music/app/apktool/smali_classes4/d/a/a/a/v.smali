.class public abstract Ld/a/a/a/v;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public Un:[Ld/a/a/a/A;

.field public Vn:[Ld/a/a/a/l;

.field public Wn:Ld/a/a/a/k;

.field public Xn:[I

.field public Yn:I

.field public Zn:Z

.field public _n:Z

.field public mDone:Z

.field public mItems:I

.field public mState:[B


# direct methods
.method public constructor <init>(I)V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ld/a/a/a/k;

    invoke-direct {v0}, Ld/a/a/a/k;-><init>()V

    iput-object v0, p0, Ld/a/a/a/v;->Wn:Ld/a/a/a/k;

    const/16 v0, 0x10

    new-array v1, v0, [B

    iput-object v1, p0, Ld/a/a/a/v;->mState:[B

    new-array v0, v0, [I

    iput-object v0, p0, Ld/a/a/a/v;->Xn:[I

    invoke-virtual {p0, p1}, Ld/a/a/a/v;->Aa(I)V

    invoke-virtual {p0}, Ld/a/a/a/v;->Reset()V

    return-void
.end method


# virtual methods
.method public Aa(I)V
    .locals 19

    move-object/from16 v0, p0

    move/from16 v1, p1

    const/4 v2, 0x6

    const/4 v3, 0x0

    if-ltz v1, :cond_0

    if-ge v1, v2, :cond_0

    goto :goto_0

    :cond_0
    move v1, v3

    :goto_0
    const/4 v4, 0x0

    iput-object v4, v0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iput-object v4, v0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    const/4 v5, 0x7

    const/4 v6, 0x3

    const/4 v7, 0x2

    const/4 v8, 0x5

    const/4 v9, 0x4

    const/4 v10, 0x1

    if-ne v1, v9, :cond_1

    new-array v1, v5, [Ld/a/a/a/A;

    new-instance v11, Ld/a/a/a/z;

    invoke-direct {v11}, Ld/a/a/a/z;-><init>()V

    aput-object v11, v1, v3

    new-instance v11, Ld/a/a/a/f;

    invoke-direct {v11}, Ld/a/a/a/f;-><init>()V

    aput-object v11, v1, v10

    new-instance v11, Ld/a/a/a/s;

    invoke-direct {v11}, Ld/a/a/a/s;-><init>()V

    aput-object v11, v1, v7

    new-instance v11, Ld/a/a/a/m;

    invoke-direct {v11}, Ld/a/a/a/m;-><init>()V

    aput-object v11, v1, v6

    new-instance v11, Ld/a/a/a/g;

    invoke-direct {v11}, Ld/a/a/a/g;-><init>()V

    aput-object v11, v1, v9

    new-instance v11, Ld/a/a/a/x;

    invoke-direct {v11}, Ld/a/a/a/x;-><init>()V

    aput-object v11, v1, v8

    new-instance v11, Ld/a/a/a/y;

    invoke-direct {v11}, Ld/a/a/a/y;-><init>()V

    aput-object v11, v1, v2

    iput-object v1, v0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    new-array v1, v5, [Ld/a/a/a/l;

    aput-object v4, v1, v3

    new-instance v5, Ld/a/a/a/a;

    invoke-direct {v5}, Ld/a/a/a/a;-><init>()V

    aput-object v5, v1, v10

    aput-object v4, v1, v7

    new-instance v5, Ld/a/a/a/d;

    invoke-direct {v5}, Ld/a/a/a/d;-><init>()V

    aput-object v5, v1, v6

    aput-object v4, v1, v9

    aput-object v4, v1, v8

    aput-object v4, v1, v2

    :goto_1
    iput-object v1, v0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    goto/16 :goto_3

    :cond_1
    if-ne v1, v8, :cond_2

    new-array v1, v2, [Ld/a/a/a/A;

    new-instance v2, Ld/a/a/a/z;

    invoke-direct {v2}, Ld/a/a/a/z;-><init>()V

    aput-object v2, v1, v3

    new-instance v2, Ld/a/a/a/j;

    invoke-direct {v2}, Ld/a/a/a/j;-><init>()V

    aput-object v2, v1, v10

    new-instance v2, Ld/a/a/a/u;

    invoke-direct {v2}, Ld/a/a/a/u;-><init>()V

    aput-object v2, v1, v7

    new-instance v2, Ld/a/a/a/g;

    invoke-direct {v2}, Ld/a/a/a/g;-><init>()V

    aput-object v2, v1, v6

    new-instance v2, Ld/a/a/a/x;

    invoke-direct {v2}, Ld/a/a/a/x;-><init>()V

    aput-object v2, v1, v9

    new-instance v2, Ld/a/a/a/y;

    invoke-direct {v2}, Ld/a/a/a/y;-><init>()V

    aput-object v2, v1, v8

    :goto_2
    iput-object v1, v0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    goto/16 :goto_3

    :cond_2
    const/16 v11, 0x8

    if-ne v1, v6, :cond_3

    new-array v1, v11, [Ld/a/a/a/A;

    new-instance v4, Ld/a/a/a/z;

    invoke-direct {v4}, Ld/a/a/a/z;-><init>()V

    aput-object v4, v1, v3

    new-instance v4, Ld/a/a/a/o;

    invoke-direct {v4}, Ld/a/a/a/o;-><init>()V

    aput-object v4, v1, v10

    new-instance v4, Ld/a/a/a/n;

    invoke-direct {v4}, Ld/a/a/a/n;-><init>()V

    aput-object v4, v1, v7

    new-instance v4, Ld/a/a/a/s;

    invoke-direct {v4}, Ld/a/a/a/s;-><init>()V

    aput-object v4, v1, v6

    new-instance v4, Ld/a/a/a/p;

    invoke-direct {v4}, Ld/a/a/a/p;-><init>()V

    aput-object v4, v1, v9

    new-instance v4, Ld/a/a/a/g;

    invoke-direct {v4}, Ld/a/a/a/g;-><init>()V

    aput-object v4, v1, v8

    new-instance v4, Ld/a/a/a/x;

    invoke-direct {v4}, Ld/a/a/a/x;-><init>()V

    aput-object v4, v1, v2

    new-instance v2, Ld/a/a/a/y;

    invoke-direct {v2}, Ld/a/a/a/y;-><init>()V

    aput-object v2, v1, v5

    goto :goto_2

    :cond_3
    if-ne v1, v10, :cond_4

    new-array v1, v5, [Ld/a/a/a/A;

    new-instance v4, Ld/a/a/a/z;

    invoke-direct {v4}, Ld/a/a/a/z;-><init>()V

    aput-object v4, v1, v3

    new-instance v4, Ld/a/a/a/w;

    invoke-direct {v4}, Ld/a/a/a/w;-><init>()V

    aput-object v4, v1, v10

    new-instance v4, Ld/a/a/a/i;

    invoke-direct {v4}, Ld/a/a/a/i;-><init>()V

    aput-object v4, v1, v7

    new-instance v4, Ld/a/a/a/t;

    invoke-direct {v4}, Ld/a/a/a/t;-><init>()V

    aput-object v4, v1, v6

    new-instance v4, Ld/a/a/a/g;

    invoke-direct {v4}, Ld/a/a/a/g;-><init>()V

    aput-object v4, v1, v9

    new-instance v4, Ld/a/a/a/x;

    invoke-direct {v4}, Ld/a/a/a/x;-><init>()V

    aput-object v4, v1, v8

    new-instance v4, Ld/a/a/a/y;

    invoke-direct {v4}, Ld/a/a/a/y;-><init>()V

    aput-object v4, v1, v2

    goto :goto_2

    :cond_4
    const/16 v12, 0x9

    const/16 v13, 0xa

    if-ne v1, v7, :cond_5

    new-array v1, v13, [Ld/a/a/a/A;

    new-instance v14, Ld/a/a/a/z;

    invoke-direct {v14}, Ld/a/a/a/z;-><init>()V

    aput-object v14, v1, v3

    new-instance v14, Ld/a/a/a/o;

    invoke-direct {v14}, Ld/a/a/a/o;-><init>()V

    aput-object v14, v1, v10

    new-instance v14, Ld/a/a/a/n;

    invoke-direct {v14}, Ld/a/a/a/n;-><init>()V

    aput-object v14, v1, v7

    new-instance v14, Ld/a/a/a/f;

    invoke-direct {v14}, Ld/a/a/a/f;-><init>()V

    aput-object v14, v1, v6

    new-instance v14, Ld/a/a/a/s;

    invoke-direct {v14}, Ld/a/a/a/s;-><init>()V

    aput-object v14, v1, v9

    new-instance v14, Ld/a/a/a/p;

    invoke-direct {v14}, Ld/a/a/a/p;-><init>()V

    aput-object v14, v1, v8

    new-instance v14, Ld/a/a/a/m;

    invoke-direct {v14}, Ld/a/a/a/m;-><init>()V

    aput-object v14, v1, v2

    new-instance v14, Ld/a/a/a/g;

    invoke-direct {v14}, Ld/a/a/a/g;-><init>()V

    aput-object v14, v1, v5

    new-instance v14, Ld/a/a/a/x;

    invoke-direct {v14}, Ld/a/a/a/x;-><init>()V

    aput-object v14, v1, v11

    new-instance v14, Ld/a/a/a/y;

    invoke-direct {v14}, Ld/a/a/a/y;-><init>()V

    aput-object v14, v1, v12

    iput-object v1, v0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    new-array v1, v13, [Ld/a/a/a/l;

    aput-object v4, v1, v3

    new-instance v13, Ld/a/a/a/e;

    invoke-direct {v13}, Ld/a/a/a/e;-><init>()V

    aput-object v13, v1, v10

    aput-object v4, v1, v7

    new-instance v7, Ld/a/a/a/a;

    invoke-direct {v7}, Ld/a/a/a/a;-><init>()V

    aput-object v7, v1, v6

    aput-object v4, v1, v9

    aput-object v4, v1, v8

    new-instance v6, Ld/a/a/a/d;

    invoke-direct {v6}, Ld/a/a/a/d;-><init>()V

    aput-object v6, v1, v2

    aput-object v4, v1, v5

    aput-object v4, v1, v11

    aput-object v4, v1, v12

    goto/16 :goto_1

    :cond_5
    if-nez v1, :cond_6

    const/16 v1, 0xf

    new-array v14, v1, [Ld/a/a/a/A;

    new-instance v15, Ld/a/a/a/z;

    invoke-direct {v15}, Ld/a/a/a/z;-><init>()V

    aput-object v15, v14, v3

    new-instance v15, Ld/a/a/a/w;

    invoke-direct {v15}, Ld/a/a/a/w;-><init>()V

    aput-object v15, v14, v10

    new-instance v15, Ld/a/a/a/i;

    invoke-direct {v15}, Ld/a/a/a/i;-><init>()V

    aput-object v15, v14, v7

    new-instance v15, Ld/a/a/a/t;

    invoke-direct {v15}, Ld/a/a/a/t;-><init>()V

    aput-object v15, v14, v6

    new-instance v15, Ld/a/a/a/j;

    invoke-direct {v15}, Ld/a/a/a/j;-><init>()V

    aput-object v15, v14, v9

    new-instance v15, Ld/a/a/a/u;

    invoke-direct {v15}, Ld/a/a/a/u;-><init>()V

    aput-object v15, v14, v8

    new-instance v15, Ld/a/a/a/f;

    invoke-direct {v15}, Ld/a/a/a/f;-><init>()V

    aput-object v15, v14, v2

    new-instance v15, Ld/a/a/a/m;

    invoke-direct {v15}, Ld/a/a/a/m;-><init>()V

    aput-object v15, v14, v5

    new-instance v15, Ld/a/a/a/o;

    invoke-direct {v15}, Ld/a/a/a/o;-><init>()V

    aput-object v15, v14, v11

    new-instance v15, Ld/a/a/a/n;

    invoke-direct {v15}, Ld/a/a/a/n;-><init>()V

    aput-object v15, v14, v12

    new-instance v15, Ld/a/a/a/s;

    invoke-direct {v15}, Ld/a/a/a/s;-><init>()V

    aput-object v15, v14, v13

    new-instance v15, Ld/a/a/a/p;

    invoke-direct {v15}, Ld/a/a/a/p;-><init>()V

    const/16 v16, 0xb

    aput-object v15, v14, v16

    new-instance v15, Ld/a/a/a/g;

    invoke-direct {v15}, Ld/a/a/a/g;-><init>()V

    const/16 v17, 0xc

    aput-object v15, v14, v17

    new-instance v15, Ld/a/a/a/x;

    invoke-direct {v15}, Ld/a/a/a/x;-><init>()V

    const/16 v18, 0xd

    aput-object v15, v14, v18

    new-instance v15, Ld/a/a/a/y;

    invoke-direct {v15}, Ld/a/a/a/y;-><init>()V

    const/16 v18, 0xe

    aput-object v15, v14, v18

    iput-object v14, v0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    new-array v1, v1, [Ld/a/a/a/l;

    aput-object v4, v1, v3

    aput-object v4, v1, v10

    new-instance v14, Ld/a/a/a/b;

    invoke-direct {v14}, Ld/a/a/a/b;-><init>()V

    aput-object v14, v1, v7

    aput-object v4, v1, v6

    new-instance v6, Ld/a/a/a/c;

    invoke-direct {v6}, Ld/a/a/a/c;-><init>()V

    aput-object v6, v1, v9

    aput-object v4, v1, v8

    new-instance v6, Ld/a/a/a/a;

    invoke-direct {v6}, Ld/a/a/a/a;-><init>()V

    aput-object v6, v1, v2

    new-instance v2, Ld/a/a/a/d;

    invoke-direct {v2}, Ld/a/a/a/d;-><init>()V

    aput-object v2, v1, v5

    new-instance v2, Ld/a/a/a/e;

    invoke-direct {v2}, Ld/a/a/a/e;-><init>()V

    aput-object v2, v1, v11

    aput-object v4, v1, v12

    aput-object v4, v1, v13

    aput-object v4, v1, v16

    aput-object v4, v1, v17

    const/16 v2, 0xd

    aput-object v4, v1, v2

    const/16 v2, 0xe

    aput-object v4, v1, v2

    goto/16 :goto_1

    :cond_6
    :goto_3
    iget-object v1, v0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    if-eqz v1, :cond_7

    move v3, v10

    :cond_7
    iput-boolean v3, v0, Ld/a/a/a/v;->_n:Z

    iget-object v1, v0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    array-length v1, v1

    iput v1, v0, Ld/a/a/a/v;->Yn:I

    return-void
.end method

.method public Reset()V
    .locals 3

    iget-boolean v0, p0, Ld/a/a/a/v;->_n:Z

    iput-boolean v0, p0, Ld/a/a/a/v;->Zn:Z

    const/4 v0, 0x0

    iput-boolean v0, p0, Ld/a/a/a/v;->mDone:Z

    iget v1, p0, Ld/a/a/a/v;->Yn:I

    iput v1, p0, Ld/a/a/a/v;->mItems:I

    move v1, v0

    :goto_0
    iget v2, p0, Ld/a/a/a/v;->mItems:I

    if-ge v1, v2, :cond_0

    iget-object v2, p0, Ld/a/a/a/v;->mState:[B

    aput-byte v0, v2, v1

    iget-object v2, p0, Ld/a/a/a/v;->Xn:[I

    aput v1, v2, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    iget-object p0, p0, Ld/a/a/a/v;->Wn:Ld/a/a/a/k;

    invoke-virtual {p0}, Ld/a/a/a/k;->Reset()V

    return-void
.end method

.method public a([BIZ)V
    .locals 8

    const/4 v0, 0x0

    move v1, v0

    move v2, v1

    move v3, v2

    :goto_0
    iget v4, p0, Ld/a/a/a/v;->mItems:I

    if-ge v1, v4, :cond_2

    iget-object v4, p0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    iget-object v5, p0, Ld/a/a/a/v;->Xn:[I

    aget v5, v5, v1

    aget-object v4, v4, v5

    if-eqz v4, :cond_0

    add-int/lit8 v2, v2, 0x1

    :cond_0
    iget-object v4, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v5, p0, Ld/a/a/a/v;->Xn:[I

    aget v5, v5, v1

    aget-object v4, v4, v5

    invoke-virtual {v4}, Ld/a/a/a/A;->ie()Z

    move-result v4

    if-nez v4, :cond_1

    iget-object v4, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v5, p0, Ld/a/a/a/v;->Xn:[I

    aget v5, v5, v1

    aget-object v4, v4, v5

    invoke-virtual {v4}, Ld/a/a/a/A;->charset()Ljava/lang/String;

    move-result-object v4

    const-string v5, "GB18030"

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-nez v4, :cond_1

    add-int/lit8 v3, v3, 0x1

    :cond_1
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    const/4 v1, 0x1

    if-le v2, v1, :cond_3

    move v4, v1

    goto :goto_1

    :cond_3
    move v4, v0

    :goto_1
    iput-boolean v4, p0, Ld/a/a/a/v;->Zn:Z

    iget-boolean v4, p0, Ld/a/a/a/v;->Zn:Z

    if-eqz v4, :cond_a

    iget-object v4, p0, Ld/a/a/a/v;->Wn:Ld/a/a/a/k;

    invoke-virtual {v4, p1, p2}, Ld/a/a/a/k;->h([BI)Z

    move-result p1

    iput-boolean p1, p0, Ld/a/a/a/v;->Zn:Z

    if-eqz p3, :cond_4

    iget-object p1, p0, Ld/a/a/a/v;->Wn:Ld/a/a/a/k;

    invoke-virtual {p1}, Ld/a/a/a/k;->ae()Z

    move-result p1

    if-nez p1, :cond_5

    :cond_4
    iget-object p1, p0, Ld/a/a/a/v;->Wn:Ld/a/a/a/k;

    invoke-virtual {p1}, Ld/a/a/a/k;->_d()Z

    move-result p1

    if-eqz p1, :cond_a

    :cond_5
    if-ne v2, v3, :cond_a

    iget-object p1, p0, Ld/a/a/a/v;->Wn:Ld/a/a/a/k;

    invoke-virtual {p1}, Ld/a/a/a/k;->Zd()V

    const/4 p1, -0x1

    const/4 p2, 0x0

    move p3, p2

    move p2, v0

    :goto_2
    iget v2, p0, Ld/a/a/a/v;->mItems:I

    if-ge v0, v2, :cond_9

    iget-object v2, p0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    iget-object v3, p0, Ld/a/a/a/v;->Xn:[I

    aget v4, v3, v0

    aget-object v2, v2, v4

    if-eqz v2, :cond_8

    iget-object v2, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    aget v3, v3, v0

    aget-object v2, v2, v3

    invoke-virtual {v2}, Ld/a/a/a/A;->charset()Ljava/lang/String;

    move-result-object v2

    const-string v3, "Big5"

    invoke-virtual {v2, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_8

    iget-object v2, p0, Ld/a/a/a/v;->Wn:Ld/a/a/a/k;

    iget-object v3, p0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    iget-object v4, p0, Ld/a/a/a/v;->Xn:[I

    aget v4, v4, v0

    aget-object v3, v3, v4

    invoke-virtual {v3}, Ld/a/a/a/l;->be()[F

    move-result-object v3

    iget-object v4, p0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    iget-object v5, p0, Ld/a/a/a/v;->Xn:[I

    aget v5, v5, v0

    aget-object v4, v4, v5

    invoke-virtual {v4}, Ld/a/a/a/l;->ce()F

    move-result v4

    iget-object v5, p0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    iget-object v6, p0, Ld/a/a/a/v;->Xn:[I

    aget v6, v6, v0

    aget-object v5, v5, v6

    invoke-virtual {v5}, Ld/a/a/a/l;->de()[F

    move-result-object v5

    iget-object v6, p0, Ld/a/a/a/v;->Vn:[Ld/a/a/a/l;

    iget-object v7, p0, Ld/a/a/a/v;->Xn:[I

    aget v7, v7, v0

    aget-object v6, v6, v7

    invoke-virtual {v6}, Ld/a/a/a/l;->ee()F

    move-result v6

    invoke-virtual {v2, v3, v4, v5, v6}, Ld/a/a/a/k;->a([FF[FF)F

    move-result v2

    add-int/lit8 v3, p2, 0x1

    if-eqz p2, :cond_6

    cmpl-float p2, p3, v2

    if-lez p2, :cond_7

    :cond_6
    move p1, v0

    move p3, v2

    :cond_7
    move p2, v3

    :cond_8
    add-int/lit8 v0, v0, 0x1

    goto :goto_2

    :cond_9
    if-ltz p1, :cond_a

    iget-object p2, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object p3, p0, Ld/a/a/a/v;->Xn:[I

    aget p1, p3, p1

    aget-object p1, p2, p1

    invoke-virtual {p1}, Ld/a/a/a/A;->charset()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ld/a/a/a/v;->tb(Ljava/lang/String;)V

    iput-boolean v1, p0, Ld/a/a/a/v;->mDone:Z

    :cond_a
    return-void
.end method

.method public fe()V
    .locals 5

    iget-boolean v0, p0, Ld/a/a/a/v;->mDone:Z

    const/4 v1, 0x1

    if-ne v0, v1, :cond_0

    return-void

    :cond_0
    iget v0, p0, Ld/a/a/a/v;->mItems:I

    const/4 v2, 0x2

    const/4 v3, 0x0

    if-ne v0, v2, :cond_2

    iget-object v0, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v2, p0, Ld/a/a/a/v;->Xn:[I

    aget v2, v2, v3

    aget-object v0, v0, v2

    invoke-virtual {v0}, Ld/a/a/a/A;->charset()Ljava/lang/String;

    move-result-object v0

    const-string v2, "GB18030"

    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1

    iget-object v0, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v2, p0, Ld/a/a/a/v;->Xn:[I

    aget v2, v2, v1

    aget-object v0, v0, v2

    :goto_0
    invoke-virtual {v0}, Ld/a/a/a/A;->charset()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ld/a/a/a/v;->tb(Ljava/lang/String;)V

    iput-boolean v1, p0, Ld/a/a/a/v;->mDone:Z

    goto :goto_1

    :cond_1
    iget-object v0, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v4, p0, Ld/a/a/a/v;->Xn:[I

    aget v4, v4, v1

    aget-object v0, v0, v4

    invoke-virtual {v0}, Ld/a/a/a/A;->charset()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_2

    iget-object v0, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v2, p0, Ld/a/a/a/v;->Xn:[I

    aget v2, v2, v3

    aget-object v0, v0, v2

    goto :goto_0

    :cond_2
    :goto_1
    iget-boolean v0, p0, Ld/a/a/a/v;->Zn:Z

    if-eqz v0, :cond_3

    const/4 v0, 0x0

    invoke-virtual {p0, v0, v3, v1}, Ld/a/a/a/v;->a([BIZ)V

    :cond_3
    return-void
.end method

.method public ge()[Ljava/lang/String;
    .locals 4

    iget v0, p0, Ld/a/a/a/v;->mItems:I

    if-gtz v0, :cond_0

    const-string p0, "nomatch"

    filled-new-array {p0}, [Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    new-array v0, v0, [Ljava/lang/String;

    const/4 v1, 0x0

    :goto_0
    iget v2, p0, Ld/a/a/a/v;->mItems:I

    if-ge v1, v2, :cond_1

    iget-object v2, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v3, p0, Ld/a/a/a/v;->Xn:[I

    aget v3, v3, v1

    aget-object v2, v2, v3

    invoke-virtual {v2}, Ld/a/a/a/A;->charset()Ljava/lang/String;

    move-result-object v2

    aput-object v2, v0, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    return-object v0
.end method

.method public h([BI)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, p2, v0}, Ld/a/a/a/v;->a([BIZ)V

    return-void
.end method

.method public i([BI)Z
    .locals 8

    const/4 v0, 0x0

    move v1, v0

    :goto_0
    if-ge v1, p2, :cond_9

    aget-byte v2, p1, v1

    move v3, v0

    :cond_0
    :goto_1
    iget v4, p0, Ld/a/a/a/v;->mItems:I

    const/4 v5, 0x1

    if-ge v3, v4, :cond_4

    iget-object v4, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v6, p0, Ld/a/a/a/v;->Xn:[I

    aget v6, v6, v3

    aget-object v4, v4, v6

    iget-object v6, p0, Ld/a/a/a/v;->mState:[B

    aget-byte v6, v6, v3

    invoke-static {v4, v2, v6}, Ld/a/a/a/A;->a(Ld/a/a/a/A;BB)B

    move-result v4

    const/4 v6, 0x2

    if-ne v4, v6, :cond_2

    iget-object p1, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object p2, p0, Ld/a/a/a/v;->Xn:[I

    aget p2, p2, v3

    aget-object p1, p1, p2

    :goto_2
    invoke-virtual {p1}, Ld/a/a/a/A;->charset()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Ld/a/a/a/v;->tb(Ljava/lang/String;)V

    :cond_1
    iput-boolean v5, p0, Ld/a/a/a/v;->mDone:Z

    iget-boolean p0, p0, Ld/a/a/a/v;->mDone:Z

    return p0

    :cond_2
    if-ne v4, v5, :cond_3

    iget v4, p0, Ld/a/a/a/v;->mItems:I

    sub-int/2addr v4, v5

    iput v4, p0, Ld/a/a/a/v;->mItems:I

    iget v4, p0, Ld/a/a/a/v;->mItems:I

    if-ge v3, v4, :cond_0

    iget-object v5, p0, Ld/a/a/a/v;->Xn:[I

    aget v6, v5, v4

    aput v6, v5, v3

    iget-object v5, p0, Ld/a/a/a/v;->mState:[B

    aget-byte v4, v5, v4

    aput-byte v4, v5, v3

    goto :goto_1

    :cond_3
    iget-object v5, p0, Ld/a/a/a/v;->mState:[B

    add-int/lit8 v6, v3, 0x1

    aput-byte v4, v5, v3

    move v3, v6

    goto :goto_1

    :cond_4
    if-gt v4, v5, :cond_5

    if-ne v5, v4, :cond_1

    iget-object p1, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object p2, p0, Ld/a/a/a/v;->Xn:[I

    aget p2, p2, v0

    aget-object p1, p1, p2

    goto :goto_2

    :cond_5
    move v2, v0

    move v3, v2

    move v4, v3

    :goto_3
    iget v6, p0, Ld/a/a/a/v;->mItems:I

    if-ge v2, v6, :cond_7

    iget-object v6, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v7, p0, Ld/a/a/a/v;->Xn:[I

    aget v7, v7, v2

    aget-object v6, v6, v7

    invoke-virtual {v6}, Ld/a/a/a/A;->ie()Z

    move-result v6

    if-nez v6, :cond_6

    iget-object v6, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object v7, p0, Ld/a/a/a/v;->Xn:[I

    aget v7, v7, v2

    aget-object v6, v6, v7

    invoke-virtual {v6}, Ld/a/a/a/A;->ie()Z

    move-result v6

    if-nez v6, :cond_6

    add-int/lit8 v3, v3, 0x1

    move v4, v2

    :cond_6
    add-int/lit8 v2, v2, 0x1

    goto :goto_3

    :cond_7
    if-ne v5, v3, :cond_8

    iget-object p1, p0, Ld/a/a/a/v;->Un:[Ld/a/a/a/A;

    iget-object p2, p0, Ld/a/a/a/v;->Xn:[I

    aget p2, p2, v4

    aget-object p1, p1, p2

    goto :goto_2

    :cond_8
    add-int/lit8 v1, v1, 0x1

    goto/16 :goto_0

    :cond_9
    iget-boolean v0, p0, Ld/a/a/a/v;->Zn:Z

    if-eqz v0, :cond_a

    invoke-virtual {p0, p1, p2}, Ld/a/a/a/v;->h([BI)V

    :cond_a
    iget-boolean p0, p0, Ld/a/a/a/v;->mDone:Z

    return p0
.end method

.method public abstract tb(Ljava/lang/String;)V
.end method
