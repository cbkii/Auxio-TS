.class public Lcpdetector/io/InputStreamDebug;
.super Ljava/io/InputStream;
.source ""


# instance fields
.field public m_delegate:Ljava/io/InputStream;


# direct methods
.method public constructor <init>(Ljava/io/InputStream;)V
    .locals 0

    invoke-direct {p0}, Ljava/io/InputStream;-><init>()V

    iput-object p1, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    return-void
.end method


# virtual methods
.method public available()I
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0}, Ljava/io/InputStream;->available()I

    move-result p0

    return p0
.end method

.method public close()V
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0}, Ljava/io/InputStream;->close()V

    return-void
.end method

.method public equals(Ljava/lang/Object;)Z
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0, p1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public hashCode()I
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0}, Ljava/lang/Object;->hashCode()I

    move-result p0

    return p0
.end method

.method public mark(I)V
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0, p1}, Ljava/io/InputStream;->mark(I)V

    return-void
.end method

.method public markSupported()Z
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0}, Ljava/io/InputStream;->markSupported()Z

    move-result p0

    return p0
.end method

.method public declared-synchronized read()I
    .locals 3

    monitor-enter p0

    :try_start_0
    iget-object v0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {v0}, Ljava/io/InputStream;->read()I

    move-result v0

    sget-object v1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    int-to-char v2, v0

    invoke-virtual {v1, v2}, Ljava/io/PrintStream;->print(C)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    monitor-exit p0

    return v0

    :catchall_0
    move-exception v0

    monitor-exit p0

    throw v0
.end method

.method public read([B)I
    .locals 2

    array-length v0, p1

    const/4 v1, 0x0

    invoke-virtual {p0, p1, v1, v0}, Lcpdetector/io/InputStreamDebug;->read([BII)I

    move-result p0

    return p0
.end method

.method public read([BII)I
    .locals 0

    invoke-virtual {p0}, Lcpdetector/io/InputStreamDebug;->read()I

    move-result p0

    const/4 p3, -0x1

    if-eq p0, p3, :cond_0

    int-to-byte p0, p0

    aput-byte p0, p1, p2

    const/4 p0, 0x1

    :cond_0
    return p0
.end method

.method public reset()V
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0}, Ljava/io/InputStream;->reset()V

    return-void
.end method

.method public skip(J)J
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0, p1, p2}, Ljava/io/InputStream;->skip(J)J

    move-result-wide p0

    return-wide p0
.end method

.method public toString()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcpdetector/io/InputStreamDebug;->m_delegate:Ljava/io/InputStream;

    invoke-virtual {p0}, Ljava/lang/Object;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
