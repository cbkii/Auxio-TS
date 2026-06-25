.class public Lantlr/TokenStreamRewriteEngine$1;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/util/Comparator;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lantlr/TokenStreamRewriteEngine;->addToSortedRewriteList(Ljava/lang/String;Lantlr/TokenStreamRewriteEngine$RewriteOperation;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x1
    name = null
.end annotation


# instance fields
.field public final synthetic this$0:Lantlr/TokenStreamRewriteEngine;


# direct methods
.method public constructor <init>(Lantlr/TokenStreamRewriteEngine;)V
    .locals 0

    iput-object p1, p0, Lantlr/TokenStreamRewriteEngine$1;->this$0:Lantlr/TokenStreamRewriteEngine;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public compare(Ljava/lang/Object;Ljava/lang/Object;)I
    .locals 0

    check-cast p1, Lantlr/TokenStreamRewriteEngine$RewriteOperation;

    check-cast p2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;

    iget p0, p1, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    iget p1, p2, Lantlr/TokenStreamRewriteEngine$RewriteOperation;->index:I

    if-ge p0, p1, :cond_0

    const/4 p0, -0x1

    return p0

    :cond_0
    if-le p0, p1, :cond_1

    const/4 p0, 0x1

    return p0

    :cond_1
    const/4 p0, 0x0

    return p0
.end method
