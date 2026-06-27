.class public Lcpdetector/io/LimitedInputStream;
.super Ljava/io/FilterInputStream;
.source ""


# instance fields
.field public m_amountOfBytesReadable:I


# direct methods
.method public constructor <init>(Ljava/io/InputStream;I)V
    .locals 0

    invoke-direct {p0, p1}, Ljava/io/FilterInputStream;-><init>(Ljava/io/InputStream;)V

    iput p2, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    return-void
.end method


# virtual methods
.method public available()I
    .locals 1

    iget v0, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    if-nez v0, :cond_0

    const/4 p0, 0x0

    goto :goto_0

    :cond_0
    invoke-super {p0}, Ljava/io/FilterInputStream;->available()I

    move-result v0

    iget p0, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    if-ge p0, v0, :cond_1

    goto :goto_0

    :cond_1
    move p0, v0

    :goto_0
    return p0
.end method

.method public read()I
    .locals 2

    iget v0, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    if-nez v0, :cond_0

    const/4 p0, -0x1

    goto :goto_0

    :cond_0
    invoke-super {p0}, Ljava/io/FilterInputStream;->read()I

    move-result v0

    if-ltz v0, :cond_1

    iget v1, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    add-int/lit8 v1, v1, -0x1

    iput v1, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    :cond_1
    move p0, v0

    :goto_0
    return p0
.end method

.method public read([BII)I
    .locals 1

    iget v0, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    if-nez v0, :cond_0

    const/4 p0, -0x1

    goto :goto_0

    :cond_0
    if-ge v0, p3, :cond_1

    move p3, v0

    :cond_1
    invoke-super {p0, p1, p2, p3}, Ljava/io/FilterInputStream;->read([BII)I

    move-result p1

    if-lez p1, :cond_2

    iget p2, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    sub-int/2addr p2, p1

    iput p2, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    :cond_2
    move p0, p1

    :goto_0
    return p0
.end method

.method public skip(J)J
    .locals 2

    iget v0, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    if-nez v0, :cond_0

    const-wide/16 p0, 0x0

    goto :goto_0

    :cond_0
    invoke-super {p0, p1, p2}, Ljava/io/FilterInputStream;->skip(J)J

    move-result-wide p1

    iget v0, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    int-to-long v0, v0

    sub-long/2addr v0, p1

    long-to-int v0, v0

    iput v0, p0, Lcpdetector/io/LimitedInputStream;->m_amountOfBytesReadable:I

    move-wide p0, p1

    :goto_0
    return-wide p0
.end method
