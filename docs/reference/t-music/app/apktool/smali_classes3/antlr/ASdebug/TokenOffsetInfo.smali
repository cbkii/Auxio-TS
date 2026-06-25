.class public Lantlr/ASdebug/TokenOffsetInfo;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public final beginOffset:I

.field public final length:I


# direct methods
.method public constructor <init>(II)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lantlr/ASdebug/TokenOffsetInfo;->beginOffset:I

    iput p2, p0, Lantlr/ASdebug/TokenOffsetInfo;->length:I

    return-void
.end method


# virtual methods
.method public getEndOffset()I
    .locals 1

    iget v0, p0, Lantlr/ASdebug/TokenOffsetInfo;->beginOffset:I

    iget p0, p0, Lantlr/ASdebug/TokenOffsetInfo;->length:I

    add-int/2addr v0, p0

    add-int/lit8 v0, v0, -0x1

    return v0
.end method
