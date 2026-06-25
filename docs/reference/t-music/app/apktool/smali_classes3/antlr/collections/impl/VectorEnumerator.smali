.class public Lantlr/collections/impl/VectorEnumerator;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/util/Enumeration;


# instance fields
.field public i:I

.field public vector:Lantlr/collections/impl/Vector;


# direct methods
.method public constructor <init>(Lantlr/collections/impl/Vector;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lantlr/collections/impl/VectorEnumerator;->vector:Lantlr/collections/impl/Vector;

    const/4 p1, 0x0

    iput p1, p0, Lantlr/collections/impl/VectorEnumerator;->i:I

    return-void
.end method


# virtual methods
.method public hasMoreElements()Z
    .locals 2

    iget-object v0, p0, Lantlr/collections/impl/VectorEnumerator;->vector:Lantlr/collections/impl/Vector;

    monitor-enter v0

    :try_start_0
    iget v1, p0, Lantlr/collections/impl/VectorEnumerator;->i:I

    iget-object p0, p0, Lantlr/collections/impl/VectorEnumerator;->vector:Lantlr/collections/impl/Vector;

    iget p0, p0, Lantlr/collections/impl/Vector;->lastElement:I

    if-gt v1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    monitor-exit v0

    return p0

    :catchall_0
    move-exception p0

    monitor-exit v0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    throw p0
.end method

.method public nextElement()Ljava/lang/Object;
    .locals 4

    iget-object v0, p0, Lantlr/collections/impl/VectorEnumerator;->vector:Lantlr/collections/impl/Vector;

    monitor-enter v0

    :try_start_0
    iget v1, p0, Lantlr/collections/impl/VectorEnumerator;->i:I

    iget-object v2, p0, Lantlr/collections/impl/VectorEnumerator;->vector:Lantlr/collections/impl/Vector;

    iget v2, v2, Lantlr/collections/impl/Vector;->lastElement:I

    if-gt v1, v2, :cond_0

    iget-object v1, p0, Lantlr/collections/impl/VectorEnumerator;->vector:Lantlr/collections/impl/Vector;

    iget-object v1, v1, Lantlr/collections/impl/Vector;->data:[Ljava/lang/Object;

    iget v2, p0, Lantlr/collections/impl/VectorEnumerator;->i:I

    add-int/lit8 v3, v2, 0x1

    iput v3, p0, Lantlr/collections/impl/VectorEnumerator;->i:I

    aget-object p0, v1, v2

    monitor-exit v0

    return-object p0

    :cond_0
    new-instance p0, Ljava/util/NoSuchElementException;

    const-string v1, "VectorEnumerator"

    invoke-direct {p0, v1}, Ljava/util/NoSuchElementException;-><init>(Ljava/lang/String;)V

    throw p0

    :catchall_0
    move-exception p0

    monitor-exit v0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    throw p0
.end method
