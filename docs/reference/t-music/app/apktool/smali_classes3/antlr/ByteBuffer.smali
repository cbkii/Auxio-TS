.class public Lantlr/ByteBuffer;
.super Lantlr/InputBuffer;
.source ""


# instance fields
.field public transient input:Ljava/io/InputStream;


# direct methods
.method public constructor <init>(Ljava/io/InputStream;)V
    .locals 0

    invoke-direct {p0}, Lantlr/InputBuffer;-><init>()V

    iput-object p1, p0, Lantlr/ByteBuffer;->input:Ljava/io/InputStream;

    return-void
.end method


# virtual methods
.method public fill(I)V
    .locals 2

    :try_start_0
    invoke-virtual {p0}, Lantlr/InputBuffer;->syncConsume()V

    :goto_0
    iget-object v0, p0, Lantlr/InputBuffer;->queue:Lantlr/CharQueue;

    iget v0, v0, Lantlr/CharQueue;->nbrEntries:I

    iget v1, p0, Lantlr/InputBuffer;->markerOffset:I

    add-int/2addr v1, p1

    if-ge v0, v1, :cond_0

    iget-object v0, p0, Lantlr/InputBuffer;->queue:Lantlr/CharQueue;

    iget-object v1, p0, Lantlr/ByteBuffer;->input:Ljava/io/InputStream;

    invoke-virtual {v1}, Ljava/io/InputStream;->read()I

    move-result v1

    int-to-char v1, v1

    invoke-virtual {v0, v1}, Lantlr/CharQueue;->append(C)V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :cond_0
    return-void

    :catch_0
    move-exception p0

    new-instance p1, Lantlr/CharStreamIOException;

    invoke-direct {p1, p0}, Lantlr/CharStreamIOException;-><init>(Ljava/io/IOException;)V

    throw p1
.end method
