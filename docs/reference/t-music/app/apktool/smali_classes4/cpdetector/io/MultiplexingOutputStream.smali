.class public Lcpdetector/io/MultiplexingOutputStream;
.super Ljava/io/OutputStream;
.source ""


# instance fields
.field public m_delegates:Ljava/util/List;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/List<",
            "Ljava/io/OutputStream;",
            ">;"
        }
    .end annotation
.end field


# direct methods
.method public constructor <init>(Ljava/io/OutputStream;Ljava/io/OutputStream;)V
    .locals 1

    invoke-direct {p0}, Ljava/io/OutputStream;-><init>()V

    new-instance v0, Ljava/util/LinkedList;

    invoke-direct {v0}, Ljava/util/LinkedList;-><init>()V

    iput-object v0, p0, Lcpdetector/io/MultiplexingOutputStream;->m_delegates:Ljava/util/List;

    iget-object v0, p0, Lcpdetector/io/MultiplexingOutputStream;->m_delegates:Ljava/util/List;

    invoke-interface {v0, p1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    iget-object p0, p0, Lcpdetector/io/MultiplexingOutputStream;->m_delegates:Ljava/util/List;

    invoke-interface {p0, p2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    return-void
.end method


# virtual methods
.method public addOutputStream(Ljava/io/OutputStream;)V
    .locals 0

    iget-object p0, p0, Lcpdetector/io/MultiplexingOutputStream;->m_delegates:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public removeOutputStream(Ljava/io/OutputStream;)Z
    .locals 0

    iget-object p0, p0, Lcpdetector/io/MultiplexingOutputStream;->m_delegates:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->remove(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public write(I)V
    .locals 1

    iget-object p0, p0, Lcpdetector/io/MultiplexingOutputStream;->m_delegates:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/io/OutputStream;

    invoke-virtual {v0, p1}, Ljava/io/OutputStream;->write(I)V

    goto :goto_0

    :cond_0
    return-void
.end method
