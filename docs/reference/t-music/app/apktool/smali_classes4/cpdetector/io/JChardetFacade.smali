.class public final Lcpdetector/io/JChardetFacade;
.super Lcpdetector/io/AbstractCodepageDetector;
.source ""

# interfaces
.implements Ld/a/a/a/q;


# static fields
.field public static det:Ld/a/a/a/h;

.field public static instance:Lcpdetector/io/JChardetFacade;


# instance fields
.field public amountOfVerifiers:I

.field public buf:[B

.field public codpage:Ljava/nio/charset/Charset;

.field public m_guessing:Z


# direct methods
.method public static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 2

    invoke-direct {p0}, Lcpdetector/io/AbstractCodepageDetector;-><init>()V

    const/16 v0, 0x1000

    new-array v0, v0, [B

    iput-object v0, p0, Lcpdetector/io/JChardetFacade;->buf:[B

    const/4 v0, 0x0

    iput-object v0, p0, Lcpdetector/io/JChardetFacade;->codpage:Ljava/nio/charset/Charset;

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcpdetector/io/JChardetFacade;->m_guessing:Z

    const/4 v0, 0x0

    iput v0, p0, Lcpdetector/io/JChardetFacade;->amountOfVerifiers:I

    new-instance v1, Ld/a/a/a/h;

    invoke-direct {v1, v0}, Ld/a/a/a/h;-><init>(I)V

    sput-object v1, Lcpdetector/io/JChardetFacade;->det:Ld/a/a/a/h;

    sget-object v0, Lcpdetector/io/JChardetFacade;->det:Ld/a/a/a/h;

    invoke-virtual {v0, p0}, Ld/a/a/a/h;->a(Ld/a/a/a/q;)V

    sget-object v0, Lcpdetector/io/JChardetFacade;->det:Ld/a/a/a/h;

    invoke-virtual {v0}, Ld/a/a/a/v;->ge()[Ljava/lang/String;

    move-result-object v0

    array-length v0, v0

    iput v0, p0, Lcpdetector/io/JChardetFacade;->amountOfVerifiers:I

    return-void
.end method

.method public static getInstance()Lcpdetector/io/JChardetFacade;
    .locals 1

    sget-object v0, Lcpdetector/io/JChardetFacade;->instance:Lcpdetector/io/JChardetFacade;

    if-nez v0, :cond_0

    new-instance v0, Lcpdetector/io/JChardetFacade;

    invoke-direct {v0}, Lcpdetector/io/JChardetFacade;-><init>()V

    sput-object v0, Lcpdetector/io/JChardetFacade;->instance:Lcpdetector/io/JChardetFacade;

    :cond_0
    sget-object v0, Lcpdetector/io/JChardetFacade;->instance:Lcpdetector/io/JChardetFacade;

    return-object v0
.end method

.method private guess()Ljava/nio/charset/Charset;
    .locals 4

    sget-object v0, Lcpdetector/io/JChardetFacade;->det:Ld/a/a/a/h;

    invoke-virtual {v0}, Ld/a/a/a/v;->ge()[Ljava/lang/String;

    move-result-object v0

    array-length v1, v0

    iget p0, p0, Lcpdetector/io/JChardetFacade;->amountOfVerifiers:I

    if-ne v1, p0, :cond_0

    const-string p0, "US-ASCII"

    invoke-static {p0}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    goto :goto_2

    :cond_0
    const/4 p0, 0x0

    aget-object v1, v0, p0

    const-string v2, "nomatch"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object p0

    goto :goto_2

    :cond_1
    const/4 v1, 0x0

    move-object v3, v1

    move v1, p0

    move-object p0, v3

    :goto_0
    if-nez p0, :cond_2

    array-length v2, v0

    if-ge v1, v2, :cond_2

    :try_start_0
    aget-object p0, v0, v1

    invoke-static {p0}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_0
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    aget-object p0, v0, v1

    invoke-static {p0}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_1
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_2
    :goto_2
    return-object p0
.end method


# virtual methods
.method public Notify(Ljava/lang/String;)V
    .locals 0

    invoke-static {p1}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p1

    iput-object p1, p0, Lcpdetector/io/JChardetFacade;->codpage:Ljava/nio/charset/Charset;

    return-void
.end method

.method public Reset()V
    .locals 1

    sget-object v0, Lcpdetector/io/JChardetFacade;->det:Ld/a/a/a/h;

    invoke-virtual {v0}, Ld/a/a/a/v;->Reset()V

    const/4 v0, 0x0

    iput-object v0, p0, Lcpdetector/io/JChardetFacade;->codpage:Ljava/nio/charset/Charset;

    return-void
.end method

.method public declared-synchronized detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;
    .locals 6

    monitor-enter p0

    :try_start_0
    invoke-virtual {p0}, Lcpdetector/io/JChardetFacade;->Reset()V

    const/4 v0, 0x0

    move v1, v0

    move v2, v1

    :cond_0
    iget-object v3, p0, Lcpdetector/io/JChardetFacade;->buf:[B

    array-length v4, v3

    sub-int v5, p2, v1

    invoke-static {v4, v5}, Ljava/lang/Math;->min(II)I

    move-result v4

    invoke-virtual {p1, v3, v0, v4}, Ljava/io/InputStream;->read([BII)I

    move-result v3

    if-lez v3, :cond_1

    add-int/2addr v1, v3

    :cond_1
    if-nez v2, :cond_2

    sget-object v2, Lcpdetector/io/JChardetFacade;->det:Ld/a/a/a/h;

    iget-object v4, p0, Lcpdetector/io/JChardetFacade;->buf:[B

    invoke-virtual {v2, v4, v3, v0}, Ld/a/a/a/h;->b([BIZ)Z

    move-result v2

    :cond_2
    if-lez v3, :cond_3

    if-eqz v2, :cond_0

    :cond_3
    sget-object p1, Lcpdetector/io/JChardetFacade;->det:Ld/a/a/a/h;

    invoke-virtual {p1}, Ld/a/a/a/v;->fe()V

    iget-object p1, p0, Lcpdetector/io/JChardetFacade;->codpage:Ljava/nio/charset/Charset;

    if-nez p1, :cond_5

    iget-boolean p1, p0, Lcpdetector/io/JChardetFacade;->m_guessing:Z

    if-eqz p1, :cond_4

    invoke-direct {p0}, Lcpdetector/io/JChardetFacade;->guess()Ljava/nio/charset/Charset;

    move-result-object p1

    goto :goto_0

    :cond_4
    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object p1
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    :cond_5
    :goto_0
    monitor-exit p0

    return-object p1

    :catchall_0
    move-exception p1

    monitor-exit p0

    throw p1
.end method

.method public isGuessing()Z
    .locals 0

    iget-boolean p0, p0, Lcpdetector/io/JChardetFacade;->m_guessing:Z

    return p0
.end method

.method public declared-synchronized setGuessing(Z)V
    .locals 0

    monitor-enter p0

    :try_start_0
    iput-boolean p1, p0, Lcpdetector/io/JChardetFacade;->m_guessing:Z
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    monitor-exit p0

    return-void

    :catchall_0
    move-exception p1

    monitor-exit p0

    throw p1
.end method
